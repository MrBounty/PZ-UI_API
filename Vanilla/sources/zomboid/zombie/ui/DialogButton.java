package zombie.ui;

import zombie.Lua.LuaManager;
import zombie.core.Color;
import zombie.core.textures.Texture;

public final class DialogButton extends UIElement {
   public boolean clicked = false;
   public UIElement MessageTarget;
   public boolean mouseOver = false;
   public String name;
   public String text;
   Texture downLeft;
   Texture downMid;
   Texture downRight;
   float origX;
   Texture upLeft;
   Texture upMid;
   Texture upRight;
   private UIEventHandler MessageTarget2 = null;

   public DialogButton(UIElement var1, float var2, float var3, String var4, String var5) {
      this.x = (double)var2;
      this.y = (double)var3;
      this.origX = var2;
      this.MessageTarget = var1;
      this.upLeft = Texture.getSharedTexture("ButtonL_Up");
      this.upMid = Texture.getSharedTexture("ButtonM_Up");
      this.upRight = Texture.getSharedTexture("ButtonR_Up");
      this.downLeft = Texture.getSharedTexture("ButtonL_Down");
      this.downMid = Texture.getSharedTexture("ButtonM_Down");
      this.downRight = Texture.getSharedTexture("ButtonR_Down");
      this.name = var5;
      this.text = var4;
      this.width = (float)TextManager.instance.MeasureStringX(UIFont.Small, var4);
      this.width += 8.0F;
      if (this.width < 40.0F) {
         this.width = 40.0F;
      }

      this.height = (float)this.downMid.getHeight();
      this.x -= (double)(this.width / 2.0F);
   }

   public DialogButton(UIEventHandler var1, int var2, int var3, String var4, String var5) {
      this.x = (double)var2;
      this.y = (double)var3;
      this.origX = (float)var2;
      this.MessageTarget2 = var1;
      this.upLeft = Texture.getSharedTexture("ButtonL_Up");
      this.upMid = Texture.getSharedTexture("ButtonM_Up");
      this.upRight = Texture.getSharedTexture("ButtonR_Up");
      this.downLeft = Texture.getSharedTexture("ButtonL_Down");
      this.downMid = Texture.getSharedTexture("ButtonM_Down");
      this.downRight = Texture.getSharedTexture("ButtonR_Down");
      this.name = var5;
      this.text = var4;
      this.width = (float)TextManager.instance.MeasureStringX(UIFont.Small, var4);
      this.width += 8.0F;
      if (this.width < 40.0F) {
         this.width = 40.0F;
      }

      this.height = (float)this.downMid.getHeight();
      this.x -= (double)(this.width / 2.0F);
   }

   public Boolean onMouseDown(double var1, double var3) {
      if (!this.isVisible()) {
         return false;
      } else {
         if (this.getTable() != null && this.getTable().rawget("onMouseDown") != null) {
            Object[] var5 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onMouseDown"), this.table, var1, var3);
         }

         this.clicked = true;
         return Boolean.TRUE;
      }
   }

   public Boolean onMouseMove(double var1, double var3) {
      this.mouseOver = true;
      if (this.getTable() != null && this.getTable().rawget("onMouseMove") != null) {
         Object[] var5 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onMouseMove"), this.table, var1, var3);
      }

      return Boolean.TRUE;
   }

   public void onMouseMoveOutside(double var1, double var3) {
      this.clicked = false;
      if (this.getTable() != null && this.getTable().rawget("onMouseMoveOutside") != null) {
         Object[] var5 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onMouseMoveOutside"), this.table, var1, var3);
      }

      this.mouseOver = false;
   }

   public Boolean onMouseUp(double var1, double var3) {
      if (this.getTable() != null && this.getTable().rawget("onMouseUp") != null) {
         Object[] var5 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onMouseUp"), this.table, var1, var3);
      }

      if (this.clicked) {
         if (this.MessageTarget2 != null) {
            this.MessageTarget2.Selected(this.name, 0, 0);
         } else if (this.MessageTarget != null) {
            this.MessageTarget.ButtonClicked(this.name);
         }
      }

      this.clicked = false;
      return Boolean.TRUE;
   }

   public void render() {
      if (this.isVisible()) {
         boolean var1 = false;
         if (this.clicked) {
            this.DrawTexture(this.downLeft, 0.0D, 0.0D, 1.0D);
            this.DrawTextureScaledCol(this.downMid, (double)this.downLeft.getWidth(), 0.0D, (double)((int)(this.getWidth() - (double)(this.downLeft.getWidth() * 2))), (double)this.downLeft.getHeight(), new Color(255, 255, 255, 255));
            this.DrawTexture(this.downRight, (double)((int)(this.getWidth() - (double)this.downRight.getWidth())), 0.0D, 1.0D);
            this.DrawTextCentre(this.text, this.getWidth() / 2.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D);
         } else {
            this.DrawTexture(this.upLeft, 0.0D, 0.0D, 1.0D);
            this.DrawTextureScaledCol(this.upMid, (double)this.downLeft.getWidth(), 0.0D, (double)((int)(this.getWidth() - (double)(this.downLeft.getWidth() * 2))), (double)this.downLeft.getHeight(), new Color(255, 255, 255, 255));
            this.DrawTexture(this.upRight, (double)((int)(this.getWidth() - (double)this.downRight.getWidth())), 0.0D, 1.0D);
            this.DrawTextCentre(this.text, this.getWidth() / 2.0D, 0.0D, 1.0D, 1.0D, 1.0D, 1.0D);
         }

         super.render();
      }
   }
}
