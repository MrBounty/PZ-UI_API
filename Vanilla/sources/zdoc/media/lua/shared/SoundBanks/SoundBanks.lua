defaultVoiceReverb = 40.0

defaultFootstepReverb = 170.0
defaultZombieFootstepReverb = 100
defaultFirearmReverb = 1.0
defaultFirearmVolume = 0.2
defaultWeaponReverb = 0.01
defaultWeaponVolume = 0.45
defaultFootstepVolume = 0.032
defaultZombieFootstepVolume = 0.14
defaultVoiceVolume = 0.12

defaultGlassReverb = 15;

defaultZombieRange = 25
defaultZombieReverbRange = 5

-- The game was using getOptionAmbientVolume() which is from 0-10, now it uses getOptionAmbientVolume()/10
local FIX = 10
ambientDefaultGain = 0.2 * FIX;
ambientScreamGain = 0.2 * FIX;
defaultTriggerRange = 175;
ambientReverbFactor = 2;


---@class voiceTable
voiceTable = {

    { alias = "MaleZombieIdle", sound = "MaleZombieIdle", priority = 1,  },
    { alias = "FemaleZombieIdle", sound = "FemaleZombieIdle", priority = 1,  },
    { alias = "MaleZombieHurt", sound = "MaleZombieHurt", priority = 2,  },
    { alias = "FemaleZombieHurt", sound = "FemaleZombieHurt", priority = 2,  },
    { alias = "MaleZombieAttack", sound = "MaleZombieAttack", priority = 3,  },
    { alias = "FemaleZombieAttack", sound = "FemaleZombieAttack", priority = 3,  },
    { alias = "MaleZombieDeath", sound = "MaleZombieDeath", priority = 4,  },
    { alias = "FemaleZombieDeath", sound = "FemaleZombieDeath", priority = 4,  },
    { alias = "FemaleBeingEatenDeath", sound = "FemaleBeingEatenDeath", priority = 4,  },
    { alias = "MaleBeingEatenDeath", sound = "MaleBeingEatenDeath", priority = 4,  },

    { alias = "FemaleHeavyBreathPanic", sound = "FemaleHeavyBreathPanic", priority = 3,  },
    { alias = "MaleHeavyBreathPanic", sound = "MaleHeavyBreathPanic", priority = 3,  },
    { alias = "MaleHeavyBreath", sound = "MaleHeavyBreath", priority = 3,  },
    { alias = "FemaleHeavyBreath", sound = "FemaleHeavyBreath", priority = 3,  },
}

---@class footStepTable
footStepTable = {
    { alias = "zombie_m", grass="ZombieFootstepGrass", wood="ZombieFootstepWood", concrete="ZombieFootstepConcrete", upstairs="ZombieFootstepUpstairs" },
    { alias = "human_m", grass="HumanFootstepGrass", wood="HumanFootstepWood", concrete="HumanFootstepConcrete", upstairs="HumanFootstepUpstairs" },
}

