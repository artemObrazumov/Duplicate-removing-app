package com.artem_obrazumov.mycontacts.common.feature.contacts.aidl;

interface DuplicateRemovingServiceCallback {

    void onCleanCancelled(int errorCode);

    void onCleanStarted(int duplicates);
    void onProgress(int progress, int duplicatesTotal);
    void onCleanCompleted(int duplicatesRemoved, int duplicatesTotal,
     in int[] errorCodes);
}