package zombie.gameStates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombie.ZomboidFileSystem;
import zombie.core.Core;
import zombie.core.GameVersion;
import zombie.core.IndieFileLoader;
import zombie.core.Language;
import zombie.core.Translator;
import zombie.core.logger.ExceptionLogger;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.core.znet.SteamWorkshop;
import zombie.debug.DebugLog;
import zombie.util.StringUtils;

public final class ChooseGameInfo {
   private static final HashMap Maps = new HashMap();
   private static final HashMap Mods = new HashMap();
   private static final HashSet MissingMods = new HashSet();
   private static final ArrayList tempStrings = new ArrayList();

   private ChooseGameInfo() {
   }

   public static void Reset() {
      Maps.clear();
      Mods.clear();
      MissingMods.clear();
   }

   private static void readTitleDotTxt(ChooseGameInfo.Map var0, String var1, Language var2) throws IOException {
      String var10000 = var2.toString();
      String var3 = "media/lua/shared/Translate/" + var10000 + "/" + var1 + "/title.txt";
      File var4 = new File(ZomboidFileSystem.instance.getString(var3));

      try {
         FileInputStream var5 = new FileInputStream(var4);

         try {
            InputStreamReader var6 = new InputStreamReader(var5, Charset.forName(var2.charset()));

            try {
               BufferedReader var7 = new BufferedReader(var6);

               try {
                  String var8 = var7.readLine();
                  var8 = StringUtils.stripBOM(var8);
                  if (!StringUtils.isNullOrWhitespace(var8)) {
                     var0.title = var8.trim();
                  }
               } catch (Throwable var13) {
                  try {
                     var7.close();
                  } catch (Throwable var12) {
                     var13.addSuppressed(var12);
                  }

                  throw var13;
               }

               var7.close();
            } catch (Throwable var14) {
               try {
                  var6.close();
               } catch (Throwable var11) {
                  var14.addSuppressed(var11);
               }

               throw var14;
            }

            var6.close();
         } catch (Throwable var15) {
            try {
               var5.close();
            } catch (Throwable var10) {
               var15.addSuppressed(var10);
            }

            throw var15;
         }

         var5.close();
      } catch (FileNotFoundException var16) {
      }

   }

   private static void readDescriptionDotTxt(ChooseGameInfo.Map var0, String var1, Language var2) throws IOException {
      String var10000 = var2.toString();
      String var3 = "media/lua/shared/Translate/" + var10000 + "/" + var1 + "/description.txt";
      File var4 = new File(ZomboidFileSystem.instance.getString(var3));

      try {
         FileInputStream var5 = new FileInputStream(var4);

         try {
            InputStreamReader var6 = new InputStreamReader(var5, Charset.forName(var2.charset()));

            try {
               BufferedReader var7 = new BufferedReader(var6);

               try {
                  var0.desc = "";

                  String var8;
                  for(boolean var9 = true; (var8 = var7.readLine()) != null; var0.desc = var0.desc + var8) {
                     if (var9) {
                        var8 = StringUtils.stripBOM(var8);
                        var9 = false;
                     }
                  }
               } catch (Throwable var13) {
                  try {
                     var7.close();
                  } catch (Throwable var12) {
                     var13.addSuppressed(var12);
                  }

                  throw var13;
               }

               var7.close();
            } catch (Throwable var14) {
               try {
                  var6.close();
               } catch (Throwable var11) {
                  var14.addSuppressed(var11);
               }

               throw var14;
            }

            var6.close();
         } catch (Throwable var15) {
            try {
               var5.close();
            } catch (Throwable var10) {
               var15.addSuppressed(var10);
            }

            throw var15;
         }

         var5.close();
      } catch (FileNotFoundException var16) {
      }

   }

