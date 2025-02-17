![banner.png](https://github.com/ThePandaOliver/Readme-Assets/blob/main/pandalib/banner.png?raw=true)

[![Discord](https://img.shields.io/discord/1021703635178115122?style=for-the-badge&logo=discord&label=Discord&labelColor=black&color=lightblue)](https://discord.gg/wjPt4vEfXb)
[![Modrinth](https://img.shields.io/modrinth/dt/mEEGbEIu?style=for-the-badge&logo=modrinth&label=Modrinth&labelColor=black&color=green)](https://modrinth.com/mod/pandalib)
[![Curseforge](https://img.shields.io/curseforge/dt/975460?style=for-the-badge&logo=curseforge&label=Curseforge&labelColor=black&color=red)](https://www.curseforge.com/minecraft/mc-mods/pandalib)
[![GitHub](https://img.shields.io/github/downloads/PandaDap2006/PandaLib/total?style=for-the-badge&logo=github&label=Github&labelColor=black&color=white)](https://github.com/PandaDap2006/PandaLib)

[![Fabric API](https://img.shields.io/badge/Fabric%20API-REQUIRED%20for%20Fabric-1?style=for-the-badge&labelColor=black&color=gold)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## About:
This Library was created to easily share code between The Panda Oliver's mods.

### Current Features:
- Embedded JOML
- Config API
  - Automatic Config synchronization

### In development:
- Multi-Loader Framework
- Custom Model Rendering API

### Planned Features:
- Config Menu API
- Wiki / Docs
- And more to come

---
## Developer

Groovy DSL
```groovy
repository {
	maven { url = "https://nexus.pandasystems.dev/repository/maven-public/" }
}

dependency {
	// Fabric Loom / Architectury Loom
	modApi "me.pandamods:pandalib-<Platform>:mc<MC Version>-<Mod Version>"
	// NeoForge
	implementation "me.pandamods:pandalib-<Platform>:mc<MC Version>-<Mod Version>"
	// Forge
	implementation fg.deobf("me.pandamods:pandalib-<Platform>:mc<MC Version>-<Mod Version>")
}
```

Kotlin DSL
```kotlin
repository {
	maven("https://nexus.pandasystems.dev/repository/maven-public/")
}

dependency {
	// Fabric Loom / Architectury Loom
	modApi("me.pandamods:pandalib-<Platform>:mc<MC Version>-<Mod Version>")
	// NeoForge
	implementation("me.pandamods:pandalib-<Platform>:mc<MC Version>-<Mod Version>")
	// Forge
	implementation(fg.deobf("me.pandamods:pandalib-<Platform>:mc<MC Version>-<Mod Version>"))
}
```
Replace \<Platform> with either "common", "fabric", "forge" or "neoforge"\
Replace \<MC Version> with your desired supported version of minecraft.\
Replace \<Mod Version> with your desired version of PandaLib.

---
## Support me
[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/S6S0WO38H)

## Advertisement
Thanks to **Jetbrains** for supporting this project with their open source program.\
[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jetbrains.svg" width=300px>](https://jb.gg/OpenSourceSupport)

Thanks to **Kinetic Hosting** for supporting this project 
![Partner Banner](https://github.com/PandaDap2006/PandaDap2006/blob/main/assets_for_readme/kinetic_hosting_banner_v2.png?raw=true)

**[Click here and use code "PANDA" to get 15% of your first month](https://t.ly/B1Kui)**

## License
The project is licensed under the GNU LGPLv3