---@class GameLoadingState : zombie.gameStates.GameLoadingState
---@field public loader Thread
---@field public newGame boolean
---@field private startTime long
---@field public build23Stop boolean
---@field public unexpectedError boolean
---@field public GameLoadingString String
---@field public playerWrongIP boolean
---@field private bShowedUI boolean
---@field public mapDownloadFailed boolean
---@field private bWaitForAssetLoadingToFinish1 boolean
---@field private bWaitForAssetLoadingToFinish2 boolean
---@field private assetLock1 Object
---@field private assetLock2 Object
---@field public playerCreated boolean
---@field public bDone boolean
---@field public convertingWorld boolean
---@field public convertingFileCount int
---@field public convertingFileMax int
---@field public Stage int
---@field TotalTime float
---@field loadingDotTick float
---@field loadingDot String
---@field private clickToSkipAlpha float
---@field private clickToSkipFadeIn boolean
---@field public Time float
---@field public bForceDone boolean
GameLoadingState = {}

---Overrides:
---
---exit in class GameState
---@public
---@return void
function GameLoadingState:exit() end

---Overrides:
---
---render in class GameState
---@public
---@return void
function GameLoadingState:render() end

---Overrides:
---
---update in class GameState
---@public
---@return GameStateMachine.StateAction
function GameLoadingState:update() end

---@public
---@return void
function GameLoadingState:Done() end

---Overrides:
---
---enter in class GameState
---@public
---@return void
function GameLoadingState:enter() end

---Overrides:
---
---redirectState in class GameState
---@public
---@return GameState
function GameLoadingState:redirectState() end
