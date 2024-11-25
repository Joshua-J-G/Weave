package org.codenobi.weave.Networking;

import net.minecraft.server.level.ServerPlayer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public interface IHandle {
    ByteBuffer BUFFER = ByteBuffer.allocate(1024);
    void ClientHandle(ByteBuffer received, SocketAddress address);

    void ServerHandle(ByteBuffer received, SocketAddress client);

    void ReliableServerHandle(ByteBuffer received, ServerPlayer player);
    void ReliableClientHandle(ByteBuffer received);

}
