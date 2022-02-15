function NewUI(width, x, y)
    if not width then width = 0.2 end
    if not x then x = 0.5 - width/2 end
    if not y then y = 0.5 - width/2 end
    local ui = ISSimpleUI:new(x, y, width)
    ui:initialise();
    ui:instantiate();
    return ui
end

function cutTextToLong(text, width, font, zoom)
    if not zoom then zoom = 1 end
	while getTextManager():MeasureStringX(font, text) * zoom > width do
		text = string.sub(text, 1, #text-1);
		if text == "" then break end
	end
    return text, getTextManager():MeasureStringX(font, text) * zoom;
end