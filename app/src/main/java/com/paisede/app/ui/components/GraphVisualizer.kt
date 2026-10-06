package com.paisede.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.paisede.app.domain.algorithm.DebtEdge
import com.paisede.app.domain.model.Member
import com.paisede.app.ui.theme.BrandTeal
import com.paisede.app.ui.theme.DebtRed

import com.paisede.app.util.CurrencyUtils
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GraphVisualizer(
    members: List<Member>,
    edges: List<DebtEdge>,
    modifier: Modifier = Modifier,
    highlightColor: Color = BrandTeal,
    isSimplified: Boolean = false
) {
    val memberMap = members.associateBy { it.id }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(340.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        if (members.isEmpty()) {
            EmptyStateView(
                title = "No Members",
                subtitle = "Add members to see the debt graph"
            )
            return@Surface
        }

        val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
        val surfaceColor = MaterialTheme.colorScheme.surface
        val outlineColor = MaterialTheme.colorScheme.outlineVariant
        val badgeBgColor = MaterialTheme.colorScheme.surfaceVariant.toArgb()
        val badgeTextColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

        Canvas(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            val width = size.width
            val height = size.height
            val centerX = width / 2f
            val centerY = height / 2f
            val radius = minOf(width, height) * 0.38f
            val nodeRadius = 24.dp.toPx()

            // 1. Calculate positions for each member evenly on a circle
            val nodePositions = mutableMapOf<String, Offset>()
            val n = members.size
            for (i in members.indices) {
                val angle = (2 * PI * i / n) - (PI / 2) // Start from top
                val x = centerX + radius * cos(angle).toFloat()
                val y = centerY + radius * sin(angle).toFloat()
                nodePositions[members[i].id] = Offset(x, y)
            }

            // 2. Draw directed edges
            val edgeColor = if (isSimplified) BrandTeal else DebtRed.copy(alpha = 0.8f)

            for (edge in edges) {
                val start = nodePositions[edge.fromUserId] ?: continue
                val end = nodePositions[edge.toUserId] ?: continue

                drawDirectedCurvedEdge(
                    start = start,
                    end = end,
                    nodeRadius = nodeRadius,
                    amountPaise = edge.amountPaise,
                    edgeColor = edgeColor,
                    badgeBgColor = badgeBgColor,
                    badgeTextColor = badgeTextColor
                )
            }

            // 3. Draw Nodes (circles with initial and name)
            for (member in members) {
                val pos = nodePositions[member.id] ?: continue

                // Node background circle
                drawCircle(
                    color = outlineColor,
                    radius = nodeRadius,
                    center = pos
                )
                drawCircle(
                    color = surfaceColor,
                    radius = nodeRadius - 2.dp.toPx(),
                    center = pos
                )

                // Member Name / Initial text
                val initial = member.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
                val textPaint = android.graphics.Paint().apply {
                    color = onSurfaceColor
                    textSize = 14.dp.toPx()
                    textAlign = android.graphics.Paint.Align.CENTER
                    isFakeBoldText = true
                    isAntiAlias = true
                }

                drawContext.canvas.nativeCanvas.drawText(
                    initial,
                    pos.x,
                    pos.y + 5.dp.toPx(),
                    textPaint
                )

                // Sub-caption with member's first name
                val namePaint = android.graphics.Paint().apply {
                    color = onSurfaceColor
                    textSize = 11.dp.toPx()
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                }

                val labelOffsetY = if (pos.y > centerY) nodeRadius + 14.dp.toPx() else -(nodeRadius + 4.dp.toPx())
                drawContext.canvas.nativeCanvas.drawText(
                    member.name.take(8),
                    pos.x,
                    pos.y + labelOffsetY,
                    namePaint
                )
            }
        }
    }
}

private fun DrawScope.drawDirectedCurvedEdge(
    start: Offset,
    end: Offset,
    nodeRadius: Float,
    amountPaise: Long,
    edgeColor: Color,
    badgeBgColor: Int,
    badgeTextColor: Int
) {
    val dx = end.x - start.x
    val dy = end.y - start.y
    val distance = kotlin.math.hypot(dx, dy)
    if (distance <= nodeRadius * 2) return

    val angle = atan2(dy, dx)

    // Truncate start and end to touch node perimeter
    val startX = start.x + nodeRadius * cos(angle)
    val startY = start.y + nodeRadius * sin(angle)
    val endX = end.x - nodeRadius * cos(angle)
    val endY = end.y - nodeRadius * sin(angle)

    // Arc offset perpendicular to the line
    val curvature = 20.dp.toPx()
    val midX = (startX + endX) / 2f - curvature * sin(angle)
    val midY = (startY + endY) / 2f + curvature * cos(angle)

    val path = Path().apply {
        moveTo(startX, startY)
        quadraticTo(midX, midY, endX, endY)
    }

    drawPath(
        path = path,
        color = edgeColor,
        style = Stroke(width = 2.5.dp.toPx())
    )

    // Arrowhead at end point
    val arrowSize = 10.dp.toPx()
    val tangentAngle = atan2(endY - midY, endX - midX)
    val arrowAngle1 = tangentAngle - PI.toFloat() / 6
    val arrowAngle2 = tangentAngle + PI.toFloat() / 6

    val arrowPath = Path().apply {
        moveTo(endX, endY)
        lineTo(
            endX - arrowSize * cos(arrowAngle1),
            endY - arrowSize * sin(arrowAngle1)
        )
        lineTo(
            endX - arrowSize * cos(arrowAngle2),
            endY - arrowSize * sin(arrowAngle2)
        )
        close()
    }
    drawPath(path = arrowPath, color = edgeColor)

    // Amount label at curve apex
    val labelText = CurrencyUtils.formatPaise(amountPaise)
    val textPaint = android.graphics.Paint().apply {
        color = badgeTextColor
        textSize = 10.dp.toPx()
        textAlign = android.graphics.Paint.Align.CENTER
        isFakeBoldText = true
        isAntiAlias = true
    }

    val bgPaint = android.graphics.Paint().apply {
        color = badgeBgColor
        isAntiAlias = true
    }

    val textBounds = android.graphics.Rect()
    textPaint.getTextBounds(labelText, 0, labelText.length, textBounds)
    val padding = 4.dp.toPx()

    drawContext.canvas.nativeCanvas.drawRoundRect(
        midX - textBounds.width() / 2f - padding,
        midY - textBounds.height() / 2f - padding,
        midX + textBounds.width() / 2f + padding,
        midY + textBounds.height() / 2f + padding,
        6f,
        6f,
        bgPaint
    )

    drawContext.canvas.nativeCanvas.drawText(
        labelText,
        midX,
        midY + textBounds.height() / 2f - 2.dp.toPx(),
        textPaint
    )
}
