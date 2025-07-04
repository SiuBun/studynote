# lib-compose 模块

这是一个独立的Compose库模块，用于在Android项目中使用Jetpack Compose。

## 模块结构

```
lib-compose/
├── build.gradle.kts          # 模块构建配置
├── proguard-rules.pro        # ProGuard规则
├── consumer-rules.pro        # 消费者ProGuard规则
├── src/
│   └── main/
│       ├── AndroidManifest.xml
│       └── java/
│           └── com/
│               └── wsb/
│                   └── compose/
│                       └── ComposeExample.kt  # 示例Compose组件
└── README.md
```

## 功能特性

- 包含最新的Jetpack Compose依赖
- 提供Material 3设计系统支持
- 包含Compose工具和预览功能
- 独立的模块结构，便于管理和复用

## 使用方法

### 1. 在app模块中添加依赖

```kotlin
dependencies {
    implementation(project(":lib-compose"))
}
```

### 2. 在Activity中使用Compose

```kotlin
class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ComposeExample()
                }
            }
        }
    }
}
```

### 3. 创建自定义Compose组件

在`lib-compose/src/main/java/com/wsb/compose/`目录下创建新的Compose组件：

```kotlin
@Composable
fun MyCustomComponent() {
    // 你的Compose代码
}
```

## 依赖版本

- Compose BOM: 2024.12.01
- Compose Compiler: 1.5.4
- Activity Compose: 1.9.2
- Lifecycle ViewModel Compose: 2.8.7

## 注意事项

1. 确保项目使用的Kotlin版本与Compose兼容
2. 在使用Compose的Activity中，需要启用Compose功能
3. 推荐使用Material 3设计系统
4. 利用Compose预览功能进行快速开发和调试 