package extensions

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

open class SourceSetExtension<T: DependenciesExtension>(dependenciesExtensionClass: KClass<T>) {
    var dependencies: T = dependenciesExtensionClass.createInstance()

    operator fun invoke(closure: SourceSetExtension<T>.() -> Unit) {
        apply(closure)
    }
}