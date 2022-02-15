package org.luaj.kahluafork.compiler;

public class ExpDesc {
   int k;
   int info;
   int aux;
   private double _nval;
   private boolean has_nval;
   int t;
   int f;

   public void setNval(double var1) {
      this._nval = var1;
      this.has_nval = true;
   }

   public double nval() {
      return this.has_nval ? this._nval : (double)this.info;
   }

   void init(int var1, int var2) {
      this.f = -1;
      this.t = -1;
      this.k = var1;
      this.info = var2;
   }

   boolean hasjumps() {
      return this.t != this.f;
   }

   boolean isnumeral() {
      return this.k == 5 && this.t == -1 && this.f == -1;
   }

   public void setvalue(ExpDesc var1) {
      this.k = var1.k;
      this._nval = var1._nval;
      this.has_nval = var1.has_nval;
      this.info = var1.info;
      this.aux = var1.aux;
      this.t = var1.t;
      this.f = var1.f;
   }
}
