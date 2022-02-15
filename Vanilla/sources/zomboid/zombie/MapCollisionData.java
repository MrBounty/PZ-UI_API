package zombie;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.gameStates.IngameState;
import zombie.iso.IsoChunk;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoLot;
import zombie.iso.IsoMetaCell;
import zombie.iso.IsoMetaChunk;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoWorld;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.MPStatistic;
import zombie.popman.ZombiePopulationManager;

public final class MapCollisionData {
   public static final MapCollisionData instance = new MapCollisionData();
   public static final byte BIT_SOLID = 1;
   public static final byte BIT_WALLN = 2;
   public static final byte BIT_WALLW = 4;
   public static final byte BIT_WATER = 8;
   public static final byte BIT_ROOM = 16;
   private static final int SQUARES_PER_CHUNK = 10;
   private static final int CHUNKS_PER_CELL = 30;
   private static final int SQUARES_PER_CELL = 300;
   private static int[] curXY = new int[2];
   public final Object renderLock = new Object();
   private final Stack freePathTasks = new Stack();
   private final ConcurrentLinkedQueue pathTaskQueue = new ConcurrentLinkedQueue();
   private final ConcurrentLinkedQueue pathResultQueue = new ConcurrentLinkedQueue();
   private final MapCollisionData.Sync sync = new MapCollisionData.Sync();
   private final byte[] squares = new byte[100];
   private final int SQUARE_UPDATE_SIZE = 9;
   private final ByteBuffer squareUpdateBuffer = ByteBuffer.allocateDirect(1024);
   private boolean bClient;
   private boolean bPaused;
   private boolean bNoSave;
   private MapCollisionData.MCDThread thread;
   private long lastUpdate;

   private static native void n_init(int var0, int var1, int var2, int var3);

   private static native void n_chunkUpdateTask(int var0, int var1, byte[] var2);

   private static native void n_squareUpdateTask(int var0, ByteBuffer var1);

   private static native int n_pathTask(int var0, int var1, int var2, int var3, int[] var4);

   private static native boolean n_hasDataForThread();

   private static native boolean n_shouldWait();

   private static native void n_update();

   private static native void n_save();

   private static native void n_stop();

   private static native void n_setGameState(String var0, boolean var1);

   private static native void n_setGameState(String var0, double var1);

   private static native void n_setGameState(String var0, float var1);

   private static native void n_setGameState(String var0, int var1);

   private static native void n_setGameState(String var0, String var1);

   private static native void n_initMetaGrid(int var0, int var1, int var2, int var3);

   private static native void n_initMetaCell(int var0, int var1, String var2);

   private static native void n_initMetaChunk(int var0, int var1, int var2, int var3, int var4);

   private static void writeToStdErr(String var0) {
      System.err.println(var0);
   }

   public void init(IsoMetaGrid var1) {
      this.bClient = GameClient.bClient;
      if (!this.bClient) {
         int var2 = var1.getMinX();
         int var3 = var1.getMinY();
         int var4 = var1.getWidth();
         int var5 = var1.getHeight();
         n_setGameState("Core.GameMode", Core.getInstance().getGameMode());
         n_setGameState("Core.GameSaveWorld", Core.GameSaveWorld);
         n_setGameState("Core.bLastStand", Core.bLastStand);
         n_setGameState("Core.noSave", this.bNoSave = Core.getInstance().isNoSave());
         n_setGameState("GameWindow.CacheDir", ZomboidFileSystem.instance.getCacheDir());
         n_setGameState("GameWindow.GameModeCacheDir", ZomboidFileSystem.instance.getGameModeCacheDir());
         n_setGameState("GameWindow.SaveDir", ZomboidFileSystem.instance.getSaveDir());
         n_setGameState("SandboxOptions.Distribution", SandboxOptions.instance.Distribution.getValue());
         n_setGameState("SandboxOptions.Zombies", SandboxOptions.instance.Zombies.getValue());
         n_setGameState("World.ZombiesDisabled", IsoWorld.getZombiesDisabled());
         n_setGameState("PAUSED", this.bPaused = true);
         n_initMetaGrid(var2, var3, var4, var5);

         for(int var6 = var3; var6 < var3 + var5; ++var6) {
            for(int var7 = var2; var7 < var2 + var4; ++var7) {
               IsoMetaCell var8 = var1.getCellData(var7, var6);
               n_initMetaCell(var7, var6, (String)IsoLot.InfoFileNames.get("chunkdata_" + var7 + "_" + var6 + ".bin"));
               if (var8 != null) {
                  for(int var9 = 0; var9 < 30; ++var9) {
                     for(int var10 = 0; var10 < 30; ++var10) {
                        IsoMetaChunk var11 = var8.getChunk(var10, var9);
                        if (var11 != null) {
                           n_initMetaChunk(var7, var6, var10, var9, var11.getUnadjustedZombieIntensity());
                        }
                     }
                  }
               }
            }
         }

         n_init(var2, var3, var4, var5);
      }
   }

