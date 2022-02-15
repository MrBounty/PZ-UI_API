VehicleDistributions = VehicleDistributions or {};

VehicleDistributions.GloveBox = {
    rolls = 4,
    items = {
        "AlcoholWipes", 8,
        "Bandage", 4,
        "Bandaid", 10,
        "Battery", 10,
        "BluePen", 8,
        "Disc_Retail", 2,
        "Eraser", 6,
        "Magazine", 10,
        "Notebook", 10,
        "Paperclip", 4,
        "Pen", 8,
        "Pencil", 10,
        "RedPen", 8,
        "RubberBand", 6,
        "Tissue", 10,
    },
    junk = {
        rolls = 1,
        items = {
            "Base.MarchRidgeMap", 4,
            "Base.MuldraughMap", 4,
            "Base.RiversideMap", 4,
            "Base.RosewoodMap", 4,
            "Base.WestpointMap", 4,
            "Cigarettes", 8,
            "Cologne", 4,
            "Comb", 4,
            "CreditCard", 4,
            "Earbuds", 2,
            "Glasses_Aviators", 0.05,
            "Glasses_Sun", 0.1,
            "Gloves_LeatherGloves", 0.1,
            "Gloves_LeatherGlovesBlack", 0.05,
            "HandTorch", 4,
            "HuntingKnife", 0.1,
            "Lighter", 4,
            "Lipstick", 6,
            "MakeupEyeshadow", 6,
            "MakeupFoundation", 6,
            "Matches", 8,
            "Mirror", 4,
            "Perfume", 4,
            "Pistol", 0.8,
            "Pistol2", 0.6,
            "Radio.CDplayer", 2,
            "Radio.WalkieTalkie2", 2,
            "Radio.WalkieTalkie3", 1,
            "Razor", 4,
            "Revolver_Short", 0.8,
            "Scotchtape", 8,
            "ToiletPaper", 4,
            "Twine", 10,
            "Wallet", 4,
            "Wallet2", 4,
            "Wallet3", 4,
            "Wallet4", 4,
            "WhiskeyFull", 0.5,
        }
    }
}

VehicleDistributions.TrunkStandard = {
    rolls = 1,
    items = {
        "BaseballBat", 1,
        "CarBattery1", 4,
        "DuctTape", 8,
        "EmptyPetrolCan", 10,
        "EmptySandbag", 4,
        "FirstAidKit", 4,
        "Garbagebag", 6,
        "Jack", 2,
        "LugWrench", 8,
        "NormalTire1", 20,
        "Plasticbag", 10,
        "Plasticbag", 10,
        "RubberBand", 6,
        "Screwdriver", 10,
        "Tarp", 10,
        "TirePump", 8,
        "Tissue", 10,
        "ToiletPaper", 6,
        "Tote", 6,
        "Twine", 10,
        "Wrench", 8,
    },
    junk = {
        rolls = 1,
        items = {
            "CorpseFemale", 0.01,
            "CorpseMale", 0.01,
            "PopBottleEmpty", 4,
            "PopEmpty", 4,
            "WaterBottleEmpty", 4,
            "WhiskeyEmpty", 1,
        }
    }
}

VehicleDistributions.TrunkHeavy = {
    rolls = 1,
    items = {
        "BaseballBat", 1,
        "CarBattery2", 4,
        "DuctTape", 8,
        "EmptyPetrolCan", 10,
        "EmptySandbag", 4,
        "FirstAidKit", 4,
        "Garbagebag", 6,
        "Jack", 2,
        "LugWrench", 8,
        "NormalTire2", 20,
        "Plasticbag", 10,
        "Plasticbag", 10,
        "RubberBand", 6,
        "Screwdriver", 10,
        "Tarp", 10,
        "TirePump", 8,
        "Tissue", 10,
        "ToiletPaper", 6,
        "Tote", 6,
        "Twine", 10,
        "Wrench", 8,
    },
    junk = {
        rolls = 1,
        items = {
            "CorpseFemale", 0.01,
            "CorpseMale", 0.01,
            "PopBottleEmpty", 4,
            "PopEmpty", 4,
            "WaterBottleEmpty", 4,
            "WhiskeyEmpty", 1,
        }
    }
}

VehicleDistributions.TrunkSports = {
    rolls = 1,
    items = {
        "BaseballBat", 1,
        "CarBattery3", 4,
        "DuctTape", 8,
        "EmptyPetrolCan", 10,
        "EmptySandbag", 4,
        "FirstAidKit", 4,
        "Garbagebag", 6,
        "Jack", 2,
        "LugWrench", 8,
        "NormalTire3", 20,
        "Plasticbag", 10,
        "Plasticbag", 10,
        "RubberBand", 6,
        "Screwdriver", 10,
        "Tarp", 10,
        "TirePump", 8,
        "Tissue", 10,
        "ToiletPaper", 6,
        "Tote", 6,
        "Twine", 10,
        "Wrench", 8,
    },
    junk = {
        rolls = 1,
        items = {
            "CorpseFemale", 0.01,
            "CorpseMale", 0.01,
            "PopBottleEmpty", 4,
            "PopEmpty", 4,
            "WaterBottleEmpty", 4,
            "WhiskeyEmpty", 1,
        }
    }
}

VehicleDistributions.DriverSeat = {
    rolls = 1,
    items = {

    },
    junk = {
        rolls = 1,
        items = {
            "CorpseMale", 0.01,
            "CorpseFemale", 0.01,
        }
    }
}

VehicleDistributions.Seat = {
    rolls = 1,
    items = {

    },
    junk = {
        rolls = 1,
        items = {
            "CorpseMale", 0.01,
            "CorpseFemale", 0.01,
        }
    }
}

