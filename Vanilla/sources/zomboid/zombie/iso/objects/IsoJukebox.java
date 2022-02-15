package zombie.iso.objects;

import fmod.fmod.Audio;
import zombie.SoundManager;
import zombie.WorldSoundManager;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.sprite.IsoSprite;

public class IsoJukebox extends IsoObject {
   private Audio JukeboxTrack = null;
   private boolean IsPlaying = false;
   private float MusicRadius = 30.0F;
   private boolean Activated = false;
   private int WorldSoundPulseRate = 150;
   private int WorldSoundPulseDelay = 0;

   public IsoJukebox(IsoCell var1, IsoGridSquare var2, IsoSprite var3) {
      super(var1, var2, var3);
   }

   public String getObjectName() {
      return "Jukebox";
   }

   public IsoJukebox(IsoCell var1) {
      super(var1);
   }

   public IsoJukebox(IsoCell var1, IsoGridSquare var2, String var3) {
      super(var1, var2, var3);
      this.JukeboxTrack = null;
      this.IsPlaying = false;
      this.Activated = false;
      this.WorldSoundPulseDelay = 0;
   }

   public void addToWorld() {
      super.addToWorld();
      this.getCell().addToStaticUpdaterObjectList(this);
   }

   public void SetPlaying(boolean var1) {
      if (this.IsPlaying != var1) {
         this.IsPlaying = var1;
         if (this.IsPlaying && this.JukeboxTrack == null) {
            String var2 = null;
            switch(Rand.Next(4)) {
            case 0:
               var2 = "paws1";
               break;
            case 1:
               var2 = "paws2";
               break;
            case 2:
               var2 = "paws3";
               break;
            case 3:
               var2 = "paws4";
            }

            this.JukeboxTrack = SoundManager.instance.PlaySound(var2, false, 0.0F);
         }

      }
   }

   public boolean onMouseLeftClick(int var1, int var2) {
      IsoPlayer var3 = IsoPlayer.getInstance();
      if (var3 != null && !var3.isDead()) {
         if (IsoPlayer.getInstance().getCurrentSquare() == null) {
            return false;
         } else {
            float var4 = 0.0F;
            int var5 = Math.abs(this.square.getX() - IsoPlayer.getInstance().getCurrentSquare().getX()) + Math.abs(this.square.getY() - IsoPlayer.getInstance().getCurrentSquare().getY() + Math.abs(this.square.getZ() - IsoPlayer.getInstance().getCurrentSquare().getZ()));
            if (var5 < 4) {
               if (!this.Activated) {
                  if (Core.NumJukeBoxesActive < Core.MaxJukeBoxesActive) {
                     this.WorldSoundPulseDelay = 0;
                     this.Activated = true;
                     this.SetPlaying(true);
                     ++Core.NumJukeBoxesActive;
                  }
               } else {
                  this.WorldSoundPulseDelay = 0;
                  this.SetPlaying(false);
                  this.Activated = false;
                  if (this.JukeboxTrack != null) {
                     SoundManager.instance.StopSound(this.JukeboxTrack);
                     this.JukeboxTrack.stop();
                     this.JukeboxTrack = null;
                  }

                  --Core.NumJukeBoxesActive;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public void update() {
      if (IsoPlayer.getInstance() != null) {
         if (IsoPlayer.getInstance().getCurrentSquare() != null) {
            if (this.Activated) {
               float var1 = 0.0F;
               int var2 = Math.abs(this.square.getX() - IsoPlayer.getInstance().getCurrentSquare().getX()) + Math.abs(this.square.getY() - IsoPlayer.getInstance().getCurrentSquare().getY() + Math.abs(this.square.getZ() - IsoPlayer.getInstance().getCurrentSquare().getZ()));
               if ((float)var2 < this.MusicRadius) {
                  this.SetPlaying(true);
                  var1 = (this.MusicRadius - (float)var2) / this.MusicRadius;
               }

               if (this.JukeboxTrack != null) {
                  float var3 = var1 + 0.2F;
                  if (var3 > 1.0F) {
                     var3 = 1.0F;
                  }

                  SoundManager.instance.BlendVolume(this.JukeboxTrack, var1);
                  if (this.WorldSoundPulseDelay > 0) {
                     --this.WorldSoundPulseDelay;
                  }

                  if (this.WorldSoundPulseDelay == 0) {
                     WorldSoundManager.instance.addSound(IsoPlayer.getInstance(), this.square.getX(), this.square.getY(), this.square.getZ(), 70, 70, true);
                     this.WorldSoundPulseDelay = this.WorldSoundPulseRate;
                  }

                  if (!this.JukeboxTrack.isPlaying()) {
                     this.WorldSoundPulseDelay = 0;
                     this.SetPlaying(false);
                     this.Activated = false;
                     if (this.JukeboxTrack != null) {
                        SoundManager.instance.StopSound(this.JukeboxTrack);
                        this.JukeboxTrack.stop();
                        this.JukeboxTrack = null;
                     }

                     --Core.NumJukeBoxesActive;
                  }
               }
            }

         }
      }
   }
}
