package com.boilerplate.kotlin.architecture.utils

import android.content.Context
import android.util.DisplayMetrics
import android.graphics.PointF
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager


/**
 * Created by a.belichenko@gmail.com on 10.10.17.
 */
class SpeedyLinearLayoutManager(context: Context?) : LinearLayoutManager(context) {

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {

        val linearSmoothScroller = object : LinearSmoothScroller(recyclerView.context) {

            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return this@SpeedyLinearLayoutManager.computeScrollVectorForPosition(targetPosition)
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }
        }

        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    companion object {

        private val MILLISECONDS_PER_INCH = 125f //default is 25f (bigger = slower)
    }
}