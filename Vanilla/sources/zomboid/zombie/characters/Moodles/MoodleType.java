package zombie.characters.Moodles;

import zombie.core.Translator;

public enum MoodleType {
   Endurance,
   Tired,
   Hungry,
   Panic,
   Sick,
   Bored,
   Unhappy,
   Bleeding,
   Wet,
   HasACold,
   Angry,
   Stress,
   Thirst,
   Injured,
   Pain,
   HeavyLoad,
   Drunk,
   Dead,
   Zombie,
   Hyperthermia,
   Hypothermia,
   Windchill,
   CantSprint,
   FoodEaten,
   MAX;

   public static MoodleType FromIndex(int var0) {
      switch(var0) {
      case 0:
         return Endurance;
      case 1:
         return Tired;
      case 2:
         return Hungry;
      case 3:
         return Panic;
      case 4:
         return Sick;
      case 5:
         return Bored;
      case 6:
         return Unhappy;
      case 7:
         return Bleeding;
      case 8:
         return Wet;
      case 9:
         return HasACold;
      case 10:
         return Angry;
      case 11:
         return Stress;
      case 12:
         return Thirst;
      case 13:
         return Injured;
      case 14:
         return Pain;
      case 15:
         return HeavyLoad;
      case 16:
         return Drunk;
      case 17:
         return Dead;
      case 18:
         return Zombie;
      case 19:
         return FoodEaten;
      case 20:
         return Hyperthermia;
      case 21:
         return Hypothermia;
      case 22:
         return Windchill;
      case 23:
         return CantSprint;
      default:
         return MAX;
      }
   }

   public static MoodleType FromString(String var0) {
      if (var0.equals("Endurance")) {
         return Endurance;
      } else if (var0.equals("Tired")) {
         return Tired;
      } else if (var0.equals("Hungry")) {
         return Hungry;
      } else if (var0.equals("Panic")) {
         return Panic;
      } else if (var0.equals("Sick")) {
         return Sick;
      } else if (var0.equals("Bored")) {
         return Bored;
      } else if (var0.equals("Unhappy")) {
         return Unhappy;
      } else if (var0.equals("Bleeding")) {
         return Bleeding;
      } else if (var0.equals("Wet")) {
         return Wet;
      } else if (var0.equals("HasACold")) {
         return HasACold;
      } else if (var0.equals("Angry")) {
         return Angry;
      } else if (var0.equals("Stress")) {
         return Stress;
      } else if (var0.equals("Thirst")) {
         return Thirst;
      } else if (var0.equals("Injured")) {
         return Injured;
      } else if (var0.equals("Pain")) {
         return Pain;
      } else if (var0.equals("HeavyLoad")) {
         return HeavyLoad;
      } else if (var0.equals("Drunk")) {
         return Drunk;
      } else if (var0.equals("Dead")) {
         return Dead;
      } else if (var0.equals("Zombie")) {
         return Zombie;
      } else if (var0.equals("Windchill")) {
         return Windchill;
      } else if (var0.equals("FoodEaten")) {
         return FoodEaten;
      } else if (var0.equals("Hyperthermia")) {
         return Hyperthermia;
      } else if (var0.equals("Hypothermia")) {
         return Hypothermia;
      } else {
         return var0.equals("CantSprint") ? CantSprint : MAX;
      }
   }

   public static String getDisplayName(MoodleType var0, int var1) {
      if (var1 > 4) {
         var1 = 4;
      }

      if (var1 == 0) {
         return "Invalid Moodle Level";
      } else if (var0 == CantSprint) {
         return Translator.getText("Moodles_CantSprint");
      } else {
         if (var0 == Endurance) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_endurance_lvl1");
            case 2:
               return Translator.getText("Moodles_endurance_lvl2");
            case 3:
               return Translator.getText("Moodles_endurance_lvl3");
            case 4:
               return Translator.getText("Moodles_endurance_lvl4");
            }
         }

