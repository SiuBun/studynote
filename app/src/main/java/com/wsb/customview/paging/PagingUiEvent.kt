package com.wsb.customview.paging

sealed class PagingUiEvent {
    data class ShowError(val message: String) : PagingUiEvent()
    data class ShowToast(val message: String) : PagingUiEvent()
    data class ShowEmpty(val message: String) : PagingUiEvent()
}