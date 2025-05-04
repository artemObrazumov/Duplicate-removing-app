package com.artem_obrazumov.mycontacts.feature.contacts.presentation.duplicates_removing;

interface DuplicateRemovingServiceCallback {
    void onCleanStarted(int duplicates);
    void onProgress(int progress);
    void onCleanCompleted(int duplicatesRemoved);
}