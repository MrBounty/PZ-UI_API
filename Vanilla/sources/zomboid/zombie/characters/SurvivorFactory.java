package zombie.characters;

import java.util.ArrayList;
import zombie.core.Rand;
import zombie.iso.IsoCell;

public final class SurvivorFactory {
   public static final ArrayList FemaleForenames = new ArrayList();
   public static final ArrayList MaleForenames = new ArrayList();
   public static final ArrayList Surnames = new ArrayList();

   public static void Reset() {
      FemaleForenames.clear();
      MaleForenames.clear();
      Surnames.clear();
      SurvivorDesc.HairCommonColors.clear();
      SurvivorDesc.TrouserCommonColors.clear();
   }

   public static SurvivorDesc[] CreateFamily(int var0) {
      SurvivorDesc[] var1 = new SurvivorDesc[var0];

      for(int var2 = 0; var2 < var0; ++var2) {
         var1[var2] = CreateSurvivor();
         if (var2 > 0) {
            var1[var2].surname = var1[0].surname;
         }
      }

      return var1;
   }

   public static SurvivorDesc CreateSurvivor() {
      switch(Rand.Next(3)) {
      case 0:
         return CreateSurvivor(SurvivorFactory.SurvivorType.Friendly);
      case 1:
         return CreateSurvivor(SurvivorFactory.SurvivorType.Neutral);
      case 2:
         return CreateSurvivor(SurvivorFactory.SurvivorType.Aggressive);
      default:
         return null;
      }
   }

   public static SurvivorDesc CreateSurvivor(SurvivorFactory.SurvivorType var0, boolean var1) {
      SurvivorDesc var2 = new SurvivorDesc();
      var2.setType(var0);
      IsoGameCharacter.getSurvivorMap().put(var2.ID, var2);
      var2.setFemale(var1);
      randomName(var2);
      if (var2.isFemale()) {
         setTorso(var2);
      } else {
         setTorso(var2);
      }

      return var2;
   }

   public static void setTorso(SurvivorDesc var0) {
      if (var0.isFemale()) {
         var0.torso = "Kate";
      } else {
         var0.torso = "Male";
      }

   }

   public static SurvivorDesc CreateSurvivor(SurvivorFactory.SurvivorType var0) {
      return CreateSurvivor(var0, Rand.Next(2) == 0);
   }

   public static SurvivorDesc[] CreateSurvivorGroup(int var0) {
      SurvivorDesc[] var1 = new SurvivorDesc[var0];

      for(int var2 = 0; var2 < var0; ++var2) {
         var1[var2] = CreateSurvivor();
      }

      return var1;
   }

   public static IsoSurvivor InstansiateInCell(SurvivorDesc var0, IsoCell var1, int var2, int var3, int var4) {
      var0.Instance = new IsoSurvivor(var0, var1, var2, var3, var4);
      return (IsoSurvivor)var0.Instance;
   }

   public static void randomName(SurvivorDesc var0) {
      if (var0.isFemale()) {
         var0.forename = (String)FemaleForenames.get(Rand.Next(FemaleForenames.size()));
      } else {
         var0.forename = (String)MaleForenames.get(Rand.Next(MaleForenames.size()));
      }

      var0.surname = (String)Surnames.get(Rand.Next(Surnames.size()));
   }

   public static void addSurname(String var0) {
      Surnames.add(var0);
   }

   public static void addFemaleForename(String var0) {
      FemaleForenames.add(var0);
   }

   public static void addMaleForename(String var0) {
      MaleForenames.add(var0);
   }

   public static enum SurvivorType {
      Friendly,
      Neutral,
      Aggressive;

      // $FF: synthetic method
      private static SurvivorFactory.SurvivorType[] $values() {
         return new SurvivorFactory.SurvivorType[]{Friendly, Neutral, Aggressive};
      }
   }
}
