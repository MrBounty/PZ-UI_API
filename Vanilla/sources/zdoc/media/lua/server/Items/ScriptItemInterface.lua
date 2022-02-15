---@class ScriptItemInterface
ScriptItemInterface = {}
ScriptItemInterface.types = {weapon = "Weapon", item = "Normal", food = "Food"}

ScriptItemInterface.newItemType = function(name, displayname, type, inventoryIcon)
    local item = createNewScriptItem("Base", name, displayname, type, inventoryIcon);

    return item;
end
ScriptItemInterface.cloneItemType = function(newName, originalFullName)
    local item = cloneItemType(newName, originalFullName)

    return item;
end

ScriptItemInterface.instanceItemType = function(item)
    local inst = instanceItem(item);
end
