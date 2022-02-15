package zombie.iso;

import gnu.trove.map.hash.THashMap;
import java.util.ArrayList;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.core.Core;
import zombie.core.math.PZMath;
import zombie.core.textures.Texture;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameServer;
import zombie.util.LocationRNG;
import zombie.util.StringUtils;

public class TileOverlays {
   public static final TileOverlays instance = new TileOverlays();
   private static final THashMap overlayMap = new THashMap();
   private static final ArrayList tempEntries = new ArrayList();

   public void addOverlays(KahluaTableImpl var1) {
      KahluaTableIterator var2 = var1.iterator();

      while(true) {
         String var3;
         do {
            if (!var2.advance()) {
               return;
            }

            var3 = var2.getKey().toString();
         } while("VERSION".equalsIgnoreCase(var3));

         TileOverlays.TileOverlay var4 = new TileOverlays.TileOverlay();
         var4.tile = var3;
         KahluaTableImpl var5 = (KahluaTableImpl)var2.getValue();
         KahluaTableIterator var6 = var5.iterator();

         while(var6.advance()) {
            KahluaTableImpl var7 = (KahluaTableImpl)var6.getValue();
            TileOverlays.TileOverlayEntry var8 = new TileOverlays.TileOverlayEntry();
            var8.room = var7.rawgetStr("name");
            var8.chance = var7.rawgetInt("chance");
            var8.usage.parse(var7.rawgetStr("usage"));
            KahluaTableImpl var9 = (KahluaTableImpl)var7.rawget("tiles");

            String var11;
            for(KahluaTableIterator var10 = var9.iterator(); var10.advance(); var8.tiles.add(var11)) {
               var11 = var10.getValue().toString();
               if (!StringUtils.isNullOrWhitespace(var11) && !"none".equalsIgnoreCase(var11)) {
                  if (Core.bDebug && !GameServer.bServer && Texture.getSharedTexture(var11) == null) {
                     System.out.println("BLANK OVERLAY TEXTURE. Set it to \"none\".: " + var11);
                  }
               } else {
                  var11 = "";
               }
            }

            var4.entries.add(var8);
         }

         overlayMap.put(var4.tile, var4);
      }
   }

   public boolean hasOverlays(IsoObject var1) {
      return var1 != null && var1.sprite != null && var1.sprite.name != null && overlayMap.containsKey(var1.sprite.name);
   }

   public void updateTileOverlaySprite(IsoObject var1) {
      if (var1 != null) {
         IsoGridSquare var2 = var1.getSquare();
         if (var2 != null) {
            String var3 = null;
            float var4 = -1.0F;
            float var5 = -1.0F;
            float var6 = -1.0F;
            float var7 = -1.0F;
            if (var1.sprite != null && var1.sprite.name != null) {
               TileOverlays.TileOverlay var8 = (TileOverlays.TileOverlay)overlayMap.get(var1.sprite.name);
               if (var8 != null) {
                  String var9 = "other";
                  if (var2.getRoom() != null) {
                     var9 = var2.getRoom().getName();
                  }

                  TileOverlays.TileOverlayEntry var10 = var8.pickRandom(var9, var2);
                  if (var10 == null) {
                     var10 = var8.pickRandom("other", var2);
                  }

                  if (var10 != null) {
                     if (var10.usage.bTableTop && this.hasObjectOnTop(var1)) {
                        return;
                     }

                     var3 = var10.pickRandom(var2.x, var2.y, var2.z);
                     if (var10.usage.alpha >= 0.0F) {
                        var6 = 1.0F;
                        var5 = 1.0F;
                        var4 = 1.0F;
                        var7 = var10.usage.alpha;
                     }
                  }
               }
            }

            if (!StringUtils.isNullOrWhitespace(var3) && !GameServer.bServer && Texture.getSharedTexture(var3) == null) {
               var3 = null;
            }

            if (!StringUtils.isNullOrWhitespace(var3)) {
               if (var1.AttachedAnimSprite == null) {
                  var1.AttachedAnimSprite = new ArrayList(4);
               }

               IsoSprite var11 = IsoSpriteManager.instance.getSprite(var3);
               var11.name = var3;
               IsoSpriteInstance var12 = IsoSpriteInstance.get(var11);
               if (var7 > 0.0F) {
                  var12.tintr = var4;
                  var12.tintg = var5;
                  var12.tintb = var6;
                  var12.alpha = var7;
               }

               var12.bCopyTargetAlpha = false;
               var12.bMultiplyObjectAlpha = true;
               var1.AttachedAnimSprite.add(var12);
            }

         }
      }
   }

   private boolean hasObjectOnTop(IsoObject var1) {
      if (!var1.isTableSurface()) {
         return false;
      } else {
         IsoGridSquare var2 = var1.getSquare();

         for(int var3 = var1.getObjectIndex() + 1; var3 < var2.getObjects().size(); ++var3) {
            IsoObject var4 = (IsoObject)var2.getObjects().get(var3);
            if (var4.isTableTopObject() || var4.isTableSurface()) {
               return true;
            }
         }

         return false;
      }
   }

