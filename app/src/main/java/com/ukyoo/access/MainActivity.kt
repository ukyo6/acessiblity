package com.ukyoo.access

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils.SimpleStringSplitter
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var mContext: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDy = findViewById<Button>(R.id.btn_hb_dy)
        btnDy.setOnClickListener(this)

        val btnWx = findViewById<Button>(R.id.btn_hb_wx)
        btnWx.setOnClickListener(this)



    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_hb_dy -> {
                // 判断服务是否开启
                val isOpen = isAccessibilitySettingsOn(this, DYHBService::class.java.name)
                if (!isOpen) {
                    AlertDialog.Builder(mContext)
                        .setTitle("无障碍服务申请说明")
                        .setMessage("需要开启 无障碍服务 以达到模拟点击的效果.请在接下来的页面开启 豆芽_红包选项")
                        .setNegativeButton("确定") { dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            startActivity(intent)
                        }
                        .setPositiveButton("取消") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                } else {
                    startService(Intent(mContext, DYHBService::class.java))
                    Toast.makeText(mContext, "服务已开启", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.btn_hb_wx -> {
                // 判断服务是否开启
                val isOpen = isAccessibilitySettingsOn(this, WXHBService::class.java.name)
                if (!isOpen) {
                    AlertDialog.Builder(mContext)
                        .setTitle("无障碍服务申请说明")
                        .setMessage("需要开启 无障碍服务 以达到模拟点击的效果.请在接下来的页面开启 微信_红包选项")
                        .setNegativeButton("确定") { dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            startActivity(intent)
                        }
                        .setPositiveButton("取消") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                } else {
                    startService(Intent(mContext, WXHBService::class.java))
                    Toast.makeText(mContext, "服务已开启", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

    private fun isAccessibilitySettingsOn(context: Context?, className: String): Boolean {
        if (context == null) {
            return false
        }
        var accessibilityEnable: Int = 0
        val serviceName: String = context.packageName + "/" + className
        try {
            accessibilityEnable =
                    Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)
        } catch (e: Exception) {
            Log.e("mainActivity", "get accessibility enable failed, the err:" + e.message)
        }
        if (accessibilityEnable == 1) {
            val mStringColonSplitter = SimpleStringSplitter(':')
            val settingValue =
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)

            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService: String = mStringColonSplitter.next()
                    if (accessibilityService.equals(serviceName, true)) {
                        Log.v("mainActivity", "We've found the correct setting - accessibility is switched on!");
                        return true
                    }
                }
            }
        } else {
            Log.d("mainActivity", "Accessibility service disable")
        }
        return false
    }
}
