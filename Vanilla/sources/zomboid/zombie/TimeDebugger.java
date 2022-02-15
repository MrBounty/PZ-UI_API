package zombie;

import java.util.ArrayList;
import zombie.debug.DebugLog;
import zombie.network.GameServer;

public class TimeDebugger {
   ArrayList records = new ArrayList();
   ArrayList recordStrings = new ArrayList();
   String name = "";

   public TimeDebugger(String var1) {
      this.name = var1;
   }

   public void clear() {
      if (GameServer.bServer) {
         this.records.clear();
         this.recordStrings.clear();
      }

   }

   public void start() {
      if (GameServer.bServer) {
         this.records.clear();
         this.recordStrings.clear();
         this.records.add(System.currentTimeMillis());
         this.recordStrings.add("Start");
      }

   }

   public void record() {
      if (GameServer.bServer) {
         this.records.add(System.currentTimeMillis());
         this.recordStrings.add(String.valueOf(this.records.size()));
      }

   }

   public void record(String var1) {
      if (GameServer.bServer) {
         this.records.add(System.currentTimeMillis());
         this.recordStrings.add(var1);
      }

   }

   public void recordTO(String var1, int var2) {
      if (GameServer.bServer && (Long)this.records.get(this.records.size() - 1) - (Long)this.records.get(this.records.size() - 2) > (long)var2) {
         this.records.add(System.currentTimeMillis());
         this.recordStrings.add(var1);
      }

   }

   public void add(TimeDebugger var1) {
      if (GameServer.bServer) {
         String var2 = var1.name;

         for(int var3 = 0; var3 < var1.records.size(); ++var3) {
            this.records.add((Long)var1.records.get(var3));
            this.recordStrings.add(var2 + "|" + (String)var1.recordStrings.get(var3));
         }

         var1.clear();
      }

   }

   public void print() {
      if (GameServer.bServer) {
         this.records.add(System.currentTimeMillis());
         this.recordStrings.add("END");
         if (this.records.size() > 1) {
            DebugLog.log("=== DBG " + this.name + " ===");
            long var1 = (Long)this.records.get(0);

            for(int var3 = 1; var3 < this.records.size(); ++var3) {
               long var4 = (Long)this.records.get(var3 - 1);
               long var6 = (Long)this.records.get(var3);
               String var8 = (String)this.recordStrings.get(var3);
               DebugLog.log("RECORD " + var3 + " " + var8 + " A:" + (var6 - var1) + " D:" + (var6 - var4));
            }

            DebugLog.log("=== END " + this.name + " (" + ((Long)this.records.get(this.records.size() - 1) - var1) + ") ===");
         } else {
            DebugLog.log("<<< DBG " + this.name + " ERROR >>>");
         }
      }

   }

   public long getExecTime() {
      return this.records.size() == 0 ? 0L : System.currentTimeMillis() - (Long)this.records.get(0);
   }
}