   public void fixTableTopOverlays(IsoGridSquare var1) {
      if (var1 != null && !var1.getObjects().isEmpty()) {
         boolean var2 = false;

         for(int var3 = var1.getObjects().size() - 1; var3 >= 0; --var3) {
            IsoObject var4 = (IsoObject)var1.getObjects().get(var3);
            if (var2 && var4.isTableSurface()) {
               this.removeTableTopOverlays(var4);
            }

            if (var4.isTableSurface() || var4.isTableTopObject()) {
               var2 = true;
            }
         }

      }
   }

   private void removeTableTopOverlays(IsoObject var1) {
      if (var1 != null && var1.isTableSurface()) {
         if (var1.sprite != null && var1.sprite.name != null) {
            if (var1.AttachedAnimSprite != null && !var1.AttachedAnimSprite.isEmpty()) {
               TileOverlays.TileOverlay var2 = (TileOverlays.TileOverlay)overlayMap.get(var1.sprite.name);
               if (var2 != null) {
                  int var3 = var1.AttachedAnimSprite.size();

                  for(int var4 = 0; var4 < var2.entries.size(); ++var4) {
                     TileOverlays.TileOverlayEntry var5 = (TileOverlays.TileOverlayEntry)var2.entries.get(var4);
                     if (var5.usage.bTableTop) {
                        for(int var6 = 0; var6 < var5.tiles.size(); ++var6) {
                           this.tryRemoveAttachedSprite(var1.AttachedAnimSprite, (String)var5.tiles.get(var6));
                        }
                     }
                  }

                  if (var3 != var1.AttachedAnimSprite.size()) {
                  }

               }
            }
         }
      }
   }

   private void tryRemoveAttachedSprite(ArrayList var1, String var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         IsoSpriteInstance var4 = (IsoSpriteInstance)var1.get(var3);
         if (var2.equals(var4.getName())) {
            var1.remove(var3--);
            IsoSpriteInstance.add(var4);
         }
      }

   }

   public void Reset() {
      overlayMap.clear();
   }

   private static final class TileOverlay {
      public String tile;
      public final ArrayList entries = new ArrayList();

      public void getEntries(String var1, IsoGridSquare var2, ArrayList var3) {
         var3.clear();

         for(int var4 = 0; var4 < this.entries.size(); ++var4) {
            TileOverlays.TileOverlayEntry var5 = (TileOverlays.TileOverlayEntry)this.entries.get(var4);
            if (var5.room.equalsIgnoreCase(var1) && var5.matchUsage(var2)) {
               var3.add(var5);
            }
         }

      }

      public TileOverlays.TileOverlayEntry pickRandom(String var1, IsoGridSquare var2) {
         this.getEntries(var1, var2, TileOverlays.tempEntries);
         if (TileOverlays.tempEntries.isEmpty()) {
            return null;
         } else {
            int var3 = LocationRNG.instance.nextInt(TileOverlays.tempEntries.size(), var2.x, var2.y, var2.z);
            return (TileOverlays.TileOverlayEntry)TileOverlays.tempEntries.get(var3);
         }
      }
   }

   private static final class TileOverlayEntry {
      public String room;
      public int chance;
      public final ArrayList tiles = new ArrayList();
      public final TileOverlays.TileOverlayUsage usage = new TileOverlays.TileOverlayUsage();

      public boolean matchUsage(IsoGridSquare var1) {
         return this.usage.match(var1);
      }

      public String pickRandom(int var1, int var2, int var3) {
         int var4 = LocationRNG.instance.nextInt(this.chance, var1, var2, var3);
         if (var4 == 0 && !this.tiles.isEmpty()) {
            var4 = LocationRNG.instance.nextInt(this.tiles.size());
            return (String)this.tiles.get(var4);
         } else {
            return null;
         }
      }
   }

   private static final class TileOverlayUsage {
      String usage;
      int zOnly = -1;
      int zGreaterThan = -1;
      float alpha = -1.0F;
      boolean bTableTop = false;

      boolean parse(String var1) {
         this.usage = var1.trim();
         if (StringUtils.isNullOrWhitespace(this.usage)) {
            return true;
         } else {
            String[] var2 = var1.split(";");

            for(int var3 = 0; var3 < var2.length; ++var3) {
               String var4 = var2[var3];
               if (var4.startsWith("z=")) {
                  this.zOnly = Integer.parseInt(var4.substring(2));
               } else if (var4.startsWith("z>")) {
                  this.zGreaterThan = Integer.parseInt(var4.substring(2));
               } else if (var4.startsWith("alpha=")) {
                  this.alpha = Float.parseFloat(var4.substring(6));
                  this.alpha = PZMath.clamp(this.alpha, 0.0F, 1.0F);
               } else {
                  if (!var4.startsWith("tabletop")) {
                     return false;
                  }

                  this.bTableTop = true;
               }
            }

            return true;
         }
      }

      boolean match(IsoGridSquare var1) {
         if (this.zOnly != -1 && var1.z != this.zOnly) {
            return false;
         } else {
            return this.zGreaterThan == -1 || var1.z > this.zGreaterThan;
         }
      }
   }
}
