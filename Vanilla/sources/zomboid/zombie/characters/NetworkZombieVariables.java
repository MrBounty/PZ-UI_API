package zombie.characters;

import zombie.ai.states.ZombieTurnAlerted;
import zombie.core.skinnedmodel.advancedanimation.IAnimatable;
import zombie.network.GameClient;
import zombie.network.GameServer;

public class NetworkZombieVariables {
   public static int getInt(IsoZombie var0, short var1) {
      switch(var1) {
      case 0:
         return (int)(var0.Health * 1000.0F);
      case 1:
         if (var0.target == null) {
            return -1;
         }

         return ((IAnimatable)var0.target).getOnlineID();
      case 2:
         return (int)(var0.speedMod * 1000.0F);
      case 3:
         return (int)var0.TimeSinceSeenFlesh;
      case 4:
         Float var2 = (Float)var0.getStateMachineParams(ZombieTurnAlerted.instance()).get(ZombieTurnAlerted.PARAM_TARGET_ANGLE);
         if (var2 == null) {
            return 0;
         }

         return var2.intValue();
      default:
         return 0;
      }
   }

   public static void setInt(IsoZombie var0, short var1, int var2) {
      switch(var1) {
      case 0:
         var0.Health = (float)var2 / 1000.0F;
         break;
      case 1:
         if (var2 == -1) {
            var0.setTargetSeenTime(0.0F);
            var0.target = null;
         } else {
            IsoPlayer var3 = (IsoPlayer)GameClient.IDToPlayerMap.get((short)var2);
            if (GameServer.bServer) {
               var3 = (IsoPlayer)GameServer.IDToPlayerMap.get((short)var2);
            }

            if (var3 != var0.target) {
               var0.setTargetSeenTime(0.0F);
               var0.target = var3;
            }
         }
         break;
      case 2:
         var0.speedMod = (float)var2 / 1000.0F;
         break;
      case 3:
         var0.TimeSinceSeenFlesh = (float)var2;
         break;
      case 4:
         var0.getStateMachineParams(ZombieTurnAlerted.instance()).put(ZombieTurnAlerted.PARAM_TARGET_ANGLE, (float)var2);
      }

   }

   public static short getBooleanVariables(IsoZombie var0) {
      byte var1 = 0;
      short var2 = (short)(var1 | (var0.isFakeDead() ? 1 : 0));
      var2 = (short)(var2 | (var0.bLunger ? 2 : 0));
      var2 = (short)(var2 | (var0.bRunning ? 4 : 0));
      var2 = (short)(var2 | (var0.isCrawling() ? 8 : 0));
      var2 = (short)(var2 | (var0.isSitAgainstWall() ? 16 : 0));
      var2 = (short)(var2 | (var0.isReanimatedPlayer() ? 32 : 0));
      var2 = (short)(var2 | (var0.isOnFire() ? 64 : 0));
      var2 = (short)(var2 | (var0.isUseless() ? 128 : 0));
      var2 = (short)(var2 | (var0.isOnFloor() ? 256 : 0));
      return var2;
   }

   public static void setBooleanVariables(IsoZombie var0, short var1) {
      var0.setFakeDead((var1 & 1) != 0);
      var0.bLunger = (var1 & 2) != 0;
      var0.bRunning = (var1 & 4) != 0;
      var0.setCrawler((var1 & 8) != 0);
      var0.setSitAgainstWall((var1 & 16) != 0);
      var0.setReanimatedPlayer((var1 & 32) != 0);
      if ((var1 & 64) != 0) {
         var0.SetOnFire();
      } else {
         var0.StopBurning();
      }

      var0.setUseless((var1 & 128) != 0);
      if (var0.isReanimatedPlayer()) {
         var0.setOnFloor((var1 & 256) != 0);
      }

   }

   public static class VariablesInt {
      public static final short health = 0;
      public static final short target = 1;
      public static final short speedMod = 2;
      public static final short timeSinceSeenFlesh = 3;
      public static final short smParamTargetAngle = 4;
      public static final short MAX = 5;
   }
}
