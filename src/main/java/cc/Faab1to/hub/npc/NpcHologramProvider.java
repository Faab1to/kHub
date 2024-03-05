package cc.invictusgames.hub.npc;

import cc.invictusgames.hub.HubPlugin;
import cc.invictusgames.hub.profile.HCFProfile;
import cc.invictusgames.hub.profile.deathban.Deathban;
import cc.invictusgames.hub.selector.ServerSelectorEntry;
import cc.invictusgames.ilib.hologram.updating.HologramProvider;
import cc.invictusgames.ilib.utils.CC;
import cc.invictusgames.invictus.Invictus;
import cc.invictusgames.invictus.InvictusBukkit;
import cc.invictusgames.invictus.profile.Profile;
import cc.invictusgames.invictus.rank.Rank;
import cc.invictusgames.invictus.server.ServerInfo;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class NpcHologramProvider implements HologramProvider {

    private final ServerSelectorEntry entry;

    @Override
    public List<String> getRawLines(Player player) {
        List<String> lines = new ArrayList<>();

        Profile profile = Invictus.getInstance().getProfileService().getProfile(player);

        ServerInfo server = ServerInfo.getServerInfo(entry.getServerName());

        HCFProfile hcfProfile = HubPlugin.getPlugin().getProfileManager().getProfile(player.getUniqueId()).getHCFProfile(server.getName());

        int lives = hcfProfile != null ? hcfProfile.getLives() : 0;
        int kills = hcfProfile != null ? hcfProfile.getKills() : 0;
        int deaths = hcfProfile != null ? hcfProfile.getDeaths() : 0;

        Deathban deathban = hcfProfile != null ? hcfProfile.getDeathban() : null;

        int online = server.getOnlinePlayers();
        int max = server.getMaxPlayers();

        int inQueue = InvictusBukkit.getBukkitInstance().getQueueService().getQueueing(entry.getServerName()).size();
        Rank rank = profile.getRealCurrentGrantOn(server.getGrantScope()).getRank();

        for (String line : entry.getNpcLines())
            lines.add(CC.translate(line)
                    .replaceAll("%online%", String.valueOf(online))
                    .replaceAll("%max%", String.valueOf(max))
                    .replaceAll("%kills%", String.valueOf(kills))
                    .replaceAll("%deaths%", String.valueOf(deaths))
                    .replaceAll("%lives%", String.valueOf(lives))
                    .replaceAll("%in_queue%", String.valueOf(inQueue))
                    .replaceAll("%rank_on_scope%", rank.getDisplayName())
                    .replaceAll("%deathban_remaining%",
                            (deathban != null ? deathban.getRemaining() : "Not Deathbanned")));

        return lines;
    }
}
