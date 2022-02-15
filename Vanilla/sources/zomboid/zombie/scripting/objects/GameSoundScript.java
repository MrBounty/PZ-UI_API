package zombie.scripting.objects;

import java.util.Iterator;
import zombie.audio.GameSound;
import zombie.audio.GameSoundClip;
import zombie.scripting.ScriptParser;

public final class GameSoundScript extends BaseScriptObject {
   public final GameSound gameSound = new GameSound();

   public void Load(String var1, String var2) {
      this.gameSound.name = var1;
      ScriptParser.Block var3 = ScriptParser.parse(var2);
      var3 = (ScriptParser.Block)var3.children.get(0);
      Iterator var4 = var3.values.iterator();

      while(var4.hasNext()) {
         ScriptParser.Value var5 = (ScriptParser.Value)var4.next();
         String[] var6 = var5.string.split("=");
         String var7 = var6[0].trim();
         String var8 = var6[1].trim();
         if ("category".equals(var7)) {
            this.gameSound.category = var8;
         } else if ("is3D".equals(var7)) {
            this.gameSound.is3D = Boolean.parseBoolean(var8);
         } else if ("loop".equals(var7)) {
            this.gameSound.loop = Boolean.parseBoolean(var8);
         } else if ("master".equals(var7)) {
            this.gameSound.master = GameSound.MasterVolume.valueOf(var8);
         }
      }

      var4 = var3.children.iterator();

      while(var4.hasNext()) {
         ScriptParser.Block var9 = (ScriptParser.Block)var4.next();
         if ("clip".equals(var9.type)) {
            GameSoundClip var10 = this.LoadClip(var9);
            this.gameSound.clips.add(var10);
         }
      }

   }

   private GameSoundClip LoadClip(ScriptParser.Block var1) {
      GameSoundClip var2 = new GameSoundClip(this.gameSound);
      Iterator var3 = var1.values.iterator();

      while(var3.hasNext()) {
         ScriptParser.Value var4 = (ScriptParser.Value)var3.next();
         String[] var5 = var4.string.split("=");
         String var6 = var5[0].trim();
         String var7 = var5[1].trim();
         if ("distanceMax".equals(var6)) {
            var2.distanceMax = (float)Integer.parseInt(var7);
            var2.initFlags |= GameSoundClip.INIT_FLAG_DISTANCE_MAX;
         } else if ("distanceMin".equals(var6)) {
            var2.distanceMin = (float)Integer.parseInt(var7);
            var2.initFlags |= GameSoundClip.INIT_FLAG_DISTANCE_MIN;
         } else if ("event".equals(var6)) {
            var2.event = var7;
         } else if ("file".equals(var6)) {
            var2.file = var7;
         } else if ("pitch".equals(var6)) {
            var2.pitch = Float.parseFloat(var7);
         } else if ("volume".equals(var6)) {
            var2.volume = Float.parseFloat(var7);
         } else if ("reverbFactor".equals(var6)) {
            var2.reverbFactor = Float.parseFloat(var7);
         } else if ("reverbMaxRange".equals(var6)) {
            var2.reverbMaxRange = Float.parseFloat(var7);
         }
      }

      return var2;
   }

   public void reset() {
      this.gameSound.reset();
   }
}
