package zombie.scripting.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import org.joml.Vector3f;
import zombie.ZomboidFileSystem;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.advancedanimation.AnimBoneWeight;
import zombie.core.skinnedmodel.model.Model;
import zombie.debug.DebugLog;
import zombie.network.GameServer;
import zombie.scripting.ScriptManager;
import zombie.scripting.ScriptParser;
import zombie.util.StringUtils;

public final class ModelScript extends BaseScriptObject {
   public static final String DEFAULT_SHADER_NAME = "basicEffect";
   public String fileName;
   public String name;
   public String meshName;
   public String textureName;
   public String shaderName;
   public boolean bStatic = true;
   public float scale = 1.0F;
   public final ArrayList m_attachments = new ArrayList();
   public boolean invertX = false;
   public Model loadedModel;
   public final ArrayList boneWeights = new ArrayList();
   private static final HashSet reported = new HashSet();

   public void Load(String var1, String var2) {
      ScriptManager var3 = ScriptManager.instance;
      this.fileName = var3.currentFileName;
      this.name = var1;
      ScriptParser.Block var4 = ScriptParser.parse(var2);
      var4 = (ScriptParser.Block)var4.children.get(0);
      Iterator var5 = var4.children.iterator();

      while(var5.hasNext()) {
         ScriptParser.Block var6 = (ScriptParser.Block)var5.next();
         if ("attachment".equals(var6.type)) {
            this.LoadAttachment(var6);
         }
      }

      var5 = var4.values.iterator();

      while(var5.hasNext()) {
         ScriptParser.Value var12 = (ScriptParser.Value)var5.next();
         String[] var7 = var12.string.split("=");
         String var8 = var7[0].trim();
         String var9 = var7[1].trim();
         if ("mesh".equalsIgnoreCase(var8)) {
            this.meshName = var9;
         } else if ("scale".equalsIgnoreCase(var8)) {
            this.scale = Float.parseFloat(var9);
         } else if ("shader".equalsIgnoreCase(var8)) {
            this.shaderName = var9;
         } else if ("static".equalsIgnoreCase(var8)) {
            this.bStatic = Boolean.parseBoolean(var9);
         } else if ("texture".equalsIgnoreCase(var8)) {
            this.textureName = var9;
         } else if ("invertX".equalsIgnoreCase(var8)) {
            this.invertX = Boolean.parseBoolean(var9);
         } else if ("boneWeight".equalsIgnoreCase(var8)) {
            String[] var10 = var9.split("\\s+");
            if (var10.length == 2) {
               AnimBoneWeight var11 = new AnimBoneWeight(var10[0], PZMath.tryParseFloat(var10[1], 1.0F));
               var11.includeDescendants = false;
               this.boneWeights.add(var11);
            }
         }
      }

   }

   private ModelAttachment LoadAttachment(ScriptParser.Block var1) {
      ModelAttachment var2 = this.getAttachmentById(var1.id);
      if (var2 == null) {
         var2 = new ModelAttachment(var1.id);
         this.m_attachments.add(var2);
      }

      Iterator var3 = var1.values.iterator();

      while(var3.hasNext()) {
         ScriptParser.Value var4 = (ScriptParser.Value)var3.next();
         String var5 = var4.getKey().trim();
         String var6 = var4.getValue().trim();
         if ("bone".equals(var5)) {
            var2.setBone(var6);
         } else if ("offset".equals(var5)) {
            this.LoadVector3f(var6, var2.getOffset());
         } else if ("rotate".equals(var5)) {
            this.LoadVector3f(var6, var2.getRotate());
         }
      }

      return var2;
   }

   private void LoadVector3f(String var1, Vector3f var2) {
      String[] var3 = var1.split(" ");
      var2.set(Float.parseFloat(var3[0]), Float.parseFloat(var3[1]), Float.parseFloat(var3[2]));
   }

   public String getName() {
      return this.name;
   }

   public String getFullType() {
      return this.module.name + "." + this.name;
   }

   public String getMeshName() {
      return this.meshName;
   }

   public String getTextureName() {
      return StringUtils.isNullOrWhitespace(this.textureName) ? this.meshName : this.textureName;
   }

