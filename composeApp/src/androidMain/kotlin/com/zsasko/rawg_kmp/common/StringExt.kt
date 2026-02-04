package com.zsasko.rawg_kmp.common


fun String.stripHtmlTags(): String {
    return this.replace(Regex(STRIP_HTML_REGEX), "")
}