   public void start() {
      if (!this.bClient) {
         if (this.thread == null) {
            this.thread = new MapCollisionData.MCDThread();
            this.thread.setDaemon(true);
            this.thread.setName("MapCollisionDataJNI");
            if (GameServer.bServer) {
               this.thread.start();
            }

         }
      }
   }

   public void startGame() {
      if (!GameClient.bClient) {
         this.updateMain();
         ZombiePopulationManager.instance.updateMain();
         n_update();
         ZombiePopulationManager.instance.updateThread();
         this.updateMain();
         ZombiePopulationManager.instance.updateMain();
         this.thread.start();
      }
   }

   public void updateMain() {
      if (!this.bClient) {
         for(MapCollisionData.PathTask var1 = (MapCollisionData.PathTask)this.pathResultQueue.poll(); var1 != null; var1 = (MapCollisionData.PathTask)this.pathResultQueue.poll()) {
            var1.result.finished(var1.status, var1.curX, var1.curY);
            var1.release();
         }

         long var3 = System.currentTimeMillis();
         if (var3 - this.lastUpdate > 10000L) {
            this.lastUpdate = var3;
            this.notifyThread();
         }

      }
   }

   public boolean hasDataForThread() {
      if (this.squareUpdateBuffer.position() > 0) {
         try {
            n_squareUpdateTask(this.squareUpdateBuffer.position() / 9, this.squareUpdateBuffer);
         } finally {
            this.squareUpdateBuffer.clear();
         }
      }

      return n_hasDataForThread();
   }

   public void updateGameState() {
      boolean var1 = Core.getInstance().isNoSave();
      if (this.bNoSave != var1) {
         this.bNoSave = var1;
         n_setGameState("Core.noSave", this.bNoSave);
      }

      boolean var2 = GameTime.isGamePaused();
      if (GameWindow.states.current != IngameState.instance) {
         var2 = true;
      }

      if (GameServer.bServer) {
         var2 = IngameState.instance.Paused;
      }

      if (var2 != this.bPaused) {
         this.bPaused = var2;
         n_setGameState("PAUSED", this.bPaused);
      }

   }

   public void notifyThread() {
      synchronized(this.thread.notifier) {
         this.thread.notifier.notify();
      }
   }

   public void addChunkToWorld(IsoChunk var1) {
      if (!this.bClient) {
         for(int var2 = 0; var2 < 10; ++var2) {
            for(int var3 = 0; var3 < 10; ++var3) {
               IsoGridSquare var4 = var1.getGridSquare(var3, var2, 0);
               if (var4 == null) {
                  this.squares[var3 + var2 * 10] = 1;
               } else {
                  byte var5 = 0;
                  if (this.isSolid(var4)) {
                     var5 = (byte)(var5 | 1);
                  }

                  if (this.isBlockedN(var4)) {
                     var5 = (byte)(var5 | 2);
                  }

                  if (this.isBlockedW(var4)) {
                     var5 = (byte)(var5 | 4);
                  }

                  if (this.isWater(var4)) {
                     var5 = (byte)(var5 | 8);
                  }

                  if (this.isRoom(var4)) {
                     var5 = (byte)(var5 | 16);
                  }

                  this.squares[var3 + var2 * 10] = var5;
               }
            }
         }

         n_chunkUpdateTask(var1.wx, var1.wy, this.squares);
      }
   }

