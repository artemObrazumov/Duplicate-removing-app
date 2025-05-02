package com.artem_obrazumov.mycontacts.core.presentation.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class StatefulViewModel<S: State, A: Action, E: Effect>(
    initialState: S
): ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()

    protected suspend fun updateState(newState: S) {
        _state.value = newState
    }

    protected suspend fun updateEffect(effect: E) {
        _effect.emit(effect)
    }

    abstract fun onAction(action: A)
}

interface State

interface Action

interface Effect