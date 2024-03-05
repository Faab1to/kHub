package cc.invictusgames.hub.parkour.listener;

import cc.invictusgames.hub.HubPlugin;
import cc.invictusgames.hub.profile.Profile;
import cc.invictusgames.ilib.utils.TimeUtils;
import eu.vortexdev.api.protocol.MovementListenerAdapter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

@RequiredArgsConstructor
public class ParkourListener implements Listener {

    private final HubPlugin plugin;

    @EventHandler
    public void onFlightToggle(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());

        if (profile.isInParkour()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        if (event.getAction() == Action.PHYSICAL) {
            if (!profile.isInParkour()
                    && player.getLocation().getBlock().getType() == Material.GOLD_PLATE
                    && player.getLocation().add(0, -1, 0).getBlock().getType() != Material.EMERALD_BLOCK) {

                plugin.getParkourManager().join(player);

                player.sendMessage(ChatColor.GREEN + "You have joined the parkour!");
            }

            if (profile.isInParkour()) {
                if (player.getLocation().getBlock().getType() == Material.GOLD_PLATE &&
                        player.getLocation().add(0, -1, 0).getBlock().getType() == Material.EMERALD_BLOCK) {

                    player.sendMessage(ChatColor.GREEN + "You have finished the parkour it took you "
                            + TimeUtils.formatHHMMSS(System.currentTimeMillis() - profile.getParkourStart(), true));

                    plugin.getParkourManager().leave(player);
                }

                if (player.getLocation().getBlock().getType() == Material.IRON_PLATE)
                    profile.setLastCheckPoint(player.getLocation());
            }

            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
        }

    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!hasPlayerMoved(event)) return;

        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        if (!profile.isInParkour()) return;

        Location checkPoint = profile.getLastCheckPoint();

        if (checkPoint == null)
            checkPoint = plugin.getHubConfig().getParkourStart();

        if (checkPoint.getY() - player.getLocation().getY() >= 5) {
            plugin.getParkourManager().checkPoint(player);
        }
    }

    public static boolean hasPlayerMoved(PlayerMoveEvent event) {
        return event.getTo().getX() != event.getFrom().getX() || event.getTo().getY() != event.getFrom().getY() || event.getTo().getZ() != event.getFrom().getZ();
    }

}
