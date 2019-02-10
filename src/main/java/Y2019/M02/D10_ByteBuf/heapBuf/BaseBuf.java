package Y2019.M02.D10_ByteBuf.heapBuf;

import java.nio.charset.StandardCharsets;

public class BaseBuf {

  public static final String HELLO_NETTY = "Hello Netty ByteBuf";

  public static String handlerArray(byte[] bytes, int offset, int length) {
    if (bytes.length == length) {
      return new String(bytes, StandardCharsets.UTF_8);
    } else {
      byte[] leftBytes = new byte[length];
      for (int i = 0; i < length; i++) {
        leftBytes[i] = bytes[offset + i];
      }
      return new String(leftBytes, StandardCharsets.UTF_8);
    }
  }

}
