package zombie.inventory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Predicate;
import se.krka.kahlua.integration.LuaReturn;
import se.krka.kahlua.vm.LuaClosure;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.SandboxOptions;
import zombie.SystemDisabler;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.SurvivorDesc;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.inventory.types.AlarmClock;
import zombie.inventory.types.AlarmClockClothing;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.Drainable;
import zombie.inventory.types.DrainableComboItem;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.InventoryContainer;
import zombie.inventory.types.Key;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoBarbecue;
import zombie.iso.objects.IsoCompost;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoFireplace;
import zombie.iso.objects.IsoMannequin;
import zombie.iso.objects.IsoStove;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.network.GameClient;
import zombie.network.PacketTypes;
import zombie.popman.ObjectPool;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.VehiclePart;

public final class ItemContainer {
   private static final ArrayList tempList = new ArrayList();
   private static final ArrayList s_tempObjects = new ArrayList();
   public boolean active = false;
   private boolean dirty = true;
   public boolean IsDevice = false;
   public float ageFactor = 1.0F;
   public float CookingFactor = 1.0F;
   public int Capacity = 50;
   public InventoryItem containingItem = null;
   public ArrayList Items = new ArrayList();
   public ArrayList IncludingObsoleteItems = new ArrayList();
   public IsoObject parent = null;
   public IsoGridSquare SourceGrid = null;
   public VehiclePart vehiclePart = null;
   public InventoryContainer inventoryContainer = null;
   public boolean bExplored = false;
   public String type = "none";
   public int ID = 0;
   private boolean drawDirty = true;
   private float customTemperature = 0.0F;
   private boolean hasBeenLooted = false;
   private String openSound = null;
   private String closeSound = null;
   private String putSound = null;
   private String OnlyAcceptCategory = null;
   private String AcceptItemFunction = null;
   private int weightReduction = 0;
   private String containerPosition = null;
   private String freezerPosition = null;
   private static final ThreadLocal TL_comparators = ThreadLocal.withInitial(ItemContainer.Comparators::new);
   private static final ThreadLocal TL_itemListPool = ThreadLocal.withInitial(ItemContainer.InventoryItemListPool::new);
   private static final ThreadLocal TL_predicates = ThreadLocal.withInitial(ItemContainer.Predicates::new);

   public ItemContainer(int var1, String var2, IsoGridSquare var3, IsoObject var4) {
      this.ID = var1;
      this.parent = var4;
      this.type = var2;
      this.SourceGrid = var3;
      if (var2.equals("fridge")) {
         this.ageFactor = 0.02F;
         this.CookingFactor = 0.0F;
      }

   }

   public ItemContainer(String var1, IsoGridSquare var2, IsoObject var3) {
      this.ID = -1;
      this.parent = var3;
      this.type = var1;
      this.SourceGrid = var2;
      if (var1.equals("fridge")) {
         this.ageFactor = 0.02F;
         this.CookingFactor = 0.0F;
      }

   }

   public ItemContainer(int var1) {
      this.ID = var1;
   }

   public ItemContainer() {
      this.ID = -1;
   }

   public static float floatingPointCorrection(float var0) {
      byte var1 = 100;
      float var2 = var0 * (float)var1;
      return (float)((int)(var2 - (float)((int)var2) >= 0.5F ? var2 + 1.0F : var2)) / (float)var1;
   }

   public int getCapacity() {
      return this.Capacity;
   }

   public InventoryItem FindAndReturnWaterItem(int var1) {
      for(int var2 = 0; var2 < this.getItems().size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.getItems().get(var2);
         if (var3 instanceof DrainableComboItem && var3.isWaterSource()) {
            DrainableComboItem var4 = (DrainableComboItem)var3;
            if (var4.getDrainableUsesInt() >= var1) {
               return var3;
            }
         }
      }

      return null;
   }

   public InventoryItem getItemFromTypeRecurse(String var1) {
      return this.getFirstTypeRecurse(var1);
   }

   public int getEffectiveCapacity(IsoGameCharacter var1) {
      if (var1 != null && !(this.parent instanceof IsoGameCharacter) && !(this.parent instanceof IsoDeadBody) && !"floor".equals(this.getType())) {
         if (var1.Traits.Organized.isSet()) {
            return (int)Math.max((float)this.Capacity * 1.3F, (float)(this.Capacity + 1));
         }

         if (var1.Traits.Disorganized.isSet()) {
            return (int)Math.max((float)this.Capacity * 0.7F, 1.0F);
         }
      }

      return this.Capacity;
   }

   public boolean hasRoomFor(IsoGameCharacter var1, InventoryItem var2) {
      if (this.vehiclePart != null && this.vehiclePart.getId().contains("Seat") && this.Items.isEmpty()) {
         return true;
      } else if (floatingPointCorrection(this.getCapacityWeight()) + var2.getUnequippedWeight() <= (float)this.getEffectiveCapacity(var1)) {
         if (this.getContainingItem() != null && this.getContainingItem().getEquipParent() != null && this.getContainingItem().getEquipParent().getInventory() != null && !this.getContainingItem().getEquipParent().getInventory().contains(var2)) {
            return floatingPointCorrection(this.getContainingItem().getEquipParent().getInventory().getCapacityWeight()) + var2.getUnequippedWeight() <= (float)this.getContainingItem().getEquipParent().getInventory().getEffectiveCapacity(var1);
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean hasRoomFor(IsoGameCharacter var1, float var2) {
      return floatingPointCorrection(this.getCapacityWeight()) + var2 <= (float)this.getEffectiveCapacity(var1);
   }

   public boolean isItemAllowed(InventoryItem var1) {
      if (var1 == null) {
         return false;
      } else {
         String var2 = this.getOnlyAcceptCategory();
         if (var2 != null && !var2.equalsIgnoreCase(var1.getCategory())) {
            return false;
         } else {
            String var3 = this.getAcceptItemFunction();
            if (var3 != null) {
               Object var4 = LuaManager.getFunctionObject(var3);
               if (var4 != null) {
                  Boolean var5 = LuaManager.caller.protectedCallBoolean(LuaManager.thread, var4, this, var1);
                  if (var5 != Boolean.TRUE) {
                     return false;
                  }
               }
            }

            if (this.parent != null && !this.parent.isItemAllowedInContainer(this, var1)) {
               return false;
            } else {
               return !this.getType().equals("clothingrack") || var1 instanceof Clothing;
            }
         }
      }
   }

   public boolean isRemoveItemAllowed(InventoryItem var1) {
      if (var1 == null) {
         return false;
      } else {
         return this.parent == null || this.parent.isRemoveItemAllowedFromContainer(this, var1);
      }
   }

   public boolean isExplored() {
      return this.bExplored;
   }

   public void setExplored(boolean var1) {
      this.bExplored = var1;
   }

   public boolean isInCharacterInventory(IsoGameCharacter var1) {
      if (var1.getInventory() == this) {
         return true;
      } else {
         if (this.containingItem != null) {
            if (var1.getInventory().contains(this.containingItem, true)) {
               return true;
            }

            if (this.containingItem.getContainer() != null) {
               return this.containingItem.getContainer().isInCharacterInventory(var1);
            }
         }

         return false;
      }
   }

   public boolean isInside(InventoryItem var1) {
      if (this.containingItem == null) {
         return false;
      } else if (this.containingItem == var1) {
         return true;
      } else {
         return this.containingItem.getContainer() != null && this.containingItem.getContainer().isInside(var1);
      }
   }

   public InventoryItem getContainingItem() {
      return this.containingItem;
   }

   public InventoryItem DoAddItem(InventoryItem var1) {
      return this.AddItem(var1);
   }

   public InventoryItem DoAddItemBlind(InventoryItem var1) {
      return this.AddItem(var1);
   }

   public ArrayList AddItems(String var1, int var2) {
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var2; ++var4) {
         InventoryItem var5 = this.AddItem(var1);
         if (var5 != null) {
            var3.add(var5);
         }
      }

      return var3;
   }

   public void AddItems(InventoryItem var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         this.AddItem(var1.getFullType());
      }

   }

   public int getNumberOfItem(String var1, boolean var2) {
      return this.getNumberOfItem(var1, var2, false);
   }

   public int getNumberOfItem(String var1) {
      return this.getNumberOfItem(var1, false);
   }

   public int getNumberOfItem(String var1, boolean var2, ArrayList var3) {
      int var4 = this.getNumberOfItem(var1, var2);
      if (var3 != null) {
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            ItemContainer var6 = (ItemContainer)var5.next();
            if (var6 != this) {
               var4 += var6.getNumberOfItem(var1, var2);
            }
         }
      }

      return var4;
   }

   public int getNumberOfItem(String var1, boolean var2, boolean var3) {
      int var4 = 0;

      for(int var5 = 0; var5 < this.Items.size(); ++var5) {
         InventoryItem var6 = (InventoryItem)this.Items.get(var5);
         if (!var6.getFullType().equals(var1) && !var6.getType().equals(var1)) {
            if (var3 && var6 instanceof InventoryContainer) {
               var4 += ((InventoryContainer)var6).getItemContainer().getNumberOfItem(var1);
            } else if (var2 && var6 instanceof DrainableComboItem && ((DrainableComboItem)var6).getReplaceOnDeplete() != null) {
               DrainableComboItem var7 = (DrainableComboItem)var6;
               if (var7.getReplaceOnDepleteFullType().equals(var1) || var7.getReplaceOnDeplete().equals(var1)) {
                  ++var4;
               }
            }
         } else {
            ++var4;
         }
      }

      return var4;
   }

   public InventoryItem addItem(InventoryItem var1) {
      return this.AddItem(var1);
   }

   public InventoryItem AddItem(InventoryItem var1) {
      if (var1 == null) {
         return null;
      } else if (this.containsID(var1.id)) {
         System.out.println("Error, container already has id");
         return this.getItemWithID(var1.id);
      } else {
         this.drawDirty = true;
         if (this.parent != null) {
            this.dirty = true;
         }

         if (this.parent != null && !(this.parent instanceof IsoGameCharacter)) {
            this.parent.DirtySlice();
         }

         if (var1.container != null) {
            var1.container.Remove(var1);
         }

         var1.container = this;
         this.Items.add(var1);
         if (IsoWorld.instance.CurrentCell != null) {
            IsoWorld.instance.CurrentCell.addToProcessItems(var1);
         }

         return var1;
      }
   }

   public InventoryItem AddItemBlind(InventoryItem var1) {
      if (var1 == null) {
         return null;
      } else if (var1.getWeight() + this.getCapacityWeight() > (float)this.getCapacity()) {
         return null;
      } else {
         if (this.parent != null && !(this.parent instanceof IsoGameCharacter)) {
            this.parent.DirtySlice();
         }

         this.Items.add(var1);
         return var1;
      }
   }

   public InventoryItem AddItem(String var1) {
      this.drawDirty = true;
      if (this.parent != null && !(this.parent instanceof IsoGameCharacter)) {
         this.dirty = true;
      }

      Item var2 = ScriptManager.instance.FindItem(var1);
      if (var2 == null) {
         DebugLog.log("ERROR: ItemContainer.AddItem: can't find " + var1);
         return null;
      } else if (var2.OBSOLETE) {
         return null;
      } else {
         InventoryItem var3 = null;
         int var4 = var2.getCount();

         for(int var5 = 0; var5 < var4; ++var5) {
            var3 = InventoryItemFactory.CreateItem(var1);
            if (var3 == null) {
               return null;
            }

            var3.container = this;
            this.Items.add(var3);
            if (var3 instanceof Food) {
               ((Food)var3).setHeat(this.getTemprature());
            }

            if (IsoWorld.instance.CurrentCell != null) {
               IsoWorld.instance.CurrentCell.addToProcessItems(var3);
            }
         }

         return var3;
      }
   }

   public boolean AddItem(String var1, float var2) {
      this.drawDirty = true;
      if (this.parent != null && !(this.parent instanceof IsoGameCharacter)) {
         this.dirty = true;
      }

      InventoryItem var3 = InventoryItemFactory.CreateItem(var1);
      if (var3 == null) {
         return false;
      } else {
         if (var3 instanceof Drainable) {
            ((Drainable)var3).setUsedDelta(var2);
         }

         var3.container = this;
         this.Items.add(var3);
         return true;
      }
   }

   public boolean contains(InventoryItem var1) {
      return this.Items.contains(var1);
   }

   public boolean containsWithModule(String var1) {
      return this.containsWithModule(var1, false);
   }

