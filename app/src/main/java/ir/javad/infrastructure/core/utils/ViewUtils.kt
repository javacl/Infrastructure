package ir.javad.infrastructure.core.utils

import android.content.res.Resources
import android.view.View
import androidx.core.view.ViewCompat
import java.text.NumberFormat
import java.util.*

object ViewUtils {

    fun directionView(view: View, isLtr: Boolean) {
        val direction: Int = if (isLtr) ViewCompat.LAYOUT_DIRECTION_LTR
        else ViewCompat.LAYOUT_DIRECTION_RTL
        ViewCompat.setLayoutDirection(view, direction)
    }

    fun dpToPx(dp: Float): Int {
        val density =
            Resources.getSystem().displayMetrics.density
        return Math.round(dp * density)
    }

    fun pxToDp(px: Float): Float {
        val densityDpi =
            Resources.getSystem().displayMetrics.densityDpi.toFloat()
        return px / (densityDpi / 160f)
    }

    fun numberFormat(amount: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(amount)
    }

    fun numberFormat(amount: Double): String {
        return NumberFormat.getNumberInstance(Locale.US).format(amount)
    }
}