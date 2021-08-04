package fr.feepin.devfinder.ui.addproject

sealed class AddProjectEvent {
    object ProjectAdded : AddProjectEvent()
}