package zombie.core.sprite;

public enum RenderStateSlot {
   Populating(0),
   Ready(1),
   Rendering(2);

   private final int m_index;

   private RenderStateSlot(int var3) {
      this.m_index = var3;
   }

   public int index() {
      return this.m_index;
   }

   public int count() {
      return 3;
   }

   // $FF: synthetic method
   private static RenderStateSlot[] $values() {
      return new RenderStateSlot[]{Populating, Ready, Rendering};
   }
}
