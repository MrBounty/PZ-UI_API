package zombie.vehicles;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import zombie.ZomboidFileSystem;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.iso.IsoChunk;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.WorldStreamer;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.popman.ZombiePopulationRenderer;
import zombie.util.ByteBufferBackedInputStream;
import zombie.util.ByteBufferOutputStream;
import zombie.util.PZSQLUtils;
import zombie.util.Pool;
import zombie.util.PooledObject;
import zombie.util.Type;

public final class VehiclesDB2 {
   public static final int INVALID_ID = -1;
   private static final int MIN_ID = 1;
   public static final VehiclesDB2 instance = new VehiclesDB2();
   private static final ThreadLocal TL_SliceBuffer = ThreadLocal.withInitial(() -> {
      return ByteBuffer.allocate(32768);
   });
   private static final ThreadLocal TL_Bytes = ThreadLocal.withInitial(() -> {
      return new byte[1024];
   });
   private final VehiclesDB2.MainThread m_main = new VehiclesDB2.MainThread();
   private final VehiclesDB2.WorldStreamerThread m_worldStreamer = new VehiclesDB2.WorldStreamerThread();

   public void init() {
      this.m_worldStreamer.m_store.init(this.m_main.m_usedIDs, this.m_main.m_seenChunks);
   }

   public void Reset() {
      assert WorldStreamer.instance.worldStreamer == null;

      this.updateWorldStreamer();

      for(VehiclesDB2.QueueItem var1 = (VehiclesDB2.QueueItem)this.m_main.m_queue.poll(); var1 != null; var1 = (VehiclesDB2.QueueItem)this.m_main.m_queue.poll()) {
         var1.release();
      }

      this.m_main.Reset();
      this.m_worldStreamer.Reset();
   }

   public void updateMain() throws IOException {
      this.m_main.update();
   }

   public void updateWorldStreamer() {
      this.m_worldStreamer.update();
   }

   public void setForceSave() {
      this.m_main.m_forceSave = true;
   }

   public void renderDebug(ZombiePopulationRenderer var1) {
   }

   public void setChunkSeen(int var1, int var2) {
      this.m_main.setChunkSeen(var1, var2);
   }

   public boolean isChunkSeen(int var1, int var2) {
      return this.m_main.isChunkSeen(var1, var2);
   }

   public void setVehicleLoaded(BaseVehicle var1) {
      this.m_main.setVehicleLoaded(var1);
   }

   public void setVehicleUnloaded(BaseVehicle var1) {
      this.m_main.setVehicleUnloaded(var1);
   }

   public boolean isVehicleLoaded(BaseVehicle var1) {
      return this.m_main.m_loadedIDs.contains(var1.sqlID);
   }

   public void loadChunk(IsoChunk var1) throws IOException {
      this.m_worldStreamer.loadChunk(var1);
   }

   public void unloadChunk(IsoChunk var1) {
      if (Thread.currentThread() != WorldStreamer.instance.worldStreamer) {
         boolean var2 = true;
      }

      this.m_worldStreamer.unloadChunk(var1);
   }

   public void addVehicle(BaseVehicle var1) {
      try {
         this.m_main.addVehicle(var1);
      } catch (Exception var3) {
         ExceptionLogger.logException(var3);
      }

   }

   public void removeVehicle(BaseVehicle var1) {
      this.m_main.removeVehicle(var1);
   }

   public void updateVehicle(BaseVehicle var1) {
      try {
         this.m_main.updateVehicle(var1);
      } catch (Exception var3) {
         ExceptionLogger.logException(var3);
      }

   }

   public void updateVehicleAndTrailer(BaseVehicle var1) {
      if (var1 != null) {
         this.updateVehicle(var1);
         BaseVehicle var2 = var1.getVehicleTowing();
         if (var2 != null) {
            this.updateVehicle(var2);
         }

      }
   }

