@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

import kotlin.js.*
import fs.WriteStream

external interface `T$0` {
    var stopOnErr: Boolean
}

external interface `T$1` {
    var errs: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$2` {
    var errs: dynamic /* String | Buffer | URL */
        get() = definedExternally
        set(value) = definedExternally
}

@Suppress("DEPRECATION")
external object ncp {
    @nativeInvoke
    operator fun invoke(source: String, destination: String, callback: (err: Array<Error>?) -> Unit)
    @nativeInvoke
    operator fun invoke(source: String, destination: String, options: Options /* Options & `T$0` */, callback: (err: Error?) -> Unit)
    @nativeInvoke
    operator fun invoke(source: String, destination: String, options: Options /* Options & `T$1` */, callback: (err: Array<Error>?) -> Unit)
    @nativeInvoke
    operator fun invoke(source: String, destination: String, options: Options /* Options & `T$2` */, callback: (err: WriteStream?) -> Unit)
    @nativeInvoke
    operator fun invoke(source: String, destination: String, options: Options, callback: (err: dynamic /* Error? | Array<Error>? | fs.WriteStream? */) -> Unit)
    var ncp: Any
    fun __promisify__(source: String, destination: String, options: Options = definedExternally): Promise<Unit>
}