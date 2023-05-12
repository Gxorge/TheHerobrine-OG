package uk.hotten.herobrine.lobby.data;

import lombok.Getter;

public class LobbyConfig {

    @Getter private String id;
    @Getter private String prefix;
    @Getter private int minPlayers;
    @Getter private int maxPlayers;
    @Getter private int startTime;
    @Getter private boolean allowOverfill;
    @Getter private int votingMaps;
    @Getter private int endVotingAt;
    @Getter private int autoStartAmount;

    public LobbyConfig(String id, String prefix, int minPlayers, int maxPlayers, int startTime,
                       boolean allowOverfill, int votingMaps, int endVotingAt, int autoStartAmount) {
        this.id = id;
        this.prefix = prefix;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.startTime = startTime;
        this.allowOverfill = allowOverfill;
        this.votingMaps = votingMaps;
        this.endVotingAt = endVotingAt;
        this.autoStartAmount = autoStartAmount;
    }

    public LobbyConfig() { }

}
