package org.inu.cafeteria.util

import android.app.AlertDialog
import android.content.Context
import android.view.View
import androidx.annotation.StringRes

/**
 * AlertDialog의 wrapper입니다.
 *
 * 메소드 체인으로 Dialog를 만들어서 띄울 수 있습니다.
 * 아니면 static 메소드를 사용하여 바로 띄울 수도 있습니다.
 */
class Popup(private val context: Context?) {

    private val dialogBuilder = AlertDialog.Builder(context).apply {
        setPositiveButton("OK", null)
    }
    private val messageBuilder = StringBuilder()

    fun show() {
        if (messageBuilder.isNotEmpty()) {
            dialogBuilder.setMessage(messageBuilder.toString())
        }

        dialogBuilder.show()
    }

    fun withTitle(title: String?): Popup {
        dialogBuilder.setTitle(title)
        return this
    }
    fun withTitle(@StringRes title: Int, vararg formatArgs: Any?): Popup = withTitle(context?.getString(title, *formatArgs))

    fun withMessage(message: String?): Popup {
        messageBuilder.clear()
        messageBuilder.append(message)
        return this
    }
    fun withMessage(@StringRes message: Int, vararg formatArgs: Any?): Popup = withMessage(context?.getString(message, *formatArgs))

    fun withMoreMessage(addition: String?): Popup {
        messageBuilder.append(addition)
        return this
    }
    fun withMoreMessage(@StringRes addition: Int, vararg formatArgs: Any?): Popup = withMoreMessage(context?.getString(addition, *formatArgs))

    fun withNewLine(): Popup = withMoreMessage("\n")

    fun withPositiveButton(text: String?, listener: () -> Unit = {}): Popup {
        dialogBuilder.setPositiveButton(text) { _, _ ->
            listener()
        }
        return this
    }
    fun withPositiveButton(@StringRes text: Int, listener: () -> Unit = {}) = withPositiveButton(context?.getString(text), listener)

    fun withNegativeButton(text: String?, listener: () -> Unit = {}): Popup {
        dialogBuilder.setNegativeButton(text) { _, _ ->
            listener()
        }
        return this
    }
    fun withNegativeButton(@StringRes text: Int, listener: () -> Unit = {}) = withNegativeButton(context?.getString(text), listener)

    fun withView(view: View): Popup {
        dialogBuilder.setView(view)
        return this
    }

    companion object {
        /**
         * 위의 것들이 귀찮을 때에 아주 빠르게 쓸 수 있는 간단한 함수입니다.
         * 컨텍스트와 내용을 받아서 다이얼로그를 만들어서 띄웁니다.
         */
        fun show(context: Context, message: String) {
            AlertDialog.Builder(context).apply {
                setPositiveButton("OK", null)
                setMessage(message)
                show()
            }
        }
    }
}