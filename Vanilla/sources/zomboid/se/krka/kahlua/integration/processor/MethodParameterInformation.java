package se.krka.kahlua.integration.processor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class MethodParameterInformation implements Serializable {
   public static final MethodParameterInformation EMPTY;
   private static final long serialVersionUID = -3059552311721486815L;
   private final List parameterNames;

   public MethodParameterInformation(List var1) {
      this.parameterNames = var1;
   }

   public String getName(int var1) {
      return var1 >= this.parameterNames.size() ? "arg" + (var1 + 1) : (String)this.parameterNames.get(var1);
   }

   static {
      EMPTY = new MethodParameterInformation(Collections.EMPTY_LIST);
   }
}
