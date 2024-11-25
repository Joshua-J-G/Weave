package org.codenobi.weave.Utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtils {
    public static UUID asUUID(ByteBuffer bb){
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    public static byte[] asBytes(UUID uuid){
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return  bb.array();
    }
}
