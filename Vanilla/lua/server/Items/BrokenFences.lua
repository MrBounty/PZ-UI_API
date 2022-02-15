--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

local tiles = {}
tiles.VERSION = 1

-- Short black metal bars N/W/NW
tiles["fencing_01_1"] = { self = { "fencing_damaged_01_1" }, left = { "fencing_damaged_01_2" }, right = { "fencing_damaged_01_0" } }
tiles["fencing_01_2"] = { self = { "fencing_damaged_01_4" }, left = { "fencing_damaged_01_5" }, right = { "fencing_damaged_01_3" } }
tiles["fencing_01_3"] = { self = { "fencing_damaged_01_118" }, left = { "fencing_damaged_01_6" }, right = { "fencing_damaged_01_7" } }

-- Short white wooden
tiles["fencing_01_5"] = { self = { "fencing_damaged_01_9" }, left = { "fencing_damaged_01_10" }, right = { "fencing_damaged_01_8" } }
tiles["fencing_01_4"] = { self = { "fencing_damaged_01_12" }, left = { "fencing_damaged_01_13" }, right = { "fencing_damaged_01_11" } }
tiles["fencing_01_6"] = { self = { "fencing_damaged_01_113" }, left = { "fencing_damaged_01_14" }, right = { "fencing_damaged_01_15" } }

-- Short metal chainlink 2-tile
tiles["fencing_01_24"] = { self = { "fencing_damaged_01_33" }, left = { "fencing_damaged_01_34" }, right = { "fencing_damaged_01_32" } }
tiles["fencing_01_25"] = { self = { "fencing_damaged_01_41" }, left = { "fencing_damaged_01_42" }, right = { "fencing_damaged_01_40" } }
tiles["fencing_01_26"] = { self = { "fencing_damaged_01_44" }, left = { "fencing_damaged_01_45" }, right = { "fencing_damaged_01_43" } }
tiles["fencing_01_27"] = { self = { "fencing_damaged_01_36" }, left = { "fencing_damaged_01_37" }, right = { "fencing_damaged_01_35" } }
tiles["fencing_01_28"] = { self = { "fencing_damaged_01_116" }, left = { "fencing_damaged_01_38" }, right = { "fencing_damaged_01_39" } }

-- Short carpentry (level 1)
tiles["carpentry_02_41"] = { self = { "fencing_damaged_01_145" }, left = { "fencing_damaged_01_146" }, right = { "fencing_damaged_01_144" } }
tiles["carpentry_02_40"] = { self = { "fencing_damaged_01_148" }, left = { "fencing_damaged_01_149" }, right = { "fencing_damaged_01_147" } }
--tiles["carpentry_02_42"] = { self = { "fencing_damaged_01_117" }, left = { "fencing_damaged_01_150" }, right = { "fencing_damaged_01_151" } }

-- Short carpentry (level 2)
tiles["carpentry_02_45"] = { self = { "fencing_damaged_01_153" }, left = { "fencing_damaged_01_154" }, right = { "fencing_damaged_01_152" } }
tiles["carpentry_02_44"] = { self = { "fencing_damaged_01_156" }, left = { "fencing_damaged_01_157" }, right = { "fencing_damaged_01_155" } }
--tiles["carpentry_02_46"] = { self = { "fencing_damaged_01_117" }, left = { "fencing_damaged_01_158" }, right = { "fencing_damaged_01_159" } }

-- Short carpentry (level 3)
tiles["carpentry_02_49"] = { self = { "fencing_damaged_01_161" }, left = { "fencing_damaged_01_162" }, right = { "fencing_damaged_01_160" } }
tiles["carpentry_02_48"] = { self = { "fencing_damaged_01_164" }, left = { "fencing_damaged_01_165" }, right = { "fencing_damaged_01_163" } }
--tiles["carpentry_02_50"] = { self = { "fencing_damaged_01_117" }, left = { "fencing_damaged_01_166" }, right = { "fencing_damaged_01_167" } }

