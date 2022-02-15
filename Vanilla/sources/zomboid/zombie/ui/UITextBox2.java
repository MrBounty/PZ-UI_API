package zombie.ui;

import gnu.trove.list.array.TIntArrayList;
import java.util.Stack;
import org.lwjglx.input.Keyboard;
import zombie.GameTime;
import zombie.Lua.LuaManager;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.fonts.AngelCodeFont;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.input.Mouse;

public class UITextBox2 extends UIElement {
   public static boolean ConsoleHasFocus = false;
   public Stack Lines = new Stack();
   public UINineGrid Frame = null;
   public String Text = "";
   public boolean Centred = false;
   public Color StandardFrameColour = new Color(50, 50, 50, 212);
   public Color TextEntryFrameColour = new Color(50, 50, 127, 212);
   public Color TextEntryCursorColour = new Color(170, 170, 220, 240);
   public Color TextEntryCursorColour2 = new Color(100, 100, 220, 160);
   public Color NuetralColour = new Color(0, 0, 255, 33);
   public Color NuetralColour2 = new Color(127, 0, 255, 33);
   public Color BadColour = new Color(255, 0, 0, 33);
   public Color GoodColour = new Color(0, 255, 33);
   public boolean DoingTextEntry = false;
   public int TextEntryCursorPos = 0;
   public int TextEntryMaxLength = 2000;
   public boolean IsEditable = false;
   public boolean IsSelectable = false;
   public int CursorLine = 0;
   public boolean multipleLine = false;
   public TIntArrayList TextOffsetOfLineStart = new TIntArrayList();
   public int ToSelectionIndex = 0;
   public String internalText = "";
   public String maskChr = "*";
   public boolean bMask = false;
   public boolean ignoreFirst;
   UIFont font;
   int[] HighlightLines = new int[1000];
   boolean HasFrame = false;
   int NumVisibleLines = 0;
   int TopLineIndex = 0;
   int BlinkFramesOn = 12;
   int BlinkFramesOff = 8;
   float BlinkFrame;
   boolean BlinkState;
   private ColorInfo textColor;
   private int EdgeSize;
   private boolean SelectingRange;
   private int maxTextLength;
   private boolean forceUpperCase;
   private int XOffset;
   private int maxLines;
   private boolean onlyNumbers;
   private Texture clearButtonTexture;
   private boolean bClearButton;
   private UITransition clearButtonTransition;
   public boolean bAlwaysPaginate;
   public boolean bTextChanged;
   private int paginateWidth;
   private UIFont paginateFont;

   public UITextBox2(UIFont var1, int var2, int var3, int var4, int var5, String var6, boolean var7) {
      this.BlinkFrame = (float)this.BlinkFramesOn;
      this.BlinkState = true;
      this.textColor = new ColorInfo();
      this.EdgeSize = 5;
      this.SelectingRange = false;
      this.maxTextLength = -1;
      this.forceUpperCase = false;
      this.XOffset = 0;
      this.maxLines = 1;
      this.onlyNumbers = false;
      this.bClearButton = false;
      this.bAlwaysPaginate = true;
      this.bTextChanged = false;
      this.paginateWidth = -1;
      this.paginateFont = null;
      this.font = var1;
      this.x = (double)var2;
      this.y = (double)var3;
      this.SetText(var6);
      this.width = (float)var4;
      this.height = (float)var5;
      this.NumVisibleLines = 10;
      this.TopLineIndex = 0;
      Core.CurrentTextEntryBox = this;

      for(int var8 = 0; var8 < 1000; ++var8) {
         this.HighlightLines[var8] = 0;
      }

      this.HasFrame = var7;
      if (var7) {
         this.Frame = new UINineGrid(0, 0, var4, var5, this.EdgeSize, this.EdgeSize, this.EdgeSize, this.EdgeSize, "media/ui/Box_TopLeft.png", "media/ui/Box_Top.png", "media/ui/Box_TopRight.png", "media/ui/Box_Left.png", "media/ui/Box_Center.png", "media/ui/Box_Right.png", "media/ui/Box_BottomLeft.png", "media/ui/Box_Bottom.png", "media/ui/Box_BottomRight.png");
         this.AddChild(this.Frame);
      }

      this.Paginate();
      this.DoingTextEntry = false;
      this.TextEntryMaxLength = 2000;
      this.TextEntryCursorPos = 0;
      this.ToSelectionIndex = this.TextEntryCursorPos;
      this.IsEditable = false;
      Keyboard.enableRepeatEvents(true);
      this.clearButtonTexture = Texture.getSharedTexture("media/ui/Panel_Icon_Close.png");
   }

