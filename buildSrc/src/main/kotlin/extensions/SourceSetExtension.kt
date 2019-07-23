package extensions

import kotlin.reflect.KClass

open class SourceSetExtension<T: DefaultDependencies>(DefaultsClass: KClass<T>) {
    val dependencies: DependencyGroups<T> = DependencyGroups(DefaultsClass)
    operator fun invoke(closure: SourceSetExtension<T>.() -> Unit) {
        apply(closure)
    }
}