   public void removeChunkFromWorld(IsoChunk var1) {
      if (!this.bClient) {
         ;
      }
   }

   public void squareChanged(IsoGridSquare var1) {
      if (!this.bClient) {
         try {
            byte var2 = 0;
            if (this.isSolid(var1)) {
               var2 = (byte)(var2 | 1);
            }

            if (this.isBlockedN(var1)) {
               var2 = (byte)(var2 | 2);
            }

            if (this.isBlockedW(var1)) {
               var2 = (byte)(var2 | 4);
            }

            if (this.isWater(var1)) {
               var2 = (byte)(var2 | 8);
            }

            if (this.isRoom(var1)) {
               var2 = (byte)(var2 | 16);
            }

            this.squareUpdateBuffer.putInt(var1.x);
            this.squareUpdateBuffer.putInt(var1.y);
            this.squareUpdateBuffer.put(var2);
            if (this.squareUpdateBuffer.remaining() < 9) {
               n_squareUpdateTask(this.squareUpdateBuffer.position() / 9, this.squareUpdateBuffer);
               this.squareUpdateBuffer.clear();
            }
         } catch (Exception var3) {
            ExceptionLogger.logException(var3);
         }

      }
   }

   public void save() {
      if (!this.bClient) {
         ZombiePopulationManager.instance.beginSaveRealZombies();
         if (!this.thread.isAlive()) {
            n_save();
            ZombiePopulationManager.instance.save();
         } else {
            this.thread.bSave = true;
            synchronized(this.thread.notifier) {
               this.thread.notifier.notify();
            }

            while(this.thread.bSave) {
               try {
                  Thread.sleep(5L);
               } catch (InterruptedException var3) {
               }
            }

            ZombiePopulationManager.instance.endSaveRealZombies();
         }
      }
   }

   public void stop() {
      if (!this.bClient) {
         this.thread.bStop = true;
         synchronized(this.thread.notifier) {
            this.thread.notifier.notify();
         }

         while(this.thread.isAlive()) {
            try {
               Thread.sleep(5L);
            } catch (InterruptedException var3) {
            }
         }

         n_stop();
         this.thread = null;
         this.pathTaskQueue.clear();
         this.pathResultQueue.clear();
         this.squareUpdateBuffer.clear();
      }
   }

   private boolean isSolid(IsoGridSquare var1) {
      boolean var2 = var1.isSolid() || var1.isSolidTrans();
      if (var1.HasStairs()) {
         var2 = true;
      }

      if (var1.Is(IsoFlagType.water)) {
         var2 = false;
      }

      if (var1.Has(IsoObjectType.tree)) {
         var2 = false;
      }

      return var2;
   }

   private boolean isBlockedN(IsoGridSquare var1) {
      if (var1.Is(IsoFlagType.HoppableN)) {
         return false;
      } else {
         boolean var2 = var1.Is(IsoFlagType.collideN);
         if (var1.Has(IsoObjectType.doorFrN)) {
            var2 = true;
         }

         if (var1.getProperties().Is(IsoFlagType.DoorWallN)) {
            var2 = true;
         }

         if (var1.Has(IsoObjectType.windowFN)) {
            var2 = true;
         }

         if (var1.Is(IsoFlagType.windowN)) {
            var2 = true;
         }

         if (var1.getProperties().Is(IsoFlagType.WindowN)) {
            var2 = true;
         }

         return var2;
      }
   }

   private boolean isBlockedW(IsoGridSquare var1) {
      if (var1.Is(IsoFlagType.HoppableW)) {
         return false;
      } else {
         boolean var2 = var1.Is(IsoFlagType.collideW);
         if (var1.Has(IsoObjectType.doorFrW)) {
            var2 = true;
         }

         if (var1.getProperties().Is(IsoFlagType.DoorWallW)) {
            var2 = true;
         }

         if (var1.Has(IsoObjectType.windowFW)) {
            var2 = true;
         }

         if (var1.Is(IsoFlagType.windowW)) {
            var2 = true;
         }

         if (var1.getProperties().Is(IsoFlagType.WindowW)) {
            var2 = true;
         }

         return var2;
      }
   }

   private boolean isWater(IsoGridSquare var1) {
      boolean var2 = var1.Is(IsoFlagType.water);
      return var2;
   }

