package zombie.characters.BodyDamage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import se.krka.kahlua.j2se.KahluaTableImpl;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.skills.PerkFactory;
import zombie.core.Rand;

public final class Fitness {
   private IsoGameCharacter parent = null;
   private HashMap regularityMap = new HashMap();
   private int fitnessLvl = 0;
   private int strLvl = 0;
   private final HashMap stiffnessTimerMap = new HashMap();
   private final HashMap stiffnessIncMap = new HashMap();
   private final ArrayList bodypartToIncStiffness = new ArrayList();
   private final HashMap exercises = new HashMap();
   private final HashMap exeTimer = new HashMap();
   private int lastUpdate = -1;
   private Fitness.FitnessExercise currentExe;
   private static final int HOURS_FOR_STIFFNESS = 12;
   private static final float BASE_STIFFNESS_INC = 0.5F;
   private static final float BASE_ENDURANCE_RED = 0.015F;
   private static final float BASE_REGULARITY_INC = 0.08F;
   private static final float BASE_REGULARITY_DEC = 0.002F;
   private static final float BASE_PAIN_INC = 2.5F;

   public Fitness(IsoGameCharacter var1) {
      this.setParent(var1);
   }

   public void update() {
      int var1 = GameTime.getInstance().getMinutes() / 10;
      if (this.lastUpdate == -1) {
         this.lastUpdate = var1;
      }

      if (var1 != this.lastUpdate) {
         this.lastUpdate = var1;
         ArrayList var2 = new ArrayList();
         this.decreaseRegularity();
         Iterator var3 = this.stiffnessTimerMap.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Integer var5 = (Integer)this.stiffnessTimerMap.get(var4);
            var5 = var5 - 1;
            if (var5 <= 0) {
               var2.add(var4);
               this.bodypartToIncStiffness.add(var4);
            } else {
               this.stiffnessTimerMap.put(var4, var5);
            }
         }

         int var8;
         for(var8 = 0; var8 < var2.size(); ++var8) {
            this.stiffnessTimerMap.remove(var2.get(var8));
         }

         for(var8 = 0; var8 < this.bodypartToIncStiffness.size(); ++var8) {
            String var9 = (String)this.bodypartToIncStiffness.get(var8);
            Float var6 = (Float)this.stiffnessIncMap.get(var9);
            if (var6 == null) {
               return;
            }

            var6 = var6 - 1.0F;
            this.increasePain(var9);
            if (var6 <= 0.0F) {
               this.bodypartToIncStiffness.remove(var8);
               this.stiffnessIncMap.remove(var9);
               --var8;
            } else {
               this.stiffnessIncMap.put(var9, var6);
            }
         }
      }

   }

   private void decreaseRegularity() {
      Iterator var1 = this.regularityMap.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         if (this.exeTimer.containsKey(var2) && GameTime.getInstance().getCalender().getTimeInMillis() - (Long)this.exeTimer.get(var2) > 86400000L) {
            float var3 = (Float)this.regularityMap.get(var2);
            var3 -= 0.002F;
            this.regularityMap.put(var2, var3);
         }
      }

   }

   private void increasePain(String var1) {
      int var2;
      BodyPart var3;
      if ("arms".equals(var1)) {
         for(var2 = BodyPartType.ForeArm_L.index(); var2 < BodyPartType.UpperArm_R.index() + 1; ++var2) {
            var3 = this.parent.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var2));
            var3.setStiffness(var3.getStiffness() + 2.5F);
         }
      }

      if ("legs".equals(var1)) {
         for(var2 = BodyPartType.UpperLeg_L.index(); var2 < BodyPartType.LowerLeg_R.index() + 1; ++var2) {
            var3 = this.parent.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var2));
            var3.setStiffness(var3.getStiffness() + 2.5F);
         }
      }

      BodyPart var4;
      if ("chest".equals(var1)) {
         var4 = this.parent.getBodyDamage().getBodyPart(BodyPartType.Torso_Upper);
         var4.setStiffness(var4.getStiffness() + 2.5F);
      }

      if ("abs".equals(var1)) {
         var4 = this.parent.getBodyDamage().getBodyPart(BodyPartType.Torso_Lower);
         var4.setStiffness(var4.getStiffness() + 2.5F);
      }

   }

   public void setCurrentExercise(String var1) {
      this.currentExe = (Fitness.FitnessExercise)this.exercises.get(var1);
   }

   public void exerciseRepeat() {
      this.fitnessLvl = this.parent.getPerkLevel(PerkFactory.Perks.Fitness);
      this.strLvl = this.parent.getPerkLevel(PerkFactory.Perks.Strength);
      this.incRegularity();
      this.reduceEndurance();
      this.incFutureStiffness();
      this.incStats();
      this.updateExeTimer();
   }

   private void updateExeTimer() {
      this.exeTimer.put(this.currentExe.type, GameTime.getInstance().getCalender().getTimeInMillis());
   }

   public void incRegularity() {
      float var1 = 0.08F;
      byte var2 = 4;
      double var3 = Math.log((double)((float)this.fitnessLvl / 5.0F + (float)var2));
      var1 = (float)((double)var1 * (Math.log((double)(var2 + 1)) / var3));
      Float var5 = (Float)this.regularityMap.get(this.currentExe.type);
      if (var5 == null) {
         var5 = 0.0F;
      }

      var5 = var5 + var1;
      var5 = Math.min(Math.max(var5, 0.0F), 100.0F);
      this.regularityMap.put(this.currentExe.type, var5);
   }

   public void reduceEndurance() {
      float var1 = 0.015F;
      Float var2 = (Float)this.regularityMap.get(this.currentExe.type);
      if (var2 == null) {
         var2 = 0.0F;
      }

      byte var3 = 50;
      double var4 = Math.log((double)(var2 / 50.0F + (float)var3));
      var1 = (float)((double)var1 * (var4 / Math.log((double)(var3 + 1))));
      if (this.currentExe.metabolics == Metabolics.FitnessHeavy) {
         var1 *= 1.3F;
      }

      var1 *= (float)(1 + this.parent.getMoodles().getMoodleLevel(MoodleType.HeavyLoad) / 3);
      this.parent.getStats().setEndurance(this.parent.getStats().getEndurance() - var1);
   }

   public void incFutureStiffness() {
      Float var1 = (Float)this.regularityMap.get(this.currentExe.type);
      if (var1 == null) {
         var1 = 0.0F;
      }

      for(int var2 = 0; var2 < this.currentExe.stiffnessInc.size(); ++var2) {
         float var3 = 0.5F;
         String var4 = (String)this.currentExe.stiffnessInc.get(var2);
         if (!this.stiffnessTimerMap.containsKey(var4) && !this.bodypartToIncStiffness.contains(var4)) {
            this.stiffnessTimerMap.put(var4, 72);
         }

         Float var5 = (Float)this.stiffnessIncMap.get(var4);
         if (var5 == null) {
            var5 = 0.0F;
         }

         var3 *= (120.0F - var1) / 170.0F;
         if (this.currentExe.metabolics == Metabolics.FitnessHeavy) {
            var3 *= 1.3F;
         }

         var3 *= (float)(1 + this.parent.getMoodles().getMoodleLevel(MoodleType.Tired) / 3);
         var5 = var5 + var3;
         var5 = Math.min(var5, 150.0F);
         this.stiffnessIncMap.put(var4, var5);
      }

   }

   public void incStats() {
      float var1 = 0.0F;
      float var2 = 0.0F;

      for(int var3 = 0; var3 < this.currentExe.stiffnessInc.size(); ++var3) {
         String var4 = (String)this.currentExe.stiffnessInc.get(var3);
         if ("arms".equals(var4)) {
            var1 += 4.0F;
         }

         if ("chest".equals(var4)) {
            var1 += 2.0F;
         }

         if ("legs".equals(var4)) {
            var2 += 4.0F;
         }

         if ("abs".equals(var4)) {
            var2 += 2.0F;
         }
      }

      if (this.strLvl > 5) {
         var1 *= (float)(1 + (this.strLvl - 5) / 10);
      }

      if (this.fitnessLvl > 5) {
         var2 *= (float)(1 + (this.fitnessLvl - 5) / 10);
      }

      var1 *= this.currentExe.xpModifier;
      var2 *= this.currentExe.xpModifier;
      this.parent.getXp().AddXP(PerkFactory.Perks.Strength, var1);
      this.parent.getXp().AddXP(PerkFactory.Perks.Fitness, var2);
   }

   public void resetValues() {
      this.stiffnessIncMap.clear();
      this.stiffnessTimerMap.clear();
      this.regularityMap.clear();
   }

   public void save(ByteBuffer var1) {
      var1.putInt(this.stiffnessIncMap.size());
      Iterator var2 = this.stiffnessIncMap.keySet().iterator();

      String var3;
      while(var2.hasNext()) {
         var3 = (String)var2.next();
         GameWindow.WriteString(var1, var3);
         var1.putFloat((Float)this.stiffnessIncMap.get(var3));
      }

      var1.putInt(this.stiffnessTimerMap.size());
      var2 = this.stiffnessTimerMap.keySet().iterator();

      while(var2.hasNext()) {
         var3 = (String)var2.next();
         GameWindow.WriteString(var1, var3);
         var1.putInt((Integer)this.stiffnessTimerMap.get(var3));
      }

      var1.putInt(this.regularityMap.size());
      var2 = this.regularityMap.keySet().iterator();

      while(var2.hasNext()) {
         var3 = (String)var2.next();
         GameWindow.WriteString(var1, var3);
         var1.putFloat((Float)this.regularityMap.get(var3));
      }

      var1.putInt(this.bodypartToIncStiffness.size());

      for(int var4 = 0; var4 < this.bodypartToIncStiffness.size(); ++var4) {
         GameWindow.WriteString(var1, (String)this.bodypartToIncStiffness.get(var4));
      }

      var1.putInt(this.exeTimer.size());
      var2 = this.exeTimer.keySet().iterator();

      while(var2.hasNext()) {
         var3 = (String)var2.next();
         GameWindow.WriteString(var1, var3);
         var1.putLong((Long)this.exeTimer.get(var3));
      }

   }

   public void load(ByteBuffer var1, int var2) {
      if (var2 >= 167) {
         int var3 = var1.getInt();
         int var4;
         if (var3 > 0) {
            for(var4 = 0; var4 < var3; ++var4) {
               this.stiffnessIncMap.put(GameWindow.ReadString(var1), var1.getFloat());
            }
         }

         var3 = var1.getInt();
         if (var3 > 0) {
            for(var4 = 0; var4 < var3; ++var4) {
               this.stiffnessTimerMap.put(GameWindow.ReadString(var1), var1.getInt());
            }
         }

         var3 = var1.getInt();
         if (var3 > 0) {
            for(var4 = 0; var4 < var3; ++var4) {
               this.regularityMap.put(GameWindow.ReadString(var1), var1.getFloat());
            }
         }

         var3 = var1.getInt();
         if (var3 > 0) {
            for(var4 = 0; var4 < var3; ++var4) {
               this.bodypartToIncStiffness.add(GameWindow.ReadString(var1));
            }
         }

         if (var2 >= 169) {
            var3 = var1.getInt();
            if (var3 > 0) {
               for(var4 = 0; var4 < var3; ++var4) {
                  this.exeTimer.put(GameWindow.ReadString(var1), var1.getLong());
               }
            }

         }
      }
   }

   public boolean onGoingStiffness() {
      return !this.bodypartToIncStiffness.isEmpty();
   }

   public int getCurrentExeStiffnessTimer(String var1) {
      var1 = var1.split(",")[0];
      return this.stiffnessTimerMap.get(var1) != null ? (Integer)this.stiffnessTimerMap.get(var1) : 0;
   }

   public float getCurrentExeStiffnessInc(String var1) {
      var1 = var1.split(",")[0];
      return this.stiffnessIncMap.get(var1) != null ? (Float)this.stiffnessIncMap.get(var1) : 0.0F;
   }

   public IsoGameCharacter getParent() {
      return this.parent;
   }

   public void setParent(IsoGameCharacter var1) {
      this.parent = var1;
   }

   public float getRegularity(String var1) {
      Float var2 = (Float)this.regularityMap.get(var1);
      if (var2 == null) {
         var2 = 0.0F;
      }

      return var2;
   }

   public HashMap getRegularityMap() {
      return this.regularityMap;
   }

   public void setRegularityMap(HashMap var1) {
      this.regularityMap = var1;
   }

   public void init() {
      if (this.exercises.isEmpty()) {
         KahluaTableImpl var1 = (KahluaTableImpl)LuaManager.env.rawget("FitnessExercises");
         KahluaTableImpl var2 = (KahluaTableImpl)var1.rawget("exercisesType");
         Iterator var3 = var2.delegate.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            this.exercises.put((String)var4.getKey(), new Fitness.FitnessExercise((KahluaTableImpl)var4.getValue()));
         }

         this.initRegularityMapProfession();
      }
   }

   public void initRegularityMapProfession() {
      if (this.regularityMap.isEmpty()) {
         boolean var1 = false;
         boolean var2 = false;
         boolean var3 = false;
         if (this.parent.getDescriptor().getProfession().equals("fitnessInstructor")) {
            var2 = true;
         }

         if (this.parent.getDescriptor().getProfession().equals("fireofficer")) {
            var1 = true;
         }

         if (this.parent.getDescriptor().getProfession().equals("securityguard")) {
            var3 = true;
         }

         if (var1 || var2 || var3) {
            float var5;
            for(Iterator var4 = this.exercises.keySet().iterator(); var4.hasNext(); this.regularityMap.put((String)var4.next(), var5)) {
               var5 = (float)Rand.Next(7, 12);
               if (var1) {
                  var5 = (float)Rand.Next(10, 20);
               }

               if (var2) {
                  var5 = (float)Rand.Next(40, 60);
               }
            }

         }
      }
   }

   public static final class FitnessExercise {
      String type = null;
      Metabolics metabolics = null;
      ArrayList stiffnessInc = null;
      float xpModifier = 1.0F;

      public FitnessExercise(KahluaTableImpl var1) {
         this.type = var1.rawgetStr("type");
         this.metabolics = (Metabolics)var1.rawget("metabolics");
         this.stiffnessInc = new ArrayList(Arrays.asList(var1.rawgetStr("stiffness").split(",")));
         if (var1.rawgetFloat("xpMod") > 0.0F) {
            this.xpModifier = var1.rawgetFloat("xpMod");
         }

      }
   }
}
