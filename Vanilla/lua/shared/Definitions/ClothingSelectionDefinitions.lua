-- define here all the clothing that'll be available for a specific profession.
-- the default will always been available

ClothingSelectionDefinitions = ClothingSelectionDefinitions or {};

-- default selection, always available
ClothingSelectionDefinitions.default = {
	-- if there's no difference between male/female outfit, just create the female one, here we can have skirt
	Female = {
		-- this is the bodyLocation (defined in BodyLocations.lua)
		Hat = {
			chance = 10,
			items = {"Base.Hat_BaseballCap",},
		},
		
		Eyes = {
			chance = 10,
			items = {"Base.Glasses_Normal", "Base.Glasses_Reading"},
		},
		
		TankTop = {
			chance = 10,
			items = {"Base.Vest_DefaultTEXTURE_TINT",},
		},
		
		Shirt = {
			chance = 10,
			items = {"Base.Shirt_FormalWhite", "Base.Shirt_FormalWhite_ShortSleeve", "Base.Shirt_Denim"},
		},
	
		Tshirt = {
			items = {"Base.Tshirt_DefaultTEXTURE_TINT", "Base.Tshirt_WhiteLongSleeveTINT", "Base.Tshirt_PoloStripedTINT", "Base.Tshirt_PoloTINT"},
		},
		
--		Sweater = {
--			chance = 30,
--			items = {"Base.Jumper_PoloNeck", "Base.Jumper_RoundNeck", "Base.Jumper_VNeck"},
--		},
--
--		Jacket = {
--			chance = 30,
--			items = {"Base.Jacket_WhiteTINT", "Base.JacketLong_Random"},
--		},
		
		Pants = {
			items = {"Base.Trousers_DefaultTEXTURE_TINT", "Base.Trousers_Denim", "Base.Shorts_LongDenim", "Base.Shorts_ShortDenim", "Base.Shorts_ShortFormal"},
		},
		
		Skirt = {
			chance = 50,
			items = {"Base.Skirt_Knees", "Base.Skirt_Long", "Base.Skirt_Normal",},
		},
		
		Socks = {
			items = {"Base.Socks_Ankle", "Base.Socks_Long",},
		},
		
		Shoes = {
			items = {"Base.Shoes_Random", "Base.Shoes_TrainerTINT"},
		},
	},
	
	Male = {
		Hat = {
			chance = 10,
			items = {"Base.Hat_BaseballCap",},
		},
		
		Eyes = {
			chance = 10,
			items = {"Base.Glasses_Normal", "Base.Glasses_Reading"},
		},
		
		TankTop = {
			chance = 30,
			items = {"Base.Vest_DefaultTEXTURE_TINT",},
		},
		
		Shirt = {
			chance = 10,
			items = {"Base.Shirt_FormalWhite", "Base.Shirt_FormalWhite_ShortSleeve",},
		},
		
		Tshirt = {
			items = {"Base.Tshirt_DefaultTEXTURE_TINT", "Base.Tshirt_WhiteLongSleeveTINT", "Base.Tshirt_PoloStripedTINT", "Base.Tshirt_PoloTINT"},
		},
		
--		Sweater = {
--			chance = 30,
--			items = {"Base.Jumper_PoloNeck", "Base.Jumper_RoundNeck", "Base.Jumper_VNeck"},
--		},
--
--		Jacket = {
--			chance = 30,
--			items = {"Base.Jacket_WhiteTINT", "Base.JacketLong_Random"},
--		},
		
		Pants = {
			items = {"Base.Trousers_DefaultTEXTURE_TINT", "Base.Trousers_Denim", "Base.Shorts_LongDenim", "Base.Shorts_ShortDenim", "Base.Shorts_ShortFormal"},
		},
		
		Socks = {
			items = {"Base.Socks_Ankle", "Base.Socks_Long"},
		},
		
		Shoes = {
			items = {"Base.Shoes_Random", "Base.Shoes_TrainerTINT"},
		},
	}
}

ClothingSelectionDefinitions.fireofficer = {
	Female = {
		Tshirt = {
			items = {"Base.Tshirt_Profession_FiremanBlue", "Base.Tshirt_Profession_FiremanRed", "Base.Tshirt_Profession_FiremanRed02", "Base.Tshirt_Profession_FiremanWhite"},
		},
		
		Pants = {
--			chance = 30,
			items = {"Base.Trousers_Fireman"},
		},
		
--		Jacket = {
--			items = {"Base.Jacket_Fireman"},
--		},
		
--		Sweater = {
--			items = {};
--		},
		
		Shoes = {
			items = {"Base.Shoes_Black", "Base.Shoes_ArmyBoots"};
		},
	},
}

