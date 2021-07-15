---
layout:     post
title:      Java代理性能比较
date:       2016-12-14 14:50:32 +0800
postId:     2016-12-14-14-50-32
categories: []
keywords:   [Java]
---

## 性能分析
对于动态代理的各种实现，其性能各不相同。找到了一篇对于各个实现方式性能测试的文章
—— 《[动态代理方案性能对比](http://javatar.iteye.com/blog/814426)》。

### 组件版本
更新了各组件到最新版本：

* JDK: 1.8.0_102
* ASM: 4.2
* CGLIB: 3.1
* JAVAASSIST: 3.12.1.GA

### 测试结果
性能测试结果如下：

```
Create JDK Proxy: 14 ms
Create CGLIB Proxy: 186 ms
Create JAVAASSIST Proxy: 104 ms
Create JAVAASSIST Bytecode Proxy: 160 ms
Create ASM Proxy: 3 ms
================================================
Run JDK Proxy: 105 ms, 13,429,194 t/s
Run CGLIB Proxy: 195 ms, 7,231,104 t/s
Run JAVAASSIST Proxy: 691 ms, 2,040,615 t/s
Run JAVAASSIST Bytecode Proxy: 102 ms, 13,824,170 t/s
Run ASM Bytecode Proxy: 127 ms, 11,102,877 t/s
------------------------------------------------
Run JDK Proxy: 419 ms, 3,365,311 t/s
Run CGLIB Proxy: 94 ms, 15,000,695 t/s
Run JAVAASSIST Proxy: 1053 ms, 1,339,093 t/s
Run JAVAASSIST Bytecode Proxy: 62 ms, 22,742,990 t/s
Run ASM Bytecode Proxy: 82 ms, 17,195,919 t/s
------------------------------------------------
Run JDK Proxy: 140 ms, 10,071,895 t/s
Run CGLIB Proxy: 92 ms, 15,326,797 t/s
Run JAVAASSIST Proxy: 528 ms, 2,670,578 t/s
Run JAVAASSIST Bytecode Proxy: 81 ms, 17,408,214 t/s
Run ASM Bytecode Proxy: 73 ms, 19,315,964 t/s
```

### 结果分析

#### 测试结论
1. ASM和JAVAASSIST字节码生成方式不相上下，都很快，是CGLIB的5倍。 
2. CGLIB次之，是JDK自带的两倍。 
3. JDK自带的再次之，因JDK1.6对动态代理做了优化，如果用低版本JDK更慢，要注意的是JDK也是通过字节码生成来实现动态代理的，而不是反射。 
4. JAVAASSIST提供者动态代理接口最慢，比JDK自带的还慢。 
(这也是为什么网上有人说JAVAASSIST比JDK还慢的原因，用JAVAASSIST最好别用它提供的动态代理接口，而可以考虑用它的字节码生成方式) 

#### 差异原因
各方案生成的字节码不一样， 
像JDK和CGLIB都考虑了很多因素，以及继承或包装了自己的一些类， 
所以生成的字节码非常大，而我们很多时候用不上这些， 
而手工生成的字节码非常小，所以速度快， 
具体的字节码对比，后面有贴出，可自行分析。 

#### 最终选型
最终决定使用JAVAASSIST的字节码生成代理方式， 
虽然ASM稍快，但并没有快一个数量级， 
而JAVAASSIST的字节码生成方式比ASM方便， 
JAVAASSIST只需用字符串拼接出Java源码，便可生成相应字节码， 
而ASM需要手工写字节码。 

## 测试源码

````
CountService.java
````
```java
public interface CountService {
  int count();
}
```
````
CountServiceImpl.java
````
```java
public class CountServiceImpl implements CountService {
  private int count = 0;
  @Override
  public int count() {
    return count ++;
  }
}
```
````
DynamicProxyPerformanceTest.java
````
```java
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DecimalFormat;

public class DynamicProxyPerformanceTest {

  public static void main(String[] args) throws Exception {
    CountService delegate = new CountServiceImpl();

    long time = System.currentTimeMillis();
    CountService jdkProxy = createJdkDynamicProxy(delegate);
    time = System.currentTimeMillis() - time;
    System.out.println("Create JDK Proxy: " + time + " ms");

    time = System.currentTimeMillis();
    CountService cglibProxy = createCglibDynamicProxy(delegate);
    time = System.currentTimeMillis() - time;
    System.out.println("Create CGLIB Proxy: " + time + " ms");

    time = System.currentTimeMillis();
    CountService javassistProxy = createJavassistDynamicProxy(delegate);
    time = System.currentTimeMillis() - time;
    System.out.println("Create JAVAASSIST Proxy: " + time + " ms");

    time = System.currentTimeMillis();
    CountService javassistBytecodeProxy = createJavassistBytecodeDynamicProxy(delegate);
    time = System.currentTimeMillis() - time;
    System.out.println("Create JAVAASSIST Bytecode Proxy: " + time + " ms");

    time = System.currentTimeMillis();
    CountService asmBytecodeProxy = createAsmBytecodeDynamicProxy(delegate);
    time = System.currentTimeMillis() - time;
    System.out.println("Create ASM Proxy: " + time + " ms");
    System.out.println("================================================");

    for (int i = 0; i < 3; i++) {
      test(jdkProxy, "Run JDK Proxy: ");
      test(cglibProxy, "Run CGLIB Proxy: ");
      test(javassistProxy, "Run JAVAASSIST Proxy: ");
      test(javassistBytecodeProxy, "Run JAVAASSIST Bytecode Proxy: ");
      test(asmBytecodeProxy, "Run ASM Bytecode Proxy: ");
      System.out.println("------------------------------------------------");
    }
  }

  private static void test(CountService service, String label)
      throws Exception {
    service.count(); // warm up
    int count = 10000000;
    long time = System.currentTimeMillis();
    for (int i = 0; i < count; i++) {
      service.count();
    }
    time = System.currentTimeMillis() - time;
    System.out.println(label + time + " ms, " + new DecimalFormat().format(count * 1000 / time) + " t/s");
  }

  private static CountService createJdkDynamicProxy(final CountService delegate) {
    CountService jdkProxy = (CountService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
        new Class[]{CountService.class}, new JdkHandler(delegate));
    return jdkProxy;
  }

  private static class JdkHandler implements InvocationHandler {

    final Object delegate;

    JdkHandler(Object delegate) {
      this.delegate = delegate;
    }

    public Object invoke(Object object, Method method, Object[] objects)
        throws Throwable {
      return method.invoke(delegate, objects);
    }
  }

  private static CountService createCglibDynamicProxy(final CountService delegate) throws Exception {
    Enhancer enhancer = new Enhancer();
    enhancer.setCallback(new CglibInterceptor(delegate));
    enhancer.setInterfaces(new Class[]{CountService.class});
    CountService cglibProxy = (CountService) enhancer.create();
    return cglibProxy;
  }

  private static class CglibInterceptor implements MethodInterceptor {
    final Object delegate;

    CglibInterceptor(Object delegate) {
      this.delegate = delegate;
    }

    public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
      return methodProxy.invoke(delegate, objects);
    }
  }

  private static CountService createJavassistDynamicProxy(final CountService delegate) throws Exception {
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.setInterfaces(new Class[]{CountService.class});
    Class proxyClass = proxyFactory.createClass();
    CountService javassistProxy = (CountService) proxyClass.newInstance();
    ((ProxyObject) javassistProxy).setHandler(new JavaAssitInterceptor(delegate));
    return javassistProxy;
  }

  private static class JavaAssitInterceptor implements MethodHandler {
    final Object delegate;

    JavaAssitInterceptor(Object delegate) {
      this.delegate = delegate;
    }

    public Object invoke(Object self, Method m, Method proceed, Object[] args) throws Throwable {
      return m.invoke(delegate, args);
    }
  }

  private static CountService createJavassistBytecodeDynamicProxy(CountService delegate) throws Exception {
    ClassPool mPool = new ClassPool(true);
    CtClass mCtc = mPool.makeClass(CountService.class.getName() + "JavaassistProxy");
    mCtc.addInterface(mPool.get(CountService.class.getName()));
    mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
    mCtc.addField(CtField.make("public " + CountService.class.getName() + " delegate;", mCtc));
    mCtc.addMethod(CtNewMethod.make("public int count() { return delegate.count(); }", mCtc));
    Class pc = mCtc.toClass();
    CountService bytecodeProxy = (CountService) pc.newInstance();
    Field filed = bytecodeProxy.getClass().getField("delegate");
    filed.set(bytecodeProxy, delegate);
    return bytecodeProxy;
  }

  private static CountService createAsmBytecodeDynamicProxy(CountService delegate) throws Exception {
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    String className = CountService.class.getName() + "AsmProxy";
    String classPath = className.replace('.', '/');
    String interfacePath = CountService.class.getName().replace('.', '/');
    classWriter.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, classPath, null, "java/lang/Object", new String[]{interfacePath});

    MethodVisitor initVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
    initVisitor.visitCode();
    initVisitor.visitVarInsn(Opcodes.ALOAD, 0);
    initVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
    initVisitor.visitInsn(Opcodes.RETURN);
    initVisitor.visitMaxs(0, 0);
    initVisitor.visitEnd();

    FieldVisitor fieldVisitor = classWriter.visitField(Opcodes.ACC_PUBLIC, "delegate", "L" + interfacePath + ";", null, null);
    fieldVisitor.visitEnd();

    MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "count", "()I", null, null);
    methodVisitor.visitCode();
    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
    methodVisitor.visitFieldInsn(Opcodes.GETFIELD, classPath, "delegate", "L" + interfacePath + ";");
    methodVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, interfacePath, "count", "()I");
    methodVisitor.visitInsn(Opcodes.IRETURN);
    methodVisitor.visitMaxs(0, 0);
    methodVisitor.visitEnd();

    classWriter.visitEnd();
    byte[] code = classWriter.toByteArray();
    CountService bytecodeProxy = (CountService) new ByteArrayClassLoader().getClass(className, code).newInstance();
    Field filed = bytecodeProxy.getClass().getField("delegate");
    filed.set(bytecodeProxy, delegate);
    return bytecodeProxy;
  }

  private static class ByteArrayClassLoader extends ClassLoader {

    public ByteArrayClassLoader() {
      super(ByteArrayClassLoader.class.getClassLoader());
    }

    public synchronized Class getClass(String name, byte[] code) {
      if (name == null) {
        throw new IllegalArgumentException("");
      }
      return defineClass(name, code, 0, code.length);
    }

  }
}
```

## 参考资料

* [动态代理方案性能对比](http://javatar.iteye.com/blog/814426)
* TODO [代理模式详解](https://snailclimb.gitee.io/javaguide/#/docs/java/basis/%E4%BB%A3%E7%90%86%E6%A8%A1%E5%BC%8F%E8%AF%A6%E8%A7%A3)
