package com.asimorphic.chat.presentation.util

import com.asimorphic.core.presentation.util.UiText
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

object DateTimeFormatter {

    fun formatMessageTime(
        instant: Instant,
    ): UiText {
        val timeZone = TimeZone.currentSystemDefault()
        val messageDateTime = instant.toLocalDateTime(timeZone = timeZone)

        val formattedTime = messageDateTime
            .format(
                format = LocalDateTime.Format {
                    amPmHour()
                    char(value = ':')
                    minute()
                    char(value = ' ')
                    amPmMarker(
                        am = "am",
                        pm = "pm"
                    )
                }
            )

        return UiText.DynamicString(value = formattedTime)
    }
}