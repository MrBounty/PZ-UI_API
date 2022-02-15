-- This file contains informations for damage texture relatives to their part, by what weapon (axe don't do same dmg as shotgun, etc.)
---@class DamageModelDefinitions
DamageModelDefinitions = DamageModelDefinitions or {};
DamageModelDefinitions.list = DamageModelDefinitions.list or {};

DamageModelDefinitions.addDefinition = function(texture, bodyPart, damageCategories, criticalHit)
	local damage = {};
	damage.texture = texture;
	damage.bodyPart = bodyPart;
	damage.damageCategories = damageCategories;
	damage.criticalHit = criticalHit;
	table.insert(DamageModelDefinitions.list, damage);
end

DamageModelDefinitions.addDefinition("ZedDmg_BELLY_Slash", BodyPartType.Torso_Lower, {"Slash"});
DamageModelDefinitions.addDefinition("ZedDmg_BELLY_Skin", BodyPartType.Torso_Lower, {"Slash"}, true);

DamageModelDefinitions.addDefinition("ZedDmg_CHEST_Slash", BodyPartType.Torso_Upper, {"Slash"});
DamageModelDefinitions.addDefinition("ZedDmg_BELLY_Skin", BodyPartType.Torso_Upper, {"Slash"}, true);

DamageModelDefinitions.addDefinition("ZedDmg_HEAD_Slash", BodyPartType.Head, {"Slash"});
DamageModelDefinitions.addDefinition("ZedDmg_HEAD_Skin", BodyPartType.Head, {"Slash"}, true);

-- Add a damaged texture on the zombie we just hit
DamageModelDefinitions.OnHitZombie = function(zombie, wielder, bodyPart, weapon)
--	print("on hit zombie", zombie, bodyPart, weapon)
	if not zombie or not weapon or not bodyPart then return; end
	
	local possibleTextures = {};
	for i,damage in ipairs(DamageModelDefinitions.list) do
		if damage.bodyPart == bodyPart and DamageModelDefinitions.checkCategory(weapon, damage.damageCategories) and DamageModelDefinitions.checkCrit(wielder, damage) then
--			print("add possible texture", damage.texture);
			table.insert(possibleTextures, damage);
		end
	end
	
	if #possibleTextures == 0 then return; end
	-- add the texture onto the body
	local dmgTexture = possibleTextures[ZombRand(1, #possibleTextures + 1)];
--	print("chosen dmg is: ", dmgTexture.texture)
	zombie:addVisualDamage(dmgTexture.texture);
	
	-- generate a hole in the clothing if needed
	if weapon:isDamageMakeHole() then
		zombie:addHole(BloodBodyPartType.FromIndex(BodyPartType.ToIndex(dmgTexture.bodyPart)));
		zombie:addHole(BloodBodyPartType.FromIndex(BodyPartType.ToIndex(dmgTexture.bodyPart)));
		zombie:addHole(BloodBodyPartType.FromIndex(BodyPartType.ToIndex(dmgTexture.bodyPart)));
	end
end

DamageModelDefinitions.checkCategory = function(weapon, damageCategories)
	for i,v in ipairs(damageCategories) do
		if weapon:getDamageCategory() == v then
			return true;
		end
	end
	return false;
end

DamageModelDefinitions.checkCrit = function(wielder, damage)
	if damage.criticalHit and wielder:isCriticalHit() then
		return true;
	end
	if not damage.criticalHit and not wielder:isCriticalHit() then
		return true;
	end
	return false;
end

Events.OnHitZombie.Add(DamageModelDefinitions.OnHitZombie);
