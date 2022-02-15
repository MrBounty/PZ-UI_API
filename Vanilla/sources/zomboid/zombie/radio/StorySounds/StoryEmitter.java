package zombie.radio.StorySounds;

import fmod.javafmod;
import fmod.fmod.FMODManager;
import java.util.ArrayList;
import java.util.Stack;
import zombie.GameSounds;
import zombie.SoundManager;
import zombie.audio.GameSound;
import zombie.audio.GameSoundClip;
import zombie.characters.IsoPlayer;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoUtils;
import zombie.iso.Vector2;

public final class StoryEmitter {
   public int max = -1;
   public float volumeMod = 1.0F;
   public boolean coordinate3D = true;
   public Stack SoundStack = new Stack();
   public ArrayList Instances = new ArrayList();
   public ArrayList ToStart = new ArrayList();
   private Vector2 soundVect = new Vector2();
   private Vector2 playerVect = new Vector2();

   public int stopSound(long var1) {
      return javafmod.FMOD_Channel_Stop(var1);
   }

   public long playSound(String var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      if (this.max != -1 && this.max <= this.Instances.size() + this.ToStart.size()) {
         return 0L;
      } else {
         GameSound var8 = GameSounds.getSound(var1);
         if (var8 == null) {
            return 0L;
         } else {
            GameSoundClip var9 = var8.getRandomClip();
            long var10 = FMODManager.instance.loadSound(var1);
            if (var10 == 0L) {
               return 0L;
            } else {
               StoryEmitter.Sound var12;
               if (this.SoundStack.isEmpty()) {
                  var12 = new StoryEmitter.Sound();
               } else {
                  var12 = (StoryEmitter.Sound)this.SoundStack.pop();
               }

               var12.minRange = var6;
               var12.maxRange = var7;
               var12.x = var3;
               var12.y = var4;
               var12.z = var5;
               var12.volume = SoundManager.instance.getSoundVolume() * var2 * this.volumeMod;
               var12.sound = var10;
               var12.channel = javafmod.FMOD_System_PlaySound(var10, true);
               this.ToStart.add(var12);
               javafmod.FMOD_Channel_Set3DAttributes(var12.channel, var12.x - IsoPlayer.getInstance().x, var12.y - IsoPlayer.getInstance().y, var12.z - IsoPlayer.getInstance().z, 0.0F, 0.0F, 0.0F);
               javafmod.FMOD_Channel_Set3DOcclusion(var12.channel, 1.0F, 1.0F);
               if (IsoPlayer.getInstance() != null && IsoPlayer.getInstance().Traits.Deaf.isSet()) {
                  javafmod.FMOD_Channel_SetVolume(var12.channel, 0.0F);
               } else {
                  javafmod.FMOD_Channel_SetVolume(var12.channel, var12.volume);
               }

               return var12.channel;
            }
         }
      }
   }

   public void tick() {
      int var1;
      StoryEmitter.Sound var2;
      for(var1 = 0; var1 < this.ToStart.size(); ++var1) {
         var2 = (StoryEmitter.Sound)this.ToStart.get(var1);
         javafmod.FMOD_Channel_SetPaused(var2.channel, false);
         this.Instances.add(var2);
      }

      this.ToStart.clear();

      for(var1 = 0; var1 < this.Instances.size(); ++var1) {
         var2 = (StoryEmitter.Sound)this.Instances.get(var1);
         if (!javafmod.FMOD_Channel_IsPlaying(var2.channel)) {
            this.SoundStack.push(var2);
            this.Instances.remove(var2);
            --var1;
         } else {
            float var3 = IsoUtils.DistanceManhatten(var2.x, var2.y, IsoPlayer.getInstance().x, IsoPlayer.getInstance().y, var2.z, IsoPlayer.getInstance().z) / var2.maxRange;
            if (var3 > 1.0F) {
               var3 = 1.0F;
            }

            if (!this.coordinate3D) {
               javafmod.FMOD_Channel_Set3DAttributes(var2.channel, Math.abs(var2.x - IsoPlayer.getInstance().x), Math.abs(var2.y - IsoPlayer.getInstance().y), Math.abs(var2.z - IsoPlayer.getInstance().z), 0.0F, 0.0F, 0.0F);
            } else {
               javafmod.FMOD_Channel_Set3DAttributes(var2.channel, Math.abs(var2.x - IsoPlayer.getInstance().x), Math.abs(var2.z - IsoPlayer.getInstance().z), Math.abs(var2.y - IsoPlayer.getInstance().y), 0.0F, 0.0F, 0.0F);
            }

            javafmod.FMOD_System_SetReverbDefault(0, FMODManager.FMOD_PRESET_MOUNTAINS);
            javafmod.FMOD_Channel_SetReverbProperties(var2.channel, 0, 1.0F);
            javafmod.FMOD_Channel_Set3DMinMaxDistance(var2.channel, var2.minRange, var2.maxRange);
            float var4 = 0.0F;
            float var5 = 0.0F;
            IsoGridSquare var6 = IsoPlayer.getInstance().getCurrentSquare();
            this.soundVect.set(var2.x, var2.y);
            this.playerVect.set(IsoPlayer.getInstance().x, IsoPlayer.getInstance().y);
            float var7 = (float)Math.toDegrees((double)this.playerVect.angleTo(this.soundVect));
            float var8 = (float)Math.toDegrees((double)IsoPlayer.getInstance().getForwardDirection().getDirectionNeg());
            if (var8 >= 0.0F && var8 <= 90.0F) {
               var8 = -90.0F - var8;
            } else if (var8 > 90.0F && var8 <= 180.0F) {
               var8 = 90.0F + (180.0F - var8);
            } else if (var8 < 0.0F && var8 >= -90.0F) {
               var8 = 0.0F - (90.0F + var8);
            } else if (var8 < 0.0F && var8 >= -180.0F) {
               var8 = 90.0F - (180.0F + var8);
            }

            float var9 = Math.abs(var7 - var8) % 360.0F;
            float var10 = var9 > 180.0F ? 360.0F - var9 : var9;
            float var11 = (180.0F - var10) / 180.0F;
            var3 /= 0.4F;
            if (var3 > 1.0F) {
               var3 = 1.0F;
            }

            var4 = 0.85F * var3 * var11;
            var5 = 0.85F * var3 * var11;
            if (var6.getRoom() != null) {
               var4 = 0.75F + 0.1F * var3 + 0.1F * var11;
               var5 = 0.75F + 0.1F * var3 + 0.1F * var11;
            }

            javafmod.FMOD_Channel_Set3DOcclusion(var2.channel, var4, var5);
         }
      }

   }

   public static final class Sound {
      public long sound;
      public long channel;
      public float volume;
      public float x;
      public float y;
      public float z;
      public float minRange;
      public float maxRange;
   }
}
