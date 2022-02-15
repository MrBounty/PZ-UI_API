package zombie.input;

public final class MouseState {
   private boolean m_isCreated = false;
   private boolean[] m_buttonDownStates = null;
   private int m_mouseX = -1;
   private int m_mouseY = -1;
   private int m_wheelDelta = 0;
   private boolean m_wasPolled = false;

   public void poll() {
      boolean var1 = !this.m_isCreated;
      this.m_isCreated = this.m_isCreated || org.lwjglx.input.Mouse.isCreated();
      if (this.m_isCreated) {
         if (var1) {
            this.m_buttonDownStates = new boolean[org.lwjglx.input.Mouse.getButtonCount()];
         }

         this.m_mouseX = org.lwjglx.input.Mouse.getX();
         this.m_mouseY = org.lwjglx.input.Mouse.getY();
         this.m_wheelDelta = org.lwjglx.input.Mouse.getDWheel();
         this.m_wasPolled = true;

         for(int var2 = 0; var2 < this.m_buttonDownStates.length; ++var2) {
            this.m_buttonDownStates[var2] = org.lwjglx.input.Mouse.isButtonDown(var2);
         }

      }
   }

   public boolean wasPolled() {
      return this.m_wasPolled;
   }

   public void set(MouseState var1) {
      this.m_isCreated = var1.m_isCreated;
      if (var1.m_buttonDownStates != null) {
         if (this.m_buttonDownStates == null || this.m_buttonDownStates.length != var1.m_buttonDownStates.length) {
            this.m_buttonDownStates = new boolean[var1.m_buttonDownStates.length];
         }

         System.arraycopy(var1.m_buttonDownStates, 0, this.m_buttonDownStates, 0, this.m_buttonDownStates.length);
      } else {
         this.m_buttonDownStates = null;
      }

      this.m_mouseX = var1.m_mouseX;
      this.m_mouseY = var1.m_mouseY;
      this.m_wheelDelta = var1.m_wheelDelta;
      this.m_wasPolled = var1.m_wasPolled;
   }

   public void reset() {
      this.m_wasPolled = false;
   }

   public boolean isCreated() {
      return this.m_isCreated;
   }

   public int getX() {
      return this.m_mouseX;
   }

   public int getY() {
      return this.m_mouseY;
   }

   public int getDWheel() {
      return this.m_wheelDelta;
   }

   public void resetDWheel() {
      this.m_wheelDelta = 0;
   }

   public boolean isButtonDown(int var1) {
      return var1 >= this.m_buttonDownStates.length ? false : this.m_buttonDownStates[var1];
   }

   public int getButtonCount() {
      return this.isCreated() ? this.m_buttonDownStates.length : 0;
   }

   public void setCursorPosition(int var1, int var2) {
      org.lwjglx.input.Mouse.setCursorPosition(var1, var2);
   }
}
