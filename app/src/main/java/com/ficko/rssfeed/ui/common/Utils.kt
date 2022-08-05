package com.ficko.rssfeed.ui.common

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.ficko.rssfeed.databinding.CustomToastBinding

object Utils {

    fun showToast(activity: Activity, resId: Int) {
        val message = activity.getString(resId)
        Toast.makeText(activity.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun showSuccessNotification(
        context: Context,
        layoutInflater: LayoutInflater,
        message: String
    ) {
        val binding = CustomToastBinding.inflate(layoutInflater)
        binding.displayMessage = message
        Toast(context).apply {
            setGravity(Gravity.TOP, 0, 0)
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }.show()
    }

    fun closeSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentlyFocusedView = activity.currentFocus
        val windowToken = currentlyFocusedView?.windowToken ?: getWindowTokenFromNewView(activity)
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun getWindowTokenFromNewView(activity: Activity) = View(activity).windowToken
}