   public boolean containsWithModule(String var1, boolean var2) {
      String var3 = var1;
      String var4 = "Base";
      if (var1.contains(".")) {
         var4 = var1.split("\\.")[0];
         var3 = var1.split("\\.")[1];
      }

      for(int var5 = 0; var5 < this.Items.size(); ++var5) {
         InventoryItem var6 = (InventoryItem)this.Items.get(var5);
         if (var6 == null) {
            this.Items.remove(var5);
            --var5;
         } else if (var6.type.equals(var3.trim()) && var4.equals(var6.getModule()) && (!var2 || !(var6 instanceof DrainableComboItem) || !(((DrainableComboItem)var6).getUsedDelta() <= 0.0F))) {
            return true;
         }
      }

      return false;
   }

   public void removeItemOnServer(InventoryItem var1) {
      if (GameClient.bClient) {
         if (this.containingItem != null && this.containingItem.getWorldItem() != null) {
            GameClient.instance.addToItemRemoveSendBuffer(this.containingItem.getWorldItem(), this, var1);
         } else {
            GameClient.instance.addToItemRemoveSendBuffer(this.parent, this, var1);
         }
      }

   }

   public void addItemOnServer(InventoryItem var1) {
      if (GameClient.bClient) {
         if (this.containingItem != null && this.containingItem.getWorldItem() != null) {
            GameClient.instance.addToItemSendBuffer(this.containingItem.getWorldItem(), this, var1);
         } else {
            GameClient.instance.addToItemSendBuffer(this.parent, this, var1);
         }
      }

   }

   public boolean contains(InventoryItem var1, boolean var2) {
      ItemContainer.InventoryItemList var3 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();

      int var4;
      for(var4 = 0; var4 < this.Items.size(); ++var4) {
         InventoryItem var5 = (InventoryItem)this.Items.get(var4);
         if (var5 == null) {
            this.Items.remove(var4);
            --var4;
         } else {
            if (var5 == var1) {
               ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var3);
               return true;
            }

            if (var2 && var5 instanceof InventoryContainer && ((InventoryContainer)var5).getInventory() != null && !var3.contains(var5)) {
               var3.add(var5);
            }
         }
      }

