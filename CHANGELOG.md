# Version 1.0.0
* Cleaned up Utility classes to fit kotlin practices
* Fully independent of Architectury API

## Rewrote Config API
* Its now possible to add or use premade serializers for your config files (Premade formats: JSON)

## Rewrote Networking API
* Added support for all connection protocols like, **play**, **status**, **login**, and **configuration**.
* Is fully independent of any mod loader's API.
* Server bundle packets are now supported, allowing you to send multiple packets in a single network message.

## Event API
* Added 2 Event types `Event` and `CancellableEvent`
* Events have priority levels and can scale infinitely event in negatives (Infinite as in the integer range)