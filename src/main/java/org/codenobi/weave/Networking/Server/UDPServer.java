package org.codenobi.weave.Networking.Server;

import org.codenobi.weave.Networking.Common.DatagramBuilder;
import org.codenobi.weave.Networking.DefaultPackets;
import org.codenobi.weave.Networking.IHandle;
import org.codenobi.weave.Networking.IPacket;
import org.codenobi.weave.Networking.MagicNumber;
import org.codenobi.weave.Networking.Minecraft.PacketHandler;
import org.codenobi.weave.Networking.Minecraft.STellClientPacket;
import org.codenobi.weave.Networking.Server.Player.WeaveServerPlayer;
import org.codenobi.weave.Weave;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UDPServer implements Runnable{
    private static DatagramChannel server;
    private static InetSocketAddress address;

    public static Queue<Integer> removeId = new LinkedList<>();
    private static int currentId = 0;


    public static int GenId(Queue<Integer> removedIds){
        if(!removedIds.isEmpty()){
            return removedIds.poll();
        }

        int id = currentId;
        currentId++;
        return id;
    }


    public static DatagramChannel startServer() throws IOException{
        address = new InetSocketAddress(Weave.INSTANCE.server.PORT);
        server = DatagramBuilder.bindChannel(address);
        server.configureBlocking(false);
        Weave.LOGGER.info("[Server] Started Server at " + address);
        return server;
    }

    private static void receiveMessage(DatagramChannel server) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        SocketAddress remoteAddress = server.receive(buffer);
        buffer.flip();

        if(remoteAddress != null) {
            MagicNumber number = new MagicNumber(buffer);
            Weave.LOGGER.info("[Server] Receive Message from Client Magic Number {}", number.GetMagicNumberString());

            if(Weave.INSTANCE.isRegistered(number)){
                IHandle handle = Weave.INSTANCE.GetRegistedHandle(number);
                handle.ServerHandle(buffer, remoteAddress);
            }
        }
    }

    public static void SendToPlayer(IPacket packet, SocketAddress client) throws IOException{
        server.send(packet.getBuffer(), client);
    }

    public static void SendToAll(IPacket packet) throws IOException{
        for(SocketAddress address : Weave.INSTANCE.server.AllClients){
            server.send(packet.getBuffer(), address);
        }
    }


    @Override
    public void run() {
        try {
            Weave.LOGGER.info("[Server] Starting Server in Thread");
            server = startServer();
            Weave.LOGGER.info("[Server] Server Started");
            while (true){
                receiveMessage(server);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
