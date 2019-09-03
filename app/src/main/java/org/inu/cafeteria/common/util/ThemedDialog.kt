package org.inu.cafeteria.common.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle

import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import org.inu.cafeteria.R
import org.inu.cafeteria.util.Popup

class ThemedDialog(context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    private lateinit var title: TextView
    private lateinit var message: TextView
    private lateinit var positiveButton: Button
    private lateinit var negativeButton: Button

    private val messageBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.8f
        window!!.attributes = layoutParams

        setContentView(R.layout.themed_dialog)

        title = findViewById(R.id.title)
        message = findViewById(R.id.message)
        positiveButton = findViewById(R.id.positive)
        negativeButton = findViewById(R.id.negative)
    }

    override fun show() {
        setMessage(messageBuilder.toString())
        super.show()
    }

    fun withTitle(title: String?): ThemedDialog {
        return this.apply {
            setTitle(title)
        }
    }
    fun withTitle(@StringRes title: Int, vararg formatArgs: Any?): ThemedDialog = withTitle(context?.getString(title, *formatArgs))

    fun withMessage(message: String?): ThemedDialog {
        return this.apply {
            messageBuilder.clear()
            messageBuilder.append(message)
        }
    }
    fun withMessage(@StringRes message: Int, vararg formatArgs: Any?): ThemedDialog = withMessage(context?.getString(message, *formatArgs))

    fun withMoreMessage(addition: String?): ThemedDialog {
        return this.apply {
            messageBuilder.append(addition)
        }
    }
    fun withMoreMessage(@StringRes addition: Int, vararg formatArgs: Any?): ThemedDialog = withMoreMessage(context?.getString(addition, *formatArgs))

    fun withNewLine(): ThemedDialog = withMoreMessage("\n")

    fun withPositiveButton(text: String?, listener: () -> Unit = {}): ThemedDialog {
        return this.apply {
            setPositiveButton(text, listener)
        }
    }
    fun withPositiveButton(@StringRes text: Int, listener: () -> Unit = {}) = withPositiveButton(context?.getString(text), listener)

    fun withNegativeButton(text: String?, listener: () -> Unit = {}): ThemedDialog {
        return this.apply {
            setNegativeButton(text, listener)
        }
    }
    fun withNegativeButton(@StringRes text: Int, listener: () -> Unit = {}) = withNegativeButton(context?.getString(text), listener)


    private fun setTitle(title: String) {
        this.title.text = title
    }

    private fun setMessage(message: String) {
        this.message.text = message
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
}