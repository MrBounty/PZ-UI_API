--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISReadABook : ISBaseTimedAction
ISReadABook = ISBaseTimedAction:derive("ISReadABook");


function ISReadABook:isValid()
    local vehicle = self.character:getVehicle()
    if vehicle and vehicle:isDriver(self.character) then
        return not vehicle:isEngineRunning()
    end
    return self.character:getInventory():contains(self.item) and ((self.item:getNumberOfPages() > 0 and self.item:getAlreadyReadPages() <= self.item:getNumberOfPages()) or self.item:getNumberOfPages() < 0);
end

function ISReadABook:update()
    self.pageTimer = self.pageTimer + getGameTime():getMultiplier();
    self.item:setJobDelta(self:getJobDelta());

    if self.item:getNumberOfPages() > 0 then
        local pagesRead = math.floor(self.item:getNumberOfPages() * self:getJobDelta())
        self.item:setAlreadyReadPages(pagesRead);
        if self.item:getAlreadyReadPages() > self.item:getNumberOfPages() then
            self.item:setAlreadyReadPages(self.item:getNumberOfPages());
        end
        self.character:setAlreadyReadPages(self.item:getFullType(), self.item:getAlreadyReadPages())
    end
    if SkillBook[self.item:getSkillTrained()] then
        if self.item:getLvlSkillTrained() > self.character:getPerkLevel(SkillBook[self.item:getSkillTrained()].perk) + 1 or self.character:HasTrait("Illiterate") then
            if self.pageTimer >= 200 then
                self.pageTimer = 0;
                local txtRandom = ZombRand(3);
                if txtRandom == 0 then
                    self.character:Say(getText("IGUI_PlayerText_DontGet"));
                elseif txtRandom == 1 then
                    self.character:Say(getText("IGUI_PlayerText_TooComplicated"));
                else
                    self.character:Say(getText("IGUI_PlayerText_DontUnderstand"));
                end
                if self.item:getNumberOfPages() > 0 then
                    self.character:setAlreadyReadPages(self.item:getFullType(), 0)
                    self:forceStop()
                end
            end
        elseif self.item:getMaxLevelTrained() < self.character:getPerkLevel(SkillBook[self.item:getSkillTrained()].perk) + 1 then
            if self.pageTimer >= 200 then
                self.pageTimer = 0;
                local txtRandom = ZombRand(2);
                if txtRandom == 0 then
                    self.character:Say(getText("IGUI_PlayerText_KnowSkill"));
                else
                    self.character:Say(getText("IGUI_PlayerText_BookObsolete"));
                end
            end
        else
            ISReadABook.checkMultiplier(self);
        end
    end

    -- Playing with longer day length reduces the effectiveness of morale-boosting
    -- literature, like Comic Book.
    local bodyDamage = self.character:getBodyDamage()
    local stats = self.character:getStats()
    if self.stats and (self.item:getBoredomChange() < 0.0) then
        if bodyDamage:getBoredomLevel() > self.stats.boredom then
            bodyDamage:setBoredomLevel(self.stats.boredom)
        end
    end
    if self.stats and (self.item:getUnhappyChange() < 0.0) then
        if bodyDamage:getUnhappynessLevel() > self.stats.unhappyness then
            bodyDamage:setUnhappynessLevel(self.stats.unhappyness)
        end
    end
    if self.stats and (self.item:getStressChange() < 0.0) then
        if stats:getStress() > self.stats.stress then
            stats:setStress(self.stats.stress)
        end
    end
end

-- get how much % of the book we already read, then we apply a multiplier depending on the book read progress
ISReadABook.checkMultiplier = function(self)
-- get all our info in the map
    local trainedStuff = SkillBook[self.item:getSkillTrained()];
    if trainedStuff then
        -- every 10% we add 10% of the max multiplier
        local readPercent = (self.item:getAlreadyReadPages() / self.item:getNumberOfPages()) * 100;
        if readPercent > 100 then
            readPercent = 100;
        end
        -- apply the multiplier to the skill
        local multiplier = (math.floor(readPercent/10) * (self.maxMultiplier/10));
        if multiplier > self.character:getXp():getMultiplier(trainedStuff.perk) then
            self.character:getXp():addXpMultiplier(trainedStuff.perk, multiplier, self.item:getLvlSkillTrained(), self.item:getMaxLevelTrained());
        end
    end
end

function ISReadABook.checkLevel(character, item)
    if item:getNumberOfPages() <= 0 then
        return
    end
    local skillBook = SkillBook[item:getSkillTrained()]
    if not skillBook then
        return
    end
    local level = character:getPerkLevel(skillBook.perk)
    if (item:getLvlSkillTrained() > level + 1) or character:HasTrait("Illiterate") then
        item:setAlreadyReadPages(0)
        character:setAlreadyReadPages(item:getFullType(), 0)
    end
end