   private boolean isRoom(IsoGridSquare var1) {
      return var1.getRoom() != null;
   }

   static class Sync {
      private int fps = 10;
      private long period;
      private long excess;
      private long beforeTime;
      private long overSleepTime;

      Sync() {
         this.period = 1000000000L / (long)this.fps;
         this.beforeTime = System.nanoTime();
         this.overSleepTime = 0L;
      }

      void begin() {
         this.beforeTime = System.nanoTime();
         this.overSleepTime = 0L;
      }

      void startFrame() {
         this.excess = 0L;
      }

      void endFrame() {
         long var1 = System.nanoTime();
         long var3 = var1 - this.beforeTime;
         long var5 = this.period - var3 - this.overSleepTime;
         if (var5 > 0L) {
            try {
               Thread.sleep(var5 / 1000000L);
            } catch (InterruptedException var8) {
            }

            this.overSleepTime = System.nanoTime() - var1 - var5;
         } else {
            this.excess -= var5;
            this.overSleepTime = 0L;
         }

         this.beforeTime = System.nanoTime();
      }
   }

   private final class MCDThread extends Thread {
      public final Object notifier = new Object();
      public boolean bStop;
      public volatile boolean bSave;
      public volatile boolean bWaiting;
      public Queue pathTasks = new ArrayDeque();

      public void run() {
         while(!this.bStop) {
            try {
               this.runInner();
            } catch (Exception var2) {
               ExceptionLogger.logException(var2);
            }
         }

      }

      private void runInner() {
         MPStatistic.getInstance().MapCollisionThread.Start();
         MapCollisionData.this.sync.startFrame();
         synchronized(MapCollisionData.this.renderLock) {
            MapCollisionData.PathTask var2 = (MapCollisionData.PathTask)MapCollisionData.this.pathTaskQueue.poll();

            while(true) {
               if (var2 == null) {
                  if (this.bSave) {
                     MapCollisionData.n_save();
                     ZombiePopulationManager.instance.save();
                     this.bSave = false;
                  }

                  MapCollisionData.n_update();
                  ZombiePopulationManager.instance.updateThread();
                  break;
               }

               var2.execute();
               var2.release();
               var2 = (MapCollisionData.PathTask)MapCollisionData.this.pathTaskQueue.poll();
            }
         }

         MapCollisionData.this.sync.endFrame();
         MPStatistic.getInstance().MapCollisionThread.End();

         while(this.shouldWait()) {
            synchronized(this.notifier) {
               this.bWaiting = true;

               try {
                  this.notifier.wait();
               } catch (InterruptedException var5) {
               }
            }
         }

         this.bWaiting = false;
      }

      private boolean shouldWait() {
         if (!this.bStop && !this.bSave) {
            if (!MapCollisionData.n_shouldWait()) {
               return false;
            } else if (!ZombiePopulationManager.instance.shouldWait()) {
               return false;
            } else {
               return MapCollisionData.this.pathTaskQueue.isEmpty() && this.pathTasks.isEmpty();
            }
         } else {
            return false;
         }
      }
   }

   private final class PathTask {
      public int startX;
      public int startY;
      public int endX;
      public int endY;
      public int curX;
      public int curY;
      public int status;
      public MapCollisionData.IPathResult result;
      public boolean myThread;

      public void init(int var1, int var2, int var3, int var4, MapCollisionData.IPathResult var5) {
         this.startX = var1;
         this.startY = var2;
         this.endX = var3;
         this.endY = var4;
         this.status = 0;
         this.result = var5;
      }

      public void execute() {
         this.status = MapCollisionData.n_pathTask(this.startX, this.startY, this.endX, this.endY, MapCollisionData.curXY);
         this.curX = MapCollisionData.curXY[0];
         this.curY = MapCollisionData.curXY[1];
         if (this.myThread) {
            this.result.finished(this.status, this.curX, this.curY);
         } else {
            MapCollisionData.this.pathResultQueue.add(this);
         }

      }

      public void release() {
         MapCollisionData.this.freePathTasks.push(this);
      }
   }

   public interface IPathResult {
      void finished(int var1, int var2, int var3);
   }
}
