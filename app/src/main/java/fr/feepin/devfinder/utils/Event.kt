package fr.feepin.devfinder.utils

class Event<T>(private val data: T) {

    private var discovered = false

    fun getData(): T? {
        return data.takeIf {
            !discovered
        }?.also { discovered = true }
    }

    fun peekData() = data
}