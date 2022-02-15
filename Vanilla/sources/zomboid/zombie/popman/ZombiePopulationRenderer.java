package zombie.popman;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;
import zombie.MapCollisionData;
import zombie.ZomboidFileSystem;
import zombie.ai.states.WalkTowardState;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.config.BooleanConfigOption;
import zombie.config.ConfigFile;
import zombie.config.ConfigOption;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.math.PZMath;
import zombie.core.textures.Texture;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoMetaCell;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoWorld;
import zombie.iso.LotHeader;
import zombie.iso.RoomDef;
import zombie.network.GameClient;
import zombie.ui.TextManager;
import zombie.ui.UIElement;
import zombie.ui.UIFont;
import zombie.vehicles.VehiclesDB2;

public final class ZombiePopulationRenderer {
   private float xPos;
   private float yPos;
   private float offx;
   private float offy;
   private float zoom;
   private float draww;
   private float drawh;
   private static final int VERSION = 1;
   private final ArrayList options = new ArrayList();
   private ZombiePopulationRenderer.BooleanDebugOption CellGrid = new ZombiePopulationRenderer.BooleanDebugOption("CellGrid", true);
   private ZombiePopulationRenderer.BooleanDebugOption MetaGridBuildings = new ZombiePopulationRenderer.BooleanDebugOption("MetaGrid.Buildings", true);
   private ZombiePopulationRenderer.BooleanDebugOption ZombiesStanding = new ZombiePopulationRenderer.BooleanDebugOption("Zombies.Standing", true);
   private ZombiePopulationRenderer.BooleanDebugOption ZombiesMoving = new ZombiePopulationRenderer.BooleanDebugOption("Zombies.Moving", true);
   private ZombiePopulationRenderer.BooleanDebugOption MCDObstacles = new ZombiePopulationRenderer.BooleanDebugOption("MapCollisionData.Obstacles", true);
   private ZombiePopulationRenderer.BooleanDebugOption MCDRegularChunkOutlines = new ZombiePopulationRenderer.BooleanDebugOption("MapCollisionData.RegularChunkOutlines", true);
   private ZombiePopulationRenderer.BooleanDebugOption MCDRooms = new ZombiePopulationRenderer.BooleanDebugOption("MapCollisionData.Rooms", true);
   private ZombiePopulationRenderer.BooleanDebugOption Vehicles = new ZombiePopulationRenderer.BooleanDebugOption("Vehicles", true);

   private native void n_render(float var1, int var2, int var3, float var4, float var5, int var6, int var7);

   private native void n_setWallFollowerStart(int var1, int var2);

   private native void n_setWallFollowerEnd(int var1, int var2);

   private native void n_wallFollowerMouseMove(int var1, int var2);

   private native void n_setDebugOption(String var1, String var2);

   public float worldToScreenX(float var1) {
      var1 -= this.xPos;
      var1 *= this.zoom;
      var1 += this.offx;
      var1 += this.draww / 2.0F;
      return var1;
   }

   public float worldToScreenY(float var1) {
      var1 -= this.yPos;
      var1 *= this.zoom;
      var1 += this.offy;
      var1 += this.drawh / 2.0F;
      return var1;
   }

   public float uiToWorldX(float var1) {
      var1 -= this.draww / 2.0F;
      var1 /= this.zoom;
      var1 += this.xPos;
      return var1;
   }

   public float uiToWorldY(float var1) {
      var1 -= this.drawh / 2.0F;
      var1 /= this.zoom;
      var1 += this.yPos;
      return var1;
   }

   public void renderString(float var1, float var2, String var3, double var4, double var6, double var8, double var10) {
      float var12 = this.worldToScreenX(var1);
      float var13 = this.worldToScreenY(var2);
      SpriteRenderer.instance.render((Texture)null, var12 - 2.0F, var13 - 2.0F, (float)(TextManager.instance.MeasureStringX(UIFont.Small, var3) + 4), (float)(TextManager.instance.font.getLineHeight() + 4), 0.0F, 0.0F, 0.0F, 0.75F, (Consumer)null);
      TextManager.instance.DrawString((double)var12, (double)var13, var3, var4, var6, var8, var10);
   }

