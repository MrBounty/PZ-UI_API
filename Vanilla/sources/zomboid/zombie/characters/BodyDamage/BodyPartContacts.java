package zombie.characters.BodyDamage;

import java.util.ArrayList;
import zombie.debug.DebugLog;

public final class BodyPartContacts {
   private static final BodyPartContacts.ContactNode root;
   private static final BodyPartContacts.ContactNode[] nodes;

   public static BodyPartType[] getAllContacts(BodyPartType var0) {
      for(int var2 = 0; var2 < nodes.length; ++var2) {
         BodyPartContacts.ContactNode var1 = nodes[var2];
         if (var1.bodyPart == var0) {
            return var1.bodyPartAllContacts;
         }
      }

      return null;
   }

   public static BodyPartType[] getChildren(BodyPartType var0) {
      for(int var2 = 0; var2 < nodes.length; ++var2) {
         BodyPartContacts.ContactNode var1 = nodes[var2];
         if (var1.bodyPart == var0) {
            return var1.bodyPartChildren;
         }
      }

      return null;
   }

   public static BodyPartType getParent(BodyPartType var0) {
      for(int var2 = 0; var2 < nodes.length; ++var2) {
         BodyPartContacts.ContactNode var1 = nodes[var2];
         if (var1.bodyPart == var0) {
            if (var1.depth == 0) {
               DebugLog.log("Warning, root node parent is always null.");
            }

            return var1.bodyPartParent;
         }
      }

      return null;
   }

   public static int getNodeDepth(BodyPartType var0) {
      for(int var2 = 0; var2 < nodes.length; ++var2) {
         BodyPartContacts.ContactNode var1 = nodes[var2];
         if (var1.bodyPart == var0) {
            if (!var1.initialised) {
               DebugLog.log("Warning: attempting to get depth for non initialised node '" + var1.bodyPart.toString() + "'.");
            }

            return var1.depth;
         }
      }

      return -1;
   }

   private static BodyPartContacts.ContactNode getNodeForBodyPart(BodyPartType var0) {
      for(int var1 = 0; var1 < nodes.length; ++var1) {
         if (nodes[var1].bodyPart == var0) {
            return nodes[var1];
         }
      }

      return null;
   }

   private static void initNodes(BodyPartContacts.ContactNode var0, int var1, BodyPartContacts.ContactNode var2) {
      var0.parent = var2;
      var0.depth = var1;
      ArrayList var3 = new ArrayList();
      if (var0.parent != null) {
         var3.add(var0.parent);
      }

      if (var0.children != null) {
         BodyPartContacts.ContactNode[] var4 = var0.children;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            BodyPartContacts.ContactNode var7 = var4[var6];
            var3.add(var7);
            initNodes(var7, var1 + 1, var0);
         }
      }

      var0.allContacts = new BodyPartContacts.ContactNode[var3.size()];
      var3.toArray(var0.allContacts);
      var0.initialised = true;
   }

   private static void postInit() {
      BodyPartContacts.ContactNode[] var0 = nodes;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         BodyPartContacts.ContactNode var3 = var0[var2];
         if (var3.parent != null) {
            var3.bodyPartParent = var3.parent.bodyPart;
         }

         int var4;
         if (var3.children != null && var3.children.length > 0) {
            var3.bodyPartChildren = new BodyPartType[var3.children.length];

            for(var4 = 0; var4 < var3.children.length; ++var4) {
               var3.bodyPartChildren[var4] = var3.children[var4].bodyPart;
            }
         } else {
            var3.bodyPartChildren = new BodyPartType[0];
         }

         if (var3.allContacts != null && var3.allContacts.length > 0) {
            var3.bodyPartAllContacts = new BodyPartType[var3.allContacts.length];

            for(var4 = 0; var4 < var3.allContacts.length; ++var4) {
               var3.bodyPartAllContacts[var4] = var3.allContacts[var4].bodyPart;
            }
         } else {
            var3.bodyPartAllContacts = new BodyPartType[0];
         }

         if (!var3.initialised) {
            DebugLog.log("Warning: node for '" + var3.bodyPart.toString() + "' is not initialised!");
         }
      }

   }

   static {
      int var0 = BodyPartType.ToIndex(BodyPartType.MAX);
      nodes = new BodyPartContacts.ContactNode[var0];

      for(int var1 = 0; var1 < var0; ++var1) {
         nodes[var1] = new BodyPartContacts.ContactNode(BodyPartType.FromIndex(var1));
      }

      root = getNodeForBodyPart(BodyPartType.Torso_Upper);
      root.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.Neck), getNodeForBodyPart(BodyPartType.Torso_Lower), getNodeForBodyPart(BodyPartType.UpperArm_L), getNodeForBodyPart(BodyPartType.UpperArm_R)};
      BodyPartContacts.ContactNode var2 = getNodeForBodyPart(BodyPartType.Neck);
      var2.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.Head)};
      var2 = getNodeForBodyPart(BodyPartType.UpperArm_L);
      var2.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.ForeArm_L)};
      var2 = getNodeForBodyPart(BodyPartType.ForeArm_L);
      var2.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.Hand_L)};
      var2 = getNodeForBodyPart(BodyPartType.UpperArm_R);
      var2.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.ForeArm_R)};
      var2 = getNodeForBodyPart(BodyPartType.ForeArm_R);
      var2.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.Hand_R)};
      var2 = getNodeForBodyPart(BodyPartType.Torso_Lower);
      var2.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.Groin)};
      var2 = getNodeForBodyPart(BodyPartType.Groin);
      var2.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.UpperLeg_L), getNodeForBodyPart(BodyPartType.UpperLeg_R)};
      var2 = getNodeForBodyPart(BodyPartType.UpperLeg_L);
      var2.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.LowerLeg_L)};
      var2 = getNodeForBodyPart(BodyPartType.LowerLeg_L);
      var2.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.Foot_L)};
      var2 = getNodeForBodyPart(BodyPartType.UpperLeg_R);
      var2.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.LowerLeg_R)};
      var2 = getNodeForBodyPart(BodyPartType.LowerLeg_R);
      var2.children = new BodyPartContacts.ContactNode[]{getNodeForBodyPart(BodyPartType.Foot_R)};
      initNodes(root, 0, (BodyPartContacts.ContactNode)null);
      postInit();
   }

   private static class ContactNode {
      BodyPartType bodyPart;
      int depth = -1;
      BodyPartContacts.ContactNode parent;
      BodyPartContacts.ContactNode[] children;
      BodyPartContacts.ContactNode[] allContacts;
      BodyPartType bodyPartParent;
      BodyPartType[] bodyPartChildren;
      BodyPartType[] bodyPartAllContacts;
      boolean initialised = false;

      public ContactNode(BodyPartType var1) {
         this.bodyPart = var1;
      }
   }
}
