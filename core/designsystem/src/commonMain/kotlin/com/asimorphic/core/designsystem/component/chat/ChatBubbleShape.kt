package com.asimorphic.core.designsystem.component.chat

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class ChatBubbleShape(
    private val triangleAlign: TriangleAlign,
    private val triangleSize: Dp = 12.dp,
    private val cornerRadius: Dp = 16.dp
): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val triangleSizePx = with(receiver = density) { triangleSize.toPx() }
        val cornerRadiusPx = with(receiver = density) { cornerRadius.toPx() }

        val path = when (triangleAlign) {
            TriangleAlign.LEFT -> {
                val bodyPath = Path().apply {
                    addRoundRect(
                        roundRect = RoundRect(
                            left = triangleSizePx,
                            top = 0f,
                            right = size.width,
                            bottom = size.height,
                            cornerRadius = CornerRadius(
                                x = cornerRadiusPx,
                                y = cornerRadiusPx
                            )
                        )
                    )
                }

                val trianglePath = Path().apply {
                    moveTo(x = 0f, y = size.height)
                    lineTo(x = triangleSizePx, y = size.height - cornerRadiusPx)
                    lineTo(x = triangleSizePx + cornerRadiusPx, y = size.height)
                    close()
                }

                Path.combine(
                    operation = PathOperation.Union,
                    path1 = bodyPath,
                    path2 = trianglePath
                )
            }
            TriangleAlign.RIGHT -> {
                val bodyPath = Path().apply {
                    addRoundRect(
                        roundRect = RoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width - triangleSizePx,
                            bottom = size.height,
                            cornerRadius = CornerRadius(
                                x = cornerRadiusPx,
                                y = cornerRadiusPx
                            )
                        )
                    )
                }

                val trianglePath = Path().apply {
                    moveTo(x = size.width, y = size.height)
                    lineTo(x = size.width - triangleSizePx, y = size.height - cornerRadiusPx)
                    lineTo(x = size.width - triangleSizePx - cornerRadiusPx, y = size.height)
                    close()
                }

                Path.combine(
                    operation = PathOperation.Union,
                    path1 = bodyPath,
                    path2 = trianglePath
                )
            }
        }

        return Outline.Generic(path = path)
    }
}

enum class TriangleAlign {
    LEFT,
    RIGHT
}