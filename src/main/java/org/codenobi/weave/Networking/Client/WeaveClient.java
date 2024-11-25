package org.codenobi.weave.Networking.Client;

import org.codenobi.weave.Networking.IPacket;
import org.codenobi.weave.Weave;

import java.io.IOException;

public class WeaveClient {
    String ip = "localhost";
    int Port = 25567;
    int userID = -1;
    boolean Authorised = false;
    UDPClient udpClient = null;
    Thread clientThread;
    public void SetIp(String IP){
        this.ip = IP;
    }

    public boolean Authorised(){
        return  Authorised;
    }

    public void SetAuth(){
        Authorised = true;
    }

    public UDPClient client() {
        return udpClient;
    }

    public void SetPort(int port){
        Port = port;
    }

    public int GetPort(){
        return Port;
    }

    public String GetIp(){
        return ip;
    }

    public void SetID(int USERID){
        this.userID = USERID;
    }

    public int GetID(){
        return userID;
    }

    public void ConnectToServer(){
        Weave.LOGGER.info("[Client] Connecting to UDP Server "+ ip + ":" + Port);
        clientThread = new Thread(udpClient = new UDPClient());
        clientThread.start();

    }


}