   public void ClearHighlights() {
      for(int var1 = 0; var1 < 1000; ++var1) {
         this.HighlightLines[var1] = 0;
      }

   }

   public void setMasked(boolean var1) {
      this.bMask = var1;
   }

   public void onresize() {
      this.Paginate();
   }

   public void render() {
      if (this.isVisible()) {
         if (this.Parent == null || this.Parent.maxDrawHeight == -1 || !((double)this.Parent.maxDrawHeight <= this.y)) {
            int var2;
            if (this.bMask) {
               if (this.internalText.length() != this.Text.length()) {
                  String var1 = "";

                  for(var2 = 0; var2 < this.internalText.length(); ++var2) {
                     var1 = var1 + this.maskChr;
                  }

                  this.Text = var1;
               }
            } else {
               this.Text = this.internalText;
            }

            super.render();
            this.Paginate();
            int var19 = TextManager.instance.getFontFromEnum(this.font).getLineHeight();
            var2 = this.getInset();
            this.keepCursorVisible();
            int var3 = (int)this.width - var2;
            if (this.bClearButton && this.clearButtonTexture != null && !this.Lines.isEmpty()) {
               var3 -= 2 + this.clearButtonTexture.getWidth() + 2;
               float var4 = 0.5F;
               if (!this.SelectingRange && this.isMouseOver() && (double)Mouse.getXA() >= this.getAbsoluteX() + (double)var3) {
                  var4 = 1.0F;
               }

               this.clearButtonTransition.setFadeIn(var4 == 1.0F);
               this.clearButtonTransition.update();
               this.DrawTexture(this.clearButtonTexture, (double)(this.width - (float)var2 - 2.0F - (float)this.clearButtonTexture.getWidth()), (double)(var2 + (var19 - this.clearButtonTexture.getHeight()) / 2), (double)(var4 * this.clearButtonTransition.fraction() + 0.35F * (1.0F - this.clearButtonTransition.fraction())));
            }

            Double var20 = this.clampToParentX((double)(this.getAbsoluteX().intValue() + var2));
            Double var5 = this.clampToParentX((double)(this.getAbsoluteX().intValue() + var3));
            Double var6 = this.clampToParentY((double)(this.getAbsoluteY().intValue() + var2));
            Double var7 = this.clampToParentY((double)(this.getAbsoluteY().intValue() + (int)this.height - var2));
            this.setStencilRect((double)(var20.intValue() - this.getAbsoluteX().intValue()), (double)(var6.intValue() - this.getAbsoluteY().intValue()), (double)(var5.intValue() - var20.intValue()), (double)(var7.intValue() - var6.intValue()));
            int var9;
            if (this.Lines.size() > 0) {
               int var8 = var2;

               for(var9 = this.TopLineIndex; var9 < this.TopLineIndex + this.NumVisibleLines && var9 < this.Lines.size(); ++var9) {
                  if (this.Lines.get(var9) != null) {
                     if (var9 >= 0 && var9 < this.HighlightLines.length) {
                        if (this.HighlightLines[var9] == 1) {
                           this.DrawTextureScaledCol((Texture)null, (double)(var2 - 1), (double)var8, (double)(this.getWidth().intValue() - var2 * 2 + 2), (double)var19, this.NuetralColour);
                        } else if (this.HighlightLines[var9] == 2) {
                           this.DrawTextureScaledCol((Texture)null, (double)(var2 - 1), (double)var8, (double)(this.getWidth().intValue() - var2 * 2 + 2), (double)var19, this.NuetralColour2);
                        } else if (this.HighlightLines[var9] == 3) {
                           this.DrawTextureScaledCol((Texture)null, (double)(var2 - 1), (double)var8, (double)(this.getWidth().intValue() - var2 * 2 + 2), (double)var19, this.BadColour);
                        } else if (this.HighlightLines[var9] == 4) {
                           this.DrawTextureScaledCol((Texture)null, (double)(var2 - 1), (double)var8, (double)(this.getWidth().intValue() - var2 * 2 + 2), (double)var19, this.GoodColour);
                        }
                     }

                     String var10 = (String)this.Lines.get(var9);
                     if (this.Centred) {
                        TextManager.instance.DrawStringCentre(this.font, (double)this.getAbsoluteX().intValue() + this.getWidth() / 2.0D + (double)var2, (double)(this.getAbsoluteY().intValue() + var8), var10, (double)this.textColor.r, (double)this.textColor.g, (double)this.textColor.b, 1.0D);
                     } else {
                        TextManager.instance.DrawString(this.font, (double)(-this.XOffset + this.getAbsoluteX().intValue() + var2), (double)(this.getAbsoluteY().intValue() + var8), var10, (double)this.textColor.r, (double)this.textColor.g, (double)this.textColor.b, 1.0D);
                     }

                     var8 += var19;
                  }
               }
            }

            ConsoleHasFocus = this.DoingTextEntry;
            if (this.TextEntryCursorPos > this.Text.length()) {
               this.TextEntryCursorPos = this.Text.length();
            }

            if (this.ToSelectionIndex > this.Text.length()) {
               this.ToSelectionIndex = this.Text.length();
            }

            this.CursorLine = this.toDisplayLine(this.TextEntryCursorPos);
            if (this.DoingTextEntry) {
               AngelCodeFont var21 = TextManager.instance.getFontFromEnum(this.font);
               int var22;
               if (this.BlinkState) {
                  var9 = 0;
                  if (this.Lines.size() > 0) {
                     var22 = this.TextEntryCursorPos - this.TextOffsetOfLineStart.get(this.CursorLine);
                     var22 = Math.min(var22, ((String)this.Lines.get(this.CursorLine)).length());
                     var9 = var21.getWidth((String)this.Lines.get(this.CursorLine), 0, var22 - 1, true);
                     if (var9 > 0) {
                        --var9;
                     }
                  }

                  this.DrawTextureScaledCol(Texture.getWhite(), (double)(-this.XOffset + var2 + var9), (double)(var2 + this.CursorLine * var19), 1.0D, (double)var19, this.TextEntryCursorColour);
               }

               if (this.Lines.size() > 0 && this.ToSelectionIndex != this.TextEntryCursorPos) {
                  var9 = Math.min(this.TextEntryCursorPos, this.ToSelectionIndex);
                  var22 = Math.max(this.TextEntryCursorPos, this.ToSelectionIndex);
                  int var11 = this.toDisplayLine(var9);
                  int var12 = this.toDisplayLine(var22);

                  for(int var13 = var11; var13 <= var12; ++var13) {
                     int var14 = this.TextOffsetOfLineStart.get(var13);
                     int var15 = var14 + ((String)this.Lines.get(var13)).length();
                     var14 = Math.max(var14, var9);
                     var15 = Math.min(var15, var22);
                     String var16 = (String)this.Lines.get(var13);
                     int var17 = var21.getWidth(var16, 0, var14 - this.TextOffsetOfLineStart.get(var13) - 1, true);
                     int var18 = var21.getWidth(var16, 0, var15 - this.TextOffsetOfLineStart.get(var13) - 1, true);
                     this.DrawTextureScaledCol((Texture)null, (double)(-this.XOffset + var2 + var17), (double)(var2 + var13 * var19), (double)(var18 - var17), (double)var19, this.TextEntryCursorColour2);
                  }
               }
            }

            this.clearStencilRect();
            if (StencilLevel > 0) {
               this.repaintStencilRect((double)(var20.intValue() - this.getAbsoluteX().intValue()), (double)(var6.intValue() - this.getAbsoluteY().intValue()), (double)(var5.intValue() - var20.intValue()), (double)(var7.intValue() - var6.intValue()));
            }

         }
      }
   }

