package com.ukyoo.kt

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var mContext: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var switchHb = findViewById<Button>(R.id.btn_hb)
        switchHb.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_hb -> {
                // 判断服务是否开启
                val isOpen = isAccessibilitySettingsOn(this, DYHBService::class.java.name)
                if (!isOpen) {
                    AlertDialog.Builder(mContext)
                        .setTitle("无障碍服务申请说明")
                        .setMessage("需要开启 无障碍服务 以达到模拟点击的效果.请在接下来的页面开启 豆芽_红包选项")
                        .setNegativeButton("确定") { dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            startActivityForResult(intent, 1)
                        }
                        .setPositiveButton("取消") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                } else {
                    startService(Intent(mContext, DYHBService::class.java))
                    Toast.makeText(mContext, "服务已开启", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isAccessibilitySettingsOn(context: Context?, className: String): Boolean {
        if (context == null) {
            return false
        }
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(100)// 获取正在运行的服务列表
        if (runningServices.size < 0) {
            return false
        }
        for (i in runningServices.indices) {
            val service = runningServices[i].service
            if (service.className == className) {
                return true
            }
        }
        return false
    }
}
