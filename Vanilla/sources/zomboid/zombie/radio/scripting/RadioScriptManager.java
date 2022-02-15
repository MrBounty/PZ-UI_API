package zombie.radio.scripting;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import zombie.GameWindow;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.radio.ZomboidRadio;

public final class RadioScriptManager {
   private final Map channels = new LinkedHashMap();
   private static RadioScriptManager instance;
   private int currentTimeStamp = 0;
   private ArrayList channelsList = new ArrayList();

   public static boolean hasInstance() {
      return instance != null;
   }

   public static RadioScriptManager getInstance() {
      if (instance == null) {
         instance = new RadioScriptManager();
      }

      return instance;
   }

   private RadioScriptManager() {
   }

   public void init(int var1) {
   }

   public Map getChannels() {
      return this.channels;
   }

   public ArrayList getChannelsList() {
      this.channelsList.clear();
      Iterator var1 = this.channels.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         this.channelsList.add((RadioChannel)var2.getValue());
      }

      return this.channelsList;
   }

   public RadioChannel getRadioChannel(String var1) {
      Iterator var2 = this.channels.entrySet().iterator();

      Entry var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Entry)var2.next();
      } while(!((RadioChannel)var3.getValue()).getGUID().equals(var1));

      return (RadioChannel)var3.getValue();
   }

   public void simulateScriptsUntil(int var1, boolean var2) {
      Iterator var3 = this.channels.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         this.simulateChannelUntil(((RadioChannel)var4.getValue()).GetFrequency(), var1, var2);
      }

   }

   public void simulateChannelUntil(int var1, int var2, boolean var3) {
      if (this.channels.containsKey(var1)) {
         RadioChannel var4 = (RadioChannel)this.channels.get(var1);
         if (var4.isTimeSynced() && !var3) {
            return;
         }

         for(int var5 = 0; var5 < var2; ++var5) {
            int var6 = var5 * 24 * 60;
            var4.UpdateScripts(this.currentTimeStamp, var6);
         }

         var4.setTimeSynced(true);
      }

   }

   public int getCurrentTimeStamp() {
      return this.currentTimeStamp;
   }

   public void PlayerListensChannel(int var1, boolean var2, boolean var3) {
      if (this.channels.containsKey(var1) && ((RadioChannel)this.channels.get(var1)).IsTv() == var3) {
         ((RadioChannel)this.channels.get(var1)).SetPlayerIsListening(var2);
      }

   }

   public void AddChannel(RadioChannel var1, boolean var2) {
      String var3;
      if (var1 == null || !var2 && this.channels.containsKey(var1.GetFrequency())) {
         var3 = var1 != null ? var1.GetName() : "null";
         DebugLog.log(DebugType.Radio, "Error adding radiochannel (" + var3 + "), channel is null or frequency key already exists");
      } else {
         this.channels.put(var1.GetFrequency(), var1);
         var3 = var1.GetCategory().name();
         ZomboidRadio.getInstance().addChannelName(var1.GetName(), var1.GetFrequency(), var3, var2);
      }

   }

   public void RemoveChannel(int var1) {
      if (this.channels.containsKey(var1)) {
         this.channels.remove(var1);
         ZomboidRadio.getInstance().removeChannelName(var1);
      }

   }

   public void UpdateScripts(int var1, int var2, int var3) {
      this.currentTimeStamp = var1 * 24 * 60 + var2 * 60 + var3;
      Iterator var4 = this.channels.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         ((RadioChannel)var5.getValue()).UpdateScripts(this.currentTimeStamp, var1);
      }

   }

   public void update() {
      Iterator var1 = this.channels.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         ((RadioChannel)var2.getValue()).update();
      }

   }

   public void reset() {
      instance = null;
   }

   public void Save(Writer var1) throws IOException {
      Iterator var2 = this.channels.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         Object var10001 = var3.getKey();
         var1.write(var10001 + "," + ((RadioChannel)var3.getValue()).getCurrentScriptLoop() + "," + ((RadioChannel)var3.getValue()).getCurrentScriptMaxLoops());
         RadioScript var4 = ((RadioChannel)var3.getValue()).getCurrentScript();
         String var6;
         if (var4 != null) {
            var6 = var4.GetName();
            var1.write("," + var6 + "," + var4.getStartDay());
         }

         RadioBroadCast var5 = ((RadioChannel)var3.getValue()).getAiringBroadcast();
         if (var5 != null) {
            var1.write("," + var5.getID());
         } else if (((RadioChannel)var3.getValue()).getLastBroadcastID() != null) {
            var1.write("," + ((RadioChannel)var3.getValue()).getLastBroadcastID());
         } else {
            var1.write(",none");
         }

         var6 = var5 != null ? var5.getCurrentLineNumber().makeConcatWithConstants<invokedynamic>(var5.getCurrentLineNumber()) : "-1";
         var1.write("," + var6);
         var1.write(System.lineSeparator());
      }

   }

   public void SaveOLD(DataOutputStream var1) throws IOException {
      var1.writeInt(this.channels.size());
      Iterator var2 = this.channels.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.writeInt((Integer)var3.getKey());
         var1.writeInt(((RadioChannel)var3.getValue()).getCurrentScriptLoop());
         var1.writeInt(((RadioChannel)var3.getValue()).getCurrentScriptMaxLoops());
         RadioScript var4 = ((RadioChannel)var3.getValue()).getCurrentScript();
         var1.writeByte(var4 != null ? 1 : 0);
         if (var4 != null) {
            GameWindow.WriteString(var1, var4.GetName());
            var1.writeInt(var4.getStartDay());
         }
      }

   }

   public void Load(BufferedReader var1) throws IOException {
      int var3 = 1;
      int var4 = 1;

      String var8;
      while((var8 = var1.readLine()) != null) {
         RadioChannel var10 = null;
         var8 = var8.trim();
         String[] var11 = var8.split(",");
         if (var11.length >= 3) {
            int var2 = Integer.parseInt(var11[0]);
            var3 = Integer.parseInt(var11[1]);
            var4 = Integer.parseInt(var11[2]);
            if (this.channels.containsKey(var2)) {
               var10 = (RadioChannel)this.channels.get(var2);
               var10.setTimeSynced(true);
            }
         }

         if (var10 != null && var11.length >= 5) {
            String var7 = var11[3];
            int var5 = Integer.parseInt(var11[4]);
            if (var10 != null) {
               var10.setActiveScript(var7, var5, var3, var4);
            }
         }

         if (var10 != null && var11.length >= 7) {
            String var9 = var11[5];
            if (!var9.equals("none")) {
               int var6 = Integer.parseInt(var11[6]);
               var10.LoadAiringBroadcast(var9, var6);
            }
         }
      }

   }

   public void LoadOLD(DataInputStream var1) throws IOException {
      Iterator var2 = this.channels.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         ((RadioChannel)var3.getValue()).setActiveScriptNull();
      }

      int var11 = var1.readInt();

      for(int var12 = 0; var12 < var11; ++var12) {
         RadioChannel var4 = null;
         int var5 = var1.readInt();
         if (this.channels.containsKey(var5)) {
            var4 = (RadioChannel)this.channels.get(var5);
            var4.setTimeSynced(true);
         }

         int var6 = var1.readInt();
         int var7 = var1.readInt();
         boolean var8 = var1.readByte() == 1;
         if (var8) {
            String var9 = GameWindow.ReadString(var1);
            int var10 = var1.readInt();
            if (var4 != null) {
               var4.setActiveScript(var9, var10, var6, var7);
            }
         }
      }

   }
}
