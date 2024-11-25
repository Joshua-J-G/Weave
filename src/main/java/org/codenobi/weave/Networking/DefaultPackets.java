package org.codenobi.weave.Networking;

import java.util.UUID;
import org.codenobi.weave.Utils.UUIDUtils;

public class DefaultPackets implements IPacket{
    public void ClientSendUUID(UUID uuid){
        // ID 0 is verification
        BUFFER.putInt(0);
        BUFFER.put(UUIDUtils.asBytes(uuid));
    }

    public void ServerTestPacket(){
        BUFFER.putInt(0);
    }

}
