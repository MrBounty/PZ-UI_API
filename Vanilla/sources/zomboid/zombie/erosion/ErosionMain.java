package zombie.erosion;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import zombie.GameTime;
import zombie.SandboxOptions;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.utils.Bits;
import zombie.debug.DebugLog;
import zombie.erosion.season.ErosionIceQueen;
import zombie.erosion.season.ErosionSeason;
import zombie.erosion.utils.Noise2D;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerMap;

public final class ErosionMain {
   private static ErosionMain instance;
   private ErosionConfig cfg;
   private boolean debug;
   private IsoSpriteManager sprMngr;
   private ErosionIceQueen IceQueen;
   private boolean isSnow;
   private String world;
   private String cfgPath;
   private IsoChunk chunk;
   private ErosionData.Chunk chunkModData;
   private Noise2D noiseMain;
   private Noise2D noiseMoisture;
   private Noise2D noiseMinerals;
   private Noise2D noiseKudzu;
   private ErosionWorld World;
   private ErosionSeason Season;
   private int tickUnit = 144;
   private int ticks = 0;
   private int eTicks = 0;
   private int day = 0;
   private int month = 0;
   private int year = 0;
   private int epoch = 0;
   private static final int[][] soilTable = new int[][]{{1, 1, 1, 1, 1, 4, 4, 4, 4, 4}, {1, 1, 1, 1, 2, 5, 4, 4, 4, 4}, {1, 1, 1, 2, 2, 5, 5, 4, 4, 4}, {1, 1, 2, 2, 3, 6, 5, 5, 4, 4}, {1, 2, 2, 3, 3, 6, 6, 5, 5, 4}, {7, 8, 8, 9, 9, 12, 12, 11, 11, 10}, {7, 7, 8, 8, 9, 12, 11, 11, 10, 10}, {7, 7, 7, 8, 8, 11, 11, 10, 10, 10}, {7, 7, 7, 7, 8, 11, 10, 10, 10, 10}, {7, 7, 7, 7, 7, 10, 10, 10, 10, 10}};
   private int snowFrac = 0;
   private int snowFracYesterday = 0;
   private int[] snowFracOnDay;

   public static ErosionMain getInstance() {
      return instance;
   }

   public ErosionMain(IsoSpriteManager var1, boolean var2) {
      instance = this;
      this.sprMngr = var1;
      this.debug = var2;
      this.start();
   }

   public ErosionConfig getConfig() {
      return this.cfg;
   }

   public ErosionSeason getSeasons() {
      return this.Season;
   }

   public int getEtick() {
      return this.eTicks;
   }

   public IsoSpriteManager getSpriteManager() {
      return this.sprMngr;
   }

   public void mainTimer() {
      if (GameClient.bClient) {
         if (Core.bDebug) {
            this.cfg.writeFile(this.cfgPath);
         }

      } else {
         int var1 = SandboxOptions.instance.ErosionDays.getValue();
         if (this.debug) {
            ++this.eTicks;
         } else if (var1 < 0) {
            this.eTicks = 0;
         } else if (var1 > 0) {
            ++this.ticks;
            this.eTicks = (int)((float)this.ticks / 144.0F / (float)var1 * 100.0F);
         } else {
            ++this.ticks;
            if (this.ticks >= this.tickUnit) {
               this.ticks = 0;
               ++this.eTicks;
            }
         }

         if (this.eTicks < 0) {
            this.eTicks = Integer.MAX_VALUE;
         }

         GameTime var2 = GameTime.getInstance();
         if (var2.getDay() != this.day || var2.getMonth() != this.month || var2.getYear() != this.year) {
            this.month = var2.getMonth();
            this.year = var2.getYear();
            this.day = var2.getDay();
            ++this.epoch;
            this.Season.setDay(this.day, this.month, this.year);
            this.snowCheck();
         }

         if (GameServer.bServer) {
            for(int var3 = 0; var3 < ServerMap.instance.LoadedCells.size(); ++var3) {
               ServerMap.ServerCell var4 = (ServerMap.ServerCell)ServerMap.instance.LoadedCells.get(var3);
               if (var4.bLoaded) {
                  for(int var5 = 0; var5 < 5; ++var5) {
                     for(int var6 = 0; var6 < 5; ++var6) {
                        IsoChunk var7 = var4.chunks[var6][var5];
                        if (var7 != null) {
                           ErosionData.Chunk var8 = var7.getErosionData();
                           if (var8.eTickStamp != this.eTicks || var8.epoch != this.epoch) {
                              for(int var9 = 0; var9 < 10; ++var9) {
                                 for(int var10 = 0; var10 < 10; ++var10) {
                                    IsoGridSquare var11 = var7.getGridSquare(var10, var9, 0);
                                    if (var11 != null) {
                                       this.loadGridsquare(var11);
                                    }
                                 }
                              }

                              var8.eTickStamp = this.eTicks;
                              var8.epoch = this.epoch;
                           }
                        }
                     }
                  }
               }
            }
         }

         this.cfg.time.ticks = this.ticks;
         this.cfg.time.eticks = this.eTicks;
         this.cfg.time.epoch = this.epoch;
         this.cfg.writeFile(this.cfgPath);
      }
   }

