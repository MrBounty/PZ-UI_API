require "Util/LuaList"

---@class SurvivalGuideEntries
SurvivalGuideEntries = {}

SurvivalGuideEntries.list = LuaList:new();

SurvivalGuideEntries.addSurvivalGuideEntry = function (title, text, moreInfo, openConditionMethod, completeConditionMethod, moreInfoText)
    local entry = {title = title, text = text, moreInfo = moreInfo};

    SurvivalGuideEntries.list:add(entry);
end

SurvivalGuideEntries.getEntry = function(num)
    return SurvivalGuideEntries.list:get(num);
end

SurvivalGuideEntries.getEntryCount = function()
    return SurvivalGuideEntries.list:size();
end

SurvivalGuideEntries.addSurvivalGuideEntry(getText("SurvivalGuide_entrie1title"), getText("SurvivalGuide_entrie1txt"), getText("SurvivalGuide_entrie1moreinfo"));

SurvivalGuideEntries.addSurvivalGuideEntry(getText("SurvivalGuide_entrie2title"), getText("SurvivalGuide_entrie2txt"), getText("SurvivalGuide_entrie2moreinfo"));

SurvivalGuideEntries.addSurvivalGuideEntry(getText("SurvivalGuide_entrie3title"), getText("SurvivalGuide_entrie3txt"), getText("SurvivalGuide_entrie3moreinfo"));

SurvivalGuideEntries.addSurvivalGuideEntry(getText("SurvivalGuide_entrie4title"), getText("SurvivalGuide_entrie4txt"), getText("SurvivalGuide_entrie4moreinfo"));

SurvivalGuideEntries.addSurvivalGuideEntry(getText("SurvivalGuide_entrie5title"), getText("SurvivalGuide_entrie5txt"), getText("SurvivalGuide_entrie5moreinfo"));

SurvivalGuideEntries.addSurvivalGuideEntry(getText("SurvivalGuide_entrie6title"), getText("SurvivalGuide_entrie6txt"), getText("SurvivalGuide_entrie6moreinfo"));

SurvivalGuideEntries.addSurvivalGuideEntry(getText("SurvivalGuide_entrie7title"), getText("SurvivalGuide_entrie7txt"), getText("SurvivalGuide_entrie7moreinfo"));

SurvivalGuideEntries.addSurvivalGuideEntry(getText("SurvivalGuide_entrie8title"), getText("SurvivalGuide_entrie8txt"), getText("SurvivalGuide_entrie8moreinfo"));

SurvivalGuideEntries.addSurvivalGuideEntry(getText("SurvivalGuide_entrie9title"), getText("SurvivalGuide_entrie9txt"), getText("SurvivalGuide_entrie9moreinfo"));

SurvivalGuideEntries.addSurvivalGuideEntry(getText("SurvivalGuide_entrie10title"), getText("SurvivalGuide_entrie10txt"), getText("SurvivalGuide_entrie10moreinfo"));


-- We don't it once it's boot 'cause we need some translation
SurvivalGuideEntries.addEntry11 = function()
    SurvivalGuideEntries.addSurvivalGuideEntry(getText("SurvivalGuide_entrie11title"),
        getText("SurvivalGuide_entrie11txt", getKeyName(getCore():getKey("Crouch")),getKeyName(getCore():getKey("Sprint")),getKeyName(getCore():getKey("Run"))),
        getText("SurvivalGuide_entrie11moreinfo", getKeyName(getCore():getKey("Sprint")), getKeyName(getCore():getKey("Toggle Clothing Protection Panel"))));
end