   public String getTextureName(boolean var1) {
      return StringUtils.isNullOrWhitespace(this.textureName) && !var1 ? this.meshName : this.textureName;
   }

   public String getShaderName() {
      return StringUtils.isNullOrWhitespace(this.shaderName) ? "basicEffect" : this.shaderName;
   }

   public String getFileName() {
      return this.fileName;
   }

   public int getAttachmentCount() {
      return this.m_attachments.size();
   }

   public ModelAttachment getAttachment(int var1) {
      return (ModelAttachment)this.m_attachments.get(var1);
   }

   public ModelAttachment getAttachmentById(String var1) {
      for(int var2 = 0; var2 < this.m_attachments.size(); ++var2) {
         ModelAttachment var3 = (ModelAttachment)this.m_attachments.get(var2);
         if (var3.getId().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public ModelAttachment addAttachment(ModelAttachment var1) {
      this.m_attachments.add(var1);
      return var1;
   }

   public ModelAttachment removeAttachment(ModelAttachment var1) {
      this.m_attachments.remove(var1);
      return var1;
   }

   public ModelAttachment addAttachmentAt(int var1, ModelAttachment var2) {
      this.m_attachments.add(var1, var2);
      return var2;
   }

   public ModelAttachment removeAttachment(int var1) {
      return (ModelAttachment)this.m_attachments.remove(var1);
   }

   public void reset() {
      this.invertX = false;
      this.name = null;
      this.meshName = null;
      this.textureName = null;
      this.shaderName = null;
      this.bStatic = true;
      this.scale = 1.0F;
      this.boneWeights.clear();
   }

   private static void checkMesh(String var0, String var1) {
      if (!StringUtils.isNullOrWhitespace(var1)) {
         String var2 = var1.toLowerCase(Locale.ENGLISH);
         if (!ZomboidFileSystem.instance.ActiveFileMap.containsKey("media/models_x/" + var2 + ".fbx") && !ZomboidFileSystem.instance.ActiveFileMap.containsKey("media/models_x/" + var2 + ".x") && !ZomboidFileSystem.instance.ActiveFileMap.containsKey("media/models/" + var2 + ".txt")) {
            reported.add(var1);
            DebugLog.Script.warn("no such mesh \"" + var1 + "\" for " + var0);
         }

      }
   }

   private static void checkTexture(String var0, String var1) {
      if (!GameServer.bServer) {
         if (!StringUtils.isNullOrWhitespace(var1)) {
            String var2 = var1.toLowerCase(Locale.ENGLISH);
            if (!ZomboidFileSystem.instance.ActiveFileMap.containsKey("media/textures/" + var2 + ".png")) {
               reported.add(var1);
               DebugLog.Script.warn("no such texture \"" + var1 + "\" for " + var0);
            }

         }
      }
   }

   private static void check(String var0, String var1) {
      if (!StringUtils.isNullOrWhitespace(var1)) {
         if (!reported.contains(var1)) {
            ModelScript var2 = ScriptManager.instance.getModelScript(var1);
            if (var2 == null) {
               reported.add(var1);
               DebugLog.Script.warn("no such model \"" + var1 + "\" for " + var0);
            } else {
               checkMesh(var2.getFullType(), var2.getMeshName());
               checkTexture(var2.getFullType(), var2.getTextureName());
            }

         }
      }
   }

   public static void ScriptsLoaded() {
      reported.clear();
      ArrayList var0 = ScriptManager.instance.getAllItems();
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         Item var2 = (Item)var1.next();
         check(var2.getFullName(), var2.getStaticModel());
         check(var2.getFullName(), var2.getWeaponSprite());
      }

      ArrayList var4 = ScriptManager.instance.getAllRecipes();
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Recipe var3 = (Recipe)var5.next();
         if (var3.getProp1() != null && !var3.getProp1().startsWith("Source=")) {
            check(var3.getFullType(), var3.getProp1());
         }

         if (var3.getProp2() != null && !var3.getProp2().startsWith("Source=")) {
            check(var3.getFullType(), var3.getProp2());
         }
      }

   }
}
