-- This is a list of all the item types and categories that can be burned in a fire.
-- The value is the number of hours of fuel added to a fire.
---@class campingFuelType
campingFuelType = {
    Charcoal = 0.5,
    Log = 6.0,
    PercedWood = 2.0,
    Plank = 2.0,
    RippedSheets = 5/60.0,
    RippedSheetsDirty = 5/60.0,
    Sheet = 15/60.0,
    SheetPaper = 5/60.0,
    WoodenStick = 0.25,
    TreeBranch = 1.0,
    UnusableWood = 1.4,
    Twigs = 0.25,
    ToiletPaper = 0.2,
}
-- For Clothing, only unequipped items with FabricType defined are allowed.
---@class campingFuelCategory
campingFuelCategory = {
    Clothing = 15/60.0,
    Literature = 15/60.0
}

-- Types of items that can be used with a lighter/matches to start a fire.
---@class campingLightFireType
campingLightFireType = {
    RippedSheets = 5/60.0,
    RippedSheetsDirty = 5/60.0,
    Sheet = 15/60.0,
    SheetPaper = 5/60.0,
    Twigs = 15/60.0,
    ToiletPaper = 5/60.0,
}
---@class campingLightFireCategory
campingLightFireCategory = {
    Clothing = 15/60.0,
    Literature = 15/60.0,
}

