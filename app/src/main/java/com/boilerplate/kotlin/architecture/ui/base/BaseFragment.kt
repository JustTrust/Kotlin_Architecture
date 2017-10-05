package com.boilerplate.kotlin.architecture.ui.base

import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.boilerplate.kotlin.architecture.utils.Constants

/**
 * Created by a.belichenko@gmail.com on 05.10.17.
 */
abstract class BaseFragment : Fragment(){

    private var mAddToBAckStack: Boolean = false

    private var lastClickTime = SystemClock.elapsedRealtime()

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, true)
    }

    fun show(fragmentManager: FragmentManager, addToBackStack: Boolean) {
        this.mAddToBAckStack = addToBackStack
        replaceFragment(fragmentManager, addToBackStack, getContainer())
    }

    private fun replaceFragment(fragmentManager: FragmentManager, addToBackStack: Boolean, container: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(container, this, getName())
        if (addToBackStack)
            transaction.addToBackStack(getName())
        transaction.commitAllowingStateLoss()
    }

    abstract fun getName(): String

    abstract fun getContainer(): Int

    protected fun isClickAllowed(): Boolean {
        val now = SystemClock.elapsedRealtime()
        return if (now - lastClickTime > Constants.CLICK_TIME_INTERVAL) {
            lastClickTime = now
            true
        } else
            false
    }
}