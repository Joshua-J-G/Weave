package org.codenobi.weave.Networking.Minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.codenobi.weave.Networking.Client.UDPClient;
import org.codenobi.weave.Networking.DefaultPackets;
import org.codenobi.weave.Networking.MagicNumber;
import org.codenobi.weave.Weave;

import java.io.IOException;
import java.util.function.Supplier;

public class STellClientPort {
    private final int port;

    public STellClientPort(int port){
        this.port = port;
    }

    public STellClientPort(FriendlyByteBuf buf){
        this(buf.readInt());
    }

    public void encode(FriendlyByteBuf bufffer){
        bufffer.writeInt(port);
    }


    public void handle(Supplier<NetworkEvent.Context> context) {
        // TODO:: Tell Player to Conenct To UDP Server
        Weave.LOGGER.info("[Client] MESSAGE RECEIVED Port " + this.port);

        Weave.INSTANCE.client.SetPort(this.port);

        Weave.INSTANCE.client.ConnectToServer();
    }
}
