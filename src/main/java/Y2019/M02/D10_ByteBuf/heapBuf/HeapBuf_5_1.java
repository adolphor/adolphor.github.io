package Y2019.M02.D10_ByteBuf.heapBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Assert;

import java.nio.charset.StandardCharsets;

import static Y2019.M02.D10_ByteBuf.heapBuf.BaseBuf.HELLO_NETTY;
import static Y2019.M02.D10_ByteBuf.heapBuf.BaseBuf.handlerArray;

public class HeapBuf_5_1 {

  public static void main(String[] args) {
    basic();
    unUsedByteBuf();
    usedByteBuf();
  }

  /**
   * heapBuf基本操作
   */
  private static void basic() {
    // wrappedBuffer方法返回的是 UnpooledHeapByteBuf 对象
    ByteBuf heapBuf = Unpooled.wrappedBuffer(HELLO_NETTY.getBytes(StandardCharsets.UTF_8));
    System.out.println(heapBuf.getClass());
    // heapByteBuf是存储于JVM的堆缓冲区，数据结构是数组，所以一定有array
    Assert.assertTrue(heapBuf.hasArray());
    if (heapBuf.hasArray()) {
      byte[] array = heapBuf.array();
      // 数组所有内容都可用
      Assert.assertEquals(array.length, heapBuf.readableBytes());
    }
    // 读取buf，会移动read指针
    ByteBuf readBytes = heapBuf.readBytes(6);
    byte[] array = heapBuf.array();
    Assert.assertEquals(array.length - 6, heapBuf.readableBytes());

    // 读取的返回值是 UnpooledByteBufAllocator 类型
    System.out.println(readBytes.getClass());

    // TODO 这个类型咋处理？怎么获取内容并转为字符串？
  }

  /**
   * ByteBuf对象从未使用的情况
   */
  private static void unUsedByteBuf() {
    ByteBuf heapBuf = Unpooled.wrappedBuffer(HELLO_NETTY.getBytes(StandardCharsets.UTF_8));
    if (heapBuf.hasArray()) {
      byte[] array = heapBuf.array();
      // arrayOffset 不都是从第一个字节开始吗？
      int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
      Assert.assertEquals(0, heapBuf.arrayOffset());
      Assert.assertEquals(0, heapBuf.readerIndex());
      int length = heapBuf.readableBytes();
      Assert.assertEquals(array.length, length);
      String result = handlerArray(array, offset, length);
      Assert.assertEquals(HELLO_NETTY, result);
    }
  }

  /**
   * ByteBuf对象使用过的情况
   */
  private static void usedByteBuf() {
    ByteBuf heapBuf = Unpooled.wrappedBuffer(HELLO_NETTY.getBytes(StandardCharsets.UTF_8));
    ByteBuf byteBuf = heapBuf.readBytes(6);
    if (heapBuf.hasArray()) {
      byte[] array = heapBuf.array();
      int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
      Assert.assertEquals(0, heapBuf.arrayOffset());
      Assert.assertEquals(6, heapBuf.readerIndex());
      int length = heapBuf.readableBytes();
      Assert.assertEquals(array.length - 6, length);
      String result = handlerArray(array, offset, length);
      Assert.assertEquals("Netty ByteBuf", result);
    }

  }

}
