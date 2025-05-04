package com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.components

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.artem_obrazumov.mycontacts.R
import com.artem_obrazumov.mycontacts.common.feature.contacts.aidl.DuplicateRemovingServiceCallback
import com.artem_obrazumov.mycontacts.common.feature.contacts.aidl.IDuplicateRemovingService
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils.ContactsError
import com.artem_obrazumov.mycontacts.feature.contacts.duplicates_removing.DuplicateCleaningService
import com.artem_obrazumov.mycontacts.common.feature.contacts.domain.utils.toContactsError
import com.artem_obrazumov.mycontacts.core.presentation.resources.resolve
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.contacts_list.ContactsListScreenState
import com.artem_obrazumov.mycontacts.feature.contacts.presentation.utils.toStringResource
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun DuplicateCleaningServiceItem(
    state: ContactsListScreenState.DuplicatesCleanerServiceState,
    serviceEffectFlow: SharedFlow<ServiceEffect>,
    onHeightCalculated: (height: Dp) -> Unit,
    onServiceStartRequested: () -> Unit,
    onServiceCancelled: (error: ContactsError) -> Unit,
    onServiceStarted: (duplicates: Int) -> Unit,
    onServiceProgress: (duplicatesRemoved: Int, total: Int) -> Unit,
    onServiceCompleted: (duplicatesRemoved: Int, total: Int, errors: List<ContactsError>) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var service by remember { mutableStateOf<IDuplicateRemovingService?>(null) }

    val callback = remember<DuplicateRemovingServiceCallback> {
        object : DuplicateRemovingServiceCallback.Stub() {
            override fun onCleanCancelled(errorCode: Int) {
                onServiceCancelled(errorCode.toContactsError())
            }

            override fun onCleanStarted(duplicates: Int) {
                onServiceStarted(duplicates)
            }

            override fun onProgress(progress: Int, duplicatesTotal: Int) {
                onServiceProgress(progress, duplicatesTotal)
            }

            override fun onCleanCompleted(
                duplicatesRemoved: Int,
                duplicatesTotal: Int,
                errorCodes: IntArray?
            ) {
                onServiceCompleted(
                    duplicatesRemoved,
                    duplicatesTotal,
                    errorCodes?.map { it.toContactsError() } ?: emptyList()
                )
            }
        }
    }

    val serviceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                service = IDuplicateRemovingService.Stub.asInterface(binder)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                service = null
            }
        }
    }

    DisposableEffect (Unit) {
        val intent = Intent(context, DuplicateCleaningService::class.java).apply {
            action = "com.artem_obrazumov.mycontacts.feature.contacts.DUPLICATE_REMOVING_SERVICE"
        }
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        onDispose {
            context.unbindService(serviceConnection)
        }
    }

    LaunchedEffect(Unit) {
        serviceEffectFlow.collect { effect ->
            when (effect) {
                ServiceEffect.StartService -> {
                    service?.startCleanContacts(callback)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.White)
            .padding(16.dp)
            .onGloballyPositioned {
                onHeightCalculated(
                    it.size.height.dp
                )
            }
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (state) {
                is ContactsListScreenState.DuplicatesCleanerServiceState.Idle -> {
                    if (state.cleaningResult != null) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(
                                R.string.cleaned_duplicates,
                                state.cleaningResult.removed, state.cleaningResult.total
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                    if (state.errors.isNotEmpty()) {
                        state.errors.forEach {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = it.toStringResource().resolve(),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    Button(
                        onClick = onServiceStartRequested
                    ) {
                        Text(
                            text = stringResource(R.string.clean_duplicates)
                        )
                    }
                }

                is ContactsListScreenState.DuplicatesCleanerServiceState.CleaningStarted -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.started_cleaning, state.duplicates),
                        textAlign = TextAlign.Center
                    )
                }

                is ContactsListScreenState.DuplicatesCleanerServiceState.Process -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.contacts_deleted,
                            state.contactsDeleted, state.total),
                        textAlign = TextAlign.Center
                    )
                }

                ContactsListScreenState.DuplicatesCleanerServiceState.Hidden -> Unit
            }
        }
    }
}

sealed class ServiceEffect {

    data object StartService : ServiceEffect()
}