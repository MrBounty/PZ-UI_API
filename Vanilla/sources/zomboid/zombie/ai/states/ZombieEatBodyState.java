package zombie.ai.states;

import java.util.HashMap;
import zombie.ai.State;
import zombie.audio.parameters.ParameterZombieState;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.iso.IsoMovingObject;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoZombieGiblets;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class ZombieEatBodyState extends State {
   private static final ZombieEatBodyState _instance = new ZombieEatBodyState();

   public static ZombieEatBodyState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      var2.setStateEventDelayTimer(Rand.Next(1800.0F, 3600.0F));
      var2.setVariable("onknees", Rand.Next(3) != 0);
      if (var2.getEatBodyTarget() instanceof IsoDeadBody) {
         IsoDeadBody var3 = (IsoDeadBody)var2.eatBodyTarget;
         if (!var2.isEatingOther(var3)) {
            HashMap var4 = var1.getStateMachineParams(this);
            var4.put(0, var3);
            var3.getEatingZombies().add(var2);
         }

         if (GameClient.bClient && var2.isLocal()) {
            GameClient.sendEatBody(var2, var2.getEatBodyTarget());
         }
      } else if (var2.getEatBodyTarget() instanceof IsoPlayer && GameClient.bClient && var2.isLocal()) {
         GameClient.sendEatBody(var2, var2.getEatBodyTarget());
      }

   }

   public void execute(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      IsoMovingObject var3 = var2.getEatBodyTarget();
      if (var2.getStateEventDelayTimer() <= 0.0F) {
         var2.setEatBodyTarget((IsoMovingObject)null, false);
      } else if (!GameServer.bServer && !Core.SoundDisabled && Rand.Next(Rand.AdjustForFramerate(15)) == 0) {
         var2.parameterZombieState.setState(ParameterZombieState.State.Eating);
      }

      var2.TimeSinceSeenFlesh = 0.0F;
      if (var3 != null) {
         var2.faceThisObject(var3);
      }

      if (Rand.Next(Rand.AdjustForFramerate(450)) == 0) {
         var2.getCurrentSquare().getChunk().addBloodSplat(var2.x + Rand.Next(-0.5F, 0.5F), var2.y + Rand.Next(-0.5F, 0.5F), var2.z, Rand.Next(8));
         if (Rand.Next(6) == 0) {
            new IsoZombieGiblets(IsoZombieGiblets.GibletType.B, var2.getCell(), var2.getX(), var2.getY(), var2.getZ() + 0.3F, Rand.Next(-0.2F, 0.2F) * 1.5F, Rand.Next(-0.2F, 0.2F) * 1.5F);
         } else {
            new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var2.getCell(), var2.getX(), var2.getY(), var2.getZ() + 0.3F, Rand.Next(-0.2F, 0.2F) * 1.5F, Rand.Next(-0.2F, 0.2F) * 1.5F);
         }

         if (Rand.Next(4) == 0) {
            var2.addBlood((BloodBodyPartType)null, true, false, false);
         }
      }

   }

   public void exit(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      HashMap var3 = var1.getStateMachineParams(this);
      if (var3.get(0) instanceof IsoDeadBody) {
         ((IsoDeadBody)var3.get(0)).getEatingZombies().remove(var2);
      }

      if (var2.parameterZombieState.isState(ParameterZombieState.State.Eating)) {
         var2.parameterZombieState.setState(ParameterZombieState.State.Idle);
      }

      if (GameClient.bClient && var2.isLocal()) {
         GameClient.sendEatBody(var2, (IsoMovingObject)null);
      }

   }
}
