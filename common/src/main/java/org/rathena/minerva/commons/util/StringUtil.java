package org.rathena.minerva.commons.util;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class StringUtil {
    public static String getCStringFromByteBuf(ByteBuf byteBuf, int firstIndex, int length) {
        String str = byteBuf.toString(firstIndex, length, CharsetUtil.US_ASCII);
        int nulIndex = str.indexOf('\0');
        if(nulIndex == -1)
            return str;
        else
            return str.substring(0, nulIndex);
    }

    public static String readCStringFromByteBuf(ByteBuf byteBuf, int length) {
        String str = getCStringFromByteBuf(byteBuf, 0, length);
        byteBuf.skipBytes(length);
        return str;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
