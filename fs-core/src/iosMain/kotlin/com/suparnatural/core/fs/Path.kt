package com.suparnatural.core.fs

import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.URLByAppendingPathComponent
import platform.Foundation.stringWithFormat

actual class Path actual constructor() {
    constructor(absolutePath: String?, relativePath: String?): this() {
        this.absolutePath = PathComponent(absolutePath)
        this.relativePath = PathComponent(relativePath)
    }

    actual var absolutePath: PathComponent? = null


    actual var relativePath: PathComponent? = null


    actual companion object {
        val Empty = Path()
        fun urlFromString(urlString: String?) = if (urlString != null) NSURL.URLWithString(urlString) else null
        fun fromUrl(url: NSURL?) = Path(url?.absoluteString, url?.relativeString)
        fun fromUrlString(urlString: String) = fromUrl(urlFromString(urlString))
        actual fun simplified(path: String): Path = Path(path, path)
    }

    override fun toString(): String {
        return "[absolutePath=$absolutePath, relativePath=$relativePath]"
    }
}

actual class PathComponent actual constructor(actual val component: String?) {

    val url = if(component != null) NSURL.URLWithString(component) else null

    actual fun byAppending(component: String): PathComponent? {
        if( url == null) return null
        return PathComponent(url.URLByAppendingPathComponent(component)?.path)
    }

    override fun toString(): String {
        return "[component=$component, url=$url]"
    }

}