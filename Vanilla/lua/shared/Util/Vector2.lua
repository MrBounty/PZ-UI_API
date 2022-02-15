Vector2 = {}


Vector2.normalize = function (x, y)
        local len = Vector2.getLength(x, y);
        if len == 0 then x = 0; y = 0; else
             x = x / len;
            y = y / len;
        end

        return x, y;
    end

Vector2.getLength = function(x, y)
       return math.sqrt( (x * x) + (y * y))
    end

