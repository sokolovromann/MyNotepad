package ru.sokolovromann.mynotepad.screens.settings.state

import ru.sokolovromann.mynotepad.data.local.settings.NotesSyncPeriod

data class SettingsSyncPeriodDialogState(
    val showDialog: Boolean,
    val selected: NotesSyncPeriod
) {

    companion object {
        val Default = SettingsSyncPeriodDialogState(
            showDialog = false,
            selected = NotesSyncPeriod.THREE_HOURS
        )
    }
}