package org.inu.cafeteria.common.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle

import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import org.inu.cafeteria.R

class ThemedDialog(context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    /**
     * View
     */
    private lateinit var titleView: TextView
    private lateinit var messageView: TextView
    private lateinit var positiveButton: Button
    private lateinit var negativeButton: Button

    /**
     * Default values
     */
    private val titleBuilder = StringBuilder()
    private val messageBuilder = StringBuilder()
    private var positive = DialogButton()
    private var negative = DialogButton()

    /**
     * This dialog has no builder.
     * This method works as a builder.build().
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.8f
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
    }

    private fun fillView() {
        setTitle(titleBuilder.toString())
        setMessage(messageBuilder.toString())
        setPositiveButton(positive.buttonText, positive.onClick)
        setNegativeButton(negative.buttonText, negative.onClick)
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

    fun withPositiveButton(text: String?, listener: () -> Unit = {}): ThemedDialog {
        return this.apply {
            positive = DialogButton(text, listener)
        }
    }
    fun withPositiveButton(@StringRes text: Int, listener: () -> Unit = {}) = withPositiveButton(context.getString(text), listener)

    fun withNegativeButton(text: String?, listener: () -> Unit = {}): ThemedDialog {
        return this.apply {
            negative = DialogButton(text, listener)
        }
    }
    fun withNegativeButton(@StringRes text: Int, listener: () -> Unit = {}) = withNegativeButton(context.getString(text), listener)

    private fun setTitle(title: String) {
        titleView.text = title
    }
    private fun setMessage(message: String) {
        messageView.text = message
    }
    private fun setPositiveButton(buttonText: String?, onClick: () -> Unit = {}) {
        with(positiveButton) {
            text = buttonText
            setOnClickListener { onClick() }
        }
    }
    private fun setNegativeButton(buttonText: String?, onClick: () -> Unit = {}) {
        with(negativeButton) {
            text = buttonText
            setOnClickListener { onClick() }
        }
    }

    data class DialogButton(val buttonText: String? = null, val onClick: () -> Unit = {})
}