   public void importPlayersFromOldDB(VehiclesDB2.IImportPlayerFromOldDB var1) {
      VehiclesDB2.SQLStore var2 = (VehiclesDB2.SQLStore)Type.tryCastTo(this.m_worldStreamer.m_store, VehiclesDB2.SQLStore.class);
      if (var2 != null && var2.m_conn != null) {
         try {
            DatabaseMetaData var3 = var2.m_conn.getMetaData();
            ResultSet var4 = var3.getTables((String)null, (String)null, "localPlayers", (String[])null);

            label91: {
               try {
                  if (var4.next()) {
                     break label91;
                  }
               } catch (Throwable var21) {
                  if (var4 != null) {
                     try {
                        var4.close();
                     } catch (Throwable var16) {
                        var21.addSuppressed(var16);
                     }
                  }

                  throw var21;
               }

               if (var4 != null) {
                  var4.close();
               }

               return;
            }

            if (var4 != null) {
               var4.close();
            }
         } catch (Exception var22) {
            ExceptionLogger.logException(var22);
            return;
         }

         String var23 = "SELECT id, name, wx, wy, x, y, z, worldversion, data, isDead FROM localPlayers";

         try {
            PreparedStatement var24 = var2.m_conn.prepareStatement(var23);

            try {
               ResultSet var5 = var24.executeQuery();

               while(var5.next()) {
                  int var6 = var5.getInt(1);
                  String var7 = var5.getString(2);
                  int var8 = var5.getInt(3);
                  int var9 = var5.getInt(4);
                  float var10 = var5.getFloat(5);
                  float var11 = var5.getFloat(6);
                  float var12 = var5.getFloat(7);
                  int var13 = var5.getInt(8);
                  byte[] var14 = var5.getBytes(9);
                  boolean var15 = var5.getBoolean(10);
                  var1.accept(var6, var7, var8, var9, var10, var11, var12, var13, var14, var15);
               }
            } catch (Throwable var19) {
               if (var24 != null) {
                  try {
                     var24.close();
                  } catch (Throwable var18) {
                     var19.addSuppressed(var18);
                  }
               }

               throw var19;
            }

            if (var24 != null) {
               var24.close();
            }
         } catch (Exception var20) {
            ExceptionLogger.logException(var20);
         }

         try {
            Statement var25 = var2.m_conn.createStatement();
            var25.executeUpdate("DROP TABLE localPlayers");
            var25.executeUpdate("DROP TABLE networkPlayers");
            var2.m_conn.commit();
         } catch (Exception var17) {
            ExceptionLogger.logException(var17);
         }

      }
   }

   private static final class MainThread {
      final TIntHashSet m_seenChunks = new TIntHashSet();
      final TIntHashSet m_usedIDs = new TIntHashSet();
      final TIntHashSet m_loadedIDs = new TIntHashSet();
      boolean m_forceSave = false;
      final ConcurrentLinkedQueue m_queue = new ConcurrentLinkedQueue();

      MainThread() {
         this.m_seenChunks.setAutoCompactionFactor(0.0F);
         this.m_usedIDs.setAutoCompactionFactor(0.0F);
         this.m_loadedIDs.setAutoCompactionFactor(0.0F);
      }

      void Reset() {
         this.m_seenChunks.clear();
         this.m_usedIDs.clear();
         this.m_loadedIDs.clear();

         assert this.m_queue.isEmpty();

         this.m_queue.clear();
         this.m_forceSave = false;
      }

      void update() throws IOException {
         if (!GameClient.bClient && !GameServer.bServer && this.m_forceSave) {
            this.m_forceSave = false;

            for(int var1 = 0; var1 < 4; ++var1) {
               IsoPlayer var2 = IsoPlayer.players[var1];
               if (var2 != null && var2.getVehicle() != null && var2.getVehicle().isEngineRunning()) {
                  this.updateVehicle(var2.getVehicle());
                  BaseVehicle var3 = var2.getVehicle().getVehicleTowing();
                  if (var3 != null) {
                     this.updateVehicle(var3);
                  }
               }
            }
         }

         for(VehiclesDB2.QueueItem var7 = (VehiclesDB2.QueueItem)this.m_queue.poll(); var7 != null; var7 = (VehiclesDB2.QueueItem)this.m_queue.poll()) {
            try {
               var7.processMain();
            } finally {
               var7.release();
            }
         }

      }

      void setChunkSeen(int var1, int var2) {
         int var3 = var2 << 16 | var1;
         this.m_seenChunks.add(var3);
      }

      boolean isChunkSeen(int var1, int var2) {
         int var3 = var2 << 16 | var1;
         return this.m_seenChunks.contains(var3);
      }

      int allocateID() {
         synchronized(this.m_usedIDs) {
            for(int var2 = 1; var2 < Integer.MAX_VALUE; ++var2) {
               if (!this.m_usedIDs.contains(var2)) {
                  this.m_usedIDs.add(var2);
                  return var2;
               }
            }

            throw new RuntimeException("ran out of unused vehicle ids");
         }
      }

