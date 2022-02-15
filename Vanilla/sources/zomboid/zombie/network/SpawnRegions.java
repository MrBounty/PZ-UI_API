package zombie.network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.DirectoryStream.Filter;
import java.util.ArrayList;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import se.krka.kahlua.vm.LuaClosure;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaManager;
import zombie.debug.DebugLog;

public class SpawnRegions {
   private SpawnRegions.Region parseRegionTable(KahluaTable var1) {
      Object var2 = var1.rawget("name");
      Object var3 = var1.rawget("file");
      Object var4 = var1.rawget("serverfile");
      SpawnRegions.Region var5;
      if (var2 instanceof String && var3 instanceof String) {
         var5 = new SpawnRegions.Region();
         var5.name = (String)var2;
         var5.file = (String)var3;
         return var5;
      } else if (var2 instanceof String && var4 instanceof String) {
         var5 = new SpawnRegions.Region();
         var5.name = (String)var2;
         var5.serverfile = (String)var4;
         return var5;
      } else {
         return null;
      }
   }

   private ArrayList parseProfessionsTable(KahluaTable var1) {
      ArrayList var2 = null;
      KahluaTableIterator var3 = var1.iterator();

      while(var3.advance()) {
         Object var4 = var3.getKey();
         Object var5 = var3.getValue();
         if (var4 instanceof String && var5 instanceof KahluaTable) {
            ArrayList var6 = this.parsePointsTable((KahluaTable)var5);
            if (var6 != null) {
               SpawnRegions.Profession var7 = new SpawnRegions.Profession();
               var7.name = (String)var4;
               var7.points = var6;
               if (var2 == null) {
                  var2 = new ArrayList();
               }

               var2.add(var7);
            }
         }
      }

      return var2;
   }

   private ArrayList parsePointsTable(KahluaTable var1) {
      ArrayList var2 = null;
      KahluaTableIterator var3 = var1.iterator();

      while(var3.advance()) {
         Object var4 = var3.getValue();
         if (var4 instanceof KahluaTable) {
            SpawnRegions.Point var5 = this.parsePointTable((KahluaTable)var4);
            if (var5 != null) {
               if (var2 == null) {
                  var2 = new ArrayList();
               }

               var2.add(var5);
            }
         }
      }

      return var2;
   }

   private SpawnRegions.Point parsePointTable(KahluaTable var1) {
      Object var2 = var1.rawget("worldX");
      Object var3 = var1.rawget("worldY");
      Object var4 = var1.rawget("posX");
      Object var5 = var1.rawget("posY");
      Object var6 = var1.rawget("posZ");
      if (var2 instanceof Double && var3 instanceof Double && var4 instanceof Double && var5 instanceof Double) {
         SpawnRegions.Point var7 = new SpawnRegions.Point();
         var7.worldX = ((Double)var2).intValue();
         var7.worldY = ((Double)var3).intValue();
         var7.posX = ((Double)var4).intValue();
         var7.posY = ((Double)var5).intValue();
         var7.posZ = var6 instanceof Double ? ((Double)var6).intValue() : 0;
         return var7;
      } else {
         return null;
      }
   }

   public ArrayList loadRegionsFile(String var1) {
      File var2 = new File(var1);
      if (!var2.exists()) {
         return null;
      } else {
         try {
            LuaManager.env.rawset("SpawnRegions", (Object)null);
            LuaManager.loaded.remove(var2.getAbsolutePath().replace("\\", "/"));
            LuaManager.RunLua(var2.getAbsolutePath());
            Object var3 = LuaManager.env.rawget("SpawnRegions");
            if (var3 instanceof LuaClosure) {
               Object[] var4 = LuaManager.caller.pcall(LuaManager.thread, var3);
               if (var4.length > 1 && var4[1] instanceof KahluaTable) {
                  ArrayList var5 = new ArrayList();
                  KahluaTableIterator var6 = ((KahluaTable)var4[1]).iterator();

                  while(var6.advance()) {
                     Object var7 = var6.getValue();
                     if (var7 instanceof KahluaTable) {
                        SpawnRegions.Region var8 = this.parseRegionTable((KahluaTable)var7);
                        if (var8 != null) {
                           var5.add(var8);
                        }
                     }
                  }

                  return var5;
               }
            }

            return null;
         } catch (Exception var9) {
            var9.printStackTrace();
            return null;
         }
      }
   }

