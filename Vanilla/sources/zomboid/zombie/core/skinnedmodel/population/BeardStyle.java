package zombie.core.skinnedmodel.population;

import java.util.ArrayList;
import zombie.util.StringUtils;

public class BeardStyle {
   public String name = "";
   public String model;
   public String texture = "F_Hair_White";
   public int level = 0;
   public ArrayList trimChoices = new ArrayList();
   public boolean growReference = false;

   public boolean isValid() {
      return !StringUtils.isNullOrWhitespace(this.model) && !StringUtils.isNullOrWhitespace(this.texture);
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
}
