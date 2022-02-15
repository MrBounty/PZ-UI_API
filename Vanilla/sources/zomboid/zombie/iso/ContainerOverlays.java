package zombie.iso;

import gnu.trove.map.hash.THashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.core.textures.Texture;
import zombie.inventory.ItemContainer;
import zombie.iso.objects.IsoStove;
import zombie.network.GameServer;
import zombie.util.LocationRNG;
import zombie.util.StringUtils;

public class ContainerOverlays {
   public static final ContainerOverlays instance = new ContainerOverlays();
   private static final ArrayList tempEntries = new ArrayList();
   private final THashMap overlayMap = new THashMap();

   private void parseContainerOverlayMapV0(KahluaTableImpl var1) {
      Iterator var2 = var1.delegate.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         String var4 = var3.getKey().toString();
         ContainerOverlays.ContainerOverlay var5 = new ContainerOverlays.ContainerOverlay();
         var5.name = var4;
         this.overlayMap.put(var5.name, var5);
         KahluaTableImpl var6 = (KahluaTableImpl)var3.getValue();
         Iterator var7 = var6.delegate.entrySet().iterator();

         while(var7.hasNext()) {
            Entry var8 = (Entry)var7.next();
            String var9 = var8.getKey().toString();
            KahluaTableImpl var10 = (KahluaTableImpl)var8.getValue();
            String var11 = null;
            if (var10.delegate.containsKey(1.0D)) {
               var11 = var10.rawget(1.0D).toString();
            }

            String var12 = null;
            if (var10.delegate.containsKey(2.0D)) {
               var12 = var10.rawget(2.0D).toString();
            }

            ContainerOverlays.ContainerOverlayEntry var13 = new ContainerOverlays.ContainerOverlayEntry();
            var13.manyItems = var11;
            var13.fewItems = var12;
            var13.room = var9;
            var5.entries.add(var13);
         }
      }

   }

   private void parseContainerOverlayMapV1(KahluaTableImpl var1) {
      KahluaTableIterator var2 = var1.iterator();

      while(true) {
         String var3;
         do {
            if (!var2.advance()) {
               return;
            }

            var3 = var2.getKey().toString();
         } while("VERSION".equalsIgnoreCase(var3));

         ContainerOverlays.ContainerOverlay var4 = new ContainerOverlays.ContainerOverlay();
         var4.name = var3;
         KahluaTableImpl var5 = (KahluaTableImpl)var2.getValue();
         KahluaTableIterator var6 = var5.iterator();

         while(var6.advance()) {
            KahluaTableImpl var7 = (KahluaTableImpl)var6.getValue();
            String var8 = var7.rawgetStr("name");
            KahluaTableImpl var9 = (KahluaTableImpl)var7.rawget("tiles");
            ContainerOverlays.ContainerOverlayEntry var10 = new ContainerOverlays.ContainerOverlayEntry();
            var10.manyItems = (String)var9.rawget(1);
            var10.fewItems = (String)var9.rawget(2);
            if (StringUtils.isNullOrWhitespace(var10.manyItems) || "none".equalsIgnoreCase(var10.manyItems)) {
               var10.manyItems = null;
            }

            if (StringUtils.isNullOrWhitespace(var10.fewItems) || "none".equalsIgnoreCase(var10.fewItems)) {
               var10.fewItems = null;
            }

            var10.room = var8;
            var4.entries.add(var10);
         }

         this.overlayMap.put(var4.name, var4);
      }
   }

   public void addOverlays(KahluaTableImpl var1) {
      int var2 = var1.rawgetInt("VERSION");
      if (var2 == -1) {
         this.parseContainerOverlayMapV0(var1);
      } else {
         if (var2 != 1) {
            throw new RuntimeException("unknown overlayMap.VERSION " + var2);
         }

         this.parseContainerOverlayMapV1(var1);
      }

   }

   public boolean hasOverlays(IsoObject var1) {
      return var1 != null && var1.sprite != null && var1.sprite.name != null && this.overlayMap.containsKey(var1.sprite.name);
   }

   public void updateContainerOverlaySprite(IsoObject var1) {
      if (var1 != null) {
         if (!(var1 instanceof IsoStove)) {
            IsoGridSquare var2 = var1.getSquare();
            if (var2 != null) {
               String var3 = null;
               ItemContainer var4 = var1.getContainer();
               if (var1.sprite != null && var1.sprite.name != null && var4 != null && var4.getItems() != null && !var4.isEmpty()) {
                  ContainerOverlays.ContainerOverlay var5 = (ContainerOverlays.ContainerOverlay)this.overlayMap.get(var1.sprite.name);
                  if (var5 != null) {
                     String var6 = "other";
                     if (var2.getRoom() != null) {
                        var6 = var2.getRoom().getName();
                     }

                     ContainerOverlays.ContainerOverlayEntry var7 = var5.pickRandom(var6, var2.x, var2.y, var2.z);
                     if (var7 == null) {
                        var7 = var5.pickRandom("other", var2.x, var2.y, var2.z);
                     }

                     if (var7 != null) {
                        var3 = var7.manyItems;
                        if (var7.fewItems != null && var4.getItems().size() < 7) {
                           var3 = var7.fewItems;
                        }
                     }
                  }
               }

               if (!StringUtils.isNullOrWhitespace(var3) && !GameServer.bServer && Texture.getSharedTexture(var3) == null) {
                  var3 = null;
               }

               var1.setOverlaySprite(var3);
            }
         }
      }
   }

   public void Reset() {
      this.overlayMap.clear();
   }

   private static final class ContainerOverlay {
      public String name;
      public final ArrayList entries = new ArrayList();

      public void getEntries(String var1, ArrayList var2) {
         var2.clear();

         for(int var3 = 0; var3 < this.entries.size(); ++var3) {
            ContainerOverlays.ContainerOverlayEntry var4 = (ContainerOverlays.ContainerOverlayEntry)this.entries.get(var3);
            if (var4.room.equalsIgnoreCase(var1)) {
               var2.add(var4);
            }
         }

      }

      public ContainerOverlays.ContainerOverlayEntry pickRandom(String var1, int var2, int var3, int var4) {
         this.getEntries(var1, ContainerOverlays.tempEntries);
         if (ContainerOverlays.tempEntries.isEmpty()) {
            return null;
         } else {
            int var5 = LocationRNG.instance.nextInt(ContainerOverlays.tempEntries.size(), var2, var3, var4);
            return (ContainerOverlays.ContainerOverlayEntry)ContainerOverlays.tempEntries.get(var5);
         }
      }
   }

   private static final class ContainerOverlayEntry {
      public String room;
      public String manyItems;
      public String fewItems;
   }
}
