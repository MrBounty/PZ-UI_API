---@class LastStandChallenge
LastStandChallenge = {};

--table.insert(LastStandChallenge, InsomniaChallenge);
--table.insert(LastStandChallenge, CDDA);
--table.insert(LastStandChallenge, EightMonthsLater);
--table.insert(LastStandChallenge, Challenge1);
--table.insert(LastStandChallenge, Challenge2);

function addChallenge(challenge)
    challenge.name = getText("Challenge_" .. challenge.id .. "_name")
    challenge.description = getText("Challenge_" .. challenge.id .. "_desc")
    table.insert(LastStandChallenge, challenge)
end
