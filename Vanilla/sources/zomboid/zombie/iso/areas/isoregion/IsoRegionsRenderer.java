package zombie.iso.areas.isoregion;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import se.krka.kahlua.vm.KahluaTable;
import zombie.MapCollisionData;
import zombie.ZomboidFileSystem;
import zombie.characters.IsoPlayer;
import zombie.config.BooleanConfigOption;
import zombie.config.ConfigFile;
import zombie.config.ConfigOption;
import zombie.core.Color;
import zombie.core.Colors;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.math.PZMath;
import zombie.core.textures.Texture;
import zombie.core.utils.Bits;
import zombie.iso.BuildingDef;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaCell;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoWorld;
import zombie.iso.LotHeader;
import zombie.iso.RoomDef;
import zombie.iso.areas.isoregion.data.DataChunk;
import zombie.iso.areas.isoregion.data.DataRoot;
import zombie.iso.areas.isoregion.regions.IsoChunkRegion;
import zombie.iso.areas.isoregion.regions.IsoWorldRegion;
import zombie.iso.objects.IsoThumpable;
import zombie.ui.TextManager;
import zombie.ui.UIElement;
import zombie.ui.UIFont;

public class IsoRegionsRenderer {
   private final List tempChunkList = new ArrayList();
   private final List debugLines = new ArrayList();
   private float xPos;
   private float yPos;
   private float offx;
   private float offy;
   private float zoom;
   private float draww;
   private float drawh;
   private boolean hasSelected = false;
   private boolean validSelection = false;
   private int selectedX;
   private int selectedY;
   private int selectedZ;
   private final HashSet drawnCells = new HashSet();
   private boolean editSquareInRange = false;
   private int editSquareX;
   private int editSquareY;
   private final ArrayList editOptions = new ArrayList();
   private boolean EditingEnabled = false;
   private final IsoRegionsRenderer.BooleanDebugOption EditWallN;
   private final IsoRegionsRenderer.BooleanDebugOption EditWallW;
   private final IsoRegionsRenderer.BooleanDebugOption EditDoorN;
   private final IsoRegionsRenderer.BooleanDebugOption EditDoorW;
   private final IsoRegionsRenderer.BooleanDebugOption EditFloor;
   private final ArrayList zLevelOptions;
   private final IsoRegionsRenderer.BooleanDebugOption zLevelPlayer;
   private final IsoRegionsRenderer.BooleanDebugOption zLevel0;
   private final IsoRegionsRenderer.BooleanDebugOption zLevel1;
   private final IsoRegionsRenderer.BooleanDebugOption zLevel2;
   private final IsoRegionsRenderer.BooleanDebugOption zLevel3;
   private final IsoRegionsRenderer.BooleanDebugOption zLevel4;
   private final IsoRegionsRenderer.BooleanDebugOption zLevel5;
   private final IsoRegionsRenderer.BooleanDebugOption zLevel6;
   private final IsoRegionsRenderer.BooleanDebugOption zLevel7;
   private static final int VERSION = 1;
   private final ArrayList options;
   private final IsoRegionsRenderer.BooleanDebugOption CellGrid;
   private final IsoRegionsRenderer.BooleanDebugOption MetaGridBuildings;
   private final IsoRegionsRenderer.BooleanDebugOption IsoRegionRender;
   private final IsoRegionsRenderer.BooleanDebugOption IsoRegionRenderChunks;
   private final IsoRegionsRenderer.BooleanDebugOption IsoRegionRenderChunksPlus;

