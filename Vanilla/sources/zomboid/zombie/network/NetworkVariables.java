package zombie.network;

public class NetworkVariables {
   public static enum ZombieState {
      Attack("attack"),
      AttackNetwork("attack-network"),
      AttackVehicle("attackvehicle"),
      AttackVehicleNetwork("attackvehicle-network"),
      Bumped("bumped"),
      ClimbFence("climbfence"),
      ClimbWindow("climbwindow"),
      EatBody("eatbody"),
      FaceTarget("face-target"),
      FakeDead("fakedead"),
      FakeDeadAttack("fakedead-attack"),
      FakeDeadAttackNetwork("fakedead-attack-network"),
      FallDown("falldown"),
      Falling("falling"),
      GetDown("getdown"),
      Getup("getup"),
      HitReaction("hitreaction"),
      HitReactionHit("hitreaction-hit"),
      HitWhileStaggered("hitwhilestaggered"),
      Idle("idle"),
      Lunge("lunge"),
      LungeNetwork("lunge-network"),
      OnGround("onground"),
      PathFind("pathfind"),
      Sitting("sitting"),
      StaggerBack("staggerback"),
      Thump("thump"),
      TurnAlerted("turnalerted"),
      WalkToward("walktoward"),
      WalkTowardNetwork("walktoward-network"),
      FakeZombieStay("fakezombie-stay"),
      FakeZombieNormal("fakezombie-normal"),
      FakeZombieAttack("fakezombie-attack");

      private final String zombieState;

      private ZombieState(String var3) {
         this.zombieState = var3;
      }

      public String toString() {
         return this.zombieState;
      }

      public static NetworkVariables.ZombieState fromString(String var0) {
         NetworkVariables.ZombieState[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            NetworkVariables.ZombieState var4 = var1[var3];
            if (var4.zombieState.equalsIgnoreCase(var0)) {
               return var4;
            }
         }

         return Idle;
      }

      public static NetworkVariables.ZombieState fromByte(Byte var0) {
         NetworkVariables.ZombieState[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            NetworkVariables.ZombieState var4 = var1[var3];
            if (var4.ordinal() == var0) {
               return var4;
            }
         }

         return Idle;
      }

      // $FF: synthetic method
      private static NetworkVariables.ZombieState[] $values() {
         return new NetworkVariables.ZombieState[]{Attack, AttackNetwork, AttackVehicle, AttackVehicleNetwork, Bumped, ClimbFence, ClimbWindow, EatBody, FaceTarget, FakeDead, FakeDeadAttack, FakeDeadAttackNetwork, FallDown, Falling, GetDown, Getup, HitReaction, HitReactionHit, HitWhileStaggered, Idle, Lunge, LungeNetwork, OnGround, PathFind, Sitting, StaggerBack, Thump, TurnAlerted, WalkToward, WalkTowardNetwork, FakeZombieStay, FakeZombieNormal, FakeZombieAttack};
      }
   }

   public static enum ThumpType {
      TTNone(""),
      TTDoor("Door"),
      TTClaw("DoorClaw"),
      TTBang("DoorBang");

      private final String thumpType;

      private ThumpType(String var3) {
         this.thumpType = var3;
      }

      public String toString() {
         return this.thumpType;
      }

      public static NetworkVariables.ThumpType fromString(String var0) {
         NetworkVariables.ThumpType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            NetworkVariables.ThumpType var4 = var1[var3];
            if (var4.thumpType.equalsIgnoreCase(var0)) {
               return var4;
            }
         }

         return TTNone;
      }

      public static NetworkVariables.ThumpType fromByte(Byte var0) {
         NetworkVariables.ThumpType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            NetworkVariables.ThumpType var4 = var1[var3];
            if (var4.ordinal() == var0) {
               return var4;
            }
         }

         return TTNone;
      }

      // $FF: synthetic method
      private static NetworkVariables.ThumpType[] $values() {
         return new NetworkVariables.ThumpType[]{TTNone, TTDoor, TTClaw, TTBang};
      }
   }

   public static enum WalkType {
      WT1("1"),
      WT2("2"),
      WT3("3"),
      WT4("4"),
      WT5("5"),
      WTSprint1("sprint1"),
      WTSprint2("sprint2"),
      WTSprint3("sprint3"),
      WTSprint4("sprint4"),
      WTSprint5("sprint5"),
      WTSlow1("slow1"),
      WTSlow2("slow2"),
      WTSlow3("slow3");

      private final String walkType;

      private WalkType(String var3) {
         this.walkType = var3;
      }

      public String toString() {
         return this.walkType;
      }

      public static NetworkVariables.WalkType fromString(String var0) {
         NetworkVariables.WalkType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            NetworkVariables.WalkType var4 = var1[var3];
            if (var4.walkType.equalsIgnoreCase(var0)) {
               return var4;
            }
         }

         return WT1;
      }

      public static NetworkVariables.WalkType fromByte(byte var0) {
         NetworkVariables.WalkType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            NetworkVariables.WalkType var4 = var1[var3];
            if (var4.ordinal() == var0) {
               return var4;
            }
         }

         return WT1;
      }

      // $FF: synthetic method
      private static NetworkVariables.WalkType[] $values() {
         return new NetworkVariables.WalkType[]{WT1, WT2, WT3, WT4, WT5, WTSprint1, WTSprint2, WTSprint3, WTSprint4, WTSprint5, WTSlow1, WTSlow2, WTSlow3};
      }
   }

   public static enum PredictionTypes {
      None,
      Moving,
      Static,
      Thump,
      Climb,
      Lunge,
      LungeHalf,
      Walk,
      WalkHalf,
      PathFind;

      public static NetworkVariables.PredictionTypes fromByte(byte var0) {
         NetworkVariables.PredictionTypes[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            NetworkVariables.PredictionTypes var4 = var1[var3];
            if (var4.ordinal() == var0) {
               return var4;
            }
         }

         return None;
      }

      // $FF: synthetic method
      private static NetworkVariables.PredictionTypes[] $values() {
         return new NetworkVariables.PredictionTypes[]{None, Moving, Static, Thump, Climb, Lunge, LungeHalf, Walk, WalkHalf, PathFind};
      }
   }
}
