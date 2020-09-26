package extensions

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

// represents a dependencies block
// acts as a pass through for KotlinDependencyHandler
abstract class Dependencies {
    open var closure: (KotlinDependencyHandler.() -> Unit)? = null
    fun apply(dependencyHandler: KotlinDependencyHandler) {
        if (closure != null) {
            dependencyHandler.apply(closure!!)
        }
    }
}

// dependencies with some default values controlled by variables.
// defaults can be disabled
abstract class DefaultDependencies: Dependencies() {
    abstract fun disable()
}

// additional dependencies without any control variables
class AdditionalDependencies: Dependencies() {
    operator fun invoke(closure: KotlinDependencyHandler.() -> Unit) {
        this.closure = closure
    }
}


class AndroidMainDefaultDependencies : DefaultDependencies() {

    var stdlib = true
    override fun disable() {
        stdlib = false
    }

    override var closure: (KotlinDependencyHandler.() -> Unit)?
        get() = {
            if (stdlib)
                implementation(kotlin("stdlib"))
        }
        set(_) {}

    operator fun invoke(closure: AndroidMainDefaultDependencies.() -> Unit) {
        apply(closure)
    }
}

class AndroidTestDefaultDependencies : DefaultDependencies() {
    var test = true
    var testJunit = true
    override var closure: (KotlinDependencyHandler.() -> Unit)?
        get() = {
            if (test)
                implementation(kotlin("test"))

            if (testJunit)
                implementation(kotlin("test-junit"))
        }
        set(_) {}

    override fun disable() {
        test = false
        testJunit = false
    }

    operator fun invoke(closure: AndroidTestDefaultDependencies.() -> Unit) {
        apply(closure)
    }
}

class AndroidVariantDefaultDependencies: DefaultDependencies() {
    // a no op
    override fun disable() {}
}

class CommonMainDefaultDependencies : DefaultDependencies() {
    var stdlib = true
    override fun disable() {
        stdlib = false
    }

    override var closure: (KotlinDependencyHandler.() -> Unit)?
        get() = {
            if (stdlib)
                implementation(kotlin("stdlib-common"))
        }
        set(_) {}

    operator fun invoke(closure: CommonMainDefaultDependencies.() -> Unit) {
        apply(closure)
    }
}

class CommonTestDefaultDependencies : DefaultDependencies() {
    var test = true
    var testAnnotations = true
    override fun disable() {
        test = false
        testAnnotations = false
    }

    override var closure: (KotlinDependencyHandler.() -> Unit)?
        get() = {
            if (test)
                implementation(kotlin("test-common"))

            if (testAnnotations)
                implementation(kotlin("test-annotations-common"))
        }
        set(_) {}

    operator fun invoke(closure: CommonTestDefaultDependencies.() -> Unit) {
        apply(closure)
    }
}

class IosArm64MainDefaultDependencies : DefaultDependencies() {
    override fun disable() { }

    override var closure: (KotlinDependencyHandler.() -> Unit)?
        get() = {}
        set(_) {}

    operator fun invoke(closure: IosArm64MainDefaultDependencies.() -> Unit) {
        apply(closure)
    }
}

class IosArm64TestDefaultDependencies : DefaultDependencies() {
    override fun disable() { }

    override var closure: (KotlinDependencyHandler.() -> Unit)?
        get() = {}
        set(_) {}

    operator fun invoke(closure: IosArm64TestDefaultDependencies.() -> Unit) {
        apply(closure)
    }
}

class IosX64MainDefaultDependencies : DefaultDependencies() {
    override fun disable() { }

    override var closure: (KotlinDependencyHandler.() -> Unit)?
        get() = {}
        set(_) {}

    operator fun invoke(closure: IosX64MainDefaultDependencies.() -> Unit) {
        apply(closure)
    }
}

class IosX64TestDefaultDependencies : DefaultDependencies() {
    override fun disable() { }

    override var closure: (KotlinDependencyHandler.() -> Unit)?
        get() = {}
        set(_) {}

    operator fun invoke(closure: IosX64TestDefaultDependencies.() -> Unit) {
        apply(closure)
    }
}

class JvmMainDefaultDependencies: DefaultDependencies() {
    var stdlib = true
    override fun disable() {
        stdlib = false
    }

    override var closure: (KotlinDependencyHandler.() -> Unit)?
        get() = {
            if (stdlib)
                implementation(kotlin("stdlib"))
        }
        set(_) {}

    operator fun invoke(closure: JvmMainDefaultDependencies.() -> Unit) {
        apply(closure)
    }
}

class JvmTestDefaultDependencies: DefaultDependencies() {
    var test = true
    var testJunit = true
    override var closure: (KotlinDependencyHandler.() -> Unit)?
        get() = {
            if (test)
                implementation(kotlin("test"))

            if (testJunit)
                implementation(kotlin("test-junit"))
        }
        set(_) {}

    override fun disable() {
        test = false
        testJunit = false
    }

    operator fun invoke(closure: JvmTestDefaultDependencies.() -> Unit) {
        apply(closure)
    }
}

// a group of default and additional dependencies.
// accepts a type of default dependencies. e.g. android can have its own default dependencies
// an instance of additional dependencies is created.
class DependencyGroups<T: DefaultDependencies>(DefaultsClass: KClass<T>) {
    val defaults: T = DefaultsClass.createInstance()
    val additional = AdditionalDependencies()
    operator fun invoke(closure: DependencyGroups<T>.() -> Unit) {
        apply(closure)
    }
}