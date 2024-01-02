package Y2016.M09.D27_annotation.demo02;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Bob on 2016/9/28.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
public @interface MyTesterInfo {

  public enum Priority {
    LOW, MEDIUM, HIGH
  }

  Priority priority() default Priority.MEDIUM;

  String[] tags() default "";

  String createdBy() default "Mkyong";

  String lastModified() default "03/01/2014";

}