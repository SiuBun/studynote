package com.wsb.customview.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wsb.customview.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

    private val _listData = MutableStateFlow<List<ListData>>(emptyList())
    val listData: StateFlow<List<ListData>> = _listData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 0

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000) // 模拟网络延迟
            currentPage = 0
            val newData = generateData(currentPage)
            _listData.value = newData
            _isLoading.value = false
        }
    }

    fun loadMore() {
        if (_isLoading.value) return
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000) // 模拟网络延迟
            currentPage++
            val moreData = generateData(currentPage)
            _listData.value = _listData.value + moreData
            _isLoading.value = false
        }
    }

    private fun generateData(page: Int): List<ListData> {
        val data = mutableListOf<ListData>()
        val start = page * 20

        val res = listOf(
            R.drawable.batman,
            R.drawable.batman_logo,
            R.drawable.icon,
            R.drawable.what_the_fuck,
            R.drawable.maps,
        )
        for (i in start until start + 20) {
            val random = (0 until  res.size).random()
            data.add(
                ListData(
                    i.toLong(),
                    res[random],
                    "这是第 $i 条数据，内容长度随机，这是一个为了测试而生的文本，长度会在30到300字之间变化。".repeat((1..5).random())
                )
            )
        }
        return data
    }
}