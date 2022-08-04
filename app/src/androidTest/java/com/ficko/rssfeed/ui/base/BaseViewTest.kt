package com.ficko.rssfeed.ui.base

import android.view.View
import com.ficko.rssfeed.ViewTestingActivity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before

@HiltAndroidTest
abstract class BaseViewTest : BaseActivityTest() {

    @Before
    open fun setUp() {
        launchActivity<ViewTestingActivity>()
    }

    protected fun loadView(view: View) {
        (getActivityInstance() as ViewTestingActivity).addView(view)
    }

    protected fun removeView(view: View) {
        (getActivityInstance() as ViewTestingActivity).removeView(view)
    }
}