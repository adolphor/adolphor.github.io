# 数组转为ByteBuf

ByteBuf byteBuf = Unpooled.wrappedBuffer(decrypt);
ByteBuf byteBuf = Unpooled.buffer().writeBytes(decrypt);

第一种方法是封装了特定字节长度的数组，后续无法追加写入；
第二种方法开辟的空间过大，浪费内存。

变通方法，使用定长，需要追加的时候重新创建一个对象。
