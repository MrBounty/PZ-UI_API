package zombie.ui;

import java.util.Stack;
import org.lwjgl.util.Rectangle;
import zombie.characters.IsoGameCharacter;
import zombie.characters.Moodles.MoodleType;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.textures.Texture;
import zombie.input.GameKeyboard;
import zombie.input.Mouse;

public final class MoodlesUI extends UIElement {
   public float clientH = 0.0F;
   public float clientW = 0.0F;
   public boolean Movable = false;
   public int ncclientH = 0;
   public int ncclientW = 0;
   private static MoodlesUI instance = null;
   private static final float OFFSCREEN_Y = 10000.0F;
   public Stack nestedItems = new Stack();
   float alpha = 1.0F;
   Texture Back_Bad_1 = null;
   Texture Back_Bad_2 = null;
   Texture Back_Bad_3 = null;
   Texture Back_Bad_4 = null;
   Texture Back_Good_1 = null;
   Texture Back_Good_2 = null;
   Texture Back_Good_3 = null;
   Texture Back_Good_4 = null;
   Texture Back_Neutral = null;
   Texture Endurance = null;
   Texture Bleeding = null;
   Texture Angry = null;
   Texture Stress = null;
   Texture Thirst = null;
   Texture Panic = null;
   Texture Hungry = null;
   Texture Injured = null;
   Texture Pain = null;
   Texture Sick = null;
   Texture Bored = null;
   Texture Unhappy = null;
   Texture Tired = null;
   Texture HeavyLoad = null;
   Texture Drunk = null;
   Texture Wet = null;
   Texture HasACold = null;
   Texture Dead = null;
   Texture Zombie = null;
   Texture Windchill = null;
   Texture CantSprint = null;
   Texture FoodEaten = null;
   Texture Hyperthermia = null;
   Texture Hypothermia = null;
   public static Texture plusRed;
   public static Texture plusGreen;
   public static Texture minusRed;
   public static Texture minusGreen;
   public static Texture chevronUp;
   public static Texture chevronUpBorder;
   public static Texture chevronDown;
   public static Texture chevronDownBorder;
   float MoodleDistY = 36.0F;
   boolean MouseOver = false;
   int MouseOverSlot = 0;
   int NumUsedSlots = 0;
   private int DebugKeyDelay = 0;
   private int DistFromRighEdge = 46;
   private int[] GoodBadNeutral;
   private int[] MoodleLevel;
   private float[] MoodleOscilationLevel;
   private float[] MoodleSlotsDesiredPos;
   private float[] MoodleSlotsPos;
   private int[] MoodleTypeInSlot;
   private float Oscilator;
   private float OscilatorDecelerator;
   private float OscilatorRate;
   private float OscilatorScalar;
   private float OscilatorStartLevel;
   private float OscilatorStep;
   private IsoGameCharacter UseCharacter;
   private boolean alphaIncrease;

