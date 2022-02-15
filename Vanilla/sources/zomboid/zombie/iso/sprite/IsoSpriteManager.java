package zombie.iso.sprite;

import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.HashMap;
import zombie.core.Color;
import zombie.debug.DebugLog;
import zombie.iso.SpriteDetails.IsoFlagType;

public final class IsoSpriteManager {
   public static final IsoSpriteManager instance = new IsoSpriteManager();
   public final HashMap NamedMap = new HashMap();
   public final TIntObjectHashMap IntMap = new TIntObjectHashMap();
   private final IsoSprite emptySprite = new IsoSprite(this);

   public IsoSpriteManager() {
      IsoSprite var1 = this.emptySprite;
      var1.name = "";
      var1.ID = -1;
      var1.Properties.Set(IsoFlagType.invisible);
      var1.CurrentAnim = new IsoAnim();
      var1.CurrentAnim.ID = var1.AnimStack.size();
      var1.AnimStack.add(var1.CurrentAnim);
      var1.AnimMap.put("default", var1.CurrentAnim);
      this.NamedMap.put(var1.name, var1);
   }

   public void Dispose() {
      IsoSprite.DisposeAll();
      IsoAnim.DisposeAll();
      Object[] var1 = this.IntMap.values();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         IsoSprite var3 = (IsoSprite)var1[var2];
         var3.Dispose();
         var3.def = null;
         var3.parentManager = null;
      }

      this.IntMap.clear();
      this.NamedMap.clear();
      this.NamedMap.put(this.emptySprite.name, this.emptySprite);
   }

   public IsoSprite getSprite(int var1) {
      return this.IntMap.containsKey(var1) ? (IsoSprite)this.IntMap.get(var1) : null;
   }

   public IsoSprite getSprite(String var1) {
      return this.NamedMap.containsKey(var1) ? (IsoSprite)this.NamedMap.get(var1) : this.AddSprite(var1);
   }

   public IsoSprite getOrAddSpriteCache(String var1) {
      if (this.NamedMap.containsKey(var1)) {
         return (IsoSprite)this.NamedMap.get(var1);
      } else {
         IsoSprite var2 = new IsoSprite(this);
         var2.LoadFramesNoDirPageSimple(var1);
         this.NamedMap.put(var1, var2);
         return var2;
      }
   }

   public IsoSprite getOrAddSpriteCache(String var1, Color var2) {
      int var3 = (int)(var2.r * 255.0F);
      int var4 = (int)(var2.g * 255.0F);
      int var5 = (int)(var2.b * 255.0F);
      String var6 = var1 + "_" + var3 + "_" + var4 + "_" + var5;
      if (this.NamedMap.containsKey(var6)) {
         return (IsoSprite)this.NamedMap.get(var6);
      } else {
         IsoSprite var7 = new IsoSprite(this);
         var7.LoadFramesNoDirPageSimple(var1);
         this.NamedMap.put(var6, var7);
         return var7;
      }
   }

   public IsoSprite AddSprite(String var1) {
      IsoSprite var2 = new IsoSprite(this);
      var2.LoadFramesNoDirPageSimple(var1);
      this.NamedMap.put(var1, var2);
      return var2;
   }

   public IsoSprite AddSprite(String var1, int var2) {
      IsoSprite var3 = new IsoSprite(this);
      var3.LoadFramesNoDirPageSimple(var1);
      if (this.NamedMap.containsKey(var1)) {
         DebugLog.log("duplicate texture " + var1 + " ignore ID=" + var2 + ", use ID=" + ((IsoSprite)this.NamedMap.get(var1)).ID);
         var2 = ((IsoSprite)this.NamedMap.get(var1)).ID;
      }

      this.NamedMap.put(var1, var3);
      var3.ID = var2;
      this.IntMap.put(var2, var3);
      return var3;
   }
}
