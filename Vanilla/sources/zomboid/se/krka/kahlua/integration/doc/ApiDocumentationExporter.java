package se.krka.kahlua.integration.doc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import se.krka.kahlua.integration.expose.ClassDebugInformation;
import se.krka.kahlua.integration.expose.MethodDebugInformation;

public class ApiDocumentationExporter implements ApiInformation {
   private final Map classes;
   private final Map classHierarchy = new HashMap();
   private final List rootClasses = new ArrayList();
   private final List allClasses = new ArrayList();
   private Comparator classSorter = new Comparator() {
      public int compare(Class var1, Class var2) {
         int var3 = var1.getSimpleName().compareTo(var2.getSimpleName());
         return var3 != 0 ? var3 : var1.getName().compareTo(var2.getName());
      }
   };
   private Comparator methodSorter = new Comparator() {
      public int compare(MethodDebugInformation var1, MethodDebugInformation var2) {
         return var1.getLuaName().compareTo(var2.getLuaName());
      }
   };

   public ApiDocumentationExporter(Map var1) {
      this.classes = var1;
      this.setupHierarchy();
   }

   public void setupHierarchy() {
      Iterator var1;
      Class var3;
      for(var1 = this.classes.entrySet().iterator(); var1.hasNext(); this.allClasses.add(var3)) {
         Entry var2 = (Entry)var1.next();
         var3 = (Class)var2.getKey();
         Class var4 = var3.getSuperclass();
         if (this.classes.get(var4) != null) {
            Object var5 = (List)this.classHierarchy.get(var4);
            if (var5 == null) {
               var5 = new ArrayList();
               this.classHierarchy.put(var4, var5);
            }

            ((List)var5).add(var3);
         } else {
            this.rootClasses.add(var3);
         }
      }

      Collections.sort(this.allClasses, this.classSorter);
      Collections.sort(this.rootClasses, this.classSorter);
      var1 = this.classHierarchy.values().iterator();

      while(var1.hasNext()) {
         List var6 = (List)var1.next();
         Collections.sort(var6, this.classSorter);
      }

   }

   public List getAllClasses() {
      return this.allClasses;
   }

   public List getChildrenForClass(Class var1) {
      List var2 = (List)this.classHierarchy.get(var1);
      return var2 != null ? var2 : Collections.emptyList();
   }

   public List getRootClasses() {
      return this.rootClasses;
   }

   private List getMethods(Class var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      ClassDebugInformation var4 = (ClassDebugInformation)this.classes.get(var1);
      Iterator var5 = var4.getMethods().values().iterator();

      while(var5.hasNext()) {
         MethodDebugInformation var6 = (MethodDebugInformation)var5.next();
         if (var6.isMethod() == var2) {
            var3.add(var6);
         }
      }

      Collections.sort(var3, this.methodSorter);
      return var3;
   }

   public List getFunctionsForClass(Class var1) {
      return this.getMethods(var1, false);
   }

   public List getMethodsForClass(Class var1) {
      return this.getMethods(var1, true);
   }
}
