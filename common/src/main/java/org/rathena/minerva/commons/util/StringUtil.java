package org.rathena.minerva.commons.util;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class StringUtil {
    public static String getCStringFromByteBuf(ByteBuf byteBuf, int index) {
        return getCStringFromByteBuf(byteBuf, index, byteBuf.readableBytes()-(1+index));
    }

    public static String getCStringFromByteBuf(ByteBuf byteBuf, int firstIndex, int length) {
        String str = byteBuf.toString(firstIndex, length, CharsetUtil.US_ASCII);
        int nulIndex = str.indexOf('\0');
        if(nulIndex == -1)
            return str;
        else
            return str.substring(0, nulIndex);
    }
}
