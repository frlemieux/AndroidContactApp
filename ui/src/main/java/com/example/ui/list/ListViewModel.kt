package com.example.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.domain.usecase.GetPagingDataContact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ListViewModel
    @Inject
    constructor(
        private val getPagingDataContact: GetPagingDataContact,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")
        private val search = MutableStateFlow(searchQuery.value)

        fun onSearchQueryChanged(query: String) {
            savedStateHandle[SEARCH_QUERY] = query
        }

        fun onSearchTriggered(query: String) {
            search.update {
                query
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        val pagingData =
            search.flatMapLatest {
                searchInPagingDataContact(query = it)
            }

        private fun searchInPagingDataContact(query: String): Flow<PagingData<ContactUi>> =
            getPagingDataContact(query = query)
                .cachedIn(viewModelScope)
                .map {
                    it.map {
                        it.toUi()
                    }
                }.distinctUntilChanged()
    }

private const val SEARCH_QUERY = "searchQuery"