   public void renderRect(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = this.worldToScreenX(var1);
      float var10 = this.worldToScreenY(var2);
      float var11 = this.worldToScreenX(var1 + var3);
      float var12 = this.worldToScreenY(var2 + var4);
      var3 = var11 - var9;
      var4 = var12 - var10;
      if (!(var9 >= this.offx + this.draww) && !(var11 < this.offx) && !(var10 >= this.offy + this.drawh) && !(var12 < this.offy)) {
         SpriteRenderer.instance.render((Texture)null, var9, var10, var3, var4, var5, var6, var7, var8, (Consumer)null);
      }
   }

   public void renderLine(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = this.worldToScreenX(var1);
      float var10 = this.worldToScreenY(var2);
      float var11 = this.worldToScreenX(var3);
      float var12 = this.worldToScreenY(var4);
      if ((!(var9 >= (float)Core.getInstance().getScreenWidth()) || !(var11 >= (float)Core.getInstance().getScreenWidth())) && (!(var10 >= (float)Core.getInstance().getScreenHeight()) || !(var12 >= (float)Core.getInstance().getScreenHeight())) && (!(var9 < 0.0F) || !(var11 < 0.0F)) && (!(var10 < 0.0F) || !(var12 < 0.0F))) {
         SpriteRenderer.instance.renderline((Texture)null, (int)var9, (int)var10, (int)var11, (int)var12, var5, var6, var7, var8);
      }
   }

   public void renderCircle(float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      byte var8 = 32;
      double var9 = (double)var1 + (double)var3 * Math.cos(Math.toRadians((double)(0.0F / (float)var8)));
      double var11 = (double)var2 + (double)var3 * Math.sin(Math.toRadians((double)(0.0F / (float)var8)));

      for(int var13 = 1; var13 <= var8; ++var13) {
         double var14 = (double)var1 + (double)var3 * Math.cos(Math.toRadians((double)((float)var13 * 360.0F / (float)var8)));
         double var16 = (double)var2 + (double)var3 * Math.sin(Math.toRadians((double)((float)var13 * 360.0F / (float)var8)));
         int var18 = (int)this.worldToScreenX((float)var9);
         int var19 = (int)this.worldToScreenY((float)var11);
         int var20 = (int)this.worldToScreenX((float)var14);
         int var21 = (int)this.worldToScreenY((float)var16);
         SpriteRenderer.instance.renderline((Texture)null, var18, var19, var20, var21, var4, var5, var6, var7);
         var9 = var14;
         var11 = var16;
      }

   }

   public void renderZombie(float var1, float var2, float var3, float var4, float var5) {
      float var6 = 1.0F / this.zoom + 0.5F;
      this.renderRect(var1 - var6 / 2.0F, var2 - var6 / 2.0F, var6, var6, var3, var4, var5, 1.0F);
   }

   public void renderVehicle(int var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = 2.0F / this.zoom + 0.5F;
      this.renderRect(var2 - var7 / 2.0F, var3 - var7 / 2.0F, var7, var7, var4, var5, var6, 1.0F);
      this.renderString(var2, var3, String.format("%d", var1), (double)var4, (double)var5, (double)var6, 1.0D);
   }

   public void outlineRect(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.renderLine(var1, var2, var1 + var3, var2, var5, var6, var7, var8);
      this.renderLine(var1 + var3, var2, var1 + var3, var2 + var4, var5, var6, var7, var8);
      this.renderLine(var1, var2 + var4, var1 + var3, var2 + var4, var5, var6, var7, var8);
      this.renderLine(var1, var2, var1, var2 + var4, var5, var6, var7, var8);
   }

