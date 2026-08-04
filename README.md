![banner.png](assets/banner.png)

> [![Discord](https://img.shields.io/discord/1021703635178115122?style=for-the-badge&logo=discord&label=Discord&labelColor=black&color=lightblue)](https://discord.gg/wjPt4vEfXb)
> [![Modrinth](https://img.shields.io/modrinth/dt/mEEGbEIu?style=for-the-badge&logo=modrinth&label=Modrinth&labelColor=black&color=green)](https://modrinth.com/mod/pandalib)
> [![Curseforge](https://img.shields.io/curseforge/dt/975460?style=for-the-badge&logo=curseforge&label=Curseforge&labelColor=black&color=red)](https://www.curseforge.com/minecraft/mc-mods/pandalib)
> [![GitHub](https://img.shields.io/github/downloads/PandaDap2006/PandaLib/total?style=for-the-badge&logo=github&label=Github&labelColor=black&color=white)](https://github.com/PandaDap2006/PandaLib)
>
> [![Fabric API](https://img.shields.io/badge/Fabric%20API-REQUIRED%20for%20Fabric-1?style=for-the-badge&labelColor=black&color=gold)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## About:

PandaLib is a library mod for Minecraft that provides various APIs and utilities to simplify mod development.

The library is written in Kotlin, and some APIs might not be fully supported by Java.

### Current Features:

- Config API
	- Premade formats: JSON
- Networking API
	- Play phase support
- Embedded libraries
	- Embedded Kotlin libraries
		- Kotlin Standard Library v2.3.0
		- Reflect v2.3.0
		- Coroutines v1.10.2
		- Serialization v1.9.0
          - Json
          - Cbor
		- IO v0.8.2
          - Bytestring
		- DateTime v0.7.1

### Future Features:

- Config Serializers (TOML, YAML, XML, Properties)
- Config Menu API
- Class Handles and Wrappers (In development)
- Networking Configuration phase support
- Common Config Synchronization
- Wiki / Docs

---

## Supported Versions

| Support tier     | Game version | Updates                           | Release policy                                                                                                |
|------------------|-------------:|-----------------------------------|---------------------------------------------------------------------------------------------------------------|
| **Current**      |         26.2 | New features and bug fixes        | Updated whenever releases are ready. Builds may contain issues, which will be addressed in later updates.     |
| **Stable (LTS)** |       1.21.1 | Bug fixes and stabilised features | Bug fixes are prioritised. New features are added only after they have been tested and stabilised in Current. |
| **Maintenance**  |       1.20.1 | Bug fixes only                    | No new features. Updates are limited to important bug fixes and compatibility fixes.                          |

Support moves from **Current** → **Stable** → **Maintenance** as newer game versions become established.

- **Current** targets the latest Minecraft version.
- **Stable** targets the most widely adopted older Minecraft version.
- **Maintenance** targets the previously widely adopted Minecraft version.
- Support is not tied to a fixed release schedule; updates are published when they are ready.

---

## Advertisement:

> ### Thanks to **Kinetic Hosting** for supporting this project
> [![Partner Banner](https://github.com/ThePandaOliver/ThePandaOliver/blob/main/assets_for_readme/Support/kinetic_hosting_banner.png?raw=true)](https://billing.kinetichosting.com/aff.php?aff=476)
>
> Every purchased server via my [affiliate link](https://billing.kinetichosting.com/aff.php?aff=476) will help support me and my work.

## License

The project is licensed under the GNU LGPLv3
