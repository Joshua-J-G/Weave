package org.codenobi.weave.Networking.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.codenobi.weave.Networking.Common.DatagramBuilder;
import org.codenobi.weave.Networking.DefaultPackets;
import org.codenobi.weave.Networking.IHandle;
import org.codenobi.weave.Networking.IPacket;
import org.codenobi.weave.Networking.MagicNumber;
import org.codenobi.weave.Networking.Minecraft.CTellServerPacket;
import org.codenobi.weave.Networking.Minecraft.PacketHandler;
import org.codenobi.weave.Networking.Minecraft.STellClientPacket;
import org.codenobi.weave.Weave;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class UDPClient implements Runnable {
    public static DatagramChannel client;
    public static InetSocketAddress server;

    public static DatagramChannel startClient() throws IOException{
        client = DatagramBuilder.bindChannel(null);
        client.configureBlocking(false);
        return client;
    }

    public static void Send(IPacket packet, SocketAddress server) throws IOException{
        client.send(packet.getBuffer(), server);
    }

    private static void receiveMessage(DatagramChannel client) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        SocketAddress remoteAddress = client.receive(buffer);
        buffer.flip();

        if(remoteAddress != null) {
            MagicNumber number = new MagicNumber(buffer);
            Weave.LOGGER.info("[Client] Receive Message from Server Magic Number {}", number.GetMagicNumberString());

            if(Weave.INSTANCE.isRegistered(number)){
                IHandle handle = Weave.INSTANCE.GetRegistedHandle(number);
                handle.ClientHandle(buffer, remoteAddress);
            }
        }
    }

    @Override
    public void run() {
        try {
            client = startClient();
            server = new InetSocketAddress(Weave.INSTANCE.client.GetIp(), Weave.INSTANCE.client.GetPort());

            while (true){

                if(!Weave.INSTANCE.client.Authorised()){
                    Weave.LOGGER.info("[Client] Trying to send Message");
                    try {
                        DefaultPackets packet = new DefaultPackets();
                        packet.SetMagicNumber(Weave.MAGIC_NUM);
                        packet.ClientSendUUID(Minecraft.getInstance().player.getUUID());
                        packet.ReadyPacket();

                        UDPClient.Send(packet, UDPClient.server);
                        Thread.sleep(100);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                receiveMessage(client);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
