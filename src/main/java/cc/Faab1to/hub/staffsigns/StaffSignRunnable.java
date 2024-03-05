package cc.invictusgames.hub.staffsigns;

import cc.invictusgames.hub.HubPlugin;
import cc.invictusgames.ilib.configuration.defaults.SimpleLocationConfig;
import cc.invictusgames.ilib.utils.CC;
import cc.invictusgames.invictus.Invictus;
import cc.invictusgames.invictus.connection.RequestHandler;
import cc.invictusgames.invictus.connection.RequestResponse;
import cc.invictusgames.invictus.rank.Rank;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class StaffSignRunnable extends BukkitRunnable {

    private static final Comparator<StaffEntry> ENTRY_COMPARATOR = (o1, o2) -> {
        Rank rank1 = o1.getRank();
        Rank rank2 = o2.getRank();

        if (rank1 != null && rank2 == null)
            return -1;

        if (rank1 == null && rank2 != null)
            return 1;

        if (rank1 == null && rank2 == null)
            return 0;

        int result = rank2.getWeight() - rank1.getWeight();
        if (result == 0)
            result = String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());

        return result;
    };

    private final HubPlugin plugin;

    @Override
    public void run() {
        RequestResponse response = RequestHandler.get("staffList");
        if (!response.wasSuccessful())
            return;
        List<StaffEntry> entries = new ArrayList<>();

        response.asArray().forEach(element -> {
            JsonObject rank = element.getAsJsonObject();
            rank.get("members").getAsJsonArray().forEach(member ->
                    entries.add(new StaffEntry(rank, member.getAsJsonObject())));
        });

        entries.sort(ENTRY_COMPARATOR);

        int i = 0;
        for (SimpleLocationConfig locationConfig : plugin.getHubConfig().getStaffSignLocations()) {
            Location location = locationConfig.getLocation();
            if (location == null)
                continue;

            Block block = location.getBlock();
            if (!(block.getState() instanceof Sign))
                continue;

            StaffEntry staffEntry = null;
            if (i < entries.size())
                staffEntry = entries.get(i++);

            Sign sign = (Sign) block.getState();

            if (staffEntry != null && staffEntry.getRank() != null) {
                sign.setLine(0, CC.DGRAY + CC.STRIKE_THROUGH + "-----------");
                sign.setLine(1, staffEntry.getRank().getDisplayName());
                sign.setLine(2, staffEntry.getName());
                sign.setLine(3, CC.DGRAY + CC.STRIKE_THROUGH + "-----------");
                sign.update();
            } else {
                sign.setLine(0, "");
                sign.setLine(1, "");
                sign.setLine(2, "");
                sign.setLine(3, "");
                sign.update();
            }

            block = block.getRelative(BlockFace.UP);
            if (!(block.getState() instanceof Skull))
                continue;

            Skull skull = (Skull) block.getState();
            skull.setOwner(staffEntry != null && staffEntry.getRank() != null ? staffEntry.getName() : "MHF_Question");
            skull.update();
        }
    }

    @Getter
    public class StaffEntry {

        private final UUID uuid;
        private final String name;
        private final UUID rankId;

        public StaffEntry(JsonObject rank, JsonObject member) {
            this.uuid = UUID.fromString(member.get("uuid").getAsString());
            this.name = member.get("name").getAsString();
            this.rankId = UUID.fromString(rank.get("uuid").getAsString());
        }

        public Rank getRank() {
            return Invictus.getInstance().getRankService().getRank(rankId);
        }

    }
}
