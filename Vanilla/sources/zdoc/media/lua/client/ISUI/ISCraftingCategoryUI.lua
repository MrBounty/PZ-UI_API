--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

---@class ISCraftingCategoryUI : ISPanelJoypad
ISCraftingCategoryUI = ISPanelJoypad:derive("ISCraftingCategoryUI");
ISCraftingCategoryUI.instance = nil;
ISCraftingCategoryUI.SMALL_FONT_HGT = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
ISCraftingCategoryUI.MEDIUM_FONT_HGT = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()

function ISCraftingCategoryUI:initialise()
    ISPanelJoypad.initialise(self);
    self:create();
end

function ISCraftingCategoryUI:update()
    if not self.parent:getIsVisible() then return; end
    local text = string.trim(self.filterEntry:getInternalText());
    local filterAll = self.filterAll:isSelected(1);

    if (text ~= self.lastText) or (filterAll ~= self.filteringAll) then
        self.filteringAll = filterAll;
        self:filter();
        self.lastText = text;
        self:syncAllFilters();
    end

    self.filterAll:setX(self.width / 3 - self.filterAll.width)
    self.filterEntry:setWidth(self.filterAll.x - 5 - self.filterEntry.x)
    self.recipes:setWidth(self.width / 3)
end

function ISCraftingCategoryUI:prerender()
    self.recipes.backgroundColor.a = 0.8
    self.recipes.doDrawItem = ISCraftingCategoryUI.drawRecipesMap
end

function ISCraftingCategoryUI:filter()
    self.recipes:clear();
    self.recipes:setScrollHeight(0);
    local items = self.parent.recipesList[self.category];
    if self.filteringAll then
        items = self.parent.allRecipesList;
    end
    if items == nil then
        return
    end
    local filterText = string.trim(self.filterEntry:getInternalText())
    if filterText == "" then
        for i,item in ipairs(items) do
            self.recipes:addItem(i,item);
        end
    else
        filterText = string.lower(filterText)
        for i,item in ipairs(items) do
            if string.contains(string.lower(item.recipe:getName()), filterText) then
                self.recipes:addItem(i,item);
            end
        end
    end

    table.sort(self.recipes.items, function(a,b)
        a = a.item
        b = b.item
        if a.available and not b.available then return true end
        if not a.available and b.available then return false end
        if a.customRecipeName and not b.customRecipeName then return true end
        if not a.customRecipeName and b.customRecipeName then return false end
        return not string.sort(a.recipe:getName(), b.recipe:getName())
    end)
end

function ISCraftingCategoryUI:syncAllFilters()
    local text = self.filterEntry:getInternalText()
    local filterAll = self.filterAll:isSelected(1)
    for _,ui in ipairs(self.parent.categories) do
        if (ui ~= self) and (not ui:isVisible()) then
            if text ~= ui.filterEntry:getInternalText() then
                ui.filterEntry:setText(text)
            end
            if filterAll ~= ui.filterAll:isSelected(1) then
                ui.filterAll:setSelected(1, filterAll)
            end
        end
    end
end

function ISCraftingCategoryUI:drawRecipesMap(y, item, alt)
    local baseItemDY = 0
    if item.item.customRecipeName then
        baseItemDY = self.SMALL_FONT_HGT
        item.height = self.itemheight + baseItemDY
    end

    if y + self:getYScroll() >= self.height then return y + item.height end
    if y + item.height + self:getYScroll() <= 0 then return y + item.height end

    local a = 0.9;

    if not item.item.available then
        a = 0.3;
    end

    self:drawRectBorder(0, (y), self:getWidth(), item.height - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), item.height - 1, 0.3, 0.7, 0.35, 0.15);
    end

    self:drawText(item.item.recipe:getName(), 6, y + 2, 1, 1, 1, a, UIFont.Medium);
    if item.item.customRecipeName then
        self:drawText(item.item.customRecipeName, 6, y + 2 + self.MEDIUM_FONT_HGT, 1, 1, 1, a, UIFont.Small);
    end

    local textWidth = 0;
    if item.item.texture then
        local texWidth = item.item.texture:getWidthOrig();
        local texHeight = item.item.texture:getHeightOrig();
        if texWidth <= 32 and texHeight <= 32 then
            self:drawTexture(item.item.texture,6+(32-texWidth)/2,y+2+self.MEDIUM_FONT_HGT+baseItemDY+(32-texHeight)/2,a,1,1,1);
        else
            self:drawTextureScaledAspect(item.item.texture,6,y+2+self.MEDIUM_FONT_HGT+baseItemDY,32,32,a,1,1,1);
        end
        local name = item.item.evolved and item.item.resultName or item.item.itemName
        self:drawText(name, texWidth + 20, y + 2 + self.MEDIUM_FONT_HGT + baseItemDY + (32 - self.SMALL_FONT_HGT) / 2 - 2, 1, 1, 1, a, UIFont.Small);
    end

    local categoryUI = self.parent
    local favoriteStar = nil
    local favoriteAlpha = a
    if item.index == self.mouseoverselected and not self:isMouseOverScrollBar() then
        if self:getMouseX() >= categoryUI:getFavoriteX() then
            favoriteStar = item.item.favorite and categoryUI.favCheckedTex or categoryUI.favNotCheckedTex
            favoriteAlpha = 0.9
        else
            favoriteStar = item.item.favorite and categoryUI.favoriteStar or categoryUI.favNotCheckedTex
            favoriteAlpha = item.item.favorite and a or 0.3
        end
    elseif item.item.favorite then
        favoriteStar = categoryUI.favoriteStar
    end
    if favoriteStar then
        self:drawTexture(favoriteStar, categoryUI:getFavoriteX() + categoryUI.favPadX, y + (item.height / 2 - favoriteStar:getHeight() / 2), favoriteAlpha,1,1,1);
    end

    return y + item.height;
