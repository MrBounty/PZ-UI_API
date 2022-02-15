package zombie.characters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameWindow;
import zombie.Lua.LuaManager;
import zombie.characters.WornItems.BodyLocations;
import zombie.characters.WornItems.WornItems;
import zombie.characters.professions.ProfessionFactory;
import zombie.characters.skills.PerkFactory;
import zombie.characters.traits.ObservationFactory;
import zombie.core.Color;
import zombie.core.Rand;
import zombie.core.skinnedmodel.population.OutfitRNG;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.IHumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.core.textures.ColorInfo;
import zombie.inventory.InventoryItem;
import zombie.iso.IsoWorld;

public final class SurvivorDesc implements IHumanVisual {
   public final HumanVisual humanVisual = new HumanVisual(this);
   public final WornItems wornItems = new WornItems(BodyLocations.getGroup("Human"));
   SurvivorGroup group = new SurvivorGroup();
   private static int IDCount = 0;
   public static final ArrayList TrouserCommonColors = new ArrayList();
   public static final ArrayList HairCommonColors = new ArrayList();
   private final HashMap xpBoostMap = new HashMap();
   private KahluaTable metaTable;
   public String Profession = "";
   protected String forename = "None";
   protected int ID = 0;
   protected IsoGameCharacter Instance = null;
   private boolean bFemale = true;
   protected String surname = "None";
   private String InventoryScript = null;
   protected String torso = "Base_Torso";
   protected final HashMap MetCount = new HashMap();
   protected float bravery = 1.0F;
   protected float loner = 0.0F;
   protected float aggressiveness = 1.0F;
   protected float compassion = 1.0F;
   protected float temper = 0.0F;
   protected float friendliness = 0.0F;
   private float favourindoors = 0.0F;
   protected float loyalty = 0.0F;
   public final ArrayList extra = new ArrayList();
   private final ArrayList Observations = new ArrayList(0);
   private SurvivorFactory.SurvivorType type;
   public boolean bDead;

   public HumanVisual getHumanVisual() {
      return this.humanVisual;
   }

   public void getItemVisuals(ItemVisuals var1) {
      this.wornItems.getItemVisuals(var1);
   }

   public boolean isFemale() {
      return this.bFemale;
   }

   public boolean isZombie() {
      return false;
   }

   public boolean isSkeleton() {
      return false;
   }

   public WornItems getWornItems() {
      return this.wornItems;
   }

   public void setWornItem(String var1, InventoryItem var2) {
      this.wornItems.setItem(var1, var2);
   }

   public InventoryItem getWornItem(String var1) {
      return this.wornItems.getItem(var1);
   }

   public void dressInNamedOutfit(String var1) {
      ItemVisuals var2 = new ItemVisuals();
      this.getHumanVisual().dressInNamedOutfit(var1, var2);
      this.getWornItems().setFromItemVisuals(var2);
   }

   public SurvivorGroup getGroup() {
      return this.group;
   }

   public boolean isLeader() {
      return this.group.getLeader() == this;
   }

   public static int getIDCount() {
      return IDCount;
   }

   public void setProfessionSkills(ProfessionFactory.Profession var1) {
      this.getXPBoostMap().clear();
      this.getXPBoostMap().putAll(var1.XPBoostMap);
   }

   public HashMap getXPBoostMap() {
      return this.xpBoostMap;
   }

   public KahluaTable getMeta() {
      if (this.metaTable == null) {
         this.metaTable = (KahluaTable)LuaManager.caller.pcall(LuaManager.thread, LuaManager.env.rawget("createMetaSurvivor"), (Object)this)[1];
      }

      return this.metaTable;
   }

   public int getCalculatedToughness() {
      this.metaTable = this.getMeta();
      KahluaTable var1 = (KahluaTable)LuaManager.env.rawget("MetaSurvivor");
      Double var2 = (Double)LuaManager.caller.pcall(LuaManager.thread, var1.rawget("getCalculatedToughness"), (Object)this.metaTable)[1];
      return var2.intValue();
   }

   public static void setIDCount(int var0) {
      IDCount = var0;
   }

   public boolean isDead() {
      return this.bDead;
   }

   public SurvivorDesc() {
      this.type = SurvivorFactory.SurvivorType.Neutral;
      this.ID = IDCount++;
      IsoWorld.instance.SurvivorDescriptors.put(this.ID, this);
      this.doStats();
   }

   public SurvivorDesc(boolean var1) {
      this.type = SurvivorFactory.SurvivorType.Neutral;
      this.ID = IDCount++;
      this.doStats();
   }

