package zombie.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import zombie.debug.DebugLog;

public final class ConfigFile {
   protected ArrayList options;
   protected int version;

   private void fileError(String var1, int var2, String var3) {
      DebugLog.log(var1 + ":" + var2 + " " + var3);
   }

   public boolean read(String var1) {
      this.options = new ArrayList();
      this.version = 0;
      File var2 = new File(var1);
      if (!var2.exists()) {
         return false;
      } else {
         DebugLog.log("reading " + var1);

         try {
            FileReader var3 = new FileReader(var2);

            try {
               BufferedReader var4 = new BufferedReader(var3);

               try {
                  int var5 = 0;

                  while(true) {
                     String var6 = var4.readLine();
                     if (var6 == null) {
                        break;
                     }

                     ++var5;
                     var6 = var6.trim();
                     if (!var6.isEmpty() && !var6.startsWith("#")) {
                        if (!var6.contains("=")) {
                           this.fileError(var1, var5, var6);
                        } else {
                           String[] var7 = var6.split("=");
                           if ("Version".equals(var7[0])) {
                              try {
                                 this.version = Integer.parseInt(var7[1]);
                              } catch (NumberFormatException var11) {
                                 this.fileError(var1, var5, "expected version number, got \"" + var7[1] + "\"");
                              }
                           } else {
                              StringConfigOption var8 = new StringConfigOption(var7[0], var7.length > 1 ? var7[1] : "");
                              this.options.add(var8);
                           }
                        }
                     }
                  }
               } catch (Throwable var12) {
                  try {
                     var4.close();
                  } catch (Throwable var10) {
                     var12.addSuppressed(var10);
                  }

                  throw var12;
               }

               var4.close();
            } catch (Throwable var13) {
               try {
                  var3.close();
               } catch (Throwable var9) {
                  var13.addSuppressed(var9);
               }

               throw var13;
            }

            var3.close();
            return true;
         } catch (Exception var14) {
            var14.printStackTrace();
            return false;
         }
      }
   }

   public boolean write(String var1, int var2, ArrayList var3) {
      File var4 = new File(var1);
      DebugLog.log("writing " + var1);

      try {
         FileWriter var5 = new FileWriter(var4, false);

         try {
            if (var2 != 0) {
               var5.write("Version=" + var2 + System.lineSeparator());
            }

            for(int var6 = 0; var6 < var3.size(); ++var6) {
               ConfigOption var7 = (ConfigOption)var3.get(var6);
               String var10001 = var7.getName();
               var5.write(var10001 + "=" + var7.getValueAsString() + System.lineSeparator());
            }
         } catch (Throwable var9) {
            try {
               var5.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         var5.close();
         return true;
      } catch (Exception var10) {
         var10.printStackTrace();
         return false;
      }
   }

   public ArrayList getOptions() {
      return this.options;
   }

   public int getVersion() {
      return this.version;
   }
}
