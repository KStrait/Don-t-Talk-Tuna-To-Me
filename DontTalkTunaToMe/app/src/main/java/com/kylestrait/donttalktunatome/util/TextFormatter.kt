package com.kylestrait.donttalktunatome.util

import android.text.Html

class TextFormatter {

    fun stripHtml(html: String): String {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(html).toString()
        }
    }

    fun stripChars(title: String): String {
        if (title.contains(":") && title.contains("(")) {
            return title.substringBefore(":")
        } else if (title.contains("(")) {
            return title.substring(title.indexOf("(") + 1, title.indexOf(")"))
        } else if (title.contains(":")) {
            return title.substringBefore(":")
        } else {
            return ""
        }
    }
}