package zombie.core.opengl;

public abstract class IOpenGLState {
   protected IOpenGLState.Value currentValue = this.defaultValue();
   private boolean dirty = true;

   public void set(IOpenGLState.Value var1) {
      if (this.dirty || !var1.equals(this.currentValue)) {
         this.setCurrentValue(var1);
         this.Set(var1);
      }

   }

   void setCurrentValue(IOpenGLState.Value var1) {
      this.dirty = false;
      this.currentValue.set(var1);
   }

   public void setDirty() {
      this.dirty = true;
   }

   IOpenGLState.Value getCurrentValue() {
      return this.currentValue;
   }

   abstract IOpenGLState.Value defaultValue();

   abstract void Set(IOpenGLState.Value var1);

   public interface Value {
      IOpenGLState.Value set(IOpenGLState.Value var1);
   }
}