   public void snowCheck() {
   }

   public int getSnowFraction() {
      return this.snowFrac;
   }

   public int getSnowFractionYesterday() {
      return this.snowFracYesterday;
   }

   public boolean isSnow() {
      return this.isSnow;
   }

   public void sendState(ByteBuffer var1) {
      if (GameServer.bServer) {
         var1.putInt(this.eTicks);
         var1.putInt(this.ticks);
         var1.putInt(this.epoch);
         var1.put((byte)this.getSnowFraction());
         var1.put((byte)this.getSnowFractionYesterday());
         var1.putFloat(GameTime.getInstance().getTimeOfDay());
      }
   }

   public void receiveState(ByteBuffer var1) {
      if (GameClient.bClient) {
         int var2 = this.eTicks;
         int var3 = this.epoch;
         this.eTicks = var1.getInt();
         this.ticks = var1.getInt();
         this.epoch = var1.getInt();
         this.cfg.time.ticks = this.ticks;
         this.cfg.time.eticks = this.eTicks;
         this.cfg.time.epoch = this.epoch;
         byte var4 = var1.get();
         byte var5 = var1.get();
         float var6 = var1.getFloat();
         GameTime var7 = GameTime.getInstance();
         if (var7.getDay() != this.day || var7.getMonth() != this.month || var7.getYear() != this.year) {
            this.month = var7.getMonth();
            this.year = var7.getYear();
            this.day = var7.getDay();
            this.Season.setDay(this.day, this.month, this.year);
         }

         if (var2 != this.eTicks || var3 != this.epoch) {
            this.updateMapNow();
         }

      }
   }

   private void loadGridsquare(IsoGridSquare var1) {
      if (var1 != null && var1.chunk != null && var1.getZ() == 0) {
         this.getChunk(var1);
         ErosionData.Square var2 = var1.getErosionData();
         if (!var2.init) {
            this.initGridSquare(var1, var2);
            this.World.validateSpawn(var1, var2, this.chunkModData);
         }

         if (var2.doNothing) {
            return;
         }

         if (this.chunkModData.eTickStamp >= this.eTicks && this.chunkModData.epoch == this.epoch) {
            return;
         }

         this.World.update(var1, var2, this.chunkModData, this.eTicks);
      }

   }

   private void initGridSquare(IsoGridSquare var1, ErosionData.Square var2) {
      int var3 = var1.getX();
      int var4 = var1.getY();
      float var5 = this.noiseMain.layeredNoise((float)var3 / 10.0F, (float)var4 / 10.0F);
      var2.noiseMainByte = Bits.packFloatUnitToByte(var5);
      var2.noiseMain = var5;
      var2.noiseMainInt = (int)Math.floor((double)(var2.noiseMain * 100.0F));
      var2.noiseKudzu = this.noiseKudzu.layeredNoise((float)var3 / 10.0F, (float)var4 / 10.0F);
      var2.soil = this.chunkModData.soil;
      float var6 = (float)var2.rand(var3, var4, 100) / 100.0F;
      var2.magicNumByte = Bits.packFloatUnitToByte(var6);
      var2.magicNum = var6;
      var2.regions.clear();
      var2.init = true;
   }

   private void getChunk(IsoGridSquare var1) {
      this.chunk = var1.getChunk();
      this.chunkModData = this.chunk.getErosionData();
      if (!this.chunkModData.init) {
         this.initChunk(this.chunk, this.chunkModData);
      }
   }

