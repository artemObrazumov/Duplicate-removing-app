package com.artem_obrazumov.mycontacts.common.feature.contacts.aidl;

import com.artem_obrazumov.mycontacts.common.feature.contacts.aidl.DuplicateRemovingServiceCallback;

interface IDuplicateRemovingService {
    void startCleanContacts(in DuplicateRemovingServiceCallback callback);
}