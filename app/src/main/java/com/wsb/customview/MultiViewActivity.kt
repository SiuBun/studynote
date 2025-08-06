package com.wsb.customview

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsb.customview.databinding.ActivityMultiViewBinding

class MultiViewActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityMultiViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMultiViewBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // 伪数据
        val data = (1..100).map { "Item $it" }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = MyAdapter(data)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "Navigation clicked", Toast.LENGTH_SHORT).show()
        }
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeAsUpIndicator(android.R.drawable.ic_menu_info_details)

        binding.toolbar.title = "Multi View Example"

        binding.btn1.setOnClickListener {
            // 按钮1点击事件
            // 这里可以自定义逻辑
            Toast.makeText(this, "btn1", Toast.LENGTH_SHORT).show()
        }


        binding.img1.setOnClickListener {
            // 图片点击事件
            Toast.makeText(this, "img1", Toast.LENGTH_SHORT).show()
        }
        binding.tv1.setOnClickListener {
            // 文本点击事件
            Toast.makeText(this, "tv1", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        // setNavigationOnClickListener会处理返回键点击事件,所以这里再写不会触发
//        if (item.itemId == android.R.id.home) {
//            Toast.makeText(this, "on Options Item home Selected", Toast.LENGTH_SHORT).show()
//            return true
//        }
        return super.onOptionsItemSelected(item)
    }

    private class MyAdapter(private val data: List<String>) :
        RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
            val view = android.view.LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = data[position]
        }

        override fun getItemCount() = data.size
    }
}
