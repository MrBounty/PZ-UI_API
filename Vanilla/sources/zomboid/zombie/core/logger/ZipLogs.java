package zombie.core.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipError;
import zombie.ZomboidFileSystem;
import zombie.core.Core;
import zombie.debug.DebugLog;
import zombie.network.MD5Checksum;

public final class ZipLogs {
   static ArrayList filePaths = new ArrayList();

   public static void addZipFile(boolean var0) {
      FileSystem var1 = null;

      try {
         String var10000 = ZomboidFileSystem.instance.getCacheDir();
         String var2 = var10000 + File.separator + "logs.zip";
         String var3 = (new File(var2)).toURI().toString();
         URI var4 = URI.create("jar:" + var3);
         Path var5 = FileSystems.getDefault().getPath(var2).toAbsolutePath();
         HashMap var6 = new HashMap();
         var6.put("create", String.valueOf(Files.notExists(var5, new LinkOption[0])));

         try {
            var1 = FileSystems.newFileSystem(var4, var6);
         } catch (IOException var19) {
            var19.printStackTrace();
            return;
         } catch (ZipError var20) {
            var20.printStackTrace();
            DebugLog.log("Deleting possibly-corrupt " + var2);

            try {
               Files.deleteIfExists(var5);
            } catch (IOException var17) {
               var17.printStackTrace();
            }

            return;
         }

         long var7 = getMD5FromZip(var1, "/meta/console.txt.md5");
         long var9 = getMD5FromZip(var1, "/meta/coop-console.txt.md5");
         long var11 = getMD5FromZip(var1, "/meta/server-console.txt.md5");
         long var13 = getMD5FromZip(var1, "/meta/DebugLog.txt.md5");
         addLogToZip(var1, "console", "console.txt", var7);
         addLogToZip(var1, "coop-console", "coop-console.txt", var9);
         addLogToZip(var1, "server-console", "server-console.txt", var11);
         addDebugLogToZip(var1, "debug-log", "DebugLog.txt", var13);
         addToZip(var1, "/configs/options.ini", "options.ini");
         addToZip(var1, "/configs/popman-options.ini", "popman-options.ini");
         addToZip(var1, "/configs/latestSave.ini", "latestSave.ini");
         addToZip(var1, "/configs/debug-options.ini", "debug-options.ini");
         addToZip(var1, "/configs/sounds.ini", "sounds.ini");
         addToZip(var1, "/addition/translationProblems.txt", "translationProblems.txt");
         addToZip(var1, "/addition/gamepadBinding.config", "gamepadBinding.config");
         addFilelistToZip(var1, "/addition/mods.txt", "mods");
         addDirToZipLua(var1, "/lua", "Lua");
         addDirToZip(var1, "/db", "db");
         addDirToZip(var1, "/server", "Server");
         addDirToZip(var1, "/statistic", "Statistic");
         if (!var0) {
            addSaveOldToZip(var1, "/save_old/map_t.bin", "map_t.bin");
            addSaveOldToZip(var1, "/save_old/map_ver.bin", "map_ver.bin");
            addSaveOldToZip(var1, "/save_old/map.bin", "map.bin");
            addSaveOldToZip(var1, "/save_old/map_sand.bin", "map_sand.bin");
            addSaveOldToZip(var1, "/save_old/reanimated.bin", "reanimated.bin");
            addSaveOldToZip(var1, "/save_old/zombies.ini", "zombies.ini");
            addSaveOldToZip(var1, "/save_old/z_outfits.bin", "z_outfits.bin");
            addSaveOldToZip(var1, "/save_old/map_p.bin", "map_p.bin");
            addSaveOldToZip(var1, "/save_old/map_meta.bin", "map_meta.bin");
            addSaveOldToZip(var1, "/save_old/map_zone.bin", "map_zone.bin");
            addSaveOldToZip(var1, "/save_old/serverid.dat", "serverid.dat");
            addSaveOldToZip(var1, "/save_old/thumb.png", "thumb.png");
            addSaveOldToZip(var1, "/save_old/players.db", "players.db");
            addSaveOldToZip(var1, "/save_old/players.db-journal", "players.db-journal");
            addSaveOldToZip(var1, "/save_old/vehicles.db", "vehicles.db");
            addSaveOldToZip(var1, "/save_old/vehicles.db-journal", "vehicles.db-journal");
            putTextFile(var1, "/save_old/description.txt", getLastSaveDescription());
         } else {
            addSaveToZip(var1, "/save/map_t.bin", "map_t.bin");
            addSaveToZip(var1, "/save/map_ver.bin", "map_ver.bin");
            addSaveToZip(var1, "/save/map.bin", "map.bin");
            addSaveToZip(var1, "/save/map_sand.bin", "map_sand.bin");
            addSaveToZip(var1, "/save/reanimated.bin", "reanimated.bin");
            addSaveToZip(var1, "/save/zombies.ini", "zombies.ini");
            addSaveToZip(var1, "/save/z_outfits.bin", "z_outfits.bin");
            addSaveToZip(var1, "/save/map_p.bin", "map_p.bin");
            addSaveToZip(var1, "/save/map_meta.bin", "map_meta.bin");
            addSaveToZip(var1, "/save/map_zone.bin", "map_zone.bin");
            addSaveToZip(var1, "/save/serverid.dat", "serverid.dat");
            addSaveToZip(var1, "/save/thumb.png", "thumb.png");
            addSaveToZip(var1, "/save/players.db", "players.db");
            addSaveToZip(var1, "/save/players.db-journal", "players.db-journal");
            addSaveToZip(var1, "/save/vehicles.db", "vehicles.db");
            addSaveToZip(var1, "/save/vehicles.db-journal", "vehicles.db-journal");
            putTextFile(var1, "/save/description.txt", getCurrentSaveDescription());
         }

         try {
            var1.close();
         } catch (IOException var18) {
            var18.printStackTrace();
         }
      } catch (Exception var21) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var16) {
               var16.printStackTrace();
            }
         }

         var21.printStackTrace();
      }

   }

   private static void copyToZip(Path var0, Path var1, Path var2) throws IOException {
      Path var3 = var0.resolve(var1.relativize(var2).toString());
      if (Files.isDirectory(var2, new LinkOption[0])) {
         Files.createDirectories(var3);
      } else {
         Files.copy(var2, var3);
      }

   }

   private static void addToZip(FileSystem var0, String var1, String var2) {
      try {
         Path var3 = var0.getPath(var1);
         Files.createDirectories(var3.getParent());
         Path var4 = FileSystems.getDefault().getPath(ZomboidFileSystem.instance.getCacheDir() + File.separator + var2).toAbsolutePath();
         Files.deleteIfExists(var3);
         if (Files.exists(var4, new LinkOption[0])) {
            Files.copy(var4, var3, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   private static void addSaveToZip(FileSystem var0, String var1, String var2) {
      try {
         Path var3 = var0.getPath(var1);
         Files.createDirectories(var3.getParent());
         Path var4 = FileSystems.getDefault().getPath(ZomboidFileSystem.instance.getFileNameInCurrentSave(var2)).toAbsolutePath();
         Files.deleteIfExists(var3);
         if (Files.exists(var4, new LinkOption[0])) {
            Files.copy(var4, var3, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   private static void addSaveOldToZip(FileSystem var0, String var1, String var2) {
      try {
         BufferedReader var3 = null;

         try {
            String var10006 = ZomboidFileSystem.instance.getCacheDir();
            var3 = new BufferedReader(new FileReader(new File(var10006 + File.separator + "latestSave.ini")));
         } catch (FileNotFoundException var8) {
            return;
         }

         String var4 = var3.readLine();
         String var5 = var3.readLine();
         var3.close();
         Path var6 = var0.getPath(var1);
         Files.createDirectories(var6.getParent());
         Path var7 = FileSystems.getDefault().getPath(ZomboidFileSystem.instance.getSaveDir() + File.separator + var5 + File.separator + var4 + File.separator + var2).toAbsolutePath();
         Files.deleteIfExists(var6);
         if (Files.exists(var7, new LinkOption[0])) {
            Files.copy(var7, var6, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException var9) {
         var9.printStackTrace();
      }

   }

   private static String getLastSaveDescription() {
      try {
         BufferedReader var0 = null;

         try {
            String var10006 = ZomboidFileSystem.instance.getCacheDir();
            var0 = new BufferedReader(new FileReader(new File(var10006 + File.separator + "latestSave.ini")));
         } catch (FileNotFoundException var3) {
            return "-";
         }

         String var1 = var0.readLine();
         String var2 = var0.readLine();
         var0.close();
         return "World: " + var1 + "\n\rGameMode:" + var2;
      } catch (IOException var4) {
         var4.printStackTrace();
         return "-";
      }
   }

   private static String getCurrentSaveDescription() {
      String var0 = "Sandbox";
      if (Core.GameMode != null) {
         var0 = Core.GameMode;
      }

      String var1 = "-";
      if (Core.GameSaveWorld != null) {
         var1 = Core.GameSaveWorld;
      }

      return "World: " + var1 + "\n\rGameMode:" + var0;
   }

   private static void addDirToZip(FileSystem var0, String var1, String var2) {
      try {
         Path var3 = var0.getPath(var1);
         deleteDirectory(var0, var3);
         Files.createDirectories(var3);
         Path var4 = FileSystems.getDefault().getPath(ZomboidFileSystem.instance.getCacheDir() + File.separator + var2).toAbsolutePath();
         Stream var5 = Files.walk(var4);
         var5.forEach((var2x) -> {
            try {
               copyToZip(var3, var4, var2x);
            } catch (IOException var4x) {
               throw new RuntimeException(var4x);
            }
         });
      } catch (IOException var6) {
      }

   }

   private static void addDirToZipLua(FileSystem var0, String var1, String var2) {
      try {
         Path var3 = var0.getPath(var1);
         deleteDirectory(var0, var3);
         Files.createDirectories(var3);
         Path var4 = FileSystems.getDefault().getPath(ZomboidFileSystem.instance.getCacheDir() + File.separator + var2).toAbsolutePath();
         Stream var5 = Files.walk(var4);
         var5.forEach((var2x) -> {
            try {
               if (!var2x.endsWith("ServerList.txt") && !var2x.endsWith("ServerListSteam.txt")) {
                  copyToZip(var3, var4, var2x);
               }

            } catch (IOException var4x) {
               throw new RuntimeException(var4x);
            }
         });
      } catch (IOException var6) {
      }

   }

   private static void addFilelistToZip(FileSystem var0, String var1, String var2) {
      try {
         Path var3 = var0.getPath(var1);
         Path var4 = FileSystems.getDefault().getPath(ZomboidFileSystem.instance.getCacheDir() + File.separator + var2).toAbsolutePath();
         Stream var5 = Files.list(var4);
         String var6 = (String)var5.map(Path::getFileName).map(Path::toString).collect(Collectors.joining("; "));
         Files.deleteIfExists(var3);
         Files.write(var3, var6.getBytes(), new OpenOption[0]);
      } catch (IOException var7) {
      }

   }

   static void deleteDirectory(FileSystem var0, Path var1) {
      filePaths.clear();
      getDirectoryFiles(var1);
      Iterator var2 = filePaths.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();

         try {
            Files.delete(var0.getPath(var3));
         } catch (IOException var5) {
            var5.printStackTrace();
         }
      }

   }

   static void getDirectoryFiles(Path var0) {
      try {
         Stream var1 = Files.walk(var0);
         var1.forEach((var1x) -> {
            if (!var1x.toString().equals(var0.toString())) {
               if (Files.isDirectory(var1x, new LinkOption[0])) {
                  getDirectoryFiles(var1x);
               } else if (!filePaths.contains(var1x.toString())) {
                  filePaths.add(var1x.toString());
               }
            }

         });
         filePaths.add(var0.toString());
      } catch (IOException var2) {
      }

   }

   private static void addLogToZip(FileSystem var0, String var1, String var2, long var3) {
      long var5;
      try {
         String var10000 = ZomboidFileSystem.instance.getCacheDir();
         var5 = MD5Checksum.createChecksum(var10000 + File.separator + var2);
      } catch (Exception var16) {
         var5 = 0L;
      }

      String var10002 = ZomboidFileSystem.instance.getCacheDir();
      File var7 = new File(var10002 + File.separator + var2);
      if (var7.exists() && !var7.isDirectory() && var5 != var3) {
         Path var8;
         try {
            var8 = var0.getPath("/" + var1 + "/log_5.txt");
            Files.delete(var8);
         } catch (Exception var15) {
         }

         Path var9;
         Path var10;
         for(int var17 = 5; var17 > 0; --var17) {
            var9 = var0.getPath("/" + var1 + "/log_" + var17 + ".txt");
            var10 = var0.getPath("/" + var1 + "/log_" + (var17 + 1) + ".txt");

            try {
               Files.move(var9, var10);
            } catch (Exception var14) {
            }
         }

         try {
            var8 = var0.getPath("/" + var1 + "/log_1.txt");
            Files.createDirectories(var8.getParent());
            var9 = FileSystems.getDefault().getPath(ZomboidFileSystem.instance.getCacheDir() + File.separator + var2).toAbsolutePath();
            Files.copy(var9, var8, StandardCopyOption.REPLACE_EXISTING);
            var10 = var0.getPath("/meta/" + var2 + ".md5");
            Files.createDirectories(var10.getParent());

            try {
               Files.delete(var10);
            } catch (Exception var12) {
            }

            Files.write(var10, String.valueOf(var5).getBytes(), new OpenOption[0]);
         } catch (Exception var13) {
            var13.printStackTrace();
         }
      }

   }

   private static void addDebugLogToZip(FileSystem var0, String var1, String var2, long var3) {
      String var5 = null;
      File var6 = new File(LoggerManager.getLogsDir());
      String[] var7 = var6.list();

      for(int var8 = 0; var8 < var7.length; ++var8) {
         String var9 = var7[var8];
         if (var9.contains("DebugLog.txt")) {
            String var10000 = LoggerManager.getLogsDir();
            var5 = var10000 + File.separator + var9;
            break;
         }
      }

      if (var5 != null) {
         long var20;
         try {
            var20 = MD5Checksum.createChecksum(var5);
         } catch (Exception var19) {
            var20 = 0L;
         }

         File var10 = new File(var5);
         if (var10.exists() && !var10.isDirectory() && var20 != var3) {
            Path var11;
            try {
               var11 = var0.getPath("/" + var1 + "/log_5.txt");
               Files.delete(var11);
            } catch (Exception var18) {
            }

            Path var12;
            Path var13;
            for(int var21 = 5; var21 > 0; --var21) {
               var12 = var0.getPath("/" + var1 + "/log_" + var21 + ".txt");
               var13 = var0.getPath("/" + var1 + "/log_" + (var21 + 1) + ".txt");

               try {
                  Files.move(var12, var13);
               } catch (Exception var17) {
               }
            }

            try {
               var11 = var0.getPath("/" + var1 + "/log_1.txt");
               Files.createDirectories(var11.getParent());
               var12 = FileSystems.getDefault().getPath(var5).toAbsolutePath();
               Files.copy(var12, var11, StandardCopyOption.REPLACE_EXISTING);
               var13 = var0.getPath("/meta/" + var2 + ".md5");
               Files.createDirectories(var13.getParent());

               try {
                  Files.delete(var13);
               } catch (Exception var15) {
               }

               Files.write(var13, String.valueOf(var20).getBytes(), new OpenOption[0]);
            } catch (Exception var16) {
               var16.printStackTrace();
            }
         }

      }
   }

   private static long getMD5FromZip(FileSystem var0, String var1) {
      long var2 = 0L;

      try {
         Path var4 = var0.getPath(var1);
         if (Files.exists(var4, new LinkOption[0])) {
            List var5 = Files.readAllLines(var4);
            var2 = Long.parseLong((String)var5.get(0));
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return var2;
   }

   private static void putTextFile(FileSystem var0, String var1, String var2) {
      try {
         Path var3 = var0.getPath(var1);
         Files.createDirectories(var3.getParent());

         try {
            Files.delete(var3);
         } catch (Exception var5) {
         }

         Files.write(var3, var2.getBytes(), new OpenOption[0]);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
