--[[
	@Author: Turbo
	- Holds a normal key/pair table and a index table counting from 1, the position of elements in the latter are ordered by the Z values defined via .add
	- Z value can be ommited in .add in which case the default value is used
	- elements can be gotten/removed either by normal key or index key
	- the indexIterator - keyIterator behave as ipairs - pairs
	
	SOME EXAMPLES:
	--LUA
		local elements = ISPriorityTable.new();

		print(tostring(elements));
		--			  Key		Value		Zindex
		elements.add( "Key1", 	"test 1", 	100 );
		elements.add(      2, 	"test 2", 	150 );
		elements.add( "Key3", 	"test 3", 	 50 );
		elements.add( "Key4",  	    true, 	 75 );
		elements.add( "Key5", 	"test 5" );

		elements.remove("Key1");
		elements.remove("Key3");
		elements.remove("Key5");

		elements.add("Key6", {"abcd","efgh"}, 100);

		print("elements size = "..tostring(#elements));

		for index, value in elements.indexIterator() do					-- note: the shortcut 'elements()' can be used instead of 'elements.indexIterator()'
			print(tostring(index).." : " ..tostring(value));
		end
		print("");
		for index = 1,#elements do
			print(tostring(index).." : " ..tostring(elements.getIndex(index)));
		end		
		print("");
		for key, value in elements.keyIterator() do
			print(tostring(key).." : " ..tostring(value));
		end
	--LUAEND
	
	PRINTS:
		ISPriorityTable
		elements size = 3
		1 : true
		2 : table: 0073b140
		3 : test 2

		1 : true
		2 : table: 0073b140
		3 : test 2	

		2 : test 2
		Key6 : table: 0073b140
		Key4 : true
--]]

---@class ISPriorityTable
ISPriorityTable = {};

function ISPriorityTable.new()
	local function getElement(_key, _value, _zindex)
		local self 	= {};
		self.key 	= _key;
		self.value 	= _value;
		self.zindex = _zindex;
		return self;	
	end

	local name 			= "ISPriorityTable";
	local array 		= {};
	array.n 			= 0;
	local list 			= {};
	local self 			= {};
	local defaultZ 		= 1000;
	
	local function setElement( _k, _v, _z ) 
		local e = list[ _k ];
		if e and _v ~= nil then			-- edit entry
			e.value = _v;
		else
			if e then					-- remove entry
				local len,found = #array,false;
				for i = 1,len do
					if not found and  array[ i ].key == _k then
						array[ i ] = nil;
						list[ _k ] = nil;
						array.n = array.n - 1;
						found = true;
					elseif found then
						array[ i-1 ] = array[ i ];
						if i == len then array[ i ] = nil; end
					end
				end
			else						-- add new entry
				e = getElement( _k, _v, _z );
				for i = #array,0,-1 do
					if  i == 0 or array[ i ].zindex <= _z then
						array[ i+1 ] = e;
						list[ _k ] = e;
						array.n = array.n + 1;
						return;
					end
					array[ i+1 ] = array[ i ];
				end		
			end
		end
	end
	
	function self.setDefaultZvalue( _z )
		defaultZ = _z;
	end
	
	function self.add( _k, _v, _z )
		local z = _z or defaultZ;
		setElement( _k, _v, z );
	end
	
	function self.get( _k )
		if list[ _k ] then
			return list[ _k ].value;
		end
	end	
	
	function self.getIndex( _index )
		if array[ _index ] then
			return array[ _index ].value;
		end
	end	
	
	function self.remove( _k )
		setElement( _k, nil );
	end
	
	function self.removeIndex( _index )
		local e = array[ _index ];
		if e then
			setElement( e.key, nil );
		end
	end	
	
	function self.size()
		return #array;
	end
	
	function self.toString()
		return name;
	end

	function self.indexIterator()
		local index, count, e = 0, #array, nil;
		return function ()
			index = index + 1;
			if index <= count then
				e = array[index];
				return index, e.value, e.key;
			end
		end
	end
	
	function self.keyIterator()
		local k,e;
		return function ()	
			k,e = next(list, k);
			if e then return k, e.value; end
		end
	end

	-- metatable
	setmetatable( self, { 
		__newindex = function( t, k, v ) end,
		__metatable = function( t ) end,
		__call = self.indexIterator,
		__tostring = self.toString,
		__len = self.size,
	});
	
	return self;
end