      void setVehicleLoaded(BaseVehicle var1) {
         if (var1.sqlID == -1) {
            var1.sqlID = this.allocateID();
         }

         assert !this.m_loadedIDs.contains(var1.sqlID);

         this.m_loadedIDs.add(var1.sqlID);
      }

      void setVehicleUnloaded(BaseVehicle var1) {
         if (var1.sqlID != -1) {
            this.m_loadedIDs.remove(var1.sqlID);
         }
      }

      void addVehicle(BaseVehicle var1) throws IOException {
         if (var1.sqlID == -1) {
            var1.sqlID = this.allocateID();
         }

         VehiclesDB2.QueueAddVehicle var2 = (VehiclesDB2.QueueAddVehicle)VehiclesDB2.QueueAddVehicle.s_pool.alloc();
         var2.init(var1);
         VehiclesDB2.instance.m_worldStreamer.m_queue.add(var2);
      }

      void removeVehicle(BaseVehicle var1) {
         VehiclesDB2.QueueRemoveVehicle var2 = (VehiclesDB2.QueueRemoveVehicle)VehiclesDB2.QueueRemoveVehicle.s_pool.alloc();
         var2.init(var1);
         VehiclesDB2.instance.m_worldStreamer.m_queue.add(var2);
      }

      void updateVehicle(BaseVehicle var1) throws IOException {
         if (var1.sqlID == -1) {
            var1.sqlID = this.allocateID();
         }

         VehiclesDB2.QueueUpdateVehicle var2 = (VehiclesDB2.QueueUpdateVehicle)VehiclesDB2.QueueUpdateVehicle.s_pool.alloc();
         var2.init(var1);
         VehiclesDB2.instance.m_worldStreamer.m_queue.add(var2);
      }
   }

   private static final class WorldStreamerThread {
      final VehiclesDB2.IVehicleStore m_store = new VehiclesDB2.SQLStore();
      final ConcurrentLinkedQueue m_queue = new ConcurrentLinkedQueue();
      final VehiclesDB2.VehicleBuffer m_vehicleBuffer = new VehiclesDB2.VehicleBuffer();

      void Reset() {
         this.m_store.Reset();

         assert this.m_queue.isEmpty();

         this.m_queue.clear();
      }

      void update() {
         for(VehiclesDB2.QueueItem var1 = (VehiclesDB2.QueueItem)this.m_queue.poll(); var1 != null; var1 = (VehiclesDB2.QueueItem)this.m_queue.poll()) {
            try {
               var1.processWorldStreamer();
            } finally {
               VehiclesDB2.instance.m_main.m_queue.add(var1);
            }
         }

      }

      void loadChunk(IsoChunk var1) throws IOException {
         this.m_store.loadChunk(var1, this::vehicleLoaded);
      }

      void vehicleLoaded(IsoChunk var1, VehiclesDB2.VehicleBuffer var2) throws IOException {
         assert var2.m_id >= 1;

         IsoGridSquare var3 = var1.getGridSquare((int)(var2.m_x - (float)(var1.wx * 10)), (int)(var2.m_y - (float)(var1.wy * 10)), 0);
         BaseVehicle var4 = new BaseVehicle(IsoWorld.instance.CurrentCell);
         var4.setSquare(var3);
         var4.setCurrent(var3);

         try {
            var4.load(var2.m_bb, var2.m_WorldVersion);
         } catch (Exception var6) {
            ExceptionLogger.logException(var6);
            DebugLog.General.error("vehicle %d is being deleted because an error occurred loading it", var2.m_id);
            this.m_store.removeVehicle(var2.m_id);
            return;
         }

         var4.sqlID = var2.m_id;
         var4.chunk = var1;
         if (var1.jobType == IsoChunk.JobType.SoftReset) {
            var4.softReset();
         }

         var1.vehicles.add(var4);
      }

      void unloadChunk(IsoChunk var1) {
         for(int var2 = 0; var2 < var1.vehicles.size(); ++var2) {
            try {
               BaseVehicle var3 = (BaseVehicle)var1.vehicles.get(var2);
               this.m_vehicleBuffer.set(var3);
               this.m_store.updateVehicle(this.m_vehicleBuffer);
            } catch (Exception var4) {
               ExceptionLogger.logException(var4);
            }
         }

      }
   }

