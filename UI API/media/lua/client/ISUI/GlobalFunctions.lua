local allUI = {};

function NewUI()
    local ui = ISSimpleUI:new(0.4, 0.4, 0.2)
    ui:initialise();
    ui:instantiate();
    table.insert(allUI, ui);
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

function onCustomUIKeyPressed(key)
    for i, ui in ipairs(allUI) do
        if ui.key and key == ui.key then
            ui:toggle();
        end
    end
end

Events.OnCustomUIKeyPressed.Add(onCustomUIKeyPressed)