package org.codenobi.weave.Networking.Minecraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.codenobi.weave.Weave;

import java.util.function.Supplier;

public class PacketHandler {
    private static final SimpleChannel INSTANCE= ChannelBuilder.named(
            new ResourceLocation(Weave.MODID, "main"))
            .serverAcceptedVersions((ver) -> true)
            .clientAcceptedVersions((ver) -> true)
            .networkProtocolVersion(() -> "1")
            .simpleChannel();

    public static void register() {
        int id = 0;
        INSTANCE.messageBuilder(STellClientPort.class, id++)
                .encoder(STellClientPort::encode)
                .decoder(STellClientPort::new)
                .consumerMainThread(STellClientPort::handle)
                .add();

        INSTANCE.messageBuilder(STellClientConnected.class, id++)
                .encoder(STellClientConnected::encode)
                .decoder(STellClientConnected::new)
                .consumerMainThread(STellClientConnected::handle)
                .add();

        INSTANCE.messageBuilder(STellClientPacket.class, id++)
                .encoder(STellClientPacket::encode)
                .decoder(STellClientPacket::new)
                .consumerMainThread(STellClientPacket::handle)
                .add();

        INSTANCE.messageBuilder(CTellServerPacket.class, id++)
                .encoder(CTellServerPacket::encode)
                .decoder(CTellServerPacket::new)
                .consumerMainThread(CTellServerPacket::handle)
                .add();

    }

    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }

    public static void sendToPlayer(Object msg, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendToAllClient(Object msg){
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }
}