   public MoodlesUI() {
      this.GoodBadNeutral = new int[MoodleType.ToIndex(MoodleType.MAX)];
      this.MoodleLevel = new int[MoodleType.ToIndex(MoodleType.MAX)];
      this.MoodleOscilationLevel = new float[MoodleType.ToIndex(MoodleType.MAX)];
      this.MoodleSlotsDesiredPos = new float[MoodleType.ToIndex(MoodleType.MAX)];
      this.MoodleSlotsPos = new float[MoodleType.ToIndex(MoodleType.MAX)];
      this.MoodleTypeInSlot = new int[MoodleType.ToIndex(MoodleType.MAX)];
      this.Oscilator = 0.0F;
      this.OscilatorDecelerator = 0.96F;
      this.OscilatorRate = 0.8F;
      this.OscilatorScalar = 15.6F;
      this.OscilatorStartLevel = 1.0F;
      this.OscilatorStep = 0.0F;
      this.UseCharacter = null;
      this.alphaIncrease = true;
      this.x = (double)(Core.getInstance().getScreenWidth() - this.DistFromRighEdge);
      this.y = 100.0D;
      this.width = 32.0F;
      this.height = 500.0F;
      this.Back_Bad_1 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Bad_1.png");
      this.Back_Bad_2 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Bad_2.png");
      this.Back_Bad_3 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Bad_3.png");
      this.Back_Bad_4 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Bad_4.png");
      this.Back_Good_1 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Good_1.png");
      this.Back_Good_2 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Good_2.png");
      this.Back_Good_3 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Good_3.png");
      this.Back_Good_4 = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Good_4.png");
      this.Back_Neutral = Texture.getSharedTexture("media/ui/Moodles/Moodle_Bkg_Bad_1.png");
      this.Endurance = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Endurance.png");
      this.Tired = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Tired.png");
      this.Hungry = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Hungry.png");
      this.Panic = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Panic.png");
      this.Sick = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Sick.png");
      this.Bored = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Bored.png");
      this.Unhappy = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Unhappy.png");
      this.Bleeding = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Bleeding.png");
      this.Wet = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Wet.png");
      this.HasACold = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Cold.png");
      this.Angry = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Angry.png");
      this.Stress = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Stressed.png");
      this.Thirst = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Thirsty.png");
      this.Injured = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Injured.png");
      this.Pain = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Pain.png");
      this.HeavyLoad = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_HeavyLoad.png");
      this.Drunk = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Drunk.png");
      this.Dead = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Dead.png");
      this.Zombie = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Zombie.png");
      this.FoodEaten = Texture.getSharedTexture("media/ui/Moodles/Moodle_Icon_Hungry.png");
      this.Hyperthermia = Texture.getSharedTexture("media/ui/weather/Moodle_Icon_TempHot.png");
      this.Hypothermia = Texture.getSharedTexture("media/ui/weather/Moodle_Icon_TempCold.png");
      this.Windchill = Texture.getSharedTexture("media/ui/Moodle_Icon_Windchill.png");
      this.CantSprint = Texture.getSharedTexture("media/ui/Moodle_Icon_CantSprint.png");
      plusRed = Texture.getSharedTexture("media/ui/Moodle_internal_plus_red.png");
      minusRed = Texture.getSharedTexture("media/ui/Moodle_internal_minus_red.png");
      plusGreen = Texture.getSharedTexture("media/ui/Moodle_internal_plus_green.png");
      minusGreen = Texture.getSharedTexture("media/ui/Moodle_internal_minus_green.png");
      chevronUp = Texture.getSharedTexture("media/ui/Moodle_chevron_up.png");
      chevronUpBorder = Texture.getSharedTexture("media/ui/Moodle_chevron_up_border.png");
      chevronDown = Texture.getSharedTexture("media/ui/Moodle_chevron_down.png");
      chevronDownBorder = Texture.getSharedTexture("media/ui/Moodle_chevron_down_border.png");

      for(int var1 = 0; var1 < MoodleType.ToIndex(MoodleType.MAX); ++var1) {
         this.MoodleSlotsPos[var1] = 10000.0F;
         this.MoodleSlotsDesiredPos[var1] = 10000.0F;
      }

      this.clientW = this.width;
      this.clientH = this.height;
      instance = this;
   }

   public boolean CurrentlyAnimating() {
      boolean var1 = false;

      for(int var2 = 0; var2 < MoodleType.ToIndex(MoodleType.MAX); ++var2) {
         if (this.MoodleSlotsPos[var2] != this.MoodleSlotsDesiredPos[var2]) {
            var1 = true;
         }
      }

      return var1;
   }

   public void Nest(UIElement var1, int var2, int var3, int var4, int var5) {
      this.AddChild(var1);
      this.nestedItems.add(new Rectangle(var5, var2, var3, var4));
   }

   public Boolean onMouseMove(double var1, double var3) {
      if (!this.isVisible()) {
         return Boolean.FALSE;
      } else {
         this.MouseOver = true;
         super.onMouseMove(var1, var3);
         this.MouseOverSlot = (int)(((double)((float)Mouse.getYA()) - this.getY()) / (double)this.MoodleDistY);
         if (this.MouseOverSlot >= this.NumUsedSlots) {
            this.MouseOverSlot = 1000;
         }

         return Boolean.TRUE;
      }
   }

   public void onMouseMoveOutside(double var1, double var3) {
      super.onMouseMoveOutside(var1, var3);
      this.MouseOverSlot = 1000;
      this.MouseOver = false;
   }