   public IsoRegionsRenderer() {
      this.EditWallN = new IsoRegionsRenderer.BooleanDebugOption(this.editOptions, "Edit.WallN", false);
      this.EditWallW = new IsoRegionsRenderer.BooleanDebugOption(this.editOptions, "Edit.WallW", false);
      this.EditDoorN = new IsoRegionsRenderer.BooleanDebugOption(this.editOptions, "Edit.DoorN", false);
      this.EditDoorW = new IsoRegionsRenderer.BooleanDebugOption(this.editOptions, "Edit.DoorW", false);
      this.EditFloor = new IsoRegionsRenderer.BooleanDebugOption(this.editOptions, "Edit.Floor", false);
      this.zLevelOptions = new ArrayList();
      this.zLevelPlayer = new IsoRegionsRenderer.BooleanDebugOption(this.zLevelOptions, "zLevel.Player", true);
      this.zLevel0 = new IsoRegionsRenderer.BooleanDebugOption(this.zLevelOptions, "zLevel.0", false, 0);
      this.zLevel1 = new IsoRegionsRenderer.BooleanDebugOption(this.zLevelOptions, "zLevel.1", false, 1);
      this.zLevel2 = new IsoRegionsRenderer.BooleanDebugOption(this.zLevelOptions, "zLevel.2", false, 2);
      this.zLevel3 = new IsoRegionsRenderer.BooleanDebugOption(this.zLevelOptions, "zLevel.3", false, 3);
      this.zLevel4 = new IsoRegionsRenderer.BooleanDebugOption(this.zLevelOptions, "zLevel.4", false, 4);
      this.zLevel5 = new IsoRegionsRenderer.BooleanDebugOption(this.zLevelOptions, "zLevel.5", false, 5);
      this.zLevel6 = new IsoRegionsRenderer.BooleanDebugOption(this.zLevelOptions, "zLevel.6", false, 6);
      this.zLevel7 = new IsoRegionsRenderer.BooleanDebugOption(this.zLevelOptions, "zLevel.7", false, 7);
      this.options = new ArrayList();
      this.CellGrid = new IsoRegionsRenderer.BooleanDebugOption(this.options, "CellGrid", true);
      this.MetaGridBuildings = new IsoRegionsRenderer.BooleanDebugOption(this.options, "MetaGrid.Buildings", true);
      this.IsoRegionRender = new IsoRegionsRenderer.BooleanDebugOption(this.options, "IsoRegion.Render", true);
      this.IsoRegionRenderChunks = new IsoRegionsRenderer.BooleanDebugOption(this.options, "IsoRegion.RenderChunks", false);
      this.IsoRegionRenderChunksPlus = new IsoRegionsRenderer.BooleanDebugOption(this.options, "IsoRegion.RenderChunksPlus", false);
   }

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

   public void renderStringUI(float var1, float var2, String var3, Color var4) {
      this.renderStringUI(var1, var2, var3, (double)var4.r, (double)var4.g, (double)var4.b, (double)var4.a);
   }

