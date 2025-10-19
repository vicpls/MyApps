package com.rs.myapps.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface StringValue {

    @JvmInline
    value class  Text(val value: String) : StringValue

    object Empty : StringValue

    class ResId(
        @field:StringRes val resId: Int,
        vararg val args: Any
    ) : StringValue

    fun asString(context: Context?): String {
        return when(this) {
            is Empty -> ""
            is Text -> value
            is ResId -> context?.getString(resId, *args).orEmpty()
        }
    }

    @Suppress("unused")
    @Composable
    fun asString(): String {
        return when(this) {
            is Empty -> ""
            is Text -> value
            is ResId -> stringResource(resId, *args)
        }
    }
}

@Suppress("unused")
fun String.toStringValue() = StringValue.Text(this)
@Suppress("unused")
fun @receiver:StringRes Int.toStringValue() = StringValue.ResId(this)

@Suppress("unused")
fun String?.toStringValue(defIfNull: String = "null"): StringValue =
    this?.let { StringValue.Text(this) } ?: StringValue.Text(defIfNull)

@Suppress("unused")
fun String?.toStringValue(@StringRes defIfNull: Int): StringValue =
    this?.let { StringValue.Text(this) } ?: StringValue.ResId(defIfNull)