package zombie.globalObjects;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.core.BoxedStaticValues;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.iso.IsoObject;
import zombie.iso.SliceY;
import zombie.network.GameClient;
import zombie.util.Type;

public final class SGlobalObjectSystem extends GlobalObjectSystem {
   private static KahluaTable tempTable;
   protected int loadedWorldVersion = -1;
   protected final HashSet modDataKeys = new HashSet();
   protected final HashSet objectModDataKeys = new HashSet();
   protected final HashSet objectSyncKeys = new HashSet();

   public SGlobalObjectSystem(String var1) {
      super(var1);
   }

   protected GlobalObject makeObject(int var1, int var2, int var3) {
      return new SGlobalObject(this, var1, var2, var3);
   }

   public void setModDataKeys(KahluaTable var1) {
      this.modDataKeys.clear();
      if (var1 != null) {
         KahluaTableIterator var2 = var1.iterator();

         while(var2.advance()) {
            Object var3 = var2.getValue();
            if (!(var3 instanceof String)) {
               throw new IllegalArgumentException("expected string but got \"" + var3 + "\"");
            }

            this.modDataKeys.add((String)var3);
         }

      }
   }

   public void setObjectModDataKeys(KahluaTable var1) {
      this.objectModDataKeys.clear();
      if (var1 != null) {
         KahluaTableIterator var2 = var1.iterator();

         while(var2.advance()) {
            Object var3 = var2.getValue();
            if (!(var3 instanceof String)) {
               throw new IllegalArgumentException("expected string but got \"" + var3 + "\"");
            }

            this.objectModDataKeys.add((String)var3);
         }

      }
   }

   public void setObjectSyncKeys(KahluaTable var1) {
      this.objectSyncKeys.clear();
      if (var1 != null) {
         KahluaTableIterator var2 = var1.iterator();

         while(var2.advance()) {
            Object var3 = var2.getValue();
            if (!(var3 instanceof String)) {
               throw new IllegalArgumentException("expected string but got \"" + var3 + "\"");
            }

            this.objectSyncKeys.add((String)var3);
         }

      }
   }

   public void update() {
   }

   public void chunkLoaded(int var1, int var2) {
      if (this.hasObjectsInChunk(var1, var2)) {
         Object var3 = this.modData.rawget("OnChunkLoaded");
         if (var3 == null) {
            throw new IllegalStateException("OnChunkLoaded method undefined for system '" + this.name + "'");
         } else {
            Double var4 = BoxedStaticValues.toDouble((double)var1);
            Double var5 = BoxedStaticValues.toDouble((double)var2);
            LuaManager.caller.pcall(LuaManager.thread, var3, this.modData, var4, var5);
         }
      }
   }

   public void sendCommand(String var1, KahluaTable var2) {
      SGlobalObjectNetwork.sendServerCommand(this.name, var1, var2);
   }

   public void receiveClientCommand(String var1, IsoPlayer var2, KahluaTable var3) {
      Object var4 = this.modData.rawget("OnClientCommand");
      if (var4 == null) {
         throw new IllegalStateException("OnClientCommand method undefined for system '" + this.name + "'");
      } else {
         LuaManager.caller.pcall(LuaManager.thread, var4, this.modData, var1, var2, var3);
      }
   }

