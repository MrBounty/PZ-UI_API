package zombie;

import java.util.ArrayList;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoPlayer;
import zombie.core.Rand;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.iso.RoomDef;
import zombie.iso.Vector2;
import zombie.network.GameServer;

public final class AmbientSoundManager extends BaseAmbientStreamManager {
   public final ArrayList ambient = new ArrayList();
   private final Vector2 tempo = new Vector2();
   public boolean initialized = false;

   public void update() {
      if (this.initialized) {
         this.doOneShotAmbients();
      }
   }

   public void addAmbient(String var1, int var2, int var3, int var4, float var5) {
   }

   public void addAmbientEmitter(float var1, float var2, int var3, String var4) {
   }

   public void addDaytimeAmbientEmitter(float var1, float var2, int var3, String var4) {
   }

   public void doOneShotAmbients() {
      for(int var1 = 0; var1 < this.ambient.size(); ++var1) {
         AmbientSoundManager.Ambient var2 = (AmbientSoundManager.Ambient)this.ambient.get(var1);
         if (var2.finished()) {
            DebugLog.log(DebugType.Sound, "ambient: removing ambient sound " + var2.name);
            this.ambient.remove(var1--);
         } else {
            var2.update();
         }
      }

   }

   public void init() {
      if (!this.initialized) {
         this.initialized = true;
      }
   }

   public void addBlend(String var1, float var2, boolean var3, boolean var4, boolean var5, boolean var6) {
   }

   protected void addRandomAmbient() {
      if (!GameServer.Players.isEmpty()) {
         IsoPlayer var1 = (IsoPlayer)GameServer.Players.get(Rand.Next(GameServer.Players.size()));
         if (var1 != null) {
            String var2 = null;
            if (GameTime.instance.getHour() > 7 && GameTime.instance.getHour() < 21) {
               switch(Rand.Next(3)) {
               case 0:
                  if (Rand.Next(10) < 2) {
                     var2 = "MetaDogBark";
                  }
                  break;
               case 1:
                  if (Rand.Next(10) < 3) {
                     var2 = "MetaScream";
                  }
               }
            } else {
               switch(Rand.Next(5)) {
               case 0:
                  if (Rand.Next(10) < 2) {
                     var2 = "MetaDogBark";
                  }
                  break;
               case 1:
                  if (Rand.Next(13) < 3) {
                     var2 = "MetaScream";
                  }
                  break;
               case 2:
                  var2 = "MetaOwl";
                  break;
               case 3:
                  var2 = "MetaWolfHowl";
               }
            }

            if (var2 != null) {
               float var3 = var1.x;
               float var4 = var1.y;
               double var5 = (double)Rand.Next(-3.1415927F, 3.1415927F);
               this.tempo.x = (float)Math.cos(var5);
               this.tempo.y = (float)Math.sin(var5);
               this.tempo.setLength(1000.0F);
               var3 += this.tempo.x;
               var4 += this.tempo.y;
               AmbientSoundManager.Ambient var7 = new AmbientSoundManager.Ambient(var2, var3, var4, 50.0F, Rand.Next(0.2F, 0.5F));
               this.ambient.add(var7);
               GameServer.sendAmbient(var2, (int)var3, (int)var4, 50, Rand.Next(0.2F, 0.5F));
            }
         }
      }
   }

   public void doGunEvent() {
      ArrayList var1 = GameServer.getPlayers();
      if (!var1.isEmpty()) {
         IsoPlayer var2 = (IsoPlayer)var1.get(Rand.Next(var1.size()));
         String var3 = null;
         float var4 = var2.x;
         float var5 = var2.y;
         short var6 = 600;
         double var7 = (double)Rand.Next(-3.1415927F, 3.1415927F);
         this.tempo.x = (float)Math.cos(var7);
         this.tempo.y = (float)Math.sin(var7);
         this.tempo.setLength((float)(var6 - 100));
         var4 += this.tempo.x;
         var5 += this.tempo.y;
         WorldSoundManager.instance.addSound((Object)null, (int)var4 + Rand.Next(-10, 10), (int)var5 + Rand.Next(-10, 10), 0, 600, 600);
         switch(Rand.Next(6)) {
         case 0:
            var3 = "MetaAssaultRifle1";
            break;
         case 1:
            var3 = "MetaPistol1";
            break;
         case 2:
            var3 = "MetaShotgun1";
            break;
         case 3:
            var3 = "MetaPistol2";
            break;
         case 4:
            var3 = "MetaPistol3";
            break;
         case 5:
            var3 = "MetaShotgun1";
         }

         float var9 = 1.0F;
         AmbientSoundManager.Ambient var10 = new AmbientSoundManager.Ambient(var3, var4, var5, 700.0F, var9);
         this.ambient.add(var10);
         GameServer.sendAmbient(var3, (int)var4, (int)var5, (int)Math.ceil((double)var10.radius), var10.volume);
      }
   }

   public void doAlarm(RoomDef var1) {
      if (var1 != null && var1.building != null && var1.building.bAlarmed) {
         float var2 = 1.0F;
         AmbientSoundManager.Ambient var3 = new AmbientSoundManager.Ambient("burglar2", (float)(var1.x + var1.getW() / 2), (float)(var1.y + var1.getH() / 2), 700.0F, var2);
         var3.duration = 49;
         var3.worldSoundDelay = 3;
         var1.building.bAlarmed = false;
         var1.building.setAllExplored(true);
         this.ambient.add(var3);
         GameServer.sendAlarm(var1.x + var1.getW() / 2, var1.y + var1.getH() / 2);
      }

   }

   public void stop() {
      this.ambient.clear();
      this.initialized = false;
   }

   public class Ambient {
      public float x;
      public float y;
      public String name;
      public float radius;
      public float volume;
      long startTime;
      public int duration;
      public int worldSoundDelay = 0;

      public Ambient(String var2, float var3, float var4, float var5, float var6) {
         this.name = var2;
         this.x = var3;
         this.y = var4;
         this.radius = var5;
         this.volume = var6;
         this.startTime = System.currentTimeMillis() / 1000L;
         this.duration = 2;
         this.update();
         LuaEventManager.triggerEvent("OnAmbientSound", var2, var3, var4);
      }

      public boolean finished() {
         long var1 = System.currentTimeMillis() / 1000L;
         return var1 - this.startTime >= (long)this.duration;
      }

      public void update() {
         long var1 = System.currentTimeMillis() / 1000L;
         if (var1 - this.startTime >= (long)this.worldSoundDelay) {
            WorldSoundManager.instance.addSound((Object)null, (int)this.x, (int)this.y, 0, 600, 600);
         }

      }
   }
}