   public float getFrameAlpha() {
      return this.Frame.getAlpha();
   }

   public void setFrameAlpha(float var1) {
      this.Frame.setAlpha(var1);
   }

   public void setTextColor(ColorInfo var1) {
      this.textColor = var1;
   }

   private void keepCursorVisible() {
      if (!this.Lines.isEmpty() && this.DoingTextEntry && !this.multipleLine) {
         if (this.TextEntryCursorPos > this.Text.length()) {
            this.TextEntryCursorPos = this.Text.length();
         }

         String var1 = (String)this.Lines.get(0);
         int var2 = TextManager.instance.MeasureStringX(this.font, var1);
         int var3 = this.getInset();
         int var4 = this.getWidth().intValue() - var3 * 2;
         if (this.bClearButton && this.clearButtonTexture != null) {
            var4 -= 2 + this.clearButtonTexture.getWidth() + 2;
         }

         if (var2 <= var4) {
            this.XOffset = 0;
         } else if (-this.XOffset + var2 < var4) {
            this.XOffset = var2 - var4;
         }

         int var5 = TextManager.instance.MeasureStringX(this.font, var1.substring(0, this.TextEntryCursorPos));
         int var6 = -this.XOffset + var3 + var5 - 1;
         if (var6 < var3) {
            this.XOffset = var5;
         } else if (var6 >= var3 + var4) {
            this.XOffset = 0;
            int var7 = this.getCursorPosFromX(var5 - var4);
            this.XOffset = TextManager.instance.MeasureStringX(this.font, var1.substring(0, var7));
            var6 = -this.XOffset + var3 + var5 - 1;
            if (var6 >= var3 + var4) {
               this.XOffset = TextManager.instance.MeasureStringX(this.font, var1.substring(0, var7 + 1));
            }

            if (-this.XOffset + var2 < var4) {
               this.XOffset = var2 - var4;
            }
         }

      } else {
         this.XOffset = 0;
      }
   }

