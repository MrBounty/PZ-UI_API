--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

local VERSION = 1

local tiles = {}

-- bent north, no corner
table.insert(tiles, { dir = "N", unbent = { "fencing_01_56", "fencing_01_57", "fencing_01_56", "fencing_01_57", "fencing_01_56", "fencing_01_57" }, bent = { "fencing_damaged_02_0", "fencing_damaged_02_1", "fencing_damaged_02_2", "fencing_damaged_02_3", "fencing_damaged_02_4", "fencing_damaged_02_5" } })

-- bent north, unbent corner
table.insert(tiles, { dir = "N", unbent = { "fencing_01_60", "fencing_01_57", "fencing_01_56", "fencing_01_57", "fencing_01_56", "fencing_01_57" }, bent = { "fencing_damaged_02_32", "fencing_damaged_02_1", "fencing_damaged_02_2", "fencing_damaged_02_3", "fencing_damaged_02_4", "fencing_damaged_02_5" } })

-- bent north, bent-west corner
table.insert(tiles, { dir = "N", unbent = { "fencing_damaged_02_34", "fencing_01_57", "fencing_01_56", "fencing_01_57", "fencing_01_56", "fencing_01_57" }, bent = { "fencing_damaged_02_37", "fencing_damaged_02_1", "fencing_damaged_02_2", "fencing_damaged_02_3", "fencing_damaged_02_4", "fencing_damaged_02_5" } })

-- bent north, bent-east corner
table.insert(tiles, { dir = "N", unbent = { "fencing_damaged_02_35", "fencing_01_57", "fencing_01_56", "fencing_01_57", "fencing_01_56", "fencing_01_57" }, bent = { "fencing_damaged_02_39", "fencing_damaged_02_1", "fencing_damaged_02_2", "fencing_damaged_02_3", "fencing_damaged_02_4", "fencing_damaged_02_5" } })



-- bent south, no corner
table.insert(tiles, { dir = "S", unbent = { "fencing_01_56", "fencing_01_57", "fencing_01_56", "fencing_01_57", "fencing_01_56", "fencing_01_57" }, bent = { "fencing_damaged_02_8", "fencing_damaged_02_9", "fencing_damaged_02_10", "fencing_damaged_02_11", "fencing_damaged_02_12", "fencing_damaged_02_13" } })

-- bent south, unbent corner
table.insert(tiles, { dir = "S", unbent = { "fencing_01_60", "fencing_01_57", "fencing_01_56", "fencing_01_57", "fencing_01_56", "fencing_01_57" }, bent = { "fencing_damaged_02_33", "fencing_damaged_02_9", "fencing_damaged_02_10", "fencing_damaged_02_11", "fencing_damaged_02_12", "fencing_damaged_02_13" } })

-- bent south, bent-west corner
table.insert(tiles, { dir = "S", unbent = { "fencing_damaged_02_34", "fencing_01_57", "fencing_01_56", "fencing_01_57", "fencing_01_56", "fencing_01_57" }, bent = { "fencing_damaged_02_38", "fencing_damaged_02_9", "fencing_damaged_02_10", "fencing_damaged_02_11", "fencing_damaged_02_12", "fencing_damaged_02_13" } })

-- bent south, bent-east corner
table.insert(tiles, { dir = "S", unbent = { "fencing_damaged_02_35", "fencing_01_57", "fencing_01_56", "fencing_01_57", "fencing_01_56", "fencing_01_57" }, bent = { "fencing_damaged_02_36", "fencing_damaged_02_9", "fencing_damaged_02_10", "fencing_damaged_02_11", "fencing_damaged_02_12", "fencing_damaged_02_13" } })



-- bent west, no corner
table.insert(tiles, { dir = "W", unbent = { "fencing_01_59", "fencing_01_58", "fencing_01_59", "fencing_01_58", "fencing_01_59", "fencing_01_58" }, bent = { "fencing_damaged_02_16", "fencing_damaged_02_17", "fencing_damaged_02_18", "fencing_damaged_02_19", "fencing_damaged_02_20", "fencing_damaged_02_21" } })

-- bent west, unbent corner
table.insert(tiles, { dir = "W", unbent = { "fencing_01_60", "fencing_01_58", "fencing_01_59", "fencing_01_58", "fencing_01_59", "fencing_01_58" }, bent = { "fencing_damaged_02_34", "fencing_damaged_02_17", "fencing_damaged_02_18", "fencing_damaged_02_19", "fencing_damaged_02_20", "fencing_damaged_02_21" } })

-- bent west, bent-north corner
table.insert(tiles, { dir = "W", unbent = { "fencing_damaged_02_32", "fencing_01_58", "fencing_01_59", "fencing_01_58", "fencing_01_59", "fencing_01_58" }, bent = { "fencing_damaged_02_37", "fencing_damaged_02_17", "fencing_damaged_02_18", "fencing_damaged_02_19", "fencing_damaged_02_20", "fencing_damaged_02_21" } })

-- bent west, bent-south corner
table.insert(tiles, { dir = "W", unbent = { "fencing_damaged_02_33", "fencing_01_58", "fencing_01_59", "fencing_01_58", "fencing_01_59", "fencing_01_58" }, bent = { "fencing_damaged_02_38", "fencing_damaged_02_17", "fencing_damaged_02_18", "fencing_damaged_02_19", "fencing_damaged_02_20", "fencing_damaged_02_21" } })



-- bent east, no corner
table.insert(tiles, { dir = "E", unbent = { "fencing_01_59", "fencing_01_58", "fencing_01_59", "fencing_01_58", "fencing_01_59", "fencing_01_58" }, bent = { "fencing_damaged_02_24", "fencing_damaged_02_25", "fencing_damaged_02_26", "fencing_damaged_02_27", "fencing_damaged_02_28", "fencing_damaged_02_29" } })

-- bent east, unbent corner
table.insert(tiles, { dir = "E", unbent = { "fencing_01_60", "fencing_01_58", "fencing_01_59", "fencing_01_58", "fencing_01_59", "fencing_01_58" }, bent = { "fencing_damaged_02_35", "fencing_damaged_02_25", "fencing_damaged_02_26", "fencing_damaged_02_27", "fencing_damaged_02_28", "fencing_damaged_02_29" } })

-- bent east, bent-south corner
table.insert(tiles, { dir = "E", unbent = { "fencing_damaged_02_33", "fencing_01_58", "fencing_01_59", "fencing_01_58", "fencing_01_59", "fencing_01_58" }, bent = { "fencing_damaged_02_36", "fencing_damaged_02_25", "fencing_damaged_02_26", "fencing_damaged_02_27", "fencing_damaged_02_28", "fencing_damaged_02_29" } })

-- bent east, bent-north corner
table.insert(tiles, { dir = "E", unbent = { "fencing_damaged_02_32", "fencing_01_58", "fencing_01_59", "fencing_01_58", "fencing_01_59", "fencing_01_58" }, bent = { "fencing_damaged_02_39", "fencing_damaged_02_25", "fencing_damaged_02_26", "fencing_damaged_02_27", "fencing_damaged_02_28", "fencing_damaged_02_29" } })



--[[
table.insert(tiles, { dir = "", unbent = { "", "", "", "", "", "" }, bent = { "", "", "", "", "", "" } })
table.insert(tiles, { dir = "", unbent = { "", "", "", "", "", "" }, bent = { "", "", "", "", "", "" } })
table.insert(tiles, { dir = "", unbent = { "", "", "", "", "", "" }, bent = { "", "", "", "", "", "" } })
]]--

BentFences.getInstance():addFenceTiles(VERSION, tiles)