   public void render() {
      if (this.UseCharacter != null) {
         float var1 = (float)PerformanceSettings.getLockFPS() / 30.0F;
         this.OscilatorStep += this.OscilatorRate / var1;
         this.Oscilator = (float)Math.sin((double)this.OscilatorStep);
         int var2 = 0;

         for(int var3 = 0; var3 < MoodleType.ToIndex(MoodleType.MAX); ++var3) {
            if (this.MoodleSlotsPos[var3] != 10000.0F) {
               float var4;
               Texture var5;
               Texture var6;
               var4 = this.Oscilator * this.OscilatorScalar * this.MoodleOscilationLevel[var3];
               var5 = this.Back_Neutral;
               var6 = this.Tired;
               label93:
               switch(this.GoodBadNeutral[var3]) {
               case 0:
                  var5 = this.Back_Neutral;
                  break;
               case 1:
                  switch(this.MoodleLevel[var3]) {
                  case 1:
                     var5 = this.Back_Good_1;
                     break label93;
                  case 2:
                     var5 = this.Back_Good_2;
                     break label93;
                  case 3:
                     var5 = this.Back_Good_3;
                     break label93;
                  case 4:
                     var5 = this.Back_Good_4;
                  default:
                     break label93;
                  }
               case 2:
                  switch(this.MoodleLevel[var3]) {
                  case 1:
                     var5 = this.Back_Bad_1;
                     break;
                  case 2:
                     var5 = this.Back_Bad_2;
                     break;
                  case 3:
                     var5 = this.Back_Bad_3;
                     break;
                  case 4:
                     var5 = this.Back_Bad_4;
                  }
               }

               switch(var3) {
               case 0:
                  var6 = this.Endurance;
                  break;
               case 1:
                  var6 = this.Tired;
                  break;
               case 2:
                  var6 = this.Hungry;
                  break;
               case 3:
                  var6 = this.Panic;
                  break;
               case 4:
                  var6 = this.Sick;
                  break;
               case 5:
                  var6 = this.Bored;
                  break;
               case 6:
                  var6 = this.Unhappy;
                  break;
               case 7:
                  var6 = this.Bleeding;
                  break;
               case 8:
                  var6 = this.Wet;
                  break;
               case 9:
                  var6 = this.HasACold;
                  break;
               case 10:
                  var6 = this.Angry;
                  break;
               case 11:
                  var6 = this.Stress;
                  break;
               case 12:
                  var6 = this.Thirst;
                  break;
               case 13:
                  var6 = this.Injured;
                  break;
               case 14:
                  var6 = this.Pain;
                  break;
               case 15:
                  var6 = this.HeavyLoad;
                  break;
               case 16:
                  var6 = this.Drunk;
                  break;
               case 17:
                  var6 = this.Dead;
                  break;
               case 18:
                  var6 = this.Zombie;
                  break;
               case 19:
                  var6 = this.FoodEaten;
                  break;
               case 20:
                  var6 = this.Hyperthermia;
                  break;
               case 21:
                  var6 = this.Hypothermia;
                  break;
               case 22:
                  var6 = this.Windchill;
                  break;
               case 23:
                  var6 = this.CantSprint;
               }

               if (MoodleType.FromIndex(var3).name().equals(Core.getInstance().getBlinkingMoodle())) {
                  if (this.alphaIncrease) {
                     this.alpha += 0.1F * (30.0F / (float)PerformanceSettings.instance.getUIRenderFPS());
                     if (this.alpha > 1.0F) {
                        this.alpha = 1.0F;
                        this.alphaIncrease = false;
                     }
                  } else {
                     this.alpha -= 0.1F * (30.0F / (float)PerformanceSettings.instance.getUIRenderFPS());
                     if (this.alpha < 0.0F) {
                        this.alpha = 0.0F;
                        this.alphaIncrease = true;
                     }
                  }
               }

               if (Core.getInstance().getBlinkingMoodle() == null) {
                  this.alpha = 1.0F;
               }

               this.DrawTexture(var5, (double)((int)var4), (double)((int)this.MoodleSlotsPos[var3]), (double)this.alpha);
               this.DrawTexture(var6, (double)((int)var4), (double)((int)this.MoodleSlotsPos[var3]), (double)this.alpha);
               int var9;
               int var10;
               if (this.UseCharacter.getMoodles().getMoodleChevronCount(var3) > 0) {
                  boolean var7 = this.UseCharacter.getMoodles().getMoodleChevronIsUp(var3);
                  Color var8 = this.UseCharacter.getMoodles().getMoodleChevronColor(var3);
                  var8.a = this.alpha;

                  for(var9 = 0; var9 < this.UseCharacter.getMoodles().getMoodleChevronCount(var3); ++var9) {
                     var10 = var9 * 4;
                     this.DrawTextureCol(var7 ? chevronUp : chevronDown, (double)((int)var4 + 16), (double)((int)this.MoodleSlotsPos[var3] + 20 - var10), var8);
                     this.DrawTextureCol(var7 ? chevronUpBorder : chevronDownBorder, (double)((int)var4 + 16), (double)((int)this.MoodleSlotsPos[var3] + 20 - var10), var8);
                  }
               }

               if (this.MouseOver && var2 == this.MouseOverSlot) {
                  String var15 = this.UseCharacter.getMoodles().getMoodleDisplayString(var3);
                  String var16 = this.UseCharacter.getMoodles().getMoodleDescriptionString(var3);
                  var9 = TextManager.instance.font.getWidth(var15);
                  var10 = TextManager.instance.font.getWidth(var16);
                  int var11 = Math.max(var9, var10);
                  int var12 = TextManager.instance.font.getLineHeight();
                  int var13 = (int)this.MoodleSlotsPos[var3] + 1;
                  int var14 = (2 + var12) * 2;
                  this.DrawTextureScaledColor((Texture)null, -10.0D - (double)var11 - 6.0D, (double)var13 - 2.0D, (double)var11 + 12.0D, (double)var14, 0.0D, 0.0D, 0.0D, 0.6D);
                  this.DrawTextRight(var15, -10.0D, (double)var13, 1.0D, 1.0D, 1.0D, 1.0D);
                  this.DrawTextRight(var16, -10.0D, (double)(var13 + var12), 0.800000011920929D, 0.800000011920929D, 0.800000011920929D, 1.0D);
               }

               ++var2;
            }
         }

         super.render();
      }
   }