   public String getText() {
      return this.Text;
   }

   public String getInternalText() {
      return this.internalText;
   }

   public void update() {
      if (this.maxTextLength > -1 && this.internalText.length() > this.maxTextLength) {
         this.internalText = this.internalText.substring(this.maxTextLength);
      }

      if (this.forceUpperCase) {
         this.internalText = this.internalText.toUpperCase();
      }

      int var2;
      if (this.bMask) {
         if (this.internalText.length() != this.Text.length()) {
            String var1 = "";

            for(var2 = 0; var2 < this.internalText.length(); ++var2) {
               var1 = var1 + this.maskChr;
            }

            if (this.DoingTextEntry && this.Text != var1) {
               this.resetBlink();
            }

            this.Text = var1;
         }
      } else {
         if (this.DoingTextEntry && this.Text != this.internalText) {
            this.resetBlink();
         }

         this.Text = this.internalText;
      }

      this.Paginate();
      int var3 = this.getInset();
      var2 = TextManager.instance.getFontFromEnum(this.font).getLineHeight();
      if ((double)(var2 + var3 * 2) > this.getHeight()) {
         this.setHeight((double)(var2 + var3 * 2));
      }

      if (this.Frame != null) {
         this.Frame.setHeight(this.getHeight());
      }

      this.NumVisibleLines = (int)(this.getHeight() - (double)(var3 * 2)) / var2;
      if (this.BlinkFrame > 0.0F) {
         this.BlinkFrame -= GameTime.getInstance().getRealworldSecondsSinceLastUpdate() * 30.0F;
      } else {
         this.BlinkState = !this.BlinkState;
         if (this.BlinkState) {
            this.BlinkFrame = (float)this.BlinkFramesOn;
         } else {
            this.BlinkFrame = (float)this.BlinkFramesOff;
         }
      }

      if (this.NumVisibleLines * var2 + var3 * 2 < this.getHeight().intValue()) {
         if (this.NumVisibleLines < this.Lines.size()) {
            this.setScrollHeight((double)((this.Lines.size() + 1) * var2));
         }

         ++this.NumVisibleLines;
      } else {
         this.setScrollHeight((double)(this.Lines.size() * var2));
      }

      if (UIDebugConsole.instance == null || this != UIDebugConsole.instance.OutputLog) {
         this.TopLineIndex = (int)(-this.getYScroll() + (double)var3) / var2;
      }

      this.setYScroll((double)(-this.TopLineIndex * var2));
   }

