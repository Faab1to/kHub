package cc.invictusgames.hub.item.parkour;

import cc.invictusgames.hub.HubPlugin;
import cc.invictusgames.hub.profile.HCFProfile;
import cc.invictusgames.hub.profile.Profile;
import cc.invictusgames.ilib.builder.ItemBuilder;
import cc.invictusgames.ilib.menu.hotbaritem.HotbarItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class ParkourCheckpointItem extends HotbarItem {

    private final Player player;

    public ParkourCheckpointItem(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.ARROW)
                .setDisplayName(ChatColor.GOLD + "Teleport to checkpoint")
                .build();
    }

    @Override
    public void click(Action action, Block block) {
        HubPlugin.getPlugin().getParkourManager().checkPoint(player);
    }

    @Override
    public void clickEntity(Entity entity) {

    }
}