end

function ISCraftingCategoryUI:getFavoriteX()
    -- scrollbar width=17 but only 13 pixels wide visually
    local scrollBarWid = self.recipes:isVScrollBarVisible() and 13 or 0
    return self.recipes:getWidth() - scrollBarWid - self.favPadX - self.favWidth - self.favPadX
end

function ISCraftingCategoryUI:isMouseOverFavorite(x)
    return (x >= self:getFavoriteX()) and not self.recipes:isMouseOverScrollBar()
end

function ISCraftingCategoryUI:onMouseDown_Recipes(x, y)
    local row = self:rowAt(x, y)
    if row == -1 then return end
    if self.parent:isMouseOverFavorite(x) then
        self.parent:addToFavorite(false)
    elseif not self:isMouseOverScrollBar() then
        self.selected = row;
    end
end

function ISCraftingCategoryUI:create()
    local fontHgtSmall = self.SMALL_FONT_HGT
    local entryHgt = fontHgtSmall + 2 * 2

    self.recipes = ISScrollingListBox:new(1, entryHgt + 25, self.width / 3, self.height - (entryHgt + 25));
    self.recipes:initialise();
    self.recipes:instantiate();
    self.recipes:setAnchorRight(false) -- resize in update()
    self.recipes:setAnchorBottom(true)
    self.recipes.itemheight = 2 + self.MEDIUM_FONT_HGT + 32 + 4;
    self.recipes.selected = 0;
    self.recipes.doDrawItem = ISCraftingCategoryUI.drawRecipesMap;
    self.recipes.onMouseDown = ISCraftingCategoryUI.onMouseDown_Recipes;
    self.recipes.onMouseDoubleClick = ISCraftingCategoryUI.onMouseDoubleClick_Recipes;
    self.recipes.joypadParent = self;
--    self.recipes.resetSelectionOnChangeFocus = true;
    self.recipes.drawBorder = false;
    self:addChild(self.recipes);

    self.recipes.SMALL_FONT_HGT = self.SMALL_FONT_HGT
    self.recipes.MEDIUM_FONT_HGT = self.MEDIUM_FONT_HGT


    self.filterLabel = ISLabel:new(4, 2, entryHgt, getText("IGUI_CraftUI_Name_Filter"),1,1,1,1,UIFont.Small, true);
    self:addChild(self.filterLabel);

    local width = ((self.width/3) - getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_CraftUI_Name_Filter"))) - 98;
    self.filterEntry = ISTextEntryBox:new("", getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_CraftUI_Name_Filter")) + 9, 2, width, fontHgtSmall);
    self.filterEntry:initialise();
    self.filterEntry:instantiate();
    self.filterEntry:setText("");
    self.filterEntry:setClearButton(true);
    self:addChild(self.filterEntry);
    self.lastText = self.filterEntry:getInternalText();
    
    self.filterAll = ISTickBox:new(self.filterEntry.x + self.filterEntry.width + 5, 2, 20, entryHgt, "", self, self.onFilterAll)
    self.filterAll:initialise()
    self.filterAll:addOption(getText("IGUI_FilterAll"));
    self.filterAll:setWidthToFit()
    self:addChild(self.filterAll)
end

function ISCraftingCategoryUI:onFilterAll(index, selected)
--    self.filteringAll = selected;
--    self:filter();
end

function ISCraftingCategoryUI:addToFavorite(fromKeyboard)
    if self.recipes:size() == 0 then return end
    local selectedIndex = self.recipes:rowAt(self.recipes:getMouseX(), self.recipes:getMouseY());
    if fromKeyboard == true then
        selectedIndex = self.recipes.selected;
    end
    local selectedItem = self.recipes.items[selectedIndex].item;
    selectedItem.favorite = not selectedItem.favorite;
    if self.character then
        self.character:getModData()[self.craftingUI:getFavoriteModDataString(selectedItem.recipe)] = selectedItem.favorite;
    end
    self.craftingUI:refresh();
end

function ISCraftingCategoryUI:onMouseDoubleClick_Recipes(x, y)
    local row = self:rowAt(x, y)
    if row == -1 then return end
    if x < self.parent:getFavoriteX() then
        self.parent.parent:craft()
    elseif not self:isMouseOverScrollBar() then
        self.parent:addToFavorite(false)
    end
end

function ISCraftingCategoryUI:new (x, y, width, height, craftingUI)
    local o = {};
    o = ISPanelJoypad:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self;
    o.craftingUI = craftingUI
    o.character = craftingUI.character;
    o.favoriteStar = getTexture("media/ui/FavoriteStar.png");
    o.favCheckedTex = getTexture("media/ui/FavoriteStarChecked.png");
    o.favNotCheckedTex = getTexture("media/ui/FavoriteStarUnchecked.png");
    o.favPadX = 20;
    o.favWidth = o.favoriteStar and o.favoriteStar:getWidth() or 13
    o:noBackground();
    ISCraftingCategoryUI.instance = o;
    return o;
end
