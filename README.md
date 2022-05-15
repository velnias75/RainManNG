# RainManNG
<img src="https://github.com/velnias75/RainManNG/raw/master/icon.svg" height="64px"> **Bukkit plugin for controlling rain frequency and length.**

This plugin hooks into weather events to let you configure the probability of rain or snow actually happening and scaling how long it lasts for, to cut down on excessive rain streaks (or make it rain for a long time, if that's what you prefer).

Originally based on [ABCRic's RainMan](https://dev.bukkit.org/projects/rainman).

## Commands
| **Command** |	**Description** |	**Permission** |
| --- | --- | --- |
| /rainmanng reload |	Reloads config | rainmanng.admin |
| /rainmanng disable-weather | Disables weather at all | rainmanng.admin |
| /rainmanng save | Saves the config | rainmanng.admin |
| /rainmanng show-config | Shows the config | rainmanng.admin |
| /rainmanng rain-chance [\<value\>] | Shows/sets the rain chance | rainmanng.admin |
| /rainmanng rain-length-scale [\<value\>] | Shows/sets the rain duration | rainmanng.admin |
| /weather (clear\|rain\|thunder) [\<duration\>] |	Sets the weather | rainmanng.weather |

## Configuration
See [here](src/main/resources/config.yml).