VehicleDistributions.NormalStandard = {
    TruckBed = VehicleDistributions.TrunkStandard;

    TruckBedOpen = VehicleDistributions.TrunkStandard;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.NormalHeavy = {
    TruckBed = VehicleDistributions.TrunkHeavy;

    TruckBedOpen = VehicleDistributions.TrunkHeavy;

    TrailerTrunk = VehicleDistributions.TrunkHeavy;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.NormalSports = {
    TruckBed = VehicleDistributions.TrunkSports;

    TruckBedOpen = VehicleDistributions.TrunkSports;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.SurvivalistTruckBed = {
    rolls = 4,
    items = {
        "Aerosolbomb", 10,
        "BeefJerky", 8,
        "CannedBolognese", 10,
        "CannedCarrots2", 8,
        "CannedChili", 10,
        "CannedCorn", 8,
        "CannedCornedBeef", 10,
        "CannedMushroomSoup", 10,
        "CannedPeas", 8,
        "CannedPotato2", 8,
        "CannedSardines", 10,
        "CannedTomato2", 8,
        "Chocolate", 8,
        "Crisps", 10,
        "Crisps2", 10,
        "Crisps3", 10,
        "Crisps4", 10,
        "FlameTrap", 4,
        "Machete", 1,
        "NoiseTrap", 10,
        "PeanutButter", 10,
        "PipeBomb", 6,
        "SmokeBomb", 10,
        "TinnedBeans", 10,
        "TinnedSoup", 10,
        "TunaTin", 10,
        "WaterBottleFull", 10,
        "WaterBottleFull", 10,
    },
    junk = {
        rolls = 1,
        items = {
            "Bag_ShotgunBag", 1,
            "Bag_ShotgunDblBag", 1,
            "Bag_ShotgunDblSawnoffBag", 1,
            "Bag_ShotgunSawnoffBag", 1,
            "camping.CampfireKit", 10,
            "camping.CampingTentKit", 10,
            "Pillow", 10,
        }
    }
}

VehicleDistributions.Survivalist = {
    TruckBed = VehicleDistributions.SurvivalistTruckBed;

    TruckBedOpen = VehicleDistributions.SurvivalistTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.FishermanTruckBed = {
    rolls = 4,
    items = {
        "BaitFish", 2,
        "BaitFish", 2,
        "Pike", 1,
        "Trout", 1,
        "Panfish", 1,
        "Crappie", 1,
        "Perch", 1,
        "Bass", 1,
        "Catfish", 1,
        "FishingTackle", 20,
        "FishingTackle", 10,
        "FishingTackle2", 20,
        "FishingTackle2", 10,
        "FishingLine", 20,
        "FishingLine", 10,
        "FishingRod", 20,
        "FishingRod", 10,
        "FishingNet", 20,
        "FishingNet", 10,
    }
}

VehicleDistributions.Fisherman = {
    specificId = "Fisherman";

    TruckBed = VehicleDistributions.FishermanTruckBed;

    TruckBedOpen = VehicleDistributions.FishermanTruckBed;

    TrailerTrunk =  VehicleDistributions.FishermanTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.GroceriesTruckBed = {
    rolls = 4,
    items = {
        "Apple", 2,
        "Avocado", 0.5,
        "BakingSoda", 2,
        "Baloney", 2,
        "Banana", 2,
        "BellPepper", 1,
        "Broccoli", 1,
        "Butter", 2,
        "CandyPackage", 1,
        "CannedBolognese", 2,
        "CannedCarrots2", 1,
        "CannedChili", 2,
        "CannedCorn", 1,
        "CannedCornedBeef", 2,
        "CannedMushroomSoup", 2,
        "CannedPeas", 1,
        "CannedPotato2", 1,
        "CannedSardines", 2,
        "CannedTomato2", 1,
        "Carrots", 2,
        "Cereal", 2,
        "Cheese", 2,
        "Cherry", 2,
        "Chicken", 0.4,
        "Chocolate", 2,
        "ChocolateChips", 2,
        "CocoaPowder", 2,
        "Coffee2", 2,
        "Corn", 2,
        "Crackers", 2,
        "Crisps", 2,
        "Crisps2", 2,
        "Crisps3", 2,
        "Crisps4", 2,
        "EggCarton", 0.4,
        "Eggplant", 2,
        "farming.Bacon", 0.4,
        "farming.Cabbage", 2,
        "farming.MayonnaiseFull", 0.1,
        "farming.RedRadish", 2,
        "farming.RemouladeFull", 0.1,
        "farming.Strewberrie", 2,
        "farming.Tomato", 2,
        "Flour", 4,
        "Grapefruit", 2,
        "Grapes", 2,
        "Gum", 2,
        "Ham", 0.4,
        "Hotsauce", 0.1,
        "JamFruit", 2,
        "JamMarmalade", 1,
        "JuiceBox", 0.5,
        "Ketchup", 0.1,
        "Leek", 2,
        "Lemon", 2,
        "Lettuce", 2,
        "Lime", 2,
        "Macandcheese", 2,
        "MapleSyrup", 0.1,
        "Marinara", 2,
        "MeatPatty", 2,
        "Milk", 2,
        "MincedMeat", 2,
        "Mustard", 0.1,
        "MuttonChop", 0.25,
        "OatsRaw", 4,
        "OilOlive", 2,
        "OilVegetable", 2,
        "Onion", 2,
        "Orange", 2,
        "Pasta", 2,
        "Peach", 2,
        "PeanutButter", 2,
        "Peanuts", 2,
        "Pear", 2,
        "PepperJalapeno", 0.5,
        "Pickles", 2,
        "Pineapple", 2,
        "PopBottle", 1,
        "Popcorn", 2,
        "PorkChop", 2,
        "Processedcheese", 2,
        "Ramen", 2,
        "Rice", 2,
        "Salami", 0.4,
        "Salmon", 0.25,
        "Sausage", 0.4,
        "Soysauce", 0.1,
        "Squid", 0.5,
        "Steak", 0.25,
        "Sugar", 2,
        "SugarBrown", 2,
        "SunflowerSeeds", 2,
        "TacoShell", 2,
        "Teabag2", 2,
        "TinnedBeans", 2,
        "TinnedSoup", 2,
        "TortillaChips", 2,
        "TunaTin", 2,
        "TVDinner", 2,
        "WaterBottleFull", 0.5,
        "WaterBottleFull", 2,
        "Watermelon", 0.1,
        "Yeast", 1,
        "Yoghurt", 0.5,
        "Zucchini", 2,
    }
}

VehicleDistributions.Groceries = {
    TruckBed = VehicleDistributions.GroceriesTruckBed;

    TruckBedOpen = VehicleDistributions.GroceriesTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.GolfTruckBed = {
    rolls = 1,
    items = {
        "Bag_GolfBag", 200,
        "Bag_GolfBag", 50,
        "Bag_GolfBag", 20,
        "Bag_GolfBag", 10,
    }
}

VehicleDistributions.Golf = {
    TruckBed = VehicleDistributions.GolfTruckBed;

    TruckBedOpen = VehicleDistributions.GolfTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.ClothingTruckBed = {
    rolls = 4,
    items = {
        "DressKnees_Straps", 1,
        "Dress_Knees", 1,
        "Dress_Long", 0.6,
        "Dress_long_Straps", 0.6,
        "Dress_Normal", 1,
        "Dress_Short", 1,
        "Dress_SmallBlackStrapless", 1,
        "Dress_SmallBlackStraps", 1,
        "Dress_SmallStrapless", 1,
        "Dress_SmallStraps", 1,
        "Dress_Straps", 1,
        "Gloves_FingerlessGloves", 1,
        "Gloves_LeatherGloves", 0.6,
        "Gloves_LeatherGlovesBlack", 0.4,
        "Gloves_LongWomenGloves", 0.4,
        "Gloves_WhiteTINT", 1,
        "Hat_BalaclavaFace", 0.8,
        "Hat_BalaclavaFull", 0.8,
        "Hat_BaseballCap", 0.8,
        "Hat_BaseballCapBlue", 0.8,
        "Hat_BaseballCapGreen", 0.8,
        "Hat_BaseballCapKY", 0.8,
        "Hat_BaseballCapKY_Red", 0.8,
        "Hat_BaseballCapRed", 0.8,
        "Hat_Beany", 0.6,
        "Hat_Beret", 0.6,
        "Hat_BucketHat", 0.6,
        "Hat_Cowboy", 0.4,
        "Hat_EarMuffs", 0.8,
        "Hat_Fedora", 0.4,
        "Hat_Fedora_Delmonte", 0.2,
        "Hat_GolfHat", 0.8,
        "Hat_GolfHatTINT", 0.8,
        "Hat_SummerHat", 0.4,
        "Hat_Sweatband", 1,
        "Hat_VisorBlack", 0.8,
        "Hat_VisorRed", 0.8,
        "Hat_Visor_WhiteTINT", 1,
        "Hat_WinterHat", 1,
        "Hat_WoolyHat", 1,
        "HoodieDOWN_WhiteTINT", 0.8,
        "HoodieDOWN_WhiteTINT", 1,
        "JacketLong_Random", 0.2,
        "Jacket_Black", 0.2,
        "Jacket_PaddedDOWN", 0.8,
        "Jacket_PaddedDOWN", 0.8,
        "Jacket_Shellsuit_Black", 0.4,
        "Jacket_Shellsuit_Blue", 0.4,
        "Jacket_Shellsuit_Green", 0.4,
        "Jacket_Shellsuit_Pink", 0.4,
        "Jacket_Shellsuit_Teal", 0.4,
        "Jacket_Shellsuit_TINT", 1,
        "Jacket_Varsity", 0.2,
        "Jacket_WhiteTINT", 1,
        "Jumper_DiamondPatternTINT", 0.4,
        "Jumper_PoloNeck", 0.6,
        "Jumper_RoundNeck", 0.8,
        "Jumper_VNeck", 0.8,
        "LongJohns", 0.6,
        "LongJohns_Bottoms", 0.8,
        "Scarf_StripeBlackWhite", 1,
        "Scarf_StripeBlueWhite", 1,
        "Scarf_StripeRedWhite", 1,
        "Scarf_White", 1,
        "SewingKit", 10,
        "Shirt_Baseball_KY", 0.6,
        "Shirt_Baseball_Rangers", 0.6,
        "Shirt_Baseball_Z", 0.6,
        "Shirt_Denim", 0.8,
        "Shirt_FormalTINT", 0.8,
        "Shirt_FormalWhite", 1,
        "Shirt_FormalWhite", 1,
        "Shirt_FormalWhite_ShortSleeve", 0.8,
        "Shirt_FormalWhite_ShortSleeveTINT", 0.8,
        "Shirt_Lumberjack", 1,
        "Shoes_ArmyBoots", 0.6,
        "Shoes_ArmyBootsDesert", 0.6,
        "Shoes_Black", 1,
        "Shoes_BlackBoots", 0.6,
        "Shoes_BlueTrainers", 0.8,
        "Shoes_Brown", 1,
        "Shoes_Fancy", 0.6,
        "Shoes_FlipFlop", 0.8,
        "Shoes_RedTrainers", 0.8,
        "Shoes_RidingBoots", 0.6,
        "Shoes_Sandals", 0.8,
        "Shoes_Strapped", 0.8,
        "Shoes_TrainerTINT", 1,
        "Shoes_Wellies", 0.4,
        "Shorts_LongSport", 0.8,
        "Shorts_ShortSport", 0.8,
        "Skirt_Knees", 1,
        "Skirt_Long", 0.6,
        "Skirt_Normal", 1,
        "Socks_Ankle", 1,
        "Socks_Long", 0.6,
        "Suit_Jacket", 1,
        "Suit_JacketTINT", 0.8,
        "TrousersMesh_DenimLight", 0.8,
        "TrousersMesh_Leather", 0.2,
        "Trousers_DefaultTEXTURE_TINT", 0.8,
        "Trousers_Denim", 0.6,
        "Trousers_JeanBaggy", 0.6,
        "Trousers_LeatherBlack", 0.2,
        "Trousers_Padded", 0.8,
        "Trousers_Shellsuit_Black", 0.4,
        "Trousers_Shellsuit_Blue", 0.4,
        "Trousers_Shellsuit_Green", 0.4,
        "Trousers_Shellsuit_Pink", 0.4,
        "Trousers_Shellsuit_Teal", 0.4,
        "Trousers_Shellsuit_TINT", 1,
        "Trousers_Suit", 0.4,
        "Trousers_Suit", 1,
        "Trousers_SuitTEXTURE", 0.6,
        "Trousers_SuitTEXTURE", 0.8,
        "Trousers_SuitWhite", 0.4,
        "Trousers_SuitWhite", 0.8,
        "Trousers_WhiteTINT", 0.8,
        "Tshirt_DefaultDECAL_TINT", 0.8,
        "Tshirt_DefaultTEXTURE_TINT", 0.8,
        "Tshirt_IndieStoneDECAL", 0.4,
        "Tshirt_PoloStripedTINT", 0.6,
        "Tshirt_PoloTINT", 0.6,
        "Tshirt_Sport", 1,
        "Tshirt_SportDECAL", 0.8,
        "Tshirt_WhiteLongSleeveTINT", 1,
        "Tshirt_WhiteTINT", 1,
        "Vest_DefaultTEXTURE_TINT", 1,
    }
}

VehicleDistributions.Clothing = {
    TruckBed = VehicleDistributions.ClothingTruckBed;

    TruckBedOpen = VehicleDistributions.ClothingTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.CarpenterTruckBed = {
    rolls = 4,
    items = {
        "Axe", 0.05,
        "BookCarpentry1", 10,
        "BookCarpentry2", 8,
        "BookCarpentry3", 6,
        "BookCarpentry4", 4,
        "BookCarpentry5", 2,
        "ClubHammer", 4,
        "DuctTape", 8,
        "DuctTape", 8,
        "GardenSaw", 6,
        "Glasses_SafetyGoggles", 2,
        "Glue", 8,
        "Hammer", 6,
        "Hammer", 6,
        "HandAxe", 1,
        "Hat_DustMask", 2,
        "Hat_HardHat", 1,
        "NailsBox", 10,
        "NailsBox", 10,
        "NailsBox", 10,
        "NailsBox", 20,
        "Plank", 10,
        "Plank", 10,
        "Plank", 10,
        "Plank", 20,
        "Saw", 4,
        "Screwdriver", 8,
        "ScrewsBox", 8,
        "Twine", 10,
        "WoodAxe", 0.025,
        "WoodenMallet", 4,
        "Woodglue", 8,
    },
    junk = {
        rolls = 1,
        items = {
            "Toolbox", 20,
        }
    }
}

VehicleDistributions.Carpenter = {
    specificId = "Carpenter";

    TruckBed = VehicleDistributions.CarpenterTruckBed;

    TruckBedOpen = VehicleDistributions.CarpenterTruckBed;

    TrailerTrunk =  VehicleDistributions.CarpenterTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.ElectricianTruckBed = {
    rolls = 4,
    items = {
        "Aluminum", 8,
        "Amplifier", 8,
        "BookElectrician1", 10,
        "BookElectrician2", 8,
        "BookElectrician3", 6,
        "BookElectrician4", 4,
        "BookElectrician5", 2,
        "DuctTape", 8,
        "ElectronicsMag1", 2,
        "ElectronicsMag2", 2,
        "ElectronicsMag3", 2,
        "ElectronicsMag4", 2,
        "ElectronicsMag5", 2,
        "ElectronicsScrap", 20,
        "ElectronicsScrap", 10,
        "EngineerMagazine1", 2,
        "EngineerMagazine2", 2,
        "MetalPipe", 6,
        "MotionSensor", 8,
        "Radio.ElectricWire", 20,
        "Radio.ElectricWire", 10,
        "Radio.RadioMag1", 2,
        "Radio.RadioMag2", 2,
        "Radio.RadioMag3", 2,
        "RemoteCraftedV1", 8,
        "RemoteCraftedV2", 4,
        "RemoteCraftedV3", 2,
        "Screwdriver", 10,
        "Sparklers", 8,
        "TimerCrafted", 8,
        "TriggerCrafted", 8,
        "Twine", 10,
    }
}

VehicleDistributions.Electrician = {
    specificId = "Electrician";

    TruckBed = VehicleDistributions.ElectricianTruckBed;

    TruckBedOpen = VehicleDistributions.ElectricianTruckBed;

    TrailerTrunk =  VehicleDistributions.ElectricianTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.FarmerTruckBed = {
    rolls = 4,
    items = {
        "BookFarming1", 10,
        "BookFarming2", 8,
        "BookFarming3", 6,
        "BookFarming4", 4,
        "BookFarming5", 2,
        "BookForaging1", 10,
        "BookForaging2", 8,
        "BookForaging3", 6,
        "BookForaging4", 4,
        "BookForaging5", 2,
        "BucketEmpty", 4,
        "CompostBag", 10,
        "farming.BroccoliBagSeed", 8,
        "farming.CabbageBagSeed", 8,
        "farming.CarrotBagSeed", 8,
        "farming.GardeningSprayEmpty", 6,
        "farming.HandShovel", 6,
        "farming.PotatoBagSeed", 8,
        "farming.RedRadishBagSeed", 8,
        "farming.StrewberrieBagSeed", 8,
        "farming.TomatoBagSeed", 8,
        "farming.WateredCan", 6,
        "FarmingMag1", 2,
        "Fertilizer", 10,
        "GardenFork", 1,
        "GardenHoe", 2,
        "HandAxe", 1,
        "HandFork", 2,
        "HandScythe", 2,
        "LeafRake", 10,
        "PickAxe", 0.5,
        "Rake", 10,
        "SeedBag", 2,
    }
}

VehicleDistributions.Farmer = {
    TruckBed = VehicleDistributions.FarmerTruckBed;

    TruckBedOpen = VehicleDistributions.FarmerTruckBed;

    TrailerTrunk =  VehicleDistributions.FarmerTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.MetalWelderTruckBed = {
    rolls = 4,
    items = {
        "BallPeenHammer", 4,
        "BlowTorch", 8,
        "BookMetalWelding1", 10,
        "BookMetalWelding2", 8,
        "BookMetalWelding3", 6,
        "BookMetalWelding4", 4,
        "BookMetalWelding5", 2,
        "Glasses_SafetyGoggles", 10,
        "Hat_DustMask", 10,
        "MetalBar", 20,
        "MetalBar", 10,
        "MetalPipe", 20,
        "MetalPipe", 10,
        "MetalworkMag1", 2,
        "MetalworkMag2", 2,
        "MetalworkMag3", 2,
        "MetalworkMag4", 2,
        "PropaneTank", 2,
        "Screwdriver", 10,
        "SheetMetal", 20,
        "SheetMetal", 10,
        "SmallSheetMetal", 20,
        "SmallSheetMetal", 10,
        "WeldingMask", 8,
        "WeldingRods", 20,
        "WeldingRods", 10,
        "Wire", 10,
    }
}

VehicleDistributions.MetalWelder = {
    TruckBed = VehicleDistributions.MetalWelderTruckBed;

    TruckBedOpen = VehicleDistributions.MetalWelderTruckBed;

    TrailerTrunk =  VehicleDistributions.MetalWelderTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.DoctorTruckBed = {
    rolls = 4,
    items = {
        "AlcoholWipes", 20,
        "AlcoholWipes", 10,
        "Antibiotics", 2,
        "Bandage", 20,
        "Bandage", 10,
        "Bandaid", 20,
        "Bandaid", 10,
        "Corset_Medical", 2,
        "CottonBalls", 20,
        "CottonBalls", 10,
        "Disinfectant", 8,
        "FirstAidKit", 10,
        "Gloves_Surgical", 10,
        "Hat_SurgicalMask_Blue", 10,
        "JacketLong_Doctor", 4,
        "Pills", 10,
        "PillsAntiDep", 10,
        "PillsBeta", 10,
        "PillsSleepingTablets", 10,
        "Scalpel", 10,
        "Scissors", 8,
        "Shirt_Scrubs", 8,
        "SutureNeedle", 20,
        "SutureNeedle", 10,
        "SutureNeedleHolder", 20,
        "SutureNeedleHolder", 10,
        "Trousers_Scrubs", 8,
        "Tweezers", 10,
    }
}

VehicleDistributions.Doctor = {
    TruckBed = VehicleDistributions.DoctorTruckBed;

    TruckBedOpen = VehicleDistributions.DoctorTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.Radio = {
    TruckBed = {
        rolls = 4,
        items = {
            "Disc_Retail", 10,
            "Headphones", 10,
            "Radio.ElectricWire", 20,
            "Radio.ElectricWire", 10,
            "Radio.RadioBlack", 2,
            "Radio.RadioMag1", 2,
            "Radio.RadioMag2", 2,
            "Radio.RadioMag3", 2,
            "Radio.RadioReceiver", 8,
            "Radio.RadioRed", 1,
            "Radio.RadioTransmitter", 8,
            "Radio.ScannerModule", 8,
            "Radio.WalkieTalkie2", 10,
            "Radio.WalkieTalkie3", 8,
            "Speaker", 8,
            "Speaker", 8,
        },
        junk = {
            rolls = 1,
            items = {
                "Radio.HamRadio1", 100,
            }
        }
    },

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.PainterTruckBed = {
    rolls = 4,
    items = {
        "BucketEmpty", 4,
        "PaintBlue", 4,
        "PaintBlack", 4,
        "Paintbrush", 10,
        "PaintRed", 4,
        "PaintBrown", 4,
        "PaintCyan", 4,
        "PaintGreen", 4,
        "PaintGrey", 4,
        "PaintLightBlue", 4,
        "PaintLightBrown", 4,
        "PaintOrange", 4,
        "PaintPink", 4,
        "PaintPurple", 4,
        "PaintTurquoise", 4,
        "PaintWhite", 4,
        "PaintYellow", 4,
    },
    junk = {
        rolls = 1,
        items = {
            "Paintbrush", 75,
            "BucketEmpty", 25,
        }
    }
}

VehicleDistributions.Painter = {
    TruckBed = VehicleDistributions.PainterTruckBed;

    TruckBedOpen = VehicleDistributions.PainterTruckBed;

    TrailerTrunk =  VehicleDistributions.PainterTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.ConstructionWorkerTruckBed = {
    rolls = 4,
    items = {
        "BarbedWire", 10,
        "BlowTorch", 8,
        "BucketEmpty", 4,
        "ConcretePowder", 10,
        "DuctTape", 8,
        "EmptySandbag", 10,
        "Glue", 8,
        "Gravelbag", 10,
        "Hammer", 10,
        "LeadPipe", 6,
        "MetalBar", 6,
        "MetalPipe", 6,
        "NailsBox", 10,
        "NailsBox", 20,
        "PickAxe", 0.5,
        "PipeWrench", 6,
        "Plank", 20,
        "Plank", 10,
        "PlasterPowder", 10,
        "Saw", 8,
        "Screwdriver", 10,
        "SheetMetal", 20,
        "SheetMetal", 10,
        "Shovel", 4,
        "Shovel2", 4,
        "Sledgehammer", 0.01,
        "Sledgehammer2", 0.01,
        "SmallSheetMetal", 20,
        "SmallSheetMetal", 10,
        "Twine", 10,
        "WeldingMask", 8,
        "WeldingRods", 20,
        "WeldingRods", 10,
        "Wire", 10,
        "Woodglue", 8,
    }
}

VehicleDistributions.ConstructionWorker = {
    specificId = "ConstructionWorker";

    TruckBed = VehicleDistributions.ConstructionWorkerTruckBed;

    TruckBedOpen = VehicleDistributions.ConstructionWorkerTruckBed;

    TrailerTrunk =  VehicleDistributions.ConstructionWorkerTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.Taxi = {
    TruckBed = {
        rolls = 4,
        items = {
            "CarBattery1", 4,
            "DuctTape", 8,
            "EmptyPetrolCan", 10,
            "EmptySandbag", 4,
            "FirstAidKit", 4,
            "Jack", 2,
            "LugWrench", 8,
            "NormalTire1", 20,
            "Screwdriver", 10,
            "Tarp", 10,
            "TirePump", 8,
            "Tissue", 10,
            "ToiletPaper", 6,
            "Tote", 6,
            "Twine", 10,
            "Wrench", 8,
        },
        junk = {
            rolls = 1,
            items = {
                "Bag_BigHikingBag", 1,
                "Bag_NormalHikingBag", 2,
                "Briefcase", 4,
                "PopBottleEmpty", 4,
                "PopEmpty", 4,
                "Suitcase", 4,
                "WaterBottleEmpty", 4,
                "WhiskeyEmpty", 1,
            }
        }
    },

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.Police = {
    TruckBed = {
        rolls = 4,
        items = {
            "308Box", 10,
            "AmmoStrap_Bullets", 4,
            "AmmoStrap_Shells", 4,
            "AssaultRifle2", 1,
            "Bullets9mmBox", 20,
            "Bullets9mmBox", 10,
            "FiberglassStock", 4,
            "HandTorch", 4,
            "Hat_CrashHelmet_Police", 2,
            "Hat_EarMuff_Protectors", 4,
            "HolsterDouble", 1,
            "HolsterSimple", 4,
            "HuntingRifle", 8,
            "M14Clip", 4,
            "Nightstick", 2,
            "Pistol", 10,
            "Radio.WalkieTalkie4", 10,
            "Shotgun", 10,
            "ShotgunShellsBox", 10,
            "Sling", 4,
            "Vest_BulletPolice", 2,
            "x2Scope", 4,
            "x4Scope", 2,
            "x8Scope", 1,
        },
        junk = {
            rolls = 1,
            items = {
                "FirstAidKit", 20,
                "Radio.HamRadio1", 20,
            }
        }
    },

    GloveBox = {
        rolls = 4,
        items = {
            "AlcoholWipes", 8,
            "Bandage", 4,
            "Bandaid", 10,
            "Battery", 10,
            "BluePen", 8,
            "Bullets9mmBox", 20,
            "Bullets9mmBox", 10,
            "Disc_Retail", 2,
            "Eraser", 6,
            "HandTorch", 4,
            "Magazine", 10,
            "Notebook", 10,
            "Paperclip", 4,
            "Pen", 8,
            "Pencil", 10,
            "Radio.WalkieTalkie4", 10,
            "RedPen", 8,
            "RubberBand", 6,
            "Tissue", 10,
        },
        junk = {
            rolls = 1,
            items = {
                "Base.MarchRidgeMap", 4,
                "Base.MuldraughMap", 4,
                "Base.RiversideMap", 4,
                "Base.RosewoodMap", 4,
                "Base.WestpointMap", 4,
                "Cigarettes", 8,
                "FirstAidKit", 20,
                "Glasses_Aviators", 0.5,
                "Gloves_LeatherGlovesBlack", 0.5,
                "HandTorch", 4,
                "HuntingKnife", 0.1,
                "Lighter", 4,
                "Matches", 8,
                "Pistol", 20,
                "Scotchtape", 8,
                "ToiletPaper", 4,
                "Twine", 10,
            }
        }
    },

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.RangerTruckBed = {
    rolls = 4,
    items = {
        "DeadBird", 4,
        "DeadRabbit", 4,
        "DeadSquirrel", 4,
        "EmptySandbag", 10,
        "farming.Shovel", 4,
        "farming.Shovel2", 4,
        "GardenHoe", 2,
        "PickAxe", 0.5,
        "Radio.WalkieTalkie4", 10,
        "TrapBox", 4,
        "TrapCage", 4,
        "TrapCrate", 4,
        "TrapSnare", 4,
        "TrapStick", 4,
    },
    junk = {
        rolls = 1,
        items = {
            "ShotgunCase1", 10,
            "ShotgunCase2", 8,
            "FirstAidKit", 20,
        }
    }
}

VehicleDistributions.Ranger = {
    TruckBed = VehicleDistributions.RangerTruckBed;

    TruckBedOpen = VehicleDistributions.RangerTruckBed;

    TrailerTrunk =  VehicleDistributions.RangerTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.FireTruckBed = {
    rolls = 4,
    items = {
        "Axe", 4,
        "Axe", 4,
        "Bandage", 10,
        "BucketEmpty", 4,
        "Disinfectant", 8,
        "Hat_Fireman", 4,
        "Hat_GasMask", 1,
        "Jacket_Fireman", 4,
        "LongJohns", 2,
        "PickAxe", 0.5,
        "Pills", 10,
        "Radio.WalkieTalkie4", 10,
        "Rope", 10,
        "Shoes_ArmyBoots", 4,
        "Socks_Long", 6,
        "Tarp", 10,
        "Trousers_Fireman", 8,
    },
    junk = {
        rolls = 1,
        items = {
            "FirstAidKit", 20,
        }
    }
}

VehicleDistributions.Fire = {
    TruckBed = VehicleDistributions.FireTruckBed;

    TruckBedOpen = VehicleDistributions.FireTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.McCoyTruckBed = {
    rolls = 4,
    items = {
        "Axe", 4,
        "Log", 10,
        "Log", 10,
        "Log", 20,
        "Log", 20,
        "Rope", 10,
        "Saw", 8,
        "Tarp", 10,
        "WoodAxe", 2,
    }
}

VehicleDistributions.McCoy = {
    TruckBed = VehicleDistributions.McCoyTruckBed;

    TruckBedOpen = VehicleDistributions.McCoyTruckBed;

    TrailerTrunk =  VehicleDistributions.McCoyTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.HunterTruckBed = {
    rolls = 4,
    items = {
        "DeadBird", 8,
        "DeadRabbit", 8,
        "DeadSquirrel", 8,
        "HuntingKnife", 8,
        "Jacket_ArmyCamoGreen", 6,
        "Machete", 1,
        "PonchoGreenDOWN", 6,
        "Tarp", 10,
        "TrapBox", 4,
        "TrapCage", 4,
        "TrapCrate", 4,
        "TrapSnare", 4,
        "TrapStick", 4,
        "Vest_Hunting_Camo", 6,
        "Vest_Hunting_CamoGreen", 6,
        "Vest_Hunting_Orange", 6,
    },
    junk = {
        rolls = 1,
        items = {
            "Bag_ALICEpack", 0.5,
            "Bag_BigHikingBag", 2,
            "Bag_NormalHikingBag", 4,
            "camping.CampfireKit", 10,
            "camping.CampingTentKit", 10,
            "RifleCase1", 50,
            "RifleCase2", 20,
            "RifleCase3", 10,
            "ShotgunCase1", 20,
            "ShotgunCase2", 10,
        }
    }
}

VehicleDistributions.Hunter = {
    specificId = "Hunter",

    TruckBed = VehicleDistributions.HunterTruckBed;

    TruckBedOpen = VehicleDistributions.HunterTruckBed;

    TrailerTrunk =  VehicleDistributions.HunterTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.FossoilTruckBed = {
    rolls = 4,
    items = {
        "EmptyPetrolCan", 20,
        "EmptyPetrolCan", 20,
        "EmptyPetrolCan", 10,
        "EmptyPetrolCan", 10,
        "PetrolCan", 4,
    }
}

VehicleDistributions.Fossoil = {
    TruckBed = VehicleDistributions.FossoilTruckBed;

    TruckBedOpen = VehicleDistributions.FossoilTruckBed;

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.Postal = {
    TruckBed =
    {
        rolls = 4,
        items = {
            "BookCarpentry1", 6,
            "BookCarpentry2", 4,
            "BookCarpentry3", 2,
            "BookCarpentry4", 1,
            "BookCarpentry5", 0.5,
            "BookCooking1", 6,
            "BookCooking2", 4,
            "BookCooking3", 2,
            "BookCooking4", 1,
            "BookCooking5", 0.5,
            "BookElectrician1", 6,
            "BookElectrician2", 4,
            "BookElectrician3", 2,
            "BookElectrician4", 1,
            "BookElectrician5", 0.5,
            "BookFarming1", 6,
            "BookFarming2", 4,
            "BookFarming3", 2,
            "BookFarming4", 1,
            "BookFarming5", 0.5,
            "BookFirstAid1", 6,
            "BookFirstAid2", 4,
            "BookFirstAid3", 2,
            "BookFirstAid4", 1,
            "BookFirstAid5", 0.5,
            "BookFishing1", 6,
            "BookFishing2", 4,
            "BookFishing3", 2,
            "BookFishing4", 1,
            "BookFishing5", 0.5,
            "BookForaging1", 6,
            "BookForaging2", 4,
            "BookForaging3", 2,
            "BookForaging4", 1,
            "BookForaging5", 0.5,
            "BookMechanic1", 6,
            "BookMechanic2", 4,
            "BookMechanic3", 2,
            "BookMechanic4", 1,
            "BookMechanic5", 0.5,
            "BookMetalWelding1", 6,
            "BookMetalWelding2", 4,
            "BookMetalWelding3", 2,
            "BookMetalWelding4", 1,
            "BookMetalWelding5", 0.5,
            "BookTailoring1", 6,
            "BookTailoring2", 4,
            "BookTailoring3", 2,
            "BookTailoring4", 1,
            "BookTailoring5", 0.5,
            "BookTrapping1", 6,
            "BookTrapping2", 4,
            "BookTrapping3", 2,
            "BookTrapping4", 1,
            "BookTrapping5", 0.5,
            "ComicBook", 8,
            "ComicBook", 8,
            "CookingMag1", 0.5,
            "CookingMag2", 0.5,
            "ElectronicsMag1", 0.5,
            "ElectronicsMag2", 0.5,
            "ElectronicsMag3", 0.5,
            "ElectronicsMag4", 0.5,
            "ElectronicsMag5", 0.5,
            "EngineerMagazine1", 0.5,
            "EngineerMagazine2", 0.5,
            "FarmingMag1", 0.5,
            "FarmingMag1", 1,
            "FishingMag1", 0.5,
            "FishingMag2", 0.5,
            "HerbalistMag", 0.5,
            "HottieZ", 0.1,
            "HuntingMag1", 0.5,
            "HuntingMag2", 0.5,
            "HuntingMag3", 0.5,
            "Magazine", 50,
            "Magazine", 20,
            "Magazine", 20,
            "Magazine", 10,
            "Magazine", 10,
            "MechanicMag1", 0.5,
            "MechanicMag2", 0.5,
            "MechanicMag3", 0.5,
            "MetalworkMag1", 0.5,
            "MetalworkMag2", 0.5,
            "MetalworkMag3", 0.5,
            "MetalworkMag4", 0.5,
            "Newspaper", 50,
            "Newspaper", 20,
            "Newspaper", 20,
            "Newspaper", 10,
            "Newspaper", 10,
        }
    },

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.Spiffo = {
    TruckBed =
    {
        rolls = 4,
        items = {
            "Apron_Spiffos", 8,
            "FountainCup", 20,
            "FountainCup", 10,
            "Hat_FastFood_Spiffo", 8,
            "MugSpiffo", 0.005,
            "Paperbag_Spiffos", 20,
            "Paperbag_Spiffos", 10,
            "Pop", 10,
            "Pop2", 10,
            "Pop3", 10,
            "PopBottle", 8,
            "Spiffo", 0.005,
            "Tie_Full_Spiffo", 8,
            "Tie_Worn_Spiffo", 8,
            "Tshirt_BusinessSpiffo", 8,
            "Tshirt_SpiffoDECAL", 8,
        },
        junk = {
            rolls = 1,
            items = {
                "Hat_Spiffo", 0.5,
                "SpiffoBig", 0.001,
                "SpiffoSuit", 0.5,
                "SpiffoTail", 0.5,
            }
        }
    },

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.MassGenFac = {
    TruckBed =
    {
        rolls = 4,
        items = {
            "BallPeenHammer", 4,
            "MetalBar", 6,
            "MetalBar", 6,
            "MetalPipe", 6,
            "MetalPipe", 6,
            "ScrapMetal", 2,
            "ScrapMetal", 2,
            "WeldingRods", 8,
            "Screwdriver", 10,
            "SheetMetal", 8,
            "SheetMetal", 8,
            "SmallSheetMetal", 8,
            "SmallSheetMetal", 8,
            "Wire", 6,
            "Wire", 6,
        },
        junk = {
            rolls = 1,
            items = {
                "WeldingMask", 8,
                "BlowTorch", 8,
                "Glasses_SafetyGoggles", 10,
                "Hat_DustMask", 10,
                "PropaneTank", 2,
            }
        }
    },

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.Transit = {
    TruckBed =
    {
        rolls = 4,
        items = {
            "LugWrench", 10,
            "Wrench", 10,
            "HandTorch", 4,
            "Screwdriver", 10,
            "TirePump", 10,
            "Torch", 2,
            "Jack", 10,
            "BlowTorch", 10,
        },
        junk = {
            rolls = 1,
            items = {
                "TirePump", 50,
                "Jack", 75,
                "Toolbox", 100,
            }
        }
    },

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.Distillery = {
    TruckBed =
    {
        rolls = 8,
        items = {
            "WhiskeyFull", 10,
            "WhiskeyFull", 10,
        },
        junk = {
            rolls = 4,
            items = {
                "WhiskeyFull", 10,
            }
        }
    },

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

VehicleDistributions.Heralds = {
    TruckBed =
    {
        rolls = 15,
        items = {
            "Journal", 10,
            "Journal", 10,
        },
        junk = {
            rolls = 5,
            items = {
                "Journal", 10,
            }
        }
    },

    GloveBox = VehicleDistributions.GloveBox;

    SeatRearLeft = VehicleDistributions.Seat;
    SeatRearRight = VehicleDistributions.Seat;
}

local distributionTable = {

    -- Classic cars
    SportsCar = {
        Normal = VehicleDistributions.NormalSports,
        Specific = { VehicleDistributions.Doctor, VehicleDistributions.Groceries, VehicleDistributions.Golf, VehicleDistributions.Clothing },
    },

    ModernCar = {
        Normal = VehicleDistributions.NormalSports,
        Specific = { VehicleDistributions.Doctor, VehicleDistributions.Groceries, VehicleDistributions.Golf, VehicleDistributions.Clothing },
    },

    ModernCar02 = {
        Normal = VehicleDistributions.NormalSports,
        Specific = { VehicleDistributions.Doctor, VehicleDistributions.Groceries, VehicleDistributions.Golf, VehicleDistributions.Clothing },
    },

    CarLuxury = {
        Normal = VehicleDistributions.NormalSports,
        Specific = { VehicleDistributions.Doctor, VehicleDistributions.Groceries, VehicleDistributions.Golf, VehicleDistributions.Clothing },
    },

    CarNormal =  {
        Normal = VehicleDistributions.NormalSports,
        Specific = { VehicleDistributions.Groceries, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.Survivalist, VehicleDistributions.Clothing, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    CarLights =  {
        Normal = VehicleDistributions.NormalSports,
        Specific = { VehicleDistributions.Groceries, VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Electrician, VehicleDistributions.Clothing, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    SmallCar =  {
        Normal = VehicleDistributions.NormalStandard,
        Specific = { VehicleDistributions.Groceries, VehicleDistributions.Fisherman, VehicleDistributions.Electrician, VehicleDistributions.Clothing },
    },

    SmallCar02 =  {
        Normal = VehicleDistributions.NormalStandard,
        Specific = { VehicleDistributions.Groceries, VehicleDistributions.Fisherman, VehicleDistributions.Electrician, VehicleDistributions.Clothing },
    },

    CarStationWagon =  {
        Normal = VehicleDistributions.NormalStandard,
        Specific = { VehicleDistributions.Groceries, VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.Survivalist, VehicleDistributions.Clothing, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    CarStationWagon2 =  {
        Normal = VehicleDistributions.NormalStandard,
        Specific = { VehicleDistributions.Groceries, VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.Survivalist, VehicleDistributions.Clothing, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    Van =  {
        Normal = VehicleDistributions.NormalHeavy,
        Specific = { VehicleDistributions.Groceries, VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    StepVan = {
        Normal = VehicleDistributions.NormalHeavy,
        Specific = { VehicleDistributions.Groceries, VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    VanSeats = {
        Normal = VehicleDistributions.NormalHeavy,
        Specific = { VehicleDistributions.Groceries, VehicleDistributions.Fisherman, VehicleDistributions.Golf, VehicleDistributions.Clothing },
    },

    OffRoad = {
        Normal = VehicleDistributions.NormalStandard,
        Specific = { VehicleDistributions.Hunter, VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.Survivalist, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    SUV = {
        Normal = VehicleDistributions.NormalHeavy,
        Specific = { VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.Survivalist, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    PickUpVan = {
        Normal = VehicleDistributions.NormalHeavy,
        Specific = { VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.Survivalist, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    PickUpVanLights = {
        Normal = VehicleDistributions.NormalHeavy,
        Specific = { VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.Survivalist, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    PickUpTruck = {
        Normal = VehicleDistributions.NormalHeavy,
        Specific = { VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.Survivalist, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    PickUpTruckLights = {
        Normal = VehicleDistributions.NormalHeavy,
        Specific = { VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.Survivalist, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    Trailer = {
        Normal = VehicleDistributions.NormalHeavy,
        Specific = { VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.Survivalist, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    TrailerCover = {
        Normal = VehicleDistributions.NormalHeavy,
        Specific = { VehicleDistributions.Fisherman, VehicleDistributions.Carpenter, VehicleDistributions.Farmer, VehicleDistributions.Electrician, VehicleDistributions.MetalWelder, VehicleDistributions.Survivalist, VehicleDistributions.ConstructionWorker, VehicleDistributions.Painter },
    },

    -- Specific cars like police, fire, ranger... We simply add their skin index to the loot table's name if they have one.

    -- Taxi
    CarTaxi = { Normal = VehicleDistributions.Taxi; },
    CarTaxi2 = { Normal = VehicleDistributions.Taxi; },

    -- Police
    PickUpVanLightsPolice = { Normal = VehicleDistributions.Police; },
    CarLightsPolice = { Normal = VehicleDistributions.Police; },

    -- Fire dept
    PickUpTruckLightsFire = { Normal = VehicleDistributions.Fire; },
    PickUpVanLightsFire = { Normal = VehicleDistributions.Fire; },

    -- Ranger
    PickUpVanLights0 = { Normal = VehicleDistributions.Ranger; },
    PickUpTruckLights0 = { Normal = VehicleDistributions.Ranger; },
    CarLights0 = { Normal = VehicleDistributions.Ranger; },

    -- McCoy
    PickUpVanMccoy = { Normal = VehicleDistributions.McCoy; },
    PickUpTruckMccoy = { Normal = VehicleDistributions.McCoy; },
    VanSpecial1 = { Normal = VehicleDistributions.McCoy; },

    -- Fossoil
    PickUpVanLights1 = { Normal = VehicleDistributions.Fossoil; },
    PickUpTruckLights1 = { Normal = VehicleDistributions.Fossoil; },
    VanSpecial0 = { Normal = VehicleDistributions.Fossoil; },

    -- Postal
    StepVanMail = { Normal = VehicleDistributions.Postal; },
    VanSpecial2 = { Normal = VehicleDistributions.Postal; },

    -- Distillery
    StepVan_Scarlet = { Normal = VehicleDistributions.Distillery; },
    Van_KnoxDisti = { Normal = VehicleDistributions.Distillery; },

    -- MassGenFac
    Van_MassGenFac = { Normal = VehicleDistributions.MassGenFac; },

    Van_Transit = { Normal = VehicleDistributions.Transit; },

    -- Ambulance
    VanAmbulance = { Normal = VehicleDistributions.Doctor; },

    -- Radio
    VanRadio = { Normal = VehicleDistributions.Radio; },
    VanRadio_3N = { Normal = VehicleDistributions.Radio; },

    -- Spiffo
    VanSpiffo = { Normal = VehicleDistributions.Spiffo; },

    -- KY Heralds
    StepVan_Heralds = { Normal = VehicleDistributions.Heralds; },

    -- LectroMax
    Van_LectroMax = { Normal = VehicleDistributions.MassGenFac; },
}

-- define smashed car like their normal counterpart
distributionTable.CarNormalSmashedRear = distributionTable.CarNormal;
distributionTable.CarNormalSmashedFront = distributionTable.CarNormal;
distributionTable.CarNormalSmashedLeft = distributionTable.CarNormal;
distributionTable.CarNormalSmashedRight = distributionTable.CarNormal;

distributionTable.CarLightsSmashedRear = distributionTable.CarLights;
distributionTable.CarLightsSmashedFront = distributionTable.CarLights;
distributionTable.CarLightsSmashedLeft = distributionTable.CarLights;
distributionTable.CarLightsSmashedRight = distributionTable.CarLights;

distributionTable.CarStationWagonSmashedRear = distributionTable.CarStationWagon;
distributionTable.CarStationWagonSmashedFront = distributionTable.CarStationWagon;
distributionTable.CarStationWagonSmashedLeft = distributionTable.CarStationWagon;
distributionTable.CarStationWagonSmashedRight = distributionTable.CarStationWagon;

distributionTable.CarStationWagon2SmashedRear = distributionTable.CarStationWagon2;
distributionTable.CarStationWagon2SmashedFront = distributionTable.CarStationWagon2;
distributionTable.CarStationWagon2SmashedLeft = distributionTable.CarStationWagon2;
distributionTable.CarStationWagon2SmashedRight = distributionTable.CarStationWagon2;

distributionTable.ModernCarSmashedRear = distributionTable.ModernCar;
distributionTable.ModernCarSmashedFront = distributionTable.ModernCar;
distributionTable.ModernCarSmashedLeft = distributionTable.ModernCar;
distributionTable.ModernCarSmashedRight = distributionTable.ModernCar;

distributionTable.ModernCar02SmashedRear = distributionTable.ModernCar02;
distributionTable.ModernCar02SmashedFront = distributionTable.ModernCar02;
distributionTable.ModernCar02SmashedLeft = distributionTable.ModernCar02;
distributionTable.ModernCar02SmashedRight = distributionTable.ModernCar02;

distributionTable.PickUpTruckSmashedRear = distributionTable.PickUpTruck;
distributionTable.PickUpTruckSmashedFront = distributionTable.PickUpTruck;
distributionTable.PickUpTruckSmashedLeft = distributionTable.PickUpTruck;
distributionTable.PickUpTruckSmashedRight = distributionTable.PickUpTruck;

distributionTable.CarLuxurySmashedRear = distributionTable.CarLuxury;
distributionTable.CarLuxurySmashedFront = distributionTable.CarLuxury;
distributionTable.CarLuxurySmashedLeft = distributionTable.CarLuxury;
distributionTable.CarLuxurySmashedRight = distributionTable.CarLuxury;

distributionTable.OffRoadSmashedRear = distributionTable.OffRoad;
distributionTable.OffRoadSmashedFront = distributionTable.OffRoad;
distributionTable.OffRoadSmashedLeft = distributionTable.OffRoad;
distributionTable.OffRoadSmashedRight = distributionTable.OffRoad;

distributionTable.PickUpTruckLightsSmashedRear = distributionTable.PickUpTruckLights;
distributionTable.PickUpTruckLightsSmashedFront = distributionTable.PickUpTruckLights;
distributionTable.PickUpTruckLightsSmashedLeft = distributionTable.PickUpTruckLights;
distributionTable.PickUpTruckLightsSmashedRight = distributionTable.PickUpTruckLights;

distributionTable.PickUpVanSmashedRear = distributionTable.PickUpVan;
distributionTable.PickUpVanSmashedFront = distributionTable.PickUpVan;
distributionTable.PickUpVanSmashedLeft = distributionTable.PickUpVan;
distributionTable.PickUpVanSmashedRight = distributionTable.PickUpVan;

distributionTable.PickUpVanLightsSmashedRear = distributionTable.PickUpVanLights;
distributionTable.PickUpVanLightsSmashedFront = distributionTable.PickUpVanLights;
distributionTable.PickUpVanLightsSmashedLeft = distributionTable.PickUpVanLights;
distributionTable.PickUpVanLightsSmashedRight = distributionTable.PickUpVanLights;

distributionTable.CarSmallSmashedRear = distributionTable.SmallCar;
distributionTable.CarSmallSmashedFront = distributionTable.SmallCar;
distributionTable.CarSmallSmashedLeft = distributionTable.SmallCar;
distributionTable.CarSmallSmashedRight = distributionTable.SmallCar;

distributionTable.CarSmall02SmashedRear = distributionTable.SmallCar02;
distributionTable.CarSmall02SmashedFront = distributionTable.SmallCar02;
distributionTable.CarSmall02SmashedLeft = distributionTable.SmallCar02;
distributionTable.CarSmall02SmashedRight = distributionTable.SmallCar02;

distributionTable.StepVanSmashedRear = distributionTable.StepVan;
distributionTable.StepVanSmashedFront = distributionTable.StepVan;
distributionTable.StepVanSmashedLeft = distributionTable.StepVan;
distributionTable.StepVanSmashedRight = distributionTable.StepVan;

distributionTable.StepVanMailSmashedRear = distributionTable.StepVanMail;
distributionTable.StepVanMailSmashedFront = distributionTable.StepVanMail;
distributionTable.StepVanMailSmashedLeft = distributionTable.StepVanMail;
distributionTable.StepVanMailSmashedRight = distributionTable.StepVanMail;

distributionTable.SUVSmashedRear = distributionTable.SUV;
distributionTable.SUVSmashedFront = distributionTable.SUV;
distributionTable.SUVSmashedLeft = distributionTable.SUV;
distributionTable.SUVSmashedRight = distributionTable.SUV;

table.insert(VehicleDistributions, 1, distributionTable);
