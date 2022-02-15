package zombie.core.sprite;

public final class SpriteRendererStates {
   private SpriteRenderState m_populating = new SpriteRenderState(0);
   private SpriteRenderState m_ready = null;
   private SpriteRenderState m_rendering = new SpriteRenderState(2);
   private SpriteRenderState m_rendered = new SpriteRenderState(1);

   public SpriteRenderState getPopulating() {
      return this.m_populating;
   }

   public GenericSpriteRenderState getPopulatingActiveState() {
      return this.m_populating.getActiveState();
   }

   public void setPopulating(SpriteRenderState var1) {
      this.m_populating = var1;
   }

   public SpriteRenderState getReady() {
      return this.m_ready;
   }

   public void setReady(SpriteRenderState var1) {
      this.m_ready = var1;
   }

   public SpriteRenderState getRendering() {
      return this.m_rendering;
   }

   public GenericSpriteRenderState getRenderingActiveState() {
      return this.m_rendering.getActiveState();
   }

   public void setRendering(SpriteRenderState var1) {
      this.m_rendering = var1;
   }

   public SpriteRenderState getRendered() {
      return this.m_rendered;
   }

   public void setRendered(SpriteRenderState var1) {
      this.m_rendered = var1;
   }

   public void movePopulatingToReady() {
      this.m_ready = this.m_populating;
      this.m_populating = this.m_rendered;
      this.m_rendered = null;
      this.m_ready.time = System.nanoTime();
      this.m_ready.onReady();
   }

   public void moveReadyToRendering() {
      this.m_rendered = this.m_rendering;
      this.m_rendering = this.m_ready;
      this.m_ready = null;
      this.m_rendering.onRenderAcquired();
   }
}