   private String fmtKey(String var1) {
      if (var1.contains("\\")) {
         var1 = var1.replace("\\", "\\\\");
      }

      if (var1.contains("\"")) {
         var1 = var1.replace("\"", "\\\"");
      }

      if (var1.contains(" ") || var1.contains("\\")) {
         var1 = "\"" + var1 + "\"";
      }

      return var1.startsWith("\"") ? "[" + var1 + "]" : var1;
   }

   private String fmtValue(String var1) {
      if (var1.contains("\\")) {
         var1 = var1.replace("\\", "\\\\");
      }

      if (var1.contains("\"")) {
         var1 = var1.replace("\"", "\\\"");
      }

      return "\"" + var1 + "\"";
   }

   public boolean saveRegionsFile(String var1, ArrayList var2) {
      File var3 = new File(var1);
      DebugLog.log("writing " + var1);

      try {
         FileWriter var4 = new FileWriter(var3);

         boolean var15;
         try {
            String var5 = System.lineSeparator();
            var4.write("function SpawnRegions()" + var5);
            var4.write("\treturn {" + var5);
            Iterator var6 = var2.iterator();

            while(true) {
               while(var6.hasNext()) {
                  SpawnRegions.Region var7 = (SpawnRegions.Region)var6.next();
                  String var10001;
                  if (var7.file != null) {
                     var10001 = this.fmtValue(var7.name);
                     var4.write("\t\t{ name = " + var10001 + ", file = " + this.fmtValue(var7.file) + " }," + var5);
                  } else if (var7.serverfile != null) {
                     var10001 = this.fmtValue(var7.name);
                     var4.write("\t\t{ name = " + var10001 + ", serverfile = " + this.fmtValue(var7.serverfile) + " }," + var5);
                  } else if (var7.professions != null) {
                     var10001 = this.fmtValue(var7.name);
                     var4.write("\t\t{ name = " + var10001 + "," + var5);
                     var4.write("\t\t\tpoints = {" + var5);
                     Iterator var8 = var7.professions.iterator();

                     while(var8.hasNext()) {
                        SpawnRegions.Profession var9 = (SpawnRegions.Profession)var8.next();
                        var10001 = this.fmtKey(var9.name);
                        var4.write("\t\t\t\t" + var10001 + " = {" + var5);
                        Iterator var10 = var9.points.iterator();

                        while(var10.hasNext()) {
                           SpawnRegions.Point var11 = (SpawnRegions.Point)var10.next();
                           var4.write("\t\t\t\t\t{ worldX = " + var11.worldX + ", worldY = " + var11.worldY + ", posX = " + var11.posX + ", posY = " + var11.posY + ", posZ = " + var11.posZ + " }," + var5);
                        }

                        var4.write("\t\t\t\t}," + var5);
                     }

                     var4.write("\t\t\t}" + var5);
                     var4.write("\t\t}," + var5);
                  }
               }

               var4.write("\t}" + var5);
               var4.write("end" + System.lineSeparator());
               var15 = true;
               break;
            }
         } catch (Throwable var13) {
            try {
               var4.close();
            } catch (Throwable var12) {
               var13.addSuppressed(var12);
            }

            throw var13;
         }

         var4.close();
         return var15;
      } catch (Exception var14) {
         var14.printStackTrace();
         return false;
      }
   }

   public ArrayList loadPointsFile(String var1) {
      File var2 = new File(var1);
      if (!var2.exists()) {
         return null;
      } else {
         try {
            LuaManager.env.rawset("SpawnPoints", (Object)null);
            LuaManager.loaded.remove(var2.getAbsolutePath().replace("\\", "/"));
            LuaManager.RunLua(var2.getAbsolutePath());
            Object var3 = LuaManager.env.rawget("SpawnPoints");
            if (var3 instanceof LuaClosure) {
               Object[] var4 = LuaManager.caller.pcall(LuaManager.thread, var3);
               if (var4.length > 1 && var4[1] instanceof KahluaTable) {
                  ArrayList var5 = this.parseProfessionsTable((KahluaTable)var4[1]);
                  return var5;
               }
            }

            return null;
         } catch (Exception var6) {
            var6.printStackTrace();
            return null;
         }
      }
   }

