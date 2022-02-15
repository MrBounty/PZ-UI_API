
---@class MusicChoices
MusicChoices = {

    drama = 0,
    last = "",

    compareByDrama = function (a, b)
        --   SurvivorSelector.contextwith = with;
        -- SurvivorSelector.contextagainst = against;
        local totala = math.abs(MusicChoices.drama - a.scores.drama) - a.scores.bias;
        local totalb = math.abs(MusicChoices.drama - b.scores.drama) - b.scores.bias;
        if MusicChoices.last == a.file then
            totala = 100000;
        end
        if MusicChoices.last == b.file then
            totalb = 100000;
        end

        if(totala < totalb) then
            return true
        else
            return false
        end
    end,

    compareByAmbientDrama = function (a, b)
        --   SurvivorSelector.contextwith = with;
        -- SurvivorSelector.contextagainst = against;
        local totala = math.abs(MusicChoices.drama - a.scores.drama);
        local totalb = math.abs(MusicChoices.drama - b.scores.drama);
        if MusicChoices.last == a.file then
            totala = 100000;
        end
        if MusicChoices.last == b.file then
            totalb = 100000;
        end

        if(totala < totalb) then
            return true
        else
            return false
        end
    end,

    get = function(drama)

        local musictable = MusicChoices.choices;
        if getSoundManager():isRemastered() then
            musictable = MusicChoices.remasteredchoices;
        end

        MusicChoices.drama = drama;
        table.sort(musictable, MusicChoices.compareByDrama);
        local c = 0;
        for i, k in ipairs(musictable) do
            c = c + 1;
        end
        c = c * 0.3;
        local i = ZombRand(c)+1;


        MusicChoices.last =  musictable[i].file;
        return musictable[i].file;
    end,

    getAmbient = function(drama)
        MusicChoices.drama = drama;
        table.sort(MusicChoices.ambient, MusicChoices.compareByAmbientDrama);
        local c = 0;
        local ambients = {};
        for i, k in ipairs(MusicChoices.ambient) do
            local found = false;
            for x=0,getSoundManager():getAmbientPieces():size() -1 do
                if getSoundManager():getAmbientPieces():get(x):getName() == k.file then
                    found = true;
                    break;
                end
            end
            if not found then
                table.insert(ambients, k);
                c = c + 1;
            end
        end
        c = c * 0.2;
        local i = ZombRand(c)+1;


        MusicChoices.last =  ambients[i].file;
        return ambients[i].file;
    end,

    choices = {

        {
            file="OldMusic_alone",
            scores = {
                drama = 3,
                bias = 5,
            }
        },


        {
            file="OldMusic_long_ambient",
            scores = {
                drama = 2,
                bias = 4,
            }
        },

        {
            file="OldMusic_barricading",
            scores = {
                drama = 2,
                bias = 5,
            }
        },
        {
            file="OldMusic_ambient1",
            scores = {
                drama = 2,
                bias = 3,
            }
        },
        {
            file="OldMusic_ambient2",
            scores = {
                drama = 2,
                bias = 3,
            }
        },
        {
            file="OldMusic_long_ambient",
            scores = {
                drama = 2,
                bias = 5,
            }
        },
        {
            file="OldMusic_the_inevitable",
            scores = {
                drama = 4,
                bias = 5,
            }
        },
        {
            file="OldMusic_what_was_lost",
            scores = {
                drama = 2,
                bias = 5,
            }
        },
        {
            file="OldMusic_wwl_tense",
            scores = {
                drama = 6,
                bias = 5,
            }
        },
        {
            file="OldMusic_wwl_active",
            scores = {
                drama = 6,
                bias = 5,
            }
        },
        {
            file="OldMusic_guitar",
            scores = {
                drama = 4,
                bias = 5,
            }
        },
        {
            file="OldMusic_the_zombie_threat",
            scores = {
                drama = 5,
                bias = 5,
            }
        },
        {
            file="OldMusic_PZ",
            scores = {
                drama = 5,
                bias = 5,
            }
        },
        {
            file="OldMusic_piano",
            scores = {
                drama = 3,
                bias = 5,
            }
        },
        {
            file="OldMusic_low",
            scores = {
                drama = 4,
                bias = 5,
            }
        },
        {
            file="OldMusic_fight_or_flight",
            scores = {
                drama = 9,
                bias = 5,
            }
        },

        {
            file="OldMusic_chase",
            scores = {
                drama = 10,
                bias = 5,
            }
        },
        {
            file="OldMusic_desperate_escape",
            scores = {
                drama = 9,
                bias = 5,
            }
        },
        {
            file="OldMusic_the_horde",
            scores = {
                drama = 9,
                bias = 5,
            }
        },

        {
            file="OldMusic_maybe_we_can_win_this",
            scores = {
                drama = 7,
                bias = 4,
            }
        },
        {
            file="OldMusic_maybe_not",
            scores = {
                drama = 6,
                bias = 5,
            }
        },
        {
            file="OldMusic_raider",
            scores = {
                drama = 4,
                bias = 5,
            }
        },
        {
            file="OldMusic_run",
            scores = {
                drama = 9,
                bias = 5,
            }
        },
        {
            file="OldMusic_saying_goodbye",
            scores = {
                drama = 3,
                bias = 5,
            }
        },
        {
            file="OldMusic_where_is_everyone",
            scores = {
                drama = 2,
                bias = 5,
            }
        },
        {
            file="OldMusic_they_were_once_here",
            scores = {
                drama = 2,
                bias = 5,
            }
        },
        {
            file="OldMusic_work_fast",
            scores = {
                drama = 5,
                bias = 5,
            }
        },
    },

    ambient = {

        {
            file="AmbientMusic_RhythmicAmbient",
            scores = {
                drama = 4,
            }
        },
        {
            file="AmbientMusic_VoiceAmbient",
            scores = {
                drama = 5,
            }
        },
        {
            file="AmbientMusic_PercussiveAmbient",
            scores = {
                drama = 6,
            }
        },
        {
            file="AmbientMusic_IntenseAmbient",
            scores = {
                drama = 7,
            }
        },
        {
            file="AmbientMusic_ZombieAmbient",
            scores = {
                drama = 8,
            }
        },
        {
            file="AmbientMusic_BrassAmbient",
            scores = {
                drama = 9,
            }
        },
        {
            file="AmbientMusic_CreepyAmbient",
            scores = {
                drama = 10,
            }
        },
    },

    remasteredchoices = {

        {

            file="NewMusic_32",
            scores = {
                drama = 3,
                bias = 5,
            }

        },
        {

            file="NewMusic_33",
            scores = {
                drama = 4,
                bias = 5,
            }

        },
        {

            file="NewMusic_34",
            scores = {
                drama = 3,
                bias = 5,
            }

        },
        {

            file="NewMusic_35",
            scores = {
                drama = 7,
                bias = 5,
            }

        },
        {

            file="NewMusic_36",
            scores = {
                drama = 7,
                bias = 5,
            }

        },
        {

            file="NewMusic_Travelling",
            scores = {
                drama = 4,
                bias = 5,
            }

        },
        {

            file="NewMusic_TakeStock",
            scores = {
                drama = 5,
                bias = 5,
            }

        },
        {

            file="NewMusic_PassingTime",
            scores = {
                drama = 4,
                bias = 5,
            }

        },
        {

            file="NewMusic_NoTime",
            scores = {
                drama = 6,
                bias = 5,
            }

        },
        {

            file="NewMusic_MoreAreComing",
            scores = {
                drama = 7,
                bias = 5,
            }

        },
        {

            file="NewMusic_OnlyOneWay",
            scores = {
                drama = 4,
                bias = 5,
            }

        },
        {

            file="NewMusic_KeepMoving",
            scores = {
                drama = 7,
                bias = 5,
            }

        },
        {

            file="NewMusic_GoItAlone",
            scores = {
                drama = 2,
                bias = 5,
            }

        },

        {

            file="NewMusic_Sunrise",
            scores = {
                drama = 4,
                bias = 5,
            }

        },
        {

            file="NewMusic_HoldingOutHope",
            scores = {
                drama = 4,
                bias = 5,
            }

        },
        {

            file="NewMusic_Working",
            scores = {
                drama = 3,
                bias = 5,
            }

        },
        {

            file="NewMusic_Sunset",
            scores = {
                drama = 0,
                bias = 5,
            }

        },
        {

            file="NewMusic_Mourning",
            scores = {
                drama = 2,
                bias = 5,
            }

        },
        {

            file="NewMusic_LookingAround",
            scores = {
                drama = 3,
                bias = 5,
            }

        },
        {

            file="NewMusic_Waiting",
            scores = {
                drama = 3,
                bias = 5,
            }

        },
        {

            file="NewMusic_Overrun",
            scores = {
                drama = 10,
                bias = 5,
            }

        },
        {

            file="NewMusic_Rest",
            scores = {
                drama = 1,
                bias = 5,
            }

        },
        {

            file="NewMusic_PressOn",
            scores = {
                drama = 3,
                bias = 5,
            }

        },
        {

            file="NewMusic_CalmBeforeTheStorm",
            scores = {
                drama = 4,
                bias = 5,
            }

        },

        {

            file="NewMusic_GetReady",
            scores = {
                drama = 7,
                bias = 5,
            }

        },
        {

            file="NewMusic_EchoesFromBefore",
            scores = {
                drama = 3,
                bias = 5,
            }

        },
        {

            file="NewMusic_Surrounded",
            scores = {
                drama = 9,
                bias = 5,
            }

        },
        {

            file="NewMusic_ThePlan",
            scores = {
                drama = 4,
                bias = 5,
            }

        },
        {

            file="NewMusic_ThinkingOfThePast",
            scores = {
                drama = 2,
                bias = 5,
            }

        },
        {

            file="NewMusic_TheyreClose",
            scores = {
                drama = 6,
                bias = 5,
            }

        },
        {

            file="NewMusic_PatchUp",
            scores = {
                drama = 1,
                bias = 5,
            }

        },
        {

            file="NewMusic_GearUp",
            scores = {
                drama = 8,
                bias = 5,
            }

        },
        {

            file="NewMusic_TouchAndGo",
            scores = {
                drama = 6,
                bias = 5,
            }

        },
        {

            file="NewMusic_FinallyCalm",
            scores = {
                drama = 2,
                bias = 5,
            }

        },
        {

            file="NewMusic_Everythings_Gone",
            scores = {
                drama = 2,
                bias = 5,
            }

        },
        {

            file="NewMusic_Tread_Carefully",
            scores = {
                drama = 3,
                bias = 5,
            }

        },

        {

            file="NewMusic_Alone",
            scores = {
                drama = 3,
                bias = 5,
            }

        },
        {
            file="NewMusic_Barricading",
            scores = {
                drama = 2,
                bias = 5,
            }
        },
        {
            file="NewMusic_Ambient",
            scores = {
                drama = 2,
                bias = 3,
            }
        },
        {
            file="NewMusic_TheInevitable",
            scores = {
                drama = 4,
                bias = 5,
            }
        },
        {
            file="NewMusic_WhatWasLost",
            scores = {
                drama = 2,
                bias = 5,
            }
        },
        {
            file="NewMusic_WhatWasLostActive",
            scores = {
                drama = 6,
                bias = 5,
            }
        },
        {
            file="NewMusic_WWL_Solo",
            scores = {
                drama = 1,
                bias = 5,
            }
        },


        {
            file="NewMusic_WhatWasLostActive2",
            scores = {
                drama = 6,
                bias = 5,
            }
        },
        {
            file="NewMusic_AmbientGuitar",
            scores = {
                drama = 4,
                bias = 5,
            }
        },
        {
            file="NewMusic_TheZombieThreat",
            scores = {
                drama = 5,
                bias = 5,
            }
        },

        {
            file="NewMusic_AmbientPiano",
            scores = {
                drama = 3,
                bias = 5,
            }
        },
        {
            file="NewMusic_AmbientLow",
            scores = {
                drama = 4,
                bias = 5,
            }
        },
        {
            file="NewMusic_FightOrFlight",
            scores = {
                drama = 9,
                bias = 5,
            }
        },

        {
            file="NewMusic_Chase",
            scores = {
                drama = 10,
                bias = 5,
            }
        },
        {
            file="NewMusic_DesperateEscape",
            scores = {
                drama = 9,
                bias = 5,
            }
        },
        {
            file="NewMusic_TheHorde",
            scores = {
                drama = 9,
                bias = 5,
            }
        },

        {
            file="NewMusic_MaybeWeCanWinThis",
            scores = {
                drama = 7,
                bias = 4,
            }
        },
        {
            file="NewMusic_MaybeNot",
            scores = {
                drama = 6,
                bias = 5,
            }
        },
        {
            file="NewMusic_AmbientRaider",
            scores = {
                drama = 4,
                bias = 5,
            }
        },
        {
            file="NewMusic_Run",
            scores = {
                drama = 9,
                bias = 5,
            }
        },
        {
            file="NewMusic_SayingGoodbye",
            scores = {
                drama = 3,
                bias = 5,
            }
        },
        {
            file="NewMusic_WhereIsEveryone",
            scores = {
                drama = 2,
                bias = 5,
            }
        },
        {
            file="NewMusic_TheyWereOnceHere",
            scores = {
                drama = 2,
                bias = 5,
            }
        },
        {
            file="NewMusic_WorkFast",
            scores = {
                drama = 5,
                bias = 5,
            }
        },
        {
            file="NewMusic_SlowSad",
            scores = {
                drama = 2,
                bias = 4,
            }
        },
    }

}
