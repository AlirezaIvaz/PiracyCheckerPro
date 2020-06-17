@file:Suppress("unused")

package ir.android.piracychecker

import android.content.Context
import androidx.fragment.app.Fragment
import ir.android.piracychecker.callbacks.AllowCallback
import ir.android.piracychecker.callbacks.DoNotAllowCallback
import ir.android.piracychecker.callbacks.OnErrorCallback
import ir.android.piracychecker.callbacks.PiracyCheckerCallbacksDSL
import ir.android.piracychecker.enums.PiracyCheckerError
import ir.android.piracychecker.enums.PirateApp

fun Context.piracyChecker(builder: PiracyChecker.() -> Unit): PiracyChecker {
    val checker = PiracyChecker(this)
    checker.builder()
    return checker
}

fun Fragment.piracyChecker(builder: PiracyChecker.() -> Unit): PiracyChecker =
    activity?.piracyChecker(builder) ?: requireContext().piracyChecker(builder)

inline fun PiracyChecker.allow(crossinline allow: () -> Unit = {}) = apply {
    allowCallback(object : AllowCallback {
        override fun allow() = allow()
    })
}

inline fun PiracyChecker.doNotAllow(crossinline doNotAllow: (PiracyCheckerError, PirateApp?) -> Unit = { _, _ -> }) =
    apply {
        doNotAllowCallback(object : DoNotAllowCallback {
            override fun doNotAllow(error: PiracyCheckerError, app: PirateApp?) =
                doNotAllow(error, app)
        })
    }

inline fun PiracyChecker.onError(crossinline onError: (PiracyCheckerError) -> Unit = {}) = apply {
    onErrorCallback(object : OnErrorCallback {
        override fun onError(error: PiracyCheckerError) {
            super.onError(error)
            onError(error)
        }
    })
}

fun PiracyChecker.callback(callbacks: PiracyCheckerCallbacksDSL.() -> Unit) {
    PiracyCheckerCallbacksDSL(this).callbacks()
}