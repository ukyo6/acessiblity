package com.ukyoo.access

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class DYHBService : AccessibilityService() {


    //该对象代表了整个窗口视图的快照
    private var mRootNodeInfo: AccessibilityNodeInfo? = null

    /**
     * 窗口行为变化的回调
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        mRootNodeInfo = rootInActiveWindow
        if (mRootNodeInfo == null) {
            return
        }
        //如果当前的事件类型是窗口内容出现了变化，那么判断是否有红包视图出现
        try {
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                val s = "xxxx" //包名
                val weikaiList: List<AccessibilityNodeInfo> =
                    mRootNodeInfo!!.findAccessibilityNodeInfosByViewId("$s:id/receive")

                for (nodeInfo in weikaiList) {
                    if (nodeInfo.text == "领取红包") {
                        nodeInfo.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                }
            }
            val s = "xxxxxx"
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                val clickedWindowList =
                    mRootNodeInfo!!.findAccessibilityNodeInfosByViewId("$s:id/redpackets_open")
                if (clickedWindowList.size > 0) {
                    val curNodeInfo1 = clickedWindowList[0]
                    curNodeInfo1.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }
            }
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                val backlist =
                    mRootNodeInfo!!.findAccessibilityNodeInfosByViewId("$s:id/back_btn")
                if (backlist.size > 0) {
                    val curNodeInfo1 = backlist[0]
                    curNodeInfo1.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }

    }


    /**
     * 行为中断的回调
     */
    override fun onInterrupt() {

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("DYHBService", "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }
}