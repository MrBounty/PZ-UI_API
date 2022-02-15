--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISCollapsableWindow"

ISCraftingUI = ISCollapsableWindow:derive("ISCraftingUI");
ISCraftingUI.instance = nil;
ISCraftingUI.largeFontHeight = getTextManager():getFontHeight(UIFont.Large)
ISCraftingUI.mediumFontHeight = getTextManager():getFontHeight(UIFont.Medium)
ISCraftingUI.smallFontHeight = getTextManager():getFontHeight(UIFont.Small)
ISCraftingUI.bottomInfoHeight = ISCraftingUI.smallFontHeight * 2
ISCraftingUI.qwertyConfiguration = true;
ISCraftingUI.bottomTextSpace = "     ";
ISCraftingUI.leftCategory = Keyboard.KEY_LEFT;
ISCraftingUI.rightCategory = Keyboard.KEY_RIGHT;
ISCraftingUI.upArrow = Keyboard.KEY_UP;
ISCraftingUI.downArrow = Keyboard.KEY_DOWN;

function ISCraftingUI:getRecipeListBox()
    return self.panel.activeView.view.recipes
end

function ISCraftingUI:setVisible(bVisible)
    self.javaObject:setVisible(bVisible);
    self.javaObject:setEnabled(bVisible)

    if not bVisible then -- save the selected index
        self.selectedIndex = {};
        for i,v in ipairs(self.categories) do
            self.selectedIndex[v.category] = v.recipes.selected;
        end
    end
--    if getPlayer() then
--        self.character:setBlockMovement(bVisible);
--    end
    if bVisible and self.recipesList then
        self:refresh();
    end
    -- load saved selected index
    if bVisible then
        for i,v in ipairs(self.categories) do
            if self.selectedIndex[v.category] then
                v.recipes.selected = self.selectedIndex[v.category];
            end
        end
    end

    self.craftInProgress = false;
    local recipeListBox = self:getRecipeListBox()
    recipeListBox:ensureVisible(recipeListBox.selected);
    if bVisible then
        self.knownRecipes = RecipeManager.getKnownRecipesNumber(self.character);
        self.totalRecipes = getAllRecipes():size();
    end
--    print("KNOWN RECIPES", self.knownRecipes, self.totalRecipes);
end

function ISCraftingUI:refresh()
    local recipeListBox = self:getRecipeListBox()
    local selectedItem = recipeListBox.items[recipeListBox.selected];
    if selectedItem then selectedItem = selectedItem.item.recipe end
    local selectedView = self.panel.activeView.name;
    self:getContainers();
    self:populateRecipesList();
    self:sortList();
    for i=#self.categories,1,-1 do
        local categoryUI = self.categories[i];
        -- Remove unknown categories (due to recipes being forgotten).
        local found = false;
        for j=1,#self.recipesListH do
            if self.recipesListH[j] == categoryUI.category then
                found = true;
                break;
            end
        end
        if not found then
            self.panel:removeView(categoryUI);
            table.remove(self.categories, i);
        else
            categoryUI:filter();
        end
    end
    self.panel:activateView(selectedView);

    if selectedItem then
        for i,item in ipairs(recipeListBox.items) do
            if item.item.recipe == selectedItem then
                recipeListBox.selected = i;
                break;
            end
        end
    end

    -- create the new categories if needed
    local k
    for k = 1 , #self.recipesListH, 1 do
        local i = self.recipesListH[k]
        local v = self.recipesList[i]
    --for i,v in pairs(self.recipesList) do
        local found = false;
        for k,l in ipairs(self.categories) do
            if i == l.category then
                found = true;
                break;
            end
        end
        if not found then
            local cat1 = ISCraftingCategoryUI:new(0, 0, self.width, self.panel.height - self.panel.tabHeight, self);
            cat1:initialise();
            local catName = getTextOrNull("IGUI_CraftCategory_"..i) or i
            self.panel:addView(catName, cat1);
            cat1.infoText = getText("UI_CraftingUI");
            cat1.parent = self;
            cat1.category = i;
            for s,d in ipairs(v) do
                cat1.recipes:addItem(s,d);
            end
            table.insert(self.categories, cat1);
        end
    end
    -- switch panel if there's no item in this list
    if #recipeListBox.items == 0 then
        self.panel:activateView(getText("IGUI_CraftCategory_General"));
    end
--    self:refreshTickBox();
    self:refreshIngredientList()
end

function ISCraftingUI:isWaterSource(item, count)
    -- Fk'n rounding differences between Java and Lua broke simple getUsedDelta()/getUseDelta() here, so I added getDrainableUsesInt()
    return instanceof(item, "DrainableComboItem") and item:isWaterSource() and item:getDrainableUsesInt() >= count
end

function ISCraftingUI:transferItems()
    local result = {}
    local recipeListBox = self:getRecipeListBox()
    local recipe = recipeListBox.items[recipeListBox.selected].item.recipe;
    local items = RecipeManager.getAvailableItemsNeeded(recipe, self.character, self.containerList, nil, nil);
    if items:isEmpty() then return result end;
    for i=1,items:size() do
        local item = items:get(i-1)
        table.insert(result, item)
        if not recipe:isCanBeDoneFromFloor() then
            if item:getContainer() ~= self.character:getInventory() then
                ISTimedActionQueue.add(ISInventoryTransferAction:new(self.character, item, item:getContainer(), self.character:getInventory(), nil));
            end
        end
    end
    return result
end

function ISCraftingUI:getAvailableItemsType()
    local result = {};
    local recipeListBox = self:getRecipeListBox()
    local recipe = recipeListBox.items[recipeListBox.selected].item.recipe;
    local items = RecipeManager.getAvailableItemsAll(recipe, self.character, self.containerList, nil, nil);
    for i=0, recipe:getSource():size()-1 do
        local source = recipe:getSource():get(i);
        local sourceItemTypes = {};
        for k=1,source:getItems():size() do
            local sourceFullType = source:getItems():get(k-1);
            sourceItemTypes[sourceFullType] = true;
        end
        for x=0,items:size()-1 do
            local item = items:get(x)
            if sourceItemTypes["Water"] and self:isWaterSource(item, source:getCount()) then
                result["Water"] = (result["Water"] or 0) + item:getDrainableUsesInt()
            elseif sourceItemTypes[item:getFullType()] then
                local count = 1
                if not source:isDestroy() and item:IsDrainable() then
                    count = item:getDrainableUsesInt()
                end
                if not source:isDestroy() and instanceof(item, "Food") then
                    if source:getUse() > 0 then
                        count = -item:getHungerChange() * 100
                    end
                end
                result[item:getFullType()] = (result[item:getFullType()] or 0) + count;
            end
        end
    end
    return result;
end

function ISCraftingUI:initialise()
    ISCollapsableWindow.initialise(self);
end

function ISCraftingUI:close()
	ISCollapsableWindow.close(self)
	if JoypadState.players[self.playerNum+1] then
		setJoypadFocus(self.playerNum, nil)
	end
end

ISCraftingUI.sortByName = function(a,b)
    return string.sort(b.recipe:getName(), a.recipe:getName());
end

function ISCraftingUI:getContainers()
    if not self.character then return end
    -- get all the surrounding inventory of the player, gonna check for the item in them too
    self.containerList = ArrayList.new();
    for i,v in ipairs(getPlayerInventory(self.playerNum).inventoryPane.inventoryPage.backpacks) do
        --        if v.inventory ~= self.character:getInventory() then -- owner inventory already check in RecipeManager
        self.containerList:add(v.inventory);
        --        end
    end
    for i,v in ipairs(getPlayerLoot(self.playerNum).inventoryPane.inventoryPage.backpacks) do
        self.containerList:add(v.inventory);
    end
end

function ISCraftingUI:refreshTickBox()
    local recipeListBox = self:getRecipeListBox()
    local selectedItem = recipeListBox.items[recipeListBox.selected].item;
    self.tickBox.options = {};
    self.tickBox.optionCount = 1;
    for m,l in ipairs(selectedItem.multipleItems) do
        self.tickBox:addOption(l.name, nil, l.texture)
        if m == 1 then
            self.tickBox:setSelected(m, true)
        end
    end
end


function ISCraftingUI:drawNonEvolvedIngredient(y, item, alt)

    if y + self:getYScroll() >= self.height then return y + self.itemheight end
    if y + self.itemheight + self:getYScroll() <= 0 then return y + self.itemheight end

    if not self.parent.recipeListHasFocus and self.selected == item.index then
        self:drawRectBorder(1, y, self:getWidth()-2, self.itemheight, 1.0, 0.5, 0.5, 0.5);
    end

    if item.item.multipleHeader then
        local r,g,b = 1,1,1
        if not item.item.available then
            r,g,b = 0.54,0.54,0.54
        end
        self:drawText(item.text, 12, y + 2, r, g, b, 1, self.font)
        self:drawTexture(item.item.texture, 4, y + (item.height - item.item.texture:getHeight()) / 2 - 2, 1,1,1,1)
    else
        local r,g,b
        local r2,g2,b2,a2
        local typesAvailable = item.item.selectedItem.typesAvailable
        if typesAvailable and (not typesAvailable[item.item.fullType] or typesAvailable[item.item.fullType] < item.item.count) then
            r,g,b = 0.54,0.54,0.54;
            r2,g2,b2,a2 = 1,1,1,0.3;
        else
            r,g,b = 1,1,1;
            r2,g2,b2,a2 = 1,1,1,0.9;
        end

        local imgW = 20
        local imgH = 20
        local dx = 6 + (item.item.multiple and 10 or 0)
        
        self:drawText(item.text, dx + imgW + 4, y + (item.height - ISCraftingUI.smallFontHeight) / 2, r, g, b, 1, self.font)
        
        if item.item.texture then
            local texWidth = item.item.texture:getWidth()
            local texHeight = item.item.texture:getHeight()
            self:drawTextureScaledAspect(item.item.texture, dx, y + (self.itemheight - imgH) / 2, 20, 20, a2,r2,g2,b2)
        end
    end

    return y + self.itemheight;
