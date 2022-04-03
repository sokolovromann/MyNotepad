package ru.sokolovromann.mynotepad.data.local.settings

enum class NotesSyncPeriod(val millis: Long) {
    ONE_HOUR(3600000L),
    THREE_HOURS(10800000L),
    FIVE_HOURS(18000000L);

    companion object {
        fun millisOf(millis: Long): NotesSyncPeriod = when (millis) {
            ONE_HOUR.millis -> ONE_HOUR
            THREE_HOURS.millis -> THREE_HOURS
            FIVE_HOURS.millis -> FIVE_HOURS
            else -> THREE_HOURS
        }
    }
}