package zombie.core.skinnedmodel.model.jassimp;

import gnu.trove.map.hash.TObjectIntHashMap;
import jassimp.AiAnimation;
import jassimp.AiBone;
import jassimp.AiBuiltInWrapperProvider;
import jassimp.AiMaterial;
import jassimp.AiMatrix4f;
import jassimp.AiMesh;
import jassimp.AiNode;
import jassimp.AiNodeAnim;
import jassimp.AiScene;
import jassimp.Jassimp;
import jassimp.JassimpLibraryLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import zombie.core.Core;
import zombie.core.skinnedmodel.model.VertexPositionNormalTangentTexture;
import zombie.core.skinnedmodel.model.VertexPositionNormalTangentTextureSkin;
import zombie.util.SharedStrings;
import zombie.util.list.PZArrayUtil;

public final class JAssImpImporter {
   private static final TObjectIntHashMap sharedStringCounts = new TObjectIntHashMap();
   private static final SharedStrings sharedStrings = new SharedStrings();
   private static final HashMap tempHashMap = new HashMap();

   public static void Init() {
      Jassimp.setLibraryLoader(new JAssImpImporter.LibraryLoader());
   }

   static AiNode FindNode(String var0, AiNode var1) {
      List var2 = var1.getChildren();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         AiNode var4 = (AiNode)var2.get(var3);
         if (var4.getName().equals(var0)) {
            return var4;
         }

         AiNode var5 = FindNode(var0, var4);
         if (var5 != null) {
            return var5;
         }
      }

