package cc.invictusgames.hub.tab;

import cc.invictusgames.hub.HubPlugin;
import cc.invictusgames.hub.selector.ServerSelectorEntry;
import cc.invictusgames.ilib.tab.TabAdapter;
import cc.invictusgames.ilib.tab.TabEntry;
import cc.invictusgames.invictus.Invictus;
import cc.invictusgames.invictus.profile.Profile;
import cc.invictusgames.invictus.server.ServerInfo;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class HubTabAdapter implements TabAdapter {

    private final HubPlugin plugin;

    @Override
    public Table<Integer, Integer, TabEntry> getEntries(Player player) {
        Table<Integer, Integer, TabEntry> entries = HashBasedTable.create();
        Profile profile = Invictus.getInstance().getProfileService().getProfile(player);

        entries.put(0, 4, new TabEntry("§4§lHub"));
        entries.put(0, 5, new TabEntry(Invictus.getInstance().getServerName()));

        entries.put(1, 1, new TabEntry("§4§l" + plugin.getHubConfig().getServerName()));
        entries.put(1, 2, new TabEntry(ServerInfo.getGlobalPlayerCount() + " / 1,000"));

        entries.put(1, 4, new TabEntry("§4§lRank"));
        entries.put(1, 5, new TabEntry(profile.getCurrentGrant().getRank().getDisplayName()
                + (profile.isDisguised() ? " &7(" + profile.getRealCurrentGrant().getRank().getDisplayName() + "&7)"
                : "")));

        entries.put(2, 4, new TabEntry("§4§lStore"));
        entries.put(2, 5, new TabEntry(ChatColor.WHITE + plugin.getHubConfig().getStore()));

        int x = 0;
        int y = 7;

        for (ServerSelectorEntry entry : plugin.getHubConfig().getServerSelector()) {
            ServerInfo server = ServerInfo.getServerInfo(entry.getServerName());
            int online = server.getOnlinePlayers();
            int max = server.getMaxPlayers();

            entries.put(x, y, new TabEntry(ChatColor.DARK_RED + ChatColor.BOLD.toString() + entry.getServerName()));
            entries.put(x, y + 1, new TabEntry(ChatColor.DARK_RED + "Status: " + ChatColor.WHITE
                    + HubPlugin.getStateName(player, server)));
            entries.put(x, y + 2, new TabEntry(ChatColor.DARK_RED + "Players: "
                    + ChatColor.WHITE + online + " / " + max));

            if (x == 3) {
                y += 2;
                x = 0;
            }

            x++;
        }

        return entries;
    }

    @Override
    public String getHeader(Player player) {
        return ChatColor.DARK_RED + ChatColor.BOLD.toString() + plugin.getHubConfig().getServerName();
    }

    @Override
    public String getFooter(Player player) {
        return ChatColor.GRAY.toString() + ChatColor.ITALIC + plugin.getHubConfig().getStore();
    }
}