ClothingSelectionDefinitions.policeofficer = {
	Female = {
--		Shirt = {
--			chance = 20,
--			items = {"Base.Shirt_PoliceBlue"},
--		},
		
		Tshirt = {
--			chance = 20,
			items = {"Base.Tshirt_Profession_PoliceBlue", "Base.Tshirt_Profession_PoliceWhite"},
		},
		
--		Jacket = {
--			chance = 20,
--			items = {"Base.Jacket_Police"},
--		},
		
		Pants = {
			items = {"Base.Trousers_Police"},
		},
		
		-- we remove sweater from this outfit, you can still select one if you want
--		Sweater = {
--			items = {};
--		},
	},
}

ClothingSelectionDefinitions.parkranger = {
	Female = {
--		Shirt = {
--			items = {"Base.Shirt_Ranger"},
--		},
		
		Tshirt = {
--			chance = 20,
			items = {"Base.Tshirt_Profession_RangerBrown", "Base.Tshirt_Profession_RangerGreen"},
		},
		
--		Jacket = {
--			chance = 20,
--			items = {"Base.Jacket_Ranger"},
--		},
		
		Pants = {
			items = {"Base.Trousers_Ranger"},
		},
		
--		Sweater = {
--			items = {};
--		},
	},
}

ClothingSelectionDefinitions.constructionworker = {
	Female = {
		Shirt = {
			items = {"Base.Shirt_Denim"},
		},
		
--		TorsoExtra = {
----			chance = 30,
--			items = {"Base.Vest_HighViz"},
--		},
		
		Pants = {
--			chance = 50,
			items = {"Base.Trousers_JeanBaggy"},
		},
		
--		Sweater = {
--			items = {};
--		},
	},
}

ClothingSelectionDefinitions.securityguard = {
	Female = {
--		Neck = {
----			chance = 70,
--			items = {"Base.Tie_Full"},
--		},
		
		Shirt = {
			items = {"Base.Shirt_FormalWhite"},
		},
		
		Pants = {
			items = {"Base.Trousers_Black"},
		},
		
--		Sweater = {
--			items = {};
--		},
--
--		Jacket = {
--			items = {};
--		},
	},
}

ClothingSelectionDefinitions.carpenter = {
	Female = {
		Shirt = {
			items = {"Base.Shirt_Lumberjack"},
		},
		
--		Hands = {
----			chance = 20,
--			items = {"Base.Gloves_LeatherGloves"},
--		},
		
		Pants = {
--			chance = 50,
			items = {"Base.Trousers_JeanBaggy"},
		},
		
--		Sweater = {
--			items = {};
--		},
	},
}

ClothingSelectionDefinitions.burglar = {
	Female = {
--		Hands = {
----			chance = 20,
--			items = {"Base.Gloves_LeatherGloves"},
--		},
		
		Pants = {
			items = {"Base.Trousers_Denim"},
		},
		
	}
}

ClothingSelectionDefinitions.chef = {
	Female = {
--		Jacket = {
----			chance = 50,
--			items = {"Base.Jacket_Chef"},
--		},
		
		Pants = {
			items = {"Base.Trousers_Chef"},
		},
	}
}

ClothingSelectionDefinitions.repairman = {
	Female = {
--		Hands = {
----			chance = 20,
--			items = {"Base.Gloves_LeatherGloves"},
--		},
		
		Shirt = {
			items = {"Base.Shirt_Denim"},
		},
		
		Pants = {
			items = {"Base.Trousers_Denim"},
		},
		
--		Sweater = {
--			items = {};
--		},
	},
}

ClothingSelectionDefinitions.farmer = {
	Female = {
--		Hands = {
----			chance = 20,
--			items = {"Base.Gloves_LeatherGloves"},
--		},
		
		Shirt = {
			items = {"Base.Shirt_Denim", "Base.Shirt_Lumberjack"},
		},
		
		Pants = {
--			chance = 50,
			items = {"Base.Trousers_Denim"},
		},
	},
}

ClothingSelectionDefinitions.fisherman = {
	Female = {
--		Hands = {
----			chance = 20,
--			items = {"Base.Gloves_LeatherGloves"},
--		},
		
		Shirt = {
			items = {"Base.Shirt_Denim", "Base.Shirt_Lumberjack"},
		},
		
		Pants = {
--			chance = 50,
			items = {"Base.Trousers_JeanBaggy"},
		},
	},
}

ClothingSelectionDefinitions.doctor = {
	Female = {
		Shirt = {
			items = {"Base.Shirt_FormalWhite"},
		},
		
--		Jacket = {
--			items = {"Base.JacketLong_Doctor"},
--		},
		
		Pants = {
			items = {"Base.Trousers_SuitTEXTURE"},
		},
	},
}

