package org.codenobi.weave.Networking.Server.Player;

import net.minecraft.server.level.ServerPlayer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class WeaveServerPlayer {
    public ServerPlayer serverPlayer = null;

    SocketAddress PlayerUDPAddress = null;

    public int ID = -1;

    private boolean AuthedForUDPCommunication = false;

    public void SetPlayerID(int id){
        ID = id;
    }

    public boolean GetAuth(){
        return AuthedForUDPCommunication;
    }

    public void Authenticate(){
        AuthedForUDPCommunication = true;
    }

    public ServerPlayer GetPlayer(){
        return serverPlayer;
    }

    public int GetID(){
        return ID;
    }

    public SocketAddress GetUDPaddress(){
        return PlayerUDPAddress;
    }



    public void SetUDP(SocketAddress address){
        this.PlayerUDPAddress = address;
    }

    public void SetPlayer(ServerPlayer player){
        this.serverPlayer = player;
    }

}
