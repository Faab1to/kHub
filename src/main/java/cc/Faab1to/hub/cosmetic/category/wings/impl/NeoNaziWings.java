package cc.invictusgames.hub.cosmetic.category.wings.impl;

import cc.invictusgames.hub.cosmetic.category.CosmeticCategory;
import cc.invictusgames.hub.cosmetic.category.wings.PatternWings;
import cc.invictusgames.hub.cosmetic.category.wings.WingsCategory;
import cc.invictusgames.hub.util.ParticleMeta;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public class NeoNaziWings extends PatternWings {

    private static final String[][] PATTERN = new String[][]{
            {"-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "44", "44", "44", "44", "44", "44"},
            {"-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "-1"},
            {"-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "-1"},
            {"-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "-1"},
            {"-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "-1"},
            {"-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "-1"},
            {"-1", "-1", "-1", "44", "44", "44", "44", "44", "44", "44", "44", "44", "44", "44", "44", "44", "44"},
            {"-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "44"},
            {"-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "44"},
            {"-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "44"},
            {"-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "44"},
            {"-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1", "44", "-1", "-1", "-1", "-1", "44"},
            {"-1", "-1", "-1", "44", "44", "44", "44", "44", "44", "44", "44", "44", "-1", "-1", "-1", "-1", "44"}
    };
    
    private static final MaterialData ICON = new MaterialData(Material.BARRIER);
    private static final String[] DESCRIPTION = new String[]{
            "&7Equip yourself with the symbol of a",
            "&7Neo Nazi Solider! 卍 (leo)"
    };

    @Override
    public CosmeticCategory getCategory() {
        return WingsCategory.INSTANCE;
    }

    @Override
    public String getId() {
        return "NEONAZI_WINGS";
    }

    @Override
    public String getName() {
        return "Neo Nazi Wings";
    }

    @Override
    public MaterialData getIcon() {
        return ICON;
    }

    @Override
    public String[] getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void onEnable(Player player) {

    }

    @Override
    public void onDisable(Player player) {

    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public String[][] getPattern() {
        return PATTERN;
    }

    @Override
    public ParticleMeta getEffect(Player player, String index) {
        Color color = Color.fromRGB(255, 255, 255);

        if (index.equals("44")) {
            color = Color.fromRGB(255, 17, 0);
        }

        return new ParticleMeta(
                EnumParticle.REDSTONE,
                color.getRed() / 255.0F,
                color.getGreen() / 255.0F,
                color.getBlue() / 255.0F,
                1,
                0
        );
    }

    @Override
    public double getHeightOffset() {
        return 0;
    }
}
