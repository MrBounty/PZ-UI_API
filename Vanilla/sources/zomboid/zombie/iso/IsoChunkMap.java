package zombie.iso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import zombie.GameTime;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.physics.WorldSimulation;
import zombie.core.textures.ColorInfo;
import zombie.core.utils.UpdateLimit;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.iso.areas.IsoRoom;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.ui.TextManager;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.VehicleCache;
import zombie.vehicles.VehicleManager;

public final class IsoChunkMap {
   public static final int LEVELS = 8;
   public static final int ChunksPerWidth = 10;
   public static final HashMap SharedChunks = new HashMap();
   public static int MPWorldXA = 0;
   public static int MPWorldYA = 0;
   public static int MPWorldZA = 0;
   public static int WorldXA = 11702;
   public static int WorldYA = 6896;
   public static int WorldZA = 0;
   public static final int[] SWorldX = new int[4];
   public static final int[] SWorldY = new int[4];
   public static final ConcurrentLinkedQueue chunkStore = new ConcurrentLinkedQueue();
   public static final ReentrantLock bSettingChunk = new ReentrantLock(true);
   private static int StartChunkGridWidth = 13;
   public static int ChunkGridWidth;
   public static int ChunkWidthInTiles;
   private static final ColorInfo inf;
   private static final ArrayList saveList;
   private static final ArrayList splatByType;
   public int PlayerID = 0;
   public boolean ignore = false;
   public int WorldX;
   public int WorldY;
   public final ArrayList filenameServerRequests;
   protected IsoChunk[] chunksSwapB;
   protected IsoChunk[] chunksSwapA;
   boolean bReadBufferA;
   int XMinTiles;
   int YMinTiles;
   int XMaxTiles;
   int YMaxTiles;
   private IsoCell cell;
   private final UpdateLimit checkVehiclesFrequency;

   public IsoChunkMap(IsoCell var1) {
      this.WorldX = tileToChunk(WorldXA);
      this.WorldY = tileToChunk(WorldYA);
      this.filenameServerRequests = new ArrayList();
      this.bReadBufferA = true;
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;
      this.checkVehiclesFrequency = new UpdateLimit(3000L);
      this.cell = var1;
      WorldReuserThread.instance.finished = false;
      this.chunksSwapB = new IsoChunk[ChunkGridWidth * ChunkGridWidth];
      this.chunksSwapA = new IsoChunk[ChunkGridWidth * ChunkGridWidth];
   }

   public static void CalcChunkWidth() {
      if (DebugOptions.instance.WorldChunkMap5x5.getValue()) {
         ChunkGridWidth = 5;
         ChunkWidthInTiles = ChunkGridWidth * 10;
      } else {
         float var0 = (float)Core.getInstance().getScreenWidth();
         float var1 = var0 / 1920.0F;
         if (var1 > 1.0F) {
            var1 = 1.0F;
         }

         ChunkGridWidth = (int)((double)((float)StartChunkGridWidth * var1) * 1.5D);
         if (ChunkGridWidth / 2 * 2 == ChunkGridWidth) {
            ++ChunkGridWidth;
         }

         ChunkWidthInTiles = ChunkGridWidth * 10;
      }
   }

   public static void setWorldStartPos(int var0, int var1) {
      SWorldX[IsoPlayer.getPlayerIndex()] = tileToChunk(var0);
      SWorldY[IsoPlayer.getPlayerIndex()] = tileToChunk(var1);
   }

   public void Dispose() {
      WorldReuserThread.instance.finished = true;
      IsoChunk.loadGridSquare.clear();
      this.chunksSwapA = null;
      this.chunksSwapB = null;
   }

   public void setInitialPos(int var1, int var2) {
      this.WorldX = var1;
      this.WorldY = var2;
      this.XMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMinTiles = -1;
      this.YMaxTiles = -1;
   }

