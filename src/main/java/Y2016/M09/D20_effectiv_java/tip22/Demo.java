package Y2016.M09.D20_effectiv_java.tip22;

/**
 * Created by adolphor on 2017/1/30.
 */
public class Demo {
    public static void main(String[] args) {
        OuterClass outterClass = new OuterClass();
        System.out.println(outterClass.toString());

        OuterClass.InnerClass1 innerClass1 = new OuterClass.InnerClass1();
        System.out.println(innerClass1.toString());



    }

}
