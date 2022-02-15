package zombie.iso.objects;

import fmod.fmod.FMODSoundEmitter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;
import zombie.WorldSoundManager;
import zombie.audio.BaseSoundEmitter;
import zombie.audio.DummySoundEmitter;
import zombie.audio.parameters.ParameterFireSize;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.network.ByteBufferWriter;
import zombie.core.textures.ColorInfo;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.util.list.PZArrayUtil;

public class IsoFireManager {
   public static double Red_Oscilator = 0.0D;
   public static double Green_Oscilator = 0.0D;
   public static double Blue_Oscilator = 0.0D;
   public static double Red_Oscilator_Rate = 0.10000000149011612D;
   public static double Green_Oscilator_Rate = 0.12999999523162842D;
   public static double Blue_Oscilator_Rate = 0.08760000020265579D;
   public static double Red_Oscilator_Val = 0.0D;
   public static double Green_Oscilator_Val = 0.0D;
   public static double Blue_Oscilator_Val = 0.0D;
   public static double OscilatorSpeedScalar = 15.600000381469727D;
   public static double OscilatorEffectScalar = 0.0038999998942017555D;
   public static int MaxFireObjects = 75;
   public static int FireRecalcDelay = 25;
   public static int FireRecalc;
   public static boolean LightCalcFromBurningCharacters;
   public static float FireAlpha;
   public static float SmokeAlpha;
   public static float FireAnimDelay;
   public static float SmokeAnimDelay;
   public static ColorInfo FireTintMod;
   public static ColorInfo SmokeTintMod;
   public static final ArrayList FireStack;
   public static final ArrayList CharactersOnFire_Stack;
   private static final IsoFireManager.FireSounds fireSounds;
   private static Stack updateStack;

   public static void Add(IsoFire var0) {
      if (FireStack.contains(var0)) {
         System.out.println("IsoFireManager.Add already added fire, ignoring");
      } else {
         if (FireStack.size() < MaxFireObjects) {
            FireStack.add(var0);
         } else {
            IsoFire var1 = null;
            int var2 = 0;

            for(int var3 = 0; var3 < FireStack.size(); ++var3) {
               if (((IsoFire)FireStack.get(var3)).Age > var2) {
                  var2 = ((IsoFire)FireStack.get(var3)).Age;
                  var1 = (IsoFire)FireStack.get(var3);
               }
            }

            if (var1 != null && var1.square != null) {
               var1.square.getProperties().UnSet(IsoFlagType.burning);
               var1.square.getProperties().UnSet(IsoFlagType.smoke);
               var1.RemoveAttachedAnims();
               var1.removeFromWorld();
               var1.removeFromSquare();
            }

            FireStack.add(var0);
         }

      }
   }

   public static void AddBurningCharacter(IsoGameCharacter var0) {
      for(int var1 = 0; var1 < CharactersOnFire_Stack.size(); ++var1) {
         if (CharactersOnFire_Stack.get(var1) == var0) {
            return;
         }
      }

      CharactersOnFire_Stack.add(var0);
   }

   public static void Fire_LightCalc(IsoGridSquare var0, IsoGridSquare var1, int var2) {
      if (var1 != null && var0 != null) {
         byte var3 = 0;
         byte var4 = 8;
         int var10 = var3 + Math.abs(var1.getX() - var0.getX());
         var10 += Math.abs(var1.getY() - var0.getY());
         var10 += Math.abs(var1.getZ() - var0.getZ());
         if (var10 <= var4) {
            float var5 = 0.199F / (float)var4 * (float)(var4 - var10);
            float var7 = var5 * 0.6F;
            float var8 = var5 * 0.4F;
            if (var1.getLightInfluenceR() == null) {
               var1.setLightInfluenceR(new ArrayList());
            }

            var1.getLightInfluenceR().add(var5);
            if (var1.getLightInfluenceG() == null) {
               var1.setLightInfluenceG(new ArrayList());
            }

            var1.getLightInfluenceG().add(var7);
            if (var1.getLightInfluenceB() == null) {
               var1.setLightInfluenceB(new ArrayList());
            }

            var1.getLightInfluenceB().add(var8);
            ColorInfo var9 = var1.lighting[var2].lightInfo();
            var9.r += var5;
            var9.g += var7;
            var9.b += var8;
            if (var9.r > 1.0F) {
               var9.r = 1.0F;
            }

            if (var9.g > 1.0F) {
               var9.g = 1.0F;
            }

            if (var9.b > 1.0F) {
               var9.b = 1.0F;
            }
         }

      }
   }

   public static void LightTileWithFire(IsoGridSquare var0) {
   }