   public void processAllLoadGridSquare() {
      for(IsoChunk var1 = (IsoChunk)IsoChunk.loadGridSquare.poll(); var1 != null; var1 = (IsoChunk)IsoChunk.loadGridSquare.poll()) {
         bSettingChunk.lock();

         try {
            boolean var2 = false;

            for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
               IsoChunkMap var4 = IsoWorld.instance.CurrentCell.ChunkMap[var3];
               if (!var4.ignore && var4.setChunkDirect(var1, false)) {
                  var2 = true;
               }
            }

            if (!var2) {
               WorldReuserThread.instance.addReuseChunk(var1);
            } else {
               var1.doLoadGridsquare();
            }
         } finally {
            bSettingChunk.unlock();
         }
      }

   }

   public void update() {
      int var2 = IsoChunk.loadGridSquare.size();
      if (var2 != 0) {
         var2 = 1 + var2 * 3 / ChunkGridWidth;
      }

      while(true) {
         IsoChunk var1;
         int var4;
         while(var2 > 0) {
            var1 = (IsoChunk)IsoChunk.loadGridSquare.poll();
            if (var1 != null) {
               boolean var3 = false;

               for(var4 = 0; var4 < IsoPlayer.numPlayers; ++var4) {
                  IsoChunkMap var5 = IsoWorld.instance.CurrentCell.ChunkMap[var4];
                  if (!var5.ignore && var5.setChunkDirect(var1, false)) {
                     var3 = true;
                  }
               }

               if (!var3) {
                  WorldReuserThread.instance.addReuseChunk(var1);
                  --var2;
                  continue;
               }

               var1.bLoaded = true;
               bSettingChunk.lock();

               try {
                  var1.doLoadGridsquare();
                  if (GameClient.bClient) {
                     List var10 = VehicleCache.vehicleGet(var1.wx, var1.wy);
                     VehicleManager.instance.sendRequestGetFull(var10);
                  }
               } finally {
                  bSettingChunk.unlock();
               }

               for(var4 = 0; var4 < IsoPlayer.numPlayers; ++var4) {
                  IsoPlayer var11 = IsoPlayer.players[var4];
                  if (var11 != null) {
                     var11.dirtyRecalcGridStackTime = 20.0F;
                  }
               }
            }

            --var2;
         }

         for(int var9 = 0; var9 < ChunkGridWidth; ++var9) {
            for(var4 = 0; var4 < ChunkGridWidth; ++var4) {
               var1 = this.getChunk(var4, var9);
               if (var1 != null) {
                  var1.update();
               }
            }
         }

         if (this.checkVehiclesFrequency.Check() && GameClient.bClient) {
            this.checkVehicles();
         }

         return;
      }
   }

   private void checkVehicles() {
      for(int var1 = 0; var1 < ChunkGridWidth; ++var1) {
         for(int var2 = 0; var2 < ChunkGridWidth; ++var2) {
            IsoChunk var3 = this.getChunk(var2, var1);
            if (var3 != null && var3.bLoaded) {
               List var4 = VehicleCache.vehicleGet(var3.wx, var3.wy);
               if (var4 != null && var3.vehicles.size() != var4.size()) {
                  for(int var5 = 0; var5 < var4.size(); ++var5) {
                     short var6 = ((VehicleCache)var4.get(var5)).id;
                     boolean var7 = false;

                     for(int var8 = 0; var8 < var3.vehicles.size(); ++var8) {
                        if (((BaseVehicle)var3.vehicles.get(var8)).getId() == var6) {
                           var7 = true;
                           break;
                        }
                     }

                     if (!var7 && VehicleManager.instance.getVehicleByID(var6) == null) {
                        VehicleManager.instance.sendRequestGetFull(var6);
                     }
                  }
               }
            }
         }
      }

   }

   public void checkIntegrity() {
      IsoWorld.instance.CurrentCell.ChunkMap[0].XMinTiles = -1;

      for(int var1 = IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMinTiles(); var1 < IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMaxTiles(); ++var1) {
         for(int var2 = IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldYMinTiles(); var2 < IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldYMaxTiles(); ++var2) {
            IsoGridSquare var3 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, 0);
            if (var3 != null && (var3.getX() != var1 || var3.getY() != var2)) {
               int var4 = var1 / 10;
               int var5 = var2 / 10;
               var4 -= IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMin();
               var5 -= IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldYMin();
               IsoChunk var6 = null;
               var6 = new IsoChunk(IsoWorld.instance.CurrentCell);
               var6.refs.add(IsoWorld.instance.CurrentCell.ChunkMap[0]);
               WorldStreamer.instance.addJob(var6, var1 / 10, var2 / 10, false);

               while(!var6.bLoaded) {
                  try {
                     Thread.sleep(13L);
                  } catch (InterruptedException var8) {
                     var8.printStackTrace();
                  }
               }
            }
         }
      }

   }

   public void checkIntegrityThread() {
      IsoWorld.instance.CurrentCell.ChunkMap[0].XMinTiles = -1;

      for(int var1 = IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMinTiles(); var1 < IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMaxTiles(); ++var1) {
         for(int var2 = IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldYMinTiles(); var2 < IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldYMaxTiles(); ++var2) {
            IsoGridSquare var3 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, 0);
            if (var3 != null && (var3.getX() != var1 || var3.getY() != var2)) {
               int var4 = var1 / 10;
               int var5 = var2 / 10;
               var4 -= IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMin();
               var5 -= IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldYMin();
               IsoChunk var6 = new IsoChunk(IsoWorld.instance.CurrentCell);
               var6.refs.add(IsoWorld.instance.CurrentCell.ChunkMap[0]);
               WorldStreamer.instance.addJobInstant(var6, var1, var2, var1 / 10, var2 / 10);
            }

            if (var3 != null) {
            }
         }
      }

   }

   public void LoadChunk(int var1, int var2, int var3, int var4) {
      IsoChunk var5 = null;
      if (SharedChunks.containsKey((var1 << 16) + var2)) {
         var5 = (IsoChunk)SharedChunks.get((var1 << 16) + var2);
         var5.setCache();
         this.setChunk(var3, var4, var5);
         var5.refs.add(this);
      } else {
         var5 = (IsoChunk)chunkStore.poll();
         if (var5 == null) {
            var5 = new IsoChunk(this.cell);
         }

         SharedChunks.put((var1 << 16) + var2, var5);
         var5.refs.add(this);
         WorldStreamer.instance.addJob(var5, var1, var2, false);
      }

   }

   public IsoChunk LoadChunkForLater(int var1, int var2, int var3, int var4) {
      if (!IsoWorld.instance.getMetaGrid().isValidChunk(var1, var2)) {
         return null;
      } else {
         IsoChunk var5;
         if (SharedChunks.containsKey((var1 << 16) + var2)) {
            var5 = (IsoChunk)SharedChunks.get((var1 << 16) + var2);
            if (!var5.refs.contains(this)) {
               var5.refs.add(this);
               var5.lightCheck[this.PlayerID] = true;
            }

            if (!var5.bLoaded) {
               return var5;
            }

            this.setChunk(var3, var4, var5);
         } else {
            var5 = (IsoChunk)chunkStore.poll();
            if (var5 == null) {
               var5 = new IsoChunk(this.cell);
            }

            SharedChunks.put((var1 << 16) + var2, var5);
            var5.refs.add(this);
            WorldStreamer.instance.addJob(var5, var1, var2, true);
         }

         return var5;
      }
   }

   public IsoChunk getChunkForGridSquare(int var1, int var2) {
      var1 = this.gridSquareToTileX(var1);
      var2 = this.gridSquareToTileY(var2);
      return !this.isTileOutOfrange(var1) && !this.isTileOutOfrange(var2) ? this.getChunk(tileToChunk(var1), tileToChunk(var2)) : null;
   }

   public IsoChunk getChunkCurrent(int var1, int var2) {
      if (var1 >= 0 && var1 < ChunkGridWidth && var2 >= 0 && var2 < ChunkGridWidth) {
         return !this.bReadBufferA ? this.chunksSwapA[ChunkGridWidth * var2 + var1] : this.chunksSwapB[ChunkGridWidth * var2 + var1];
      } else {
         return null;
      }
   }

   public void setGridSquare(IsoGridSquare var1, int var2, int var3, int var4) {
      assert var1 == null || var1.x == var2 && var1.y == var3 && var1.z == var4;

      int var5 = this.gridSquareToTileX(var2);
      int var6 = this.gridSquareToTileY(var3);
      if (!this.isTileOutOfrange(var5) && !this.isTileOutOfrange(var6) && !this.isGridSquareOutOfRangeZ(var4)) {
         IsoChunk var7 = this.getChunk(tileToChunk(var5), tileToChunk(var6));
         if (var7 != null) {
            if (var4 > var7.maxLevel) {
               var7.maxLevel = var4;
            }

            var7.setSquare(this.tileToGridSquare(var5), this.tileToGridSquare(var6), var4, var1);
         }
      }
   }

   public IsoGridSquare getGridSquare(int var1, int var2, int var3) {
      var1 = this.gridSquareToTileX(var1);
      var2 = this.gridSquareToTileY(var2);
      return this.getGridSquareDirect(var1, var2, var3);
   }

   public IsoGridSquare getGridSquareDirect(int var1, int var2, int var3) {
      if (!this.isTileOutOfrange(var1) && !this.isTileOutOfrange(var2) && !this.isGridSquareOutOfRangeZ(var3)) {
         IsoChunk var4 = this.getChunk(tileToChunk(var1), tileToChunk(var2));
         return var4 == null ? null : var4.getGridSquare(this.tileToGridSquare(var1), this.tileToGridSquare(var2), var3);
      } else {
         return null;
      }
   }

   private int tileToGridSquare(int var1) {
      return var1 % 10;
   }

   private static int tileToChunk(int var0) {
      return var0 / 10;
   }

   private boolean isTileOutOfrange(int var1) {
      return var1 < 0 || var1 >= this.getWidthInTiles();
   }

   private boolean isGridSquareOutOfRangeZ(int var1) {
      return var1 < 0 || var1 >= 8;
   }

   private int gridSquareToTileX(int var1) {
      int var2 = var1 - (this.WorldX - ChunkGridWidth / 2) * 10;
      return var2;
   }

   private int gridSquareToTileY(int var1) {
      int var2 = var1 - (this.WorldY - ChunkGridWidth / 2) * 10;
      return var2;
   }

   public IsoChunk getChunk(int var1, int var2) {
      if (var1 >= 0 && var1 < ChunkGridWidth && var2 >= 0 && var2 < ChunkGridWidth) {
         return this.bReadBufferA ? this.chunksSwapA[ChunkGridWidth * var2 + var1] : this.chunksSwapB[ChunkGridWidth * var2 + var1];
      } else {
         return null;
      }
   }

   private void setChunk(int var1, int var2, IsoChunk var3) {
      if (!this.bReadBufferA) {
         this.chunksSwapA[ChunkGridWidth * var2 + var1] = var3;
      } else {
         this.chunksSwapB[ChunkGridWidth * var2 + var1] = var3;
      }

   }

   public boolean setChunkDirect(IsoChunk var1, boolean var2) {
      long var3 = System.nanoTime();
      if (var2) {
         bSettingChunk.lock();
      }

      long var5 = System.nanoTime();
      int var7 = var1.wx - this.WorldX;
      int var8 = var1.wy - this.WorldY;
      var7 += ChunkGridWidth / 2;
      var8 += ChunkGridWidth / 2;
      if (var1.jobType == IsoChunk.JobType.Convert) {
         var7 = 0;
         var8 = 0;
      }

      if (!var1.refs.isEmpty() && var7 >= 0 && var8 >= 0 && var7 < ChunkGridWidth && var8 < ChunkGridWidth) {
         try {
            if (this.bReadBufferA) {
               this.chunksSwapA[ChunkGridWidth * var8 + var7] = var1;
            } else {
               this.chunksSwapB[ChunkGridWidth * var8 + var7] = var1;
            }

            var1.bLoaded = true;
            if (var1.jobType == IsoChunk.JobType.None) {
               var1.setCache();
               var1.updateBuildings();
            }

            double var9 = (double)(System.nanoTime() - var5) / 1000000.0D;
            double var11 = (double)(System.nanoTime() - var3) / 1000000.0D;
            if (LightingThread.DebugLockTime && var11 > 10.0D) {
               DebugLog.log("setChunkDirect time " + var9 + "/" + var11 + " ms");
            }
         } finally {
            if (var2) {
               bSettingChunk.unlock();
            }

         }

         return true;
      } else {
         if (var1.refs.contains(this)) {
            var1.refs.remove(this);
            if (var1.refs.isEmpty()) {
               SharedChunks.remove((var1.wx << 16) + var1.wy);
            }
         }

         if (var2) {
            bSettingChunk.unlock();
         }

         return false;
      }
   }

   public void drawDebugChunkMap() {
      int var1 = 64;
      boolean var2 = false;

      for(int var3 = 0; var3 < ChunkGridWidth; ++var3) {
         int var7 = 0;

         for(int var4 = 0; var4 < ChunkGridWidth; ++var4) {
            var7 += 64;
            IsoChunk var5 = this.getChunk(var3, var4);
            if (var5 != null) {
               IsoGridSquare var6 = var5.getGridSquare(0, 0, 0);
               if (var6 == null) {
                  TextManager.instance.DrawString((double)var1, (double)var7, "wx:" + var5.wx + " wy:" + var5.wy);
               }
            }
         }

         var1 += 128;
      }

   }

   private void LoadLeft() {
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;
      this.Left();
      WorldSimulation.instance.scrollGroundLeft(this.PlayerID);
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;

      for(int var1 = -(ChunkGridWidth / 2); var1 <= ChunkGridWidth / 2; ++var1) {
         this.LoadChunkForLater(this.WorldX - ChunkGridWidth / 2, this.WorldY + var1, 0, var1 + ChunkGridWidth / 2);
      }

      this.SwapChunkBuffers();
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;
      this.UpdateCellCache();
      LightingThread.instance.scrollLeft(this.PlayerID);
   }

   public void SwapChunkBuffers() {
      for(int var1 = 0; var1 < ChunkGridWidth * ChunkGridWidth; ++var1) {
         if (this.bReadBufferA) {
            this.chunksSwapA[var1] = null;
         } else {
            this.chunksSwapB[var1] = null;
         }
      }

      this.XMinTiles = this.XMaxTiles = -1;
      this.YMinTiles = this.YMaxTiles = -1;
      this.bReadBufferA = !this.bReadBufferA;
   }

   private void setChunk(int var1, IsoChunk var2) {
      if (!this.bReadBufferA) {
         this.chunksSwapA[var1] = var2;
      } else {
         this.chunksSwapB[var1] = var2;
      }

   }

   private IsoChunk getChunk(int var1) {
      return this.bReadBufferA ? this.chunksSwapA[var1] : this.chunksSwapB[var1];
   }

   private void LoadRight() {
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;
      this.Right();
      WorldSimulation.instance.scrollGroundRight(this.PlayerID);
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;

      for(int var1 = -(ChunkGridWidth / 2); var1 <= ChunkGridWidth / 2; ++var1) {
         this.LoadChunkForLater(this.WorldX + ChunkGridWidth / 2, this.WorldY + var1, ChunkGridWidth - 1, var1 + ChunkGridWidth / 2);
      }

      this.SwapChunkBuffers();
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;
      this.UpdateCellCache();
      LightingThread.instance.scrollRight(this.PlayerID);
   }

   private void LoadUp() {
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;
      this.Up();
      WorldSimulation.instance.scrollGroundUp(this.PlayerID);
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;

      for(int var1 = -(ChunkGridWidth / 2); var1 <= ChunkGridWidth / 2; ++var1) {
         this.LoadChunkForLater(this.WorldX + var1, this.WorldY - ChunkGridWidth / 2, var1 + ChunkGridWidth / 2, 0);
      }

      this.SwapChunkBuffers();
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;
      this.UpdateCellCache();
      LightingThread.instance.scrollUp(this.PlayerID);
   }

   private void LoadDown() {
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;
      this.Down();
      WorldSimulation.instance.scrollGroundDown(this.PlayerID);
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;

      for(int var1 = -(ChunkGridWidth / 2); var1 <= ChunkGridWidth / 2; ++var1) {
         this.LoadChunkForLater(this.WorldX + var1, this.WorldY + ChunkGridWidth / 2, var1 + ChunkGridWidth / 2, ChunkGridWidth - 1);
      }

      this.SwapChunkBuffers();
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;
      this.UpdateCellCache();
      LightingThread.instance.scrollDown(this.PlayerID);
   }

   private void UpdateCellCache() {
      int var1 = this.getWidthInTiles();

      for(int var2 = 0; var2 < var1; ++var2) {
         for(int var3 = 0; var3 < var1; ++var3) {
            for(int var4 = 0; var4 < 8; ++var4) {
               IsoGridSquare var5 = this.getGridSquare(var2 + this.getWorldXMinTiles(), var3 + this.getWorldYMinTiles(), var4);
               IsoWorld.instance.CurrentCell.setCacheGridSquareLocal(var2, var3, var4, var5, this.PlayerID);
            }
         }
      }

   }

   private void Up() {
      for(int var1 = 0; var1 < ChunkGridWidth; ++var1) {
         for(int var2 = ChunkGridWidth - 1; var2 > 0; --var2) {
            IsoChunk var3 = this.getChunk(var1, var2);
            if (var3 == null && var2 == ChunkGridWidth - 1) {
               int var4 = this.WorldX - ChunkGridWidth / 2 + var1;
               int var5 = this.WorldY - ChunkGridWidth / 2 + var2;
               var3 = (IsoChunk)SharedChunks.get((var4 << 16) + var5);
               if (var3 != null) {
                  if (var3.refs.contains(this)) {
                     var3.refs.remove(this);
                     if (var3.refs.isEmpty()) {
                        SharedChunks.remove((var3.wx << 16) + var3.wy);
                     }
                  }

                  var3 = null;
               }
            }

            if (var3 != null && var2 == ChunkGridWidth - 1) {
               var3.refs.remove(this);
               if (var3.refs.isEmpty()) {
                  SharedChunks.remove((var3.wx << 16) + var3.wy);
                  var3.removeFromWorld();
                  ChunkSaveWorker.instance.Add(var3);
               }
            }

            this.setChunk(var1, var2, this.getChunk(var1, var2 - 1));
         }

         this.setChunk(var1, 0, (IsoChunk)null);
      }

      --this.WorldY;
   }

   private void Down() {
      for(int var1 = 0; var1 < ChunkGridWidth; ++var1) {
         for(int var2 = 0; var2 < ChunkGridWidth - 1; ++var2) {
            IsoChunk var3 = this.getChunk(var1, var2);
            if (var3 == null && var2 == 0) {
               int var4 = this.WorldX - ChunkGridWidth / 2 + var1;
               int var5 = this.WorldY - ChunkGridWidth / 2 + var2;
               var3 = (IsoChunk)SharedChunks.get((var4 << 16) + var5);
               if (var3 != null) {
                  if (var3.refs.contains(this)) {
                     var3.refs.remove(this);
                     if (var3.refs.isEmpty()) {
                        SharedChunks.remove((var3.wx << 16) + var3.wy);
                     }
                  }

                  var3 = null;
               }
            }

            if (var3 != null && var2 == 0) {
               var3.refs.remove(this);
               if (var3.refs.isEmpty()) {
                  SharedChunks.remove((var3.wx << 16) + var3.wy);
                  var3.removeFromWorld();
                  ChunkSaveWorker.instance.Add(var3);
               }
            }

            this.setChunk(var1, var2, this.getChunk(var1, var2 + 1));
         }

         this.setChunk(var1, ChunkGridWidth - 1, (IsoChunk)null);
      }

      ++this.WorldY;
   }

   private void Left() {
      for(int var1 = 0; var1 < ChunkGridWidth; ++var1) {
         for(int var2 = ChunkGridWidth - 1; var2 > 0; --var2) {
            IsoChunk var3 = this.getChunk(var2, var1);
            if (var3 == null && var2 == ChunkGridWidth - 1) {
               int var4 = this.WorldX - ChunkGridWidth / 2 + var2;
               int var5 = this.WorldY - ChunkGridWidth / 2 + var1;
               var3 = (IsoChunk)SharedChunks.get((var4 << 16) + var5);
               if (var3 != null) {
                  if (var3.refs.contains(this)) {
                     var3.refs.remove(this);
                     if (var3.refs.isEmpty()) {
                        SharedChunks.remove((var3.wx << 16) + var3.wy);
                     }
                  }

                  var3 = null;
               }
            }

            if (var3 != null && var2 == ChunkGridWidth - 1) {
               var3.refs.remove(this);
               if (var3.refs.isEmpty()) {
                  SharedChunks.remove((var3.wx << 16) + var3.wy);
                  var3.removeFromWorld();
                  ChunkSaveWorker.instance.Add(var3);
               }
            }

            this.setChunk(var2, var1, this.getChunk(var2 - 1, var1));
         }

         this.setChunk(0, var1, (IsoChunk)null);
      }

      --this.WorldX;
   }

   private void Right() {
      for(int var1 = 0; var1 < ChunkGridWidth; ++var1) {
         for(int var2 = 0; var2 < ChunkGridWidth - 1; ++var2) {
            IsoChunk var3 = this.getChunk(var2, var1);
            if (var3 == null && var2 == 0) {
               int var4 = this.WorldX - ChunkGridWidth / 2 + var2;
               int var5 = this.WorldY - ChunkGridWidth / 2 + var1;
               var3 = (IsoChunk)SharedChunks.get((var4 << 16) + var5);
               if (var3 != null) {
                  if (var3.refs.contains(this)) {
                     var3.refs.remove(this);
                     if (var3.refs.isEmpty()) {
                        SharedChunks.remove((var3.wx << 16) + var3.wy);
                     }
                  }

                  var3 = null;
               }
            }

            if (var3 != null && var2 == 0) {
               var3.refs.remove(this);
               if (var3.refs.isEmpty()) {
                  SharedChunks.remove((var3.wx << 16) + var3.wy);
                  var3.removeFromWorld();
                  ChunkSaveWorker.instance.Add(var3);
               }
            }

            this.setChunk(var2, var1, this.getChunk(var2 + 1, var1));
         }

         this.setChunk(ChunkGridWidth - 1, var1, (IsoChunk)null);
      }

      ++this.WorldX;
   }

   public int getWorldXMin() {
      return this.WorldX - ChunkGridWidth / 2;
   }

   public int getWorldYMin() {
      return this.WorldY - ChunkGridWidth / 2;
   }

   public void ProcessChunkPos(IsoGameCharacter var1) {
      int var2 = (int)var1.getX();
      int var3 = (int)var1.getY();
      int var4 = (int)var1.getZ();
      if (IsoPlayer.getInstance() != null && IsoPlayer.getInstance().getVehicle() != null) {
         IsoPlayer var5 = IsoPlayer.getInstance();
         BaseVehicle var6 = var5.getVehicle();
         float var7 = var6.getCurrentSpeedKmHour() / 5.0F;
         var2 += Math.round(var5.getForwardDirection().x * var7);
         var3 += Math.round(var5.getForwardDirection().y * var7);
      }

      var2 /= 10;
      var3 /= 10;
      if (var2 != this.WorldX || var3 != this.WorldY) {
         long var21 = System.nanoTime();
         double var22 = 0.0D;
         bSettingChunk.lock();
         long var9 = System.nanoTime();

         try {
            if (Math.abs(var2 - this.WorldX) < ChunkGridWidth && Math.abs(var3 - this.WorldY) < ChunkGridWidth) {
               if (var2 != this.WorldX) {
                  if (var2 < this.WorldX) {
                     this.LoadLeft();
                  } else {
                     this.LoadRight();
                  }
               } else if (var3 != this.WorldY) {
                  if (var3 < this.WorldY) {
                     this.LoadUp();
                  } else {
                     this.LoadDown();
                  }
               }
            } else {
               if (LightingJNI.init) {
                  LightingJNI.teleport(this.PlayerID, var2 - ChunkGridWidth / 2, var3 - ChunkGridWidth / 2);
               }

               this.Unload();
               IsoPlayer var11 = IsoPlayer.players[this.PlayerID];
               var11.removeFromSquare();
               var11.square = null;
               this.WorldX = var2;
               this.WorldY = var3;
               WorldSimulation.instance.activateChunkMap(this.PlayerID);
               int var12 = this.WorldX - ChunkGridWidth / 2;
               int var13 = this.WorldY - ChunkGridWidth / 2;
               int var14 = this.WorldX + ChunkGridWidth / 2;
               int var15 = this.WorldY + ChunkGridWidth / 2;

               for(int var16 = var12; var16 <= var14; ++var16) {
                  for(int var17 = var13; var17 <= var15; ++var17) {
                     this.LoadChunkForLater(var16, var17, var16 - var12, var17 - var13);
                  }
               }

               this.SwapChunkBuffers();
               this.UpdateCellCache();
               if (!IsoWorld.instance.getCell().getObjectList().contains(var11)) {
                  IsoWorld.instance.getCell().getAddList().add(var11);
               }
            }
         } finally {
            bSettingChunk.unlock();
         }

         var22 = (double)(System.nanoTime() - var9) / 1000000.0D;
         double var23 = (double)(System.nanoTime() - var21) / 1000000.0D;
         if (LightingThread.DebugLockTime && var23 > 10.0D) {
            DebugLog.log("ProcessChunkPos time " + var22 + "/" + var23 + " ms");
         }

      }
   }

   public IsoRoom getRoom(int var1) {
      return null;
   }

   public int getWidthInTiles() {
      return ChunkWidthInTiles;
   }

   public int getWorldXMinTiles() {
      if (this.XMinTiles != -1) {
         return this.XMinTiles;
      } else {
         this.XMinTiles = this.getWorldXMin() * 10;
         return this.XMinTiles;
      }
   }

   public int getWorldYMinTiles() {
      if (this.YMinTiles != -1) {
         return this.YMinTiles;
      } else {
         this.YMinTiles = this.getWorldYMin() * 10;
         return this.YMinTiles;
      }
   }

   public int getWorldXMaxTiles() {
      if (this.XMaxTiles != -1) {
         return this.XMaxTiles;
      } else {
         this.XMaxTiles = this.getWorldXMin() * 10 + this.getWidthInTiles();
         return this.XMaxTiles;
      }
   }

   public int getWorldYMaxTiles() {
      if (this.YMaxTiles != -1) {
         return this.YMaxTiles;
      } else {
         this.YMaxTiles = this.getWorldYMin() * 10 + this.getWidthInTiles();
         return this.YMaxTiles;
      }
   }

   public void Save() {
      if (!GameServer.bServer) {
         for(int var1 = 0; var1 < ChunkGridWidth; ++var1) {
            for(int var2 = 0; var2 < ChunkGridWidth; ++var2) {
               IsoChunk var3 = this.getChunk(var1, var2);
               if (var3 != null && !saveList.contains(var3)) {
                  try {
                     var3.Save(true);
                  } catch (IOException var5) {
                     var5.printStackTrace();
                  }
               }
            }
         }

      }
   }

   public void renderBloodForChunks(int var1) {
      if (DebugOptions.instance.Terrain.RenderTiles.BloodDecals.getValue()) {
         if (!((float)var1 > IsoCamera.CamCharacter.z)) {
            if (Core.OptionBloodDecals != 0) {
               float var2 = (float)GameTime.getInstance().getWorldAgeHours();
               int var3 = IsoCamera.frameState.playerIndex;

               int var4;
               for(var4 = 0; var4 < IsoFloorBloodSplat.FloorBloodTypes.length; ++var4) {
                  ((ArrayList)splatByType.get(var4)).clear();
               }

               for(var4 = 0; var4 < ChunkGridWidth; ++var4) {
                  for(int var5 = 0; var5 < ChunkGridWidth; ++var5) {
                     IsoChunk var6 = this.getChunk(var4, var5);
                     if (var6 != null) {
                        int var7;
                        IsoFloorBloodSplat var8;
                        for(var7 = 0; var7 < var6.FloorBloodSplatsFade.size(); ++var7) {
                           var8 = (IsoFloorBloodSplat)var6.FloorBloodSplatsFade.get(var7);
                           if ((var8.index < 1 || var8.index > 10 || IsoChunk.renderByIndex[Core.OptionBloodDecals - 1][var8.index - 1] != 0) && (int)var8.z == var1 && var8.Type >= 0 && var8.Type < IsoFloorBloodSplat.FloorBloodTypes.length) {
                              var8.chunk = var6;
                              ((ArrayList)splatByType.get(var8.Type)).add(var8);
                           }
                        }

                        if (!var6.FloorBloodSplats.isEmpty()) {
                           for(var7 = 0; var7 < var6.FloorBloodSplats.size(); ++var7) {
                              var8 = (IsoFloorBloodSplat)var6.FloorBloodSplats.get(var7);
                              if ((var8.index < 1 || var8.index > 10 || IsoChunk.renderByIndex[Core.OptionBloodDecals - 1][var8.index - 1] != 0) && (int)var8.z == var1 && var8.Type >= 0 && var8.Type < IsoFloorBloodSplat.FloorBloodTypes.length) {
                                 var8.chunk = var6;
                                 ((ArrayList)splatByType.get(var8.Type)).add(var8);
                              }
                           }
                        }
                     }
                  }
               }

               for(var4 = 0; var4 < splatByType.size(); ++var4) {
                  ArrayList var31 = (ArrayList)splatByType.get(var4);
                  if (!var31.isEmpty()) {
                     String var32 = IsoFloorBloodSplat.FloorBloodTypes[var4];
                     IsoSprite var33 = null;
                     if (!IsoFloorBloodSplat.SpriteMap.containsKey(var32)) {
                        IsoSprite var34 = IsoSprite.CreateSprite(IsoSpriteManager.instance);
                        var34.LoadFramesPageSimple(var32, var32, var32, var32);
                        IsoFloorBloodSplat.SpriteMap.put(var32, var34);
                        var33 = var34;
                     } else {
                        var33 = (IsoSprite)IsoFloorBloodSplat.SpriteMap.get(var32);
                     }

                     for(int var35 = 0; var35 < var31.size(); ++var35) {
                        IsoFloorBloodSplat var9 = (IsoFloorBloodSplat)var31.get(var35);
                        inf.r = 1.0F;
                        inf.g = 1.0F;
                        inf.b = 1.0F;
                        inf.a = 0.27F;
                        float var10 = (var9.x + var9.y / var9.x) * (float)(var9.Type + 1);
                        float var11 = var10 * var9.x / var9.y * (float)(var9.Type + 1) / (var10 + var9.y);
                        float var12 = var11 * var10 * var11 * var9.x / (var9.y + 2.0F);
                        var10 *= 42367.543F;
                        var11 *= 6367.123F;
                        var12 *= 23367.133F;
                        var10 %= 1000.0F;
                        var11 %= 1000.0F;
                        var12 %= 1000.0F;
                        var10 /= 1000.0F;
                        var11 /= 1000.0F;
                        var12 /= 1000.0F;
                        if (var10 > 0.25F) {
                           var10 = 0.25F;
                        }

                        ColorInfo var10000 = inf;
                        var10000.r -= var10 * 2.0F;
                        var10000 = inf;
                        var10000.g -= var10 * 2.0F;
                        var10000 = inf;
                        var10000.b -= var10 * 2.0F;
                        var10000 = inf;
                        var10000.r += var11 / 3.0F;
                        var10000 = inf;
                        var10000.g -= var12 / 3.0F;
                        var10000 = inf;
                        var10000.b -= var12 / 3.0F;
                        float var13 = var2 - var9.worldAge;
                        if (var13 >= 0.0F && var13 < 72.0F) {
                           float var14 = 1.0F - var13 / 72.0F;
                           var10000 = inf;
                           var10000.r *= 0.2F + var14 * 0.8F;
                           var10000 = inf;
                           var10000.g *= 0.2F + var14 * 0.8F;
                           var10000 = inf;
                           var10000.b *= 0.2F + var14 * 0.8F;
                           var10000 = inf;
                           var10000.a *= 0.25F + var14 * 0.75F;
                        } else {
                           var10000 = inf;
                           var10000.r *= 0.2F;
                           var10000 = inf;
                           var10000.g *= 0.2F;
                           var10000 = inf;
                           var10000.b *= 0.2F;
                           var10000 = inf;
                           var10000.a *= 0.25F;
                        }

                        if (var9.fade > 0) {
                           var10000 = inf;
                           var10000.a *= (float)var9.fade / ((float)PerformanceSettings.getLockFPS() * 5.0F);
                           if (--var9.fade == 0) {
                              var9.chunk.FloorBloodSplatsFade.remove(var9);
                           }
                        }

                        IsoGridSquare var36 = var9.chunk.getGridSquare((int)var9.x, (int)var9.y, (int)var9.z);
                        if (var36 != null) {
                           int var15 = var36.getVertLight(0, var3);
                           int var16 = var36.getVertLight(1, var3);
                           int var17 = var36.getVertLight(2, var3);
                           int var18 = var36.getVertLight(3, var3);
                           float var19 = Color.getRedChannelFromABGR(var15);
                           float var20 = Color.getGreenChannelFromABGR(var15);
                           float var21 = Color.getBlueChannelFromABGR(var15);
                           float var22 = Color.getRedChannelFromABGR(var16);
                           float var23 = Color.getGreenChannelFromABGR(var16);
                           float var24 = Color.getBlueChannelFromABGR(var16);
                           float var25 = Color.getRedChannelFromABGR(var17);
                           float var26 = Color.getGreenChannelFromABGR(var17);
                           float var27 = Color.getBlueChannelFromABGR(var17);
                           float var28 = Color.getRedChannelFromABGR(var18);
                           float var29 = Color.getGreenChannelFromABGR(var18);
                           float var30 = Color.getBlueChannelFromABGR(var18);
                           var10000 = inf;
                           var10000.r *= (var19 + var22 + var25 + var28) / 4.0F;
                           var10000 = inf;
                           var10000.g *= (var20 + var23 + var26 + var29) / 4.0F;
                           var10000 = inf;
                           var10000.b *= (var21 + var24 + var27 + var30) / 4.0F;
                        }

                        var33.renderBloodSplat((float)(var9.chunk.wx * 10) + var9.x, (float)(var9.chunk.wy * 10) + var9.y, var9.z, inf);
                     }
                  }
               }

            }
         }
      }
   }

   public void copy(IsoChunkMap var1) {
      IsoChunkMap var2 = this;
      this.WorldX = var1.WorldX;
      this.WorldY = var1.WorldY;
      this.XMinTiles = -1;
      this.YMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMaxTiles = -1;

      for(int var3 = 0; var3 < ChunkGridWidth * ChunkGridWidth; ++var3) {
         var2.bReadBufferA = var1.bReadBufferA;
         if (var2.bReadBufferA) {
            if (var1.chunksSwapA[var3] != null) {
               var1.chunksSwapA[var3].refs.add(var2);
               var2.chunksSwapA[var3] = var1.chunksSwapA[var3];
            }
         } else if (var1.chunksSwapB[var3] != null) {
            var1.chunksSwapB[var3].refs.add(var2);
            var2.chunksSwapB[var3] = var1.chunksSwapB[var3];
         }
      }

   }

   public void Unload() {
      for(int var1 = 0; var1 < ChunkGridWidth; ++var1) {
         for(int var2 = 0; var2 < ChunkGridWidth; ++var2) {
            IsoChunk var3 = this.getChunk(var2, var1);
            if (var3 != null) {
               if (var3.refs.contains(this)) {
                  var3.refs.remove(this);
                  if (var3.refs.isEmpty()) {
                     SharedChunks.remove((var3.wx << 16) + var3.wy);
                     var3.removeFromWorld();
                     ChunkSaveWorker.instance.Add(var3);
                  }
               }

               this.chunksSwapA[var1 * ChunkGridWidth + var2] = null;
               this.chunksSwapB[var1 * ChunkGridWidth + var2] = null;
            }
         }
      }

      WorldSimulation.instance.deactivateChunkMap(this.PlayerID);
      this.XMinTiles = -1;
      this.XMaxTiles = -1;
      this.YMinTiles = -1;
      this.YMaxTiles = -1;
      if (IsoWorld.instance != null && IsoWorld.instance.CurrentCell != null) {
         IsoWorld.instance.CurrentCell.clearCacheGridSquare(this.PlayerID);
      }

   }

   static {
      ChunkGridWidth = StartChunkGridWidth;
      ChunkWidthInTiles = 10 * ChunkGridWidth;
      inf = new ColorInfo();
      saveList = new ArrayList();
      splatByType = new ArrayList();

      for(int var0 = 0; var0 < IsoFloorBloodSplat.FloorBloodTypes.length; ++var0) {
         splatByType.add(new ArrayList());
      }

   }
}