   private abstract static class IVehicleStore {
      abstract void init(TIntHashSet var1, TIntHashSet var2);

      abstract void Reset();

      abstract void loadChunk(IsoChunk var1, VehiclesDB2.ThrowingBiConsumer var2) throws IOException;

      abstract void updateVehicle(VehiclesDB2.VehicleBuffer var1);

      abstract void removeVehicle(int var1);
   }

   private abstract static class QueueItem extends PooledObject {
      abstract void processMain();

      abstract void processWorldStreamer();
   }

   private static final class SQLStore extends VehiclesDB2.IVehicleStore {
      Connection m_conn = null;
      final VehiclesDB2.VehicleBuffer m_vehicleBuffer = new VehiclesDB2.VehicleBuffer();

      void init(TIntHashSet var1, TIntHashSet var2) {
         var1.clear();
         var2.clear();
         if (!Core.getInstance().isNoSave()) {
            this.create();

            try {
               this.initUsedIDs(var1, var2);
            } catch (SQLException var4) {
               ExceptionLogger.logException(var4);
            }

         }
      }

      void Reset() {
         if (this.m_conn != null) {
            try {
               this.m_conn.close();
            } catch (SQLException var2) {
               ExceptionLogger.logException(var2);
            }

            this.m_conn = null;
         }
      }

      void loadChunk(IsoChunk var1, VehiclesDB2.ThrowingBiConsumer var2) throws IOException {
         if (this.m_conn != null && var1 != null) {
            String var3 = "SELECT id, x, y, data, worldversion FROM vehicles WHERE wx=? AND wy=?";

            try {
               PreparedStatement var4 = this.m_conn.prepareStatement(var3);

               try {
                  var4.setInt(1, var1.wx);
                  var4.setInt(2, var1.wy);
                  ResultSet var5 = var4.executeQuery();

                  while(var5.next()) {
                     this.m_vehicleBuffer.m_id = var5.getInt(1);
                     this.m_vehicleBuffer.m_wx = var1.wx;
                     this.m_vehicleBuffer.m_wy = var1.wy;
                     this.m_vehicleBuffer.m_x = var5.getFloat(2);
                     this.m_vehicleBuffer.m_y = var5.getFloat(3);
                     InputStream var6 = var5.getBinaryStream(4);
                     this.m_vehicleBuffer.setBytes(var6);
                     this.m_vehicleBuffer.m_WorldVersion = var5.getInt(5);
                     boolean var7 = this.m_vehicleBuffer.m_bb.get() != 0;
                     byte var8 = this.m_vehicleBuffer.m_bb.get();
                     if (var8 == IsoObject.getFactoryVehicle().getClassID() && var7) {
                        var2.accept(var1, this.m_vehicleBuffer);
                     }
                  }
               } catch (Throwable var10) {
                  if (var4 != null) {
                     try {
                        var4.close();
                     } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                     }
                  }

                  throw var10;
               }

               if (var4 != null) {
                  var4.close();
               }
            } catch (Exception var11) {
               ExceptionLogger.logException(var11);
            }

         }
      }

      void updateVehicle(VehiclesDB2.VehicleBuffer var1) {
         if (this.m_conn != null) {
            assert var1.m_id >= 1;

            synchronized(VehiclesDB2.instance.m_main.m_usedIDs) {
               assert VehiclesDB2.instance.m_main.m_usedIDs.contains(var1.m_id);
            }

            try {
               if (this.isInDB(var1.m_id)) {
                  this.updateDB(var1);
               } else {
                  this.addToDB(var1);
               }
            } catch (Exception var4) {
               ExceptionLogger.logException(var4);
               this.rollback();
            }

         }
      }

