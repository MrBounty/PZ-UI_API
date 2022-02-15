package zombie.network;

import java.nio.ByteBuffer;
import org.joml.Vector3f;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.vehicles.BaseVehicle;

public final class PassengerMap {
   private static final int CHUNKS = 7;
   private static final int MAX_PASSENGERS = 16;
   private static final PassengerMap.PassengerLocal[] perPlayerPngr = new PassengerMap.PassengerLocal[4];
   private static final PassengerMap.DriverLocal[] perPlayerDriver = new PassengerMap.DriverLocal[4];

   public static void updatePassenger(IsoPlayer var0) {
      if (var0 != null && var0.getVehicle() != null && !var0.getVehicle().isDriver(var0)) {
         IsoGameCharacter var1 = var0.getVehicle().getDriver();
         if (var1 instanceof IsoPlayer && !((IsoPlayer)var1).isLocalPlayer()) {
            PassengerMap.PassengerLocal var2 = perPlayerPngr[var0.PlayerIndex];
            var2.chunkMap = IsoWorld.instance.CurrentCell.ChunkMap[var0.PlayerIndex];
            var2.updateLoaded();
         }
      }
   }

   public static void serverReceivePacket(ByteBuffer var0, UdpConnection var1) {
      byte var2 = var0.get();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      long var5 = var0.getLong();
      IsoPlayer var7 = var1.players[var2];
      if (var7 != null && var7.getVehicle() != null) {
         IsoGameCharacter var8 = var7.getVehicle().getDriver();
         if (var8 instanceof IsoPlayer && var8 != var7) {
            UdpConnection var9 = GameServer.getConnectionFromPlayer((IsoPlayer)var8);
            if (var9 != null) {
               ByteBufferWriter var10 = var9.startPacket();
               PacketTypes.PacketType.PassengerMap.doPacket(var10);
               var10.putShort(var7.getVehicle().VehicleID);
               var10.putByte((byte)var7.getVehicle().getSeat(var7));
               var10.putInt(var3);
               var10.putInt(var4);
               var10.putLong(var5);
               PacketTypes.PacketType.PassengerMap.send(var9);
            }
         }
      }
   }

   public static void clientReceivePacket(ByteBuffer var0) {
      short var1 = var0.getShort();
      byte var2 = var0.get();
      int var3 = var0.getInt();
      int var4 = var0.getInt();
      long var5 = var0.getLong();

      for(int var7 = 0; var7 < IsoPlayer.numPlayers; ++var7) {
         IsoPlayer var8 = IsoPlayer.players[var7];
         if (var8 != null && var8.getVehicle() != null) {
            BaseVehicle var9 = var8.getVehicle();
            if (var9.VehicleID == var1 && var9.isDriver(var8)) {
               PassengerMap.DriverLocal var10 = perPlayerDriver[var7];
               PassengerMap.PassengerRemote var11 = var10.passengers[var2];
               if (var11 == null) {
                  var11 = var10.passengers[var2] = new PassengerMap.PassengerRemote();
               }

               var11.setLoaded(var3, var4, var5);
            }
         }
      }

   }

