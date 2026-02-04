package com.zsasko.rawg_kmp.common

import androidx.core.text.HtmlCompat

fun String.stripHtmlTags(): String {
    return HtmlCompat.fromHtml(
        this,
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()
}