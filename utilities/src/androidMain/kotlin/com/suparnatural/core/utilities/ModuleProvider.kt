package com.suparnatural.core.utilities

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri

/**
 * A base content provider for auto injecting libraries with context on application startup.
 * The consumer must set the `applicationId` in `android` configuration. Otherwise this will raise an exception.
 * ### Examples
 *
 * #### Injector for My Library
 * ```
 * class MyLibraryProvider: ModuleProvider("com.my.library", "myLibraryProvider") {
 *     override fun bootstrap(context: Context?) {
 *         FileSystem.initialize(context)
 *     }
 * }
 * ```
 *
 * #### Manifest
 *
 * ```
 * <?xml version="1.0" encoding="utf-8"?>
 * <manifest xmlns:android="http://schemas.android.com/apk/res/android"
 *   package="com.suparnatural.core.fs">
 *       <application>
 *           <provider
 *               android:authorities="${applicationId}.myLibraryProvider"
 *               android:name=".MyLibraryProvider"
 *               android:exported="false"
 *               android:enabled="true"/>
 *       </application>
 * </manifest>
 * ```
 *
 */
abstract class ModuleProvider(private val ownerIdentifier: String, private val providerIdentifier: String): ContentProvider() {
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? = null

    override fun onCreate(): Boolean {
        bootstrap(context)
        return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = null

    /**
     * Implement this method with all the bootstrapping code.
     */
    abstract fun bootstrap(context: Context?)

    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        if (info == null) {
            throw NullPointerException("ModuleProvider $providerIdentifier in $ownerIdentifier 's ProviderInfo cannot be null.")
        }
        // So if the authorities equal the library internal ones, the developer forgot to set his applicationId
        if ("$ownerIdentifier.$providerIdentifier" == info.authority) {
            throw IllegalStateException("Incorrect provider authority in manifest.")
        }
        super.attachInfo(context, info)
    }
}