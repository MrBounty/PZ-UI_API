package zombie.characters;

import fmod.fmod.EmitterType;
import fmod.fmod.FMODManager;
import fmod.fmod.FMODSoundBank;
import fmod.fmod.FMODSoundEmitter;
import fmod.fmod.FMODVoice;
import fmod.fmod.FMOD_STUDIO_PARAMETER_DESCRIPTION;
import zombie.SoundManager;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.interfaces.ICommonSoundEmitter;
import zombie.iso.IsoObject;
import zombie.network.GameServer;

public final class CharacterSoundEmitter extends BaseCharacterSoundEmitter implements ICommonSoundEmitter {
   float currentPriority;
   final FMODSoundEmitter vocals = new FMODSoundEmitter();
   final FMODSoundEmitter footsteps = new FMODSoundEmitter();
   final FMODSoundEmitter extra = new FMODSoundEmitter();
   private long footstep1 = 0L;
   private long footstep2 = 0L;

   public CharacterSoundEmitter(IsoGameCharacter var1) {
      super(var1);
      this.vocals.emitterType = EmitterType.Voice;
      this.vocals.parent = this.character;
      this.vocals.parameterUpdater = var1;
      this.footsteps.emitterType = EmitterType.Footstep;
      this.footsteps.parent = this.character;
      this.footsteps.parameterUpdater = var1;
      this.extra.emitterType = EmitterType.Extra;
      this.extra.parent = this.character;
      this.extra.parameterUpdater = var1;
   }

   public void register() {
      SoundManager.instance.registerEmitter(this.vocals);
      SoundManager.instance.registerEmitter(this.footsteps);
      SoundManager.instance.registerEmitter(this.extra);
   }

   public void unregister() {
      SoundManager.instance.unregisterEmitter(this.vocals);
      SoundManager.instance.unregisterEmitter(this.footsteps);
      SoundManager.instance.unregisterEmitter(this.extra);
   }

   public long playVocals(String var1) {
      if (GameServer.bServer) {
         return 0L;
      } else {
         FMODVoice var2 = FMODSoundBank.instance.getVoice(var1);
         if (var2 == null) {
            long var6 = this.vocals.playSoundImpl(var1, false, (IsoObject)null);
            return var6;
         } else {
            float var3 = var2.priority;
            long var4 = this.vocals.playSound(var2.sound, (IsoObject)this.character);
            this.currentPriority = var3;
            return var4;
         }
      }
   }

   CharacterSoundEmitter.footstep getFootstepToPlay() {
      if (FMODManager.instance.getNumListeners() == 1) {
         for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
            IsoPlayer var2 = IsoPlayer.players[var1];
            if (var2 != null && var2 != this.character && !var2.Traits.Deaf.isSet()) {
               if ((int)var2.getZ() < (int)this.character.getZ()) {
                  return CharacterSoundEmitter.footstep.upstairs;
               }
               break;
            }
         }
      }

