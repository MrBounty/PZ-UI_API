package zombie.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import zombie.ai.states.ThumpState;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.Stance;
import zombie.characters.Stats;
import zombie.characters.SurvivorDesc;
import zombie.characters.SurvivorGroup;
import zombie.iso.IsoMovingObject;
import zombie.iso.LosUtil;
import zombie.iso.Vector2;
import zombie.iso.Vector3;

public final class GameCharacterAIBrain {
   private final IsoGameCharacter character;
   public final ArrayList spottedCharacters = new ArrayList();
   public boolean StepBehaviors;
   public Stance stance;
   public boolean controlledByAdvancedPathfinder;
   public boolean isInMeta;
   public final HashMap BlockedMemories = new HashMap();
   public final Vector2 AIFocusPoint = new Vector2();
   public final Vector3 nextPathTarget = new Vector3();
   public IsoMovingObject aiTarget;
   public boolean NextPathNodeInvalidated;
   public final AIBrainPlayerControlVars HumanControlVars = new AIBrainPlayerControlVars();
   String order;
   public ArrayList teammateChasingZombies = new ArrayList();
   public ArrayList chasingZombies = new ArrayList();
   public boolean allowLongTermTick = true;
   public boolean isAI = false;
   static ArrayList tempZombies = new ArrayList();
   static IsoGameCharacter compare;
   private static final Stack Vectors = new Stack();

   public IsoGameCharacter getCharacter() {
      return this.character;
   }

   public GameCharacterAIBrain(IsoGameCharacter var1) {
      this.character = var1;
   }

   public void update() {
   }

   public void postUpdateHuman(IsoPlayer var1) {
   }

   public String getOrder() {
      return this.order;
   }

   public void setOrder(String var1) {
      this.order = var1;
   }

   public SurvivorGroup getGroup() {
      return this.character.getDescriptor().getGroup();
   }

   public int getCloseZombieCount() {
      this.character.getStats();
      return Stats.NumCloseZombies;
   }

   public IsoZombie getClosestChasingZombie(boolean var1) {
      IsoZombie var2 = null;
      float var3 = 1.0E7F;

      int var4;
      for(var4 = 0; var4 < this.chasingZombies.size(); ++var4) {
         IsoZombie var5 = (IsoZombie)this.chasingZombies.get(var4);
         float var6 = var5.DistTo(this.character);
         if (var5.isOnFloor()) {
            var6 += 2.0F;
         }

         if (!LosUtil.lineClearCollide((int)var5.x, (int)var5.y, (int)var5.z, (int)this.character.x, (int)this.character.y, (int)this.character.z, false) && var5.getStateMachine().getCurrent() != ThumpState.instance() && var6 < var3 && var5.target == this.character) {
            var3 = var6;
            var2 = (IsoZombie)this.chasingZombies.get(var4);
         }
      }

      float var7;
      IsoGameCharacter var8;
      IsoZombie var9;
      if (var2 == null && var1) {
         for(var4 = 0; var4 < this.getGroup().Members.size(); ++var4) {
            var8 = ((SurvivorDesc)this.getGroup().Members.get(var4)).getInstance();
            var9 = var8.getGameCharacterAIBrain().getClosestChasingZombie(false);
            if (var9 != null) {
               var7 = var9.DistTo(this.character);
               if (var7 < var3) {
                  var3 = var7;
                  var2 = var9;
               }
            }
         }
      }

      if (var2 == null && var1) {
         for(var4 = 0; var4 < this.spottedCharacters.size(); ++var4) {
            var8 = (IsoGameCharacter)this.spottedCharacters.get(var4);
            var9 = var8.getGameCharacterAIBrain().getClosestChasingZombie(false);
            if (var9 != null) {
               var7 = var9.DistTo(this.character);
               if (var7 < var3) {
                  var3 = var7;
                  var2 = var9;
               }
            }
         }
      }

      return var2 != null && var2.DistTo(this.character) > 30.0F ? null : var2;
   }

   public IsoZombie getClosestChasingZombie() {
      return this.getClosestChasingZombie(true);
   }

   public ArrayList getClosestChasingZombies(int var1) {
      tempZombies.clear();
      Object var2 = null;
      float var3 = 1.0E7F;

      int var4;
      for(var4 = 0; var4 < this.chasingZombies.size(); ++var4) {
         IsoZombie var5 = (IsoZombie)this.chasingZombies.get(var4);
         var5.DistTo(this.character);
         if (!LosUtil.lineClearCollide((int)var5.x, (int)var5.y, (int)var5.z, (int)this.character.x, (int)this.character.y, (int)this.character.z, false)) {
            tempZombies.add(var5);
         }
      }

      compare = this.character;
      tempZombies.sort((var0, var1x) -> {
         float var2 = compare.DistTo(var0);
         float var3 = compare.DistTo(var1x);
         if (var2 > var3) {
            return 1;
         } else {
            return var2 < var3 ? -1 : 0;
         }
      });
      var4 = var1 - tempZombies.size();
      if (var4 > tempZombies.size() - 2) {
         var4 = tempZombies.size() - 2;
      }

      for(int var7 = 0; var7 < var4; ++var7) {
         tempZombies.remove(tempZombies.size() - 1);
      }

      return tempZombies;
   }

   public void AddBlockedMemory(int var1, int var2, int var3) {
      synchronized(this.BlockedMemories) {
         Vector3 var5 = new Vector3((float)((int)this.character.x), (float)((int)this.character.y), (float)((int)this.character.z));
         if (!this.BlockedMemories.containsKey(var5)) {
            this.BlockedMemories.put(var5, new ArrayList());
         }

         ArrayList var6 = (ArrayList)this.BlockedMemories.get(var5);
         Vector3 var7 = new Vector3((float)var1, (float)var2, (float)var3);
         if (!var6.contains(var7)) {
            var6.add(var7);
         }

      }
   }

   public boolean HasBlockedMemory(int var1, int var2, int var3, int var4, int var5, int var6) {
      synchronized(this.BlockedMemories) {
         boolean var10000;
         synchronized(Vectors) {
            Vector3 var7;
            if (Vectors.isEmpty()) {
               var7 = new Vector3();
            } else {
               var7 = (Vector3)Vectors.pop();
            }

            Vector3 var8;
            if (Vectors.isEmpty()) {
               var8 = new Vector3();
            } else {
               var8 = (Vector3)Vectors.pop();
            }

            var7.x = (float)var1;
            var7.y = (float)var2;
            var7.z = (float)var3;
            var8.x = (float)var4;
            var8.y = (float)var5;
            var8.z = (float)var6;
            if (!this.BlockedMemories.containsKey(var7)) {
               Vectors.push(var7);
               Vectors.push(var8);
               var10000 = false;
               return var10000;
            }

            if (!((ArrayList)this.BlockedMemories.get(var7)).contains(var8)) {
               Vectors.push(var7);
               Vectors.push(var8);
               return false;
            }

            Vectors.push(var7);
            Vectors.push(var8);
            var10000 = true;
         }

         return var10000;
      }
   }

   public void renderlast() {
   }
}
