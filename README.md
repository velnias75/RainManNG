# RainManNG ![Spiget Version](https://img.shields.io/spiget/version/102026?label=Latest%20version) [![CodeQL](https://github.com/velnias75/RainManNG/actions/workflows/codeql.yml/badge.svg)](https://github.com/velnias75/RainManNG/actions/workflows/codeql.yml)
<img src="https://github.com/velnias75/RainManNG/raw/master/icon.svg" height="64px"> **Spigot plugin for controlling rain frequency and length.**

This plugin hooks into weather events to let you configure the probability of rain or snow actually happening and scaling how long it lasts for, to cut down on excessive rain streaks (or make it rain for a long time, if that's what you prefer).

Originally based on [ABCRic's RainMan](https://dev.bukkit.org/projects/rainman).

## Commands
| **Command** |	**Description** |	**Permission** |
| --- | --- | --- |
| /rainmanng reload |	Reloads config | rainmanng.admin |
| /rainmanng disable-weather | Disables weather at all | rainmanng.admin |
| /rainmanng offline-weather | Allow weather if nobody is online | rainmanng.admin |
| /rainmanng save | Saves the config | rainmanng.admin |
| /rainmanng show-config | Shows the config | rainmanng.admin |
| /rainmanng get-weather | Gets the current weather | rainmanng.admin |
| /rainmanng rain-chance [\<value\>] | Shows/sets the rain chance | rainmanng.admin |
| /rainmanng rain-length-scale [\<value\>] | Shows/sets the rain duration | rainmanng.admin |
| /weather (clear\|rain\|thunder) [\<duration\>] |	Sets the weather | rainmanng.weather |

## Configuration
See [here](src/main/resources/config.yml).

### Building
After *checkout/clone* do
`git submodule update --init --recursive` once, than `./gradlew clean build`.

---

![](https://bstats.org/signatures/bukkit/RainManNG.svg)
