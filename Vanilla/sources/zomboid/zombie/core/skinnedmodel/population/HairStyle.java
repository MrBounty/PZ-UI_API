package zombie.core.skinnedmodel.population;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import zombie.util.StringUtils;

public final class HairStyle {
   public String name = "";
   public String model;
   public String texture = "F_Hair_White";
   public final ArrayList alternate = new ArrayList();
   public int level = 0;
   public final ArrayList trimChoices = new ArrayList();
   public boolean growReference = false;
   public boolean attachedHair = false;
   public boolean noChoose = false;

   public boolean isValid() {
      return !StringUtils.isNullOrWhitespace(this.model) && !StringUtils.isNullOrWhitespace(this.texture);
   }

   public String getAlternate(String var1) {
      for(int var2 = 0; var2 < this.alternate.size(); ++var2) {
         HairStyle.Alternate var3 = (HairStyle.Alternate)this.alternate.get(var2);
         if (var1.equalsIgnoreCase(var3.category)) {
            return var3.style;
         }
      }

      return this.name;
   }

   public int getLevel() {
      return this.level;
   }

   public String getName() {
      return this.name;
   }

   public ArrayList getTrimChoices() {
      return this.trimChoices;
   }

   public boolean isAttachedHair() {
      return this.attachedHair;
   }

   public boolean isGrowReference() {
      return this.growReference;
   }

   public boolean isNoChoose() {
      return this.noChoose;
   }

   public static final class Alternate {
      @XmlAttribute
      public String category;
      @XmlAttribute
      public String style;
   }
}