   public static ChooseGameInfo.Map getMapDetails(String var0) {
      if (Maps.containsKey(var0)) {
         return (ChooseGameInfo.Map)Maps.get(var0);
      } else {
         File var1 = new File(ZomboidFileSystem.instance.getString("media/maps/" + var0 + "/map.info"));
         if (!var1.exists()) {
            return null;
         } else {
            ChooseGameInfo.Map var2 = new ChooseGameInfo.Map();
            var2.dir = (new File(var1.getParent())).getAbsolutePath();
            var2.title = var0;
            var2.lotsDir = new ArrayList();

            try {
               FileReader var3 = new FileReader(var1.getAbsolutePath());
               BufferedReader var4 = new BufferedReader(var3);
               String var5 = null;

               try {
                  while((var5 = var4.readLine()) != null) {
                     var5 = var5.trim();
                     if (var5.startsWith("title=")) {
                        var2.title = var5.replace("title=", "");
                     } else if (var5.startsWith("lots=")) {
                        var2.lotsDir.add(var5.replace("lots=", "").trim());
                     } else if (var5.startsWith("description=")) {
                        if (var2.desc == null) {
                           var2.desc = "";
                        }

                        String var10001 = var2.desc;
                        var2.desc = var10001 + var5.replace("description=", "");
                     } else if (var5.startsWith("fixed2x=")) {
                        var2.bFixed2x = Boolean.parseBoolean(var5.replace("fixed2x=", "").trim());
                     }
                  }
               } catch (IOException var9) {
                  Logger.getLogger(ChooseGameInfo.class.getName()).log(Level.SEVERE, (String)null, var9);
               }

               var4.close();
               var2.thumb = Texture.getSharedTexture(var2.dir + "/thumb.png");
               ArrayList var6 = new ArrayList();
               Translator.addLanguageToList(Translator.getLanguage(), var6);
               Translator.addLanguageToList(Translator.getDefaultLanguage(), var6);

               for(int var7 = var6.size() - 1; var7 >= 0; --var7) {
                  Language var8 = (Language)var6.get(var7);
                  readTitleDotTxt(var2, var0, var8);
                  readDescriptionDotTxt(var2, var0, var8);
               }
            } catch (Exception var10) {
               ExceptionLogger.logException(var10);
               return null;
            }

            Maps.put(var0, var2);
            return var2;
         }
      }
   }

   public static ChooseGameInfo.Mod getModDetails(String var0) {
      if (MissingMods.contains(var0)) {
         return null;
      } else if (Mods.containsKey(var0)) {
         return (ChooseGameInfo.Mod)Mods.get(var0);
      } else {
         String var1 = ZomboidFileSystem.instance.getModDir(var0);
         if (var1 == null) {
            ArrayList var2 = tempStrings;
            ZomboidFileSystem.instance.getAllModFolders(var2);
            ArrayList var3 = new ArrayList();

            for(int var4 = 0; var4 < var2.size(); ++var4) {
               File var5 = new File((String)var2.get(var4), "mod.info");
               var3.clear();
               ChooseGameInfo.Mod var6 = ZomboidFileSystem.instance.searchForModInfo(var5, var0, var3);

               for(int var7 = 0; var7 < var3.size(); ++var7) {
                  ChooseGameInfo.Mod var8 = (ChooseGameInfo.Mod)var3.get(var7);
                  Mods.putIfAbsent(var8.getId(), var8);
               }

               if (var6 != null) {
                  return var6;
               }
            }
         }

         ChooseGameInfo.Mod var9 = readModInfo(var1);
         if (var9 == null) {
            MissingMods.add(var0);
         }

         return var9;
      }
   }

   public static ChooseGameInfo.Mod getAvailableModDetails(String var0) {
      ChooseGameInfo.Mod var1 = getModDetails(var0);
      return var1 != null && var1.isAvailable() ? var1 : null;
   }

   public static ChooseGameInfo.Mod readModInfo(String var0) {
      ChooseGameInfo.Mod var1 = readModInfoAux(var0);
      if (var1 != null) {
         ChooseGameInfo.Mod var2 = (ChooseGameInfo.Mod)Mods.get(var1.getId());
         if (var2 == null) {
            Mods.put(var1.getId(), var1);
         } else if (var2 != var1) {
            ZomboidFileSystem.instance.getAllModFolders(tempStrings);
            int var3 = tempStrings.indexOf(var1.getDir());
            int var4 = tempStrings.indexOf(var2.getDir());
            if (var3 < var4) {
               Mods.put(var1.getId(), var1);
            }
         }
      }

      return var1;
   }

