--***********************************************************
--**                LEMMY/ROBERT JOHNSON                   **
--***********************************************************
-- made some changes to support merging of distribution tables for itemzed/mod, the SuburbsDistributions spawn table contents are moved to Distributions.lua. Turbo.

-- used to not spawn items inside item container (bags) in this room
NoContainerFillRooms = { armysurplus = {}, armysurplustorage = {}, bookstore = {}, camping = {}, campingstorage = {}, clothingstore = {}, clothingstorage = {}, hunting = {}, jayschicken_dining = {}, jayschicken_kitchen = {}, musicstore = {}, pawnshop = {}, pawnshopoffice = {}, pawnshopstorage = {}, spiffo_dining = {}, spiffoskitchen = {} }

WeaponUpgrades = {
    VarmintRifle = {"x2Scope", "x4Scope", "x8Scope", "AmmoStraps", "Sling", "FiberglassStock", "RecoilPad", "IronSight"},
    HuntingRifle = {"x2Scope", "x4Scope", "x8Scope", "AmmoStraps", "Sling", "FiberglassStock", "RecoilPad"},
    Shotgun = {"AmmoStraps", "Sling", "ChokeTubeFull", "ChokeTubeImproved"},
    Pistol = {"Laser", "RedDot", "IronSight"}
}


-- can be used to clear all items from distribution tables.
function ClearAllDistributionItems(_dist, _dorecursive)
    local recursive = (_dorecursive==nil) or _dorecursive

    for k,v in pairs(_dist) do
        if type(_dist[k])=="table" then
            if type(k)=="string" and k=="items" then
                _dist[k] = {}
            else
                if recursive then
                    ClearAllDistributionItems(_dist[k], _dorecursive)
                end
            end
        end
    end
end

-- optionally set 'chance' to false if you want to ommit it but do want to set dorecursive
function RemoveItemFromDistribution(_dist, _item, _chance, _dorecursive)
    local recursive = (_dorecursive==nil) or _dorecursive
    local doChance = type(_chance)=="number"

    for k,v in pairs(_dist) do
        if type(_dist[k])=="table" then
            if type(k)=="string" and k=="items" then
                local validItem = false
                local isChance = true
                for i=#_dist[k],1,-1 do
                    local val = _dist[k][i]
                    if isChance then
                        validItem = not doChance
                        if doChance and type(val)=="number" and val==_chance then
                            validItem = true
                        end
                    else
                        if validItem and type(val)=="string" and val==_item then
                            table.remove(_dist[k], i+1)
                            table.remove(_dist[k], i)
                        end
                    end


                    isChance = not isChance
                    if isChance then
                        validItem = false
                    end
                end
            else
                if recursive then
                    RemoveItemFromDistribution(_dist[k], _item, _chance, _dorecursive)
                end
            end
        end
    end
end

-- set 'chance' or 'replaceChance' to false if you want to ommit either or both of them.
function ReplaceItemInDistribution(_dist, _item, _chance, _replacement, _replaceChance, _dorecursive)
    local recursive = (_dorecursive==nil) or _dorecursive
    local doChance = type(_chance)=="number"
    local doReplaceChance = type(_replaceChance)=="number"

    for k,v in pairs(_dist) do
        if type(_dist[k])=="table" then
            if type(k)=="string" and k=="items" then
                local validItem = false
                local isChance = true
                for i=#_dist[k],1,-1 do
                    local val = _dist[k][i]
                    if isChance then
                        validItem = not doChance
                        if doChance and type(val)=="number" and val==_chance then
                            validItem = true
                        end
                    else
                        if validItem and type(val)=="string" and val==_item then
                            _dist[k][i] = _replacement
                            if doReplaceChance then
                                _dist[k][i] = _replaceChance
                            end
                        end
                    end


                    isChance = not isChance
                    if isChance then
                        validItem = false
                    end
                end
            else
                if recursive then
                    ReplaceItemInDistribution(_dist[k], _item, _chance, _replacement, _replaceChance, _dorecursive)
                end
            end
        end
    end
end

function MergeDistributionRecursive(_orig, _mod)
    for k,v in pairs(_mod) do
        if _orig[k]~=nil then
            if type(_mod[k])=="table" then
                if type(k)=="string" and k=="items" then
                    for _,v2 in ipairs(_mod[k]) do
                        table.insert(_orig[k], v2)
                    end
                else
                    MergeDistributionRecursive(_orig[k], _mod[k])
                end
            end
        else --if original doesnt have the table the mod has, add it.
            _orig[k] = _mod[k]
        end
    end
end

local function preDistributionMerge()
end


local function mergeDistributions()
    SuburbsDistributions = Distributions[1] -- the games distribition table should always be the first in table.

    -- if there are modded distribution tables merge them into the main suburbsdistributions
    if #Distributions>1 then
        print("### Merging distribution tables ###")
        for key,dist in pairs(Distributions) do
            if key > 1 then
                print("Merging distribution addon #",key-1)
                MergeDistributionRecursive(SuburbsDistributions, dist)
            end
        end
        print("###################################")
    end

    SuburbsDistributions.cells = SuburbsDistributions.prisoncells
    SuburbsDistributions.clinic = SuburbsDistributions.medical
    SuburbsDistributions.clothesstorage = SuburbsDistributions.clothingstorage
    SuburbsDistributions.clothesstore = SuburbsDistributions.clothingstore
    SuburbsDistributions.clothesstorestorage = SuburbsDistributions.clothingstorage
    SuburbsDistributions.dinnerkitchen = SuburbsDistributions.dinerkitchen
    SuburbsDistributions.garage = SuburbsDistributions.garagestorage
    SuburbsDistributions.glassesstore = SuburbsDistributions.optometrist
    SuburbsDistributions.grocers = SuburbsDistributions.grocery
    SuburbsDistributions.hairdresser = SuburbsDistributions.aesthetic
    SuburbsDistributions.icecreamstand = SuburbsDistributions.icecreamkitchen
    SuburbsDistributions.knoxbutcher = SuburbsDistributions.butcher
    SuburbsDistributions.laumdromat = SuburbsDistributions.laundry
    SuburbsDistributions.laundromat = SuburbsDistributions.laundry
    SuburbsDistributions.locker = SuburbsDistributions.changeroom
    SuburbsDistributions.lockerroom = SuburbsDistributions.changeroom
    SuburbsDistributions.lockers = SuburbsDistributions.changeroom
    SuburbsDistributions.medclinic = SuburbsDistributions.medical
    SuburbsDistributions.medicaloffice = SuburbsDistributions.medical
    SuburbsDistributions.motelbedroom = SuburbsDistributions.motelroom
    SuburbsDistributions.restaurant_dining = SuburbsDistributions.dining
    SuburbsDistributions.storageclothes = SuburbsDistributions.clothingstorage
    SuburbsDistributions.tacokitchen = SuburbsDistributions.mexicankitchen
    
    --print("###################################")
end

local function postDistributionMerge()
    --RemoveItemFromDistribution(SuburbsDistributions, "Razor")

    --DeepPrintDistributionTable(SuburbsDistributions,"")
end

function DeepPrintDistributionTable (_dist, tab)
    if type(_dist) == "table" then
        for k,v in pairs(_dist) do
            print(tab .. " " .. tostring(k))
            DeepPrintTable(v, tab.."  ")
        end
    else
        print(tab .. tostring(_dist))
    end
end

Events.OnPreDistributionMerge.Add(preDistributionMerge)
Events.OnDistributionMerge.Add(mergeDistributions)
Events.OnPostDistributionMerge.Add(postDistributionMerge)
