package fr.feepin.devfinder

sealed class MainEvent {
    data class GoToProfile(val userId: String): MainEvent()
}
