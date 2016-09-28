package Y2016.M09.D27_annotation.demo01;

/**
 * Created by Bob on 2016/9/28.
 */
@CustomClassAnnotation(date = "2016-09-28")
public class AnnotatedClass {

  @CustomMethodAnnotation(date = "2014-06-05", description = "annotated method")
  public String annotatedMethod() {
    return "nothing niente";
  }

  @CustomMethodAnnotation(author = "friend of mine", date = "2014-06-05", description = "annotated method")
  public String annotatedMethodFromAFriend() {
    return "nothing niente";
  }
}
