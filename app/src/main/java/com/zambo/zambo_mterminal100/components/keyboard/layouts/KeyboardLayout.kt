package com.donbrody.customkeyboard.components.keyboard.layouts

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.preference.PreferenceScreen
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.donbrody.customkeyboard.components.keyboard.KeyboardListener
import com.donbrody.customkeyboard.components.keyboard.controllers.KeyboardController
import com.donbrody.customkeyboard.components.utilities.ComponentUtils
import com.zambo.zambo_mterminal100.R


abstract class KeyboardLayout(context: Context, private val controller: KeyboardController?,
                              var hasNextFocus: Boolean = false) : LinearLayout(context) {

    private var screenWidth = 0.0f
    private var screenheight = 150.0f
    internal var textSize = 20.0f

    private val Int.toDp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()

    init {
        layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun createKeyboard(screenWidth: Float = this.screenWidth) {
        removeAllViews()
        this.screenWidth = screenWidth
        gravity = (Gravity.CENTER)
        val keyboardWrapper = createWrapperLayout()
        for (row in createRows()) {
            keyboardWrapper.addView(row)
        }
        addView(keyboardWrapper)
    }

    private fun createWrapperLayout(): LinearLayout {
        val wrapper = LinearLayout(context)
        val lp = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        )
        lp.topMargin = 15.toDp
        lp.bottomMargin = 15.toDp
        wrapper.layoutParams = lp
        wrapper.orientation = VERTICAL
        return wrapper
    }

    private fun createButton(text: String, widthAsPctOfScreen: Float): Button {
        val button = Button(context)
        button.layoutParams = LayoutParams(
                (screenWidth * widthAsPctOfScreen).toInt(),
                screenheight.toInt()

        )
        ComponentUtils.setBackgroundTint(button, Color.BLUE)
        button.setAllCaps(false)
        button.textSize = textSize
        button.text = text
        button.setTextColor(Color.WHITE)
        return button
    }

    internal fun createButton(text: String, widthAsPctOfScreen: Float, c: Char): Button {
        val button = createButton(text, widthAsPctOfScreen)
        button.setOnClickListener { controller?.onKeyStroke(c) }
        return button
    }

    internal fun createButton(text: String, widthAsPctOfScreen: Float,
                              key: KeyboardController.SpecialKey): Button {
        val button = createButton(text, widthAsPctOfScreen)
        button.setOnClickListener { controller?.onKeyStroke(key) }
        return button
    }

    internal fun createSpacer(widthAsPctOfScreen: Float): View {
        val view = View(context)
        view.layoutParams = LayoutParams(
                (screenWidth * widthAsPctOfScreen).toInt(), 0
        )
        view.isEnabled = false
        view.visibility = View.INVISIBLE
        return view
    }

    internal fun createRow(buttons: List<View>): LinearLayout {
        val row = LinearLayout(context)
        row.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        )
        orientation = HORIZONTAL
        row.gravity = Gravity.CENTER

        for (button in buttons) {
            row.addView(button)
        }
        return row
    }

    internal fun registerListener(listener: KeyboardListener) {
        controller?.registerListener(listener)
    }

    internal abstract fun createRows(): List<LinearLayout>
}
