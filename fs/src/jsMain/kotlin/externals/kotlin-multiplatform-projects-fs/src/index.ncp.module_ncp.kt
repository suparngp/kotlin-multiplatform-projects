@file:JsModule("ncp")
@file:JsNonModule
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

import kotlin.js.*
import NodeJS.ReadableStream
import NodeJS.WritableStream

external interface File {
    var name: String
    var mode: Number
    var atime: Date
    var mtime: Date
}

external interface Options {
    var filter: dynamic /* RegExp? | ((filename: String) -> Boolean)? */
        get() = definedExternally
        set(value) = definedExternally
    var transform: ((read: ReadableStream, write: WritableStream, file: File) -> Unit)?
        get() = definedExternally
        set(value) = definedExternally
    var clobber: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var dereference: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var stopOnErr: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var errs: dynamic /* String? | Buffer? | URL? */
        get() = definedExternally
        set(value) = definedExternally
    var limit: Number?
        get() = definedExternally
        set(value) = definedExternally
}