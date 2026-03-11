package com.asimorphic.chat.presentation.util

import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.today
import chirp.feature.chat.presentation.generated.resources.yesterday
import com.asimorphic.core.presentation.util.UiText
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
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

    fun formatDateSeparator (
        date: LocalDate,
        clock: Clock = Clock.System
    ): UiText {
        val timeZone = TimeZone.currentSystemDefault()
        val today = clock.now().toLocalDateTime(timeZone).date
        val yesterday = today.minus(value = 1, unit = DateTimeUnit.DAY)

        return when (date) {
            today -> UiText.Resource(Res.string.today)
            yesterday -> UiText.Resource(Res.string.yesterday)
            else -> {
                val formatted = date.format(
                    format = LocalDate.Format {
                        day()
                        char('/')
                        monthNumber()
                        char('/')
                        year()
                    }
                )
                UiText.DynamicString(formatted)
            }
        }
    }
}