
---@class StringReplacer
StringReplacer = {}

StringReplacer.DoCharacter = function(str, desc)

	local ret = str;
	local n = 1;
	if desc:isFemale() then
		ret, n = string.gsub(ret, "$HeShe$", "She");
		ret, n = string.gsub(ret, "$heshe$", "she");
		ret, n = string.gsub(ret, "$HisHer$", "Her");
		ret, n = string.gsub(ret, "$hisher$", "her");
		ret, n = string.gsub(ret, "$HimselfHerself$", "Herself");
		ret, n = string.gsub(ret, "$himselfherself$", "herself");

	else
		ret, n = string.gsub(ret, "$HeShe$", "He");
		ret, n = string.gsub(ret, "$heshe$", "he");
		ret, n = string.gsub(ret, "$HisHer$", "His");
		ret, n = string.gsub(ret, "$hisher$", "his");
		ret, n = string.gsub(ret, "$HimselfHerself$", "Himself");
		ret, n = string.gsub(ret, "$himselfherself$", "himself");
	end

	return ret;
end
