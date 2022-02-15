package zombie.radio.scripting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import zombie.GameTime;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.radio.ChannelCategory;
import zombie.radio.RadioData;
import zombie.radio.ZomboidRadio;

public class RadioChannel {
   private String GUID;
   private RadioData radioData;
   private boolean isTimeSynced;
   private Map scripts;
   private int frequency;
   private String name;
   private boolean isTv;
   private ChannelCategory category;
   private boolean playerIsListening;
   private RadioScript currentScript;
   private int currentScriptLoop;
   private int currentScriptMaxLoops;
   private RadioBroadCast airingBroadcast;
   private float airCounter;
   private String lastAiredLine;
   private String lastBroadcastID;
   private float airCounterMultiplier;
   private boolean louisvilleObfuscate;
   float minmod;
   float maxmod;

   public RadioChannel(String var1, int var2, ChannelCategory var3) {
      this(var1, var2, var3, UUID.randomUUID().toString());
   }

   public RadioChannel(String var1, int var2, ChannelCategory var3, String var4) {
      this.isTimeSynced = false;
      this.scripts = new HashMap();
      this.frequency = -1;
      this.name = "Unnamed channel";
      this.isTv = false;
      this.category = ChannelCategory.Undefined;
      this.playerIsListening = false;
      this.currentScript = null;
      this.currentScriptLoop = 1;
      this.currentScriptMaxLoops = 1;
      this.airingBroadcast = null;
      this.airCounter = 0.0F;
      this.lastAiredLine = "";
      this.lastBroadcastID = null;
      this.airCounterMultiplier = 1.0F;
      this.louisvilleObfuscate = false;
      this.minmod = 1.5F;
      this.maxmod = 5.0F;
      this.name = var1;
      this.frequency = var2;
      this.category = var3;
      this.isTv = this.category == ChannelCategory.Television;
      this.GUID = var4;
   }

   public String getGUID() {
      return this.GUID;
   }

   public int GetFrequency() {
      return this.frequency;
   }

   public String GetName() {
      return this.name;
   }

   public boolean IsTv() {
      return this.isTv;
   }

   public ChannelCategory GetCategory() {
      return this.category;
   }

   public RadioScript getCurrentScript() {
      return this.currentScript;
   }

   public RadioBroadCast getAiringBroadcast() {
      return this.airingBroadcast;
   }

   public String getLastAiredLine() {
      return this.lastAiredLine;
   }

   public int getCurrentScriptLoop() {
      return this.currentScriptLoop;
   }

   public int getCurrentScriptMaxLoops() {
      return this.currentScriptMaxLoops;
   }

   public String getLastBroadcastID() {
      return this.lastBroadcastID;
   }

   public RadioData getRadioData() {
      return this.radioData;
   }

   public void setRadioData(RadioData var1) {
      this.radioData = var1;
   }

   public boolean isTimeSynced() {
      return this.isTimeSynced;
   }

   public void setTimeSynced(boolean var1) {
      this.isTimeSynced = var1;
   }

   public boolean isVanilla() {
      return this.radioData == null || this.radioData.isVanilla();
   }

   public void setLouisvilleObfuscate(boolean var1) {
      this.louisvilleObfuscate = var1;
   }

   public void LoadAiringBroadcast(String var1, int var2) {
      if (this.currentScript != null) {
         this.airingBroadcast = this.currentScript.getBroadcastWithID(var1);
         if (var2 < 0) {
            this.airingBroadcast = null;
         }

         if (this.airingBroadcast != null && var2 >= 0) {
            this.airingBroadcast.resetLineCounter();
            this.airingBroadcast.setCurrentLineNumber(var2);
            this.airCounter = 120.0F;
            this.playerIsListening = true;
         }
      }

   }

   public void SetPlayerIsListening(boolean var1) {
      this.playerIsListening = var1;
      if (this.playerIsListening && this.airingBroadcast == null && this.currentScript != null) {
         this.airingBroadcast = this.currentScript.getValidAirBroadcast();
         if (this.airingBroadcast != null) {
            this.airingBroadcast.resetLineCounter();
         }

         this.airCounter = 0.0F;
      }

   }

   public boolean GetPlayerIsListening() {
      return this.playerIsListening;
   }

   public void setActiveScriptNull() {
      this.currentScript = null;
      this.airingBroadcast = null;
   }

   public void setActiveScript(String var1, int var2) {
      this.setActiveScript(var1, var2, 1, -1);
   }

