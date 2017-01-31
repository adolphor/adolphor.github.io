package Y2016.M09.D20_effectiv_java.tip22;

/**
 * Created by adolphor on 2017/1/30.
 */
public class OuterClass {

    private String o1p1;

    private void o1fun1(){
        System.out.println("I'm outer class.");
    }

    static class InnerClass1 {
        private String i1p1;

        @Override
        public String toString() {
            return super.toString();
        }
    }

    class InnerClass2{
        private String i2p1;

        @Override
        public String toString() {
            return super.toString();
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