   public static boolean isChunkLoaded(BaseVehicle var0, int var1, int var2) {
      if (!GameClient.bClient) {
         return false;
      } else if (var0 != null && var1 >= 0 && var2 >= 0) {
         IsoGameCharacter var3 = var0.getDriver();
         if (var3 instanceof IsoPlayer && ((IsoPlayer)var3).isLocalPlayer()) {
            int var4 = ((IsoPlayer)var3).PlayerIndex;
            PassengerMap.DriverLocal var5 = perPlayerDriver[var4];

            for(int var6 = 1; var6 < var0.getMaxPassengers(); ++var6) {
               PassengerMap.PassengerRemote var7 = var5.passengers[var6];
               if (var7 != null && var7.wx != -1) {
                  IsoGameCharacter var8 = var0.getCharacter(var6);
                  if (var8 instanceof IsoPlayer && !((IsoPlayer)var8).isLocalPlayer()) {
                     int var9 = var7.wx - 3;
                     int var10 = var7.wy - 3;
                     if (var1 >= var9 && var2 >= var10 && var1 < var9 + 7 && var2 < var10 + 7 && (var7.loaded & 1L << var1 - var9 + (var2 - var10) * 7) == 0L) {
                        return false;
                     }
                  } else {
                     var7.wx = -1;
                  }
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static void render(int var0) {
      if (GameClient.bClient) {
         IsoPlayer var1 = IsoPlayer.players[var0];
         if (var1 != null && var1.getVehicle() != null) {
            BaseVehicle var2 = var1.getVehicle();
            int var3 = Core.TileScale;
            byte var4 = 10;
            float var5 = 0.1F;
            float var6 = 0.1F;
            float var7 = 0.1F;
            float var8 = 0.75F;
            float var9 = 0.0F;
            PassengerMap.DriverLocal var10 = perPlayerDriver[var0];

            for(int var11 = 1; var11 < var2.getMaxPassengers(); ++var11) {
               PassengerMap.PassengerRemote var12 = var10.passengers[var11];
               if (var12 != null && var12.wx != -1) {
                  IsoGameCharacter var13 = var2.getCharacter(var11);
                  if (var13 instanceof IsoPlayer && !((IsoPlayer)var13).isLocalPlayer()) {
                     for(int var14 = 0; var14 < 7; ++var14) {
                        for(int var15 = 0; var15 < 7; ++var15) {
                           boolean var16 = (var12.loaded & 1L << var15 + var14 * 7) != 0L;
                           if (!var16) {
                              float var17 = (float)((var12.wx - 3 + var15) * var4);
                              float var18 = (float)((var12.wy - 3 + var14) * var4);
                              float var19 = IsoUtils.XToScreenExact(var17, var18 + (float)var4, var9, 0);
                              float var20 = IsoUtils.YToScreenExact(var17, var18 + (float)var4, var9, 0);
                              SpriteRenderer.instance.renderPoly((float)((int)var19), (float)((int)var20), (float)((int)(var19 + (float)(var4 * 64 / 2 * var3))), (float)((int)(var20 - (float)(var4 * 32 / 2 * var3))), (float)((int)(var19 + (float)(var4 * 64 * var3))), (float)((int)var20), (float)((int)(var19 + (float)(var4 * 64 / 2 * var3))), (float)((int)(var20 + (float)(var4 * 32 / 2 * var3))), var5, var6, var7, var8);
                           }
                        }
                     }
                  } else {
                     var12.wx = -1;
                  }
               }
            }

         }
      }
   }

   public static void Reset() {
      for(int var0 = 0; var0 < 4; ++var0) {
         PassengerMap.PassengerLocal var1 = perPlayerPngr[var0];
         var1.wx = -1;
         PassengerMap.DriverLocal var2 = perPlayerDriver[var0];

         for(int var3 = 0; var3 < 16; ++var3) {
            PassengerMap.PassengerRemote var4 = var2.passengers[var3];
            if (var4 != null) {
               var4.wx = -1;
            }
         }
      }

   }

   static {
      for(int var0 = 0; var0 < 4; ++var0) {
         perPlayerPngr[var0] = new PassengerMap.PassengerLocal(var0);
         perPlayerDriver[var0] = new PassengerMap.DriverLocal();
      }

   }

   private static final class PassengerLocal {
      final int playerIndex;
      IsoChunkMap chunkMap;
      int wx = -1;
      int wy = -1;
      long loaded = 0L;

      PassengerLocal(int var1) {
         this.playerIndex = var1;
      }

      boolean setLoaded() {
         int var1 = this.chunkMap.WorldX;
         int var2 = this.chunkMap.WorldY;
         Vector3f var3 = IsoPlayer.players[this.playerIndex].getVehicle().jniLinearVelocity;
         float var4 = Math.abs(var3.x);
         float var5 = Math.abs(var3.z);
         boolean var6 = var3.x < 0.0F && var4 > var5;
         boolean var7 = var3.x > 0.0F && var4 > var5;
         boolean var8 = var3.z < 0.0F && var5 > var4;
         boolean var9 = var3.z > 0.0F && var5 > var4;
         if (var7) {
            ++var1;
         } else if (var6) {
            --var1;
         } else if (var8) {
            --var2;
         } else if (var9) {
            ++var2;
         }

         long var10 = 0L;

         for(int var12 = 0; var12 < 7; ++var12) {
            for(int var13 = 0; var13 < 7; ++var13) {
               IsoChunk var14 = this.chunkMap.getChunk(IsoChunkMap.ChunkGridWidth / 2 - 3 + var13, IsoChunkMap.ChunkGridWidth / 2 - 3 + var12);
               if (var14 != null && var14.bLoaded) {
                  var10 |= 1L << var13 + var12 * 7;
               }
            }
         }

         boolean var15 = var1 != this.wx || var2 != this.wy || var10 != this.loaded;
         if (var15) {
            this.wx = var1;
            this.wy = var2;
            this.loaded = var10;
         }

         return var15;
      }

      void updateLoaded() {
         if (this.setLoaded()) {
            this.clientSendPacket(GameClient.connection);
         }

      }

      void clientSendPacket(UdpConnection var1) {
         ByteBufferWriter var2 = var1.startPacket();
         PacketTypes.PacketType.PassengerMap.doPacket(var2);
         var2.putByte((byte)this.playerIndex);
         var2.putInt(this.wx);
         var2.putInt(this.wy);
         var2.putLong(this.loaded);
         PacketTypes.PacketType.PassengerMap.send(var1);
      }
   }

   private static final class DriverLocal {
      final PassengerMap.PassengerRemote[] passengers = new PassengerMap.PassengerRemote[16];
   }

   private static final class PassengerRemote {
      int wx = -1;
      int wy = -1;
      long loaded = 0L;

      void setLoaded(int var1, int var2, long var3) {
         this.wx = var1;
         this.wy = var2;
         this.loaded = var3;
      }
   }
}
