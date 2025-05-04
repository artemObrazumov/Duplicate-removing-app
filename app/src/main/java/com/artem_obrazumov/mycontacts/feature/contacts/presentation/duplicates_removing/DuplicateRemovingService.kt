package com.artem_obrazumov.mycontacts.feature.contacts.presentation.duplicates_removing

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import com.artem_obrazumov.mycontacts.app.MyContactsApp
import com.artem_obrazumov.mycontacts.common.feature.contacts.aidl.DuplicateRemovingServiceCallback
import com.artem_obrazumov.mycontacts.common.feature.contacts.aidl.IDuplicateRemovingService
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.usecase.GetDuplicateContactsIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DuplicateRemovingService: Service() {

    @Inject
    lateinit var getDuplicateContactsIdUseCase: GetDuplicateContactsIdUseCase

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var cleaningJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        (application as MyContactsApp).provideContactsFeatureComponent().duplicateRemovingServiceComponentFactory.create().inject(this)
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
            callback.onCleanStarted(ids.size)
            var deletedContacts = 0
            ids.forEach { id ->
                contentResolver.delete(
                    ContactsContract.RawContacts.CONTENT_URI,
                    "${ContactsContract.RawContacts._ID} = ?",
                    arrayOf(id.toString())
                )
                deletedContacts++
                callback.onProgress(deletedContacts)
            }
            callback.onCleanCompleted(deletedContacts)
        }
    }
}