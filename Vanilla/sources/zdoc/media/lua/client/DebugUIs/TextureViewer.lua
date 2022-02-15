require "ISUI/ISCollapsableWindow"

---@class TextureWindow : ISCollapsableWindow
TextureWindow = ISCollapsableWindow:derive("TextureWindow");


function TextureWindow:initialise()

    ISCollapsableWindow.initialise(self);
    self.title = self.tex:getName();

end

function TextureWindow:createChildren()
    --print("instance");
    ISCollapsableWindow.createChildren(self);

    self.renderPanel = ISPanel:new(0, 16, self.width, self.height-16);
    self.renderPanel.render = TextureWindow.renderTex;
    self.renderPanel.tex = self.tex;
    self.renderPanel:initialise();
    self.renderPanel:setAnchorRight(true);
    self.renderPanel:setAnchorBottom(true);
    self:addChild(self.renderPanel);

end


function TextureWindow:renderTex()
    self:drawTexture(self.tex, 0, 0, self:getWidth(), self:getHeight(), 1, 1, 1, 1);
end

function TextureWindow:new (x, y, width, height, tex)
    local o = {}
    --o.data = {}
    o = ISCollapsableWindow:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.tex = tex;
    o.backgroundColor = {r=0, g=0, b=0, a=1.0};
    return o
end

