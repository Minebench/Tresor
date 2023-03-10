# Tresor

![](https://img.shields.io/github/license/minebench/tresor) ![](https://buttons.phoenix616.dev/TresorWorkTime.svg) [![](https://img.shields.io/github/milestones/progress-percent/minebench/tresor/1?label=progress)](https://github.com/Minebench/Tresor/milestone/1) [![](https://img.shields.io/github/issues-raw/minebench/tresor)](https://github.com/Minebench/Tresor/issues)

A Vault compatible abstraction library for Bukkit plugins.

It is currently a work in progress. If you want to help out please [contact me](https://phoenix616.dev/discord)!

## Features
- [x] UUID compatible
- [x] Run multiple service provider (economy/permission/etc.)
      and **set different providers for different plugins!**
- [x] ServiceHook utility to manage accessing service providers properly
- [x] Fallback to other service providers
- [ ] Check returns of multiple providers
- [ ] (Maybe: BungeeCord support)

## Further Information
- [Why Tresor?](https://github.com/Minebench/Tresor/wiki)
- [Offered Service Abstractions](https://github.com/Minebench/Tresor/wiki/Offered-Services)
- [Supported Plugins](https://github.com/Minebench/Tresor/wiki/Supported-Plugins)
- [How to correctly hook into Tresor](https://github.com/Minebench/Tresor/wiki/Hook-into-Tresor)
- [Planning Project](https://github.com/Minebench/Tresor/projects/1)
- [Development builds](https://ci.minebench.de/job/Tresor)

## License
Tresor is distributed under the terms of the [GPL v3.0](https://github.com/Minebench/Tresor/blob/master/LICENSE)
```
Copyright (C) 2020 Max Lee (https://github.com/Phoenix616)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```

The Tresor-API artifact that other plugins will interact with is licensed under
[LGPL v3.0](https://www.gnu.org/licenses/lgpl-3.0.txt) as noted in its files.

Tresor includes (modified) files from [Vault](https://github.com/MilkBowl/Vault)
and the [VaultAPI](https://github.com/MilkBowl/VaultAPI) which are licensed under
LGPL v3.0. This origin and the corresponding copyright notice is included in the
used files.
