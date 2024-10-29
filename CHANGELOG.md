# Changelog 0.5 Beta

## Config API
* Converting json elements into nbt tags when sending them over via packets

## Technical changes
* Restructured the whole project

## License Update
* Reverted license to GNU LGPLv3 (previously GNU GPLv3)

# Developer Info
* Replaced the old config codecs with a new `ConfigCodec` class
* Added a new util class `NBTUtils` which contains conversion functions for converting between nbt tags and gson json elements