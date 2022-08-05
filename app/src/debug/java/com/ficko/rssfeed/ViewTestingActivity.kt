package com.ficko.rssfeed

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ViewTestingActivity : AppCompatActivity() {

    private lateinit var viewContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_testing_activity)
        viewContainer = findViewById(R.id.view_container)
    }

    fun addView(view: View) {
        viewContainer.addView(view)
    }

    fun removeView(view: View) {
        viewContainer.removeView(view)
    }
}