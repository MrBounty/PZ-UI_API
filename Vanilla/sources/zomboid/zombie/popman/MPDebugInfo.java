package zombie.popman;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import zombie.GameTime;
import zombie.WorldSoundManager;
import zombie.characters.IsoPlayer;
import zombie.core.Color;
import zombie.core.Colors;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.RakVoice;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugOptions;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;

public final class MPDebugInfo {
   public static final MPDebugInfo instance = new MPDebugInfo();
   private static final ConcurrentHashMap debugSounds = new ConcurrentHashMap();
   private final ArrayList loadedCells = new ArrayList();
   private final ObjectPool cellPool = new ObjectPool(MPDebugInfo.MPCell::new);
   private final LoadedAreas loadedAreas = new LoadedAreas(false);
   private ArrayList repopEvents = new ArrayList();
   private final ObjectPool repopEventPool = new ObjectPool(MPDebugInfo.MPRepopEvent::new);
   private short repopEpoch = 0;
   private long requestTime = 0L;
   private boolean requestFlag = false;
   private boolean requestPacketReceived = false;
   private final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
   private float RESPAWN_EVERY_HOURS = 1.0F;
   private float REPOP_DISPLAY_HOURS = 0.5F;

   private static native boolean n_hasData(boolean var0);

   private static native void n_requestData();

   private static native int n_getLoadedCellsCount();

   private static native int n_getLoadedCellsData(int var0, ByteBuffer var1);

   private static native int n_getLoadedAreasCount();

   private static native int n_getLoadedAreasData(int var0, ByteBuffer var1);

   private static native int n_getRepopEventCount();

   private static native int n_getRepopEventData(int var0, ByteBuffer var1);

   private void requestServerInfo() {
      if (GameClient.bClient) {
         long var1 = System.currentTimeMillis();
         if (this.requestTime + 1000L <= var1) {
            this.requestTime = var1;
            ByteBufferWriter var3 = GameClient.connection.startPacket();
            PacketTypes.PacketType.KeepAlive.doPacket(var3);
            var3.bb.put((byte)1);
            var3.bb.putShort(this.repopEpoch);
            PacketTypes.PacketType.KeepAlive.send(GameClient.connection);
         }
      }
   }

   public void clientPacket(ByteBuffer var1) {
      if (GameClient.bClient) {
         byte var2 = var1.get();
         short var3;
         int var4;
         if (var2 == 1) {
            this.cellPool.release((List)this.loadedCells);
            this.loadedCells.clear();
            this.RESPAWN_EVERY_HOURS = var1.getFloat();
            var3 = var1.getShort();

            for(var4 = 0; var4 < var3; ++var4) {
               MPDebugInfo.MPCell var5 = (MPDebugInfo.MPCell)this.cellPool.alloc();
               var5.cx = var1.getShort();
               var5.cy = var1.getShort();
               var5.currentPopulation = var1.getShort();
               var5.desiredPopulation = var1.getShort();
               var5.lastRepopTime = var1.getFloat();
               this.loadedCells.add(var5);
            }

            this.loadedAreas.clear();
            short var10 = var1.getShort();

            for(int var11 = 0; var11 < var10; ++var11) {
               short var6 = var1.getShort();
               short var7 = var1.getShort();
               short var8 = var1.getShort();
               short var9 = var1.getShort();
               this.loadedAreas.add(var6, var7, var8, var9);
            }
         }

         if (var2 == 2) {
            this.repopEventPool.release((List)this.repopEvents);
            this.repopEvents.clear();
            this.repopEpoch = var1.getShort();
            var3 = var1.getShort();

            for(var4 = 0; var4 < var3; ++var4) {
               MPDebugInfo.MPRepopEvent var12 = (MPDebugInfo.MPRepopEvent)this.repopEventPool.alloc();
               var12.wx = var1.getShort();
               var12.wy = var1.getShort();
               var12.worldAge = var1.getFloat();
               this.repopEvents.add(var12);
            }
         }

      }
   }

