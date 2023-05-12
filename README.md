# The Herobrine!

This is my in-progress remake of HiveMC's "The Herobrine!" v2 game-mode that was removed in Java 2. The core version of the game can be found under the `core` branch. For changes required by the TrueOG network, please see the `trueog` branch.

## Database Requirements
The Herobrine requires 2 databases: a MySQL DB and a Redis DB. Redis is used for storing player kit selections, as it is fast. Player statistics are stored in a MySQL database.

### MySQL Setup
Within the configured database, you should have a table called `hb_stat`. Within this table should be 4 columns:
- `uuid`, type varchar(255) and unique 
- `points`, type int
- `captures`, type int
- `kills`, type int
- `deaths`, type int

The plugin currently does not generate this automatically and therefore it must be done manually.

## Commands and Permissions
- /hbvote [map id] - allows players to vote for a map - no permission
- /hbsetherobrone [player] - set the player to be herobrine - theherobrine.command.setherobrine
- /hbforcestart [time] - force the game to start at a specified time - theherobrine.command.forcestart
- /hbdropshard - force the shard carrier to drop the shard +10 blocks on the x-axis - theherobrine.command.dropshard
- /hbjoin [lobby id] - allows players to join a lobby - no permission
- /hbcreatelobby <configuration id> - create a new lobby - theherobrine.command.createlobby
- /hbdeletelobby <lobby id> - delete a lobby - theherobrine.command.deletelobby
- /hbspectate - switch from playing to spectating whilst in-lobby - theherobrine.command.spectate
- /hbreloadconfigs - reload lobby configs - theherobrine.command.reloadconfigs

### Kit Permissions
Requiring permissions for classic and unlockable kits can be set in the config.yml.

- Archer - theherobrine.kit.classic.archer
- Priest - theherobrine.kit.classic.priest
- Scout - theherobrine.kit.classic.scout
- Wizard - theherobrine.kit.classic.wizard

- Mage - theherobrine.kit.unlockable.mage
- Paladin - theherobrine.kit.unlockable.paladin
- Sorcerer - theherobrine.kit.unlockable.sorcerer

### Other Permissions
- theherobrine.overfill - allow players to join above the limit

## Lobby Setup
Within your `plugins/TheHerobrine-OG` folder you should have a folder called "lobbies". Within this, you can have yaml files for each of your lobby configurations. This is an example configuration:
```yaml
id: "default"         # Used internally for identification, and by the map base (see below)
prefix: "HB"          # Lobby prefix to be used.
minPlayers: 8         # Min players to start a game
maxPlayers: 13        # Max players
startTime: 90         # How long until the game starts when the min is reached
allowOverfill: false  # Can players with the permission overfill the max?
votingMaps: 4         # How many maps should be selected for voting, this should be no more than the amount in the map base config
endVotingAt: 10       # What time does voting end? (in the starting countdown)
autoStartAmount: 1    # How many lobbies should be auto started on boot?
```
This file and directory will be automatically created if they do not exist. (A default config will be created if there is zero in the folder.)

## Map Setup
Your base server directory should include a folder called `maps` (or whatever set in the config.yml). Within this folder, there should be a file for each of your configs (mentioned above) called `<config id>.yaml`. This is how it should look:
```yaml
maps:
  - map1
  - map2
  - map3
```

In your `maps` directory, there should be a folder with the name listed in `<config id>.yaml` with the world data and a file called `mapdata.yaml`.
This files contains map information and the location of the survivor's spawn, herobrine's spawn, the alter location and the shard location.
This is what an example file should look like:
```yaml
name: Map 1
builder: Good Builder
shardMin: -100
shardMax: 1000
datapoints:
  - type: SURVIVOR_SPAWN
    x: -2
    y: 156
    z: 925
  - type: HEROBRINE_SPAWN
    x: 110
    y: 140
    z: 854
  - type: ALTER
    x: -6
    y: 157
    z: 925
  - type: SHARD_SPAWN
    x: 110
    y: 149
    z: 834
```
