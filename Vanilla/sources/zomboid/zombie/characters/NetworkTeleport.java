package zombie.characters;

import zombie.Lua.LuaManager;
import zombie.core.Core;
import zombie.debug.DebugOptions;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.network.MPStatisticClient;
import zombie.network.NetworkVariables;
import zombie.network.packets.PlayerPacket;

public class NetworkTeleport {
   public static boolean enable = true;
   public static boolean enableInstantTeleport = true;
   private NetworkTeleport.Type teleportType;
   private IsoGameCharacter character;
   private boolean setNewPos;
   private float nx;
   private float ny;
   private byte nz;
   public float ndirection;
   private float tx;
   private float ty;
   private byte tz;
   private long startTime;
   private long duration;

   public NetworkTeleport(IsoGameCharacter var1, NetworkTeleport.Type var2, float var3, float var4, byte var5, float var6) {
      this.teleportType = NetworkTeleport.Type.none;
      this.character = null;
      this.setNewPos = false;
      this.nx = 0.0F;
      this.ny = 0.0F;
      this.nz = 0;
      this.tx = 0.0F;
      this.ty = 0.0F;
      this.tz = 0;
      this.character = var1;
      this.setNewPos = false;
      this.nx = var3;
      this.ny = var4;
      this.nz = var5;
      this.teleportType = var2;
      this.startTime = System.currentTimeMillis();
      this.duration = (long)(1000.0D * (double)var6);
      var1.setTeleport(this);
      if (Core.bDebug && var1.getNetworkCharacterAI() != null && DebugOptions.instance.MultiplayerShowTeleport.getValue()) {
         var1.getNetworkCharacterAI().setTeleportDebug(new NetworkTeleport.NetworkTeleportDebug(var1.getOnlineID(), var1.x, var1.y, var1.z, var3, var4, (float)var5, var1.getNetworkCharacterAI().predictionType));
      }

   }

   public void process(int var1) {
      if (!enable) {
         this.character.setX(this.nx);
         this.character.setY(this.ny);
         this.character.setZ((float)this.nz);
         this.character.ensureOnTile();
         this.character.setTeleport((NetworkTeleport)null);
         this.character = null;
      } else {
         boolean var2 = this.character.getCurrentSquare().isCanSee(var1);
         float var3 = Math.min(1.0F, (float)(System.currentTimeMillis() - this.startTime) / (float)this.duration);
         switch(this.teleportType) {
         case disappearing:
            if (var3 < 0.99F) {
               this.character.setAlpha(var1, Math.min(this.character.getAlpha(var1), 1.0F - var3));
            } else {
               this.stop(var1);
            }
            break;
         case teleportation:
            if (var3 < 0.5F) {
               if (this.character.isoPlayer == null || this.character.isoPlayer != null && this.character.isoPlayer.spottedByPlayer) {
                  this.character.setAlpha(var1, Math.min(this.character.getAlpha(var1), 1.0F - var3 * 2.0F));
               }
            } else if (var3 < 0.99F) {
               if (!this.setNewPos) {
                  this.setNewPos = true;
                  this.character.setX(this.nx);
                  this.character.setY(this.ny);
                  this.character.setZ((float)this.nz);
                  this.character.ensureOnTile();
               }

               if (this.character.isoPlayer == null || this.character.isoPlayer != null && this.character.isoPlayer.spottedByPlayer) {
                  this.character.setAlpha(var1, Math.min(this.character.getTargetAlpha(var1), (var3 - 0.5F) * 2.0F));
               }
            } else {
               this.stop(var1);
            }
            break;
         case materialization:
            if (var3 < 0.99F) {
               this.character.setAlpha(var1, Math.min(this.character.getTargetAlpha(var1), var3));
            } else {
               this.stop(var1);
            }
         }

      }
   }

   public void stop(int var1) {
      this.character.setTeleport((NetworkTeleport)null);
      switch(this.teleportType) {
      case disappearing:
         this.character.setAlpha(var1, Math.min(this.character.getAlpha(var1), 0.0F));
      default:
         this.character = null;
      }
   }

