---@class ItemContainer : zombie.inventory.ItemContainer
---@field private tempList ArrayList|Unknown
---@field private s_tempObjects ArrayList|Unknown
---@field public active boolean
---@field public dirty boolean
---@field public IsDevice boolean
---@field public ageFactor float
---@field public CookingFactor float
---@field public Capacity int
---@field public containingItem InventoryItem
---@field public Items ArrayList|InventoryItem
---@field public IncludingObsoleteItems ArrayList|InventoryItem
---@field public parent IsoObject
---@field public SourceGrid IsoGridSquare
---@field public vehiclePart VehiclePart
---@field public inventoryContainer InventoryContainer
---@field public bExplored boolean
---@field public type String
---@field public ID int
---@field private drawDirty boolean
---@field private customTemperature float
---@field private hasBeenLooted boolean
---@field private openSound String
---@field private closeSound String
---@field private putSound String
---@field private OnlyAcceptCategory String
---@field private AcceptItemFunction String
---@field private weightReduction int
---@field private containerPosition String
---@field private freezerPosition String
---@field private TL_comparators ThreadLocal|Unknown
---@field private TL_itemListPool ThreadLocal|Unknown
---@field private TL_predicates ThreadLocal|Unknown
ItemContainer = {}

---@private
---@param arg0 String
---@param arg1 String
---@return boolean
---@overload fun(arg0:String, arg1:InventoryItem)
function ItemContainer:compareType(arg0, arg1) end

---@private
---@param arg0 String
---@param arg1 InventoryItem
---@return boolean
function ItemContainer:compareType(arg0, arg1) end

---@public
---@param arg0 String
---@return int
function ItemContainer:getItemCountFromTypeRecurse(arg0) end

---@public
---@param String String
---@return InventoryItem
---@overload fun(arg0:String, arg1:int)
---@overload fun(arg0:String, arg1:ArrayList|Unknown)
function ItemContainer:FindAndReturn(String) end

