/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.inu.cafeteria.feature.splash

import android.app.Activity
import com.inu.cafeteria.R
import com.inu.cafeteria.common.Navigator
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.base.BaseViewModel
import com.inu.cafeteria.common.extension.finishActivity
import com.inu.cafeteria.common.widget.ThemedDialog
import com.inu.cafeteria.exception.ServerNoResponseException
import com.inu.cafeteria.model.VersionCompared
import com.inu.cafeteria.model.scheme.Notice
import com.inu.cafeteria.repository.NoticeRepository
import com.inu.cafeteria.repository.VersionRepository
import com.inu.cafeteria.usecase.GetNotice
import com.inu.cafeteria.usecase.GetVersion
import org.koin.core.inject
import timber.log.Timber

class SplashViewModel : BaseViewModel() {

    private val getVersion: GetVersion by inject()
    private val getNotice: GetNotice by inject()

    private val navigator: Navigator by inject()

    private val versionRepo: VersionRepository by inject()
    private val noticeRepo: NoticeRepository by inject()

    var onNoConnection: () -> Unit = {}
    var onUnknownError: (Exception) -> Unit = {}

    init {
        failables += this
        failables += getVersion
        failables += navigator
        failables += versionRepo
    }

    /**
     * Compare build version to the latest version(from the server).
     * If update is needed, let user know it.
     *
     * @param activity activity for popup.
     * @param onFail launched on server fault.
     * @param onPass launched when no need to update.
     * @param onUpdate launched when user pressed update button.
     * @param onDismiss launched when user pressed cancel button.
     */

fun tryCheckVersion(
        activity: Activity,
        onFail: (e: Exception) -> Unit,
        onPass: () -> Unit,
        onUpdate: () -> Unit,
        onDismiss: () -> Unit
    ) {
        val getDialog = { version: VersionCompared ->
            ThemedDialog(activity)
                .withTitle(R.string.dialog_new_version, version.latestVersion)
                .withMessage(R.string.dialog_ask_update)
                .withCheckBox(R.string.desc_pass_this_version)
                .withPositiveButton(R.string.button_update) { dismiss ->
                    if (dismiss) {
                        versionRepo.dismissVersion(version.latestVersion)
                    }
                    onUpdate()
                }
                .withNegativeButton(R.string.button_cancel) { dismiss ->
                    if (dismiss) {
                        versionRepo.dismissVersion(version.latestVersion)
                    }
                    onDismiss()
                }
        }

        getVersion(Unit) {
            it.onSuccess { versionCompared ->

                Timber.i("Current version: ${versionCompared.currentVersion}")
                Timber.i("Latest version: ${versionCompared.latestVersion}")
                Timber.i("Ignored update: ${versionRepo.getDismissedVersion()}")

                val thisVersionIsIgnoredByUser = versionRepo.getDismissedVersion() == versionCompared.latestVersion

                if (versionCompared.needUpdate() && !thisVersionIsIgnoredByUser) {
                    getDialog(versionCompared).show()
                } else {
                    onPass()
                }

            }.onError { e ->
                onFail(e)
            }
        }
    }

    /**
     * Check for new notices.
     * If new or non-ignored notice exists, show user.
     *
     * @param activity activity for popup.
     * @param onFail launched on server fault.
     * @param onPass launched when nothing is to be shown.
     * @param onConfirm launched when user pressed confirm button.
     */

fun tryShowNotice(
        activity: Activity,
        onFail: (e: Exception) -> Unit,
        onPass: () -> Unit,
        onConfirm: () -> Unit
    ) {
        val getAllDialog = { notice: Notice, next: () -> Unit ->
            ThemedDialog(activity)
                .withTitle(notice.all.title)
                .withMessage(notice.all.message)
                .withCheckBox(R.string.desc_dont_show_again)
                .withPositiveButton(R.string.button_confirm) { dismiss ->
                    if (dismiss) {
                        noticeRepo.dismissNotice(
                            NoticeRepository.DeviceType.All,
                            notice.all.id
                        )
                    }
                    next()
                }
        }
        val getAndroidDialog = { notice: Notice, next: () -> Unit ->
            ThemedDialog(activity)
                .withTitle(notice.android.title)
                .withMessage(notice.android.message)
                .withCheckBox(R.string.desc_dont_show_again)
                .withPositiveButton(R.string.button_confirm) { dismiss ->
                    if (dismiss) {
                        noticeRepo.dismissNotice(
                            NoticeRepository.DeviceType.Android,
                            notice.android.id
                        )
                    }
                    next()
                }
        }

        // Android dialog is not synchronous.
        // So we need a callback.
        getNotice(Unit) {
            it.onSuccess { notice ->
                val allNoticeIgnored = noticeRepo.getDismissedNoticeId(NoticeRepository.DeviceType.All) == notice.all.id
                val showAllNotice = !allNoticeIgnored && notice.all.id > 0

                val androidNoticeIgnored = noticeRepo.getDismissedNoticeId(NoticeRepository.DeviceType.Android) == notice.android.id
                val showAndroidNotice = !androidNoticeIgnored && notice.android.id > 0

                Timber.i("Show notice for all? $showAllNotice. Notice id is ${notice.all.id}, ignored? $allNoticeIgnored.")
                Timber.i("Show notice for android? $showAndroidNotice. Notice id is ${notice.android.id}, ignored? $androidNoticeIgnored.")

                if (showAllNotice && showAndroidNotice) {
                    // First
                    getAllDialog(notice) {
                        // Second
                        getAndroidDialog(notice) {
                            // Third
                            onConfirm()
                        }.show()
                    }.show()
                } else if (showAllNotice) {
                    getAllDialog(notice, onConfirm).show()
                } else if (showAndroidNotice) {
                    getAndroidDialog(notice, onConfirm).show()
                } else {
                    onPass()
                }

            }.onError { e ->
                onFail(e)
            }
        }
    }

    /**
     * ServerNoResponseException is a special case, so it need to be
     * handled carefully.
     */

fun handleConnectionFailure(e: Exception) {
        when (e) {
            is ServerNoResponseException -> onNoConnection()
            else -> onUnknownError(e)
        }
    }

    /**
     * Navigate to LoginActivity and close current activity.
     */

fun showLogin(fragment: BaseFragment) {
        navigator.showLogin()
        fragment.finishActivity()
    }

    fun goUpdate() {
        navigator.showStore()
        Timber.i("User is gone to the store.")
    }
}