package zombie.radio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import zombie.core.Color;
import zombie.input.GameKeyboard;
import zombie.radio.scripting.RadioBroadCast;
import zombie.radio.scripting.RadioChannel;
import zombie.radio.scripting.RadioScript;
import zombie.radio.scripting.RadioScriptManager;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

public final class RadioDebugConsole {
   private final HashMap state = new HashMap();
   private int channelIndex = 0;
   private int testcounter = 0;
   private Color colRed = new Color(255, 0, 0, 255);
   private Color colGreen = new Color(0, 255, 0, 255);
   private Color colWhite = new Color(255, 255, 255, 255);
   private Color colGrey = new Color(150, 150, 150, 255);
   private Color colDyn = new Color(255, 255, 255, 255);
   private int drawY = 0;
   private int drawX = 0;
   private int drawYLine = 20;

   public RadioDebugConsole() {
      this.state.put(12, false);
      this.state.put(13, false);
      this.state.put(53, false);
      this.state.put(26, false);
   }

   public void update() {
      Map var1 = RadioScriptManager.getInstance().getChannels();

      boolean var2;
      Entry var4;
      for(Iterator var3 = this.state.entrySet().iterator(); var3.hasNext(); var4.setValue(var2)) {
         var4 = (Entry)var3.next();
         var2 = GameKeyboard.isKeyDown((Integer)var4.getKey());
         if (var2 && (Boolean)var4.getValue() != var2) {
            switch((Integer)var4.getKey()) {
            case 12:
               --this.channelIndex;
               if (this.channelIndex < 0 && var1 != null) {
                  this.channelIndex = var1.size() - 1;
               }
               break;
            case 13:
               ++this.channelIndex;
               if (var1 != null && this.channelIndex >= var1.size()) {
                  this.channelIndex = 0;
               }
            case 26:
            case 53:
            }
         }
      }

   }

