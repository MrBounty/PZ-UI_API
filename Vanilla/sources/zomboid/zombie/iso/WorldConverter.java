package zombie.iso;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import zombie.ZomboidFileSystem;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.znet.SteamUtils;
import zombie.debug.DebugLog;
import zombie.erosion.ErosionRegions;
import zombie.erosion.season.ErosionIceQueen;
import zombie.gameStates.GameLoadingState;
import zombie.gameStates.IngameState;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.CoopSlave;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerMap;
import zombie.network.ServerOptions;
import zombie.vehicles.VehicleManager;

public final class WorldConverter {
   public static final WorldConverter instance = new WorldConverter();
   public static boolean converting;
   public HashMap TilesetConversions = null;
   int oldID = 0;

   public void convert(String var1, IsoSpriteManager var2) throws IOException {
      String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
      File var3 = new File(var10002 + File.separator + var1 + File.separator + "map_ver.bin");
      if (var3.exists()) {
         converting = true;
         FileInputStream var4 = new FileInputStream(var3);
         DataInputStream var5 = new DataInputStream(var4);
         int var6 = var5.readInt();
         var5.close();
         if (var6 < 186) {
            if (var6 < 24) {
               GameLoadingState.build23Stop = true;
               return;
            }

            try {
               this.convert(var1, var6, 186);
            } catch (Exception var8) {
               IngameState.createWorld(var1);
               IngameState.copyWorld(var1 + "_backup", var1);
               var8.printStackTrace();
            }
         }

         converting = false;
      }

   }

   private void convert(String var1, int var2, int var3) {
      if (!GameClient.bClient) {
         GameLoadingState.convertingWorld = true;
         String var4 = Core.GameSaveWorld;
         IngameState.createWorld(var1 + "_backup");
         IngameState.copyWorld(var1, Core.GameSaveWorld);
         Core.GameSaveWorld = var4;
         if (var3 >= 14 && var2 < 14) {
            try {
               this.convertchunks(var1, 25, 25);
            } catch (IOException var8) {
               var8.printStackTrace();
            }
         } else if (var2 == 7) {
            try {
               this.convertchunks(var1);
            } catch (IOException var7) {
               var7.printStackTrace();
            }
         }

         if (var2 <= 4) {
            this.loadconversionmap(var2, "tiledefinitions");
            this.loadconversionmap(var2, "newtiledefinitions");

            try {
               this.convertchunks(var1);
            } catch (IOException var6) {
               var6.printStackTrace();
            }
         }

         GameLoadingState.convertingWorld = false;
      }
   }

