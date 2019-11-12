package com.ukyoo.access

import android.accessibilityservice.AccessibilityService
import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class WXHBService : AccessibilityService() {
    override fun onInterrupt() {
    }

    //该对象代表了整个窗口视图的快照
    private var mRootNodeInfo: AccessibilityNodeInfo? = null

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        mRootNodeInfo = rootInActiveWindow
        if (mRootNodeInfo == null) {
            return
        }



        //如果当前的事件类型是窗口内容出现了变化，那么判断是否有红包视图出现
        try {
            val s = "com.tencent.mm" //包名

            if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI" == event.className.toString()) {

                //显示红包, 点击"开"抢红包
                rootInActiveWindow?.findAccessibilityNodeInfosByViewId("$s:id/dan")?.let {

                    if (it.isNotEmpty()) {
                        it.first().performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                }
            } else if ("com.tencent.mm.ui.LauncherUI" == event.className.toString()) {

                rootInActiveWindow?.findAccessibilityNodeInfosByViewId("$s:id/aum")?.let {
                    if (it.isNotEmpty()) {
                        Log.d("11111", "1111111")
                        it.last().parent?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                }


            }   else if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI" == event.className.toString()) {
                //到抢红包记录界面, 回去
                mRootNodeInfo?.findAccessibilityNodeInfosByViewId("$s:id/m1")?.let {
                    if (it.isNotEmpty()) {
                        it[0].parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                }
            }

        } catch (e1: Exception) {
            e1.printStackTrace()
            Log.d("error", e1.message)
        }
    }

}