   public SurvivorDesc(SurvivorDesc var1) {
      this.type = SurvivorFactory.SurvivorType.Neutral;
      this.aggressiveness = var1.aggressiveness;
      this.bDead = var1.bDead;
      this.bFemale = var1.bFemale;
      this.bravery = var1.bravery;
      this.compassion = var1.compassion;
      this.extra.addAll(var1.extra);
      this.favourindoors = var1.favourindoors;
      this.forename = var1.forename;
      this.friendliness = var1.friendliness;
      this.InventoryScript = var1.InventoryScript;
      this.loner = var1.loner;
      this.loyalty = var1.loyalty;
      this.Profession = var1.Profession;
      this.surname = var1.surname;
      this.temper = var1.temper;
      this.torso = var1.torso;
      this.type = var1.type;
   }

   public void meet(SurvivorDesc var1) {
      if (this.MetCount.containsKey(var1.ID)) {
         this.MetCount.put(var1.ID, (Integer)this.MetCount.get(var1.ID) + 1);
      } else {
         this.MetCount.put(var1.ID, 1);
      }

      if (var1.MetCount.containsKey(this.ID)) {
         var1.MetCount.put(this.ID, (Integer)var1.MetCount.get(this.ID) + 1);
      } else {
         var1.MetCount.put(this.ID, 1);
      }

   }

   public boolean hasObservation(String var1) {
      for(int var2 = 0; var2 < this.Observations.size(); ++var2) {
         if (var1.equals(((ObservationFactory.Observation)this.Observations.get(var2)).getTraitID())) {
            return true;
         }
      }

      return false;
   }

   private void savePerk(ByteBuffer var1, PerkFactory.Perk var2) throws IOException {
      GameWindow.WriteStringUTF(var1, var2 == null ? "" : var2.getId());
   }

   private PerkFactory.Perk loadPerk(ByteBuffer var1, int var2) throws IOException {
      PerkFactory.Perk var4;
      if (var2 >= 152) {
         String var5 = GameWindow.ReadStringUTF(var1);
         var4 = PerkFactory.Perks.FromString(var5);
         return var4 == PerkFactory.Perks.MAX ? null : var4;
      } else {
         int var3 = var1.getInt();
         if (var3 >= 0 && var3 < PerkFactory.Perks.MAX.index()) {
            var4 = PerkFactory.Perks.fromIndex(var3);
            return var4 == PerkFactory.Perks.MAX ? null : var4;
         } else {
            return null;
         }
      }
   }

   public void load(ByteBuffer var1, int var2, IsoGameCharacter var3) throws IOException {
      this.ID = var1.getInt();
      IsoWorld.instance.SurvivorDescriptors.put(this.ID, this);
      this.forename = GameWindow.ReadString(var1);
      this.surname = GameWindow.ReadString(var1);
      this.torso = GameWindow.ReadString(var1);
      this.bFemale = var1.getInt() == 1;
      this.Profession = GameWindow.ReadString(var1);
      this.doStats();
      if (IDCount < this.ID) {
         IDCount = this.ID;
      }

      this.extra.clear();
      int var4;
      int var5;
      if (var1.getInt() == 1) {
         var4 = var1.getInt();

         for(var5 = 0; var5 < var4; ++var5) {
            String var6 = GameWindow.ReadString(var1);
            this.extra.add(var6);
         }
      }

      var4 = var1.getInt();

      for(var5 = 0; var5 < var4; ++var5) {
         PerkFactory.Perk var8 = this.loadPerk(var1, var2);
         int var7 = var1.getInt();
         if (var8 != null) {
            this.getXPBoostMap().put(var8, var7);
         }
      }

      this.Instance = var3;
   }

