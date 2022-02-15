package se.krka.kahlua.integration.expose;

public class MethodParameter {
   private final String name;
   private final String type;
   private final String description;

   public MethodParameter(String var1, String var2, String var3) {
      this.name = var1;
      this.type = var2;
      this.description = var3;
   }

   public String getName() {
      return this.name;
   }

   public String getType() {
      return this.type;
   }

   public String getDescription() {
      return this.description;
   }
}
