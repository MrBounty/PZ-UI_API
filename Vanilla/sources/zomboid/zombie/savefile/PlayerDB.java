package zombie.savefile;

import gnu.trove.set.hash.TIntHashSet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import zombie.ZomboidFileSystem;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.core.utils.UpdateLimit;
import zombie.debug.DebugLog;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoWorld;
import zombie.iso.WorldStreamer;
import zombie.util.ByteBufferBackedInputStream;
import zombie.util.ByteBufferOutputStream;
import zombie.vehicles.VehiclesDB2;

public final class PlayerDB {
   public static final int INVALID_ID = -1;
   private static final int MIN_ID = 1;
   private static PlayerDB instance = null;
   private static final ThreadLocal TL_SliceBuffer = ThreadLocal.withInitial(() -> {
      return ByteBuffer.allocate(32768);
   });
   private static final ThreadLocal TL_Bytes = ThreadLocal.withInitial(() -> {
      return new byte[1024];
   });
   private static boolean s_allow = false;
   private final PlayerDB.IPlayerStore m_store = new PlayerDB.SQLPlayerStore();
   private final TIntHashSet m_usedIDs = new TIntHashSet();
   private final ConcurrentLinkedQueue m_toThread = new ConcurrentLinkedQueue();
   private final ConcurrentLinkedQueue m_fromThread = new ConcurrentLinkedQueue();
   private boolean m_forceSavePlayers;
   public boolean m_canSavePlayers = false;
   private final UpdateLimit m_saveToDBPeriod = new UpdateLimit(10000L);

   public static synchronized PlayerDB getInstance() {
      if (instance == null && s_allow) {
         instance = new PlayerDB();
      }

      return instance;
   }

   public static void setAllow(boolean var0) {
      s_allow = var0;
   }

   public static boolean isAllow() {
      return s_allow;
   }

   public static boolean isAvailable() {
      return instance != null;
   }

   public PlayerDB() {
      if (!Core.getInstance().isNoSave()) {
         this.create();
      }
   }

   private void create() {
      try {
         this.m_store.init(this.m_usedIDs);
         this.m_usedIDs.add(1);
      } catch (Exception var2) {
         ExceptionLogger.logException(var2);
      }

   }

   public void close() {
      assert WorldStreamer.instance.worldStreamer == null;

      this.updateWorldStreamer();

      assert this.m_toThread.isEmpty();

      try {
         this.m_store.Reset();
      } catch (Exception var2) {
         ExceptionLogger.logException(var2);
      }

      this.m_fromThread.clear();
      instance = null;
      s_allow = false;
   }

   private int allocateID() {
      synchronized(this.m_usedIDs) {
         for(int var2 = 1; var2 < Integer.MAX_VALUE; ++var2) {
            if (!this.m_usedIDs.contains(var2)) {
               this.m_usedIDs.add(var2);
               return var2;
            }
         }

         throw new RuntimeException("ran out of unused players.db ids");
      }
   }

   private PlayerDB.PlayerData allocPlayerData() {
      PlayerDB.PlayerData var1 = (PlayerDB.PlayerData)this.m_fromThread.poll();
      if (var1 == null) {
         var1 = new PlayerDB.PlayerData();
      }

      assert var1.m_sqlID == -1;

      return var1;
   }

   private void releasePlayerData(PlayerDB.PlayerData var1) {
      var1.m_sqlID = -1;
      this.m_fromThread.add(var1);
   }

   public void updateMain() {
      if (this.m_canSavePlayers && (this.m_forceSavePlayers || this.m_saveToDBPeriod.Check())) {
         this.m_forceSavePlayers = false;
         this.savePlayersAsync();
         VehiclesDB2.instance.setForceSave();
      }

   }

