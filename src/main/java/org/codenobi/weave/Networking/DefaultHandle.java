package org.codenobi.weave.Networking;

import net.minecraft.server.level.ServerPlayer;
import org.codenobi.weave.Networking.Minecraft.PacketHandler;
import org.codenobi.weave.Networking.Minecraft.STellClientConnected;
import org.codenobi.weave.Networking.Minecraft.STellClientPort;
import org.codenobi.weave.Networking.Server.Player.WeaveServerPlayer;
import org.codenobi.weave.Networking.Server.PlayerDictonary;
import org.codenobi.weave.Networking.Server.UDPServer;
import org.codenobi.weave.Utils.UUIDUtils;
import org.codenobi.weave.Weave;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.UUID;

public class DefaultHandle implements IHandle{
    @Override
    public void ClientHandle(ByteBuffer received, SocketAddress address) {
        int id = received.getInt();

        if(id == 0){
            Weave.LOGGER.info("[Client] Recived Server UDP");
        }
    }

    @Override
    public void ServerHandle(ByteBuffer received, SocketAddress client) {
        int id = received.getInt();

        if(id == 0){
            UUID uuid = UUIDUtils.asUUID(received);
            Weave.LOGGER.info("[Server] UUID Recived " + uuid);

            if(Weave.INSTANCE.Dictionary.UUIDSearch.containsKey(uuid)){
                Weave.LOGGER.info("[Server] Player Is on The Server");
                WeaveServerPlayer player = Weave.INSTANCE.Dictionary.UUIDSearch.get(uuid);
                if(!player.GetAuth()){
                    player.Authenticate();
                    player.SetUDP(client);
                    int userid = UDPServer.GenId(UDPServer.removeId);
                    player.SetPlayerID(userid);

                    Weave.INSTANCE.Dictionary.IDtoUUID.put(userid, uuid);
                    Weave.INSTANCE.Dictionary.SockettoUUID.put(client, uuid);

                    Weave.INSTANCE.server.AllClients.add(client);



                    DefaultPackets packets = new DefaultPackets();
                    packets.SetMagicNumber(Weave.MAGIC_NUM);
                    packets.ServerTestPacket();
                    packets.ReadyPacket();

                    try {
                        UDPServer.SendToPlayer(packets, client);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    PacketHandler.sendToPlayer(new STellClientConnected(userid), player.GetPlayer());
                }
            }
        }
    }

    @Override
    public void ReliableServerHandle(ByteBuffer received, ServerPlayer player) {
        int id = received.getInt();

        if(id == 0){
            Weave.LOGGER.info("COOL ITEM JUST DROPPED FROM CLIENT " + player.getUUID());
        }
    }

    @Override
    public void ReliableClientHandle(ByteBuffer received) {
        int id = received.getInt();

        if(id == 0){
            Weave.LOGGER.info("COOL ITEM JUST DROPPED FROM SERVER");
        }
    }
}