   public void setActiveScript(String var1, int var2, int var3, int var4) {
      if (var1 != null && this.scripts.containsKey(var1)) {
         this.currentScript = (RadioScript)this.scripts.get(var1);
         if (this.currentScript != null) {
            this.currentScript.Reset();
            this.currentScript.setStartDayStamp(var2);
            this.currentScriptLoop = var3;
            if (var4 == -1) {
               int var5 = this.currentScript.getLoopMin();
               int var6 = this.currentScript.getLoopMax();
               if (var5 != var6 && var5 <= var6) {
                  var4 = Rand.Next(var5, var6);
               } else {
                  var4 = var5;
               }
            }

            this.currentScriptMaxLoops = var4;
            if (DebugLog.isEnabled(DebugType.Radio)) {
               DebugLog.Radio.println("Script: " + var1 + ", day = " + var2 + ", minloops = " + this.currentScript.getLoopMin() + ", maxloops = " + this.currentScriptMaxLoops);
            }
         }
      }

   }

   private void getNextScript(int var1) {
      if (this.currentScript != null) {
         if (this.currentScriptLoop < this.currentScriptMaxLoops) {
            ++this.currentScriptLoop;
            this.currentScript.Reset();
            this.currentScript.setStartDayStamp(var1);
         } else {
            RadioScript.ExitOption var2 = this.currentScript.getNextScript();
            this.currentScript = null;
            if (var2 != null) {
               this.setActiveScript(var2.getScriptname(), var1 + var2.getStartDelay());
            }
         }
      }

   }

   public void UpdateScripts(int var1, int var2) {
      this.playerIsListening = false;
      if (this.currentScript != null && !this.currentScript.UpdateScript(var1)) {
         this.getNextScript(var2 + 1);
      }

   }

   public void update() {
      if (this.airingBroadcast != null) {
         this.airCounter -= 1.25F * GameTime.getInstance().getMultiplier();
         if (this.airCounter < 0.0F) {
            RadioLine var1 = this.airingBroadcast.getNextLine();
            if (var1 == null) {
               this.lastBroadcastID = this.airingBroadcast.getID();
               this.airingBroadcast = null;
               this.playerIsListening = false;
            } else {
               this.lastAiredLine = var1.getText();
               if (!ZomboidRadio.DISABLE_BROADCASTING) {
                  String var2 = var1.getText();
                  if (this.louisvilleObfuscate && ZomboidRadio.LOUISVILLE_OBFUSCATION) {
                     var2 = ZomboidRadio.getInstance().scrambleString(var2, 85, true, (String)null);
                     ZomboidRadio.getInstance().SendTransmission(0, 0, this.frequency, var2, "", 0.7F, 0.5F, 0.5F, -1, this.isTv);
                  } else {
                     ZomboidRadio.getInstance().SendTransmission(0, 0, this.frequency, var2, var1.getEffectsString(), var1.getR(), var1.getG(), var1.getB(), -1, this.isTv);
                  }
               }

               if (var1.isCustomAirTime()) {
                  this.airCounter = var1.getAirTime() * 60.0F;
               } else {
                  this.airCounter = (float)var1.getText().length() / 10.0F * 60.0F;
                  if (this.airCounter < 60.0F * this.minmod) {
                     this.airCounter = 60.0F * this.minmod;
                  } else if (this.airCounter > 60.0F * this.maxmod) {
                     this.airCounter = 60.0F * this.maxmod;
                  }

                  this.airCounter *= this.airCounterMultiplier;
               }
            }
         }
      }

   }

   public void AddRadioScript(RadioScript var1) {
      if (var1 != null && !this.scripts.containsKey(var1.GetName())) {
         this.scripts.put(var1.GetName(), var1);
      } else {
         String var2 = var1 != null ? var1.GetName() : "null";
         DebugLog.log(DebugType.Radio, "Error while attempting to add script (" + var2 + "), null or name already exists.");
      }

   }

   public RadioScript getRadioScript(String var1) {
      return var1 != null && this.scripts.containsKey(var1) ? (RadioScript)this.scripts.get(var1) : null;
   }

   public void setAiringBroadcast(RadioBroadCast var1) {
      this.airingBroadcast = var1;
   }

   public float getAirCounterMultiplier() {
      return this.airCounterMultiplier;
   }

   public void setAirCounterMultiplier(float var1) {
      this.airCounterMultiplier = PZMath.clamp(var1, 0.1F, 10.0F);
   }
}
