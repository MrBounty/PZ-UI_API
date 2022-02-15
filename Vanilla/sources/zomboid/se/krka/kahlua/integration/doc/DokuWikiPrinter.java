package se.krka.kahlua.integration.doc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import se.krka.kahlua.integration.expose.MethodDebugInformation;
import se.krka.kahlua.integration.expose.MethodParameter;

public class DokuWikiPrinter {
   private final ApiInformation information;
   private final PrintWriter writer;

   public DokuWikiPrinter(File var1, ApiInformation var2) throws IOException {
      this((Writer)(new FileWriter(var1)), var2);
   }

   public DokuWikiPrinter(Writer var1, ApiInformation var2) {
      this.information = var2;
      this.writer = new PrintWriter(var1);
   }

   public void process() {
      this.printClassHierarchy();
      this.printFunctions();
      this.writer.close();
   }

   private void printFunctions() {
      this.writer.println("====== Global functions ======");
      List var1 = this.information.getAllClasses();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Class var3 = (Class)var2.next();
         this.printClassFunctions(var3);
      }

   }

   private void printClassFunctions(Class var1) {
      List var2 = this.information.getFunctionsForClass(var1);
      if (var2.size() > 0) {
         this.writer.printf("===== %s ====\n", var1.getSimpleName());
         this.writer.printf("In package: %s\n", var1.getPackage().getName());
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            MethodDebugInformation var4 = (MethodDebugInformation)var3.next();
            this.printFunction(var4, "====");
         }

         this.writer.printf("\n----\n\n");
      }

   }

   private void printFunction(MethodDebugInformation var1, String var2) {
      this.writer.printf("%s %s %s\n", var2, var1.getLuaName(), var2);
      this.writer.printf("<code lua>%s</code>\n", var1.getLuaDescription());
      Iterator var3 = var1.getParameters().iterator();

      while(var3.hasNext()) {
         MethodParameter var4 = (MethodParameter)var3.next();
         String var5 = var4.getName();
         String var6 = var4.getType();
         String var7 = var4.getDescription();
         if (var7 == null) {
            this.writer.printf("  - **''%s''** ''%s''\n", var6, var5);
         } else {
            this.writer.printf("  - **''%s''** ''%s'': %s\n", var6, var5, var7);
         }
      }

      String var8 = var1.getReturnDescription();
      if (var8 == null) {
         this.writer.printf("  * returns ''%s''\n", var1.getReturnType());
      } else {
         this.writer.printf("  * returns ''%s'': %s\n", var1.getReturnType(), var8);
      }

   }

   private void printClassHierarchy() {
      this.writer.println("====== Class hierarchy ======");
      List var1 = this.information.getRootClasses();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Class var3 = (Class)var2.next();
         this.printClassHierarchy(var3, (Class)null);
      }

   }

   private void printClassHierarchy(Class var1, Class var2) {
      List var3 = this.information.getChildrenForClass(var1);
      List var4 = this.information.getMethodsForClass(var1);
      if (var3.size() > 0 || var4.size() > 0 || var2 != null) {
         this.writer.printf("===== %s =====\n", var1.getSimpleName());
         this.writer.printf("In package: ''%s''\n", var1.getPackage().getName());
         if (var2 != null) {
            this.writer.printf("\nSubclass of [[#%s|%s]]\n", var2.getSimpleName(), var2.getSimpleName());
         }

         if (var3.size() > 0) {
            this.writer.printf("\nChildren: ");
            boolean var5 = false;

            Class var7;
            for(Iterator var6 = var3.iterator(); var6.hasNext(); this.writer.printf("[[#%s|%s]]", var7.getSimpleName(), var7.getSimpleName())) {
               var7 = (Class)var6.next();
               if (var5) {
                  this.writer.print(", ");
               } else {
                  var5 = true;
               }
            }
         }

         this.printMethods(var1);
         this.writer.printf("\n----\n\n");
         Iterator var8 = var3.iterator();

         while(var8.hasNext()) {
            Class var9 = (Class)var8.next();
            this.printClassHierarchy(var9, var1);
         }
      }

   }

   private void printMethods(Class var1) {
      List var2 = this.information.getMethodsForClass(var1);
      if (var2.size() > 0) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            MethodDebugInformation var4 = (MethodDebugInformation)var3.next();
            this.printFunction(var4, "====");
         }
      }

   }
}
