package com.suparnatural.core.fs

import android.content.Context
import android.content.pm.ProviderInfo
import android.util.Log
import com.suparnatural.core.utilities.ModuleProvider

/**
 * Provider for File System. Automatically initializes the library with the context.
 */
class FileSystemProvider: ModuleProvider("com.suparnatural.core.fs", "fileSystemProvider") {
    override fun bootstrap(context: Context?) {
        FileSystem.initialize(context)
    }
}