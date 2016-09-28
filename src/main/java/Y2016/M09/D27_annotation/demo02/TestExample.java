package Y2016.M09.D27_annotation.demo02;

/**
 * Created by Bob on 2016/9/28.
 */

@MyTesterInfo(
    priority = MyTesterInfo.Priority.HIGH,
    createdBy = "mkyong.com",
    tags = {"sales", "test"}
)
public class TestExample {

  @MyTest
  void testA() {
    if (true)
      throw new RuntimeException("This test always failed");
  }

  @MyTest(enabled = false)
  void testB() {
    if (false)
      throw new RuntimeException("This test always passed");
  }

  @MyTest(enabled = true)
  void testC() {
    if (10 > 1) {
      // do nothing, this test always passed.
    }
  }

}