   public void serverPacket(ByteBuffer var1, UdpConnection var2) {
      if (GameServer.bServer) {
         if (var2.accessLevel.equals("admin")) {
            byte var3 = var1.get();
            short var4;
            if (var3 == 1) {
               this.requestTime = System.currentTimeMillis();
               this.requestPacketReceived = true;
               var4 = var1.getShort();
               ByteBufferWriter var5 = var2.startPacket();
               PacketTypes.PacketType.KeepAlive.doPacket(var5);
               var5.bb.put((byte)1);
               var5.bb.putFloat(this.RESPAWN_EVERY_HOURS);
               var5.bb.putShort((short)this.loadedCells.size());

               int var6;
               for(var6 = 0; var6 < this.loadedCells.size(); ++var6) {
                  MPDebugInfo.MPCell var7 = (MPDebugInfo.MPCell)this.loadedCells.get(var6);
                  var5.bb.putShort(var7.cx);
                  var5.bb.putShort(var7.cy);
                  var5.bb.putShort(var7.currentPopulation);
                  var5.bb.putShort(var7.desiredPopulation);
                  var5.bb.putFloat(var7.lastRepopTime);
               }

               var5.bb.putShort((short)this.loadedAreas.count);

               for(var6 = 0; var6 < this.loadedAreas.count; ++var6) {
                  int var12 = var6 * 4;
                  var5.bb.putShort((short)this.loadedAreas.areas[var12++]);
                  var5.bb.putShort((short)this.loadedAreas.areas[var12++]);
                  var5.bb.putShort((short)this.loadedAreas.areas[var12++]);
                  var5.bb.putShort((short)this.loadedAreas.areas[var12++]);
               }

               if (var4 != this.repopEpoch) {
                  var3 = 2;
               }

               PacketTypes.PacketType.KeepAlive.send(var2);
            }

            if (var3 != 2) {
               short var10;
               if (var3 == 3) {
                  var4 = var1.getShort();
                  var10 = var1.getShort();
                  ZombiePopulationManager.instance.dbgSpawnTimeToZero(var4, var10);
               } else if (var3 == 4) {
                  var4 = var1.getShort();
                  var10 = var1.getShort();
                  ZombiePopulationManager.instance.dbgClearZombies(var4, var10);
               } else if (var3 == 5) {
                  var4 = var1.getShort();
                  var10 = var1.getShort();
                  ZombiePopulationManager.instance.dbgSpawnNow(var4, var10);
               }
            } else {
               ByteBufferWriter var8 = var2.startPacket();
               PacketTypes.PacketType.KeepAlive.doPacket(var8);
               var8.bb.put((byte)2);
               var8.bb.putShort(this.repopEpoch);
               var8.bb.putShort((short)this.repopEvents.size());

               for(int var9 = 0; var9 < this.repopEvents.size(); ++var9) {
                  MPDebugInfo.MPRepopEvent var11 = (MPDebugInfo.MPRepopEvent)this.repopEvents.get(var9);
                  var8.bb.putShort((short)var11.wx);
                  var8.bb.putShort((short)var11.wy);
                  var8.bb.putFloat(var11.worldAge);
               }

               PacketTypes.PacketType.KeepAlive.send(var2);
            }
         }
      }
   }

   public void request() {
      if (GameServer.bServer) {
         this.requestTime = System.currentTimeMillis();
      }
   }

   private void addRepopEvent(int var1, int var2, float var3) {
      float var4 = (float)GameTime.getInstance().getWorldAgeHours();

      while(!this.repopEvents.isEmpty() && ((MPDebugInfo.MPRepopEvent)this.repopEvents.get(0)).worldAge + this.REPOP_DISPLAY_HOURS < var4) {
         this.repopEventPool.release((Object)((MPDebugInfo.MPRepopEvent)this.repopEvents.remove(0)));
      }

      this.repopEvents.add(((MPDebugInfo.MPRepopEvent)this.repopEventPool.alloc()).init(var1, var2, var3));
      ++this.repopEpoch;
   }

   public void serverUpdate() {
      if (GameServer.bServer) {
         long var1 = System.currentTimeMillis();
         if (this.requestTime + 10000L < var1) {
            this.requestFlag = false;
            this.requestPacketReceived = false;
         } else {
            int var3;
            int var4;
            int var5;
            int var6;
            short var8;
            if (this.requestFlag) {
               if (n_hasData(false)) {
                  this.requestFlag = false;
                  this.cellPool.release((List)this.loadedCells);
                  this.loadedCells.clear();
                  this.loadedAreas.clear();
                  var3 = n_getLoadedCellsCount();
                  var4 = 0;

                  while(var4 < var3) {
                     this.byteBuffer.clear();
                     var5 = n_getLoadedCellsData(var4, this.byteBuffer);
                     var4 += var5;

                     for(var6 = 0; var6 < var5; ++var6) {
                        MPDebugInfo.MPCell var7 = (MPDebugInfo.MPCell)this.cellPool.alloc();
                        var7.cx = this.byteBuffer.getShort();
                        var7.cy = this.byteBuffer.getShort();
                        var7.currentPopulation = this.byteBuffer.getShort();
                        var7.desiredPopulation = this.byteBuffer.getShort();
                        var7.lastRepopTime = this.byteBuffer.getFloat();
                        this.loadedCells.add(var7);
                     }
                  }

                  var3 = n_getLoadedAreasCount();
                  var4 = 0;

                  while(var4 < var3) {
                     this.byteBuffer.clear();
                     var5 = n_getLoadedAreasData(var4, this.byteBuffer);
                     var4 += var5;

                     for(var6 = 0; var6 < var5; ++var6) {
                        boolean var12 = this.byteBuffer.get() == 0;
                        var8 = this.byteBuffer.getShort();
                        short var9 = this.byteBuffer.getShort();
                        short var10 = this.byteBuffer.getShort();
                        short var11 = this.byteBuffer.getShort();
                        this.loadedAreas.add(var8, var9, var10, var11);
                     }
                  }
               }
            } else if (this.requestPacketReceived) {
               n_requestData();
               this.requestFlag = true;
               this.requestPacketReceived = false;
            }

            if (n_hasData(true)) {
               var3 = n_getRepopEventCount();
               var4 = 0;

               while(var4 < var3) {
                  this.byteBuffer.clear();
                  var5 = n_getRepopEventData(var4, this.byteBuffer);
                  var4 += var5;

                  for(var6 = 0; var6 < var5; ++var6) {
                     short var13 = this.byteBuffer.getShort();
                     var8 = this.byteBuffer.getShort();
                     float var14 = this.byteBuffer.getFloat();
                     this.addRepopEvent(var13, var8, var14);
                  }
               }
            }

         }
      }
   }

