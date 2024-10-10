package com.example.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetContactById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
    @Inject
    constructor(
        private val getContactById: GetContactById,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _state: MutableStateFlow<DetailsUiState> = MutableStateFlow(DetailsUiState.Loading)
        val state: StateFlow<DetailsUiState>
            get() = _state

        init {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val contactIndex = requireNotNull(savedStateHandle.get<Int>("index"))
                    val contactUi = getContactById(contactIndex)?.toUi()
                    withContext(Dispatchers.Main) {
                        _state.value =
                            if (contactUi != null) {
                                DetailsUiState.Contact(contactUi)
                            } else {
                                DetailsUiState.Error
                            }
                    }
                }
            }
        }
    }
