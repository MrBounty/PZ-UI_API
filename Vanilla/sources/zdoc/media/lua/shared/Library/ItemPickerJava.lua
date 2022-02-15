---@class ItemPickerJava : zombie.inventory.ItemPickerJava
---@field private player IsoPlayer
---@field private OtherLootModifier float
---@field private FoodLootModifier float
---@field private CannedFoodLootModifier float
---@field private WeaponLootModifier float
---@field private RangedWeaponLootModifier float
---@field private AmmoLootModifier float
---@field private LiteratureLootModifier float
---@field private SurvivalGearsLootModifier float
---@field private MedicalLootModifier float
---@field private BagLootModifier float
---@field private MechanicsLootModifier float
---@field public zombieDensityCap float
---@field public NoContainerFillRooms ArrayList|Unknown
---@field public WeaponUpgrades ArrayList|Unknown
---@field public WeaponUpgradeMap HashMap|Unknown|Unknown
---@field public rooms THashMap|Unknown|Unknown
---@field public containers THashMap|Unknown|Unknown
---@field public ProceduralDistributions THashMap|Unknown|Unknown
---@field public VehicleDistributions THashMap|Unknown|Unknown
ItemPickerJava = {}

---@public
---@param arg0 ItemPickerJava.ItemPickerContainer
---@param arg1 ItemContainer
---@param arg2 float
---@param arg3 IsoGameCharacter
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 ItemPickerJava.ItemPickerRoom
---@return void
function ItemPickerJava:doRollItem(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@param arg0 InventoryItem
---@param arg1 ItemPickerJava.ItemPickerContainer
---@return void
function ItemPickerJava:checkStashItem(arg0, arg1) end

---@public
---@param arg0 IsoGridSquare
---@return void
function ItemPickerJava:doOverlaySprite(arg0) end

---@public
---@param arg0 InventoryContainer
---@param arg1 IsoGameCharacter
---@param arg2 ItemPickerJava.ItemPickerContainer
---@return void
function ItemPickerJava:rollContainerItem(arg0, arg1, arg2) end

---@private
---@param arg0 int
---@return float
function ItemPickerJava:doSandboxSettings(arg0) end

---@public
---@param arg0 ItemPickerJava.ItemPickerRoom
---@param arg1 ItemContainer
---@param arg2 String
---@param arg3 IsoGameCharacter
---@return void
function ItemPickerJava:fillContainerType(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 KahluaTableImpl
---@return ArrayList|Unknown
function ItemPickerJava:ExtractProcList(arg0) end

---@private
---@return void
function ItemPickerJava:ParseVehicleDistributions() end

---@public
---@param arg0 ItemContainer
---@param arg1 IsoPlayer
---@return void
function ItemPickerJava:fillContainer(arg0, arg1) end

---@public
---@return void
function ItemPickerJava:Parse() end

---@private
---@return void
function ItemPickerJava:ParseProceduralDistributions() end

---@public
---@param arg0 IsoObject
---@return void
function ItemPickerJava:updateOverlaySprite(arg0) end

---@private
---@param arg0 KahluaTableImpl
---@return ItemPickerJava.ItemPickerContainer
function ItemPickerJava:ExtractContainersFromLua(arg0) end

---@public
---@param arg0 String
---@return float
function ItemPickerJava:getLootModifier(arg0) end

---@private
---@param arg0 InventoryItem
---@return void
function ItemPickerJava:DoWeaponUpgrade(arg0) end

---@private
---@param arg0 HashMap|Unknown|Unknown
---@return String
function ItemPickerJava:getDistribInHashMap(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@param arg3 boolean
---@return ItemPickerJava.ItemPickerContainer
function ItemPickerJava:getItemContainer(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 ItemContainer
---@param arg1 String
---@param arg2 ItemPickerJava.ItemPickerContainer
---@return InventoryItem
function ItemPickerJava:tryAddItemToContainer(arg0, arg1, arg2) end

---@public
---@param arg0 ItemPickerJava.ItemPickerContainer
---@param arg1 ItemContainer
---@param arg2 boolean
---@param arg3 IsoGameCharacter
---@param arg4 ItemPickerJava.ItemPickerRoom
---@return void
function ItemPickerJava:rollItem(arg0, arg1, arg2, arg3, arg4) end

---@private
---@return void
function ItemPickerJava:ParseSuburbsDistributions() end

---@public
---@return void
function ItemPickerJava:InitSandboxLootSettings() end

---@private
---@param arg0 ArrayList|Unknown
---@param arg1 ItemContainer
---@param arg2 float
---@param arg3 IsoGameCharacter
---@param arg4 ItemPickerJava.ItemPickerRoom
---@return void
function ItemPickerJava:rollProceduralItem(arg0, arg1, arg2, arg3, arg4) end
