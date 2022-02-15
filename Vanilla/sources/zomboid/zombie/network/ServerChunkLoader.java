package zombie.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.CRC32;
import zombie.GameTime;
import zombie.ZomboidFileSystem;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.SliceY;
import zombie.iso.WorldReuserThread;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.vehicles.VehiclesDB2;

public class ServerChunkLoader {
   private long debugSlowMapLoadingDelay = 0L;
   private boolean MapLoading = false;
   private ServerChunkLoader.LoaderThread threadLoad = new ServerChunkLoader.LoaderThread();
   private ServerChunkLoader.SaveChunkThread threadSave;
   private final CRC32 crcSave = new CRC32();
   private ServerChunkLoader.RecalcAllThread threadRecalc;

   public ServerChunkLoader() {
      this.threadLoad.setName("LoadChunk");
      this.threadLoad.setDaemon(true);
      this.threadLoad.start();
      this.threadRecalc = new ServerChunkLoader.RecalcAllThread();
      this.threadRecalc.setName("RecalcAll");
      this.threadRecalc.setDaemon(true);
      this.threadRecalc.setPriority(10);
      this.threadRecalc.start();
      this.threadSave = new ServerChunkLoader.SaveChunkThread();
      this.threadSave.setName("SaveChunk");
      this.threadSave.setDaemon(true);
      this.threadSave.start();
   }

   public void addJob(ServerMap.ServerCell var1) {
      this.MapLoading = DebugType.Do(DebugType.MapLoading);
      this.threadLoad.toThread.add(var1);
      MPStatistic.getInstance().LoaderThreadTasks.Added();
   }

   public void getLoaded(ArrayList var1) {
      this.threadLoad.fromThread.drainTo(var1);
   }

   public void quit() {
      this.threadLoad.quit();

      while(this.threadLoad.isAlive()) {
         try {
            Thread.sleep(500L);
         } catch (InterruptedException var3) {
         }
      }

      this.threadSave.quit();

      while(this.threadSave.isAlive()) {
         try {
            Thread.sleep(500L);
         } catch (InterruptedException var2) {
         }
      }

   }

   public void addSaveUnloadedJob(IsoChunk var1) {
      this.threadSave.addUnloadedJob(var1);
   }

   public void addSaveLoadedJob(IsoChunk var1) {
      this.threadSave.addLoadedJob(var1);
   }

   public void saveLater(GameTime var1) {
      this.threadSave.saveLater(var1);
   }

   public void updateSaved() {
      this.threadSave.update();
   }

   public void addRecalcJob(ServerMap.ServerCell var1) {
      this.threadRecalc.toThread.add(var1);
      MPStatistic.getInstance().RecalcThreadTasks.Added();
   }

   public void getRecalc(ArrayList var1) {
      MPStatistic.getInstance().ServerMapLoaded2.Added(this.threadRecalc.fromThread.size());
      this.threadRecalc.fromThread.drainTo(var1);
      MPStatistic.getInstance().RecalcThreadTasks.Processed();
   }

   private class LoaderThread extends Thread {
      private final LinkedBlockingQueue toThread = new LinkedBlockingQueue();
      private final LinkedBlockingQueue fromThread = new LinkedBlockingQueue();
      ArrayDeque isoGridSquareCache = new ArrayDeque();

