# The Herobrine!

This is my in-progress remake of HiveMC's "The Herobrine!" v2 game-mode that was removed in Java 2.

## Map Setup
This is how to setup maps for The Herobrine.

Your base server directory should include a folder called `maps`. Within this folder, there should be a file called `maps.yaml`. This is how a demo `maps.yaml` should look:
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