   private void Paginate() {
      boolean var1 = this.bAlwaysPaginate;
      if (!this.bAlwaysPaginate) {
         if (this.paginateFont != this.font) {
            this.paginateFont = this.font;
            var1 = true;
         }

         if (this.paginateWidth != this.getWidth().intValue()) {
            this.paginateWidth = this.getWidth().intValue();
            var1 = true;
         }

         if (this.bTextChanged) {
            this.bTextChanged = false;
            var1 = true;
         }

         if (!var1) {
            return;
         }
      }

      this.Lines.clear();
      this.TextOffsetOfLineStart.resetQuick();
      if (!this.Text.isEmpty()) {
         if (!this.multipleLine) {
            this.Lines.add(this.Text);
            this.TextOffsetOfLineStart.add(0);
         } else {
            String[] var2 = this.Text.split("\n", -1);
            int var3 = 0;
            String[] var4 = var2;
            int var5 = var2.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String var7 = var4[var6];
               int var8 = 0;
               if (var7.length() == 0) {
                  this.Lines.add(this.multipleLine ? "" : " ");
                  this.TextOffsetOfLineStart.add(var3);
                  ++var3;
               } else {
                  do {
                     int var9 = var7.indexOf(" ", var8 + 1);
                     int var10 = var9;
                     if (var9 == -1) {
                        var10 = var7.length();
                     }

                     int var11 = TextManager.instance.MeasureStringX(this.font, var7.substring(0, var10));
                     byte var12 = 17;
                     if ((double)var11 >= this.getWidth() - (double)(this.getInset() * 2) - (double)var12 && var8 > 0) {
                        String var13 = var7.substring(0, var8);
                        var7 = var7.substring(var8 + 1);
                        this.Lines.add(var13);
                        this.TextOffsetOfLineStart.add(var3);
                        var3 += var13.length() + 1;
                        var9 = 0;
                     } else if (var9 == -1) {
                        this.Lines.add(var7);
                        this.TextOffsetOfLineStart.add(var3);
                        var3 += var7.length() + 1;
                        break;
                     }

                     var8 = var9;
                  } while(var7.length() > 0);
               }
            }

         }
      }
   }

   public int getInset() {
      int var1 = 2;
      if (this.HasFrame) {
         var1 = this.EdgeSize;
      }

      return var1;
   }

   public void setEditable(boolean var1) {
      this.IsEditable = var1;
   }

   public void setSelectable(boolean var1) {
      this.IsSelectable = var1;
   }

   public Boolean onMouseUp(double var1, double var3) {
      if (!this.isVisible()) {
         return false;
      } else {
         super.onMouseUp(var1, var3);
         this.SelectingRange = false;
         return Boolean.TRUE;
      }
   }

   public void onMouseUpOutside(double var1, double var3) {
      if (this.isVisible()) {
         super.onMouseUpOutside(var1, var3);
         this.SelectingRange = false;
      }
   }

   public Boolean onMouseMove(double var1, double var3) {
      int var5 = Mouse.getXA();
      int var6 = Mouse.getYA();
      if (!this.isVisible()) {
         return Boolean.FALSE;
      } else {
         boolean var7 = this.isConsumeMouseEvents();
         this.setConsumeMouseEvents(false);
         Boolean var8 = super.onMouseMove(var1, var3);
         this.setConsumeMouseEvents(var7);
         if (var8) {
            return Boolean.TRUE;
         } else if ((this.IsEditable || this.IsSelectable) && this.SelectingRange) {
            if (this.multipleLine) {
               int var9 = this.getInset();
               int var10 = TextManager.instance.getFontFromEnum(this.font).getLineHeight();
               this.CursorLine = (var6 - this.getAbsoluteY().intValue() - var9 - this.getYScroll().intValue()) / var10;
               if (this.CursorLine > this.Lines.size() - 1) {
                  this.CursorLine = this.Lines.size() - 1;
               }
            }

            this.TextEntryCursorPos = this.getCursorPosFromX((int)((double)var5 - this.getAbsoluteX()));
            return Boolean.TRUE;
         } else {
            return Boolean.FALSE;
         }
      }
   }

   public void onMouseMoveOutside(double var1, double var3) {
      int var5 = Mouse.getXA();
      int var6 = Mouse.getYA();
      if (!Mouse.isButtonDown(0)) {
         this.SelectingRange = false;
      }

      if (this.isVisible()) {
         super.onMouseMoveOutside(var1, var3);
         if ((this.IsEditable || this.IsSelectable) && this.SelectingRange) {
            if (this.multipleLine) {
               int var7 = this.getInset();
               int var8 = TextManager.instance.getFontFromEnum(this.font).getLineHeight();
               this.CursorLine = (var6 - this.getAbsoluteY().intValue() - var7 - this.getYScroll().intValue()) / var8;
               if (this.CursorLine < 0) {
                  this.CursorLine = 0;
               }

               if (this.CursorLine > this.Lines.size() - 1) {
                  this.CursorLine = this.Lines.size() - 1;
               }
            }

            this.TextEntryCursorPos = this.getCursorPosFromX((int)((double)var5 - this.getAbsoluteX()));
         }

      }
   }

   public void focus() {
      this.DoingTextEntry = true;
      Core.CurrentTextEntryBox = this;
   }

   public void unfocus() {
      this.DoingTextEntry = false;
      if (Core.CurrentTextEntryBox == this) {
         Core.CurrentTextEntryBox = null;
      }

   }

   public void ignoreFirstInput() {
      this.ignoreFirst = true;
   }

   public Boolean onMouseDown(double var1, double var3) {
      if (!this.isVisible()) {
         return Boolean.FALSE;
      } else {
         int var5;
         if (!this.getControls().isEmpty()) {
            for(var5 = 0; var5 < this.getControls().size(); ++var5) {
               UIElement var6 = (UIElement)this.getControls().get(var5);
               if (var6 != this.Frame && var6.isMouseOver()) {
                  return var6.onMouseDown(var1 - (double)var6.getXScrolled(this).intValue(), var3 - (double)var6.getYScrolled(this).intValue()) ? Boolean.TRUE : Boolean.FALSE;
               }
            }
         }

         if (this.bClearButton && this.clearButtonTexture != null && !this.Lines.isEmpty()) {
            var5 = this.getWidth().intValue() - this.getInset();
            var5 -= 2 + this.clearButtonTexture.getWidth() + 2;
            if (var1 >= (double)var5) {
               this.clearInput();
               return Boolean.TRUE;
            }
         }

         if (this.multipleLine) {
            var5 = this.getInset();
            int var7 = TextManager.instance.getFontFromEnum(this.font).getLineHeight();
            this.CursorLine = ((int)var3 - var5 - this.getYScroll().intValue()) / var7;
            if (this.CursorLine > this.Lines.size() - 1) {
               this.CursorLine = this.Lines.size() - 1;
            }
         }

         if (!this.IsEditable && !this.IsSelectable) {
            if (this.Frame != null) {
               this.Frame.Colour = this.StandardFrameColour;
            }

            this.DoingTextEntry = false;
            return Boolean.FALSE;
         } else {
            if (Core.CurrentTextEntryBox != this) {
               if (Core.CurrentTextEntryBox != null) {
                  Core.CurrentTextEntryBox.DoingTextEntry = false;
                  if (Core.CurrentTextEntryBox.Frame != null) {
                     Core.CurrentTextEntryBox.Frame.Colour = this.StandardFrameColour;
                  }
               }

               Core.CurrentTextEntryBox = this;
               Core.CurrentTextEntryBox.SelectingRange = true;
            }

            if (!this.DoingTextEntry) {
               this.DoingTextEntry = true;
               this.TextEntryCursorPos = this.getCursorPosFromX((int)var1);
               this.ToSelectionIndex = this.TextEntryCursorPos;
               if (this.Frame != null) {
                  this.Frame.Colour = this.TextEntryFrameColour;
               }
            } else {
               this.TextEntryCursorPos = this.getCursorPosFromX((int)var1);
               this.ToSelectionIndex = this.TextEntryCursorPos;
            }

            return Boolean.TRUE;
         }
      }
   }

   private int getCursorPosFromX(int var1) {
      if (this.Lines.isEmpty()) {
         return 0;
      } else {
         String var2 = (String)this.Lines.get(this.CursorLine);
         if (var2.length() == 0) {
            return this.TextOffsetOfLineStart.get(this.CursorLine);
         } else if (var1 + this.XOffset < 0) {
            return this.TextOffsetOfLineStart.get(this.CursorLine);
         } else {
            for(int var3 = 0; var3 <= var2.length(); ++var3) {
               String var4 = "";
               if (var3 > 0) {
                  var4 = var2.substring(0, var3);
               }

               int var5 = TextManager.instance.MeasureStringX(this.font, var4);
               if (var5 > var1 + this.XOffset && var3 >= 0) {
                  return this.TextOffsetOfLineStart.get(this.CursorLine) + var3 - 1;
               }
            }

            return this.TextOffsetOfLineStart.get(this.CursorLine) + var2.length();
         }
      }
   }

   public void updateText() {
      if (this.bMask) {
         String var1 = "";

         for(int var2 = 0; var2 < this.internalText.length(); ++var2) {
            var1 = var1 + this.maskChr;
         }

         this.Text = var1;
      } else {
         this.Text = this.internalText;
      }

   }

   public void SetText(String var1) {
      this.internalText = var1;
      int var2;
      if (this.bMask) {
         var1 = "";

         for(var2 = 0; var2 < this.internalText.length(); ++var2) {
            var1 = var1 + this.maskChr;
         }

         this.Text = var1;
      } else {
         this.Text = var1;
      }

      this.TextEntryCursorPos = var1.length();
      this.ToSelectionIndex = this.TextEntryCursorPos;
      this.update();
      this.TextEntryCursorPos = this.ToSelectionIndex = 0;
      if (!this.Lines.isEmpty()) {
         var2 = this.Lines.size() - 1;
         this.TextEntryCursorPos = this.ToSelectionIndex = this.TextOffsetOfLineStart.get(var2) + ((String)this.Lines.get(var2)).length();
      }

   }

   public void clearInput() {
      this.Text = "";
      this.internalText = "";
      this.TextEntryCursorPos = 0;
      this.ToSelectionIndex = 0;
      this.update();
      this.onTextChange();
   }

   public void onPressUp() {
      if (this.getTable() != null && this.getTable().rawget("onPressUp") != null) {
         Object[] var1 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onPressUp"), (Object)this.getTable());
      }

   }

   public void onPressDown() {
      if (this.getTable() != null && this.getTable().rawget("onPressDown") != null) {
         Object[] var1 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onPressDown"), (Object)this.getTable());
      }

   }

   public void onCommandEntered() {
      if (this.getTable() != null && this.getTable().rawget("onCommandEntered") != null) {
         Object[] var1 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onCommandEntered"), (Object)this.getTable());
      }

   }

   public void onTextChange() {
      if (this.getTable() != null && this.getTable().rawget("onTextChange") != null) {
         Object[] var1 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onTextChange"), (Object)this.getTable());
      }

   }

   public void onOtherKey(int var1) {
      if (this.getTable() != null && this.getTable().rawget("onOtherKey") != null) {
         Object[] var2 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onOtherKey"), this.getTable(), var1);
      }

   }

   public int getMaxTextLength() {
      return this.maxTextLength;
   }

   public void setMaxTextLength(int var1) {
      this.maxTextLength = var1;
   }

   public boolean getForceUpperCase() {
      return this.forceUpperCase;
   }

   public void setForceUpperCase(boolean var1) {
      this.forceUpperCase = var1;
   }

   public void setHasFrame(boolean var1) {
      if (this.HasFrame != var1) {
         this.HasFrame = var1;
         if (this.HasFrame) {
            this.Frame = new UINineGrid(0, 0, (int)this.width, (int)this.height, this.EdgeSize, this.EdgeSize, this.EdgeSize, this.EdgeSize, "media/ui/Box_TopLeft.png", "media/ui/Box_Top.png", "media/ui/Box_TopRight.png", "media/ui/Box_Left.png", "media/ui/Box_Center.png", "media/ui/Box_Right.png", "media/ui/Box_BottomLeft.png", "media/ui/Box_Bottom.png", "media/ui/Box_BottomRight.png");
            this.Frame.setAnchorRight(true);
            this.AddChild(this.Frame);
         } else {
            this.RemoveChild(this.Frame);
            this.Frame = null;
         }

      }
   }

   public void setClearButton(boolean var1) {
      this.bClearButton = var1;
      if (this.bClearButton && this.clearButtonTransition == null) {
         this.clearButtonTransition = new UITransition();
      }

   }

   public int toDisplayLine(int var1) {
      for(int var2 = 0; var2 < this.Lines.size(); ++var2) {
         if (var1 >= this.TextOffsetOfLineStart.get(var2) && var1 <= this.TextOffsetOfLineStart.get(var2) + ((String)this.Lines.get(var2)).length()) {
            return var2;
         }
      }

      return 0;
   }

   public void setMultipleLine(boolean var1) {
      this.multipleLine = var1;
   }

   public void setCursorLine(int var1) {
      this.CursorLine = var1;
   }

   public int getMaxLines() {
      return this.maxLines;
   }

   public void setMaxLines(int var1) {
      this.maxLines = var1;
   }

   public boolean isFocused() {
      return this.DoingTextEntry;
   }

   public boolean isOnlyNumbers() {
      return this.onlyNumbers;
   }

   public void setOnlyNumbers(boolean var1) {
      this.onlyNumbers = var1;
   }

   public void resetBlink() {
      this.BlinkState = true;
      this.BlinkFrame = (float)this.BlinkFramesOn;
   }

   public void selectAll() {
      this.TextEntryCursorPos = this.internalText.length();
      this.ToSelectionIndex = 0;
   }
}
