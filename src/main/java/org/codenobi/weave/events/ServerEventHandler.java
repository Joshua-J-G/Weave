package org.codenobi.weave.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.codenobi.weave.Networking.Minecraft.PacketHandler;
import org.codenobi.weave.Networking.Minecraft.STellClientPort;
import org.codenobi.weave.Weave;

@Mod.EventBusSubscriber(modid = Weave.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerEventHandler {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event){
        ServerPlayer player = (ServerPlayer) event.getEntity();
        Weave.LOGGER.info("[SERVER] Server Sending Welcome Packet");
        PacketHandler.sendToPlayer(new STellClientPort(1111), player);
    }
}