      return null;
   }

   static Matrix4f getMatrixFromAiMatrix(AiMatrix4f var0) {
      return getMatrixFromAiMatrix(var0, new Matrix4f());
   }

   static Matrix4f getMatrixFromAiMatrix(AiMatrix4f var0, Matrix4f var1) {
      var1.m00 = var0.get(0, 0);
      var1.m01 = var0.get(0, 1);
      var1.m02 = var0.get(0, 2);
      var1.m03 = var0.get(0, 3);
      var1.m10 = var0.get(1, 0);
      var1.m11 = var0.get(1, 1);
      var1.m12 = var0.get(1, 2);
      var1.m13 = var0.get(1, 3);
      var1.m20 = var0.get(2, 0);
      var1.m21 = var0.get(2, 1);
      var1.m22 = var0.get(2, 2);
      var1.m23 = var0.get(2, 3);
      var1.m30 = var0.get(3, 0);
      var1.m31 = var0.get(3, 1);
      var1.m32 = var0.get(3, 2);
      var1.m33 = var0.get(3, 3);
      return var1;
   }

   static void CollectBoneNodes(ArrayList var0, AiNode var1) {
      var0.add(var1);

      for(int var2 = 0; var2 < var1.getNumChildren(); ++var2) {
         CollectBoneNodes(var0, (AiNode)var1.getChildren().get(var2));
      }

   }

   static String DumpAiMatrix(AiMatrix4f var0) {
      String var1 = "";
      var1 = var1 + String.format("%1$.8f, ", var0.get(0, 0));
      var1 = var1 + String.format("%1$.8f, ", var0.get(0, 1));
      var1 = var1 + String.format("%1$.8f, ", var0.get(0, 2));
      var1 = var1 + String.format("%1$.8f\n ", var0.get(0, 3));
      var1 = var1 + String.format("%1$.8f, ", var0.get(1, 0));
      var1 = var1 + String.format("%1$.8f, ", var0.get(1, 1));
      var1 = var1 + String.format("%1$.8f, ", var0.get(1, 2));
      var1 = var1 + String.format("%1$.8f\n ", var0.get(1, 3));
      var1 = var1 + String.format("%1$.8f, ", var0.get(2, 0));
      var1 = var1 + String.format("%1$.8f, ", var0.get(2, 1));
      var1 = var1 + String.format("%1$.8f, ", var0.get(2, 2));
      var1 = var1 + String.format("%1$.8f\n ", var0.get(2, 3));
      var1 = var1 + String.format("%1$.8f, ", var0.get(3, 0));
      var1 = var1 + String.format("%1$.8f, ", var0.get(3, 1));
      var1 = var1 + String.format("%1$.8f, ", var0.get(3, 2));
      var1 = var1 + String.format("%1$.8f\n ", var0.get(3, 3));
      return var1;
   }

   static String DumpMatrix(Matrix4f var0) {
      String var1 = "";
      var1 = var1 + String.format("%1$.8f, ", var0.m00);
      var1 = var1 + String.format("%1$.8f, ", var0.m01);
      var1 = var1 + String.format("%1$.8f, ", var0.m02);
      var1 = var1 + String.format("%1$.8f\n ", var0.m03);
      var1 = var1 + String.format("%1$.8f, ", var0.m10);
      var1 = var1 + String.format("%1$.8f, ", var0.m11);
      var1 = var1 + String.format("%1$.8f, ", var0.m12);
      var1 = var1 + String.format("%1$.8f\n ", var0.m13);
      var1 = var1 + String.format("%1$.8f, ", var0.m20);
      var1 = var1 + String.format("%1$.8f, ", var0.m21);
      var1 = var1 + String.format("%1$.8f, ", var0.m22);
      var1 = var1 + String.format("%1$.8f\n ", var0.m23);
      var1 = var1 + String.format("%1$.8f, ", var0.m30);
      var1 = var1 + String.format("%1$.8f, ", var0.m31);
      var1 = var1 + String.format("%1$.8f, ", var0.m32);
      var1 = var1 + String.format("%1$.8f\n ", var0.m33);
      return var1;
   }

   static AiBone FindAiBone(String var0, List var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         AiBone var4 = (AiBone)var1.get(var3);
         String var5 = var4.getName();
         if (var5.equals(var0)) {
            return var4;
         }
      }

      return null;
   }

   private static void DumpMesh(VertexPositionNormalTangentTextureSkin[] var0) {
      StringBuilder var1 = new StringBuilder();
      VertexPositionNormalTangentTextureSkin[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         VertexPositionNormalTangentTextureSkin var5 = var2[var4];
         var1.append(var5.Position.x()).append('\t').append(var5.Position.y()).append('\t').append(var5.Position.z()).append('\t').append('\n');
      }

      String var6 = var1.toString();
      var2 = null;
   }

   static Vector3f GetKeyFramePosition(AiNodeAnim var0, float var1) {
      int var2 = -1;

      float var4;
      for(int var3 = 0; var3 < var0.getNumPosKeys(); ++var3) {
         var4 = (float)var0.getPosKeyTime(var3);
         if (var4 > var1) {
            break;
         }

         var2 = var3;
         if (var4 == var1) {
            return new Vector3f(var0.getPosKeyX(var3), var0.getPosKeyY(var3), var0.getPosKeyZ(var3));
         }
      }

      if (var2 < 0) {
         return new Vector3f();
      } else if (var0.getNumPosKeys() > var2 + 1) {
         float var16 = (float)var0.getPosKeyTime(var2);
         var4 = (float)var0.getPosKeyTime(var2 + 1);
         float var5 = var4 - var16;
         float var6 = var1 - var16;
         var6 /= var5;
         float var7 = var0.getPosKeyX(var2);
         float var8 = var0.getPosKeyX(var2 + 1);
         float var9 = var7 + var6 * (var8 - var7);
         float var10 = var0.getPosKeyY(var2);
         float var11 = var0.getPosKeyY(var2 + 1);
         float var12 = var10 + var6 * (var11 - var10);
         float var13 = var0.getPosKeyZ(var2);
         float var14 = var0.getPosKeyZ(var2 + 1);
         float var15 = var13 + var6 * (var14 - var13);
         return new Vector3f(var9, var12, var15);
      } else {
         return new Vector3f(var0.getPosKeyX(var2), var0.getPosKeyY(var2), var0.getPosKeyZ(var2));
      }
   }

   static Quaternion GetKeyFrameRotation(AiNodeAnim var0, float var1) {
      boolean var2 = false;
      Quaternion var3 = new Quaternion();
      int var4 = -1;

      float var6;
      for(int var5 = 0; var5 < var0.getNumRotKeys(); ++var5) {
         var6 = (float)var0.getRotKeyTime(var5);
         if (var6 > var1) {
            break;
         }

         var4 = var5;
         if (var6 == var1) {
            var3.set(var0.getRotKeyX(var5), var0.getRotKeyY(var5), var0.getRotKeyZ(var5), var0.getRotKeyW(var5));
            var2 = true;
            break;
         }
      }

      if (!var2 && var4 < 0) {
         return new Quaternion();
      } else {
         if (!var2 && var0.getNumRotKeys() > var4 + 1) {
            float var21 = (float)var0.getRotKeyTime(var4);
            var6 = (float)var0.getRotKeyTime(var4 + 1);
            float var7 = var6 - var21;
            float var8 = var1 - var21;
            var8 /= var7;
            float var9 = var0.getRotKeyX(var4);
            float var10 = var0.getRotKeyX(var4 + 1);
            float var11 = var9 + var8 * (var10 - var9);
            float var12 = var0.getRotKeyY(var4);
            float var13 = var0.getRotKeyY(var4 + 1);
            float var14 = var12 + var8 * (var13 - var12);
            float var15 = var0.getRotKeyZ(var4);
            float var16 = var0.getRotKeyZ(var4 + 1);
            float var17 = var15 + var8 * (var16 - var15);
            float var18 = var0.getRotKeyW(var4);
            float var19 = var0.getRotKeyW(var4 + 1);
            float var20 = var18 + var8 * (var19 - var18);
            var3.set(var11, var14, var17, var20);
            var2 = true;
         }

         if (!var2 && var0.getNumRotKeys() > var4) {
            var3.set(var0.getRotKeyX(var4), var0.getRotKeyY(var4), var0.getRotKeyZ(var4), var0.getRotKeyW(var4));
            var2 = true;
         }

         return var3;
      }
   }

   static Vector3f GetKeyFrameScale(AiNodeAnim var0, float var1) {
      int var2 = -1;

      float var4;
      for(int var3 = 0; var3 < var0.getNumScaleKeys(); ++var3) {
         var4 = (float)var0.getScaleKeyTime(var3);
         if (var4 > var1) {
            break;
         }

         var2 = var3;
         if (var4 == var1) {
            return new Vector3f(var0.getScaleKeyX(var3), var0.getScaleKeyY(var3), var0.getScaleKeyZ(var3));
         }
      }

      if (var2 < 0) {
         return new Vector3f(1.0F, 1.0F, 1.0F);
      } else if (var0.getNumScaleKeys() > var2 + 1) {
         float var16 = (float)var0.getScaleKeyTime(var2);
         var4 = (float)var0.getScaleKeyTime(var2 + 1);
         float var5 = var4 - var16;
         float var6 = var1 - var16;
         var6 /= var5;
         float var7 = var0.getScaleKeyX(var2);
         float var8 = var0.getScaleKeyX(var2 + 1);
         float var9 = var7 + var6 * (var8 - var7);
         float var10 = var0.getScaleKeyY(var2);
         float var11 = var0.getScaleKeyY(var2 + 1);
         float var12 = var10 + var6 * (var11 - var10);
         float var13 = var0.getScaleKeyZ(var2);
         float var14 = var0.getScaleKeyZ(var2 + 1);
         float var15 = var13 + var6 * (var14 - var13);
         return new Vector3f(var9, var12, var15);
      } else {
         return new Vector3f(var0.getScaleKeyX(var2), var0.getScaleKeyY(var2), var0.getScaleKeyZ(var2));
      }
   }

   static void replaceHashMapKeys(HashMap var0, String var1) {
      tempHashMap.clear();
      tempHashMap.putAll(var0);
      var0.clear();
      Iterator var2 = tempHashMap.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         String var4 = getSharedString((String)var3.getKey(), var1);
         var0.put(var4, (Integer)var3.getValue());
      }

      tempHashMap.clear();
   }

   public static String getSharedString(String var0, String var1) {
      String var2 = sharedStrings.get(var0);
      if (Core.bDebug && var0 != var2) {
         sharedStringCounts.adjustOrPutValue(var1, 1, 0);
      }

      return var2;
   }

   private static void takeOutTheTrash(VertexPositionNormalTangentTexture[] var0) {
      PZArrayUtil.forEach((Object[])var0, JAssImpImporter::takeOutTheTrash);
      Arrays.fill(var0, (Object)null);
   }

   private static void takeOutTheTrash(VertexPositionNormalTangentTextureSkin[] var0) {
      PZArrayUtil.forEach((Object[])var0, JAssImpImporter::takeOutTheTrash);
      Arrays.fill(var0, (Object)null);
   }

   private static void takeOutTheTrash(VertexPositionNormalTangentTexture var0) {
      var0.Normal = null;
      var0.Position = null;
      var0.TextureCoordinates = null;
      var0.Tangent = null;
   }

   private static void takeOutTheTrash(VertexPositionNormalTangentTextureSkin var0) {
      var0.Normal = null;
      var0.Position = null;
      var0.TextureCoordinates = null;
      var0.Tangent = null;
      var0.BlendWeights = null;
      var0.BlendIndices = null;
   }

   public static void takeOutTheTrash(AiScene var0) {
      Iterator var1 = var0.getAnimations().iterator();

      while(var1.hasNext()) {
         AiAnimation var2 = (AiAnimation)var1.next();
         var2.getChannels().clear();
      }

      var0.getAnimations().clear();
      var0.getCameras().clear();
      var0.getLights().clear();
      var1 = var0.getMaterials().iterator();

      while(var1.hasNext()) {
         AiMaterial var6 = (AiMaterial)var1.next();
         var6.getProperties().clear();
      }

      var0.getMaterials().clear();
      var1 = var0.getMeshes().iterator();

      while(var1.hasNext()) {
         AiMesh var7 = (AiMesh)var1.next();
         Iterator var3 = var7.getBones().iterator();

         while(var3.hasNext()) {
            AiBone var4 = (AiBone)var3.next();
            var4.getBoneWeights().clear();
         }

         var7.getBones().clear();
      }

      var0.getMeshes().clear();
      AiNode var5 = (AiNode)var0.getSceneRoot(new AiBuiltInWrapperProvider());
      takeOutTheTrash(var5);
   }

   private static void takeOutTheTrash(AiNode var0) {
      Iterator var1 = var0.getChildren().iterator();

      while(var1.hasNext()) {
         AiNode var2 = (AiNode)var1.next();
         takeOutTheTrash(var2);
      }

      var0.getChildren().clear();
   }

   private static class LibraryLoader extends JassimpLibraryLoader {
      public void loadLibrary() {
         if (System.getProperty("os.name").contains("OS X")) {
            System.loadLibrary("jassimp");
         } else if (System.getProperty("os.name").startsWith("Win")) {
            if (System.getProperty("sun.arch.data.model").equals("64")) {
               System.loadLibrary("jassimp64");
            } else {
               System.loadLibrary("jassimp32");
            }
         } else if (System.getProperty("sun.arch.data.model").equals("64")) {
            System.loadLibrary("jassimp64");
         } else {
            System.loadLibrary("jassimp32");
         }

      }
   }

   public static enum LoadMode {
      Normal,
      StaticMesh,
      AnimationOnly;

      // $FF: synthetic method
      private static JAssImpImporter.LoadMode[] $values() {
         return new JAssImpImporter.LoadMode[]{Normal, StaticMesh, AnimationOnly};
      }
   }
}
