package zombie.util;

import java.io.PrintStream;

public class ExecuteTimeAnalyse {
   String caption;
   ExecuteTimeAnalyse.TimeStamp[] list;
   int listIndex = 0;

   public ExecuteTimeAnalyse(String var1, int var2) {
      this.caption = var1;
      this.list = new ExecuteTimeAnalyse.TimeStamp[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.list[var3] = new ExecuteTimeAnalyse.TimeStamp();
      }

   }

   public void reset() {
      this.listIndex = 0;
   }

   public void add(String var1) {
      this.list[this.listIndex].time = System.nanoTime();
      this.list[this.listIndex].comment = var1;
      ++this.listIndex;
   }

   public long getNanoTime() {
      return this.listIndex == 0 ? 0L : System.nanoTime() - this.list[0].time;
   }

   public int getMsTime() {
      return this.listIndex == 0 ? 0 : (int)((System.nanoTime() - this.list[0].time) / 1000000L);
   }

   public void print() {
      long var1 = this.list[0].time;
      System.out.println("========== START === " + this.caption + " =============");

      for(int var3 = 1; var3 < this.listIndex; ++var3) {
         System.out.println(var3 + " " + this.list[var3].comment + ": " + (this.list[var3].time - var1) / 1000000L);
         var1 = this.list[var3].time;
      }

      PrintStream var10000 = System.out;
      long var10001 = System.nanoTime() - this.list[0].time;
      var10000.println("END: " + var10001 / 1000000L);
      System.out.println("==========  END  === " + this.caption + " =============");
   }

   static class TimeStamp {
      long time;
      String comment;

      public TimeStamp(String var1) {
         this.comment = var1;
         this.time = System.nanoTime();
      }

      public TimeStamp() {
      }
   }
}
