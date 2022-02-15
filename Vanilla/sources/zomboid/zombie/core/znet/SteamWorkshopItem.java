package zombie.core.znet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import zombie.ZomboidFileSystem;
import zombie.core.logger.ExceptionLogger;
import zombie.core.textures.PNGDecoder;

public class SteamWorkshopItem {
   private String workshopFolder;
   private String PublishedFileId;
   private String title = "";
   private String description = "";
   private String visibility = "public";
   private final ArrayList tags = new ArrayList();
   private String changeNote = "";
   private boolean bHasMod;
   private boolean bHasMap;
   private final ArrayList modIDs = new ArrayList();
   private final ArrayList mapFolders = new ArrayList();
   private static final int VERSION1 = 1;
   private static final int LATEST_VERSION = 1;

   public SteamWorkshopItem(String var1) {
      this.workshopFolder = var1;
   }

   public String getContentFolder() {
      return this.workshopFolder + File.separator + "Contents";
   }

   public String getFolderName() {
      return (new File(this.workshopFolder)).getName();
   }

   public void setID(String var1) {
      if (var1 != null && !SteamUtils.isValidSteamID(var1)) {
         var1 = null;
      }

      this.PublishedFileId = var1;
   }

   public String getID() {
      return this.PublishedFileId;
   }

   public void setTitle(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.title = var1;
   }

   public String getTitle() {
      return this.title;
   }