         if (var0 == Angry) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_angry_lvl1");
            case 2:
               return Translator.getText("Moodles_angry_lvl2");
            case 3:
               return Translator.getText("Moodles_angry_lvl3");
            case 4:
               return Translator.getText("Moodles_angry_lvl4");
            }
         }

         if (var0 == Stress) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_stress_lvl1");
            case 2:
               return Translator.getText("Moodles_stress_lvl2");
            case 3:
               return Translator.getText("Moodles_stress_lvl3");
            case 4:
               return Translator.getText("Moodles_stress_lvl4");
            }
         }

         if (var0 == Thirst) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_thirst_lvl1");
            case 2:
               return Translator.getText("Moodles_thirst_lvl2");
            case 3:
               return Translator.getText("Moodles_thirst_lvl3");
            case 4:
               return Translator.getText("Moodles_thirst_lvl4");
            }
         }

         if (var0 == Tired) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_tired_lvl1");
            case 2:
               return Translator.getText("Moodles_tired_lvl2");
            case 3:
               return Translator.getText("Moodles_tired_lvl3");
            case 4:
               return Translator.getText("Moodles_tired_lvl4");
            }
         }

         if (var0 == Hungry) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_hungry_lvl1");
            case 2:
               return Translator.getText("Moodles_hungry_lvl2");
            case 3:
               return Translator.getText("Moodles_hungry_lvl3");
            case 4:
               return Translator.getText("Moodles_hungry_lvl4");
            }
         }

         if (var0 == Panic) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_panic_lvl1");
            case 2:
               return Translator.getText("Moodles_panic_lvl2");
            case 3:
               return Translator.getText("Moodles_panic_lvl3");
            case 4:
               return Translator.getText("Moodles_panic_lvl4");
            }
         }

         if (var0 == Sick) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_sick_lvl1");
            case 2:
               return Translator.getText("Moodles_sick_lvl2");
            case 3:
               return Translator.getText("Moodles_sick_lvl3");
            case 4:
               return Translator.getText("Moodles_sick_lvl4");
            }
         }

         if (var0 == Bored) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_bored_lvl1");
            case 2:
               return Translator.getText("Moodles_bored_lvl2");
            case 3:
               return Translator.getText("Moodles_bored_lvl3");
            case 4:
               return Translator.getText("Moodles_bored_lvl4");
            }
         }

         if (var0 == Unhappy) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_unhappy_lvl1");
            case 2:
               return Translator.getText("Moodles_unhappy_lvl2");
            case 3:
               return Translator.getText("Moodles_unhappy_lvl3");
            case 4:
               return Translator.getText("Moodles_unhappy_lvl4");
            }
         }

         if (var0 == Bleeding) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_bleeding_lvl1");
            case 2:
               return Translator.getText("Moodles_bleeding_lvl2");
            case 3:
               return Translator.getText("Moodles_bleeding_lvl3");
            case 4:
               return Translator.getText("Moodles_bleeding_lvl4");
            }
         }

         if (var0 == Wet) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_wet_lvl1");
            case 2:
               return Translator.getText("Moodles_wet_lvl2");
            case 3:
               return Translator.getText("Moodles_wet_lvl3");
            case 4:
               return Translator.getText("Moodles_wet_lvl4");
            }
         }

         if (var0 == HasACold) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_hascold_lvl1");
            case 2:
               return Translator.getText("Moodles_hascold_lvl2");
            case 3:
               return Translator.getText("Moodles_hascold_lvl3");
            case 4:
               return Translator.getText("Moodles_hascold_lvl4");
            }
         }

         if (var0 == Injured) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_injured_lvl1");
            case 2:
               return Translator.getText("Moodles_injured_lvl2");
            case 3:
               return Translator.getText("Moodles_injured_lvl3");
            case 4:
               return Translator.getText("Moodles_injured_lvl4");
            }
         }

         if (var0 == Pain) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_pain_lvl1");
            case 2:
               return Translator.getText("Moodles_pain_lvl2");
            case 3:
               return Translator.getText("Moodles_pain_lvl3");
            case 4:
               return Translator.getText("Moodles_pain_lvl4");
            }
         }

         if (var0 == HeavyLoad) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_heavyload_lvl1");
            case 2:
               return Translator.getText("Moodles_heavyload_lvl2");
            case 3:
               return Translator.getText("Moodles_heavyload_lvl3");
            case 4:
               return Translator.getText("Moodles_heavyload_lvl4");
            }
         }

         if (var0 == Drunk) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_drunk_lvl1");
            case 2:
               return Translator.getText("Moodles_drunk_lvl2");
            case 3:
               return Translator.getText("Moodles_drunk_lvl3");
            case 4:
               return Translator.getText("Moodles_drunk_lvl4");
            }
         }

         if (var0 == Dead) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_dead_lvl1");
            case 2:
               return Translator.getText("Moodles_dead_lvl1");
            case 3:
               return Translator.getText("Moodles_dead_lvl1");
            case 4:
               return Translator.getText("Moodles_dead_lvl1");
            }
         }

         if (var0 == Zombie) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_zombie_lvl1");
            case 2:
               return Translator.getText("Moodles_zombie_lvl1");
            case 3:
               return Translator.getText("Moodles_zombie_lvl1");
            case 4:
               return Translator.getText("Moodles_zombie_lvl1");
            }
         }

         if (var0 == Windchill) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_windchill_lvl1");
            case 2:
               return Translator.getText("Moodles_windchill_lvl2");
            case 3:
               return Translator.getText("Moodles_windchill_lvl3");
            case 4:
               return Translator.getText("Moodles_windchill_lvl4");
            }
         }

         if (var0 == FoodEaten) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_foodeaten_lvl1");
            case 2:
               return Translator.getText("Moodles_foodeaten_lvl2");
            case 3:
               return Translator.getText("Moodles_foodeaten_lvl3");
            case 4:
               return Translator.getText("Moodles_foodeaten_lvl4");
            }
         }

         if (var0 == Hyperthermia) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_hyperthermia_lvl1");
            case 2:
               return Translator.getText("Moodles_hyperthermia_lvl2");
            case 3:
               return Translator.getText("Moodles_hyperthermia_lvl3");
            case 4:
               return Translator.getText("Moodles_hyperthermia_lvl4");
            }
         }

         if (var0 == Hypothermia) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_hypothermia_lvl1");
            case 2:
               return Translator.getText("Moodles_hypothermia_lvl2");
            case 3:
               return Translator.getText("Moodles_hypothermia_lvl3");
            case 4:
               return Translator.getText("Moodles_hypothermia_lvl4");
            }
         }

         return "Unkown Moodle Type";
      }
   }

   public static String getDescriptionText(MoodleType var0, int var1) {
      if (var1 > 4) {
         var1 = 4;
      }

      if (var1 == 0) {
         return "Invalid Moodle Level";
      } else if (var0 == CantSprint) {
         return Translator.getText("Moodles_CantSprint_desc");
      } else {
         if (var0 == Endurance) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_endurance_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_endurance_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_endurance_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_endurance_desc_lvl4");
            }
         }

         if (var0 == Angry) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_angry_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_angry_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_angry_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_angry_desc_lvl4");
            }
         }

         if (var0 == Stress) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_stress_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_stress_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_stress_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_stress_desc_lvl4");
            }
         }

         if (var0 == Thirst) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_thirst_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_thirst_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_thirst_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_thirst_desc_lvl4");
            }
         }

         if (var0 == Tired) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_tired_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_tired_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_tired_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_tired_desc_lvl4");
            }
         }

         if (var0 == Hungry) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_hungry_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_hungry_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_hungry_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_hungry_desc_lvl4");
            }
         }

         if (var0 == Panic) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_panic_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_panic_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_panic_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_panic_desc_lvl4");
            }
         }

         if (var0 == Sick) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_sick_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_sick_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_sick_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_sick_desc_lvl4");
            }
         }

         if (var0 == Bored) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_bored_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_bored_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_bored_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_bored_desc_lvl4");
            }
         }

         if (var0 == Unhappy) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_unhappy_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_unhappy_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_unhappy_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_unhappy_desc_lvl4");
            }
         }

         if (var0 == Bleeding) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_bleed_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_bleed_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_bleed_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_bleed_desc_lvl4");
            }
         }

         if (var0 == Wet) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_wet_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_wet_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_wet_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_wet_desc_lvl4");
            }
         }

         if (var0 == HasACold) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_hasacold_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_hasacold_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_hasacold_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_hasacold_desc_lvl4");
            }
         }

         if (var0 == Injured) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_injured_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_injured_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_injured_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_injured_desc_lvl4");
            }
         }

         if (var0 == Pain) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_pain_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_pain_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_pain_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_pain_desc_lvl4");
            }
         }

         if (var0 == HeavyLoad) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_heavyload_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_heavyload_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_heavyload_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_heavyload_desc_lvl4");
            }
         }

         if (var0 == Drunk) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_drunk_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_drunk_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_drunk_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_drunk_desc_lvl4");
            }
         }

         if (var0 == Dead) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_dead_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_dead_desc_lvl1");
            case 3:
               return Translator.getText("Moodles_dead_desc_lvl1");
            case 4:
               return Translator.getText("Moodles_dead_desc_lvl1");
            }
         }

         if (var0 == Zombie) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_zombified_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_zombified_desc_lvl1");
            case 3:
               return Translator.getText("Moodles_zombified_desc_lvl1");
            case 4:
               return Translator.getText("Moodles_zombified_desc_lvl1");
            }
         }

         if (var0 == Windchill) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_windchill_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_windchill_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_windchill_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_windchill_desc_lvl4");
            }
         }

         if (var0 == FoodEaten) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_foodeaten_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_foodeaten_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_foodeaten_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_foodeaten_desc_lvl4");
            }
         }

         if (var0 == Hyperthermia) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_hyperthermia_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_hyperthermia_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_hyperthermia_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_hyperthermia_desc_lvl4");
            }
         }

         if (var0 == Hypothermia) {
            switch(var1) {
            case 1:
               return Translator.getText("Moodles_hypothermia_desc_lvl1");
            case 2:
               return Translator.getText("Moodles_hypothermia_desc_lvl2");
            case 3:
               return Translator.getText("Moodles_hypothermia_desc_lvl3");
            case 4:
               return Translator.getText("Moodles_hypothermia_desc_lvl4");
            }
         }

         return "Unkown Moodle Type";
      }
   }

   public static int GoodBadNeutral(MoodleType var0) {
      if (var0 == Endurance) {
         return 2;
      } else if (var0 == Tired) {
         return 2;
      } else if (var0 == Hungry) {
         return 2;
      } else if (var0 == Panic) {
         return 2;
      } else if (var0 == Sick) {
         return 2;
      } else if (var0 == Bored) {
         return 2;
      } else if (var0 == Unhappy) {
         return 2;
      } else if (var0 == Bleeding) {
         return 2;
      } else if (var0 == Wet) {
         return 2;
      } else if (var0 == HasACold) {
         return 2;
      } else if (var0 == Angry) {
         return 2;
      } else if (var0 == Stress) {
         return 2;
      } else if (var0 == Thirst) {
         return 2;
      } else if (var0 == CantSprint) {
         return 2;
      } else if (var0 == Injured) {
         return 2;
      } else if (var0 == Pain) {
         return 2;
      } else if (var0 == HeavyLoad) {
         return 2;
      } else if (var0 == Drunk) {
         return 2;
      } else if (var0 == Dead) {
         return 2;
      } else if (var0 == Zombie) {
         return 2;
      } else if (var0 == Windchill) {
         return 2;
      } else if (var0 == FoodEaten) {
         return 1;
      } else if (var0 == Hyperthermia) {
         return 2;
      } else {
         return var0 == Hypothermia ? 2 : 2;
      }
   }

   public static int ToIndex(MoodleType var0) {
      if (var0 == null) {
         return 0;
      } else {
         switch(var0) {
         case Endurance:
            return 0;
         case Tired:
            return 1;
         case Hungry:
            return 2;
         case Panic:
            return 3;
         case Sick:
            return 4;
         case Bored:
            return 5;
         case Unhappy:
            return 6;
         case Bleeding:
            return 7;
         case Wet:
            return 8;
         case HasACold:
            return 9;
         case Angry:
            return 10;
         case Stress:
            return 11;
         case Thirst:
            return 12;
         case Injured:
            return 13;
         case Pain:
            return 14;
         case HeavyLoad:
            return 15;
         case Drunk:
            return 16;
         case Dead:
            return 17;
         case Zombie:
            return 18;
         case FoodEaten:
            return 19;
         case Hyperthermia:
            return 20;
         case Hypothermia:
            return 21;
         case Windchill:
            return 22;
         case CantSprint:
            return 23;
         case MAX:
            return 24;
         default:
            return 0;
         }
      }
   }

   // $FF: synthetic method
   private static MoodleType[] $values() {
      return new MoodleType[]{Endurance, Tired, Hungry, Panic, Sick, Bored, Unhappy, Bleeding, Wet, HasACold, Angry, Stress, Thirst, Injured, Pain, HeavyLoad, Drunk, Dead, Zombie, Hyperthermia, Hypothermia, Windchill, CantSprint, FoodEaten, MAX};
   }
}