   public void wiggle(MoodleType var1) {
      this.MoodleOscilationLevel[MoodleType.ToIndex(var1)] = this.OscilatorStartLevel;
   }

   public void update() {
      super.update();
      if (this.UseCharacter != null) {
         if (!this.CurrentlyAnimating()) {
            if (this.DebugKeyDelay > 0) {
               --this.DebugKeyDelay;
            } else if (GameKeyboard.isKeyDown(57)) {
               this.DebugKeyDelay = 10;
            }
         }

         float var1 = (float)PerformanceSettings.getLockFPS() / 30.0F;

         float[] var10000;
         int var2;
         for(var2 = 0; var2 < MoodleType.ToIndex(MoodleType.MAX); ++var2) {
            var10000 = this.MoodleOscilationLevel;
            var10000[var2] -= this.MoodleOscilationLevel[var2] * (1.0F - this.OscilatorDecelerator) / var1;
            if ((double)this.MoodleOscilationLevel[var2] < 0.01D) {
               this.MoodleOscilationLevel[var2] = 0.0F;
            }
         }

         if (this.UseCharacter.getMoodles().UI_RefreshNeeded()) {
            var2 = 0;

            for(int var3 = 0; var3 < MoodleType.ToIndex(MoodleType.MAX); ++var3) {
               if (this.UseCharacter.getMoodles().getMoodleLevel(var3) > 0) {
                  boolean var4 = false;
                  if (this.MoodleLevel[var3] != this.UseCharacter.getMoodles().getMoodleLevel(var3)) {
                     var4 = true;
                     this.MoodleLevel[var3] = this.UseCharacter.getMoodles().getMoodleLevel(var3);
                     this.MoodleOscilationLevel[var3] = this.OscilatorStartLevel;
                  }

                  this.MoodleSlotsDesiredPos[var3] = this.MoodleDistY * (float)var2;
                  if (var4) {
                     if (this.MoodleSlotsPos[var3] == 10000.0F) {
                        this.MoodleSlotsPos[var3] = this.MoodleSlotsDesiredPos[var3] + 500.0F;
                        this.MoodleOscilationLevel[var3] = 0.0F;
                     }

                     this.GoodBadNeutral[var3] = this.UseCharacter.getMoodles().getGoodBadNeutral(var3);
                  } else {
                     this.MoodleOscilationLevel[var3] = 0.0F;
                  }

                  this.MoodleTypeInSlot[var2] = var3;
                  ++var2;
               } else {
                  this.MoodleSlotsPos[var3] = 10000.0F;
                  this.MoodleSlotsDesiredPos[var3] = 10000.0F;
                  this.MoodleOscilationLevel[var3] = 0.0F;
                  this.MoodleLevel[var3] = 0;
               }
            }

            this.NumUsedSlots = var2;
         }

         for(var2 = 0; var2 < MoodleType.ToIndex(MoodleType.MAX); ++var2) {
            if (Math.abs(this.MoodleSlotsPos[var2] - this.MoodleSlotsDesiredPos[var2]) > 0.8F) {
               var10000 = this.MoodleSlotsPos;
               var10000[var2] += (this.MoodleSlotsDesiredPos[var2] - this.MoodleSlotsPos[var2]) * 0.15F;
            } else {
               this.MoodleSlotsPos[var2] = this.MoodleSlotsDesiredPos[var2];
            }
         }

      }
   }

   public void setCharacter(IsoGameCharacter var1) {
      if (var1 != this.UseCharacter) {
         this.UseCharacter = var1;
         if (this.UseCharacter != null && this.UseCharacter.getMoodles() != null) {
            this.UseCharacter.getMoodles().setMoodlesStateChanged(true);
         }

      }
   }

   public static MoodlesUI getInstance() {
      return instance;
   }
}
