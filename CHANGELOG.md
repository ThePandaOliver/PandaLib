# Version 1.0.0

* Cleaned up Utility classes to fit kotlin practices
* Fully independent of Architectury API

## Rewrote Config API

* Rewrote to fit kotlin practices
* Its now possible to add or use premade serializers for your config files (Premade formats: JSON)

## Rewrote Networking API

* Rewrote to fit kotlin practices
* Added support for all connection protocols like, **play**, **status**, **login**, and **configuration**.
* Is fully independent of any mod loader's API.
* Custom implementation of Server bundle packets, allowing you to send multiple packets in a single network message.

## Event API

* Reworked event API to fit kotlin practices
* Events have priority levels

## Registration API

* Added Renderer Registration for Entities and Block entities