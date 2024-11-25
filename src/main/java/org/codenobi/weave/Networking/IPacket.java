package org.codenobi.weave.Networking;

import java.nio.ByteBuffer;

public interface IPacket {
    ByteBuffer BUFFER = ByteBuffer.allocate(1024);

    // This Should be the first Thing Called after Creating a packet
    default void SetMagicNumber(MagicNumber number){
        BUFFER.clear();
        char[] num = number.GetMagicNumber();
        BUFFER.putChar(num[0]);
        BUFFER.putChar(num[1]);
        BUFFER.putChar(num[2]);
        BUFFER.putChar(num[3]);
    }

    default void ReadyPacket(){
        BUFFER.flip();
    }

    default ByteBuffer getBuffer(){
        return BUFFER;
    }
}
