package zombie.iso;

import java.util.ArrayList;
import zombie.GameTime;
import zombie.SandboxOptions;
import zombie.characters.IsoPlayer;
import zombie.core.opengl.RenderSettings;
import zombie.iso.areas.IsoBuilding;

public class IsoLightSource {
   public static int NextID = 1;
   public int ID;
   public int x;
   public int y;
   public int z;
   public float r;
   public float g;
   public float b;
   public float rJNI;
   public float gJNI;
   public float bJNI;
   public int radius;
   public boolean bActive;
   public boolean bWasActive;
   public boolean bActiveJNI;
   public int life = -1;
   public int startlife = -1;
   public IsoBuilding localToBuilding;
   public boolean bHydroPowered = false;
   public ArrayList switches = new ArrayList(0);
   public IsoChunk chunk;
   public Object lightMap;

   public IsoLightSource(int var1, int var2, int var3, float var4, float var5, float var6, int var7) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.r = var4;
      this.g = var5;
      this.b = var6;
      this.radius = var7;
      this.bActive = true;
   }

   public IsoLightSource(int var1, int var2, int var3, float var4, float var5, float var6, int var7, IsoBuilding var8) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.r = var4;
      this.g = var5;
      this.b = var6;
      this.radius = var7;
      this.bActive = true;
      this.localToBuilding = var8;
   }

   public IsoLightSource(int var1, int var2, int var3, float var4, float var5, float var6, int var7, int var8) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.r = var4;
      this.g = var5;
      this.b = var6;
      this.radius = var7;
      this.bActive = true;
      this.startlife = this.life = var8;
   }

   public void update() {
      IsoGridSquare var1 = IsoWorld.instance.CurrentCell.getGridSquare(this.x, this.y, this.z);
      if (!this.bHydroPowered || GameTime.instance.NightsSurvived < SandboxOptions.instance.getElecShutModifier() || var1 != null && var1.haveElectricity()) {
         if (this.bActive) {
            if (this.localToBuilding != null) {
               this.r = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.7F;
               this.g = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.7F;
               this.b = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.7F;
            }

            if (this.life > 0) {
               --this.life;
            }

            if (this.localToBuilding != null && var1 != null) {
               this.r = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.8F * IsoGridSquare.rmod * 0.7F;
               this.g = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.8F * IsoGridSquare.gmod * 0.7F;
               this.b = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.8F * IsoGridSquare.bmod * 0.7F;
            }

            for(int var2 = this.x - this.radius; var2 < this.x + this.radius; ++var2) {
               for(int var3 = this.y - this.radius; var3 < this.y + this.radius; ++var3) {
                  for(int var4 = 0; var4 < 8; ++var4) {
                     var1 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
                     if (var1 != null && (this.localToBuilding == null || this.localToBuilding == var1.getBuilding())) {
                        LosUtil.TestResults var5 = LosUtil.lineClear(var1.getCell(), this.x, this.y, this.z, var1.getX(), var1.getY(), var1.getZ(), false);
                        if (var1.getX() == this.x && var1.getY() == this.y && var1.getZ() == this.z || var5 != LosUtil.TestResults.Blocked) {
                           float var6 = 0.0F;
                           float var7;
                           if (Math.abs(var1.getZ() - this.z) <= 1) {
                              var7 = IsoUtils.DistanceTo((float)this.x, (float)this.y, 0.0F, (float)var1.getX(), (float)var1.getY(), 0.0F);
                           } else {
                              var7 = IsoUtils.DistanceTo((float)this.x, (float)this.y, (float)this.z, (float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
                           }

                           if (!(var7 > (float)this.radius)) {
                              var6 = var7 / (float)this.radius;
                              var6 = 1.0F - var6;
                              var6 *= var6;
                              if (this.life > -1) {
                                 var6 *= (float)this.life / (float)this.startlife;
                              }

                              float var8 = var6 * this.r * 2.0F;
                              float var9 = var6 * this.g * 2.0F;
                              float var10 = var6 * this.b * 2.0F;
                              var1.setLampostTotalR(var1.getLampostTotalR() + var8);
                              var1.setLampostTotalG(var1.getLampostTotalG() + var9);
                              var1.setLampostTotalB(var1.getLampostTotalB() + var10);
                           }
                        }
                     }
                  }
               }
            }

         }
      } else {
         this.bActive = false;
      }
   }

   public int getX() {
      return this.x;
   }

   public void setX(int var1) {
      this.x = var1;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int var1) {
      this.y = var1;
   }

   public int getZ() {
      return this.z;
   }

   public void setZ(int var1) {
      this.z = var1;
   }

   public float getR() {
      return this.r;
   }

   public void setR(float var1) {
      this.r = var1;
   }

   public float getG() {
      return this.g;
   }

   public void setG(float var1) {
      this.g = var1;
   }

   public float getB() {
      return this.b;
   }

   public void setB(float var1) {
      this.b = var1;
   }

   public int getRadius() {
      return this.radius;
   }

   public void setRadius(int var1) {
      this.radius = var1;
   }

   public boolean isActive() {
      return this.bActive;
   }

   public void setActive(boolean var1) {
      this.bActive = var1;
   }

   public boolean wasActive() {
      return this.bWasActive;
   }

   public void setWasActive(boolean var1) {
      this.bWasActive = var1;
   }

   public ArrayList getSwitches() {
      return this.switches;
   }

   public void setSwitches(ArrayList var1) {
      this.switches = var1;
   }

   public void clearInfluence() {
      for(int var1 = this.x - this.radius; var1 < this.x + this.radius; ++var1) {
         for(int var2 = this.y - this.radius; var2 < this.y + this.radius; ++var2) {
            for(int var3 = 0; var3 < 8; ++var3) {
               IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
               if (var4 != null) {
                  var4.setLampostTotalR(0.0F);
                  var4.setLampostTotalG(0.0F);
                  var4.setLampostTotalB(0.0F);
               }
            }
         }
      }

   }

   public boolean isInBounds(int var1, int var2, int var3, int var4) {
      return this.x >= var1 && this.x < var3 && this.y >= var2 && this.y < var4;
   }

   public boolean isInBounds() {
      IsoChunkMap[] var1 = IsoWorld.instance.CurrentCell.ChunkMap;

      for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         if (!var1[var2].ignore) {
            int var3 = var1[var2].getWorldXMinTiles();
            int var4 = var1[var2].getWorldXMaxTiles();
            int var5 = var1[var2].getWorldYMinTiles();
            int var6 = var1[var2].getWorldYMaxTiles();
            if (this.isInBounds(var3, var5, var4, var6)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isHydroPowered() {
      return this.bHydroPowered;
   }

   public IsoBuilding getLocalToBuilding() {
      return this.localToBuilding;
   }
}
