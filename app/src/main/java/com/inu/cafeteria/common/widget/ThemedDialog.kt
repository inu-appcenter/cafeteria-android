/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.common.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle

import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.StringRes
import com.inu.cafeteria.R
import com.inu.cafeteria.common.extension.setVisible

class ThemedDialog(context: Context) : Dialog(context, R.style.Transparent) {

    /**
     * View
     */
    private lateinit var titleView: TextView
    private lateinit var messageView: TextView
    private lateinit var positiveButton: Button
    private lateinit var negativeButton: Button
    private lateinit var checkBox: CheckBox

    /**
     * Default values
     */
    private val titleBuilder = StringBuilder()
    private val messageBuilder = StringBuilder()
    private var positive = DialogButton()
    private var negative = DialogButton()
    private var checkBoxText: String? = null

    /**
     * This dialog has no builder.
     * This method works as a builder.build().
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = window!!.attributes
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.6f
        window!!.attributes = layoutParams

        setContentView(R.layout.themed_dialog)

        setView()
        fillView()
    }

    private fun setView() {
        titleView = findViewById(R.id.title)
        messageView = findViewById(R.id.message)
        positiveButton = findViewById(R.id.positive)
        negativeButton = findViewById(R.id.negative)
        checkBox = findViewById(R.id.checkBox)
    }

    private fun fillView() {
        setTitle(titleBuilder.toString())
        setMessage(messageBuilder.toString())
        setPositiveButton(positive.buttonText, positive.onClick)
        setNegativeButton(negative.buttonText, negative.onClick)
        setCheckBox(checkBoxText)
    }

    fun withTitle(title: String?): ThemedDialog {
        return this.apply {
            titleBuilder.clear()
            titleBuilder.append(title)
        }
    }
    fun withTitle(@StringRes title: Int, vararg formatArgs: Any?): ThemedDialog = withTitle(context.getString(title, *formatArgs))

    fun withMessage(message: String?): ThemedDialog {
        return this.apply {
            messageBuilder.clear()
            messageBuilder.append(message)
        }
    }
    fun withMessage(@StringRes message: Int, vararg formatArgs: Any?): ThemedDialog = withMessage(context.getString(message, *formatArgs))

    fun withMoreMessage(addition: String?): ThemedDialog {
        return this.apply {
            messageBuilder.append(addition)
        }
    }
    fun withMoreMessage(@StringRes addition: Int, vararg formatArgs: Any?): ThemedDialog = withMoreMessage(context.getString(addition, *formatArgs))

    fun withNewLine(): ThemedDialog = withMoreMessage("\n")

    fun withPositiveButton(text: String?, listener: (Boolean) -> Unit = {}): ThemedDialog {
        return this.apply {
            positive = DialogButton(text, listener)
        }
    }
    fun withPositiveButton(@StringRes text: Int, listener: (Boolean) -> Unit = {}) = withPositiveButton(context.getString(text), listener)

    fun withNegativeButton(text: String?, listener: (Boolean) -> Unit = {}): ThemedDialog {
        return this.apply {
            negative = DialogButton(text, listener)
        }
    }
    fun withNegativeButton(@StringRes text: Int, listener: (Boolean) -> Unit = {}) = withNegativeButton(context.getString(text), listener)

    fun withCheckBox(text: String?): ThemedDialog {
        return this.apply {
            checkBoxText = text
        }
    }
    fun withCheckBox(@StringRes text: Int) = withCheckBox(context.getString(text))

    private fun setTitle(title: String) {
        titleView.text = title
    }
    private fun setMessage(message: String) {
        messageView.text = message
    }
    private fun setPositiveButton(buttonText: String?, onClick: (Boolean) -> Unit = {}) {
        with(positiveButton) {
            setVisible(buttonText != null)
            text = buttonText
            setOnClickListener {
                onClick(checkBox.isSelected)
                dismiss()
            }
        }
    }
    private fun setNegativeButton(buttonText: String?, onClick: (Boolean) -> Unit = {}) {
        with(negativeButton) {
            setVisible(buttonText != null)
            text = buttonText
            setOnClickListener {
                onClick(checkBox.isSelected)
                dismiss()
            }
        }
    }
    private fun setCheckBox(checkBoxText: String?) {
        with(checkBox) {
            setVisible(checkBoxText != null)
            text = checkBoxText
        }
    }

    data class DialogButton(val buttonText: String? = null, val onClick: (Boolean) -> Unit = {})
}