package zombie.ui;

import java.util.Iterator;
import java.util.Stack;

public final class TextBox extends UIElement {
   public boolean ResizeParent;
   UIFont font;
   Stack Lines = new Stack();
   String Text;
   public boolean Centred = false;

   public TextBox(UIFont var1, int var2, int var3, int var4, String var5) {
      this.font = var1;
      this.x = (double)var2;
      this.y = (double)var3;
      this.Text = var5;
      this.width = (float)var4;
      this.Paginate();
   }

   public void onresize() {
      this.Paginate();
   }

   public void render() {
      if (this.isVisible()) {
         super.render();
         this.Paginate();
         int var1 = 0;

         for(Iterator var2 = this.Lines.iterator(); var2.hasNext(); var1 += TextManager.instance.MeasureStringY(this.font, (String)this.Lines.get(0))) {
            String var3 = (String)var2.next();
            if (this.Centred) {
               TextManager.instance.DrawStringCentre(this.font, (double)this.getAbsoluteX().intValue() + this.getWidth() / 2.0D, (double)(this.getAbsoluteY().intValue() + var1), var3, 1.0D, 1.0D, 1.0D, 1.0D);
            } else {
               TextManager.instance.DrawString(this.font, (double)this.getAbsoluteX().intValue(), (double)(this.getAbsoluteY().intValue() + var1), var3, 1.0D, 1.0D, 1.0D, 1.0D);
            }
         }

         this.setHeight((double)var1);
      }
   }

   public void update() {
      this.Paginate();
      int var1 = 0;

      for(Iterator var2 = this.Lines.iterator(); var2.hasNext(); var1 += TextManager.instance.MeasureStringY(this.font, (String)this.Lines.get(0))) {
         String var3 = (String)var2.next();
      }

      this.setHeight((double)var1);
   }

   private void Paginate() {
      int var1 = 0;
      this.Lines.clear();
      String[] var2 = this.Text.split("<br>");
      String[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (var6.length() == 0) {
            this.Lines.add(" ");
         } else {
            do {
               int var7 = var6.indexOf(" ", var1 + 1);
               int var8 = var7;
               if (var7 == -1) {
                  var8 = var6.length();
               }

               int var9 = TextManager.instance.MeasureStringX(this.font, var6.substring(0, var8));
               if ((double)var9 >= this.getWidth()) {
                  String var10 = var6.substring(0, var1);
                  var6 = var6.substring(var1 + 1);
                  this.Lines.add(var10);
                  var7 = 0;
               } else if (var7 == -1) {
                  this.Lines.add(var6);
                  break;
               }

               var1 = var7;
            } while(var6.length() > 0);
         }
      }

   }

   public void SetText(String var1) {
      this.Text = var1;
      this.Paginate();
   }
}
