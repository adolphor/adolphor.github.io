package Y2019.M02.D10_ByteBuf.heapBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;

import java.nio.charset.StandardCharsets;

import static Y2019.M02.D10_ByteBuf.heapBuf.BaseBuf.HELLO_NETTY;
import static Y2019.M02.D10_ByteBuf.heapBuf.BaseBuf.handlerArray;

public class DirectBuf_5_2 {
  public static void main(String[] args) {
    ByteBuf directBuf = new UnpooledDirectByteBuf(ByteBufAllocator.DEFAULT, 1, 100);
    directBuf.writeBytes(HELLO_NETTY.getBytes(StandardCharsets.UTF_8));
    if (!directBuf.hasArray()) {
      int length = directBuf.readableBytes();
      byte[] array = new byte[length];
      directBuf.getBytes(directBuf.readerIndex(), array);
      String s = handlerArray(array, 0, length);
      System.out.println(s);
    }
  }
}
