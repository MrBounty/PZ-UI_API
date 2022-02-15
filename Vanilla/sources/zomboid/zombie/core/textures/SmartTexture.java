package zombie.core.textures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import org.lwjgl.opengl.GL11;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characterTextures.CharacterSmartTexture;
import zombie.core.Core;
import zombie.core.ImmutableColor;
import zombie.core.logger.ExceptionLogger;
import zombie.core.opengl.SmartShader;
import zombie.core.skinnedmodel.model.CharacterMask;
import zombie.core.utils.WrappedBuffer;
import zombie.debug.DebugLog;
import zombie.util.Lambda;
import zombie.util.list.PZArrayUtil;

public class SmartTexture extends Texture {
   public final ArrayList commands = new ArrayList();
   public Texture result;
   private boolean dirty = true;
   private static SmartShader hue;
   private static SmartShader tint;
   private static SmartShader masked;
   private static SmartShader dirtMask;
   private final HashMap categoryMap = new HashMap();
   private static SmartShader bodyMask;
   private static SmartShader bodyMaskTint;
   private static SmartShader bodyMaskHue;
   private static final ArrayList bodyMaskParams = new ArrayList();
   private static SmartShader addHole;
   private static final ArrayList addHoleParams = new ArrayList();
   private static SmartShader removeHole;
   private static final ArrayList removeHoleParams = new ArrayList();
   private static SmartShader blit;

   public SmartTexture() {
      this.name = "SmartTexture";
   }

   void addToCat(int var1) {
      ArrayList var2 = null;
      if (!this.categoryMap.containsKey(var1)) {
         var2 = new ArrayList();
         this.categoryMap.put(var1, var2);
      } else {
         var2 = (ArrayList)this.categoryMap.get(var1);
      }

      var2.add(this.commands.size());
   }

   public TextureCombinerCommand getFirstFromCategory(int var1) {
      return !this.categoryMap.containsKey(var1) ? null : (TextureCombinerCommand)this.commands.get((Integer)((ArrayList)this.categoryMap.get(var1)).get(0));
   }

   public void addOverlayPatches(String var1, String var2, int var3) {
      if (blit == null) {
         this.create();
      }

      this.addToCat(var3);
      ArrayList var4 = new ArrayList();
      this.add((String)var1, blit, (String)var2, var4, 770, 771);
   }

   public void addOverlay(String var1, String var2, float var3, int var4) {
      if (masked == null) {
         this.create();
      }

      this.addToCat(var4);
      ArrayList var5 = new ArrayList();
      var5.add(new TextureCombinerShaderParam("intensity", var3));
      var5.add(new TextureCombinerShaderParam("bloodDark", 0.5F, 0.5F));
      this.add((String)var1, masked, (String)var2, var5, 774, 771);
   }

   public void addDirtOverlay(String var1, String var2, float var3, int var4) {
      if (dirtMask == null) {
         this.create();
      }

      this.addToCat(var4);
      ArrayList var5 = new ArrayList();
      var5.add(new TextureCombinerShaderParam("intensity", var3));
      this.add((String)var1, dirtMask, (String)var2, var5, 774, 771);
   }

   public void addOverlay(String var1) {
      if (tint == null) {
         this.create();
      }

      this.add((String)var1, 774, 771);
   }

   public void addRect(String var1, int var2, int var3, int var4, int var5) {
      this.commands.add(TextureCombinerCommand.get().init(Texture.getSharedTexture(var1), var2, var3, var4, var5));
      this.dirty = true;
   }

   public void destroy() {
      if (this.result != null) {
         TextureCombiner.instance.releaseTexture(this.result);
      }

      this.clear();
      this.dirty = false;
   }

   public void addTint(String var1, int var2, float var3, float var4, float var5) {
      this.addTint(Texture.getSharedTexture(var1), var2, var3, var4, var5);
   }

