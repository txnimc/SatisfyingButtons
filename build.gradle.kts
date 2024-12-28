import toni.blahaj.*
import toni.blahaj.api.*


val templateSettings = object : BlahajSettings {
	// -------------------- Dependencies ---------------------- //
	override val depsHandler: BlahajDependencyHandler get() = object : BlahajDependencyHandler {
		override fun addGlobal(mod : ModData, deps: DependencyHandler) {
			deps.modImplementation("toni.txnilib:${mod.loader}-${mod.mcVersion}:1.0.21")
		}

		override fun addFabric(mod : ModData, deps: DependencyHandler) {
			when(mod.mcVersion){
				"1.21.4"->{
					deps.modImplementation(modrinth("modmenu", "13.0.0-beta.1"))
					deps.modCompileOnly(modrinth("fancymenu", "3.3.4-1.21.4-fabric"))
					deps.modCompileOnly(modrinth("melody", "1.0.10-1.21-fabric"))
					deps.modCompileOnly(modrinth("konkrete", "1.9.9-1.21.4-fabric"))
				}
				"1.21.1"->{
					deps.modImplementation(modrinth("modmenu", "11.0.2"))
					deps.modCompileOnly(modrinth("fancymenu", "3.2.5-1.21-fabric"))
					deps.modCompileOnly(modrinth("melody", "1.0.10-1.21-fabric"))
					deps.modCompileOnly(modrinth("konkrete", "1.9.9-1.21-neoforge"))
				}
				"1.20.1"->{
					deps.modImplementation(modrinth("fancymenu", "3.2.3-1.20.1-fabric"))
					deps.modImplementation(modrinth("melody", "1.0.4-1.20.1-1.20.4-fabric"))
					deps.modImplementation(modrinth("konkrete", "1.8.1-1.20.1-fabric"))
				}
			}

		}

		override fun addForge(mod : ModData, deps: DependencyHandler) {
			deps.compileOnly(deps.annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")!!)
			deps.include(deps.implementation("io.github.llamalad7:mixinextras-forge:0.3.5")!!)

			deps.modCompileOnly(modrinth("fancymenu", "3.2.3-1.20.1-forge"))
			deps.modCompileOnly(modrinth("melody", "1.0.3-1.20.1-1.20.4-forge"))
			deps.modCompileOnly(modrinth("konkrete", "1.8.0-1.20-1.20.1-forge"))
		}

		override fun addNeo(mod : ModData, deps: DependencyHandler) {
			deps.modCompileOnly(modrinth("fancymenu", "3.2.5-1.21-neoforge"))
			deps.modCompileOnly(modrinth("melody", "1.0.10-1.21-neoforge"))
			deps.modCompileOnly(modrinth("konkrete", "1.9.9-1.21-neoforge"))
		}
	}

	// ---------- Curseforge/Modrinth Configuration ----------- //
	// For configuring the dependecies that will show up on your mod page.
	override val publishHandler: BlahajPublishDependencyHandler get() = object : BlahajPublishDependencyHandler {
		override fun addShared(mod : ModData, deps: DependencyContainer) {
			deps.requires("txnilib")
			if (mod.isFabric) {
				deps.requires("fabric-api")

			}
		}

		override fun addCurseForge(mod : ModData, deps: DependencyContainer) {

		}

		override fun addModrinth(mod : ModData, deps: DependencyContainer) {

		}
	}
}

plugins {
	`maven-publish`
	application
	id("toni.blahaj") version "1.0.15"
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("dev.kikugie.j52j") version "1.0"
	id("dev.architectury.loom")
	id("me.modmuss50.mod-publish-plugin")
	id("systems.manifold.manifold-gradle-plugin")
}

blahaj {
	sc = stonecutter
	settings = templateSettings
	init()
}

// Dependencies
repositories {
	maven("https://www.cursemaven.com")
	maven("https://api.modrinth.com/maven")
	maven("https://thedarkcolour.github.io/KotlinForForge/")
	maven("https://maven.kikugie.dev/releases")
	maven("https://maven.txni.dev/releases")
	maven("https://jitpack.io")
	maven("https://maven.neoforged.net/releases/")
	maven("https://maven.terraformersmc.com/releases/")
	maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
	maven("https://maven.parchmentmc.org")
	maven("https://maven.su5ed.dev/releases")
	maven("https://maven.su5ed.dev/releases")
	maven("https://maven.fabricmc.net")
	maven("https://maven.shedaniel.me/")
}