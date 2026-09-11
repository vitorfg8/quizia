package com.vitorfg8.quizia.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import com.vitorfg8.quizia.designsystem.QuiziaSizes

internal const val PHOSPHOR_VIEWPORT: Float = 256f
internal const val DUOTONE_SECONDARY_ALPHA: Float = 0.2f

internal data class PhosphorPath(
    val data: String,
    val alpha: Float = 1f,
)

internal fun buildPhosphorIcon(
    name: String,
    paths: List<PhosphorPath>,
    autoMirror: Boolean = false,
): ImageVector {
    return ImageVector.Builder(
        name = name,
        defaultWidth = QuiziaSizes().iconMedium,
        defaultHeight = QuiziaSizes().iconMedium,
        viewportWidth = PHOSPHOR_VIEWPORT,
        viewportHeight = PHOSPHOR_VIEWPORT,
        autoMirror = autoMirror,
    ).apply {
        paths.forEach { path ->
            addPath(
                pathData = PathParser().parsePathString(path.data).toNodes(),
                fill = SolidColor(Color.Black),
                fillAlpha = path.alpha,
            )
        }
    }.build()
}
