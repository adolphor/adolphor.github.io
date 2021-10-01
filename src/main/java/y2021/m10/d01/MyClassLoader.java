package y2021.m10.d01;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/10/1 23:49
 * @Email: 0haizhu0@gmail.com
 */
public class MyClassLoader extends ClassLoader {

  private String classpath;

  public MyClassLoader(String classpath) {
    // 让系统类加载器(AppClassLoader)成为该类加载器的父类加载器
    super();
    this.classpath = classpath;
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    byte[] classByte = getClassBytes(name);
    return super.defineClass(null, classByte, 0, classByte.length);
  }

  private byte[] getClassBytes(String name) {
    try {
      String fileName = getClassFile(name);
      FileInputStream fileInput = new FileInputStream(fileName);
      FileChannel channel = fileInput.getChannel();
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      WritableByteChannel byteChannel = Channels.newChannel(output);
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      int flag;
      while ((flag = channel.read(buffer)) != -1) {
        if (flag == 0) break;
        buffer.flip();
        byteChannel.write(buffer);
        buffer.clear();
      }
      fileInput.close();
      channel.close();
      byteChannel.close();
      return output.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("MyClassLoader getClassBytes error: " + e.getMessage());
    }
  }

  private String getClassFile(String name) {
    //利用StringBuilder将包形式的类名转化为Unix形式的路径
    StringBuilder sb = new StringBuilder(classpath);
    sb.append("/")
      .append(name.replace('.', '/'))
      .append(".class");
    return sb.toString();
  }

}
