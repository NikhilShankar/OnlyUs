package com.kishknkoopz.onlyus.ui.pages.dare

import android.graphics.BlurMaskFilter
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.kishknkoopz.onlyus.R
import com.kishknkoopz.onlyus.application.OUApplicationHelper
import com.kishknkoopz.onlyus.ui.activity.OUMainViewModel
import com.kishknkoopz.onlyus.ui.states.OUGameData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DareScreen(
    gameState: OUGameData,
    mainViewModel: OUMainViewModel
) {
    val dareState = mainViewModel.dareFlow.collectAsState()
    Log.i("NIKHIL-1234", "ABC - 0")
    var isDareSelected by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Log.i("NIKHIL-1234", "ABC")

        //LOWER PART OF GAME PLAY
        AnimatedVisibility(isDareSelected, label = "", enter = fadeIn(initialAlpha = 0f), exit = fadeOut()) {
            dareState.value?.getDisplayText()?.let {
                Log.i("NIKHIL-1234", "text : $it")
                Text(it, color = Color.White)
            }
        }
    }
    AnimatedVisibility(!isDareSelected, label = "", enter = fadeIn(initialAlpha = 0f), exit = fadeOut()) {
        DareScreenSelectionScreen(
            modifier = Modifier.fillMaxSize(),
            gameState = gameState,
            onClickHandle = {
                mainViewModel.getNextDare(userConfig = gameState.userConfig, partners = gameState.partners)
                isDareSelected = true
            })
    }
}

@Composable
fun DareScreenSelectionScreen(
    modifier: Modifier,
    gameState: OUGameData,
    onClickHandle: () -> Unit
) {
    val cardSize = IntSize(90.dp.value.toInt(), 160.dp.value.toInt())
    val scrollState = rememberScrollState()
    val itemList = List(100) { index -> "Item ${index + 1}" }
    val screenSize = remember {
        mutableStateOf(IntSize(0, 0))
    }
    val selectedCardTopLeftOffset: MutableState<Offset> = remember {
        mutableStateOf(Offset((screenSize.value.width/2 - cardSize.width/2).toFloat(), 0f))
    }


    Box(modifier = modifier
        .fillMaxSize()
        .onSizeChanged {
            screenSize.value = it

        }) {

        Text(text = "${gameState.partners.getPlayingPartnerName()}, Choose a card")
        val animationCoroutineScope = rememberCoroutineScope()
        val lazyRowScrollState = rememberLazyListState()
        LazyRow(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.BottomCenter)
            .padding(start = 0.dp, end = 0.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                (-32).dp
            ), verticalAlignment = Alignment.Bottom, state = lazyRowScrollState
        ) {
            items(itemList.size, itemContent = {
                Box {
                    var offsetY =remember { mutableStateOf(0f) }
                    var offsetX =remember { mutableStateOf(0f) }
                    var currentPositionOnScreen by remember {
                        mutableStateOf(Offset.Zero)
                    }

                    val screenSize = remember {
                        mutableStateOf(IntSize(0, 0))
                    }
                    Box(modifier = Modifier
                        .wrapContentSize()
                        .offset {
                            IntOffset(
                                offsetX.value.roundToInt(),
                                offsetY.value.roundToInt()
                            )
                        }
                        .onGloballyPositioned {
                            currentPositionOnScreen = it.positionInRoot()
                        }
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState(onDelta = {
                                offsetY.value += it.toInt()
                            }), onDragStopped = {
                                if (offsetY.value.absoluteValue > screenSize.value.height) {
                                    Log.i("NIKHIL-123", "offsetY: ${offsetY.value}")
                                    onClickHandle()
                                } else {
                                    Log.i("NIKHIL-1234", "offsetY: ${offsetY.value}")
                                    animationCoroutineScope.launch {
                                        launch {
                                            animate(
                                                initialValue = offsetX.value,
                                                targetValue = selectedCardTopLeftOffset.value.x,
                                                initialVelocity = it,
                                                animationSpec = tween(
                                                    durationMillis = 1000
                                                )
                                            ) { value, _ ->
                                                offsetX.value = value

                                            }
                                        }
                                        launch {
                                            animate(
                                                initialValue = offsetY.value,
                                                targetValue = selectedCardTopLeftOffset.value.y,
                                                initialVelocity = it,
                                                animationSpec = tween(
                                                    durationMillis = 1000
                                                )
                                            ) { value, _ ->
                                                offsetY.value = value
                                            }
                                        }
                                    }
                                }
                            }
                        )
                        .onSizeChanged {
                            screenSize.value = it
                            Log.i("NIKHIL-1234", "screenSize: ${screenSize.value}")

                        }
                    ) {
                        Image(
                             (ResourcesCompat.getDrawable(
                                OUApplicationHelper.getAppContext().resources,
                                R.drawable.onlu_us_card_min,
                                null
                            ) as BitmapDrawable).bitmap.asImageBitmap(),
                            contentDescription = null
                        )
                    }
                }
            })
        }

    }
}


