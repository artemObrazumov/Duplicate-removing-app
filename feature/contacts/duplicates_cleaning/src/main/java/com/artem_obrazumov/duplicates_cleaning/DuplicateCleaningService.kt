package com.artem_obrazumov.duplicates_cleaning

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.artem_obrazumov.contacts.aidl.DuplicateRemovingServiceCallback
import com.artem_obrazumov.contacts.aidl.IDuplicateRemovingService
import com.artem_obrazumov.domain.usecase.GetDuplicateContactsIdUseCase
import com.artem_obrazumov.domain.usecase.RemoveContactUseCase
import com.artem_obrazumov.domain.usecase.RemoveContactUseCaseResult
import com.artem_obrazumov.domain.utils.ErrorCodes
import com.artem_obrazumov.domain.utils.toErrorCode
import com.artem_obrazumov.duplicates_cleaning.di.DuplicatesRemovingServiceComponentProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DuplicateCleaningService: Service() {

    @Inject
    lateinit var getDuplicateContactsIdUseCase: GetDuplicateContactsIdUseCase

    @Inject
    lateinit var removeContactUseCase: RemoveContactUseCase

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var cleaningJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        (application as DuplicatesRemovingServiceComponentProvider)
            .provideDuplicateRemovingServiceComponent()
            .inject(this)
    }

    private val binder = object : IDuplicateRemovingService.Stub() {
        override fun startCleanContacts(callback: DuplicateRemovingServiceCallback) {
            cleaningJob = scope.launch {
                cleanDuplicates(callback)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    private suspend fun cleanDuplicates(callback: DuplicateRemovingServiceCallback) {
        withContext(Dispatchers.IO) {
            val ids = getDuplicateContactsIdUseCase()

            if (ids.isEmpty()) {
                callback.onCleanCancelled(ErrorCodes.DUPLICATES_NOT_FOUND_ERROR)
                return@withContext
            }

            val errorCodes = HashSet<Int>()
            callback.onCleanStarted(ids.size)
            var deletedContacts = 0
            ids.forEach { id ->
                when (val result = removeContactUseCase(id)) {
                    is RemoveContactUseCaseResult.Failure -> {
                        errorCodes.add(result.error.toErrorCode())
                    }
                    RemoveContactUseCaseResult.Success -> {
                        deletedContacts++
                        callback.onProgress(deletedContacts, ids.size)
                    }
                }
            }
            callback.onCleanCompleted(deletedContacts, ids.size, errorCodes.toIntArray())
        }
    }
}