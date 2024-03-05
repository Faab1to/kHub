package cc.invictusgames.hub.profile;

import cc.invictusgames.hub.HubPlugin;
import cc.invictusgames.hub.profile.listener.ProfileListener;
import cc.invictusgames.ilib.utils.callback.TypeCallable;
import cc.invictusgames.invictus.Invictus;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager {

    private final HubPlugin plugin;
    private final Map<UUID, Profile> profileMap = new HashMap<>();

    public ProfileManager(HubPlugin plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(new ProfileListener(plugin), plugin);
    }

    public void loadOrCreate(UUID uuid, TypeCallable<Profile> callable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Document document = plugin.getMongoManager().getProfiles()
                    .find(Filters.eq("uuid", uuid.toString())).first();

            Profile profile;

            if (profileMap.containsKey(uuid)) {
                callable.callback(getProfile(uuid));
                return;
            }

            if (document == null) {
                profile = getProfile(uuid);
                callable.callback(profile);
                return;
            }

            profile = new Profile(document);
            profileMap.put(uuid, profile);
            callable.callback(profile);
        });
    }

    public Profile getProfile(UUID uuid) {
        return profileMap.getOrDefault(uuid,
                new Profile(uuid));
    }

    public Map<UUID, Profile> getProfileMap() {
        return profileMap;
    }
}
