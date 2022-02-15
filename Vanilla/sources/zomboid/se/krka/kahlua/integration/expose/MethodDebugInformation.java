package se.krka.kahlua.integration.expose;

import java.util.Iterator;
import java.util.List;

public class MethodDebugInformation {
   private final String luaName;
   private final boolean isMethod;
   private final List parameters;
   private final String returnType;
   private final String returnDescription;

   public MethodDebugInformation(String var1, boolean var2, List var3, String var4, String var5) {
      this.parameters = var3;
      this.luaName = var1;
      this.isMethod = var2;
      this.returnDescription = var5;
      if (var3.size() > 0 && ((MethodParameter)var3.get(0)).getType().equals(ReturnValues.class.getName())) {
         var4 = "...";
         var3.remove(0);
      }

      this.returnType = var4;
   }

   public String getLuaName() {
      return this.luaName;
   }

   public String getLuaDescription() {
      String var1 = this.isMethod ? "obj:" : "";
      String var2 = TypeUtil.removePackages(this.returnType) + " " + var1 + this.luaName + "(" + this.getLuaParameterList() + ")\n";
      if (this.getReturnDescription() != null) {
         var2 = var2 + this.getReturnDescription() + "\n";
      }

      return var2;
   }

   public boolean isMethod() {
      return this.isMethod;
   }

   public List getParameters() {
      return this.parameters;
   }

   public String getReturnDescription() {
      return this.returnDescription;
   }

   public String getReturnType() {
      return this.returnType;
   }

   public String toString() {
      return this.getLuaDescription();
   }

   private String getLuaParameterList() {
      StringBuilder var1 = new StringBuilder();
      boolean var2 = true;
      Iterator var3 = this.parameters.iterator();

      while(var3.hasNext()) {
         MethodParameter var4 = (MethodParameter)var3.next();
         if (var2) {
            var2 = false;
         } else {
            var1.append(", ");
         }

         String var5 = TypeUtil.removePackages(var4.getType());
         var1.append(var5).append(" ").append(var4.getName());
      }

      return var1.toString();
   }

   private String getParameterList() {
      StringBuilder var1 = new StringBuilder();
      boolean var2 = true;

      MethodParameter var4;
      for(Iterator var3 = this.parameters.iterator(); var3.hasNext(); var1.append(var4.getType()).append(" ").append(var4.getName())) {
         var4 = (MethodParameter)var3.next();
         if (var2) {
            var2 = false;
         } else {
            var1.append(", ");
         }
      }

      return var1.toString();
   }
}
