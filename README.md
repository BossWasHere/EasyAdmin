---

**\[PRE-RELEASE]** This plugin is still in early development and is not ready for production usage!

---

# EasyAdmin

A complete and up-to-date toolkit for server administration and moderation.

## Platforms

- [x] Spigot
- [x] BungeeCord (and Waterfall)
- [ ] Velocity (to be added in the future)
- [ ] Sponge (may be added in the future)
- [ ] Fabric (may be added in the future)
- [ ] Forge (may be added in the future)
- [ ] Web API (to be added in the future)

## Features

- [x] Out-of-the-box ready
- [x] Simple moderation commands
- [x] Support for IP-based bans and mutes
- [x] Support for temporary bans and mutes
- [x] Multiple operation modes
    - Proxy-only
    - Standalone
    - Bridged
- [x] Multiple data storage backend options
    - MySQL
    - PostgreSQL
    - H2 (local)
    - SQLite (local)
- [x] Completely scalable
- [x] Highly customizable
    - Documented configuration
- [x] Configurable messages **per locale**
    - Support for multi-language servers
- [x] Contextual modifiers (regex support)
    - Global (across the network)
    - Server (local to the server)
    - In-server (world names)
- [x] Block specific (regex) commands when muted (with context support)
- [x] Allow specific (regex) messages when muted (with context support)
- [x] Automated actions
- [x] Instantaneous cross-network updates
- [x] Resolve player profiles for offline/never connected players
- [ ] Bulk operations (coming soon)

## For Larger Networks

- [x] Complete platform-independent plugin API
    - Create and update records
    - Event-driven
    - Fully documented
- [x] Supervisor and training modes
    - Withhold certain operations for approval
    - Ensure proper staff usage
- [ ] Optional REST web API (coming soon)
- [ ] Remote web dashboard (coming soon)
    - Perform moderation operations
    - System performance monitoring
    - Event logs
    - Live chat

## Commands

|  Command  | Description                                                                 | Optional Scopes                          |
|:---------:|:----------------------------------------------------------------------------|:-----------------------------------------|
|   `ban`   | Prevents a player from connecting to one or all servers.                    | Global/Contextual + Temporary + IP-based |
|  `mute`   | Prevents a player from chatting in one or all servers.                      | Global/Contextual + Temporary + IP-based |
|  `kick`   | Forcefully ejects a player from their current server or the entire network. | Global/Local                             |
|  `warn`   | Sends the player a warning message.                                         | N/A                                      |
| `comment` | Adds a comment to the player's profile.                                     | N/A                                      |
| `lookup`  | Retrieves a player's history.                                               | IP-based                                 |
|  `unban`  | Terminates an existing ban for a player.                                    | (as above)                               |
| `unmute`  | Terminates an existing mute for a player.                                   | (as above)                               |

## Permissions

All permissions below are prefixed with `easyadmin`.

|  Operation  |                          Permission                           | Description                                                                                                                                                                   |
|:-----------:|:-------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|     Ban     |   `.ban`<br>`.ban.context`<br>`.ban.temporary`<br>`.ban.ip`   | Can ban players from the network.<br>Can ban players in a specifc context.<br>Can ban players temporarily.<br>Can ban players connecting from a specific IP address.          |
|    Mute     | `.mute`<br>`.mute.context`<br>`.mute.temporary`<br>`.mute.ip` | Can mute players througout the network.<br>Can mute players in a specifc context.<br>Can mute players temporarily.<br>Can mute players connecting from a specific IP address. |
|    Kick     |                   `.kick`<br>`.kick.global`                   | Can kick players from their current server<br>Can kick players from the network.                                                                                              |
|   Warning   |                            `.warn`                            | Can warn players.                                                                                                                                                             |
|   Comment   |                          `.comment`                           | Can add comments to player profiles.                                                                                                                                          |
|   Lookup    |                  `.lookup`<br>`.stafflookup`                  | Can lookup player profiles.<br>Can lookup staff profiles.                                                                                                                     |
| ~*Immunity* |                `.immune.[operation].[scopes]`                 | Prevents the given operation from being applied to the target player (e.g. `.immune.ban.*` will grant immunity to all bans).                                                  |

## Building

Requirements:

- Java 16 or higher
- Git

```bash
git clone git@github.com:BossWasHere/EasyAdmin.git
cd EasyAdmin/
./gradlew build
```

Platform-specific JARs will be generated in `[platform]/build/libs/`.

## Contributing

You are welcome to create pull requests and report issues!

(TODO: Add contribution guidelines, project layout, style guides, issue templates, etc.)

## License

EasyAdmin is licensed under the [MIT license](https://github.com/BossWasHere/EasyAdmin/blob/main/LICENSE).