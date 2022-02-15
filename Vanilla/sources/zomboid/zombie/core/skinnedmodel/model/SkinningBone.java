package zombie.core.skinnedmodel.model;

import java.util.function.Consumer;

public final class SkinningBone {
   public SkinningBone Parent;
   public String Name;
   public int Index;
   public SkinningBone[] Children;

   public void forEachDescendant(Consumer var1) {
      forEachDescendant(this, var1);
   }

   private static void forEachDescendant(SkinningBone var0, Consumer var1) {
      if (var0.Children != null && var0.Children.length != 0) {
         SkinningBone[] var2 = var0.Children;
         int var3 = var2.length;

         int var4;
         SkinningBone var5;
         for(var4 = 0; var4 < var3; ++var4) {
            var5 = var2[var4];
            var1.accept(var5);
         }

         var2 = var0.Children;
         var3 = var2.length;

         for(var4 = 0; var4 < var3; ++var4) {
            var5 = var2[var4];
            forEachDescendant(var5, var1);
         }

      }
   }

   public String toString() {
      String var1 = System.lineSeparator();
      return this.getClass().getName() + var1 + "{" + var1 + "\tName:\"" + this.Name + "\"" + var1 + "\tIndex:" + this.Index + var1 + "}";
   }
}
