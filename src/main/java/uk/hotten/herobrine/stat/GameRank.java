package uk.hotten.herobrine.stat;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum GameRank {

    SPIRIT("Spirit", ChatColor.GRAY, 0, 99),
    DEFENDER("Defender", ChatColor.DARK_AQUA, 100, 499),
    CASPER("Casper", ChatColor.AQUA, 500, 1499),
    BANSHEE("Banshee", ChatColor.LIGHT_PURPLE, 1500, 2499),
    SOULSEEKER("Soul Seeker", ChatColor.GOLD, 2500, 3999),
    HAUNTER("Haunter", ChatColor.YELLOW, 4000, 4999),
    POSSESSED("Possessed", ChatColor.RED, 5000, 7499),
    WRAITH("Wraith", ChatColor.AQUA, 7500, 14999),
    SKINWALKER("Skinwalker", ChatColor.YELLOW, 15000, 24999),
    MYSTERIA("Mysteria", ChatColor.LIGHT_PURPLE, 25000, 34999),
    WIDOWER("Widower", ChatColor.DARK_AQUA, 35000, 44999),
    PARANORMAL("Paranormal", ChatColor.BLUE, 45000, 59999),
    SPECTRAL("Spectral", ChatColor.DARK_GREEN, 60000, 79999),
    MYTHICAL("Mythical", ChatColor.GOLD, 80000, 99999),
    GHOSTBUSTER("GhostBuster", ChatColor.GRAY, 100000, 149999),
    SPOOKY("Spooky", ChatColor.DARK_PURPLE, 150000, 199999),
    ETHEREAL("Ethereal", ChatColor.RED, 200000, 249999),
    DEMONHUNTER("DemonHunter", ChatColor.GRAY, 250000, 299999),
    DIVINE("Divine", ChatColor.GRAY, 300000, (Integer.MAX_VALUE-1)),
    DEATHBRINGER("Death Bringer", ChatColor.RED, Integer.MAX_VALUE, Integer.MAX_VALUE); // reserved for top player

    @Getter private String display;
    private ChatColor color;
    @Getter int lowBound;
    @Getter int highBound;

    private GameRank(String display, ChatColor color, int lowBound, int highBound) {
        this.display = display;
        this.color = color;
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

    public ChatColor getColor() {
        if (color == null)
            return ChatColor.DARK_GRAY;
        return color;
    }

}
