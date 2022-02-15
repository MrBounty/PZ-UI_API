package zombie.characters.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import zombie.ZomboidFileSystem;
import zombie.debug.DebugLog;
import zombie.debug.DebugLogStream;
import zombie.debug.DebugType;

public final class ActionGroup {
   private static final Map actionGroupMap = new HashMap();
   String initialState;
   private List states = new ArrayList();
   private Map stateLookup;

   public static ActionGroup getActionGroup(String var0) {
      var0 = var0.toLowerCase();
      ActionGroup var1 = (ActionGroup)actionGroupMap.get(var0);
      if (var1 == null && !actionGroupMap.containsKey(var0)) {
         var1 = new ActionGroup();
         actionGroupMap.put(var0, var1);

         try {
            var1.load(var0);
         } catch (Exception var3) {
            DebugLog.ActionSystem.error("Error loading action group: " + var0);
            var3.printStackTrace(DebugLog.ActionSystem);
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void reloadAll() {
      Iterator var0 = actionGroupMap.entrySet().iterator();

      while(var0.hasNext()) {
         Entry var1 = (Entry)var0.next();
         ActionGroup var2 = (ActionGroup)var1.getValue();
         Iterator var3 = var2.states.iterator();

         while(var3.hasNext()) {
            ActionState var4 = (ActionState)var3.next();
            var4.resetForReload();
         }

         var2.load((String)var1.getKey());
      }

   }

   void load(String var1) {
      if (DebugLog.isEnabled(DebugType.ActionSystem)) {
         DebugLog.ActionSystem.debugln("Loading ActionGroup: " + var1);
      }

      File var2 = ZomboidFileSystem.instance.getMediaFile("actiongroups/" + var1 + "/actionGroup.xml");
      if (var2.exists() && var2.canRead()) {
         this.loadGroupData(var2);
      }

      File var3 = ZomboidFileSystem.instance.getMediaFile("actiongroups/" + var1);
      File[] var4 = var3.listFiles();
      if (var4 != null) {
         File[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            File var8 = var5[var7];
            if (var8.isDirectory()) {
               String var9 = var8.getPath();
               ActionState var10 = this.getOrCreate(var8.getName());
               var10.load(var9);
            }
         }
      }

   }

   private void loadGroupData(File var1) {
      Document var2;
      try {
         DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var4 = var3.newDocumentBuilder();
         var2 = var4.parse(var1);
      } catch (SAXException | IOException | ParserConfigurationException var8) {
         DebugLog.ActionSystem.error("Error loading: " + var1.getPath());
         var8.printStackTrace(DebugLog.ActionSystem);
         return;
      }

      var2.getDocumentElement().normalize();
      Element var9 = var2.getDocumentElement();
      if (!var9.getNodeName().equals("actiongroup")) {
         DebugLogStream var10000 = DebugLog.ActionSystem;
         String var10001 = var1.getPath();
         var10000.error("Error loading: " + var10001 + ", expected root element '<actiongroup>', received '<" + var9.getNodeName() + ">'");
      } else {
         Node var10;
         for(var10 = var9.getFirstChild(); var10 != null; var10 = var10.getNextSibling()) {
            if (var10.getNodeName().equals("inherit") && var10 instanceof Element) {
               String var5 = var10.getTextContent().trim();
               this.inherit(getActionGroup(var5));
            }
         }

         for(var10 = var9.getFirstChild(); var10 != null; var10 = var10.getNextSibling()) {
            if (var10 instanceof Element) {
               Element var11 = (Element)var10;
               String var6 = var11.getNodeName();
               byte var7 = -1;
               switch(var6.hashCode()) {
               case 1946980603:
                  if (var6.equals("inherit")) {
                     var7 = 1;
                  }
                  break;
               case 1948342084:
                  if (var6.equals("initial")) {
                     var7 = 0;
                  }
               }

               switch(var7) {
               case 0:
                  this.initialState = var11.getTextContent().trim();
               case 1:
                  break;
               default:
                  DebugLog.ActionSystem.warn("Warning: Unknown element '<>' in '" + var1.getPath() + "'");
               }
            }
         }

      }
   }

   private void inherit(ActionGroup var1) {
      if (var1 != null) {
         if (var1.initialState != null) {
            this.initialState = var1.initialState;
         }

         Iterator var2 = var1.states.iterator();

         while(var2.hasNext()) {
            ActionState var3 = (ActionState)var2.next();
            ActionState var4 = this.getOrCreate(var3.name);
            Iterator var5 = var3.transitions.iterator();

            while(var5.hasNext()) {
               ActionTransition var6 = (ActionTransition)var5.next();
               var4.transitions.add(var6.clone());
               var4.sortTransitions();
            }
         }

      }
   }

   private void rebuildLookup() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.states.iterator();

      while(var2.hasNext()) {
         ActionState var3 = (ActionState)var2.next();
         var1.put(var3.name.toLowerCase(), var3);
      }

      this.stateLookup = var1;
   }

   public void addState(ActionState var1) {
      this.states.add(var1);
      this.stateLookup = null;
   }

   public ActionState get(String var1) {
      if (this.stateLookup == null) {
         this.rebuildLookup();
      }

      return (ActionState)this.stateLookup.get(var1.toLowerCase());
   }

   ActionState getOrCreate(String var1) {
      if (this.stateLookup == null) {
         this.rebuildLookup();
      }

      var1 = var1.toLowerCase();
      ActionState var2 = (ActionState)this.stateLookup.get(var1);
      if (var2 == null) {
         var2 = new ActionState(var1);
         this.states.add(var2);
         this.stateLookup.put(var1, var2);
      }

      return var2;
   }

   public ActionState getInitialState() {
      ActionState var1 = null;
      if (this.initialState != null) {
         var1 = this.get(this.initialState);
      }

      if (var1 == null && this.states.size() > 0) {
         var1 = (ActionState)this.states.get(0);
      }

      return var1;
   }

   public ActionState getDefaultState() {
      return this.getInitialState();
   }
}
