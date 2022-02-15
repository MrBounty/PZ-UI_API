---@class IsoCamera : zombie.iso.IsoCamera
---@field public cameras PlayerCamera[]
---@field public CamCharacter IsoGameCharacter
---@field public FakePos JVector2
---@field public FakePosVec JVector2
---@field public TargetTileX int
---@field public TargetTileY int
---@field public PLAYER_OFFSET_X int
---@field public PLAYER_OFFSET_Y int
---@field public frameState IsoCamera.FrameState
IsoCamera = {}

---@public
---@param aTargetTileX int @the TargetTileX to set
---@return void
function IsoCamera:setTargetTileX(aTargetTileX) end

---@public
---@param playerIndex int
---@return int
function IsoCamera:getScreenTop(playerIndex) end

---@public
---@param aCamCharacter IsoGameCharacter @the CamCharacter to set
---@return void
function IsoCamera:setCamCharacter(aCamCharacter) end

---@public
---@return float
function IsoCamera:getTOffX() end

---@public
---@param playerIndex int
---@return int
function IsoCamera:getOffscreenWidth(playerIndex) end

---@public
---@param aOffY float @the OffY to set
---@return void
function IsoCamera:setOffY(aOffY) end

---@public
---@return float @the OffY
function IsoCamera:getOffY() end

---@public
---@return void
function IsoCamera:init() end

---@public
---@param aLastOffX float @the lastOffX to set
---@return void
function IsoCamera:setLastOffX(aLastOffX) end

---@public
---@return int @the TargetTileX
function IsoCamera:getTargetTileX() end

---@public
---@return float
function IsoCamera:getTOffY() end

---@public
---@param playerIndex int
---@return int
function IsoCamera:getOffscreenHeight(playerIndex) end

---@public
---@return JVector2 @the FakePos
function IsoCamera:getFakePos() end

---@public
---@return float
function IsoCamera:getRightClickOffY() end

---@public
---@param playerIndex int
---@return int
function IsoCamera:getScreenLeft(playerIndex) end

---@public
---@param playerIndex int
---@return int
function IsoCamera:getOffscreenTop(playerIndex) end

---@public
---@return float @the lastOffY
function IsoCamera:getLastOffY() end

---@public
---@return void
function IsoCamera:update() end

---@public
---@param GameChar IsoGameCharacter
---@return void
function IsoCamera:SetCharacterToFollow(GameChar) end

---@public
---@return void
function IsoCamera:updateAll() end

---@public
---@return JVector2 @the FakePosVec
function IsoCamera:getFakePosVec() end

---@public
---@param aFakePosVec JVector2 @the FakePosVec to set
---@return void
function IsoCamera:setFakePosVec(aFakePosVec) end

---@public
---@return IsoGameCharacter @the CamCharacter
function IsoCamera:getCamCharacter() end

---@public
---@param playerIndex int
---@return int
function IsoCamera:getOffscreenLeft(playerIndex) end

---@public
---@param aLastOffY float @the lastOffY to set
---@return void
function IsoCamera:setLastOffY(aLastOffY) end

---@public
---@param playerIndex int
---@return int
function IsoCamera:getScreenHeight(playerIndex) end

---@public
---@param playerIndex int
---@return int
function IsoCamera:getScreenWidth(playerIndex) end

---@public
---@return float @the lastOffX
function IsoCamera:getLastOffX() end

---@public
---@return float
function IsoCamera:getRightClickOffX() end

---@public
---@param aOffX float @the OffX to set
---@return void
function IsoCamera:setOffX(aOffX) end

---@public
---@param aFakePos JVector2 @the FakePos to set
---@return void
function IsoCamera:setFakePos(aFakePos) end

---@public
---@return int @the TargetTileY
function IsoCamera:getTargetTileY() end

---@public
---@param aTargetTileY int @the TargetTileY to set
---@return void
function IsoCamera:setTargetTileY(aTargetTileY) end

---@public
---@return float @the OffX
function IsoCamera:getOffX() end
