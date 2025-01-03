package com.vasberc.presentation.componets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.vasberc.presentation.R
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TextWithAnimatedRing(
    counter: Int,
    calculating: Boolean,
    onClick: () -> Unit
) {

    val infiniteTransition = if (calculating) rememberInfiniteTransition(label = "") else null
    val rotateAnimation = infiniteTransition?.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
        label = ""
    )

    Text(
        textAlign = TextAlign.Center,
        text = stringResource(R.string.counter, counter),
        modifier = Modifier
            .drawBehind {
                val rainbowColors = listOf(
                    Color.Red,
                    Color.Yellow,
                    Color.Green,
                    Color.Cyan,
                    Color.Blue,
                    Color.Magenta,
                    Color.Red
                )
                val radius = size.minDimension / 2
                val center = Offset(size.width / 2, size.height / 2)
                val strokeWidth = 10f
                drawCircle(Color.White)
                rotateAnimation?.value?.let { degrees ->
                    rotate(degrees = degrees) {
                        drawArc(
                            brush = Brush.sweepGradient(rainbowColors),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                } ?: run {
                    drawCircle(
                        color = Color.Black,
                        radius = radius,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }


            }
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(15.dp)

    )
}

@Preview(showBackground = true, backgroundColor = 0xFF625b71)
@Composable
fun PreviewTextWithAnimatedRing() {
    TextWithAnimatedRing(100, false, {})
}