   public void addTint(Texture var1, int var2, float var3, float var4, float var5) {
      if (tint == null) {
         this.create();
      }

      this.addToCat(var2);
      ArrayList var6 = new ArrayList();
      var6.add(new TextureCombinerShaderParam("R", var3));
      var6.add(new TextureCombinerShaderParam("G", var4));
      var6.add(new TextureCombinerShaderParam("B", var5));
      this.add(var1, tint, var6);
   }

   public void addHue(String var1, int var2, float var3) {
      this.addHue(Texture.getSharedTexture(var1), var2, var3);
   }

   public void addHue(Texture var1, int var2, float var3) {
      if (hue == null) {
         this.create();
      }

      this.addToCat(var2);
      ArrayList var4 = new ArrayList();
      var4.add(new TextureCombinerShaderParam("HueChange", var3));
      this.add(var1, hue, var4);
   }

   public Texture addHole(BloodBodyPartType var1) {
      String[] var10000 = CharacterSmartTexture.MaskFiles;
      String var2 = "media/textures/HoleTextures/" + var10000[var1.index()] + ".png";
      if (addHole == null) {
         this.create();
      }

      this.addToCat(CharacterSmartTexture.ClothingItemCategory);
      this.calculate();
      Texture var3 = this.result;
      this.clear();
      this.result = null;
      this.commands.add(TextureCombinerCommand.get().init(var3, addHole, addHoleParams, Texture.getSharedTexture(var2), 770, 0));
      this.dirty = true;
      return var3;
   }

   public void removeHole(String var1, BloodBodyPartType var2) {
      String[] var10000 = CharacterSmartTexture.MaskFiles;
      String var3 = "media/textures/HoleTextures/" + var10000[var2.index()] + ".png";
      this.removeHole(Texture.getSharedTexture(var1), Texture.getSharedTexture(var3), var2);
   }

   public void removeHole(Texture var1, BloodBodyPartType var2) {
      String[] var10000 = CharacterSmartTexture.MaskFiles;
      String var3 = "media/textures/HoleTextures/" + var10000[var2.index()] + ".png";
      this.removeHole(var1, Texture.getSharedTexture(var3), var2);
   }

   public void removeHole(Texture var1, Texture var2, BloodBodyPartType var3) {
      if (removeHole == null) {
         this.create();
      }

      this.addToCat(CharacterSmartTexture.ClothingItemCategory);
      this.commands.add(TextureCombinerCommand.get().init(var1, removeHole, removeHoleParams, var2, 770, 771));
      this.dirty = true;
   }

   public void mask(String var1, String var2, int var3) {
      this.mask(Texture.getSharedTexture(var1), Texture.getSharedTexture(var2), var3);
   }

   public void mask(Texture var1, Texture var2, int var3) {
      if (bodyMask == null) {
         this.create();
      }

      this.addToCat(var3);
      this.commands.add(TextureCombinerCommand.get().init(var1, bodyMask, bodyMaskParams, var2, 770, 771));
      this.dirty = true;
   }

   public void maskHue(String var1, String var2, int var3, float var4) {
      this.maskHue(Texture.getSharedTexture(var1), Texture.getSharedTexture(var2), var3, var4);
   }

   public void maskHue(Texture var1, Texture var2, int var3, float var4) {
      if (bodyMask == null) {
         this.create();
      }

      this.addToCat(var3);
      ArrayList var5 = new ArrayList();
      var5.add(new TextureCombinerShaderParam("HueChange", var4));
      this.commands.add(TextureCombinerCommand.get().init(var1, bodyMaskHue, var5, var2, 770, 771));
      this.dirty = true;
   }

   public void maskTint(String var1, String var2, int var3, float var4, float var5, float var6) {
      this.maskTint(Texture.getSharedTexture(var1), Texture.getSharedTexture(var2), var3, var4, var5, var6);
   }

   public void maskTint(Texture var1, Texture var2, int var3, float var4, float var5, float var6) {
      if (bodyMask == null) {
         this.create();
      }

      this.addToCat(var3);
      ArrayList var7 = new ArrayList();
      var7.add(new TextureCombinerShaderParam("R", var4));
      var7.add(new TextureCombinerShaderParam("G", var5));
      var7.add(new TextureCombinerShaderParam("B", var6));
      this.commands.add(TextureCombinerCommand.get().init(var1, bodyMaskTint, var7, var2, 770, 771));
      this.dirty = true;
   }