   public static void explode(IsoCell var0, IsoGridSquare var1, int var2) {
      if (var1 != null) {
         IsoGridSquare var3 = null;
         Object var4 = null;
         FireRecalc = 1;

         for(int var5 = -2; var5 <= 2; ++var5) {
            for(int var6 = -2; var6 <= 2; ++var6) {
               for(int var7 = 0; var7 <= 1; ++var7) {
                  var3 = var0.getGridSquare(var1.getX() + var5, var1.getY() + var6, var1.getZ() + var7);
                  if (var3 != null && Rand.Next(100) < var2 && IsoFire.CanAddFire(var3, true)) {
                     StartFire(var0, var3, true, Rand.Next(100, 250 + var2));
                     var3.BurnWalls(true);
                  }
               }
            }
         }

      }
   }

   public static void MolotovSmash(IsoCell var0, IsoGridSquare var1) {
      if (var1 != null) {
         IsoGridSquare var2 = null;
         Object var3 = null;
         FireRecalc = 1;
         var2 = var0.getGridSquare(var1.getX(), var1.getY() - 1, var1.getZ());
         StartFire(var0, var2, true, 50);
         var2 = var0.getGridSquare(var1.getX() + 1, var1.getY() - 1, var1.getZ());
         StartFire(var0, var2, true, 50);
         var2 = var0.getGridSquare(var1.getX() + 1, var1.getY(), var1.getZ());
         StartFire(var0, var2, true, 50);
         var2 = var0.getGridSquare(var1.getX() + 1, var1.getY() + 1, var1.getZ());
         StartFire(var0, var2, true, 50);
         var2 = var0.getGridSquare(var1.getX(), var1.getY() + 1, var1.getZ());
         StartFire(var0, var2, true, 50);
         var2 = var0.getGridSquare(var1.getX() - 1, var1.getY() + 1, var1.getZ());
         StartFire(var0, var2, true, 50);
         var2 = var0.getGridSquare(var1.getX() - 1, var1.getY(), var1.getZ());
         StartFire(var0, var2, true, 50);
         var2 = var0.getGridSquare(var1.getX() - 1, var1.getY() - 1, var1.getZ());
         StartFire(var0, var2, true, 50);
         var2 = var0.getGridSquare(var1.getX(), var1.getY(), var1.getZ());
         StartFire(var0, var2, true, 50);
      }
   }

   public static void Remove(IsoFire var0) {
      if (!FireStack.contains(var0)) {
         System.out.println("IsoFireManager.Remove unknown fire, ignoring");
      } else {
         FireStack.remove(var0);
      }
   }

   public static void RemoveBurningCharacter(IsoGameCharacter var0) {
      CharactersOnFire_Stack.remove(var0);
   }

   public static void StartFire(IsoCell var0, IsoGridSquare var1, boolean var2, int var3, int var4) {
      if (var1.getFloor() != null && var1.getFloor().getSprite() != null) {
         var3 -= var1.getFloor().getSprite().firerequirement;
      }

      if (var3 < 5) {
         var3 = 5;
      }

      if (IsoFire.CanAddFire(var1, var2)) {
         if (GameClient.bClient) {
            ByteBufferWriter var6 = GameClient.connection.startPacket();
            PacketTypes.PacketType.StartFire.doPacket(var6);
            var6.putInt(var1.getX());
            var6.putInt(var1.getY());
            var6.putInt(var1.getZ());
            var6.putInt(var3);
            var6.putBoolean(var2);
            var6.putInt(var4);
            var6.putBoolean(false);
            PacketTypes.PacketType.StartFire.send(GameClient.connection);
         } else if (GameServer.bServer) {
            GameServer.startFireOnClient(var1, var3, var2, var4, false);
         } else {
            IsoFire var5 = new IsoFire(var0, var1, var2, var3, var4);
            Add(var5);
            var1.getObjects().add(var5);
            if (Rand.Next(5) == 0) {
               WorldSoundManager.instance.addSound(var5, var1.getX(), var1.getY(), var1.getZ(), 20, 20);
            }

         }
      }
   }

   public static void StartSmoke(IsoCell var0, IsoGridSquare var1, boolean var2, int var3, int var4) {
      if (IsoFire.CanAddSmoke(var1, var2)) {
         if (GameClient.bClient) {
            ByteBufferWriter var6 = GameClient.connection.startPacket();
            PacketTypes.PacketType.StartFire.doPacket(var6);
            var6.putInt(var1.getX());
            var6.putInt(var1.getY());
            var6.putInt(var1.getZ());
            var6.putInt(var3);
            var6.putBoolean(var2);
            var6.putInt(var4);
            var6.putBoolean(true);
            PacketTypes.PacketType.StartFire.send(GameClient.connection);
         } else if (GameServer.bServer) {
            GameServer.startFireOnClient(var1, var3, var2, var4, true);
         } else {
            IsoFire var5 = new IsoFire(var0, var1, var2, var3, var4, true);
            Add(var5);
            var1.getObjects().add(var5);
         }
      }
   }

