package com.wsb.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PracticeWidget(modifier: Modifier = Modifier) {
    var textFieldValue by remember { mutableStateOf("") }
    var checkboxState by remember { mutableStateOf(false) }
    var switchState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "🚀 Compose 组件指南",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 文本组件
        Text("标题文本", style = MaterialTheme.typography.titleLarge)
        Text("正文文本", style = MaterialTheme.typography.bodyMedium)
        Text("小号文本", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        // 按钮组件
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { }) { Text("按钮") }
            OutlinedButton(onClick = { }) { Text("边框按钮") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 输入组件
        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("输入框") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 选择组件
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = checkboxState, onCheckedChange = { checkboxState = it })
            Text("复选框")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("开关")
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = switchState, onCheckedChange = { switchState = it })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 布局示例
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.3f))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("项目1")
            Text("项目2")
            Text("项目3")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 卡片组件
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("卡片标题", style = MaterialTheme.typography.titleMedium)
                Text("卡片内容", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 列表组件
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(5) { index ->
                Card(
                    modifier = Modifier.size(80.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Cyan.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$index")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PracticeWidgetPreview() {
    MaterialTheme {
        Surface {
            PracticeWidget()
        }
    }
}