   boolean isRespawnEnabled() {
      if (IsoWorld.getZombiesDisabled()) {
         return false;
      } else {
         return !(this.RESPAWN_EVERY_HOURS <= 0.0F);
      }
   }

   public void render(ZombiePopulationRenderer var1, float var2) {
      this.requestServerInfo();
      float var3 = (float)GameTime.getInstance().getWorldAgeHours();
      IsoMetaGrid var4 = IsoWorld.instance.MetaGrid;
      var1.outlineRect((float)(var4.minX * 300) * 1.0F, (float)(var4.minY * 300) * 1.0F, (float)((var4.maxX - var4.minX + 1) * 300) * 1.0F, (float)((var4.maxY - var4.minY + 1) * 300) * 1.0F, 1.0F, 1.0F, 1.0F, 0.25F);

      int var5;
      MPDebugInfo.MPCell var6;
      float var7;
      for(var5 = 0; var5 < this.loadedCells.size(); ++var5) {
         var6 = (MPDebugInfo.MPCell)this.loadedCells.get(var5);
         var1.outlineRect((float)(var6.cx * 300), (float)(var6.cy * 300), 300.0F, 300.0F, 1.0F, 1.0F, 1.0F, 0.25F);
         if (this.isRespawnEnabled()) {
            var7 = Math.min(var3 - var6.lastRepopTime, this.RESPAWN_EVERY_HOURS) / this.RESPAWN_EVERY_HOURS;
            if (var6.lastRepopTime > var3) {
               var7 = 0.0F;
            }

            var1.outlineRect((float)(var6.cx * 300 + 1), (float)(var6.cy * 300 + 1), 298.0F, 298.0F, 0.0F, 1.0F, 0.0F, var7 * var7);
         }
      }

      for(var5 = 0; var5 < this.loadedAreas.count; ++var5) {
         int var12 = var5 * 4;
         int var14 = this.loadedAreas.areas[var12++];
         int var8 = this.loadedAreas.areas[var12++];
         int var9 = this.loadedAreas.areas[var12++];
         int var10 = this.loadedAreas.areas[var12++];
         var1.outlineRect((float)(var14 * 10), (float)(var8 * 10), (float)(var9 * 10), (float)(var10 * 10), 0.7F, 0.7F, 0.7F, 1.0F);
      }

      for(var5 = 0; var5 < this.repopEvents.size(); ++var5) {
         MPDebugInfo.MPRepopEvent var15 = (MPDebugInfo.MPRepopEvent)this.repopEvents.get(var5);
         if (!(var15.worldAge + this.REPOP_DISPLAY_HOURS < var3)) {
            var7 = 1.0F - (var3 - var15.worldAge) / this.REPOP_DISPLAY_HOURS;
            var7 = Math.max(var7, 0.1F);
            var1.outlineRect((float)(var15.wx * 10), (float)(var15.wy * 10), 50.0F, 50.0F, 0.0F, 0.0F, 1.0F, var7);
         }
      }

      Color var23;
      if (GameClient.bClient && DebugOptions.instance.MultiplayerShowPosition.getValue()) {
         float var13 = (float)((IsoChunkMap.ChunkGridWidth / 2 + 2) * 10);
         Iterator var17 = GameClient.positions.entrySet().iterator();

         while(var17.hasNext()) {
            Entry var21 = (Entry)var17.next();
            IsoPlayer var18 = (IsoPlayer)GameClient.IDToPlayerMap.get(var21.getKey());
            Color var22 = Color.white;
            if (var18 != null) {
               var22 = var18.getSpeakColour();
            }

            Vector2 var25 = (Vector2)var21.getValue();
            var1.renderZombie(var25.x, var25.y, var22.r, var22.g, var22.b);
            var1.renderCircle(var25.x, var25.y, var13, var22.r, var22.g, var22.b, var22.a);
            var1.renderString(var25.x, var25.y, var18 == null ? String.valueOf(var21.getKey()) : var18.getUsername(), (double)var22.r, (double)var22.g, (double)var22.b, (double)var22.a);
         }

         if (IsoPlayer.getInstance() != null) {
            IsoPlayer var19 = IsoPlayer.getInstance();
            var23 = var19.getSpeakColour();
            var1.renderZombie(var19.x, var19.y, var23.r, var23.g, var23.b);
            var1.renderCircle(var19.x, var19.y, var13, var23.r, var23.g, var23.b, var23.a);
            var1.renderString(var19.x, var19.y, var19.getUsername(), (double)var23.r, (double)var23.g, (double)var23.b, (double)var23.a);
            var23 = Colors.LightBlue;
            var1.renderCircle(var19.x, var19.y, RakVoice.GetMinDistance(), var23.r, var23.g, var23.b, var23.a);
            var1.renderCircle(var19.x, var19.y, RakVoice.GetMaxDistance(), var23.r, var23.g, var23.b, var23.a);
         }
      }

      if (var2 > 0.25F) {
         for(var5 = 0; var5 < this.loadedCells.size(); ++var5) {
            var6 = (MPDebugInfo.MPCell)this.loadedCells.get(var5);
            var1.renderCellInfo(var6.cx, var6.cy, var6.currentPopulation, var6.desiredPopulation, var6.lastRepopTime + this.RESPAWN_EVERY_HOURS - var3);
         }
      }

      try {
         debugSounds.entrySet().removeIf((var0) -> {
            return System.currentTimeMillis() > (Long)var0.getKey() + 1000L;
         });
         Iterator var16 = debugSounds.entrySet().iterator();

         while(var16.hasNext()) {
            Entry var24 = (Entry)var16.next();
            var23 = Colors.LightBlue;
            if (((MPDebugInfo.MPSoundDebugInfo)var24.getValue()).sourceIsZombie) {
               var23 = Colors.GreenYellow;
            } else if (((MPDebugInfo.MPSoundDebugInfo)var24.getValue()).bRepeating) {
               var23 = Colors.Coral;
            }

            float var20 = 1.0F - Math.max(0.0F, Math.min(1.0F, (float)(System.currentTimeMillis() - (Long)var24.getKey()) / 1000.0F));
            var1.renderCircle((float)((MPDebugInfo.MPSoundDebugInfo)var24.getValue()).x, (float)((MPDebugInfo.MPSoundDebugInfo)var24.getValue()).y, (float)((MPDebugInfo.MPSoundDebugInfo)var24.getValue()).radius, var23.r, var23.g, var23.b, var20);
         }
      } catch (Exception var11) {
      }

   }

