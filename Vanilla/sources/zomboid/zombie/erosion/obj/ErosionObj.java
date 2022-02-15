package zombie.erosion.obj;

import java.util.ArrayList;
import zombie.erosion.ErosionMain;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.objects.IsoTree;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.util.list.PZArrayList;

public final class ErosionObj {
   private final ErosionObjSprites sprites;
   public String name;
   public int stages;
   public boolean hasSnow;
   public boolean hasFlower;
   public boolean hasChildSprite;
   public float bloomStart;
   public float bloomEnd;
   public boolean noSeasonBase;
   public int cycleTime = 1;

   public ErosionObj(ErosionObjSprites var1, int var2, float var3, float var4, boolean var5) {
      this.sprites = var1;
      this.name = var1.name;
      this.stages = var1.stages;
      this.hasSnow = var1.hasSnow;
      this.hasFlower = var1.hasFlower;
      this.hasChildSprite = var1.hasChildSprite;
      this.bloomStart = var3;
      this.bloomEnd = var4;
      this.noSeasonBase = var5;
      this.cycleTime = var2;
   }

   public IsoObject getObject(IsoGridSquare var1, boolean var2) {
      PZArrayList var3 = var1.getObjects();

      for(int var4 = var3.size() - 1; var4 >= 0; --var4) {
         IsoObject var5 = (IsoObject)var3.get(var4);
         if (this.name.equals(var5.getName())) {
            if (var2) {
               var3.remove(var4);
            }

            var5.doNotSync = true;
            return var5;
         }
      }

      return null;
   }

   public IsoObject createObject(IsoGridSquare var1, int var2, boolean var3, int var4) {
      String var6 = this.sprites.getBase(var2, this.noSeasonBase ? 0 : var4);
      if (var6 == null) {
         var6 = "";
      }

      Object var5;
      if (var3) {
         var5 = IsoTree.getNew();
         ((IsoObject)var5).sprite = (IsoSprite)IsoSpriteManager.instance.NamedMap.get(var6);
         ((IsoObject)var5).square = var1;
         ((IsoObject)var5).sx = 0.0F;
         ((IsoTree)var5).initTree();
      } else {
         var5 = IsoObject.getNew(var1, var6, this.name, false);
      }

      ((IsoObject)var5).setName(this.name);
      ((IsoObject)var5).doNotSync = true;
      return (IsoObject)var5;
   }

   public boolean placeObject(IsoGridSquare var1, int var2, boolean var3, int var4, boolean var5) {
      IsoObject var6 = this.createObject(var1, var2, var3, var4);
      if (var6 != null && this.setStageObject(var2, var6, var4, var5)) {
         var6.doNotSync = true;
         if (!var3) {
            var1.getObjects().add(var6);
            var6.addToWorld();
         } else {
            var1.AddTileObject(var6);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean setStageObject(int var1, IsoObject var2, int var3, boolean var4) {
      var2.doNotSync = true;
      if (var1 >= 0 && var1 < this.stages && var2 != null) {
         String var6 = this.sprites.getBase(var1, this.noSeasonBase ? 0 : var3);
         if (var6 == null) {
            var2.setSprite(this.getSprite(""));
            if (var2.AttachedAnimSprite != null) {
               var2.AttachedAnimSprite.clear();
            }

            return true;
         } else {
            IsoSprite var7 = this.getSprite(var6);
            var2.setSprite(var7);
            if (this.hasChildSprite || this.hasFlower) {
               if (var2.AttachedAnimSprite == null) {
                  var2.AttachedAnimSprite = new ArrayList();
               }

               var2.AttachedAnimSprite.clear();
               if (this.hasChildSprite && var3 != 0) {
                  var6 = this.sprites.getChildSprite(var1, var3);
                  if (var6 != null) {
                     var7 = this.getSprite(var6);
                     var2.AttachedAnimSprite.add(var7.newInstance());
                  }
               }

               if (this.hasFlower && var4) {
                  var6 = this.sprites.getFlower(var1);
                  if (var6 != null) {
                     var7 = this.getSprite(var6);
                     var2.AttachedAnimSprite.add(var7.newInstance());
                  }
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public boolean setStage(IsoGridSquare var1, int var2, int var3, boolean var4) {
      IsoObject var5 = this.getObject(var1, false);
      return var5 != null ? this.setStageObject(var2, var5, var3, var4) : false;
   }

   public IsoObject removeObject(IsoGridSquare var1) {
      return this.getObject(var1, true);
   }

   private IsoSprite getSprite(String var1) {
      return ErosionMain.getInstance().getSpriteManager().getSprite(var1);
   }
}
