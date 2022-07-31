package com.ficko.rssfeed.ui.common

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

object Utils {

    fun showToast(activity: Activity, resId: Int) {
        val message = activity.getString(resId)
        Toast.makeText(activity.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun closeSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentlyFocusedView = activity.currentFocus
        val windowToken = currentlyFocusedView?.windowToken ?: getWindowTokenFromNewView(activity)
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun getWindowTokenFromNewView(activity: Activity) = View(activity).windowToken
}