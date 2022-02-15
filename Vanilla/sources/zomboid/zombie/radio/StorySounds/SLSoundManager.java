package zombie.radio.StorySounds;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import zombie.ZomboidFileSystem;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.input.GameKeyboard;
import zombie.iso.Vector2;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

public final class SLSoundManager {
   public static boolean ENABLED = false;
   public static boolean DEBUG = false;
   public static boolean LUA_DEBUG = false;
   public static StoryEmitter Emitter = new StoryEmitter();
   private static SLSoundManager instance;
   private HashMap state = new HashMap();
   private ArrayList storySounds = new ArrayList();
   private int nextTick = 0;
   private float borderCenterX = 10500.0F;
   private float borderCenterY = 9000.0F;
   private float borderRadiusMin = 12000.0F;
   private float borderRadiusMax = 16000.0F;
   private float borderScale = 1.0F;

   public static SLSoundManager getInstance() {
      if (instance == null) {
         instance = new SLSoundManager();
      }

      return instance;
   }

   private SLSoundManager() {
      this.state.put(12, false);
      this.state.put(13, false);
   }

   public boolean getDebug() {
      return DEBUG;
   }

   public boolean getLuaDebug() {
      return LUA_DEBUG;
   }

   public ArrayList getStorySounds() {
      return this.storySounds;
   }

   public void print(String var1) {
      if (DEBUG) {
         System.out.println(var1);
      }

   }

   public void init() {
      this.loadSounds();
   }

   public void loadSounds() {
      this.storySounds.clear();

      try {
         File var1 = ZomboidFileSystem.instance.getMediaFile("sound" + File.separator);
         if (var1.exists() && var1.isDirectory()) {
            File[] var2 = var1.listFiles();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (var2[var3].isFile()) {
                  String var4 = var2[var3].getName();
                  if (var4.lastIndexOf(".") != -1 && var4.lastIndexOf(".") != 0 && var4.substring(var4.lastIndexOf(".") + 1).equals("ogg")) {
                     String var5 = var4.substring(0, var4.lastIndexOf("."));
                     this.print("Adding sound: " + var5);
                     this.addStorySound(new StorySound(var5, 1.0F));
                  }
               }
            }
         }
      } catch (Exception var6) {
         System.out.print(var6.getMessage());
      }

   }

   private void addStorySound(StorySound var1) {
      this.storySounds.add(var1);
   }

   public void updateKeys() {
      boolean var1;
      Entry var3;
      for(Iterator var2 = this.state.entrySet().iterator(); var2.hasNext(); var3.setValue(var1)) {
         var3 = (Entry)var2.next();
         var1 = GameKeyboard.isKeyDown((Integer)var3.getKey());
         if (var1 && (Boolean)var3.getValue() != var1) {
            switch((Integer)var3.getKey()) {
            case 12:
            case 26:
            case 53:
            default:
               break;
            case 13:
               Emitter.coordinate3D = !Emitter.coordinate3D;
            }
         }
      }

   }

   public void update(int var1, int var2, int var3) {
      this.updateKeys();
      Emitter.tick();
   }

   public void thunderTest() {
      --this.nextTick;
      if (this.nextTick <= 0) {
         this.nextTick = Rand.Next(10, 180);
         float var1 = Rand.Next(0.0F, 8000.0F);
         double var2 = Math.random() * 3.141592653589793D * 2.0D;
         float var4 = this.borderCenterX + (float)(Math.cos(var2) * (double)var1);
         float var5 = this.borderCenterY + (float)(Math.sin(var2) * (double)var1);
         if (Rand.Next(0, 100) < 60) {
            Emitter.playSound("thunder", 1.0F, var4, var5, 0.0F, 100.0F, 8500.0F);
         } else {
            Emitter.playSound("thundereffect", 1.0F, var4, var5, 0.0F, 100.0F, 8500.0F);
         }
      }

   }

   public void render() {
      this.renderDebug();
   }

   public void renderDebug() {
      if (DEBUG) {
         String var1 = Emitter.coordinate3D ? "3D coordinates, X-Z-Y" : "2D coordinates X-Y-Z";
         int var2 = TextManager.instance.MeasureStringX(UIFont.Large, var1) / 2;
         int var3 = TextManager.instance.MeasureStringY(UIFont.Large, var1);
         int var4 = Core.getInstance().getScreenWidth() / 2;
         int var5 = Core.getInstance().getScreenHeight() / 2;
         this.renderLine(UIFont.Large, var1, var4 - var2, var5);
      }

   }

   private void renderLine(UIFont var1, String var2, int var3, int var4) {
      TextManager.instance.DrawString(var1, (double)(var3 + 1), (double)(var4 + 1), var2, 0.0D, 0.0D, 0.0D, 1.0D);
      TextManager.instance.DrawString(var1, (double)(var3 - 1), (double)(var4 - 1), var2, 0.0D, 0.0D, 0.0D, 1.0D);
      TextManager.instance.DrawString(var1, (double)(var3 + 1), (double)(var4 - 1), var2, 0.0D, 0.0D, 0.0D, 1.0D);
      TextManager.instance.DrawString(var1, (double)(var3 - 1), (double)(var4 + 1), var2, 0.0D, 0.0D, 0.0D, 1.0D);
      TextManager.instance.DrawString(var1, (double)var3, (double)var4, var2, 1.0D, 1.0D, 1.0D, 1.0D);
   }

   public Vector2 getRandomBorderPosition() {
      float var1 = Rand.Next(this.borderRadiusMin * this.borderScale, this.borderRadiusMax * this.borderScale);
      double var2 = Math.random() * 3.141592653589793D * 2.0D;
      float var4 = this.borderCenterX + (float)(Math.cos(var2) * (double)var1);
      float var5 = this.borderCenterY + (float)(Math.sin(var2) * (double)var1);
      return new Vector2(var4, var5);
   }

   public float getRandomBorderRange() {
      return Rand.Next(this.borderRadiusMin * this.borderScale * 1.5F, this.borderRadiusMax * this.borderScale * 1.5F);
   }
}
