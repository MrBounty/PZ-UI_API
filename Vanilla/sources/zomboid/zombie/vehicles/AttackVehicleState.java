package zombie.vehicles;

import fmod.fmod.FMODSoundEmitter;
import org.joml.Vector3f;
import zombie.GameTime;
import zombie.ai.State;
import zombie.ai.states.ZombieIdleState;
import zombie.audio.BaseSoundEmitter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.iso.IsoMovingObject;
import zombie.network.GameServer;

public final class AttackVehicleState extends State {
   private static final AttackVehicleState _instance = new AttackVehicleState();
   private BaseSoundEmitter emitter;
   private final Vector3f worldPos = new Vector3f();

   public static AttackVehicleState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
   }

   public void execute(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      if (var2.target instanceof IsoGameCharacter) {
         IsoGameCharacter var3 = (IsoGameCharacter)var2.target;
         if (var3.isDead()) {
            if (var3.getLeaveBodyTimedown() > 3600.0F) {
               var2.changeState(ZombieIdleState.instance());
               var2.setTarget((IsoMovingObject)null);
            } else {
               var3.setLeaveBodyTimedown(var3.getLeaveBodyTimedown() + GameTime.getInstance().getMultiplier() / 1.6F);
               if (!GameServer.bServer && !Core.SoundDisabled && Rand.Next(Rand.AdjustForFramerate(15)) == 0) {
                  if (this.emitter == null) {
                     this.emitter = new FMODSoundEmitter();
                  }

                  String var6 = var2.isFemale() ? "FemaleZombieEating" : "MaleZombieEating";
                  if (!this.emitter.isPlaying(var6)) {
                     this.emitter.playSound(var6);
                  }
               }
            }

            var2.TimeSinceSeenFlesh = 0.0F;
         } else {
            BaseVehicle var4 = var3.getVehicle();
            if (var4 != null && var4.isCharacterAdjacentTo(var1)) {
               Vector3f var5 = var4.chooseBestAttackPosition(var3, var1, this.worldPos);
               if (var5 == null) {
                  if (var2.AllowRepathDelay <= 0.0F) {
                     var1.pathToCharacter(var3);
                     var2.AllowRepathDelay = 6.25F;
                  }

               } else if (var5 != null && (Math.abs(var5.x - var1.x) > 0.1F || Math.abs(var5.y - var1.y) > 0.1F)) {
                  if (!(Math.abs(var4.getCurrentSpeedKmHour()) > 0.1F) || !var4.isCharacterAdjacentTo(var1) && !(var4.DistToSquared(var1) < 16.0F)) {
                     if (var2.AllowRepathDelay <= 0.0F) {
                        var1.pathToCharacter(var3);
                        var2.AllowRepathDelay = 6.25F;
                     }

                  }
               } else {
                  var1.faceThisObject(var3);
               }
            }
         }
      }
   }

   public void exit(IsoGameCharacter var1) {
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      IsoZombie var3 = (IsoZombie)var1;
      if (var3.target instanceof IsoGameCharacter) {
         IsoGameCharacter var4 = (IsoGameCharacter)var3.target;
         BaseVehicle var5 = var4.getVehicle();
         if (var5 != null) {
            if (!var4.isDead()) {
               if (var2.m_EventName.equalsIgnoreCase("AttackCollisionCheck")) {
                  var4.getBodyDamage().AddRandomDamageFromZombie(var3, (String)null);
                  var4.getBodyDamage().Update();
                  if (var4.isDead()) {
                     if (var4.isFemale()) {
                        var3.getEmitter().playVocals("FemaleBeingEatenDeath");
                     } else {
                        var3.getEmitter().playVocals("MaleBeingEatenDeath");
                     }

                     var4.setHealth(0.0F);
                  } else if (var4.isAsleep()) {
                     if (GameServer.bServer) {
                        var4.sendObjectChange("wakeUp");
                        var4.setAsleep(false);
                     } else {
                        var4.forceAwake();
                     }
                  }
               } else if (var2.m_EventName.equalsIgnoreCase("ThumpFrame")) {
                  VehicleWindow var6 = null;
                  VehiclePart var7 = null;
                  int var8 = var5.getSeat(var4);
                  String var9 = var5.getPassengerArea(var8);
                  if (var5.isInArea(var9, var1)) {
                     VehiclePart var10 = var5.getPassengerDoor(var8);
                     if (var10 != null && var10.getDoor() != null && var10.getInventoryItem() != null && !var10.getDoor().isOpen()) {
                        var6 = var10.findWindow();
                        if (var6 != null && !var6.isHittable()) {
                           var6 = null;
                        }

                        if (var6 == null) {
                           var7 = var10;
                        }
                     }
                  } else {
                     var7 = var5.getNearestBodyworkPart(var1);
                     if (var7 != null) {
                        var6 = var7.getWindow();
                        if (var6 == null) {
                           var6 = var7.findWindow();
                        }

                        if (var6 != null && !var6.isHittable()) {
                           var6 = null;
                        }

                        if (var6 != null) {
                           var7 = null;
                        }
                     }
                  }

                  if (var6 != null) {
                     var6.damage(var3.strength);
                     var5.setBloodIntensity(var6.part.getId(), var5.getBloodIntensity(var6.part.getId()) + 0.025F);
                     if (!GameServer.bServer) {
                        var3.setVehicleHitLocation(var5);
                        var1.getEmitter().playSound("ZombieThumpVehicleWindow", var5);
                     }

                     var3.setThumpFlag(3);
                  } else {
                     if (!GameServer.bServer) {
                        var3.setVehicleHitLocation(var5);
                        var1.getEmitter().playSound("ZombieThumpVehicle", var5);
                     }

                     var3.setThumpFlag(1);
                  }

                  var5.setAddThumpWorldSound(true);
                  if (var7 != null && var7.getWindow() == null) {
                     var7.setCondition(var7.getCondition() - var3.strength);
                  }

                  if (var4.isAsleep()) {
                     if (GameServer.bServer) {
                        var4.sendObjectChange("wakeUp");
                        var4.setAsleep(false);
                     } else {
                        var4.forceAwake();
                     }
                  }
               }

            }
         }
      }
   }

   public boolean isAttacking(IsoGameCharacter var1) {
      return true;
   }

   public boolean isPassengerExposed(IsoGameCharacter var1) {
      if (!(var1 instanceof IsoZombie)) {
         return false;
      } else {
         IsoZombie var2 = (IsoZombie)var1;
         if (!(var2.target instanceof IsoGameCharacter)) {
            return false;
         } else {
            IsoGameCharacter var3 = (IsoGameCharacter)var2.target;
            BaseVehicle var4 = var3.getVehicle();
            if (var4 == null) {
               return false;
            } else {
               boolean var5 = false;
               VehicleWindow var6 = null;
               int var7 = var4.getSeat(var3);
               String var8 = var4.getPassengerArea(var7);
               VehiclePart var9 = null;
               if (var4.isInArea(var8, var1)) {
                  var9 = var4.getPassengerDoor(var7);
                  if (var9 != null && var9.getDoor() != null) {
                     if (var9.getInventoryItem() != null && !var9.getDoor().isOpen()) {
                        var6 = var9.findWindow();
                        if (var6 != null) {
                           if (!var6.isHittable()) {
                              var6 = null;
                           }

                           var5 = var6 == null;
                        } else {
                           var5 = false;
                        }
                     } else {
                        var5 = true;
                     }
                  }
               } else {
                  var9 = var4.getNearestBodyworkPart(var1);
                  if (var9 != null) {
                     var6 = var9.findWindow();
                     if (var6 != null && !var6.isHittable()) {
                        var6 = null;
                     }
                  }
               }

               return var5;
            }
         }
      }
   }
}
