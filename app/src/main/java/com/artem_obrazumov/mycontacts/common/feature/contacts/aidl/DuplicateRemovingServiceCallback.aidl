package com.artem_obrazumov.mycontacts.common.feature.contacts.aidl;

interface DuplicateRemovingServiceCallback {
    void onCleanStarted(int duplicates);
    void onProgress(int progress);
    void onCleanCompleted(int duplicatesRemoved);
}