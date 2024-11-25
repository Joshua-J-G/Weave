package org.codenobi.weave.Networking.Server;


import org.codenobi.weave.Networking.Server.Player.WeaveServerPlayer;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDictonary {
    public Map<UUID, WeaveServerPlayer> UUIDSearch = new HashMap<>();
    public Map<Integer, UUID> IDtoUUID = new HashMap<>();
    public Map<SocketAddress, UUID> SockettoUUID = new HashMap<>();

}
