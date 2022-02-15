package zombie.inventory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.ZomboidFileSystem;
import zombie.core.logger.ExceptionLogger;
import zombie.util.StringUtils;

public final class ClothingItemsDotTxt {
   public static final ClothingItemsDotTxt instance = new ClothingItemsDotTxt();
   private final StringBuilder buf = new StringBuilder();

   private int readBlock(String var1, int var2, ClothingItemsDotTxt.Block var3) {
      int var4;
      for(var4 = var2; var4 < var1.length(); ++var4) {
         if (var1.charAt(var4) == '{') {
            ClothingItemsDotTxt.Block var5 = new ClothingItemsDotTxt.Block();
            var3.children.add(var5);
            var3.elements.add(var5);
            String var6 = var1.substring(var2, var4).trim();
            int var7 = var6.indexOf(32);
            int var8 = var6.indexOf(9);
            int var9 = Math.max(var7, var8);
            if (var9 == -1) {
               var5.type = var6;
            } else {
               var5.type = var6.substring(0, var9);
               var5.id = var6.substring(var9).trim();
            }

            var4 = this.readBlock(var1, var4 + 1, var5);
            var2 = var4;
         } else {
            if (var1.charAt(var4) == '}') {
               String var11 = var1.substring(var2, var4).trim();
               if (!var11.isEmpty()) {
                  ClothingItemsDotTxt.Value var12 = new ClothingItemsDotTxt.Value();
                  var12.string = var1.substring(var2, var4).trim();
                  var3.values.add(var12.string);
                  var3.elements.add(var12);
               }

               return var4 + 1;
            }

            if (var1.charAt(var4) == ',') {
               ClothingItemsDotTxt.Value var10 = new ClothingItemsDotTxt.Value();
               var10.string = var1.substring(var2, var4).trim();
               var3.values.add(var10.string);
               var3.elements.add(var10);
               var2 = var4 + 1;
            }
         }
      }

      return var4;
   }

   public void LoadFile() {
      String var1 = ZomboidFileSystem.instance.getString("media/scripts/clothingItems.txt");
      File var2 = new File(var1);
      if (var2.exists()) {
         try {
            FileReader var3 = new FileReader(var1);

            try {
               BufferedReader var4 = new BufferedReader(var3);

               try {
                  this.buf.setLength(0);

                  String var5;
                  while((var5 = var4.readLine()) != null) {
                     this.buf.append(var5);
                  }
               } catch (Throwable var19) {
                  try {
                     var4.close();
                  } catch (Throwable var12) {
                     var19.addSuppressed(var12);
                  }

                  throw var19;
               }

               var4.close();
            } catch (Throwable var20) {
               try {
                  var3.close();
               } catch (Throwable var11) {
                  var20.addSuppressed(var11);
               }

               throw var20;
            }

            var3.close();
         } catch (Throwable var21) {
            ExceptionLogger.logException(var21);
            return;
         }

         int var23;
         for(int var22 = this.buf.lastIndexOf("*/"); var22 != -1; var22 = this.buf.lastIndexOf("*/", var23)) {
            var23 = this.buf.lastIndexOf("/*", var22 - 1);
            if (var23 == -1) {
               break;
            }

            int var6;
            for(int var25 = this.buf.lastIndexOf("*/", var22 - 1); var25 > var23; var25 = this.buf.lastIndexOf("*/", var6 - 2)) {
               var6 = var23;
               this.buf.substring(var23, var25 + 2);
               var23 = this.buf.lastIndexOf("/*", var23 - 2);
               if (var23 == -1) {
                  break;
               }
            }

            if (var23 == -1) {
               break;
            }

            this.buf.substring(var23, var22 + 2);
            this.buf.replace(var23, var22 + 2, "");
         }

         ClothingItemsDotTxt.Block var24 = new ClothingItemsDotTxt.Block();
         this.readBlock(this.buf.toString(), 0, var24);
         Path var26 = FileSystems.getDefault().getPath("media/clothing/clothingItems");

         try {
            DirectoryStream var27 = Files.newDirectoryStream(var26);

            try {
               Iterator var7 = var27.iterator();

               while(var7.hasNext()) {
                  Path var8 = (Path)var7.next();
                  if (!Files.isDirectory(var8, new LinkOption[0])) {
                     String var9 = var8.getFileName().toString();
                     if (var9.endsWith(".xml")) {
                        String var10 = StringUtils.trimSuffix(var9, ".xml");
                        System.out.println(var9 + " -> " + var10);
                        this.addClothingItem(var10, (ClothingItemsDotTxt.Block)var24.children.get(0));
                     }
                  }
               }
            } catch (Throwable var17) {
               if (var27 != null) {
                  try {
                     var27.close();
                  } catch (Throwable var16) {
                     var17.addSuppressed(var16);
                  }
               }

               throw var17;
            }

            if (var27 != null) {
               var27.close();
            }
         } catch (Exception var18) {
            var18.printStackTrace();
         }

         try {
            FileWriter var28 = new FileWriter(var2);

            try {
               var28.write(((ClothingItemsDotTxt.Block)var24.children.get(0)).toString());
            } catch (Throwable var14) {
               try {
                  var28.close();
               } catch (Throwable var13) {
                  var14.addSuppressed(var13);
               }

               throw var14;
            }

            var28.close();
         } catch (Throwable var15) {
            ExceptionLogger.logException(var15);
         }

         System.out.println(var24.children.get(0));
      }
   }