      boolean isInDB(int var1) throws SQLException {
         String var2 = "SELECT 1 FROM vehicles WHERE id=?";
         PreparedStatement var3 = this.m_conn.prepareStatement(var2);

         boolean var5;
         try {
            var3.setInt(1, var1);
            ResultSet var4 = var3.executeQuery();
            var5 = var4.next();
         } catch (Throwable var7) {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (var3 != null) {
            var3.close();
         }

         return var5;
      }

      void addToDB(VehiclesDB2.VehicleBuffer var1) throws SQLException {
         String var2 = "INSERT INTO vehicles(wx,wy,x,y,worldversion,data,id) VALUES(?,?,?,?,?,?,?)";

         try {
            PreparedStatement var3 = this.m_conn.prepareStatement(var2);

            try {
               var3.setInt(1, var1.m_wx);
               var3.setInt(2, var1.m_wy);
               var3.setFloat(3, var1.m_x);
               var3.setFloat(4, var1.m_y);
               var3.setInt(5, var1.m_WorldVersion);
               ByteBuffer var4 = var1.m_bb;
               var4.rewind();
               var3.setBinaryStream(6, new ByteBufferBackedInputStream(var4), var4.remaining());
               var3.setInt(7, var1.m_id);
               int var5 = var3.executeUpdate();
               this.m_conn.commit();
            } catch (Throwable var7) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (var3 != null) {
               var3.close();
            }

         } catch (Exception var8) {
            this.rollback();
            throw var8;
         }
      }

      void updateDB(VehiclesDB2.VehicleBuffer var1) throws SQLException {
         String var2 = "UPDATE vehicles SET wx = ?, wy = ?, x = ?, y = ?, worldversion = ?, data = ? WHERE id=?";

         try {
            PreparedStatement var3 = this.m_conn.prepareStatement(var2);

            try {
               var3.setInt(1, var1.m_wx);
               var3.setInt(2, var1.m_wy);
               var3.setFloat(3, var1.m_x);
               var3.setFloat(4, var1.m_y);
               var3.setInt(5, var1.m_WorldVersion);
               ByteBuffer var4 = var1.m_bb;
               var4.rewind();
               var3.setBinaryStream(6, new ByteBufferBackedInputStream(var4), var4.remaining());
               var3.setInt(7, var1.m_id);
               int var5 = var3.executeUpdate();
               this.m_conn.commit();
            } catch (Throwable var7) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (var3 != null) {
               var3.close();
            }

         } catch (Exception var8) {
            this.rollback();
            throw var8;
         }
      }

      void removeVehicle(int var1) {
         if (this.m_conn != null && var1 >= 1) {
            String var2 = "DELETE FROM vehicles WHERE id=?";

            try {
               PreparedStatement var3 = this.m_conn.prepareStatement(var2);

               try {
                  var3.setInt(1, var1);
                  int var4 = var3.executeUpdate();
                  this.m_conn.commit();
               } catch (Throwable var7) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                     }
                  }

                  throw var7;
               }

               if (var3 != null) {
                  var3.close();
               }
            } catch (Exception var8) {
               ExceptionLogger.logException(var8);
               this.rollback();
            }

         }
      }

      void create() {
         String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
         String var1 = var10000 + Core.GameSaveWorld;
         File var2 = new File(var1);
         if (!var2.exists()) {
            var2.mkdirs();
         }

         File var3 = new File(var1 + File.separator + "vehicles.db");
         var3.setReadable(true, false);
         var3.setExecutable(true, false);
         var3.setWritable(true, false);
         Statement var4;
         if (!var3.exists()) {
            try {
               var3.createNewFile();
               this.m_conn = PZSQLUtils.getConnection(var3.getAbsolutePath());
               var4 = this.m_conn.createStatement();
               var4.executeUpdate("CREATE TABLE vehicles (id   INTEGER PRIMARY KEY NOT NULL,wx    INTEGER,wy    INTEGER,x    FLOAT,y    FLOAT,worldversion    INTEGER,data BLOB);");
               var4.executeUpdate("CREATE INDEX ivwx ON vehicles (wx);");
               var4.executeUpdate("CREATE INDEX ivwy ON vehicles (wy);");
               var4.close();
            } catch (Exception var8) {
               ExceptionLogger.logException(var8);
               DebugLog.log("failed to create vehicles database");
               System.exit(1);
            }
         }

         if (this.m_conn == null) {
            try {
               this.m_conn = PZSQLUtils.getConnection(var3.getAbsolutePath());
            } catch (Exception var7) {
               DebugLog.log("failed to create vehicles database");
               System.exit(1);
            }
         }

         try {
            var4 = this.m_conn.createStatement();
            var4.executeQuery("PRAGMA JOURNAL_MODE=TRUNCATE;");
            var4.close();
         } catch (Exception var6) {
            ExceptionLogger.logException(var6);
            System.exit(1);
         }

         try {
            this.m_conn.setAutoCommit(false);
         } catch (SQLException var5) {
            ExceptionLogger.logException(var5);
         }

      }

      private String searchPathForSqliteLib(String var1) {
         String[] var2 = System.getProperty("java.library.path", "").split(File.pathSeparator);
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            File var6 = new File(var5, var1);
            if (var6.exists()) {
               return var5;
            }
         }

         return "";
      }

      void initUsedIDs(TIntHashSet var1, TIntHashSet var2) throws SQLException {
         String var3 = "SELECT wx,wy,id FROM vehicles";
         PreparedStatement var4 = this.m_conn.prepareStatement(var3);

         try {
            ResultSet var5 = var4.executeQuery();

            while(var5.next()) {
               int var6 = var5.getInt(1);
               int var7 = var5.getInt(2);
               var2.add(var7 << 16 | var6);
               var1.add(var5.getInt(3));
            }
         } catch (Throwable var9) {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (var4 != null) {
            var4.close();
         }

      }

      private void rollback() {
         if (this.m_conn != null) {
            try {
               this.m_conn.rollback();
            } catch (SQLException var2) {
               ExceptionLogger.logException(var2);
            }

         }
      }
   }

   public interface IImportPlayerFromOldDB {
      void accept(int var1, String var2, int var3, int var4, float var5, float var6, float var7, int var8, byte[] var9, boolean var10);
   }

   private static final class QueueUpdateVehicle extends VehiclesDB2.QueueItem {
      static final Pool s_pool = new Pool(VehiclesDB2.QueueUpdateVehicle::new);
      final VehiclesDB2.VehicleBuffer m_vehicleBuffer = new VehiclesDB2.VehicleBuffer();

      void init(BaseVehicle var1) throws IOException {
         this.m_vehicleBuffer.set(var1);
      }

      void processMain() {
      }

      void processWorldStreamer() {
         VehiclesDB2.instance.m_worldStreamer.m_store.updateVehicle(this.m_vehicleBuffer);
      }
   }

   private static class QueueRemoveVehicle extends VehiclesDB2.QueueItem {
      static final Pool s_pool = new Pool(VehiclesDB2.QueueRemoveVehicle::new);
      int m_id;

      void init(BaseVehicle var1) {
         this.m_id = var1.sqlID;
      }

      void processMain() {
      }

      void processWorldStreamer() {
         VehiclesDB2.instance.m_worldStreamer.m_store.removeVehicle(this.m_id);
      }
   }

   private static final class QueueAddVehicle extends VehiclesDB2.QueueItem {
      static final Pool s_pool = new Pool(VehiclesDB2.QueueAddVehicle::new);
      final VehiclesDB2.VehicleBuffer m_vehicleBuffer = new VehiclesDB2.VehicleBuffer();

      void init(BaseVehicle var1) throws IOException {
         this.m_vehicleBuffer.set(var1);
      }

      void processMain() {
      }

      void processWorldStreamer() {
         VehiclesDB2.instance.m_worldStreamer.m_store.updateVehicle(this.m_vehicleBuffer);
      }
   }

   private static class MemoryStore extends VehiclesDB2.IVehicleStore {
      final TIntObjectHashMap m_IDToVehicle = new TIntObjectHashMap();
      final TIntObjectHashMap m_ChunkToVehicles = new TIntObjectHashMap();

      void init(TIntHashSet var1, TIntHashSet var2) {
         var1.clear();
         var2.clear();
      }

      void Reset() {
         this.m_IDToVehicle.clear();
         this.m_ChunkToVehicles.clear();
      }

      void loadChunk(IsoChunk var1, VehiclesDB2.ThrowingBiConsumer var2) throws IOException {
         int var3 = var1.wy << 16 | var1.wx;
         ArrayList var4 = (ArrayList)this.m_ChunkToVehicles.get(var3);
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.size(); ++var5) {
               VehiclesDB2.VehicleBuffer var6 = (VehiclesDB2.VehicleBuffer)var4.get(var5);
               var6.m_bb.rewind();
               boolean var7 = var6.m_bb.get() == 1;
               int var8 = var6.m_bb.getInt();
               var2.accept(var1, var6);
            }

         }
      }

      void updateVehicle(VehiclesDB2.VehicleBuffer var1) {
         assert var1.m_id >= 1;

         synchronized(VehiclesDB2.instance.m_main.m_usedIDs) {
            assert VehiclesDB2.instance.m_main.m_usedIDs.contains(var1.m_id);
         }

         var1.m_bb.rewind();
         VehiclesDB2.VehicleBuffer var2 = (VehiclesDB2.VehicleBuffer)this.m_IDToVehicle.get(var1.m_id);
         int var3;
         if (var2 == null) {
            var2 = new VehiclesDB2.VehicleBuffer();
            var2.m_id = var1.m_id;
            this.m_IDToVehicle.put(var1.m_id, var2);
         } else {
            var3 = var2.m_wy << 16 | var2.m_wx;
            ((ArrayList)this.m_ChunkToVehicles.get(var3)).remove(var2);
         }

         var2.m_wx = var1.m_wx;
         var2.m_wy = var1.m_wy;
         var2.m_x = var1.m_x;
         var2.m_y = var1.m_y;
         var2.m_WorldVersion = var1.m_WorldVersion;
         var2.setBytes(var1.m_bb);
         var3 = var2.m_wy << 16 | var2.m_wx;
         if (this.m_ChunkToVehicles.get(var3) == null) {
            this.m_ChunkToVehicles.put(var3, new ArrayList());
         }

         ((ArrayList)this.m_ChunkToVehicles.get(var3)).add(var2);
      }

      void removeVehicle(int var1) {
         VehiclesDB2.VehicleBuffer var2 = (VehiclesDB2.VehicleBuffer)this.m_IDToVehicle.remove(var1);
         if (var2 != null) {
            int var3 = var2.m_wy << 16 | var2.m_wx;
            ((ArrayList)this.m_ChunkToVehicles.get(var3)).remove(var2);
         }
      }
   }

   @FunctionalInterface
   public interface ThrowingBiConsumer {
      void accept(Object var1, Object var2) throws Exception;
   }

   private static final class VehicleBuffer {
      int m_id = -1;
      int m_wx;
      int m_wy;
      float m_x;
      float m_y;
      int m_WorldVersion;
      ByteBuffer m_bb = ByteBuffer.allocate(32768);

      void set(BaseVehicle var1) throws IOException {
         assert var1.sqlID >= 1;

         synchronized(VehiclesDB2.instance.m_main.m_usedIDs) {
            assert VehiclesDB2.instance.m_main.m_usedIDs.contains(var1.sqlID);
         }

         this.m_id = var1.sqlID;
         this.m_wx = var1.chunk.wx;
         this.m_wy = var1.chunk.wy;
         this.m_x = var1.getX();
         this.m_y = var1.getY();
         this.m_WorldVersion = IsoWorld.getWorldVersion();
         ByteBuffer var2 = (ByteBuffer)VehiclesDB2.TL_SliceBuffer.get();
         var2.clear();

         while(true) {
            try {
               var1.save(var2);
               break;
            } catch (BufferOverflowException var4) {
               if (var2.capacity() >= 2097152) {
                  DebugLog.General.error("the vehicle %d cannot be saved", var1.sqlID);
                  throw var4;
               }

               var2 = ByteBuffer.allocate(var2.capacity() + '耀');
               VehiclesDB2.TL_SliceBuffer.set(var2);
            }
         }

         var2.flip();
         this.setBytes(var2);
      }

      void setBytes(ByteBuffer var1) {
         var1.rewind();
         ByteBufferOutputStream var2 = new ByteBufferOutputStream(this.m_bb, true);
         var2.clear();
         byte[] var3 = (byte[])VehiclesDB2.TL_Bytes.get();

         int var5;
         for(int var4 = var1.limit(); var4 > 0; var4 -= var5) {
            var5 = Math.min(var3.length, var4);
            var1.get(var3, 0, var5);
            var2.write(var3, 0, var5);
         }

         var2.flip();
         this.m_bb = var2.getWrappedBuffer();
      }

      void setBytes(byte[] var1) {
         ByteBufferOutputStream var2 = new ByteBufferOutputStream(this.m_bb, true);
         var2.clear();
         var2.write(var1);
         var2.flip();
         this.m_bb = var2.getWrappedBuffer();
      }

      void setBytes(InputStream var1) throws IOException {
         ByteBufferOutputStream var2 = new ByteBufferOutputStream(this.m_bb, true);
         var2.clear();
         byte[] var3 = (byte[])VehiclesDB2.TL_Bytes.get();

         while(true) {
            int var4 = var1.read(var3);
            if (var4 < 1) {
               var2.flip();
               this.m_bb = var2.getWrappedBuffer();
               return;
            }

            var2.write(var3, 0, var4);
         }
      }
   }
}
