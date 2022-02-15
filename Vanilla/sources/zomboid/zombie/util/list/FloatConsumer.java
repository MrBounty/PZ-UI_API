package zombie.util.list;

import java.util.Objects;

public interface FloatConsumer {
   void accept(float var1);

   default FloatConsumer andThen(FloatConsumer var1) {
      Objects.requireNonNull(var1);
      return (var2) -> {
         this.accept(var2);
         var1.accept(var2);
      };
   }
}
