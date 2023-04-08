package uk.hotten.herobrine.stat;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum GameRank {

    SPIRIT(ChatColor.GRAY + "Spirit", 0, 99),
    DEFENDER(ChatColor.DARK_AQUA + "Defender", 100, 499),
    CASPER(ChatColor.AQUA + "Casper", 500, 1499),
    BANSHEE(ChatColor.LIGHT_PURPLE + "Banshee", 1500, 2499),
    SOULSEEKER(ChatColor.GOLD + "Soul Seeker", 2500, 3999),
    HAUNTER(ChatColor.YELLOW + "Haunter", 4000, 4999),
    POSSESSED(ChatColor.RED + "Possessed", 5000, 7499),
    WRAITH(ChatColor.AQUA + "Wraith", 7500, 9999),
    PHANTOM(ChatColor.AQUA + "Wraith", 10000, 14999),
    SKINWALKER(ChatColor.YELLOW + "Skinwalker", 15000, 24999),
    MYSTERIA(ChatColor.LIGHT_PURPLE + "Mysteria", 25000, 34999),
    WIDOWER(ChatColor.DARK_AQUA + "Widower", 35000, 44999),
    PARANORMAL(ChatColor.BLUE + "Paranormal", 45000, 59999),
    SPECTRAL(ChatColor.DARK_GREEN + "Spectral", 60000, 79999),
    MYTHICAL(ChatColor.GOLD + "Mythical", 80000, 99999),
    GHOSTBUSTER(ChatColor.WHITE + "GhostBuster", 100000, 149999),
    SPOOKY("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Spooky", 150000, 199999),
    ETHEREAL("" + ChatColor.RED + ChatColor.BOLD + "Ethereal", 200000, 249999),
    DEMONHUNTER("" + ChatColor.GRAY + ChatColor.BOLD + "DemonHunter", 250000, 299999),
    DIVINE("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Divine", 300000, (Integer.MAX_VALUE-1)),
    DEATHBRINGER("" + ChatColor.DARK_RED + ChatColor.MAGIC + "# " + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + "Death Bringer", Integer.MAX_VALUE, Integer.MAX_VALUE); // reserved for top player

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
