---@class ISLayoutManager
ISLayoutManager = {}
ISLayoutManager.windows = {}
ISLayoutManager.enableLog = false;

-- Public API
ISLayoutManager.RegisterWindow = function(name, funcs, target)
    if getCore():getGameMode() == "Tutorial" then return; end
    if ISLayoutManager.enableLog then print('layout: register window '..name) end
	ISLayoutManager.windows[name] = {}
	ISLayoutManager.windows[name].funcs = funcs
	ISLayoutManager.windows[name].target = target
	ISLayoutManager.TryRestore(name)
end

ISLayoutManager.DefaultRestoreWindow = function(window, layout)
    if getCore():getGameMode() == "Tutorial" then return; end
	local x = tonumber(layout.x)
	local y = tonumber(layout.y)
	local width = tonumber(layout.width)
	local height = tonumber(layout.height)
	local s = ''
	for k,v in pairs(layout) do
		s = s..k..'='..v..' '
	end
    if ISLayoutManager.enableLog then print('layout: restoring '..s) end;
	if x ~= nil and y ~= nil then
		window:setX(x)
		window:setY(y)
	end
	if width ~= nil and height ~= nil then
		local resizeWidget = ISLayoutManager.FindResizeWidget(window)
		if resizeWidget ~= nil then
			resizeWidget:resize(width - window:getWidth(), height - window:getHeight())
		else
			window:setWidth(width)
			window:setHeight(height)
		end
	end
	if x ~= nil and y ~= nil then
		-- Set these a second time in case the keep-on-screen code moved the window
		window:setX(x)
		window:setY(y)
	end
    if ISLayoutManager.enableLog then print('layout: final geom x,y,width,height='..window:getX()..','..window:getY()..','..window:getWidth()..','..window:getHeight()) end
	if layout.visible == 'true' then
		window:addToUIManager()
		window:setVisible(true)
	elseif layout.visible == 'false' then
		window:setVisible(false)
	end
end

ISLayoutManager.DefaultSaveWindow = function(window, layout)
    if getCore():getGameMode() == "Tutorial" then return; end
	layout.x = window:getX()
	layout.y = window:getY()
	layout.width = window:getWidth()
	layout.height = window:getHeight()
	ISLayoutManager.SaveWindowVisible(window, layout)
end

-- In-game windows are hidden when the main menu is shown
ISLayoutManager.SaveWindowVisible = function(window, layout)
    if getCore():getGameMode() == "Tutorial" then return; end
	local visible = window:getIsVisible()
	if ISUIHandler.visibleUI and window:getJavaObject() then
		for i,v in ipairs(ISUIHandler.visibleUI) do
			if v == window:getJavaObject():toString() then
				visible = true
			end
		end
	end
	layout.visible = tostring(visible)
end

