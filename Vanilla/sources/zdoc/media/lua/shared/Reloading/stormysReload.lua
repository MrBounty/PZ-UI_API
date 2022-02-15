require "ISBaseObject"
require "Reloading/ISReloadManager"

--07 April 2013


---@class ReloadUtil : ISReloadUtil
ReloadUtil = ISReloadUtil:new();
---@class ReloadManager
ReloadManager = {}
ReloadManager[1] = ISReloadManager:new(0);
ReloadManager[2] = ISReloadManager:new(1);
ReloadManager[3] = ISReloadManager:new(2);
ReloadManager[4] = ISReloadManager:new(3);

--*****************************************************************
-- TEST METHODS
--*****************************************************************
local hasWeapons = false;
function addShotgun()
if(isKeyDown(Keyboard.KEY_L) == true) then
    if(hasWeapons == false) then
	getPlayer():getInventory():AddItem('Base.Shotgun');
    getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --getPlayer():getInventory():AddItem('Base.ShotgunShells');
    -- getPlayer():getInventory():AddItem('Base.ShotgunShells');
    -- getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --  getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --  getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --  getPlayer():getInventory():AddItem('Base.ShotgunShells');
    -- getPlayer():getInventory():AddItem('Base.ShotgunShells');
    -- getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --  getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --  getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --  getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --  getPlayer():getInventory():AddItem('Base.ShotgunShells');
    --getPlayer():getInventory():AddItem('Base.ShotgunShells');
	--getPlayer():getInventory():AddItem('Base.Pistol');
	getPlayer():getInventory():AddItem('Base.BerettaClip');
	getPlayer():getInventory():AddItem('Base.BerettaClip');
   	getPlayer():getInventory():AddItem('Base.Pistol');
       getPlayer():getInventory():AddItem('Base.Bullets9mm');
       getPlayer():getInventory():AddItem('Base.Bullets9mm');
       getPlayer():getInventory():AddItem('Base.Bullets9mm');
    --  getPlayer():getInventory():AddItem('Base.Bullets9mm');
    --  getPlayer():getInventory():AddItem('Base.Bullets9mm');
    --  getPlayer():getInventory():AddItem('Base.Bullets9mm');
    --   getPlayer():getInventory():AddItem('Base.Bullets9mm');
    --	getPlayer():getInventory():AddItem('Base.Bullets9mm');
    --	getPlayer():getInventory():AddItem('Base.Bullets9mm');
    --	getPlayer():getInventory():AddItem('Base.Bullets9mm');
    --  --	getPlayer():getInventory():AddItem('Base.Bullets9mm');
    --	getPlayer():getInventory():AddItem('Base.Bullets9mm');
    --	getPlayer():getInventory():AddItem('Base.Bullets9mm');
    --	getPlayer():getInventory():AddItem('Base.Bullets9mm');
    end
    hasWeapons = true;
    end
end
--Events.OnPlayerUpdate.Add(addShotgun);

