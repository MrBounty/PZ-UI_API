package zombie.characters;

import java.util.ArrayList;
import zombie.iso.BuildingDef;

public final class SurvivorGroup {
   public final ArrayList Members = new ArrayList();
   public String Order;
   public BuildingDef Safehouse;

   public void addMember(SurvivorDesc var1) {
   }

   public void removeMember(SurvivorDesc var1) {
   }

   public SurvivorDesc getLeader() {
      return null;
   }

   public boolean isLeader(SurvivorDesc var1) {
      return false;
   }
}
