package com.ficko.rssfeed.ui.common

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ficko.rssfeed.R
import com.ficko.rssfeed.databinding.AppBarBinding

class AppBar(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    interface AppBarListener {
        fun backButtonClicked()
        fun addButtonClicked()
        fun favoriteButtonClicked()
    }

    private lateinit var binding: AppBarBinding
    private var listener: AppBarListener? = null

    init {
        if (isInEditMode) {
            LayoutInflater.from(context).inflate(R.layout.app_bar, this, true)
        } else {
            binding = AppBarBinding.inflate(LayoutInflater.from(context), this, true)
            binding.view = this
            handleCustomAttributes(context, attrs)
        }
    }

    fun setListener(listener: AppBarListener) {
        this.listener = listener
    }

    fun updateView(
        backButtonEnabled: Boolean? = null,
        title: String? = null,
        favoriteButtonEnabled: Boolean? = null,
        addButtonEnabled: Boolean? = null
    ) {
        backButtonEnabled?.let { binding.backButtonEnabled = it }
        title?.let { binding.titleValue = it }
        addButtonEnabled?.let { binding.addButtonEnabled = it }
        favoriteButtonEnabled?.let { binding.favoriteButtonEnabled = it }
    }

    fun backButtonClicked() {
        listener?.backButtonClicked()
    }

    fun addButtonClicked() {
        listener?.addButtonClicked()
    }

    fun favoriteButtonClicked() {
        listener?.favoriteButtonClicked()
    }

    private fun handleCustomAttributes(context: Context, attrs: AttributeSet?) {
        val attributeArray: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AppBar, 0, 0)
        attributeArray.let {
            val backEnabled = it.getBoolean(R.styleable.AppBar_backButtonEnabled, false)
            val title = it.getString(R.styleable.AppBar_title)
            val favoriteEnabled = it.getBoolean(R.styleable.AppBar_favoriteButtonEnabled, false)
            val addEnabled = it.getBoolean(R.styleable.AppBar_addButtonEnabled, false)
            updateView(backEnabled, title, favoriteEnabled, addEnabled)
        }
    }
}