package zombie.core.skinnedmodel.population;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;

public class ClothingDecalGroup {
   @XmlElement(
      name = "name"
   )
   public String m_Name;
   @XmlElement(
      name = "decal"
   )
   public final ArrayList m_Decals = new ArrayList();
   @XmlElement(
      name = "group"
   )
   public final ArrayList m_Groups = new ArrayList();
   private final ArrayList tempDecals = new ArrayList();

   public String getRandomDecal() {
      this.tempDecals.clear();
      this.getDecals(this.tempDecals);
      String var1 = (String)OutfitRNG.pickRandom(this.tempDecals);
      return var1 == null ? null : var1;
   }

   public void getDecals(ArrayList var1) {
      var1.addAll(this.m_Decals);

      for(int var2 = 0; var2 < this.m_Groups.size(); ++var2) {
         ClothingDecalGroup var3 = ClothingDecals.instance.FindGroup((String)this.m_Groups.get(var2));
         if (var3 != null) {
            var3.getDecals(var1);
         }
      }

   }
}
