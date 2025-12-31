# Version 1.0.0 | Alpha 2

- Cleaned up the codebase
- Updated to Fabric API 0.138.4
- Updated to Fabric Loader 0.18.4
- Updated to NeoForge 21.10.64

## Bug fixes
- Fixed a bug where mixin couldn't find variables properly

---

# Version 1.0.0 | Alpha 1

* Cleaned up Utility classes to fit kotlin practices
* Fully independent of Architectury API

## Rewrote Config API

* Rewrote to fit kotlin practices
* Its now possible to add or use premade serializers for your config files (Premade formats: JSON)
* Config Synchronization has been fully rewritten

## Rewrote Networking API

* Rewrote to fit kotlin practices
* Added support for all connection protocols like, **play**, **status**, **login**, and **configuration**.
* Is fully independent of any mod loader's API.
* Custom implementation of Server bundle packets, allowing you to send multiple packets in a single network message.

## Event API

* Rewrote to fit kotlin practices
* Events have priority levels

## Registration API

* Added Renderer Registration for Entities and Block entities