      for(var4 = 0; var4 < var3.size(); ++var4) {
         ItemContainer var6 = ((InventoryContainer)var3.get(var4)).getInventory();
         if (var6.contains(var1, var2)) {
            ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var3);
            return true;
         }
      }

      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var3);
      return false;
   }

   public boolean contains(String var1, boolean var2) {
      return this.contains(var1, var2, false);
   }

   public boolean containsType(String var1) {
      return this.contains(var1, false, false);
   }

   public boolean containsTypeRecurse(String var1) {
      return this.contains(var1, true, false);
   }

   private boolean testBroken(boolean var1, InventoryItem var2) {
      if (!var1) {
         return true;
      } else {
         return !var2.isBroken();
      }
   }

   public boolean contains(String var1, boolean var2, boolean var3) {
      ItemContainer.InventoryItemList var4 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();
      int var5;
      InventoryItem var6;
      if (var1.contains("Type:")) {
         for(var5 = 0; var5 < this.Items.size(); ++var5) {
            var6 = (InventoryItem)this.Items.get(var5);
            if (var1.contains("Food") && var6 instanceof Food) {
               ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var4);
               return true;
            }

            if (var1.contains("Weapon") && var6 instanceof HandWeapon && this.testBroken(var3, var6)) {
               ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var4);
               return true;
            }

            if (var1.contains("AlarmClock") && var6 instanceof AlarmClock) {
               ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var4);
               return true;
            }

            if (var1.contains("AlarmClockClothing") && var6 instanceof AlarmClockClothing) {
               ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var4);
               return true;
            }

            if (var2 && var6 instanceof InventoryContainer && ((InventoryContainer)var6).getInventory() != null && !var4.contains(var6)) {
               var4.add(var6);
            }
         }
      } else if (var1.contains("/")) {
         String[] var12 = var1.split("/");
         String[] var13 = var12;
         int var7 = var12.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var13[var8];

            for(int var10 = 0; var10 < this.Items.size(); ++var10) {
               InventoryItem var11 = (InventoryItem)this.Items.get(var10);
               if (compareType(var9.trim(), var11) && this.testBroken(var3, var11)) {
                  ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var4);
                  return true;
               }

               if (var2 && var11 instanceof InventoryContainer && ((InventoryContainer)var11).getInventory() != null && !var4.contains(var11)) {
                  var4.add(var11);
               }
            }
         }
      } else {
         for(var5 = 0; var5 < this.Items.size(); ++var5) {
            var6 = (InventoryItem)this.Items.get(var5);
            if (var6 == null) {
               this.Items.remove(var5);
               --var5;
            } else {
               if (compareType(var1.trim(), var6) && this.testBroken(var3, var6)) {
                  ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var4);
                  return true;
               }

               if (var2 && var6 instanceof InventoryContainer && ((InventoryContainer)var6).getInventory() != null && !var4.contains(var6)) {
                  var4.add(var6);
               }
            }
         }
      }

      for(var5 = 0; var5 < var4.size(); ++var5) {
         ItemContainer var14 = ((InventoryContainer)var4.get(var5)).getInventory();
         if (var14.contains(var1, var2, var3)) {
            ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var4);
            return true;
         }
      }

      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var4);
      return false;
   }

   public boolean contains(String var1) {
      return this.contains(var1, false);
   }

   private static InventoryItem getBestOf(ItemContainer.InventoryItemList var0, Comparator var1) {
      if (var0 != null && !var0.isEmpty()) {
         InventoryItem var2 = (InventoryItem)var0.get(0);

         for(int var3 = 1; var3 < var0.size(); ++var3) {
            InventoryItem var4 = (InventoryItem)var0.get(var3);
            if (var1.compare(var4, var2) > 0) {
               var2 = var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public InventoryItem getBest(Predicate var1, Comparator var2) {
      ItemContainer.InventoryItemList var3 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();
      this.getAll(var1, var3);
      InventoryItem var4 = getBestOf(var3, var2);
      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var3);
      return var4;
   }

   public InventoryItem getBestRecurse(Predicate var1, Comparator var2) {
      ItemContainer.InventoryItemList var3 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();
      this.getAllRecurse(var1, var3);
      InventoryItem var4 = getBestOf(var3, var2);
      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var3);
      return var4;
   }

   public InventoryItem getBestType(String var1, Comparator var2) {
      ItemContainer.TypePredicate var3 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);

      InventoryItem var4;
      try {
         var4 = this.getBest(var3, var2);
      } finally {
         ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var3);
      }

      return var4;
   }

   public InventoryItem getBestTypeRecurse(String var1, Comparator var2) {
      ItemContainer.TypePredicate var3 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);

      InventoryItem var4;
      try {
         var4 = this.getBestRecurse(var3, var2);
      } finally {
         ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var3);
      }

      return var4;
   }

   public InventoryItem getBestEval(LuaClosure var1, LuaClosure var2) {
      ItemContainer.EvalPredicate var3 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      ItemContainer.EvalComparator var4 = ((ItemContainer.EvalComparator)((ItemContainer.Comparators)TL_comparators.get()).eval.alloc()).init(var2);

      InventoryItem var5;
      try {
         var5 = this.getBest(var3, var4);
      } finally {
         ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var3);
         ((ItemContainer.Comparators)TL_comparators.get()).eval.release((Object)var4);
      }

      return var5;
   }

   public InventoryItem getBestEvalRecurse(LuaClosure var1, LuaClosure var2) {
      ItemContainer.EvalPredicate var3 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      ItemContainer.EvalComparator var4 = ((ItemContainer.EvalComparator)((ItemContainer.Comparators)TL_comparators.get()).eval.alloc()).init(var2);

      InventoryItem var5;
      try {
         var5 = this.getBestRecurse(var3, var4);
      } finally {
         ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var3);
         ((ItemContainer.Comparators)TL_comparators.get()).eval.release((Object)var4);
      }

      return var5;
   }

   public InventoryItem getBestEvalArg(LuaClosure var1, LuaClosure var2, Object var3) {
      ItemContainer.EvalArgPredicate var4 = ((ItemContainer.EvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).evalArg.alloc()).init(var1, var3);
      ItemContainer.EvalArgComparator var5 = ((ItemContainer.EvalArgComparator)((ItemContainer.Comparators)TL_comparators.get()).evalArg.alloc()).init(var2, var3);

      InventoryItem var6;
      try {
         var6 = this.getBest(var4, var5);
      } finally {
         ((ItemContainer.Predicates)TL_predicates.get()).evalArg.release((Object)var4);
         ((ItemContainer.Comparators)TL_comparators.get()).evalArg.release((Object)var5);
      }

      return var6;
   }

   public InventoryItem getBestEvalArgRecurse(LuaClosure var1, LuaClosure var2, Object var3) {
      ItemContainer.EvalArgPredicate var4 = ((ItemContainer.EvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).evalArg.alloc()).init(var1, var3);
      ItemContainer.EvalArgComparator var5 = ((ItemContainer.EvalArgComparator)((ItemContainer.Comparators)TL_comparators.get()).evalArg.alloc()).init(var2, var3);

      InventoryItem var6;
      try {
         var6 = this.getBestRecurse(var4, var5);
      } finally {
         ((ItemContainer.Predicates)TL_predicates.get()).evalArg.release((Object)var4);
         ((ItemContainer.Comparators)TL_comparators.get()).evalArg.release((Object)var5);
      }

      return var6;
   }

   public InventoryItem getBestTypeEval(String var1, LuaClosure var2) {
      ItemContainer.TypePredicate var3 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      ItemContainer.EvalComparator var4 = ((ItemContainer.EvalComparator)((ItemContainer.Comparators)TL_comparators.get()).eval.alloc()).init(var2);

      InventoryItem var5;
      try {
         var5 = this.getBest(var3, var4);
      } finally {
         ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var3);
         ((ItemContainer.Comparators)TL_comparators.get()).eval.release((Object)var4);
      }

      return var5;
   }

   public InventoryItem getBestTypeEvalRecurse(String var1, LuaClosure var2) {
      ItemContainer.TypePredicate var3 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      ItemContainer.EvalComparator var4 = ((ItemContainer.EvalComparator)((ItemContainer.Comparators)TL_comparators.get()).eval.alloc()).init(var2);

      InventoryItem var5;
      try {
         var5 = this.getBestRecurse(var3, var4);
      } finally {
         ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var3);
         ((ItemContainer.Comparators)TL_comparators.get()).eval.release((Object)var4);
      }

      return var5;
   }

   public InventoryItem getBestTypeEvalArg(String var1, LuaClosure var2, Object var3) {
      ItemContainer.TypePredicate var4 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      ItemContainer.EvalArgComparator var5 = ((ItemContainer.EvalArgComparator)((ItemContainer.Comparators)TL_comparators.get()).evalArg.alloc()).init(var2, var3);

      InventoryItem var6;
      try {
         var6 = this.getBest(var4, var5);
      } finally {
         ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var4);
         ((ItemContainer.Comparators)TL_comparators.get()).evalArg.release((Object)var5);
      }

      return var6;
   }

   public InventoryItem getBestTypeEvalArgRecurse(String var1, LuaClosure var2, Object var3) {
      ItemContainer.TypePredicate var4 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      ItemContainer.EvalArgComparator var5 = ((ItemContainer.EvalArgComparator)((ItemContainer.Comparators)TL_comparators.get()).evalArg.alloc()).init(var2, var3);

      InventoryItem var6;
      try {
         var6 = this.getBestRecurse(var4, var5);
      } finally {
         ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var4);
         ((ItemContainer.Comparators)TL_comparators.get()).evalArg.release((Object)var5);
      }

      return var6;
   }

   public InventoryItem getBestCondition(Predicate var1) {
      ItemContainer.ConditionComparator var2 = (ItemContainer.ConditionComparator)((ItemContainer.Comparators)TL_comparators.get()).condition.alloc();
      InventoryItem var3 = this.getBest(var1, var2);
      ((ItemContainer.Comparators)TL_comparators.get()).condition.release((Object)var2);
      if (var3 != null && var3.getCondition() <= 0) {
         var3 = null;
      }

      return var3;
   }

   public InventoryItem getBestConditionRecurse(Predicate var1) {
      ItemContainer.ConditionComparator var2 = (ItemContainer.ConditionComparator)((ItemContainer.Comparators)TL_comparators.get()).condition.alloc();
      InventoryItem var3 = this.getBestRecurse(var1, var2);
      ((ItemContainer.Comparators)TL_comparators.get()).condition.release((Object)var2);
      if (var3 != null && var3.getCondition() <= 0) {
         var3 = null;
      }

      return var3;
   }

   public InventoryItem getBestCondition(String var1) {
      ItemContainer.TypePredicate var2 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      InventoryItem var3 = this.getBestCondition((Predicate)var2);
      ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var2);
      return var3;
   }

   public InventoryItem getBestConditionRecurse(String var1) {
      ItemContainer.TypePredicate var2 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      InventoryItem var3 = this.getBestConditionRecurse((Predicate)var2);
      ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var2);
      return var3;
   }

   public InventoryItem getBestConditionEval(LuaClosure var1) {
      ItemContainer.EvalPredicate var2 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      InventoryItem var3 = this.getBestCondition((Predicate)var2);
      ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var2);
      return var3;
   }

   public InventoryItem getBestConditionEvalRecurse(LuaClosure var1) {
      ItemContainer.EvalPredicate var2 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      InventoryItem var3 = this.getBestConditionRecurse((Predicate)var2);
      ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var2);
      return var3;
   }

   public InventoryItem getFirstEval(LuaClosure var1) {
      ItemContainer.EvalPredicate var2 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      InventoryItem var3 = this.getFirst(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var2);
      return var3;
   }

   public InventoryItem getFirstEvalArg(LuaClosure var1, Object var2) {
      ItemContainer.EvalArgPredicate var3 = ((ItemContainer.EvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).evalArg.alloc()).init(var1, var2);
      InventoryItem var4 = this.getFirst(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).evalArg.release((Object)var3);
      return var4;
   }

   public boolean containsEval(LuaClosure var1) {
      return this.getFirstEval(var1) != null;
   }

   public boolean containsEvalArg(LuaClosure var1, Object var2) {
      return this.getFirstEvalArg(var1, var2) != null;
   }

   public boolean containsEvalRecurse(LuaClosure var1) {
      return this.getFirstEvalRecurse(var1) != null;
   }

   public boolean containsEvalArgRecurse(LuaClosure var1, Object var2) {
      return this.getFirstEvalArgRecurse(var1, var2) != null;
   }

   public boolean containsTag(String var1) {
      return this.getFirstTag(var1) != null;
   }

   public boolean containsTagEval(String var1, LuaClosure var2) {
      return this.getFirstTagEval(var1, var2) != null;
   }

   public boolean containsTagRecurse(String var1) {
      return this.getFirstTagRecurse(var1) != null;
   }

   public boolean containsTagEvalRecurse(String var1, LuaClosure var2) {
      return this.getFirstTagEvalRecurse(var1, var2) != null;
   }

   public boolean containsTagEvalArgRecurse(String var1, LuaClosure var2, Object var3) {
      return this.getFirstTagEvalArgRecurse(var1, var2, var3) != null;
   }

   public boolean containsTypeEvalRecurse(String var1, LuaClosure var2) {
      return this.getFirstTypeEvalRecurse(var1, var2) != null;
   }

   public boolean containsTypeEvalArgRecurse(String var1, LuaClosure var2, Object var3) {
      return this.getFirstTypeEvalArgRecurse(var1, var2, var3) != null;
   }

   private static boolean compareType(String var0, String var1) {
      if (!var0.contains("/")) {
         return var0.equals(var1);
      } else {
         int var2 = var0.indexOf(var1);
         if (var2 == -1) {
            return false;
         } else {
            char var3 = var2 > 0 ? var0.charAt(var2 - 1) : 0;
            char var4 = var2 + var1.length() < var0.length() ? var0.charAt(var2 + var1.length()) : 0;
            return var3 == 0 && var4 == '/' || var3 == '/' && var4 == 0 || var3 == '/' && var4 == '/';
         }
      }
   }

   private static boolean compareType(String var0, InventoryItem var1) {
      if (var0.indexOf(46) == -1) {
         return compareType(var0, var1.getType());
      } else {
         return compareType(var0, var1.getFullType()) || compareType(var0, var1.getType());
      }
   }

   public InventoryItem getFirst(Predicate var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3 == null) {
            this.Items.remove(var2);
            --var2;
         } else if (var1.test(var3)) {
            return var3;
         }
      }

      return null;
   }

   public InventoryItem getFirstRecurse(Predicate var1) {
      ItemContainer.InventoryItemList var2 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();

      int var3;
      for(var3 = 0; var3 < this.Items.size(); ++var3) {
         InventoryItem var4 = (InventoryItem)this.Items.get(var3);
         if (var4 == null) {
            this.Items.remove(var3);
            --var3;
         } else {
            if (var1.test(var4)) {
               ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var2);
               return var4;
            }

            if (var4 instanceof InventoryContainer) {
               var2.add(var4);
            }
         }
      }

      for(var3 = 0; var3 < var2.size(); ++var3) {
         ItemContainer var6 = ((InventoryContainer)var2.get(var3)).getInventory();
         InventoryItem var5 = var6.getFirstRecurse(var1);
         if (var5 != null) {
            ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var2);
            return var5;
         }
      }

      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var2);
      return null;
   }

   public ArrayList getSome(Predicate var1, int var2, ArrayList var3) {
      for(int var4 = 0; var4 < this.Items.size(); ++var4) {
         InventoryItem var5 = (InventoryItem)this.Items.get(var4);
         if (var5 == null) {
            this.Items.remove(var4);
            --var4;
         } else if (var1.test(var5)) {
            var3.add(var5);
            if (var3.size() >= var2) {
               break;
            }
         }
      }

      return var3;
   }

   public ArrayList getSomeRecurse(Predicate var1, int var2, ArrayList var3) {
      ItemContainer.InventoryItemList var4 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();

      int var5;
      for(var5 = 0; var5 < this.Items.size(); ++var5) {
         InventoryItem var6 = (InventoryItem)this.Items.get(var5);
         if (var6 == null) {
            this.Items.remove(var5);
            --var5;
         } else {
            if (var1.test(var6)) {
               var3.add(var6);
               if (var3.size() >= var2) {
                  ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var4);
                  return var3;
               }
            }

            if (var6 instanceof InventoryContainer) {
               var4.add(var6);
            }
         }
      }

      for(var5 = 0; var5 < var4.size(); ++var5) {
         ItemContainer var7 = ((InventoryContainer)var4.get(var5)).getInventory();
         var7.getSomeRecurse(var1, var2, var3);
         if (var3.size() >= var2) {
            break;
         }
      }

      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var4);
      return var3;
   }

   public ArrayList getAll(Predicate var1, ArrayList var2) {
      for(int var3 = 0; var3 < this.Items.size(); ++var3) {
         InventoryItem var4 = (InventoryItem)this.Items.get(var3);
         if (var4 == null) {
            this.Items.remove(var3);
            --var3;
         } else if (var1.test(var4)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public ArrayList getAllRecurse(Predicate var1, ArrayList var2) {
      ItemContainer.InventoryItemList var3 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();

      int var4;
      for(var4 = 0; var4 < this.Items.size(); ++var4) {
         InventoryItem var5 = (InventoryItem)this.Items.get(var4);
         if (var5 == null) {
            this.Items.remove(var4);
            --var4;
         } else {
            if (var1.test(var5)) {
               var2.add(var5);
            }

            if (var5 instanceof InventoryContainer) {
               var3.add(var5);
            }
         }
      }

      for(var4 = 0; var4 < var3.size(); ++var4) {
         ItemContainer var6 = ((InventoryContainer)var3.get(var4)).getInventory();
         var6.getAllRecurse(var1, var2);
      }

      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var3);
      return var2;
   }

   public int getCount(Predicate var1) {
      ItemContainer.InventoryItemList var2 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();
      this.getAll(var1, var2);
      int var3 = var2.size();
      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var2);
      return var3;
   }

   public int getCountRecurse(Predicate var1) {
      ItemContainer.InventoryItemList var2 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();
      this.getAllRecurse(var1, var2);
      int var3 = var2.size();
      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var2);
      return var3;
   }

   public int getCountTag(String var1) {
      ItemContainer.TagPredicate var2 = ((ItemContainer.TagPredicate)((ItemContainer.Predicates)TL_predicates.get()).tag.alloc()).init(var1);
      int var3 = this.getCount(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).tag.release((Object)var2);
      return var3;
   }

   public int getCountTagEval(String var1, LuaClosure var2) {
      ItemContainer.TagEvalPredicate var3 = ((ItemContainer.TagEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEval.alloc()).init(var1, var2);
      int var4 = this.getCount(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEval.release((Object)var3);
      return var4;
   }

   public int getCountTagEvalArg(String var1, LuaClosure var2, Object var3) {
      ItemContainer.TagEvalArgPredicate var4 = ((ItemContainer.TagEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.alloc()).init(var1, var2, var3);
      int var5 = this.getCount(var4);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.release((Object)var4);
      return var5;
   }

   public int getCountTagRecurse(String var1) {
      ItemContainer.TagPredicate var2 = ((ItemContainer.TagPredicate)((ItemContainer.Predicates)TL_predicates.get()).tag.alloc()).init(var1);
      int var3 = this.getCountRecurse(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).tag.release((Object)var2);
      return var3;
   }

   public int getCountTagEvalRecurse(String var1, LuaClosure var2) {
      ItemContainer.TagEvalPredicate var3 = ((ItemContainer.TagEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEval.alloc()).init(var1, var2);
      int var4 = this.getCountRecurse(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEval.release((Object)var3);
      return var4;
   }

   public int getCountTagEvalArgRecurse(String var1, LuaClosure var2, Object var3) {
      ItemContainer.TagEvalArgPredicate var4 = ((ItemContainer.TagEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.alloc()).init(var1, var2, var3);
      int var5 = this.getCountRecurse(var4);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.release((Object)var4);
      return var5;
   }

   public int getCountType(String var1) {
      ItemContainer.TypePredicate var2 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      int var3 = this.getCount(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var2);
      return var3;
   }

   public int getCountTypeEval(String var1, LuaClosure var2) {
      ItemContainer.TypeEvalPredicate var3 = ((ItemContainer.TypeEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEval.alloc()).init(var1, var2);
      int var4 = this.getCount(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEval.release((Object)var3);
      return var4;
   }

   public int getCountTypeEvalArg(String var1, LuaClosure var2, Object var3) {
      ItemContainer.TypeEvalArgPredicate var4 = ((ItemContainer.TypeEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.alloc()).init(var1, var2, var3);
      int var5 = this.getCount(var4);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.release((Object)var4);
      return var5;
   }

   public int getCountTypeRecurse(String var1) {
      ItemContainer.TypePredicate var2 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      int var3 = this.getCountRecurse(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var2);
      return var3;
   }

   public int getCountTypeEvalRecurse(String var1, LuaClosure var2) {
      ItemContainer.TypeEvalPredicate var3 = ((ItemContainer.TypeEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEval.alloc()).init(var1, var2);
      int var4 = this.getCountRecurse(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEval.release((Object)var3);
      return var4;
   }

   public int getCountTypeEvalArgRecurse(String var1, LuaClosure var2, Object var3) {
      ItemContainer.TypeEvalArgPredicate var4 = ((ItemContainer.TypeEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.alloc()).init(var1, var2, var3);
      int var5 = this.getCountRecurse(var4);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.release((Object)var4);
      return var5;
   }

   public int getCountEval(LuaClosure var1) {
      ItemContainer.EvalPredicate var2 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      int var3 = this.getCount(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var2);
      return var3;
   }

   public int getCountEvalArg(LuaClosure var1, Object var2) {
      ItemContainer.EvalArgPredicate var3 = ((ItemContainer.EvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).evalArg.alloc()).init(var1, var2);
      int var4 = this.getCount(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).evalArg.release((Object)var3);
      return var4;
   }

   public int getCountEvalRecurse(LuaClosure var1) {
      ItemContainer.EvalPredicate var2 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      int var3 = this.getCountRecurse(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var2);
      return var3;
   }

   public int getCountEvalArgRecurse(LuaClosure var1, Object var2) {
      ItemContainer.EvalArgPredicate var3 = ((ItemContainer.EvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).evalArg.alloc()).init(var1, var2);
      int var4 = this.getCountRecurse(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).evalArg.release((Object)var3);
      return var4;
   }

   public InventoryItem getFirstCategory(String var1) {
      ItemContainer.CategoryPredicate var2 = ((ItemContainer.CategoryPredicate)((ItemContainer.Predicates)TL_predicates.get()).category.alloc()).init(var1);
      InventoryItem var3 = this.getFirst(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).category.release((Object)var2);
      return var3;
   }

   public InventoryItem getFirstCategoryRecurse(String var1) {
      ItemContainer.CategoryPredicate var2 = ((ItemContainer.CategoryPredicate)((ItemContainer.Predicates)TL_predicates.get()).category.alloc()).init(var1);
      InventoryItem var3 = this.getFirstRecurse(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).category.release((Object)var2);
      return var3;
   }

   public InventoryItem getFirstEvalRecurse(LuaClosure var1) {
      ItemContainer.EvalPredicate var2 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      InventoryItem var3 = this.getFirstRecurse(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var2);
      return var3;
   }

   public InventoryItem getFirstEvalArgRecurse(LuaClosure var1, Object var2) {
      ItemContainer.EvalArgPredicate var3 = ((ItemContainer.EvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).evalArg.alloc()).init(var1, var2);
      InventoryItem var4 = this.getFirstRecurse(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).evalArg.release((Object)var3);
      return var4;
   }

   public InventoryItem getFirstTag(String var1) {
      ItemContainer.TagPredicate var2 = ((ItemContainer.TagPredicate)((ItemContainer.Predicates)TL_predicates.get()).tag.alloc()).init(var1);
      InventoryItem var3 = this.getFirst(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).tag.release((Object)var2);
      return var3;
   }

   public InventoryItem getFirstTagRecurse(String var1) {
      ItemContainer.TagPredicate var2 = ((ItemContainer.TagPredicate)((ItemContainer.Predicates)TL_predicates.get()).tag.alloc()).init(var1);
      InventoryItem var3 = this.getFirstRecurse(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).tag.release((Object)var2);
      return var3;
   }

   public InventoryItem getFirstTagEval(String var1, LuaClosure var2) {
      ItemContainer.TagEvalPredicate var3 = ((ItemContainer.TagEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEval.alloc()).init(var1, var2);
      InventoryItem var4 = this.getFirstRecurse(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEval.release((Object)var3);
      return var4;
   }

   public InventoryItem getFirstTagEvalRecurse(String var1, LuaClosure var2) {
      ItemContainer.TagEvalPredicate var3 = ((ItemContainer.TagEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEval.alloc()).init(var1, var2);
      InventoryItem var4 = this.getFirstRecurse(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEval.release((Object)var3);
      return var4;
   }

   public InventoryItem getFirstTagEvalArgRecurse(String var1, LuaClosure var2, Object var3) {
      ItemContainer.TagEvalArgPredicate var4 = ((ItemContainer.TagEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.alloc()).init(var1, var2, var3);
      InventoryItem var5 = this.getFirstRecurse(var4);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.release((Object)var4);
      return var5;
   }

   public InventoryItem getFirstType(String var1) {
      ItemContainer.TypePredicate var2 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      InventoryItem var3 = this.getFirst(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var2);
      return var3;
   }

   public InventoryItem getFirstTypeRecurse(String var1) {
      ItemContainer.TypePredicate var2 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      InventoryItem var3 = this.getFirstRecurse(var2);
      ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var2);
      return var3;
   }

   public InventoryItem getFirstTypeEval(String var1, LuaClosure var2) {
      ItemContainer.TypeEvalPredicate var3 = ((ItemContainer.TypeEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEval.alloc()).init(var1, var2);
      InventoryItem var4 = this.getFirstRecurse(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEval.release((Object)var3);
      return var4;
   }

   public InventoryItem getFirstTypeEvalRecurse(String var1, LuaClosure var2) {
      ItemContainer.TypeEvalPredicate var3 = ((ItemContainer.TypeEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEval.alloc()).init(var1, var2);
      InventoryItem var4 = this.getFirstRecurse(var3);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEval.release((Object)var3);
      return var4;
   }

   public InventoryItem getFirstTypeEvalArgRecurse(String var1, LuaClosure var2, Object var3) {
      ItemContainer.TypeEvalArgPredicate var4 = ((ItemContainer.TypeEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.alloc()).init(var1, var2, var3);
      InventoryItem var5 = this.getFirstRecurse(var4);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.release((Object)var4);
      return var5;
   }

   public ArrayList getSomeCategory(String var1, int var2, ArrayList var3) {
      ItemContainer.CategoryPredicate var4 = ((ItemContainer.CategoryPredicate)((ItemContainer.Predicates)TL_predicates.get()).category.alloc()).init(var1);
      ArrayList var5 = this.getSome(var4, var2, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).category.release((Object)var4);
      return var5;
   }

   public ArrayList getSomeCategoryRecurse(String var1, int var2, ArrayList var3) {
      ItemContainer.CategoryPredicate var4 = ((ItemContainer.CategoryPredicate)((ItemContainer.Predicates)TL_predicates.get()).category.alloc()).init(var1);
      ArrayList var5 = this.getSomeRecurse(var4, var2, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).category.release((Object)var4);
      return var5;
   }

   public ArrayList getSomeTag(String var1, int var2, ArrayList var3) {
      ItemContainer.TagPredicate var4 = ((ItemContainer.TagPredicate)((ItemContainer.Predicates)TL_predicates.get()).tag.alloc()).init(var1);
      ArrayList var5 = this.getSome(var4, var2, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).tag.release((Object)var4);
      return var5;
   }

   public ArrayList getSomeTagEval(String var1, LuaClosure var2, int var3, ArrayList var4) {
      ItemContainer.TagEvalPredicate var5 = ((ItemContainer.TagEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEval.alloc()).init(var1, var2);
      ArrayList var6 = this.getSome(var5, var3, var4);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEval.release((Object)var5);
      return var6;
   }

   public ArrayList getSomeTagEvalArg(String var1, LuaClosure var2, Object var3, int var4, ArrayList var5) {
      ItemContainer.TagEvalArgPredicate var6 = ((ItemContainer.TagEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.alloc()).init(var1, var2, var3);
      ArrayList var7 = this.getSome(var6, var4, var5);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.release((Object)var6);
      return var7;
   }

   public ArrayList getSomeTagRecurse(String var1, int var2, ArrayList var3) {
      ItemContainer.TagPredicate var4 = ((ItemContainer.TagPredicate)((ItemContainer.Predicates)TL_predicates.get()).tag.alloc()).init(var1);
      ArrayList var5 = this.getSomeRecurse(var4, var2, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).tag.release((Object)var4);
      return var5;
   }

   public ArrayList getSomeTagEvalRecurse(String var1, LuaClosure var2, int var3, ArrayList var4) {
      ItemContainer.TagEvalPredicate var5 = ((ItemContainer.TagEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEval.alloc()).init(var1, var2);
      ArrayList var6 = this.getSomeRecurse(var5, var3, var4);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEval.release((Object)var5);
      return var6;
   }

   public ArrayList getSomeTagEvalArgRecurse(String var1, LuaClosure var2, Object var3, int var4, ArrayList var5) {
      ItemContainer.TagEvalArgPredicate var6 = ((ItemContainer.TagEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.alloc()).init(var1, var2, var3);
      ArrayList var7 = this.getSomeRecurse(var6, var4, var5);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.release((Object)var6);
      return var7;
   }

   public ArrayList getSomeType(String var1, int var2, ArrayList var3) {
      ItemContainer.TypePredicate var4 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      ArrayList var5 = this.getSome(var4, var2, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var4);
      return var5;
   }

   public ArrayList getSomeTypeEval(String var1, LuaClosure var2, int var3, ArrayList var4) {
      ItemContainer.TypeEvalPredicate var5 = ((ItemContainer.TypeEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEval.alloc()).init(var1, var2);
      ArrayList var6 = this.getSome(var5, var3, var4);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEval.release((Object)var5);
      return var6;
   }

   public ArrayList getSomeTypeEvalArg(String var1, LuaClosure var2, Object var3, int var4, ArrayList var5) {
      ItemContainer.TypeEvalArgPredicate var6 = ((ItemContainer.TypeEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.alloc()).init(var1, var2, var3);
      ArrayList var7 = this.getSome(var6, var4, var5);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.release((Object)var6);
      return var7;
   }

   public ArrayList getSomeTypeRecurse(String var1, int var2, ArrayList var3) {
      ItemContainer.TypePredicate var4 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      ArrayList var5 = this.getSomeRecurse(var4, var2, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var4);
      return var5;
   }

   public ArrayList getSomeTypeEvalRecurse(String var1, LuaClosure var2, int var3, ArrayList var4) {
      ItemContainer.TypeEvalPredicate var5 = ((ItemContainer.TypeEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEval.alloc()).init(var1, var2);
      ArrayList var6 = this.getSomeRecurse(var5, var3, var4);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEval.release((Object)var5);
      return var6;
   }

   public ArrayList getSomeTypeEvalArgRecurse(String var1, LuaClosure var2, Object var3, int var4, ArrayList var5) {
      ItemContainer.TypeEvalArgPredicate var6 = ((ItemContainer.TypeEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.alloc()).init(var1, var2, var3);
      ArrayList var7 = this.getSomeRecurse(var6, var4, var5);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.release((Object)var6);
      return var7;
   }

   public ArrayList getSomeEval(LuaClosure var1, int var2, ArrayList var3) {
      ItemContainer.EvalPredicate var4 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      ArrayList var5 = this.getSome(var4, var2, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var4);
      return var5;
   }

   public ArrayList getSomeEvalArg(LuaClosure var1, Object var2, int var3, ArrayList var4) {
      ItemContainer.EvalArgPredicate var5 = ((ItemContainer.EvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).evalArg.alloc()).init(var1, var2);
      ArrayList var6 = this.getSome(var5, var3, var4);
      ((ItemContainer.Predicates)TL_predicates.get()).evalArg.release((Object)var5);
      return var6;
   }

   public ArrayList getSomeEvalRecurse(LuaClosure var1, int var2, ArrayList var3) {
      ItemContainer.EvalPredicate var4 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      ArrayList var5 = this.getSomeRecurse(var4, var2, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var4);
      return var5;
   }

   public ArrayList getSomeEvalArgRecurse(LuaClosure var1, Object var2, int var3, ArrayList var4) {
      ItemContainer.EvalArgPredicate var5 = ((ItemContainer.EvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).evalArg.alloc()).init(var1, var2);
      ArrayList var6 = this.getSomeRecurse(var5, var3, var4);
      ((ItemContainer.Predicates)TL_predicates.get()).evalArg.release((Object)var5);
      return var6;
   }

   public ArrayList getAllCategory(String var1, ArrayList var2) {
      ItemContainer.CategoryPredicate var3 = ((ItemContainer.CategoryPredicate)((ItemContainer.Predicates)TL_predicates.get()).category.alloc()).init(var1);
      ArrayList var4 = this.getAll(var3, var2);
      ((ItemContainer.Predicates)TL_predicates.get()).category.release((Object)var3);
      return var4;
   }

   public ArrayList getAllCategoryRecurse(String var1, ArrayList var2) {
      ItemContainer.CategoryPredicate var3 = ((ItemContainer.CategoryPredicate)((ItemContainer.Predicates)TL_predicates.get()).category.alloc()).init(var1);
      ArrayList var4 = this.getAllRecurse(var3, var2);
      ((ItemContainer.Predicates)TL_predicates.get()).category.release((Object)var3);
      return var4;
   }

   public ArrayList getAllTag(String var1, ArrayList var2) {
      ItemContainer.TagPredicate var3 = ((ItemContainer.TagPredicate)((ItemContainer.Predicates)TL_predicates.get()).tag.alloc()).init(var1);
      ArrayList var4 = this.getAll(var3, var2);
      ((ItemContainer.Predicates)TL_predicates.get()).tag.release((Object)var3);
      return var4;
   }

   public ArrayList getAllTagEval(String var1, LuaClosure var2, ArrayList var3) {
      ItemContainer.TagEvalPredicate var4 = ((ItemContainer.TagEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEval.alloc()).init(var1, var2);
      ArrayList var5 = this.getAll(var4, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEval.release((Object)var4);
      return var5;
   }

   public ArrayList getAllTagEvalArg(String var1, LuaClosure var2, Object var3, ArrayList var4) {
      ItemContainer.TagEvalArgPredicate var5 = ((ItemContainer.TagEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.alloc()).init(var1, var2, var3);
      ArrayList var6 = this.getAll(var5, var4);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.release((Object)var5);
      return var6;
   }

   public ArrayList getAllTagRecurse(String var1, ArrayList var2) {
      ItemContainer.TagPredicate var3 = ((ItemContainer.TagPredicate)((ItemContainer.Predicates)TL_predicates.get()).tag.alloc()).init(var1);
      ArrayList var4 = this.getAllRecurse(var3, var2);
      ((ItemContainer.Predicates)TL_predicates.get()).tag.release((Object)var3);
      return var4;
   }

   public ArrayList getAllTagEvalRecurse(String var1, LuaClosure var2, ArrayList var3) {
      ItemContainer.TagEvalPredicate var4 = ((ItemContainer.TagEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEval.alloc()).init(var1, var2);
      ArrayList var5 = this.getAllRecurse(var4, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEval.release((Object)var4);
      return var5;
   }

   public ArrayList getAllTagEvalArgRecurse(String var1, LuaClosure var2, Object var3, ArrayList var4) {
      ItemContainer.TagEvalArgPredicate var5 = ((ItemContainer.TagEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.alloc()).init(var1, var2, var3);
      ArrayList var6 = this.getAllRecurse(var5, var4);
      ((ItemContainer.Predicates)TL_predicates.get()).tagEvalArg.release((Object)var5);
      return var6;
   }

   public ArrayList getAllType(String var1, ArrayList var2) {
      ItemContainer.TypePredicate var3 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      ArrayList var4 = this.getAll(var3, var2);
      ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var3);
      return var4;
   }

   public ArrayList getAllTypeEval(String var1, LuaClosure var2, ArrayList var3) {
      ItemContainer.TypeEvalPredicate var4 = ((ItemContainer.TypeEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEval.alloc()).init(var1, var2);
      ArrayList var5 = this.getAll(var4, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEval.release((Object)var4);
      return var5;
   }

   public ArrayList getAllTypeEvalArg(String var1, LuaClosure var2, Object var3, ArrayList var4) {
      ItemContainer.TypeEvalArgPredicate var5 = ((ItemContainer.TypeEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.alloc()).init(var1, var2, var3);
      ArrayList var6 = this.getAll(var5, var4);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.release((Object)var5);
      return var6;
   }

   public ArrayList getAllTypeRecurse(String var1, ArrayList var2) {
      ItemContainer.TypePredicate var3 = ((ItemContainer.TypePredicate)((ItemContainer.Predicates)TL_predicates.get()).type.alloc()).init(var1);
      ArrayList var4 = this.getAllRecurse(var3, var2);
      ((ItemContainer.Predicates)TL_predicates.get()).type.release((Object)var3);
      return var4;
   }

   public ArrayList getAllTypeEvalRecurse(String var1, LuaClosure var2, ArrayList var3) {
      ItemContainer.TypeEvalPredicate var4 = ((ItemContainer.TypeEvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEval.alloc()).init(var1, var2);
      ArrayList var5 = this.getAllRecurse(var4, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEval.release((Object)var4);
      return var5;
   }

   public ArrayList getAllTypeEvalArgRecurse(String var1, LuaClosure var2, Object var3, ArrayList var4) {
      ItemContainer.TypeEvalArgPredicate var5 = ((ItemContainer.TypeEvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.alloc()).init(var1, var2, var3);
      ArrayList var6 = this.getAllRecurse(var5, var4);
      ((ItemContainer.Predicates)TL_predicates.get()).typeEvalArg.release((Object)var5);
      return var6;
   }

   public ArrayList getAllEval(LuaClosure var1, ArrayList var2) {
      ItemContainer.EvalPredicate var3 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      ArrayList var4 = this.getAll(var3, var2);
      ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var3);
      return var4;
   }

   public ArrayList getAllEvalArg(LuaClosure var1, Object var2, ArrayList var3) {
      ItemContainer.EvalArgPredicate var4 = ((ItemContainer.EvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).evalArg.alloc()).init(var1, var2);
      ArrayList var5 = this.getAll(var4, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).evalArg.release((Object)var4);
      return var5;
   }

   public ArrayList getAllEvalRecurse(LuaClosure var1, ArrayList var2) {
      ItemContainer.EvalPredicate var3 = ((ItemContainer.EvalPredicate)((ItemContainer.Predicates)TL_predicates.get()).eval.alloc()).init(var1);
      ArrayList var4 = this.getAllRecurse(var3, var2);
      ((ItemContainer.Predicates)TL_predicates.get()).eval.release((Object)var3);
      return var4;
   }

   public ArrayList getAllEvalArgRecurse(LuaClosure var1, Object var2, ArrayList var3) {
      ItemContainer.EvalArgPredicate var4 = ((ItemContainer.EvalArgPredicate)((ItemContainer.Predicates)TL_predicates.get()).evalArg.alloc()).init(var1, var2);
      ArrayList var5 = this.getAllRecurse(var4, var3);
      ((ItemContainer.Predicates)TL_predicates.get()).evalArg.release((Object)var4);
      return var5;
   }

   public ArrayList getSomeCategory(String var1, int var2) {
      return this.getSomeCategory(var1, var2, new ArrayList());
   }

   public ArrayList getSomeEval(LuaClosure var1, int var2) {
      return this.getSomeEval(var1, var2, new ArrayList());
   }

   public ArrayList getSomeEvalArg(LuaClosure var1, Object var2, int var3) {
      return this.getSomeEvalArg(var1, var2, var3, new ArrayList());
   }

   public ArrayList getSomeTypeEval(String var1, LuaClosure var2, int var3) {
      return this.getSomeTypeEval(var1, var2, var3, new ArrayList());
   }

   public ArrayList getSomeTypeEvalArg(String var1, LuaClosure var2, Object var3, int var4) {
      return this.getSomeTypeEvalArg(var1, var2, var3, var4, new ArrayList());
   }

   public ArrayList getSomeEvalRecurse(LuaClosure var1, int var2) {
      return this.getSomeEvalRecurse(var1, var2, new ArrayList());
   }

   public ArrayList getSomeEvalArgRecurse(LuaClosure var1, Object var2, int var3) {
      return this.getSomeEvalArgRecurse(var1, var2, var3, new ArrayList());
   }

   public ArrayList getSomeTag(String var1, int var2) {
      return this.getSomeTag(var1, var2, new ArrayList());
   }

   public ArrayList getSomeTagRecurse(String var1, int var2) {
      return this.getSomeTagRecurse(var1, var2, new ArrayList());
   }

   public ArrayList getSomeTagEvalRecurse(String var1, LuaClosure var2, int var3) {
      return this.getSomeTagEvalRecurse(var1, var2, var3, new ArrayList());
   }

   public ArrayList getSomeTagEvalArgRecurse(String var1, LuaClosure var2, Object var3, int var4) {
      return this.getSomeTagEvalArgRecurse(var1, var2, var3, var4, new ArrayList());
   }

   public ArrayList getSomeType(String var1, int var2) {
      return this.getSomeType(var1, var2, new ArrayList());
   }

   public ArrayList getSomeTypeRecurse(String var1, int var2) {
      return this.getSomeTypeRecurse(var1, var2, new ArrayList());
   }

   public ArrayList getSomeTypeEvalRecurse(String var1, LuaClosure var2, int var3) {
      return this.getSomeTypeEvalRecurse(var1, var2, var3, new ArrayList());
   }

   public ArrayList getSomeTypeEvalArgRecurse(String var1, LuaClosure var2, Object var3, int var4) {
      return this.getSomeTypeEvalArgRecurse(var1, var2, var3, var4, new ArrayList());
   }

   public ArrayList getAll(Predicate var1) {
      return this.getAll(var1, new ArrayList());
   }

   public ArrayList getAllCategory(String var1) {
      return this.getAllCategory(var1, new ArrayList());
   }

   public ArrayList getAllEval(LuaClosure var1) {
      return this.getAllEval(var1, new ArrayList());
   }

   public ArrayList getAllEvalArg(LuaClosure var1, Object var2) {
      return this.getAllEvalArg(var1, var2, new ArrayList());
   }

   public ArrayList getAllTagEval(String var1, LuaClosure var2) {
      return this.getAllTagEval(var1, var2, new ArrayList());
   }

   public ArrayList getAllTagEvalArg(String var1, LuaClosure var2, Object var3) {
      return this.getAllTagEvalArg(var1, var2, var3, new ArrayList());
   }

   public ArrayList getAllTypeEval(String var1, LuaClosure var2) {
      return this.getAllTypeEval(var1, var2, new ArrayList());
   }

   public ArrayList getAllTypeEvalArg(String var1, LuaClosure var2, Object var3) {
      return this.getAllTypeEvalArg(var1, var2, var3, new ArrayList());
   }

   public ArrayList getAllEvalRecurse(LuaClosure var1) {
      return this.getAllEvalRecurse(var1, new ArrayList());
   }

   public ArrayList getAllEvalArgRecurse(LuaClosure var1, Object var2) {
      return this.getAllEvalArgRecurse(var1, var2, new ArrayList());
   }

   public ArrayList getAllType(String var1) {
      return this.getAllType(var1, new ArrayList());
   }

   public ArrayList getAllTypeRecurse(String var1) {
      return this.getAllTypeRecurse(var1, new ArrayList());
   }

   public ArrayList getAllTypeEvalRecurse(String var1, LuaClosure var2) {
      return this.getAllTypeEvalRecurse(var1, var2, new ArrayList());
   }

   public ArrayList getAllTypeEvalArgRecurse(String var1, LuaClosure var2, Object var3) {
      return this.getAllTypeEvalArgRecurse(var1, var2, var3, new ArrayList());
   }

   public InventoryItem FindAndReturnCategory(String var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3.getCategory().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public ArrayList FindAndReturn(String var1, int var2) {
      return this.getSomeType(var1, var2);
   }

   public InventoryItem FindAndReturn(String var1, ArrayList var2) {
      if (var1 == null) {
         return null;
      } else {
         for(int var3 = 0; var3 < this.Items.size(); ++var3) {
            InventoryItem var4 = (InventoryItem)this.Items.get(var3);
            if (var4.type != null && compareType(var1, var4) && !var2.contains(var4)) {
               return var4;
            }
         }

         return null;
      }
   }

   public InventoryItem FindAndReturn(String var1) {
      return this.getFirstType(var1);
   }

   public ArrayList FindAll(String var1) {
      return this.getAllType(var1);
   }

   public InventoryItem FindAndReturnStack(String var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (compareType(var1, var3)) {
            InventoryItem var4 = InventoryItemFactory.CreateItem(var3.module + "." + var1);
            if (var3.CanStack(var4)) {
               return var3;
            }
         }
      }

      return null;
   }

   public InventoryItem FindAndReturnStack(InventoryItem var1) {
      String var2 = var1.type;

      for(int var3 = 0; var3 < this.Items.size(); ++var3) {
         InventoryItem var4 = (InventoryItem)this.Items.get(var3);
         if (var4.type == null) {
            if (var2 != null) {
               continue;
            }
         } else if (!var4.type.equals(var2)) {
            continue;
         }

         if (var4.CanStack(var1)) {
            return var4;
         }
      }

      return null;
   }

   public boolean HasType(ItemType var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3.cat == var1) {
            return true;
         }
      }

      return false;
   }

   public void Remove(InventoryItem var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3 == var1) {
            if (var1.uses > 1) {
               --var1.uses;
            } else {
               this.Items.remove(var1);
            }

            var1.container = null;
            this.drawDirty = true;
            this.dirty = true;
            if (this.parent != null) {
               this.dirty = true;
            }

            if (this.parent instanceof IsoDeadBody) {
               ((IsoDeadBody)this.parent).checkClothing(var1);
            }

            if (this.parent instanceof IsoMannequin) {
               ((IsoMannequin)this.parent).checkClothing(var1);
            }

            return;
         }
      }

   }

   public void DoRemoveItem(InventoryItem var1) {
      this.drawDirty = true;
      if (this.parent != null) {
         this.dirty = true;
      }

      this.Items.remove(var1);
      var1.container = null;
      if (this.parent instanceof IsoDeadBody) {
         ((IsoDeadBody)this.parent).checkClothing(var1);
      }

      if (this.parent instanceof IsoMannequin) {
         ((IsoMannequin)this.parent).checkClothing(var1);
      }

   }

   public void Remove(String var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3.type.equals(var1)) {
            if (var3.uses > 1) {
               --var3.uses;
            } else {
               this.Items.remove(var3);
            }

            var3.container = null;
            this.drawDirty = true;
            this.dirty = true;
            if (this.parent != null) {
               this.dirty = true;
            }

            return;
         }
      }

   }

   public InventoryItem Remove(ItemType var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3.cat == var1) {
            this.Items.remove(var3);
            var3.container = null;
            this.drawDirty = true;
            this.dirty = true;
            if (this.parent != null) {
               this.dirty = true;
            }

            return var3;
         }
      }

      return null;
   }

   public InventoryItem Find(ItemType var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3.cat == var1) {
            return var3;
         }
      }

      return null;
   }

   public void RemoveAll(String var1) {
      this.drawDirty = true;
      if (this.parent != null) {
         this.dirty = true;
      }

      ArrayList var2 = new ArrayList();

      InventoryItem var4;
      for(int var3 = 0; var3 < this.Items.size(); ++var3) {
         var4 = (InventoryItem)this.Items.get(var3);
         if (var4.type.equals(var1)) {
            var4.container = null;
            var2.add(var4);
            this.dirty = true;
         }
      }

      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         var4 = (InventoryItem)var5.next();
         this.Items.remove(var4);
      }

   }

   public boolean RemoveOneOf(String var1, boolean var2) {
      this.drawDirty = true;
      if (this.parent != null && !(this.parent instanceof IsoGameCharacter)) {
         this.dirty = true;
      }

      int var3;
      InventoryItem var4;
      for(var3 = 0; var3 < this.Items.size(); ++var3) {
         var4 = (InventoryItem)this.Items.get(var3);
         if (var4.getFullType().equals(var1) || var4.type.equals(var1)) {
            if (var4.uses > 1) {
               --var4.uses;
            } else {
               var4.container = null;
               this.Items.remove(var4);
            }

            this.dirty = true;
            return true;
         }
      }

      if (var2) {
         for(var3 = 0; var3 < this.Items.size(); ++var3) {
            var4 = (InventoryItem)this.Items.get(var3);
            if (var4 instanceof InventoryContainer && ((InventoryContainer)var4).getItemContainer() != null && ((InventoryContainer)var4).getItemContainer().RemoveOneOf(var1, var2)) {
               return true;
            }
         }
      }

      return false;
   }

   public void RemoveOneOf(String var1) {
      this.RemoveOneOf(var1, true);
   }

   /** @deprecated */
   public int getWeight() {
      if (this.parent instanceof IsoPlayer && ((IsoPlayer)this.parent).isGhostMode()) {
         return 0;
      } else {
         float var1 = 0.0F;

         for(int var2 = 0; var2 < this.Items.size(); ++var2) {
            InventoryItem var3 = (InventoryItem)this.Items.get(var2);
            var1 += var3.ActualWeight * (float)var3.uses;
         }

         return (int)(var1 * ((float)this.weightReduction / 0.01F));
      }
   }

   public float getContentsWeight() {
      float var1 = 0.0F;

      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         var1 += var3.getUnequippedWeight();
      }

      return var1;
   }

   public float getMaxWeight() {
      return this.parent instanceof IsoGameCharacter ? (float)((IsoGameCharacter)this.parent).getMaxWeight() : (float)this.Capacity;
   }

   public float getCapacityWeight() {
      if (this.parent instanceof IsoPlayer) {
         if (Core.bDebug && ((IsoPlayer)this.parent).isGhostMode() || !((IsoPlayer)this.parent).getAccessLevel().equals("None") && ((IsoPlayer)this.parent).isUnlimitedCarry()) {
            return 0.0F;
         }

         if (((IsoPlayer)this.parent).isUnlimitedCarry()) {
            return 0.0F;
         }
      }

      return this.parent instanceof IsoGameCharacter ? ((IsoGameCharacter)this.parent).getInventoryWeight() : this.getContentsWeight();
   }

   public boolean isEmpty() {
      return this.Items == null || this.Items.isEmpty();
   }

   public boolean isMicrowave() {
      return "microwave".equals(this.getType());
   }

   private boolean isSquareInRoom(IsoGridSquare var1) {
      if (var1 == null) {
         return false;
      } else {
         return var1.getRoom() != null;
      }
   }

   private boolean isSquarePowered(IsoGridSquare var1) {
      if (var1 == null) {
         return false;
      } else {
         boolean var2 = GameTime.getInstance().getNightsSurvived() < SandboxOptions.instance.getElecShutModifier();
         if (var2 && var1.getRoom() != null) {
            return true;
         } else if (var1.haveElectricity()) {
            return true;
         } else {
            if (var2 && var1.getRoom() == null) {
               IsoGridSquare var3 = var1.nav[IsoDirections.N.index()];
               IsoGridSquare var4 = var1.nav[IsoDirections.S.index()];
               IsoGridSquare var5 = var1.nav[IsoDirections.W.index()];
               IsoGridSquare var6 = var1.nav[IsoDirections.E.index()];
               if (this.isSquareInRoom(var3) || this.isSquareInRoom(var4) || this.isSquareInRoom(var5) || this.isSquareInRoom(var6)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public boolean isPowered() {
      if (this.parent != null && this.parent.getObjectIndex() != -1) {
         IsoGridSquare var1 = this.parent.getSquare();
         if (this.isSquarePowered(var1)) {
            return true;
         } else {
            this.parent.getSpriteGridObjects(s_tempObjects);

            for(int var2 = 0; var2 < s_tempObjects.size(); ++var2) {
               IsoObject var3 = (IsoObject)s_tempObjects.get(var2);
               if (var3 != this.parent) {
                  IsoGridSquare var4 = var3.getSquare();
                  if (this.isSquarePowered(var4)) {
                     return true;
                  }
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public float getTemprature() {
      if (this.customTemperature != 0.0F) {
         return this.customTemperature;
      } else {
         boolean var1 = false;
         if (this.getParent() != null && this.getParent().getSprite() != null) {
            var1 = this.getParent().getSprite().getProperties().Is("IsFridge");
         }

         if (this.isPowered()) {
            if (this.type.equals("fridge") || this.type.equals("freezer") || var1) {
               return 0.2F;
            }

            if (("stove".equals(this.type) || "microwave".equals(this.type)) && this.parent instanceof IsoStove) {
               return ((IsoStove)this.parent).getCurrentTemperature() / 100.0F;
            }
         }

         if ("barbecue".equals(this.type) && this.parent instanceof IsoBarbecue) {
            return ((IsoBarbecue)this.parent).getTemperature();
         } else if ("fireplace".equals(this.type) && this.parent instanceof IsoFireplace) {
            return ((IsoFireplace)this.parent).getTemperature();
         } else if ("woodstove".equals(this.type) && this.parent instanceof IsoFireplace) {
            return ((IsoFireplace)this.parent).getTemperature();
         } else if ((this.type.equals("fridge") || this.type.equals("freezer") || var1) && GameTime.instance.NightsSurvived == SandboxOptions.instance.getElecShutModifier() && GameTime.instance.getTimeOfDay() < 13.0F) {
            float var2 = (GameTime.instance.getTimeOfDay() - 7.0F) / 6.0F;
            return GameTime.instance.Lerp(0.2F, 1.0F, var2);
         } else {
            return 1.0F;
         }
      }
   }

   public boolean isTemperatureChanging() {
      return this.parent instanceof IsoStove ? ((IsoStove)this.parent).isTemperatureChanging() : false;
   }

   public ArrayList save(ByteBuffer var1, IsoGameCharacter var2) throws IOException {
      GameWindow.WriteString(var1, this.type);
      var1.put((byte)(this.bExplored ? 1 : 0));
      ArrayList var3 = CompressIdenticalItems.save(var1, this.Items, (IsoGameCharacter)null);
      var1.put((byte)(this.isHasBeenLooted() ? 1 : 0));
      var1.putInt(this.Capacity);
      return var3;
   }

   public ArrayList save(ByteBuffer var1) throws IOException {
      return this.save(var1, (IsoGameCharacter)null);
   }

   public ArrayList load(ByteBuffer var1, int var2) throws IOException {
      this.type = GameWindow.ReadString(var1);
      this.bExplored = var1.get() == 1;
      ArrayList var3 = CompressIdenticalItems.load(var1, var2, this.Items, this.IncludingObsoleteItems);

      for(int var4 = 0; var4 < this.Items.size(); ++var4) {
         InventoryItem var5 = (InventoryItem)this.Items.get(var4);
         var5.container = this;
      }

      this.setHasBeenLooted(var1.get() == 1);
      this.Capacity = var1.getInt();
      this.dirty = false;
      return var3;
   }

   public boolean isDrawDirty() {
      return this.drawDirty;
   }

   public void setDrawDirty(boolean var1) {
      this.drawDirty = var1;
   }

   public InventoryItem getBestWeapon(SurvivorDesc var1) {
      InventoryItem var2 = null;
      float var3 = -1.0E7F;

      for(int var4 = 0; var4 < this.Items.size(); ++var4) {
         InventoryItem var5 = (InventoryItem)this.Items.get(var4);
         if (var5 instanceof HandWeapon) {
            float var6 = var5.getScore(var1);
            if (var6 >= var3) {
               var3 = var6;
               var2 = var5;
            }
         }
      }

      return var2;
   }

   public InventoryItem getBestWeapon() {
      InventoryItem var1 = null;
      float var2 = 0.0F;

      for(int var3 = 0; var3 < this.Items.size(); ++var3) {
         InventoryItem var4 = (InventoryItem)this.Items.get(var3);
         if (var4 instanceof HandWeapon) {
            float var5 = var4.getScore((SurvivorDesc)null);
            if (var5 >= var2) {
               var2 = var5;
               var1 = var4;
            }
         }
      }

      return var1;
   }

   public float getTotalFoodScore(SurvivorDesc var1) {
      float var2 = 0.0F;

      for(int var3 = 0; var3 < this.Items.size(); ++var3) {
         InventoryItem var4 = (InventoryItem)this.Items.get(var3);
         if (var4 instanceof Food) {
            var2 += var4.getScore(var1);
         }
      }

      return var2;
   }

   public float getTotalWeaponScore(SurvivorDesc var1) {
      float var2 = 0.0F;

      for(int var3 = 0; var3 < this.Items.size(); ++var3) {
         InventoryItem var4 = (InventoryItem)this.Items.get(var3);
         if (var4 instanceof HandWeapon) {
            var2 += var4.getScore(var1);
         }
      }

      return var2;
   }

   public InventoryItem getBestFood(SurvivorDesc var1) {
      InventoryItem var2 = null;
      float var3 = 0.0F;

      for(int var4 = 0; var4 < this.Items.size(); ++var4) {
         InventoryItem var5 = (InventoryItem)this.Items.get(var4);
         if (var5 instanceof Food) {
            float var6 = var5.getScore(var1);
            if (((Food)var5).isbDangerousUncooked() && !var5.isCooked()) {
               var6 *= 0.2F;
            }

            if (((Food)var5).Age > (float)var5.OffAge) {
               var6 *= 0.2F;
            }

            if (var6 >= var3) {
               var3 = var6;
               var2 = var5;
            }
         }
      }

      return var2;
   }

   public InventoryItem getBestBandage(SurvivorDesc var1) {
      InventoryItem var2 = null;

      for(int var3 = 0; var3 < this.Items.size(); ++var3) {
         InventoryItem var4 = (InventoryItem)this.Items.get(var3);
         if (var4.isCanBandage()) {
            var2 = var4;
            break;
         }
      }

      return var2;
   }

   public int getNumItems(String var1) {
      int var2 = 0;
      int var3;
      InventoryItem var4;
      if (var1.contains("Type:")) {
         for(var3 = 0; var3 < this.Items.size(); ++var3) {
            var4 = (InventoryItem)this.Items.get(var3);
            if (var4 instanceof Food && var1.contains("Food")) {
               var2 += var4.uses;
            }

            if (var4 instanceof HandWeapon && var1.contains("Weapon")) {
               var2 += var4.uses;
            }
         }
      } else {
         for(var3 = 0; var3 < this.Items.size(); ++var3) {
            var4 = (InventoryItem)this.Items.get(var3);
            if (var4.type.equals(var1)) {
               var2 += var4.uses;
            }
         }
      }

      return var2;
   }

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean var1) {
      this.active = var1;
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public void setDirty(boolean var1) {
      this.dirty = var1;
   }

   public boolean isIsDevice() {
      return this.IsDevice;
   }

   public void setIsDevice(boolean var1) {
      this.IsDevice = var1;
   }

   public float getAgeFactor() {
      return this.ageFactor;
   }

   public void setAgeFactor(float var1) {
      this.ageFactor = var1;
   }

   public float getCookingFactor() {
      return this.CookingFactor;
   }

   public void setCookingFactor(float var1) {
      this.CookingFactor = var1;
   }

   public ArrayList getItems() {
      return this.Items;
   }

   public void setItems(ArrayList var1) {
      this.Items = var1;
   }

   public IsoObject getParent() {
      return this.parent;
   }

   public void setParent(IsoObject var1) {
      this.parent = var1;
   }

   public IsoGridSquare getSourceGrid() {
      return this.SourceGrid;
   }

   public void setSourceGrid(IsoGridSquare var1) {
      this.SourceGrid = var1;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public void clear() {
      this.Items.clear();
      this.dirty = true;
      this.drawDirty = true;
   }

   public int getWaterContainerCount() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3.CanStoreWater) {
            ++var1;
         }
      }

      return var1;
   }

   public InventoryItem FindWaterSource() {
      for(int var1 = 0; var1 < this.Items.size(); ++var1) {
         InventoryItem var2 = (InventoryItem)this.Items.get(var1);
         if (var2.isWaterSource()) {
            if (!(var2 instanceof Drainable)) {
               return var2;
            }

            if (((Drainable)var2).getUsedDelta() > 0.0F) {
               return var2;
            }
         }
      }

      return null;
   }

   public ArrayList getAllWaterFillables() {
      tempList.clear();

      for(int var1 = 0; var1 < this.Items.size(); ++var1) {
         InventoryItem var2 = (InventoryItem)this.Items.get(var1);
         if (var2.CanStoreWater) {
            tempList.add(var2);
         }
      }

      return tempList;
   }

   public int getItemCount(String var1) {
      return this.getCountType(var1);
   }

   public int getItemCountRecurse(String var1) {
      return this.getCountTypeRecurse(var1);
   }

   public int getItemCount(String var1, boolean var2) {
      return var2 ? this.getCountTypeRecurse(var1) : this.getCountType(var1);
   }

   private static int getUses(ItemContainer.InventoryItemList var0) {
      int var1 = 0;

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         DrainableComboItem var3 = (DrainableComboItem)Type.tryCastTo((InventoryItem)var0.get(var2), DrainableComboItem.class);
         if (var3 != null) {
            var1 += var3.getDrainableUsesInt();
         } else {
            ++var1;
         }
      }

      return var1;
   }

   public int getUsesRecurse(Predicate var1) {
      ItemContainer.InventoryItemList var2 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();
      this.getAllRecurse(var1, var2);
      int var3 = getUses(var2);
      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var2);
      return var3;
   }

   public int getUsesType(String var1) {
      ItemContainer.InventoryItemList var2 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();
      this.getAllType(var1, var2);
      int var3 = getUses(var2);
      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var2);
      return var3;
   }

   public int getUsesTypeRecurse(String var1) {
      ItemContainer.InventoryItemList var2 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();
      this.getAllTypeRecurse(var1, var2);
      int var3 = getUses(var2);
      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var2);
      return var3;
   }

   public int getWeightReduction() {
      return this.weightReduction;
   }

   public void setWeightReduction(int var1) {
      var1 = Math.min(var1, 100);
      var1 = Math.max(var1, 0);
      this.weightReduction = var1;
   }

   public void removeAllItems() {
      this.drawDirty = true;
      if (this.parent != null) {
         this.dirty = true;
      }

      for(int var1 = 0; var1 < this.Items.size(); ++var1) {
         InventoryItem var2 = (InventoryItem)this.Items.get(var1);
         var2.container = null;
      }

      this.Items.clear();
      if (this.parent instanceof IsoDeadBody) {
         ((IsoDeadBody)this.parent).checkClothing((InventoryItem)null);
      }

      if (this.parent instanceof IsoMannequin) {
         ((IsoMannequin)this.parent).checkClothing((InventoryItem)null);
      }

   }

   public boolean containsRecursive(InventoryItem var1) {
      for(int var2 = 0; var2 < this.getItems().size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.getItems().get(var2);
         if (var3 == var1) {
            return true;
         }

         if (var3 instanceof InventoryContainer && ((InventoryContainer)var3).getInventory().containsRecursive(var1)) {
            return true;
         }
      }

      return false;
   }

   public int getItemCountFromTypeRecurse(String var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < this.getItems().size(); ++var3) {
         InventoryItem var4 = (InventoryItem)this.getItems().get(var3);
         if (var4.getFullType().equals(var1)) {
            ++var2;
         }

         if (var4 instanceof InventoryContainer) {
            int var5 = ((InventoryContainer)var4).getInventory().getItemCountFromTypeRecurse(var1);
            var2 += var5;
         }
      }

      return var2;
   }

   public float getCustomTemperature() {
      return this.customTemperature;
   }

   public void setCustomTemperature(float var1) {
      this.customTemperature = var1;
   }

   public InventoryItem getItemFromType(String var1, IsoGameCharacter var2, boolean var3, boolean var4, boolean var5) {
      ItemContainer.InventoryItemList var6 = (ItemContainer.InventoryItemList)((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).alloc();
      if (var1.contains(".")) {
         var1 = var1.split("\\.")[1];
      }

      int var7;
      for(var7 = 0; var7 < this.getItems().size(); ++var7) {
         InventoryItem var8 = (InventoryItem)this.getItems().get(var7);
         if (!var8.getFullType().equals(var1) && !var8.getType().equals(var1)) {
            if (var5 && var8 instanceof InventoryContainer && ((InventoryContainer)var8).getInventory() != null && !var6.contains(var8)) {
               var6.add(var8);
            }
         } else if ((!var3 || var2 == null || !var2.isEquippedClothing(var8)) && this.testBroken(var4, var8)) {
            ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var6);
            return var8;
         }
      }

      for(var7 = 0; var7 < var6.size(); ++var7) {
         ItemContainer var10 = ((InventoryContainer)var6.get(var7)).getInventory();
         InventoryItem var9 = var10.getItemFromType(var1, var2, var3, var4, var5);
         if (var9 != null) {
            ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var6);
            return var9;
         }
      }

      ((ItemContainer.InventoryItemListPool)TL_itemListPool.get()).release(var6);
      return null;
   }

   public InventoryItem getItemFromType(String var1, boolean var2, boolean var3) {
      return this.getItemFromType(var1, (IsoGameCharacter)null, false, var2, var3);
   }

   public InventoryItem getItemFromType(String var1) {
      return this.getFirstType(var1);
   }

   public ArrayList getItemsFromType(String var1) {
      return this.getAllType(var1);
   }

   public ArrayList getItemsFromFullType(String var1) {
      return var1 != null && var1.contains(".") ? this.getAllType(var1) : new ArrayList();
   }

   public ArrayList getItemsFromFullType(String var1, boolean var2) {
      if (var1 != null && var1.contains(".")) {
         return var2 ? this.getAllTypeRecurse(var1) : this.getAllType(var1);
      } else {
         return new ArrayList();
      }
   }

   public ArrayList getItemsFromType(String var1, boolean var2) {
      return var2 ? this.getAllTypeRecurse(var1) : this.getAllType(var1);
   }

   public ArrayList getItemsFromCategory(String var1) {
      return this.getAllCategory(var1);
   }

   public void sendContentsToRemoteContainer() {
      if (GameClient.bClient) {
         this.sendContentsToRemoteContainer(GameClient.connection);
      }

   }

   public void requestSync() {
      if (GameClient.bClient) {
         if (this.parent == null || this.parent.square == null || this.parent.square.chunk == null) {
            return;
         }

         GameClient.instance.worldObjectsSyncReq.putRequestSyncIsoChunk(this.parent.square.chunk);
      }

   }

   public void requestServerItemsForContainer() {
      if (this.parent != null && this.parent.square != null) {
         UdpConnection var1 = GameClient.connection;
         ByteBufferWriter var2 = var1.startPacket();
         PacketTypes.PacketType.RequestItemsForContainer.doPacket(var2);
         var2.putShort(IsoPlayer.getInstance().OnlineID);
         var2.putUTF(this.type);
         if (this.parent.square.getRoom() != null) {
            var2.putUTF(this.parent.square.getRoom().getName());
         } else {
            var2.putUTF("all");
         }

         var2.putInt(this.parent.square.getX());
         var2.putInt(this.parent.square.getY());
         var2.putInt(this.parent.square.getZ());
         int var3 = this.parent.square.getObjects().indexOf(this.parent);
         if (var3 == -1 && this.parent.square.getStaticMovingObjects().indexOf(this.parent) != -1) {
            var2.putShort((short)0);
            var3 = this.parent.square.getStaticMovingObjects().indexOf(this.parent);
            var2.putByte((byte)var3);
         } else if (this.parent instanceof IsoWorldInventoryObject) {
            var2.putShort((short)1);
            var2.putInt(((IsoWorldInventoryObject)this.parent).getItem().id);
         } else if (this.parent instanceof BaseVehicle) {
            var2.putShort((short)3);
            var2.putShort(((BaseVehicle)this.parent).VehicleID);
            var2.putByte((byte)this.vehiclePart.getIndex());
         } else {
            var2.putShort((short)2);
            var2.putByte((byte)var3);
            var2.putByte((byte)this.parent.getContainerIndex(this));
         }

         PacketTypes.PacketType.RequestItemsForContainer.send(var1);
      }
   }

   /** @deprecated */
   @Deprecated
   public void sendContentsToRemoteContainer(UdpConnection var1) {
      ByteBufferWriter var2 = var1.startPacket();
      PacketTypes.PacketType.AddInventoryItemToContainer.doPacket(var2);
      var2.putInt(0);
      boolean var3 = false;
      var2.putInt(this.parent.square.getX());
      var2.putInt(this.parent.square.getY());
      var2.putInt(this.parent.square.getZ());
      var2.putByte((byte)this.parent.square.getObjects().indexOf(this.parent));

      try {
         CompressIdenticalItems.save(var2.bb, this.Items, (IsoGameCharacter)null);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      PacketTypes.PacketType.AddInventoryItemToContainer.send(var1);
   }

   public InventoryItem getItemWithIDRecursiv(int var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3.id == var1) {
            return var3;
         }

         if (var3 instanceof InventoryContainer && ((InventoryContainer)var3).getItemContainer() != null && !((InventoryContainer)var3).getItemContainer().getItems().isEmpty()) {
            var3 = ((InventoryContainer)var3).getItemContainer().getItemWithIDRecursiv(var1);
            if (var3 != null) {
               return var3;
            }
         }
      }

      return null;
   }

   public InventoryItem getItemWithID(int var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3.id == var1) {
            return var3;
         }
      }

      return null;
   }

   public boolean removeItemWithID(int var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3.id == var1) {
            this.Remove(var3);
            return true;
         }
      }

      return false;
   }

   public boolean containsID(int var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3.id == var1) {
            return true;
         }
      }

      return false;
   }

   public boolean removeItemWithIDRecurse(int var1) {
      for(int var2 = 0; var2 < this.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.Items.get(var2);
         if (var3.id == var1) {
            this.Remove(var3);
            return true;
         }

         if (var3 instanceof InventoryContainer && ((InventoryContainer)var3).getInventory().removeItemWithIDRecurse(var1)) {
            return true;
         }
      }

      return false;
   }

   public boolean isHasBeenLooted() {
      return this.hasBeenLooted;
   }

   public void setHasBeenLooted(boolean var1) {
      this.hasBeenLooted = var1;
   }

   public String getOpenSound() {
      return this.openSound;
   }

   public void setOpenSound(String var1) {
      this.openSound = var1;
   }

   public String getCloseSound() {
      return this.closeSound;
   }

   public void setCloseSound(String var1) {
      this.closeSound = var1;
   }

   public String getPutSound() {
      return this.putSound;
   }

   public void setPutSound(String var1) {
      this.putSound = var1;
   }

   public InventoryItem haveThisKeyId(int var1) {
      for(int var2 = 0; var2 < this.getItems().size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.getItems().get(var2);
         if (var3 instanceof Key) {
            Key var4 = (Key)var3;
            if (var4.getKeyId() == var1) {
               return var4;
            }
         } else if (var3.getType().equals("KeyRing") && ((InventoryContainer)var3).getInventory().haveThisKeyId(var1) != null) {
            return ((InventoryContainer)var3).getInventory().haveThisKeyId(var1);
         }
      }

      return null;
   }

   public String getOnlyAcceptCategory() {
      return this.OnlyAcceptCategory;
   }

   public void setOnlyAcceptCategory(String var1) {
      this.OnlyAcceptCategory = StringUtils.discardNullOrWhitespace(var1);
   }

   public String getAcceptItemFunction() {
      return this.AcceptItemFunction;
   }

   public void setAcceptItemFunction(String var1) {
      this.AcceptItemFunction = StringUtils.discardNullOrWhitespace(var1);
   }

   public IsoGameCharacter getCharacter() {
      if (this.getParent() instanceof IsoGameCharacter) {
         return (IsoGameCharacter)this.getParent();
      } else {
         return this.containingItem != null && this.containingItem.getContainer() != null ? this.containingItem.getContainer().getCharacter() : null;
      }
   }

   public void emptyIt() {
      this.Items = new ArrayList();
   }

   public LinkedHashMap getItems4Admin() {
      LinkedHashMap var1 = new LinkedHashMap();

      for(int var2 = 0; var2 < this.getItems().size(); ++var2) {
         InventoryItem var3 = (InventoryItem)this.getItems().get(var2);
         var3.setCount(1);
         if (var3.getCat() != ItemType.Drainable && var3.getCat() != ItemType.Weapon && var1.get(var3.getFullType()) != null && !(var3 instanceof InventoryContainer)) {
            ((InventoryItem)var1.get(var3.getFullType())).setCount(((InventoryItem)var1.get(var3.getFullType())).getCount() + 1);
         } else if (var1.get(var3.getFullType()) != null) {
            var1.put(var3.getFullType() + Rand.Next(100000), var3);
         } else {
            var1.put(var3.getFullType(), var3);
         }
      }

      return var1;
   }

   public LinkedHashMap getAllItems(LinkedHashMap var1, boolean var2) {
      if (var1 == null) {
         var1 = new LinkedHashMap();
      }

      for(int var3 = 0; var3 < this.getItems().size(); ++var3) {
         InventoryItem var4 = (InventoryItem)this.getItems().get(var3);
         if (var2) {
            var4.setWorker("inInv");
         }

         var4.setCount(1);
         if (var4.getCat() != ItemType.Drainable && var4.getCat() != ItemType.Weapon && var1.get(var4.getFullType()) != null) {
            ((InventoryItem)var1.get(var4.getFullType())).setCount(((InventoryItem)var1.get(var4.getFullType())).getCount() + 1);
         } else if (var1.get(var4.getFullType()) != null) {
            var1.put(var4.getFullType() + Rand.Next(100000), var4);
         } else {
            var1.put(var4.getFullType(), var4);
         }

         if (var4 instanceof InventoryContainer && ((InventoryContainer)var4).getItemContainer() != null && !((InventoryContainer)var4).getItemContainer().getItems().isEmpty()) {
            var1 = ((InventoryContainer)var4).getItemContainer().getAllItems(var1, true);
         }
      }

      return var1;
   }

   public InventoryItem getItemById(long var1) {
      for(int var3 = 0; var3 < this.getItems().size(); ++var3) {
         InventoryItem var4 = (InventoryItem)this.getItems().get(var3);
         if ((long)var4.getID() == var1) {
            return var4;
         }

         if (var4 instanceof InventoryContainer && ((InventoryContainer)var4).getItemContainer() != null && !((InventoryContainer)var4).getItemContainer().getItems().isEmpty()) {
            var4 = ((InventoryContainer)var4).getItemContainer().getItemById(var1);
            if (var4 != null) {
               return var4;
            }
         }
      }

      return null;
   }

   public void addItemsToProcessItems() {
      IsoWorld.instance.CurrentCell.addToProcessItems(this.Items);
   }

   public void removeItemsFromProcessItems() {
      IsoWorld.instance.CurrentCell.addToProcessItemsRemove(this.Items);
      if (!"floor".equals(this.type)) {
         ItemSoundManager.removeItems(this.Items);
      }

   }

   public boolean isExistYet() {
      if (!SystemDisabler.doWorldSyncEnable) {
         return true;
      } else if (this.getCharacter() != null) {
         return true;
      } else if (this.getParent() instanceof BaseVehicle) {
         return true;
      } else if (this.parent instanceof IsoDeadBody) {
         return this.parent.getStaticMovingObjectIndex() != -1;
      } else if (this.parent instanceof IsoCompost) {
         return this.parent.getObjectIndex() != -1;
      } else if (this.containingItem != null && this.containingItem.worldItem != null) {
         return this.containingItem.worldItem.getWorldObjectIndex() != -1;
      } else if (this.getType().equals("floor")) {
         return true;
      } else if (this.SourceGrid == null) {
         return false;
      } else {
         IsoGridSquare var1 = this.SourceGrid;
         if (!var1.getObjects().contains(this.parent)) {
            return false;
         } else {
            return this.parent.getContainerIndex(this) != -1;
         }
      }
   }

   public String getContainerPosition() {
      return this.containerPosition;
   }

   public void setContainerPosition(String var1) {
      this.containerPosition = var1;
   }

   public String getFreezerPosition() {
      return this.freezerPosition;
   }

   public void setFreezerPosition(String var1) {
      this.freezerPosition = var1;
   }

   public VehiclePart getVehiclePart() {
      return this.vehiclePart;
   }

   private static final class InventoryItemListPool extends ObjectPool {
      public InventoryItemListPool() {
         super(ItemContainer.InventoryItemList::new);
      }

      public void release(ItemContainer.InventoryItemList var1) {
         var1.clear();
         super.release((Object)var1);
      }
   }

   private static final class InventoryItemList extends ArrayList {
      public boolean equals(Object var1) {
         return this == var1;
      }
   }

   private static final class Predicates {
      final ObjectPool category = new ObjectPool(ItemContainer.CategoryPredicate::new);
      final ObjectPool eval = new ObjectPool(ItemContainer.EvalPredicate::new);
      final ObjectPool evalArg = new ObjectPool(ItemContainer.EvalArgPredicate::new);
      final ObjectPool tag = new ObjectPool(ItemContainer.TagPredicate::new);
      final ObjectPool tagEval = new ObjectPool(ItemContainer.TagEvalPredicate::new);
      final ObjectPool tagEvalArg = new ObjectPool(ItemContainer.TagEvalArgPredicate::new);
      final ObjectPool type = new ObjectPool(ItemContainer.TypePredicate::new);
      final ObjectPool typeEval = new ObjectPool(ItemContainer.TypeEvalPredicate::new);
      final ObjectPool typeEvalArg = new ObjectPool(ItemContainer.TypeEvalArgPredicate::new);
   }

   private static final class TypePredicate implements Predicate {
      String type;

      ItemContainer.TypePredicate init(String var1) {
         this.type = (String)Objects.requireNonNull(var1);
         return this;
      }

      public boolean test(InventoryItem var1) {
         return ItemContainer.compareType(this.type, var1);
      }
   }

   private static final class EvalPredicate implements Predicate {
      LuaClosure functionObj;

      ItemContainer.EvalPredicate init(LuaClosure var1) {
         this.functionObj = (LuaClosure)Objects.requireNonNull(var1);
         return this;
      }

      public boolean test(InventoryItem var1) {
         return LuaManager.caller.protectedCallBoolean(LuaManager.thread, this.functionObj, (Object)var1) == Boolean.TRUE;
      }
   }

   private static final class Comparators {
      ObjectPool condition = new ObjectPool(ItemContainer.ConditionComparator::new);
      ObjectPool eval = new ObjectPool(ItemContainer.EvalComparator::new);
      ObjectPool evalArg = new ObjectPool(ItemContainer.EvalArgComparator::new);
   }

   private static final class EvalComparator implements Comparator {
      LuaClosure functionObj;

      ItemContainer.EvalComparator init(LuaClosure var1) {
         this.functionObj = (LuaClosure)Objects.requireNonNull(var1);
         return this;
      }

      public int compare(InventoryItem var1, InventoryItem var2) {
         LuaReturn var3 = LuaManager.caller.protectedCall(LuaManager.thread, this.functionObj, var1, var2);
         if (var3.isSuccess() && !var3.isEmpty() && var3.getFirst() instanceof Double) {
            double var4 = (Double)var3.getFirst();
            return Double.compare(var4, 0.0D);
         } else {
            return 0;
         }
      }
   }

   private static final class EvalArgPredicate implements Predicate {
      LuaClosure functionObj;
      Object arg;

      ItemContainer.EvalArgPredicate init(LuaClosure var1, Object var2) {
         this.functionObj = (LuaClosure)Objects.requireNonNull(var1);
         this.arg = var2;
         return this;
      }

      public boolean test(InventoryItem var1) {
         return LuaManager.caller.protectedCallBoolean(LuaManager.thread, this.functionObj, var1, this.arg) == Boolean.TRUE;
      }
   }

   private static final class EvalArgComparator implements Comparator {
      LuaClosure functionObj;
      Object arg;

      ItemContainer.EvalArgComparator init(LuaClosure var1, Object var2) {
         this.functionObj = (LuaClosure)Objects.requireNonNull(var1);
         this.arg = var2;
         return this;
      }

      public int compare(InventoryItem var1, InventoryItem var2) {
         LuaReturn var3 = LuaManager.caller.protectedCall(LuaManager.thread, this.functionObj, var1, var2, this.arg);
         if (var3.isSuccess() && !var3.isEmpty() && var3.getFirst() instanceof Double) {
            double var4 = (Double)var3.getFirst();
            return Double.compare(var4, 0.0D);
         } else {
            return 0;
         }
      }
   }

   private static final class ConditionComparator implements Comparator {
      public int compare(InventoryItem var1, InventoryItem var2) {
         return var1.getCondition() - var2.getCondition();
      }
   }

   private static final class TagPredicate implements Predicate {
      String tag;

      ItemContainer.TagPredicate init(String var1) {
         this.tag = (String)Objects.requireNonNull(var1);
         return this;
      }

      public boolean test(InventoryItem var1) {
         return var1.hasTag(this.tag);
      }
   }

   private static final class TagEvalPredicate implements Predicate {
      String tag;
      LuaClosure functionObj;

      ItemContainer.TagEvalPredicate init(String var1, LuaClosure var2) {
         this.tag = var1;
         this.functionObj = (LuaClosure)Objects.requireNonNull(var2);
         return this;
      }

      public boolean test(InventoryItem var1) {
         return var1.hasTag(this.tag) && LuaManager.caller.protectedCallBoolean(LuaManager.thread, this.functionObj, (Object)var1) == Boolean.TRUE;
      }
   }

   private static final class TagEvalArgPredicate implements Predicate {
      String tag;
      LuaClosure functionObj;
      Object arg;

      ItemContainer.TagEvalArgPredicate init(String var1, LuaClosure var2, Object var3) {
         this.tag = var1;
         this.functionObj = (LuaClosure)Objects.requireNonNull(var2);
         this.arg = var3;
         return this;
      }

      public boolean test(InventoryItem var1) {
         return var1.hasTag(this.tag) && LuaManager.caller.protectedCallBoolean(LuaManager.thread, this.functionObj, var1, this.arg) == Boolean.TRUE;
      }
   }

   private static final class TypeEvalPredicate implements Predicate {
      String type;
      LuaClosure functionObj;

      ItemContainer.TypeEvalPredicate init(String var1, LuaClosure var2) {
         this.type = var1;
         this.functionObj = (LuaClosure)Objects.requireNonNull(var2);
         return this;
      }

      public boolean test(InventoryItem var1) {
         return ItemContainer.compareType(this.type, var1) && LuaManager.caller.protectedCallBoolean(LuaManager.thread, this.functionObj, (Object)var1) == Boolean.TRUE;
      }
   }

   private static final class TypeEvalArgPredicate implements Predicate {
      String type;
      LuaClosure functionObj;
      Object arg;

      ItemContainer.TypeEvalArgPredicate init(String var1, LuaClosure var2, Object var3) {
         this.type = var1;
         this.functionObj = (LuaClosure)Objects.requireNonNull(var2);
         this.arg = var3;
         return this;
      }

      public boolean test(InventoryItem var1) {
         return ItemContainer.compareType(this.type, var1) && LuaManager.caller.protectedCallBoolean(LuaManager.thread, this.functionObj, var1, this.arg) == Boolean.TRUE;
      }
   }

   private static final class CategoryPredicate implements Predicate {
      String category;

      ItemContainer.CategoryPredicate init(String var1) {
         this.category = (String)Objects.requireNonNull(var1);
         return this;
      }

      public boolean test(InventoryItem var1) {
         return var1.getCategory().equals(this.category);
      }
   }
}
