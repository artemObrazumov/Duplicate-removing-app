package com.artem_obrazumov.contacts.aidl;

import com.artem_obrazumov.contacts.aidl.DuplicateRemovingServiceCallback;

interface IDuplicateRemovingService {
    void startCleanContacts(in DuplicateRemovingServiceCallback callback);
}