package moe.gabriella.herobrine.utils;

import lombok.Getter;

public enum WinType {

    SURVIVORS("§eThe world is safe again! Thank you!", "§fThe world is saved!"),
    HEROBRINE("§7The world is forever in danger... Goodbye.", "§7The world is in great danger...");

    @Getter private String desc;
    @Getter private String titleDesc;

    private WinType(String desc, String titleDesc) {
        this.desc = desc;
        this.titleDesc = titleDesc;
    }

}