   public void addMaskedTexture(CharacterMask var1, String var2, String var3, int var4, ImmutableColor var5, float var6) {
      addMaskedTexture(this, var1, var2, Texture.getSharedTexture(var3), var4, var5, var6);
   }

   public void addMaskedTexture(CharacterMask var1, String var2, Texture var3, int var4, ImmutableColor var5, float var6) {
      addMaskedTexture(this, var1, var2, var3, var4, var5, var6);
   }

   private static void addMaskFlags(SmartTexture var0, CharacterMask var1, String var2, Texture var3, int var4) {
      Consumer var5 = Lambda.consumer(var0, var2, var3, var4, (var0x, var1x, var2x, var3x, var4x) -> {
         var1x.mask(var3x, Texture.getSharedTexture(var2x + "/" + var0x + ".png"), var4x);
      });
      var1.forEachVisible(var5);
   }

   private static void addMaskFlagsHue(SmartTexture var0, CharacterMask var1, String var2, Texture var3, int var4, float var5) {
      Consumer var6 = Lambda.consumer(var0, var2, var3, var4, var5, (var0x, var1x, var2x, var3x, var4x, var5x) -> {
         var1x.maskHue(var3x, Texture.getSharedTexture(var2x + "/" + var0x + ".png"), var4x, var5x);
      });
      var1.forEachVisible(var6);
   }

   private static void addMaskFlagsTint(SmartTexture var0, CharacterMask var1, String var2, Texture var3, int var4, ImmutableColor var5) {
      Consumer var6 = Lambda.consumer(var0, var2, var3, var4, var5, (var0x, var1x, var2x, var3x, var4x, var5x) -> {
         var1x.maskTint(var3x, Texture.getSharedTexture(var2x + "/" + var0x + ".png"), var4x, var5x.r, var5x.g, var5x.b);
      });
      var1.forEachVisible(var6);
   }

   private static void addMaskedTexture(SmartTexture var0, CharacterMask var1, String var2, Texture var3, int var4, ImmutableColor var5, float var6) {
      if (!var1.isNothingVisible()) {
         if (var1.isAllVisible()) {
            if (!ImmutableColor.white.equals(var5)) {
               var0.addTint(var3, var4, var5.r, var5.g, var5.b);
            } else if (!(var6 < -1.0E-4F) && !(var6 > 1.0E-4F)) {
               var0.add(var3);
            } else {
               var0.addHue(var3, var4, var6);
            }

         } else {
            if (!ImmutableColor.white.equals(var5)) {
               addMaskFlagsTint(var0, var1, var2, var3, var4, var5);
            } else if (!(var6 < -1.0E-4F) && !(var6 > 1.0E-4F)) {
               addMaskFlags(var0, var1, var2, var3, var4);
            } else {
               addMaskFlagsHue(var0, var1, var2, var3, var4, var6);
            }

         }
      }
   }

   public void addTexture(String var1, int var2, ImmutableColor var3, float var4) {
      addTexture(this, var1, var2, var3, var4);
   }

   private static void addTexture(SmartTexture var0, String var1, int var2, ImmutableColor var3, float var4) {
      if (!ImmutableColor.white.equals(var3)) {
         var0.addTint(var1, var2, var3.r, var3.g, var3.b);
      } else if (!(var4 < -1.0E-4F) && !(var4 > 1.0E-4F)) {
         var0.add(var1);
      } else {
         var0.addHue(var1, var2, var4);
      }

   }

   private void create() {
      tint = new SmartShader("hueChange");
      hue = new SmartShader("hueChange");
      masked = new SmartShader("overlayMask");
      dirtMask = new SmartShader("dirtMask");
      bodyMask = new SmartShader("bodyMask");
      bodyMaskHue = new SmartShader("bodyMaskHue");
      bodyMaskTint = new SmartShader("bodyMaskTint");
      addHole = new SmartShader("addHole");
      removeHole = new SmartShader("removeHole");
      blit = new SmartShader("blit");
   }