-- Short wooden 2-tile
tiles["fencing_01_32"] = { self = { "fencing_damaged_01_49" }, left = { "fencing_damaged_01_50" }, right = { "fencing_damaged_01_48" } }
tiles["fencing_01_33"] = { self = { "fencing_damaged_01_57" }, left = { "fencing_damaged_01_58" }, right = { "fencing_damaged_01_56" } }
tiles["fencing_01_34"] = { self = { "fencing_damaged_01_60" }, left = { "fencing_damaged_01_61" }, right = { "fencing_damaged_01_59" } }
tiles["fencing_01_35"] = { self = { "fencing_damaged_01_52" }, left = { "fencing_damaged_01_53" }, right = { "fencing_damaged_01_51" } }
tiles["fencing_01_36"] = { self = { "fencing_damaged_01_117" }, left = { "fencing_damaged_01_54" }, right = { "fencing_damaged_01_55" } }

-- Tall concrete
tiles["fencing_01_41"] = { self = { "fencing_damaged_01_25" }, left = { "fencing_damaged_01_26" }, right = { "fencing_damaged_01_24" } }
tiles["fencing_01_40"] = { self = { "fencing_damaged_01_28" }, left = { "fencing_damaged_01_29" }, right = { "fencing_damaged_01_27" } }
tiles["fencing_01_42"] = { self = { "fencing_damaged_01_115" }, left = { "fencing_damaged_01_30" }, right = { "fencing_damaged_01_31" } }

-- Tall chainlink 2-tile
tiles["fencing_01_56"] = { self = { "fencing_damaged_01_81" }, left = { "fencing_damaged_01_82" }, right = { "fencing_damaged_01_80" } }
tiles["fencing_01_57"] = { self = { "fencing_damaged_01_89" }, left = { "fencing_damaged_01_90" }, right = { "fencing_damaged_01_88" } }
tiles["fencing_01_58"] = { self = { "fencing_damaged_01_92" }, left = { "fencing_damaged_01_93" }, right = { "fencing_damaged_01_91" } }
tiles["fencing_01_59"] = { self = { "fencing_damaged_01_84" }, left = { "fencing_damaged_01_85" }, right = { "fencing_damaged_01_83" } }
tiles["fencing_01_60"] = { self = { "fencing_damaged_01_116" }, left = { "fencing_damaged_01_86" }, right = { "fencing_damaged_01_87" } }

-- Tall black metal bars 2-tile
tiles["fencing_01_64"] = { self = { "fencing_damaged_01_97" }, left = { "fencing_damaged_01_98" }, right = { "fencing_damaged_01_96" } }
tiles["fencing_01_65"] = { self = { "fencing_damaged_01_105" }, left = { "fencing_damaged_01_106" }, right = { "fencing_damaged_01_104" } }
tiles["fencing_01_66"] = { self = { "fencing_damaged_01_108" }, left = { "fencing_damaged_01_109" }, right = { "fencing_damaged_01_107" } }
tiles["fencing_01_67"] = { self = { "fencing_damaged_01_100" }, left = { "fencing_damaged_01_101" }, right = { "fencing_damaged_01_99" } }
tiles["fencing_01_68"] = { self = { "fencing_damaged_01_118" }, left = { "fencing_damaged_01_102" }, right = { "fencing_damaged_01_103" } }

-- Tall wooden flat tops
tiles["fencing_01_73"] = { self = { "fencing_damaged_01_17" }, left = { "fencing_damaged_01_18" }, right = { "fencing_damaged_01_16" } }
tiles["fencing_01_72"] = { self = { "fencing_damaged_01_21" }, left = { "fencing_damaged_01_20" }, right = { "fencing_damaged_01_19" } }
tiles["fencing_01_75"] = { self = { "fencing_damaged_01_114" }, left = { "fencing_damaged_01_22" }, right = { "fencing_damaged_01_23" } }

-- Tall wooden 2-tile sloped tops
tiles["fencing_01_8"] = { self = { "fencing_damaged_01_65" }, left = { "fencing_damaged_01_66" }, right = { "fencing_damaged_01_64" } }
tiles["fencing_01_9"] = { self = { "fencing_damaged_01_73" }, left = { "fencing_damaged_01_74" }, right = { "fencing_damaged_01_72" } }
tiles["fencing_01_10"] = { self = { "fencing_damaged_01_68" }, left = { "fencing_damaged_01_77" }, right = { "fencing_damaged_01_75" } }
tiles["fencing_01_11"] = { self = { "fencing_damaged_01_76" }, left = { "fencing_damaged_01_69" }, right = { "fencing_damaged_01_67" } }
--tiles["fencing_01_12"] = { self = { "fencing_damaged_01_" }, left = { "fencing_damaged_01_" }, right = { "fencing_damaged_01_" } }

