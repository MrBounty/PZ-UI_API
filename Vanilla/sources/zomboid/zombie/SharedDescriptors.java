package zombie;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.IHumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.debug.DebugLog;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.util.Type;

public final class SharedDescriptors {
   private static final int DESCRIPTOR_COUNT = 500;
   private static final int DESCRIPTOR_ID_START = 500;
   private static final byte[] DESCRIPTOR_MAGIC = new byte[]{68, 69, 83, 67};
   private static final int VERSION_1 = 1;
   private static final int VERSION_2 = 2;
   private static final int VERSION = 2;
   private static SharedDescriptors.Descriptor[] PlayerZombieDescriptors = new SharedDescriptors.Descriptor[10];
   private static final int FIRST_PLAYER_ZOMBIE_DESCRIPTOR_ID = 1000;

   public static void initSharedDescriptors() {
      if (GameServer.bServer) {
         ;
      }
   }

   private static void noise(String var0) {
      DebugLog.log("shared-descriptor: " + var0);
   }

   public static void createPlayerZombieDescriptor(IsoZombie var0) {
      if (GameServer.bServer) {
         if (var0.isReanimatedPlayer()) {
            if (var0.getDescriptor().getID() == 0) {
               int var1 = -1;

               int var2;
               for(var2 = 0; var2 < PlayerZombieDescriptors.length; ++var2) {
                  if (PlayerZombieDescriptors[var2] == null) {
                     var1 = var2;
                     break;
                  }
               }

               if (var1 == -1) {
                  SharedDescriptors.Descriptor[] var10 = new SharedDescriptors.Descriptor[PlayerZombieDescriptors.length + 10];
                  System.arraycopy(PlayerZombieDescriptors, 0, var10, 0, PlayerZombieDescriptors.length);
                  var1 = PlayerZombieDescriptors.length;
                  PlayerZombieDescriptors = var10;
                  noise("resized PlayerZombieDescriptors array size=" + PlayerZombieDescriptors.length);
               }

               var0.getDescriptor().setID(1000 + var1);
               var2 = PersistentOutfits.instance.pickOutfit("ReanimatedPlayer", var0.isFemale());
               var2 = var2 & -65536 | var1 + 1;
               var0.setPersistentOutfitID(var2);
               SharedDescriptors.Descriptor var3 = new SharedDescriptors.Descriptor();
               var3.bFemale = var0.isFemale();
               var3.bZombie = false;
               var3.ID = 1000 + var1;
               var3.persistentOutfitID = var2;
               var3.getHumanVisual().copyFrom(var0.getHumanVisual());
               ItemVisuals var4 = new ItemVisuals();
               var0.getItemVisuals(var4);

               int var5;
               for(var5 = 0; var5 < var4.size(); ++var5) {
                  ItemVisual var6 = new ItemVisual((ItemVisual)var4.get(var5));
                  var3.itemVisuals.add(var6);
               }

               PlayerZombieDescriptors[var1] = var3;
               noise("added id=" + var3.getID());

               for(var5 = 0; var5 < GameServer.udpEngine.connections.size(); ++var5) {
                  UdpConnection var11 = (UdpConnection)GameServer.udpEngine.connections.get(var5);
                  ByteBufferWriter var7 = var11.startPacket();

                  try {
                     PacketTypes.PacketType.ZombieDescriptors.doPacket(var7);
                     var3.save(var7.bb);
                     PacketTypes.PacketType.ZombieDescriptors.send(var11);
                  } catch (Exception var9) {
                     var9.printStackTrace();
                     var11.cancelPacket();
                  }
               }

            }
         }
      }
   }

   public static void releasePlayerZombieDescriptor(IsoZombie var0) {
      if (GameServer.bServer) {
         if (var0.isReanimatedPlayer()) {
            int var1 = var0.getDescriptor().getID() - 1000;
            if (var1 >= 0 && var1 < PlayerZombieDescriptors.length) {
               noise("released id=" + var0.getDescriptor().getID());
               var0.getDescriptor().setID(0);
               PlayerZombieDescriptors[var1] = null;
            }
         }
      }
   }