   private static ChooseGameInfo.Mod readModInfoAux(String var0) {
      if (var0 != null) {
         ChooseGameInfo.Mod var1 = ZomboidFileSystem.instance.getModInfoForDir(var0);
         if (var1.bRead) {
            return var1.bValid ? var1 : null;
         }

         var1.bRead = true;
         String var2 = var0 + File.separator + "mod.info";
         File var3 = new File(var2);
         if (!var3.exists()) {
            DebugLog.Mod.warn("can't find \"" + var2 + "\"");
            return null;
         }

         var1.setId(var3.getParentFile().getName());

         try {
            InputStreamReader var4 = IndieFileLoader.getStreamReader(var2);

            ChooseGameInfo.Mod var24;
            label272: {
               Object var23;
               label273: {
                  label274: {
                     Object var11;
                     label275: {
                        String var8;
                        label276: {
                           try {
                              label277: {
                                 BufferedReader var5 = new BufferedReader(var4);

                                 label251: {
                                    label250: {
                                       label249: {
                                          label248: {
                                             label247: {
                                                try {
                                                   label245:
                                                   while(true) {
                                                      while(true) {
                                                         String var6;
                                                         while((var6 = var5.readLine()) != null) {
                                                            if (!var6.contains("name=")) {
                                                               String var21;
                                                               if (var6.contains("poster=")) {
                                                                  var21 = var6.replace("poster=", "");
                                                                  if (!StringUtils.isNullOrWhitespace(var21)) {
                                                                     var1.posters.add(var0 + File.separator + var21);
                                                                  }
                                                               } else if (var6.contains("description=")) {
                                                                  String var10001 = var1.desc;
                                                                  var1.desc = var10001 + var6.replace("description=", "");
                                                               } else if (var6.contains("require=")) {
                                                                  var1.setRequire(new ArrayList(Arrays.asList(var6.replace("require=", "").split(","))));
                                                               } else if (var6.contains("id=")) {
                                                                  var1.setId(var6.replace("id=", ""));
                                                               } else if (var6.contains("url=")) {
                                                                  var1.setUrl(var6.replace("url=", ""));
                                                               } else {
                                                                  int var9;
                                                                  if (var6.contains("pack=")) {
                                                                     var21 = var6.replace("pack=", "").trim();
                                                                     if (!var21.isEmpty()) {
                                                                        int var22 = TextureID.bUseCompressionOption ? 4 : 0;
                                                                        var22 |= 64;
                                                                        var9 = var21.indexOf("type=");
                                                                        if (var9 != -1) {
                                                                           String var10 = var21.substring(var9 + "type=".length());
                                                                           byte var12 = -1;
                                                                           switch(var10.hashCode()) {
                                                                           case 3732:
                                                                              if (var10.equals("ui")) {
                                                                                 var12 = 0;
                                                                              }
                                                                           default:
                                                                              switch(var12) {
                                                                              case 0:
                                                                                 var22 = 2;
                                                                                 break;
                                                                              default:
                                                                                 DebugLog.Mod.error("unknown pack type=" + var10);
                                                                              }

                                                                              int var26 = var21.indexOf(32);
                                                                              var21 = var21.substring(0, var26).trim();
                                                                           }
                                                                        }

                                                                        if (var21.endsWith(".floor")) {
                                                                           var22 &= -5;
                                                                        }

                                                                        if (Core.TileScale == 2) {
                                                                           File var25 = new File(var0 + File.separator + "media" + File.separator + "texturepacks" + File.separator + var21 + "2x.pack");
                                                                           if (var25.isFile()) {
                                                                              DebugLog.Mod.printf("2x version of %s.pack found.\n", var21);
                                                                              var21 = var21 + "2x";
                                                                           } else {
                                                                              DebugLog.Mod.printf("2x version of %s.pack not found.\n", var21);
                                                                           }
                                                                        }

                                                                        var1.addPack(var21, var22);
                                                                     } else {
                                                                        DebugLog.Mod.error("pack= line requires a file name");
                                                                        var8 = null;
                                                                        break label249;
                                                                     }
                                                                  } else if (!var6.contains("tiledef=")) {
                                                                     if (var6.startsWith("versionMax=")) {
                                                                        var21 = var6.replace("versionMax=", "").trim();
                                                                        if (!var21.isEmpty()) {
                                                                           try {
                                                                              var1.versionMax = GameVersion.parse(var21);
                                                                           } catch (Exception var15) {
                                                                              DebugLog.Mod.error("invalid versionMax: " + var15.getMessage());
                                                                              var23 = null;
                                                                              break label251;
                                                                           }
                                                                        }
                                                                     } else if (var6.startsWith("versionMin=")) {
                                                                        var21 = var6.replace("versionMin=", "").trim();
                                                                        if (!var21.isEmpty()) {
                                                                           try {
                                                                              var1.versionMin = GameVersion.parse(var21);
                                                                           } catch (Exception var16) {
                                                                              DebugLog.Mod.error("invalid versionMin: " + var16.getMessage());
                                                                              var23 = null;
                                                                              break label250;
                                                                           }
                                                                        }
                                                                     }
                                                                  } else {
                                                                     String[] var7 = var6.replace("tiledef=", "").trim().split("\\s+");
                                                                     if (var7.length != 2) {
                                                                        DebugLog.Mod.error("tiledef= line requires file name and file number");
                                                                        var8 = null;
                                                                        break label247;
                                                                     }

                                                                     var8 = var7[0];

                                                                     try {
                                                                        var9 = Integer.parseInt(var7[1]);
                                                                     } catch (NumberFormatException var17) {
                                                                        DebugLog.Mod.error("tiledef= line requires file name and file number");
                                                                        var11 = null;
                                                                        break label245;
                                                                     }

                                                                     if (var9 < 100 || var9 > 1000) {
                                                                        DebugLog.Mod.error("tiledef=%s %d file number must be from 100 to 1000", var8, var9);
                                                                     }

                                                                     var1.addTileDef(var8, var9);
                                                                  }
                                                               }
                                                            } else {
                                                               var1.name = var6.replace("name=", "");
                                                            }
                                                         }

                                                         if (var1.getUrl() == null) {
                                                            var1.setUrl("");
                                                         }

                                                         var1.bValid = true;
                                                         var24 = var1;
                                                         break label248;
                                                      }
                                                   }
                                                } catch (Throwable var18) {
                                                   try {
                                                      var5.close();
                                                   } catch (Throwable var14) {
                                                      var18.addSuppressed(var14);
                                                   }

                                                   throw var18;
                                                }

                                                var5.close();
                                                break label275;
                                             }

                                             var5.close();
                                             break label276;
                                          }

                                          var5.close();
                                          break label272;
                                       }

                                       var5.close();
                                       break label277;
                                    }

                                    var5.close();
                                    break label273;
                                 }

                                 var5.close();
                                 break label274;
                              }
                           } catch (Throwable var19) {
                              if (var4 != null) {
                                 try {
                                    var4.close();
                                 } catch (Throwable var13) {
                                    var19.addSuppressed(var13);
                                 }
                              }

                              throw var19;
                           }

                           if (var4 != null) {
                              var4.close();
                           }

                           return var8;
                        }

                        if (var4 != null) {
                           var4.close();
                        }

                        return var8;
                     }

                     if (var4 != null) {
                        var4.close();
                     }

                     return (ChooseGameInfo.Mod)var11;
                  }

                  if (var4 != null) {
                     var4.close();
                  }

                  return (ChooseGameInfo.Mod)var23;
               }

               if (var4 != null) {
                  var4.close();
               }

               return (ChooseGameInfo.Mod)var23;
            }

            if (var4 != null) {
               var4.close();
            }

            return var24;
         } catch (Exception var20) {
            ExceptionLogger.logException(var20);
         }
      }

      return null;
   }