   private void addClothingItem(String var1, ClothingItemsDotTxt.Block var2) {
      if (!var1.startsWith("FemaleHair_")) {
         if (!var1.startsWith("MaleBeard_")) {
            if (!var1.startsWith("MaleHair_")) {
               if (!var1.startsWith("ZedDmg_")) {
                  if (!var1.startsWith("Bandage_")) {
                     if (!var1.startsWith("Zed_Skin")) {
                        Iterator var3 = var2.children.iterator();

                        ClothingItemsDotTxt.Block var4;
                        do {
                           if (!var3.hasNext()) {
                              ClothingItemsDotTxt.Block var5 = new ClothingItemsDotTxt.Block();
                              var5.type = "item";
                              var5.id = var1;
                              ClothingItemsDotTxt.Value var6 = new ClothingItemsDotTxt.Value();
                              var6.string = "Type = Clothing";
                              var5.elements.add(var6);
                              var5.values.add(var6.string);
                              var6 = new ClothingItemsDotTxt.Value();
                              var6.string = "DisplayName = " + var1;
                              var5.elements.add(var6);
                              var5.values.add(var6.string);
                              var6 = new ClothingItemsDotTxt.Value();
                              var6.string = "ClothingItem = " + var1;
                              var5.elements.add(var6);
                              var5.values.add(var6.string);
                              var2.elements.add(var5);
                              var2.children.add(var5);
                              return;
                           }

                           var4 = (ClothingItemsDotTxt.Block)var3.next();
                        } while(!"item".equals(var4.type) || !var1.equals(var4.id));

                     }
                  }
               }
            }
         }
      }
   }

   private static class Block implements ClothingItemsDotTxt.BlockElement {
      public String type;
      public String id;
      public ArrayList elements = new ArrayList();
      public ArrayList values = new ArrayList();
      public ArrayList children = new ArrayList();

      public ClothingItemsDotTxt.Block asBlock() {
         return this;
      }

      public ClothingItemsDotTxt.Value asValue() {
         return null;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         String var10001 = this.type;
         var1.append(var10001 + (this.id == null ? "" : " " + this.id) + "\n");
         var1.append("{\n");
         Iterator var2 = this.elements.iterator();

         while(var2.hasNext()) {
            ClothingItemsDotTxt.BlockElement var3 = (ClothingItemsDotTxt.BlockElement)var2.next();
            String var4 = var3.toString();
            String[] var5 = var4.split("\n");
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String var8 = var5[var7];
               var1.append("\t" + var8 + "\n");
            }
         }

         var1.append("}\n");
         return var1.toString();
      }

      public String toXML() {
         StringBuilder var1 = new StringBuilder();
         var1.append("<Block type=\"" + this.type + "\" id=\"" + this.id + "\">\n");
         Iterator var2 = this.elements.iterator();

         while(var2.hasNext()) {
            ClothingItemsDotTxt.BlockElement var3 = (ClothingItemsDotTxt.BlockElement)var2.next();
            String var4 = var3.toXML();
            String[] var5 = var4.split("\n");
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String var8 = var5[var7];
               var1.append("    " + var8 + "\n");
            }
         }

         var1.append("</Block>\n");
         return var1.toString();
      }
   }

   private static class Value implements ClothingItemsDotTxt.BlockElement {
      String string;

      public ClothingItemsDotTxt.Block asBlock() {
         return null;
      }

      public ClothingItemsDotTxt.Value asValue() {
         return this;
      }

      public String toString() {
         return this.string + ",\n";
      }

      public String toXML() {
         return "<Value>" + this.string + "</Value>\n";
      }
   }

   private interface BlockElement {
      ClothingItemsDotTxt.Block asBlock();

      ClothingItemsDotTxt.Value asValue();

      String toXML();
   }
}
