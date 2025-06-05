package pers.neige.neigeitems.utils;

import lombok.val;

import java.util.UUID;

/**
 * @author 墨水瓶InkerBot
 */
public class UUIDUtils {
    public static UUID uuidFromBytes(byte[] bytes) {
        return new UUID(
                ((long) bytes[0] << 56)
                        | ((long) (bytes[1] & 0xFF) << 48)
                        | ((long) (bytes[2] & 0xFF) << 40)
                        | ((long) (bytes[3] & 0xFF) << 32)
                        | ((long) (bytes[4] & 0xFF) << 24)
                        | ((long) (bytes[5] & 0xFF) << 16)
                        | ((long) (bytes[6] & 0xFF) << 8)
                        | ((long) (bytes[7] & 0xFF)),
                ((long) bytes[8] << 56)
                        | ((long) (bytes[9] & 0xFF) << 48)
                        | ((long) (bytes[10] & 0xFF) << 40)
                        | ((long) (bytes[11] & 0xFF) << 32)
                        | ((long) (bytes[12] & 0xFF) << 24)
                        | ((long) (bytes[13] & 0xFF) << 16)
                        | ((long) (bytes[14] & 0xFF) << 8)
                        | ((long) (bytes[15] & 0xFF))
        );
    }

    public static byte[] bytesFromUuid(UUID uuid) {
        val mostSigBits = uuid.getMostSignificantBits();
        val leastSigBits = uuid.getLeastSignificantBits();
        return new byte[]{
                (byte) (mostSigBits >> 56),
                (byte) (mostSigBits >> 48),
                (byte) (mostSigBits >> 40),
                (byte) (mostSigBits >> 32),
                (byte) (mostSigBits >> 24),
                (byte) (mostSigBits >> 16),
                (byte) (mostSigBits >> 8),
                (byte) mostSigBits,
                (byte) (leastSigBits >> 56),
                (byte) (leastSigBits >> 48),
                (byte) (leastSigBits >> 40),
                (byte) (leastSigBits >> 32),
                (byte) (leastSigBits >> 24),
                (byte) (leastSigBits >> 16),
                (byte) (leastSigBits >> 8),
                (byte) leastSigBits
        };
    }
}
