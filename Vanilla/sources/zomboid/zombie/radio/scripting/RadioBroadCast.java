package zombie.radio.scripting;

import java.util.ArrayList;

public final class RadioBroadCast {
   private static final RadioLine pauseLine = new RadioLine("~", 0.5F, 0.5F, 0.5F);
   private final ArrayList lines = new ArrayList();
   private String ID = "";
   private int startStamp = 0;
   private int endStamp = 0;
   private int lineCount = 0;
   private RadioBroadCast preSegment = null;
   private RadioBroadCast postSegment = null;
   private boolean hasDonePreSegment = false;
   private boolean hasDonePostSegment = false;
   private boolean hasDonePostPause = false;

   public RadioBroadCast(String var1, int var2, int var3) {
      this.ID = var1;
      this.startStamp = var2;
      this.endStamp = var3;
   }

   public String getID() {
      return this.ID;
   }

   public int getStartStamp() {
      return this.startStamp;
   }

   public int getEndStamp() {
      return this.endStamp;
   }

   public void resetLineCounter() {
      this.resetLineCounter(true);
   }

   public void resetLineCounter(boolean var1) {
      this.lineCount = 0;
      if (var1) {
         if (this.preSegment != null) {
            this.preSegment.resetLineCounter(false);
         }

         if (this.postSegment != null) {
            this.postSegment.resetLineCounter(false);
         }
      }

   }

   public void setPreSegment(RadioBroadCast var1) {
      this.preSegment = var1;
   }

   public void setPostSegment(RadioBroadCast var1) {
      this.postSegment = var1;
   }

   public RadioLine getNextLine() {
      return this.getNextLine(true);
   }

   public RadioLine getNextLine(boolean var1) {
      RadioLine var2 = null;
      if (var1 && !this.hasDonePreSegment && this.lineCount == 0 && this.preSegment != null) {
         var2 = this.preSegment.getNextLine();
         if (var2 != null) {
            return var2;
         } else {
            this.hasDonePreSegment = true;
            return pauseLine;
         }
      } else {
         if (this.lineCount >= 0 && this.lineCount < this.lines.size()) {
            var2 = (RadioLine)this.lines.get(this.lineCount);
         }

         if (var1 && var2 == null && this.postSegment != null) {
            if (!this.hasDonePostPause) {
               this.hasDonePostPause = true;
               return pauseLine;
            } else {
               var2 = this.postSegment.getNextLine();
               return var2;
            }
         } else {
            ++this.lineCount;
            return var2;
         }
      }
   }

   public int getCurrentLineNumber() {
      return this.lineCount;
   }

   public void setCurrentLineNumber(int var1) {
      this.lineCount = var1;
      if (this.lineCount < 0) {
         this.lineCount = 0;
      }

   }

   public RadioLine getCurrentLine() {
      return this.lineCount >= 0 && this.lineCount < this.lines.size() ? (RadioLine)this.lines.get(this.lineCount) : null;
   }

   public String PeekNextLineText() {
      if (this.lineCount >= 0 && this.lineCount < this.lines.size()) {
         return this.lines.get(this.lineCount) != null && ((RadioLine)this.lines.get(this.lineCount)).getText() != null ? ((RadioLine)this.lines.get(this.lineCount)).getText() : "Error";
      } else {
         return "None";
      }
   }

   public void AddRadioLine(RadioLine var1) {
      if (var1 != null) {
         this.lines.add(var1);
      }

   }

   public ArrayList getLines() {
      return this.lines;
   }
}
