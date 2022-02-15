package zombie.scripting.objects;

public final class ItemRecipe {
   public String name;
   public Integer use = -1;
   public Boolean cooked = false;
   private String module = null;

   public Integer getUse() {
      return this.use;
   }

   public ItemRecipe(String var1, String var2, Integer var3) {
      this.name = var1;
      this.use = var3;
      this.setModule(var2);
   }

   public String getName() {
      return this.name;
   }

   public String getModule() {
      return this.module;
   }

   public void setModule(String var1) {
      this.module = var1;
   }

   public String getFullType() {
      return this.module + "." + this.name;
   }
}
