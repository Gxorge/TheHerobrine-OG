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
    WRAITH("Wraith", ChatColor.YELLOW, 7500, 14999),
    SKINWALKER("Skinwalker", null, 15000, 24999),
    MYSTERIA("Mysteria", ChatColor.LIGHT_PURPLE, 25000, 34999),
    WIDOWER("Widower", ChatColor.DARK_AQUA, 35000, 44999),
    PARANORMAL("Paranormal", null, 45000, 999999),
    GHOSTBUSTER("Ghostbuster", null, 100000, 149999),
    SPOOKY("Spooky", null, 150000, 199999),
    ETHEREAL("Ethereal", null, 200000, 249000),
    DEMONHUNTER("DemonHunter", null, 25000, (Integer.MAX_VALUE-1)),
    DEATHBRINGER("Death Bringer", null, Integer.MAX_VALUE, Integer.MAX_VALUE), // reserved for top player
    CHONKER("Chonker", ChatColor.DARK_GRAY, Integer.MAX_VALUE, Integer.MAX_VALUE); // meme

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
        return CHONKER;
    }

    public ChatColor getColor() {
        if (color == null)
            return ChatColor.DARK_GRAY;
        return color;
    }

}
