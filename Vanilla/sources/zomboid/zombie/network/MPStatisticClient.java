package zombie.network;

import java.util.Arrays;
import java.util.Iterator;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.network.ByteBufferWriter;
import zombie.iso.IsoUtils;

public class MPStatisticClient {
   public static MPStatisticClient instance = new MPStatisticClient();
   private boolean needUpdate = true;
   private int zombiesLocalOwnership = 0;
   private float zombiesDesyncAVG = 0.0F;
   private float zombiesDesyncMax = 0.0F;
   private int zombiesTeleports = 0;
   private float remotePlayersDesyncAVG = 0.0F;
   private float remotePlayersDesyncMax = 0.0F;
   private int remotePlayersTeleports = 0;
   private float FPS = 0.0F;
   long lastRender = System.currentTimeMillis();
   short FPSAcc = 0;
   private float[] fpsArray = new float[1000];
   private short fpsArrayCount = 0;

   public static MPStatisticClient getInstance() {
      return instance;
   }

   public void incrementZombiesTeleports() {
      ++this.zombiesTeleports;
   }

   public void incrementRemotePlayersTeleports() {
      ++this.remotePlayersTeleports;
   }

   public float getFPS() {
      return this.FPS;
   }

   public void update() {
      if (this.needUpdate) {
         this.needUpdate = false;

         float var3;
         for(int var1 = 0; var1 < GameClient.IDToZombieMap.values().length; ++var1) {
            IsoZombie var2 = (IsoZombie)GameClient.IDToZombieMap.values()[var1];
            if (!var2.isRemoteZombie()) {
               ++this.zombiesLocalOwnership;
            } else {
               var3 = IsoUtils.DistanceTo(var2.x, var2.y, var2.z, var2.realx, var2.realy, (float)var2.realz);
               this.zombiesDesyncAVG += (var3 - this.zombiesDesyncAVG) * 0.05F;
               if (var3 > this.zombiesDesyncMax) {
                  this.zombiesDesyncMax = var3;
               }
            }
         }

         Iterator var4 = GameClient.IDToPlayerMap.values().iterator();

         while(var4.hasNext()) {
            IsoPlayer var5 = (IsoPlayer)var4.next();
            if (!var5.isLocalPlayer()) {
               var3 = IsoUtils.DistanceTo(var5.x, var5.y, var5.z, var5.realx, var5.realy, (float)var5.realz);
               this.remotePlayersDesyncAVG += (var3 - this.remotePlayersDesyncAVG) * 0.05F;
               if (var3 > this.remotePlayersDesyncMax) {
                  this.remotePlayersDesyncMax = var3;
               }
            }
         }
      }

   }

   public void send(ByteBufferWriter var1) {
      var1.putInt(GameClient.IDToZombieMap.size());
      var1.putInt(this.zombiesLocalOwnership);
      var1.putFloat(this.zombiesDesyncAVG);
      var1.putFloat(this.zombiesDesyncMax);
      var1.putInt(this.zombiesTeleports);
      var1.putInt(GameClient.IDToPlayerMap.size());
      var1.putFloat(this.remotePlayersDesyncAVG);
      var1.putFloat(this.remotePlayersDesyncMax);
      var1.putInt(this.remotePlayersTeleports);
      Object var2 = null;
      boolean var3 = false;
      float[] var14;
      short var15;
      synchronized(this.fpsArray) {
         var14 = (float[])this.fpsArray.clone();
         Arrays.fill(this.fpsArray, 0, this.fpsArrayCount, 0.0F);
         var15 = this.fpsArrayCount;
         this.fpsArrayCount = 0;
      }

      float var4 = var14[0];
      float var5 = var14[0];
      float var6 = var14[0];
      short[] var7 = new short[32];
      Arrays.fill(var7, (short)0);

      float var9;
      for(int var8 = 1; var8 < var15; ++var8) {
         var9 = var14[var8];
         if (var4 > var9) {
            var4 = var9;
         }

         if (var6 < var9) {
            var6 = var9;
         }

         var5 += var9;
      }

      var5 /= (float)var15;
      if (var5 < var4 + 16.0F) {
         var4 = var5 - 16.0F;
      }

      if (var6 < var5 + 16.0F) {
         var6 = var5 + 16.0F;
      }

      float var16 = (var5 - var4) / (float)(var7.length / 2);
      var9 = (var6 - var5) / (float)(var7.length / 2);

      int var10;
      for(var10 = 0; var10 < var15; ++var10) {
         float var11 = var14[var10];
         int var12;
         if (var11 < var5) {
            var12 = (int)Math.ceil((double)((var11 - var4) / var16));
            ++var7[var12];
         }

         if (var11 >= var5) {
            var12 = (int)Math.ceil((double)((var11 - var5) / var9)) + var7.length / 2 - 1;
            ++var7[var12];
         }
      }

      var1.putFloat(this.FPS);
      var1.putFloat(var4);
      var1.putFloat(var5);
      var1.putFloat(var6);

      for(var10 = 0; var10 < var7.length; ++var10) {
         var1.putShort(var7[var10]);
      }

      this.zombiesDesyncMax = 0.0F;
      this.zombiesTeleports = 0;
      this.remotePlayersDesyncMax = 0.0F;
      this.remotePlayersTeleports = 0;
      this.zombiesLocalOwnership = 0;
      this.needUpdate = true;
   }

   public void fpsProcess() {
      ++this.FPSAcc;
      long var1 = System.currentTimeMillis();
      if (var1 - this.lastRender >= 1000L) {
         this.FPS = (float)this.FPSAcc;
         this.FPSAcc = 0;
         this.lastRender = var1;
         if (this.fpsArrayCount < this.fpsArray.length) {
            synchronized(this.fpsArray) {
               this.fpsArray[this.fpsArrayCount] = this.FPS;
               ++this.fpsArrayCount;
            }
         }
      }

   }
}
