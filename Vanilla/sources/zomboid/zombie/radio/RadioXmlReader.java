package zombie.radio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import zombie.core.Rand;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.radio.globals.RadioGlobal;
import zombie.radio.globals.RadioGlobalBool;
import zombie.radio.globals.RadioGlobalFloat;
import zombie.radio.globals.RadioGlobalInt;
import zombie.radio.globals.RadioGlobalString;
import zombie.radio.globals.RadioGlobalType;
import zombie.radio.globals.RadioGlobalsManager;
import zombie.radio.scripting.RadioBroadCast;
import zombie.radio.scripting.RadioChannel;
import zombie.radio.scripting.RadioLine;
import zombie.radio.scripting.RadioScript;
import zombie.radio.scripting.RadioScriptManager;

public final class RadioXmlReader {
   private boolean printDebug;
   private ArrayList globalQueue;
   private ArrayList channelQueue;
   private Map advertQue;
   private final String charsNormal;
   private final String charsEncrypt;
   private String radioVersion;
   private float version;
   private float formatVersion;
   private final Map radioFileSettings;

   public RadioXmlReader() {
      this(false);
   }

   public RadioXmlReader(boolean var1) {
      this.printDebug = false;
      this.charsNormal = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
      this.charsEncrypt = "UVWKLMABCDEFGXYZHIJOPQRSTNuvwklmabcdefgxyzhijopqrstn";
      this.radioVersion = "1.0";
      this.version = 1.0F;
      this.formatVersion = 1.0F;
      this.radioFileSettings = new HashMap();
      this.printDebug = var1;
   }

   public static RadioData ReadFileHeader(String var0) {
      new RadioXmlReader(ZomboidRadio.DEBUG_XML);
      return null;
   }

   private void readfileheader(String var1) throws ParserConfigurationException, IOException, SAXException {
      File var2 = new File(var1);
      DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
      DocumentBuilder var4 = var3.newDocumentBuilder();
      Document var5 = var4.parse(var2);
      var5.getDocumentElement().normalize();
      NodeList var6 = var5.getElementsByTagName("RadioData");
      if (var6.getLength() > 0) {
         Node var7 = var6.item(0);
         Node var8 = null;
         Iterator var9 = this.getChildNodes(var7).iterator();

         while(var9.hasNext()) {
            Node var10 = (Node)var9.next();
            if (this.nodeNameIs(var10, "RootInfo")) {
               var8 = var10;
               break;
            }
         }

         this.loadRootInfo(var8);
      }

   }

   public static boolean LoadFile(String var0) {
      RadioXmlReader var1 = new RadioXmlReader(ZomboidRadio.DEBUG_XML);

      try {
         var1.start(var0);
      } catch (Exception var7) {
         DebugLog.log(DebugType.Radio, "Error loading radio system: " + var7.getMessage());
         var7.printStackTrace();
         boolean var3 = false;
      } finally {
         DebugLog.log(DebugType.Radio, "RadioSystem online.");
         return true;
      }
   }

   public static ArrayList LoadTranslatorNames(String var0) {
      ArrayList var1 = new ArrayList();
      RadioXmlReader var2 = new RadioXmlReader(ZomboidRadio.DEBUG_XML);

      try {
         ArrayList var3 = var2.readTranslatorNames(var0);
         var1 = var3;
      } catch (Exception var8) {
         DebugLog.log(DebugType.Radio, "Error reading translator names: " + var8.getMessage());
         var8.printStackTrace();
      } finally {
         DebugLog.log(DebugType.Radio, "Returning translator names.");
         return var1;
      }
   }

   private void print(String var1) {
      if (this.printDebug) {
         DebugLog.log(DebugType.Radio, var1);
      }

   }

   private ArrayList getChildNodes(Node var1) {
      ArrayList var2 = new ArrayList();
      if (var1.hasChildNodes()) {
         Node var3 = var1.getFirstChild();

         while(var3 != null) {
            if (!(var3 instanceof Element)) {
               var3 = var3.getNextSibling();
            } else {
               var2.add(var3);
               var3 = var3.getNextSibling();
            }
         }
      }

      return var2;
   }

   private boolean nodeNameIs(Node var1, String var2) {
      return var1.getNodeName().equals(var2);
   }

   private String getAttrib(Node var1, String var2, boolean var3) {
      return this.getAttrib(var1, var2, var3, false);
   }

   private String getAttrib(Node var1, String var2) {
      return this.getAttrib(var1, var2, true, false).trim();
   }

