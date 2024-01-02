package Y2016.M09.D20_effectiv_java.tip08;

/**
 * Created by adolphor on 2017/1/18.
 * 引用地址：
 * http://adolphor.com/blog/2016/09/20/effective-java-second-edition.html#tip08
 * 来源：
 * http://www.cnblogs.com/tinyphp/p/3771989.html
 */
public class User {

  String name;
  int age;

  /**
   * 比较过程思路：
   * 1、两个对象指向位置相同，那么他们就相等，return后跳出函数，不再往下执行
   * 2、指向位置不同，有3情况
   * a：这两对象类型相同，并且内容相同，也属于相等
   * b：类型相同，内容不同，属于不等;
   * c：类型不同，属于不等
   */
  public boolean equals(Object obj) {
    //1、指向位置相同，指定同一对象
    if (this == obj) {
      return true;
    }

    //2、指向位置不同
    //能调用这里的equals，肯定是生成了User对象的，所以this肯定是User类型
    //判断obj是不是User类型，如果是就跟this为同一类型
    boolean b = obj instanceof User;
    if (b) {
      /**
       * 类型相同,obj为User类型，而跟this位置不同的对象(第1步已证明)
       * obj来源可能是：
       * User obj = new User();
       * 或
       * Object obj =new Object(); //Object是 User的父类
       */
      //obj向下转型为User对象，以便一定能调用User里面的值
      User u = (User) obj;
      //这里的equals是用name调用的(jdk里面String复写过的equals方法)，不是当前类的equals
      //基本数据类型用==比，引用数据类型用equals比
      if (this.age == u.age && this.name.equals(u.name)) {
        return true;
      } else {
        return false;
      }
    } else {
      //类型不同，不是user对象，不等
      return false;
    }
  }


}