      IsoObject var4 = this.character.getCurrentSquare().getFloor();
      if (var4 != null && var4.getSprite() != null && var4.getSprite().getName() != null) {
         String var5 = var4.getSprite().getName();
         if (!var5.endsWith("blends_natural_01_5") && !var5.endsWith("blends_natural_01_6") && !var5.endsWith("blends_natural_01_7") && !var5.endsWith("blends_natural_01_0")) {
            if (!var5.endsWith("blends_street_01_48") && !var5.endsWith("blends_street_01_53") && !var5.endsWith("blends_street_01_54") && !var5.endsWith("blends_street_01_55")) {
               if (var5.startsWith("blends_natural_01")) {
                  return CharacterSoundEmitter.footstep.grass;
               } else if (var5.startsWith("floors_interior_tilesandwood_01_")) {
                  int var3 = Integer.parseInt(var5.replaceFirst("floors_interior_tilesandwood_01_", ""));
                  return var3 > 40 && var3 < 48 ? CharacterSoundEmitter.footstep.wood : CharacterSoundEmitter.footstep.concrete;
               } else if (var5.startsWith("carpentry_02_")) {
                  return CharacterSoundEmitter.footstep.wood;
               } else {
                  return var5.startsWith("floors_interior_carpet_") ? CharacterSoundEmitter.footstep.wood : CharacterSoundEmitter.footstep.concrete;
               }
            } else {
               return CharacterSoundEmitter.footstep.gravel;
            }
         } else {
            return CharacterSoundEmitter.footstep.gravel;
         }
      } else {
         return CharacterSoundEmitter.footstep.concrete;
      }
   }

   public void playFootsteps(String var1, float var2) {
      if (!GameServer.bServer) {
         boolean var3 = this.footsteps.isPlaying(this.footstep1);
         boolean var4 = this.footsteps.isPlaying(this.footstep2);
         long var5;
         if (var3 && var4) {
            var5 = this.footstep1;
            this.footstep1 = this.footstep2;
            this.footstep2 = var5;
            if (this.footsteps.restart(this.footstep2)) {
               return;
            }

            this.footsteps.stopSound(this.footstep2);
            this.footstep2 = 0L;
         } else if (var4) {
            this.footstep1 = this.footstep2;
            this.footstep2 = 0L;
            var3 = true;
            var4 = false;
         }

         var5 = this.footsteps.playSoundImpl(var1, false, (IsoObject)null);
         if (!var3) {
            this.footstep1 = var5;
         } else {
            this.footstep2 = var5;
         }

      }
   }

   public long playSound(String var1) {
      if (DebugLog.isEnabled(DebugType.Sound)) {
         DebugLog.Sound.debugln("Playing sound: " + var1 + (this.character.isZombie() ? " for zombie" : " for player"));
      }

      return this.character.isInvisible() && !DebugOptions.instance.Character.Debug.PlaySoundWhenInvisible.getValue() ? 0L : this.extra.playSound(var1);
   }

   public long playSound(String var1, boolean var2) {
      if (DebugLog.isEnabled(DebugType.Sound)) {
         DebugLog.Sound.debugln("Playing sound: " + var1 + (this.character.isZombie() ? " for zombie" : " for player"));
      }

      return this.extra.playSound(var1, var2);
   }

   public long playSound(String var1, IsoObject var2) {
      if (DebugLog.isEnabled(DebugType.Sound)) {
         DebugLog.Sound.debugln("Playing sound: " + var1 + (this.character.isZombie() ? " for zombie" : " for player"));
      }

      return GameServer.bServer ? 0L : this.extra.playSound(var1, var2);
   }

   public long playSoundImpl(String var1, IsoObject var2) {
      if (DebugLog.isEnabled(DebugType.Sound)) {
         DebugLog.Sound.debugln("Playing sound: " + var1 + (this.character.isZombie() ? " for zombie" : " for player"));
      }

      return this.extra.playSoundImpl(var1, false, var2);
   }

   public void tick() {
      this.vocals.tick();
      this.footsteps.tick();
      this.extra.tick();
   }

   public void setPos(float var1, float var2, float var3) {
      this.set(var1, var2, var3);
   }

   public void set(float var1, float var2, float var3) {
      this.vocals.x = this.footsteps.x = this.extra.x = var1;
      this.vocals.y = this.footsteps.y = this.extra.y = var2;
      this.vocals.z = this.footsteps.z = this.extra.z = var3;
   }

   public boolean isEmpty() {
      return this.isClear();
   }

   public boolean isClear() {
      return this.vocals.isEmpty() && this.footsteps.isEmpty() && this.extra.isEmpty();
   }

   public void setPitch(long var1, float var3) {
      this.extra.setPitch(var1, var3);
      this.footsteps.setPitch(var1, var3);
      this.vocals.setPitch(var1, var3);
   }

   public void setVolume(long var1, float var3) {
      this.extra.setVolume(var1, var3);
      this.footsteps.setVolume(var1, var3);
      this.vocals.setVolume(var1, var3);
   }

   public boolean hasSustainPoints(long var1) {
      if (this.extra.isPlaying(var1)) {
         return this.extra.hasSustainPoints(var1);
      } else if (this.footsteps.isPlaying(var1)) {
         return this.footsteps.hasSustainPoints(var1);
      } else {
         return this.vocals.isPlaying(var1) ? this.vocals.hasSustainPoints(var1) : false;
      }
   }

   public void triggerCue(long var1) {
      if (this.extra.isPlaying(var1)) {
         this.extra.triggerCue(var1);
      } else if (this.footsteps.isPlaying(var1)) {
         this.footsteps.triggerCue(var1);
      } else if (this.vocals.isPlaying(var1)) {
         this.vocals.triggerCue(var1);
      }

   }

   public int stopSound(long var1) {
      this.extra.stopSound(var1);
      this.footsteps.stopSound(var1);
      this.vocals.stopSound(var1);
      return 0;
   }

   public void stopOrTriggerSound(long var1) {
      this.extra.stopOrTriggerSound(var1);
      this.footsteps.stopOrTriggerSound(var1);
      this.vocals.stopOrTriggerSound(var1);
   }

   public void stopOrTriggerSoundByName(String var1) {
      this.extra.stopOrTriggerSoundByName(var1);
      this.footsteps.stopOrTriggerSoundByName(var1);
      this.vocals.stopOrTriggerSoundByName(var1);
   }

   public void stopAll() {
      this.extra.stopAll();
      this.footsteps.stopAll();
      this.vocals.stopAll();
   }

   public int stopSoundByName(String var1) {
      this.extra.stopSoundByName(var1);
      this.footsteps.stopSoundByName(var1);
      this.vocals.stopSoundByName(var1);
      return 0;
   }

   public boolean hasSoundsToStart() {
      return this.extra.hasSoundsToStart() || this.footsteps.hasSoundsToStart() || this.vocals.hasSoundsToStart();
   }

   public boolean isPlaying(long var1) {
      return this.extra.isPlaying(var1) || this.footsteps.isPlaying(var1) || this.vocals.isPlaying(var1);
   }

   public boolean isPlaying(String var1) {
      return this.extra.isPlaying(var1) || this.footsteps.isPlaying(var1) || this.vocals.isPlaying(var1);
   }

   public void setParameterValue(long var1, FMOD_STUDIO_PARAMETER_DESCRIPTION var3, float var4) {
      this.extra.setParameterValue(var1, var3, var4);
   }

   static enum footstep {
      upstairs,
      grass,
      wood,
      concrete,
      gravel,
      snow;

      // $FF: synthetic method
      private static CharacterSoundEmitter.footstep[] $values() {
         return new CharacterSoundEmitter.footstep[]{upstairs, grass, wood, concrete, gravel, snow};
      }
   }
}
