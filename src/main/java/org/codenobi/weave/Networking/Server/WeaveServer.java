package org.codenobi.weave.Networking.Server;

/*
We Create our Own UDP Server
But Will Use Java's TCP Server
 */

import org.codenobi.weave.Networking.DefaultPackets;
import org.codenobi.weave.Networking.Minecraft.PacketHandler;
import org.codenobi.weave.Networking.Minecraft.STellClientPacket;
import org.codenobi.weave.Weave;

import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;

public class WeaveServer {
    public int PORT = 25657;
    Thread serverThread;
    UDPServer udpServer;
    public Set<SocketAddress> AllClients = new HashSet<>();
    // Who you Going to Call

    public void Start(){
        Weave.LOGGER.info("[Server] Starting UDP Server");
        serverThread = new Thread(udpServer = new UDPServer());
        serverThread.start();
    }





















    // Ghost Busters
}