end

-- Return true if item2's type is in item1's getClothingExtraItem() list.
function ISCraftingUI:isExtraClothingItemOf(item1, item2)
    local scriptItem = getScriptManager():FindItem(item1.fullType)
    if not scriptItem then
        return false
    end
    local extras = scriptItem:getClothingItemExtra()
    if not extras then
        return false
    end
    local moduleName = scriptItem:getModule():getName()
    for i=1,extras:size() do
        local extra = extras:get(i-1)
        local fullType = moduleDotType(moduleName, extra)
        if item2.fullType == fullType then
            return true
        end
    end
    return false
end

function ISCraftingUI:removeExtraClothingItemsFromList(index, item, itemList)
    for k=#itemList,index,-1 do
        local item2 = itemList[k]
        if self:isExtraClothingItemOf(item, item2) then
            table.remove(itemList, k)
        end
    end
end

-- Non-evolved recipes
function ISCraftingUI:refreshIngredientPanel()
    local hasFocus = not self.recipeListHasFocus
    self.recipeListHasFocus = true

    self.ingredientPanel:setVisible(false)

    local recipeListbox = self:getRecipeListBox()
    if not recipeListbox.items or #recipeListbox.items == 0 or not recipeListbox.items[recipeListbox.selected] then return end
    local selectedItem = recipeListbox.items[recipeListbox.selected].item;
    if not selectedItem or selectedItem.evolved then return end

    selectedItem.typesAvailable = self:getAvailableItemsType()

    self.recipeListHasFocus = not hasFocus
    self.ingredientPanel:setVisible(true) 

    self.ingredientPanel:clear()
    
    -- Display single-item sources before multi-item sources
    local sortedSources = {}
    for _,source in ipairs(selectedItem.sources) do
        table.insert(sortedSources, source)
    end
    table.sort(sortedSources, function(a,b) return #a.items == 1 and #b.items > 1 end)

    for _,source in ipairs(sortedSources) do
        local available = {}
        local unavailable = {}

        for _,item in ipairs(source.items) do
            local data = {}
            data.selectedItem = selectedItem
            data.name = item.name
            data.texture = item.texture
            data.fullType = item.fullType
            data.count = item.count
            data.recipe = selectedItem.recipe
            data.multiple = #source.items > 1
            if selectedItem.typesAvailable and (not selectedItem.typesAvailable[item.fullType] or selectedItem.typesAvailable[item.fullType] < item.count) then
                table.insert(unavailable, data)
            else
                table.insert(available, data)
            end
        end
        table.sort(available, function(a,b) return not string.sort(a.name, b.name) end)
        table.sort(unavailable, function(a,b) return not string.sort(a.name, b.name) end)
        
        if #source.items > 1 then
            local data = {}
            data.selectedItem = selectedItem
            data.texture = self.TreeExpanded
            data.multipleHeader = true
            data.available = #available > 0
            self.ingredientPanel:addItem(getText("IGUI_CraftUI_OneOf"), data)
        end

        -- Hack for "Dismantle Digital Watch" and similar recipes.
        -- Recipe sources include both left-hand and right-hand versions of the same item.
        -- We only want to display one of them.
        ---[[
        for j=1,#available do
            local item = available[j]
            self:removeExtraClothingItemsFromList(j+1, item, available)
        end

        for j=1,#available do
            local item = available[j]
            self:removeExtraClothingItemsFromList(1, item, unavailable)
        end

        for j=1,#unavailable do
            local item = unavailable[j]
            self:removeExtraClothingItemsFromList(j+1, item, unavailable)
        end
        --]]

        for k,item in ipairs(available) do
            self.ingredientPanel:addItem(item.name, item)
        end
        for k,item in ipairs(unavailable) do
            self.ingredientPanel:addItem(item.name, item)
        end
    end

    self.refreshTypesAvailableMS = getTimestampMs()

    self.ingredientPanel.doDrawItem = ISCraftingUI.drawNonEvolvedIngredient
end

function ISCraftingUI:drawEvolvedIngredient(y, item, alt)
    if y + self:getYScroll() >= self.height then return y + self.itemheight end
    if y + self.itemheight + self:getYScroll() <= 0 then return y + self.itemheight end

    local a = 0.9;
    if not item.item.available then
        a = 0.3;
    end

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    local imgW = 20
    local imgH = 20
    self:drawText(item.text, 6 + imgW + 4, y + (item.height - ISCraftingUI.smallFontHeight) / 2, 1, 1, 1, a, self.font);

    if item.item.texture then
        local texWidth = item.item.texture:getWidth();
        local texHeight = item.item.texture:getHeight();
        self:drawTextureScaledAspect(item.item.texture, 6, y + (self.itemheight - imgH) / 2, 20, 20, a,1,1,1);
    end

    if item.item.poison then
        if self.PoisonTexture then
            local textW = getTextManager():MeasureStringX(self.font, item.text)
            self:drawTexture(self.PoisonTexture, 6 + imgW + 4 + textW + 6, y + (self.itemheight - self.PoisonTexture:getHeight()) / 2, a, 1, 1, 1)
        end
    end
    
    return y + self.itemheight;
end

function ISCraftingUI:onDblClickIngredientListbox(data)
    if data and data.available then
        self:addItemInEvolvedRecipe(data)
    end
end

function ISCraftingUI:onAddRandomIngredient(button)
    self:addItemInEvolvedRecipe(button.list[ZombRand(1, #button.list+1)]);
end

function ISCraftingUI:onAddIngredient()
    local item = self.ingredientListbox.items[self.ingredientListbox.selected]
    if item and item.item.available then
        self:addItemInEvolvedRecipe(item.item);
    end
end

-- Evolved recipes only
function ISCraftingUI:refreshIngredientList()
    if not self.catListButtons then self.catListButtons = {}; end
    for i,v in ipairs(self.catListButtons) do
        v:setVisible(false);
        self:removeChild(v);
    end
    self.catListButtons = {};
    local hasFocus = not self.recipeListHasFocus
    self.recipeListHasFocus = true

    self.ingredientListbox:setVisible(false)

    local recipeListbox = self:getRecipeListBox()
    if not recipeListbox.items or #recipeListbox.items == 0 or not recipeListbox.items[recipeListbox.selected] then return end
    local selectedItem = recipeListbox.items[recipeListbox.selected].item;
    if not selectedItem or not selectedItem.evolved then return end

    self.recipeListHasFocus = not hasFocus
    self.ingredientListbox:setVisible(true)

    local available = {}
    local unavailable = {}
    for k,item in ipairs(selectedItem.items) do
        local data = {}
        data.available = item.available
        data.name = item.name
        data.texture = item.texture
        data.item = item.itemToAdd
        data.baseItem = selectedItem.baseItem
        data.recipe = selectedItem.recipe
        data.poison = item.poison
        if instanceof(item.itemToAdd, "Food") and item.itemToAdd:isFrozen() then
            item.available = false;
            data.available = false;
        end
        if item.available then
            table.insert(available, data)
        else
            table.insert(unavailable, data)
        end
    end
    table.sort(available, function(a,b) return not string.sort(a.name, b.name) end)
    table.sort(unavailable, function(a,b) return not string.sort(a.name, b.name) end)
    
    self.ingredientListbox:clear()
    -- check for every item category to add a "add random category" buttons in bottom of the ingredient list
    self.catList = {};
    for k,item in ipairs(available) do
        self.ingredientListbox:addItem(item.name, item)
        local foodType = item.item:IsFood() and item.item:getFoodType()
        if foodType then
            if not self.catList[foodType] then self.catList[foodType] = {}; end
            table.insert(self.catList[foodType], item);
        end
    end
    for k,item in ipairs(unavailable) do
        self.ingredientListbox:addItem(item.name, item)
    end
    
    local y = self.ingredientListbox:getY();
    for i,v in pairs(self.catList) do
        local button = ISButton:new(self.ingredientListbox:getX() + self.ingredientListbox:getWidth() + 10 , y ,50,20,getText("ContextMenu_AddRandom", getText("ContextMenu_FoodType_"..i)), self, ISCraftingUI.onAddRandomIngredient);
        button.list = self.catList[i];
        button:initialise()
        self:addChild(button);
        table.insert(self.catListButtons, button);
        y = y + 25;
    end
end

local function tableSize(table1)
    if not table1 then return 0 end
    local count = 0;
    for _,v in pairs(table1) do
        count = count + 1;
    end
    return count;
end

local function areTablesDifferent(table1, table2)
    local size1 = tableSize(table1)
    local size2 = tableSize(table2)
    if size1 ~= size2 then return true end
    if size1 == 0 then return false end
    for k1,v1 in pairs(table1) do
        if table2[k1] ~= v1 then
            return true
        end
    end
    return false
end

function ISCraftingUI:render()
    ISCollapsableWindow.render(self);
    if self.isCollapsed then return end
    local multipleItemEvolvedRecipes = {};
    self.addIngredientButton:setVisible(false);
    -- draw bottom infos
    local rh = self.resizable and self:resizeWidgetHeight() or 0
    self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), self.borderColor.a, self.borderColor.r,self.borderColor.g,self.borderColor.b);
    self.javaObject:DrawTextureScaledColor(nil, 0, self:getHeight() - rh - ISCraftingUI.bottomInfoHeight, self:getWidth(), 1, self.borderColor.r, self.borderColor.g,self.borderColor.b,self.borderColor.a);

    local textWidth = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_CraftingUI_KnownRecipes", self.knownRecipes,self.totalRecipes))
    self:drawText(getText("IGUI_CraftingUI_KnownRecipes", self.knownRecipes,self.totalRecipes), self.width - textWidth - 5, self.panel:getY() + self.panel.tabHeight + 8, 1,1,1,1, UIFont.Small);

    local textY = self:getHeight() - rh - ISCraftingUI.bottomInfoHeight + (ISCraftingUI.bottomInfoHeight - ISCraftingUI.smallFontHeight) / 2
    local buttonSize = 24
    local buttonSpace = 8
    local buttonY = self:getHeight() - rh - ISCraftingUI.bottomInfoHeight + (ISCraftingUI.bottomInfoHeight - buttonSize) / 2
    local spacing = 32 
    if self.drawJoypadFocus and self.ingredientListbox:getIsVisible() then
        local width1 = buttonSize + buttonSpace + getTextManager():MeasureStringX(UIFont.Small, self.LabelAddIngredient)
        local width3 = buttonSize + buttonSpace + getTextManager():MeasureStringX(UIFont.Small, self.LabelFavorite)
        local width4 = buttonSize + buttonSpace + getTextManager():MeasureStringX(UIFont.Small, self.LabelClose)
        local totalWidth = width1 + width3 + width4 + spacing * 2
        local left = (self.width - totalWidth) / 2

        self:drawTextureScaled(Joypad.Texture.AButton, left, buttonY, buttonSize, buttonSize, 1, 1, 1, 1)
        self:drawText(self.LabelAddIngredient, left + buttonSize + buttonSpace, textY, 1, 1, 1, 1, UIFont.Small)
        left = left + width1 + spacing

        self:drawTextureScaled(Joypad.Texture.YButton, left, buttonY, buttonSize, buttonSize, 1, 1, 1, 1)
        self:drawText(self.LabelFavorite, left + buttonSize + buttonSpace, textY, 1, 1, 1, 1, UIFont.Small)
        left = left + width3 + spacing

        self:drawTextureScaled(Joypad.Texture.BButton, left, buttonY, buttonSize, buttonSize, 1, 1, 1, 1)
        self:drawText(self.LabelClose, left + buttonSize + buttonSpace, textY, 1, 1, 1, 1, UIFont.Small)
    elseif self.drawJoypadFocus then
        local width1 = buttonSize + buttonSpace + getTextManager():MeasureStringX(UIFont.Small, self.LabelCraftOne)
        local width2 = buttonSize + buttonSpace + getTextManager():MeasureStringX(UIFont.Small, self.LabelCraftAll)
        local width3 = buttonSize + buttonSpace + getTextManager():MeasureStringX(UIFont.Small, self.LabelFavorite)
        local width4 = buttonSize + buttonSpace + getTextManager():MeasureStringX(UIFont.Small, self.LabelClose)
        local totalWidth = width1 + width2 + width3 + width4 + spacing * 3
        local left = (self.width - totalWidth) / 2

        self:drawTextureScaled(Joypad.Texture.AButton, left, buttonY, buttonSize, buttonSize, 1, 1, 1, 1)
        self:drawText(self.LabelCraftOne, left + buttonSize + buttonSpace, textY, 1, 1, 1, 1, UIFont.Small)
        left = left + width1 + spacing

        self:drawTextureScaled(Joypad.Texture.XButton, left, buttonY, buttonSize, buttonSize, 1, 1, 1, 1)
        self:drawText(self.LabelCraftAll, left + buttonSize + buttonSpace, textY, 1, 1, 1, 1, UIFont.Small)
        left = left + width2 + spacing

        self:drawTextureScaled(Joypad.Texture.YButton, left, buttonY, buttonSize, buttonSize, 1, 1, 1, 1)
        self:drawText(self.LabelFavorite, left + buttonSize + buttonSpace, textY, 1, 1, 1, 1, UIFont.Small)
        left = left + width3 + spacing

        self:drawTextureScaled(Joypad.Texture.BButton, left, buttonY, buttonSize, buttonSize, 1, 1, 1, 1)
        self:drawText(self.LabelClose, left + buttonSize + buttonSpace, textY, 1, 1, 1, 1, UIFont.Small)
    else
        local text = self.ingredientListbox:getIsVisible() and self.bottomInfoText2 or self.bottomInfoText1

        local noteX = 0
        local noteWidth = self.width
        if noteWidth ~= self.keysRichText.width then
            self.keysRichText:setWidth(noteWidth)
            self.keysRichText.textDirty = true
        end
        if text ~= self.keysText then
            self.keysText = text
            self.keysRichText:setText(" <CENTRE> " .. text)
            self.keysRichText.textDirty = true
        end
        local noteY = self:getHeight() - rh - ISCraftingUI.bottomInfoHeight
        noteY = noteY + (ISCraftingUI.bottomInfoHeight - self.keysRichText.height) / 2
        self.keysRichText:render(noteX, noteY, self)
    end

    local noteX = self:getWidth() / 3 + 10
    local noteWidth = self.width - 10 - noteX
    if noteWidth ~= self.noteRichText.width then
        self.noteRichText:setWidth(noteWidth)
        self.noteRichText.textDirty = true
    end
    local noteY = self:getHeight() - rh - ISCraftingUI.bottomInfoHeight - self.noteRichText.height - 4
    if noteY >= self.craftOneButton:getBottom() + 10 then
        self.noteRichText:render(noteX, noteY, self)
    end

    local recipeListBox = self:getRecipeListBox()
    if not recipeListBox.items or #recipeListBox.items == 0 or not recipeListBox.items[recipeListBox.selected] then
        self.craftOneButton:setVisible(false);
        self.craftAllButton:setVisible(false);
        self.debugGiveIngredientsButton:setVisible(false);
        self.ingredientPanel:setVisible(false);
        self.ingredientListbox:setVisible(false);
        self.selectedRecipeItem = nil
        return
    end
    
    -- draw recipes infos
    local x = self:getWidth()/3 + 80;
    local y = 110;
    local selectedItem = recipeListBox.items[recipeListBox.selected].item;
    if not selectedItem.evolved then
        local now = getTimestampMs()
        if not self.refreshTypesAvailableMS or (self.refreshTypesAvailableMS + 500 < now) then
            self.refreshTypesAvailableMS = now
            local typesAvailable = self:getAvailableItemsType();
            self.needRefreshIngredientPanel = self.needRefreshIngredientPanel or areTablesDifferent(selectedItem.typesAvailable, typesAvailable);
            selectedItem.typesAvailable = typesAvailable;
        end
        self:getContainers();
        selectedItem.available = RecipeManager.IsRecipeValid(selectedItem.recipe, self.character, nil, self.containerList);
        self.craftOneButton:setVisible(true);
        self.craftAllButton:setVisible(true);
        self.debugGiveIngredientsButton:setVisible(getDebug());
    else
        self.craftOneButton:setVisible(false);
        self.craftAllButton:setVisible(false);
        self.debugGiveIngredientsButton:setVisible(false);
    end
    -- render the right part, the craft information
    self:drawRectBorder(x, y, 32 + 10, 32 + 10, 1.0, 1.0, 1.0, 1.0);
    if selectedItem.texture then
        if selectedItem.texture:getWidth() <= 32 and selectedItem.texture:getHeight() <= 32 then
            local newX = (32 - selectedItem.texture:getWidthOrig()) / 2;
            local newY = (32 - selectedItem.texture:getHeightOrig()) / 2;
            self:drawTexture(selectedItem.texture,x+5 + newX,y+5 + newY,1,1,1,1);
        else
            self:drawTextureScaledAspect(selectedItem.texture,x+5,y+5,32,32,1,1,1,1);
        end
    end
    self:drawText(selectedItem.recipe:getName() , x + 32 + 15, y, 1,1,1,1, UIFont.Large);
    local name = selectedItem.evolved and selectedItem.resultName or selectedItem.itemName
    self:drawText(name, x + 32 + 15, y + ISCraftingUI.largeFontHeight, 1,1,1,1, UIFont.Small);
    y = y + math.max(45, ISCraftingUI.largeFontHeight + ISCraftingUI.smallFontHeight);
    local imgW = 20;
    local imgH = 20;
    local imgPadX = 4;
    local dyText = (imgH - ISCraftingUI.smallFontHeight) / 2;
    if selectedItem.evolved and selectedItem.baseItem then
        self:drawText(getText("IGUI_CraftUI_BaseItem"), x, y, 1,1,1,1, UIFont.Medium);
        y = y + ISCraftingUI.mediumFontHeight;
        local offset = 15;
        local labelWidth = self.LabelDashWidth
        local r,g,b = 1,1,1
        local r2,g2,b2 = 1,1,1;
        if not selectedItem.available then
            r,g,b = 0.54,0.54,0.54
            r2,g2,b2 = 1,0.3,0.3;
        end
        self:drawText(self.LabelDash, x + offset, y + dyText, r,g,b,1, UIFont.Small);
        self:drawTextureScaledAspect(selectedItem.baseItem:getTex(), x + offset + labelWidth + imgPadX, y, imgW, imgH, 1,r2,b2,g2);
        self:drawText(selectedItem.baseItem:getDisplayName(), x + offset + labelWidth + imgPadX + imgW + imgPadX, y + dyText, r,g,b,1, UIFont.Small);
        y = y + ISCraftingUI.smallFontHeight + 7;

        if selectedItem.extraItems and #selectedItem.extraItems > 0 then
            self:drawText(getText("IGUI_CraftUI_AlreadyContainsItems"), x, y, 1,1,1,1, UIFont.Medium);
            y = y + ISCraftingUI.mediumFontHeight + 7;
            self:drawText(self.LabelDash, x + offset, y + dyText, r,g,b,1, UIFont.Small);
            local newX = x + offset + labelWidth + imgPadX;
            for g,h in ipairs(selectedItem.extraItems) do
                self:drawTextureScaledAspect(h, newX, y, imgW, imgH, g2,r2,b2,g2);
                newX = newX + 22;
            end
            if self.character and self.character:isKnownPoison(selectedItem.baseItem) and self.PoisonTexture then
                self:drawTexture(self.PoisonTexture, newX, y + (imgH - self.PoisonTexture:getHeight()) / 2, 1,r2,g2,b2)
            end
            y = y + ISCraftingUI.mediumFontHeight + 7;
        elseif self.character and self.character:isKnownPoison(selectedItem.baseItem) and self.PoisonTexture then
            self:drawText(getText("IGUI_CraftUI_AlreadyContainsItems"), x, y, 1,1,1,1, UIFont.Medium);
            y = y + ISCraftingUI.mediumFontHeight + 7;
            self:drawText(self.LabelDash, x + offset, y + dyText, r,g,b,1, UIFont.Small);
            local newX = x + offset + labelWidth + imgPadX;
            self:drawTexture(self.PoisonTexture, newX, y + (imgH - self.PoisonTexture:getHeight()) / 2, 1,r2,g2,b2)
            y = y + ISCraftingUI.smallFontHeight + 7;
        end
    end
    if not selectedItem.evolved then
        self:drawText(getText("IGUI_CraftUI_RequiredItems"), x, y, 1,1,1,1, UIFont.Medium);
    else
        self:drawText(getText("IGUI_CraftUI_ItemsToAdd"), x, y, 1,1,1,1, UIFont.Medium);
    end
    y = y + ISCraftingUI.mediumFontHeight + 7;
    if selectedItem.evolved then
        self.ingredientListbox:setX(x + 15)
        self.ingredientListbox:setY(y)
        self.ingredientListbox:setHeight(self.ingredientListbox.itemheight * 8)
        self.addIngredientButton:setX(self.ingredientListbox:getX());
        self.addIngredientButton:setY(self.ingredientListbox:getY() + self.ingredientListbox:getHeight() + 10);
        self.addIngredientButton:setVisible(true);
        if selectedItem.available then
            self.addIngredientButton.enable = true;
        else
            self.addIngredientButton.enable = false;
        end
        local item = self.ingredientListbox.items[self.ingredientListbox.selected]
        if not item or not item.item.available then
            self.addIngredientButton.enable = false;
        else
            self.addIngredientButton.enable = true;
        end
    else
        self.ingredientPanel:setX(x + 15)
        self.ingredientPanel:setY(y)
        self.ingredientPanel:setHeight(self.ingredientListbox.itemheight * 8)
        y = self.ingredientPanel:getBottom()
    end

    if selectedItem ~= self.selectedRecipeItem then
        self:refreshIngredientPanel()
        self:refreshIngredientList()
        self.selectedRecipeItem = selectedItem
    end
    if selectedItem.evolved then
        y = self.ingredientListbox:getBottom()
    end

--    y = y + 10;
--    if selectedItem.multipleItems then
--        self.tickBox:setX(x);
--        self.tickBox:setY(y);
--        self.tickBox:setVisible(true);
--    else
--        self.tickBox:setVisible(false);
--    end
--    y = y + (#self.tickBox.options * 20);
--    y = y + 10;

    y = y + 4;
    if not selectedItem.evolved and (selectedItem.recipe:getRequiredSkillCount() > 0) then
        self:drawText(getText("IGUI_CraftUI_RequiredSkills"), x, y, 1,1,1,1, UIFont.Medium);
        y = y + ISCraftingUI.mediumFontHeight;
        for i=1,selectedItem.recipe:getRequiredSkillCount() do
            local skill = selectedItem.recipe:getRequiredSkill(i-1);
            local perk = PerkFactory.getPerk(skill:getPerk());
            local playerLevel = self.character and self.character:getPerkLevel(skill:getPerk()) or 0
            local perkName = perk and perk:getName() or skill:getPerk():name()
            local text = " - " .. perkName .. ": " .. tostring(playerLevel) .. " / " .. tostring(skill:getLevel());
            local r,g,b = 1,1,1
            if self.character and (playerLevel < skill:getLevel()) then
                g = 0;
                b = 0;
            end
            self:drawText(text, x + 15, y, r,g,b,1, UIFont.Small);
            y = y + ISCraftingUI.smallFontHeight;
        end
        y = y + 4;
    end
    if not selectedItem.evolved and selectedItem.recipe:getNearItem() then
        self:drawText(getText("IGUI_CraftUI_NearItem", selectedItem.recipe:getNearItem()), x, y, 1,1,1,1, UIFont.Medium);
        y = y + ISCraftingUI.mediumFontHeight;
    end
    if not selectedItem.evolved then
        self:drawText(getText("IGUI_CraftUI_RequiredTime", selectedItem.recipe:getTimeToMake()), x, y, 1,1,1,1, UIFont.Medium);
        y = y + ISCraftingUI.mediumFontHeight;
    end
    if not selectedItem.evolved and selectedItem.recipe:getTooltip() then
        y = y + 10
        local tooltip = getText(selectedItem.recipe:getTooltip())
        local numLines = 1
        local p = string.find(tooltip, "\n")
        while p do
            numLines = numLines + 1
            p = string.find(tooltip, "\n", p + 4)
        end
        self:drawText(tooltip, x, y, 1,1,1,1, UIFont.Small);
        y = y + ISCraftingUI.smallFontHeight * numLines;
    end
    
    if not selectedItem.evolved then
        y = y + 10
        self.craftOneButton:setX(x);
        self.craftOneButton:setY(y);
        self.craftOneButton.enable = selectedItem.available;

        self.craftAllButton:setX(self.craftOneButton:getX() + 5 + self.craftOneButton:getWidth());
        self.craftAllButton:setY(y);
        self.craftAllButton.enable = selectedItem.available;
        local title = getText("IGUI_CraftUI_ButtonCraftAll")
        if self.craftAllButton.enable then
            local count = RecipeManager.getNumberOfTimesRecipeCanBeDone(selectedItem.recipe, self.character, self.containerList, nil)
            if count > 1 then
                title = getText("IGUI_CraftUI_ButtonCraftAllCount", count)
            elseif count == 1 then
                self.craftAllButton.enable = false
            end
        end
        if title ~= self.craftAllButton:getTitle() then
            self.craftAllButton:setTitle(title)
            self.craftAllButton:setWidthToTitle()
        end

        self.debugGiveIngredientsButton:setX(self.craftAllButton:getRight() + 5)
        self.debugGiveIngredientsButton:setY(y);

        y = y + self.craftAllButton:getHeight() + 10;
    end
    
    -- stop allowing crafting while driving
    self.craftOneButton.tooltip = nil;
    self.craftAllButton.tooltip = nil;
    if self.character:isDriving() then
        self.craftAllButton.enable = false;
        self.craftOneButton.enable = false;
        self.craftOneButton.tooltip = getText("Tooltip_CantCraftDriving");
        self.craftAllButton.tooltip = getText("Tooltip_CantCraftDriving");
    end

    local currentAction = ISTimedActionQueue.getTimedActionQueue(self.character);
    if currentAction and currentAction.queue and currentAction.queue[1] and self.craftInProgress then
        self.taskLabel:setX(x);
        if selectedItem.evolved then
            y = self.addIngredientButton:getY() + 30;
        end
        self.taskLabel:setY(y);
        self.taskLabel.name = currentAction.queue[1].jobType;
        self:drawProgressBar(x, y + 20, getTextManager():MeasureStringX(UIFont.Small, self.taskLabel.name), self.lineH, currentAction.queue[1].action:getJobDelta(), self.fgBar)
        if not self.taskLabel.name or self.taskLabel.name == "" then
           self.taskLabel:setVisible(false);
        else
            self.taskLabel:setVisible(true);
        end
    else
        self.taskLabel:setVisible(false);
    end

    if self.drawJoypadFocus and self.recipeListHasFocus then
        local ui = self:getRecipeListBox()
        local dx,dy = 0,self:titleBarHeight()
        local parent = ui.parent
        while parent ~= self do
            dx = dx + parent:getX()
            dy = dy + parent:getY()
            parent = parent.parent
        end
        self:drawRectBorder(ui:getX(), dy + ui:getY(), ui:getWidth(), ui:getHeight(), 0.4, 0.2, 1.0, 1.0);
        self:drawRectBorder(ui:getX()+1, dy + ui:getY()+1, ui:getWidth()-2, ui:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
    elseif self.drawJoypadFocus and self.ingredientPanel:getIsVisible() then
        local ui = self.ingredientPanel
        self:drawRectBorder(ui:getX(), ui:getY(), ui:getWidth(), ui:getHeight(), 0.4, 0.2, 1.0, 1.0);
        self:drawRectBorder(ui:getX()+1, ui:getY()+1, ui:getWidth()-2, ui:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
    elseif self.drawJoypadFocus and self.ingredientListbox:getIsVisible() then
        local ui = self.ingredientListbox
        self:drawRectBorder(ui:getX(), ui:getY(), ui:getWidth(), ui:getHeight(), 0.4, 0.2, 1.0, 1.0);
        self:drawRectBorder(ui:getX()+1, ui:getY()+1, ui:getWidth()-2, ui:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
    end
end

function ISCraftingUI:new (x, y, width, height, character)
    local o = {};
    if x == 0 and y == 0 then
       x = (getCore():getScreenWidth() / 2) - (width / 2);
       y = (getCore():getScreenHeight() / 2) - (height / 2);
    end
    o = ISCollapsableWindow:new(x, y, width, height);
    o.minimumWidth = 800
    o.minimumHeight = 600
    setmetatable(o, self);
    if getCore():getKey("Forward") ~= 44 then -- hack, seriously, need a way to detect qwert/azerty keyboard :(
        ISCraftingUI.qwertyConfiguration = false;
    end

    o.LabelDash = "-"
    o.LabelDashWidth = getTextManager():MeasureStringX(UIFont.Small, o.LabelDash)
    o.LabelCraftOne = getText("IGUI_CraftUI_CraftOne")
    o.LabelCraftAll = getText("IGUI_CraftUI_CraftAll")
    o.LabelAddIngredient = getText("IGUI_CraftUI_ButtonAddIngredient")
    o.LabelFavorite = getText("IGUI_CraftUI_Favorite")
    o.LabelClose = getText("IGUI_CraftUI_Close")
    
    o.bottomInfoText1 = getText("IGUI_CraftUI_Controls1",
        getKeyName(ISCraftingUI.upArrow), getKeyName(ISCraftingUI.downArrow),
        getKeyName(ISCraftingUI.leftCategory), getKeyName(ISCraftingUI.rightCategory));
    
    o.bottomInfoText2 = getText("IGUI_CraftUI_Controls2",
        getKeyName(ISCraftingUI.upArrow), getKeyName(ISCraftingUI.downArrow),
        getKeyName(ISCraftingUI.leftCategory), getKeyName(ISCraftingUI.rightCategory));

    o.title = getText("IGUI_CraftUI_Title");
    self.__index = self;
    o.character = character;
    o.playerNum = character and character:getPlayerNum() or -1
    o:setResizable(true);
    o.lineH = 10;
    o.fgBar = {r=0, g=0.6, b=0, a=0.7 }
    o.craftInProgress = false;
    o.selectedIndex = {}
    o.recipeListHasFocus = true
    o.TreeExpanded = getTexture("media/ui/TreeExpanded.png")
    o.PoisonTexture = getTexture("media/ui/SkullPoison.png")
    o.knownRecipes = RecipeManager.getKnownRecipesNumber(o.character);
    o.totalRecipes = getAllRecipes():size();
    o:setWantKeyEvents(true);
    return o;
end

function ISCraftingUI:onActivateView()
    local recipeListBox = self:getRecipeListBox()
    recipeListBox:ensureVisible(recipeListBox.selected);
end

function ISCraftingUI:createChildren()
    ISCollapsableWindow.createChildren(self);
    local th = self:titleBarHeight();
    local rh = self.resizable and self:resizeWidgetHeight() or 0
    self.panel = ISTabPanel:new(0, th, self.width, self.height-th-rh-ISCraftingUI.bottomInfoHeight);
    self.panel:initialise();
    self.panel:setAnchorRight(true)
    self.panel:setAnchorBottom(true)
    self.panel.borderColor = { r = 0, g = 0, b = 0, a = 0};
    self.panel.onActivateView = ISCraftingUI.onActivateView;
    self.panel.target = self;
    self.panel:setEqualTabWidth(false)
    self:addChild(self.panel);
--    self.panel:setOnTabTornOff(self, ISCraftingUI.onTabTornOff)

    -- populate all the recipes, splitted in categories
    self:populateRecipesList();
    self.categories = {};
	local k
    for k = 1 , #self.recipesListH, 1 do
        local i = self.recipesListH[k]
        local l = self.recipesList[i]
    --for i,l in pairs(self.recipesList) do
       local cat1 = ISCraftingCategoryUI:new(0, 0, self.width, self.panel.height - self.panel.tabHeight, self);
       cat1:initialise();
       cat1:setAnchorRight(true)
       cat1:setAnchorBottom(true)
       local catName = getTextOrNull("IGUI_CraftCategory_"..i) or i
       self.panel:addView(catName, cat1);
       cat1.infoText = getText("UI_CraftingUI");
       cat1.parent = self;
       cat1.category = i;
       for s,d in ipairs(l) do
          cat1.recipes:addItem(s,d);
       end
       table.insert(self.categories, cat1);
    end

    self.craftOneButton = ISButton:new(0, self.height-ISCraftingUI.bottomInfoHeight-20-15, 50,25,getText("IGUI_CraftUI_ButtonCraftOne"),self, ISCraftingUI.craft);
    self.craftOneButton:initialise()
    self:addChild(self.craftOneButton);

    self.craftAllButton = ISButton:new(0, self.height-ISCraftingUI.bottomInfoHeight-20-15, 50,25,getText("IGUI_CraftUI_ButtonCraftAll"),self, ISCraftingUI.craftAll);
    self.craftAllButton:initialise()
    self:addChild(self.craftAllButton);

    self.debugGiveIngredientsButton = ISButton:new(0, 0, 50, 25, "DBG: Give Ingredients", self, ISCraftingUI.debugGiveIngredients);
    self.debugGiveIngredientsButton:initialise();
    self:addChild(self.debugGiveIngredientsButton);

    self.taskLabel = ISLabel:new(4,5,19,"",1,1,1,1,UIFont.Small, true);
    self:addChild(self.taskLabel);

    self.addIngredientButton = ISButton:new(0, self.height-ISCraftingUI.bottomInfoHeight-20-15, 50,25,getText("IGUI_CraftUI_ButtonAddIngredient"),self, ISCraftingUI.onAddIngredient);
    self.addIngredientButton:initialise()
    self:addChild(self.addIngredientButton);
    self.addIngredientButton:setVisible(false);

--    self.tickBox = ISTickBox:new(0, 0, 100, 20, "", self, ISCraftingUI.tickBoxChange)
--    self.tickBox.onlyOnePossibility = true;
--    self.tickBox.choicesColor = {r=1, g=1, b=1, a=1}
--    self.tickBox:initialise();
--    self:addChild(self.tickBox)

    -- For non-evolved recipes
    self.ingredientPanel = ISScrollingListBox:new(1, 30, self.width / 3, self.height - (59 + ISCraftingUI.bottomInfoHeight));
    self.ingredientPanel:initialise()
    self.ingredientPanel:instantiate()
    self.ingredientPanel.itemheight = math.max(ISCraftingUI.smallFontHeight, 22)
    self.ingredientPanel.font = UIFont.NewSmall
    self.ingredientPanel.doDrawItem = self.drawNonEvolvedIngredient
    self.ingredientPanel.drawBorder = true
    self.ingredientPanel:setVisible(false)
    self:addChild(self.ingredientPanel)

    -- For evolved recipes
    self.ingredientListbox = ISScrollingListBox:new(1, 30, self.width / 3, self.height - (59 + ISCraftingUI.bottomInfoHeight));
    self.ingredientListbox:initialise();
    self.ingredientListbox:instantiate();
    self.ingredientListbox.itemheight = math.max(ISCraftingUI.smallFontHeight, 22);
    self.ingredientListbox.selected = 0;
    self.ingredientListbox.joypadParent = self;
    self.ingredientListbox.font = UIFont.NewSmall
    self.ingredientListbox.doDrawItem = self.drawEvolvedIngredient
    self.ingredientListbox:setOnMouseDoubleClick(self, self.onDblClickIngredientListbox);
    self.ingredientListbox.drawBorder = true
    self.ingredientListbox:setVisible(false)
--    self.ingredientListbox.resetSelectionOnChangeFocus = true;
    self:addChild(self.ingredientListbox);
    self.ingredientListbox.PoisonTexture = self.PoisonTexture

	self.noteRichText = ISRichTextLayout:new(self.width)
	self.noteRichText:setMargins(0, 0, 0, 0)
	self.noteRichText:setText(getText("IGUI_CraftUI_Note"))
	self.noteRichText.textDirty = true

	self.keysRichText = ISRichTextLayout:new(self.width)
	self.keysRichText:setMargins(5, 0, 5, 0)

    self:refresh();
end

function ISCraftingUI:populateRecipesList()
    local allRecipes = getAllRecipes();
    self.allRecipesList = {};
    self.recipesList = {};
	self.recipesListH = {};
    self.recipesList[getText("IGUI_CraftCategory_Favorite")] = {}; -- set these 2 to have a good order
	self.recipesListH[#self.recipesListH+1] = getText("IGUI_CraftCategory_Favorite")
    self.recipesList[getText("IGUI_CraftCategory_General")] = {};
	self.recipesListH[#self.recipesListH+1] = getText("IGUI_CraftCategory_General")
    self:getContainers();

    for i=0,allRecipes:size()-1 do
        local newItem = {};
        local recipe = allRecipes:get(i);
        if not recipe:needToBeLearn() or (self.character and self.character:isRecipeKnown(recipe)) then
            if recipe:getCategory() then
                newItem.category = recipe:getCategory();
            else
                newItem.category = getText("IGUI_CraftCategory_General");
            end
            if not self.recipesList[newItem.category] then
                self.recipesList[newItem.category] = {};
                self.recipesListH[#self.recipesListH+1] = newItem.category
            end
            newItem.recipe = recipe;
            if self.character then
                newItem.available = RecipeManager.IsRecipeValid(recipe, self.character, nil, self.containerList);

                local modData = self.character:getModData();
                if modData[self:getFavoriteModDataLocalString(recipe)] or false then  -- Update the favorite list and save backward compatibility
                    --table.remove(modData, self:getFavoriteModDataLocalString(recipe));
                    modData[self:getFavoriteModDataString(recipe)] = true;
                end
                newItem.favorite = modData[self:getFavoriteModDataString(recipe)] or false;
            end
            if newItem.favorite then
                table.insert(self.recipesList[getText("IGUI_CraftCategory_Favorite")], newItem);
            end
            local resultItem = self:GetItemInstance(recipe:getResult():getFullType());
            if resultItem then
                newItem.texture = resultItem:getTex();
                newItem.itemName = resultItem:getDisplayName();
                if recipe:getResult():getCount() > 1 then
                   newItem.itemName = (recipe:getResult():getCount() * resultItem:getCount()) .. " " .. newItem.itemName;
                end
            end
            newItem.sources = {};
            for x=0,recipe:getSource():size()-1 do
                local source = recipe:getSource():get(x);
                local sourceInList = {};
                sourceInList.items = {}
                for k=1,source:getItems():size() do
                    local sourceFullType = source:getItems():get(k-1)
                    local item = nil
                    local itemName = nil
                    if sourceFullType == "Water" then
                        item = self:GetItemInstance("Base.WaterDrop");
                    elseif luautils.stringStarts(sourceFullType, "[") then
                        -- a Lua test function
                        item = self:GetItemInstance("Base.WristWatch_Right_DigitalBlack");
                    else
                        item = self:GetItemInstance(sourceFullType);
                    end
                    if item then
                        local itemInList = {};
                        itemInList.count = source:getCount();
                        itemInList.texture = item:getTex();
                        if sourceFullType == "Water" then
                            if itemInList.count == 1 then
                                itemInList.name = getText("IGUI_CraftUI_CountOneUnit", getText("ContextMenu_WaterName"))
                            else
                                itemInList.name = getText("IGUI_CraftUI_CountUnits", getText("ContextMenu_WaterName"), itemInList.count)
                            end
                        elseif source:getItems():size() > 1 then -- no units
                            itemInList.name = item:getDisplayName()
                        elseif not source:isDestroy() and item:IsDrainable() then
                            if itemInList.count == 1 then
                                itemInList.name = getText("IGUI_CraftUI_CountOneUnit", item:getDisplayName())
                            else
                                itemInList.name = getText("IGUI_CraftUI_CountUnits", item:getDisplayName(), itemInList.count)
                            end
                        elseif not source:isDestroy() and source:getUse() > 0 then -- food
                            itemInList.count = source:getUse()
                            if itemInList.count == 1 then
                                itemInList.name = getText("IGUI_CraftUI_CountOneUnit", item:getDisplayName())
                            else
                                itemInList.name = getText("IGUI_CraftUI_CountUnits", item:getDisplayName(), itemInList.count)
                            end
                        elseif itemInList.count > 1 then
                            itemInList.name = getText("IGUI_CraftUI_CountNumber", item:getDisplayName(), itemInList.count)
                        else
                            itemInList.name = item:getDisplayName()
                        end
                        itemInList.fullType = item:getFullType()
                        if sourceFullType == "Water" then
                            itemInList.fullType = "Water"
                        end
                        table.insert(sourceInList.items, itemInList);
                    end
                end
                table.insert(newItem.sources, sourceInList)
            end
            table.insert(self.recipesList[newItem.category], newItem);
            table.insert(self.allRecipesList, newItem);
        end
    end

    -- now do the evolved recipe
    local newRecipe = {};
    local itemInList = {};
    local doneRecipes = {};
    local doneItems = {};
    -- first we get all our available evolvedRecipe
    for i=0,self.containerList:size()-1 do
       local container = self.containerList:get(i);
       for x=0,container:getItems():size() - 1 do
           local baseItem = container:getItems():get(x);
           local evorecipe = RecipeManager.getEvolvedRecipe(baseItem, self.character, self.containerList, false);
           if evorecipe and evorecipe:size() > 0 then
                for y=0,evorecipe:size() - 1 do
                   local evo = evorecipe:get(y);
                    newRecipe = {};
                    if not doneRecipes[evo:getName() .. baseItem:getDisplayName()] then
                        doneRecipes[evo:getName() .. baseItem:getDisplayName()] = true;
                        doneItems = {};
                        newRecipe.baseItem = baseItem;
                        local resultItem = self:GetItemInstance(evo:getFullResultItem());
                        newRecipe.texture = resultItem:getTex();
                        newRecipe.resultName = resultItem:getDisplayName();
                        newRecipe.items = {};
                        newRecipe.available = false;
                        newRecipe.itemName = evo:getName();
                        if baseItem:getType() ~= evo:getBaseItem() then
                            newRecipe.customRecipeName = getText("IGUI_CraftUI_FromBaseItem", baseItem:getDisplayName());
                            -- add the textures of our extra items to display them
                            newRecipe.extraItems = {};
                            if baseItem:getExtraItems() then
                                for u=0,baseItem:getExtraItems():size()-1 do
                                   local extraItem = self:GetItemInstance(baseItem:getExtraItems():get(u));
                                    if extraItem then
                                        table.insert(newRecipe.extraItems, extraItem:getTex());
                                    end
                                end
                            end
                            if instanceof(baseItem, "Food") and baseItem:getSpices() then
                                for u=0,baseItem:getSpices():size()-1 do
                                   local extraItem = self:GetItemInstance(baseItem:getSpices():get(u));
                                    if extraItem then
                                        table.insert(newRecipe.extraItems, extraItem:getTex());
                                    end
                                end
                            end
                        end
                        newRecipe.recipe = evo;
                        newRecipe.evolved = true;
                        local itemCanBeUse = evo:getItemsCanBeUse(self.character, baseItem, self.containerList);
                        for l=0, itemCanBeUse:size()-1 do
                            local newItem = itemCanBeUse:get(l);
                            if not doneItems[newItem] then
                                doneItems[newItem] = true;
                                newRecipe.available = true;
                                itemInList.texture = newItem:getTex();
                                itemInList.name = newItem:getName();
                                itemInList.fullType = newItem:getFullType();
                                itemInList.itemToAdd = newItem;
                                itemInList.available = true;
                                itemInList.poison = self.character:isKnownPoison(newItem)
                                table.insert(newRecipe.items, itemInList);
                                itemInList = {};
                            end
                        end
                        if self.character then
                            local modData = self.character:getModData();
                            newRecipe.favorite = modData[self:getFavoriteModDataString(evo)] or false;
                        end
                        table.insert(self.recipesList["Cooking"], newRecipe);
                        table.insert(self.allRecipesList, newRecipe);
                        if newRecipe.favorite then
                            table.insert(self.recipesList[getText("IGUI_CraftCategory_Favorite")], newRecipe);
                        end
                   end
               end
           end
       end
    end

    -- then we look for missing recipes
    local allRecipes = RecipeManager.getAllEvolvedRecipes();
    for i=0, allRecipes:size()-1 do
        local evolvedRecipe = allRecipes:get(i);
        local found = false;
        for x,v in ipairs(self.recipesList["Cooking"]) do
            if v.evolved and v.recipe == evolvedRecipe then -- check possible missing items
                local possibleItems = evolvedRecipe:getPossibleItems();
                for k=0, possibleItems:size() -1 do
                    local possibleItem = possibleItems:get(k);
                    local found2 = false;
                    for g,h in ipairs(v.items) do
                        if h.fullType == possibleItem:getFullType() then
                            found2 = true;
                            break;
                        end
                    end
                    if not found2 then
                        local newItem = self:GetItemInstance(possibleItem:getFullType());
                        itemInList.texture = newItem:getTex();
                        itemInList.name = newItem:getDisplayName();
                        itemInList.available = false;
                        table.insert(v.items, itemInList);
                        itemInList = {};
                    end
                end
                found = true;
            end
        end
        if not found then -- recipe not in list, we add it with all the missing items
            newRecipe = {};
            local resultItem = self:GetItemInstance(evolvedRecipe:getFullResultItem());
            if resultItem then
                newRecipe.texture = resultItem:getTex();
                newRecipe.resultName = resultItem:getDisplayName();
                newRecipe.items = {};
                newRecipe.available = false;
                newRecipe.itemName = evolvedRecipe:getName();
                newRecipe.recipe = evolvedRecipe;
                newRecipe.evolved = true;
                newRecipe.baseItem = self:GetItemInstance(evolvedRecipe:getModule():getName() .. "." .. evolvedRecipe:getBaseItem());
                local possibleItems = evolvedRecipe:getPossibleItems();
                for k=0, possibleItems:size() -1 do
                        local possibleItem = possibleItems:get(k);
                        local newItem = self:GetItemInstance(possibleItem:getFullType());
                        itemInList.texture = newItem:getTex();
                        itemInList.name = newItem:getDisplayName();
                        itemInList.available = false;
                        table.insert(newRecipe.items, itemInList);
                        itemInList = {};
                end
                if self.character then
                        local modData = self.character:getModData();
                        newRecipe.favorite = modData[self:getFavoriteModDataString(evolvedRecipe)] or false;
                end
                table.insert(self.recipesList["Cooking"], newRecipe);
                if newRecipe.favorite then
                        table.insert(self.recipesList[getText("IGUI_CraftCategory_Favorite")], newRecipe);
                end
            else
                print('ISCraftingUI: no such result item '..tostring(evolvedRecipe:getFullResultItem()))
            end
        end
    end
--    if #self.recipesList["Favorite"] == 0 then self.recipesList["Favorite"] = nil; end
end

function ISCraftingUI:sortList() -- sort list with items you can craft in first
    local availableList = {};
    local notAvailableList = {};
    for i,v in pairs(self.recipesList) do
        if not availableList[i] then
            availableList[i] = {};
            notAvailableList[i] = {};
        end
        for k,l in ipairs(v) do
            if l.available then
                table.insert(availableList[i], l);
            else
                table.insert(notAvailableList[i], l);
            end
        end
    end
    self.recipesList = {};

    -- now populate our list
    for i,v in pairs(availableList) do
        table.sort(v, ISCraftingUI.sortByName);
        if not self.recipesList[i] then
            self.recipesList[i] = {};
        end
        for k,l in ipairs(v) do
		   self.recipesList[i][#self.recipesList[i]+1] = l;
        end
    end
    for i,v in pairs(notAvailableList) do
        table.sort(v, ISCraftingUI.sortByName);
        if not self.recipesList[i] then
            self.recipesList[i] = {};
        end
        for k,l in ipairs(v) do
			self.recipesList[i][#self.recipesList[i]+1] = l;
        end
    end
end

ISCraftingUI.toggleCraftingUI = function()
    local ui = getPlayerCraftingUI(0)
    if ui then
        if ui:getIsVisible() then
            ui:setVisible(false)
            ui:removeFromUIManager() -- avoid update() while hidden
        else
            ui:setVisible(true)
            ui:addToUIManager()
        end
    end
end

ISCraftingUI.onPressKey = function(key)
    if not MainScreen.instance or not MainScreen.instance.inGame or MainScreen.instance:getIsVisible() then
        return
    end
    if key == getCore():getKey("Crafting UI") then
        ISCraftingUI.toggleCraftingUI();
    end
end

function ISCraftingUI:isKeyConsumed(key)
    return key == Keyboard.KEY_ESCAPE or
            key == getCore():getKey("Crafting UI") or
            key == ISCraftingUI.upArrow or
            key == ISCraftingUI.downArrow or
            key == ISCraftingUI.leftCategory or
            key == ISCraftingUI.rightCategory or
            key == Keyboard.KEY_C or
            key == Keyboard.KEY_R or
            key == Keyboard.KEY_F
end

function ISCraftingUI:onKeyRelease(key)
    local ui = self
    if not ui.panel or not ui.panel.activeView then return; end
    if key == getCore():getKey("Crafting UI") then
        ISCraftingUI.toggleCraftingUI();
        return;
    end
    if key == Keyboard.KEY_ESCAPE then
        ISCraftingUI.toggleCraftingUI();
        return;
    end
    local self = ui.panel.activeView.view.recipes;
    if key == ISCraftingUI.upArrow then
        self.selected = self.selected - 1;
        if self.selected <= 0 then
            self.selected = self.count;
        end
    elseif key == ISCraftingUI.downArrow then
        self.selected = self.selected + 1;
        if self.selected > self.count then
            self.selected = 1;
        end
    end
    local viewIndex = ui.panel:getActiveViewIndex()
    local oldViewIndex = viewIndex
    if key == ISCraftingUI.leftCategory then
        if viewIndex == 1 then
            viewIndex = #ui.panel.viewList
        else
            viewIndex = viewIndex - 1
        end
    elseif key == ISCraftingUI.rightCategory then
        if viewIndex == #ui.panel.viewList then
            viewIndex = 1
        else
            viewIndex = viewIndex + 1
        end
    end
    if key == Keyboard.KEY_C then
        if ui.ingredientListbox:getIsVisible() then
            ui:onAddIngredient();
        elseif ui.craftOneButton.enable then
            ui:craft();
        end
    elseif key == Keyboard.KEY_R and ui.craftAllButton.enable then
        ui:craftAll();
    elseif key == Keyboard.KEY_F then
        ui.panel.activeView.view:addToFavorite(true);
    end
    if oldViewIndex ~= viewIndex then
        ui.panel:activateView(ui.panel.viewList[viewIndex].name)
    end
    ui.panel.activeView.view.recipes:ensureVisible(ui.panel.activeView.view.recipes.selected)
end

function ISCraftingUI:getFavoriteModDataString(recipe)
    local text = "craftingFavorite:" .. recipe:getOriginalname();
    if instanceof(recipe, "EvolvedRecipe") then
        text = text .. ':' .. recipe:getBaseItem()
        text = text .. ':' .. recipe:getResultItem()
    else
        for i=0,recipe:getSource():size()-1 do
            local source = recipe:getSource():get(i)
            for j=1,source:getItems():size() do
                text = text .. ':' .. source:getItems():get(j-1);
            end
        end
    end
    return text;
end

function ISCraftingUI:getFavoriteModDataLocalString(recipe) -- For backward compatibility only
    local text = "craftingFavorite:" .. recipe:getName();
    if instanceof(recipe, "EvolvedRecipe") then
        text = text .. ':' .. recipe:getBaseItem()
        text = text .. ':' .. recipe:getResultItem()
    else
        for i=0,recipe:getSource():size()-1 do
            local source = recipe:getSource():get(i)
            for j=1,source:getItems():size() do
                text = text .. ':' .. source:getItems():get(j-1);
            end
        end
    end
    return text;
end

-- just enable/disable the crafting UI
ISCraftingUI.onKeyPressed = function (key)
    if key == getCore():getKey("Crafting UI") then
        if not ISCraftingUI.instance then
            ISCraftingUI.instance = ISCraftingUI:new(0,0,800,600,getPlayer());
            ISCraftingUI.instance:initialise();
            ISCraftingUI.instance:addToUIManager();
            ISCraftingUI.instance:setVisible(true);
        else
            ISCraftingUI.instance:setVisible(not ISCraftingUI.instance:getIsVisible());
        end
    end
end

ISCraftingUI.load = function()
    ISCraftingUI.instance = ISCraftingUI:new(0,0,800,600,nil);
    ISCraftingUI.instance:initialise();
    ISCraftingUI.instance:addToUIManager();
--    ISCraftingUI.instance:setVisible(true);
end

function ISCraftingUI:update()
    if self.craftInProgress then
        local currentAction = ISTimedActionQueue.getTimedActionQueue(self.character);
        if not currentAction or not currentAction.queue or not currentAction.queue[1] then
            self:refresh();
            self.craftInProgress = false;
        end
    end
    if self.needRefreshIngredientPanel then
        self.needRefreshIngredientPanel = false
        self:refreshIngredientPanel()
    end
end

function ISCraftingUI:onResize()
    self.ingredientPanel:setWidth(self.width / 3)
    self.ingredientListbox:setWidth(self.width / 3)
    if self.catListButtons then
        for _,button in ipairs(self.catListButtons) do
            button:setX(self.ingredientListbox:getRight() + 10)
        end
    end
end

function ISCraftingUI:addItemInEvolvedRecipe(button)
    -- get the required item
--    local itemFound = nil;
--    for i=0,self.containerList:size()-1 do
--       itemFound = self.containerList:get(i):FindAndReturn(button.item)
--        if itemFound then break; end
--    end
--    if itemFound then
        local returnToContainer = {};
        if not self.character:getInventory():contains(button.item) then -- take the item if it's not in our inventory
            ISTimedActionQueue.add(ISInventoryTransferAction:new(self.character, button.item, button.item:getContainer(), self.character:getInventory(), nil));
            table.insert(returnToContainer, button.item)
        end
        if not self.character:getInventory():contains(button.baseItem) then -- take the base item if it's not in our inventory
            ISTimedActionQueue.add(ISInventoryTransferAction:new(self.character, button.baseItem, button.baseItem:getContainer(), self.character:getInventory(), nil));
            table.insert(returnToContainer, button.baseItem)
        end
        ISTimedActionQueue.add(ISAddItemInRecipe:new(self.character, button.recipe, button.baseItem, button.item, (70 - self.character:getPerkLevel(Perks.Cooking))));
        self.craftInProgress = true;
        ISCraftingUI.ReturnItemsToOriginalContainer(self.character, returnToContainer);
--    end
    self:refresh();
end

function ISCraftingUI:craftAll()
    self:craft(nil, true);
end

function ISCraftingUI:craft(button, all)
    self.craftInProgress = false
    local recipeListBox = self:getRecipeListBox()
    local selectedItem = recipeListBox.items[recipeListBox.selected].item;
    if selectedItem.evolved then return; end
    if not RecipeManager.IsRecipeValid(selectedItem.recipe, self.character, nil, self.containerList) then return; end
--    local multipleItemSelected = nil;
--    -- if we had more than one choice, grab the selected item
--    if selectedItem.multipleItems then
--        local selectedIndex = -1;
--        for i,v in pairs(self.tickBox.selected) do
--           if v == true then selectedIndex = i; break; end
--        end
--        if selectedItem.multipleItems[selectedIndex] then
--            multipleItemSelected = selectedItem.multipleItems[selectedIndex].name;
--        end
--    end
    if not getPlayer() then return; end
    local itemsUsed = self:transferItems();
    if #itemsUsed == 0 then
        self:refresh();
        return
    end
    local returnToContainer = {};
    local container = itemsUsed[1]:getContainer()
    if not selectedItem.recipe:isCanBeDoneFromFloor() then
        container = self.character:getInventory()
        for _,item in ipairs(itemsUsed) do
            if item:getContainer() ~= self.character:getInventory() then
                table.insert(returnToContainer, item)
            end
        end
    end
    local action = ISCraftAction:new(self.character, itemsUsed[1], selectedItem.recipe:getTimeToMake(), selectedItem.recipe, container, self.containerList)
    if all then
        action:setOnComplete(ISCraftingUI.onCraftComplete, self, action, selectedItem.recipe, container, self.containerList)
    end
    ISTimedActionQueue.add(action);
    self.craftInProgress = true; -- keep a track of our current task because we'll refresh the list once it's done

    ISCraftingUI.ReturnItemsToOriginalContainer(self.character, returnToContainer)
end

function ISCraftingUI:onCraftComplete(completedAction, recipe, container, containers)
    if not RecipeManager.IsRecipeValid(recipe, self.character, nil, containers) then return end
    local items = RecipeManager.getAvailableItemsNeeded(recipe, self.character, containers, nil, nil)
    if items:isEmpty() then return end
    local previousAction = completedAction
    local returnToContainer = {};
    if not recipe:isCanBeDoneFromFloor() then
        for i=1,items:size() do
            local item = items:get(i-1)
            if item:getContainer() ~= self.character:getInventory() then
                local action = ISInventoryTransferAction:new(self.character, item, item:getContainer(), self.character:getInventory(), nil)
                ISTimedActionQueue.addAfter(previousAction, action)
                previousAction = action
                table.insert(returnToContainer, item)
            end
        end
    end
    local action = ISCraftAction:new(self.character, items:get(0), recipe:getTimeToMake(), recipe, container, containers)
    action:setOnComplete(ISCraftingUI.onCraftComplete, self, action, recipe, container, containers)
    ISTimedActionQueue.addAfter(previousAction, action)
    ISCraftingUI.ReturnItemsToOriginalContainer(self.character, returnToContainer)
end

function ISCraftingUI.ReturnItemsToOriginalContainer(playerObj, items)
    for _,item in ipairs(items) do
        if item:getContainer() ~= playerObj:getInventory() then
            local action = ISInventoryTransferAction:new(playerObj, item, playerObj:getInventory(), item:getContainer(), nil)
            action:setAllowMissingItems(true)
            ISTimedActionQueue.add(action)
        end
    end
end

function ISCraftingUI:GetItemInstance(type)
    if not self.ItemInstances then self.ItemInstances = {} end
    local item = self.ItemInstances[type]
    if not item then
        item = InventoryItemFactory.CreateItem(type)
        if item then
            self.ItemInstances[type] = item
            self.ItemInstances[item:getFullType()] = item
        end
    end
    return item
end

function ISCraftingUI:onGainJoypadFocus(joypadData)
    self.drawJoypadFocus = true
end

function ISCraftingUI:onJoypadDown(button)
    if button == Joypad.AButton then
        if self.ingredientListbox:getIsVisible() and not self.recipeListHasFocus then
            local item = self.ingredientListbox.items[self.ingredientListbox.selected]
            if item and item.item.available then
                self:addItemInEvolvedRecipe(item.item)
            end
        elseif self.craftOneButton.enable then
            self:craft()
        end
    end
    if button == Joypad.BButton then
        self:setVisible(false)
        setJoypadFocus(self.playerNum, nil)
    end
    if button == Joypad.XButton then
        if self.craftAllButton.enable then
            self:craftAll()
        end
    end
    if button == Joypad.YButton then
        self.panel.activeView.view:addToFavorite(true);
    end
    if button == Joypad.LBumper or button == Joypad.RBumper then
        local viewIndex = self.panel:getActiveViewIndex()
        if button == Joypad.LBumper then
            if viewIndex == 1 then
                viewIndex = #self.panel.viewList
            else
                viewIndex = viewIndex - 1
            end
        elseif button == Joypad.RBumper then
            if viewIndex == #self.panel.viewList then
                viewIndex = 1
            else
                viewIndex = viewIndex + 1
            end
        end
        self.panel:activateView(self.panel.viewList[viewIndex].name)
        local recipeListBox = self:getRecipeListBox()
        recipeListBox:ensureVisible(recipeListBox.selected)
    end
end

function ISCraftingUI:onJoypadDirUp()
    if self.recipeListHasFocus then
        self:getRecipeListBox():onJoypadDirUp()
    elseif self.ingredientPanel:getIsVisible() then
        self.ingredientPanel:onJoypadDirUp()
    elseif self.ingredientListbox:getIsVisible() then
        self.ingredientListbox:onJoypadDirUp()
    end
end

function ISCraftingUI:onJoypadDirDown()
    if self.recipeListHasFocus then
        self:getRecipeListBox():onJoypadDirDown()
    elseif self.ingredientPanel:getIsVisible() then
        self.ingredientPanel:onJoypadDirDown()
    elseif self.ingredientListbox:getIsVisible() then
        self.ingredientListbox:onJoypadDirDown()
    end
end

function ISCraftingUI:onJoypadDirLeft()
    self.recipeListHasFocus = true
end

function ISCraftingUI:onJoypadDirRight()
    if self.recipeListHasFocus and self.ingredientPanel:getIsVisible() then
        self.recipeListHasFocus = false
    elseif self.recipeListHasFocus and self.ingredientListbox:getIsVisible() then
        self.recipeListHasFocus = false
    end
end

function ISCraftingUI:debugGiveIngredients()
    local recipeListBox = self:getRecipeListBox()
    local selectedItem = recipeListBox.items[recipeListBox.selected].item
    if selectedItem.evolved then return end
    local recipe = selectedItem.recipe
    local items = {}
    local options = {}
    options.AvailableItemsAll = RecipeManager.getAvailableItemsAll(recipe, self.character, self:getContainers(), nil, nil)
    options.MaxItemsPerSource = 10
    options.NoDuplicateKeep = true
    RecipeUtils.CreateSourceItems(recipe, options, items)
    for _,item in ipairs(items) do
        self.character:getInventory():AddItem(item)
    end
end

--Events.OnMainMenuEnter.Add(ISCraftingUI.load);

Events.OnCustomUIKey.Add(ISCraftingUI.onPressKey);

--Events.OnKeyPressed.Add(ISCraftingUI.onKeyPressed);