   public void setDescription(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.description = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setVisibility(String var1) {
      this.visibility = var1;
   }

   public String getVisibility() {
      return this.visibility;
   }

   public void setVisibilityInteger(int var1) {
      switch(var1) {
      case 0:
         this.visibility = "public";
         break;
      case 1:
         this.visibility = "friendsOnly";
         break;
      case 2:
         this.visibility = "private";
         break;
      case 3:
         this.visibility = "unlisted";
         break;
      default:
         this.visibility = "public";
      }

   }

   public int getVisibilityInteger() {
      if ("public".equals(this.visibility)) {
         return 0;
      } else if ("friendsOnly".equals(this.visibility)) {
         return 1;
      } else if ("private".equals(this.visibility)) {
         return 2;
      } else {
         return "unlisted".equals(this.visibility) ? 3 : 0;
      }
   }

   public void setTags(ArrayList var1) {
      this.tags.clear();
      this.tags.addAll(var1);
   }

   public static ArrayList getAllowedTags() {
      ArrayList var0 = new ArrayList();
      File var1 = ZomboidFileSystem.instance.getMediaFile("WorkshopTags.txt");

      try {
         FileReader var2 = new FileReader(var1);

         try {
            BufferedReader var3 = new BufferedReader(var2);

            String var4;
            try {
               while((var4 = var3.readLine()) != null) {
                  var4 = var4.trim();
                  if (!var4.isEmpty()) {
                     var0.add(var4);
                  }
               }
            } catch (Throwable var8) {
               try {
                  var3.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            var3.close();
         } catch (Throwable var9) {
            try {
               var2.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }

            throw var9;
         }

         var2.close();
      } catch (Exception var10) {
         ExceptionLogger.logException(var10);
      }

      return var0;
   }

   public ArrayList getTags() {
      return this.tags;
   }

   public String getSubmitDescription() {
      String var1 = this.getDescription();
      if (!var1.isEmpty()) {
         var1 = var1 + "\n\n";
      }

      var1 = var1 + "Workshop ID: " + this.getID();

      int var2;
      for(var2 = 0; var2 < this.modIDs.size(); ++var2) {
         var1 = var1 + "\nMod ID: " + (String)this.modIDs.get(var2);
      }

      for(var2 = 0; var2 < this.mapFolders.size(); ++var2) {
         var1 = var1 + "\nMap Folder: " + (String)this.mapFolders.get(var2);
      }

      return var1;
   }

   public String[] getSubmitTags() {
      ArrayList var1 = new ArrayList();
      var1.addAll(this.tags);
      return (String[])var1.toArray(new String[var1.size()]);
   }

   public String getPreviewImage() {
      return this.workshopFolder + File.separator + "preview.png";
   }

   public void setChangeNote(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.changeNote = var1;
   }

   public String getChangeNote() {
      return this.changeNote;
   }

   public boolean create() {
      return SteamWorkshop.instance.CreateWorkshopItem(this);
   }

   public boolean submitUpdate() {
      return SteamWorkshop.instance.SubmitWorkshopItem(this);
   }

   public boolean getUpdateProgress(KahluaTable var1) {
      if (var1 == null) {
         throw new NullPointerException("table is null");
      } else {
         long[] var2 = new long[2];
         if (SteamWorkshop.instance.GetItemUpdateProgress(var2)) {
            System.out.println(var2[0] + "/" + var2[1]);
            var1.rawset("processed", (double)var2[0]);
            var1.rawset("total", (double)Math.max(var2[1], 1L));
            return true;
         } else {
            return false;
         }
      }
   }

   public int getUpdateProgressTotal() {
      return 1;
   }

   private String validateFileTypes(Path var1) {
      try {
         DirectoryStream var2 = Files.newDirectoryStream(var1);

         String var6;
         label90: {
            label91: {
               try {
                  Iterator var3 = var2.iterator();

                  while(true) {
                     if (!var3.hasNext()) {
                        break label91;
                     }

                     Path var4 = (Path)var3.next();
                     String var5;
                     if (Files.isDirectory(var4, new LinkOption[0])) {
                        var5 = this.validateFileTypes(var4);
                        if (var5 != null) {
                           var6 = var5;
                           break;
                        }
                     } else {
                        var5 = var4.getFileName().toString();
                        if (var5.endsWith(".exe") || var5.endsWith(".dll") || var5.endsWith(".bat") || var5.endsWith(".app") || var5.endsWith(".dylib") || var5.endsWith(".sh") || var5.endsWith(".so") || var5.endsWith(".zip")) {
                           var6 = "FileTypeNotAllowed";
                           break label90;
                        }
                     }
                  }
               } catch (Throwable var8) {
                  if (var2 != null) {
                     try {
                        var2.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }
                  }

                  throw var8;
               }

               if (var2 != null) {
                  var2.close();
               }

               return var6;
            }

            if (var2 != null) {
               var2.close();
            }

            return null;
         }

         if (var2 != null) {
            var2.close();
         }

         return var6;
      } catch (Exception var9) {
         var9.printStackTrace();
         return "IOError";
      }
   }

   private String validateModDotInfo(Path var1) {
      String var2 = null;

      try {
         FileReader var3 = new FileReader(var1.toFile());

         try {
            BufferedReader var4 = new BufferedReader(var3);

            String var5;
            try {
               while((var5 = var4.readLine()) != null) {
                  if (var5.startsWith("id=")) {
                     var2 = var5.replace("id=", "").trim();
                     break;
                  }
               }
            } catch (Throwable var9) {
               try {
                  var4.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            var4.close();
         } catch (Throwable var10) {
            try {
               var3.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }

            throw var10;
         }

         var3.close();
      } catch (FileNotFoundException var11) {
         return "MissingModDotInfo";
      } catch (IOException var12) {
         var12.printStackTrace();
         return "IOError";
      }

      if (var2 != null && !var2.isEmpty()) {
         this.modIDs.add(var2);
         return null;
      } else {
         return "InvalidModDotInfo";
      }
   }

   private String validateMapDotInfo(Path var1) {
      return null;
   }

   private String validateMapFolder(Path var1) {
      boolean var2 = false;

      try {
         label68: {
            DirectoryStream var3;
            String var7;
            label69: {
               var3 = Files.newDirectoryStream(var1);

               try {
                  Iterator var4 = var3.iterator();

                  while(var4.hasNext()) {
                     Path var5 = (Path)var4.next();
                     if (!Files.isDirectory(var5, new LinkOption[0]) && "map.info".equals(var5.getFileName().toString())) {
                        String var6 = this.validateMapDotInfo(var5);
                        if (var6 != null) {
                           var7 = var6;
                           break label69;
                        }

                        var2 = true;
                     }
                  }
               } catch (Throwable var9) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                     }
                  }

                  throw var9;
               }

               if (var3 != null) {
                  var3.close();
               }
               break label68;
            }

            if (var3 != null) {
               var3.close();
            }

            return var7;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         return "IOError";
      }

      if (!var2) {
         return "MissingMapDotInfo";
      } else {
         this.mapFolders.add(var1.getFileName().toString());
         return null;
      }
   }

   private String validateMapsFolder(Path var1) {
      boolean var2 = false;

      try {
         label64: {
            DirectoryStream var3;
            String var7;
            label65: {
               var3 = Files.newDirectoryStream(var1);

               try {
                  Iterator var4 = var3.iterator();

                  while(var4.hasNext()) {
                     Path var5 = (Path)var4.next();
                     if (Files.isDirectory(var5, new LinkOption[0])) {
                        String var6 = this.validateMapFolder(var5);
                        if (var6 != null) {
                           var7 = var6;
                           break label65;
                        }

                        var2 = true;
                     }
                  }
               } catch (Throwable var9) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                     }
                  }

                  throw var9;
               }

               if (var3 != null) {
                  var3.close();
               }
               break label64;
            }

            if (var3 != null) {
               var3.close();
            }

            return var7;
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         return "IOError";
      }

      if (!var2) {
         return null;
      } else {
         this.bHasMap = true;
         return null;
      }
   }

   private String validateMediaFolder(Path var1) {
      try {
         DirectoryStream var2 = Files.newDirectoryStream(var1);

         String var6;
         label55: {
            try {
               Iterator var3 = var2.iterator();

               while(var3.hasNext()) {
                  Path var4 = (Path)var3.next();
                  if (Files.isDirectory(var4, new LinkOption[0]) && "maps".equals(var4.getFileName().toString())) {
                     String var5 = this.validateMapsFolder(var4);
                     if (var5 != null) {
                        var6 = var5;
                        break label55;
                     }
                  }
               }
            } catch (Throwable var8) {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (var2 != null) {
               var2.close();
            }

            return null;
         }

         if (var2 != null) {
            var2.close();
         }

         return var6;
      } catch (Exception var9) {
         var9.printStackTrace();
         return "IOError";
      }
   }

   private String validateModFolder(Path var1) {
      boolean var2 = false;

      try {
         DirectoryStream var3;
         String var7;
         label90: {
            label83: {
               var3 = Files.newDirectoryStream(var1);

               try {
                  Iterator var4 = var3.iterator();

                  while(true) {
                     if (!var4.hasNext()) {
                        break label83;
                     }

                     Path var5 = (Path)var4.next();
                     String var6;
                     if (Files.isDirectory(var5, new LinkOption[0])) {
                        if ("media".equals(var5.getFileName().toString())) {
                           var6 = this.validateMediaFolder(var5);
                           if (var6 != null) {
                              var7 = var6;
                              break;
                           }
                        }
                     } else if ("mod.info".equals(var5.getFileName().toString())) {
                        var6 = this.validateModDotInfo(var5);
                        if (var6 != null) {
                           var7 = var6;
                           break label90;
                        }

                        var2 = true;
                     }
                  }
               } catch (Throwable var9) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                     }
                  }

                  throw var9;
               }

               if (var3 != null) {
                  var3.close();
               }

               return var7;
            }

            if (var3 != null) {
               var3.close();
            }

            return !var2 ? "MissingModDotInfo" : null;
         }

         if (var3 != null) {
            var3.close();
         }

         return var7;
      } catch (Exception var10) {
         var10.printStackTrace();
         return "IOError";
      }
   }

   private String validateModsFolder(Path var1) {
      boolean var2 = false;

      try {
         DirectoryStream var3;
         label75: {
            String var7;
            label76: {
               var3 = Files.newDirectoryStream(var1);

               String var6;
               try {
                  Iterator var4 = var3.iterator();

                  while(true) {
                     if (!var4.hasNext()) {
                        break label75;
                     }

                     Path var5 = (Path)var4.next();
                     if (!Files.isDirectory(var5, new LinkOption[0])) {
                        var6 = "FileNotAllowedInMods";
                        break;
                     }

                     var6 = this.validateModFolder(var5);
                     if (var6 != null) {
                        var7 = var6;
                        break label76;
                     }

                     var2 = true;
                  }
               } catch (Throwable var9) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                     }
                  }

                  throw var9;
               }

               if (var3 != null) {
                  var3.close();
               }

               return var6;
            }

            if (var3 != null) {
               var3.close();
            }

            return var7;
         }

         if (var3 != null) {
            var3.close();
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         return "IOError";
      }

      if (!var2) {
         return "EmptyModsFolder";
      } else {
         this.bHasMod = true;
         return null;
      }
   }

   private String validateBuildingsFolder(Path var1) {
      return null;
   }

   private String validateCreativeFolder(Path var1) {
      return null;
   }

   public String validatePreviewImage(Path var1) throws IOException {
      if (Files.exists(var1, new LinkOption[0]) && Files.isReadable(var1) && !Files.isDirectory(var1, new LinkOption[0])) {
         if (Files.size(var1) > 1024000L) {
            return "PreviewFileSize";
         } else {
            try {
               FileInputStream var2 = new FileInputStream(var1.toFile());

               label60: {
                  String var5;
                  try {
                     BufferedInputStream var3 = new BufferedInputStream(var2);

                     label56: {
                        try {
                           PNGDecoder var4 = new PNGDecoder(var3, false);
                           if (var4.getWidth() != 256 || var4.getHeight() != 256) {
                              var5 = "PreviewDimensions";
                              break label56;
                           }
                        } catch (Throwable var8) {
                           try {
                              var3.close();
                           } catch (Throwable var7) {
                              var8.addSuppressed(var7);
                           }

                           throw var8;
                        }

                        var3.close();
                        break label60;
                     }

                     var3.close();
                  } catch (Throwable var9) {
                     try {
                        var2.close();
                     } catch (Throwable var6) {
                        var9.addSuppressed(var6);
                     }

                     throw var9;
                  }

                  var2.close();
                  return var5;
               }

               var2.close();
               return null;
            } catch (IOException var10) {
               var10.printStackTrace();
               return "PreviewFormat";
            }
         }
      } else {
         return "PreviewNotFound";
      }
   }

   public String validateContents() {
      this.bHasMod = false;
      this.bHasMap = false;
      this.modIDs.clear();
      this.mapFolders.clear();

      try {
         Path var1 = FileSystems.getDefault().getPath(this.getContentFolder());
         if (!Files.isDirectory(var1, new LinkOption[0])) {
            return "MissingContents";
         } else {
            Path var2 = FileSystems.getDefault().getPath(this.getPreviewImage());
            String var3 = this.validatePreviewImage(var2);
            if (var3 != null) {
               return var3;
            } else {
               boolean var4 = false;

               try {
                  DirectoryStream var5;
                  label143: {
                     String var8;
                     label144: {
                        label145: {
                           label146: {
                              label147: {
                                 var5 = Files.newDirectoryStream(var1);

                                 try {
                                    Iterator var6 = var5.iterator();

                                    while(true) {
                                       if (!var6.hasNext()) {
                                          break label143;
                                       }

                                       Path var7 = (Path)var6.next();
                                       if (!Files.isDirectory(var7, new LinkOption[0])) {
                                          var8 = "FileNotAllowedInContents";
                                          break;
                                       }

                                       if ("buildings".equals(var7.getFileName().toString())) {
                                          var3 = this.validateBuildingsFolder(var7);
                                          if (var3 != null) {
                                             var8 = var3;
                                             break label144;
                                          }
                                       } else if ("creative".equals(var7.getFileName().toString())) {
                                          var3 = this.validateCreativeFolder(var7);
                                          if (var3 != null) {
                                             var8 = var3;
                                             break label145;
                                          }
                                       } else {
                                          if (!"mods".equals(var7.getFileName().toString())) {
                                             var8 = "FolderNotAllowedInContents";
                                             break label147;
                                          }

                                          var3 = this.validateModsFolder(var7);
                                          if (var3 != null) {
                                             var8 = var3;
                                             break label146;
                                          }
                                       }

                                       var4 = true;
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

                                 return var8;
                              }

                              if (var5 != null) {
                                 var5.close();
                              }

                              return var8;
                           }

                           if (var5 != null) {
                              var5.close();
                           }

                           return var8;
                        }

                        if (var5 != null) {
                           var5.close();
                        }

                        return var8;
                     }

                     if (var5 != null) {
                        var5.close();
                     }

                     return var8;
                  }

                  if (var5 != null) {
                     var5.close();
                  }
               } catch (Exception var11) {
                  var11.printStackTrace();
                  return "IOError";
               }

               return !var4 ? "EmptyContentsFolder" : this.validateFileTypes(var1);
            }
         }
      } catch (IOException var12) {
         var12.printStackTrace();
         return "IOError";
      }
   }

   public String getExtendedErrorInfo(String var1) {
      if ("FolderNotAllowedInContents".equals(var1)) {
         return "buildings/ creative/ mods/";
      } else {
         return "FileTypeNotAllowed".equals(var1) ? "*.exe *.dll *.bat *.app *.dylib *.sh *.so *.zip" : "";
      }
   }

   public boolean readWorkshopTxt() {
      String var1 = this.workshopFolder + File.separator + "workshop.txt";
      if (!(new File(var1)).exists()) {
         return true;
      } else {
         try {
            FileReader var2 = new FileReader(var1);

            boolean var11;
            try {
               BufferedReader var3 = new BufferedReader(var2);

               try {
                  String var4;
                  while((var4 = var3.readLine()) != null) {
                     var4 = var4.trim();
                     if (!var4.isEmpty() && !var4.startsWith("#") && !var4.startsWith("//")) {
                        if (var4.startsWith("id=")) {
                           String var5 = var4.replace("id=", "");
                           this.setID(var5);
                        } else if (var4.startsWith("description=")) {
                           if (!this.description.isEmpty()) {
                              this.description = this.description + "\n";
                           }

                           String var10001 = this.description;
                           this.description = var10001 + var4.replace("description=", "");
                        } else if (var4.startsWith("tags=")) {
                           this.tags.addAll(Arrays.asList(var4.replace("tags=", "").split(";")));
                        } else if (var4.startsWith("title=")) {
                           this.title = var4.replace("title=", "");
                        } else if (!var4.startsWith("version=") && var4.startsWith("visibility=")) {
                           this.visibility = var4.replace("visibility=", "");
                        }
                     }
                  }

                  var11 = true;
               } catch (Throwable var8) {
                  try {
                     var3.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }

                  throw var8;
               }

               var3.close();
            } catch (Throwable var9) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }

               throw var9;
            }

            var2.close();
            return var11;
         } catch (IOException var10) {
            var10.printStackTrace();
            return false;
         }
      }
   }

   public boolean writeWorkshopTxt() {
      String var1 = this.workshopFolder + File.separator + "workshop.txt";
      File var2 = new File(var1);

      try {
         FileWriter var3 = new FileWriter(var2);
         BufferedWriter var4 = new BufferedWriter(var3);
         var4.write("version=1");
         var4.newLine();
         String var10001 = this.PublishedFileId == null ? "" : this.PublishedFileId;
         var4.write("id=" + var10001);
         var4.newLine();
         var4.write("title=" + this.title);
         var4.newLine();
         String[] var5 = this.description.split("\n");
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7];
            var4.write("description=" + var8);
            var4.newLine();
         }

         String var10 = "";

         for(var6 = 0; var6 < this.tags.size(); ++var6) {
            var10 = var10 + (String)this.tags.get(var6);
            if (var6 < this.tags.size() - 1) {
               var10 = var10 + ";";
            }
         }

         var4.write("tags=" + var10);
         var4.newLine();
         var4.write("visibility=" + this.visibility);
         var4.newLine();
         var4.close();
         return true;
      } catch (IOException var9) {
         var9.printStackTrace();
         return false;
      }
   }

   public static enum ItemState {
      None(0),
      Subscribed(1),
      LegacyItem(2),
      Installed(4),
      NeedsUpdate(8),
      Downloading(16),
      DownloadPending(32);

      private final int value;

      private ItemState(int var3) {
         this.value = var3;
      }

      public int getValue() {
         return this.value;
      }

      public boolean and(SteamWorkshopItem.ItemState var1) {
         return (this.value & var1.value) != 0;
      }

      public boolean and(long var1) {
         return ((long)this.value & var1) != 0L;
      }

      public static String toString(long var0) {
         if (var0 == (long)None.getValue()) {
            return "None";
         } else {
            StringBuilder var2 = new StringBuilder();
            SteamWorkshopItem.ItemState[] var3 = values();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               SteamWorkshopItem.ItemState var6 = var3[var5];
               if (var6 != None && var6.and(var0)) {
                  if (var2.length() > 0) {
                     var2.append("|");
                  }

                  var2.append(var6.name());
               }
            }

            return var2.toString();
         }
      }

      // $FF: synthetic method
      private static SteamWorkshopItem.ItemState[] $values() {
         return new SteamWorkshopItem.ItemState[]{None, Subscribed, LegacyItem, Installed, NeedsUpdate, Downloading, DownloadPending};
      }
   }
}
