package astar.tests;

import astar.ASearchNode;
import astar.ISearchNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class SearchNodeCity extends ASearchNode {
   private String name;
   private SearchNodeCity parent;
   private SearchNodeCity goal;
   private HashMap adjacencyMatrix = new HashMap();

   public SearchNodeCity(String var1) {
      this.name = var1;
      this.fillAdjacencyMatrix();
   }

   private void fillAdjacencyMatrix() {
      HashMap var1 = new HashMap();
      var1.put("Kaiserslautern", 70);
      var1.put("Karlsruhe", 145);
      HashMap var2 = new HashMap();
      var2.put("Frankfurt", 103);
      var2.put("Ludwigshafen", 53);
      HashMap var3 = new HashMap();
      var3.put("Heilbronn", 84);
      HashMap var4 = new HashMap();
      var4.put("WÃ¼rzburg", 116);
      HashMap var5 = new HashMap();
      var5.put("WÃ¼rzburg", 183);
      HashMap var6 = new HashMap();
      var6.put("WÃ¼rzburg", 102);
      this.adjacencyMatrix.put("SaarbrÃ¼cken", var1);
      this.adjacencyMatrix.put("Kaiserslautern", var2);
      this.adjacencyMatrix.put("Frankfurt", var4);
      this.adjacencyMatrix.put("Karlsruhe", var3);
      this.adjacencyMatrix.put("Ludwigshafen", var5);
      this.adjacencyMatrix.put("Heilbronn", var6);
   }

   public double h() {
      String var1 = this.name;
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1711156285:
         if (var1.equals("Frankfurt")) {
            var2 = 3;
         }
         break;
      case -1524566265:
         if (var1.equals("Kaiserslautern")) {
            var2 = 1;
         }
         break;
      case 431649091:
         if (var1.equals("Karlsruhe")) {
            var2 = 2;
         }
         break;
      case 527047967:
         if (var1.equals("Heilbronn")) {
            var2 = 5;
         }
         break;
      case 642279168:
         if (var1.equals("WÃ¼rzburg")) {
            var2 = 6;
         }
         break;
      case 697309017:
         if (var1.equals("SaarbrÃ¼cken")) {
            var2 = 0;
         }
         break;
      case 1541689853:
         if (var1.equals("Ludwigshafen")) {
            var2 = 4;
         }
      }

      switch(var2) {
      case 0:
         return 222.0D;
      case 1:
         return 158.0D;
      case 2:
         return 140.0D;
      case 3:
         return 96.0D;
      case 4:
         return 108.0D;
      case 5:
         return 87.0D;
      case 6:
         return 0.0D;
      default:
         return 0.0D;
      }
   }

   public double c(ISearchNode var1) {
      return (double)(Integer)((HashMap)this.adjacencyMatrix.get(this.name)).get(this.castToSearchNodeCity(var1).getName());
   }

   public void getSuccessors(ArrayList var1) {
      Set var2 = ((HashMap)this.adjacencyMatrix.get(this.name)).keySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var1.add(new SearchNodeCity(var4));
      }

   }

   public ArrayList getSuccessors() {
      ArrayList var1 = new ArrayList();
      Set var2 = ((HashMap)this.adjacencyMatrix.get(this.name)).keySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var1.add(new SearchNodeCity(var4));
      }

      return var1;
   }

   public SearchNodeCity getParent() {
      return this.parent;
   }

   public void setParent(ISearchNode var1) {
      this.parent = this.castToSearchNodeCity(var1);
   }

   public Integer keyCode() {
      return this.name.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof SearchNodeCity) {
         SearchNodeCity var2 = (SearchNodeCity)var1;
         return this.name == var2.getName();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public String getName() {
      return this.name;
   }

   private SearchNodeCity castToSearchNodeCity(ISearchNode var1) {
      return (SearchNodeCity)var1;
   }

   public String toString() {
      String var10000 = this.name;
      return var10000 + ",f:" + this.f();
   }
}
