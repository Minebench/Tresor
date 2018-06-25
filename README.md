# Tresor
A Vault compatible abstraction library for Bukkit plugins.

Currently a work in progress. Take a look at the lists below at which features are
implemented and should already work.

## Features
- [x] UUID compatible
- [x] Run multiple service provider (economy/permission/etc.)
      and set different providers for different plugins
- [x] Fallback to other service providers
- [ ] (Maybe: BungeeCord support)

## Included Abstractions
- [x] Economy
- [x] Permissions
- [x] Chat
- [ ] UUID/Username storage and lookup
- [ ] Local Namechange storage and lookup
- [ ] Item data storage
- [ ] Friends 
- [ ] Parties/clans/guilds
- [ ] Placeholders
- [ ] Region protection/claim management
- [ ] Single block access management

## License
Tresor is licensed under [LGPL v3.0](https://github.com/Minebench/Tresor/blob/master/LICENSE)
```
Copyright (C) 2017 Max Lee (https://github.com/Phoenix616)

Tresor is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Tresor is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with Tresor.  If not, see <http://www.gnu.org/licenses/>.
```
Tresor includes (modified) files from [Vault](https://github.com/MilkBowl/Vault)
and the [VaultAPI](https://github.com/MilkBowl/VaultAPI) which are licensed under
LGPL v3.0. This origin and the corresponding copyright notice is included in the
used files.