   public void renderCellInfo(int var1, int var2, int var3, int var4, float var5) {
      float var6 = this.worldToScreenX((float)(var1 * 300)) + 4.0F;
      float var7 = this.worldToScreenY((float)(var2 * 300)) + 4.0F;
      String var8 = var3 + " / " + var4;
      if (var5 > 0.0F) {
         var8 = var8 + String.format(" %.2f", var5);
      }

      SpriteRenderer.instance.render((Texture)null, var6 - 2.0F, var7 - 2.0F, (float)(TextManager.instance.MeasureStringX(UIFont.Small, var8) + 4), (float)(TextManager.instance.font.getLineHeight() + 4), 0.0F, 0.0F, 0.0F, 0.75F, (Consumer)null);
      TextManager.instance.DrawString((double)var6, (double)var7, var8, 1.0D, 1.0D, 1.0D, 1.0D);
   }

   public void render(UIElement var1, float var2, float var3, float var4) {
      synchronized(MapCollisionData.instance.renderLock) {
         this._render(var1, var2, var3, var4);
      }
   }

   private void _render(UIElement var1, float var2, float var3, float var4) {
      this.draww = (float)var1.getWidth().intValue();
      this.drawh = (float)var1.getHeight().intValue();
      this.xPos = var3;
      this.yPos = var4;
      this.offx = (float)var1.getAbsoluteX().intValue();
      this.offy = (float)var1.getAbsoluteY().intValue();
      this.zoom = var2;
      IsoCell var5 = IsoWorld.instance.CurrentCell;
      IsoChunkMap var6 = IsoWorld.instance.CurrentCell.ChunkMap[0];
      IsoMetaGrid var7 = IsoWorld.instance.MetaGrid;
      IsoMetaCell[][] var8 = var7.Grid;
      int var9 = (int)(this.uiToWorldX(0.0F) / 300.0F) - var7.minX;
      int var10 = (int)(this.uiToWorldY(0.0F) / 300.0F) - var7.minY;
      int var11 = (int)(this.uiToWorldX(this.draww) / 300.0F) + 1 - var7.minX;
      int var12 = (int)(this.uiToWorldY(this.drawh) / 300.0F) + 1 - var7.minY;
      var9 = PZMath.clamp(var9, 0, var7.getWidth() - 1);
      var10 = PZMath.clamp(var10, 0, var7.getHeight() - 1);
      var11 = PZMath.clamp(var11, 0, var7.getWidth() - 1);
      var12 = PZMath.clamp(var12, 0, var7.getHeight() - 1);
      int var13;
      if (this.MetaGridBuildings.getValue()) {
         for(var13 = var9; var13 <= var11; ++var13) {
            for(int var14 = var10; var14 <= var12; ++var14) {
               LotHeader var15 = var8[var13][var14].info;
               if (var15 != null) {
                  for(int var16 = 0; var16 < var15.Buildings.size(); ++var16) {
                     BuildingDef var17 = (BuildingDef)var15.Buildings.get(var16);

                     for(int var18 = 0; var18 < var17.rooms.size(); ++var18) {
                        if (((RoomDef)var17.rooms.get(var18)).level <= 0) {
                           ArrayList var19 = ((RoomDef)var17.rooms.get(var18)).getRects();

                           for(int var20 = 0; var20 < var19.size(); ++var20) {
                              RoomDef.RoomRect var21 = (RoomDef.RoomRect)var19.get(var20);
                              if (var17.bAlarmed) {
                                 this.renderRect((float)var21.getX(), (float)var21.getY(), (float)var21.getW(), (float)var21.getH(), 0.8F, 0.8F, 0.5F, 0.3F);
                              } else {
                                 this.renderRect((float)var21.getX(), (float)var21.getY(), (float)var21.getW(), (float)var21.getH(), 0.5F, 0.5F, 0.8F, 0.3F);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      if (this.CellGrid.getValue()) {
         for(var13 = var10; var13 <= var12; ++var13) {
            this.renderLine((float)(var7.minX * 300), (float)((var7.minY + var13) * 300), (float)((var7.maxX + 1) * 300), (float)((var7.minY + var13) * 300), 1.0F, 1.0F, 1.0F, 0.15F);
         }

         for(var13 = var9; var13 <= var11; ++var13) {
            this.renderLine((float)((var7.minX + var13) * 300), (float)(var7.minY * 300), (float)((var7.minX + var13) * 300), (float)((var7.maxY + 1) * 300), 1.0F, 1.0F, 1.0F, 0.15F);
         }
      }

      for(var13 = 0; var13 < IsoWorld.instance.CurrentCell.getZombieList().size(); ++var13) {
         IsoZombie var22 = (IsoZombie)IsoWorld.instance.CurrentCell.getZombieList().get(var13);
         float var24 = 1.0F;
         float var25 = 1.0F;
         float var26 = 0.0F;
         this.renderZombie(var22.x, var22.y, var24, var25, var26);
         if (var22.getCurrentState() == WalkTowardState.instance()) {
            this.renderLine(var22.x, var22.y, (float)var22.getPathTargetX(), (float)var22.getPathTargetY(), 1.0F, 1.0F, 1.0F, 0.5F);
         }
      }

      for(var13 = 0; var13 < IsoPlayer.numPlayers; ++var13) {
         IsoPlayer var23 = IsoPlayer.players[var13];
         if (var23 != null) {
            this.renderZombie(var23.x, var23.y, 0.0F, 0.5F, 0.0F);
         }
      }

      if (GameClient.bClient) {
         MPDebugInfo.instance.render(this, var2);
      } else {
         if (this.Vehicles.getValue()) {
            VehiclesDB2.instance.renderDebug(this);
         }

         this.n_render(var2, (int)this.offx, (int)this.offy, var3, var4, (int)this.draww, (int)this.drawh);
      }
   }

   public void setWallFollowerStart(int var1, int var2) {
      if (!GameClient.bClient) {
         this.n_setWallFollowerStart(var1, var2);
      }
   }

   public void setWallFollowerEnd(int var1, int var2) {
      if (!GameClient.bClient) {
         this.n_setWallFollowerEnd(var1, var2);
      }
   }

   public void wallFollowerMouseMove(int var1, int var2) {
      if (!GameClient.bClient) {
         this.n_wallFollowerMouseMove(var1, var2);
      }
   }

   public ConfigOption getOptionByName(String var1) {
      for(int var2 = 0; var2 < this.options.size(); ++var2) {
         ConfigOption var3 = (ConfigOption)this.options.get(var2);
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public int getOptionCount() {
      return this.options.size();
   }

   public ConfigOption getOptionByIndex(int var1) {
      return (ConfigOption)this.options.get(var1);
   }

   public void setBoolean(String var1, boolean var2) {
      ConfigOption var3 = this.getOptionByName(var1);
      if (var3 instanceof BooleanConfigOption) {
         ((BooleanConfigOption)var3).setValue(var2);
      }

   }

   public boolean getBoolean(String var1) {
      ConfigOption var2 = this.getOptionByName(var1);
      return var2 instanceof BooleanConfigOption ? ((BooleanConfigOption)var2).getValue() : false;
   }

   public void save() {
      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      String var1 = var10000 + File.separator + "popman-options.ini";
      ConfigFile var2 = new ConfigFile();
      var2.write(var1, 1, this.options);

      for(int var3 = 0; var3 < this.options.size(); ++var3) {
         ConfigOption var4 = (ConfigOption)this.options.get(var3);
         this.n_setDebugOption(var4.getName(), var4.getValueAsString());
      }

   }

   public void load() {
      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      String var1 = var10000 + File.separator + "popman-options.ini";
      ConfigFile var2 = new ConfigFile();
      int var3;
      ConfigOption var4;
      if (var2.read(var1)) {
         for(var3 = 0; var3 < var2.getOptions().size(); ++var3) {
            var4 = (ConfigOption)var2.getOptions().get(var3);
            ConfigOption var5 = this.getOptionByName(var4.getName());
            if (var5 != null) {
               var5.parse(var4.getValueAsString());
            }
         }
      }

      for(var3 = 0; var3 < this.options.size(); ++var3) {
         var4 = (ConfigOption)this.options.get(var3);
         this.n_setDebugOption(var4.getName(), var4.getValueAsString());
      }

   }

   public class BooleanDebugOption extends BooleanConfigOption {
      public BooleanDebugOption(String var2, boolean var3) {
         super(var2, var3);
         ZombiePopulationRenderer.this.options.add(this);
      }
   }
}
