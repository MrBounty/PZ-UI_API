package fmod.fmod;

import fmod.FMOD_STUDIO_EVENT_PROPERTY;
import fmod.javafmod;
import fmod.javafmodJNI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import zombie.GameSounds;
import zombie.SoundManager;
import zombie.audio.BaseSoundEmitter;
import zombie.audio.FMODParameter;
import zombie.audio.GameSound;
import zombie.audio.GameSoundClip;
import zombie.audio.parameters.ParameterOcclusion;
import zombie.characters.IsoPlayer;
import zombie.debug.DebugLog;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.areas.IsoRoom;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.popman.ObjectPool;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.SoundTimelineScript;

public final class FMODSoundEmitter extends BaseSoundEmitter {
   private final ArrayList ToStart = new ArrayList();
   private final ArrayList Instances = new ArrayList();
   public float x;
   public float y;
   public float z;
   public EmitterType emitterType;
   public IsoObject parent;
   private final ParameterOcclusion occlusion = new ParameterOcclusion(this);
   private final ArrayList parameters = new ArrayList();
   public IFMODParameterUpdater parameterUpdater;
   private final ArrayList parameterValues = new ArrayList();
   private static final ObjectPool parameterValuePool = new ObjectPool(FMODSoundEmitter.ParameterValue::new);
   private static BitSet parameterSet;
   private final ArrayDeque eventSoundPool = new ArrayDeque();
   private final ArrayDeque fileSoundPool = new ArrayDeque();
   private static long CurrentTimeMS = 0L;

   public FMODSoundEmitter() {
      SoundManager.instance.registerEmitter(this);
      if (parameterSet == null) {
         parameterSet = new BitSet(FMODManager.instance.getParameterCount());
      }

   }

   public void randomStart() {
   }

