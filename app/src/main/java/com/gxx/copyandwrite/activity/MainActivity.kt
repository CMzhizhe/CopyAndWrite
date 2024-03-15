package com.gxx.copyandwrite.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.gxx.copyandwrite.R
import com.gxx.copyandwrite.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch(Dispatchers.Default) {
            //删除缓存的文件
           FileUtils.getInstance().deleteDirectory(this@MainActivity.cacheDir.absolutePath)
           FileUtils.getInstance().putAssetsToSDCard(this@MainActivity,"common")
            withContext(Dispatchers.Main){
                Toast.makeText(this@MainActivity,"操作完成",Toast.LENGTH_SHORT).show()
            }
        }

    }
}