   public static SharedDescriptors.Descriptor[] getPlayerZombieDescriptors() {
      return PlayerZombieDescriptors;
   }

   public static void registerPlayerZombieDescriptor(SharedDescriptors.Descriptor var0) {
      if (GameClient.bClient) {
         int var1 = var0.getID() - 1000;
         if (var1 >= 0 && var1 < 32767) {
            if (PlayerZombieDescriptors.length <= var1) {
               int var2 = (var1 + 10) / 10 * 10;
               SharedDescriptors.Descriptor[] var3 = new SharedDescriptors.Descriptor[var2];
               System.arraycopy(PlayerZombieDescriptors, 0, var3, 0, PlayerZombieDescriptors.length);
               PlayerZombieDescriptors = var3;
               noise("resized PlayerZombieDescriptors array size=" + PlayerZombieDescriptors.length);
            }

            PlayerZombieDescriptors[var1] = var0;
            noise("registered id=" + var0.getID());
         }
      }
   }

   public static void ApplyReanimatedPlayerOutfit(int var0, String var1, IsoGameCharacter var2) {
      IsoZombie var3 = (IsoZombie)Type.tryCastTo(var2, IsoZombie.class);
      if (var3 != null) {
         short var4 = (short)(var0 & '\uffff');
         if (var4 >= 1 && var4 <= PlayerZombieDescriptors.length) {
            SharedDescriptors.Descriptor var5 = PlayerZombieDescriptors[var4 - 1];
            if (var5 != null) {
               var3.useDescriptor(var5);
            }
         }
      }
   }

   public static final class Descriptor implements IHumanVisual {
      public int ID = 0;
      public int persistentOutfitID = 0;
      public String outfitName;
      public final HumanVisual humanVisual = new HumanVisual(this);
      public final ItemVisuals itemVisuals = new ItemVisuals();
      public boolean bFemale = false;
      public boolean bZombie = false;

      public int getID() {
         return this.ID;
      }

      public int getPersistentOutfitID() {
         return this.persistentOutfitID;
      }

      public HumanVisual getHumanVisual() {
         return this.humanVisual;
      }

      public void getItemVisuals(ItemVisuals var1) {
         var1.clear();
         var1.addAll(this.itemVisuals);
      }

      public boolean isFemale() {
         return this.bFemale;
      }

      public boolean isZombie() {
         return this.bZombie;
      }

      public boolean isSkeleton() {
         return false;
      }

      public void save(ByteBuffer var1) throws IOException {
         byte var2 = 0;
         if (this.bFemale) {
            var2 = (byte)(var2 | 1);
         }

         if (this.bZombie) {
            var2 = (byte)(var2 | 2);
         }

         var1.put(var2);
         var1.putInt(this.ID);
         var1.putInt(this.persistentOutfitID);
         GameWindow.WriteStringUTF(var1, this.outfitName);
         this.humanVisual.save(var1);
         this.itemVisuals.save(var1);
      }

      public void load(ByteBuffer var1, int var2) throws IOException {
         this.humanVisual.clear();
         this.itemVisuals.clear();
         byte var3 = var1.get();
         this.bFemale = (var3 & 1) != 0;
         this.bZombie = (var3 & 2) != 0;
         this.ID = var1.getInt();
         this.persistentOutfitID = var1.getInt();
         this.outfitName = GameWindow.ReadStringUTF(var1);
         this.humanVisual.load(var1, var2);
         short var4 = var1.getShort();

         for(int var5 = 0; var5 < var4; ++var5) {
            ItemVisual var6 = new ItemVisual();
            var6.load(var1, var2);
            this.itemVisuals.add(var6);
         }

      }
   }

   private static final class DescriptorList extends ArrayList {
   }
}
