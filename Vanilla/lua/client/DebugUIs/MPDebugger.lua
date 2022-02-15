require "ISUI/ISCollapsableWindow"

MPDebugger = ISCollapsableWindow:derive("MPDebugger");


MPDebugger.onKeyPressed = function(key)

end

Events.OnKeyPressed.Add(MPDebugger.onKeyPressed);