   public static final class Map {
      private String dir;
      private Texture thumb;
      private String title;
      private ArrayList lotsDir;
      private String desc;
      private boolean bFixed2x;

      public String getDirectory() {
         return this.dir;
      }

      public void setDirectory(String var1) {
         this.dir = var1;
      }

      public Texture getThumbnail() {
         return this.thumb;
      }

      public void setThumbnail(Texture var1) {
         this.thumb = var1;
      }

      public String getTitle() {
         return this.title;
      }

      public void setTitle(String var1) {
         this.title = var1;
      }

      public ArrayList getLotDirectories() {
         return this.lotsDir;
      }

      public String getDescription() {
         return this.desc;
      }

      public void setDescription(String var1) {
         this.desc = var1;
      }

      public boolean isFixed2x() {
         return this.bFixed2x;
      }

      public void setFixed2x(boolean var1) {
         this.bFixed2x = var1;
      }
   }

   public static final class Mod {
      public String dir;
      public final File baseFile;
      public final File mediaFile;
      public final File actionGroupsFile;
      public final File animSetsFile;
      public final File animsXFile;
      private final ArrayList posters = new ArrayList();
      public Texture tex;
      private ArrayList require;
      private String name = "Unnamed Mod";
      private String desc = "";
      private String id;
      private String url;
      private String workshopID;
      private boolean bAvailableDone = false;
      private boolean available = true;
      private GameVersion versionMin;
      private GameVersion versionMax;
      private final ArrayList packs = new ArrayList();
      private final ArrayList tileDefs = new ArrayList();
      private boolean bRead = false;
      private boolean bValid = false;

      public Mod(String var1) {
         this.dir = var1;
         File var2 = (new File(var1)).getAbsoluteFile();

         try {
            var2 = var2.getCanonicalFile();
         } catch (Exception var4) {
            ExceptionLogger.logException(var4);
         }

         this.baseFile = var2;
         this.mediaFile = new File(var2, "media");
         this.actionGroupsFile = new File(this.mediaFile, "actiongroups");
         this.animSetsFile = new File(this.mediaFile, "AnimSets");
         this.animsXFile = new File(this.mediaFile, "anims_X");
         File var3 = var2.getParentFile();
         if (var3 != null) {
            var3 = var3.getParentFile();
            if (var3 != null) {
               this.workshopID = SteamWorkshop.instance.getIDFromItemInstallFolder(var3.getAbsolutePath());
            }
         }

      }