   private String getAttrib(Node var1, String var2, boolean var3, boolean var4) {
      String var5 = var1.getAttributes().getNamedItem(var2).getTextContent();
      if (var3) {
         var5 = var5.trim();
      }

      if (var4) {
         var5 = var5.toLowerCase();
      }

      return var5;
   }

   private RadioGlobal getGlobalFromQueue(String var1) {
      Iterator var2 = this.globalQueue.iterator();

      RadioGlobal var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (RadioGlobal)var2.next();
      } while(var3 == null || !var3.getName().equals(var1));

      return var3;
   }

   private RadioGlobal createGlobal(String var1, String var2) {
      return this.createGlobal("", var1, var2);
   }

   private RadioGlobal createGlobal(String var1, String var2, String var3) {
      if (var1 != null && var2 != null && var3 != null) {
         RadioGlobalType var4 = RadioGlobalType.valueOf(var2.trim());
         switch(var4) {
         case String:
            return new RadioGlobalString(var1, var3);
         case Integer:
            return new RadioGlobalInt(var1, Integer.parseInt(var3.trim()));
         case Float:
            return new RadioGlobalFloat(var1, Float.parseFloat(var3.trim()));
         case Boolean:
            return new RadioGlobalBool(var1, Boolean.parseBoolean(var3.trim().toLowerCase()));
         default:
            return null;
         }
      } else {
         return null;
      }
   }

   private ArrayList readTranslatorNames(String var1) throws ParserConfigurationException, IOException, SAXException {
      File var2 = new File(var1);
      DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
      DocumentBuilder var4 = var3.newDocumentBuilder();
      Document var5 = var4.parse(var2);
      var5.getDocumentElement().normalize();
      ArrayList var6 = new ArrayList();
      NodeList var7 = var5.getElementsByTagName("TranslationData");
      if (var7.getLength() > 0) {
         Node var8 = var7.item(0);
         Iterator var9 = this.getChildNodes(var8).iterator();

         while(var9.hasNext()) {
            Node var10 = (Node)var9.next();
            if (this.nodeNameIs(var10, "RootInfo")) {
               Iterator var11 = this.getChildNodes(var10).iterator();

               Node var12;
               do {
                  if (!var11.hasNext()) {
                     return var6;
                  }

                  var12 = (Node)var11.next();
               } while(!this.nodeNameIs(var12, "Translators"));

               Iterator var13 = this.getChildNodes(var12).iterator();

               while(var13.hasNext()) {
                  Node var14 = (Node)var13.next();
                  String var15 = this.getAttrib(var14, "name", true, false);
                  if (var15 != null) {
                     var6.add(var15);
                  }
               }

               return var6;
            }
         }
      }

      return var6;
   }

   private void start(String var1) throws ParserConfigurationException, IOException, SAXException {
      File var2 = new File(var1);
      this.print("RadioDataFile: " + var2.getAbsolutePath());
      DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
      DocumentBuilder var4 = var3.newDocumentBuilder();
      Document var5 = var4.parse(var2);
      var5.getDocumentElement().normalize();
      this.globalQueue = new ArrayList();
      this.channelQueue = new ArrayList();
      this.advertQue = new HashMap();
      NodeList var6 = var5.getElementsByTagName("RadioData");
      Iterator var9;
      if (var6.getLength() > 0) {
         Node var7 = var6.item(0);
         Node var8 = null;
         var9 = this.getChildNodes(var7).iterator();

         Node var10;
         while(var9.hasNext()) {
            var10 = (Node)var9.next();
            if (this.nodeNameIs(var10, "RootInfo")) {
               var8 = var10;
               break;
            }
         }

         this.loadRootInfo(var8);
         var9 = this.getChildNodes(var7).iterator();

         while(var9.hasNext()) {
            var10 = (Node)var9.next();
            if (this.nodeNameIs(var10, "Globals")) {
               this.loadGlobals(var10);
            } else if (this.nodeNameIs(var10, "Adverts")) {
               this.loadAdverts(var10);
            } else if (this.nodeNameIs(var10, "Channels")) {
               this.loadChannels(var10);
            }
         }
      }

      RadioGlobalsManager var11 = RadioGlobalsManager.getInstance();
      Iterator var12 = this.globalQueue.iterator();

      while(var12.hasNext()) {
         RadioGlobal var14 = (RadioGlobal)var12.next();
         var11.addGlobal(var14.getName(), var14);
      }

      RadioScriptManager var13 = RadioScriptManager.getInstance();
      var9 = this.channelQueue.iterator();

      while(var9.hasNext()) {
         RadioChannel var15 = (RadioChannel)var9.next();
         var13.AddChannel(var15, false);
      }

   }

   private void loadRootInfo(Node var1) {
      this.print(">>> Loading root info...");
      if (var1 == null) {
         this.print(" -> root info not found, default version = " + this.radioVersion);
         this.radioFileSettings.put("Version", this.radioVersion);
      } else {
         this.print(" -> Reading RootInfo");
         Iterator var2 = this.getChildNodes(var1).iterator();

         while(var2.hasNext()) {
            Node var3 = (Node)var2.next();
            String var4 = var3.getNodeName();
            String var5 = var3.getTextContent();
            if (var4 != null && var5 != null) {
               this.print("   -> " + var4 + " = " + var5);
               this.radioFileSettings.put(var4, var5);
               if (var4.equals("Version")) {
                  this.radioVersion = var5;
                  this.version = Float.parseFloat(this.radioVersion);
               }
            }
         }

      }
   }

   private void loadGlobals(Node var1) {
      this.print(">>> Loading globals...");
      Iterator var2 = this.getChildNodes(var1).iterator();

      while(var2.hasNext()) {
         Node var3 = (Node)var2.next();
         if (this.nodeNameIs(var3, "GlobalEntry")) {
            String var4 = this.getAttrib(var3, "name");
            String var5 = this.getAttrib(var3, "type");
            String var6 = var3.getTextContent();
            this.print(" -> Found global, name: " + var4 + ", type: " + var5 + ", value: " + var6);
            RadioGlobal var7 = this.createGlobal(var4, var5, var6);
            if (var7 != null) {
               this.globalQueue.add(var7);
            } else {
               this.print(" -> Error adding Global, name: " + var4 + ", type: " + var5 + ", value: " + var6);
            }
         }
      }

   }

   private void loadAdverts(Node var1) {
      this.print(">>> Loading adverts...");
      Iterator var2 = this.getChildNodes(var1).iterator();

      while(true) {
         Node var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (Node)var2.next();
         } while(!this.nodeNameIs(var3, "AdvertCategory"));

         String var4 = this.getAttrib(var3, "name");
         if (!this.advertQue.containsKey(var4)) {
            this.advertQue.put(var4, new ArrayList());
         }

         this.print(" -> Found category: " + var4);
         Iterator var5 = this.getChildNodes(var3).iterator();

         while(var5.hasNext()) {
            Node var6 = (Node)var5.next();
            RadioBroadCast var7 = this.loadBroadcast(var6, (RadioScript)null);
            ((ArrayList)this.advertQue.get(var4)).add(var7);
         }
      }
   }

   private void loadChannels(Node var1) {
      this.print(">>> Loading channels...");
      Iterator var2 = this.getChildNodes(var1).iterator();

      while(var2.hasNext()) {
         Node var3 = (Node)var2.next();
         if (this.nodeNameIs(var3, "ChannelEntry")) {
            String var4 = this.getAttrib(var3, "name");
            String var5 = this.getAttrib(var3, "cat");
            String var6 = this.getAttrib(var3, "freq");
            String var7 = this.getAttrib(var3, "startscript");
            this.print(" -> Found channel: " + var4 + ", on freq: " + var6 + " , category: " + var5 + ", startscript: " + var7);
            RadioChannel var8 = new RadioChannel(var4, Integer.parseInt(var6), ChannelCategory.valueOf(var5));
            this.loadScripts(var3, var8);
            var8.setActiveScript(var7, 0);
            this.channelQueue.add(var8);
         }
      }

   }

   private void loadScripts(Node var1, RadioChannel var2) {
      this.print(" --> Loading scripts...");
      Iterator var3 = this.getChildNodes(var1).iterator();

      while(true) {
         Node var4;
         do {
            if (!var3.hasNext()) {
               return;
            }

            var4 = (Node)var3.next();
         } while(!this.nodeNameIs(var4, "ScriptEntry"));

         String var5 = this.getAttrib(var4, "name");
         String var6 = this.getAttrib(var4, "loopmin");
         String var7 = this.getAttrib(var4, "loopmin");
         this.print(" ---> Found script: " + var5);
         RadioScript var8 = new RadioScript(var5, Integer.parseInt(var6), Integer.parseInt(var7));
         Iterator var9 = this.getChildNodes(var4).iterator();

         while(var9.hasNext()) {
            Node var10 = (Node)var9.next();
            if (this.nodeNameIs(var10, "BroadcastEntry")) {
               this.loadBroadcast(var10, var8);
            } else if (this.nodeNameIs(var10, "ExitOptions")) {
               this.loadExitOptions(var10, var8);
            }
         }

         var2.AddRadioScript(var8);
      }
   }

   private RadioBroadCast loadBroadcast(Node var1, RadioScript var2) {
      String var3 = this.getAttrib(var1, "ID");
      String var4 = this.getAttrib(var1, "timestamp");
      String var5 = this.getAttrib(var1, "endstamp");
      this.print(" ----> BroadCast, Timestamp: " + var4 + ", endstamp: " + var5);
      int var6 = Integer.parseInt(var4);
      int var7 = Integer.parseInt(var5);
      String var8 = this.getAttrib(var1, "preCat");
      int var9 = Integer.parseInt(this.getAttrib(var1, "preChance"));
      String var10 = this.getAttrib(var1, "postCat");
      int var11 = Integer.parseInt(this.getAttrib(var1, "postChance"));
      RadioBroadCast var12 = new RadioBroadCast(var3, var6, var7);
      int var13;
      int var14;
      if (!var8.equals("none") && this.advertQue.containsKey(var8)) {
         var13 = Rand.Next(101);
         var14 = ((ArrayList)this.advertQue.get(var8)).size();
         if (var14 > 0 && var13 <= var9) {
            var12.setPreSegment((RadioBroadCast)((ArrayList)this.advertQue.get(var8)).get(Rand.Next(var14)));
         }
      }

      if (!var10.equals("none") && this.advertQue.containsKey(var10)) {
         var13 = Rand.Next(101);
         var14 = ((ArrayList)this.advertQue.get(var10)).size();
         if (var14 > 0 && var13 <= var11) {
            var12.setPostSegment((RadioBroadCast)((ArrayList)this.advertQue.get(var10)).get(Rand.Next(var14)));
         }
      }

      Iterator var26 = this.getChildNodes(var1).iterator();

      while(true) {
         Node var27;
         do {
            if (!var26.hasNext()) {
               if (var2 != null) {
                  var2.AddBroadcast(var12);
               }

               return var12;
            }

            var27 = (Node)var26.next();
         } while(!this.nodeNameIs(var27, "LineEntry"));

         String var15 = this.getAttrib(var27, "r");
         String var16 = this.getAttrib(var27, "g");
         String var17 = this.getAttrib(var27, "b");
         String var18 = null;
         String var19 = var27.getTextContent();
         this.print(" -----> New Line, Color: " + var15 + ", " + var16 + ", " + var17);
         Iterator var20 = this.getChildNodes(var27).iterator();

         label59:
         while(var20.hasNext()) {
            Node var21 = (Node)var20.next();
            if (this.nodeNameIs(var21, "LineEffects")) {
               var18 = "";
               Iterator var22 = this.getChildNodes(var27).iterator();

               while(true) {
                  if (!var22.hasNext()) {
                     break label59;
                  }

                  Node var23 = (Node)var22.next();
                  if (this.nodeNameIs(var23, "Effect")) {
                     String var24 = this.getAttrib(var23, "tag");
                     String var25 = this.getAttrib(var23, "value");
                     var18 = var18 + var24 + "=" + var25 + ",";
                  }
               }
            }
         }

         var19 = this.simpleDecrypt(var19);
         RadioLine var28 = new RadioLine(var19, Float.parseFloat(var15) / 255.0F, Float.parseFloat(var16) / 255.0F, Float.parseFloat(var17) / 255.0F, var18);
         var12.AddRadioLine(var28);
      }
   }

   private String simpleDecrypt(String var1) {
      String var2 = "";

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         if ("UVWKLMABCDEFGXYZHIJOPQRSTNuvwklmabcdefgxyzhijopqrstn".indexOf(var4) != -1) {
            var2 = var2 + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt("UVWKLMABCDEFGXYZHIJOPQRSTNuvwklmabcdefgxyzhijopqrstn".indexOf(var4));
         } else {
            var2 = var2 + var4;
         }
      }

      return var2;
   }

   private void loadExitOptions(Node var1, RadioScript var2) {
      Iterator var3 = this.getChildNodes(var1).iterator();

      while(var3.hasNext()) {
         Node var4 = (Node)var3.next();
         if (this.nodeNameIs(var4, "ExitOption")) {
            String var5 = this.getAttrib(var4, "script");
            String var6 = this.getAttrib(var4, "chance");
            String var7 = this.getAttrib(var4, "delay");
            int var8 = Integer.parseInt(var6);
            int var9 = Integer.parseInt(var7);
            var2.AddExitOption(var5, var8, var9);
         }
      }

   }
}