   public static void StartFire(IsoCell var0, IsoGridSquare var1, boolean var2, int var3) {
      StartFire(var0, var1, var2, var3, 0);
   }

   public static void Update() {
      Red_Oscilator_Val = Math.sin(Red_Oscilator += Blue_Oscilator_Rate * OscilatorSpeedScalar);
      Green_Oscilator_Val = Math.sin(Green_Oscilator += Blue_Oscilator_Rate * OscilatorSpeedScalar);
      Blue_Oscilator_Val = Math.sin(Blue_Oscilator += Blue_Oscilator_Rate * OscilatorSpeedScalar);
      Red_Oscilator_Val = (Red_Oscilator_Val + 1.0D) / 2.0D;
      Green_Oscilator_Val = (Green_Oscilator_Val + 1.0D) / 2.0D;
      Blue_Oscilator_Val = (Blue_Oscilator_Val + 1.0D) / 2.0D;
      Red_Oscilator_Val *= OscilatorEffectScalar;
      Green_Oscilator_Val *= OscilatorEffectScalar;
      Blue_Oscilator_Val *= OscilatorEffectScalar;
      updateStack.clear();
      updateStack.addAll(FireStack);

      for(int var0 = 0; var0 < updateStack.size(); ++var0) {
         IsoFire var1 = (IsoFire)updateStack.get(var0);
         if (var1.getObjectIndex() != -1 && FireStack.contains(var1)) {
            var1.update();
         }
      }

      --FireRecalc;
      if (FireRecalc < 0) {
         FireRecalc = FireRecalcDelay;
      }

      fireSounds.update();
   }

   public static void updateSound(IsoFire var0) {
      fireSounds.addFire(var0);
   }

   public static void stopSound(IsoFire var0) {
      fireSounds.removeFire(var0);
   }

   public static void RemoveAllOn(IsoGridSquare var0) {
      for(int var1 = FireStack.size() - 1; var1 >= 0; --var1) {
         IsoFire var2 = (IsoFire)FireStack.get(var1);
         if (var2.square == var0) {
            var2.extinctFire();
         }
      }

   }

   public static void Reset() {
      FireStack.clear();
      CharactersOnFire_Stack.clear();
      fireSounds.Reset();
   }

   static {
      FireRecalc = FireRecalcDelay;
      LightCalcFromBurningCharacters = false;
      FireAlpha = 1.0F;
      SmokeAlpha = 0.3F;
      FireAnimDelay = 0.2F;
      SmokeAnimDelay = 0.2F;
      FireTintMod = new ColorInfo(1.0F, 1.0F, 1.0F, 1.0F);
      SmokeTintMod = new ColorInfo(0.5F, 0.5F, 0.5F, 1.0F);
      FireStack = new ArrayList();
      CharactersOnFire_Stack = new ArrayList();
      fireSounds = new IsoFireManager.FireSounds(20);
      updateStack = new Stack();
   }

   private static final class FireSounds {
      final ArrayList fires = new ArrayList();
      final IsoFireManager.FireSounds.Slot[] slots;
      final Comparator comp = new Comparator() {
         public int compare(IsoFire var1, IsoFire var2) {
            float var3 = FireSounds.this.getClosestListener((float)var1.square.x + 0.5F, (float)var1.square.y + 0.5F, (float)var1.square.z);
            float var4 = FireSounds.this.getClosestListener((float)var2.square.x + 0.5F, (float)var2.square.y + 0.5F, (float)var2.square.z);
            if (var3 > var4) {
               return 1;
            } else {
               return var3 < var4 ? -1 : 0;
            }
         }
      };

      FireSounds(int var1) {
         this.slots = (IsoFireManager.FireSounds.Slot[])PZArrayUtil.newInstance(IsoFireManager.FireSounds.Slot.class, var1, IsoFireManager.FireSounds.Slot::new);
      }

      void addFire(IsoFire var1) {
         if (!this.fires.contains(var1)) {
            this.fires.add(var1);
         }

      }

      void removeFire(IsoFire var1) {
         this.fires.remove(var1);
      }

