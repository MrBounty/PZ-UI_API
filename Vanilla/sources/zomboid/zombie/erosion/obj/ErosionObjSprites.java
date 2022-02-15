package zombie.erosion.obj;

import java.util.ArrayList;
import zombie.core.Core;
import zombie.debug.DebugLog;
import zombie.iso.IsoDirections;
import zombie.iso.sprite.IsoDirectionFrame;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameServer;

public final class ErosionObjSprites {
   public static final int SECTION_BASE = 0;
   public static final int SECTION_SNOW = 1;
   public static final int SECTION_FLOWER = 2;
   public static final int SECTION_CHILD = 3;
   public static final int NUM_SECTIONS = 4;
   public String name;
   public int stages;
   public boolean hasSnow;
   public boolean hasFlower;
   public boolean hasChildSprite;
   public boolean noSeasonBase;
   public int cycleTime = 1;
   private ErosionObjSprites.Stage[] sprites;

   public ErosionObjSprites(int var1, String var2, boolean var3, boolean var4, boolean var5) {
      this.name = var2;
      this.stages = var1;
      this.hasSnow = var3;
      this.hasFlower = var4;
      this.hasChildSprite = var5;
      this.sprites = new ErosionObjSprites.Stage[var1];

      for(int var6 = 0; var6 < var1; ++var6) {
         this.sprites[var6] = new ErosionObjSprites.Stage();
         this.sprites[var6].sections[0] = new ErosionObjSprites.Section();
         if (this.hasSnow) {
            this.sprites[var6].sections[1] = new ErosionObjSprites.Section();
         }

         if (this.hasFlower) {
            this.sprites[var6].sections[2] = new ErosionObjSprites.Section();
         }

         if (this.hasChildSprite) {
            this.sprites[var6].sections[3] = new ErosionObjSprites.Section();
         }
      }

   }

   private String getSprite(int var1, int var2, int var3) {
      return this.sprites[var1] != null && this.sprites[var1].sections[var2] != null && this.sprites[var1].sections[var2].seasons[var3] != null ? this.sprites[var1].sections[var2].seasons[var3].getNext() : null;
   }

   public String getBase(int var1, int var2) {
      return this.getSprite(var1, 0, var2);
   }

   public String getFlower(int var1) {
      return this.hasFlower ? this.getSprite(var1, 2, 0) : null;
   }

   public String getChildSprite(int var1, int var2) {
      return this.hasChildSprite ? this.getSprite(var1, 3, var2) : null;
   }

   private void setSprite(int var1, int var2, String var3, int var4) {
      if (this.sprites[var1] != null && this.sprites[var1].sections[var2] != null) {
         this.sprites[var1].sections[var2].seasons[var4] = new ErosionObjSprites.Sprites(var3);
      }

   }

   private void setSprite(int var1, int var2, ArrayList var3, int var4) {
      assert !var3.isEmpty();

      if (this.sprites[var1] != null && this.sprites[var1].sections[var2] != null) {
         this.sprites[var1].sections[var2].seasons[var4] = new ErosionObjSprites.Sprites(var3);
      }

   }

   public void setBase(int var1, String var2, int var3) {
      this.setSprite(var1, 0, (String)var2, var3);
   }

   public void setBase(int var1, ArrayList var2, int var3) {
      this.setSprite(var1, 0, (ArrayList)var2, var3);
   }

   public void setFlower(int var1, String var2) {
      this.setSprite(var1, 2, (String)var2, 0);
   }

   public void setFlower(int var1, ArrayList var2) {
      this.setSprite(var1, 2, (ArrayList)var2, 0);
   }

   public void setChildSprite(int var1, String var2, int var3) {
      this.setSprite(var1, 3, (String)var2, var3);
   }

   public void setChildSprite(int var1, ArrayList var2, int var3) {
      this.setSprite(var1, 3, (ArrayList)var2, var3);
   }

   private static class Stage {
      public ErosionObjSprites.Section[] sections = new ErosionObjSprites.Section[4];
   }

   private static class Section {
      public ErosionObjSprites.Sprites[] seasons = new ErosionObjSprites.Sprites[6];
   }

   private static final class Sprites {
      public final ArrayList sprites = new ArrayList();
      private int index = -1;

      public Sprites(String var1) {
         if (Core.bDebug || GameServer.bServer && GameServer.bDebug) {
            IsoSprite var2 = IsoSpriteManager.instance.getSprite(var1);
            if (var2.CurrentAnim.Frames.size() == 0 || !GameServer.bServer && ((IsoDirectionFrame)var2.CurrentAnim.Frames.get(0)).getTexture(IsoDirections.N) == null || var2.ID < 10000) {
               DebugLog.log("EMPTY SPRITE " + var1);
            }
         }

         this.sprites.add(var1);
      }

      public Sprites(ArrayList var1) {
         if (Core.bDebug || GameServer.bServer && GameServer.bDebug) {
            for(int var2 = 0; var2 < var1.size(); ++var2) {
               IsoSprite var3 = IsoSpriteManager.instance.getSprite((String)var1.get(var2));
               if (var3.CurrentAnim.Frames.size() == 0 || !GameServer.bServer && ((IsoDirectionFrame)var3.CurrentAnim.Frames.get(0)).getTexture(IsoDirections.N) == null || var3.ID < 10000) {
                  Object var10000 = var1.get(var2);
                  DebugLog.log("EMPTY SPRITE " + (String)var10000);
               }
            }
         }

         this.sprites.addAll(var1);
      }

      public String getNext() {
         if (++this.index >= this.sprites.size()) {
            this.index = 0;
         }

         return (String)this.sprites.get(this.index);
      }
   }
}
