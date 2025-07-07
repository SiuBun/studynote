package com.wsb.customview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 正确的MVI架构：在Activity层管理ViewModel，组件只接收State和Intent处理器
                    val viewModel: ListViewModel = viewModel()
                    val state by viewModel.state.collectAsState()
                    
                    ListScreen(
                        state = state,
                        onIntent = viewModel::handleIntent
                    )
                }
            }
        }
    }
} 