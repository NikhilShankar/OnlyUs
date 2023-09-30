package com.kishknkoopz.onlyus.ui.wheel

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.atan2

@Composable
fun OnlyUsWheelContainer(minSize: Dp = 64.dp,
                         modifier: Modifier,
                         onAnimationStartedListener: (() -> Unit)? = null,
                         onAnimationEndedListener: (() -> Unit)? = null) {
    var angle by remember{mutableStateOf(0f)}
    var dragStartedAngle by remember { mutableStateOf(0f) }
    var oldAngle by remember { mutableStateOf(angle) }
    var animationDrivingCoroutineScope = rememberCoroutineScope()
    var dragStartedTime = remember { mutableStateOf(0L) }
    var dragStartPosition = remember { mutableStateOf(Offset.Zero) }
    var dragEndPosition = remember { mutableStateOf(Offset.Zero) }

    var shouldAllowDrag = remember { mutableStateOf(true) }
    var velocity = remember {
        mutableStateOf(0f)
    }

    BoxWithConstraints(modifier.pointerInput(true) {
        if(shouldAllowDrag.value) {
            detectDragGestures(onDragStart = { offset ->
                dragStartedTime.value = System.currentTimeMillis()
                dragStartPosition.value = offset
                dragStartedAngle = (atan2(
                    y = size.center.x - offset.x,
                    x = size.center.y - offset.y
                ) * (180f / Math.PI.toFloat()) * -1).toFloat()
            }, onDragEnd = {
                oldAngle = angle
                animationDrivingCoroutineScope.launch {
                    val dragTime = System.currentTimeMillis() - dragStartedTime.value
                    val dragAmount = dragStartPosition.value - dragEndPosition.value
                    velocity.value = dragAmount.getDistance() / dragTime
                    shouldAllowDrag.value = false
                }
            }) { change, dragAmount ->

                val touchAngle = atan2(
                    y = size.center.x - change.position.x,
                    x = size.center.y - change.position.y
                ) * (180f / Math.PI.toFloat()) * -1

                angle = (oldAngle + (touchAngle - dragStartedAngle)) % 360
                dragEndPosition.value = change.position
            }
        }
    }) {
        InnerWheel(minSize, modifier = modifier, angle = angle,
            velocity = velocity.value,
            onAnimationEnded = {
                shouldAllowDrag.value = true
            })
    }
}




@Composable
fun InnerWheel(
    minSize: Dp = 64.dp,
    modifier: Modifier,
    angle: Float,
    velocity: Float?,
    onAnimationEnded: () -> Unit
    ) {
    BoxWithConstraints() {

        var animateAngle = remember {
            Animatable(angle)
        }

        val width = if (minWidth < 1.dp)
            minSize else minWidth

        val height = if (minHeight < 1.dp)
            minSize else minHeight

        if(velocity != null) {
            LaunchedEffect(key1 = velocity) {
                if (velocity != 0f) {
                    try {
                        animateAngle.snapTo(animateAngle.value)
                        val result = animateAngle.animateTo(
                            velocity * 360f,
                            animationSpec = tween(1000 * velocity.coerceIn(1.0f, 5.0f).toInt())
                        )
                        if (result.endReason == AnimationEndReason.Finished) {
                            onAnimationEnded()
                        }
                    } catch (e: Exception) {
                        onAnimationEnded()
                    }
                }
            }
        }

        Canvas(
            modifier = modifier
                .size(width, height)
        ) {
            val radius = size.width * 0.75f
            val center = Offset(size.width / 2.0f, size.height / 1.2f)
            rotate(
                    animateAngle.value, pivot = center) {
                drawCircle(
                    brush = Brush.horizontalGradient(colors = listOf(Color.Yellow, Color.Green)),
                    radius = radius,
                    center = center
                )
            }
        }
    }
}