   public void updateWorldStreamer() {
      for(PlayerDB.PlayerData var1 = (PlayerDB.PlayerData)this.m_toThread.poll(); var1 != null; var1 = (PlayerDB.PlayerData)this.m_toThread.poll()) {
         try {
            this.m_store.save(var1);
         } catch (Exception var6) {
            ExceptionLogger.logException(var6);
         } finally {
            this.releasePlayerData(var1);
         }
      }

   }

   private void savePlayerAsync(IsoPlayer var1) throws Exception {
      if (var1 != null) {
         if (var1.sqlID == -1) {
            var1.sqlID = this.allocateID();
         }

         PlayerDB.PlayerData var2 = this.allocPlayerData();

         try {
            var2.set(var1);
            this.m_toThread.add(var2);
         } catch (Exception var4) {
            this.releasePlayerData(var2);
            throw var4;
         }
      }
   }

   private void savePlayersAsync() {
      for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
         IsoPlayer var2 = IsoPlayer.players[var1];
         if (var2 != null) {
            try {
               this.savePlayerAsync(var2);
            } catch (Exception var4) {
               ExceptionLogger.logException(var4);
            }
         }
      }

   }

   public void savePlayers() {
      if (this.m_canSavePlayers) {
         this.m_forceSavePlayers = true;
      }
   }

   public void saveLocalPlayersForce() {
      this.savePlayersAsync();
      if (WorldStreamer.instance.worldStreamer == null) {
         this.updateWorldStreamer();
      }

   }

   public void importPlayersFromVehiclesDB() {
      VehiclesDB2.instance.importPlayersFromOldDB((var1, var2, var3, var4, var5, var6, var7, var8, var9, var10) -> {
         PlayerDB.PlayerData var11 = this.allocPlayerData();
         var11.m_sqlID = this.allocateID();
         var11.m_x = var5;
         var11.m_y = var6;
         var11.m_z = var7;
         var11.m_isDead = var10;
         var11.m_name = var2;
         var11.m_WorldVersion = var8;
         var11.setBytes(var9);

         try {
            this.m_store.save(var11);
         } catch (Exception var13) {
            ExceptionLogger.logException(var13);
         }

         this.releasePlayerData(var11);
      });
   }

   public void uploadLocalPlayers2DB() {
      this.savePlayersAsync();
      String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
      String var1 = var10000 + Core.GameSaveWorld;

      for(int var2 = 1; var2 < 100; ++var2) {
         File var3 = new File(var1 + File.separator + "map_p" + var2 + ".bin");
         if (var3.exists()) {
            try {
               IsoPlayer var4 = new IsoPlayer(IsoWorld.instance.CurrentCell);
               var4.load(var3.getAbsolutePath());
               this.savePlayerAsync(var4);
               var3.delete();
            } catch (Exception var5) {
               ExceptionLogger.logException(var5);
            }
         }
      }

      if (WorldStreamer.instance.worldStreamer == null) {
         this.updateWorldStreamer();
      }

   }

   private boolean loadPlayer(int var1, IsoPlayer var2) {
      PlayerDB.PlayerData var3 = this.allocPlayerData();

      boolean var4;
      try {
         var3.m_sqlID = var1;
         if (!this.m_store.load(var3)) {
            var4 = false;
            return var4;
         }

         var2.load(var3.m_byteBuffer, var3.m_WorldVersion);
         if (var3.m_isDead) {
            var2.getBodyDamage().setOverallBodyHealth(0.0F);
            var2.setHealth(0.0F);
         }

         var2.sqlID = var1;
         var4 = true;
      } catch (Exception var8) {
         ExceptionLogger.logException(var8);
         return false;
      } finally {
         this.releasePlayerData(var3);
      }

      return var4;
   }

   public boolean loadLocalPlayer(int var1) {
      try {
         IsoPlayer var2 = IsoPlayer.getInstance();
         if (var2 == null) {
            var2 = new IsoPlayer(IsoCell.getInstance());
            IsoPlayer.setInstance(var2);
            IsoPlayer.players[0] = var2;
         }

         if (this.loadPlayer(var1, var2)) {
            int var3 = (int)(var2.x / 10.0F);
            int var4 = (int)(var2.y / 10.0F);
            IsoCell.getInstance().ChunkMap[IsoPlayer.getPlayerIndex()].WorldX = var3 + IsoWorld.saveoffsetx * 30;
            IsoCell.getInstance().ChunkMap[IsoPlayer.getPlayerIndex()].WorldY = var4 + IsoWorld.saveoffsety * 30;
            return true;
         }
      } catch (Exception var5) {
         ExceptionLogger.logException(var5);
      }

      return false;
   }

   public ArrayList getAllLocalPlayers() {
      ArrayList var1 = new ArrayList();
      this.m_usedIDs.forEach((var2) -> {
         if (var2 <= 1) {
            return true;
         } else {
            IsoPlayer var3 = new IsoPlayer(IsoWorld.instance.CurrentCell);
            if (this.loadPlayer(var2, var3)) {
               var1.add(var3);
            }

            return true;
         }
      });
      return var1;
   }

   public boolean loadLocalPlayerInfo(int var1) {
      PlayerDB.PlayerData var2 = this.allocPlayerData();

      boolean var3;
      try {
         var2.m_sqlID = var1;
         if (!this.m_store.loadEverythingExceptBytes(var2)) {
            return false;
         }

         IsoChunkMap.WorldXA = (int)var2.m_x;
         IsoChunkMap.WorldYA = (int)var2.m_y;
         IsoChunkMap.WorldZA = (int)var2.m_z;
         IsoChunkMap.WorldXA += 300 * IsoWorld.saveoffsetx;
         IsoChunkMap.WorldYA += 300 * IsoWorld.saveoffsety;
         IsoChunkMap.SWorldX[0] = (int)(var2.m_x / 10.0F);
         IsoChunkMap.SWorldY[0] = (int)(var2.m_y / 10.0F);
         int[] var10000 = IsoChunkMap.SWorldX;
         var10000[0] += 30 * IsoWorld.saveoffsetx;
         var10000 = IsoChunkMap.SWorldY;
         var10000[0] += 30 * IsoWorld.saveoffsety;
         var3 = true;
      } catch (Exception var7) {
         ExceptionLogger.logException(var7);
         return false;
      } finally {
         this.releasePlayerData(var2);
      }

      return var3;
   }

   private static final class SQLPlayerStore implements PlayerDB.IPlayerStore {
      Connection m_conn = null;

      public void init(TIntHashSet var1) throws Exception {
         var1.clear();
         if (!Core.getInstance().isNoSave()) {
            this.m_conn = PlayerDBHelper.create();
            this.initUsedIDs(var1);
         }
      }

      public void Reset() {
         if (this.m_conn != null) {
            try {
               this.m_conn.close();
            } catch (SQLException var2) {
               ExceptionLogger.logException(var2);
            }

            this.m_conn = null;
         }
      }

      public void save(PlayerDB.PlayerData var1) throws Exception {
         assert var1.m_sqlID >= 1;

         if (this.m_conn != null) {
            if (this.isInDB(var1.m_sqlID)) {
               this.update(var1);
            } else {
               this.add(var1);
            }

         }
      }

      public boolean load(PlayerDB.PlayerData var1) throws Exception {
         assert var1.m_sqlID >= 1;

         if (this.m_conn == null) {
            return false;
         } else {
            String var2 = "SELECT data,worldversion,x,y,z,isDead,name FROM localPlayers WHERE id=?";
            PreparedStatement var3 = this.m_conn.prepareStatement(var2);

            boolean var6;
            label50: {
               try {
                  var3.setInt(1, var1.m_sqlID);
                  ResultSet var4 = var3.executeQuery();
                  if (var4.next()) {
                     InputStream var5 = var4.getBinaryStream(1);
                     var1.setBytes(var5);
                     var1.m_WorldVersion = var4.getInt(2);
                     var1.m_x = (float)var4.getInt(3);
                     var1.m_y = (float)var4.getInt(4);
                     var1.m_z = (float)var4.getInt(5);
                     var1.m_isDead = var4.getBoolean(6);
                     var1.m_name = var4.getString(7);
                     var6 = true;
                     break label50;
                  }
               } catch (Throwable var8) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }
                  }

                  throw var8;
               }

               if (var3 != null) {
                  var3.close();
               }

               return false;
            }

            if (var3 != null) {
               var3.close();
            }

            return var6;
         }
      }

      public boolean loadEverythingExceptBytes(PlayerDB.PlayerData var1) throws Exception {
         if (this.m_conn == null) {
            return false;
         } else {
            String var2 = "SELECT worldversion,x,y,z,isDead,name FROM localPlayers WHERE id=?";
            PreparedStatement var3 = this.m_conn.prepareStatement(var2);

            boolean var5;
            label44: {
               try {
                  var3.setInt(1, var1.m_sqlID);
                  ResultSet var4 = var3.executeQuery();
                  if (var4.next()) {
                     var1.m_WorldVersion = var4.getInt(1);
                     var1.m_x = (float)var4.getInt(2);
                     var1.m_y = (float)var4.getInt(3);
                     var1.m_z = (float)var4.getInt(4);
                     var1.m_isDead = var4.getBoolean(5);
                     var1.m_name = var4.getString(6);
                     var5 = true;
                     break label44;
                  }
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

               return false;
            }

            if (var3 != null) {
               var3.close();
            }

            return var5;
         }
      }

      void initUsedIDs(TIntHashSet var1) throws SQLException {
         String var2 = "SELECT id FROM localPlayers";
         PreparedStatement var3 = this.m_conn.prepareStatement(var2);

         try {
            ResultSet var4 = var3.executeQuery();

            while(var4.next()) {
               var1.add(var4.getInt(1));
            }
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

      }

      boolean isInDB(int var1) throws SQLException {
         String var2 = "SELECT 1 FROM localPlayers WHERE id=?";
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

      void add(PlayerDB.PlayerData var1) throws Exception {
         if (this.m_conn != null && var1.m_sqlID >= 1) {
            String var2 = "INSERT INTO localPlayers(wx,wy,x,y,z,worldversion,data,isDead,name,id) VALUES(?,?,?,?,?,?,?,?,?,?)";

            try {
               PreparedStatement var3 = this.m_conn.prepareStatement(var2);

               try {
                  var3.setInt(1, (int)(var1.m_x / 10.0F));
                  var3.setInt(2, (int)(var1.m_y / 10.0F));
                  var3.setFloat(3, var1.m_x);
                  var3.setFloat(4, var1.m_y);
                  var3.setFloat(5, var1.m_z);
                  var3.setInt(6, var1.m_WorldVersion);
                  ByteBuffer var4 = var1.m_byteBuffer;
                  var4.rewind();
                  var3.setBinaryStream(7, new ByteBufferBackedInputStream(var4), var4.remaining());
                  var3.setBoolean(8, var1.m_isDead);
                  var3.setString(9, var1.m_name);
                  var3.setInt(10, var1.m_sqlID);
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
               PlayerDBHelper.rollback(this.m_conn);
               throw var8;
            }
         }
      }

      public void update(PlayerDB.PlayerData var1) throws Exception {
         if (this.m_conn != null && var1.m_sqlID >= 1) {
            String var2 = "UPDATE localPlayers SET wx = ?, wy = ?, x = ?, y = ?, z = ?, worldversion = ?, data = ?, isDead = ?, name = ? WHERE id=?";

            try {
               PreparedStatement var3 = this.m_conn.prepareStatement(var2);

               try {
                  var3.setInt(1, (int)(var1.m_x / 10.0F));
                  var3.setInt(2, (int)(var1.m_y / 10.0F));
                  var3.setFloat(3, var1.m_x);
                  var3.setFloat(4, var1.m_y);
                  var3.setFloat(5, var1.m_z);
                  var3.setInt(6, var1.m_WorldVersion);
                  ByteBuffer var4 = var1.m_byteBuffer;
                  var4.rewind();
                  var3.setBinaryStream(7, new ByteBufferBackedInputStream(var4), var4.remaining());
                  var3.setBoolean(8, var1.m_isDead);
                  var3.setString(9, var1.m_name);
                  var3.setInt(10, var1.m_sqlID);
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
               PlayerDBHelper.rollback(this.m_conn);
               throw var8;
            }
         }
      }
   }

   private interface IPlayerStore {
      void init(TIntHashSet var1) throws Exception;

      void Reset() throws Exception;

      void save(PlayerDB.PlayerData var1) throws Exception;

      boolean load(PlayerDB.PlayerData var1) throws Exception;

      boolean loadEverythingExceptBytes(PlayerDB.PlayerData var1) throws Exception;
   }

   private static final class PlayerData {
      int m_sqlID = -1;
      float m_x;
      float m_y;
      float m_z;
      boolean m_isDead;
      String m_name;
      int m_WorldVersion;
      ByteBuffer m_byteBuffer = ByteBuffer.allocate(32768);

      PlayerDB.PlayerData set(IsoPlayer var1) throws IOException {
         assert var1.sqlID >= 1;

         this.m_sqlID = var1.sqlID;
         this.m_x = var1.getX();
         this.m_y = var1.getY();
         this.m_z = var1.getZ();
         this.m_isDead = var1.isDead();
         String var10001 = var1.getDescriptor().getForename();
         this.m_name = var10001 + " " + var1.getDescriptor().getSurname();
         this.m_WorldVersion = IsoWorld.getWorldVersion();
         ByteBuffer var2 = (ByteBuffer)PlayerDB.TL_SliceBuffer.get();
         var2.clear();

         while(true) {
            try {
               var1.save(var2);
               break;
            } catch (BufferOverflowException var4) {
               if (var2.capacity() >= 2097152) {
                  DebugLog.General.error("the player %s cannot be saved", var1.getUsername());
                  throw var4;
               }

               var2 = ByteBuffer.allocate(var2.capacity() + '耀');
               PlayerDB.TL_SliceBuffer.set(var2);
            }
         }

         var2.flip();
         this.setBytes(var2);
         return this;
      }

      void setBytes(ByteBuffer var1) {
         var1.rewind();
         ByteBufferOutputStream var2 = new ByteBufferOutputStream(this.m_byteBuffer, true);
         var2.clear();
         byte[] var3 = (byte[])PlayerDB.TL_Bytes.get();

         int var5;
         for(int var4 = var1.limit(); var4 > 0; var4 -= var5) {
            var5 = Math.min(var3.length, var4);
            var1.get(var3, 0, var5);
            var2.write(var3, 0, var5);
         }

         var2.flip();
         this.m_byteBuffer = var2.getWrappedBuffer();
      }

      void setBytes(byte[] var1) {
         ByteBufferOutputStream var2 = new ByteBufferOutputStream(this.m_byteBuffer, true);
         var2.clear();
         var2.write(var1);
         var2.flip();
         this.m_byteBuffer = var2.getWrappedBuffer();
      }

      void setBytes(InputStream var1) throws IOException {
         ByteBufferOutputStream var2 = new ByteBufferOutputStream(this.m_byteBuffer, true);
         var2.clear();
         byte[] var3 = (byte[])PlayerDB.TL_Bytes.get();

         while(true) {
            int var4 = var1.read(var3);
            if (var4 < 1) {
               var2.flip();
               this.m_byteBuffer = var2.getWrappedBuffer();
               return;
            }

            var2.write(var3, 0, var4);
         }
      }
   }
}
