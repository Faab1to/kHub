package cc.invictusgames.hub.parkour;

import cc.invictusgames.hub.HubPlugin;
import cc.invictusgames.hub.item.CompassItem;
import cc.invictusgames.hub.item.CosmeticItem;
import cc.invictusgames.hub.item.DisguiseItem;
import cc.invictusgames.hub.item.EnderpearlItem;
import cc.invictusgames.hub.item.parkour.ParkourCheckpointItem;
import cc.invictusgames.hub.item.parkour.ParkourLeaveItem;
import cc.invictusgames.hub.playersetting.OsmiumSettings;
import cc.invictusgames.hub.profile.Profile;
import cc.invictusgames.ilib.builder.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class ParkourManager {

    private final HubPlugin plugin;

    public void checkPoint(Player player) {
        Profile profile = HubPlugin.getPlugin().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getLastCheckPoint() == null) {
            player.teleport(HubPlugin.getPlugin().getHubConfig().getParkourStart());
            profile.setParkourStart(System.currentTimeMillis());
            return;
        }

        player.teleport(profile.getLastCheckPoint());
    }

    public void leave(Player player) {
        Location spawnLocation = plugin.getHubConfig().getSpawnLocation();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        profile.setLastCheckPoint(null);
        profile.setInParkour(false);
        profile.setParkourStart(-1);

        if (spawnLocation != null)
            player.teleport(spawnLocation);

        if (OsmiumSettings.FLY_MODE.canUpdate(player)
                && OsmiumSettings.FLY_MODE.get(player)) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        player.setWalkSpeed(0.5F);
        player.getInventory().clear();
        plugin.giveItems(player);
        player.updateInventory();
    }

    public void join(Player player) {
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        profile.setInParkour(true);
        profile.setLastCheckPoint(null);
        profile.setParkourStart(System.currentTimeMillis());

        player.getInventory().clear();

        player.getInventory().setItem(5, new ParkourLeaveItem(player).getItem());
        player.getInventory().setItem(3, new ParkourCheckpointItem(player).getItem());

        player.setWalkSpeed(0.2F);
        player.setFlying(false);
        player.setAllowFlight(false);

        player.teleport(HubPlugin.getPlugin().getHubConfig().getParkourStart());
    }
}
