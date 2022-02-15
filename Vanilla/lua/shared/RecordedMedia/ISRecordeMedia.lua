--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

--[[
 NOTE: A temporary solution, when devices update comes things *may* change drastically.
--]]

RecMedia = RecMedia or {};

ISRecordedMedia = {};

-- gets passed instance of RecordedMedia (java) as parameter
ISRecordedMedia.init = function(_rc)
    for k,v in pairs(RecMedia) do
        -- register(String category, String id, String itemDisplayName, int spawning[0-2]) returns MediaData
        local data = _rc:register(v.category, k, v.itemDisplayName, v.spawning and v.spawning or 0);

        -- set optional properties:
        data:setTitle(v.title);
        data:setSubtitle(v.subtitle);
        data:setAuthor(v.author);
        data:setExtra(v.extra);

        -- add lines:
        for i,j in ipairs(v.lines) do
            data:addLine(j.text, j.r, j.g, j.b, j.codes);
        end
    end
end

Events.OnInitRecordedMedia.Add(ISRecordedMedia.init);