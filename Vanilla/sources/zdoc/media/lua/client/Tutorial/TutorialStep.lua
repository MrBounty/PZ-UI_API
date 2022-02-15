require "ISBaseObject"

---@class TutorialStep : ISBaseObject
TutorialStep = ISBaseObject:derive("TutorialStep");


function TutorialStep:begin()

end

function TutorialStep:isComplete()
    return self.messages ~= nil and self.messages:isEmpty();
end

function TutorialStep:finish()
    TutorialFocus.unset();
end

function TutorialStep:during()

end

function TutorialStep:onClose(message)
    self.messages:removeAt(0);
    if not self.messages:isEmpty() then
        self:doMessage();
    end
end

function TutorialStep:addMessage(text, x, y)
    if self.messages == nil then
        self.messages = LuaList:new();
    end

    self.messages:add( { text=text, x=x, y=y } )

end

function TutorialStep:addMessage(text, x, y, w, h, clickToSkip, test,focusx, focusy, focusw,focush)
    if self.messages == nil then
        self.messages = LuaList:new();
    end

    self.messages:add( { text=text, x=x, y=y, w=w, h=h, focusx=focusx, focusy=focusy, focusw=focusw,focush=focush,clickToSkip=clickToSkip, test=test } )
end

function TutorialStep:doMessage()
    if self.messages == nil then
        self.messages = LuaList:new();
    end

    if self.messages:isEmpty() then return; end

    local message = self.messages:get( 0 );

    if message.focusx ~= nil then
        TutorialFocus.set(message.focusx, message.focusy, message.focusw, message.focush)
    end

    local modal = TutorialMessage.getInstance(message.x, message.y, message.w, message.h, message.text, message.clickToSkip, self, message.test);

    -- onclick handler
end

function TutorialStep:new (type)
    local o = {}
    setmetatable(o, type)
    self.__index = type
    return o
end