   public static boolean teleport(IsoGameCharacter var0, NetworkTeleport.Type var1, float var2, float var3, byte var4, float var5) {
      if (!enable) {
         return false;
      } else {
         if (var0.getCurrentSquare() != null && enableInstantTeleport) {
            boolean var6 = false;

            for(int var7 = 0; var7 < 4; ++var7) {
               if (var0.getCurrentSquare().isCanSee(var7)) {
                  var6 = true;
                  break;
               }
            }

            IsoGridSquare var9 = LuaManager.GlobalObject.getCell().getGridSquare((int)var2, (int)var3, var4);
            if (var9 != null) {
               for(int var8 = 0; var8 < 4; ++var8) {
                  if (var9.isCanSee(var8)) {
                     var6 = true;
                     break;
                  }
               }
            }

            if (!var6) {
               var0.setX(var2);
               var0.setY(var3);
               var0.setZ((float)var4);
               var0.ensureOnTile();
               return false;
            }
         }

         if (!var0.isTeleporting()) {
            if (var0 instanceof IsoZombie) {
               MPStatisticClient.getInstance().incrementZombiesTeleports();
            } else {
               MPStatisticClient.getInstance().incrementRemotePlayersTeleports();
            }

            new NetworkTeleport(var0, var1, var2, var3, var4, var5);
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean teleport(IsoGameCharacter var0, PlayerPacket var1, float var2) {
      if (!enable) {
         return false;
      } else {
         if (LuaManager.GlobalObject.getCell().getGridSquare((int)var1.x, (int)var1.y, var1.z) == null) {
            var0.setX(var1.x);
            var0.setY(var1.y);
            var0.setZ((float)var1.z);
            var0.realx = var1.realx;
            var0.realy = var1.realy;
            var0.realz = var1.realz;
            var0.realdir = IsoDirections.fromIndex(var1.realdir);
            var0.ensureOnTile();
         }

         IsoGridSquare var8;
         if (var0.getCurrentSquare() != null && enableInstantTeleport) {
            boolean var3 = false;

            for(int var4 = 0; var4 < 4; ++var4) {
               if (var0.getCurrentSquare().isCanSee(var4)) {
                  var3 = true;
                  break;
               }
            }

            var8 = LuaManager.GlobalObject.getCell().getGridSquare((int)var1.x, (int)var1.y, var1.z);
            if (var8 != null) {
               for(int var5 = 0; var5 < 4; ++var5) {
                  if (var8.isCanSee(var5)) {
                     var3 = true;
                     break;
                  }
               }
            }

            if (!var3) {
               var0.setX(var1.x);
               var0.setY(var1.y);
               var0.setZ((float)var1.z);
               var0.ensureOnTile();
               return false;
            }
         }

         if (!var0.isTeleporting()) {
            if (var0 instanceof IsoZombie) {
               MPStatisticClient.getInstance().incrementZombiesTeleports();
            } else {
               MPStatisticClient.getInstance().incrementRemotePlayersTeleports();
            }

            IsoGridSquare var7 = IsoWorld.instance.CurrentCell.getGridSquare((double)var0.x, (double)var0.y, (double)var0.z);
            if (var7 == null) {
               var8 = IsoWorld.instance.CurrentCell.getGridSquare((double)var1.realx, (double)var1.realy, (double)var1.realz);
               var0.setAlphaAndTarget(0.0F);
               var0.setX(var1.realx);
               var0.setY(var1.realy);
               var0.setZ((float)var1.realz);
               var0.ensureOnTile();
               float var11 = 0.5F;
               NetworkTeleport var6 = new NetworkTeleport(var0, NetworkTeleport.Type.materialization, var11 * var1.x + (1.0F - var11) * var1.realx, var11 * var1.y + (1.0F - var11) * var1.realy, (byte)((int)(var11 * (float)var1.z + (1.0F - var11) * (float)var1.realz)), var2);
               var6.ndirection = var1.direction;
               var6.tx = var1.x;
               var6.ty = var1.y;
               var6.tz = var1.z;
               return true;
            } else {
               float var9 = 0.5F;
               NetworkTeleport var10 = new NetworkTeleport(var0, NetworkTeleport.Type.teleportation, var9 * var1.x + (1.0F - var9) * var1.realx, var9 * var1.y + (1.0F - var9) * var1.realy, (byte)((int)(var9 * (float)var1.z + (1.0F - var9) * (float)var1.realz)), var2);
               var10.ndirection = var1.direction;
               var10.tx = var1.x;
               var10.ty = var1.y;
               var10.tz = var1.z;
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public static void update(IsoGameCharacter var0, PlayerPacket var1) {
      if (var0.isTeleporting()) {
         NetworkTeleport var2 = var0.getTeleport();
         if (var2.teleportType == NetworkTeleport.Type.teleportation) {
            float var3 = Math.min(1.0F, (float)(System.currentTimeMillis() - var2.startTime) / (float)var2.duration);
            if (var3 < 0.5F) {
               float var4 = 0.5F;
               var2.nx = var4 * var1.x + (1.0F - var4) * var1.realx;
               var2.ny = var4 * var1.y + (1.0F - var4) * var1.realy;
               var2.nz = (byte)((int)(var4 * (float)var1.z + (1.0F - var4) * (float)var1.realz));
            }

            var2.ndirection = var1.direction;
            var2.tx = var1.x;
            var2.ty = var1.y;
            var2.tz = var1.z;
         }
      }
   }

   public static enum Type {
      none,
      disappearing,
      teleportation,
      materialization;

      // $FF: synthetic method
      private static NetworkTeleport.Type[] $values() {
         return new NetworkTeleport.Type[]{none, disappearing, teleportation, materialization};
      }
   }

   public static class NetworkTeleportDebug {
      short id;
      float nx;
      float ny;
      float nz;
      float lx;
      float ly;
      float lz;
      NetworkVariables.PredictionTypes type;

      public NetworkTeleportDebug(short var1, float var2, float var3, float var4, float var5, float var6, float var7, NetworkVariables.PredictionTypes var8) {
         this.id = var1;
         this.nx = var5;
         this.ny = var6;
         this.nz = var7;
         this.lx = var2;
         this.ly = var3;
         this.lz = var4;
         this.type = var8;
      }

      public float getDistance() {
         return IsoUtils.DistanceTo(this.lx, this.ly, this.lz, this.nx, this.ny, this.nz);
      }
   }
}