   private void convertchunks(String var1) throws IOException {
      IsoCell var2 = new IsoCell(300, 300);
      IsoChunkMap var3 = new IsoChunkMap(var2);
      String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
      File var4 = new File(var10002 + File.separator + var1 + File.separator);
      if (!var4.exists()) {
         var4.mkdir();
      }

      String[] var5 = var4.list();
      String[] var6 = var5;
      int var7 = var5.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String var9 = var6[var8];
         if (var9.contains(".bin") && !var9.equals("map.bin") && !var9.equals("map_p.bin") && !var9.matches("map_p[0-9]+\\.bin") && !var9.equals("map_t.bin") && !var9.equals("map_c.bin") && !var9.equals("map_ver.bin") && !var9.equals("map_sand.bin") && !var9.equals("map_mov.bin") && !var9.equals("map_meta.bin") && !var9.equals("map_cm.bin") && !var9.equals("pc.bin") && !var9.startsWith("zpop_") && !var9.startsWith("chunkdata_")) {
            String[] var10 = var9.replace(".bin", "").replace("map_", "").split("_");
            int var11 = Integer.parseInt(var10[0]);
            int var12 = Integer.parseInt(var10[1]);
            var3.LoadChunkForLater(var11, var12, 0, 0);
            var3.SwapChunkBuffers();
            var3.getChunk(0, 0).Save(true);
         }
      }

   }

   private void convertchunks(String var1, int var2, int var3) throws IOException {
      IsoCell var4 = new IsoCell(300, 300);
      new IsoChunkMap(var4);
      String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
      File var6 = new File(var10002 + File.separator + var1 + File.separator);
      if (!var6.exists()) {
         var6.mkdir();
      }

      String[] var7 = var6.list();
      IsoWorld.saveoffsetx = var2;
      IsoWorld.saveoffsety = var3;
      IsoWorld.instance.MetaGrid.Create();
      WorldStreamer.instance.create();
      String[] var8 = var7;
      int var9 = var7.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         String var11 = var8[var10];
         if (var11.contains(".bin") && !var11.equals("map.bin") && !var11.equals("map_p.bin") && !var11.matches("map_p[0-9]+\\.bin") && !var11.equals("map_t.bin") && !var11.equals("map_c.bin") && !var11.equals("map_ver.bin") && !var11.equals("map_sand.bin") && !var11.equals("map_mov.bin") && !var11.equals("map_meta.bin") && !var11.equals("map_cm.bin") && !var11.equals("pc.bin") && !var11.startsWith("zpop_") && !var11.startsWith("chunkdata_")) {
            String[] var12 = var11.replace(".bin", "").replace("map_", "").split("_");
            int var13 = Integer.parseInt(var12[0]);
            int var14 = Integer.parseInt(var12[1]);
            IsoChunk var15 = new IsoChunk(var4);
            var15.refs.add(var4.ChunkMap[0]);
            WorldStreamer.instance.addJobConvert(var15, 0, 0, var13, var14);

            while(!var15.bLoaded) {
               try {
                  Thread.sleep(20L);
               } catch (InterruptedException var18) {
                  var18.printStackTrace();
               }
            }

            var15.wx += var2 * 30;
            var15.wy += var3 * 30;
            var15.jobType = IsoChunk.JobType.Convert;
            var15.Save(true);
            File var16 = new File(ZomboidFileSystem.instance.getGameModeCacheDir() + File.separator + var1 + File.separator + var11);

            while(!ChunkSaveWorker.instance.toSaveQueue.isEmpty()) {
               try {
                  Thread.sleep(13L);
               } catch (InterruptedException var19) {
                  var19.printStackTrace();
               }
            }

            var16.delete();
         }
      }

   }

   private void loadconversionmap(int var1, String var2) {
      String var3 = "media/" + var2 + "_" + var1 + ".tiles";
      File var4 = new File(var3);
      if (var4.exists()) {
         try {
            RandomAccessFile var5 = new RandomAccessFile(var4.getAbsolutePath(), "r");
            int var6 = IsoWorld.readInt(var5);

            for(int var7 = 0; var7 < var6; ++var7) {
               Thread.sleep(4L);
               String var8 = IsoWorld.readString(var5);
               String var9 = var8.trim();
               IsoWorld.readString(var5);
               int var10 = IsoWorld.readInt(var5);
               int var11 = IsoWorld.readInt(var5);
               int var12 = IsoWorld.readInt(var5);

               for(int var13 = 0; var13 < var12; ++var13) {
                  IsoSprite var14 = (IsoSprite)IsoSpriteManager.instance.NamedMap.get(var9 + "_" + var13);
                  if (this.TilesetConversions == null) {
                     this.TilesetConversions = new HashMap();
                  }

                  this.TilesetConversions.put(this.oldID, var14.ID);
                  ++this.oldID;
                  int var15 = IsoWorld.readInt(var5);

                  for(int var16 = 0; var16 < var15; ++var16) {
                     var8 = IsoWorld.readString(var5);
                     String var17 = var8.trim();
                     var8 = IsoWorld.readString(var5);
                     String var18 = var8.trim();
                  }
               }
            }
         } catch (Exception var19) {
         }
      }

   }

   public void softreset() {
      String var1 = GameServer.ServerName;
      Core.GameSaveWorld = var1;
      IsoCell var2 = new IsoCell(300, 300);
      IsoChunk var3 = new IsoChunk(var2);
      String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
      File var4 = new File(var10002 + File.separator + var1 + File.separator);
      if (!var4.exists()) {
         var4.mkdir();
      }

      String[] var5 = var4.list();
      if (CoopSlave.instance != null) {
         CoopSlave.instance.sendMessage("softreset-count", (String)null, Integer.toString(var5.length));
      }

      IsoWorld.instance.MetaGrid.Create();
      ServerMap.instance.init(IsoWorld.instance.MetaGrid);
      new ErosionIceQueen(IsoSpriteManager.instance);
      ErosionRegions.init();
      WorldStreamer.instance.create();
      VehicleManager.instance = new VehicleManager();
      int var6 = var5.length;
      DebugLog.log("processing " + var6 + " files");
      String[] var7 = var5;
      int var8 = var5.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         String var10 = var7[var9];
         --var6;
         if (var10.startsWith("zpop_")) {
            deleteFile(var10);
         } else if (var10.equals("map_t.bin")) {
            deleteFile(var10);
         } else if (!var10.equals("map_meta.bin") && !var10.equals("map_zone.bin")) {
            if (var10.equals("reanimated.bin")) {
               deleteFile(var10);
            } else if (var10.matches("map_[0-9]+_[0-9]+\\.bin")) {
               System.out.println("Soft clearing chunk: " + var10);
               String[] var11 = var10.replace(".bin", "").replace("map_", "").split("_");
               int var12 = Integer.parseInt(var11[0]);
               int var13 = Integer.parseInt(var11[1]);
               var3.refs.add(var2.ChunkMap[0]);
               WorldStreamer.instance.addJobWipe(var3, 0, 0, var12, var13);

               while(!var3.bLoaded) {
                  try {
                     Thread.sleep(20L);
                  } catch (InterruptedException var16) {
                     var16.printStackTrace();
                  }
               }

               var3.jobType = IsoChunk.JobType.Convert;
               var3.FloorBloodSplats.clear();

               try {
                  var3.Save(true);
               } catch (IOException var15) {
                  var15.printStackTrace();
               }

               var3.doReuseGridsquares();
               IsoChunkMap.chunkStore.remove(var3);
               if (var6 % 100 == 0) {
                  DebugLog.log(var6 + " files to go");
               }

               if (CoopSlave.instance != null && var6 % 10 == 0) {
                  CoopSlave.instance.sendMessage("softreset-remaining", (String)null, Integer.toString(var6));
               }
            }
         } else {
            deleteFile(var10);
         }
      }

      GameServer.ResetID = Rand.Next(10000000);
      ServerOptions.instance.putSaveOption("ResetID", String.valueOf(GameServer.ResetID));
      IsoWorld.instance.CurrentCell = null;
      DebugLog.log("soft-reset complete, server terminated");
      if (CoopSlave.instance != null) {
         CoopSlave.instance.sendMessage("softreset-finished", (String)null, "");
      }

      SteamUtils.shutdown();
      System.exit(0);
   }

   private static void deleteFile(String var0) {
      String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
      File var1 = new File(var10002 + File.separator + GameServer.ServerName + File.separator + var0);
      var1.delete();
   }
}
