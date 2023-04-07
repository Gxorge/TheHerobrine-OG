# The Herobrine!

This is my in-progress remake of HiveMC's "The Herobrine!" v2 game-mode that was removed in Java 2. This branch contains changes required for the TrueOG network, who this project is now being maintained for. The unedited version can be found in the `core` branch and downloads can be found for any release tags that end in `-core`.

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
- /vote [map id] - allows players to vote for a map - no permission
- /setherobrone <player> - set the player to be herobrine - theherobrine.command.setherobrine
- /forcestart [time] - force the game to start at a specified time - theherobrine.command.forcestart
- /dropshard - force the shard carrier to drop the shard +10 blocks on the x-axis - theherobrine.command.dropshard

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

## Map Setup
Your base server directory should include a folder called `maps` (or whatever set in the config.yml). Within this folder, there should be a file called `maps.yaml`. This is how a demo `maps.yaml` should look:
```yaml
maps:
  - map1
  - map2
  - map3
```

In your `maps` directory, there should be a folder with the name listed in `maps.yaml` with the world data and a file called `mapdata.yaml`.
This files contains map information and the location of the survivor's spawn, herobrine's spawn, the alter location and the shard location.
This is what an example file should look like:
```yaml
name: Map 1
builder: Good Builder
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