BrokenFences.getInstance():addBrokenTiles(tiles)

debris = {}
debris.VERSION = 1

-- Short black metal bars
debris["fencing_01_1"] = { north = { "fencing_damaged_01_120" }, south = { "fencing_damaged_01_121" }, west = { "fencing_damaged_01_123" }, east = { "fencing_damaged_01_122" } }
debris["fencing_01_2"] = debris["fencing_01_1"]

-- Short white wooden
debris["fencing_01_5"] = { north = { "fencing_damaged_01_124" }, south = { "fencing_damaged_01_125" }, west = { "fencing_damaged_01_127" }, east = { "fencing_damaged_01_126" } }
debris["fencing_01_4"] = debris["fencing_01_5"]

-- Short metal chainlink 2-tile
debris["fencing_01_24"] = { north = { "fencing_damaged_01_140" }, south = { "fencing_damaged_01_141" }, west = { "fencing_damaged_01_143" }, east = { "fencing_damaged_01_142" } }
debris["fencing_01_25"] = debris["fencing_01_24"]
debris["fencing_01_26"] = debris["fencing_01_24"]
debris["fencing_01_27"] = debris["fencing_01_24"]

-- Short carpentry (level 1)
debris["carpentry_02_41"] = { north = { "fencing_damaged_01_168" }, south = { "fencing_damaged_01_169" }, west = { "fencing_damaged_01_171" }, east = { "fencing_damaged_01_170" } }
debris["carpentry_02_40"] = debris["carpentry_02_41"]

-- Short carpentry (level 2)
debris["carpentry_02_45"] = debris["carpentry_02_41"]
debris["carpentry_02_44"] = debris["carpentry_02_41"]

-- Short carpentry (level 3)
debris["carpentry_02_49"] = debris["carpentry_02_41"]
debris["carpentry_02_48"] = debris["carpentry_02_41"]

-- Short wooden 2-tile
debris["fencing_01_32"] = { north = { "fencing_damaged_01_136" }, south = { "fencing_damaged_01_137" }, west = { "fencing_damaged_01_139" }, east = { "fencing_damaged_01_138" } }
debris["fencing_01_33"] = debris["fencing_01_32"]
debris["fencing_01_34"] = debris["fencing_01_32"]
debris["fencing_01_35"] = debris["fencing_01_32"]

-- Tall concrete
debris["fencing_01_41"] = { north = { "fencing_damaged_01_132" }, south = { "fencing_damaged_01_133" }, west = { "fencing_damaged_01_135" }, east = { "fencing_damaged_01_134" } }
debris["fencing_01_40"] = debris["fencing_01_41"]

-- Tall chainlink 2-tile
debris["fencing_01_56"] = { north = { "fencing_damaged_01_140" }, south = { "fencing_damaged_01_141" }, west = { "fencing_damaged_01_143" }, east = { "fencing_damaged_01_142" } }
debris["fencing_01_57"] = debris["fencing_01_56"]
debris["fencing_01_58"] = debris["fencing_01_56"]
debris["fencing_01_59"] = debris["fencing_01_56"]

-- Tall black metal bars
debris["fencing_01_64"] = { north = { "fencing_damaged_01_120" }, south = { "fencing_damaged_01_121" }, west = { "fencing_damaged_01_123" }, east = { "fencing_damaged_01_122" } }
debris["fencing_01_65"] = debris["fencing_01_64"]
debris["fencing_01_66"] = debris["fencing_01_64"]
debris["fencing_01_67"] = debris["fencing_01_64"]

-- Tall wooden flat tops
debris["fencing_01_73"] = { north = { "fencing_damaged_01_128" }, south = { "fencing_damaged_01_129" }, west = { "fencing_damaged_01_131" }, east = { "fencing_damaged_01_130" } }
debris["fencing_01_72"] = debris["fencing_01_5"]

-- Tall wooden 2-tile sloped tops
debris["fencing_01_8"] = { north = { "fencing_damaged_01_128" }, south = { "fencing_damaged_01_129" }, west = { "fencing_damaged_01_131" }, east = { "fencing_damaged_01_130" } }
debris["fencing_01_9"] = debris["fencing_01_8"]
debris["fencing_01_10"] = debris["fencing_01_8"]
debris["fencing_01_11"] = debris["fencing_01_8"]

BrokenFences.getInstance():addDebrisTiles(debris)

