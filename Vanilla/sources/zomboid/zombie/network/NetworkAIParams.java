package zombie.network;

import zombie.debug.DebugOptions;

public class NetworkAIParams {
   public static final int ZOMBIE_UPDATE_INFO_BUNCH_RATE_MS = 200;
   public static final int CHARACTER_UPDATE_RATE_MS = 200;
   public static final int CHARACTER_EXTRAPOLATION_UPDATE_INTERVAL_MS = 500;
   public static final float ZOMBIE_ANTICIPATORY_UPDATE_MULTIPLIER = 0.6F;
   public static final int ZOMBIE_REMOVE_INTERVAL_MS = 4000;
   public static final int ZOMBIE_MAX_UPDATE_INTERVAL_MS = 3800;
   public static final int ZOMBIE_MIN_UPDATE_INTERVAL_MS = 200;
   public static final int CHARACTER_PREDICTION_INTERVAL_MS = 2000;
   public static final int ZOMBIE_TELEPORT_PLAYER = 2;
   public static final int ZOMBIE_TELEPORT_DISTANCE_SQ = 9;
   public static final int VEHICLE_SPEED_CAP = 10;

   public static void Init() {
      if (GameClient.bClient) {
         DebugOptions.instance.MultiplayerPing.setValue(true);
         MPStatistics.Reset();
      }

   }
}