function ISReadABook:start()
    if self.startPage then
        self:setCurrentTime(self.maxTime * (self.startPage / self.item:getNumberOfPages()))
    end
    self.item:setJobType(getText("ContextMenu_Read") ..' '.. self.item:getName());
    self.item:setJobDelta(0.0);
    --self.character:SetPerformingAction(GameCharacterActions.Reading, nil, self.item)
    if (self.item:getType() == "Newspaper") then
        self:setAnimVariable("ReadType", "newspaper")
    else
        self:setAnimVariable("ReadType", "book")
    end
    self:setActionAnim(CharacterActionAnims.Read);
    self:setOverrideHandModels(nil, self.item);
    self.character:setReading(true)
    
    self.character:reportEvent("EventRead");

    if not SkillBook[self.item:getSkillTrained()] then
        self.stats = {}
        self.stats.boredom = self.character:getBodyDamage():getBoredomLevel()
        self.stats.unhappyness = self.character:getBodyDamage():getUnhappynessLevel()
        self.stats.stress = self.character:getStats():getStress()
    end

    if SkillBook[self.item:getSkillTrained()] then
        self.character:playSound("OpenBook")
    else
        self.character:playSound("OpenMagazine")
    end
end

function ISReadABook:stop()
    if self.item:getNumberOfPages() > 0 and self.item:getAlreadyReadPages() >= self.item:getNumberOfPages() then
        self.item:setAlreadyReadPages(self.item:getNumberOfPages());
    end
    self.character:setReading(false);
    self.item:setJobDelta(0.0);
    if SkillBook[self.item:getSkillTrained()] then
        self.character:playSound("CloseBook")
    else
        self.character:playSound("CloseMagazine")
    end
    ISBaseTimedAction.stop(self);
end

function ISReadABook:perform()
    self.character:setReading(false);
    self.item:getContainer():setDrawDirty(true);
    self.item:setJobDelta(0.0);
    if self.item:getTeachedRecipes() and not self.item:getTeachedRecipes():isEmpty() then
        self.character:getAlreadyReadBook():add(self.item:getFullType());
    end
    if not SkillBook[self.item:getSkillTrained()] then
        self.character:ReadLiterature(self.item);
    elseif self.item:getAlreadyReadPages() >= self.item:getNumberOfPages() then
--        self.character:ReadLiterature(self.item);
        self.item:setAlreadyReadPages(0);
    end
--    if self.item:getTeachedRecipes() and not self.item:getTeachedRecipes():isEmpty() then
--        for i=0, self.item:getTeachedRecipes():size() - 1 do
--           if not self.character:getKnownRecipes():contains(self.item:getTeachedRecipes():get(i)) then
--               self.character:getKnownRecipes():add(self.item:getTeachedRecipes():get(i));
--           else
--               self.character:Say(getText("IGUI_PlayerText_KnowSkill"));
--           end
--        end
--    end
    if SkillBook[self.item:getSkillTrained()] then
        self.character:playSound("CloseBook")
    else
        self.character:playSound("CloseMagazine")
    end
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
end

function ISReadABook:animEvent(event, parameter)
    if event == "PageFlip" then
        if getGameSpeed() ~= 1 then
            return
        end
        if SkillBook[self.item:getSkillTrained()] then
            self.character:playSound("PageFlipBook")
        else
            self.character:playSound("PageFlipMagazine")
        end
    end
end

function ISReadABook:new(character, item, time)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.character = character;
    o.item = item;
    o.stopOnWalk = true;
    o.stopOnRun = true;

    local numPages
    if item:getNumberOfPages() > 0 then
        ISReadABook.checkLevel(character, item)
        item:setAlreadyReadPages(character:getAlreadyReadPages(item:getFullType()))
        o.startPage = item:getAlreadyReadPages()
        numPages = item:getNumberOfPages() -- item:getAlreadyReadPages()
    else
        numPages = 5
    end
    if isClient() then
        o.minutesPerPage = getServerOptions():getFloat("MinutesPerPage") or 1.0
        if o.minutesPerPage < 0.0 then o.minutesPerPage = 1.0 end
    else
        o.minutesPerPage = 2.0
    end
    local f = 1 / getGameTime():getMinutesPerDay() / 2
    time = numPages * o.minutesPerPage / f

    if(character:HasTrait("FastReader")) then
        time = time * 0.7;
    end
    if(character:HasTrait("SlowReader")) then
        time = time * 1.3;
    end

    if SkillBook[item:getSkillTrained()] then
        if item:getLvlSkillTrained() == 1 then
            o.maxMultiplier = SkillBook[item:getSkillTrained()].maxMultiplier1;
        elseif item:getLvlSkillTrained() == 3 then
            o.maxMultiplier = SkillBook[item:getSkillTrained()].maxMultiplier2;
        elseif item:getLvlSkillTrained() == 5 then
            o.maxMultiplier = SkillBook[item:getSkillTrained()].maxMultiplier3;
        elseif item:getLvlSkillTrained() == 7 then
            o.maxMultiplier = SkillBook[item:getSkillTrained()].maxMultiplier4;
        elseif item:getLvlSkillTrained() == 9 then
            o.maxMultiplier = SkillBook[item:getSkillTrained()].maxMultiplier5;
        else
            o.maxMultiplier = 1
            print('ERROR: book has unhandled skill level ' .. item:getLvlSkillTrained())
        end
    end
    o.ignoreHandsWounds = true;
    o.maxTime = time;
    o.caloriesModifier = 0.5;
    o.pageTimer = 0;
    o.forceProgressBar = true;
    if character:isTimedActionInstant() then
        o.maxTime = 1;
    end

    return o;
end
