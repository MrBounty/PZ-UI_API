package com.jcraft.jorbis;

import java.io.PrintStream;
import zombie.debug.DebugLog;

class ChainingExample {
   public static void main(String[] var0) {
      VorbisFile var1 = null;

      try {
         if (var0.length > 0) {
            var1 = new VorbisFile(var0[0]);
         } else {
            var1 = new VorbisFile(System.in, (byte[])null, -1);
         }
      } catch (Exception var5) {
         System.err.println(var5);
         return;
      }

      if (var1.seekable()) {
         DebugLog.log("Input bitstream contained " + var1.streams() + " logical bitstream section(s).");
         DebugLog.log("Total bitstream playing time: " + var1.time_total(-1) + " seconds\n");
      } else {
         DebugLog.log("Standard input was not seekable.");
         DebugLog.log("First logical bitstream information:\n");
      }

      for(int var2 = 0; var2 < var1.streams(); ++var2) {
         Info var3 = var1.getInfo(var2);
         DebugLog.log("\tlogical bitstream section " + (var2 + 1) + " information:");
         int var10000 = var3.rate;
         DebugLog.log("\t\t" + var10000 + "Hz " + var3.channels + " channels bitrate " + var1.bitrate(var2) / 1000 + "kbps serial number=" + var1.serialnumber(var2));
         PrintStream var6 = System.out;
         long var10001 = var1.raw_total(var2);
         var6.print("\t\tcompressed length: " + var10001 + " bytes ");
         float var7 = var1.time_total(var2);
         DebugLog.log(" play time: " + var7 + "s");
         Comment var4 = var1.getComment(var2);
         DebugLog.log((Object)var4);
      }

   }
}