      public void run() {
         while(true) {
            while(true) {
               try {
                  MPStatistic.getInstance().LoaderThread.End();
                  ServerMap.ServerCell var1 = (ServerMap.ServerCell)this.toThread.take();
                  MPStatistic.getInstance().LoaderThread.Start();
                  if (this.isoGridSquareCache.size() < 10000) {
                     IsoGridSquare.getSquaresForThread(this.isoGridSquareCache, 10000);
                     IsoGridSquare.loadGridSquareCache = this.isoGridSquareCache;
                  }

                  if (var1.WX == -1 && var1.WY == -1) {
                     return;
                  }

                  if (!var1.bCancelLoading) {
                     long var2 = System.nanoTime();

                     for(int var4 = 0; var4 < 5; ++var4) {
                        for(int var5 = 0; var5 < 5; ++var5) {
                           int var6 = var1.WX * 5 + var4;
                           int var7 = var1.WY * 5 + var5;
                           if (IsoWorld.instance.MetaGrid.isValidChunk(var6, var7)) {
                              IsoChunk var8 = (IsoChunk)IsoChunkMap.chunkStore.poll();
                              if (var8 == null) {
                                 var8 = new IsoChunk((IsoCell)null);
                              }

                              ServerChunkLoader.this.threadSave.saveNow(var6, var7);

                              try {
                                 if (var8.LoadOrCreate(var6, var7, (ByteBuffer)null)) {
                                    var8.bLoaded = true;
                                 } else {
                                    ChunkChecksum.setChecksum(var6, var7, 0L);
                                    var8.Blam(var6, var7);
                                    if (var8.LoadBrandNew(var6, var7)) {
                                       var8.bLoaded = true;
                                    }
                                 }

                                 if (var8.bLoaded) {
                                    VehiclesDB2.instance.loadChunk(var8);
                                 }
                              } catch (Exception var10) {
                                 var10.printStackTrace();
                                 LoggerManager.getLogger("map").write(var10);
                              }

                              if (var8.bLoaded) {
                                 var1.chunks[var4][var5] = var8;
                              }
                           }
                        }
                     }

                     if (GameServer.bDebug && ServerChunkLoader.this.debugSlowMapLoadingDelay > 0L) {
                        Thread.sleep(ServerChunkLoader.this.debugSlowMapLoadingDelay);
                     }

                     float var12 = (float)(System.nanoTime() - var2) / 1000000.0F;
                     MPStatistic.getInstance().IncrementLoadCellFromDisk();
                     this.fromThread.add(var1);
                     MPStatistic.getInstance().LoaderThreadTasks.Processed();
                  } else {
                     if (ServerChunkLoader.this.MapLoading) {
                        DebugLog.log(DebugType.MapLoading, "LoaderThread: cancelled " + var1.WX + "," + var1.WY);
                     }

                     var1.bLoadingWasCancelled = true;
                  }
               } catch (Exception var11) {
                  var11.printStackTrace();
                  LoggerManager.getLogger("map").write(var11);
               }
            }
         }
      }

      public void quit() {
         ServerMap.ServerCell var1 = new ServerMap.ServerCell();
         var1.WX = -1;
         var1.WY = -1;
         this.toThread.add(var1);
         MPStatistic.getInstance().LoaderThreadTasks.Added();
      }
   }

   private class RecalcAllThread extends Thread {
      private final LinkedBlockingQueue toThread = new LinkedBlockingQueue();
      private final LinkedBlockingQueue fromThread = new LinkedBlockingQueue();
      private final ServerChunkLoader.GetSquare serverCellGetSquare = ServerChunkLoader.this.new GetSquare();

      public void run() {
         while(true) {
            try {
               this.runInner();
            } catch (Exception var2) {
               var2.printStackTrace();
            }
         }
      }