   public void render() {
      Map var1 = RadioScriptManager.getInstance().getChannels();
      if (var1 != null && var1.size() != 0) {
         if (this.channelIndex < 0) {
            this.channelIndex = 0;
         }

         if (this.channelIndex >= var1.size()) {
            this.channelIndex = var1.size() - 1;
         }

         this.drawYLine = 20;
         this.drawX = 20;
         this.drawY = 200;
         short var2 = 150;
         this.DrawLine("Scamble once: ", 0, false, this.colGrey);
         this.AddBlancLine();
         this.DrawLine("Radio Script Manager Debug.", 0, true);
         this.DrawLine("Real Time: ", 0, false, this.colGrey);
         this.DrawLine(timeStampToString(RadioScriptManager.getInstance().getCurrentTimeStamp()), var2, true);
         this.AddBlancLine();
         this.AddBlancLine();
         this.DrawLine("Index: " + (this.channelIndex + 1) + " of " + var1.size() + " total channels.", 0, true);
         RadioChannel var3 = (RadioChannel)var1.values().toArray()[this.channelIndex];
         if (var3 != null) {
            this.DrawLine("Selected channel: ", 0, false, this.colGrey);
            this.DrawLine(var3.GetName(), var2, true);
            this.DrawLine("Type: ", 0, false, this.colGrey);
            this.DrawLine(var3.IsTv() ? "Television" : "Radio", var2, true);
            this.DrawLine("Frequency: ", 0, false, this.colGrey);
            this.DrawLine(Integer.toString(var3.GetFrequency()), var2, true);
            this.DrawLine("Category: ", 0, false, this.colGrey);
            this.DrawLine(var3.GetCategory().toString(), var2, true);
            this.DrawLine("PlayerListening: ", 0, false, this.colGrey);
            if (var3.GetPlayerIsListening()) {
               this.DrawLine("Yes", var2, true, this.colGreen);
            } else {
               this.DrawLine("No", var2, true, this.colRed);
            }

            RadioBroadCast var4 = var3.getAiringBroadcast();
            if (var4 != null) {
               this.AddBlancLine();
               this.DrawLine("Is airing a broadcast:", 0, true, this.colGreen);
               this.DrawLine("ID: ", 0, false, this.colGrey);
               this.DrawLine(var4.getID(), var2, true);
               this.DrawLine("StartStamp: ", 0, false, this.colGrey);
               this.DrawLine(timeStampToString(var4.getStartStamp()), var2, true);
               this.DrawLine("EndStamp: ", 0, false, this.colGrey);
               this.DrawLine(timeStampToString(var4.getEndStamp()), var2, true);
               if (var4.getCurrentLine() != null) {
                  this.colDyn.r = var4.getCurrentLine().getR();
                  this.colDyn.g = var4.getCurrentLine().getG();
                  this.colDyn.b = var4.getCurrentLine().getB();
                  if (var4.getCurrentLine().getText() != null) {
                     this.DrawLine("Next line to be aired: ", 0, false, this.colGrey);
                     this.DrawLine(var4.PeekNextLineText(), var2, true, this.colDyn);
                  }
               }
            }

            this.AddBlancLine();
            RadioScript var5 = var3.getCurrentScript();
            if (var5 != null) {
               this.DrawLine("Currently working on RadioScript: ", 0, true);
               this.DrawLine("Name: ", 0, false, this.colGrey);
               this.DrawLine(var5.GetName(), var2, true);
               this.DrawLine("Start day: ", 0, false, this.colGrey);
               this.DrawLine(timeStampToString(var5.getStartDayStamp()), var2, true);
               this.DrawLine("Current loop: ", 0, false, this.colGrey);
               this.DrawLine(Integer.toString(var3.getCurrentScriptLoop()), var2, true);
               this.DrawLine("Total loops: ", 0, false, this.colGrey);
               this.DrawLine(Integer.toString(var3.getCurrentScriptMaxLoops()), var2, true);
               var4 = var5.getCurrentBroadcast();
               if (var4 != null) {
                  this.AddBlancLine();
                  this.DrawLine("Currently active broadcast:", 0, true);
                  this.DrawLine("ID: ", 0, false, this.colGrey);
                  this.DrawLine(var4.getID(), var2, true);
                  this.DrawLine("Real StartStamp: ", 0, false, this.colGrey);
                  this.DrawLine(timeStampToString(var4.getStartStamp() + var5.getStartDayStamp()), var2, true);
                  this.DrawLine("Real EndStamp: ", 0, false, this.colGrey);
                  this.DrawLine(timeStampToString(var4.getEndStamp() + var5.getStartDayStamp()), var2, true);
                  this.DrawLine("Script StartStamp: ", 0, false, this.colGrey);
                  this.DrawLine(timeStampToString(var4.getStartStamp()), var2, true);
                  this.DrawLine("Script EndStamp: ", 0, false, this.colGrey);
                  this.DrawLine(timeStampToString(var4.getEndStamp()), var2, true);
                  if (var4.getCurrentLine() != null) {
                     this.colDyn.r = var4.getCurrentLine().getR();
                     this.colDyn.g = var4.getCurrentLine().getG();
                     this.colDyn.b = var4.getCurrentLine().getB();
                     if (var4.getCurrentLine().getText() != null) {
                        this.DrawLine("Next line to be aired: ", 0, false, this.colGrey);
                        this.DrawLine(var4.PeekNextLineText(), var2, true, this.colDyn);
                     }
                  }
               }
            }
         }

      }
   }

   public static String timeStampToString(int var0) {
      int var1 = var0 / 1440;
      int var2 = var0 / 60 % 24;
      int var3 = var0 % 60;
      String var10000 = Integer.toString(var1);
      return "Day: " + var10000 + ", Hour: " + Integer.toString(var2) + ", Minute: " + Integer.toString(var3);
   }

   private void AddBlancLine() {
      this.drawY += this.drawYLine;
   }

   private void DrawLine(String var1, int var2, boolean var3, Color var4) {
      TextManager.instance.DrawString(UIFont.Medium, (double)(this.drawX + var2), (double)this.drawY, var1, (double)var4.r, (double)var4.g, (double)var4.b, (double)var4.a);
      if (var3) {
         this.drawY += this.drawYLine;
      }

   }

   private void DrawLine(String var1, int var2, boolean var3) {
      this.DrawLine(var1, var2, var3, this.colWhite);
   }
}
