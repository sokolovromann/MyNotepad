package ru.sokolovromann.mynotepad.screens

interface ScreensEvent<T> {

    fun onEvent(event: T)
}