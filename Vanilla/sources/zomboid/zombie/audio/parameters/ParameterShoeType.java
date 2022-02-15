package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.characters.IsoGameCharacter;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.scripting.objects.Item;

public final class ParameterShoeType extends FMODLocalParameter {
   private static final ItemVisuals tempItemVisuals = new ItemVisuals();
   private final IsoGameCharacter character;
   private ParameterShoeType.ShoeType shoeType = null;

   public ParameterShoeType(IsoGameCharacter var1) {
      super("ShoeType");
      this.character = var1;
   }

   public float calculateCurrentValue() {
      if (this.shoeType == null) {
         this.shoeType = this.getShoeType();
      }

      return (float)this.shoeType.label;
   }

   private ParameterShoeType.ShoeType getShoeType() {
      this.character.getItemVisuals(tempItemVisuals);
      Item var1 = null;

      for(int var2 = 0; var2 < tempItemVisuals.size(); ++var2) {
         ItemVisual var3 = (ItemVisual)tempItemVisuals.get(var2);
         Item var4 = var3.getScriptItem();
         if (var4 != null && "Shoes".equals(var4.getBodyLocation())) {
            var1 = var4;
            break;
         }
      }

      if (var1 == null) {
         return ParameterShoeType.ShoeType.Barefoot;
      } else {
         String var5 = var1.getName();
         if (!var5.contains("Boots") && !var5.contains("Wellies")) {
            if (var5.contains("FlipFlop")) {
               return ParameterShoeType.ShoeType.FlipFlops;
            } else if (var5.contains("Slippers")) {
               return ParameterShoeType.ShoeType.Slippers;
            } else {
               return var5.contains("Trainer") ? ParameterShoeType.ShoeType.Sneakers : ParameterShoeType.ShoeType.Shoes;
            }
         } else {
            return ParameterShoeType.ShoeType.Boots;
         }
      }
   }

   public void setShoeType(ParameterShoeType.ShoeType var1) {
      this.shoeType = var1;
   }

   private static enum ShoeType {
      Barefoot(0),
      Boots(1),
      FlipFlops(2),
      Shoes(3),
      Slippers(4),
      Sneakers(5);

      final int label;

      private ShoeType(int var3) {
         this.label = var3;
      }

      // $FF: synthetic method
      private static ParameterShoeType.ShoeType[] $values() {
         return new ParameterShoeType.ShoeType[]{Barefoot, Boots, FlipFlops, Shoes, Slippers, Sneakers};
      }
   }
}
