package com.wsb.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 示例Compose组件
 */
@Composable
fun ComposeExample(modifier: Modifier = Modifier) {
    var count by remember { mutableStateOf(0) }
    
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello Compose!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        
        Text(
            text = "Count: $count",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
        
        Button(
            onClick = { count++ },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Click me")
        }

        OutlinedButton(
            onClick = { count++ },
            modifier = Modifier.padding(16.dp).background(Color.LightGray)
        ) {
            Text("Click me")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComposeExamplePreview() {
    MaterialTheme {
        Surface {
            ComposeExample()
        }
    }
} 