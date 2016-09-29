package Y2016.M09.D27_annotation.demo01;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Bob on 2016/9/28.
 */
public class Demo01 {
  public static void main(String[] args) throws Exception {

    Class<AnnotatedTest> object = AnnotatedTest.class;
    // Retrieve all annotations from the class
    Annotation[] annotations = object.getAnnotations();
    for (Annotation annotation : annotations) {
      System.out.println(annotation);
    }

    // Checks if an annotation is present
    if (object.isAnnotationPresent(CustomClassAnnotation.class)) {
      // Gets the desired annotation
      Annotation annotation = object.getAnnotation(CustomClassAnnotation.class);
      CustomClassAnnotation customClassAnnotation = (CustomClassAnnotation) annotation;
      String author = customClassAnnotation.author();
      String date = customClassAnnotation.date();
      System.out.println(customClassAnnotation + " => " + author + ", " + date);
    }

    // the same for all methods of the class
    for (Method method : object.getDeclaredMethods()) {
      if (method.isAnnotationPresent(CustomMethodAnnotation.class)) {
        Annotation annotation = method.getAnnotation(CustomMethodAnnotation.class);
        CustomMethodAnnotation customMethodAnnotation = (CustomMethodAnnotation) annotation;
        String author = customMethodAnnotation.author();
        String date = customMethodAnnotation.date();
        String description = customMethodAnnotation.description();
        System.out.println(annotation + " => " + author + ", " + date + ", " + description);
      }
    }
  }
}