      public Texture getTexture() {
         if (this.tex == null) {
            String var1 = this.posters.isEmpty() ? null : (String)this.posters.get(0);
            if (!StringUtils.isNullOrWhitespace(var1)) {
               this.tex = Texture.getSharedTexture(var1);
            }

            if (this.tex == null || this.tex.isFailure()) {
               if (Core.bDebug && this.tex == null) {
                  String var10001 = var1 == null ? this.id : var1;
                  DebugLog.Mod.println("failed to load poster " + var10001);
               }

               this.tex = Texture.getWhite();
            }
         }

         return this.tex;
      }

      public void setTexture(Texture var1) {
         this.tex = var1;
      }

      public int getPosterCount() {
         return this.posters.size();
      }

      public String getPoster(int var1) {
         return var1 >= 0 && var1 < this.posters.size() ? (String)this.posters.get(var1) : null;
      }

      public String getName() {
         return this.name;
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public String getDir() {
         return this.dir;
      }

      public String getDescription() {
         return this.desc;
      }

      public ArrayList getRequire() {
         return this.require;
      }

      public void setRequire(ArrayList var1) {
         this.require = var1;
      }

      public String getId() {
         return this.id;
      }

      public void setId(String var1) {
         this.id = var1;
      }

      public boolean isAvailable() {
         if (this.bAvailableDone) {
            return this.available;
         } else {
            this.bAvailableDone = true;
            if (!this.isAvailableSelf()) {
               this.available = false;
               return false;
            } else {
               ChooseGameInfo.tempStrings.clear();
               ChooseGameInfo.tempStrings.add(this.getId());
               if (!this.isAvailableRequired(ChooseGameInfo.tempStrings)) {
                  this.available = false;
                  return false;
               } else {
                  this.available = true;
                  return true;
               }
            }
         }
      }

      private boolean isAvailableSelf() {
         GameVersion var1 = Core.getInstance().getGameVersion();
         if (this.versionMin != null && this.versionMin.isGreaterThan(var1)) {
            return false;
         } else {
            return this.versionMax == null || !this.versionMax.isLessThan(var1);
         }
      }

      private boolean isAvailableRequired(ArrayList var1) {
         if (this.require != null && !this.require.isEmpty()) {
            for(int var2 = 0; var2 < this.require.size(); ++var2) {
               String var3 = ((String)this.require.get(var2)).trim();
               if (!var1.contains(var3)) {
                  var1.add(var3);
                  ChooseGameInfo.Mod var4 = ChooseGameInfo.getModDetails(var3);
                  if (var4 == null) {
                     return false;
                  }

                  if (!var4.isAvailableSelf()) {
                     return false;
                  }

                  if (!var4.isAvailableRequired(var1)) {
                     return false;
                  }
               }
            }

            return true;
         } else {
            return true;
         }
      }

      /** @deprecated */
      @Deprecated
      public void setAvailable(boolean var1) {
      }

      public String getUrl() {
         return this.url == null ? "" : this.url;
      }

      public void setUrl(String var1) {
         if (var1.startsWith("http://theindiestone.com") || var1.startsWith("http://www.theindiestone.com") || var1.startsWith("http://pz-mods.net") || var1.startsWith("http://www.pz-mods.net")) {
            this.url = var1;
         }

      }

      public GameVersion getVersionMin() {
         return this.versionMin;
      }

      public GameVersion getVersionMax() {
         return this.versionMax;
      }

      public void addPack(String var1, int var2) {
         this.packs.add(new ChooseGameInfo.PackFile(var1, var2));
      }

      public void addTileDef(String var1, int var2) {
         this.tileDefs.add(new ChooseGameInfo.TileDef(var1, var2));
      }

      public ArrayList getPacks() {
         return this.packs;
      }

      public ArrayList getTileDefs() {
         return this.tileDefs;
      }

      public String getWorkshopID() {
         return this.workshopID;
      }
   }

   public static final class TileDef {
      public String name;
      public int fileNumber;

      public TileDef(String var1, int var2) {
         this.name = var1;
         this.fileNumber = var2;
      }
   }

   public static final class PackFile {
      public final String name;
      public final int flags;

      public PackFile(String var1, int var2) {
         this.name = var1;
         this.flags = var2;
      }
   }

   public static final class SpawnOrigin {
      public int x;
      public int y;
      public int w;
      public int h;

      public SpawnOrigin(int var1, int var2, int var3, int var4) {
         this.x = var1;
         this.y = var2;
         this.w = var3;
         this.h = var4;
      }
   }
}
