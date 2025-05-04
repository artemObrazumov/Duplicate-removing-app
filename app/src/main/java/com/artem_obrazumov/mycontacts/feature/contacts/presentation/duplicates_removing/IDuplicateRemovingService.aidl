package com.artem_obrazumov.mycontacts.feature.contacts.presentation.duplicates_removing;

import com.artem_obrazumov.mycontacts.feature.contacts.presentation.duplicates_removing.DuplicateRemovingServiceCallback;

interface IDuplicateRemovingService {
    void startCleanContacts(in DuplicateRemovingServiceCallback callback);
}