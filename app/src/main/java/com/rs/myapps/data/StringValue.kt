package com.rs.myapps.data

import android.content.Context
import androidx.annotation.StringRes

sealed class StringValue {
    class DynamicString(val value: String?) : StringValue()

    object Empty : StringValue()

    class StringResource(
        @field:StringRes val resId: Int,
        vararg val args: Any
    ) : StringValue()

    fun asString(context: Context?): String {
        return when (this) {
            is Empty -> ""
            is DynamicString -> value ?: "null"
            is StringResource -> context?.getString(resId, *args).orEmpty()
        }
    }
}