   public void save(ByteBuffer var1) throws IOException {
      var1.putInt(this.ID);
      GameWindow.WriteString(var1, this.forename);
      GameWindow.WriteString(var1, this.surname);
      GameWindow.WriteString(var1, this.torso);
      var1.putInt(this.bFemale ? 1 : 0);
      GameWindow.WriteString(var1, this.Profession);
      if (!this.extra.isEmpty()) {
         var1.putInt(1);
         var1.putInt(this.extra.size());

         for(int var2 = 0; var2 < this.extra.size(); ++var2) {
            String var3 = (String)this.extra.get(var2);
            GameWindow.WriteString(var1, var3);
         }
      } else {
         var1.putInt(0);
      }

      var1.putInt(this.getXPBoostMap().size());
      Iterator var4 = this.getXPBoostMap().entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         this.savePerk(var1, (PerkFactory.Perk)var5.getKey());
         var1.putInt((Integer)var5.getValue());
      }

   }

   public void loadCompact(ByteBuffer var1) {
      this.ID = -1;
      this.torso = GameWindow.ReadString(var1);
      this.bFemale = var1.get() == 1;
      this.extra.clear();
      if (var1.get() == 1) {
         byte var2 = var1.get();

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = GameWindow.ReadString(var1);
            this.extra.add(var4);
         }
      }

   }

   public void saveCompact(ByteBuffer var1) throws UnsupportedEncodingException {
      GameWindow.WriteString(var1, this.torso);
      var1.put((byte)(this.bFemale ? 1 : 0));
      if (!this.extra.isEmpty()) {
         var1.put((byte)1);
         var1.put((byte)this.extra.size());
         Iterator var2 = this.extra.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            GameWindow.WriteString(var1, var3);
         }
      } else {
         var1.put((byte)0);
      }

   }

   public void addObservation(String var1) {
      ObservationFactory.Observation var2 = ObservationFactory.getObservation(var1);
      if (var2 != null) {
         this.Observations.add(var2);
      }
   }

   private void doStats() {
      this.bravery = Rand.Next(2) == 0 ? 10.0F : 0.0F;
      this.aggressiveness = Rand.Next(2) == 0 ? 10.0F : 0.0F;
      this.compassion = 10.0F - this.aggressiveness;
      this.loner = Rand.Next(2) == 0 ? 10.0F : 0.0F;
      this.temper = Rand.Next(2) == 0 ? 10.0F : 0.0F;
      this.friendliness = 10.0F - this.loner;
      this.favourindoors = Rand.Next(2) == 0 ? 10.0F : 0.0F;
      this.loyalty = Rand.Next(2) == 0 ? 10.0F : 0.0F;
   }

   public int getMetCount(SurvivorDesc var1) {
      return this.MetCount.containsKey(var1.ID) ? (Integer)this.MetCount.get(var1.ID) : 0;
   }

   public String getForename() {
      return this.forename;
   }

   public void setForename(String var1) {
      this.forename = var1;
   }

   public int getID() {
      return this.ID;
   }

   public void setID(int var1) {
      this.ID = var1;
   }

   public IsoGameCharacter getInstance() {
      return this.Instance;
   }

   public void setInstance(IsoGameCharacter var1) {
      this.Instance = var1;
   }

   public String getSurname() {
      return this.surname;
   }

   public void setSurname(String var1) {
      this.surname = var1;
   }

   public String getInventoryScript() {
      return this.InventoryScript;
   }

   public void setInventoryScript(String var1) {
      this.InventoryScript = var1;
   }

   public String getTorso() {
      return this.torso;
   }

   public void setTorso(String var1) {
      this.torso = var1;
   }

   public HashMap getMetCount() {
      return this.MetCount;
   }

   public float getBravery() {
      return this.bravery;
   }

   public void setBravery(float var1) {
      this.bravery = var1;
   }

   public float getLoner() {
      return this.loner;
   }

   public void setLoner(float var1) {
      this.loner = var1;
   }

   public float getAggressiveness() {
      return this.aggressiveness;
   }

   public void setAggressiveness(float var1) {
      this.aggressiveness = var1;
   }

   public float getCompassion() {
      return this.compassion;
   }

   public void setCompassion(float var1) {
      this.compassion = var1;
   }

   public float getTemper() {
      return this.temper;
   }

   public void setTemper(float var1) {
      this.temper = var1;
   }

   public float getFriendliness() {
      return this.friendliness;
   }

   public void setFriendliness(float var1) {
      this.friendliness = var1;
   }

   public float getFavourindoors() {
      return this.favourindoors;
   }

   public void setFavourindoors(float var1) {
      this.favourindoors = var1;
   }

   public float getLoyalty() {
      return this.loyalty;
   }

   public void setLoyalty(float var1) {
      this.loyalty = var1;
   }

   public String getProfession() {
      return this.Profession;
   }

   public void setProfession(String var1) {
      this.Profession = var1;
   }

   public boolean isAggressive() {
      Iterator var1 = this.Observations.iterator();

      ObservationFactory.Observation var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (ObservationFactory.Observation)var1.next();
      } while(!"Aggressive".equals(var2.getTraitID()));

      return true;
   }

   public ArrayList getObservations() {
      return this.Observations;
   }

   public boolean isFriendly() {
      Iterator var1 = this.Observations.iterator();

      ObservationFactory.Observation var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (ObservationFactory.Observation)var1.next();
      } while(!"Friendly".equals(var2.getTraitID()));

      return true;
   }

   public SurvivorFactory.SurvivorType getType() {
      return this.type;
   }

   public void setType(SurvivorFactory.SurvivorType var1) {
      this.type = var1;
   }

   public void setFemale(boolean var1) {
      this.bFemale = var1;
   }

   public ArrayList getExtras() {
      return this.extra;
   }

   public ArrayList getCommonHairColor() {
      return HairCommonColors;
   }

   public static void addTrouserColor(ColorInfo var0) {
      TrouserCommonColors.add(var0.toColor());
   }

   public static void addHairColor(ColorInfo var0) {
      HairCommonColors.add(var0.toImmutableColor());
   }

   public static Color getRandomSkinColor() {
      return OutfitRNG.Next(3) == 0 ? new Color(OutfitRNG.Next(0.5F, 0.6F), OutfitRNG.Next(0.3F, 0.4F), OutfitRNG.Next(0.15F, 0.23F)) : new Color(OutfitRNG.Next(0.9F, 1.0F), OutfitRNG.Next(0.75F, 0.88F), OutfitRNG.Next(0.45F, 0.58F));
   }
}
