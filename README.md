# Tresor
A Vault compatible abstraction library for Bukkit plugins.

Currently a work in progress. Take a look at the lists below at which features are
implemented and should already work.

## Features
- [x] UUID compatible
- [x] Run multiple service provider (economy/permission/etc.)
      and set different providers for different plugins
- [x] Fallback to other service providers
- [ ] Check returns of multiple providers
- [ ] (Maybe: BungeeCord support)

## Included Abstractions
- [x] Economy
- [x] Modern Economy using BigDecimal and CompletableFutures
- [x] Permissions
- [ ] Modern, async Permissions provider using CompletableFutures
- [x] Chat
- [ ] UUID/Username/Profile storage and lookup
- [ ] Local Namechange storage and lookup
- [ ] Item data storage
- [ ] Friends 
- [ ] Parties/clans/guilds
- [ ] Component parsing
- [ ] Placeholders
- [ ] Region protection/claim management
- [ ] Single block access management
- [ ] Player statuses (AFK, DND, Vanished, etc)

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

The Tresor-API artifact that other plugins will interact with it licensed under
[LGPL v3.0](https://www.gnu.org/licenses/lgpl-3.0.txt) as noted in its files.

Tresor includes (modified) files from [Vault](https://github.com/MilkBowl/Vault)
and the [VaultAPI](https://github.com/MilkBowl/VaultAPI) which are licensed under
LGPL v3.0. This origin and the corresponding copyright notice is included in the
used files.
