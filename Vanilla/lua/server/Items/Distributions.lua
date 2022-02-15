Distributions = Distributions or {};

local distributionTable = {

    -- =====================
    --    Room List (A-Z)
    -- =====================

    aesthetic = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="SalonCounter", min=0, max=99},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="SalonShelfHaircare", min=0, max=99, weightChance=100},
                {name="SalonShelfTowels", min=0, max=99, weightChance=10},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="SalonShelfTowels", min=0, max=99, weightChance=50},
                {name="SalonShelfHaircare", min=0, max=99, weightChance=100},
            }
        },
    },

    aestheticstorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="SalonShelfHaircare", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="SalonShelfHaircare", min=0, max=99},
            }
        },
    },

    all = {
        bin = {
            rolls = 4,
            items = {
                "BandageDirty", 0.5,
                "BeerEmpty", 0.25,
                "Cockroach", 6,
                "DeadMouse", 0.5,
                "DeadRat", 1,
                "ElectronicsScrap", 0.5,
                "PopBottleEmpty", 0.5,
                "PopEmpty", 4,
                "ScrapMetal", 0.5,
                "SmashedBottle", 0.25,
                "TinCanEmpty", 4,
                "WaterBottleEmpty", 0.5,
                "WhiskeyEmpty", 0.25,
                "WineEmpty", 0.25,
                "WineEmpty2", 0.25,
            },
            junk = {
                rolls = 1,
                items = {
                    "Garbagebag", 100,
                }
            }
        },
        campfire = {
            rolls = 1,
            items = {

            }
        },
        cashregister = {
            rolls = 4,
            items = {
                "Money", 2,
                "Money", 100,
                "Money", 100,
            }
        },
        clothingdryer = {
            rolls = 0,
            items = {

            }
        },
        clothingdryerbasic = {
            rolls = 0,
            items = {

            }
        },
        clothingrack = {
            procedural = true,
            procList = {
                {name="ClothingStoresDress", min=0, max=99, weightChance=100},
                {name="ClothingStoresShirts", min=0, max=99, weightChance=100},
                {name="ClothingStoresShirtsFormal", min=0, max=99, weightChance=100},
                {name="ClothingStoresJumpers", min=0, max=99, weightChance=100},
                {name="ClothingStoresJackets", min=0, max=99, weightChance=100},
                {name="ClothingStoresJacketsFormal", min=0, max=99, weightChance=100},
                {name="ClothingStoresJumpers", min=0, max=99, weightChance=100},
                {name="ClothingStoresPantsFormal", min=0, max=99, weightChance=100},
            }
        },
        clothingwasher = {
            rolls = 0,
            items = {

            }
        },
        corn = {
            rolls = 2,
            items = {
                "Corn", 1,
                "Corn", 1,
                "Corn", 1,
            }
        },
        counter = {
            rolls = 4,
            items = {
                "Battery", 2,
                "BluePen", 8,
                "Book", 10,
                "Eraser", 6,
                "Magazine", 10,
                "Notebook", 10,
                "Paperclip", 20,
                "PaperclipBox", 0.1,
                "Pen", 8,
                "Pencil", 20,
                "RedPen", 8,
                "RubberBand", 6,
                "SheetPaper2", 20,
            },
            junk = {
                rolls = 1,
                items = {
                    "Glue", 2,
                    "HandTorch", 4,
                    "Radio.RadioBlack", 2,
                    "Radio.RadioRed", 1,
                    "Radio.WalkieTalkie2", 1,
                    "Radio.WalkieTalkie3", 0.5,
                    "Scissors", 2,
                    "Scotchtape", 2,
                    "Torch", 2,
                }
            }
        },
        crate = {
            rolls = 1,
            items = {
                "Axe", 0.005,
                "BallPeenHammer", 0.6,
                "BarbedWire", 0.2,
                "Battery", 1,
                "BlowTorch", 0.8,
                "BucketEmpty", 0.4,
                "camping.TentPeg", 2,
                "Charcoal", 0.05,
                "ClubHammer", 0.4,
                "ConcretePowder", 2,
                "Crowbar", 0.4,
                "DuctTape", 0.8,
                "EmptySandbag", 0.2,
                "farming.BroccoliBagSeed", 0.8,
                "farming.CabbageBagSeed", 0.8,
                "farming.CarrotBagSeed", 0.8,
                "farming.GardeningSprayEmpty", 0.6,
                "farming.HandShovel", 1,
                "farming.PotatoBagSeed", 0.8,
                "farming.RedRadishBagSeed", 0.8,
                "farming.StrewberrieBagSeed", 0.8,
                "farming.TomatoBagSeed", 0.8,
                "farming.WateredCan", 0.6,
                "Fertilizer", 2,
                "GardenFork", 0.1,
                "GardenHoe", 0.2,
                "GardenSaw", 0.6,
                "Glasses_SafetyGoggles", 0.2,
                "Gravelbag", 2,
                "Hammer", 0.6,
                "HandAxe", 0.1,
                "HandFork", 0.2,
                "HandScythe", 0.2,
                "HandTorch", 0.4,
                "Hat_DustMask", 0.2,
                "Hat_HardHat", 0.1,
                "Jack", 0.2,
                "LeadPipe", 0.4,
                "LeafRake", 1,
                "Lighter", 4,
                "LugWrench", 0.9,
                "MetalBar", 1.2,
                "MetalPipe", 2,
                "NailsBox", 1,
                "NailsBox", 1,
                "NailsBox", 1,
                "NailsBox", 1,
                "PaintBlack", 0.8,
                "PaintBlue", 0.8,
                "PaintBrown", 0.8,
                "Paintbrush", 0.4,
                "PaintCyan", 0.8,
                "PaintGreen", 0.8,
                "PaintGrey", 0.8,
                "PaintLightBlue", 0.8,
                "PaintLightBrown", 0.8,
                "PaintOrange", 0.8,
                "PaintPink", 0.8,
                "PaintPurple", 0.8,
                "PaintRed", 0.8,
                "PaintTurquoise",0.8,
                "PaintWhite", 0.8,
                "PaintYellow", 0.8,
                "Paperclip", 0.8,
                "PaperclipBox", 0.5,
                "PickAxe", 0.05,
                "PipeWrench", 0.4,
                "Plank", 1,
                "Plank", 1,
                "PlasterPowder", 2,
                "PropaneTank", 0.05,
                "Radio.ElectricWire", 0.5,
                "Radio.HamRadio1", 0.005,
                "Radio.RadioBlack", 2,
                "Radio.RadioRed", 1,
                "Radio.WalkieTalkie1", 0.05,
                "Radio.WalkieTalkie2", 0.03,
                "Radio.WalkieTalkie3", 0.01,
                "Rake", 1,
                "Rope", 0.8,
                "Sandbag", 0.5,
                "Saw", 0.4,
                "Screwdriver", 1,
                "ScrewsBox", 0.4,
                "SheetMetal", 2,
                "Shovel", 0.4,
                "Shovel2", 0.4,
                "Sledgehammer", 0.001,
                "Sledgehammer2", 0.001,
                "SmallSheetMetal", 1,
                "Tarp", 1,
                "TirePump", 0.6,
                "Torch", 0.2,
                "Twine", 1,
                "WeldingMask", 0.2,
                "WeldingRods", 1,
                "Wire", 0.8,
                "WoodAxe", 0.0025,
                "WoodenMallet", 0.4,
                "Woodglue", 0.4,
                "Wrench", 0.6,
            }
        },
        desk = {
            procedural = true,
            procList = {
                {name="DeskGeneric", min=0, max=99},
            }
        },
        dishescabinet = {
            procedural = true,
            procList = {
                {name="DishCabinetGeneric", min=0, max=99, weightChance=100},
                {name="DishCabinetLiquor", min=0, max=99, weightChance=40},
            }
        },
        displaycasebakery = {
            procedural = true,
            procList = {
                {name="BakeryBread", min=0, max=99, weightChance=100},
                {name="BakeryPie", min=0, max=99, weightChance=60},
                {name="BakeryCake", min=0, max=99, weightChance=80},
                {name="BakeryMisc", min=0, max=99, weightChance=20},
            }
        },
        dresser = {
            procedural = true,
            procList = {
                {name="DresserGeneric", min=0, max=99},
            }
        },
        filingcabinet = {
            procedural = true,
            procList = {
                {name="FilingCabinetGeneric", min=0, max=99},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FreezerIceCream", min=0, max=9999},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeGeneric", min=0, max=99, weightChance=100},
                {name="FridgeRich", min=0, max=99, forceForZones="Rich"},
                {name="FridgeTrailerPark", min=0, max=99, forceForZones="TrailerPark"},
            }
        },
        fruitbusha = {
            rolls = 10,
            items = {
                "BerryBlack", 50,
            },
            noAutoAge = true,
        },
        fruitbushb = {
            rolls = 10,
            items = {
                "BerryBlue", 50,
            },
            noAutoAge = true,
        },
        fruitbushc = {
            rolls = 10,
            items = {
                "BerryGeneric1", 50,
            },
            noAutoAge = true,
        },
        fruitbushd = {
            rolls = 10,
            items = {
                "BerryGeneric2", 25,
                "BerryGeneric5", 25,
            },
            noAutoAge = true,
        },
        fruitbushe = {
            rolls = 10,
            items = {
                "BerryGeneric3", 25,
                "BerryGeneric4", 25,
            },
            noAutoAge = true,
        },
        grocerstand = {
            rolls = 4,
            items = {
                "Apple", 8,
                "Apple", 8,
                "Banana", 8,
                "Banana", 8,
                "Cherry", 8,
                "farming.Strewberrie", 8,
                "Grapes", 8,
                "Orange", 8,
                "Orange", 8,
                "Peach", 8,
                "Pear", 8,
            }
        },
        inventoryfemale = {
            rolls = 1,
            items = {
                "BluePen", 1,
                "Cigarettes", 0.5,
                "Comb", 1,
                "CreditCard", 1,
                "Doll", 0.5,
                "Earbuds", 1,
                "Lighter", 0.5,
                "Lipstick", 1,
                "Locket", 1,
                "Magazine", 1,
                "MakeupEyeshadow", 1,
                "MakeupFoundation", 1,
                "MarchRidgeMap", 0.1,
                "Matches", 0.5,
                "MuldraughMap", 0.1,
                "Newspaper", 1,
                "Notebook", 1,
                "Pen", 1,
                "Pencil", 1,
                "Perfume", 0.5,
                "Pills", 0.1,
                "PillsAntiDep", 0.1,
                "PillsBeta", 0.1,
                "PillsVitamins", 0.1,
                "Radio.WalkieTalkie1", 0.05,
                "Radio.WalkieTalkie2", 0.03,
                "Radio.WalkieTalkie3", 0.001,
                "RedPen", 1,
                "RiversideMap", 0.1,
                "RosewoodMap", 0.1,
                "WestpointMap", 0.1,
            }
        },
        inventorymale = {
            rolls = 1,
            items = {
                "BluePen", 1,
                "Cigarettes", 0.5,
                "Cologne", 0.5,
                "Comb", 1,
                "CreditCard", 1,
                "Earbuds", 1,
                "Lighter", 0.5,
                "Locket", 1,
                "Magazine", 1,
                "MarchRidgeMap", 0.1,
                "Matches", 0.5,
                "MuldraughMap", 0.1,
                "Newspaper", 1,
                "Notebook", 1,
                "Pen", 1,
                "Pencil", 1,
                "Pills", 0.1,
                "PillsAntiDep", 0.1,
                "PillsBeta", 0.1,
                "PillsVitamins", 0.1,
                "Radio.WalkieTalkie1", 0.05,
                "Radio.WalkieTalkie2", 0.03,
                "Radio.WalkieTalkie3", 0.001,
                "RedPen", 1,
                "RiversideMap", 0.1,
                "RosewoodMap", 0.1,
                "Wallet", 1,
                "Wallet2", 1,
                "Wallet3", 1,
                "Wallet4", 1,
                "WestpointMap", 0.1,
            }
        },
        locker = {
            procedural = true,
            procList = {
                {name="Locker", min=0, max=99, weightChance=100},
                {name="LockerArmyBedroom", min=0, max=99, forceForZones="Army"},
                {name="LockerArmyBedroom", min=0, max=99, forceForItems="furniture_bedding_01_56;furniture_bedding_01_57;furniture_bedding_01_58;furniture_bedding_01_59"},
                {name="LockerClassy", min=0, max=99, forceForZones="Rich"},
            }
        },
        logs = {
            rolls = 1,
            items = {
                "Log", 100,
                "Log", 100,
                "Log", 10,
                "Log", 10,
                "Log", 10,
                "Log", 10,
            }
        },
        medicine = {
            rolls = 4,
            items = {
                "Antibiotics", 0.25,
                "Bandage", 6,
                "Bandaid", 10,
                "Bandaid", 10,
                "Pills", 4,
                "PillsAntiDep", 1,
                "PillsBeta", 1,
                "PillsSleepingTablets", 1,
                "PillsVitamins", 4,
            },
            junk = {
                rolls = 1,
                items = {
                    "Comb", 4,
                    "Disinfectant", 2,
                    "Toothbrush", 10,
                    "Toothpaste", 10,
                    "Tweezers", 10,
                }
            }
        },
        metal_shelves = {
            rolls = 4,
            items = {
                "BallPeenHammer", 0.6,
                "Battery", 1,
                "BucketEmpty", 0.4,
                "ClubHammer", 0.4,
                "Crowbar", 0.4,
                "DuctTape", 0.8,
                "Glasses_SafetyGoggles", 0.2,
                "Hammer", 0.6,
                "HandAxe", 0.1,
                "HandTorch", 0.4,
                "Hat_DustMask", 0.2,
                "Hat_HardHat", 0.1,
                "LeadPipe", 0.4,
                "LeadPipe", 0.4,
                "MetalBar", 1.2,
                "MetalPipe", 2,
                "Nails", 4,
                "NailsBox", 1,
                "PipeWrench", 0.4,
                "Radio.ElectricWire", 0.5,
                "Radio.RadioBlack", 2,
                "Radio.RadioRed", 1,
                "Radio.WalkieTalkie1", 0.05,
                "Radio.WalkieTalkie2", 0.03,
                "Radio.WalkieTalkie3", 0.01,
                "Rope", 0.8,
                "Saw", 0.4,
                "Screwdriver", 1,
                "ScrewsBox", 0.4,
                "SheetMetal", 2,
                "Shovel", 0.4,
                "Shovel2", 0.4,
                "SmallSheetMetal", 1,
                "Tarp", 1,
                "Toolbox", 0.5,
                "Torch", 0.2,
                "Twine", 1,
                "WeldingMask", 0.2,
                "WeldingRods", 1,
                "Wire", 0.8,
                "WoodenMallet", 0.4,
                "Woodglue", 0.4,
                "Wrench", 0.6,
            }
        },
        microwave = {
            rolls = 1,
            items = {

            }
        },
        militarycrate = {
            procedural = true,
            procList = {
                {name="ArmyStorageAmmunition", min=0, max=9999, weightChance=100},
                {name="ArmyStorageElectronics", min=0, max=9999, weightChance=100},
                {name="ArmyStorageGuns", min=0, max=9999, weightChance=100},
                {name="ArmyStorageMedical", min=0, max=9999, weightChance=100},
                {name="ArmyStorageOutfit", min=0, max=9999, weightChance=100},
            }
        },
        officedrawers = {
            procedural = true,
            procList = {
                {name="OfficeDrawers", min=0, max=99},
            }
        },
        other = {
            procedural = true,
            procList = {
                {name="OtherGeneric", min=0, max=99},
            }
        },
        plankstash = {
            procedural = true,
            procList = {
                {name="PlankStashMoney", min=0, max=99, weightChance=100},
                {name="PlankStashWeapon", min=0, max=99, weightChance=100},
            }
        },
        postbox = {
            rolls = 1,
            items = {
                "FarmingMag1", 0.5,
                "FishingMag1", 0.5,
                "FishingMag2", 0.5,
                "HuntingMag1", 0.5,
                "HuntingMag2", 0.5,
                "HuntingMag3", 0.5,
                "HerbalistMag", 0.5,
                "CookingMag1", 0.5,
                "CookingMag2", 0.5,
                "ElectronicsMag1", 0.5,
                "ElectronicsMag2", 0.5,
                "ElectronicsMag3", 0.5,
                "ElectronicsMag4", 0.5,
                "ElectronicsMag5", 0.5,
                "MechanicMag1", 0.5,
                "MechanicMag2", 0.5,
                "MechanicMag3", 0.5,
                "EngineerMagazine1", 0.5,
                "EngineerMagazine2", 0.5,
                "MetalworkMag1", 0.5,
                "MetalworkMag2", 0.5,
                "MetalworkMag3", 0.5,
                "MetalworkMag4", 0.5,
                "Newspaper", 10,
            }
        },
        restaurantdisplay = {
            rolls = 1,
            items = {

            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ShelfGeneric", min=0, max=99},
            }
        },
        shelvesmag = {
            procedural = true,
            procList = {
                {name="MagazineRackMaps", min=0, max=1, weightChance=50},
                {name="MagazineRackNewspaper", min=0, max=1, weightChance=50},
                {name="MagazineRackMixed", min=0, max=99, weightChance=100},
            }
        },
        sidetable = {
            rolls = 1,
            items = {
                "Battery", 4,
                "BluePen", 8,
                "Cigarettes", 4,
                "ComicBook", 2,
                "CookingMag1", 0.5,
                "CookingMag2", 0.5,
                "ElectronicsMag1", 0.5,
                "ElectronicsMag2", 0.5,
                "ElectronicsMag3", 0.5,
                "ElectronicsMag4", 0.5,
                "ElectronicsMag5", 0.5,
                "EngineerMagazine1", 0.5,
                "EngineerMagazine2", 0.5,
                "Eraser", 6,
                "FishingMag1", 0.5,
                "FishingMag2", 0.5,
                "HandTorch", 1,
                "HerbalistMag", 0.5,
                "HuntingMag1", 0.5,
                "HuntingMag2", 0.5,
                "HuntingMag3", 0.5,
                "Journal", 1,
                "Lighter", 4,
                "Magazine", 4,
                "MarchRidgeMap", 0.01,
                "Matches", 4,
                "MechanicMag1", 0.5,
                "MechanicMag2", 0.5,
                "MechanicMag3", 0.5,
                "MetalworkMag1", 0.5,
                "MetalworkMag2", 0.5,
                "MetalworkMag3", 0.5,
                "MetalworkMag4", 0.5,
                "MuldraughMap", 0.01,
                "Newspaper", 4,
                "Notebook", 2,
                "Paperclip", 4,
                "Pen", 8,
                "Pencil", 10,
                "RedPen", 8,
                "RubberBand", 1,
                "SheetPaper2", 10,
            },
            junk = {
                rolls = 1,
                items = {
                    "Candle", 4,
                    "CordlessPhone", 1,
                    "Disc_Retail", 2,
                    "Earbuds", 1,
                    "Glasses_Reading", 1,
                    "Headphones", 1,
                    "Mirror", 1,
                    "Radio.RadioBlack", 2,
                    "Radio.RadioRed", 1,
                    "Radio.WalkieTalkie1", 0.05,
                    "Radio.WalkieTalkie2", 0.03,
                    "Radio.WalkieTalkie3", 0.01,
                    "Remote", 8,
                    "RiversideMap", 0.01,
                    "RosewoodMap", 0.01,
                    "SewingKit", 0.5,
                    "Tissue", 1,
                    "Torch", 1,
                    "WestpointMap", 0.01,
                }
            }
        },
        stove = {
            rolls = 0,
            items = {

            }
        },
        vendingpop = {
            rolls = 4,
            items = {
                "Pop", 10,
                "Pop2", 10,
                "Pop3", 10,
                "PopBottle", 10,
            }
        },
        vendingsnack = {
            rolls = 4,
            items = {
                "Crisps", 10,
                "Crisps2", 10,
                "Crisps3", 10,
            }
        },
        wardrobe = {
            procedural = true,
            procList = {
                {name="WardrobeMan", min=0, max=99, weightChance=100},
                {name="WardrobeManClassy", min=0, max=99, weightChance=100, forceForZones="Rich"},
                {name="WardrobeRedneck", min=0, max=99, weightChance=100, forceForZones="Poor"},
                {name="WardrobeWoman", min=0, max=99, weightChance=100},
                {name="WardrobeWomanClassy", min=0, max=99, weightChance=100, forceForZones="Rich"},
            }
        },

        -- This is an example to add a specific distribution on a dead corpse according to his outfit
        Outfit_Generic99 = {
            rolls = 1,
            items = {
                "Wallet", 100,
            }
        },
    },

    armyhanger = {
        counter = {
            procedural = true,
            procList = {
                {name="ArmyHangarTools", min=0, max=99},
            }
        },
        locker = {
            procedural = true,
            procList = {
                {name="ArmyHangarOutfit", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ArmyHangarTools", min=0, max=99},
            }
        },
    },

    armystorage = {
        locker = {
            procedural = true,
            procList = {
                {name="ArmyStorageGuns", min=0, max=99, forceForTiles="furniture_storage_02_8;furniture_storage_02_9;furniture_storage_02_10;furniture_storage_02_11"},
                {name="ArmyStorageOutfit", min=0, max=99, forceForTiles="furniture_storage_02_4;furniture_storage_02_5;furniture_storage_02_6;furniture_storage_02_7"},
            },
            dontSpawnAmmo = true,
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ArmyStorageGuns", min=0, max=99, weightChance=100},
                {name="ArmyStorageElectronics", min=0, max=1, weightChance=20},
            },
            dontSpawnAmmo = true,
        },
    },

    armysurplus = {
        isShop = true,
        clothingrack = {
            procedural = true,
            procList = {
                {name="ArmySurplusOutfit", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="ArmySurplusHeadwear", min=0, max=4, weightChance=100},
                {name="ArmySurplusMisc", min=0, max=99, weightChance=20},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="GunStoreDisplayCase", min=0, max=99},
            }
        },
        locker = {
            rolls = 1,
            items = {

            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ArmySurplusBackpacks", min=0, max=99, weightChance=100},
                {name="ArmySurplusFootwear", min=0, max=99, weightChance=100},
                {name="ArmySurplusOutfit", min=0, max=99, weightChance=100},
                {name="ArmySurplusMisc", min=0, max=99, weightChance=100},
                {name="ArmySurplusTools", min=0, max=99, weightChance=100},
                {name="CampingStoreGear", min=0, max=99, weightChance=100},
                {name="ClothingStorageWinter", min=0, max=99, weightChance=100},
            }
        },
        militarycrate = {
            rolls = 1,
            items = {

            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ArmySurplusBackpacks", min=0, max=99, weightChance=100},
                {name="ArmySurplusFootwear", min=0, max=99, weightChance=100},
                {name="ArmySurplusOutfit", min=0, max=99, weightChance=100},
                {name="ArmySurplusMisc", min=0, max=99, weightChance=100},
                {name="ArmySurplusTools", min=0, max=99, weightChance=100},
                {name="CampingStoreGear", min=0, max=99, weightChance=100},
                {name="ClothingStorageWinter", min=0, max=99, weightChance=100},
            }
        },

    },

    artstore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="LibraryCounter", min=0, max=99},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBagsFancy", min=0, max=1, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ArtStorePaper", min=0, max=99, weightChance=100},
                {name="ArtStorePen", min=0, max=99, weightChance=100},
                {name="ArtStoreOther", min=0, max=99, weightChance=50},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
    },

    bakery = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBagsFancy", min=0, max=1, weightChance=100},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="BakeryBread", min=1, max=99, weightChance=100},
                {name="BakeryCake", min=1, max=99, weightChance=80},
                {name="BakeryDoughnuts", min=1, max=99, weightChance=80},
                {name="BakeryMisc", min=1, max=99, weightChance=20},
                {name="BakeryPie", min=1, max=99, weightChance=60},
            }
        },
        displaycasebakery = {
            procedural = true,
            procList = {
                {name="BakeryBread", min=1, max=99, weightChance=100},
                {name="BakeryCake", min=1, max=99, weightChance=80},
                {name="BakeryDoughnuts", min=1, max=99, weightChance=80},
                {name="BakeryMisc", min=1, max=99, weightChance=20},
                {name="BakeryPie", min=1, max=99, weightChance=60},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="BakeryKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="BakeryKitchenFridge", min=0, max=99},
            }
        },
        grocerstand = {
            procedural = true,
            procList = {
                {name="BakeryBread", min=1, max=99, weightChance=100},
                {name="BakeryCake", min=1, max=99, weightChance=80},
                {name="BakeryDoughnuts", min=1, max=99, weightChance=80},
                {name="BakeryMisc", min=1, max=99, weightChance=20},
                {name="BakeryPie", min=1, max=99, weightChance=60},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="BakeryBread", min=1, max=99, weightChance=100},
                {name="BakeryCake", min=1, max=99, weightChance=80},
                {name="BakeryDoughnuts", min=1, max=99, weightChance=80},
                {name="BakeryMisc", min=1, max=99, weightChance=20},
                {name="BakeryPie", min=1, max=99, weightChance=60},
            }
        },
    },

    bakerykitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="BakeryKitchenBaking", min=1, max=2, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenCafe", min=0, max=2, weightChance=100},
                {name="StoreKitchenCutlery", min=0, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateBakingSoda", min=0, max=1, weightChance=100},
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateChocolateChips", min=0, max=1, weightChance=100},
                {name="CrateCocoaPowder", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateSugarBrown", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="BakeryKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="BakeryKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateBakingSoda", min=0, max=1, weightChance=100},
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateChocolateChips", min=0, max=1, weightChance=100},
                {name="CrateCocoaPowder", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateSugarBrown", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="BakeryBread", min=1, max=99, weightChance=100},
                {name="BakeryCake", min=1, max=99, weightChance=80},
                {name="BakeryDoughnuts", min=1, max=99, weightChance=80},
                {name="BakeryMisc", min=1, max=99, weightChance=20},
                {name="BakeryPie", min=1, max=99, weightChance=60},
            }
        },
    },

    bandkitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99, weightChance=100},
                {name="StoreShelfSnacks", min=0, max=99, weightChance=100},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="BandPracticeFridge", min=0, max=99, weightChance=100},
            }
        },
        overhead = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99, weightChance=100},
                {name="StoreShelfSnacks", min=0, max=99, weightChance=100},
            }
        },
    },

    bandlivingroom = {
        isShop = true,
        locker = {
            procedural = true,
            procList = {
                {name="BandPracticeClothing", min=0, max=99, weightChance=100},
            }
        },
    },

    bandmerch = {
        isShop = true,
        clothingrack = {
            procedural = true,
            procList = {
                {name="BandMerchClothes", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="BandMerchClothes", min=0, max=99, weightChance=100},
                {name="BandMerchShelves", min=0, max=99, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="BandMerchShelves", min=0, max=99, weightChance=100},
            }
        },
    },

    bar = {
        isShop = true,
        bin = {
            procedural = true,
            procList = {
                {name="BinBar", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="BarCounterGlasses", min=1, max=99, weightChance=100},
                {name="BarCounterMisc", min=1, max=99, weightChance=20},
                {name="BarCounterWeapon", min=1, max=1, weightChance=100},
                {name="BarCounterLiquor", min=0, max=2, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99, weightChance=100},
                {name="CrateCigarettes", min=0, max=99, weightChance=40},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeBeer", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99},
                {name="CrateCigarettes", min=0, max=99, weightChance=40},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99},
            }
        },
    },

    barkitchen = {
        isShop = true,
        bin = {
            procedural = true,
            procList = {
                {name="BinBar", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="BarCounterMisc", min=0, max=99, weightChance=50},
                {name="BarCounterWeapon", min=0, max=1, weightChance=20},
                {name="BarCounterLiquor", min=0, max=99, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99, weightChance=100},
                {name="CrateCigarettes", min=0, max=99, weightChance=40},
            }
        },
        freezer = {
            rolls = 1,
            items = {

            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeBeer", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99, weightChance=100},
                {name="CrateCigarettes", min=0, max=99, weightChance=40},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99},
            }
        },
    },

    barstorage = {
        isShop = true,
        bin = {
            procedural = true,
            procList = {
                {name="BinBar", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="BarCounterMisc", min=0, max=99, weightChance=50},
                {name="BarCounterWeapon", min=0, max=1, weightChance=20},
                {name="BarCounterLiquor", min=0, max=99, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="BarCratePool", min=0, max=99, weightChance=100},
                {name="BarCrateDarts", min=0, max=99, weightChance=100},
                {name="BarShelfLiquor", min=0, max=99, weightChance=20},
            }
        },
        freezer = {
            rolls = 1,
            items = {

            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeBeer", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99},
            }
        },
    },

    batfactory = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="BatFactoryBats", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="BatFactoryBats", min=0, max=99, weightChance=100},
            }
        },
    },

    bathroom = {
        counter = {
            procedural = true,
            procList = {
                {name="BathroomCounter", min=0, max=99},
                {name="BathroomCounterEmpty", min=0, max=99, forceForRooms="armysurplus;bank;bar;breakroom;church;classroom;daycare;empty;factory;fitness;firestorage;grocery;gym;meetingroom;metalshop;motelroom;policestorage;spiffoskitchen;restaurant;restaurantkitchen"},
                {name="BathroomCounterNoMeds", min=0, max=99, forceForItems="fixtures_bathroom_01_28;fixtures_bathroom_01_29;fixtures_bathroom_01_37;fixtures_bathroom_01_38"},
            }
        },
        locker = {
            procedural = true,
            procList = {
                {name="FactoryLockers", min=0, max=99, forceForRooms="batteryfactory;brewery;cabinetfactory;dogfoodfactory;factory;fryshipping;metalshop;radiofactory;warehouse;wirefactory;whiskeybottling"},
                {name="FireDeptLockers", min=0, max=99, forceForRooms="firestorage"},
                {name="GymLockers", min=0, max=99, forceForRooms="fitness;gym"},
                {name="HospitalLockers", min=0, max=99, forceForRooms="hospitalroom"},
                {name="PoliceLockers", min=0, max=99, forceForRooms="policestorage"},
                {name="PrisonGuardLockers", min=0, max=99, forceForRooms="cells"},
                {name="SchoolLockers", min=0, max=99, forceForRooms="classroom"},
            }
        },
        medicine = {
            procedural = true,
            procList = {
                {name="BathroomCabinet", min=0, max=99},
            }
        }
    },

    batstorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="BatFactoryBats", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="BatFactoryBats", min=0, max=99, weightChance=100},
            }
        },
    },

    batteryfactory = {
        isShop = true,
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateBatteries", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateBatteries", min=0, max=99, weightChance=100},
            }
        },
    },

    batterystorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CrateBatteries", min=0, max=99, weightChance=100},
            }
        },
    },

    bedroom = {
        crate = {
            procedural = true,
            procList = {
                {name="ClothingStorageWinter", min=0, max=1, weightChance=100},
                {name="CrateBooks", min=0, max=1, weightChance=100},
                {name="CrateCanning", min=0, max=1, weightChance=100},
                {name="CrateClothesRandom", min=0, max=1, weightChance=100},
                {name="CrateComics", min=0, max=1, weightChance=100},
                {name="CrateCompactDisks", min=0, max=1, weightChance=100},
                {name="CrateElectronics", min=0, max=1, weightChance=100},
                {name="CrateFootwearRandom", min=0, max=1, weightChance=100},
                {name="CrateInstruments", min=0, max=1, weightChance=100},
                {name="CrateLinens", min=0, max=1, weightChance=100},
                {name="CrateMagazines", min=0, max=1, weightChance=100},
                {name="CrateNewspapers", min=0, max=1, weightChance=100},
                {name="CrateRandomJunk", min=0, max=99, weightChance=20},
                {name="CrateTailoring", min=0, max=1, weightChance=100},
                {name="CrateVHSTapes", min=0, max=1, weightChance=100},
            }
        },
        desk = {
            procedural = true,
            procList = {
                {name="OfficeDeskHome", min=0, max=99}
            }
        },
        dresser = {
            procedural = true,
            procList = {
                {name="BedroomDresser", min=0, max=99},
            }
        },
        locker = {
            procedural = true,
            procList = {
                {name="LockerArmyBedroom", min=0, max=99, forceForZones="Army"},
                {name="LockerArmyBedroom", min=0, max=99, forceForItems="furniture_bedding_01_56;furniture_bedding_01_57;furniture_bedding_01_58;furniture_bedding_01_59"},
            }
        },
        plankstash = {
            procedural = true,
            procList = {
                {name="PlankStashMagazine", min=0, max=99},
            }
        },
        sidetable = {
            procedural = true,
            procList = {
                {name="BedroomSideTable", min=0, max=99},
            }
        },
        wardrobe = {
            procedural = true,
            procList = {
                -- Search entire room for listed sprites. If found, force container to spawn.
                {name="WardrobeChild", min=0, max=2, forceForItems="furniture_bedding_01_36;furniture_bedding_01_38"},
                {name="WardrobeMan", min=0, max=2, weightChance=100},
                {name="WardrobeManClassy", min=0, max=2, forceForZones="Rich"},
                {name="WardrobeRedneck", min=0, max=2, forceForZones="TrailerPark"},
                {name="WardrobeWomanClassy", min=0, max=2, forceForZones="Rich"},
                {name="WardrobeWoman", min=0, max=2, weightChance=100},
            }
        }
    },

    beergarden = {
        isShop = true,
        bin = {
            procedural = true,
            procList = {
                {name="BinBar", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="BarCounterMisc", min=0, max=99, weightChance=50},
                {name="BarCounterWeapon", min=0, max=1, weightChance=20},
                {name="BarCounterLiquor", min=0, max=99, weightChance=100},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeBeer", min=0, max=99, weightChance=100},
            }
        },
    },

    bookstore = {
        shelves = {
            procedural = true,
            procList = {
                {name="BookstoreBooks", min=6, max=99, weightChance=100},
                {name="BookstoreMisc", min=0, max=99, weightChance=20},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="BookstoreBags", min=0, max=1, weightChance=20},
                {name="BookstoreStationery", min=0, max=12, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="BookstoreBooks", min=0, max=99},
            }
        }
    },

    bowllingalley = {
        shelves = {
            procedural = true,
            procList = {
                {name="BowlingAlleyShoes", min=0, max=99, weightChance=100},
            }
        },
    },

    breakroom = {
        counter = {
            procedural = true,
            procList = {
                {name="BreakRoomCounter", min=0, max=99},
            }
        },
        freezer = {
            rolls = 1,
            items = {
                
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeBreakRoom", min=0, max=99},
            }
        },
        overhead = {
            procedural = true,
            procList = {
                {name="BreakRoomShelves", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="BreakRoomShelves", min=0, max=99},
            }
        }
    },

    brewery = {
        isShop = true,
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="BreweryBottles", min=0, max=99, weightChance=100},
                {name="BreweryCans", min=0, max=99, weightChance=100},
                {name="BreweryEmptyBottles", min=0, max=99, weightChance=80},
            }
        },
        laundrydryerbasic = {
            procedural = true,
            procList = {

                {name="BreweryEmptyBottles", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="BreweryBottles", min=0, max=99, weightChance=100},
                {name="BreweryCans", min=0, max=99, weightChance=100},
                {name="BreweryEmptyBottles", min=0, max=99, weightChance=80},
            }
        },
    },

    brewerystorage = {
        isShop = true,
        counter = {
            rolls = 0,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="BreweryBottles", min=0, max=99, weightChance=100},
                {name="BreweryCans", min=0, max=99, weightChance=100},
                {name="BreweryEmptyBottles", min=0, max=99, weightChance=80},
            }
        },
        laundrydryerbasic = {
            procedural = true,
            procList = {
                {name="BreweryEmptyBottles", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="BreweryBottles", min=0, max=99, weightChance=100},
                {name="BreweryCans", min=0, max=99, weightChance=100},
                {name="BreweryEmptyBottles", min=0, max=99, weightChance=80},
            }
        },
    },

    burgerkitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="BurgerKitchenButcher", min=1, max=1, weightChance=100},
                {name="BurgerKitchenSauce", min=1, max=1, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenBaking", min=1, max=1, weightChance=100},
                {name="StoreKitchenCutlery", min=0, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=1, weightChance=20},
                {name="StoreKitchenPotatoes", min=1, max=1, weightChance=100},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=4, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="BurgerKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="BurgerKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=4, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayBurgers", min=1, max=4, weightChance=100},
                {name="ServingTrayFries", min=1, max=2, weightChance=60},
                {name="ServingTrayGravy", min=1, max=2, weightChance=20},
                {name="ServingTrayHotdogs", min=1, max=2, weightChance=60},
                {name="ServingTrayOnionRings", min=1, max=2, weightChance=60},
            }
        },
    },

    burgerstorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=4, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=4, weightChance=100},
            }
        },
    },

    butcher = {
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
            }
        },
        displaycasebutcher = {
            procedural = true,
            procList = {
                {name="ButcherChops", min=1, max=99, weightChance=100},
                {name="ButcherGround", min=1, max=99, weightChance=60},
                {name="ButcherChicken", min=1, max=99, weightChance=80},
                {name="ButcherSmoked", min=1, max=99, weightChance=40},
                {name="ButcherFish", min=1, max=99, weightChance=20},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="ButcherFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="ButcherFreezer", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ButcherSnacks", min=0, max=99, weightChance=100},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
    },

    cabinetfactory = {
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CabinetFactoryTools", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CabinetFactoryTools", min=0, max=99, weightChance=80},
                {name="CrateLumber", min=0, max=99, weightChance=100},
            }
        },
        overhead = {
            rolls = 1,
            items = {

            }
        },
    },

    cabinetshipping = {
        counter = {
            rolls = 1,
            items = {

            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CabinetFactoryTools", min=0, max=99},
            }
        },
        overhead = {
            rolls = 1,
            items = {

            }
        },
    },

    cafe = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBagsFancy", min=0, max=1, weightChance=100},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="BakeryCake", min=1, max=99, weightChance=80},
                {name="BakeryDoughnuts", min=1, max=99, weightChance=100},
                {name="BakeryMisc", min=1, max=99, weightChance=40},
                {name="BakeryPie", min=1, max=99, weightChance=60},
            }
        },
        displaycasebakery = {
            procedural = true,
            procList = {
                {name="BakeryCake", min=1, max=99, weightChance=80},
                {name="BakeryDoughnuts", min=1, max=99, weightChance=100},
                {name="BakeryMisc", min=1, max=99, weightChance=40},
                {name="BakeryPie", min=1, max=99, weightChance=60},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FreezerIceCream", min=0, max=99, forceForTiles="appliances_refrigeration_01_20;appliances_refrigeration_01_21;appliances_refrigeration_01_38;appliances_refrigeration_01_39"},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="CafeKitchenFridge", min=0, max=99},
            }
        },
        grocerstand = {
            procedural = true,
            procList = {
                {name="BakeryCake", min=1, max=99, weightChance=80},
                {name="BakeryDoughnuts", min=1, max=99, weightChance=100},
                {name="BakeryMisc", min=1, max=99, weightChance=40},
                {name="BakeryPie", min=1, max=99, weightChance=60},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="BakeryKitchenBaking", min=0, max=99, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="BakeryCake", min=0, max=99, weightChance=80},
                {name="BakeryDoughnuts", min=0, max=99, weightChance=100},
                {name="BakeryMisc", min=0, max=99, weightChance=40},
                {name="BakeryPie", min=0, max=99, weightChance=60},
                {name="CafeShelfBooks", min=0, max=99, forceForTiles="furniture_shelving_01_40;furniture_shelving_01_41;furniture_shelving_01_42;furniture_shelving_01_43;furniture_shelving_01_44;furniture_shelving_01_45;furniture_shelving_01_46;furniture_shelving_01_47"},
            }
        },
    },

    cafekitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="BakeryKitchenBaking", min=1, max=1, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenCafe", min=1, max=4, weightChance=100},
                {name="StoreKitchenCutlery", min=0, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=1, weightChance=20},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateBakingSoda", min=0, max=1, weightChance=100},
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=100},
                {name="CrateChocolateChips", min=0, max=1, weightChance=100},
                {name="CrateCocoaPowder", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGrahamCrackers", min=0, max=1, weightChance=100},
                {name="CrateMarshmallows", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateSugarBrown", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FreezerIceCream", min=0, max=99, forceForTiles="appliances_refrigeration_01_20;appliances_refrigeration_01_21;appliances_refrigeration_01_38;appliances_refrigeration_01_39"},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="CafeKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateBakingSoda", min=0, max=1, weightChance=100},
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=100},
                {name="CrateChocolateChips", min=0, max=1, weightChance=100},
                {name="CrateCocoaPowder", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGrahamCrackers", min=0, max=1, weightChance=100},
                {name="CrateMarshmallows", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateSugarBrown", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="BakeryDoughnuts", min=1, max=2, weightChance=100},
                {name="BakeryMisc", min=1, max=2, weightChance=100},
                {name="ServingTrayOmelettes", min=1, max=2, weightChance=60},
                {name="ServingTrayPancakes", min=1, max=2, weightChance=60},
                {name="ServingTrayScrambledEggs", min=1, max=1, weightChance=60},
                {name="ServingTrayWaffles", min=1, max=1, weightChance=60},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="StoreKitchenCafe", min=0, max=99},
            }
        },
    },

    cafeteria = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenCutlery", min=0, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=1, weightChance=20},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
            }
        },
        displaycasebakery = {
            procedural = true,
            procList = {
                {name="CafeteriaSandwiches", min=1, max=99, weightChance=40},
                {name="CafeteriaSnacks", min=1, max=99, weightChance=100},
                {name="CafeteriaDrinks", min=1, max=99, weightChance=80},
                {name="CaferteriaFruit", min=1, max=99, weightChance=60},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {

            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="StoreShelfDrinks", min=0, max=99, weightChance=100},
                {name="StoreShelfSnacks", min=0, max=99, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayBurger", min=0, max=1, weightChance=100},
                {name="ServingTrayBurritos", min=0, max=1, weightChance=100},
                {name="ServingTrayChickenNuggets", min=0, max=1, weightChance=100},
                {name="ServingTrayFries", min=0, max=1, weightChance=60},
                {name="ServingTrayGravy", min=0, max=1, weightChance=20},
                {name="ServingTrayHotdogs", min=0, max=1, weightChance=60},
                {name="ServingTrayOmelettes", min=0, max=1, weightChance=60},
                {name="ServingTrayPancakes", min=0, max=1, weightChance=60},
                {name="ServingTrayPizza", min=0, max=1, weightChance=100},
                {name="ServingTrayScrambledEggs", min=0, max=1, weightChance=60},
            }
        },
    },

    cafeteriakitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenBaking", min=1, max=1, weightChance=100},
                {name="StoreKitchenButcher", min=1, max=1, weightChance=100},
                {name="StoreKitchenCutlery", min=0, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=1, weightChance=20},
                {name="StoreKitchenPotatoes", min=1, max=1, weightChance=100},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
                {name="StoreKitchenSauce", min=1, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateCereal", min=0, max=1, weightChance=100},
                {name="CrateCrackers", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateMapleSyrup", min=0, max=1, weightChance=100},
                {name="CrateMarinara", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CratePancakeMix", min=0, max=1, weightChance=100},
                {name="CratePasta", min=0, max=1, weightChance=100},
                {name="CrateRice", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="BurgerKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="BurgerKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateCereal", min=0, max=1, weightChance=100},
                {name="CrateCrackers", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateMapleSyrup", min=0, max=1, weightChance=100},
                {name="CrateMarinara", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CratePancakeMix", min=0, max=1, weightChance=100},
                {name="CratePasta", min=0, max=1, weightChance=100},
                {name="CrateRice", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayBurger", min=0, max=1, weightChance=100},
                {name="ServingTrayBurritos", min=0, max=1, weightChance=100},
                {name="ServingTrayChickenNuggets", min=0, max=1, weightChance=100},
                {name="ServingTrayFries", min=0, max=1, weightChance=60},
                {name="ServingTrayGravy", min=0, max=1, weightChance=20},
                {name="ServingTrayHotdogs", min=0, max=1, weightChance=60},
                {name="ServingTrayOmelettes", min=0, max=1, weightChance=60},
                {name="ServingTrayPancakes", min=0, max=1, weightChance=60},
                {name="ServingTrayPizza", min=0, max=1, weightChance=100},
                {name="ServingTrayScrambledEggs", min=0, max=1, weightChance=60},
            }
        },
    },

    camerastore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCameraFilm", min=0, max=99},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="CameraStoreDisplayCase", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCameraFilm", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="CameraStoreShelves", min=0, max=99},
                {name="StoreShelfCombo", min=0, max=99, weightChance=100, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
    },

    camping = {
        isShop = true,
        clothingrack = {
            procedural = true,
            procList = {
                {name="CampingStoreClothes", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeSnacks", min=0, max=99},
            }
        },
        freezer = {
            rolls = 1,
            items = {

            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
                {name="CampingStoreBooks", min=0, max=4, weightChance=80},
                {name="CampingStoreLegwear", min=0, max=2, weightChance=60},
                {name="CampingStoreBackpacks", min=0, max=2, weightChance=40},
                {name="CampingStoreGear", min=0, max=4, weightChance=100},
                {name="FishingStoreGear", min=0, max=2, weightChance=20},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="CampingStoreBackpacks", min=0, max=2, weightChance=40},
                {name="CampingStoreBooks", min=0, max=4, weightChance=80},
                {name="CampingStoreLegwear", min=0, max=2, weightChance=60},
                {name="CampingStoreGear", min=0, max=4, weightChance=100},
                {name="FishingStoreGear", min=0, max=2, weightChance=20},
            }
        }
    },

    campingstorage = {
        crate = {
            procedural = true,
            procList = {
                {name="CampingStoreClothes", min=0, max=2, weightChance=60},
                {name="CampingStoreLegwear", min=0, max=2, weightChance=60},
                {name="CampingStoreBackpacks", min=0, max=2, weightChance=40},
                {name="CampingStoreGear", min=0, max=4, weightChance=100},
                {name="FishingStoreGear", min=0, max=2, weightChance=20},
            }
        }
    },

    candystorage = {
        isShop = true,
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GigamartCandy", min=0, max=2, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="GigamartCandy", min=0, max=12, weightChance=40},
            }
        }
    },

    candystore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="CandyStoreSnacks", min=0, max=99, weightChance=40},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="CandyStoreSnacks", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="CandyStoreSnacks", min=0, max=99},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
    },

    carsupply = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
                {name="CarSupplyTools", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CarTiresModern1", min=0, max=99, weightChance=80},
                {name="CarTiresModern2", min=0, max=99, weightChance=60},
                {name="CarTiresModern3", min=0, max=99, weightChance=40},
                {name="CarTiresNormal1", min=0, max=99, weightChance=100},
                {name="CarTiresNormal2", min=0, max=99, weightChance=80},
                {name="CarTiresNormal3", min=0, max=99, weightChance=60},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="CarSupplyTools", min=0, max=99, weightChance=100},
            }
        },
    },

    changeroom = {
        locker = {
            procedural = true,
            procList = {
                {name="FactoryLockers", min=0, max=99, forceForRooms="batteryfactory;brewery;dogfoodfactory;factory;fryshipping;metalshop;radiofactory;warehouse;wirefactory;whiskeybottling"},
                {name="FireDeptLockers", min=0, max=99, forceForRooms="firestorage"},
                {name="GymLockers", min=0, max=99, forceForRooms="fitness"},
                {name="HospitalLockers", min=0, max=99, forceForRooms="hospitalroom"},
                {name="HuntingLockers", min=0, max=99, forceForRooms="hunting"},
                {name="PoliceLockers", min=0, max=99, forceForRooms="policestorage"},
                {name="PrisonGuardLockers", min=0, max=99, forceForRooms="cells"},
                {name="SchoolLockers", min=0, max=99, forceForRooms="classroom"},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="ChangeroomCounters", min=0, max=99},
            }
        }
    },

    chinesekitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="ChineseKitchenCutlery", min=1, max=2, weightChance=20},
                {name="ChineseKitchenBaking", min=1, max=1, weightChance=100},
                {name="ChineseKitchenButcher", min=1, max=2, weightChance=100},
                {name="ChineseKitchenSauce", min=1, max=1, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=1, weightChance=20},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateRice", min=0, max=2, weightChance=100},
                {name="CrateSoysauce", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="ChineseKitchenFreezer", min=0, max=99, weightChance=100},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="ChineseKitchenFridge", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateRice", min=0, max=2, weightChance=100},
                {name="CrateSoysauce", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayMeatDumplings", min=1, max=2, weightChance=100},
                {name="ServingTrayMeatSpringRolls", min=1, max=2, weightChance=60},
                {name="ServingTrayMeatSteamBuns", min=1, max=2, weightChance=100},
                {name="ServingTrayMeatTofuFried", min=1, max=2, weightChance=60},
                {name="ServingTrayNoodleSoup", min=1, max=2, weightChance=20},
                {name="ServingTrayShrimpDumplings", min=1, max=2, weightChance=100},
            }
        },
    },

    chineserestaurant = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayMeatDumplings", min=1, max=2, weightChance=100},
                {name="ServingTrayMeatSpringRolls", min=1, max=2, weightChance=60},
                {name="ServingTrayMeatSteamBuns", min=1, max=2, weightChance=100},
                {name="ServingTrayMeatTofuFried", min=1, max=2, weightChance=60},
                {name="ServingTrayNoodleSoup", min=1, max=2, weightChance=20},
                {name="ServingTrayShrimpDumplings", min=1, max=2, weightChance=100},
            }
        },
    },

    classroom = {
        counter = {
            procedural = true,
            procList = {
                {name="ClassroomMisc", min=0, max=99},
            }
        },
        desk = {
            procedural = true,
            procList = {
                {name="ClassroomDesk", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ClassroomShelves", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ClassroomShelves", min=0, max=99},
            }
        }
    },

    closet = {
        crate = {
            procedural = true,
            procList = {
                {name="ClothingStorageWinter", min=0, max=1, weightChance=100},
                {name="CrateBooks", min=0, max=1, weightChance=100},
                {name="CrateCamping", min=0, max=1, weightChance=100},
                {name="CrateCanning", min=0, max=1, weightChance=100},
                {name="CrateCarpentry", min=0, max=1, weightChance=100},
                {name="CrateClothesRandom", min=0, max=1, weightChance=100},
                {name="CrateComics", min=0, max=1, weightChance=100},
                {name="CrateCompactDisks", min=0, max=1, weightChance=100},
                {name="CrateElectronics", min=0, max=1, weightChance=100},
                {name="CrateFarming", min=0, max=1, weightChance=100},
                {name="CrateFishing", min=0, max=1, weightChance=100},
                {name="CrateFitnessWeights", min=0, max=1, weightChance=100},
                {name="CrateFootwearRandom", min=0, max=1, weightChance=100},
                {name="CrateInstruments", min=0, max=1, weightChance=100},
                {name="CrateLinens", min=0, max=1, weightChance=100},
                {name="CrateMagazines", min=0, max=1, weightChance=100},
                {name="CrateMechanics", min=0, max=1, weightChance=100},
                {name="CrateNewspapers", min=0, max=1, weightChance=100},
                {name="CratePaint", min=0, max=1, weightChance=100},
                {name="CratePetSupplies", min=0, max=1, weightChance=100},
                {name="CrateRandomJunk", min=0, max=1, weightChance=100},
                {name="CrateSports", min=0, max=1, weightChance=100},
                {name="CrateTailoring", min=0, max=1, weightChance=100},
                {name="CrateTools", min=0, max=1, weightChance=100},
                {name="CrateVHSTapes", min=0, max=1, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ClosetShelfGeneric", min=0, max=99, weightChance=100},
            }
        },
    },

    clothingstorage = {
        clothingrack = {
            procedural = true,
            procList = {
                {name="ClothingStorageAllJackets", min=0, max=99, weightChance=100},
                {name="ClothingStorageAllShirts", min=0, max=99, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="ClothingStorageAllJackets", min=0, max=99, weightChance=40},
                {name="ClothingStorageAllShirts", min=0, max=99, weightChance=80},
                {name="ClothingStorageFootwear", min=0, max=99, weightChance=40},
                {name="ClothingStorageHeadwear", min=0, max=99, weightChance=20},
                {name="ClothingStorageLegwear", min=0, max=99, weightChance=80},
                {name="ClothingStorageWinter", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ClothingStorageFootwear", min=0, max=99, weightChance=40},
                {name="ClothingStorageHeadwear", min=0, max=99, weightChance=20},
                {name="ClothingStorageLegwear", min=0, max=99, weightChance=100},
            }
        },
    },

    clothingstore = {
        isShop = true,
        clothingrack = {
            procedural = true,
            procList = {
                {name="ClothingStoresDress", min=0, max=99, weightChance=20},
                {name="ClothingStoresJackets", min=0, max=99, weightChance=40},
                {name="ClothingStoresJacketsFormal", min=0, max=99, weightChance=10},
                {name="ClothingStoresJumpers", min=0, max=99, weightChance=60},
                {name="ClothingStoresOvershirts", min=0, max=99, weightChance=80},
                {name="ClothingStoresPants", min=0, max=99, weightChance=100},
                {name="ClothingStoresPantsFormal", min=0, max=99, weightChance=10},
                {name="ClothingStoresShirts", min=0, max=99, weightChance=100},
                {name="ClothingStoresShirtsFormal", min=0, max=99, weightChance=10},
                {name="ClothingStoresSport", min=0, max=99, weightChance=40},
                {name="ClothingStoresSummer", min=0, max=99, weightChance=40},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBagsFancy", min=0, max=1, weightChance=100},
                {name="ClothingStoresGloves", min=0, max=99, weightChance=40},
                {name="ClothingStoresEyewear", min=0, max=99, weightChance=100},
                {name="ClothingStoresHeadwear", min=0, max=99, weightChance=60},
                {name="ClothingStoresSocks", min=0, max=99, weightChance=20},
                {name="ClothingStoresUnderwearWoman", min=0, max=99, weightChance=20},
                {name="ClothingStoresUnderwearMan", min=0, max=99, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="ClothingStorageAllJackets", min=0, max=99, weightChance=40},
                {name="ClothingStorageAllShirts", min=0, max=99, weightChance=80},
                {name="ClothingStorageFootwear", min=0, max=99, weightChance=40},
                {name="ClothingStorageHeadwear", min=0, max=99, weightChance=20},
                {name="ClothingStorageLegwear", min=0, max=99, weightChance=80},
                {name="ClothingStorageWinter", min=0, max=99, weightChance=100},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="StoreDisplayWatches", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ClothingStorageFootwear", min=0, max=99, weightChance=40},
                {name="ClothingStorageHeadwear", min=0, max=99, weightChance=20},
                {name="ClothingStorageLegwear", min=0, max=99, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ClothingStoresBoots", min=0, max=99, weightChance=50},
                {name="ClothingStoresShoes", min=0, max=99, weightChance=100},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
    },

    construction = {
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateConcrete", min=0, max=99, weightChance=40},
                {name="CrateLumber", min=0, max=99, weightChance=100},
                {name="CratePaint", min=0, max=99, weightChance=80},
                {name="CratePlaster", min=0, max=99, weightChance=80},
                {name="CrateTools", min=0, max=99, weightChance=100},
            }
        },
    },

    conveniencestore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="Empty", min=0, max=99, forceForTiles="location_shop_accessories_01_8;location_shop_accessories_01_9;location_shop_accessories_01_10;location_shop_accessories_01_11;location_shop_accessories_01_11;location_shop_accessories_01_12;location_shop_fossoil_01_10;location_shop_fossoil_01_11;"},
                {name="StoreCounterBags", min=0, max=2, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23"},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FreezerIceCream", min=0, max=99, forceForTiles="appliances_refrigeration_01_20;appliances_refrigeration_01_21;appliances_refrigeration_01_38;appliances_refrigeration_01_39"},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeOther", min=1, max=99, weightChance=40},
                {name="FridgeSnacks", min=1, max=99, weightChance=100},
                {name="FridgeSoda", min=1, max=99, weightChance=100},
                {name="FridgeWater", min=1, max=99, weightChance=60},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="StoreCounterTobacco", min=0, max=99, forceForTiles="location_shop_generic_01_28;location_shop_generic_01_29;location_shop_generic_01_30;location_shop_generic_01_31"},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
                {name="StoreShelfDrinks", min=0, max=99, weightChance=100},
                {name="StoreShelfMechanics", min=0, max=99, forceForTiles="location_shop_generic_01_3;location_shop_generic_01_4"},
                {name="StoreShelfMedical", min=0, max=1, weightChance=20},
                {name="StoreShelfSnacks", min=0, max=99, weightChance=100},
            }
        }
    },

    cornerstore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="Empty", min=0, max=99, forceForTiles="location_shop_accessories_01_8;location_shop_accessories_01_9;location_shop_accessories_01_10;location_shop_accessories_01_11;location_shop_accessories_01_11;location_shop_accessories_01_12;location_shop_fossoil_01_10;location_shop_fossoil_01_11;"},
                {name="StoreCounterBags", min=0, max=2, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23"},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        displaycasebakery = {
            procedural = true,
            procList = {
                {name="BakeryBread", min=0, max=99, weightChance=100},
                {name="BakeryCake", min=0, max=99, weightChance=80},
                {name="BakeryMisc", min=0, max=99, weightChance=20},
                {name="BakeryPie", min=0, max=99, weightChance=60},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FreezerIceCream", min=0, max=99, forceForTiles="appliances_refrigeration_01_20;appliances_refrigeration_01_21;appliances_refrigeration_01_38;appliances_refrigeration_01_39"},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeOther", min=1, max=99, weightChance=40},
                {name="FridgeSnacks", min=1, max=99, weightChance=100},
                {name="FridgeSoda", min=1, max=99, weightChance=100},
                {name="FridgeWater", min=1, max=99, weightChance=60},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="StoreCounterTobacco", min=0, max=99, forceForTiles="location_shop_generic_01_28;location_shop_generic_01_29;location_shop_generic_01_30;location_shop_generic_01_31"},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
                {name="StoreShelfDrinks", min=0, max=99, weightChance=100},
                {name="StoreShelfMechanics", min=0, max=99, forceForTiles="location_shop_generic_01_3;location_shop_generic_01_4"},
                {name="StoreShelfMedical", min=0, max=1, weightChance=20},
                {name="StoreShelfSnacks", min=0, max=99, weightChance=100},
            }
        }
    },

    cornerstorestorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
    },

    daycare = {
        counter = {
            procedural = true,
            procList = {
                {name="DaycareCounter", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeBreakRoom", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="DaycareShelves", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="DaycareShelves", min=0, max=99},
            }
        },
        wardrobe = {
            procedural = true,
            procList = {
                {name="WardrobeChild", min=0, max=99},
            }
        },
    },

    deepfry_kitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="BurgerKitchenButcher", min=0, max=1, weightChance=100},
                {name="BurgerKitchenSauce", min=0, max=1, weightChance=100},
                {name="FishChipsKitchenButcher", min=0, max=1, weightChance=100},
                {name="FishChipsKitchenSauce", min=0, max=1, weightChance=100},
                {name="JaysKitchenBaking", min=0, max=1, weightChance=100},
                {name="JaysKitchenButcher", min=0, max=1, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenBaking", min=0, max=1, weightChance=100},
                {name="StoreKitchenCutlery", min=1, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=1, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=1, max=1, weightChance=20},
                {name="StoreKitchenPotatoes", min=0, max=1, weightChance=100},
                {name="StoreKitchenPots", min=1, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCornflour", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="DeepFryKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="DeepFryKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCornflour", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayBiscuits", min=0, max=1, weightChance=60},
                {name="ServingTrayCornbread", min=0, max=1, weightChance=60},
                {name="ServingTrayFish", min=1, max=2, weightChance=100},
                {name="ServingTrayFries", min=1, max=2, weightChance=100},
                {name="ServingTrayGravy", min=1, max=2, weightChance=20},
                {name="ServingTrayOnionRings", min=1, max=2, weightChance=60},
                {name="ServingTrayOysters", min=0, max=1, weightChance=40},
                {name="ServingTrayShrimp", min=0, max=1, weightChance=40},
            }
        },
    },

    dentiststorage = {
        isShop = true,
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MedicalStorageDrugs", min=0, max=6, weightChance=100},
                {name="MedicalStorageTools", min=0, max=4, weightChance=80},
                {name="MedicalStorageOutfit", min=0, max=2, weightChance=40},
            }
        }
    },

    departmentstorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CrateTV", min=0, max=2, weightChance=10},
                {name="CrateTVWide", min=0, max=2, weightChance=10},
                {name="ClothingStorageWinter", min=0, max=4, weightChance=100},
                {name="ClothingStorageHeadwear", min=0, max=2, weightChance=20},
                {name="ClothingStorageFootwear", min=0, max=2, weightChance=20},
                {name="ClothingStorageAllJackets", min=0, max=2, weightChance=80},
                {name="ClothingStorageAllShirts", min=0, max=2, weightChance=80},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ClothingStorageFootwear", min=0, max=2, weightChance=20},
                {name="ClothingStorageHeadwear", min=0, max=2, weightChance=20},
                {name="ClothingStorageLegwear", min=0, max=4, weightChance=80},
                {name="GigamartHousewares", min=0, max=2, weightChance=60},
                {name="GigamartBedding", min=0, max=2, weightChance=60},
                {name="GigamartPots", min=0, max=2, weightChance=60},
                {name="GigamartLightbulb", min=0, max=2, weightChance=60},
                {name="GigamartHouseElectronics", min=0, max=4, weightChance=100},
            }
        },
        wardrobe = {
            rolls = 0,
            items = {

            }
        },
        sidetable = {
            rolls = 0,
            items = {

            }
        }
    },

    departmentstore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="JewelrySilver", min=0, max=4, weightChance=80},
                {name="JewelryGold", min=0, max=4, weightChance=40},
                {name="JewelryGems", min=0, max=2, weightChance=10},
                {name="JewelryWeddingRings", min=0, max=8, weightChance=100},
                {name="JewelryWrist", min=0, max=2, weightChance=80},
                {name="JewelryOthers", min=0, max=99, weightChance=10},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ClothingStoresBoots", min=0, max=12, weightChance=50},
                {name="ClothingStoresShoes", min=0, max=24, weightChance=100},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
        wardrobe = {
            rolls = 0,
            items = {

            }
        },
        sidetable = {
            rolls = 0,
            items = {

            }
        }
    },

    dinerkitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenBaking", min=1, max=1, weightChance=100},
                {name="StoreKitchenButcher", min=1, max=1, weightChance=100},
                {name="StoreKitchenCafe", min=0, max=1, weightChance=100},
                {name="StoreKitchenCutlery", min=0, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=1, weightChance=20},
                {name="StoreKitchenPotatoes", min=1, max=1, weightChance=100},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
                {name="StoreKitchenSauce", min=1, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCoffee", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateTea", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="DinerKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="DinerKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCoffee", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateTea", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="BakeryDoughnuts", min=0, max=1, weightChance=20},
                {name="ServingTrayBurgers", min=1, max=2, weightChance=100},
                {name="ServingTrayBurritos", min=0, max=1, weightChance=20},
                {name="ServingTrayChickenNuggets", min=0, max=1, weightChance=80},
                {name="ServingTrayFries", min=1, max=2, weightChance=60},
                {name="ServingTrayGravy", min=1, max=2, weightChance=20},
                {name="ServingTrayHotdogs", min=0, max=1, weightChance=40},
                {name="ServingTrayOmelettes", min=0, max=1, weightChance=20},
                {name="ServingTrayOnionRings", min=0, max=1, weightChance=40},
                {name="ServingTrayPancakes", min=0, max=1, weightChance=20},
                {name="ServingTrayPie", min=0, max=1, weightChance=40},
                {name="ServingTrayPizza", min=0, max=1, weightChance=40},
                {name="ServingTrayScrambledEggs", min=0, max=1, weightChance=20},
                {name="ServingTrayWaffles", min=0, max=1, weightChance=20},
            }
        },
    },

    dining = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="BarCounterLiquor", min=0, max=4, forceForTiles="location_restaurant_bar_01_0;location_restaurant_bar_01_1;location_restaurant_bar_01_2;location_restaurant_bar_01_3;location_restaurant_bar_01_4;location_restaurant_bar_01_5;location_restaurant_bar_01_6;location_restaurant_bar_01_7;location_restaurant_bar_01_16;location_restaurant_bar_01_17;location_restaurant_bar_01_18;location_restaurant_bar_01_19;location_restaurant_bar_01_20;location_restaurant_bar_01_21;location_restaurant_bar_01_22;location_restaurant_bar_01_23;location_restaurant_bar_01_56;location_restaurant_bar_01_57;location_restaurant_bar_01_58;location_restaurant_bar_01_59;location_restaurant_bar_01_60;location_restaurant_bar_01_61;location_restaurant_bar_01_62;location_restaurant_bar_01_63"},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="BakeryDoughnuts", min=0, max=1, weightChance=20},
                {name="ServingTrayBurgers", min=1, max=2, weightChance=100},
                {name="ServingTrayBurritos", min=0, max=1, weightChance=20},
                {name="ServingTrayChickenNuggets", min=0, max=1, weightChance=80},
                {name="ServingTrayFries", min=1, max=2, weightChance=60},
                {name="ServingTrayGravy", min=1, max=2, weightChance=20},
                {name="ServingTrayHotdogs", min=0, max=1, weightChance=40},
                {name="ServingTrayOmelettes", min=0, max=1, weightChance=20},
                {name="ServingTrayOnionRings", min=0, max=1, weightChance=40},
                {name="ServingTrayPancakes", min=0, max=1, weightChance=20},
                {name="ServingTrayPie", min=0, max=1, weightChance=40},
                {name="ServingTrayPizza", min=0, max=1, weightChance=40},
                {name="ServingTrayScrambledEggs", min=0, max=1, weightChance=20},
                {name="ServingTrayWaffles", min=0, max=1, weightChance=20},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99, forceForTiles="location_restaurant_bar_01_29;location_restaurant_bar_01_30;location_restaurant_bar_01_31;location_restaurant_bar_01_37;location_restaurant_bar_01_38;location_restaurant_bar_01_39;location_restaurant_bar_01_64;location_restaurant_bar_01_65;location_restaurant_bar_01_66;location_restaurant_bar_01_72;location_restaurant_bar_01_73;location_restaurant_bar_01_74"},
            }
        }
    },

    dogfoodfactory = {
        isShop = true,
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="DogFoodFactoryCans", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="DogFoodFactoryCans", min=0, max=99},
            }
        },
    },

    dogfoodshipping = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="DogFoodFactoryCans", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="DogFoodFactoryCans", min=0, max=99},
            }
        },
    },

    dogfoodstorage = {
        isShop = true,
        freezer = {
            rolls = 1,
            items = {

            }
        },
        fridge = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="DogFoodFactoryCans", min=0, max=99},
            }
        },
    },

    donut_dining = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        },
    },
    
    donut_kitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="BakeryKitchenBaking", min=1, max=2, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenCafe", min=0, max=2, weightChance=100},
                {name="StoreKitchenCutlery", min=0, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateBakingSoda", min=0, max=1, weightChance=100},
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateSugarBrown", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateBakingSoda", min=0, max=1, weightChance=100},
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateSugarBrown", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="BakeryDoughnuts", min=1, max=99, weightChance=100},
            }
        },
    },
    
    donut_kitchenstorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CrateBakingSoda", min=0, max=1, weightChance=100},
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateSugarBrown", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="BakeryKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="BakeryKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateBakingSoda", min=0, max=1, weightChance=100},
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateSugarBrown", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
    },
    
    electronicsstorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="ElectronicStoreComputer", min=0, max=12, weightChance=100},
                {name="CrateTV", min=0, max=12, weightChance=40},
                {name="CrateTVWide", min=0, max=12, weightChance=20},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ElectronicStoreMusic", min=0, max=2, weightChance=100},
                {name="ElectronicStoreLights", min=0, max=2, weightChance=20},
                {name="ElectronicStoreMagazines", min=0, max=2, weightChance=40},
                {name="ElectronicStoreMisc", min=0, max=99, weightChance=40},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ElectronicStoreComputer", min=0, max=2, weightChance=100},
                {name="ElectronicStoreHAMRadio", min=0, max=1, weightChance=20},
                {name="ElectronicStoreMisc", min=0, max=99, weightChance=40},
            }
        }
    },

    electronicsstore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
                {name="ElectronicStoreHAMRadio", min=0, max=1, weightChance=20},
                {name="ElectronicStoreComputer", min=0, max=4, weightChance=100},
                {name="ElectronicStoreMusic", min=0, max=4, weightChance=100},
                {name="ElectronicStoreLights", min=0, max=2, weightChance=20},
                {name="ElectronicStoreMagazines", min=0, max=2, weightChance=40},
                {name="ElectronicStoreMisc", min=0, max=99, weightChance=40},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="StoreDisplayWatches", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ElectronicStoreHAMRadio", min=0, max=1, weightChance=20},
                {name="ElectronicStoreComputer", min=0, max=4, weightChance=100},
                {name="ElectronicStoreMusic", min=0, max=4, weightChance=100},
                {name="ElectronicStoreLights", min=0, max=2, weightChance=20},
                {name="ElectronicStoreMagazines", min=1, max=2, weightChance=40},
                {name="ElectronicStoreMisc", min=0, max=99, weightChance=40},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        }
    },

    empty = {
        crate = {
            procedural = true,
            procList = {
                {name="RandomFiller", min=0, max=99},
            }
        },
        other = {
            rolls = 0,
            items = {

            }
        },
    },

    factory = {
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateAntiqueStove", min=0, max=1, weightChance=5},
                {name="CrateCarpentry", min=0, max=99, weightChance=100},
                {name="CrateConcrete", min=0, max=99, weightChance=40},
                {name="CrateFarming", min=0, max=99, weightChance=40},
                {name="CrateGravelBags", min=0, max=99, weightChance=40},
                {name="CrateLumber", min=0, max=99, weightChance=40},
                {name="CrateMetalwork", min=0, max=99, weightChance=100},
                {name="CratePaint", min=0, max=99, weightChance=40},
                {name="CratePlaster", min=0, max=99, weightChance=40},
                {name="CrateSandBags", min=0, max=99, weightChance=40},
                {name="CrateSheetMetal", min=0, max=99, weightChance=40},
                {name="CrateTools", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCarpentry", min=0, max=99, weightChance=100},
                {name="CrateFarming", min=0, max=99, weightChance=40},
                {name="CrateMetalwork", min=0, max=99, weightChance=100},
                {name="CratePaint", min=0, max=99, weightChance=40},
                {name="CrateTools", min=0, max=99, weightChance=100},
            }
        },
    },

    factorystorage = {
        locker = {
            procedural = true,
            procList = {
                {name="FactoryLockers", min=0, max=99, weightChance=100},
                {name="MechanicShelfOutfit", min=0, max=99, forceForRooms="mechanic"},
            }
        }
    },

    farmstorage = {
        crate = {
            procedural = true,
            procList = {
                {name="CarTiresNormal3", min=0, max=1, weightChance=40},
                {name="CrateFarming", min=0, max=99, weightChance=20},
                {name="CrateFertilizer", min=0, max=1, weightChance=100},
                {name="CrateGravelBags", min=0, max=1, weightChance=40},
                {name="CratePlaster", min=0, max=1, weightChance=40},
                {name="CrateSandbags", min=0, max=1, weightChance=60},
                {name="CrateTools", min=0, max=2, weightChance=40},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CarTiresNormal3", min=0, max=1, weightChance=40},
                {name="CrateFarming", min=0, max=99, weightChance=20},
                {name="CrateFertilizer", min=0, max=1, weightChance=100},
                {name="CrateGravelBags", min=0, max=1, weightChance=40},
                {name="CratePlaster", min=0, max=1, weightChance=40},
                {name="CrateSandbags", min=0, max=1, weightChance=60},
                {name="CrateTools", min=0, max=2, weightChance=40},
            }
        },
    },

    firestorage = {
        metal_shelves = {
            procedural = true,
            procList = {
                {name="FireStorageTools", min=0, max=99, weightChance=100},
                {name="FireStorageOutfit", min=0, max=99, weightChance=40},
            }
        }
    },

    fishchipskitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="FishChipsKitchenButcher", min=1, max=1, weightChance=100},
                {name="FishChipsKitchenSauce", min=1, max=1, weightChance=100},
                {name="JaysKitchenBaking", min=1, max=1, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenCutlery", min=0, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=1, weightChance=20},
                {name="StoreKitchenPotatoes", min=1, max=1, weightChance=100},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCornflour", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FishChipsKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FishChipsKitchenFreezer", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCornflour", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayBiscuits", min=1, max=2, weightChance=60},
                {name="ServingTrayFish", min=1, max=4, weightChance=100},
                {name="ServingTrayFries", min=1, max=4, weightChance=100},
                {name="ServingTrayGravy", min=1, max=2, weightChance=20},
            }
        },
    },

    fishingstorage = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
                {name="CampingStoreBooks", min=0, max=2, weightChance=80},
                {name="CampingStoreLegwear", min=0, max=2, weightChance=40},
                {name="CampingStoreBackpacks", min=0, max=2, weightChance=20},
                {name="CampingStoreGear", min=0, max=2, weightChance=60},
                {name="FishingStoreGear", min=0, max=12, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="FishingStoreGear", min=0, max=99},
            }
        }
    },

    fossoil = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterBags", min=0, max=1, weightChance=20},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_fossoil_01_10;location_shop_fossoil_01_11;location_shop_accessories_01_10;location_shop_accessories_01_11;location_shop_accessories_01_8;location_shop_accessories_01_9;location_shop_accessories_01_12;location_shop_accessories_01_13"},
                {name="StoreCounterTobacco", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23"},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FreezerIceCream", min=0, max=99, forceForTiles="appliances_refrigeration_01_20;appliances_refrigeration_01_21;appliances_refrigeration_01_38;appliances_refrigeration_01_39"},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeOther", min=1, max=99, weightChance=40},
                {name="FridgeSnacks", min=1, max=99, weightChance=100},
                {name="FridgeSoda", min=1, max=99, weightChance=100},
                {name="FridgeWater", min=1, max=99, weightChance=60},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="StoreCounterTobacco", min=0, max=99, forceForTiles="location_shop_generic_01_28;location_shop_generic_01_29;location_shop_generic_01_30;location_shop_generic_01_31"},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
                {name="StoreShelfDrinks", min=0, max=99, weightChance=100},
                {name="StoreShelfMechanics", min=0, max=99, forceForTiles="location_shop_generic_01_3;location_shop_generic_01_4"},
                {name="StoreShelfMedical", min=0, max=1, weightChance=20},
                {name="StoreShelfSnacks", min=0, max=99, weightChance=100},
            }
        }
    },

    fryshipping = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="FryShippingPotatoes", min=0, max=99},
            }
        }
    },

    furniturestorage = {
        isShop = true,
        counter = {
            rolls = 0,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateBlueComfyChair", min=0, max=1, weightChance=60},
                {name="CrateBluePlasticChairs", min=0, max=1, weightChance=100},
                {name="CrateBlueRattanChair", min=0, max=1, weightChance=40},
                {name="CrateBrownComfyChair", min=0, max=1, weightChance=60},
                {name="CrateBrownLowTables", min=0, max=1, weightChance=40},
                {name="CrateChromeSinks", min=0, max=1, weightChance=20},
                {name="CrateDarkBlueChairs", min=0, max=1, weightChance=80},
                {name="CrateDarkWoodenChairs", min=0, max=1, weightChance=80},
                {name="CrateFancyBlackChairs", min=0, max=1, weightChance=60},
                {name="CrateFancyDarkTables", min=0, max=1, weightChance=40},
                {name="CrateFancyLowTables", min=0, max=1, weightChance=40},
                {name="CrateFancyToilets", min=0, max=1, weightChance=20},
                {name="CrateFancyWhiteChairs", min=0, max=1, weightChance=60},
                {name="CrateFoldingChairs", min=0, max=1, weightChance=100},
                {name="CrateGreenChairs", min=0, max=1, weightChance=80},
                {name="CrateGreenComfyChair", min=0, max=1, weightChance=60},
                {name="CrateGreenOven", min=0, max=1, weightChance=20},
                {name="CrateGreyChairs", min=0, max=1, weightChance=80},
                {name="CrateGreyComfyChair", min=0, max=1, weightChance=60},
                {name="CrateGreyOven", min=0, max=1, weightChance=20},
                {name="CrateIndustrialSinks", min=0, max=1, weightChance=20},
                {name="CrateLightRoundTable", min=0, max=1, weightChance=60},
                {name="CrateMetalLockers", min=0, max=1, weightChance=40},
                {name="CrateModernOven", min=0, max=1, weightChance=20},
                {name="CrateOakRoundTable", min=0, max=1, weightChance=60},
                {name="CrateOfficeChairs", min=0, max=1, weightChance=100},
                {name="CrateOrangeModernChair", min=0, max=1, weightChance=60},
                {name="CratePlasticChairs", min=0, max=1, weightChance=100},
                {name="CratePurpleRattanChair", min=0, max=1, weightChance=40},
                {name="CratePurpleWoodenChairs", min=0, max=1, weightChance=80},
                {name="CrateRedBBQs", min=0, max=1, weightChance=40},
                {name="CrateRedChairs", min=0, max=1, weightChance=80},
                {name="CrateRedOven", min=0, max=1, weightChance=20},
                {name="CrateRedWoodenChairs", min=0, max=1, weightChance=80},
                {name="CrateRoundTable", min=0, max=1, weightChance=40},
                {name="CrateWhiteComfyChair", min=0, max=1, weightChance=60},
                {name="CrateWhiteSimpleChairs", min=0, max=1, weightChance=80},
                {name="CrateWhiteSinks", min=0, max=1, weightChance=20},
                {name="CrateWhiteWoodenChairs", min=0, max=1, weightChance=80},
                {name="CrateWoodenChairs", min=0, max=1, weightChance=80},
                {name="CrateWoodenStools", min=0, max=1, weightChance=80},
                {name="CrateYellowModernChair", min=0, max=1, weightChance=60},
            }
        },
        desk = {
            rolls = 0,
            items = {

            }
        },
        dishescabinet = {
            rolls = 0,
            items = {

            }
        },
        freezer = {
            rolls = 0,
            items = {

            }
        },
        fridge = {
            rolls = 0,
            items = {

            }
        },
        overhead = {
            rolls = 0,
            items = {

            }
        },
        sidetable = {
            rolls = 0,
            items = {

            }
        },
        wardrobe = {
            rolls = 0,
            items = {

            }
        },
    },

    furniturestore = {
        isShop = true,
        counter = {
            rolls = 0,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateBlueComfyChair", min=0, max=1, weightChance=60},
                {name="CrateBluePlasticChairs", min=0, max=1, weightChance=100},
                {name="CrateBlueRattanChair", min=0, max=1, weightChance=40},
                {name="CrateBrownComfyChair", min=0, max=1, weightChance=60},
                {name="CrateBrownLowTables", min=0, max=1, weightChance=40},
                {name="CrateChromeSinks", min=0, max=1, weightChance=20},
                {name="CrateDarkBlueChairs", min=0, max=1, weightChance=80},
                {name="CrateDarkWoodenChairs", min=0, max=1, weightChance=80},
                {name="CrateFancyBlackChairs", min=0, max=1, weightChance=60},
                {name="CrateFancyDarkTables", min=0, max=1, weightChance=40},
                {name="CrateFancyLowTables", min=0, max=1, weightChance=40},
                {name="CrateFancyToilets", min=0, max=1, weightChance=20},
                {name="CrateFancyWhiteChairs", min=0, max=1, weightChance=60},
                {name="CrateFoldingChairs", min=0, max=1, weightChance=100},
                {name="CrateGreenChairs", min=0, max=1, weightChance=80},
                {name="CrateGreenComfyChair", min=0, max=1, weightChance=60},
                {name="CrateGreenOven", min=0, max=1, weightChance=20},
                {name="CrateGreyChairs", min=0, max=1, weightChance=80},
                {name="CrateGreyComfyChair", min=0, max=1, weightChance=60},
                {name="CrateGreyOven", min=0, max=1, weightChance=20},
                {name="CrateIndustrialSinks", min=0, max=1, weightChance=20},
                {name="CrateLightRoundTable", min=0, max=1, weightChance=60},
                {name="CrateMetalLockers", min=0, max=1, weightChance=40},
                {name="CrateModernOven", min=0, max=1, weightChance=20},
                {name="CrateOakRoundTable", min=0, max=1, weightChance=60},
                {name="CrateOfficeChairs", min=0, max=1, weightChance=100},
                {name="CrateOrangeModernChair", min=0, max=1, weightChance=60},
                {name="CratePlasticChairs", min=0, max=1, weightChance=100},
                {name="CratePurpleRattanChair", min=0, max=1, weightChance=40},
                {name="CratePurpleWoodenChairs", min=0, max=1, weightChance=80},
                {name="CrateRedBBQs", min=0, max=1, weightChance=40},
                {name="CrateRedChairs", min=0, max=1, weightChance=80},
                {name="CrateRedOven", min=0, max=1, weightChance=20},
                {name="CrateRedWoodenChairs", min=0, max=1, weightChance=80},
                {name="CrateRoundTable", min=0, max=1, weightChance=40},
                {name="CrateWhiteComfyChair", min=0, max=1, weightChance=60},
                {name="CrateWhiteSimpleChairs", min=0, max=1, weightChance=80},
                {name="CrateWhiteSinks", min=0, max=1, weightChance=20},
                {name="CrateWhiteWoodenChairs", min=0, max=1, weightChance=80},
                {name="CrateWoodenChairs", min=0, max=1, weightChance=80},
                {name="CrateWoodenStools", min=0, max=1, weightChance=80},
                {name="CrateYellowModernChair", min=0, max=1, weightChance=60},
            }
        },
        desk = {
            rolls = 0,
            items = {

            }
        },
        dishescabinet = {
            rolls = 0,
            items = {

            }
        },
        displaycase = {
            rolls = 0,
            items = {

            }
        },
        freezer = {
            rolls = 0,
            items = {

            }
        },
        fridge = {
            rolls = 0,
            items = {

            }
        },
        shelves = {
            rolls = 0,
            items = {

            }
        },
        sidetable = {
            rolls = 0,
            items = {

            }
        },
        wardrobe = {
            rolls = 0,
            items = {

            }
        },
    },

    garagestorage = {
        counter = {
            procedural = true,
            procList = {
                {name="GarageCarpentry", min=0, max=2, weightChance=100},
                {name="GarageMechanics", min=0, max=1, weightChance=100},
                {name="GarageMetalwork", min=0, max=2, weightChance=100},
                {name="GarageTools", min=0, max=99, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="ClothingStorageWinter", min=0, max=1, weightChance=40},
                {name="CrateAntiqueStove", min=0, max=1, weightChance=5},
                {name="CrateBluePlasticChairs", min=0, max=1, weightChance=40},
                {name="CrateBooks", min=0, max=1, weightChance=80},
                {name="CrateCamping", min=0, max=1, weightChance=100},
                {name="CrateCannedFood", min=0, max=1, weightChance=20},
                {name="CrateCannedFoodSpoiled", min=0, max=1, weightChance=20},
                {name="CrateCanning", min=0, max=1, weightChance=20},
                {name="CrateClothesRandom", min=0, max=1, weightChance=80},
                {name="CrateComics", min=0, max=1, weightChance=80},
                {name="CrateCompactDiscs", min=0, max=1, weightChance=20},
                {name="CrateComputer", min=0, max=1, weightChance=40},
                {name="CrateConcrete", min=0, max=1, weightChance=100},
                {name="CrateDishes", min=0, max=1, weightChance=60},
                {name="CrateElectronics", min=0, max=1, weightChance=60},
                {name="CrateEmptyBottles1", min=0, max=1, weightChance=100},
                {name="CrateEmptyBottles2", min=0, max=1, weightChance=100},
                {name="CrateEmptyMixed", min=0, max=1, weightChance=100},
                {name="CrateEmptyTinCans", min=0, max=1, weightChance=100},
                {name="CrateFarming", min=0, max=1, weightChance=100},
                {name="CrateFertilizer", min=0, max=1, weightChance=100},
                {name="CrateFishing", min=0, max=1, weightChance=100},
                {name="CrateFitnessWeights", min=0, max=1, weightChance=40},
                {name="CrateFoldingChairs", min=0, max=1, weightChance=40},
                {name="CrateFootwearRandom", min=0, max=1, weightChance=80},
                {name="CrateGravelBags", min=0, max=1, weightChance=100},
                {name="CrateInstruments", min=0, max=1, weightChance=40},
                {name="CrateLinens", min=0, max=1, weightChance=60},
                {name="CrateLumber", min=0, max=1, weightChance=100},
                {name="CrateMagazines", min=0, max=1, weightChance=80, },
                {name="CrateMechanics", min=0, max=1, weightChance=100},
                {name="CrateMetalwork", min=0, max=1, weightChance=100},
                {name="CrateNewspapers", min=0, max=1, weightChance=80},
                {name="CrateOfficeSupplies", min=0, max=1, weightChance=80},
                {name="CratePaint", min=0, max=1, weightChance=100},
                {name="CratePetSupplies", min=0, max=1, weightChance=40},
                {name="CratePlaster", min=0, max=1, weightChance=100},
                {name="CratePlasticChairs", min=0, max=1, weightChance=40},
                {name="CrateRandomJunk", min=0, max=1, weightChance=100},
                {name="CrateSandbags", min=0, max=1, weightChance=100},
                {name="CrateSheetMetal", min=0, max=1, weightChance=100},
                {name="CrateSports", min=0, max=1, weightChance=60},
                {name="CrateTailoring", min=0, max=1, weightChance=100},
                {name="CrateTools", min=0, max=1, weightChance=60},
                {name="CrateToys", min=0, max=1, weightChance=40},
                {name="CrateTV", min=0, max=1, weightChance=40},
                {name="CrateVHSTapes", min=0, max=1, weightChance=20},
                {name="CrateWhiteWoodenChairs", min=0, max=1, weightChance=40},
                {name="CrateWoodenChairs", min=0, max=1, weightChance=40},
                {name="CrateWoodenStools", min=0, max=1, weightChance=40},
            }
        },
        locker = {
            procedural = true,
            procList = {
                {name="FireDeptLockers", min=0, max=99, forceForRooms="firestorage"},
                {name="GarageFirearms", min=0, max=99, forceForTiles="furniture_storage_02_8;furniture_storage_02_9;furniture_storage_02_10;furniture_storage_02_11"},
                {name="GarageTools", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GarageCarpentry", min=0, max=1, weightChance=100},
                {name="GarageMechanics", min=0, max=1, weightChance=100},
                {name="GarageMetalwork", min=0, max=1, weightChance=100},
                {name="GarageTools", min=0, max=99, weightChance=20},
            }
        },
    },

    gardenstore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
                {name="GardenStoreMisc", min=0, max=99, weightChance=100},
            }
        },
        clothingrack = {
            procedural = true,
            procList = {
                {name="CampingStoreClothes", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GardenStoreTools", min=0, max=99, weightChance=100},
                {name="GardenStoreMisc", min=0, max=99, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="GardenStoreTools", min=0, max=99, weightChance=100},
                {name="GardenStoreMisc", min=0, max=99, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateFertilizer", min=0, max=99, weightChance=100},
                {name="CrateGravelBags", min=0, max=99, weightChance=60},
                {name="CrateSandBags", min=0, max=99, weightChance=60},
            }
        }
    },

    gasstorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=4, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        }
    },

    gasstore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterBags", min=0, max=1, weightChance=20},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_fossoil_01_10;location_shop_fossoil_01_11;location_shop_accessories_01_10;location_shop_accessories_01_11;location_shop_accessories_01_8;location_shop_accessories_01_9;location_shop_accessories_01_12;location_shop_accessories_01_13"},
                {name="StoreCounterTobacco", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23"},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FreezerIceCream", min=0, max=99, forceForTiles="appliances_refrigeration_01_20;appliances_refrigeration_01_21;appliances_refrigeration_01_38;appliances_refrigeration_01_39"},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeOther", min=1, max=99, weightChance=40},
                {name="FridgeSnacks", min=1, max=99, weightChance=100},
                {name="FridgeSoda", min=1, max=99, weightChance=100},
                {name="FridgeWater", min=1, max=99, weightChance=60},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=40},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="StoreCounterTobacco", min=0, max=99, forceForTiles="location_shop_generic_01_28;location_shop_generic_01_29;location_shop_generic_01_30;location_shop_generic_01_31"},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
                {name="StoreShelfDrinks", min=0, max=99, weightChance=100},
                {name="StoreShelfMechanics", min=0, max=99, forceForTiles="location_shop_generic_01_3;location_shop_generic_01_4"},
                {name="StoreShelfMedical", min=0, max=1, weightChance=20},
                {name="StoreShelfSnacks", min=0, max=99, weightChance=100},
            }
        }
    },

    generalstore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCigarettes", min=0, max=1, weightChance=40},
                {name="GigamartCrisps", min=0, max=99, weightChance=25},
                {name="GigamartCandy", min=0, max=99, weightChance=25},
                {name="GigamartCannedFood", min=0, max=99, weightChance=100},
                {name="GigamartSauce", min=0, max=99, weightChance=10},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeSnacks", min=0, max=99, weightChance=100},
                {name="FridgeSoda", min=0, max=99, weightChance=100},
                {name="FridgeWater", min=0, max=99, weightChance=40},
                {name="FridgeOther", min=1, max=99, weightChance=60},
            }
        },
        freezer = {
            rolls = 1,
            items = {

            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GigamartTools", min=0, max=99, weightChance=100},
                {name="GigamartFarming", min=0, max=99, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="GigamartBakingMisc", min=1, max=4, weightChance=60},
                {name="GigamartBedding", min=1, max=2, weightChance=40},
                {name="GigamartBottles", min=2, max=4, weightChance=60},
                {name="GigamartCandy", min=1, max=4, weightChance=60},
                {name="GigamartCannedFood", min=0, max=99, weightChance=20},
                {name="GigamartCrisps", min=1, max=4, weightChance=60},
                {name="GigamartDryGoods", min=1, max=4, weightChance=60},
                {name="GigamartFarming", min=1, max=4, weightChance=60},
                {name="GigamartHouseElectronics", min=1, max=2, weightChance=60},
                {name="GigamartHousewares", min=1, max=2, weightChance=60},
                {name="GigamartLightbulb", min=0, max=1, weightChance=20},
                {name="GigamartPots", min=1, max=2, weightChance=60},
                {name="GigamartSauce", min=1, max=2, weightChance=80},
                {name="GigamartSchool", min=0, max=2, weightChance=40},
                {name="GigamartTools", min=1, max=4, weightChance=60},
                {name="GigamartToys", min=0, max=2, weightChance=40},
                {name="StoreCounterTobacco", min=0, max=99, forceForTiles="location_shop_generic_01_28;location_shop_generic_01_29;location_shop_generic_01_30;location_shop_generic_01_31"},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
                {name="StoreShelfMechanics", min=1, max=2, weightChance=20},
                {name="StoreShelfMedical", min=1, max=2, weightChance=20},
            }
        }
    },

    generalstorestorage = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterTobacco", min=1, max=2, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="GigamartBakingMisc", min=0, max=99, weightChance=40},
                {name="GigamartCannedFood", min=0, max=99, weightChance=100},
                {name="GigamartDryGoods", min=0, max=99, weightChance=60},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeSnacks", min=1, max=2, weightChance=100},
                {name="FridgeSoda", min=1, max=4, weightChance=100},
                {name="FridgeWater", min=1, max=4, weightChance=100},
                {name="FridgeOther", min=1, max=2, weightChance=100},
            }
        },
        freezer = {
            rolls = 1,
            items = {

            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="GigamartBakingMisc", min=1, max=4, weightChance=60},
                {name="GigamartBedding", min=1, max=2, weightChance=40},
                {name="GigamartBottles", min=2, max=4, weightChance=60},
                {name="GigamartCandy", min=1, max=4, weightChance=60},
                {name="GigamartCannedFood", min=0, max=99, weightChance=20},
                {name="GigamartCrisps", min=1, max=4, weightChance=60},
                {name="GigamartDryGoods", min=1, max=4, weightChance=60},
                {name="GigamartFarming", min=1, max=4, weightChance=60},
                {name="GigamartHouseElectronics", min=1, max=2, weightChance=60},
                {name="GigamartHousewares", min=1, max=2, weightChance=60},
                {name="GigamartLightbulb", min=0, max=1, weightChance=20},
                {name="GigamartPots", min=1, max=2, weightChance=60},
                {name="GigamartSauce", min=1, max=2, weightChance=80},
                {name="GigamartSchool", min=0, max=2, weightChance=40},
                {name="GigamartTools", min=1, max=4, weightChance=60},
                {name="GigamartToys", min=0, max=2, weightChance=40},
                {name="StoreCounterTobacco", min=0, max=99, forceForTiles="location_shop_generic_01_28;location_shop_generic_01_29;location_shop_generic_01_30;location_shop_generic_01_31"},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
                {name="StoreShelfMechanics", min=1, max=2, weightChance=20},
                {name="StoreShelfMedical", min=1, max=2, weightChance=20},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GigamartTools", min=0, max=99, weightChance=100},
                {name="GigamartFarming", min=0, max=99, weightChance=100},
            }
        },

    },

    giftstorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="GigamartToys", min=1, max=99},
            }
        }
    },

    giftstore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="GigamartToys", min=0, max=99, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBagsFancy", min=0, max=1, weightChance=100},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="StoreDisplayWatches", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="GigamartToys", min=0, max=99, weightChance=100},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
    },

    gigamart = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="GigamartCrisps", min=0, max=99, weightChance=25},
                {name="GigamartCandy", min=0, max=99, weightChance=25},
                {name="GigamartCannedFood", min=0, max=99, weightChance=100},
                {name="GigamartSauce", min=0, max=99, weightChance=10},
            }
        },
        displaycasebakery = {
            procedural = true,
            procList = {
                {name="BakeryBread", min=1, max=99, weightChance=100},
                {name="BakeryPie", min=1, max=99, weightChance=100},
                {name="BakeryCake", min=1, max=99, weightChance=100},
                {name="BakeryMisc", min=0, max=99, weightChance=50},
            }
        },
        displaycasebutcher = {
            procedural = true,
            procList = {
                {name="ButcherChops", min=1, max=99, weightChance=100},
                {name="ButcherGround", min=1, max=99, weightChance=50},
                {name="ButcherChicken", min=1, max=99, weightChance=100},
                {name="ButcherSmoked", min=1, max=99, weightChance=50},
                {name="ButcherFish", min=1, max=99, weightChance=25},
            }
        },
        freezer = {
            rolls = 0,
            items = {

            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeSnacks", min=0, max=99, weightChance=20},
                {name="FridgeSoda", min=0, max=99, weightChance=20},
                {name="FridgeWater", min=0, max=99, weightChance=20},
                {name="FridgeOther", min=1, max=99, weightChance=100},
            }
        },
        grocerstand = {
            procedural = true,
            procList = {
                {name="GroceryStandVegetables1", min=1, max=99, weightChance=100},
                {name="GroceryStandVegetables2", min=1, max=99, weightChance=100},
                {name="GroceryStandFruits1", min=1, max=99, weightChance=100},
                {name="GroceryStandFruits2", min=1, max=99, weightChance=100},
                {name="GroceryStandFruits3", min=1, max=99, weightChance=100},
                {name="GroceryStandLettuce", min=1, max=99, weightChance=25},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="GigamartBottles", min=2, max=99, weightChance=20},
                {name="GigamartCrisps", min=2, max=99, weightChance=20},
                {name="GigamartCandy", min=1, max=99, weightChance=20},
                {name="GigamartBakingMisc", min=1, max=99, weightChance=20},
                {name="GigamartDryGoods", min=2, max=99, weightChance=100},
                {name="GigamartHousewares", min=1, max=99, weightChance=20},
                {name="GigamartCannedFood", min=2, max=99, weightChance=100},
                {name="GigamartSauce", min=1, max=99, weightChance=20},
                {name="GigamartToys", min=1, max=99, weightChance=20},
                {name="GigamartTools", min=1, max=99, weightChance=10},
                {name="GigamartSchool", min=1, max=99, weightChance=20},
                {name="GigamartBedding", min=1, max=99, weightChance=20},
                {name="GigamartPots", min=1, max=99, weightChance=20},
                {name="GigamartFarming", min=1, max=99, weightChance=10},
                {name="GigamartLightbulb", min=1, max=99, weightChance=10},
                {name="GigamartHouseElectronics", min=1, max=99, weightChance=10},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
        smallbox = {
            rolls = 1,
            items = {
                "GroceryBag1", 10,
                "GroceryBag2", 10,
                "GroceryBag3", 10,
                "GroceryBag4", 10,
            }
        },
    },

    gigamartkitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenBaking", min=1, max=2, weightChance=100},
                {name="StoreKitchenButcher", min=1, max=2, weightChance=100},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
            }
        },
        displaycasebutcher = {
            procedural = true,
            procList = {
                {name="ButcherChops", min=1, max=4, weightChance=100},
                {name="ButcherGround", min=1, max=2, weightChance=100},
                {name="ButcherChicken", min=1, max=1, weightChance=100},
                {name="ButcherSmoked", min=1, max=4, weightChance=100},
                {name="ButcherFish", min=0, max=1, weightChance=100},
            }
        },
    },

    grocery = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="GigamartCrisps", min=0, max=99, weightChance=25},
                {name="GigamartCandy", min=0, max=99, weightChance=25},
                {name="GigamartCannedFood", min=0, max=99, weightChance=100},
                {name="GigamartSauce", min=0, max=99, weightChance=10},
            }
        },
        displaycasebakery = {
            procedural = true,
            procList = {
                {name="BakeryBread", min=1, max=99, weightChance=100},
                {name="BakeryPie", min=1, max=99, weightChance=100},
                {name="BakeryCake", min=1, max=99, weightChance=100},
                {name="BakeryMisc", min=0, max=99, weightChance=50},
            }
        },
        displaycasebutcher = {
            procedural = true,
            procList = {
                {name="ButcherChops", min=1, max=99, weightChance=100},
                {name="ButcherGround", min=1, max=99, weightChance=50},
                {name="ButcherChicken", min=1, max=99, weightChance=100},
                {name="ButcherSmoked", min=1, max=99, weightChance=50},
                {name="ButcherFish", min=1, max=99, weightChance=25},
            }
        },
        freezer = {
            rolls = 0,
            items = {

            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeSnacks", min=0, max=99, weightChance=20},
                {name="FridgeSoda", min=0, max=99, weightChance=20},
                {name="FridgeWater", min=0, max=99, weightChance=20},
                {name="FridgeOther", min=1, max=99, weightChance=100},
            }
        },
        grocerstand = {
            procedural = true,
            procList = {
                {name="GroceryStandVegetables1", min=1, max=99, weightChance=100},
                {name="GroceryStandVegetables2", min=1, max=99, weightChance=100},
                {name="GroceryStandFruits1", min=1, max=99, weightChance=100},
                {name="GroceryStandFruits2", min=1, max=99, weightChance=100},
                {name="GroceryStandFruits3", min=1, max=99, weightChance=100},
                {name="GroceryStandLettuce", min=1, max=99, weightChance=25},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="GigamartBottles", min=2, max=99, weightChance=20},
                {name="GigamartCrisps", min=2, max=99, weightChance=20},
                {name="GigamartCandy", min=1, max=99, weightChance=20},
                {name="GigamartBakingMisc", min=1, max=99, weightChance=20},
                {name="GigamartDryGoods", min=2, max=99, weightChance=100},
                {name="GigamartCannedFood", min=2, max=99, weightChance=100},
                {name="GigamartSauce", min=1, max=99, weightChance=20},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
        smallcrate = {
            procedural = true,
            procList = {
                {name="GigamartCrisps", min=0, max=99, weightChance=25},
                {name="GigamartCandy", min=0, max=99, weightChance=25},
                {name="GigamartCannedFood", min=0, max=99, weightChance=100},
                {name="GigamartSauce", min=0, max=99, weightChance=10},
            }
        },
        smallbox = {
            rolls = 1,
            items = {
                "GroceryBag1", 10,
                "GroceryBag2", 10,
                "GroceryBag3", 10,
                "GroceryBag4", 10,
            }
        },
    },

    grocerystorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="GigamartBakingMisc", min=0, max=99, weightChance=100},
                {name="GigamartCannedFood", min=0, max=99, weightChance=100},
                {name="GigamartDryGoods", min=0, max=99, weightChance=100},
            }
        },
        freezer = {
            rolls = 0,
            items = {

            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeSoda", min=0, max=99, weightChance=100},
                {name="FridgeWater", min=0, max=99, weightChance=100},
                {name="FridgeOther", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GigamartBakingMisc", min=0, max=99, weightChance=100},
                {name="GigamartCannedFood", min=0, max=99, weightChance=100},
                {name="GigamartDryGoods", min=0, max=99, weightChance=100},
            }
        },
        smallbox = {
            rolls = 1,
            items = {
                "GroceryBag1", 10,
                "GroceryBag2", 10,
                "GroceryBag3", 10,
                "GroceryBag4", 10,
            }
        },
        smallcrate = {
            procedural = true,
            procList = {
                {name="GigamartBakingMisc", min=0, max=99, weightChance=100},
                {name="GigamartCannedFood", min=0, max=99, weightChance=100},
                {name="GigamartDryGoods", min=0, max=99, weightChance=100},
            }
        },
    },

    gunstore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="GunStoreCounter", min=0, max=99},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=10},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="GunStoreDisplayCase", min=0, max=99},
            },
            dontSpawnAmmo = true,
        },
        locker = {
            procedural = true,
            procList = {
                {name="GunStoreShelf", min=0, max=99, weightChance=20},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GunStoreShelf", min=0, max=99, weightChance=100},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=40},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="GunStoreShelf", min=0, max=99, weightChance=100},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=40},
            }
        },
        shelvesmag = {
            procedural = true,
            procList = {
                {name="GunStoreMagazineRack", min=0, max=99},
            }
        }
    },

    gunstorestorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="GunStoreAmmunition", min=0, max=99, weightChance=100},
                {name="GunStoreDisplayCase", min=0, max=99, weightChance=20},
            },
            dontSpawnAmmo = true,
        },
        locker = {
            procedural = true,
            procList = {
                {name="GunStoreShelf", min=0, max=99, weightChance=20},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=100},
            }
        },
        militarycrate = {
            procedural = true,
            procList = {
                {name="GunStoreAmmunition", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GunStoreShelf", min=0, max=99, weightChance=100},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=20},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="GunStoreShelf", min=0, max=99, weightChance=100},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=20},
            }
        }
    },

    gym = {
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GymWeights", min=0, max=99, forceForRooms="fitness;gym"},
            }
        },
    },

    hall = {
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="RandomFiller", min=0, max=99, weightChance=100},
            }
        },
        displaycasebakery = {
            procedural = true,
            procList = {
                {name="BakeryDonuts", min=0, max=99, forceForRooms="donut_kitchen"},
            }
        },
        laundrydryerbasic = {
            procedural = true,
            procList = {
                {name="GymTowels", min=0, max=99, forceForRooms="fitness;gym"},
            }
        },
        locker = {
            procedural = true,
            procList = {
                {name="SchoolLockers", min=0, max=99, forceForRooms="classroom"},
            }
        },
    },

    hospitalstorage = {
        isShop = true,
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MedicalStorageDrugs", min=0, max=6, weightChance=100},
                {name="MedicalStorageTools", min=0, max=4, weightChance=80},
                {name="MedicalStorageOutfit", min=0, max=2, weightChance=40},
            }
        }
    },

    housewarestore = {
        isShop = true,
        shelves = {
            procedural = true,
            procList = {
                {name="GigamartHousewares", min=1, max=12, weightChance=100},
                {name="GigamartBedding", min=0, max=2, weightChance=100},
                {name="GigamartPots", min=1, max=6, weightChance=100},
                {name="GigamartLightbulb", min=1, max=2, weightChance=100},
                {name="GigamartHouseElectronics", min=1, max=2, weightChance=100},
            }
        }
    },

    hunting = {
        isShop = true,
        clothingrack = {
            procedural = true,
            procList = {
                {name="CampingStoreClothes", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=10},
                {name="CampingStoreBooks", min=0, max=4, weightChance=80},
                {name="CampingStoreLegwear", min=0, max=2, weightChance=60},
                {name="CampingStoreBackpacks", min=0, max=2, weightChance=40},
                {name="CampingStoreGear", min=0, max=4, weightChance=100},
                {name="FishingStoreGear", min=0, max=2, weightChance=20},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="GunStoreDisplayCase", min=0, max=99},
            },
            dontSpawnAmmo = true,
        },
        locker = {
            procedural = true,
            procList = {
                {name="GunStoreDisplayCase", min=0, max=99, weightChance=20},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GunStoreShelf", min=0, max=99, weightChance=100},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=40},
            }
        },
        shelvesmag = {
            procedural = true,
            procList = {
                {name="GunStoreMagazineRack", min=0, max=99},
            }
        }
    },

    icecream = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenDishes", min=0, max=2, weightChance=100},
                {name="StoreKitchenPots", min=0, max=2, weightChance=100},
            }
        },
        displaycasebakery = {
            procedural = true,
            procList = {
                {name="CrateConesIceCream", min=0, max=99},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="IceCreamKitchenFreezer", min=0, max=99},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="IceCreamKitchenFreezer", min=0, max=99},
            }
        }
    },

    icecreamkitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateConesIceCream", min=0, max=99, weightChance=100},
            }
        },
        displaycasebakery = {
            procedural = true,
            procList = {
                {name="CrateConesIceCream", min=0, max=99, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FreezerIceCream", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="GroceryStandFruits1", min=0, max=99, weightChance=100},
                {name="GroceryStandFruits2", min=0, max=99, weightChance=100},
                {name="GroceryStandFruits3", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateConesIceCream", min=0, max=99, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="FreezerIceCream", min=0, max=99},
            }
        }
    },

    italiankitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="ItalianKitchenBaking", min=1, max=1, weightChance=100},
                {name="ItalianKitchenButcher", min=1, max=1, weightChance=100},
                {name="PizzaKitchenSauce", min=1, max=1, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenCutlery", min=0, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=1, weightChance=20},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateMarinara", min=0, max=2, weightChance=100},
                {name="CrateOilOlive", min=0, max=1, weightChance=100},
                {name="CratePasta", min=0, max=2, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="ItalianKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="ItalianKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateMarinara", min=0, max=2, weightChance=100},
                {name="CrateOilOlive", min=0, max=1, weightChance=100},
                {name="CratePasta", min=0, max=2, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayPizza", min=0, max=99},
            }
        },
    },

    italianrestaurant = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="ServingTrayPizza", min=0, max=99},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayPizza", min=0, max=99},
            }
        },
    },

    janitor = {
        metal_shelves = {
            procedural = true,
            procList = {
                {name="JanitorTools", min=1, max=1, weightChance=100},
                {name="JanitorCleaning", min=1, max=1, weightChance=100},
                {name="JanitorChemicals", min=0, max=99, weightChance=100},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="JanitorMisc", min=1, max=1, weightChance=100},
                {name="JanitorTools", min=0, max=1, weightChance=100},
                {name="JanitorCleaning", min=0, max=1, weightChance=100},
                {name="JanitorChemicals", min=0, max=99, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="JanitorChemicals", min=0, max=99},
            }
        }
    },

    jayschicken_dining = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="Empty", min=0, max=99, forceForTiles="location_shop_accessories_01_8;location_shop_accessories_01_9;location_shop_accessories_01_10;location_shop_accessories_01_11;location_shop_accessories_01_11;location_shop_accessories_01_12"},
                {name="JaysDiningCounter", min=0, max=99, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayChicken", min=1, max=99, weightChance=100},
                {name="ServingTrayChickenNuggets", min=1, max=99, weightChance=100},
                {name="ServingTrayCornbread", min=1, max=99, weightChance=60},
                {name="ServingTrayFries", min=1, max=99, weightChance=60},
                {name="ServingTrayGravy", min=1, max=99, weightChance=20},
            }
        },
    },

    jayschicken_kitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="JaysKitchenBags", min=0, max=1, weightChance=20},
                {name="JaysKitchenBaking", min=1, max=1, weightChance=100},
                {name="JaysKitchenButcher", min=1, max=1, weightChance=100},
                {name="JaysKitchenSauce", min=1, max=1, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenCups", min=0, max=1, weightChance=20},
                {name="StoreKitchenPotatoes", min=1, max=1, weightChance=100},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
                {name="StoreKitchenTrays", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateFountainCups", min=0, max=1, weightChance=60},
                {name="CrateNapkins", min=0, max=1, weightChance=60},
                {name="CratePaperBagSpiffos", min=0, max=1, weightChance=60},
                {name="CratePlasticTrays", min=0, max=1, weightChance=60},
                {name="CrateSpiffoMerch", min=0, max=1, weightChance=5},
                {name="FryFactoryPotatoes", min=0, max=4, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="JaysKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="JaysKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCornflour", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayChicken", min=1, max=4, weightChance=100},
                {name="ServingTrayChickenNuggets", min=1, max=99, weightChance=100},
                {name="ServingTrayCornbread", min=1, max=2, weightChance=60},
                {name="ServingTrayFries", min=1, max=4, weightChance=60},
                {name="ServingTrayGravy", min=1, max=2, weightChance=20},
            }
        },
    },

    jewelrystorage = {
        isShop = true,
        metal_shelves = {
            procedural = true,
            procList = {
                {name="JewelryStorageAll", min=0, max=99},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="JewelryStorageAll", min=0, max=99},
            }
        },
    },

    jewelrystore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="RandomFiller", min=0, max=99, weightChance=10},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=20},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="JewelryGems", min=0, max=99, weightChance=10},
                {name="JewelryGold", min=1, max=99, weightChance=100},
                {name="JewelrySilver", min=1, max=99, weightChance=100},
                {name="JewelryNavelRings", min=0, max=99, weightChance=20},
                {name="JewelryOthers", min=0, max=99, weightChance=40},
                {name="JewelryWeddingRings", min=1, max=99, weightChance=100},
                {name="JewelryWrist", min=0, max=99, weightChance=40},
            }
        },
    },

    kennels = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="CratePetSupplies", min=1, max=10, weightChance=100},
                {name="MedicalClinicTools", min=1, max=10, weightChance=80},
                {name="MedicalClinicDrugs", min=1, max=10, weightChance=70},
                {name="MedicalClinicOutfit", min=1, max=10, weightChance=20},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MedicalStorageDrugs", min=1, max=6, weightChance=100},
                {name="MedicalStorageTools", min=1, max=4, weightChance=100},
                {name="MedicalStorageOutfit", min=1, max=2, weightChance=100},
            }
        }
    },

    kitchen = {
        counter = {
            procedural = true,
            procList = {
                {name="KitchenBottles", min=0, max=1, weightChance=40},
                {name="KitchenBaking", min=0, max=1, weightChance=40},
                {name="KitchenBreakfast", min=0, max=1, weightChance=80},
                {name="KitchenCannedFood", min=1, max=1, weightChance=100},
                {name="KitchenDishes", min=1, max=1, weightChance=80},
                {name="KitchenDryFood", min=0, max=1, weightChance=100},
                {name="KitchenPots", min=1, max=1, weightChance=80},
                {name="KitchenRandom", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateBooks", min=0, max=1, weightChance=100},
                {name="CrateCannedFood", min=0, max=1, weightChance=60},
                {name="CrateCanning", min=0, max=1, weightChance=40},
                {name="CrateDishes", min=0, max=1, weightChance=80},
                {name="CrateMagazines", min=0, max=1, weightChance=100},
                {name="CrateNewspapers", min=0, max=1, weightChance=100},
                {name="CrateTailoring", min=0, max=1, weightChance=80},
                {name="CrateTools", min=0, max=1, weightChance=10},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FreezerGeneric", min=0, max=99, weightChance=100},
                {name="FreezerRich", min=0, max=99, forceForZones="Rich"},
                {name="FreezerTrailerPark", min=0, max=99, weightChance="TrailerPark"},
            }
        },
        overhead = {
            procedural = true,
            procList = {
                {name="KitchenBaking", min=0, max=1, weightChance=40},
                {name="KitchenBottles", min=0, max=1, weightChance=40},
                {name="KitchenBreakfast", min=0, max=1, weightChance=80},
                {name="KitchenCannedFood", min=0, max=1, weightChance=100},
                {name="KitchenDishes", min=1, max=1, weightChance=100},
                {name="KitchenDryFood", min=0, max=1, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="KitchenBook", min=0, max=99, forceForTiles="furniture_shelving_01_19;furniture_shelving_01_23;furniture_shelving_01_51;furniture_shelving_01_55"},
                {name="KitchenBottles", min=0, max=1, weightChance=40},
                {name="KitchenCannedFood", min=0, max=1, weightChance=100},
                {name="KitchenDishes", min=0, max=1, weightChance=80},
                {name="KitchenDryFood", min=0, max=1, weightChance=100},
            }
        }
    },

    kitchen_crepe = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="CrepeKitchenBaking", min=1, max=1, weightChance=100},
                {name="CrepeKitchenSauce", min=1, max=1, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenCafe", min=1, max=1, weightChance=100},
                {name="StoreKitchenCutlery", min=0, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=1, weightChance=20},
                {name="StoreKitchenPotatoes", min=1, max=1, weightChance=100},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateMapleSyrup", min=0, max=2, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CratePancakeMix", min=0, max=4, weightChance=100},
            }
        },
        freezer = {
            rolls = 1,
            items = {

            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="CrepeKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateMapleSyrup", min=0, max=2, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CratePancakeMix", min=0, max=4, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayOmelettes", min=1, max=2, weightChance=60},
                {name="ServingTrayPancakes", min=1, max=4, weightChance=100},
                {name="ServingTrayScrambledEggs", min=1, max=2, weightChance=60},
                {name="ServingTrayWaffles", min=1, max=4, weightChance=100},
            }
        },
    },

    kitchenwares = {
        isShop = true,
        shelves = {
            procedural = true,
            procList = {
                {name="GigamartHousewares", min=1, max=12, weightChance=100},
                {name="GigamartBedding", min=0, max=2, weightChance=100},
                {name="GigamartPots", min=1, max=6, weightChance=100},
                {name="GigamartLightbulb", min=1, max=2, weightChance=100},
                {name="GigamartHouseElectronics", min=1, max=2, weightChance=100},
            }
        }
    },

    knifefactory = {
        isShop = true,
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateSheetMetal", min=0, max=99, weightChance=60},
                {name="KnifeFactoryCutlery", min=0, max=99, weightChance=100},
                {name="KnifeFactoryMeatCleaver", min=0, max=99, weightChance=20},
                {name="KnifeFactoryKitchenKnife", min=0, max=99, weightChance=40},
            }
        },
        metal_shelf = {
            procedural = true,
            procList = {
                {name="CrateSheetMetal", min=0, max=99, weightChance=100},
                {name="KnifeFactoryMeatCleaver", min=0, max=99, weightChance=10},
                {name="KnifeFactoryKitchenKnife", min=0, max=99, weightChance=20},
            }
        },
    },

    knifeshipping = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="KnifeFactoryCutlery", min=0, max=99, weightChance=100},
                {name="KnifeFactoryMeatCleaver", min=0, max=99, weightChance=20},
                {name="KnifeFactoryKitchenKnife", min=0, max=99, weightChance=40},
            }
        },
    },

    knifestore = {
        isShop = true,
        shelves = {
            procedural = true,
            procList = {
                {name="KnifeFactoryCutlery", min=0, max=99, weightChance=100},
                {name="KnifeFactoryMeatCleaver", min=0, max=99, weightChance=20},
                {name="KnifeFactoryKitchenKnife", min=0, max=99, weightChance=40},
            }
        },
    },

    lasertag = {
        isShop = true,
        crate = {
            rolls = 1,
            items = {

            }
        }
    },

    laundry = {
        clothingdryer = {
            procedural = true,
            procList = {
                {name="GymLaundry", min=0, max=99, forceForRooms="fitness"},
                {name="Empty", min=0, max=99, weightChance=100},
                {name="LaundryLoad1", min=0, max=1, weightChance=10},
                {name="LaundryLoad2", min=0, max=1, weightChance=10},
                {name="LaundryLoad3", min=0, max=1, weightChance=10},
                {name="LaundryLoad4", min=0, max=1, weightChance=10},
                {name="LaundryLoad5", min=0, max=1, weightChance=10},
                {name="LaundryLoad6", min=0, max=1, weightChance=10},
                {name="LaundryLoad7", min=0, max=1, weightChance=10},
                {name="LaundryLoad8", min=0, max=1, weightChance=10},
            }
        },
        clothingdryerbasic = {
            procedural = true,
            procList = {
                {name="GymLaundry", min=0, max=99, forceForRooms="fitness"},
                {name="Empty", min=0, max=99, weightChance=100},
                {name="LaundryLoad1", min=0, max=1, weightChance=10},
                {name="LaundryLoad2", min=0, max=1, weightChance=10},
                {name="LaundryLoad3", min=0, max=1, weightChance=10},
                {name="LaundryLoad4", min=0, max=1, weightChance=10},
                {name="LaundryLoad5", min=0, max=1, weightChance=10},
                {name="LaundryLoad6", min=0, max=1, weightChance=10},
                {name="LaundryLoad7", min=0, max=1, weightChance=10},
                {name="LaundryLoad8", min=0, max=1, weightChance=10},
            }
        },
        clothingwasher = {
            procedural = true,
            procList = {
                {name="GymLaundry", min=0, max=99, forceForRooms="fitness"},
                {name="Empty", min=0, max=99, weightChance=100},
                {name="LaundryLoad1", min=0, max=1, weightChance=10},
                {name="LaundryLoad2", min=0, max=1, weightChance=10},
                {name="LaundryLoad3", min=0, max=1, weightChance=10},
                {name="LaundryLoad4", min=0, max=1, weightChance=10},
                {name="LaundryLoad5", min=0, max=1, weightChance=10},
                {name="LaundryLoad6", min=0, max=1, weightChance=10},
                {name="LaundryLoad7", min=0, max=1, weightChance=10},
                {name="LaundryLoad8", min=0, max=1, weightChance=10},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="LaundryCleaning", min=0, max=99, weightChance=100},
                {name="LaundryLoad1", min=0, max=1, weightChance=10},
                {name="LaundryLoad2", min=0, max=1, weightChance=10},
                {name="LaundryLoad3", min=0, max=1, weightChance=10},
                {name="LaundryLoad4", min=0, max=1, weightChance=10},
                {name="LaundryLoad5", min=0, max=1, weightChance=10},
                {name="LaundryLoad6", min=0, max=1, weightChance=10},
            }
        },
        locker = {
            procedural = true,
            procList = {
                {name="FactoryLockers", min=0, max=99, forceForRooms="batteryfactory"},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="LaundryCleaning", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="LaundryCleaning", min=0, max=99},
            }
        },
    },

    leatherclothesstore = {
        isShop = true,
        displaycase = {
            procedural = true,
            procList = {
                {name="StoreDisplayWatches", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ClothingStoresBoots", min=0, max=99, weightChance=60},
                {name="ClothingStoresGlovesLeather", min=0, max=99, weightChance=10},
                {name="ClothingStoresPantsLeather", min=0, max=99, weightChance=100},
                {name="ClothingStoresShoesLeather", min=0, max=99, weightChance=100},
            }
        },
        clothingrack = {
            procedural = true,
            procList = {
                {name="ClothingStoresJacketsLeather", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="ClothingStoresGlovesLeather", min=0, max=4, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBagsFancy", min=0, max=1, weightChance=100},
            }
        }
    },

    library = {
        shelves = {
            procedural = true,
            procList = {
                {name="LibraryBooks", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="LibraryCounter", min=0, max=99},
            }
        }
    },

    liquorstore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterBags", min=0, max=1, weightChance=20},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterTobacco", min=0, max=2, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="StoreShelfWhiskey", min=0, max=99, weightChance=40},
                {name="StoreShelfWine", min=0, max=99, weightChance=60},
                {name="StoreShelfBeer", min=0, max=99, weightChance=100},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeBeer", min=0, max=99},
            }
        },
        freezer = {
            rolls = 0,
            items = {

            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="StoreShelfWhiskey", min=0, max=99, weightChance=40},
                {name="StoreShelfWine", min=0, max=99, weightChance=60},
                {name="StoreShelfBeer", min=0, max=99, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="StoreShelfCombo", min=0, max=99, weightChance=100, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
                {name="StoreShelfWhiskey", min=0, max=99, weightChance=40},
                {name="StoreShelfWine", min=0, max=99, weightChance=60},
                {name="StoreShelfBeer", min=0, max=99, weightChance=100},
            }
        },
        smallbox = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99},
            }
        },
    },

    livingroom = {
        counter = {
            procedural = true,
            procList = {
                {name="KitchenDishes", min=1, max=1, weightChance=100},
                {name="KitchenPots", min=1, max=1, weightChance=100},
                {name="KitchenCannedFood", min=1, max=1, weightChance=100},
                {name="KitchenDryFood", min=0, max=1, weightChance=100},
                {name="KitchenBreakfast", min=0, max=1, weightChance=100},
                {name="KitchenBottles", min=0, max=1, weightChance=100},
                {name="KitchenRandom", min=0, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateBooks", min=0, max=1, weightChance=100},
                {name="CrateComics", min=0, max=1, weightChance=100},
                {name="CrateCompactDisks", min=0, max=1, weightChance=100},
                {name="CrateElectronics", min=0, max=1, weightChance=100},
                {name="CrateMagazines", min=0, max=1, weightChance=100},
                {name="CrateNewspapers", min=0, max=1, weightChance=100},
                {name="CrateVHSTapes", min=0, max=1, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FreezerGeneric", min=0, max=99, weightChance=100},
                {name="FreezerRich", min=0, max=99, forceForZones="Rich"},
                {name="FreezerTrailerPark", min=0, max=99, weightChance="TrailerPark"},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="LivingRoomShelfNoTapes", min=0, max=99, weightChance=100},
                {name="LivingRoomShelf", min=0, max=99, forceForItems="appliances_television_01_0;appliances_television_01_1;appliances_television_01_2;appliances_television_01_3;appliances_television_01_4;appliances_television_01_5;appliances_television_01_6;appliances_television_01_7"},
            }
        },
        overhead = {
            procedural = true,
            procList = {
                {name="KitchenDishes", min=1, max=1, weightChance=100},
                {name="KitchenCannedFood", min=1, max=1, weightChance=100},
                {name="KitchenDryFood", min=0, max=1, weightChance=100},
                {name="KitchenBreakfast", min=0, max=1, weightChance=100},
                {name="KitchenBottles", min=0, max=1, weightChance=100},
                {name="KitchenBook", min=0, max=1, weightChance=100},
            }
        }
    },

    lobby = {
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="RandomFiller", min=0, max=99, weightChance=100},
            }
        }
    },

    loggingfactory = {
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCarpentry", min=0, max=99, weightChance=40},
                {name="CrateLumber", min=0, max=99, weightChance=100},
                {name="CrateTools", min=0, max=99, weightChance=20},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCarpentry", min=0, max=99, weightChance=100},
                {name="CrateTools", min=0, max=99, weightChance=100},
            }
        },
    },

    loggingtruck = {
        crate = {
            procedural = true,
            procList = {
                {name="CrateLumber", min=0, max=99, weightChance=100},
            }
        }
    },

    mechanic = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CarBrakesModern1", min=0, max=1, weightChance=60},
                {name="CarBrakesModern2", min=0, max=1, weightChance=40},
                {name="CarBrakesModern3", min=0, max=1, weightChance=20},
                {name="CarBrakesNormal1", min=0, max=1, weightChance=100},
                {name="CarBrakesNormal2", min=0, max=1, weightChance=80},
                {name="CarBrakesNormal3", min=0, max=1, weightChance=60},
                {name="CarSuspensionModern1", min=0, max=1, weightChance=60},
                {name="CarSuspensionModern2", min=0, max=1, weightChance=40},
                {name="CarSuspensionModern3", min=0, max=1, weightChance=20},
                {name="CarSuspensionNormal1", min=0, max=1, weightChance=100},
                {name="CarSuspensionNormal2", min=0, max=1, weightChance=80},
                {name="CarSuspensionNormal3", min=0, max=1, weightChance=60},
                {name="CarTiresModern1", min=0, max=1, weightChance=60},
                {name="CarTiresModern2", min=0, max=1, weightChance=40},
                {name="CarTiresModern3", min=0, max=1, weightChance=20},
                {name="CarTiresNormal1", min=0, max=1, weightChance=100},
                {name="CarTiresNormal2", min=0, max=1, weightChance=80},
                {name="CarTiresNormal3", min=0, max=1, weightChance=60},
                {name="CarWindows1", min=0, max=1, weightChance=100},
                {name="CarWindows2", min=0, max=1, weightChance=80},
                {name="CarWindows3", min=0, max=1, weightChance=60},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MechanicShelfBooks", min=0, max=1, weightChance=20},
                {name="MechanicShelfBrakes", min=0, max=1, weightChance=60},
                {name="MechanicShelfElectric", min=0, max=1, weightChance=60},
                {name="MechanicShelfMufflers", min=0, max=1, weightChance=60},
                {name="MechanicShelfOutfit", min=0, max=1, weightChance=40},
                {name="MechanicShelfSuspension", min=0, max=1, weightChance=60},
                {name="MechanicShelfTools", min=1, max=2, weightChance=100},
                {name="MechanicShelfWheels", min=0, max=2, weightChance=80},
            }
        },
        wardrobe = {
            procedural = true,
            procList = {
                {name="MechanicShelfBooks", min=0, max=1, weightChance=20},
                {name="MechanicShelfMisc", min=0, max=1, weightChance=40},
                {name="MechanicShelfOutfit", min=1, max=2, weightChance=100},
            }
        },
    },

    medical = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="MedicalClinicDrugs", min=1, max=99, weightChance=100},
                {name="MedicalClinicTools", min=1, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MedicalStorageDrugs", min=1, max=99, weightChance=100},
                {name="MedicalStorageTools", min=1, max=99, weightChance=100},
                {name="MedicalStorageOutfit", min=0, max=99, weightChance=100},
            }
        },
        wardrobe = {
            procedural = true,
            procList = {
                {name="MedicalStorageOutfit", min=0, max=99, weightChance=100},
            }
        },
    },

    medicalstorage = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="MedicalClinicDrugs", min=1, max=4, weightChance=100},
                {name="MedicalClinicTools", min=1, max=2, weightChance=100},
                {name="MedicalClinicOutfit", min=1, max=2, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MedicalStorageDrugs", min=1, max=6, weightChance=100},
                {name="MedicalStorageTools", min=1, max=4, weightChance=100},
                {name="MedicalStorageOutfit", min=1, max=2, weightChance=100},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeWater", min=0, max=12},
            }
        },
        freezer = {
            rolls = 0,
            items = {

            }
        },
    },

    metalshipping = {
        crate = {
            procedural = true,
            procList = {
                {name="CrateSheetMetal", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MetalShopTools", min=0, max=99, weightChance=80},
                {name="CrateSheetMetal", min=0, max=99, weightChance=100},
            }
        },
    },

    metalshop = {
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            rolls = 1,
            items = {

            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MetalShopTools", min=0, max=99, weightChance=80},
                {name="CrateSheetMetal", min=0, max=99, weightChance=100},
            }
        },
    },

    mexicankitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="MexicanKitchenBaking", min=1, max=1, weightChance=100},
                {name="MexicanKitchenButcher", min=1, max=1, weightChance=100},
                {name="MexicanKitchenSauce", min=1, max=1, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenBags", min=0, max=1, weightChance=20},
                {name="StoreKitchenCups", min=0, max=1, weightChance=20},
                {name="StoreKitchenPots", min=0, max=99, weightChance=20},
                {name="StoreKitchenTrays", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateHotsauce", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateTacoShells", min=0, max=2, weightChance=100},
                {name="CrateTortillaChips", min=0, max=2, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="MexicanKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="MexicanKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateHotsauce", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateTacoShells", min=0, max=2, weightChance=100},
                {name="CrateTortillaChips", min=0, max=2, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayTacos", min=1, max=4, weightChance=100},
                {name="ServingTrayBurritos", min=1, max=4, weightChance=100},
                {name="ServingTrayRefriedBeans", min=1, max=2, weightChance=20},
            }
        },
    },

    motelroom = {
        bin = {
            rolls = 0,
            items = {

            },
        },
        dresser = {
            rolls = 0,
            items = {

            },
        },
        freezer = {
            rolls = 0,
            items = {

            },
        },
        fridge = {
            rolls = 0,
            items = {

            },
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MotelLinens", min=0, max=99, weightChance=100},
                {name="MotelTowels", min=0, max=99, weightChance=100},
            }
        },
        sidetable = {
            rolls = 1,
            items = {
                "Book", 200,
            },
        },
        wardrobe = {
            procedural = true,
            procList = {
                {name="MotelLinens", min=0, max=1, weightChance=100},
                {name="MotelTowels", min=0, max=1, weightChance=100},
            }
        },
    },

    motelroomoccupied = {
        bin = {
            procedural = true,
            procList = {
                {name="BinGeneric", min=0, max=99},
            }
        },
        dresser = {
            rolls = 1,
            items = {
                "Bag_DuffelBagTINT", 0.5,
                "Bag_Schoolbag", 0.5,
                "Bag_NormalHikingBag", 0.2,
                "Bag_BigHikingBag", 0.2,
            }
        },
        freezer = {
            rolls = 1,
            items = {
                "IcePick", 0.01,
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="MotelFridge", min=1, max=1},
            }
        },
        sidetable = {
            rolls = 1,
            items = {
                "Book", 200,
                "Earbuds", 2,
                "Comb", 2,
                "Magazine", 2,
                "Newspaper", 2,
                "Notebook", 2,
                "ComicBook", 2,
                "Pencil", 2,
                "Pen", 2,
                "BluePen", 1,
                "RedPen", 1,
                "Pills", 1,
                "PillsBeta", 1,
                "PillsAntiDep", 1,
                "PillsVitamins", 1,
            }
        },
        wardrobe = {
            procedural = true,
            procList = {
                {name="MotelLinens", min=1, max=1, weightChance=100},
                {name="MotelTowels", min=1, max=1, weightChance=100},
            }
        },

    },

    movierental = {
        isShop = true,
        clothingrack = {
            procedural = true,
            procList = {
                {name="MovieRentalShelves", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateVHSTapes", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateVHSTapes", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="MovieRentalShelves", min=0, max=99},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
    },

    musicstore = {
        isShop = true,
        shelves = {
            procedural = true,
            procList = {
                {name="MusicStoreCDs", min=1, max=4, weightChance=100},
                {name="MusicStoreCases", min=1, max=2, weightChance=100},
                {name="MusicStoreAcoustic", min=1, max=6, weightChance=100},
                {name="MusicStoreBass", min=1, max=2, weightChance=100},
                {name="MusicStoreOthers", min=1, max=4, weightChance=100},
                {name="MusicStoreSpeaker", min=1, max=6, weightChance=100},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        }
    },

    newspaperprint = {
        crate = {
            procedural = true,
            procList = {
                {name="CrateNewspapers", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural= true,
            procList = {
                {name="CrateNewspapers", min=0, max=99},
            }
        },
    },

    newspapershipping = {
        crate = {
            procedural = true,
            procList = {
                {name="CrateNewspapers", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural= true,
            procList = {
                {name="CrateNewspapers", min=0, max=99},
            }
        },
    },

    newspaperstorage = {
        crate = {
            procedural = true,
            procList = {
                {name="CrateNewspapers", min=0, max=99},
            }
        },
    },

    office = {
        crate = {
            procedural = true,
            procList = {
                {name="CrateOfficeSupplies", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="OfficeCounter", min=0, max=99},
            }
        },
        desk = {
            procedural = true,
            procList = {
                {name="OfficeDesk", min=0, max=99, weightChance=100},
                {name="PoliceDesk", min=0, max=99, forceForRooms="policestorage"},
            }
        },
        freezer = {
            rolls = 1,
            items = {
                
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="OfficeFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural= true,
            procList = {
                {name="OfficeShelfSupplies", min=0, max=99},
            }
        },
    },

    officestorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CrateOfficeSupplies", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural= true,
            procList = {
                {name="OfficeShelfSupplies", min=0, max=99},
            }
        }
    },

    optometrist = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBagsFancy", min=0, max=1, weightChance=100},
                {name="OptometristGlasses", min=0, max=99, weightChance=20},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="OptometristGlasses", min=0, max=99, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="OptometristGlasses", min=0, max=99, weightChance=100},
            }
        },
    },

    paintershop = {
        crate = {
            procedural = true,
            procList = {
                {name="CratePaint", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CratePaint", min=0, max=99},
            }
        },
        overhead = {
            rolls = 1,
            items = {

            }
        },
    },

    pawnshop = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="ArmyStorageElectronics", min=0, max=1, weightChance=20},
                {name="ArmyStorageOutfit", min=0, max=1, weightChance=40},
                {name="ArmySurplusBackpacks", min=0, max=1, weightChance=40},
                {name="ArmySurplusTools", min=0, max=1, weightChance=20},
                {name="ElectronicsStoreMusic", min=0, max=1, weightChance=40},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="ToolStoreTools", min=0, max=99, weightChance=10},
            }
        },
        clothingrack = {
            procedural = true,
            procList = {
                {name="ArmySurplusOutfit", min=0, max=4, weightChance=100},
                {name="ClothingStorageAllJackets", min=0, max=99, weightChance=10},
                {name="ClothingStorageAllShirts", min=0, max=99, weightChance=10},
                {name="ClothingStorageLegwear", min=0, max=99, weightChance=10},
                {name="ClothingStoresDress", min=0, max=2, weightChance=40},
                {name="ClothingStoresJacketsFormal", min=0, max=2, weightChance=40},
                {name="ClothingStoresPantsFormal", min=0, max=2, weightChance=40},
                {name="ClothingStoresShirtsFormal", min=0, max=2, weightChance=40},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="ArmySurplusFootwear", min=0, max=1, weightChance=100},
                {name="ArmySurplusOutfit", min=0, max=1, weightChance=100},
                {name="BookstoreBooks", min=0, max=99, weightChance=100},
                {name="CampingStoreGear", min=0, max=1, weightChance=100},
                {name="ClothingStorageAllJackets", min=0, max=1, weightChance=100},
                {name="ClothingStorageAllShirts", min=0, max=1, weightChance=100},
                {name="ClothingStorageLegwear", min=0, max=1, weightChance=100},
                {name="ClothingStorageWinter", min=0, max=1, weightChance=100},
                {name="ClothingStoresDress", min=0, max=1, weightChance=100},
                {name="ClothingStoresJacketsFormal", min=0, max=1, weightChance=100},
                {name="ClothingStoresPantsFormal", min=0, max=1, weightChance=100},
                {name="ClothingStoresShirtsFormal", min=0, max=1, weightChance=100},
                {name="CrateCompactDiscs", min=0, max=99, weightChance=100},
                {name="CrateElectronics", min=0, max=99, weightChance=100},
                {name="CrateVHSTapes", min=0, max=99, weightChance=100},
                {name="ToolStoreTools", min=0, max=99, weightChance=100},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="DepartmentStoreWatches", min=0, max=2, weightChance=100},
                {name="JewelryGold", min=0, max=1, weightChance=80},
                {name="JewelrySilver", min=0, max=1, weightChance=80},
                {name="JewelryWeddingRings", min=0, max=1, weightChance=80},
                {name="OptometristGlasses", min=0, max=1, weightChance=80},
                {name="PawnShopGuns", min=1, max=99, weightChance=10},
                {name="PawnShopKnives", min=1, max=99, weightChance=10},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ArmyStorageElectronics", min=0, max=1, weightChance=20},
                {name="ArmyStorageOutfit", min=0, max=1, weightChance=10},
                {name="ArmySurplusBackpacks", min=0, max=1, weightChance=80},
                {name="ArmySurplusFootwear", min=0, max=1, weightChance=80},
                {name="ArmySurplusOutfit", min=0, max=1, weightChance=20},
                {name="CampingStoreGear", min=0, max=1, weightChance=20},
                {name="ClothingStorageWinter", min=0, max=1, weightChance=40},
                {name="PawnShopCases", min=0, max=1, weightChance=40},
                {name="ToolStoreTools", min=0, max=99, weightChance=10},
                {name="ClothingStorageAllJackets", min=0, max=99, weightChance=10},
                {name="ClothingStorageAllShirts", min=0, max=99, weightChance=10},
                {name="ClothingStorageLegwear", min=0, max=99, weightChance=10},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ArmyStorageElectronics", min=0, max=1, weightChance=20},
                {name="ArmyStorageOutfit", min=0, max=1, weightChance=20},
                {name="ArmySurplusBackpacks", min=0, max=1, weightChance=80},
                {name="ArmySurplusFootwear", min=0, max=1, weightChance=80},
                {name="ArmySurplusOutfit", min=0, max=1, weightChance=80},
                {name="BookstoreBooks", min=0, max=99, forceForTiles="furniture_shelving_01_40;furniture_shelving_01_41;furniture_shelving_01_42;furniture_shelving_01_43"},
                {name="CampingStoreGear", min=0, max=1, weightChance=20},
                {name="ClothingStorageWinter", min=0, max=1, weightChance=40},
                {name="ElectronicsStoreMisc", min=0, max=99, weightChance=10},
                {name="MovieRentalShelves", min=0, max=99, weightChance=10},
                {name="MusicStoreBass", min=0, max=1, weightChance=40},
                {name="MusicStoreCDs", min=0, max=99, weightChance=10},
                {name="MusicStoreGuitar", min=0, max=1, weightChance=40},
                {name="MusicStoreOthers", min=0, max=1, weightChance=40},
                {name="PawnShopCases", min=0, max=1, weightChance=40},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
                {name="ToolStoreTools", min=0, max=99, weightChance=10},
            }
        }
    },

    pawnshopcooking = {
        all = {
            rolls = 0,
            items = {

            }
        },
    },

    pawnshopoffice = {
        crate = {
            procedural = true,
            procList = {
                {name="ArmyStorageElectronics", min=0, max=1, weightChance=100},
                {name="ArmyStorageOutfit", min=0, max=1, weightChance=100},
                {name="ArmySurplusTools", min=0, max=1, weightChance=100},
                {name="CrateCompactDiscs", min=0, max=99, weightChance=100},
                {name="CrateElectronics", min=0, max=99, weightChance=100},
                {name="CrateVHSTapes", min=0, max=99, weightChance=100},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=100},
                {name="GunStoreGuns", min=0, max=99, weightChance=100},
                {name="JewelryStorageAll", min=0, max=99, weightChance=100},
                {name="PoliceStorageOutfit", min=0, max=1, weightChance=100},
            }
        },
        locker = {
            procedural = true,
            procList = {
                {name="ArmyStorageOutfit", min=0, max=99, weightChance=100},
                {name="PawnShopGunsSpecial", min=0, max=99, forceForTiles="furniture_storage_02_8;furniture_storage_02_9;furniture_storage_02_10;furniture_storage_02_11"},
                {name="PoliceStorageOutfit", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ArmyStorageElectronics", min=0, max=1, weightChance=100},
                {name="ArmyStorageOutfit", min=0, max=99, weightChance=100},
                {name="ArmySurplusTools", min=0, max=1, weightChance=100},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=100},
                {name="GunStoreGuns", min=0, max=99, weightChance=100},
                {name="PoliceStorageOutfit", min=0, max=1, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="BookstoreBooks", min=0, max=99, forceForTiles="furniture_shelving_01_40;furniture_shelving_01_41;furniture_shelving_01_42;furniture_shelving_01_43"},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=100},
                {name="GunStoreGuns", min=0, max=99, weightChance=100},
            }
        }
    },

    pawnshopstorage = {
        crate = {
            procedural = true,
            procList = {
                {name="ArmyStorageElectronics", min=0, max=1, weightChance=100},
                {name="ArmyStorageOutfit", min=0, max=1, weightChance=100},
                {name="ArmySurplusTools", min=0, max=1, weightChance=100},
                {name="CrateElectronics", min=0, max=99, weightChance=100},
                {name="CrateInstruments", min=0, max=99, weightChance=100},
                {name="CrateVHSTapes", min=0, max=99, weightChance=100},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=100},
                {name="ToolStoreTools", min=0, max=99, weightChance=100},
            }
        },
        locker = {
            procedural = true,
            procList = {
                {name="ArmyStorageOutfit", min=0, max=99, weightChance=100},
                {name="PawnShopGunsSpecial", min=0, max=99, forceForTiles="furniture_storage_02_8;furniture_storage_02_9;furniture_storage_02_10;furniture_storage_02_11"},
                {name="PoliceStorageOutfit", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ArmyStorageElectronics", min=0, max=1, weightChance=100},
                {name="ArmyStorageOutfit", min=0, max=1, weightChance=100},
                {name="ArmySurplusTools", min=0, max=1, weightChance=100},
                {name="CrateElectronics", min=0, max=99, weightChance=100},
                {name="CrateInstruments", min=0, max=99, weightChance=100},
                {name="CrateVHSTapes", min=0, max=99, weightChance=100},
                {name="GunStoreAmmunition", min=0, max=99, weightChance=100},
                {name="ToolStoreTools", min=0, max=99, weightChance=100},
            }
        },
    },

    pharmacy = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="MedicalClinicDrugs", min=0, max=99, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=10},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeSnacks", min=0, max=2, weightChance=100},
                {name="FridgeSoda", min=0, max=6, weightChance=100},
                {name="FridgeWater", min=0, max=4, weightChance=100},
                {name="FridgeOther", min=0, max=2, weightChance=100},
            }
        },
        freezer = {
            rolls = 0,
            items = {

            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MedicalStorageDrugs", min=1, max=6, weightChance=100},
                {name="MedicalStorageTools", min=1, max=4, weightChance=100},
                {name="MedicalStorageOutfit", min=1, max=2, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="PharmacyCosmetics", min=1, max=4, weightChance=60},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
                {name="StoreShelfDrinks", min=1, max=4, weightChance=40},
                {name="StoreShelfSnacks", min=1, max=4, weightChance=40},
                {name="StoreShelfMedical", min=4, max=24, weightChance=100},
            }
        },
    },

    pharmacystorage = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="MedicalClinicDrugs", min=1, max=4, weightChance=100},
                {name="MedicalClinicTools", min=1, max=2, weightChance=100},
                {name="MedicalClinicOutfit", min=1, max=2, weightChance=100},
            }
        },
        freezer = {
            rolls = 1,
            items = {

            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeWater", min=0, max=12},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MedicalStorageDrugs", min=1, max=6, weightChance=100},
                {name="MedicalStorageTools", min=1, max=4, weightChance=100},
                {name="MedicalStorageOutfit", min=1, max=2, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="MedicalStorageDrugs", min=1, max=6, weightChance=100},
                {name="MedicalStorageTools", min=1, max=4, weightChance=100},
                {name="MedicalStorageOutfit", min=1, max=2, weightChance=100},
            }
        },
    },

    photoroom = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="CrateCameraFilm", min=0, max=99, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="CrateCameraFilm", min=0, max=99, weightChance=100},
            }
        },
    },

    picnic = {
        crate = {
            rolls = 1,
            items = {

            }
        },
    },

    pileocrepe = {
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayPancakes", min=1, max=99, weightChance=100},
                {name="ServingTrayWaffles", min=1, max=99, weightChance=100},
                {name="ServingTrayOmelettes", min=1, max=99, weightChance=60},
                {name="ServingTrayScrambledEggs", min=1, max=99, weightChance=60},
            }
        },
    },

    pizzakitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="PizzaKitchenBaking", min=1, max=1, weightChance=100},
                {name="PizzaKitchenButcher", min=1, max=1, weightChance=100},
                {name="PizzaKitchenCheese", min=1, max=1, weightChance=100},
                {name="PizzaKitchenSauce", min=1, max=1, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenBags", min=0, max=1, weightChance=20},
                {name="StoreKitchenCups", min=0, max=1, weightChance=20},
                {name="StoreKitchenTrays", min=0, max=1, weightChance=20},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateMarinara", min=0, max=2, weightChance=100},
                {name="CrateOilOlive", min=0, max=1, weightChance=100},
                {name="CratePasta", min=0, max=2, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="ServingTrayPizza", min=0, max=99},
            }
        },
        freezer = {
            rolls = 0,
            items = {

            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="PizzaKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateMarinara", min=0, max=2, weightChance=100},
                {name="CrateOilOlive", min=0, max=1, weightChance=100},
                {name="CratePasta", min=0, max=2, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayPizza", min=0, max=99},
            }
        },
        wardrobe = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateMarinara", min=0, max=2, weightChance=100},
                {name="CrateOilOlive", min=0, max=1, weightChance=100},
                {name="CratePasta", min=0, max=2, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
            }
        },
    },

    pizzawhirled = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="ServingTrayPizza", min=0, max=99},
            }
        },
        wardrobe = {
            rolls = 1,
            items = {

            }
        },
    },

    plazastore1 = {
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBags", min=0, max=1, weightChance=100},
                {name="RandomFiller", min=0, max=99, weightChance=100},
            }
        }
    },

    policestorage = {
        crate = {
            procedural = true,
            procList = {
                {name="PoliceStorageAmmunition", min=0, max=99},
            }
        },
        locker = {
            procedural = true,
            procList = {
                {name="PoliceStorageGuns", min=0, max=99, forceForTiles="furniture_storage_02_8;furniture_storage_02_9;furniture_storage_02_10;furniture_storage_02_11"},
                {name="PoliceStorageOutfit", min=0, max=99, forceForTiles="furniture_storage_02_4;furniture_storage_02_5;furniture_storage_02_6;furniture_storage_02_7"},
            },
            dontSpawnAmmo = true,
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="PoliceStorageGuns", min=0, max=99, weightChance=100},
            },
            dontSpawnAmmo = true,
        }
    },
    
    pool = {
        locker = {
            procedural = true,
            procList = {
                {name="PoolLockers", min=0, max=99, weightChance=100},
            }
        },
    },
    
    post = {
        counter = {
            procedural = true,
            procList = {
                {name="PostOfficeSupplies", min=1, max=99, weightChance=100},
                {name="PostOfficeBoxes", min=1, max=4, weightChance=100},
            }
        },
    },

    poststorage = {
        metal_shelves = {
            procedural = true,
            procList = {
                {name="PostOfficeBoxes", min=1, max=2, weightChance=100},
                {name="PostOfficeNewspapers", min=0, max=2, weightChance=100},
                {name="PostOfficeMagazines", min=0, max=2, weightChance=100},
                {name="PostOfficeBooks", min=0, max=99, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="PostOfficeNewspapers", min=0, max=2, weightChance=100},
                {name="PostOfficeMagazines", min=0, max=2, weightChance=100},
                {name="PostOfficeBooks", min=0, max=99, weightChance=100},
            }
        },
    },

    potatostorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="FryFactoryPotatoes", min=0, max=99, weightChance=100},
            }
        },
        laundrydryerbasic = {
            procedural = true,
            procList = {
                {name="FryFactoryPotatoes", min=0, max=99, weightChance=100},
            }
        },
    },

    prisoncells = {
        wardrobe = {
            procedural = true,
            procList = {
                {name="PrisonCellRandom", min=0, max=99},
            }
        }
    },

    producestorage = {
        isShop = true,
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="ProduceStorageApples", min=0, max=99, weightChance=80},
                {name="ProduceStorageBellPeppers", min=0, max=99, weightChance=100},
                {name="ProduceStorageBroccoli", min=0, max=99, weightChance=100},
                {name="ProduceStorageCabbages", min=0, max=99, weightChance=100},
                {name="ProduceStorageCarrots", min=0, max=99, weightChance=100},
                {name="ProduceStorageCherries", min=0, max=99, weightChance=80},
                {name="ProduceStorageCorn", min=0, max=99, weightChance=100},
                {name="ProduceStorageEggplant", min=0, max=99, weightChance=100},
                {name="ProduceStorageGrapes", min=0, max=99, weightChance=80},
                {name="ProduceStorageLeeks", min=0, max=99, weightChance=100},
                {name="ProduceStorageLettuce", min=0, max=99, weightChance=100},
                {name="ProduceStorageOnions", min=0, max=99, weightChance=100},
                {name="ProduceStoragePeaches", min=0, max=99, weightChance=100},
                {name="ProduceStoragePears", min=0, max=99, weightChance=80},
                {name="ProduceStoragePotatoes", min=0, max=99, weightChance=100},
                {name="ProduceStorageRadishes", min=0, max=99, weightChance=80},
                {name="ProduceStorageStrawberries", min=0, max=99, weightChance=80},
                {name="ProduceStorageTomatoes", min=0, max=99, weightChance=100},
                {name="ProduceStorageWatermelons", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GardenStoreTools", min=0, max=99, weightChance=100},
                {name="GardenStoreMisc", min=0, max=99, weightChance=100},
            }
        },
    },

    radiofactory = {
        isShop = true,
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="RadioFactoryComponents", min=0, max=99},
            },
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="RadioFactoryComponents", min=0, max=99},
            },
        },
    },

    radioshipping = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="RadioFactoryComponents", min=0, max=99},
            },
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="RadioFactoryComponents", min=0, max=99},
            },
        },
    },

    radiostorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="RadioFactoryComponents", min=0, max=99},
            },
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="RadioFactoryComponents", min=0, max=99},
            },
        },
    },

    restaurant = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="BarCounterLiquor", min=0, max=99, forceForTiles="location_restaurant_bar_01_0;location_restaurant_bar_01_1;location_restaurant_bar_01_2;location_restaurant_bar_01_3;location_restaurant_bar_01_4;location_restaurant_bar_01_5;location_restaurant_bar_01_6;location_restaurant_bar_01_7;location_restaurant_bar_01_16;location_restaurant_bar_01_17;location_restaurant_bar_01_18;location_restaurant_bar_01_19;location_restaurant_bar_01_20;location_restaurant_bar_01_21;location_restaurant_bar_01_22;location_restaurant_bar_01_23;location_restaurant_bar_01_56;location_restaurant_bar_01_57;location_restaurant_bar_01_58;location_restaurant_bar_01_59;location_restaurant_bar_01_60;location_restaurant_bar_01_61;location_restaurant_bar_01_62;location_restaurant_bar_01_63"},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="ServingTrayPizza", min=0, max=99, forceForRooms="pizzakitchen"},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="BakeryDoughnuts", min=0, max=1, weightChance=20},
                {name="ServingTrayBurgers", min=1, max=2, weightChance=100},
                {name="ServingTrayBurritos", min=0, max=1, weightChance=20},
                {name="ServingTrayChickenNuggets", min=0, max=1, weightChance=80},
                {name="ServingTrayFries", min=1, max=2, weightChance=60},
                {name="ServingTrayGravy", min=1, max=2, weightChance=20},
                {name="ServingTrayHotdogs", min=0, max=1, weightChance=40},
                {name="ServingTrayOmelettes", min=0, max=1, weightChance=20},
                {name="ServingTrayOnionRings", min=0, max=1, weightChance=40},
                {name="ServingTrayPancakes", min=0, max=1, weightChance=20},
                {name="ServingTrayPie", min=0, max=1, weightChance=40},
                {name="ServingTrayPizza", min=0, max=1, weightChance=40},
                {name="ServingTrayScrambledEggs", min=0, max=1, weightChance=20},
                {name="ServingTrayWaffles", min=0, max=1, weightChance=20},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="BarShelfLiquor", min=0, max=99, forceForTiles="location_restaurant_bar_01_29;location_restaurant_bar_01_30;location_restaurant_bar_01_31;location_restaurant_bar_01_37;location_restaurant_bar_01_38;location_restaurant_bar_01_39;location_restaurant_bar_01_64;location_restaurant_bar_01_65;location_restaurant_bar_01_66;location_restaurant_bar_01_72;location_restaurant_bar_01_73;location_restaurant_bar_01_74"},
            }
        }
    },

    restaurantkitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenBaking", min=1, max=1, weightChance=100},
                {name="StoreKitchenButcher", min=1, max=1, weightChance=100},
                {name="StoreKitchenCafe", min=0, max=1, weightChance=100},
                {name="StoreKitchenCutlery", min=0, max=1, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=1, weightChance=20},
                {name="StoreKitchenPotatoes", min=1, max=1, weightChance=100},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
                {name="StoreKitchenSauce", min=1, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCoffee", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateTea", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="RestaurantKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="RestaurantKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCoffee", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateTea", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="BakeryDoughnuts", min=0, max=1, weightChance=20},
                {name="ServingTrayBurgers", min=1, max=2, weightChance=100},
                {name="ServingTrayBurritos", min=0, max=1, weightChance=20},
                {name="ServingTrayFries", min=1, max=2, weightChance=60},
                {name="ServingTrayGravy", min=1, max=2, weightChance=20},
                {name="ServingTrayHotdogs", min=0, max=1, weightChance=40},
                {name="ServingTrayOmelettes", min=0, max=1, weightChance=20},
                {name="ServingTrayOnionRings", min=0, max=1, weightChance=40},
                {name="ServingTrayPancakes", min=0, max=1, weightChance=20},
                {name="ServingTrayPie", min=0, max=1, weightChance=40},
                {name="ServingTrayPizza", min=0, max=1, weightChance=40},
                {name="ServingTrayScrambledEggs", min=0, max=1, weightChance=20},
                {name="ServingTrayWaffles", min=0, max=1, weightChance=20},
            }
        },
    },

    schoolstorage = {
        counter = {
            procedural = true,
            procList = {
                {name="JanitorMisc", min=1, max=1, weightChance=100},
                {name="JanitorTools", min=0, max=1, weightChance=100},
                {name="JanitorCleaning", min=0, max=1, weightChance=100},
                {name="JanitorChemicals", min=0, max=99, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="SportStorageBats", min=0, max=1, weightChance=100},
                {name="SportStorageBalls", min=0, max=1, weightChance=100},
                {name="SportStorageHelmets", min=0, max=1, weightChance=100},
                {name="SportStoragePaddles", min=0, max=1, weightChance=100},
                {name="SportStorageRacquets", min=0, max=2, weightChance=100},
                {name="SportStorageSticks", min=0, max=2, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ClassroomMisc", min=0, max=99},
            }
        },
        desk = {
            rolls = 0,
            items = {

            }
        }
    },

    seafoodkitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="FishChipsKitchenSauce", min=1, max=1, weightChance=100},
                {name="JaysKitchenBaking", min=1, max=1, weightChance=100},
                {name="SeafoodKitchenButcher", min=1, max=2, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenBags", min=0, max=1, weightChance=20},
                {name="StoreKitchenCups", min=0, max=1, weightChance=20},
                {name="StoreKitchenPotatoes", min=1, max=1, weightChance=100},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
                {name="StoreKitchenTrays", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="SeafoodKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="SeafoodKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayFish", min=1, max=8, weightChance=100},
                {name="ServingTrayOysters", min=1, max=4, weightChance=20},
                {name="ServingTrayShrimp", min=1, max=4, weightChance=60},
            }
        },
    },

    security = {
        locker = {
            procedural = true,
            procList = {
                {name="SecurityLockers", min=0, max=99, forceForTiles="furniture_storage_02_8;furniture_storage_02_9;furniture_storage_02_10;furniture_storage_02_11"},
            },
            dontSpawnAmmo = true,
        }
    },

    sewingstorage = {
        isShop = true,
        metal_shelves = {
            procedural = true,
            procList = {
                {name="SewingStoreTools", min=0, max=99, weightChance=100},
                {name="SewingStoreFabric", min=0, max=99, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="SewingStoreTools", min=0, max=99, weightChance=100},
                {name="SewingStoreFabric", min=0, max=99, weightChance=100},
            }
        }
    },

    sewingstore = {
        isShop = true,
        counter ={
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBagsFancy", min=0, max=1, weightChance=100},
                {name="BookstoreTailoring", min=1, max=2, weightChance=100},
                {name="SewingStoreTools", min=1, max=99, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="SewingStoreTools", min=1, max=99, weightChance=100},
                {name="SewingStoreFabric", min=1, max=99, weightChance=100},
            }
        },
        clothingrack = {
            procedural = true,
            procList = {
                {name="SewingStoreTools", min=1, max=99, weightChance=100},
                {name="SewingStoreFabric", min=1, max=99, weightChance=100},
                {name="ClothingStoresDress", min=1, max=5, weightChance=80},
                {name="ClothingStoresWoman", min=1, max=5, weightChance=80},
                {name="ClothingStoresJumpers", min=1, max=5, weightChance=80},
            }
        }
    },

    shed = {
        locker = {
            procedural = true,
            procList = {
                {name="GarageTools", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="GarageCarpentry", min=0, max=1, weightChance=100},
                {name="GarageTools", min=0, max=1, weightChance=100},
                {name="GarageMetalwork", min=0, max=1, weightChance=100},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="GarageCarpentry", min=0, max=2, weightChance=100},
                {name="GarageMetalwork", min=0, max=2, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateConcrete", min=0, max=1, weightChance=100},
                {name="CrateFarming", min=0, max=1, weightChance=100},
                {name="CrateFertilizer", min=0, max=1, weightChance=100},
                {name="CrateGravelBags", min=0, max=1, weightChance=100},
                {name="CrateLumber", min=0, max=1, weightChance=100},
                {name="CrateMetalwork", min=0, max=1, weightChance=100},
                {name="CratePaint", min=0, max=1, weightChance=100},
                {name="CratePlaster", min=0, max=1, weightChance=100},
                {name="CrateTools", min=0, max=1, weightChance=100},
            }
        }
    },

    shoestore = {
        isShop = true,
        clothingrack = {
            procedural = true,
            procList = {
                {name="ClothingStoresDress", min=0, max=99, weightChance=20},
                {name="ClothingStoresJackets", min=0, max=99, weightChance=40},
                {name="ClothingStoresJacketsFormal", min=0, max=99, weightChance=10},
                {name="ClothingStoresJumpers", min=0, max=99, weightChance=60},
                {name="ClothingStoresOvershirts", min=0, max=99, weightChance=80},
                {name="ClothingStoresPants", min=0, max=99, weightChance=100},
                {name="ClothingStoresPantsFormal", min=0, max=99, weightChance=10},
                {name="ClothingStoresShirts", min=0, max=99, weightChance=100},
                {name="ClothingStoresShirtsFormal", min=0, max=99, weightChance=10},
                {name="ClothingStoresSport", min=0, max=99, weightChance=40},
                {name="ClothingStoresSummer", min=0, max=99, weightChance=40},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="ClothingStoresGloves", min=0, max=99, weightChance=40},
                {name="ClothingStoresEyewear", min=0, max=99, weightChance=100},
                {name="ClothingStoresHeadwear", min=0, max=99, weightChance=60},
                {name="ClothingStoresSocks", min=0, max=99, weightChance=20},
                {name="ClothingStoresUnderwearWoman", min=0, max=99, weightChance=20},
                {name="ClothingStoresUnderwearMan", min=0, max=99, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="ClothingStorageFootwear", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ClothingStorageFootwear", min=0, max=99},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="ClothingStoresBoots", min=0, max=99, weightChance=40},
                {name="ClothingStoresShoes", min=0, max=99, weightChance=100},
            }
        },
    },

    sodatruck = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CrateSodaBottles", min=0, max=99, weightChance=100},
                {name="CrateSodaCans", min=0, max=99, weightChance=100},
            }
        },
    },

    spiffo_dining = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="Empty", min=0, max=99, forceForTiles="location_shop_accessories_01_8;location_shop_accessories_01_9;location_shop_accessories_01_10;location_shop_accessories_01_11;location_shop_accessories_01_11;location_shop_accessories_01_12"},
                {name="SpiffosDiningCounter", min=0, max=99,weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayBurgers", min=1, max=8, weightChance=100},
                {name="ServingTrayChickenNuggets", min=0, max=99, weightChance=100},
                {name="ServingTrayFries", min=1, max=4, weightChance=60},
                {name="ServingTrayOnionRings", min=1, max=4, weightChance=60},
            }
        },
    },

    spiffoskitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="BurgerKitchenButcher", min=1, max=1, weightChance=100},
                {name="BurgerKitchenSauce", min=1, max=1, weightChance=100},
                {name="SpiffosKitchenBags", min=0, max=1, weightChance=20},
                {name="SpiffosKitchenCounter", min=0, max=1, weightChance=5},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenBaking", min=1, max=1, weightChance=100},
                {name="StoreKitchenCups", min=0, max=1, weightChance=20},
                {name="StoreKitchenPotatoes", min=1, max=1, weightChance=100},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
                {name="StoreKitchenTrays", min=0, max=1, weightChance=20},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateFountainCups", min=0, max=1, weightChance=60},
                {name="CrateNapkins", min=0, max=1, weightChance=60},
                {name="CratePaperBagSpiffos", min=0, max=1, weightChance=60},
                {name="CratePlasticTrays", min=0, max=1, weightChance=60},
                {name="CrateSpiffoMerch", min=0, max=1, weightChance=5},
                {name="FryFactoryPotatoes", min=0, max=4, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="SpiffosKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="SpiffosKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateFountainCups", min=0, max=1, weightChance=60},
                {name="CrateNapkins", min=0, max=1, weightChance=60},
                {name="CratePaperBagSpiffos", min=0, max=1, weightChance=60},
                {name="CratePlasticTrays", min=0, max=1, weightChance=60},
                {name="CrateSpiffoMerch", min=0, max=1, weightChance=5},
                {name="FryFactoryPotatoes", min=0, max=4, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayBurgers", min=1, max=8, weightChance=100},
                {name="ServingTrayChickenNuggets", min=0, max=99, weightChance=100},
                {name="ServingTrayFries", min=1, max=4, weightChance=60},
                {name="ServingTrayOnionRings", min=1, max=4, weightChance=60},
            }
        },
    },

    spiffosstorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CrateFountainCups", min=0, max=1, weightChance=60},
                {name="CrateNapkins", min=0, max=1, weightChance=60},
                {name="CratePaperBagSpiffos", min=0, max=1, weightChance=60},
                {name="CratePlasticTrays", min=0, max=1, weightChance=60},
                {name="CrateSpiffoMerch", min=0, max=1, weightChance=5},
                {name="FryFactoryPotatoes", min=0, max=4, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateFountainCups", min=0, max=1, weightChance=60},
                {name="CrateNapkins", min=0, max=1, weightChance=60},
                {name="CratePaperBagSpiffos", min=0, max=1, weightChance=60},
                {name="CratePlasticTrays", min=0, max=1, weightChance=60},
                {name="CrateSpiffoMerch", min=0, max=1, weightChance=5},
                {name="FryFactoryPotatoes", min=0, max=4, weightChance=100},
            }
        },
    },

    sportstorage = {
        isShop = true,
        metal_shelves = {
            procedural = true,
            procList = {
                {name="SportStorageBats", min=0, max=2, weightChance=100},
                {name="SportStorageHelmets", min=0, max=2, weightChance=80},
                {name="SportStoragePaddles", min=0, max=1, weightChance=10},
                {name="SportStorageRacquets", min=0, max=4, weightChance=20},
                {name="SportStorageSticks", min=0, max=4, weightChance=80},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="SportsStoreSneakers", min=0, max=99, weightChance=100},
                {name="SportStorageBats", min=0, max=2, weightChance=100},
                {name="SportStorageHelmets", min=0, max=2, weightChance=80},
                {name="SportStoragePaddles", min=0, max=1, weightChance=10},
                {name="SportStorageRacquets", min=0, max=4, weightChance=20},
                {name="SportStorageSticks", min=0, max=4, weightChance=80},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="SportStorageBats", min=0, max=2, weightChance=100},
                {name="SportStorageBalls", min=0, max=2, weightChance=20},
                {name="SportStorageHelmets", min=0, max=2, weightChance=80},
                {name="SportStoragePaddles", min=0, max=1, weightChance=10},
                {name="SportStorageRacquets", min=0, max=4, weightChance=20},
                {name="SportStorageSticks", min=0, max=4, weightChance=80},
            }
        },
        clothingrack = {
            procedural = true,
            procList = {
                {name="ClothingStoresSport", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBagsFancy", min=0, max=1, weightChance=20},
                {name="ClothingStoresEyewear", min=0, max=2, weightChance=60},
                {name="ClothingStoresHeadwear", min=0, max=4, weightChance=100},
                {name="SportsStoreSneakers", min=0, max=99, weightChance=80},
            }
        }
    },

    sportstore = {
        isShop = true,
        shelves = {
            procedural = true,
            procList = {
                {name="SportsStoreSneakers", min=0, max=99, weightChance=100},
                {name="SportStorageBats", min=0, max=99, weightChance=100},
                {name="SportStorageHelmets", min=0, max=99, weightChance=80},
                {name="SportStoragePaddles", min=0, max=99, weightChance=10},
                {name="SportStorageRacquets", min=0, max=99, weightChance=20},
                {name="SportStorageSticks", min=0, max=99, weightChance=80},
            }
        },
        clothingrack = {
            procedural = true,
            procList = {
                {name="ClothingStoresSport", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBagsFancy", min=0, max=1, weightChance=20},
                {name="ClothingStoresEyewear", min=0, max=2, weightChance=60},
                {name="ClothingStoresHeadwear", min=0, max=4, weightChance=100},
                {name="SportsStoreSneakers", min=0, max=99, weightChance=80},
            }
        }
    },

    storageunit = {
        counter = {
            rolls = 1,
            items = {
                
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="ClothingStorageWinter", min=0, max=1, weightChance=40},
                {name="CrateAntiqueStove", min=0, max=1, weightChance=10},
                {name="CrateBluePlasticChairs", min=0, max=1, weightChance=40},
                {name="CrateBooks", min=0, max=1, weightChance=80},
                {name="CrateCamping", min=0, max=1, weightChance=100},
                {name="CrateCannedFood", min=0, max=1, weightChance=20},
                {name="CrateCannedFoodSpoiled", min=0, max=1, weightChance=20},
                {name="CrateCanning", min=0, max=1, weightChance=20},
                {name="CrateClothesRandom", min=0, max=1, weightChance=80},
                {name="CrateComics", min=0, max=1, weightChance=80},
                {name="CrateCompactDiscs", min=0, max=1, weightChance=20},
                {name="CrateComputer", min=0, max=1, weightChance=40},
                {name="CrateConcrete", min=0, max=1, weightChance=100},
                {name="CrateDishes", min=0, max=1, weightChance=60},
                {name="CrateElectronics", min=0, max=1, weightChance=60},
                {name="CrateFarming", min=0, max=1, weightChance=100},
                {name="CrateFertilizer", min=0, max=1, weightChance=100},
                {name="CrateFishing", min=0, max=1, weightChance=100},
                {name="CrateFitnessWeights", min=0, max=1, weightChance=40},
                {name="CrateFoldingChairs", min=0, max=1, weightChance=40},
                {name="CrateFootwearRandom", min=0, max=1, weightChance=80},
                {name="CrateGravelBags", min=0, max=1, weightChance=100},
                {name="CrateInstruments", min=0, max=1, weightChance=40},
                {name="CrateLinens", min=0, max=1, weightChance=60},
                {name="CrateLumber", min=0, max=1, weightChance=100},
                {name="CrateMagazines", min=0, max=1, weightChance=80},
                {name="CrateMannequins", min=0, max=1, weightChance=10},
                {name="CrateMechanics", min=0, max=1, weightChance=100},
                {name="CrateMetalwork", min=0, max=1, weightChance=100},
                {name="CrateOfficeSupplies", min=0, max=1, weightChance=80},
                {name="CratePaint", min=0, max=1, weightChance=100},
                {name="CratePetSupplies", min=0, max=1, weightChance=40},
                {name="CratePlaster", min=0, max=1, weightChance=100},
                {name="CratePlasticChairs", min=0, max=1, weightChance=40},
                {name="CrateRandomJunk", min=0, max=1, weightChance=100},
                {name="CrateSandbags", min=0, max=1, weightChance=100},
                {name="CrateSheetMetal", min=0, max=1, weightChance=100},
                {name="CrateSports", min=0, max=1, weightChance=60},
                {name="CrateTailoring", min=0, max=1, weightChance=100},
                {name="CrateTools", min=0, max=1, weightChance=60},
                {name="CrateToys", min=0, max=1, weightChance=40},
                {name="CrateTV", min=0, max=1, weightChance=40},
                {name="CrateVHSTapes", min=0, max=1, weightChance=20},
                {name="CrateWhiteWoodenChairs", min=0, max=1, weightChance=40},
                {name="CrateWoodenChairs", min=0, max=1, weightChance=40},
                {name="CrateWoodenStools", min=0, max=1, weightChance=40},
            }
        },
        dresser = {
            rolls = 1,
            items = {

            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="ClothingStorageWinter", min=0, max=1, weightChance=40},
                {name="CrateCamping", min=0, max=1, weightChance=100},
                {name="CrateCanning", min=0, max=1, weightChance=20},
                {name="CrateClothesRandom", min=0, max=1, weightChance=80},
                {name="CrateCompactDiscs", min=0, max=1, weightChance=20},
                {name="CrateDishes", min=0, max=1, weightChance=60},
                {name="CrateElectronics", min=0, max=1, weightChance=60},
                {name="CrateFarming", min=0, max=1, weightChance=100},
                {name="CrateFishing", min=0, max=1, weightChance=100},
                {name="CrateFootwearRandom", min=0, max=1, weightChance=80},
                {name="CrateInstruments", min=0, max=1, weightChance=40},
                {name="CrateLinens", min=0, max=1, weightChance=20},
                {name="CrateMechanics", min=0, max=1, weightChance=100},
                {name="CrateMetalwork", min=0, max=1, weightChance=100},
                {name="CrateOfficeSupplies", min=0, max=1, weightChance=20},
                {name="CratePaint", min=0, max=1, weightChance=100},
                {name="CratePetSupplies", min=0, max=1, weightChance=40},
                {name="CrateRandomJunk", min=0, max=1, weightChance=100},
                {name="CrateSports", min=0, max=1, weightChance=60},
                {name="CrateTailoring", min=0, max=1, weightChance=100},
                {name="CrateTools", min=0, max=1, weightChance=60},
                {name="CrateToys", min=0, max=1, weightChance=40},
                {name="CrateVHSTapes", min=0, max=1, weightChance=20},
            }
        },
        shelves = {
            rolls = 1,
            items = {

            }
        },
        sidetable = {
            rolls = 1,
            items = {

            }
        },
        wardrobe = {
            rolls = 1,
            items = {

            }
        },
    },

    stripclub = {
        dresser = {
            procedural = true,
            procList = {
                {name="StripClubDressers", min=0, max=99, weightChance=100},
            }
        },
    },

    sushidining = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        },
        displaycase = {
            procedural = true,
            procList = {
                {name="ServingTrayMaki", min=0, max=99, weightChance=80},
                {name="ServingTrayOnigiri", min=0, max=99, weightChance=80},
                {name="ServingTraySpringRolls", min=0, max=99, weightChance=40},
                {name="ServingTraySushiEgg", min=1, max=99, weightChance=100},
                {name="ServingTraySushiFish", min=1, max=99, weightChance=100},
            }
        },
    },

    sushikitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenDishes", min=0, max=1, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=1, weightChance=20},
                {name="StoreKitchenPots", min=0, max=1, weightChance=20},
                {name="SushiKitchenBaking", min=1, max=1, weightChance=100},
                {name="SushiKitchenButcher", min=1, max=2, weightChance=100},
                {name="SushiKitchenCutlery", min=0, max=2, weightChance=20},
                {name="SushiKitchenSauce", min=1, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateRice", min=0, max=4, weightChance=100},
                {name="CrateRiceVinegar", min=0, max=1, weightChance=100},
                {name="CrateSeaweed", min=0, max=2, weightChance=100},
                {name="CrateSoysauce", min=0, max=1, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="SushiKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="SushiKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateRice", min=0, max=4, weightChance=100},
                {name="CrateRiceVinegar", min=0, max=1, weightChance=100},
                {name="CrateSeaweed", min=0, max=2, weightChance=100},
                {name="CrateSoysauce", min=0, max=1, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTraySushiEgg", min=1, max=4, weightChance=100},
                {name="ServingTraySushiFish", min=1, max=4, weightChance=100},
                {name="ServingTrayMaki", min=1, max=4, weightChance=60},
                {name="ServingTrayOnigiri", min=1, max=4, weightChance=60},
                {name="ServingTrayTofuFried", min=0, max=2, weightChance=40},
                {name="ServingTraySpringRolls", min=0, max=2, weightChance=40},
            }
        },
    },

    theatre = {
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        }
    },

    theatrekitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CratePopcorn", min=0, max=99, weightChance=100},
                {name="CrateSodaBottles", min=0, max=99, weightChance=40},
                {name="CrateSodaCans", min=0, max=99, weightChance=60},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="TheatreKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            rolls = 1,
            items = {

            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CratePopcorn", min=0, max=99, weightChance=100},
                {name="CrateSodaBottles", min=0, max=99, weightChance=40},
                {name="CrateSodaCans", min=0, max=99, weightChance=60},
            }
        },
    },

    theatrestorage = {
        crate = {
            procedural = true,
            procList = {
                {name="CratePopcorn", min=0, max=99, weightChance=100},
                {name="CrateSodaBottles", min=0, max=99, weightChance=40},
                {name="CrateSodaCans", min=0, max=99, weightChance=60},
            }
        },
    },

    toolstorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CrateConcrete", min=0, max=99, weightChance=40},
                {name="CrateLumber", min=0, max=99, weightChance=100},
                {name="CratePlaster", min=0, max=99, weightChance=80},
                {name="ToolStoreTools", min=0, max=99, weightChance=20},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateConcrete", min=0, max=99, weightChance=40},
                {name="CrateLumber", min=0, max=99, weightChance=100},
                {name="CratePlaster", min=0, max=99, weightChance=80},
                {name="ToolStoreTools", min=0, max=99, weightChance=20},
                {name="ToolStoreFarming", min=0, max=99, weightChance=10},
                {name="ToolStoreCarpentry", min=0, max=99, weightChance=10},
                {name="ToolStoreMetalwork", min=0, max=99, weightChance=10},
            }
        }
    },

    toolstore = {
        isShop = true,
        clothingrack = {
            procedural = true,
            procList = {
                {name="ToolStoreOutfit", min=0, max=99},
            }
        },
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="ToolStoreFootwear", min=0, max=2, weightChance=40},
                {name="ToolStoreBooks", min=0, max=2, weightChance=20},
                {name="ToolStoreAccessories", min=0, max=2, weightChance=80},
                {name="ToolStoreMisc", min=0, max=99, weightChance=100},
                {name="ToolStoreTools", min=0, max=99, weightChance=5},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateConcrete", min=0, max=99, weightChance=40},
                {name="CrateLumber", min=0, max=99, weightChance=100},
                {name="CratePlaster", min=0, max=99, weightChance=80},
                {name="ToolStoreTools", min=0, max=99, weightChance=20},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateConcrete", min=0, max=99, weightChance=40},
                {name="CrateLumber", min=0, max=99, weightChance=100},
                {name="CratePlaster", min=0, max=99, weightChance=80},
                {name="ToolStoreTools", min=0, max=99, weightChance=20},
                {name="ToolStoreFarming", min=0, max=99, weightChance=10},
                {name="ToolStoreCarpentry", min=0, max=99, weightChance=10},
                {name="ToolStoreMetalwork", min=0, max=99, weightChance=10},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="FishingStoreGear", min=0, max=99, weightChance=10},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
                {name="ToolStoreBooks", min=0, max=99, forceForTiles="furniture_shelving_01_40;furniture_shelving_01_41;furniture_shelving_01_42;furniture_shelving_01_43"},
                {name="ToolStoreCarpentry", min=0, max=99, weightChance=60},
                {name="ToolStoreFarming", min=0, max=99, weightChance=40},
                {name="ToolStoreMetalwork", min=0, max=99, weightChance=60},
                {name="ToolStoreMisc", min=0, max=99, weightChance=20},
                {name="ToolStoreTools", min=0, max=99, weightChance=100},
                {name="ToolStoreOutfit", min=0, max=99, weightChance=20},
            }
        },
    },

    toystore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="GigamartToys", min=0, max=99, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreCounterBagsFancy", min=0, max=1, weightChance=100},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="GigamartToys", min=0, max=99},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
            }
        },
    },

    toystorestorage = {
        isShop = true,
        shelves = {
            procedural = true,
            procList = {
                {name="GigamartToys", min=0, max=99},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="GigamartToys", min=0, max=99},
            }
        }
    },

    warehouse = {
        isShop = true,
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateAntiqueStove", min=0, max=1, weightChance=5},
                {name="CrateCarpentry", min=0, max=99, weightChance=100},
                {name="CrateConcrete", min=0, max=99, weightChance=40},
                {name="CrateFarming", min=0, max=99, weightChance=40},
                {name="CrateGravelBags", min=0, max=99, weightChance=40},
                {name="CrateLumber", min=0, max=99, weightChance=40},
                {name="CrateMetalwork", min=0, max=99, weightChance=100},
                {name="CratePaint", min=0, max=99, weightChance=40},
                {name="CratePlaster", min=0, max=99, weightChance=40},
                {name="CrateSandBags", min=0, max=99, weightChance=40},
                {name="CrateSheetMetal", min=0, max=99, weightChance=40},
                {name="CrateTools", min=0, max=99, weightChance=100},
            }
        },
        freezer = {
            rolls = 1,
            items = {
                
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="OfficeFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCarpentry", min=0, max=99, weightChance=100},
                {name="CrateFarming", min=0, max=99, weightChance=40},
                {name="CrateMetalwork", min=0, max=99, weightChance=100},
                {name="CratePaint", min=0, max=99, weightChance=40},
                {name="CrateTools", min=0, max=99, weightChance=100},
            }
        },
    },

    westernkitchen = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23;fixtures_sinks_01_0;fixtures_sinks_01_1;fixtures_sinks_01_2;fixtures_sinks_01_3;fixtures_sinks_01_4;fixtures_sinks_01_5;fixtures_sinks_01_6;fixtures_sinks_01_7;fixtures_sinks_01_8;fixtures_sinks_01_9;fixtures_sinks_01_10;fixtures_sinks_01_11;fixtures_sinks_01_16;fixtures_sinks_01_17;fixtures_sinks_01_18;fixtures_sinks_01_19"},
                {name="StoreKitchenCutlery", min=0, max=99, weightChance=20},
                {name="StoreKitchenDishes", min=0, max=99, weightChance=20},
                {name="StoreKitchenGlasses", min=0, max=99, weightChance=20},
                {name="StoreKitchenPotatoes", min=1, max=1, weightChance=100},
                {name="StoreKitchenPots", min=0, max=99, weightChance=20},
                {name="WesternKitchenBaking", min=1, max=1, weightChance=100},
                {name="WesternKitchenButcher", min=1, max=1, weightChance=100},
                {name="WesternKitchenSauce", min=1, max=1, weightChance=100},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateCornflour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="WesternKitchenFreezer", min=0, max=99},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="WesternKitchenFridge", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateCornflour", min=0, max=1, weightChance=100},
                {name="CrateGravyMix", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CrateYeast", min=0, max=1, weightChance=100},
                {name="FryFactoryPotatoes", min=0, max=2, weightChance=100},
            }
        },
        restaurantdisplay = {
            procedural = true,
            procList = {
                {name="ServingTrayBurgers", min=1, max=2, weightChance=100},
                {name="ServingTrayBiscuits", min=1, max=2, weightChance=100},
                {name="ServingTrayGravy", min=1, max=2, weightChance=20},
                {name="ServingTrayCornbread", min=1, max=2, weightChance=60},
                {name="ServingTrayFries", min=1, max=2, weightChance=40},
                {name="ServingTrayOnionRings", min=1, max=2, weightChance=40},
            }
        },
    },

    whiskeybottling = {
        isShop = true,
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="WhiskeyBottlingEmpty", min=0, max=99, weightChance=80},
                {name="WhiskeyBottlingFull", min=0, max=99, weightChance=100},
            },
        },
        laundrydryerbasic = {
            procedural = true,
            procList = {
                {name="WhiskeyBottlingEmpty", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="WhiskeyBottlingEmpty", min=0, max=99, weightChance=80},
                {name="WhiskeyBottlingFull", min=0, max=99, weightChance=100},
            }
        },
    },

    wirefactory = {
        isShop = true,
        counter = {
            rolls = 1,
            items = {

            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="WireFactoryBarbed", min=0, max=99, weightChance=100},
                {name="WireFactoryBasic", min=0, max=99, weightChance=100},
                {name="WireFactoryElectric", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="WireFactoryBarbed", min=0, max=99, weightChance=100},
                {name="WireFactoryBasic", min=0, max=99, weightChance=100},
                {name="WireFactoryElectric", min=0, max=99, weightChance=100},
            }
        },
    },

    zippeestorage = {
        isShop = true,
        crate = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
    },

    zippeestore = {
        isShop = true,
        counter = {
            procedural = true,
            procList = {
                {name="Empty", min=0, max=99, forceForTiles="location_shop_accessories_01_8;location_shop_accessories_01_9;location_shop_accessories_01_10;location_shop_accessories_01_11;location_shop_accessories_01_11;location_shop_accessories_01_12;location_shop_fossoil_01_10;location_shop_fossoil_01_11;"},
                {name="StoreCounterBags", min=0, max=2, weightChance=100},
                {name="StoreCounterCleaning", min=0, max=99, forceForTiles="location_shop_accessories_01_0;location_shop_accessories_01_1;location_shop_accessories_01_2;location_shop_accessories_01_3;location_shop_accessories_01_20;location_shop_accessories_01_21;location_shop_accessories_01_22;location_shop_accessories_01_23"},
            }
        },
        crate = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        freezer = {
            procedural = true,
            procList = {
                {name="FreezerIceCream", min=0, max=99, forceForTiles="appliances_refrigeration_01_20;appliances_refrigeration_01_21;appliances_refrigeration_01_38;appliances_refrigeration_01_39"},
            }
        },
        fridge = {
            procedural = true,
            procList = {
                {name="FridgeOther", min=1, max=99, weightChance=40},
                {name="FridgeSnacks", min=1, max=99, weightChance=100},
                {name="FridgeSoda", min=1, max=99, weightChance=100},
                {name="FridgeWater", min=1, max=99, weightChance=60},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCandyPackage", min=0, max=1, weightChance=40},
                {name="CrateChips", min=0, max=1, weightChance=100},
                {name="CrateChocolate", min=0, max=1, weightChance=40},
                {name="CrateCigarettes", min=0, max=1, weightChance=60},
                {name="CrateGum", min=0, max=1, weightChance=40},
                {name="CratePeanuts", min=0, max=1, weightChance=40},
                {name="CrateSodaBottles", min=0, max=1, weightChance=100},
                {name="CrateSodaCans", min=0, max=1, weightChance=100},
                {name="CrateSunflowerSeeds", min=0, max=1, weightChance=40},
                {name="CrateTortillaChips", min=0, max=1, weightChance=40},
            }
        },
        shelves = {
            procedural = true,
            procList = {
                {name="StoreCounterTobacco", min=0, max=99, forceForTiles="location_shop_zippee_01_0;location_shop_zippee_01_1;location_shop_zippee_01_2;location_shop_zippee_01_3"},
                {name="StoreShelfCombo", min=0, max=99, forceForTiles="location_shop_generic_01_0;location_shop_generic_01_1"},
                {name="StoreShelfDrinks", min=0, max=99, weightChance=100},
                {name="StoreShelfMechanics", min=0, max=99, forceForTiles="location_shop_zippee_01_4;location_shop_zippee_01_5"},
                {name="StoreShelfMedical", min=0, max=1, weightChance=20},
                {name="StoreShelfSnacks", min=0, max=99, weightChance=100},
            }
        },
    },

    -- =====================
    --    Bags/Containers
    -- =====================

    Bag_BigHikingBag = {
        rolls = 1,
        items = {
            "AlcoholWipes", 1,
            "Bandage", 1,
            "camping.CampingTentKit", 0.5,
            "Chocolate", 1,
            "Cigarettes", 1,
            "Crisps", 1,
            "Disc_Retail", 2,
            "Earbuds", 1,
            "HoodieDOWN_WhiteTINT", 0.5,
            "Lighter", 1,
            "Peanuts", 1,
            "Radio.CDplayer", 1,
            "Socks_Ankle", 2,
            "SunflowerSeeds", 1,
            "BathTowel", 2,
            "Vest_DefaultTEXTURE_TINT", 2,
            "WaterBottleFull", 1,
        },
        fillRand = 1,
    },

    Bag_BowlingBallBag = {
        rolls = 1,
        items = {
            "Shoes_Bowling", 50,
            "Shirt_Bowling_Blue", 1,
            "Shirt_Bowling_Brown", 1,
            "Shirt_Bowling_Green", 1,
            "Shirt_Bowling_LimeGreen", 1,
            "Shirt_Bowling_Pink", 1,
            "Shirt_Bowling_White", 1,
        }
    },

    Bag_DuffelBag = {
        rolls = 1,
        items = {
            "Disc_Retail", 2,
            "Earbuds", 1,
            "Hat_BaseballCap", 1,
            "Hat_Sweatband", 1,
            "HoodieDOWN_WhiteTINT", 0.5,
            "Radio.CDplayer", 1,
            "Shorts_LongSport", 2,
            "Shorts_ShortSport", 2,
            "Socks_Ankle", 2,
            "BathTowel", 2,
            "Tshirt_Sport", 2,
            "Vest_DefaultTEXTURE_TINT", 2,
            "WaterBottleFull", 1,
        },
        fillRand = 1,
    },

    Bag_DuffelBagTINT = {
        rolls = 1,
        items = {
            "Disc_Retail", 2,
            "Earbuds", 1,
            "Hat_BaseballCap", 1,
            "Hat_Sweatband", 1,
            "HoodieDOWN_WhiteTINT", 0.5,
            "Radio.CDplayer", 1,
            "Shorts_LongSport", 2,
            "Shorts_ShortSport", 2,
            "Socks_Ankle", 2,
            "BathTowel", 2,
            "Tshirt_Sport", 2,
            "Vest_DefaultTEXTURE_TINT", 2,
            "WaterBottleFull", 1,
        },
        fillRand = 1,
    },

    Bag_GolfBag = {
        rolls = 1,
        items = {
            "Golfclub", 200,
            "Golfclub", 50,
            "Golfclub", 20,
            "Golfclub", 10,
            "GolfBall", 200,
            "GolfBall", 50,
            "GolfBall", 20,
            "GolfBall", 10,
            "Gloves_LeatherGloves", 1,
            "Hat_GolfHatTINT", 2,
            "Hat_VisorBlack", 2,
            "Hat_VisorRed", 2,
            "Pencil", 10,
            "Eraser", 10,
            "Notebook", 10,
            "Cigarettes", 4,
        },
        fillRand = 0,
    },

    Bag_InmateEscapedBag = {
        rolls = 1,
        items = {
            "DuctTape", 50,
            "Hammer", 20,
            "RippedSheets", 20,
            "RippedSheets", 20,
            "Rope", 50,
            "Screwdriver", 20,
            "SheetRope", 20,
            "SheetRope", 20,
        },
        fillRand = 0,
    },

    Bag_JanitorToolbox = {
        rolls = 1,
        items = {
            "PipeWrench", 50,
            "Wrench", 50,
            "HandTorch", 50,
            "Hammer", 50,
            "Saw", 50,
            "Screwdriver", 50,
            "Crowbar", 25,
            "DuctTape", 10,
            "Scotchtape", 10,
            "RippedSheets", 10,
            "RippedSheets", 10,
            "RippedSheetsDirty", 25,
            "RippedSheetsDirty", 25,
        },
        fillRand = 0,
    },

    Bag_MoneyBag = {
        rolls = 10,
        items = {
            "Money", 100,
            "Money", 100,
            "Money", 100,
            "Money", 100,
            "Money", 100,
            "Money", 100,
            "Money", 100,
            "Money", 100,
        },
        fillRand = 0,
    },

    Bag_NormalHikingBag = {
        rolls = 1,
        items = {
            "AlcoholWipes", 1,
            "Bandage", 1,
            "Chocolate", 1,
            "Cigarettes", 1,
            "Crisps", 1,
            "Disc_Retail", 2,
            "Earbuds", 1,
            "HoodieDOWN_WhiteTINT", 0.5,
            "Lighter", 1,
            "Peanuts", 1,
            "Radio.CDplayer", 1,
            "Socks_Ankle", 2,
            "SunflowerSeeds", 1,
            "BathTowel", 2,
            "Vest_DefaultTEXTURE_TINT", 2,
            "WaterBottleFull", 1,
        },
        fillRand = 1,
    },

    Bag_Schoolbag = {
        rolls = 1,
        items = {
            "BluePen", 8,
            "Book", 20,
            "Book", 10,
            "Cigarettes", 4,
            "ComicBook", 10,
            "Crisps", 10,
            "Disc_Retail", 2,
            "Earbuds", 1,
            "Lighter", 1,
            "Pen", 8,
            "Pencil", 10,
            "Pop", 1,
            "Radio.CDplayer", 1,
            "RedPen", 8,
            "RubberBand", 1,
            "Scissors", 2,
        },
        fillRand = 0,
    },

    Bag_ShotgunBag = {
        rolls = 1,
        items = {
            "Shotgun", 200,
            "ShotgunShells", 200,
            "ShotgunShells", 50,
            "ShotgunShells", 20,
            "ShotgunShells", 10,
            "ShotgunShellsBox", 50,
            "ShotgunShellsBox", 20,
            "ShotgunShellsBox", 10,
        }
    },

    Bag_ShotgunDblBag = {
        rolls = 1,
        items = {
            "DoubleBarrelShotgun", 200,
            "ShotgunShells", 200,
            "ShotgunShells", 50,
            "ShotgunShells", 20,
            "ShotgunShells", 10,
            "ShotgunShellsBox", 50,
            "ShotgunShellsBox", 20,
            "ShotgunShellsBox", 10,
        }
    },

    Bag_ShotgunDblSawnoffBag = {
        rolls = 1,
        items = {
            "DoubleBarrelShotgunSawnoff", 200,
            "ShotgunShells", 200,
            "ShotgunShells", 50,
            "ShotgunShells", 20,
            "ShotgunShells", 10,
            "ShotgunShellsBox", 50,
            "ShotgunShellsBox", 20,
            "ShotgunShellsBox", 10,
        }
    },

    Bag_ShotgunSawnoffBag = {
        rolls = 1,
        items = {
            "ShotgunSawnoff", 200,
            "ShotgunShells", 200,
            "ShotgunShells", 50,
            "ShotgunShells", 20,
            "ShotgunShells", 10,
            "ShotgunShellsBox", 50,
            "ShotgunShellsBox", 20,
            "ShotgunShellsBox", 10,
        }
    },

    Bag_SurvivorBag = {
        rolls = 2,
        items = {
            "Bandage", 10,
            "Bandaid", 10,
            "BeefJerky", 20,
            "CannedBolognese", 20,
            "CannedCarrots2", 20,
            "CannedChili", 20,
            "CannedCorn", 20,
            "CannedCornedBeef", 20,
            "CannedMushroomSoup", 20,
            "CannedPeas", 20,
            "CannedPotato2", 20,
            "CannedSardines", 20,
            "CannedTomato2", 20,
            "Crisps", 20,
            "Crisps2", 20,
            "Crisps3", 20,
            "Crisps4", 20,
            "DoubleBarrelShotgun", 8,
            "FirstAidKit", 4,
            "Machete", 4,
            "MarchRidgeMap", 20,
            "MuldraughMap", 20,
            "RiversideMap", 20,
            "RosewoodMap", 20,
            "SewingKit", 2,
            "Shotgun", 10,
            "ShotgunShellsBox", 10,
            "TinnedBeans", 20,
            "TinnedSoup", 20,
            "WestpointMap", 20,
        },
        -- only two map allowed
        maxMap = 2,
        -- this mean 90% chance on normal sandbox settings to have an annoted map
        stashChance = 10,
        fillRand = 0,
    },

    Bag_WeaponBag = {
        rolls = 4,
        items = {
            "BaseballBat", 8,
            "Bullets38Box", 10,
            "Bullets44Box", 10,
            "Bullets45Box", 10,
            "Bullets9mmBox", 20,
            "Bullets9mmBox", 10,
            "Crowbar", 8,
            "DoubleBarrelShotgun", 8,
            "Machete", 4,
            "Pistol", 8,
            "Pistol2", 6,
            "Pistol3", 4,
            "Revolver", 6,
            "Revolver_Long", 4,
            "Revolver_Short", 8,
            "Shotgun", 10,
            "ShotgunShellsBox", 20,
            "ShotgunShellsBox", 10,
        },
        fillRand = 0,
    },

    Bag_WorkerBag = {
        rolls = 1,
        items = {
            "BluePen", 8,
            "Chocolate", 8,
            "Cigarettes", 4,
            "Crisps", 10,
            "PeanutButterSandwich", 10,
            "Peanuts", 8,
            "Pen", 8,
            "Pencil", 10,
            "RedPen", 8,
            "RubberBand", 1,
            "Scissors", 2,
            "SunflowerSeeds", 8,
            "WaterBottleFull", 10,
        },
        fillRand = 0,
    },

    Briefcase = {
        rolls = 1,
        items = {
            "BluePen", 8,
            "BluePen", 8,
            "CordlessPhone", 1,
            "Notebook", 10,
            "Pen", 8,
            "Pen", 8,
            "Pencil", 10,
            "Pencil", 10,
            "RedPen", 8,
            "RedPen", 8,
            "RubberBand", 6,
            "SheetPaper2", 20,
            "SheetPaper2", 20,
            "SheetPaper2", 20,
            "SheetPaper2", 20,
        },
        fillRand = 1,
    },

    FirstAidKit = {
        rolls = 1,
        items = {
            "AlcoholWipes", 100,
            "AlcoholWipes", 40,
            "AlcoholWipes", 20,
            "Bandage", 40,
            "Bandage", 20,
            "Bandage", 20,
            "Bandaid", 100,
            "Bandaid", 40,
            "Bandaid", 40,
            "Bandaid", 20,
            "Coldpack", 20,
            "CottonBalls", 20,
            "CottonBalls", 20,
            "Disinfectant", 20,
            "Scissors", 10,
            "SutureNeedle", 100,
            "SutureNeedle", 20,
            "SutureNeedle", 20,
            "SutureNeedleHolder", 10,
            "Tweezers", 10,
        },
        fillRand = 0,
    },

    Flightcase = {
        rolls = 1,
        items = {
            "GuitarAcoustic", 5,
            "GuitarElectricBlack", 5,
            "GuitarElectricBlue", 5,
            "GuitarElectricRed", 5,
            "GuitarElectricBassBlue", 5,
            "GuitarElectricBassBlack", 5,
            "GuitarElectricBassRed", 5,
        },
        fillRand = 0,
    },

    Garbagebag = {
        rolls = 0,
        items = {

        }
    },

    GroceryBag1 = {
        rolls = 4,
        items = {
            "Bread", 10,
            "Butter", 10,
            "Cereal", 10,
            "Coffee2", 10,
            "EggCarton", 10,
            "Ham", 10,
            "Marinara", 20,
            "Milk", 10,
            "Pasta", 20,
            "PeanutButter", 10,
            "Processedcheese", 10,
        },
    },

    GroceryBag2 = {
        rolls = 4,
        items = {
            "BellPepper", 10,
            "Carrots", 10,
            "Corn", 10,
            "Eggplant", 10,
            "farming.Bacon", 10,
            "farming.Cabbage", 10,
            "Leek", 10,
            "Onion", 10,
            "Ramen", 20,
            "Rice", 10,
            "Tofu", 10,
            "TunaTin", 10,
        },
    },

    GroceryBag3 = {
        rolls = 4,
        items = {
            "CandyPackage", 4,
            "Chocolate", 10,
            "CookieChocolateChip", 4,
            "CookieJelly", 4,
            "Crisps", 10,
            "Crisps2", 10,
            "Crisps3", 10,
            "Crisps4", 10,
            "Cupcake", 4,
            "PopBottle", 20,
            "Popcorn", 10,
        },
    },

    GroceryBag4 = {
        rolls = 4,
        items = {
            "Bread", 10,
            "CannedBolognese", 20,
            "CannedChili", 20,
            "Chicken", 4,
            "Ketchup", 4,
            "Macandcheese", 20,
            "MeatPatty", 10,
            "Mustard", 4,
            "PopBottle", 20,
            "PorkChop", 4,
            "Steak", 2,
        },
    },

    Guitarcase = {
        rolls = 1,
        items = {
            "GuitarAcoustic", 5,
            "GuitarElectricBlack", 5,
            "GuitarElectricBlue", 5,
            "GuitarElectricRed", 5,
            "GuitarElectricBassBlue", 5,
            "GuitarElectricBassBlack", 5,
            "GuitarElectricBassRed", 5,
        },
    },

    Handbag = {
        rolls = 1,
        items = {
            "Cigarettes", 1,
            "Comb", 4,
            "CreditCard", 10,
            "CreditCard", 2,
            "CreditCard", 2,
            "Disc_Retail", 2,
            "Earbuds", 1,
            "Lighter", 1,
            "Lipstick", 6,
            "Magazine", 10,
            "MakeupEyeshadow", 6,
            "MakeupFoundation", 6,
            "Perfume", 4,
            "PillsVitamins", 1,
            "Radio.CDplayer", 1,
            "Wallet", 10,
        },
        fillRand = 0,
    },

    Lunchbox = {
        rolls = 1,
        items = {
            "Apple", 20,
            "Banana", 20,
            "CheeseSandwich", 10,
            "Chocolate", 8,
            "CookieChocolateChip", 10,
            "CookieChocolateChip", 10,
            "Crisps", 10,
            "Crisps2", 10,
            "MuffinFruit", 10,
            "Orange", 20,
            "PeanutButterSandwich", 10,
            "Peanuts", 8,
            "SunflowerSeeds", 8,
        }
    },

    Lunchbox2 = {
        rolls = 1,
        items = {
            "Apple", 20,
            "Banana", 20,
            "CheeseSandwich", 10,
            "Chocolate", 8,
            "CookiesOatmeal", 10,
            "CookiesOatmeal", 10,
            "Crisps3", 10,
            "Crisps4", 10,
            "MuffinFruit", 10,
            "Orange", 20,
            "PeanutButterSandwich", 10,
            "Peanuts", 8,
            "SunflowerSeeds", 8,
        }
    },

    Paperbag = {
        rolls = 1,
        items = {
            
        }
    },

    Paperbag_Jays = {
        rolls = 1,
        items = {
            "ChickenFried", 100,
            "ChickenFried", 50,
            "Fries", 100,
            "Fries", 50,
            "Cornbread", 50,
            "PaperNapkins", 100,
            "PaperNapkins", 50,
            "Straw", 100,
            "Straw", 50,
        }
    },

    Paperbag_Spiffos = {
        rolls = 1,
        items = {
            "Burger", 100,
            "Burger", 50,
            "Fries", 100,
            "Fries", 50,
            "OnionRings", 50,
            "PaperNapkins", 100,
            "PaperNapkins", 50,
            "Straw", 100,
            "Straw", 50,
        }
    },

    PistolCase1 = {
        rolls = 1,
        items = {
            "Pistol", 200,
            "9mmClip", 200,
            "9mmClip", 10,
            "Bullets9mmBox", 200,
            "Bullets9mmBox", 50,
            "Bullets9mmBox", 20,
            "Bullets9mmBox", 10,
        }
    },

    PistolCase2 = {
        rolls = 1,
        items = {
            "Pistol2", 200,
            "45Clip", 200,
            "45Clip", 10,
            "Bullets45Box", 200,
            "Bullets45Box", 50,
            "Bullets45Box", 20,
            "Bullets45Box", 10,
        }
    },

    PistolCase3 = {
        rolls = 1,
        items = {
            "Pistol3", 200,
            "44Clip", 200,
            "44Clip", 10,
            "Bullets44Box", 200,
            "Bullets44Box", 50,
            "Bullets44Box", 20,
            "Bullets44Box", 10,
        }
    },

    Plasticbag = {
        rolls = 0,
        items = {

        }
    },

    Purse = {
        rolls = 1,
        items = {
            "Cigarettes", 1,
            "Comb", 4,
            "CreditCard", 10,
            "CreditCard", 2,
            "CreditCard", 2,
            "Disc_Retail", 2,
            "Earbuds", 1,
            "Lighter", 1,
            "Lipstick", 6,
            "Magazine", 10,
            "MakeupEyeshadow", 6,
            "MakeupFoundation", 6,
            "Perfume", 4,
            "PillsVitamins", 1,
            "Radio.CDplayer", 1,
            "Wallet", 10,
        },
        fillRand = 0,
    },

    RevolverCase1 = {
        rolls = 1,
        items = {
            "Revolver_Short", 200,
            "Bullets38Box", 200,
            "Bullets38Box", 50,
            "Bullets38Box", 20,
            "Bullets38Box", 10,
        }
    },

    RevolverCase2 = {
        rolls = 1,
        items = {
            "Revolver", 200,
            "Bullets45Box", 200,
            "Bullets45Box", 50,
            "Bullets45Box", 20,
            "Bullets45Box", 10,
        }
    },

    RevolverCase3 = {
        rolls = 1,
        items = {
            "Revolver_Long", 200,
            "Bullets44Box", 200,
            "Bullets44Box", 50,
            "Bullets44Box", 20,
            "Bullets44Box", 10,
        }
    },

    RifleCase1 = {
        rolls = 1,
        items = {
            "VarmintRifle", 200,
            "223Box", 200,
            "223Box", 50,
            "223Box", 20,
            "223Box", 10,
        }
    },

    RifleCase2 = {
        rolls = 1,
        items = {
            "HuntingRifle", 200,
            "308Clip", 200,
            "308Clip", 10,
            "308Box", 200,
            "308Box", 50,
            "308Box", 20,
            "308Box", 10,
        }
    },

    RifleCase3 = {
        rolls = 1,
        items = {
            "AssaultRifle2", 200,
            "M14Clip", 200,
            "M14Clip", 10,
            "308Box", 200,
            "308Box", 50,
            "308Box", 20,
            "308Box", 10,
        }
    },

    Satchel = {
        rolls = 1,
        items = {
            "Apple", 20,
            "Banana", 20,
            "PeanutButterSandwich", 10,
            "Pop", 10,
            "WaterBottleFull", 8,
            "Crisps", 10,
            "Notebook", 10,
            "Journal", 10,
            "Magazine", 10,
            "Pencil", 20,
        },
        fillRand = 0,
    },

    SeedBag = {
        rolls = 1,
        items = {
            "farming.CarrotBagSeed", 20,
            "farming.BroccoliBagSeed", 20,
            "farming.RedRadishBagSeed", 20,
            "farming.StrewberrieBagSeed", 20,
            "farming.TomatoBagSeed", 20,
            "farming.PotatoBagSeed", 20,
            "farming.CabbageBagSeed", 20,
        },
        fillRand = 0,
    },

    SewingKit = {
        rolls = 1,
        items = {
            "Thread", 200,
            "Thread", 10,
            "Thread", 10,
            "Thread", 10,
            "Needle", 200,
            "Needle", 10,
            "Needle", 10,
            "Needle", 10,
            "Scissors", 8,
            "KnittingNeedles", 4,
            "Yarn", 4,
        },
        fillRand = 0,
    },

    ShotgunCase1 = {
        rolls = 1,
        items = {
            "Shotgun", 200,
            "ShotgunShellsBox", 200,
            "ShotgunShellsBox", 50,
            "ShotgunShellsBox", 20,
            "ShotgunShellsBox", 10,
        }
    },

    ShotgunCase2 = {
        rolls = 1,
        items = {
            "DoubleBarrelShotgun", 200,
            "ShotgunShellsBox", 200,
            "ShotgunShellsBox", 50,
            "ShotgunShellsBox", 20,
            "ShotgunShellsBox", 10,
        }
    },

    Suitcase = {
        rolls = 1,
        items = {
            "BluePen", 8,
            "BluePen", 8,
            "CordlessPhone", 1,
            "Notebook", 10,
            "Pen", 8,
            "Pen", 8,
            "Pencil", 10,
            "Pencil", 10,
            "RedPen", 8,
            "RedPen", 8,
            "RubberBand", 6,
            "SheetPaper2", 20,
            "SheetPaper2", 20,
            "SheetPaper2", 10,
            "SheetPaper2", 10,
        },
        fillRand = 1,
    },

    Toolbox = {
        rolls = 1,
        items = {
            "BallPeenHammer", 6,
            "ClubHammer", 4,
            "Crowbar", 4,
            "DuctTape", 8,
            "GardenSaw", 10,
            "Hammer", 8,
            "HandTorch", 10,
            "Nails", 10,
            "NailsBox", 10,
            "PipeWrench", 6,
            "Saw", 8,
            "Screwdriver", 10,
            "ScrewsBox", 8,
            "Twine", 10,
            "WoodenMallet", 4,
            "Woodglue", 8,
            "Wrench", 6,
        },
        fillRand = 0,
    },

    Tote = {
        rolls = 1,
        items = {

        }
    },

    -- =====================
    --    Profession Houses
    -- =====================

    BandPractice = {
        crate = {
            procedural = true,
            procList = {
                {name="BandPracticeClothing", min=0, max=99, weightChance=100},
                {name="BandPracticeInstruments", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="BandPracticeClothing", min=0, max=99, weightChance=100},
                {name="BandPracticeInstruments", min=0, max=99, weightChance=100},
            }
        },
    },

    Carpenter = {
        crate = {
            procedural = true,
            procList = {
                {name="CrateCarpentry", min=1, max=99, weightChance=100},
                {name="CrateLumber", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateCarpentry", min=1, max=99, weightChance=100},
                {name="CrateLumber", min=0, max=99, weightChance=100},
            }
        },
    },

    Chef = {
        crate = {
            procedural = true,
            procList = {
                {name="CrateBakingSoda", min=0, max=1, weightChance=100},
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateCanning", min=0, max=1, weightChance=100},
                {name="CrateChocolateChips", min=0, max=1, weightChance=100},
                {name="CrateCocoaPowder", min=0, max=1, weightChance=100},
                {name="CrateCornflour", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateMarinara", min=0, max=1, weightChance=100},
                {name="CrateOilOlive", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CratePancakeMix", min=0, max=1, weightChance=100},
                {name="CratePasta", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateSugarBrown", min=0, max=1, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateBakingSoda", min=0, max=1, weightChance=100},
                {name="CrateButter", min=0, max=1, weightChance=100},
                {name="CrateCanning", min=0, max=1, weightChance=100},
                {name="CrateChocolateChips", min=0, max=1, weightChance=100},
                {name="CrateCocoaPowder", min=0, max=1, weightChance=100},
                {name="CrateCornflour", min=0, max=1, weightChance=100},
                {name="CrateFlour", min=0, max=1, weightChance=100},
                {name="CrateMarinara", min=0, max=1, weightChance=100},
                {name="CrateOilOlive", min=0, max=1, weightChance=100},
                {name="CrateOilVegetable", min=0, max=1, weightChance=100},
                {name="CratePancakeMix", min=0, max=1, weightChance=100},
                {name="CratePasta", min=0, max=1, weightChance=100},
                {name="CrateSugar", min=0, max=1, weightChance=100},
                {name="CrateSugarBrown", min=0, max=1, weightChance=100},
            }
        },
    },

    Electrician = {
        crate = {
            procedural = true,
            procList = {
                {name="CrateElectronics", min=0, max=99, weightChance=100},
                {name="EngineerTools", min=1, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateElectronics", min=0, max=99, weightChance=100},
                {name="EngineerTools", min=1, max=99, weightChance=100},
            }
        },
    },

    Farmer = {
        crate = {
            procedural = true,
            procList = {
                {name="CrateFarming", min=1, max=99, weightChance=100},
                {name="CrateFertilizer", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="CrateFarming", min=1, max=99, weightChance=100},
                {name="CrateFertilizer", min=0, max=99, weightChance=100},
            }
        },
    },

    Nurse = {
        crate = {
            procedural = true,
            procList = {
                {name="MedicalClinicDrugs", min=0, max=99, weightChance=100},
                {name="MedicalClinicTools", min=0, max=99, weightChance=100},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MedicalClinicDrugs", min=0, max=99, weightChance=100},
                {name="MedicalClinicTools", min=0, max=99, weightChance=100},
            }
        },
    },

    -- =====================
    --    Caches
    -- =====================

    GunCache1 = {
        GunBox = {
            rolls = 4,
            items = {
                "Bullets38Box", 20,
                "Bullets38Box", 10,
                "Bullets44Box", 20,
                "Bullets44Box", 10,
                "Bullets45Box", 20,
                "Bullets45Box", 10,
                "Bullets9mm", 20,
                "Bullets9mm", 10,
                "Bullets9mmBox", 20,
                "Bullets9mmBox", 10,
                "Pistol", 20,
                "Pistol2", 20,
                "Pistol3", 4,
                "Revolver", 6,
                "Revolver_Long", 4,
                "Revolver_Short", 8,
            },
            dontSpawnAmmo = true,
        },

        counter = {
            rolls = 4,
            items = {
                "Pistol", 8,
                "Pistol2", 6,
                "Pistol3", 4,
                "Revolver", 6,
                "Revolver_Long", 4,
                "Revolver_Short", 8,
            },
        },

        Bag_DuffelBagTINT = {
            rolls = 4,
            items = {
                "DoubleBarrelShotgun", 8,
                "Pistol", 50,
                "Pistol2", 50,
                "Pistol3", 4,
                "Revolver", 6,
                "Revolver_Long", 4,
                "Revolver_Short", 8,
                "Shotgun", 10,
            },
            fillRand = 1,
        },
    },

    GunCache2 = {
        GunBox = {
            rolls = 4,
            items = {
                "Bullets9mm", 50,
                "Bullets9mm", 20,
                "Bullets9mm", 20,
                "Bullets9mm", 10,
                "Bullets9mm", 10,
                "Bullets9mmBox", 50,
                "Bullets9mmBox", 20,
                "Bullets9mmBox", 20,
                "Bullets9mmBox", 10,
                "Bullets9mmBox", 10,
                "Pistol", 50,
                "Pistol2", 50,
                "Pistol3", 4,
                "Revolver", 6,
                "Revolver_Long", 4,
                "Revolver_Short", 8,
            },
            dontSpawnAmmo = true,
        },

        Bag_DuffelBagTINT = {
            rolls = 2,
            items = {
                "Bullets38Box", 20,
                "Bullets38Box", 10,
                "Bullets44Box", 20,
                "Bullets44Box", 10,
                "Bullets45Box", 20,
                "Bullets45Box", 10,
                "Bullets9mm", 50,
                "Bullets9mm", 20,
                "Bullets9mm", 20,
                "Bullets9mm", 10,
                "Bullets9mm", 10,
                "Bullets9mmBox", 50,
                "Bullets9mmBox", 20,
                "Bullets9mmBox", 20,
                "Bullets9mmBox", 10,
                "Bullets9mmBox", 10,
                "Pistol", 50,
                "Pistol2", 50,
                "Pistol3", 4,
                "Revolver", 6,
                "Revolver_Long", 4,
                "Revolver_Short", 8,
            },
            fillRand=1,
        },
    },

    SafehouseLoot = {
        crate = {
            procedural = true,
            procList = {
                {name="MeleeWeapons", min=1, max=2, weightChance=100},
                {name="FirearmWeapons", min=0, max=1, weightChance=100},
                {name="SafehouseTraps", min=0, max=1, weightChance=20},
            }
        },
        medicine = {
            procedural = true,
            procList = {
                {name="SafehouseMedical", min=0, max=99},
            }
        },
        metal_shelves = {
            procedural = true,
            procList = {
                {name="MeleeWeapons", min=1, max=2, weightChance=100},
                {name="FirearmWeapons", min=0, max=1, weightChance=100},
                {name="SafehouseTraps", min=0, max=1, weightChance=20},
            }
        },
    },

    ShotgunCache1 = {
        ShotgunBox = {
            rolls = 1,
            items = {
                "Shotgun", 200,
                "ShotgunShells", 200,
                "ShotgunShells", 50,
                "ShotgunShells", 20,
                "ShotgunShells", 10,
                "ShotgunShellsBox", 50,
                "ShotgunShellsBox", 20,
                "ShotgunShellsBox", 10,
            }
        },

        Bag_DuffelBagTINT = {
            rolls = 1,
            items = {
                "ShotgunSawnoff", 200,
                "ShotgunShells", 200,
                "ShotgunShells", 50,
                "ShotgunShells", 20,
                "ShotgunShells", 10,
                "ShotgunShellsBox", 50,
                "ShotgunShellsBox", 20,
                "ShotgunShellsBox", 10,
            },
            fillRand=0,
        },
    },

    ShotgunCache2 = {
        ShotgunBox = {
            rolls = 1,
            items = {
                "Shotgun", 200,
                "ShotgunShells", 200,
                "ShotgunShells", 50,
                "ShotgunShells", 20,
                "ShotgunShells", 10,
                "ShotgunShellsBox", 50,
                "ShotgunShellsBox", 20,
                "ShotgunShellsBox", 10,
            }
        },

        counter = {
            rolls = 4,
            items = {
                "Shotgun", 10,
                "ShotgunShells", 50,
                "ShotgunShells", 20,
                "ShotgunShells", 10,
                "ShotgunShellsBox", 20,
                "ShotgunShellsBox", 10,
            }
        },
    },

    SurvivorCache1 = {
        counter = {
            procedural = true,
            procList = {
                {name="KitchenCannedFood", min=1, max=7, weightChance=100},
                {name="KitchenDryFood", min=1, max=2, weightChance=100},
                {name="MeleeWeapons", min=1, max=2, weightChance=100},
                {name="FirearmWeapons", min=1, max=1, weightChance=100},
            },
        },

        SurvivorCrate = {
            rolls = 4,
            items = {
                "Bag_ShotgunBag", 1,
                "Bag_ShotgunDblBag", 1,
                "Bag_ShotgunDblSawnoffBag", 1,
                "Bag_ShotgunSawnoffBag", 1,
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
                "Machete", 1,
                "PeanutButter", 10,
                "TinnedBeans", 10,
                "TinnedSoup", 10,
                "TunaTin", 10,
                "WaterBottleFull", 10,
                "WaterBottleFull", 10,
            }
        },
    },

    SurvivorCache2 = {
        counter = {
            procedural = true,
            procList = {
                {name="KitchenCannedFood", min=1, max=7, weightChance=100},
                {name="KitchenDryFood", min=1, max=2, weightChance=100},
                {name="MeleeWeapons", min=1, max=2, weightChance=100},
                {name="FirearmWeapons", min=1, max=1, weightChance=100},
            },
        },

        SurvivorCrate = {
            rolls = 4,
            items = {
                "Bag_ShotgunBag", 1,
                "Bag_ShotgunDblBag", 1,
                "Bag_ShotgunDblSawnoffBag", 1,
                "Bag_ShotgunSawnoffBag", 1,
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
                "Machete", 1,
                "PeanutButter", 10,
                "TinnedBeans", 10,
                "TinnedSoup", 10,
                "TunaTin", 10,
                "WaterBottleFull", 10,
                "WaterBottleFull", 10,
            }
        },
    },

    ToolsCache1 = {
        ToolsBox = {
            rolls = 4,
            items = {
                "Axe", 1,
                "ClubHammer", 4,
                "DuctTape", 8,
                "GardenSaw", 10,
                "Hammer", 8,
                "HandAxe", 4,
                "LeadPipe", 10,
                "Machete", 1,
                "Nails", 20,
                "Nails", 10,
                "NailsBox", 10,
                "PickAxe", 4,
                "PipeWrench", 6,
                "Rope", 10,
                "Saw", 8,
                "Screwdriver", 10,
                "Shovel", 4,
                "Shovel2", 4,
                "Sledgehammer", 0.01,
                "Sledgehammer2", 0.01,
                "Tarp", 10,
                "Twine", 10,
                "WoodAxe", 1,
                "WoodenMallet", 4,
                "Woodglue", 8,
                "Wrench", 6,
            },
        },

        counter = {
            rolls = 4,
            items = {
                "Axe", 1,
                "ClubHammer", 4,
                "DuctTape", 8,
                "farming.HandShovel", 10,
                "GardenSaw", 10,
                "Hammer", 8,
                "HandAxe", 1,
                "HandFork", 1,
                "HandScythe", 1,
                "LeadPipe", 10,
                "Nails", 20,
                "Nails", 10,
                "NailsBox", 10,
                "PipeWrench", 6,
                "Rope", 10,
                "Saw", 8,
                "Shovel", 4,
                "Shovel2", 4,
                "Tarp", 10,
                "Twine", 10,
                "WoodAxe", 1,
                "WoodenMallet", 4,
                "Woodglue", 8,
                "Wrench", 6,
            },
        },

        Bag_DuffelBagTINT = {
            rolls = 4,
            items = {
                "Axe", 1,
                "ClubHammer", 4,
                "DuctTape", 8,
                "GardenSaw", 10,
                "Glue", 8,
                "Hammer", 10,
                "HandAxe", 1,
                "LeadPipe", 10,
                "NailsBox", 20,
                "NailsBox", 10,
                "PipeWrench", 6,
                "Rope", 10,
                "Saw", 8,
                "Tarp", 10,
                "Twine", 10,
                "WoodAxe", 1,
                "WoodenMallet", 4,
                "Woodglue", 8,
                "Wrench", 6,
            },
            fillRand = 1,
        },
    },
}

table.insert(Distributions, 1, distributionTable);

--for mod compat:
SuburbsDistributions = distributionTable;