---@class baseSoundTable
baseSoundTable = {
--    { alias = "stormy9mmClick", file = "media/sound/stormy9mmClick.ogg", gain = 1, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultFirearmReverb, priority = 5},
--    { alias = "stormy9mmClipEject", file = "media/sound/stormy9mmClipEject.ogg", gain = 1, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultFirearmReverb, priority = 5},
--    { alias = "stormy9mmClipLoad", file = "media/sound/stormy9mmClipLoad.ogg", gain = 1, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultFirearmReverb, priority = 5},
--    { alias = "lockDoor", file = "media/sound/lockDoor.ogg", gain = 0.5, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultFirearmReverb, priority = 5},
--    { alias = "unlockDoor", file = "media/sound/unlockDoor.ogg", gain = 0.5, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultFirearmReverb, priority = 5},
--    { alias = "dooropen", file = "media/sound/dooropen.wav", gain = 0.4, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultFirearmReverb, priority = 5},
--    { alias = "doorclose", file = "media/sound/doorclose.wav", gain = 0.12, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultFirearmReverb, priority = 5},
--    { alias = "doorlocked", file = "media/sound/doorlocked.wav", gain = 0.6, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultFirearmReverb, priority = 5},
--    { alias = "bigExplosion", file = "media/sound/bigExplosion.ogg", gain = 0.6, minrange = 0.001,  maxrange = 50,  maxreverbrange = 50,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "lightswitch", file = "media/sound/lightswitch.wav", gain = 0.2, minrange = 0.001,  maxrange = 6,  maxreverbrange = 6,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "RemoveBrokenGlass", file = "media/sound/RemoveBrokenGlass.ogg", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "shoveling", file = "media/sound/shoveling.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "getFish", file = "media/sound/getFish.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "waterSplash", file = "media/sound/waterSplash.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "breakdoor", file = "media/sound/breakdoor.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "crackwood", file = "media/sound/crackwood.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "PZ_MetalSnap", file = "media/sound/PZ_MetalSnap.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "PZ_WoodSnap", file = "media/sound/PZ_WoodSnap.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },

--    { alias = "PZ_BidTrapped_wood", file = "media/sound/PZ_BidTrapped_wood_01.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "PZ_BidTrapped_wood", file = "media/sound/PZ_BidTrapped_wood_02.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "PZ_BidTrapped_wood", file = "media/sound/PZ_BidTrapped_wood_03.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "PZ_AnimalTrapped_wood", file = "media/sound/PZ_AnimalTrapped_wood_01.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "PZ_AnimalTrapped_wood", file = "media/sound/PZ_AnimalTrapped_wood_02.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "PZ_AnimalTrapped_wood", file = "media/sound/PZ_AnimalTrapped_wood_03.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "PZ_AnimalTrapped_wood", file = "media/sound/PZ_AnimalTrapped_wood_04.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "PZ_AnimalTrappedMetal", file = "media/sound/PZ_AnimalTrappedMetal_01.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "PZ_AnimalTrappedMetal", file = "media/sound/PZ_AnimalTrappedMetal_02.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },
--    { alias = "PZ_AnimalTrappedMetal", file = "media/sound/PZ_AnimalTrappedMetal_03.wav", gain = 0.32, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 5 },

--    { alias = "PZ_Fire", file = "media/sound/PZ_Fire.wav", gain = 1, minrange = 0.001, maxrange = 50,  maxreverbrange = 50,  reverbfactor = defaultFirearmReverb, priority = 5},
--    { alias = "PZ_Swallowing", file = "media/sound/PZ_Swallowing.wav", gain = 0.3, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },

--    { alias = "chopdoor", file = "media/sound/chopdoor.wav", gain = 0.58, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieReverbRange,  reverbfactor = 1, priority = 4 },
--    { alias = "PZ_ChopTree", file = "media/sound/PZ_ChopTree1.wav", gain = 1, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieReverbRange,  reverbfactor = 1, priority = 4 },
--    { alias = "PZ_ChopTree", file = "media/sound/PZ_ChopTree2.wav", gain = 1, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieReverbRange,  reverbfactor = 1, priority = 4 },
--   { alias = "PZ_ChopTree", file = "media/sound/PZ_ChopTree3.wav", gain = 1, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieReverbRange,  reverbfactor = 1, priority = 4 },
--    { alias = "PZ_ChopTree", file = "media/sound/PZ_ChopTree4.wav", gain = 1, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieReverbRange,  reverbfactor = 1, priority = 4 },

--    { alias = "PZ_Saw", file = "media/sound/PZ_Saw.wav", gain = 0.3 , minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_Hammer", file = "media/sound/PZ_Hammer.wav", gain = 0.3 , minrange = 0.001,  maxrange = 30,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },

--    { alias = "female_heavybreathpanic", file = "media/sound/female_heavybreathpanic.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "male_heavybreathpanic", file = "media/sound/male_heavybreathpanic.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },

--    { alias = "female_heavybreath", file = "media/sound/female_outofbreath_01.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "female_heavybreath", file = "media/sound/female_outofbreath_02.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "female_heavybreath", file = "media/sound/female_outofbreath_03.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "female_heavybreath", file = "media/sound/female_outofbreath_04.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },

--    { alias = "male_heavybreath", file = "media/sound/male_outofbreath_01.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "male_heavybreath", file = "media/sound/male_outofbreath_02.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "male_heavybreath", file = "media/sound/male_outofbreath_03.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "male_heavybreath", file = "media/sound/male_outofbreath_04.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "male_heavybreath", file = "media/sound/male_outofbreath_05.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "male_heavybreath", file = "media/sound/male_outofbreath_06.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "male_heavybreath", file = "media/sound/male_outofbreath_07.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "male_heavybreath", file = "media/sound/male_outofbreath_08.wav", gain = defaultVoiceVolume * 0.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },

    --
--    { alias = "footstep_upstairs", file = "media/sound/footstep_upstairs_01.wav", gain = 0.8, minrange = 0.001, maxrange = 15,  maxreverbrange = 40,  reverbfactor = 0, priority = 0 },
--    { alias = "footstep_upstairs", file = "media/sound/footstep_upstairs_02.wav", gain = 0.8, minrange = 0.001, maxrange = 15,  maxreverbrange = 40,  reverbfactor = 0, priority = 0 },
--    { alias = "footstep_upstairs", file = "media/sound/footstep_upstairs_03.wav", gain = 0.8, minrange = 0.001, maxrange = 15,  maxreverbrange = 40,  reverbfactor = 0, priority = 0 },
--    { alias = "footstep_upstairs", file = "media/sound/footstep_upstairs_04.wav", gain = 0.8, minrange = 0.001, maxrange = 15,  maxreverbrange = 40,  reverbfactor = 0, priority = 0 },
--    { alias = "footstep_upstairs", file = "media/sound/footstep_upstairs_05.wav", gain = 0.8, minrange = 0.001, maxrange = 15,  maxreverbrange = 40,  reverbfactor = 0, priority = 0 },
--    { alias = "footstep_upstairs", file = "media/sound/footstep_upstairs_06.wav", gain = 0.8, minrange = 0.001, maxrange = 15,  maxreverbrange = 40,  reverbfactor = 0, priority = 0 },
--    { alias = "footstep_upstairs", file = "media/sound/footstep_upstairs_07.wav", gain = 0.8, minrange = 0.001, maxrange = 15,  maxreverbrange = 40,  reverbfactor = 0, priority = 0 },
--    { alias = "footstep_upstairs", file = "media/sound/footstep_upstairs_08.wav", gain = 0.8, minrange = 0.001, maxrange = 15,  maxreverbrange = 40,  reverbfactor = 0, priority = 0 },

--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_01.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_02.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_03.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_04.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_05.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_06.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_07.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_08.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_09.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_10.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_11.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
--    { alias = "zombie_footstep_m", file = "media/sound/zombie_footstep_m_12.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 14,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, priority = 0 },
    --{ alias = "footscrape_male_indoor_close", file = "media/sound/footscrape_male_concrete_close.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 40,  reverbfactor = defaultZombieFootstepReverb, },

    --

--    { alias = "9mmShot", file = "media/sound/9mmShot.wav", gain = defaultFirearmVolume, minrange = 0.001, maxrange = 150,  maxreverbrange = 150,  reverbfactor = defaultFirearmReverb, priority = 9},
--    { alias = "shotgun", file = "media/sound/shotgun.wav", gain = defaultFirearmVolume, minrange = 0.001, maxrange = 150,  maxreverbrange = 150,  reverbfactor = defaultFirearmReverb,  priority = 9 },
--    { alias = "shotgun2", file = "media/sound/shotgun2.wav", gain = defaultFirearmVolume, minrange = 0.001, maxrange = 150,  maxreverbrange = 150,  reverbfactor = defaultFirearmReverb,  priority = 9 },
--    { alias = "batSwing", file = "media/sound/batSwing.wav", gain = defaultWeaponVolume, minrange = 0.001, maxrange = 10,  maxreverbrange = 150,  reverbfactor = defaultWeaponReverb, priority = 9 },
--    { alias = "PZ_HeadSmash", file = "media/sound/PZ_HeadSmash_01.wav", gain = 0.32, minrange = 0.001, maxrange = 20,  maxreverbrange = 150,  reverbfactor = defaultWeaponReverb, priority = 9 },
--    { alias = "PZ_HeadSmash", file = "media/sound/PZ_HeadSmash_02.wav", gain = 0.32, minrange = 0.001, maxrange = 20,  maxreverbrange = 150,  reverbfactor = defaultWeaponReverb, priority = 9 },

--    { alias = "PZ_HeadStab", file = "media/sound/PZ_HeadStab_02.wav", gain = 0.32, minrange = 0.001, maxrange = 20,  maxreverbrange = 150,  reverbfactor = defaultWeaponReverb, priority = 9 },
--    { alias = "zombieImpact", file = "media/sound/zombieImpact.wav", gain = 0.08, minrange = 0.001, maxrange = 20,  maxreverbrange = 150,  reverbfactor = defaultWeaponReverb, priority = 9 },


--    { alias = "PZ_MaleZombieEating", file = "media/sound/PZ_MaleZombieEating.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 9 },
--    { alias = "PZ_FemaleZombieEating", file = "media/sound/PZ_FemaleZombieEating.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 9 },

--    { alias = "PZ_FemaleBeingEaten_Death", file = "media/sound/PZ_FemaleBeingEaten_Death.wav", gain = defaultVoiceVolume*2, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 9 },
--    { alias = "PZ_MaleBeingEaten_Death", file = "media/sound/PZ_MaleBeingEaten_Death.wav", gain = defaultVoiceVolume*2, minrange = 0.001, maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 9 },

--    { alias = "bushes", file = "media/sound/bushes1.wav", gain = 0.1, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = 0, priority = 1 },
--    { alias = "bushes", file = "media/sound/bushes2.wav", gain = 0.1, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = 0, priority = 1 },
--    { alias = "bushes", file = "media/sound/bushes3.wav", gain = 0.1, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = 0, priority = 1 },

--    { alias = "smashwindow", file = "media/sound/smashwindow.wav", gain = 0.2, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 9 },
--    { alias = "thumpa2", file = "media/sound/thumpa2.wav", gain = 0.5, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpa2", file = "media/sound/thump1.wav", gain = 0.4, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpa2", file = "media/sound/thump2.wav", gain = 0.4, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpa2", file = "media/sound/thump3.wav", gain = 0.5, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpa2", file = "media/sound/thump4.wav", gain = 0.6, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpa2", file = "media/sound/thump5.wav", gain = 0.6, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpa2", file = "media/sound/thump6.wav", gain = 0.6, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpsqueak", file = "media/sound/thumpsqueak1.wav", gain = 0.3, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpsqueak", file = "media/sound/thumpsqueak2.wav", gain = 0.3, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpsqueak", file = "media/sound/thumpsqueak3.wav", gain = 0.3, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpsqueak", file = "media/sound/thumpsqueak4.wav", gain = 0.3, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpsqueak", file = "media/sound/thumpsqueak5.wav", gain = 0.3, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },
--    { alias = "thumpsqueak", file = "media/sound/thumpsqueak6.wav", gain = 0.3, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 6 },

    -- creepy around ambients
--    { alias = "windowwind", file = "media/sound/windowwind.wav", gain = 0.1 * FIX, minrange = 0.001, maxrange = 6,  maxreverbrange = 6,  reverbfactor = defaultVoiceReverb, priority = 1, looped = true },
--    { alias = "waterdrip", file = "media/sound/waterdrip2.wav", gain = 0.05 * FIX, minrange = 0.001, maxrange = 7,  maxreverbrange = 1,  reverbfactor = defaultVoiceReverb, priority = 1, looped = true },
--    { alias = "wooddoorcreaks", file = "media/sound/wooddoorcreaks.wav", gain = 0.3 * FIX, minrange = 0.001, maxrange = 12,  maxreverbrange = 12,  reverbfactor = defaultVoiceReverb, priority = 1, looped = true },
--    { alias = "windowrattle", file = "media/sound/windowrattle.wav", gain = 0.1 * FIX, minrange = 0.001, maxrange = 7,  maxreverbrange = 7,  reverbfactor = defaultVoiceReverb, priority = 1, looped = true },
--    { alias = "fridgehum", file = "media/sound/fridgehum.wav", gain = 0.03 * FIX, minrange = 0.001,  maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 1, looped = true },

--    { alias = "whispers", file = "media/sound/insane1.wav", gain = 0.005 * FIX,   minrange = 0.001,  maxrange = 50,  maxreverbrange = 50,  reverbfactor = defaultVoiceReverb, priority = 0, looped = true },
--    { alias = "whispers", file = "media/sound/insane2.wav", gain = 0.005 * FIX,   minrange = 0.001,  maxrange = 50,  maxreverbrange = 50,  reverbfactor = defaultVoiceReverb, priority = 0, looped = true },
--    { alias = "whispers", file = "media/sound/insane3.wav", gain = 0.024 * FIX,   minrange = 0.001,  maxrange = 50,  maxreverbrange = 50,  reverbfactor = defaultVoiceReverb, priority = 0, looped = true },
--    { alias = "whispers", file = "media/sound/insane4.wav", gain = 0.033 * FIX,   minrange = 0.001,  maxrange = 50,  maxreverbrange = 50,  reverbfactor = defaultVoiceReverb, priority = 0, looped = true },
--    { alias = "bird",     file = "media/sound/bird1.wav",   gain = 0.1 * FIX,     minrange = 0.001,  maxrange = 30,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor,  priority = 0, looped = true },
--    { alias = "bird",     file = "media/sound/bird2.wav",   gain = 0.1 * FIX,     minrange = 0.001,  maxrange = 30,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor,  priority = 0, looped = true },
--    { alias = "bird",     file = "media/sound/bird4.wav",   gain = 0.1 * FIX,     minrange = 0.001,  maxrange = 30,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor,  priority = 0, looped = true },
--    { alias = "bird",     file = "media/sound/bird5.wav",   gain = 0.1 * FIX,     minrange = 0.001,  maxrange = 30,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor,  priority = 0, looped = true },
--    { alias = "bird",     file = "media/sound/bird6.wav",   gain = 0.1 * FIX,     minrange = 0.001,  maxrange = 30,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor,  priority = 0, looped = true },
--    { alias = "bird",     file = "media/sound/bird8.wav",   gain = 0.1 * FIX,     minrange = 0.001,  maxrange = 30,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor,  priority = 0, looped = true },
--    { alias = "thunder", file = "media/sound/thunder1.wav", gain = 1, minrange = 0.001, maxrange = 200,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 5 },
--    { alias = "thunder", file = "media/sound/thunder2.wav", gain = 1, minrange = 0.001, maxrange = 200,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 5 },
--    { alias = "thunder", file = "media/sound/thunder3.wav", gain = 1, minrange = 0.001, maxrange = 200,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 5 },
--    { alias = "rumbleThunder", file = "media/sound/rumbleThunder1.wav", gain = 1, minrange = 0.001, maxrange = 100,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 5 },
--    { alias = "rumbleThunder", file = "media/sound/rumbleThunder2.wav", gain = 1, minrange = 0.001, maxrange = 100,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 5 },
--    { alias = "rumbleThunder", file = "media/sound/rumbleThunder3.wav", gain = 1, minrange = 0.001, maxrange = 100,  maxreverbrange = 25,  reverbfactor = defaultGlassReverb, priority = 5 },

    --getFMODSoundBank():addSound(v2.alias, v2.file, v2.gain, v2.minrange, v2.maxrange, v2.maxreverbrange, v2.reverbfactor, v2.priority, v2.looped);

--    { alias = "zombiedeath", file = "media/sound/PZ_MaleDeath_01.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "zombiedeath", file = "media/sound/PZ_MaleDeath_02.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "zombiedeath", file = "media/sound/PZ_MaleDeath_03.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "zombiedeath", file = "media/sound/PZ_MaleDeath_04.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "zombiedeath", file = "media/sound/PZ_MaleDeath_05.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "zombiedeath", file = "media/sound/PZ_MaleDeath_06.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "zombiedeath", file = "media/sound/PZ_MaleDeath_07.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "fzombiedeath", file = "media/sound/PZ_Female_zombie_Death_01.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "fzombiedeath", file = "media/sound/PZ_Female_zombie_Death_02.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "fzombiedeath", file = "media/sound/PZ_Female_zombie_Death_03.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "fzombiedeath", file = "media/sound/PZ_Female_zombie_Death_04.wav", gain = defaultVoiceVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultVoiceReverb, priority = 6 },

--    { alias = "zombierand", file = "media/sound/zombierand1.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "zombierand", file = "media/sound/zombierand2.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "zombierand", file = "media/sound/zombierand3.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "zombierand", file = "media/sound/zombierand4.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "zombierand", file = "media/sound/zombierand5.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "zombierand", file = "media/sound/zombierand6.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "zombierand", file = "media/sound/zombierand7.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "zombierand", file = "media/sound/zombierand8.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "zombierand", file = "media/sound/zombierand9.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "fzombierand", file = "media/sound/fzombierand1.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "fzombierand", file = "media/sound/fzombierand2.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "fzombierand", file = "media/sound/fzombierand3.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "fzombierand", file = "media/sound/fzombierand4.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "fzombierand", file = "media/sound/fzombierand5.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "fzombierand", file = "media/sound/fzombierand6.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "fzombierand", file = "media/sound/fzombierand7.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "fzombierand", file = "media/sound/fzombierand8.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "fzombierand", file = "media/sound/fzombierand9.wav", gain = defaultVoiceVolume * 0.6, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "zombiehit", file = "media/sound/zombiehit1.wav", gain = defaultVoiceVolume, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "zombiehit", file = "media/sound/zombiehit2.wav", gain = defaultVoiceVolume, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "zombiehit", file = "media/sound/zombiehit3.wav", gain = defaultVoiceVolume, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "zombiehit", file = "media/sound/zombiehit4.wav", gain = defaultVoiceVolume, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "zombiehit", file = "media/sound/zombiehit5.wav", gain = defaultVoiceVolume, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "fzombiehit", file = "media/sound/fzombiehit1.wav", gain = defaultVoiceVolume, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "fzombiehit", file = "media/sound/fzombiehit2.wav", gain = defaultVoiceVolume, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "fzombiehit", file = "media/sound/fzombiehit3.wav", gain = defaultVoiceVolume, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "fzombiehit", file = "media/sound/fzombiehit4.wav", gain = defaultVoiceVolume, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 6 },
--    { alias = "fzombiehit", file = "media/sound/fzombiehit5.wav", gain = defaultVoiceVolume, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_MaleZombie", file = "media/sound/PZ_MaleZombie_01.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_MaleZombie", file = "media/sound/PZ_MaleZombie_02.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_MaleZombie", file = "media/sound/PZ_MaleZombie_03.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_MaleZombie", file = "media/sound/PZ_MaleZombie_04.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_MaleZombie", file = "media/sound/PZ_MaleZombie_05.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_MaleZombie", file = "media/sound/PZ_MaleZombie_06.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_MaleZombie", file = "media/sound/PZ_MaleZombie_07.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_MaleZombie", file = "media/sound/PZ_MaleZombie_08.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_Female_zombie", file = "media/sound/PZ_Female_zombie_01.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_Female_zombie", file = "media/sound/PZ_Female_zombie_02.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_Female_zombie", file = "media/sound/PZ_Female_zombie_03.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_Female_zombie", file = "media/sound/PZ_Female_zombie_04.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_Female_zombie", file = "media/sound/PZ_Female_zombie_05.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_Female_zombie", file = "media/sound/PZ_Female_zombie_06.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_Female_zombie", file = "media/sound/PZ_Female_zombie_07.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },
--    { alias = "PZ_Female_zombie", file = "media/sound/PZ_Female_zombie_08.wav", gain = defaultVoiceVolume * 1.2, minrange = 0.001,  maxrange = defaultZombieRange,  maxreverbrange = defaultZombieRange,  reverbfactor = defaultVoiceReverb, priority = 2 },

--    { alias = "zombiebite", file = "media/sound/zombiebite.wav", gain = 0.6, minrange = 0.001, maxrange = 10, maxreverbrange = 10, reverbfactor = 1, priority = 9 },
--    { alias = "zombiescratch", file = "media/sound/zombiescratch.wav", gain = 0.3, minrange = 0.001, maxrange = 10, maxreverbrange = 10, reverbfactor = 1, priority = 9 },

--    { alias = "PZ_FootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_01.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_FootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_03.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_FootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_04.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_FootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_05.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_FootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_06.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_FootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_07.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_FootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_08.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_WoodFoot", file = "media/sound/PZ_WoodFoot_01.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_WoodFoot", file = "media/sound/PZ_WoodFoot_02.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_WoodFoot", file = "media/sound/PZ_WoodFoot_03.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_WoodFoot", file = "media/sound/PZ_WoodFoot_04.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_WoodFoot", file = "media/sound/PZ_WoodFoot_05.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_WoodFoot", file = "media/sound/PZ_WoodFoot_06.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_WoodFoot", file = "media/sound/PZ_WoodFoot_07.wav", gain = defaultFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_grassFoot", file = "media/sound/PZ_grassFoot_01.wav", gain = defaultFootstepVolume * 1.2, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_grassFoot", file = "media/sound/PZ_grassFoot_02.wav", gain = defaultFootstepVolume * 1.2, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_grassFoot", file = "media/sound/PZ_grassFoot_03.wav", gain = defaultFootstepVolume * 1.2, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_grassFoot", file = "media/sound/PZ_grassFoot_04.wav", gain = defaultFootstepVolume * 1.2, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },
--    { alias = "PZ_grassFoot", file = "media/sound/PZ_grassFoot_05.wav", gain = defaultFootstepVolume * 1.2, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 1 },

--    { alias = "PZ_ZFootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_01.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZFootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_03.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZFootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_04.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZFootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_05.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZFootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_06.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZFootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_07.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZFootSteps_Concrete", file = "media/sound/PZ_FootSteps_Concrete_08.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZWoodFoot", file = "media/sound/PZ_WoodFoot_01.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZWoodFoot", file = "media/sound/PZ_WoodFoot_02.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZWoodFoot", file = "media/sound/PZ_WoodFoot_03.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZWoodFoot", file = "media/sound/PZ_WoodFoot_04.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZWoodFoot", file = "media/sound/PZ_WoodFoot_05.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZWoodFoot", file = "media/sound/PZ_WoodFoot_06.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZWoodFoot", file = "media/sound/PZ_WoodFoot_07.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZgrassFoot", file = "media/sound/PZ_grassFoot_01.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZgrassFoot", file = "media/sound/PZ_grassFoot_02.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZgrassFoot", file = "media/sound/PZ_grassFoot_03.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZgrassFoot", file = "media/sound/PZ_grassFoot_04.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },
--    { alias = "PZ_ZgrassFoot", file = "media/sound/PZ_grassFoot_05.wav", gain = defaultZombieFootstepVolume, minrange = 0.001, maxrange = 25,  maxreverbrange = 25,  reverbfactor = defaultFootstepReverb, priority = 0 },

--    { alias = "generator", file = "media/sound/generator.ogg", gain = 1.0, minrange = 0.001, maxrange = 50,  maxreverbrange = 50,  reverbfactor = 1.0, priority = 5 },
}

---@class ambientSoundTable
ambientSoundTable = {
--    { alias = "PZ_DogBark", file = "media/sound/PZ_DogBark1.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "PZ_DogBark", file = "media/sound/PZ_DogBark2.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "burglar2", file = "media/sound/burglar2.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 20,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },

--    { alias = "distscream", file = "media/sound/distscream1.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "distscream", file = "media/sound/distscream2.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "distscream", file = "media/sound/distscream3.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "distscream", file = "media/sound/distscream4.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "distscream", file = "media/sound/distscream5.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "distscream", file = "media/sound/distscream6.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "distscream", file = "media/sound/distscream7.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "distscream", file = "media/sound/distscream8.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "distscream", file = "media/sound/distscream9.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },

--    { alias = "fscream", file = "media/sound/fscream1.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "fscream", file = "media/sound/fscream2.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "fscream", file = "media/sound/fscream3.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },

--    { alias = "scream", file = "media/sound/scream1.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "scream", file = "media/sound/scream2.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "scream", file = "media/sound/scream3.wav", gain = ambientScreamGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },

--    { alias = "PZ_DogBark_Whimper", file = "media/sound/PZ_DogBark_Whimper1.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 1,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },

--    { alias = "stormy9mmClick", file = "media/sound/stormy9mmClick.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 1,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },

--    { alias = "PZ_Owl", file = "media/sound/PZ_Owl_01.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "PZ_Owl", file = "media/sound/PZ_Owl_02.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "PZ_Owl", file = "media/sound/PZ_Owl_03.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "PZ_Owl", file = "media/sound/PZ_Owl_04.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },

--    { alias = "PZ_WolfHowl", file = "media/sound/PZ_WolfHowl_01.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "PZ_WolfHowl", file = "media/sound/PZ_WolfHowl_02.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "PZ_WolfHowl", file = "media/sound/PZ_WolfHowl_03.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "PZ_WolfHowl", file = "media/sound/PZ_WolfHowl_04.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },
--    { alias = "PZ_WolfHowl", file = "media/sound/PZ_WolfHowl_05.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 250,  maxreverbrange = 50,  reverbfactor = ambientReverbFactor, preferedTriggerRange=defaultTriggerRange },

--    { alias = "assaultrifledistant", file = "media/sound/assaultrifledistant2.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "assaultrifledistant", file = "media/sound/assaultrifledistant3.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "assaultrifledistant", file = "media/sound/assaultrifledistant4.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "assaultrifledistant", file = "media/sound/assaultrifledistant5.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "assaultrifledistant", file = "media/sound/assaultrifledistant6.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "assaultrifledistant", file = "media/sound/assaultrifledistant7.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "assaultrifledistant", file = "media/sound/assaultrifledistant8.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "assaultrifledistant", file = "media/sound/assaultrifledistant8.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "pistoldistant", file = "media/sound/pistoldistant1.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "pistoldistant", file = "media/sound/pistoldistant2.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "pistolfiredist", file = "media/sound/pistolfiredist1.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "pistolfiredist", file = "media/sound/pistolfiredist2.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "pistoloutside", file = "media/sound/pistoloutside1.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "pistoloutside", file = "media/sound/pistoloutside2.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "shotgundistant", file = "media/sound/shotgundistant1.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "shotgundistant", file = "media/sound/shotgundistant01.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
--    { alias = "shotgundistant", file = "media/sound/shotgundistant02.wav", gain = ambientDefaultGain, minrange = 0.001, maxrange = 1000,  maxreverbrange = 1000,  reverbfactor = 1, preferedTriggerRange=defaultTriggerRange},
}

-- These are 2D sounds
---@class globalSoundTable
globalSoundTable = {
--    { alias = "heart", file = "media/sound/heart.ogg", gain = 1.0 },
--    { alias = "levelup", file = "media/sound/levelup.ogg", gain = 0.6 },
--    { alias = "stab", file = "media/sound/stab1.ogg", gain = 0.6 },
--    { alias = "stab", file = "media/sound/stab2.ogg", gain = 0.6 },
--    { alias = "stab", file = "media/sound/stab3.ogg", gain = 0.6 },
}

function doLoadSoundbanks()
    print ("OnLoadSoundbanks");

    for k2, v2 in pairs(baseSoundTable) do
        if v2.looped == nil then
            v2.looped = false;
        end
--        print(v2.alias)
        getFMODSoundBank():addSound(v2.alias, v2.file, v2.gain, v2.minrange, v2.maxrange, v2.maxreverbrange, v2.reverbfactor, v2.priority, v2.looped);
    end
    for k2, v2 in pairs(ambientSoundTable) do
        if v2.looped == nil then
            v2.looped = false;
        end
--        print(v2.alias)
v2.looped = false
        getFMODSoundBank():addAmbientSound(v2.alias, v2.file, v2.gain, v2.minrange, v2.maxrange, v2.maxreverbrange, v2.reverbfactor, v2.preferedTriggerRange, v2.looped);
    end
    for k2, v2 in pairs(voiceTable) do
--         print(v2.alias)
        getFMODSoundBank():addVoice(v2.alias, v2.sound, v2.priority);
    end
    for k2, v2 in pairs(footStepTable) do
--        print(v2.alias)
        getFMODSoundBank():addFootstep(v2.alias, v2.grass, v2.wood, v2.concrete, v2.upstairs);
    end
    for k2, v2 in pairs(globalSoundTable) do
        getFMODSoundBank():addGlobalSound(v2.alias, v2.file, v2.gain);
    end
end

Events.OnLoadSoundBanks.Add(doLoadSoundbanks);
