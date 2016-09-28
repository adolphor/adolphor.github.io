package Y2016.M09.D27_annotation.demo01;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Bob on 2016/9/28.
 */
public class Demo01 {
  public static void main(String[] args) throws Exception {
    Class<AnnotatedClass> object = AnnotatedClass.class;
    // Retrieve all annotations from the class
    Annotation[] annotations = object.getAnnotations();
    for (Annotation annotation : annotations) {
      System.out.println(annotation);
    }

    // Checks if an annotation is present
    if (object.isAnnotationPresent(CustomClassAnnotation.class)) {
      // Gets the desired annotation
      Annotation annotation = object.getAnnotation(CustomClassAnnotation.class);
      System.out.println(annotation);
    }
    // the same for all methods of the class
    for (Method method : object.getDeclaredMethods()) {
      if (method.isAnnotationPresent(CustomMethodAnnotation.class)) {
        Annotation annotation = method.getAnnotation(CustomMethodAnnotation.class);
        System.out.println(annotation);
      }
    }
  }
}
