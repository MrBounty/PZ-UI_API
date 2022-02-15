package zombie.ui;

import zombie.Lua.LuaManager;
import zombie.core.textures.Texture;

public final class GenericButton extends UIElement {
   public boolean clicked = false;
   public UIElement MessageTarget;
   public boolean mouseOver = false;
   public String name;
   public String text;
   Texture UpTexture = null;
   Texture DownTexture = null;
   private UIEventHandler MessageTarget2 = null;

   public GenericButton(UIElement var1, float var2, float var3, float var4, float var5, String var6, String var7, Texture var8, Texture var9) {
      this.x = (double)var2;
      this.y = (double)var3;
      this.MessageTarget = var1;
      this.name = var6;
      this.text = var7;
      this.width = var4;
      this.height = var5;
      this.UpTexture = var8;
      this.DownTexture = var9;
   }

   public GenericButton(UIEventHandler var1, float var2, float var3, float var4, float var5, String var6, String var7, Texture var8, Texture var9) {
      this.x = (double)var2;
      this.y = (double)var3;
      this.MessageTarget2 = var1;
      this.name = var6;
      this.text = var7;
      this.width = var4;
      this.height = var5;
      this.UpTexture = var8;
      this.DownTexture = var9;
   }

   public Boolean onMouseDown(double var1, double var3) {
      if (!this.isVisible()) {
         return Boolean.FALSE;
      } else {
         if (this.getTable() != null && this.getTable().rawget("onMouseDown") != null) {
            Object[] var5 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onMouseDown"), this.table, var1, var3);
         }

         this.clicked = true;
         return Boolean.TRUE;
      }
   }

   public Boolean onMouseMove(double var1, double var3) {
      if (this.getTable() != null && this.getTable().rawget("onMouseMove") != null) {
         Object[] var5 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onMouseMove"), this.table, var1, var3);
      }

      this.mouseOver = true;
      return Boolean.TRUE;
   }

   public void onMouseMoveOutside(double var1, double var3) {
      if (this.getTable() != null && this.getTable().rawget("onMouseMoveOutside") != null) {
         Object[] var5 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onMouseMoveOutside"), this.table, var1, var3);
      }

      this.clicked = false;
      this.mouseOver = false;
   }

   public Boolean onMouseUp(double var1, double var3) {
      if (this.getTable() != null && this.getTable().rawget("onMouseUp") != null) {
         Object[] var5 = LuaManager.caller.pcall(LuaManager.thread, this.getTable().rawget("onMouseUp"), this.table, var1, var3);
      }

      if (this.clicked) {
         if (this.MessageTarget2 != null) {
            this.MessageTarget2.Selected(this.name, 0, 0);
         } else {
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
            this.DrawTextureScaled(this.DownTexture, 0.0D, 0.0D, this.getWidth(), this.getHeight(), 1.0D);
            this.DrawTextCentre(this.text, this.getWidth() / 2.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D);
         } else {
            this.DrawTextureScaled(this.UpTexture, 0.0D, 0.0D, this.getWidth(), this.getHeight(), 1.0D);
            this.DrawTextCentre(this.text, this.getWidth() / 2.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D);
         }

         super.render();
      }
   }
}
