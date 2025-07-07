package com.wsb.customview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ==============================================================================
 * 正确的MVI架构分层示例
 * ==============================================================================
 */

// 1. 数据层 (Data Layer)
data class ListItem(
    val id: Int,
    val title: String,
    val description: String,
    val isLoading: Boolean = false
)

// 2. 领域层 (Domain Layer) - MVI Contract
sealed class ListIntent {
    object RefreshAll : ListIntent()
    object AddItem : ListIntent()
    data class DeleteItem(val id: Int) : ListIntent()
    data class UpdateItem(val id: Int) : ListIntent()
}

data class ListState(
    val items: List<ListItem> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false
)

// 3. 表现层 (Presentation Layer) - ViewModel
class ListViewModel : ViewModel() {
    private val _state = MutableStateFlow(ListState())
    val state: StateFlow<ListState> = _state.asStateFlow()
    
    private var nextId = 1
    
    init {
        _state.value = _state.value.copy(items = generateInitialItems())
    }
    
    fun handleIntent(intent: ListIntent) {
        when (intent) {
            is ListIntent.RefreshAll -> refreshAll()
            is ListIntent.AddItem -> addItem()
            is ListIntent.DeleteItem -> deleteItem(intent.id)
            is ListIntent.UpdateItem -> updateItem(intent.id)
        }
    }
    
    private fun refreshAll() {
        _state.value = _state.value.copy(isRefreshing = true)
        kotlinx.coroutines.GlobalScope.launch {
            delay(1000)
            _state.value = _state.value.copy(
                items = generateInitialItems(),
                isRefreshing = false
            )
        }
    }
    
    private fun addItem() {
        val newItem = ListItem(
            id = nextId++,
            title = "新项目 $nextId",
            description = "这是一个新添加的项目"
        )
        _state.value = _state.value.copy(items = _state.value.items + newItem)
    }
    
    private fun deleteItem(id: Int) {
        _state.value = _state.value.copy(
            items = _state.value.items.filter { it.id != id }
        )
    }
    
    private fun updateItem(id: Int) {
        val currentItems = _state.value.items.toMutableList()
        val index = currentItems.indexOfFirst { it.id == id }
        
        if (index != -1) {
            currentItems[index] = currentItems[index].copy(isLoading = true)
            _state.value = _state.value.copy(items = currentItems)
            
            kotlinx.coroutines.GlobalScope.launch {
                delay(800)
                val updatedItems = _state.value.items.toMutableList()
                val updateIndex = updatedItems.indexOfFirst { it.id == id }
                if (updateIndex != -1) {
                    updatedItems[updateIndex] = updatedItems[updateIndex].copy(
                        title = "已更新 ${System.currentTimeMillis() % 1000}",
                        description = "这个项目已经被更新了",
                        isLoading = false
                    )
                    _state.value = _state.value.copy(items = updatedItems)
                }
            }
        }
    }
    
    private fun generateInitialItems(): List<ListItem> {
        return (1..10).map { i ->
            ListItem(
                id = i,
                title = "项目 $i",
                description = "这是项目 $i 的描述信息"
            )
        }.also { nextId = 11 }
    }
}

// 4. UI层 (UI Layer) - 纯函数组件
/**
 * 核心列表组件 - 纯函数，不依赖任何具体实现
 * 
 * 优点：
 * - 易于测试：可以传入任意State进行测试
 * - 可复用：可以与不同的ViewModel实现配合
 * - 解耦：不知道具体的ViewModel实现
 * - 符合MVI原则：只关心State和Intent处理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    state: ListState,
    onIntent: (ListIntent) -> Unit
) {
    val listState = rememberLazyListState()
    
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("MVI 列表示例") },
            actions = {
                IconButton(onClick = { onIntent(ListIntent.RefreshAll) }) {
                    Icon(Icons.Default.Refresh, contentDescription = "全量刷新")
                }
                IconButton(onClick = { onIntent(ListIntent.AddItem) }) {
                    Icon(Icons.Default.Add, contentDescription = "添加项目")
                }
            }
        )
        
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.items, key = { it.id }) { item ->
                    ListItemCard(
                        item = item,
                        onUpdate = { onIntent(ListIntent.UpdateItem(item.id)) },
                        onDelete = { onIntent(ListIntent.DeleteItem(item.id)) }
                    )
                }
            }
            
            if (state.isRefreshing) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("正在刷新...")
                        }
                    }
                }
            }
        }
    }
}

/**
 * 列表项组件 - 纯函数
 */
@Composable
fun ListItemCard(
    item: ListItem,
    onUpdate: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (item.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Row {
                    IconButton(onClick = onUpdate) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "更新",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "删除",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}

/**
 * 容器组件 - 连接ViewModel和UI
 * 这个组件负责管理ViewModel的生命周期和状态订阅
 */
@Composable
fun ListScreenContainer(
    modifier: Modifier = Modifier,
    vm: ListViewModel = viewModel()  // ✅ 使用完整路径避免命名冲突
) {
    val state by vm.state.collectAsState()
    
    ListScreen(
        modifier = modifier,
        state = state,
        onIntent = vm::handleIntent
    )
}

/**
 * 临时备选方案 - 如果viewModel()还没同步，可以用这个
 */
@Composable
fun ListScreenContainerTemp(
    modifier: Modifier = Modifier
) {
    val vm = remember { ListViewModel() }  // ✅ 临时使用remember
    val state by vm.state.collectAsState()
    
    ListScreen(
        modifier = modifier,
        state = state,
        onIntent = vm::handleIntent
    )
}

/**
 * 备选方案：使用不同的参数名避免冲突
 */
@Composable
fun ListScreenContainer2(
    modifier: Modifier = Modifier,
    listViewModel: ListViewModel = viewModel()  // ✅ 参数名不同，避免冲突
) {
    val state by listViewModel.state.collectAsState()
    
    ListScreen(
        modifier = modifier,
        state = state,
        onIntent = listViewModel::handleIntent
    )
}

/**
 * 备选方案：使用remember手动管理
 */
@Composable
fun ListScreenContainer3(
    modifier: Modifier = Modifier
) {
    val viewModel = remember { ListViewModel() }  // ✅ 使用remember手动缓存
    val state by viewModel.state.collectAsState()
    
    ListScreen(
        modifier = modifier,
        state = state,
        onIntent = viewModel::handleIntent
    )
}

/**
 * 使用示例 - 在Activity中的正确用法
 */
@Composable
fun ListScreenExample() {
    // 方式1：使用容器组件（推荐用于简单场景）
    ListScreenContainer()
    
    // 方式2：手动管理ViewModel（推荐用于复杂场景）
    // val viewModel: ListViewModel = viewModel()
    // val state by viewModel.state.collectAsState()
    // ListScreen(state = state, onIntent = viewModel::handleIntent)
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    MaterialTheme {
        ListScreenContainer()
    }
}

/**
 * ==============================================================================
 * 架构层次说明：
 * 
 * 1. Data Layer: ListItem (数据模型)
 * 2. Domain Layer: ListIntent, ListState (业务契约)
 * 3. Presentation Layer: ListViewModel (业务逻辑)
 * 4. UI Layer: ListScreen (纯UI组件)
 * 5. Container Layer: ListScreenContainer (连接层)
 * 
 * 优点：
 * - 各层职责清晰，易于维护
 * - UI组件可单独测试
 * - ViewModel可单独测试
 * - 符合SOLID原则
 * - 易于替换不同的实现
 * ============================================================================== 
 */ 