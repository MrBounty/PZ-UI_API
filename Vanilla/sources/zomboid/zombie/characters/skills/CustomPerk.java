package zombie.characters.skills;

public final class CustomPerk {
   public String m_id;
   public String m_parent = "None";
   public String m_translation;
   public boolean m_bPassive = false;
   public final int[] m_xp = new int[10];

   public CustomPerk(String var1) {
      this.m_id = var1;
   }
}