ClothingSelectionDefinitions.veteran = {
	Female = {
		Tshirt = {
			items = {"Base.Tshirt_Profession_VeterenGreen", "Base.Tshirt_Profession_VeterenRed"},
		},
		
		Pants = {
			items = {"Base.Shorts_CamoGreenLong"},
		},
		
		Shoes = {
			items = {"Base.Shoes_TrainerTINT"},
		},
	},
}

ClothingSelectionDefinitions.nurse = {
	Female = {
		Tshirt = {
			items = {"Base.Tshirt_DefaultTEXTURE_TINT"},
		},
		
		Pants = {
			items = {"Base.Trousers_DefaultTEXTURE_TINT"},
		},
		
		Shoes = {
			items = {"Base.Shoes_TrainerTINT"},
		},
		
--		Jacket = {
--			items = {};
--		},
--
--		Sweater = {
--			items = {};
--		},
	},
}

ClothingSelectionDefinitions.lumberjack = {
	Female = {
		Shirt = {
			items = {"Base.Shirt_Lumberjack"},
		},
		
--		Hands = {
----			chance = 20,
--			items = {"Base.Gloves_LeatherGloves"},
--		},
		
		Pants = {
--			chance = 50,
			items = {"Base.Trousers_JeanBaggy"},
		},
		
--		Sweater = {
--			items = {};
--		},
	},
}

ClothingSelectionDefinitions.fitnessInstructor = {
	Female = {
		Tshirt = {
--			chance = 30,
			items = {"Base.Tshirt_Sport"},
		},
		
		Pants = {
			items = {"Base.Shorts_LongSport"},
		},
		
		Shoes = {
			items = {"Base.Shoes_TrainerTINT"},
		},
		
--		Sweater = {
--			items = {};
--		},
--
--		Jacket = {
--			items = {};
--		},
	},
}

ClothingSelectionDefinitions.burgerflipper = {
	Female = {
--		TankTop = {
--			items = {"Base.Vest_DefaultTEXTURE_TINT",},
--		},
		
		Shirt = {
			items = {};
		},
		
		Tshirt = {
			items = {"Base.Tshirt_BusinessSpiffo"};
		},
		
		Shoes = {
			items = {"Base.Shoes_TrainerTINT"},
		},
		
		Pants = {
			items = {"Base.Trousers_Denim"},
		},
		
--		Sweater = {
--			items = {};
--		},
--
--		Jacket = {
--			items = {};
--		},
		
--		TorsoExtra = {
--			items = {"Base.Apron_White"},
--		}
	},
}

ClothingSelectionDefinitions.electrician = {
	Female = {
		Shirt = {
			items = {"Base.Shirt_Lumberjack"},
		},
		
--		Hands = {
--			chance = 20,
--			items = {"Base.Gloves_LeatherGloves"},
--		},
		
		Pants = {
			items = {"Base.Trousers_Denim"},
		},
		
--		Sweater = {
--			items = {};
--		},
		
--		FullSuit = {
--			chance = 20,
--			items = {"Boilersuit"},
--		},
		
--		TorsoExtra = {
--			chance = 30,
--			items = {"Base.Vest_HighViz"},
--		},
	},
}

ClothingSelectionDefinitions.metalworker = {
	Female = {
		Shirt = {
			items = {"Base.Shirt_Lumberjack"},
		},
		
--		Hands = {
--			chance = 20,
--			items = {"Base.Gloves_LeatherGloves"},
--		},
		
		Pants = {
			items = {"Base.Trousers_Denim"},
		},
		
--		Sweater = {
--			items = {};
--		},
		
--		FullSuit = {
--			chance = 20,
--			items = {"Boilersuit"},
--		},
		
--		TorsoExtra = {
--			chance = 30,
--			items = {"Base.Vest_HighViz"},
--		},
	},
}

ClothingSelectionDefinitions.engineer = {
	Female = {
--		Neck = {
----			chance = 20,
--			items = {"Base.Tie_Full"},
--		},
		
		Shirt = {
			items = {"Base.Shirt_FormalWhite"},
		},
		
		Pants = {
			items = {"Base.Trousers_SuitTEXTURE"},
		},
		
--		Sweater = {
--			items = {};
--		},
		
--		TorsoExtra = {
----			chance = 30,
--			items = {"Base.Vest_HighViz"},
--		},
	},
}

ClothingSelectionDefinitions.mechanics = {
	Female = {
		Shirt = {
			items = {"Base.Shirt_Lumberjack"},
		},
		
--		Hands = {
--			chance = 20,
--			items = {"Base.Gloves_LeatherGloves"},
--		},
		
		Pants = {
			items = {"Base.Trousers_Denim"},
		},
		
--		Sweater = {
--			items = {};
--		},
		
--		FullSuit = {
--			chance = 20,
--			items = {"Boilersuit"},
--		},
		
--		TorsoExtra = {
--			chance = 30,
--			items = {"Base.Vest_HighViz"},
--		},
	},
}
