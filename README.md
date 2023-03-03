# Tresor

![](https://img.shields.io/github/license/minebench/tresor) ![](https://buttons.phoenix616.dev/TresorWorkTime.svg) [![](https://img.shields.io/github/milestones/progress-percent/minebench/tresor/1?label=progress)](https://github.com/Minebench/Tresor/milestone/1) [![](https://img.shields.io/github/issues-raw/minebench/tresor)](https://github.com/Minebench/Tresor/issues)

A Vault compatible abstraction library for Bukkit plugins.

Currently a work in progress. If you want to help out please [contact me](https://phoenix616.dev/discord)!

Take a look at the lists below at which features are implemented and should already work.

## Features
- [x] UUID compatible
- [x] Run multiple service provider (economy/permission/etc.)
      and **set different providers for different plugins!**
- [x] ServiceHook utility to manage accessing service providers properly
- [x] Fallback to other service providers
- [ ] Check returns of multiple providers
- [ ] (Maybe: BungeeCord support)

## Included Abstractions
- [x] Economy
- [x] Modern Economy using BigDecimal and CompletableFutures
- [x] Permissions
- [ ] Modern, async Permissions provider using CompletableFutures
- [x] Chat
- [ ] Modern, async Chat provider using CompletableFutures
- [ ] UUID/Username/Profile storage and lookup
- [ ] Local Namechange storage and lookup
- [ ] Item data storage
- [ ] Player vaults/virtual backpacks
- [ ] Friends 
- [ ] Parties/clans/guilds
- [ ] Component parsing
- [ ] Placeholders
- [ ] Region protection/claim management
- [ ] Single block protection management
- [ ] Player statuses (AFK, DND, Vanished, etc)
- [x] Player authentication
- [ ] Logging (player info and blocks, including querying and rollback)

See the [project](https://github.com/Minebench/Tresor/projects/1) for a better overview.

## Supported plugins
Any plugin can implement and register a provider the same way as it was
 possible with Vault (TODO: instructions on wiki).

For convenience implementations of providers for the following popular
 plugins are provided directly in Tresor:

- [ ] LuckPerms (Permissions, Modern Permissions, Chat, UUID)
- [ ] BungeePerms (Permissions, Modern Permissions, Chat, UUID)
- [ ] GroupManager (Permissions, Modern Permissions, Chat, UUID)
- [ ] EssentialsX (Economy, Modern Economy, Chat, UUID, Namechanges, Player Statuses, Regions)
- [x] CraftConomy3 (Economy, Modern Economy)
- [ ] ChestShop (Item data storage, Protection)
- [ ] Bolt (Protection)
- [ ] LWCX (Protection)
- [ ] Lockette (Protection)
- [ ] WorldGuard (Regions, Protection)
- [ ] PlotSquared (Regions)
- [ ] RedProtect (Regions)
- [ ] Factions (Regions, Clans)
- [ ] Towny (Regions, Clans)
- [ ] PlaceholderAPI (Placeholders)
- [ ] MineDown (Component Parsing)
- [ ] MiniMessage (Component Parsing)
- [ ] LogBlock (Logging, UUID)
- [ ] CoreProtect (Logging, UUID)
- [x] AuthMe (Authentication)
- [x] MCAuthenticator (Authentication)
- [x] OpeNLogin (Authentication)
- [x] nLogin (Authentication)

Want support for a specific plugin? Open an issue to request it or even better:
 Write a provider implementation yourself and open a pull request! :)

## How to hook into Tresor

Tresor uses the ServiceManager which is built into Bukkit/Spigot/Paper.

E.g. if you want to hook into the ModernEconomy service you can use the ServiceHook utility like this in your onEnable:

```java
public class MyPlugin extends JavaPlugin implements Listener {
    private ServiceHook<ModernEconomy> economyHook;

    public void onEnable() {
        // create the hook
        economyHook = new ServiceHook<>(this, ModernEconomy.class);
    }

    public ModernEconomy getEconomy() {
        // get the provider from the hook, might return null if none exists!
        return economyHook.getProvider();
    }
}
```

This utility can be used for all services, not just Tresor ones!

### Manually

If you want to do the hooking manually then make sure to get the provider onEnable and then listen on the
ServiceRegisterEvent and ServiceUnregisterEvent! (This can be used for all services!)

```java
public class MyPlugin extends JavaPlugin implements Listener {
    private TresorAPI tresorApi = null;
    private ModernEconomy economy = null;

    private void onEnable() {
        updateEconomyProvider();
        // get the TresorAPI to make sure we use our extended services manager
        tresorApi = (TresorAPI) getServer().getPlugin("Tresor");
    }

    private void updateEconomyProvider() {
        // get the service provider registration for this plugin
        economy = tresorApi.getServicesManager().load(this, ModernEconomy.class);
    }

    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent event) {
        if (event.getProvider().getProvider() instanceof ModernEconomy) {
            updateEconomyProvider();
        }
    }

    @EventHandler
    public void onServiceUnregister(ServiceUnregisterEvent event) {
        if (event.getProvider().getProvider() instanceof ModernEconomy) {
            updateEconomyProvider();
        }
    }
}
```

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