      void update() {
         if (!GameServer.bServer) {
            int var1;
            for(var1 = 0; var1 < this.slots.length; ++var1) {
               this.slots[var1].playing = false;
            }

            if (this.fires.isEmpty()) {
               this.stopNotPlaying();
            } else {
               Collections.sort(this.fires, this.comp);
               var1 = Math.min(this.fires.size(), this.slots.length);

               int var2;
               IsoFire var3;
               int var4;
               for(var2 = 0; var2 < var1; ++var2) {
                  var3 = (IsoFire)this.fires.get(var2);
                  if (this.shouldPlay(var3)) {
                     var4 = this.getExistingSlot(var3);
                     if (var4 != -1) {
                        this.slots[var4].playSound(var3);
                     }
                  }
               }

               for(var2 = 0; var2 < var1; ++var2) {
                  var3 = (IsoFire)this.fires.get(var2);
                  if (this.shouldPlay(var3)) {
                     var4 = this.getExistingSlot(var3);
                     if (var4 == -1) {
                        var4 = this.getFreeSlot();
                        this.slots[var4].playSound(var3);
                     }
                  }
               }

               this.stopNotPlaying();
               this.fires.clear();
            }
         }
      }

      float getClosestListener(float var1, float var2, float var3) {
         float var4 = Float.MAX_VALUE;

         for(int var5 = 0; var5 < IsoPlayer.numPlayers; ++var5) {
            IsoPlayer var6 = IsoPlayer.players[var5];
            if (var6 != null && var6.getCurrentSquare() != null) {
               float var7 = var6.getX();
               float var8 = var6.getY();
               float var9 = var6.getZ();
               float var10 = IsoUtils.DistanceToSquared(var7, var8, var9 * 3.0F, var1, var2, var3 * 3.0F);
               if (var6.Traits.HardOfHearing.isSet()) {
                  var10 *= 4.5F;
               }

               if (var10 < var4) {
                  var4 = var10;
               }
            }
         }

         return var4;
      }

      boolean shouldPlay(IsoFire var1) {
         return var1 != null && var1.getObjectIndex() != -1 && var1.LifeStage < 4;
      }

      int getExistingSlot(IsoFire var1) {
         for(int var2 = 0; var2 < this.slots.length; ++var2) {
            if (this.slots[var2].fire == var1) {
               return var2;
            }
         }

         return -1;
      }

      int getFreeSlot() {
         for(int var1 = 0; var1 < this.slots.length; ++var1) {
            if (!this.slots[var1].playing) {
               return var1;
            }
         }

         return -1;
      }

      void stopNotPlaying() {
         for(int var1 = 0; var1 < this.slots.length; ++var1) {
            IsoFireManager.FireSounds.Slot var2 = this.slots[var1];
            if (!var2.playing) {
               var2.stopPlaying();
               var2.fire = null;
            }
         }

      }

      void Reset() {
         for(int var1 = 0; var1 < this.slots.length; ++var1) {
            this.slots[var1].stopPlaying();
            this.slots[var1].fire = null;
            this.slots[var1].playing = false;
         }

      }

      static final class Slot {
         IsoFire fire;
         BaseSoundEmitter emitter;
         final ParameterFireSize parameterFireSize = new ParameterFireSize();
         long instance = 0L;
         boolean playing;

         void playSound(IsoFire var1) {
            if (this.emitter == null) {
               this.emitter = (BaseSoundEmitter)(Core.SoundDisabled ? new DummySoundEmitter() : new FMODSoundEmitter());
               if (!Core.SoundDisabled) {
                  ((FMODSoundEmitter)this.emitter).addParameter(this.parameterFireSize);
               }
            }

            this.emitter.setPos((float)var1.square.x + 0.5F, (float)var1.square.y + 0.5F, (float)var1.square.z);
            byte var10000;
            switch(var1.LifeStage) {
            case 1:
            case 3:
               var10000 = 1;
               break;
            case 2:
               var10000 = 2;
               break;
            default:
               var10000 = 0;
            }

            byte var2 = var10000;
            this.parameterFireSize.setSize(var2);
            if (!this.emitter.isPlaying("Fire")) {
               this.instance = this.emitter.playSoundImpl("Fire", (IsoObject)null);
            }

            this.fire = var1;
            this.playing = true;
            this.emitter.tick();
         }

         void stopPlaying() {
            if (this.emitter != null && this.instance != 0L) {
               if (this.emitter.hasSustainPoints(this.instance)) {
                  this.emitter.triggerCue(this.instance);
                  this.instance = 0L;
               } else {
                  this.emitter.stopAll();
                  this.instance = 0L;
               }
            }
         }
      }
   }
}
