package Y2016.M09.D27_annotation.demo01;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Bob on 2016/9/28.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomMethodAnnotation {

  String author() default "danibuiza";

  String date();

  String description();

}