   public WrappedBuffer getData() {
      synchronized(this) {
         if (this.dirty) {
            this.calculate();
         }

         return this.result.dataid.getData();
      }
   }

   public synchronized void bind() {
      if (this.dirty) {
         this.calculate();
      }

      this.result.bind(3553);
   }

   public int getID() {
      synchronized(this) {
         if (this.dirty) {
            this.calculate();
         }
      }

      return this.result.dataid.id;
   }

   public void calculate() {
      synchronized(this) {
         if (Core.bDebug) {
            GL11.glGetError();
         }

         try {
            this.result = TextureCombiner.instance.combine(this.commands);
         } catch (Exception var4) {
            DebugLog.General.error(var4.getClass().getSimpleName() + " encountered while combining texture.");
            DebugLog.General.error("Intended width : " + TextureCombiner.getResultingWidth(this.commands));
            DebugLog.General.error("Intended height: " + TextureCombiner.getResultingHeight(this.commands));
            DebugLog.General.error("");
            DebugLog.General.error("Commands list: " + PZArrayUtil.arrayToString((Iterable)this.commands));
            DebugLog.General.error("");
            DebugLog.General.error("Stack trace: ");
            ExceptionLogger.logException(var4);
            DebugLog.General.error("This SmartTexture will no longer be valid.");
            this.width = -1;
            this.height = -1;
            this.dirty = false;
            return;
         }

         this.width = this.result.width;
         this.height = this.result.height;
         this.dirty = false;
      }
   }

   public void clear() {
      TextureCombinerCommand.pool.release((List)this.commands);
      this.commands.clear();
      this.categoryMap.clear();
      this.dirty = false;
   }

   public void add(String var1) {
      this.add(Texture.getSharedTexture(var1));
   }

   public void add(Texture var1) {
      if (blit == null) {
         this.create();
      }

      this.commands.add(TextureCombinerCommand.get().init(var1, blit));
      this.dirty = true;
   }

   public void add(String var1, SmartShader var2, ArrayList var3) {
      this.add(Texture.getSharedTexture(var1), var2, var3);
   }

   public void add(Texture var1, SmartShader var2, ArrayList var3) {
      this.commands.add(TextureCombinerCommand.get().init(var1, var2, var3));
      this.dirty = true;
   }

   public void add(String var1, SmartShader var2, String var3, int var4, int var5) {
      this.add(Texture.getSharedTexture(var1), var2, Texture.getSharedTexture(var3), var4, var5);
   }

   public void add(Texture var1, SmartShader var2, Texture var3, int var4, int var5) {
      this.commands.add(TextureCombinerCommand.get().init(var1, var2, var3, var4, var5));
      this.dirty = true;
   }

   public void add(String var1, int var2, int var3) {
      this.add(Texture.getSharedTexture(var1), var2, var3);
   }

   public void add(Texture var1, int var2, int var3) {
      this.commands.add(TextureCombinerCommand.get().init(var1, var2, var3));
      this.dirty = true;
   }

   public void add(String var1, SmartShader var2, String var3, ArrayList var4, int var5, int var6) {
      this.add(Texture.getSharedTexture(var1), var2, Texture.getSharedTexture(var3), var4, var5, var6);
   }

   public void add(Texture var1, SmartShader var2, Texture var3, ArrayList var4, int var5, int var6) {
      this.commands.add(TextureCombinerCommand.get().init(var1, var2, var4, var3, var5, var6));
      this.dirty = true;
   }

   public void save(String var1) {
      if (this.dirty) {
         this.calculate();
      }

      this.result.save(var1);
   }

   protected void setDirty() {
      this.dirty = true;
   }

   public boolean isEmpty() {
      return this.result == null ? true : this.result.isEmpty();
   }

   public boolean isFailure() {
      return this.result == null ? false : this.result.isFailure();
   }

   public boolean isReady() {
      return this.result == null ? false : this.result.isReady();
   }
}
