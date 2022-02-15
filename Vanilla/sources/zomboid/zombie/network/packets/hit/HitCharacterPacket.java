package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.network.GameClient;
import zombie.network.packets.INetworkPacket;

public abstract class HitCharacterPacket implements INetworkPacket {
   private final HitCharacterPacket.HitType hitType;

   public HitCharacterPacket(HitCharacterPacket.HitType var1) {
      this.hitType = var1;
   }

   public static HitCharacterPacket process(ByteBuffer var0) {
      byte var1 = var0.get();
      if (var1 > HitCharacterPacket.HitType.Min.ordinal() && var1 < HitCharacterPacket.HitType.Max.ordinal()) {
         Object var10000;
         switch(HitCharacterPacket.HitType.values()[var1]) {
         case PlayerHitSquare:
            var10000 = new PlayerHitSquarePacket();
            break;
         case PlayerHitVehicle:
            var10000 = new PlayerHitVehiclePacket();
            break;
         case PlayerHitZombie:
            var10000 = new PlayerHitZombiePacket();
            break;
         case PlayerHitPlayer:
            var10000 = new PlayerHitPlayerPacket();
            break;
         case ZombieHitPlayer:
            var10000 = new ZombieHitPlayerPacket();
            break;
         case VehicleHitZombie:
            var10000 = new VehicleHitZombiePacket();
            break;
         case VehicleHitPlayer:
            var10000 = new VehicleHitPlayerPacket();
            break;
         default:
            var10000 = null;
         }

         return (HitCharacterPacket)var10000;
      } else {
         return null;
      }
   }

   public void write(ByteBufferWriter var1) {
      var1.putByte((byte)this.hitType.ordinal());
   }

   public String getDescription() {
      String var10000 = INetworkPacket.super.getDescription();
      return var10000 + " (" + this.hitType.name() + ")";
   }

   public String getHitDescription() {
      String var10000 = INetworkPacket.super.getDescription();
      return var10000 + " (" + this.hitType.name() + ")";
   }

   public void tryProcess() {
      if (!GameClient.bClient || !HitCharacterPacket.HitType.VehicleHitZombie.equals(this.hitType) && !HitCharacterPacket.HitType.VehicleHitPlayer.equals(this.hitType)) {
         this.tryProcessInternal();
      } else {
         this.postpone();
      }

   }

   public void tryProcessInternal() {
      if (this.isConsistent()) {
         this.preProcess();
         this.process();
         this.postProcess();
         if (GameClient.bClient) {
            this.attack();
         }

         this.react();
      } else {
         DebugLog.Multiplayer.warn("HitCharacter: check error");
      }

   }

   public abstract boolean isRelevant(UdpConnection var1);

   protected abstract void attack();

   protected abstract void react();

   protected void preProcess() {
   }

   protected void process() {
   }

   protected void postProcess() {
   }

   protected void postpone() {
   }

   public static enum HitType {
      Min,
      PlayerHitSquare,
      PlayerHitVehicle,
      PlayerHitZombie,
      PlayerHitPlayer,
      ZombieHitPlayer,
      VehicleHitZombie,
      VehicleHitPlayer,
      Max;

      // $FF: synthetic method
      private static HitCharacterPacket.HitType[] $values() {
         return new HitCharacterPacket.HitType[]{Min, PlayerHitSquare, PlayerHitVehicle, PlayerHitZombie, PlayerHitPlayer, ZombieHitPlayer, VehicleHitZombie, VehicleHitPlayer, Max};
      }
   }
}
