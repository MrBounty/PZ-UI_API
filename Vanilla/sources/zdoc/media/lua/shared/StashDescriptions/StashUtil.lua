--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 21/02/2017
-- Time: 09:18
-- To change this template use File | Settings | File Templates.
--

---@class StashUtil
StashUtil = {}

StashUtil.newStash = function(name, type, item, customName)
    if not StashDescriptions then
        StashDescriptions = {};
    end
    local newStash = {};
    newStash.name = name;
    newStash.type = type;
    newStash.item = item;
    newStash.customName = customName;
    newStash.addStamp = StashUtil.addStamp;
    newStash.addContainer = StashUtil.addContainer;
    table.insert(StashDescriptions, newStash);
    return newStash;
end

function StashUtil:addStamp(symbol,text,mapX,mapY,r,g,b)
    if not self.annotations then
        self.annotations = {};
    end
    local annotation = {};
    annotation.symbol = symbol;
    annotation.text = text;
    annotation.x = mapX;
    annotation.y = mapY;
    annotation.r = r;
    annotation.g = g;
    annotation.b = b;
    table.insert(self.annotations, annotation);
end

function StashUtil:addContainer(containerType,containerSprite,containerItem,room,x,y,z)
    if not self.containers then
        self.containers = {};
    end
    local container = {};
    container.contX = x;
    container.contY = y;
    container.contZ = z;
    container.room = room;
    container.containerType = containerType;
    container.containerSprite = containerSprite;
    container.containerItem = containerItem;
    table.insert(self.containers, container);
end
