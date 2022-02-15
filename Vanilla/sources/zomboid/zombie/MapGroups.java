package zombie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import zombie.core.Core;
import zombie.core.Translator;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.gameStates.ChooseGameInfo;
import zombie.iso.IsoWorld;
import zombie.modding.ActiveMods;

public final class MapGroups {
   private final ArrayList groups = new ArrayList();
   private final ArrayList realDirectories = new ArrayList();

   private static ArrayList getVanillaMapDirectories(boolean var0) {
      ArrayList var1 = new ArrayList();
      File var2 = ZomboidFileSystem.instance.getMediaFile("maps");
      String[] var3 = var2.list();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4];
            if (var5.equalsIgnoreCase("challengemaps")) {
               if (var0) {
                  try {
                     DirectoryStream var6 = Files.newDirectoryStream(Paths.get(var2.getPath(), var5), (var0x) -> {
                        return Files.isDirectory(var0x, new LinkOption[0]) && Files.exists(var0x.resolve("map.info"), new LinkOption[0]);
                     });

                     try {
                        Iterator var7 = var6.iterator();

                        while(var7.hasNext()) {
                           Path var8 = (Path)var7.next();
                           var1.add(var5 + "/" + var8.getFileName().toString());
                        }
                     } catch (Throwable var10) {
                        if (var6 != null) {
                           try {
                              var6.close();
                           } catch (Throwable var9) {
                              var10.addSuppressed(var9);
                           }
                        }

                        throw var10;
                     }

                     if (var6 != null) {
                        var6.close();
                     }
                  } catch (Exception var11) {
                  }
               }
            } else {
               var1.add(var5);
            }
         }
      }

      return var1;
   }

   public static String addMissingVanillaDirectories(String var0) {
      ArrayList var1 = getVanillaMapDirectories(false);
      boolean var2 = false;
      String[] var3 = var0.split(";");
      String[] var4 = var3;
      int var5 = var3.length;

      int var6;
      String var7;
      for(var6 = 0; var6 < var5; ++var6) {
         var7 = var4[var6];
         var7 = var7.trim();
         if (!var7.isEmpty() && var1.contains(var7)) {
            var2 = true;
            break;
         }
      }

      if (!var2) {
         return var0;
      } else {
         ArrayList var9 = new ArrayList();
         String[] var10 = var3;
         var6 = var3.length;

         for(int var15 = 0; var15 < var6; ++var15) {
            String var8 = var10[var15];
            var8 = var8.trim();
            if (!var8.isEmpty()) {
               var9.add(var8);
            }
         }

         Iterator var11 = var1.iterator();

         while(var11.hasNext()) {
            String var13 = (String)var11.next();
            if (!var9.contains(var13)) {
               var9.add(var13);
            }
         }

         String var12 = "";

         for(Iterator var14 = var9.iterator(); var14.hasNext(); var12 = var12 + var7) {
            var7 = (String)var14.next();
            if (!var12.isEmpty()) {
               var12 = var12 + ";";
            }
         }

         return var12;
      }
   }

   public void createGroups() {
      this.createGroups(ActiveMods.getById("currentGame"), true);
   }

   public void createGroups(ActiveMods var1, boolean var2) {
      this.createGroups(var1, var2, false);
   }

   public void createGroups(ActiveMods var1, boolean var2, boolean var3) {
      this.groups.clear();
      this.realDirectories.clear();
      Iterator var4 = var1.getMods().iterator();

      while(true) {
         ChooseGameInfo.Mod var6;
         String[] var8;
         do {
            File var7;
            do {
               do {
                  String var5;
                  if (!var4.hasNext()) {
                     if (var2) {
                        ArrayList var11 = getVanillaMapDirectories(var3);
                        var5 = ZomboidFileSystem.instance.getMediaPath("maps");
                        Iterator var15 = var11.iterator();

                        while(var15.hasNext()) {
                           String var19 = (String)var15.next();
                           this.handleMapDirectory(var19, var5 + File.separator + var19);
                        }
                     }

                     var4 = this.realDirectories.iterator();

                     while(var4.hasNext()) {
                        MapGroups.MapDirectory var12 = (MapGroups.MapDirectory)var4.next();
                        ArrayList var16 = new ArrayList();
                        this.getDirsRecursively(var12, var16);
                        MapGroups.MapGroup var20 = this.findGroupWithAnyOfTheseDirectories(var16);
                        if (var20 == null) {
                           var20 = new MapGroups.MapGroup();
                           this.groups.add(var20);
                        }

                        Iterator var21 = var16.iterator();

                        while(var21.hasNext()) {
                           MapGroups.MapDirectory var24 = (MapGroups.MapDirectory)var21.next();
                           if (!var20.hasDirectory(var24.name)) {
                              var20.addDirectory(var24);
                           }
                        }
                     }

                     var4 = this.groups.iterator();

                     MapGroups.MapGroup var14;
                     while(var4.hasNext()) {
                        var14 = (MapGroups.MapGroup)var4.next();
                        var14.setPriority();
                     }

                     var4 = this.groups.iterator();

                     while(var4.hasNext()) {
                        var14 = (MapGroups.MapGroup)var4.next();
                        var14.setOrder(var1);
                     }

                     if (Core.bDebug) {
                        int var13 = 1;

                        for(Iterator var17 = this.groups.iterator(); var17.hasNext(); ++var13) {
                           MapGroups.MapGroup var18 = (MapGroups.MapGroup)var17.next();
                           DebugLog.log("MapGroup " + var13 + "/" + this.groups.size());
                           Iterator var22 = var18.directories.iterator();

                           while(var22.hasNext()) {
                              MapGroups.MapDirectory var23 = (MapGroups.MapDirectory)var22.next();
                              DebugLog.log("  " + var23.name);
                           }
                        }

                        DebugLog.log("-----");
                     }

                     return;
                  }

                  var5 = (String)var4.next();
                  var6 = ChooseGameInfo.getAvailableModDetails(var5);
               } while(var6 == null);

               var7 = new File(var6.getDir() + "/media/maps/");
            } while(!var7.exists());

            var8 = var7.list();
         } while(var8 == null);

         for(int var9 = 0; var9 < var8.length; ++var9) {
            String var10 = var8[var9];
            if (var10.equalsIgnoreCase("challengemaps")) {
               if (var3) {
               }
            } else {
               String var10002 = var6.getDir();
               this.handleMapDirectory(var10, var10002 + "/media/maps/" + var10);
            }
         }
      }
   }

   private void getDirsRecursively(MapGroups.MapDirectory var1, ArrayList var2) {
      if (!var2.contains(var1)) {
         var2.add(var1);
         Iterator var3 = var1.lotDirs.iterator();

         while(true) {
            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               Iterator var5 = this.realDirectories.iterator();

               while(var5.hasNext()) {
                  MapGroups.MapDirectory var6 = (MapGroups.MapDirectory)var5.next();
                  if (var6.name.equals(var4)) {
                     this.getDirsRecursively(var6, var2);
                     break;
                  }
               }
            }

            return;
         }
      }
   }

   public int getNumberOfGroups() {
      return this.groups.size();
   }

   public ArrayList getMapDirectoriesInGroup(int var1) {
      if (var1 >= 0 && var1 < this.groups.size()) {
         ArrayList var2 = new ArrayList();
         Iterator var3 = ((MapGroups.MapGroup)this.groups.get(var1)).directories.iterator();

         while(var3.hasNext()) {
            MapGroups.MapDirectory var4 = (MapGroups.MapDirectory)var3.next();
            var2.add(var4.name);
         }

         return var2;
      } else {
         throw new RuntimeException("invalid MapGroups index " + var1);
      }
   }

   public void setWorld(int var1) {
      ArrayList var2 = this.getMapDirectoriesInGroup(var1);
      String var3 = "";

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         var3 = var3 + (String)var2.get(var4);
         if (var4 < var2.size() - 1) {
            var3 = var3 + ";";
         }
      }

      IsoWorld.instance.setMap(var3);
   }

   private void handleMapDirectory(String var1, String var2) {
      ArrayList var3 = this.getLotDirectories(var2);
      if (var3 != null) {
         MapGroups.MapDirectory var4 = new MapGroups.MapDirectory(var1, var2, var3);
         this.realDirectories.add(var4);
      }
   }

   private ArrayList getLotDirectories(String var1) {
      File var2 = new File(var1 + "/map.info");
      if (!var2.exists()) {
         return null;
      } else {
         ArrayList var3 = new ArrayList();

         try {
            FileReader var4 = new FileReader(var2.getAbsolutePath());

            try {
               BufferedReader var5 = new BufferedReader(var4);

               String var6;
               try {
                  while((var6 = var5.readLine()) != null) {
                     var6 = var6.trim();
                     if (var6.startsWith("lots=")) {
                        var3.add(var6.replace("lots=", "").trim());
                     }
                  }
               } catch (Throwable var10) {
                  try {
                     var5.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }

                  throw var10;
               }

               var5.close();
            } catch (Throwable var11) {
               try {
                  var4.close();
               } catch (Throwable var8) {
                  var11.addSuppressed(var8);
               }

               throw var11;
            }

            var4.close();
            return var3;
         } catch (Exception var12) {
            ExceptionLogger.logException(var12);
            return null;
         }
      }
   }

   private MapGroups.MapGroup findGroupWithAnyOfTheseDirectories(ArrayList var1) {
      Iterator var2 = this.groups.iterator();

      MapGroups.MapGroup var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MapGroups.MapGroup)var2.next();
      } while(!var3.hasAnyOfTheseDirectories(var1));

      return var3;
   }

   public ArrayList getAllMapsInOrder() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.groups.iterator();

      while(var2.hasNext()) {
         MapGroups.MapGroup var3 = (MapGroups.MapGroup)var2.next();
         Iterator var4 = var3.directories.iterator();

         while(var4.hasNext()) {
            MapGroups.MapDirectory var5 = (MapGroups.MapDirectory)var4.next();
            var1.add(var5.name);
         }
      }

      return var1;
   }

   public boolean checkMapConflicts() {
      boolean var1 = false;

      MapGroups.MapGroup var3;
      for(Iterator var2 = this.groups.iterator(); var2.hasNext(); var1 |= var3.checkMapConflicts()) {
         var3 = (MapGroups.MapGroup)var2.next();
      }

      return var1;
   }

   public ArrayList getMapConflicts(String var1) {
      Iterator var2 = this.groups.iterator();

      MapGroups.MapDirectory var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         MapGroups.MapGroup var3 = (MapGroups.MapGroup)var2.next();
         var4 = var3.getDirectoryByName(var1);
      } while(var4 == null);

      ArrayList var5 = new ArrayList();
      var5.addAll(var4.conflicts);
      return var5;
   }

   private class MapDirectory {
      String name;
      String path;
      ArrayList lotDirs = new ArrayList();
      ArrayList conflicts = new ArrayList();

      public MapDirectory(String var2, String var3) {
         this.name = var2;
         this.path = var3;
      }

      public MapDirectory(String var2, String var3, ArrayList var4) {
         this.name = var2;
         this.path = var3;
         this.lotDirs.addAll(var4);
      }

      public void getLotHeaders(ArrayList var1) {
         File var2 = new File(this.path);
         if (var2.isDirectory()) {
            String[] var3 = var2.list();
            if (var3 != null) {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  if (var3[var4].endsWith(".lotheader")) {
                     var1.add(var3[var4]);
                  }
               }

            }
         }
      }
   }

   private class MapGroup {
      private LinkedList directories = new LinkedList();

      void addDirectory(String var1, String var2) {
         assert !this.hasDirectory(var1);

         MapGroups.MapDirectory var3 = MapGroups.this.new MapDirectory(var1, var2);
         this.directories.add(var3);
      }

      void addDirectory(String var1, String var2, ArrayList var3) {
         assert !this.hasDirectory(var1);

         MapGroups.MapDirectory var4 = MapGroups.this.new MapDirectory(var1, var2, var3);
         this.directories.add(var4);
      }

      void addDirectory(MapGroups.MapDirectory var1) {
         assert !this.hasDirectory(var1.name);

         this.directories.add(var1);
      }

      MapGroups.MapDirectory getDirectoryByName(String var1) {
         Iterator var2 = this.directories.iterator();

         MapGroups.MapDirectory var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (MapGroups.MapDirectory)var2.next();
         } while(!var3.name.equals(var1));

         return var3;
      }

      boolean hasDirectory(String var1) {
         return this.getDirectoryByName(var1) != null;
      }

      boolean hasAnyOfTheseDirectories(ArrayList var1) {
         Iterator var2 = var1.iterator();

         MapGroups.MapDirectory var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (MapGroups.MapDirectory)var2.next();
         } while(!this.directories.contains(var3));

         return true;
      }

      boolean isReferencedByOtherMaps(MapGroups.MapDirectory var1) {
         Iterator var2 = this.directories.iterator();

         MapGroups.MapDirectory var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (MapGroups.MapDirectory)var2.next();
         } while(var1 == var3 || !var3.lotDirs.contains(var1.name));

         return true;
      }

      void getDirsRecursively(MapGroups.MapDirectory var1, ArrayList var2) {
         if (!var2.contains(var1.name)) {
            var2.add(var1.name);
            Iterator var3 = var1.lotDirs.iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               MapGroups.MapDirectory var5 = this.getDirectoryByName(var4);
               if (var5 != null) {
                  this.getDirsRecursively(var5, var2);
               }
            }

         }
      }

      void setPriority() {
         ArrayList var1 = new ArrayList(this.directories);
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            MapGroups.MapDirectory var3 = (MapGroups.MapDirectory)var2.next();
            if (!this.isReferencedByOtherMaps(var3)) {
               ArrayList var4 = new ArrayList();
               this.getDirsRecursively(var3, var4);
               this.setPriority(var4);
            }
         }

      }

      void setPriority(List var1) {
         ArrayList var2 = new ArrayList(var1.size());
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (this.hasDirectory(var4)) {
               var2.add(this.getDirectoryByName(var4));
            }
         }

         for(int var5 = 0; var5 < this.directories.size(); ++var5) {
            MapGroups.MapDirectory var6 = (MapGroups.MapDirectory)this.directories.get(var5);
            if (var1.contains(var6.name)) {
               this.directories.set(var5, (MapGroups.MapDirectory)var2.remove(0));
            }
         }

      }

      void setOrder(ActiveMods var1) {
         if (!var1.getMapOrder().isEmpty()) {
            this.setPriority(var1.getMapOrder());
         }

      }

      boolean checkMapConflicts() {
         HashMap var1 = new HashMap();
         ArrayList var2 = new ArrayList();
         Iterator var3 = this.directories.iterator();

         while(var3.hasNext()) {
            MapGroups.MapDirectory var4 = (MapGroups.MapDirectory)var3.next();
            var4.conflicts.clear();
            var2.clear();
            var4.getLotHeaders(var2);

            String var6;
            for(Iterator var5 = var2.iterator(); var5.hasNext(); ((ArrayList)var1.get(var6)).add(var4.name)) {
               var6 = (String)var5.next();
               if (!var1.containsKey(var6)) {
                  var1.put(var6, new ArrayList());
               }
            }
         }

         boolean var11 = false;
         Iterator var12 = var1.keySet().iterator();

         while(true) {
            String var13;
            ArrayList var14;
            do {
               if (!var12.hasNext()) {
                  return var11;
               }

               var13 = (String)var12.next();
               var14 = (ArrayList)var1.get(var13);
            } while(var14.size() <= 1);

            for(int var7 = 0; var7 < var14.size(); ++var7) {
               MapGroups.MapDirectory var8 = this.getDirectoryByName((String)var14.get(var7));

               for(int var9 = 0; var9 < var14.size(); ++var9) {
                  if (var7 != var9) {
                     String var10 = Translator.getText("UI_MapConflict", var8.name, var14.get(var9), var13);
                     var8.conflicts.add(var10);
                     var11 = true;
                  }
               }
            }
         }
      }
   }
}
