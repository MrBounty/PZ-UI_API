package zombie.radio.StorySounds;

import zombie.characters.IsoPlayer;
import zombie.iso.Vector2;

public final class StorySound {
   protected String name = null;
   protected float baseVolume = 1.0F;

   public StorySound(String var1, float var2) {
      this.name = var1;
      this.baseVolume = var2;
   }

   public long playSound() {
      Vector2 var1 = SLSoundManager.getInstance().getRandomBorderPosition();
      return SLSoundManager.Emitter.playSound(this.name, this.baseVolume, var1.x, var1.y, 0.0F, 100.0F, SLSoundManager.getInstance().getRandomBorderRange());
   }

   public long playSound(float var1) {
      return SLSoundManager.Emitter.playSound(this.name, var1, IsoPlayer.getInstance().x, IsoPlayer.getInstance().y, IsoPlayer.getInstance().z, 10.0F, 50.0F);
   }

   public long playSound(float var1, float var2, float var3, float var4, float var5) {
      return this.playSound(this.baseVolume, var1, var2, var3, var4, var5);
   }

   public long playSound(float var1, float var2, float var3, float var4, float var5, float var6) {
      return SLSoundManager.Emitter.playSound(this.name, this.baseVolume * var1, var2, var3, var4, var5, var6);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public float getBaseVolume() {
      return this.baseVolume;
   }

   public void setBaseVolume(float var1) {
      this.baseVolume = var1;
   }

   public StorySound getClone() {
      return new StorySound(this.name, this.baseVolume);
   }
}
