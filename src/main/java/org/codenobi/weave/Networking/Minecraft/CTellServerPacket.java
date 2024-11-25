package org.codenobi.weave.Networking.Minecraft;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.codenobi.weave.Networking.IHandle;
import org.codenobi.weave.Networking.MagicNumber;
import org.codenobi.weave.Weave;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

public class CTellServerPacket {
    private final ByteBuffer packet;

    public CTellServerPacket(ByteBuffer packet){
        this.packet = packet;
    }

    public CTellServerPacket(FriendlyByteBuf buf){
        this(buf.nioBuffer());
    }

    public void encode(FriendlyByteBuf bufffer){
        bufffer.writeBytes(packet);
    }


    public void handle(Supplier<NetworkEvent.Context> context) {
        // TODO:: Tell Player to Conenct To UDP Server
        MagicNumber number = new MagicNumber(packet);

        if(Weave.INSTANCE.isRegistered(number)){
            IHandle handle = Weave.INSTANCE.GetRegistedHandle(number);

            handle.ReliableServerHandle(packet, context.get().getSender());
        }

    }
}
