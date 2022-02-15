package zombie.characters;

import java.util.List;
import java.util.Stack;
import zombie.ai.State;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.characters.Moodles.Moodles;
import zombie.characters.skills.PerkFactory;
import zombie.characters.traits.TraitCollection;
import zombie.core.skinnedmodel.advancedanimation.debug.AnimatorDebugMonitor;
import zombie.core.skinnedmodel.visual.BaseVisual;
import zombie.core.textures.ColorInfo;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Literature;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoWindow;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.scripting.objects.Recipe;
import zombie.ui.UIFont;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.PathFindBehavior2;
import zombie.vehicles.PolygonalMap2;
import zombie.vehicles.VehiclePart;

public interface ILuaGameCharacter extends ILuaVariableSource, ILuaGameCharacterAttachedItems, ILuaGameCharacterDamage, ILuaGameCharacterClothing, ILuaGameCharacterHealth {
   String getFullName();

   SurvivorDesc getDescriptor();

   void setDescriptor(SurvivorDesc var1);

   boolean isRangedWeaponEmpty();

   void setRangedWeaponEmpty(boolean var1);

   BaseVisual getVisual();

   BaseCharacterSoundEmitter getEmitter();

   void resetModel();

   void resetModelNextFrame();

   IsoSpriteInstance getSpriteDef();

   boolean hasItems(String var1, int var2);

   int getXpForLevel(int var1);

   IsoGameCharacter.XP getXp();

   boolean isAsleep();

   void setAsleep(boolean var1);

   int getZombieKills();

   void setForceWakeUpTime(float var1);

   ItemContainer getInventory();

   InventoryItem getPrimaryHandItem();

   void setPrimaryHandItem(InventoryItem var1);

   InventoryItem getSecondaryHandItem();

   void setSecondaryHandItem(InventoryItem var1);

   boolean hasEquipped(String var1);

   boolean hasEquippedTag(String var1);

   boolean isHandItem(InventoryItem var1);

   boolean isPrimaryHandItem(InventoryItem var1);

   boolean isSecondaryHandItem(InventoryItem var1);

   boolean isItemInBothHands(InventoryItem var1);

   boolean removeFromHands(InventoryItem var1);

   void setSpeakColourInfo(ColorInfo var1);

   boolean isSpeaking();

   Moodles getMoodles();

   Stats getStats();

   TraitCollection getTraits();

   int getMaxWeight();

   void PlayAnim(String var1);

   void PlayAnimWithSpeed(String var1, float var2);

   void PlayAnimUnlooped(String var1);

   void StartTimedActionAnim(String var1);

   void StartTimedActionAnim(String var1, String var2);

   void StopTimedActionAnim();

   Stack getCharacterActions();

   void StartAction(BaseAction var1);

   void StopAllActionQueue();

   int getPerkLevel(PerkFactory.Perk var1);

   IsoGameCharacter.PerkInfo getPerkInfo(PerkFactory.Perk var1);

   void setPerkLevelDebug(PerkFactory.Perk var1, int var2);

   void LoseLevel(PerkFactory.Perk var1);

   void LevelPerk(PerkFactory.Perk var1, boolean var2);

   void LevelPerk(PerkFactory.Perk var1);

   void ReadLiterature(Literature var1);

   void setDir(IsoDirections var1);

   void Callout();

   boolean IsSpeaking();

   void Say(String var1);

   void Say(String var1, float var2, float var3, float var4, UIFont var5, float var6, String var7);

   void setHaloNote(String var1);

   void setHaloNote(String var1, float var2);

   void setHaloNote(String var1, int var2, int var3, int var4, float var5);

   void initSpritePartsEmpty();

   boolean HasTrait(String var1);

   void changeState(State var1);

   boolean isCurrentState(State var1);

   State getCurrentState();

   void pathToLocation(int var1, int var2, int var3);

   void pathToLocationF(float var1, float var2, float var3);

   boolean CanAttack();

   void smashCarWindow(VehiclePart var1);

   void smashWindow(IsoWindow var1);

   void openWindow(IsoWindow var1);

   void closeWindow(IsoWindow var1);

   void climbThroughWindow(IsoWindow var1);

   void climbThroughWindow(IsoWindow var1, Integer var2);

   void climbThroughWindowFrame(IsoObject var1);

   void climbSheetRope();

   void climbDownSheetRope();

   boolean canClimbSheetRope(IsoGridSquare var1);

   boolean canClimbDownSheetRopeInCurrentSquare();

   boolean canClimbDownSheetRope(IsoGridSquare var1);

   void climbThroughWindow(IsoThumpable var1);

   void climbThroughWindow(IsoThumpable var1, Integer var2);

   void climbOverFence(IsoDirections var1);

   boolean isAboveTopOfStairs();

   double getHoursSurvived();

   boolean isOutside();

   boolean isFemale();

   void setFemale(boolean var1);

   boolean isZombie();

   boolean isEquipped(InventoryItem var1);

   boolean isEquippedClothing(InventoryItem var1);

   boolean isAttachedItem(InventoryItem var1);

   void faceThisObject(IsoObject var1);

   void facePosition(int var1, int var2);

   void faceThisObjectAlt(IsoObject var1);

   int getAlreadyReadPages(String var1);

   void setAlreadyReadPages(String var1, int var2);

   boolean isSafety();

   void setSafety(boolean var1);

   float getSafetyCooldown();

   void setSafetyCooldown(float var1);

   float getMeleeDelay();

   void setMeleeDelay(float var1);

   float getRecoilDelay();

   void setRecoilDelay(float var1);

   int getMaintenanceMod();

   float getHammerSoundMod();

   float getWeldingSoundMod();

   boolean isGodMod();

   void setGodMod(boolean var1);

   BaseVehicle getVehicle();

   void setVehicle(BaseVehicle var1);

   float getInventoryWeight();

   List getKnownRecipes();

   boolean isRecipeKnown(Recipe var1);

   boolean isRecipeKnown(String var1);

   long playSound(String var1);

   void stopOrTriggerSound(long var1);

   void addWorldSoundUnlessInvisible(int var1, int var2, boolean var3);

   boolean isKnownPoison(InventoryItem var1);

   String getBedType();

   void setBedType(String var1);

   PolygonalMap2.Path getPath2();

   void setPath2(PolygonalMap2.Path var1);

   PathFindBehavior2 getPathFindBehavior2();

   IsoObject getBed();

   void setBed(IsoObject var1);

   boolean isReading();

   void setReading(boolean var1);

   float getTimeSinceLastSmoke();

   void setTimeSinceLastSmoke(float var1);

   boolean isInvisible();

   void setInvisible(boolean var1);

   boolean isDriving();

   boolean isInARoom();

   boolean isUnlimitedCarry();

   void setUnlimitedCarry(boolean var1);

   boolean isBuildCheat();

   void setBuildCheat(boolean var1);

   boolean isFarmingCheat();

   void setFarmingCheat(boolean var1);

   boolean isHealthCheat();

   void setHealthCheat(boolean var1);

   boolean isMechanicsCheat();

   void setMechanicsCheat(boolean var1);

   boolean isMovablesCheat();

   void setMovablesCheat(boolean var1);

   boolean isTimedActionInstantCheat();

   void setTimedActionInstantCheat(boolean var1);

   boolean isTimedActionInstant();

   boolean isShowAdminTag();

   void setShowAdminTag(boolean var1);

   void reportEvent(String var1);

   AnimatorDebugMonitor getDebugMonitor();

   void setDebugMonitor(AnimatorDebugMonitor var1);

   boolean isAiming();

   void resetBeardGrowingTime();

   void resetHairGrowingTime();
}
