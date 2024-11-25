package org.codenobi.weave.Networking.Common;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

public class DatagramBuilder {
    public static DatagramChannel openChannel() throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        return  channel;
    }

    public static DatagramChannel bindChannel(SocketAddress local) throws  IOException{
        return  openChannel().bind(local);
    }
}