   public void addGlobalObjectOnClient(SGlobalObject var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("globalObject is null");
      } else if (var1.system != this) {
         throw new IllegalArgumentException("object not in this system");
      } else {
         SGlobalObjectNetwork.addGlobalObjectOnClient(var1);
      }
   }

   public void removeGlobalObjectOnClient(SGlobalObject var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("globalObject is null");
      } else if (var1.system != this) {
         throw new IllegalArgumentException("object not in this system");
      } else {
         SGlobalObjectNetwork.removeGlobalObjectOnClient(var1);
      }
   }

   public void updateGlobalObjectOnClient(SGlobalObject var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("globalObject is null");
      } else if (var1.system != this) {
         throw new IllegalArgumentException("object not in this system");
      } else {
         SGlobalObjectNetwork.updateGlobalObjectOnClient(var1);
      }
   }

   private String getFileName() {
      return ZomboidFileSystem.instance.getFileNameInCurrentSave("gos_" + this.name + ".bin");
   }

   public KahluaTable getInitialStateForClient() {
      Object var1 = this.modData.rawget("getInitialStateForClient");
      if (var1 == null) {
         throw new IllegalStateException("getInitialStateForClient method undefined for system '" + this.name + "'");
      } else {
         Object[] var2 = LuaManager.caller.pcall(LuaManager.thread, var1, (Object)this.modData);
         return var2 != null && var2[0].equals(Boolean.TRUE) && var2[1] instanceof KahluaTable ? (KahluaTable)var2[1] : null;
      }
   }

   public void OnIsoObjectChangedItself(IsoObject var1) {
      GlobalObject var2 = this.getObjectAt(var1.getSquare().x, var1.getSquare().y, var1.getSquare().z);
      if (var2 != null) {
         Object var3 = this.modData.rawget("OnIsoObjectChangedItself");
         if (var3 == null) {
            throw new IllegalStateException("OnIsoObjectChangedItself method undefined for system '" + this.name + "'");
         } else {
            LuaManager.caller.pcall(LuaManager.thread, var3, this.modData, var1);
         }
      }
   }

   public int loadedWorldVersion() {
      return this.loadedWorldVersion;
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      boolean var3 = var1.get() == 0;
      if (!var3) {
         this.modData.load(var1, var2);
      }

      int var4 = var1.getInt();

      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = var1.getInt();
         int var7 = var1.getInt();
         byte var8 = var1.get();
         SGlobalObject var9 = (SGlobalObject)Type.tryCastTo(this.newObject(var6, var7, var8), SGlobalObject.class);
         var9.load(var1, var2);
      }

      this.loadedWorldVersion = var2;
   }

   public void save(ByteBuffer var1) throws IOException {
      if (tempTable == null) {
         tempTable = LuaManager.platform.newTable();
      }

      tempTable.wipe();
      KahluaTableIterator var2 = this.modData.iterator();

      while(var2.advance()) {
         Object var3 = var2.getKey();
         if (this.modDataKeys.contains(var3)) {
            tempTable.rawset(var3, this.modData.rawget(var3));
         }
      }

      if (tempTable.isEmpty()) {
         var1.put((byte)0);
      } else {
         var1.put((byte)1);
         tempTable.save(var1);
      }

      var1.putInt(this.objects.size());

      for(int var5 = 0; var5 < this.objects.size(); ++var5) {
         SGlobalObject var4 = (SGlobalObject)Type.tryCastTo((GlobalObject)this.objects.get(var5), SGlobalObject.class);
         var4.save(var1);
      }

   }

   public void load() {
      File var1 = new File(this.getFileName());

      try {
         FileInputStream var2 = new FileInputStream(var1);

         try {
            BufferedInputStream var3 = new BufferedInputStream(var2);

            try {
               synchronized(SliceY.SliceBufferLock) {
                  ByteBuffer var5 = SliceY.SliceBuffer;
                  var5.clear();
                  int var6 = var3.read(var5.array());
                  var5.limit(var6);
                  byte var7 = var5.get();
                  byte var8 = var5.get();
                  byte var9 = var5.get();
                  byte var10 = var5.get();
                  if (var7 != 71 || var8 != 76 || var9 != 79 || var10 != 83) {
                     throw new IOException("doesn't appear to be a GlobalObjectSystem file:" + var1.getAbsolutePath());
                  }

                  int var11 = var5.getInt();
                  if (var11 < 134) {
                     throw new IOException("invalid WorldVersion " + var11 + ": " + var1.getAbsolutePath());
                  }

                  if (var11 > 186) {
                     throw new IOException("file is from a newer version " + var11 + " of the game: " + var1.getAbsolutePath());
                  }

                  this.load(var5, var11);
               }
            } catch (Throwable var16) {
               try {
                  var3.close();
               } catch (Throwable var14) {
                  var16.addSuppressed(var14);
               }

               throw var16;
            }

            var3.close();
         } catch (Throwable var17) {
            try {
               var2.close();
            } catch (Throwable var13) {
               var17.addSuppressed(var13);
            }

            throw var17;
         }

         var2.close();
      } catch (FileNotFoundException var18) {
      } catch (Throwable var19) {
         ExceptionLogger.logException(var19);
      }

   }

   public void save() {
      if (!Core.getInstance().isNoSave()) {
         if (!GameClient.bClient) {
            File var1 = new File(this.getFileName());

            try {
               FileOutputStream var2 = new FileOutputStream(var1);

               try {
                  BufferedOutputStream var3 = new BufferedOutputStream(var2);

                  try {
                     synchronized(SliceY.SliceBufferLock) {
                        ByteBuffer var5 = SliceY.SliceBuffer;
                        var5.clear();
                        var5.put((byte)71);
                        var5.put((byte)76);
                        var5.put((byte)79);
                        var5.put((byte)83);
                        var5.putInt(186);
                        this.save(var5);
                        var3.write(var5.array(), 0, var5.position());
                     }
                  } catch (Throwable var10) {
                     try {
                        var3.close();
                     } catch (Throwable var8) {
                        var10.addSuppressed(var8);
                     }

                     throw var10;
                  }

                  var3.close();
               } catch (Throwable var11) {
                  try {
                     var2.close();
                  } catch (Throwable var7) {
                     var11.addSuppressed(var7);
                  }

                  throw var11;
               }

               var2.close();
            } catch (Throwable var12) {
               ExceptionLogger.logException(var12);
            }

         }
      }
   }

   public void Reset() {
      super.Reset();
      this.modDataKeys.clear();
      this.objectModDataKeys.clear();
      this.objectSyncKeys.clear();
   }
}
