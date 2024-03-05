package cc.invictusgames.hub.staffsigns;

import cc.invictusgames.hub.HubPlugin;
import cc.invictusgames.ilib.utils.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

@RequiredArgsConstructor
public class StaffSignListener implements Listener {

    private final HubPlugin plugin;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignChange(SignChangeEvent event) {
        if (event.isCancelled())
            return;

        if (!event.getPlayer().isOp())
            return;

        if (!event.getLine(0).equalsIgnoreCase("[StaffSign]"))
            return;

        event.setLine(0, "");
        event.setLine(1, "");
        event.setLine(2, "");
        event.setLine(3, "");

        plugin.getHubConfig().addStaffSign(event.getBlock().getLocation());
        plugin.getHubConfig().saveConfig();
        event.getPlayer().sendMessage(CC.GREEN + "You added a staff sign.");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;
        
        if (!event.getPlayer().isOp())
            return;

        if (!(event.getBlock().getState() instanceof Sign))
            return;

        if (!plugin.getHubConfig().removeStaffSignAt(event.getBlock().getLocation()))
            return;

        plugin.getHubConfig().saveConfig();
        event.getPlayer().sendMessage(CC.GREEN + "You removed a staff sign");
    }

}