   public boolean savePointsFile(String var1, ArrayList var2) {
      File var3 = new File(var1);
      DebugLog.log("writing " + var1);

      try {
         FileWriter var4 = new FileWriter(var3);

         boolean var13;
         try {
            String var5 = System.lineSeparator();
            var4.write("function SpawnPoints()" + var5);
            var4.write("\treturn {" + var5);
            Iterator var6 = var2.iterator();

            while(true) {
               if (!var6.hasNext()) {
                  var4.write("\t}" + var5);
                  var4.write("end" + System.lineSeparator());
                  var13 = true;
                  break;
               }

               SpawnRegions.Profession var7 = (SpawnRegions.Profession)var6.next();
               String var10001 = this.fmtKey(var7.name);
               var4.write("\t\t" + var10001 + " = {" + var5);
               Iterator var8 = var7.points.iterator();

               while(var8.hasNext()) {
                  SpawnRegions.Point var9 = (SpawnRegions.Point)var8.next();
                  var4.write("\t\t\t{ worldX = " + var9.worldX + ", worldY = " + var9.worldY + ", posX = " + var9.posX + ", posY = " + var9.posY + ", posZ = " + var9.posZ + " }," + var5);
               }

               var4.write("\t\t}," + var5);
            }
         } catch (Throwable var11) {
            try {
               var4.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }

            throw var11;
         }

         var4.close();
         return var13;
      } catch (Exception var12) {
         var12.printStackTrace();
         return false;
      }
   }

   public KahluaTable loadPointsTable(String var1) {
      ArrayList var2 = this.loadPointsFile(var1);
      if (var2 == null) {
         return null;
      } else {
         KahluaTable var3 = LuaManager.platform.newTable();

         for(int var4 = 0; var4 < var2.size(); ++var4) {
            SpawnRegions.Profession var5 = (SpawnRegions.Profession)var2.get(var4);
            KahluaTable var6 = LuaManager.platform.newTable();

            for(int var7 = 0; var7 < var5.points.size(); ++var7) {
               SpawnRegions.Point var8 = (SpawnRegions.Point)var5.points.get(var7);
               KahluaTable var9 = LuaManager.platform.newTable();
               var9.rawset("worldX", (double)var8.worldX);
               var9.rawset("worldY", (double)var8.worldY);
               var9.rawset("posX", (double)var8.posX);
               var9.rawset("posY", (double)var8.posY);
               var9.rawset("posZ", (double)var8.posZ);
               var6.rawset(var7 + 1, var9);
            }

            var3.rawset(var5.name, var6);
         }

         return var3;
      }
   }

   public boolean savePointsTable(String var1, KahluaTable var2) {
      ArrayList var3 = this.parseProfessionsTable(var2);
      return var3 != null ? this.savePointsFile(var1, var3) : false;
   }

   public ArrayList getDefaultServerRegions() {
      ArrayList var1 = new ArrayList();
      Filter var2 = new Filter() {
         public boolean accept(Path var1) throws IOException {
            return Files.isDirectory(var1, new LinkOption[0]) && Files.exists(var1.resolve("spawnpoints.lua"), new LinkOption[0]);
         }
      };
      String var3 = ZomboidFileSystem.instance.getMediaPath("maps");
      Path var4 = FileSystems.getDefault().getPath(var3);
      if (!Files.exists(var4, new LinkOption[0])) {
         return var1;
      } else {
         try {
            DirectoryStream var5 = Files.newDirectoryStream(var4, var2);

            try {
               Iterator var6 = var5.iterator();

               while(var6.hasNext()) {
                  Path var7 = (Path)var6.next();
                  SpawnRegions.Region var8 = new SpawnRegions.Region();
                  var8.name = var7.getFileName().toString();
                  var8.file = "media/maps/" + var8.name + "/spawnpoints.lua";
                  var1.add(var8);
               }
            } catch (Throwable var10) {
               if (var5 != null) {
                  try {
                     var5.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (var5 != null) {
               var5.close();
            }
         } catch (Exception var11) {
            var11.printStackTrace();
         }

         return var1;
      }
   }

   public ArrayList getDefaultServerPoints() {
      ArrayList var1 = new ArrayList();
      SpawnRegions.Profession var2 = new SpawnRegions.Profession();
      var2.name = "unemployed";
      var2.points = new ArrayList();
      var1.add(var2);
      SpawnRegions.Point var3 = new SpawnRegions.Point();
      var3.worldX = 40;
      var3.worldY = 22;
      var3.posX = 67;
      var3.posY = 201;
      var3.posZ = 0;
      var2.points.add(var3);
      return var1;
   }

   public static class Region {
      public String name;
      public String file;
      public String serverfile;
      public ArrayList professions;
   }

   public static class Profession {
      public String name;
      public ArrayList points;
   }

   public static class Point {
      public int worldX;
      public int worldY;
      public int posX;
      public int posY;
      public int posZ;
   }
}
