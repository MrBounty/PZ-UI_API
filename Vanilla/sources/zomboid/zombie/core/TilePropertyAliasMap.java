package zombie.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public final class TilePropertyAliasMap {
   public static final TilePropertyAliasMap instance = new TilePropertyAliasMap();
   public final HashMap PropertyToID = new HashMap();
   public final ArrayList Properties = new ArrayList();

   public void Generate(HashMap var1) {
      this.Properties.clear();
      this.PropertyToID.clear();
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         String var4 = (String)var3.getKey();
         ArrayList var5 = (ArrayList)var3.getValue();
         this.PropertyToID.put(var4, this.Properties.size());
         TilePropertyAliasMap.TileProperty var6 = new TilePropertyAliasMap.TileProperty();
         this.Properties.add(var6);
         var6.propertyName = var4;
         var6.possibleValues.addAll(var5);
         ArrayList var7 = var6.possibleValues;

         for(int var8 = 0; var8 < var7.size(); ++var8) {
            String var9 = (String)var7.get(var8);
            var6.idMap.put(var9, var8);
         }
      }

   }

   public int getIDFromPropertyName(String var1) {
      return !this.PropertyToID.containsKey(var1) ? -1 : (Integer)this.PropertyToID.get(var1);
   }

   public int getIDFromPropertyValue(int var1, String var2) {
      TilePropertyAliasMap.TileProperty var3 = (TilePropertyAliasMap.TileProperty)this.Properties.get(var1);
      if (var3.possibleValues.isEmpty()) {
         return 0;
      } else {
         return !var3.idMap.containsKey(var2) ? 0 : (Integer)var3.idMap.get(var2);
      }
   }

   public String getPropertyValueString(int var1, int var2) {
      TilePropertyAliasMap.TileProperty var3 = (TilePropertyAliasMap.TileProperty)this.Properties.get(var1);
      return var3.possibleValues.isEmpty() ? "" : (String)var3.possibleValues.get(var2);
   }

   public static final class TileProperty {
      public String propertyName;
      public final ArrayList possibleValues = new ArrayList();
      public final HashMap idMap = new HashMap();
   }
}
