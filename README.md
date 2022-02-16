# PZ-Simple UI API
Want to make a simple menu for your mod to :  
- Ask the player to choose between two things?
- Ask the player a nickname, age, height, weight, etc ?
- Display a map ?
- Display player-related information from your mod such as amount of money, time on server, etc ?
- Warn players the start of an event ?
- And more ?

This mod is for you !  

# Make my first window
First you create a new UI with the `NewUI()` function.  
Find all useable function of UI [here](https://github.com/MrBounty/PZ-UI_API/blob/main/UI%20functions.md).  
After that it's easy, you just add [elements](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md) for the first line.  
When you added your elements, you jump to the next line with the function `ui:nextLine()` and you continu like that.  
One all line create, you just call `ui:saveLayout()` to finish creating the ui.  

As in the following diagram:
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/preview%20perso.png)

I highly recommend doing the UIs in event `OnCreateUI` but you can do it anywhere.  

# Understand coordinates
Menus are in 2d.  
Dimensions of 2d object are its **position in x and y** as well as its **size in width and height**.  
x and y are coordinates of the left top corner.  
Positions and sizes are in percentage of screen or in pixel.   
Percent is to simplify and allow compatibility between different screen sizes. For example a width of 0.2 will take 20% of the screen.    
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/schema2d%20(1).png)

# Examples
## Hello world
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/HelloWorld.jpg)
```lua
function onCreateUI()
    local UI = NewUI();
    UI:addText("", "Hello world", "Small", "Center")
    UI:saveLayout()
end

Events.OnCreateUI.Add(onCreateUI)
```

## Quest
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/quest.jpg)
[code](https://github.com/MrBounty/PZ-UI_API/blob/main/UI%20API/media/lua/client/Exemples/QuestChoiceUI.lua)

## Choose your team
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/team.jpg)
[code](https://github.com/MrBounty/PZ-UI_API/blob/main/UI%20API/media/lua/client/Exemples/TeamChoiceUI.lua)

## Choose a job
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/exemple1.gif)
[code](https://github.com/MrBounty/PZ-UI_API/blob/main/UI%20API/media/lua/client/Exemples/JobChoiceUI.lua)