   public void renderStringUI(float var1, float var2, String var3, double var4, double var6, double var8, double var10) {
      float var12 = this.offx + var1;
      float var13 = this.offy + var2;
      SpriteRenderer.instance.render((Texture)null, var12 - 2.0F, var13 - 2.0F, (float)(TextManager.instance.MeasureStringX(UIFont.Small, var3) + 4), (float)(TextManager.instance.font.getLineHeight() + 4), 0.0F, 0.0F, 0.0F, 0.75F, (Consumer)null);
      TextManager.instance.DrawString((double)var12, (double)var13, var3, var4, var6, var8, var10);
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

   public void renderZombie(float var1, float var2, float var3, float var4, float var5) {
      float var6 = 1.0F / this.zoom + 0.5F;
      this.renderRect(var1 - var6 / 2.0F, var2 - var6 / 2.0F, var6, var6, var3, var4, var5, 1.0F);
   }

   public void renderSquare(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = 1.0F;
      this.renderRect(var1, var2, var7, var7, var3, var4, var5, var6);
   }

   public void renderEntity(float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = var1 / this.zoom + 0.5F;
      this.renderRect(var2 - var8 / 2.0F, var3 - var8 / 2.0F, var8, var8, var4, var5, var6, var7);
   }

   public void render(UIElement var1, float var2, float var3, float var4) {
      synchronized(MapCollisionData.instance.renderLock) {
         this._render(var1, var2, var3, var4);
      }
   }

   private void debugLine(String var1) {
      this.debugLines.add(var1);
   }

   public void recalcSurroundings() {
      IsoRegions.forceRecalcSurroundingChunks();
   }

   public boolean hasChunkRegion(int var1, int var2) {
      int var3 = this.getZLevel();
      DataRoot var4 = IsoRegions.getDataRoot();
      return var4.getIsoChunkRegion(var1, var2, var3) != null;
   }

   public IsoChunkRegion getChunkRegion(int var1, int var2) {
      int var3 = this.getZLevel();
      DataRoot var4 = IsoRegions.getDataRoot();
      return var4.getIsoChunkRegion(var1, var2, var3);
   }

   public void setSelected(int var1, int var2) {
      this.setSelectedWorld((int)this.uiToWorldX((float)var1), (int)this.uiToWorldY((float)var2));
   }

   public void setSelectedWorld(int var1, int var2) {
      this.selectedZ = this.getZLevel();
      this.hasSelected = true;
      this.selectedX = var1;
      this.selectedY = var2;
   }

   public void unsetSelected() {
      this.hasSelected = false;
   }

   public boolean isHasSelected() {
      return this.hasSelected;
   }

   private void _render(UIElement var1, float var2, float var3, float var4) {
      this.debugLines.clear();
      this.drawnCells.clear();
      this.draww = (float)var1.getWidth().intValue();
      this.drawh = (float)var1.getHeight().intValue();
      this.xPos = var3;
      this.yPos = var4;
      this.offx = (float)var1.getAbsoluteX().intValue();
      this.offy = (float)var1.getAbsoluteY().intValue();
      this.zoom = var2;
      this.debugLine("Zoom: " + var2);
      this.debugLine("zLevel: " + this.getZLevel());
      IsoMetaGrid var5 = IsoWorld.instance.MetaGrid;
      IsoMetaCell[][] var6 = var5.Grid;
      int var7 = (int)(this.uiToWorldX(0.0F) / 300.0F) - var5.minX;
      int var8 = (int)(this.uiToWorldY(0.0F) / 300.0F) - var5.minY;
      int var9 = (int)(this.uiToWorldX(this.draww) / 300.0F) + 1 - var5.minX;
      int var10 = (int)(this.uiToWorldY(this.drawh) / 300.0F) + 1 - var5.minY;
      var7 = PZMath.clamp(var7, 0, var5.getWidth() - 1);
      var8 = PZMath.clamp(var8, 0, var5.getHeight() - 1);
      var9 = PZMath.clamp(var9, 0, var5.getWidth() - 1);
      var10 = PZMath.clamp(var10, 0, var5.getHeight() - 1);
      float var11 = Math.max(1.0F - var2 / 2.0F, 0.1F);
      IsoChunkRegion var12 = null;
      IsoWorldRegion var13 = null;
      this.validSelection = false;
      DataRoot var15;
      DataChunk var16;
      int var17;
      int var18;
      float var22;
      if (this.IsoRegionRender.getValue()) {
         IsoPlayer var14 = IsoPlayer.getInstance();
         var15 = IsoRegions.getDataRoot();
         this.tempChunkList.clear();
         var15.getAllChunks(this.tempChunkList);
         this.debugLine("DataChunks: " + this.tempChunkList.size());
         this.debugLine("IsoChunkRegions: " + var15.regionManager.getChunkRegionCount());
         this.debugLine("IsoWorldRegions: " + var15.regionManager.getWorldRegionCount());
         if (this.hasSelected) {
            var12 = var15.getIsoChunkRegion(this.selectedX, this.selectedY, this.selectedZ);
            var13 = var15.getIsoWorldRegion(this.selectedX, this.selectedY, this.selectedZ);
            if (var13 != null && !var13.isEnclosed() && (!this.IsoRegionRenderChunks.getValue() || !this.IsoRegionRenderChunksPlus.getValue())) {
               var13 = null;
               var12 = null;
            }

            if (var12 != null) {
               this.validSelection = true;
            }
         }

         for(int var23 = 0; var23 < this.tempChunkList.size(); ++var23) {
            var16 = (DataChunk)this.tempChunkList.get(var23);
            var17 = var16.getChunkX() * 10;
            var18 = var16.getChunkY() * 10;
            if (var2 > 0.1F) {
               float var19 = this.worldToScreenX((float)var17);
               float var21 = this.worldToScreenY((float)var18);
               float var20 = this.worldToScreenX((float)(var17 + 10));
               var22 = this.worldToScreenY((float)(var18 + 10));
               if (!(var19 >= this.offx + this.draww) && !(var20 < this.offx) && !(var21 >= this.offy + this.drawh) && !(var22 < this.offy)) {
                  this.renderRect((float)var17, (float)var18, 10.0F, 10.0F, 0.0F, var11, 0.0F, 1.0F);
               }
            }
         }
      }

      float var35;
      int var37;
      int var38;
      if (this.MetaGridBuildings.getValue()) {
         var35 = PZMath.clamp(0.3F * (var2 / 5.0F), 0.15F, 0.3F);

         for(var37 = var7; var37 < var9; ++var37) {
            for(var38 = var8; var38 < var10; ++var38) {
               LotHeader var39 = var6[var37][var38].info;
               if (var39 != null) {
                  for(var18 = 0; var18 < var39.Buildings.size(); ++var18) {
                     BuildingDef var42 = (BuildingDef)var39.Buildings.get(var18);

                     for(int var44 = 0; var44 < var42.rooms.size(); ++var44) {
                        if (((RoomDef)var42.rooms.get(var44)).level <= 0) {
                           ArrayList var46 = ((RoomDef)var42.rooms.get(var44)).getRects();

                           for(int var48 = 0; var48 < var46.size(); ++var48) {
                              RoomDef.RoomRect var49 = (RoomDef.RoomRect)var46.get(var48);
                              if (var42.bAlarmed) {
                                 this.renderRect((float)var49.getX(), (float)var49.getY(), (float)var49.getW(), (float)var49.getH(), 0.8F * var35, 0.8F * var35, 0.5F * var35, 1.0F);
                              } else {
                                 this.renderRect((float)var49.getX(), (float)var49.getY(), (float)var49.getW(), (float)var49.getH(), 0.5F * var35, 0.5F * var35, 0.8F * var35, 1.0F);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      int var36;
      if (this.IsoRegionRender.getValue()) {
         var36 = this.getZLevel();
         var15 = IsoRegions.getDataRoot();
         this.tempChunkList.clear();
         var15.getAllChunks(this.tempChunkList);
         float var27 = 1.0F;

         for(int var28 = 0; var28 < this.tempChunkList.size(); ++var28) {
            var16 = (DataChunk)this.tempChunkList.get(var28);
            var17 = var16.getChunkX() * 10;
            var18 = var16.getChunkY() * 10;
            int var29;
            int var30;
            int var31;
            if (var2 <= 0.1F) {
               var29 = var17 / 300;
               var30 = var18 / 300;
               var31 = IsoRegions.hash(var29, var30);
               if (!this.drawnCells.contains(var31)) {
                  this.drawnCells.add(var31);
                  this.renderRect((float)(var29 * 300), (float)(var30 * 300), 300.0F, 300.0F, 0.0F, var11, 0.0F, 1.0F);
               }
            } else if (!(var2 < 1.0F)) {
               var22 = this.worldToScreenX((float)var17);
               float var24 = this.worldToScreenY((float)var18);
               float var50 = this.worldToScreenX((float)(var17 + 10));
               float var25 = this.worldToScreenY((float)(var18 + 10));
               if (!(var22 >= this.offx + this.draww) && !(var50 < this.offx) && !(var24 >= this.offy + this.drawh) && !(var25 < this.offy)) {
                  for(var29 = 0; var29 < 10; ++var29) {
                     for(var30 = 0; var30 < 10; ++var30) {
                        var31 = var36 > 0 ? var36 - 1 : var36;

                        for(int var32 = var31; var32 <= var36; ++var32) {
                           float var33 = var32 < var36 ? 0.25F : 1.0F;
                           byte var26 = var16.getSquare(var29, var30, var32);
                           if (var26 >= 0) {
                              IsoChunkRegion var43 = var16.getIsoChunkRegion(var29, var30, var32);
                              IsoWorldRegion var45;
                              if (var43 != null) {
                                 Color var47;
                                 if (var2 > 6.0F && this.IsoRegionRenderChunks.getValue() && this.IsoRegionRenderChunksPlus.getValue()) {
                                    var47 = var43.getColor();
                                    var27 = 1.0F;
                                    if (var12 != null && var43 != var12) {
                                       var27 = 0.25F;
                                    }

                                    this.renderSquare((float)(var17 + var29), (float)(var18 + var30), var47.r, var47.g, var47.b, var27 * var33);
                                 } else {
                                    var45 = var43.getIsoWorldRegion();
                                    if (var45 != null && var45.isEnclosed()) {
                                       var27 = 1.0F;
                                       if (this.IsoRegionRenderChunks.getValue()) {
                                          var47 = var43.getColor();
                                          if (var12 != null && var43 != var12) {
                                             var27 = 0.25F;
                                          }
                                       } else {
                                          var47 = var45.getColor();
                                          if (var13 != null && var45 != var13) {
                                             var27 = 0.25F;
                                          }
                                       }

                                       this.renderSquare((float)(var17 + var29), (float)(var18 + var30), var47.r, var47.g, var47.b, var27 * var33);
                                    }
                                 }
                              }

                              if (var32 > 0 && var32 == var36) {
                                 var43 = var16.getIsoChunkRegion(var29, var30, var32);
                                 var45 = var43 != null ? var43.getIsoWorldRegion() : null;
                                 boolean var34 = var43 == null || var45 == null || !var45.isEnclosed();
                                 if (var34 && Bits.hasFlags((byte)var26, 16)) {
                                    this.renderSquare((float)(var17 + var29), (float)(var18 + var30), 0.5F, 0.5F, 0.5F, 1.0F);
                                 }
                              }

                              if (Bits.hasFlags((byte)var26, 1) || Bits.hasFlags((byte)var26, 4)) {
                                 this.renderRect((float)(var17 + var29), (float)(var18 + var30), 1.0F, 0.1F, 1.0F, 1.0F, 1.0F, 1.0F * var33);
                              }

                              if (Bits.hasFlags((byte)var26, 2) || Bits.hasFlags((byte)var26, 8)) {
                                 this.renderRect((float)(var17 + var29), (float)(var18 + var30), 0.1F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F * var33);
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
         var35 = 1.0F;
         if (var2 < 0.1F) {
            var35 = Math.max(var2 / 0.1F, 0.25F);
         }

         for(var37 = var8; var37 <= var10; ++var37) {
            this.renderLine((float)(var5.minX * 300), (float)((var5.minY + var37) * 300), (float)((var5.maxX + 1) * 300), (float)((var5.minY + var37) * 300), 1.0F, 1.0F, 1.0F, 0.15F * var35);
            if (var2 > 1.0F) {
               for(var38 = 1; var38 < 30; ++var38) {
                  this.renderLine((float)(var5.minX * 300), (float)((var5.minY + var37) * 300 + var38 * 10), (float)((var5.maxX + 1) * 300), (float)((var5.minY + var37) * 300 + var38 * 10), 1.0F, 1.0F, 1.0F, 0.0325F);
               }
            } else if (var2 > 0.15F) {
               this.renderLine((float)(var5.minX * 300), (float)((var5.minY + var37) * 300 + 100), (float)((var5.maxX + 1) * 300), (float)((var5.minY + var37) * 300 + 100), 1.0F, 1.0F, 1.0F, 0.075F);
               this.renderLine((float)(var5.minX * 300), (float)((var5.minY + var37) * 300 + 200), (float)((var5.maxX + 1) * 300), (float)((var5.minY + var37) * 300 + 200), 1.0F, 1.0F, 1.0F, 0.075F);
            }
         }

         for(var37 = var7; var37 <= var9; ++var37) {
            this.renderLine((float)((var5.minX + var37) * 300), (float)(var5.minY * 300), (float)((var5.minX + var37) * 300), (float)((var5.maxY + 1) * 300), 1.0F, 1.0F, 1.0F, 0.15F * var35);
            if (var2 > 1.0F) {
               for(var38 = 1; var38 < 30; ++var38) {
                  this.renderLine((float)((var5.minX + var37) * 300 + var38 * 10), (float)(var5.minY * 300), (float)((var5.minX + var37) * 300 + var38 * 10), (float)((var5.maxY + 1) * 300), 1.0F, 1.0F, 1.0F, 0.0325F);
               }
            } else if (var2 > 0.15F) {
               this.renderLine((float)((var5.minX + var37) * 300 + 100), (float)(var5.minY * 300), (float)((var5.minX + var37) * 300 + 100), (float)((var5.maxY + 1) * 300), 1.0F, 1.0F, 1.0F, 0.075F);
               this.renderLine((float)((var5.minX + var37) * 300 + 200), (float)(var5.minY * 300), (float)((var5.minX + var37) * 300 + 200), (float)((var5.maxY + 1) * 300), 1.0F, 1.0F, 1.0F, 0.075F);
            }
         }
      }

      for(var36 = 0; var36 < IsoPlayer.numPlayers; ++var36) {
         IsoPlayer var40 = IsoPlayer.players[var36];
         if (var40 != null) {
            this.renderZombie(var40.x, var40.y, 0.0F, 0.5F, 0.0F);
         }
      }

      if (this.isEditingEnabled()) {
         var35 = this.editSquareInRange ? 0.0F : 1.0F;
         float var41 = this.editSquareInRange ? 1.0F : 0.0F;
         if (!this.EditWallN.getValue() && !this.EditDoorN.getValue()) {
            if (!this.EditWallW.getValue() && !this.EditDoorW.getValue()) {
               this.renderRect((float)this.editSquareX, (float)this.editSquareY, 1.0F, 1.0F, var35, var41, 0.0F, 0.5F);
               this.renderRect((float)this.editSquareX, (float)this.editSquareY, 1.0F, 0.05F, var35, var41, 0.0F, 1.0F);
               this.renderRect((float)this.editSquareX, (float)this.editSquareY, 0.05F, 1.0F, var35, var41, 0.0F, 1.0F);
               this.renderRect((float)this.editSquareX, (float)this.editSquareY + 0.95F, 1.0F, 0.05F, var35, var41, 0.0F, 1.0F);
               this.renderRect((float)this.editSquareX + 0.95F, (float)this.editSquareY, 0.05F, 1.0F, var35, var41, 0.0F, 1.0F);
            } else {
               this.renderRect((float)this.editSquareX, (float)this.editSquareY, 0.25F, 1.0F, var35, var41, 0.0F, 0.5F);
               this.renderRect((float)this.editSquareX, (float)this.editSquareY, 0.25F, 0.05F, var35, var41, 0.0F, 1.0F);
               this.renderRect((float)this.editSquareX, (float)this.editSquareY, 0.05F, 1.0F, var35, var41, 0.0F, 1.0F);
               this.renderRect((float)this.editSquareX, (float)this.editSquareY + 0.95F, 0.25F, 0.05F, var35, var41, 0.0F, 1.0F);
               this.renderRect((float)this.editSquareX + 0.2F, (float)this.editSquareY, 0.05F, 1.0F, var35, var41, 0.0F, 1.0F);
            }
         } else {
            this.renderRect((float)this.editSquareX, (float)this.editSquareY, 1.0F, 0.25F, var35, var41, 0.0F, 0.5F);
            this.renderRect((float)this.editSquareX, (float)this.editSquareY, 1.0F, 0.05F, var35, var41, 0.0F, 1.0F);
            this.renderRect((float)this.editSquareX, (float)this.editSquareY, 0.05F, 0.25F, var35, var41, 0.0F, 1.0F);
            this.renderRect((float)this.editSquareX, (float)this.editSquareY + 0.2F, 1.0F, 0.05F, var35, var41, 0.0F, 1.0F);
            this.renderRect((float)this.editSquareX + 0.95F, (float)this.editSquareY, 0.05F, 0.25F, var35, var41, 0.0F, 1.0F);
         }
      }

      if (var12 != null) {
         this.debugLine("- ChunkRegion -");
         this.debugLine("ID: " + var12.getID());
         this.debugLine("Squares: " + var12.getSquareSize());
         this.debugLine("Roofs: " + var12.getRoofCnt());
         this.debugLine("Neighbors: " + var12.getNeighborCount());
         this.debugLine("ConnectedNeighbors: " + var12.getConnectedNeighbors().size());
         this.debugLine("FullyEnclosed: " + var12.getIsEnclosed());
      }

      if (var13 != null) {
         this.debugLine("- WorldRegion -");
         this.debugLine("ID: " + var13.getID());
         this.debugLine("Squares: " + var13.getSquareSize());
         this.debugLine("Roofs: " + var13.getRoofCnt());
         this.debugLine("IsFullyRoofed: " + var13.isFullyRoofed());
         this.debugLine("RoofPercentage: " + var13.getRoofedPercentage());
         this.debugLine("IsEnclosed: " + var13.isEnclosed());
         this.debugLine("Neighbors: " + var13.getNeighbors().size());
         this.debugLine("ChunkRegionCount: " + var13.size());
      }

      var36 = 15;

      for(var37 = 0; var37 < this.debugLines.size(); ++var37) {
         this.renderStringUI(10.0F, (float)var36, (String)this.debugLines.get(var37), Colors.CornFlowerBlue);
         var36 += 18;
      }

   }

   public void setEditSquareCoord(int var1, int var2) {
      this.editSquareX = var1;
      this.editSquareY = var2;
      this.editSquareInRange = false;
      if (this.editCoordInRange(var1, var2)) {
         this.editSquareInRange = true;
      }

   }

   private boolean editCoordInRange(int var1, int var2) {
      IsoGridSquare var3 = IsoWorld.instance.getCell().getGridSquare(var1, var2, 0);
      return var3 != null;
   }

   public void editSquare(int var1, int var2) {
      if (this.isEditingEnabled()) {
         int var3 = this.getZLevel();
         IsoGridSquare var4 = IsoWorld.instance.getCell().getGridSquare(var1, var2, var3);
         DataRoot var5 = IsoRegions.getDataRoot();
         byte var6 = var5.getSquareFlags(var1, var2, var3);
         if (this.editCoordInRange(var1, var2)) {
            if (var4 == null) {
               var4 = IsoWorld.instance.getCell().createNewGridSquare(var1, var2, var3, true);
               if (var4 == null) {
                  return;
               }
            }

            this.editSquareInRange = true;

            for(int var7 = 0; var7 < this.editOptions.size(); ++var7) {
               IsoRegionsRenderer.BooleanDebugOption var8 = (IsoRegionsRenderer.BooleanDebugOption)this.editOptions.get(var7);
               if (var8.getValue()) {
                  String var9 = var8.getName();
                  byte var10 = -1;
                  switch(var9.hashCode()) {
                  case -1465489028:
                     if (var9.equals("Edit.DoorN")) {
                        var10 = 3;
                     }
                     break;
                  case -1465489019:
                     if (var9.equals("Edit.DoorW")) {
                        var10 = 2;
                     }
                     break;
                  case -1463731416:
                     if (var9.equals("Edit.Floor")) {
                        var10 = 4;
                     }
                     break;
                  case -1448362272:
                     if (var9.equals("Edit.WallN")) {
                        var10 = 1;
                     }
                     break;
                  case -1448362263:
                     if (var9.equals("Edit.WallW")) {
                        var10 = 0;
                     }
                  }

                  IsoThumpable var11;
                  switch(var10) {
                  case 0:
                  case 1:
                     if (var8.getName().equals("Edit.WallN")) {
                        if (var6 > 0 && Bits.hasFlags((byte)var6, 1)) {
                           return;
                        }

                        var11 = new IsoThumpable(IsoWorld.instance.getCell(), var4, "walls_exterior_wooden_01_25", true, (KahluaTable)null);
                     } else {
                        if (var6 > 0 && Bits.hasFlags((byte)var6, 2)) {
                           return;
                        }

                        var11 = new IsoThumpable(IsoWorld.instance.getCell(), var4, "walls_exterior_wooden_01_24", true, (KahluaTable)null);
                     }

                     var11.setMaxHealth(100);
                     var11.setName("Wall Debug");
                     var11.setBreakSound("BreakObject");
                     var4.AddSpecialObject(var11);
                     var4.RecalcAllWithNeighbours(true);
                     var11.transmitCompleteItemToServer();
                     if (var4.getZone() != null) {
                        var4.getZone().setHaveConstruction(true);
                     }
                     break;
                  case 2:
                  case 3:
                     if (var8.getName().equals("Edit.DoorN")) {
                        if (var6 > 0 && Bits.hasFlags((byte)var6, 1)) {
                           return;
                        }

                        var11 = new IsoThumpable(IsoWorld.instance.getCell(), var4, "walls_exterior_wooden_01_35", true, (KahluaTable)null);
                     } else {
                        if (var6 > 0 && Bits.hasFlags((byte)var6, 2)) {
                           return;
                        }

                        var11 = new IsoThumpable(IsoWorld.instance.getCell(), var4, "walls_exterior_wooden_01_34", true, (KahluaTable)null);
                     }

                     var11.setMaxHealth(100);
                     var11.setName("Door Frame Debug");
                     var11.setBreakSound("BreakObject");
                     var4.AddSpecialObject(var11);
                     var4.RecalcAllWithNeighbours(true);
                     var11.transmitCompleteItemToServer();
                     if (var4.getZone() != null) {
                        var4.getZone().setHaveConstruction(true);
                     }
                     break;
                  case 4:
                     if (var6 > 0 && Bits.hasFlags((byte)var6, 16)) {
                        return;
                     }

                     if (var3 == 0) {
                        return;
                     }

                     var4.addFloor("carpentry_02_56");
                     if (var4.getZone() != null) {
                        var4.getZone().setHaveConstruction(true);
                     }
                  }
               }
            }
         } else {
            this.editSquareInRange = false;
         }
      }

   }

   public boolean isEditingEnabled() {
      return this.EditingEnabled;
   }

   public void editRotate() {
      if (this.EditWallN.getValue()) {
         this.EditWallN.setValue(false);
         this.EditWallW.setValue(true);
      } else if (this.EditWallW.getValue()) {
         this.EditWallW.setValue(false);
         this.EditWallN.setValue(true);
      }

      if (this.EditDoorN.getValue()) {
         this.EditDoorN.setValue(false);
         this.EditDoorW.setValue(true);
      } else if (this.EditDoorW.getValue()) {
         this.EditDoorW.setValue(false);
         this.EditDoorN.setValue(true);
      }

   }

   public ConfigOption getEditOptionByName(String var1) {
      for(int var2 = 0; var2 < this.editOptions.size(); ++var2) {
         ConfigOption var3 = (ConfigOption)this.editOptions.get(var2);
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public int getEditOptionCount() {
      return this.editOptions.size();
   }

   public ConfigOption getEditOptionByIndex(int var1) {
      return (ConfigOption)this.editOptions.get(var1);
   }

   public void setEditOption(int var1, boolean var2) {
      for(int var3 = 0; var3 < this.editOptions.size(); ++var3) {
         IsoRegionsRenderer.BooleanDebugOption var4 = (IsoRegionsRenderer.BooleanDebugOption)this.editOptions.get(var3);
         if (var3 != var1) {
            var4.setValue(false);
         } else {
            var4.setValue(var2);
            this.EditingEnabled = var2;
         }
      }

   }

   public int getZLevel() {
      if (this.zLevelPlayer.getValue()) {
         return (int)IsoPlayer.getInstance().getZ();
      } else {
         for(int var1 = 0; var1 < this.zLevelOptions.size(); ++var1) {
            IsoRegionsRenderer.BooleanDebugOption var2 = (IsoRegionsRenderer.BooleanDebugOption)this.zLevelOptions.get(var1);
            if (var2.getValue()) {
               return var2.zLevel;
            }
         }

         return 0;
      }
   }

   public ConfigOption getZLevelOptionByName(String var1) {
      for(int var2 = 0; var2 < this.zLevelOptions.size(); ++var2) {
         ConfigOption var3 = (ConfigOption)this.zLevelOptions.get(var2);
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public int getZLevelOptionCount() {
      return this.zLevelOptions.size();
   }

   public ConfigOption getZLevelOptionByIndex(int var1) {
      return (ConfigOption)this.zLevelOptions.get(var1);
   }

   public void setZLevelOption(int var1, boolean var2) {
      for(int var3 = 0; var3 < this.zLevelOptions.size(); ++var3) {
         IsoRegionsRenderer.BooleanDebugOption var4 = (IsoRegionsRenderer.BooleanDebugOption)this.zLevelOptions.get(var3);
         if (var3 != var1) {
            var4.setValue(false);
         } else {
            var4.setValue(var2);
         }
      }

      if (!var2) {
         this.zLevelPlayer.setValue(true);
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
      String var1 = var10000 + File.separator + "isoregions-options.ini";
      ConfigFile var2 = new ConfigFile();
      var2.write(var1, 1, this.options);
   }

   public void load() {
      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      String var1 = var10000 + File.separator + "isoregions-options.ini";
      ConfigFile var2 = new ConfigFile();
      if (var2.read(var1)) {
         for(int var3 = 0; var3 < var2.getOptions().size(); ++var3) {
            ConfigOption var4 = (ConfigOption)var2.getOptions().get(var3);
            ConfigOption var5 = this.getOptionByName(var4.getName());
            if (var5 != null) {
               var5.parse(var4.getValueAsString());
            }
         }
      }

   }

   public static class BooleanDebugOption extends BooleanConfigOption {
      private int index;
      private int zLevel = 0;

      public BooleanDebugOption(ArrayList var1, String var2, boolean var3, int var4) {
         super(var2, var3);
         this.index = var1.size();
         this.zLevel = var4;
         var1.add(this);
      }

      public BooleanDebugOption(ArrayList var1, String var2, boolean var3) {
         super(var2, var3);
         this.index = var1.size();
         var1.add(this);
      }

      public int getIndex() {
         return this.index;
      }
   }
}
