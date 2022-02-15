package zombie.characters.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import org.w3c.dom.Element;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.util.PZXmlUtil;
import zombie.util.StringUtils;
import zombie.util.list.PZArrayUtil;

public final class ActionState {
   public final String name;
   public final ArrayList transitions = new ArrayList();
   private String[] m_tags;
   private String[] m_childTags;
   private static final Comparator transitionComparator = (var0, var1) -> {
      return var1.conditions.size() - var0.conditions.size();
   };

   public ActionState(String var1) {
      this.name = var1;
   }

   public final boolean canHaveSubStates() {
      return !PZArrayUtil.isNullOrEmpty((Object[])this.m_childTags);
   }

   public final boolean canBeSubstate() {
      return !PZArrayUtil.isNullOrEmpty((Object[])this.m_tags);
   }

   public final boolean canHaveSubState(ActionState var1) {
      return canHaveSubState(this, var1);
   }

   public static boolean canHaveSubState(ActionState var0, ActionState var1) {
      String[] var2 = var0.m_childTags;
      String[] var3 = var1.m_tags;
      return tagsOverlap(var2, var3);
   }

   public static boolean tagsOverlap(String[] var0, String[] var1) {
      if (PZArrayUtil.isNullOrEmpty((Object[])var0)) {
         return false;
      } else if (PZArrayUtil.isNullOrEmpty((Object[])var1)) {
         return false;
      } else {
         boolean var2 = false;

         for(int var3 = 0; var3 < var0.length; ++var3) {
            String var4 = var0[var3];

            for(int var5 = 0; var5 < var1.length; ++var5) {
               String var6 = var1[var5];
               if (StringUtils.equalsIgnoreCase(var4, var6)) {
                  var2 = true;
                  break;
               }
            }
         }

         return var2;
      }
   }

   public String getName() {
      return this.name;
   }

   public void load(String var1) {
      File var2 = (new File(var1)).getAbsoluteFile();
      File[] var3 = var2.listFiles((var0, var1x) -> {
         return var1x.toLowerCase().endsWith(".xml");
      });
      if (var3 != null) {
         File[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            this.parse(var7);
         }

         this.sortTransitions();
      }
   }

   public void parse(File var1) {
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      String var5 = var1.getPath();

      try {
         Element var6 = PZXmlUtil.parseXml(var5);
         if (ActionTransition.parse(var6, var5, var2)) {
            this.transitions.addAll(var2);
            if (DebugLog.isEnabled(DebugType.ActionSystem)) {
               DebugLog.ActionSystem.debugln("Loaded transitions from file: %s", var5);
            }

            return;
         }

         if (this.parseTags(var6, var3, var4)) {
            this.m_tags = (String[])PZArrayUtil.concat(this.m_tags, (String[])var3.toArray(new String[0]));
            this.m_childTags = (String[])PZArrayUtil.concat(this.m_childTags, (String[])var4.toArray(new String[0]));
            if (DebugLog.isEnabled(DebugType.ActionSystem)) {
               DebugLog.ActionSystem.debugln("Loaded tags from file: %s", var5);
            }

            return;
         }

         if (DebugLog.isEnabled(DebugType.ActionSystem)) {
            DebugLog.ActionSystem.warn("Unrecognized xml file. It does not appear to be a transition nor a tag(s). %s", var5);
         }
      } catch (Exception var7) {
         DebugLog.ActionSystem.error("Error loading: " + var5);
         DebugLog.ActionSystem.error(var7);
      }

   }

   private boolean parseTags(Element var1, ArrayList var2, ArrayList var3) {
      var2.clear();
      var3.clear();
      if (var1.getNodeName().equals("tags")) {
         PZXmlUtil.forEachElement(var1, (var1x) -> {
            if (var1x.getNodeName().equals("tag")) {
               var3.add(var1x.getTextContent());
            }

         });
         return true;
      } else if (var1.getNodeName().equals("childTags")) {
         PZXmlUtil.forEachElement(var1, (var1x) -> {
            if (var1x.getNodeName().equals("tag")) {
               var3.add(var1x.getTextContent());
            }

         });
         return true;
      } else {
         return false;
      }
   }

   public void sortTransitions() {
      this.transitions.sort(transitionComparator);
   }

   public void resetForReload() {
      this.transitions.clear();
      this.m_tags = null;
      this.m_childTags = null;
   }
}