      private void runInner() throws InterruptedException {
         MPStatistic.getInstance().RecalcAllThread.End();
         ServerMap.ServerCell var1 = (ServerMap.ServerCell)this.toThread.take();
         MPStatistic.getInstance().RecalcAllThread.Start();
         if (var1.bCancelLoading && !this.hasAnyBrandNewChunks(var1)) {
            for(int var18 = 0; var18 < 5; ++var18) {
               for(int var3 = 0; var3 < 5; ++var3) {
                  IsoChunk var19 = var1.chunks[var3][var18];
                  if (var19 != null) {
                     var1.chunks[var3][var18] = null;
                     WorldReuserThread.instance.addReuseChunk(var19);
                  }
               }
            }

            if (ServerChunkLoader.this.MapLoading) {
               DebugLog.log(DebugType.MapLoading, "RecalcAllThread: cancelled " + var1.WX + "," + var1.WY);
            }

            var1.bLoadingWasCancelled = true;
         } else {
            long var2 = System.nanoTime();
            this.serverCellGetSquare.cell = var1;
            int var4 = var1.WX * 50;
            int var5 = var1.WY * 50;
            int var6 = var4 + 50;
            int var7 = var5 + 50;
            int var8 = 0;
            byte var9 = 100;

            int var10;
            int var11;
            IsoChunk var12;
            int var13;
            int var14;
            IsoGridSquare var15;
            for(var10 = 0; var10 < 5; ++var10) {
               for(var11 = 0; var11 < 5; ++var11) {
                  var12 = var1.chunks[var10][var11];
                  if (var12 != null) {
                     var12.bLoaded = false;

                     for(var13 = 0; var13 < var9; ++var13) {
                        for(var14 = 0; var14 <= var12.maxLevel; ++var14) {
                           var15 = var12.squares[var14][var13];
                           if (var14 == 0) {
                              if (var15 == null) {
                                 int var16 = var12.wx * 10 + var13 % 10;
                                 int var17 = var12.wy * 10 + var13 / 10;
                                 var15 = IsoGridSquare.getNew(IsoWorld.instance.CurrentCell, (SliceY)null, var16, var17, var14);
                                 var12.setSquare(var16 % 10, var17 % 10, var14, var15);
                              }

                              if (var15.getFloor() == null) {
                                 DebugLog.log("ERROR: added floor at " + var15.x + "," + var15.y + "," + var15.z + " because there wasn't one");
                                 IsoObject var21 = IsoObject.getNew();
                                 var21.sprite = IsoSprite.getSprite(IsoSpriteManager.instance, (String)"carpentry_02_58", 0);
                                 var21.square = var15;
                                 var15.getObjects().add(0, var21);
                              }
                           }

                           if (var15 != null) {
                              var15.RecalcProperties();
                           }
                        }
                     }

                     if (var12.maxLevel > var8) {
                        var8 = var12.maxLevel;
                     }
                  }
               }
            }

            for(var10 = 0; var10 < 5; ++var10) {
               for(var11 = 0; var11 < 5; ++var11) {
                  var12 = var1.chunks[var10][var11];
                  if (var12 != null) {
                     for(var13 = 0; var13 < var9; ++var13) {
                        for(var14 = 0; var14 <= var12.maxLevel; ++var14) {
                           var15 = var12.squares[var14][var13];
                           if (var15 != null) {
                              if (var14 > 0 && !var15.getObjects().isEmpty()) {
                                 this.serverCellGetSquare.EnsureSurroundNotNull(var15.x - var4, var15.y - var5, var14);
                              }

                              var15.RecalcAllWithNeighbours(true, this.serverCellGetSquare);
                           }
                        }
                     }
                  }
               }
            }

            for(var10 = 0; var10 < 5; ++var10) {
               for(var11 = 0; var11 < 5; ++var11) {
                  var12 = var1.chunks[var10][var11];
                  if (var12 != null) {
                     label149:
                     for(var13 = 0; var13 < var9; ++var13) {
                        for(var14 = var12.maxLevel; var14 > 0; --var14) {
                           var15 = var12.squares[var14][var13];
                           if (var15 != null && var15.Is(IsoFlagType.solidfloor)) {
                              --var14;

                              while(true) {
                                 if (var14 < 0) {
                                    continue label149;
                                 }

                                 var15 = var12.squares[var14][var13];
                                 if (var15 != null) {
                                    var15.haveRoof = true;
                                    var15.getProperties().UnSet(IsoFlagType.exterior);
                                 }

                                 --var14;
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (GameServer.bDebug && ServerChunkLoader.this.debugSlowMapLoadingDelay > 0L) {
               Thread.sleep(ServerChunkLoader.this.debugSlowMapLoadingDelay);
            }

            float var20 = (float)(System.nanoTime() - var2) / 1000000.0F;
            if (ServerChunkLoader.this.MapLoading) {
               DebugLog.log(DebugType.MapLoading, "RecalcAll for cell " + var1.WX + "," + var1.WY + " ms=" + var20);
            }

            this.fromThread.add(var1);
         }
      }

      private boolean hasAnyBrandNewChunks(ServerMap.ServerCell var1) {
         for(int var2 = 0; var2 < 5; ++var2) {
            for(int var3 = 0; var3 < 5; ++var3) {
               IsoChunk var4 = var1.chunks[var3][var2];
               if (var4 != null && !var4.getErosionData().init) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private class SaveChunkThread extends Thread {
      private final LinkedBlockingQueue toThread = new LinkedBlockingQueue();
      private final LinkedBlockingQueue fromThread = new LinkedBlockingQueue();
      private boolean quit = false;
      private final CRC32 crc32 = new CRC32();
      private final ClientChunkRequest ccr = new ClientChunkRequest();
      private final ArrayList toSaveChunk = new ArrayList();
      private final ArrayList savedChunks = new ArrayList();

      public void run() {
         do {
            ServerChunkLoader.SaveTask var1 = null;

            try {
               MPStatistic.getInstance().SaveThread.End();
               var1 = (ServerChunkLoader.SaveTask)this.toThread.take();
               MPStatistic.getInstance().SaveThread.Start();
               MPStatistic.getInstance().IncrementSaveCellToDisk();
               var1.save();
               this.fromThread.add(var1);
               MPStatistic.getInstance().SaveTasks.Processed();
            } catch (InterruptedException var3) {
            } catch (Exception var4) {
               var4.printStackTrace();
               if (var1 != null) {
                  ZLogger var10000 = LoggerManager.getLogger("map");
                  int var10001 = var1.wx();
                  var10000.write("Error saving chunk " + var10001 + "," + var1.wy());
               }

               LoggerManager.getLogger("map").write(var4);
            }
         } while(!this.quit || !this.toThread.isEmpty());

      }

      public void addUnloadedJob(IsoChunk var1) {
         this.toThread.add(ServerChunkLoader.this.new SaveUnloadedTask(var1));
         MPStatistic.getInstance().SaveTasks.SaveUnloadedTasksAdded();
      }

      public void addLoadedJob(IsoChunk var1) {
         ClientChunkRequest.Chunk var2 = this.ccr.getChunk();
         var2.wx = var1.wx;
         var2.wy = var1.wy;
         this.ccr.getByteBuffer(var2);

         try {
            var1.SaveLoadedChunk(var2, this.crc32);
         } catch (Exception var4) {
            var4.printStackTrace();
            LoggerManager.getLogger("map").write(var4);
            this.ccr.releaseChunk(var2);
            return;
         }

         this.toThread.add(ServerChunkLoader.this.new SaveLoadedTask(this.ccr, var2));
         MPStatistic.getInstance().SaveTasks.SaveLoadedTasksAdded();
      }

      public void saveLater(GameTime var1) {
         this.toThread.add(ServerChunkLoader.this.new SaveGameTimeTask(var1));
         MPStatistic.getInstance().SaveTasks.SaveGameTimeTasksAdded();
      }

      public void saveNow(int var1, int var2) {
         this.toSaveChunk.clear();
         this.toThread.drainTo(this.toSaveChunk);

         for(int var3 = 0; var3 < this.toSaveChunk.size(); ++var3) {
            ServerChunkLoader.SaveTask var4 = (ServerChunkLoader.SaveTask)this.toSaveChunk.get(var3);
            if (var4.wx() == var1 && var4.wy() == var2) {
               try {
                  this.toSaveChunk.remove(var3--);
                  var4.save();
                  MPStatistic.getInstance().IncrementServerChunkThreadSaveNow();
               } catch (Exception var6) {
                  var6.printStackTrace();
                  LoggerManager.getLogger("map").write("Error saving chunk " + var1 + "," + var2);
                  LoggerManager.getLogger("map").write(var6);
               }

               MPStatistic.getInstance().SaveTasks.Processed();
               this.fromThread.add(var4);
            }
         }

         this.toThread.addAll(this.toSaveChunk);
      }

      public void quit() {
         this.toThread.add(ServerChunkLoader.this.new QuitThreadTask());
         MPStatistic.getInstance().SaveTasks.QuitThreadTasksAdded();
      }

      public void update() {
         this.savedChunks.clear();
         this.fromThread.drainTo(this.savedChunks);

         for(int var1 = 0; var1 < this.savedChunks.size(); ++var1) {
            ((ServerChunkLoader.SaveTask)this.savedChunks.get(var1)).release();
         }

         this.savedChunks.clear();
      }
   }

   private class GetSquare implements IsoGridSquare.GetSquare {
      ServerMap.ServerCell cell;

      public IsoGridSquare getGridSquare(int var1, int var2, int var3) {
         var1 -= this.cell.WX * 50;
         var2 -= this.cell.WY * 50;
         if (var1 >= 0 && var1 < 50) {
            if (var2 >= 0 && var2 < 50) {
               IsoChunk var4 = this.cell.chunks[var1 / 10][var2 / 10];
               return var4 == null ? null : var4.getGridSquare(var1 % 10, var2 % 10, var3);
            } else {
               return null;
            }
         } else {
            return null;
         }
      }

      public boolean contains(int var1, int var2, int var3) {
         if (var1 >= 0 && var1 < 50) {
            return var2 >= 0 && var2 < 50;
         } else {
            return false;
         }
      }

      public IsoChunk getChunkForSquare(int var1, int var2) {
         var1 -= this.cell.WX * 50;
         var2 -= this.cell.WY * 50;
         if (var1 >= 0 && var1 < 50) {
            return var2 >= 0 && var2 < 50 ? this.cell.chunks[var1 / 10][var2 / 10] : null;
         } else {
            return null;
         }
      }

      public void EnsureSurroundNotNull(int var1, int var2, int var3) {
         int var4 = this.cell.WX * 50;
         int var5 = this.cell.WY * 50;

         for(int var6 = -1; var6 <= 1; ++var6) {
            for(int var7 = -1; var7 <= 1; ++var7) {
               if ((var6 != 0 || var7 != 0) && this.contains(var1 + var6, var2 + var7, var3)) {
                  IsoGridSquare var8 = this.getGridSquare(var4 + var1 + var6, var5 + var2 + var7, var3);
                  if (var8 == null) {
                     var8 = IsoGridSquare.getNew(IsoWorld.instance.CurrentCell, (SliceY)null, var4 + var1 + var6, var5 + var2 + var7, var3);
                     int var9 = (var1 + var6) / 10;
                     int var10 = (var2 + var7) / 10;
                     int var11 = (var1 + var6) % 10;
                     int var12 = (var2 + var7) % 10;
                     if (this.cell.chunks[var9][var10] != null) {
                        this.cell.chunks[var9][var10].setSquare(var11, var12, var3, var8);
                     }
                  }
               }
            }
         }

      }
   }

   private class QuitThreadTask implements ServerChunkLoader.SaveTask {
      public void save() throws Exception {
         ServerChunkLoader.this.threadSave.quit = true;
      }

      public void release() {
      }

      public int wx() {
         return 0;
      }

      public int wy() {
         return 0;
      }
   }

   private class SaveGameTimeTask implements ServerChunkLoader.SaveTask {
      private byte[] bytes;

      public SaveGameTimeTask(GameTime var2) {
         try {
            ByteArrayOutputStream var3 = new ByteArrayOutputStream(32768);

            try {
               DataOutputStream var4 = new DataOutputStream(var3);

               try {
                  var2.save(var4);
                  var4.close();
                  this.bytes = var3.toByteArray();
               } catch (Throwable var9) {
                  try {
                     var4.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }

                  throw var9;
               }

               var4.close();
            } catch (Throwable var10) {
               try {
                  var3.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }

               throw var10;
            }

            var3.close();
         } catch (Exception var11) {
            var11.printStackTrace();
         }
      }

      public void save() throws Exception {
         if (this.bytes != null) {
            File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("map_t.bin");

            try {
               FileOutputStream var2 = new FileOutputStream(var1);

               try {
                  var2.write(this.bytes);
               } catch (Throwable var6) {
                  try {
                     var2.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }

                  throw var6;
               }

               var2.close();
            } catch (Exception var7) {
               var7.printStackTrace();
               return;
            }
         }

      }

      public void release() {
      }

      public int wx() {
         return 0;
      }

      public int wy() {
         return 0;
      }
   }

   private class SaveLoadedTask implements ServerChunkLoader.SaveTask {
      private final ClientChunkRequest ccr;
      private final ClientChunkRequest.Chunk chunk;

      public SaveLoadedTask(ClientChunkRequest var2, ClientChunkRequest.Chunk var3) {
         this.ccr = var2;
         this.chunk = var3;
      }

      public void save() throws Exception {
         long var1 = ChunkChecksum.getChecksumIfExists(this.chunk.wx, this.chunk.wy);
         ServerChunkLoader.this.crcSave.reset();
         ServerChunkLoader.this.crcSave.update(this.chunk.bb.array(), 0, this.chunk.bb.position());
         if (var1 != ServerChunkLoader.this.crcSave.getValue()) {
            ChunkChecksum.setChecksum(this.chunk.wx, this.chunk.wy, ServerChunkLoader.this.crcSave.getValue());
            IsoChunk.SafeWrite("map_", this.chunk.wx, this.chunk.wy, this.chunk.bb);
         }

      }

      public void release() {
         this.ccr.releaseChunk(this.chunk);
      }

      public int wx() {
         return this.chunk.wx;
      }

      public int wy() {
         return this.chunk.wy;
      }
   }

   private class SaveUnloadedTask implements ServerChunkLoader.SaveTask {
      private final IsoChunk chunk;

      public SaveUnloadedTask(IsoChunk var2) {
         this.chunk = var2;
      }

      public void save() throws Exception {
         this.chunk.Save(false);
      }

      public void release() {
         WorldReuserThread.instance.addReuseChunk(this.chunk);
      }

      public int wx() {
         return this.chunk.wx;
      }

      public int wy() {
         return this.chunk.wy;
      }
   }

   private interface SaveTask {
      void save() throws Exception;

      void release();

      int wx();

      int wy();
   }
}
