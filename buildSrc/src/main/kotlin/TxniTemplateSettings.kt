import org.gradle.kotlin.dsl.DependencyHandlerScope

interface TxniTemplateSettings {
    val depsHandler : DependencyHandler
    val publishHandler : PublishDependencyHandler
}


interface DependencyHandler {
    fun modrinth(name: String, dep: Any?) = "maven.modrinth:$name:$dep"

    fun modrinth(name: String) : String {
        val regex = """(?:https:\/\/modrinth\.com\/mod\/)?(.*?)\/version\/(.*)""".toRegex()
        val maven = regex.replace(name, "maven.modrinth:\$1:\$2")
        return maven
    }

    fun addGlobal(deps: DependencyHandlerScope)
    fun addFabric(deps: DependencyHandlerScope)
    fun addForge(deps: DependencyHandlerScope)
    fun addNeo(deps: DependencyHandlerScope)
}

interface PublishDependencyHandler {
    fun addShared(deps: DependencyContainer)
    fun addCurseForge(deps: DependencyContainer)
    fun addModrinth(deps: DependencyContainer)
}