   private void initChunk(IsoChunk var1, ErosionData.Chunk var2) {
      var2.set(var1);
      float var3 = (float)var2.x / 5.0F;
      float var4 = (float)var2.y / 5.0F;
      float var5 = this.noiseMoisture.layeredNoise(var3, var4);
      float var6 = this.noiseMinerals.layeredNoise(var3, var4);
      int var7 = var5 < 1.0F ? (int)Math.floor((double)(var5 * 10.0F)) : 9;
      int var8 = var6 < 1.0F ? (int)Math.floor((double)(var6 * 10.0F)) : 9;
      var2.init = true;
      var2.eTickStamp = -1;
      var2.epoch = -1;
      var2.moisture = var5;
      var2.minerals = var6;
      var2.soil = soilTable[var7][var8] - 1;
   }

   private boolean initConfig() {
      String var1 = "erosion.ini";
      if (GameClient.bClient) {
         this.cfg = GameClient.instance.erosionConfig;

         assert this.cfg != null;

         GameClient.instance.erosionConfig = null;
         this.cfgPath = ZomboidFileSystem.instance.getFileNameInCurrentSave(var1);
         return true;
      } else {
         this.cfg = new ErosionConfig();
         this.cfgPath = ZomboidFileSystem.instance.getFileNameInCurrentSave(var1);
         File var2 = new File(this.cfgPath);
         if (var2.exists()) {
            DebugLog.log("erosion: reading " + var2.getAbsolutePath());
            if (this.cfg.readFile(var2.getAbsolutePath())) {
               return true;
            }

            this.cfg = new ErosionConfig();
         }

         String var10002 = ZomboidFileSystem.instance.getCacheDir();
         var2 = new File(var10002 + File.separator + var1);
         if (!var2.exists() && !Core.getInstance().isNoSave()) {
            File var3 = ZomboidFileSystem.instance.getMediaFile("data" + File.separator + var1);
            if (var3.exists()) {
               try {
                  String var10000 = var3.getAbsolutePath();
                  DebugLog.log("erosion: copying " + var10000 + " to " + var2.getAbsolutePath());
                  Files.copy(var3.toPath(), var2.toPath());
               } catch (Exception var7) {
                  var7.printStackTrace();
               }
            }
         }

         if (var2.exists()) {
            DebugLog.log("erosion: reading " + var2.getAbsolutePath());
            if (!this.cfg.readFile(var2.getAbsolutePath())) {
               this.cfg = new ErosionConfig();
            }
         }

         int var8 = SandboxOptions.instance.getErosionSpeed();
         ErosionConfig.Time var9;
         switch(var8) {
         case 1:
            var9 = this.cfg.time;
            var9.tickunit /= 5;
            break;
         case 2:
            var9 = this.cfg.time;
            var9.tickunit /= 2;
         case 3:
         default:
            break;
         case 4:
            var9 = this.cfg.time;
            var9.tickunit *= 2;
            break;
         case 5:
            var9 = this.cfg.time;
            var9.tickunit *= 5;
         }

         float var4 = (float)(this.cfg.time.tickunit * 100) / 144.0F;
         float var5 = (float)((SandboxOptions.instance.TimeSinceApo.getValue() - 1) * 30);
         this.cfg.time.eticks = (int)Math.floor((double)(Math.min(1.0F, var5 / var4) * 100.0F));
         int var6 = SandboxOptions.instance.ErosionDays.getValue();
         if (var6 > 0) {
            this.cfg.time.tickunit = 144;
            this.cfg.time.eticks = (int)Math.floor((double)(Math.min(1.0F, var5 / (float)var6) * 100.0F));
         }

         return true;
      }
   }

