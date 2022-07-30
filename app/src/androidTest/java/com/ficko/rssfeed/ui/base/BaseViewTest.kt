package com.ficko.rssfeed.ui.base

import android.view.View
import com.ficko.rssfeed.ViewTestingActivity
import dagger.hilt.android.testing.HiltAndroidTest

@HiltAndroidTest
abstract class BaseViewTest : BaseActivityTest() {

    override val activityClass = ViewTestingActivity::class.java

    protected fun loadView(view: View) {
        (getActivityInstance() as ViewTestingActivity).addView(view)
    }

    protected fun removeView(view: View) {
        (getActivityInstance() as ViewTestingActivity).removeView(view)
    }
}