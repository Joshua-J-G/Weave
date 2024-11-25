package org.codenobi.weave;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.codenobi.weave.Networking.Client.WeaveClient;
import org.codenobi.weave.Networking.DefaultHandle;
import org.codenobi.weave.Networking.IHandle;
import org.codenobi.weave.Networking.MagicNumber;
import org.codenobi.weave.Networking.Minecraft.PacketHandler;
import org.codenobi.weave.Networking.Minecraft.STellClientPort;
import org.codenobi.weave.Networking.Server.Player.WeaveServerPlayer;
import org.codenobi.weave.Networking.Server.PlayerDictonary;
import org.codenobi.weave.Networking.Server.WeaveServer;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Weave.MODID)
public class Weave {
    public static Weave INSTANCE;

    // Magic Number for This Mod
    public static MagicNumber MAGIC_NUM = new MagicNumber('W', 'E', 'A', 'V');


    // Define mod id in a common place for everything to reference
    public static final String MODID = "weave";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "weave" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "weave" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "weave" namespace

    public Weave() {
        INSTANCE = this;

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    DefaultHandle handle = new DefaultHandle();

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        RegisterMod(MAGIC_NUM, handle);
    }


    public WeaveServer server = new WeaveServer();

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");

        server.Start();

    }

    Map<String, IHandle> RegisteredMods = new HashMap<>();

    public void RegisterMod(MagicNumber Number, IHandle handle) {
        RegisteredMods.put(Number.GetMagicNumberString(), handle);
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

    public PlayerDictonary Dictionary = new PlayerDictonary();

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event){
        ServerPlayer player = (ServerPlayer) event.getEntity();

        WeaveServerPlayer Wplayer = new WeaveServerPlayer();
        Wplayer.SetPlayer(player);

        Dictionary.UUIDSearch.put(player.getUUID(), Wplayer);

        Weave.LOGGER.info("[SERVER] Server Sending Welcome Packet");
        PacketHandler.sendToPlayer(new STellClientPort(Weave.INSTANCE.server.PORT), player);
    }

    public boolean isRegistered(MagicNumber num){
        return RegisteredMods.containsKey(num.GetMagicNumberString());
    }

    public IHandle GetRegistedHandle(MagicNumber num){
        return RegisteredMods.get(num.GetMagicNumberString());
    }

   public WeaveClient client = new WeaveClient();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        Minecraft minecraft = Minecraft.getInstance();

        ClientPacketListener connection = minecraft.getConnection();

        if(connection != null && !minecraft.isSingleplayer()){
            InetSocketAddress inetSocketAddress = (InetSocketAddress) connection.getConnection().getRemoteAddress();

            client.SetIp(inetSocketAddress.getAddress().getHostAddress());
        }
    }
}