   public void setPos(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public int stopSound(long var1) {
      int var3;
      FMODSoundEmitter.Sound var4;
      for(var3 = 0; var3 < this.ToStart.size(); ++var3) {
         var4 = (FMODSoundEmitter.Sound)this.ToStart.get(var3);
         if (var4.getRef() == var1) {
            this.sendStopSound(var4.name, false);
            var4.release();
            this.ToStart.remove(var3--);
         }
      }

      for(var3 = 0; var3 < this.Instances.size(); ++var3) {
         var4 = (FMODSoundEmitter.Sound)this.Instances.get(var3);
         if (var4.getRef() == var1) {
            var4.stop();
            this.sendStopSound(var4.name, false);
            var4.release();
            this.Instances.remove(var3--);
         }
      }

      return 0;
   }

   public int stopSoundByName(String var1) {
      GameSound var2 = GameSounds.getSound(var1);
      if (var2 == null) {
         return 0;
      } else {
         int var3;
         FMODSoundEmitter.Sound var4;
         for(var3 = 0; var3 < this.ToStart.size(); ++var3) {
            var4 = (FMODSoundEmitter.Sound)this.ToStart.get(var3);
            if (var2.clips.contains(var4.clip)) {
               var4.release();
               this.ToStart.remove(var3--);
            }
         }

         for(var3 = 0; var3 < this.Instances.size(); ++var3) {
            var4 = (FMODSoundEmitter.Sound)this.Instances.get(var3);
            if (var2.clips.contains(var4.clip)) {
               var4.stop();
               var4.release();
               this.Instances.remove(var3--);
            }
         }

         return 0;
      }
   }

   public void stopOrTriggerSound(long var1) {
      int var3 = this.findToStart(var1);
      FMODSoundEmitter.Sound var4;
      if (var3 != -1) {
         var4 = (FMODSoundEmitter.Sound)this.ToStart.remove(var3);
         this.sendStopSound(var4.name, true);
         var4.release();
      } else {
         var3 = this.findInstance(var1);
         if (var3 != -1) {
            var4 = (FMODSoundEmitter.Sound)this.Instances.get(var3);
            this.sendStopSound(var4.name, true);
            if (var4.clip.hasSustainPoints()) {
               var4.triggerCue();
            } else {
               this.Instances.remove(var3);
               var4.stop();
               var4.release();
            }
         }

      }
   }

   public void stopOrTriggerSoundByName(String var1) {
      GameSound var2 = GameSounds.getSound(var1);
      if (var2 != null) {
         int var3;
         FMODSoundEmitter.Sound var4;
         for(var3 = 0; var3 < this.ToStart.size(); ++var3) {
            var4 = (FMODSoundEmitter.Sound)this.ToStart.get(var3);
            if (var2.clips.contains(var4.clip)) {
               this.ToStart.remove(var3--);
               var4.release();
            }
         }

         for(var3 = 0; var3 < this.Instances.size(); ++var3) {
            var4 = (FMODSoundEmitter.Sound)this.Instances.get(var3);
            if (var2.clips.contains(var4.clip)) {
               if (var4.clip.hasSustainPoints()) {
                  var4.triggerCue();
               } else {
                  var4.stop();
                  var4.release();
                  this.Instances.remove(var3--);
               }
            }
         }

      }
   }

   public void setVolume(long var1, float var3) {
      int var4;
      FMODSoundEmitter.Sound var5;
      for(var4 = 0; var4 < this.ToStart.size(); ++var4) {
         var5 = (FMODSoundEmitter.Sound)this.ToStart.get(var4);
         if (var5.getRef() == var1) {
            var5.volume = var3;
         }
      }

      for(var4 = 0; var4 < this.Instances.size(); ++var4) {
         var5 = (FMODSoundEmitter.Sound)this.Instances.get(var4);
         if (var5.getRef() == var1) {
            var5.volume = var3;
         }
      }

   }

   public void setPitch(long var1, float var3) {
      int var4;
      FMODSoundEmitter.Sound var5;
      for(var4 = 0; var4 < this.ToStart.size(); ++var4) {
         var5 = (FMODSoundEmitter.Sound)this.ToStart.get(var4);
         if (var5.getRef() == var1) {
            var5.pitch = var3;
         }
      }

      for(var4 = 0; var4 < this.Instances.size(); ++var4) {
         var5 = (FMODSoundEmitter.Sound)this.Instances.get(var4);
         if (var5.getRef() == var1) {
            var5.pitch = var3;
         }
      }

   }

   public boolean hasSustainPoints(long var1) {
      int var3;
      FMODSoundEmitter.Sound var4;
      for(var3 = 0; var3 < this.ToStart.size(); ++var3) {
         var4 = (FMODSoundEmitter.Sound)this.ToStart.get(var3);
         if (var4.getRef() == var1) {
            if (var4.clip.eventDescription == null) {
               return false;
            }

            return var4.clip.eventDescription.bHasSustainPoints;
         }
      }

      for(var3 = 0; var3 < this.Instances.size(); ++var3) {
         var4 = (FMODSoundEmitter.Sound)this.Instances.get(var3);
         if (var4.getRef() == var1) {
            if (var4.clip.eventDescription == null) {
               return false;
            }

            return var4.clip.eventDescription.bHasSustainPoints;
         }
      }

      return false;
   }

   public void triggerCue(long var1) {
      for(int var3 = 0; var3 < this.Instances.size(); ++var3) {
         FMODSoundEmitter.Sound var4 = (FMODSoundEmitter.Sound)this.Instances.get(var3);
         if (var4.getRef() == var1) {
            var4.triggerCue();
         }
      }

   }

   public void setVolumeAll(float var1) {
      int var2;
      FMODSoundEmitter.Sound var3;
      for(var2 = 0; var2 < this.ToStart.size(); ++var2) {
         var3 = (FMODSoundEmitter.Sound)this.ToStart.get(var2);
         var3.volume = var1;
      }

      for(var2 = 0; var2 < this.Instances.size(); ++var2) {
         var3 = (FMODSoundEmitter.Sound)this.Instances.get(var2);
         var3.volume = var1;
      }

   }

   public void stopAll() {
      int var1;
      FMODSoundEmitter.Sound var2;
      for(var1 = 0; var1 < this.ToStart.size(); ++var1) {
         var2 = (FMODSoundEmitter.Sound)this.ToStart.get(var1);
         var2.release();
      }

      for(var1 = 0; var1 < this.Instances.size(); ++var1) {
         var2 = (FMODSoundEmitter.Sound)this.Instances.get(var1);
         var2.stop();
         var2.release();
      }

      this.ToStart.clear();
      this.Instances.clear();
   }

   public long playSound(String var1) {
      if (GameClient.bClient) {
         if (this.parent instanceof IsoMovingObject) {
            GameClient.instance.PlaySound(var1, false, (IsoMovingObject)this.parent);
         } else {
            GameClient.instance.PlayWorldSound(var1, (int)this.x, (int)this.y, (byte)((int)this.z));
         }
      }

      return GameServer.bServer ? 0L : this.playSoundImpl(var1, (IsoObject)null);
   }

   public long playSound(String var1, int var2, int var3, int var4) {
      this.x = (float)var2;
      this.y = (float)var3;
      this.z = (float)var4;
      return this.playSound(var1);
   }

   public long playSound(String var1, IsoGridSquare var2) {
      this.x = (float)var2.x + 0.5F;
      this.y = (float)var2.y + 0.5F;
      this.z = (float)var2.z;
      return this.playSound(var1);
   }

   public long playSoundImpl(String var1, IsoGridSquare var2) {
      this.x = (float)var2.x + 0.5F;
      this.y = (float)var2.y + 0.5F;
      this.z = (float)var2.z + 0.5F;
      return this.playSoundImpl(var1, (IsoObject)null);
   }

   public long playSound(String var1, boolean var2) {
      return this.playSound(var1);
   }

   public long playSoundImpl(String var1, boolean var2, IsoObject var3) {
      return this.playSoundImpl(var1, var3);
   }

   public long playSoundLooped(String var1) {
      if (GameClient.bClient) {
         if (this.parent instanceof IsoMovingObject) {
            GameClient.instance.PlaySound(var1, true, (IsoMovingObject)this.parent);
         } else {
            GameClient.instance.PlayWorldSound(var1, (int)this.x, (int)this.y, (byte)((int)this.z));
         }
      }

      return this.playSoundLoopedImpl(var1);
   }

   public long playSoundLoopedImpl(String var1) {
      return this.playSoundImpl(var1, false, (IsoObject)null);
   }

   public long playSound(String var1, IsoObject var2) {
      if (GameClient.bClient) {
         if (var2 instanceof IsoMovingObject) {
            GameClient.instance.PlaySound(var1, false, (IsoMovingObject)this.parent);
         } else {
            GameClient.instance.PlayWorldSound(var1, (int)this.x, (int)this.y, (byte)((int)this.z));
         }
      }

      return GameServer.bServer ? 0L : this.playSoundImpl(var1, var2);
   }

   public long playSoundImpl(String var1, IsoObject var2) {
      GameSound var3 = GameSounds.getSound(var1);
      if (var3 == null) {
         return 0L;
      } else {
         GameSoundClip var4 = var3.getRandomClip();
         return this.playClip(var4, var2);
      }
   }

   public long playClip(GameSoundClip var1, IsoObject var2) {
      FMODSoundEmitter.Sound var3 = this.addSound(var1, 1.0F, var2);
      return var3 == null ? 0L : var3.getRef();
   }

   public long playAmbientSound(String var1) {
      if (GameServer.bServer) {
         return 0L;
      } else {
         GameSound var2 = GameSounds.getSound(var1);
         if (var2 == null) {
            return 0L;
         } else {
            GameSoundClip var3 = var2.getRandomClip();
            FMODSoundEmitter.Sound var4 = this.addSound(var3, 1.0F, (IsoObject)null);
            if (var4 instanceof FMODSoundEmitter.FileSound) {
               ((FMODSoundEmitter.FileSound)var4).ambient = true;
            }

            return var4 == null ? 0L : var4.getRef();
         }
      }
   }

   public long playAmbientLoopedImpl(String var1) {
      if (GameServer.bServer) {
         return 0L;
      } else {
         GameSound var2 = GameSounds.getSound(var1);
         if (var2 == null) {
            return 0L;
         } else {
            GameSoundClip var3 = var2.getRandomClip();
            FMODSoundEmitter.Sound var4 = this.addSound(var3, 1.0F, (IsoObject)null);
            return var4 == null ? 0L : var4.getRef();
         }
      }
   }

   public void set3D(long var1, boolean var3) {
      int var4;
      FMODSoundEmitter.Sound var5;
      for(var4 = 0; var4 < this.ToStart.size(); ++var4) {
         var5 = (FMODSoundEmitter.Sound)this.ToStart.get(var4);
         if (var5.getRef() == var1) {
            var5.set3D(var3);
         }
      }

      for(var4 = 0; var4 < this.Instances.size(); ++var4) {
         var5 = (FMODSoundEmitter.Sound)this.Instances.get(var4);
         if (var5.getRef() == var1) {
            var5.set3D(var3);
         }
      }

   }

   public void tick() {
      int var1;
      if (!this.isEmpty()) {
         this.occlusion.update();

         for(var1 = 0; var1 < this.parameters.size(); ++var1) {
            FMODParameter var2 = (FMODParameter)this.parameters.get(var1);
            var2.update();
         }
      }

      FMODSoundEmitter.Sound var4;
      for(var1 = 0; var1 < this.ToStart.size(); ++var1) {
         var4 = (FMODSoundEmitter.Sound)this.ToStart.get(var1);
         this.Instances.add(var4);
      }

      for(var1 = 0; var1 < this.Instances.size(); ++var1) {
         var4 = (FMODSoundEmitter.Sound)this.Instances.get(var1);
         boolean var3 = this.ToStart.contains(var4);
         if (var4.tick(var3)) {
            this.Instances.remove(var1--);
            var4.release();
         }
      }

      this.ToStart.clear();
   }

   public boolean hasSoundsToStart() {
      return !this.ToStart.isEmpty();
   }

   public boolean isEmpty() {
      return this.ToStart.isEmpty() && this.Instances.isEmpty();
   }

   public boolean isPlaying(long var1) {
      int var3;
      for(var3 = 0; var3 < this.ToStart.size(); ++var3) {
         if (((FMODSoundEmitter.Sound)this.ToStart.get(var3)).getRef() == var1) {
            return true;
         }
      }

      for(var3 = 0; var3 < this.Instances.size(); ++var3) {
         if (((FMODSoundEmitter.Sound)this.Instances.get(var3)).getRef() == var1) {
            return true;
         }
      }

      return false;
   }

   public boolean isPlaying(String var1) {
      int var2;
      for(var2 = 0; var2 < this.ToStart.size(); ++var2) {
         if (var1.equals(((FMODSoundEmitter.Sound)this.ToStart.get(var2)).name)) {
            return true;
         }
      }

      for(var2 = 0; var2 < this.Instances.size(); ++var2) {
         if (var1.equals(((FMODSoundEmitter.Sound)this.Instances.get(var2)).name)) {
            return true;
         }
      }

      return false;
   }

   public boolean restart(long var1) {
      int var3 = this.findToStart(var1);
      if (var3 != -1) {
         return true;
      } else {
         var3 = this.findInstance(var1);
         return var3 != -1 && ((FMODSoundEmitter.Sound)this.Instances.get(var3)).restart();
      }
   }

   private int findInstance(long var1) {
      for(int var3 = 0; var3 < this.Instances.size(); ++var3) {
         FMODSoundEmitter.Sound var4 = (FMODSoundEmitter.Sound)this.Instances.get(var3);
         if (var4.getRef() == var1) {
            return var3;
         }
      }

      return -1;
   }

   private int findInstance(String var1) {
      GameSound var2 = GameSounds.getSound(var1);
      if (var2 == null) {
         return -1;
      } else {
         for(int var3 = 0; var3 < this.Instances.size(); ++var3) {
            FMODSoundEmitter.Sound var4 = (FMODSoundEmitter.Sound)this.Instances.get(var3);
            if (var2.clips.contains(var4.clip)) {
               return var3;
            }
         }

         return -1;
      }
   }

   private int findToStart(long var1) {
      for(int var3 = 0; var3 < this.ToStart.size(); ++var3) {
         FMODSoundEmitter.Sound var4 = (FMODSoundEmitter.Sound)this.ToStart.get(var3);
         if (var4.getRef() == var1) {
            return var3;
         }
      }

      return -1;
   }

   private int findToStart(String var1) {
      GameSound var2 = GameSounds.getSound(var1);
      if (var2 == null) {
         return -1;
      } else {
         for(int var3 = 0; var3 < this.ToStart.size(); ++var3) {
            FMODSoundEmitter.Sound var4 = (FMODSoundEmitter.Sound)this.ToStart.get(var3);
            if (var2.clips.contains(var4.clip)) {
               return var3;
            }
         }

         return -1;
      }
   }

   public void addParameter(FMODParameter var1) {
      this.parameters.add(var1);
   }

   public void setParameterValue(long var1, FMOD_STUDIO_PARAMETER_DESCRIPTION var3, float var4) {
      if (var1 != 0L && var3 != null) {
         int var5 = this.findInstance(var1);
         if (var5 != -1) {
            FMODSoundEmitter.Sound var7 = (FMODSoundEmitter.Sound)this.Instances.get(var5);
            var7.setParameterValue(var3, var4);
         } else {
            var5 = this.findParameterValue(var1, var3);
            if (var5 != -1) {
               ((FMODSoundEmitter.ParameterValue)this.parameterValues.get(var5)).value = var4;
            } else {
               FMODSoundEmitter.ParameterValue var6 = (FMODSoundEmitter.ParameterValue)parameterValuePool.alloc();
               var6.eventInstance = var1;
               var6.parameterDescription = var3;
               var6.value = var4;
               this.parameterValues.add(var6);
            }
         }
      }
   }

   public void setTimelinePosition(long var1, String var3) {
      if (var1 != 0L) {
         int var4 = this.findToStart(var1);
         if (var4 != -1) {
            FMODSoundEmitter.Sound var5 = (FMODSoundEmitter.Sound)this.ToStart.get(var4);
            var5.setTimelinePosition(var3);
         }

      }
   }

   private int findParameterValue(long var1, FMOD_STUDIO_PARAMETER_DESCRIPTION var3) {
      for(int var4 = 0; var4 < this.parameterValues.size(); ++var4) {
         FMODSoundEmitter.ParameterValue var5 = (FMODSoundEmitter.ParameterValue)this.parameterValues.get(var4);
         if (var5.eventInstance == var1 && var5.parameterDescription == var3) {
            return var4;
         }
      }

      return -1;
   }

   public void clearParameters() {
      this.occlusion.resetToDefault();
      this.parameters.clear();
      parameterValuePool.releaseAll(this.parameterValues);
      this.parameterValues.clear();
   }

   private void startEvent(long var1, GameSoundClip var3) {
      parameterSet.clear();
      ArrayList var4 = this.parameters;
      ArrayList var5 = var3.eventDescription.parameters;

      for(int var6 = 0; var6 < var5.size(); ++var6) {
         FMOD_STUDIO_PARAMETER_DESCRIPTION var7 = (FMOD_STUDIO_PARAMETER_DESCRIPTION)var5.get(var6);
         int var8 = this.findParameterValue(var1, var7);
         if (var8 != -1) {
            FMODSoundEmitter.ParameterValue var11 = (FMODSoundEmitter.ParameterValue)this.parameterValues.get(var8);
            javafmod.FMOD_Studio_EventInstance_SetParameterByID(var1, var7.id, var11.value, false);
            parameterSet.set(var7.globalIndex, true);
         } else if (var7 == this.occlusion.getParameterDescription()) {
            this.occlusion.startEventInstance(var1);
            parameterSet.set(var7.globalIndex, true);
         } else {
            for(int var9 = 0; var9 < var4.size(); ++var9) {
               FMODParameter var10 = (FMODParameter)var4.get(var9);
               if (var10.getParameterDescription() == var7) {
                  var10.startEventInstance(var1);
                  parameterSet.set(var7.globalIndex, true);
                  break;
               }
            }
         }
      }

      if (this.parameterUpdater != null) {
         this.parameterUpdater.startEvent(var1, var3, parameterSet);
      }

   }

   private void updateEvent(long var1, GameSoundClip var3) {
      if (this.parameterUpdater != null) {
         this.parameterUpdater.updateEvent(var1, var3);
      }

   }

   private void stopEvent(long var1, GameSoundClip var3) {
      parameterSet.clear();
      ArrayList var4 = this.parameters;
      ArrayList var5 = var3.eventDescription.parameters;

      for(int var6 = 0; var6 < var5.size(); ++var6) {
         FMOD_STUDIO_PARAMETER_DESCRIPTION var7 = (FMOD_STUDIO_PARAMETER_DESCRIPTION)var5.get(var6);
         int var8 = this.findParameterValue(var1, var7);
         if (var8 != -1) {
            FMODSoundEmitter.ParameterValue var11 = (FMODSoundEmitter.ParameterValue)this.parameterValues.remove(var8);
            parameterValuePool.release((Object)var11);
            parameterSet.set(var7.globalIndex, true);
         } else if (var7 == this.occlusion.getParameterDescription()) {
            this.occlusion.stopEventInstance(var1);
            parameterSet.set(var7.globalIndex, true);
         } else {
            for(int var9 = 0; var9 < var4.size(); ++var9) {
               FMODParameter var10 = (FMODParameter)var4.get(var9);
               if (var10.getParameterDescription() == var7) {
                  var10.stopEventInstance(var1);
                  parameterSet.set(var7.globalIndex, true);
                  break;
               }
            }
         }
      }

      if (this.parameterUpdater != null) {
         this.parameterUpdater.stopEvent(var1, var3, parameterSet);
      }

   }

   private FMODSoundEmitter.EventSound allocEventSound() {
      return this.eventSoundPool.isEmpty() ? new FMODSoundEmitter.EventSound(this) : (FMODSoundEmitter.EventSound)this.eventSoundPool.pop();
   }

   private void releaseEventSound(FMODSoundEmitter.EventSound var1) {
      assert !this.eventSoundPool.contains(var1);

      this.eventSoundPool.push(var1);
   }

   private FMODSoundEmitter.FileSound allocFileSound() {
      return this.fileSoundPool.isEmpty() ? new FMODSoundEmitter.FileSound(this) : (FMODSoundEmitter.FileSound)this.fileSoundPool.pop();
   }

   private void releaseFileSound(FMODSoundEmitter.FileSound var1) {
      assert !this.fileSoundPool.contains(var1);

      this.fileSoundPool.push(var1);
   }

   private FMODSoundEmitter.Sound addSound(GameSoundClip var1, float var2, IsoObject var3) {
      if (var1 == null) {
         DebugLog.log("null sound passed to SoundEmitter.playSoundImpl");
         return null;
      } else {
         long var4;
         if (var1.event != null && !var1.event.isEmpty()) {
            if (var1.eventDescription == null) {
               return null;
            } else {
               var4 = javafmod.FMOD_Studio_System_CreateEventInstance(var1.eventDescription.address);
               if (var4 < 0L) {
                  return null;
               } else {
                  if (var1.hasMinDistance()) {
                     javafmodJNI.FMOD_Studio_EventInstance_SetProperty(var4, FMOD_STUDIO_EVENT_PROPERTY.FMOD_STUDIO_EVENT_PROPERTY_MINIMUM_DISTANCE.ordinal(), var1.getMinDistance());
                  }

                  if (var1.hasMaxDistance()) {
                     javafmodJNI.FMOD_Studio_EventInstance_SetProperty(var4, FMOD_STUDIO_EVENT_PROPERTY.FMOD_STUDIO_EVENT_PROPERTY_MAXIMUM_DISTANCE.ordinal(), var1.getMaxDistance());
                  }

                  FMODSoundEmitter.EventSound var9 = this.allocEventSound();
                  var9.clip = var1;
                  var9.name = var1.gameSound.getName();
                  var9.eventInstance = var4;
                  var9.volume = var2;
                  var9.parent = var3;
                  var9.setVolume = 1.0F;
                  var9.setX = var9.setY = var9.setZ = 0.0F;
                  this.ToStart.add(var9);
                  return var9;
               }
            }
         } else if (var1.file != null && !var1.file.isEmpty()) {
            var4 = FMODManager.instance.loadSound(var1.file);
            if (var4 == 0L) {
               return null;
            } else {
               long var6 = javafmod.FMOD_System_PlaySound(var4, true);
               javafmod.FMOD_Channel_SetVolume(var6, 0.0F);
               javafmod.FMOD_Channel_SetPriority(var6, 9 - var1.priority);
               javafmod.FMOD_Channel_SetChannelGroup(var6, FMODManager.instance.channelGroupInGameNonBankSounds);
               if (var1.distanceMax == 0.0F || this.x == 0.0F && this.y == 0.0F) {
                  javafmod.FMOD_Channel_SetMode(var6, (long)FMODManager.FMOD_2D);
               }

               FMODSoundEmitter.FileSound var8 = this.allocFileSound();
               var8.clip = var1;
               var8.name = var1.gameSound.getName();
               var8.sound = var4;
               var8.pitch = var1.pitch;
               var8.channel = var6;
               var8.parent = var3;
               var8.volume = var2;
               var8.setVolume = 1.0F;
               var8.setX = var8.setY = var8.setZ = 0.0F;
               var8.is3D = -1;
               var8.ambient = false;
               this.ToStart.add(var8);
               return var8;
            }
         } else {
            return null;
         }
      }
   }

   private void sendStopSound(String var1, boolean var2) {
      if (GameClient.bClient && this.parent instanceof IsoMovingObject) {
         GameClient.instance.StopSound((IsoMovingObject)this.parent, var1, var2);
      }

   }

   public static void update() {
      CurrentTimeMS = System.currentTimeMillis();
   }

   private abstract static class Sound {
      FMODSoundEmitter emitter;
      public GameSoundClip clip;
      public String name;
      public float volume = 1.0F;
      public float pitch = 1.0F;
      public IsoObject parent;
      public float setVolume = 1.0F;
      float setX = 0.0F;
      float setY = 0.0F;
      float setZ = 0.0F;

      Sound(FMODSoundEmitter var1) {
         this.emitter = var1;
      }

      abstract long getRef();

      abstract void stop();

      abstract void set3D(boolean var1);

      abstract void release();

      abstract boolean tick(boolean var1);

      float getVolume() {
         this.clip = this.clip.checkReloaded();
         return this.volume * this.clip.getEffectiveVolume();
      }

      abstract void setParameterValue(FMOD_STUDIO_PARAMETER_DESCRIPTION var1, float var2);

      abstract void setTimelinePosition(String var1);

      abstract void triggerCue();

      abstract boolean restart();
   }

   private static final class FileSound extends FMODSoundEmitter.Sound {
      long sound;
      long channel;
      byte is3D = -1;
      boolean ambient;
      float lx;
      float ly;
      float lz;

      FileSound(FMODSoundEmitter var1) {
         super(var1);
      }

      long getRef() {
         return this.channel;
      }

      void stop() {
         if (this.channel != 0L) {
            javafmod.FMOD_Channel_Stop(this.channel);
            this.sound = 0L;
            this.channel = 0L;
         }
      }

      void set3D(boolean var1) {
         if (this.is3D != (byte)(var1 ? 1 : 0)) {
            javafmod.FMOD_Channel_SetMode(this.channel, var1 ? (long)FMODManager.FMOD_3D : (long)FMODManager.FMOD_2D);
            if (var1) {
               javafmod.FMOD_Channel_Set3DAttributes(this.channel, this.emitter.x, this.emitter.y, this.emitter.z * 3.0F, 0.0F, 0.0F, 0.0F);
            }

            this.is3D = (byte)(var1 ? 1 : 0);
         }

      }

      void release() {
         this.stop();
         this.emitter.releaseFileSound(this);
      }

      boolean tick(boolean var1) {
         if (var1 && this.clip.gameSound.isLooped()) {
            javafmod.FMOD_Channel_SetMode(this.channel, (long)FMODManager.FMOD_LOOP_NORMAL);
         }

         float var2 = this.clip.distanceMin;
         if (!var1 && !javafmod.FMOD_Channel_IsPlaying(this.channel)) {
            return true;
         } else {
            float var3 = this.emitter.x;
            float var4 = this.emitter.y;
            float var5 = this.emitter.z;
            if (this.clip.gameSound.is3D && (var3 != 0.0F || var4 != 0.0F)) {
               this.lx = var3;
               this.ly = var4;
               this.lz = var5;
               javafmod.FMOD_Channel_Set3DAttributes(this.channel, var3, var4, var5 * 3.0F, var3 - this.lx, var4 - this.ly, var5 * 3.0F - this.lz * 3.0F);
               if (IsoPlayer.numPlayers > 1) {
                  if (var1) {
                     javafmod.FMOD_System_SetReverbDefault(0, FMODManager.FMOD_PRESET_OFF);
                     javafmod.FMOD_Channel_Set3DMinMaxDistance(this.channel, this.clip.distanceMin, this.clip.distanceMax);
                     javafmod.FMOD_Channel_Set3DOcclusion(this.channel, 0.0F, 0.0F);
                  }

                  javafmod.FMOD_Channel_SetVolume(this.channel, this.getVolume());
                  if (var1) {
                     javafmod.FMOD_Channel_SetPaused(this.channel, false);
                  }

                  javafmod.FMOD_Channel_SetReverbProperties(this.channel, 0, 0.0F);
                  javafmod.FMOD_Channel_SetReverbProperties(this.channel, 1, 0.0F);
                  javafmod.FMOD_System_SetReverbDefault(1, FMODManager.FMOD_PRESET_OFF);
                  javafmod.FMOD_Channel_Set3DOcclusion(this.channel, 0.0F, 0.0F);
                  return false;
               } else {
                  float var6 = this.clip.reverbMaxRange;
                  float var7 = IsoUtils.DistanceManhatten(var3, var4, IsoPlayer.getInstance().x, IsoPlayer.getInstance().y, var5, IsoPlayer.getInstance().z) / var6;
                  IsoGridSquare var8 = IsoPlayer.getInstance().getCurrentSquare();
                  if (var8 == null) {
                     javafmod.FMOD_Channel_Set3DMinMaxDistance(this.channel, var2, this.clip.distanceMax);
                     javafmod.FMOD_Channel_SetVolume(this.channel, this.getVolume());
                     if (var1) {
                        javafmod.FMOD_Channel_SetPaused(this.channel, false);
                     }

                     return false;
                  } else {
                     if (var8.getRoom() == null) {
                        if (!this.ambient) {
                           var7 += IsoPlayer.getInstance().numNearbyBuildingsRooms / 32.0F;
                        }

                        if (!this.ambient) {
                           var7 += 0.08F;
                        }
                     } else {
                        float var9 = (float)var8.getRoom().Squares.size();
                        if (!this.ambient) {
                           var7 += var9 / 500.0F;
                        }
                     }

                     if (var7 > 1.0F) {
                        var7 = 1.0F;
                     }

                     var7 *= var7;
                     var7 *= var7;
                     var7 *= this.clip.reverbFactor;
                     var7 *= 10.0F;
                     if (IsoPlayer.getInstance().getCurrentSquare().getRoom() == null && var7 < 0.1F) {
                        var7 = 0.1F;
                     }

                     byte var10;
                     byte var11;
                     byte var15;
                     if (!this.ambient) {
                        if (var8.getRoom() != null) {
                           var15 = 0;
                           var10 = 1;
                           var11 = 2;
                        } else {
                           var15 = 2;
                           var10 = 0;
                           var11 = 1;
                        }
                     } else {
                        var15 = 2;
                        var10 = 0;
                        var11 = 1;
                     }

                     IsoGridSquare var12 = IsoWorld.instance.CurrentCell.getGridSquare((double)var3, (double)var4, (double)var5);
                     if (var12 != null && var12.getZone() != null && (var12.getZone().getType().equals("Forest") || var12.getZone().getType().equals("DeepForest"))) {
                        var15 = 1;
                        var10 = 0;
                        var11 = 2;
                     }

                     javafmod.FMOD_Channel_SetReverbProperties(this.channel, var15, 0.0F);
                     javafmod.FMOD_Channel_SetReverbProperties(this.channel, var10, 0.0F);
                     javafmod.FMOD_Channel_SetReverbProperties(this.channel, var11, 0.0F);
                     javafmod.FMOD_Channel_Set3DMinMaxDistance(this.channel, var2, this.clip.distanceMax);
                     IsoGridSquare var16 = IsoWorld.instance.CurrentCell.getGridSquare((double)var3, (double)var4, (double)var5);
                     float var17 = 0.0F;
                     float var19 = 0.0F;
                     IsoRoom var18;
                     if (var16 != null) {
                        if (!(this.emitter.parent instanceof IsoWindow) && !(this.emitter.parent instanceof IsoDoor)) {
                           if (var16.getRoom() != null) {
                              var18 = IsoPlayer.getInstance().getCurrentSquare().getRoom();
                              if (var18 == null) {
                                 var17 = 0.33F;
                                 var19 = 0.23F;
                              } else if (var18 != var16.getRoom()) {
                                 var17 = 0.24F;
                                 var19 = 0.24F;
                              }

                              if (var18 != null && var16.getRoom().getBuilding() != var18.getBuilding()) {
                                 var17 = 1.0F;
                                 var19 = 0.8F;
                              }

                              if (var18 != null && var16.getRoom().def.level != (int)IsoPlayer.getInstance().z) {
                                 var17 = 0.6F;
                                 var19 = 0.6F;
                              }
                           } else {
                              var18 = IsoPlayer.getInstance().getCurrentSquare().getRoom();
                              if (var18 != null) {
                                 var17 = 0.79F;
                                 var19 = 0.59F;
                              }
                           }
                        } else {
                           var18 = IsoPlayer.getInstance().getCurrentSquare().getRoom();
                           if (var18 != this.emitter.parent.square.getRoom()) {
                              if (var18 != null && var18.getBuilding() == this.emitter.parent.square.getBuilding()) {
                                 var17 = 0.33F;
                                 var19 = 0.33F;
                              } else {
                                 IsoGridSquare var13 = null;
                                 if (this.emitter.parent instanceof IsoDoor) {
                                    IsoDoor var14 = (IsoDoor)this.emitter.parent;
                                    if (var14.north) {
                                       var13 = IsoWorld.instance.CurrentCell.getGridSquare((double)var14.getX(), (double)(var14.getY() - 1.0F), (double)var14.getZ());
                                    } else {
                                       var13 = IsoWorld.instance.CurrentCell.getGridSquare((double)(var14.getX() - 1.0F), (double)var14.getY(), (double)var14.getZ());
                                    }
                                 } else {
                                    IsoWindow var20 = (IsoWindow)this.emitter.parent;
                                    if (var20.north) {
                                       var13 = IsoWorld.instance.CurrentCell.getGridSquare((double)var20.getX(), (double)(var20.getY() - 1.0F), (double)var20.getZ());
                                    } else {
                                       var13 = IsoWorld.instance.CurrentCell.getGridSquare((double)(var20.getX() - 1.0F), (double)var20.getY(), (double)var20.getZ());
                                    }
                                 }

                                 if (var13 != null) {
                                    var18 = IsoPlayer.getInstance().getCurrentSquare().getRoom();
                                    if (var18 != null || var13.getRoom() == null) {
                                       if (var18 != null && var13.getRoom() != null && var18.building == var13.getBuilding()) {
                                          if (var18 != var13.getRoom()) {
                                             if (var18.def.level == var13.getZ()) {
                                                var17 = 0.33F;
                                                var19 = 0.33F;
                                             } else {
                                                var17 = 0.6F;
                                                var19 = 0.6F;
                                             }
                                          }
                                       } else {
                                          var17 = 0.33F;
                                          var19 = 0.33F;
                                       }
                                    }
                                 }
                              }
                           }
                        }

                        if (!var16.isCouldSee(IsoPlayer.getPlayerIndex()) && var16 != IsoPlayer.getInstance().getCurrentSquare()) {
                           var17 += 0.4F;
                        }
                     } else {
                        if (IsoWorld.instance.MetaGrid.getRoomAt((int)var3, (int)var4, (int)var5) != null) {
                           var17 = 1.0F;
                           var19 = 1.0F;
                        }

                        var18 = IsoPlayer.getInstance().getCurrentSquare().getRoom();
                        if (var18 != null) {
                           var17 += 0.94F;
                        } else {
                           var17 += 0.6F;
                        }
                     }

                     if (var16 != null && (int)IsoPlayer.getInstance().z != var16.getZ()) {
                        var17 *= 1.3F;
                     }

                     if (var17 > 0.9F) {
                        var17 = 0.9F;
                     }

                     if (var19 > 0.9F) {
                        var19 = 0.9F;
                     }

                     if (this.emitter.emitterType == EmitterType.Footstep && var5 > IsoPlayer.getInstance().z && var16.getBuilding() == IsoPlayer.getInstance().getBuilding()) {
                        var17 = 0.0F;
                        var19 = 0.0F;
                     }

                     if ("HouseAlarm".equals(this.name)) {
                        var17 = 0.0F;
                        var19 = 0.0F;
                     }

                     javafmod.FMOD_Channel_Set3DOcclusion(this.channel, var17, var19);
                     javafmod.FMOD_Channel_SetVolume(this.channel, this.getVolume());
                     javafmod.FMOD_Channel_SetPitch(this.channel, this.pitch);
                     if (var1) {
                        javafmod.FMOD_Channel_SetPaused(this.channel, false);
                     }

                     this.lx = var3;
                     this.ly = var4;
                     this.lz = var5;
                     return false;
                  }
               }
            } else {
               if ((var3 != 0.0F || var4 != 0.0F) && (var1 || var3 != this.lx || var4 != this.ly) && this.is3D == 1) {
                  javafmod.FMOD_Channel_Set3DAttributes(this.channel, var3, var4, var5 * 3.0F, 0.0F, 0.0F, 0.0F);
               }

               javafmod.FMOD_Channel_SetVolume(this.channel, this.getVolume());
               javafmod.FMOD_Channel_SetPitch(this.channel, this.pitch);
               if (var1) {
                  javafmod.FMOD_Channel_SetPaused(this.channel, false);
               }

               return false;
            }
         }
      }

      void setParameterValue(FMOD_STUDIO_PARAMETER_DESCRIPTION var1, float var2) {
      }

      void setTimelinePosition(String var1) {
      }

      void triggerCue() {
      }

      boolean restart() {
         return false;
      }
   }

   private static final class ParameterValue {
      long eventInstance;
      FMOD_STUDIO_PARAMETER_DESCRIPTION parameterDescription;
      float value;
   }

   private static final class EventSound extends FMODSoundEmitter.Sound {
      long eventInstance;
      boolean bTriggeredCue = false;
      long checkTimeMS = 0L;

      EventSound(FMODSoundEmitter var1) {
         super(var1);
      }

      long getRef() {
         return this.eventInstance;
      }

      void stop() {
         if (this.eventInstance != 0L) {
            this.emitter.stopEvent(this.eventInstance, this.clip);
            javafmod.FMOD_Studio_EventInstance_Stop(this.eventInstance, false);
            javafmod.FMOD_Studio_ReleaseEventInstance(this.eventInstance);
            this.eventInstance = 0L;
         }
      }

      void set3D(boolean var1) {
      }

      void release() {
         this.stop();
         this.checkTimeMS = 0L;
         this.bTriggeredCue = false;
         this.emitter.releaseEventSound(this);
      }

      boolean tick(boolean var1) {
         IsoPlayer var2 = IsoPlayer.getInstance();
         if (IsoPlayer.numPlayers > 1) {
            var2 = null;
         }

         if (!var1) {
            int var3 = javafmod.FMOD_Studio_GetPlaybackState(this.eventInstance);
            if (var3 == FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_STOPPING.index) {
               return false;
            }

            if (var3 == FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_STOPPED.index) {
               javafmod.FMOD_Studio_ReleaseEventInstance(this.eventInstance);
               this.emitter.stopEvent(this.eventInstance, this.clip);
               this.eventInstance = 0L;
               return true;
            }

            if (this.bTriggeredCue && this.clip.eventDescription.length > 0L && FMODSoundEmitter.CurrentTimeMS - this.checkTimeMS > 1500L) {
               long var4 = javafmodJNI.FMOD_Studio_GetTimelinePosition(this.eventInstance);
               if (var4 > this.clip.eventDescription.length + 1000L) {
                  javafmod.FMOD_Studio_EventInstance_Stop(this.eventInstance, false);
               }

               this.checkTimeMS = FMODSoundEmitter.CurrentTimeMS;
            }
         }

         boolean var6 = Float.compare(this.emitter.x, this.setX) != 0 || Float.compare(this.emitter.y, this.setY) != 0 || Float.compare(this.emitter.z, this.setZ) != 0;
         if (var6) {
            this.setX = this.emitter.x;
            this.setY = this.emitter.y;
            this.setZ = this.emitter.z;
            javafmod.FMOD_Studio_EventInstance3D(this.eventInstance, this.emitter.x, this.emitter.y, this.emitter.z * 3.0F);
         }

         float var7 = this.getVolume();
         if (Float.compare(var7, this.setVolume) != 0) {
            this.setVolume = var7;
            javafmod.FMOD_Studio_EventInstance_SetVolume(this.eventInstance, var7);
         }

         if (var1) {
            this.emitter.startEvent(this.eventInstance, this.clip);
            javafmod.FMOD_Studio_StartEvent(this.eventInstance);
         } else {
            this.emitter.updateEvent(this.eventInstance, this.clip);
         }

         return false;
      }

      void setParameterValue(FMOD_STUDIO_PARAMETER_DESCRIPTION var1, float var2) {
         if (this.eventInstance != 0L) {
            javafmod.FMOD_Studio_EventInstance_SetParameterByID(this.eventInstance, var1.id, var2, false);
         }
      }

      void setTimelinePosition(String var1) {
         if (this.eventInstance != 0L && this.clip != null && this.clip.event != null) {
            SoundTimelineScript var2 = ScriptManager.instance.getSoundTimeline(this.clip.event);
            if (var2 != null) {
               int var3 = var2.getPosition(var1);
               if (var3 != -1) {
                  javafmodJNI.FMOD_Studio_EventInstance_SetTimelinePosition(this.eventInstance, var3);
               }
            }
         }
      }

      void triggerCue() {
         if (this.eventInstance != 0L) {
            if (this.clip.hasSustainPoints()) {
               javafmodJNI.FMOD_Studio_EventInstance_TriggerCue(this.eventInstance);
               this.bTriggeredCue = true;
            }
         }
      }

      boolean restart() {
         if (this.eventInstance == 0L) {
            return false;
         } else {
            javafmodJNI.FMOD_Studio_StartEvent(this.eventInstance);
            return true;
         }
      }
   }
}