   public static void AddDebugSound(WorldSoundManager.WorldSound var0) {
      try {
         debugSounds.put(System.currentTimeMillis(), new MPDebugInfo.MPSoundDebugInfo(var0));
      } catch (Exception var2) {
      }

   }

   private static final class MPCell {
      public short cx;
      public short cy;
      public short currentPopulation;
      public short desiredPopulation;
      public float lastRepopTime;

      MPDebugInfo.MPCell init(int var1, int var2, int var3, int var4, float var5) {
         this.cx = (short)var1;
         this.cy = (short)var2;
         this.currentPopulation = (short)var3;
         this.desiredPopulation = (short)var4;
         this.lastRepopTime = var5;
         return this;
      }
   }

   private static final class MPRepopEvent {
      public int wx;
      public int wy;
      public float worldAge;

      public MPDebugInfo.MPRepopEvent init(int var1, int var2, float var3) {
         this.wx = var1;
         this.wy = var2;
         this.worldAge = var3;
         return this;
      }
   }

   private static class MPSoundDebugInfo {
      int x;
      int y;
      int radius;
      boolean bRepeating;
      boolean sourceIsZombie;

      MPSoundDebugInfo(WorldSoundManager.WorldSound var1) {
         this.x = var1.x;
         this.y = var1.y;
         this.radius = var1.radius;
         this.bRepeating = var1.bRepeating;
         this.sourceIsZombie = var1.sourceIsZombie;
      }
   }
}
