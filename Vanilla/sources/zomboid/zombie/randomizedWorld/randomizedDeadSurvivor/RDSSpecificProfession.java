package zombie.randomizedWorld.randomizedDeadSurvivor;

import java.util.ArrayList;
import java.util.List;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.ItemPickerJava;
import zombie.iso.BuildingDef;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.objects.IsoDeadBody;
import zombie.util.list.PZArrayUtil;

public final class RDSSpecificProfession extends RandomizedDeadSurvivorBase {
   private final ArrayList specificProfessionDistribution = new ArrayList();

   public void randomizeDeadSurvivor(BuildingDef var1) {
      IsoGridSquare var2 = var1.getFreeSquareInRoom();
      if (var2 != null) {
         IsoDeadBody var3 = createRandomDeadBody(var2.getX(), var2.getY(), var2.getZ(), (IsoDirections)null, 0);
         if (var3 != null) {
            ItemPickerJava.ItemPickerRoom var4 = (ItemPickerJava.ItemPickerRoom)ItemPickerJava.rooms.get(PZArrayUtil.pickRandom((List)this.specificProfessionDistribution));
            ItemPickerJava.rollItem((ItemPickerJava.ItemPickerContainer)var4.Containers.get("counter"), var3.getContainer(), true, (IsoGameCharacter)null, (ItemPickerJava.ItemPickerRoom)null);
         }
      }
   }

   public RDSSpecificProfession() {
      this.specificProfessionDistribution.add("Carpenter");
      this.specificProfessionDistribution.add("Electrician");
      this.specificProfessionDistribution.add("Farmer");
      this.specificProfessionDistribution.add("Nurse");
   }
}
