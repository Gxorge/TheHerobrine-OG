package uk.hotten.herobrine.stat;

import lombok.Getter;

public enum GameRank {

    SPIRIT("&7Spirit", 0, 99),
    DEFENDER("&3Defender", 100, 499),
    CASPER("&bCasper", 500, 1499),
    BANSHEE("&dBanshee", 1500, 2499),
    SOULSEEKER("&6Soul Seeker", 2500, 3999),
    HAUNTER("&eHaunter", 4000, 4999),
    POSSESSED("&cPossessed", 5000, 7499),
    WRAITH("&bWraith", 7500, 9999),
    PHANTOM("&bPhantom", 10000, 14999),
    SKINWALKER("&eSkinwalker", 15000, 24999),
    MYSTERIA("&dMysteria", 25000, 34999),
    WIDOWER("&3Widower", 35000, 44999),
    PARANORMAL("&1Paranormal", 45000, 59999),
    SPECTRAL("&2Spectral", 60000, 79999),
    MYTHICAL("&6Mythical", 80000, 99999),
    GHOSTBUSTER("&fGhostBuster", 100000, 149999),
    SPOOKY("&5&lSpooky", 150000, 199999),
    ETHEREAL("&c&lEthereal", 200000, 249999),
    DEMONHUNTER("&7&lDemonHunter", 250000, 299999),
    DIVINE("&d&lDivine", 300000, (Integer.MAX_VALUE-1)),
    DEATHBRINGER("&4&k# &r&c&lDeath Bringer", Integer.MAX_VALUE, Integer.MAX_VALUE); // reserved for top player

    @Getter private String display;
    @Getter int lowBound;
    @Getter int highBound;

    private GameRank(String display, int lowBound, int highBound) {
        this.display = display;
        this.lowBound = lowBound;
        this.highBound = highBound;
    }

    public static GameRank findRank(int points) {
        for (GameRank rank : GameRank.values()) {
            if (points >= rank.getLowBound() && points <= rank.getHighBound())
                return rank;
        }
        return DEFENDER;
    }
}