   public void start() {
      if (this.initConfig()) {
         this.world = Core.GameSaveWorld;
         this.tickUnit = this.cfg.time.tickunit;
         this.ticks = this.cfg.time.ticks;
         this.eTicks = this.cfg.time.eticks;
         this.month = GameTime.getInstance().getMonth();
         this.year = GameTime.getInstance().getYear();
         this.day = GameTime.getInstance().getDay();
         this.debug = !GameServer.bServer && this.cfg.debug.enabled;
         this.cfg.consolePrint();
         this.noiseMain = new Noise2D();
         this.noiseMain.addLayer(this.cfg.seeds.seedMain_0, 0.5F, 3.0F);
         this.noiseMain.addLayer(this.cfg.seeds.seedMain_1, 2.0F, 5.0F);
         this.noiseMain.addLayer(this.cfg.seeds.seedMain_2, 5.0F, 8.0F);
         this.noiseMoisture = new Noise2D();
         this.noiseMoisture.addLayer(this.cfg.seeds.seedMoisture_0, 2.0F, 3.0F);
         this.noiseMoisture.addLayer(this.cfg.seeds.seedMoisture_1, 1.6F, 5.0F);
         this.noiseMoisture.addLayer(this.cfg.seeds.seedMoisture_2, 0.6F, 8.0F);
         this.noiseMinerals = new Noise2D();
         this.noiseMinerals.addLayer(this.cfg.seeds.seedMinerals_0, 2.0F, 3.0F);
         this.noiseMinerals.addLayer(this.cfg.seeds.seedMinerals_1, 1.6F, 5.0F);
         this.noiseMinerals.addLayer(this.cfg.seeds.seedMinerals_2, 0.6F, 8.0F);
         this.noiseKudzu = new Noise2D();
         this.noiseKudzu.addLayer(this.cfg.seeds.seedKudzu_0, 6.0F, 3.0F);
         this.noiseKudzu.addLayer(this.cfg.seeds.seedKudzu_1, 3.0F, 5.0F);
         this.noiseKudzu.addLayer(this.cfg.seeds.seedKudzu_2, 0.5F, 8.0F);
         this.Season = new ErosionSeason();
         ErosionConfig.Season var1 = this.cfg.season;
         int var2 = var1.tempMin;
         int var3 = var1.tempMax;
         if (SandboxOptions.instance.getTemperatureModifier() == 1) {
            var2 -= 10;
            var3 -= 10;
         } else if (SandboxOptions.instance.getTemperatureModifier() == 2) {
            var2 -= 5;
            var3 -= 5;
         } else if (SandboxOptions.instance.getTemperatureModifier() == 4) {
            var2 = (int)((double)var2 + 7.5D);
            var3 += 4;
         } else if (SandboxOptions.instance.getTemperatureModifier() == 5) {
            var2 += 15;
            var3 += 8;
         }

         this.Season.init(var1.lat, var3, var2, var1.tempDiff, var1.seasonLag, var1.noon, var1.seedA, var1.seedB, var1.seedC);
         this.Season.setRain(var1.jan, var1.feb, var1.mar, var1.apr, var1.may, var1.jun, var1.jul, var1.aug, var1.sep, var1.oct, var1.nov, var1.dec);
         this.Season.setDay(this.day, this.month, this.year);
         LuaEventManager.triggerEvent("OnInitSeasons", this.Season);
         this.IceQueen = new ErosionIceQueen(this.sprMngr);
         this.World = new ErosionWorld();
         if (this.World.init()) {
            this.snowCheck();
            if (this.debug) {
            }

            if (GameServer.bServer) {
            }

         }
      }
   }

   private void loadChunk(IsoChunk var1) {
      ErosionData.Chunk var2 = var1.getErosionData();
      if (!var2.init) {
         this.initChunk(var1, var2);
      }

      var2.eTickStamp = this.eTicks;
      var2.epoch = this.epoch;
   }

   public void DebugUpdateMapNow() {
      this.updateMapNow();
   }

   private void updateMapNow() {
      for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
         IsoChunkMap var2 = IsoWorld.instance.CurrentCell.getChunkMap(var1);
         if (!var2.ignore) {
            IsoChunkMap.bSettingChunk.lock();

            try {
               for(int var3 = 0; var3 < IsoChunkMap.ChunkGridWidth; ++var3) {
                  for(int var4 = 0; var4 < IsoChunkMap.ChunkGridWidth; ++var4) {
                     IsoChunk var5 = var2.getChunk(var4, var3);
                     if (var5 != null) {
                        ErosionData.Chunk var6 = var5.getErosionData();
                        if (var6.eTickStamp != this.eTicks || var6.epoch != this.epoch) {
                           for(int var7 = 0; var7 < 10; ++var7) {
                              for(int var8 = 0; var8 < 10; ++var8) {
                                 IsoGridSquare var9 = var5.getGridSquare(var8, var7, 0);
                                 if (var9 != null) {
                                    this.loadGridsquare(var9);
                                 }
                              }
                           }

                           var6.eTickStamp = this.eTicks;
                           var6.epoch = this.epoch;
                        }
                     }
                  }
               }
            } finally {
               IsoChunkMap.bSettingChunk.unlock();
            }
         }
      }

   }

   public static void LoadGridsquare(IsoGridSquare var0) {
      instance.loadGridsquare(var0);
   }

   public static void ChunkLoaded(IsoChunk var0) {
      instance.loadChunk(var0);
   }

   public static void EveryTenMinutes() {
      instance.mainTimer();
   }

   public static void Reset() {
      instance = null;
   }
}