---@public
---@param arg0 String
---@param arg1 int
---@return ArrayList|Unknown
function ItemContainer:FindAndReturn(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return InventoryItem
function ItemContainer:FindAndReturn(arg0, arg1) end

---@public
---@return ArrayList|InventoryItem
function ItemContainer:getAllWaterFillables() end

---@public
---@param itemlike InventoryItem
---@return InventoryItem
---@overload fun(String:String)
function ItemContainer:FindAndReturnStack(itemlike) end

---@public
---@param String String
---@return InventoryItem
function ItemContainer:FindAndReturnStack(String) end

---@public
---@param keyId int
---@return InventoryItem
function ItemContainer:haveThisKeyId(keyId) end

---@public
---@param item InventoryItem
---@return boolean
---@overload fun(type:String)
---@overload fun(arg0:InventoryItem, arg1:boolean)
---@overload fun(arg0:String, arg1:boolean)
---@overload fun(arg0:String, arg1:boolean, arg2:boolean)
function ItemContainer:contains(item) end

---@public
---@param type String
---@return boolean
function ItemContainer:contains(type) end

---@public
---@param arg0 InventoryItem
---@param arg1 boolean
---@return boolean
function ItemContainer:contains(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return boolean
function ItemContainer:contains(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 boolean
---@return boolean
function ItemContainer:contains(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@return boolean
function ItemContainer:removeItemWithID(arg0) end

---@public
---@param item String
---@return int
function ItemContainer:getNumItems(item) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return InventoryItem
function ItemContainer:getFirstTagEvalArgRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@return InventoryItem
function ItemContainer:getFirstEvalArgRecurse(arg0, arg1) end

---@public
---@param newTemp float
---@return void
function ItemContainer:setCustomTemperature(newTemp) end

---@public
---@param arg0 Predicate|Unknown
---@param arg1 int
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSome(arg0, arg1, arg2) end

---@public
---@return ArrayList|InventoryItem @the Items
function ItemContainer:getItems() end

---@public
---@return String
function ItemContainer:getAcceptItemFunction() end

---@public
---@param arg0 int
---@return InventoryItem
function ItemContainer:getItemWithIDRecursiv(arg0) end

---@public
---@param item InventoryItem
---@return void
function ItemContainer:DoRemoveItem(item) end

---@public
---@param item InventoryItem
---@param use int
---@return void
---@overload fun(item:String, use:int)
function ItemContainer:AddItems(item, use) end

---@public
---@param item String
---@param use int
---@return ArrayList|InventoryItem
function ItemContainer:AddItems(item, use) end

---@public
---@param arg0 LuaClosure
---@return boolean
function ItemContainer:containsEvalRecurse(arg0) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return InventoryItem
function ItemContainer:getBestTypeEval(arg0, arg1) end

---@public
---@param onlyAcceptCategory String
---@return void
function ItemContainer:setOnlyAcceptCategory(onlyAcceptCategory) end

---@public
---@return void
---@overload fun(connection:UdpConnection)
function ItemContainer:sendContentsToRemoteContainer() end

---@public
---@param connection UdpConnection
---@return void
function ItemContainer:sendContentsToRemoteContainer(connection) end

---@public
---@return boolean @the IsDevice
function ItemContainer:isIsDevice() end

---@public
---@return void
function ItemContainer:removeAllItems() end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@return InventoryItem
function ItemContainer:getFirstEvalArg(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 float
---@return boolean
---@overload fun(chr:IsoGameCharacter, item:InventoryItem)
function ItemContainer:hasRoomFor(arg0, arg1) end

---@public
---@param chr IsoGameCharacter
---@param item InventoryItem
---@return boolean
function ItemContainer:hasRoomFor(chr, item) end

---@public
---@param closeSound String
---@return void
function ItemContainer:setCloseSound(closeSound) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:ArrayList|Unknown)
function ItemContainer:getAllTypeEval(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllTypeEval(arg0, arg1, arg2) end

---@public
---@param type String
---@return InventoryItem
---@overload fun(arg0:String, arg1:boolean, arg2:boolean)
---@overload fun(arg0:String, arg1:IsoGameCharacter, arg2:boolean, arg3:boolean, arg4:boolean)
function ItemContainer:getItemFromType(type) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 boolean
---@return InventoryItem
function ItemContainer:getItemFromType(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 IsoGameCharacter
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@return InventoryItem
function ItemContainer:getItemFromType(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 float
---@return float
function ItemContainer:floatingPointCorrection(arg0) end

---@public
---@param arg0 ByteBuffer
---@return ArrayList|Unknown
---@overload fun(arg0:ByteBuffer, arg1:IsoGameCharacter)
function ItemContainer:save(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 IsoGameCharacter
---@return ArrayList|Unknown
function ItemContainer:save(arg0, arg1) end

---@public
---@param chr IsoGameCharacter
---@return boolean
function ItemContainer:isInCharacterInventory(chr) end

---@public
---@param arg0 LuaClosure
---@return InventoryItem
function ItemContainer:getFirstEvalRecurse(arg0) end

---@public
---@return boolean
function ItemContainer:isDrawDirty() end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@return boolean
function ItemContainer:containsEvalArgRecurse(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:Object, arg3:ArrayList|Unknown)
function ItemContainer:getAllTypeEvalArg(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllTypeEvalArg(arg0, arg1, arg2, arg3) end

---@public
---@return String
function ItemContainer:getContainerPosition() end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 int
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeTagEval(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@param arg1 int
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:int, arg2:ArrayList|Unknown)
function ItemContainer:getSomeType(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeType(arg0, arg1, arg2) end

---@public
---@param type String @the type to set
---@return void
function ItemContainer:setType(type) end

---@public
---@param arg0 Predicate|Unknown
---@return ArrayList|Unknown
---@overload fun(arg0:Predicate|Unknown, arg1:ArrayList|Unknown)
function ItemContainer:getAll(arg0) end

---@public
---@param arg0 Predicate|Unknown
---@param arg1 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAll(arg0, arg1) end

---@public
---@return int
function ItemContainer:getCapacity() end

---@public
---@return float @the CookingFactor
function ItemContainer:getCookingFactor() end

---@public
---@param arg0 String
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:ArrayList|Unknown)
function ItemContainer:getAllTypeRecurse(arg0) end

---@public
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllTypeRecurse(arg0, arg1) end

---@public
---@param arg0 Predicate|Unknown
---@param arg1 Comparator|Unknown
---@return InventoryItem
function ItemContainer:getBest(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@param arg3 int
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:Object, arg3:int, arg4:ArrayList|Unknown)
function ItemContainer:getSomeTypeEvalArgRecurse(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@param arg3 int
---@param arg4 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeTypeEvalArgRecurse(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param itemType String
---@return ArrayList|InventoryItem
function ItemContainer:FindAll(itemType) end

---@public
---@return int
function ItemContainer:getWaterContainerCount() end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@param arg3 int
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:Object, arg3:int, arg4:ArrayList|Unknown)
function ItemContainer:getSomeTypeEvalArg(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@param arg3 int
---@param arg4 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeTypeEvalArg(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 String
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:ArrayList|Unknown)
function ItemContainer:getAllCategory(arg0) end

---@public
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllCategory(arg0, arg1) end

---@public
---@param arg0 String
---@return int
function ItemContainer:getCountType(arg0) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return boolean
function ItemContainer:containsTagEval(arg0, arg1) end

---@public
---@return float
function ItemContainer:getContentsWeight() end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:ArrayList|Unknown)
function ItemContainer:getAllTagEval(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllTagEval(arg0, arg1, arg2) end

---@public
---@param arg0 LuaClosure
---@param arg1 LuaClosure
---@return InventoryItem
function ItemContainer:getBestEvalRecurse(arg0, arg1) end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@param arg2 int
---@return ArrayList|Unknown
---@overload fun(arg0:LuaClosure, arg1:Object, arg2:int, arg3:ArrayList|Unknown)
function ItemContainer:getSomeEvalArg(arg0, arg1, arg2) end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@param arg2 int
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeEvalArg(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@return boolean
function ItemContainer:containsTag(arg0) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return int
function ItemContainer:getCountTagEval(arg0, arg1) end

---@public
---@param type String
---@return ArrayList|InventoryItem
---@overload fun(arg0:String, arg1:boolean)
function ItemContainer:getItemsFromType(type) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return ArrayList|Unknown
function ItemContainer:getItemsFromType(arg0, arg1) end

---@public
---@param arg0 LuaClosure
---@param arg1 int
---@return ArrayList|Unknown
---@overload fun(arg0:LuaClosure, arg1:int, arg2:ArrayList|Unknown)
function ItemContainer:getSomeEval(arg0, arg1) end

---@public
---@param arg0 LuaClosure
---@param arg1 int
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeEval(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:Object, arg3:ArrayList|Unknown)
function ItemContainer:getAllTypeEvalArgRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllTypeEvalArgRecurse(arg0, arg1, arg2, arg3) end

---@public
---@return LinkedHashMap|Unknown|Unknown
function ItemContainer:getItems4Admin() end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllTagEvalRecurse(arg0, arg1, arg2) end

---@public
---@return float
function ItemContainer:getCustomTemperature() end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@return ArrayList|Unknown
---@overload fun(arg0:LuaClosure, arg1:Object, arg2:ArrayList|Unknown)
function ItemContainer:getAllEvalArg(arg0, arg1) end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllEvalArg(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 int
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:int, arg2:ArrayList|Unknown)
function ItemContainer:getSomeTypeRecurse(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeTypeRecurse(arg0, arg1, arg2) end

---@public
---@param type String
---@return InventoryItem
---@overload fun(item:InventoryItem)
---@overload fun(type:String, useDelta:float)
function ItemContainer:AddItem(type) end

---@public
---@param item InventoryItem
---@return InventoryItem
function ItemContainer:AddItem(item) end

---@public
---@param type String
---@param useDelta float
---@return boolean
function ItemContainer:AddItem(type, useDelta) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return int
function ItemContainer:getCountTagEvalRecurse(arg0, arg1) end

---@public
---@param item InventoryItem
---@return boolean
function ItemContainer:isInside(item) end

---@public
---@param item InventoryItem
---@return void
function ItemContainer:removeItemOnServer(item) end

---@public
---@param arg0 String
---@return boolean
function ItemContainer:containsTypeRecurse(arg0) end

---@public
---@return float
function ItemContainer:getTemprature() end

---@public
---@param arg0 LuaClosure
---@return InventoryItem
function ItemContainer:getFirstEval(arg0) end

---@public
---@return boolean
function ItemContainer:isExistYet() end

---@public
---@return void
function ItemContainer:removeItemsFromProcessItems() end

---@public
---@param hasBeenLooted boolean
---@return void
function ItemContainer:setHasBeenLooted(hasBeenLooted) end

---@public
---@param arg0 String
---@return void
function ItemContainer:setContainerPosition(arg0) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return InventoryItem
function ItemContainer:getBestTypeEvalRecurse(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 Comparator|Unknown
---@return InventoryItem
function ItemContainer:getBestTypeRecurse(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return InventoryItem
function ItemContainer:getFirstTypeEvalArgRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return int
function ItemContainer:getCountTag(arg0) end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@return boolean
function ItemContainer:containsEvalArg(arg0, arg1) end

---@public
---@param type String
---@return ArrayList|InventoryItem
---@overload fun(arg0:String, arg1:boolean)
function ItemContainer:getItemsFromFullType(type) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return ArrayList|Unknown
function ItemContainer:getItemsFromFullType(arg0, arg1) end

---@public
---@param arg0 String
---@return InventoryItem
---@overload fun(arg0:Predicate|Unknown)
function ItemContainer:getBestConditionRecurse(arg0) end

---@public
---@param arg0 Predicate|Unknown
---@return InventoryItem
function ItemContainer:getBestConditionRecurse(arg0) end

---@public
---@param arg0 Predicate|Unknown
---@return int
function ItemContainer:getCountRecurse(arg0) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:Object, arg3:ArrayList|Unknown)
function ItemContainer:getAllTagEvalArg(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllTagEvalArg(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 LinkedHashMap|Unknown|Unknown
---@param arg1 boolean
---@return LinkedHashMap|Unknown|Unknown
function ItemContainer:getAllItems(arg0, arg1) end

---@public
---@param item InventoryItem
---@return void
function ItemContainer:addItemOnServer(item) end

---@public
---@return boolean
function ItemContainer:isHasBeenLooted() end

---@public
---@param itemType ItemType
---@return boolean
function ItemContainer:HasType(itemType) end

---@public
---@param arg0 String
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:ArrayList|Unknown)
function ItemContainer:getAllType(arg0) end

---@public
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllType(arg0, arg1) end

---@public
---@param arg0 LuaClosure
---@return InventoryItem
function ItemContainer:getBestConditionEvalRecurse(arg0) end

---@public
---@param arg0 LuaClosure
---@return ArrayList|Unknown
---@overload fun(arg0:LuaClosure, arg1:ArrayList|Unknown)
function ItemContainer:getAllEvalRecurse(arg0) end

---@public
---@param arg0 LuaClosure
---@param arg1 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllEvalRecurse(arg0, arg1) end

---@public
---@param arg0 int
---@return boolean
function ItemContainer:containsID(arg0) end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@return ArrayList|Unknown
---@overload fun(arg0:LuaClosure, arg1:Object, arg2:ArrayList|Unknown)
function ItemContainer:getAllEvalArgRecurse(arg0, arg1) end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllEvalArgRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 LuaClosure
---@param arg1 int
---@return ArrayList|Unknown
---@overload fun(arg0:LuaClosure, arg1:int, arg2:ArrayList|Unknown)
function ItemContainer:getSomeEvalRecurse(arg0, arg1) end

---@public
---@param arg0 LuaClosure
---@param arg1 int
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeEvalRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return int
function ItemContainer:getCountTagRecurse(arg0) end

---@private
---@param arg0 IsoGridSquare
---@return boolean
function ItemContainer:isSquareInRoom(arg0) end

---@public
---@param item InventoryItem
---@return InventoryItem
function ItemContainer:addItem(item) end

---@public
---@param moduleType String
---@return boolean
---@overload fun(moduleType:String, withDeltaLeft:boolean)
function ItemContainer:containsWithModule(moduleType) end

---@public
---@param moduleType String
---@param withDeltaLeft boolean
---@return boolean
function ItemContainer:containsWithModule(moduleType, withDeltaLeft) end

---@private
---@param arg0 ItemContainer.InventoryItemList
---@return int
function ItemContainer:getUses(arg0) end

---@public
---@param arg0 Predicate|Unknown
---@return int
function ItemContainer:getCount(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:int, arg2:ArrayList|Unknown)
function ItemContainer:getSomeTag(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeTag(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return InventoryItem
function ItemContainer:getFirstCategoryRecurse(arg0) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:ArrayList|Unknown)
function ItemContainer:getAllTypeEvalRecurse(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllTypeEvalRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 int
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:int, arg3:ArrayList|Unknown)
function ItemContainer:getSomeTagEvalRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 int
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeTagEvalRecurse(arg0, arg1, arg2, arg3) end

---@public
---@param findItem String
---@return int
---@overload fun(findItem:String, includeReplaceOnDeplete:boolean)
---@overload fun(findItem:String, includeReplaceOnDeplete:boolean, insideInv:boolean)
---@overload fun(arg0:String, arg1:boolean, arg2:ArrayList|Unknown)
function ItemContainer:getNumberOfItem(findItem) end

---@public
---@param findItem String
---@param includeReplaceOnDeplete boolean
---@return int
function ItemContainer:getNumberOfItem(findItem, includeReplaceOnDeplete) end

---@public
---@param findItem String
---@param includeReplaceOnDeplete boolean
---@param insideInv boolean
---@return int
function ItemContainer:getNumberOfItem(findItem, includeReplaceOnDeplete, insideInv) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 ArrayList|Unknown
---@return int
function ItemContainer:getNumberOfItem(arg0, arg1, arg2) end

---@public
---@return void
function ItemContainer:requestSync() end

---@public
---@return boolean
function ItemContainer:isPowered() end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return int
function ItemContainer:getCountTypeEvalRecurse(arg0, arg1) end

---@public
---@param weightReduction int
---@return void
function ItemContainer:setWeightReduction(weightReduction) end

---@public
---@param type String
---@return int
---@overload fun(type:String, doBags:boolean)
function ItemContainer:getItemCount(type) end

---@public
---@param type String
---@param doBags boolean
---@return int
function ItemContainer:getItemCount(type, doBags) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return int
function ItemContainer:getCountTypeEval(arg0, arg1) end

---@public
---@param chr IsoGameCharacter
---@return int
function ItemContainer:getEffectiveCapacity(chr) end

---@public
---@param arg0 LuaClosure
---@return int
function ItemContainer:getCountEval(arg0) end

---@public
---@param arg0 Predicate|Unknown
---@return InventoryItem
function ItemContainer:getFirst(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:int, arg2:ArrayList|Unknown)
function ItemContainer:getSomeTagRecurse(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeTagRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 long
---@return InventoryItem
function ItemContainer:getItemById(arg0) end

---@public
---@param item InventoryItem
---@return InventoryItem
function ItemContainer:DoAddItemBlind(item) end

---@public
---@param arg0 Predicate|Unknown
---@return int
function ItemContainer:getUsesRecurse(arg0) end

---@private
---@param arg0 IsoGridSquare
---@return boolean
function ItemContainer:isSquarePowered(arg0) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return InventoryItem
function ItemContainer:getFirstTypeEval(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return int
function ItemContainer:getCountTagEvalArg(arg0, arg1, arg2) end

---@public
---@param item InventoryItem
---@return void
---@overload fun(itemType:ItemType)
---@overload fun(itemTypes:String)
function ItemContainer:Remove(item) end

---@public
---@param itemType ItemType
---@return InventoryItem
function ItemContainer:Remove(itemType) end

---@public
---@param itemTypes String
---@return void
function ItemContainer:Remove(itemTypes) end

---@private
---@param arg0 ItemContainer.InventoryItemList
---@param arg1 Comparator|Unknown
---@return InventoryItem
function ItemContainer:getBestOf(arg0, arg1) end

---@public
---@return IsoGridSquare @the SourceGrid
function ItemContainer:getSourceGrid() end

---@public
---@param arg0 LuaClosure
---@return boolean
function ItemContainer:containsEval(arg0) end

---@public
---@param arg0 Predicate|Unknown
---@param arg1 int
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@return int
function ItemContainer:getCountEvalArgRecurse(arg0, arg1) end

---@public
---@param arg0 LuaClosure
---@param arg1 LuaClosure
---@return InventoryItem
function ItemContainer:getBestEval(arg0, arg1) end

---@public
---@param arg0 String
---@return InventoryItem
function ItemContainer:getFirstCategory(arg0) end

---@public
---@param arg0 int
---@return boolean
function ItemContainer:removeItemWithIDRecurse(arg0) end

---@public
---@return InventoryItem
---@overload fun(desc:SurvivorDesc)
function ItemContainer:getBestWeapon() end

---@public
---@param desc SurvivorDesc
---@return InventoryItem
function ItemContainer:getBestWeapon(desc) end

---@public
---@param String String
---@return void
---@overload fun(arg0:String, arg1:boolean)
function ItemContainer:RemoveOneOf(String) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return boolean
function ItemContainer:RemoveOneOf(arg0, arg1) end

---@public
---@return VehiclePart
function ItemContainer:getVehiclePart() end

---@public
---@param arg0 LuaClosure
---@return InventoryItem
function ItemContainer:getBestConditionEval(arg0) end

---@public
---@param arg0 LuaClosure
---@return ArrayList|Unknown
---@overload fun(arg0:LuaClosure, arg1:ArrayList|Unknown)
function ItemContainer:getAllEval(arg0) end

---@public
---@param arg0 LuaClosure
---@param arg1 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllEval(arg0, arg1) end

---@public
---@param category String
---@return InventoryItem
function ItemContainer:FindAndReturnCategory(category) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return int
function ItemContainer:getCountTagEvalArgRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 InventoryItem
---@return boolean
function ItemContainer:isRemoveItemAllowed(arg0) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return InventoryItem
function ItemContainer:getBestTypeEvalArgRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 LuaClosure
---@return int
function ItemContainer:getCountEvalRecurse(arg0) end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@param arg2 int
---@return ArrayList|Unknown
---@overload fun(arg0:LuaClosure, arg1:Object, arg2:int, arg3:ArrayList|Unknown)
function ItemContainer:getSomeEvalArgRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@param arg2 int
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeEvalArgRecurse(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 boolean
---@param arg1 InventoryItem
---@return boolean
function ItemContainer:testBroken(arg0, arg1) end

---@public
---@return int
function ItemContainer:getWeightReduction() end

---@public
---@param arg0 Predicate|Unknown
---@param arg1 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllRecurse(arg0, arg1) end

---@public
---@param String String
---@return InventoryItem
---@overload fun(arg0:Predicate|Unknown)
function ItemContainer:getBestCondition(String) end

---@public
---@param arg0 Predicate|Unknown
---@return InventoryItem
function ItemContainer:getBestCondition(arg0) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 int
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:int, arg3:ArrayList|Unknown)
function ItemContainer:getSomeTypeEvalRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 int
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeTypeEvalRecurse(arg0, arg1, arg2, arg3) end

---@public
---@param putSound String
---@return void
function ItemContainer:setPutSound(putSound) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return boolean
function ItemContainer:containsTypeEvalArgRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@param arg3 int
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:Object, arg3:int, arg4:ArrayList|Unknown)
function ItemContainer:getSomeTagEvalArgRecurse(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@param arg3 int
---@param arg4 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeTagEvalArgRecurse(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 String
---@param arg1 int
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:int, arg2:ArrayList|Unknown)
function ItemContainer:getSomeCategory(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeCategory(arg0, arg1, arg2) end

---@public
---@return String
function ItemContainer:getFreezerPosition() end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 int
---@return ArrayList|Unknown
---@overload fun(arg0:String, arg1:LuaClosure, arg2:int, arg3:ArrayList|Unknown)
function ItemContainer:getSomeTypeEval(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 int
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeTypeEval(arg0, arg1, arg2, arg3) end

---@public
---@return boolean
function ItemContainer:isExplored() end

---@public
---@param arg0 LuaClosure
---@param arg1 LuaClosure
---@param arg2 Object
---@return InventoryItem
function ItemContainer:getBestEvalArgRecurse(arg0, arg1, arg2) end

---@public
---@return boolean
function ItemContainer:isTemperatureChanging() end

---@public
---@return String
function ItemContainer:getPutSound() end

---@public
---@param ageFactor float @the ageFactor to set
---@return void
function ItemContainer:setAgeFactor(ageFactor) end

---@public
---@param parent IsoObject @the parent to set
---@return void
function ItemContainer:setParent(parent) end

---@public
---@param arg0 Predicate|Unknown
---@return InventoryItem
function ItemContainer:getFirstRecurse(arg0) end

---@public
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllCategoryRecurse(arg0, arg1) end

---Remove all the item of the type in parameter inside the container
---
---Ex of itemType : Broccoli (no need the module like Base.Broccoli)
---@public
---@param itemType String
---@return void
function ItemContainer:RemoveAll(itemType) end

---@public
---@return IsoObject @the parent
function ItemContainer:getParent() end

---@public
---@param arg0 String
---@return boolean
function ItemContainer:containsTagRecurse(arg0) end

---@public
---@param descriptor SurvivorDesc
---@return InventoryItem
function ItemContainer:getBestBandage(descriptor) end

---@public
---@return boolean
function ItemContainer:isEmpty() end

---@public
---@return float @the ageFactor
function ItemContainer:getAgeFactor() end

---@public
---@return boolean @the dirty
function ItemContainer:isDirty() end

---@public
---@return int
function ItemContainer:getWeight() end

---@public
---@return String @the type
function ItemContainer:getType() end

---@public
---@return String
function ItemContainer:getCloseSound() end

---@public
---@param CookingFactor float @the CookingFactor to set
---@return void
function ItemContainer:setCookingFactor(CookingFactor) end

---@public
---@param desc SurvivorDesc
---@return float
function ItemContainer:getTotalFoodScore(desc) end

---@public
---@param arg0 int
---@return InventoryItem
function ItemContainer:FindAndReturnWaterItem(arg0) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@param arg3 int
---@param arg4 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeTagEvalArg(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getSomeCategoryRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return InventoryItem
function ItemContainer:getFirstTagEval(arg0, arg1) end

---@public
---@param arg0 String
---@return boolean
function ItemContainer:containsType(arg0) end

---@public
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllTagRecurse(arg0, arg1) end

---@public
---@param desc SurvivorDesc
---@return float
function ItemContainer:getTotalWeaponScore(desc) end

---@public
---@param arg0 String
---@return void
function ItemContainer:setFreezerPosition(arg0) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return InventoryItem
function ItemContainer:getFirstTypeEvalRecurse(arg0, arg1) end

---@public
---@param Items ArrayList|InventoryItem @the Items to set
---@return void
function ItemContainer:setItems(Items) end

---@public
---@return void
function ItemContainer:clear() end

---@public
---@param openSound String
---@return void
function ItemContainer:setOpenSound(openSound) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return int
function ItemContainer:getCountTypeEvalArgRecurse(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return InventoryItem
function ItemContainer:getItemFromTypeRecurse(arg0) end

---@public
---@param arg0 String
---@return InventoryItem
function ItemContainer:getFirstTypeRecurse(arg0) end

---@public
---@return IsoGameCharacter
function ItemContainer:getCharacter() end

---@public
---@param dirty boolean @the dirty to set
---@return void
function ItemContainer:setDirty(dirty) end

---@public
---@param arg0 int
---@return InventoryItem
function ItemContainer:getItemWithID(arg0) end

---@public
---@param category String
---@return ArrayList|InventoryItem
function ItemContainer:getItemsFromCategory(category) end

---@public
---@return boolean
function ItemContainer:isMicrowave() end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return boolean
function ItemContainer:containsTagEvalArgRecurse(arg0, arg1, arg2) end

---@public
---@param descriptor SurvivorDesc
---@return InventoryItem
function ItemContainer:getBestFood(descriptor) end

---@public
---@return void
function ItemContainer:requestServerItemsForContainer() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return ArrayList|Unknown
function ItemContainer:load(arg0, arg1) end

---Get the weight inside a inventory, also check if there is another inventory inside it to count his item too
---
---apply the weight reduction if there is one
---@public
---@return float
function ItemContainer:getMaxWeight() end

---@public
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllTag(arg0, arg1) end

---@public
---@return String
function ItemContainer:getOnlyAcceptCategory() end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return InventoryItem
function ItemContainer:getFirstTagEvalRecurse(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return int
function ItemContainer:getCountTypeEvalArg(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return InventoryItem
function ItemContainer:getFirstTagRecurse(arg0) end

---@public
---@return void
function ItemContainer:addItemsToProcessItems() end

---@public
---@return void
function ItemContainer:emptyIt() end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return boolean
function ItemContainer:containsTypeEvalRecurse(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@return boolean
function ItemContainer:containsTagEvalRecurse(arg0, arg1) end

---@public
---@return boolean @the active
function ItemContainer:isActive() end

---@public
---@param b boolean
---@return void
function ItemContainer:setDrawDirty(b) end

---@public
---@param arg0 LuaClosure
---@param arg1 LuaClosure
---@param arg2 Object
---@return InventoryItem
function ItemContainer:getBestEvalArg(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@return InventoryItem
function ItemContainer:getBestTypeEvalArg(arg0, arg1, arg2) end

---@public
---@param arg0 InventoryItem
---@return boolean
function ItemContainer:isItemAllowed(arg0) end

---@public
---@return String
function ItemContainer:getOpenSound() end

---@public
---@param arg0 String
---@return void
function ItemContainer:setAcceptItemFunction(arg0) end

---@public
---@return InventoryItem
function ItemContainer:getContainingItem() end

---@public
---@param arg0 LuaClosure
---@param arg1 Object
---@return int
function ItemContainer:getCountEvalArg(arg0, arg1) end

---@public
---@param arg0 String
---@return int
function ItemContainer:getUsesTypeRecurse(arg0) end

---@public
---@param item InventoryItem
---@return InventoryItem
function ItemContainer:DoAddItem(item) end

---@public
---@param IsDevice boolean @the IsDevice to set
---@return void
function ItemContainer:setIsDevice(IsDevice) end

---@public
---@param b boolean
---@return void
function ItemContainer:setExplored(b) end

---@public
---@param active boolean @the active to set
---@return void
function ItemContainer:setActive(active) end

---@public
---@return float
function ItemContainer:getCapacityWeight() end

---@public
---@param SourceGrid IsoGridSquare @the SourceGrid to set
---@return void
function ItemContainer:setSourceGrid(SourceGrid) end

---@public
---@param arg0 String
---@param arg1 Comparator|Unknown
---@return InventoryItem
function ItemContainer:getBestType(arg0, arg1) end

---@public
---@param arg0 String
---@return int
function ItemContainer:getItemCountRecurse(arg0) end

---@public
---@param arg0 InventoryItem
---@return boolean
function ItemContainer:containsRecursive(arg0) end

---@public
---@param arg0 String
---@return InventoryItem
function ItemContainer:getFirstTag(arg0) end

---@public
---@param arg0 String
---@return int
function ItemContainer:getUsesType(arg0) end

---@public
---@param arg0 String
---@return int
function ItemContainer:getCountTypeRecurse(arg0) end

---@public
---@param itemType ItemType
---@return InventoryItem
function ItemContainer:Find(itemType) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 Object
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function ItemContainer:getAllTagEvalArgRecurse(arg0, arg1, arg2, arg3) end

---@public
---@param item InventoryItem
---@return InventoryItem
function ItemContainer:AddItemBlind(item) end

---@public
---@param arg0 String
---@return InventoryItem
function ItemContainer:getFirstType(arg0) end

---@public
---@param arg0 Predicate|Unknown
---@param arg1 Comparator|Unknown
---@return InventoryItem
function ItemContainer:getBestRecurse(arg0, arg1) end

---@public
---@return InventoryItem
function ItemContainer:FindWaterSource() end
