/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.usecase

import com.inu.cafeteria.functional.Result
import com.inu.cafeteria.interactor.UseCase
import com.inu.cafeteria.model.scheme.Notice
import com.inu.cafeteria.repository.NoticeRepository
import com.inu.cafeteria.repository.Repository

class GetNotice(
    private val noticeRepo: NoticeRepository
) : UseCase<Unit, Notice>() {

    override fun run(params: Unit): Result<Notice> = Result.of {
        var notice: Notice? = null
        var failure: Exception? = null

        noticeRepo.getNotice(
            Repository.DataCallback(
                async = false,
                onSuccess = { notice = it },
                onFail = { failure = it }
            )
        )

        failure?.let { throw it }

        return@of notice ?: Notice.emptyNotice()
    }
}