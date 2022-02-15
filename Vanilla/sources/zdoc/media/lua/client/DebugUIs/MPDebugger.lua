require "ISUI/ISCollapsableWindow"

---@class MPDebugger : ISCollapsableWindow
MPDebugger = ISCollapsableWindow:derive("MPDebugger");


MPDebugger.onKeyPressed = function(key)

end

Events.OnKeyPressed.Add(MPDebugger.onKeyPressed);
