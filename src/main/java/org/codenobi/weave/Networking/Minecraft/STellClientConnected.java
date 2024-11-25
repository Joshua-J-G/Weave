package org.codenobi.weave.Networking.Minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.codenobi.weave.Networking.DefaultPackets;
import org.codenobi.weave.Weave;

import java.util.function.Supplier;

public class STellClientConnected {
    private final int id;

    public STellClientConnected(int id){
        this.id = id;
    }

    public STellClientConnected(FriendlyByteBuf buf){
        this(buf.readInt());
    }

    public void encode(FriendlyByteBuf bufffer){
        bufffer.writeInt(id);
    }


    public void handle(Supplier<NetworkEvent.Context> context) {
        // TODO:: Tell Player to Conenct To UDP Server
        Weave.LOGGER.info("[Client] Server Assigned Us ID:  " + this.id);


        Weave.INSTANCE.client.SetID(this.id);
        Weave.INSTANCE.client.SetAuth();
    }
}
