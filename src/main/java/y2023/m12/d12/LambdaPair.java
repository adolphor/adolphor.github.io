package y2023.m12.d12;

import java.util.function.BiConsumer;

public record LambdaPair<A, B>(A first, B second) {
  public static <A, B> LambdaPair<A, B> of(A a, B b) {
    return new LambdaPair<>(a, b);
  }

  public void apply(BiConsumer<A, B> action) {
    action.accept(first, second);
  }

}



