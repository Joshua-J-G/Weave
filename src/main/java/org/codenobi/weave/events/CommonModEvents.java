package org.codenobi.weave.events;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.codenobi.weave.Networking.Minecraft.PacketHandler;
import org.codenobi.weave.Weave;

@Mod.EventBusSubscriber(modid = Weave.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            PacketHandler.register();
        });
    }

}
