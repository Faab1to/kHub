package cc.invictusgames.hub;

import cc.invictusgames.hub.command.OsmiumCommands;
import cc.invictusgames.hub.command.ParkourCommand;
import cc.invictusgames.hub.command.SpawnCommands;
import cc.invictusgames.hub.cosmetic.CosmeticManager;
import cc.invictusgames.hub.item.CompassItem;
import cc.invictusgames.hub.item.CosmeticItem;
import cc.invictusgames.hub.item.DisguiseItem;
import cc.invictusgames.hub.item.EnderpearlItem;
import cc.invictusgames.hub.listener.PlayerListener;
import cc.invictusgames.hub.staffsigns.StaffSignListener;
import cc.invictusgames.hub.listener.WorldListener;
import cc.invictusgames.hub.mongo.MongoManager;
import cc.invictusgames.hub.npc.NpcManager;
import cc.invictusgames.hub.parkour.ParkourManager;
import cc.invictusgames.hub.parkour.listener.ParkourListener;
import cc.invictusgames.hub.playersetting.OsmiumSettings;
import cc.invictusgames.hub.profile.ProfileManager;
import cc.invictusgames.hub.scoreboard.HubBoardAdapter;
import cc.invictusgames.hub.staffsigns.StaffSignRunnable;
import cc.invictusgames.hub.tab.HubTabAdapter;
import cc.invictusgames.hub.visibility.HubVisibilityAdapter;
import cc.invictusgames.ilib.command.CommandService;
import cc.invictusgames.ilib.configuration.ConfigurationService;
import cc.invictusgames.ilib.configuration.JsonConfigurationService;
import cc.invictusgames.ilib.playersetting.PlayerSettingService;
import cc.invictusgames.ilib.scoreboard.ScoreboardService;
import cc.invictusgames.ilib.tab.TabService;
import cc.invictusgames.ilib.utils.CC;
import cc.invictusgames.ilib.visibility.VisibilityService;
import cc.invictusgames.invictus.server.ServerInfo;
import eu.vortexdev.api.SpigotAPI;
import eu.vortexdev.api.protocol.MovementListenerAdapter;
import eu.vortexdev.invictusspigot.InvictusSpigot;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Getter
public class HubPlugin extends JavaPlugin {

    private ConfigurationService configurationService;
    private HubConfig hubConfig;
    private MongoManager mongoManager;
    private CosmeticManager cosmeticManager;
    private ProfileManager profileManager;
    private NpcManager npcManager;
    private ParkourManager parkourManager;

    @Override
    public void onEnable() {
        configurationService = new JsonConfigurationService();
        loadConfig();

        mongoManager = new MongoManager(this);
        cosmeticManager = new CosmeticManager(this);
        profileManager = new ProfileManager(this);
        npcManager = new NpcManager(this);
        parkourManager = new ParkourManager(this);

        //For some reason skins don't work when we don't spawn this delayed
        Bukkit.getScheduler().runTaskLater(this, npcManager::loadNpcs, 20L);

        if (mongoManager.connect())
            System.out.println("Connected to mongo!");

        VisibilityService.registerVisibilityAdapter(new HubVisibilityAdapter(this));
        new ScoreboardService(this, new HubBoardAdapter(this));
        new TabService(this, new HubTabAdapter(this));

        CommandService.register(this,
                new SpawnCommands(this),
                new OsmiumCommands(this),
                new ParkourCommand(this));

        PlayerListener playerListener = new PlayerListener(this);
        ParkourListener parkourListener = new ParkourListener(this);

        Stream.of(
                new WorldListener(),
                playerListener,
                parkourListener,
                new StaffSignListener(this)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        SpigotAPI.addMovementListener(playerListener);

        PlayerSettingService.registerProvider(new OsmiumSettings());
//        PacketRegistry.registerListener(new NPCListener());

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setTime(1600);
        }
        new StaffSignRunnable(this).runTaskTimerAsynchronously(
                this,
                0L,
                TimeUnit.MINUTES.toSeconds(5) * 20
        );
    }

    public void loadConfig() {
        hubConfig = configurationService.loadConfiguration(HubConfig.class,
                new File(getDataFolder(), "config.json"));
    }

    public static String getStateName(Player player, ServerInfo server) {
        switch (server.getState()) {
            case UNKNOWN:
                return CC.format("&cOffline&7%s", player.isOp() ? " (UN)" : "");
            case HEARTBEAT_TIMEOUT:
                return CC.format("&cOffline&7%s", player.isOp() ? " (HB)" : "");
            case OFFLINE:
                return CC.translate("&cOffline");
            case WHITELISTED:
                return CC.translate("&eWhitelisted");
            case ONLINE:
            return CC.translate("&aOnline");
        }
        return CC.translate("&cOffline");
    }

    public void giveItems(Player player) {
        player.getInventory().setItem(0, new EnderpearlItem(player).getItem());
        if (player.hasPermission("invictus.command.disguise"))
            player.getInventory().setItem(7, new DisguiseItem(this, player).getItem());
        player.getInventory().setItem(4, new CompassItem(this, player).getItem());
        player.getInventory().setItem(8, new CosmeticItem(this, player).getItem());
    }

    public static HubPlugin getPlugin() {
        return JavaPlugin.getPlugin(HubPlugin.class);
    }
}