-- Private API
ISLayoutManager.TryRestore = function(name)
    if getCore():getGameMode() == "Tutorial" then return; end
	if ISLayoutManager.layouts == nil then
		ISLayoutManager.ReadIni()
	end
	local xres = getCore():getScreenWidth()
	local yres = getCore():getScreenHeight()
    if ISLayoutManager.enableLog then print('layout: looking for '..name..' at '..xres..'x'..yres) end
	for _,resolution in pairs(ISLayoutManager.layouts) do
        if ISLayoutManager.enableLog then print('layout: looking at resolution '..resolution.width..'x'..resolution.height) end
		if resolution.width == xres and resolution.height == yres then
            if ISLayoutManager.enableLog then print('layout: looking at '..#resolution.windows..' possible windows') end
			for _,layout in pairs(resolution.windows) do
                if ISLayoutManager.enableLog then print('layout: looking at window '..layout.name) end
				if layout.name == name then
					return ISLayoutManager.CallRestoreLayout(name, layout)
				end
			end
		else
--			print("layout: that isn't the resolution we're looking for")
--			print(xres);print(yres);print(v.width);print(v.height);
--			if xres==v.width then print("xres==v.width") else print("xres!=v.width") end
--			if yres==v.height then print("yres=v.height") else print("yres!=v.height") end
		end
	end
end

ISLayoutManager.CallRestoreLayout = function(name, layout)
    if getCore():getGameMode() == "Tutorial" then return; end
	local w = ISLayoutManager.windows[name]
	if w ~= nil and w.funcs.RestoreLayout ~= nil then
		w.funcs.RestoreLayout(w.target, name, layout)
	end
end

ISLayoutManager.CallSaveLayout = function(name, layout)
    if getCore():getGameMode() == "Tutorial" then return; end
	local w = ISLayoutManager.windows[name]
	if w ~= nil and w.funcs.SaveLayout ~= nil then
		w.funcs.SaveLayout(w.target, name, layout)
	end
end

ISLayoutManager.FindResizeWidget = function(window)
    if getCore():getGameMode() == "Tutorial" then return; end
	for _,v in pairs(window:getChildren()) do
		if v.Type == 'ISResizeWidget' then
			return v
		end
	end
	return nil
end

ISLayoutManager.ReadIni = function()
    if getCore():getGameMode() == "Tutorial" then return; end
	local reader = getFileReader("layout.ini", true)
	ISLayoutManager.layouts = {}
	local resolution = nil
	while true do
		local line = reader:readLine()
		if line == nil then
			reader:close()
			break
		end
		line = string.trim(line)
		if line == '' then
			-- ignore blank line
		elseif luautils.stringStarts(line, '[') then
			-- [WxH]
			local resString = string.sub(line, 2, string.len(line) - 1)
			local values = string.split(resString, "x")
			if resolution ~= nil then
				table.insert(ISLayoutManager.layouts, resolution)
			end
			resolution = {}
			resolution.width = tonumber(values[1])
			resolution.height = tonumber(values[2])
			resolution.windows = {}
            if ISLayoutManager.enableLog then print('layout: resolution='..resolution.width..'x'..resolution.height) end
		else
			-- name x=N y=N width=N height=N visible=V pinned=P
			local values = string.split(line, " ")
			local layout = {}
			layout.name = values[1]
			for i = 2, #values do
				local kv = string.split(values[i], "=")
				layout[kv[1]] = kv[2]
			end
--			breakpoint()
			table.insert(resolution.windows, layout)
            if ISLayoutManager.enableLog then print('layout: window='..layout.name) end
		end    
	end
	if resolution ~= nil then
		table.insert(ISLayoutManager.layouts, resolution)
	end
end

ISLayoutManager.WriteIni = function()
    if getCore():getGameMode() == "Tutorial" then return; end
    if ISLayoutManager.enableLog then print('layout: writing ini file') end
	local writer = getFileWriter("layout.ini", true, false);
	for _,v in pairs(ISLayoutManager.layouts) do
		writer:write("["..v.width.."x"..v.height.."]\r\n")
		for _,v2 in pairs(v.windows) do
			writer:write(v2.name)
			for i3,v3 in pairs(v2) do
				if i3 ~= 'name' then
					writer:write(" "..i3.."="..v3)
				end
			end
			writer:write("\r\n")
		end
	end
	writer:close();
end

ISLayoutManager.SaveLayout = function(name, window)
end

ISLayoutManager.OnPostSave = function()

    if getCore():getGameMode() == "Tutorial" then return; end

	local xres = getCore():getScreenWidth()
	local yres = getCore():getScreenHeight()
	local curRes = nil
	for _,resolution in pairs(ISLayoutManager.layouts) do
		if resolution.width == xres and resolution.height == yres then
			curRes = resolution
			break
		end
	end
	if curRes == nil then
		curRes = {}
		curRes.width = xres
		curRes.height = yres
		curRes.windows = {}
		table.insert(ISLayoutManager.layouts, curRes)
	end
	for name,window in pairs(ISLayoutManager.windows) do
        if ISLayoutManager.enableLog then print('layout: saving window '..name) end
		local wlayout = nil
		for _,layout in pairs(curRes.windows) do
			if layout.name == name then
                if ISLayoutManager.enableLog then print('layout: updating existing info for '..name) end
				wlayout = layout
				break
			end
		end
		if wlayout == nil then
            if ISLayoutManager.enableLog then print('layout: adding new info for '..name) end
			wlayout = {}
			wlayout.name = name
			table.insert(curRes.windows, wlayout)
		end
		ISLayoutManager.CallSaveLayout(name, wlayout)
	end
	ISLayoutManager.WriteIni()
end

--Events.OnGameStart.Add(ISLayoutManager.ReadIni)
Events.OnPostSave.Add(ISLayoutManager.OnPostSave)

