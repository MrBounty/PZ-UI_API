--[[
  ChunkReader.lua - 2014.
  Author: Kees "TurboTuTone" Bekkema.
  ]]


---@class EDebug
EDebug = EDebug or {};

function EDebug.ChunkReader( _chunkTileW )
    local self = {};

    local self_chunkTileWidth 	= _chunkTileW or 10;

    local self_Point 			= Point2D;
    local self_Floor			= math.floor;
    local self_Rand				= ZombRand;

    local self_chunkMap 		= getWorld():getCell():getChunkMap(getPlayer():getPlayerNum());
    local self_chunkMapWidth 	= self_chunkMap:getWidthInTiles() / self_chunkTileWidth;

    local self_p_chunkMapOffset = self_Point:new( self_Floor(self_chunkMapWidth/2), self_Floor(self_chunkMapWidth/2) );

    -- Points
    local self_p_zero 			= self_Point:new(0,0);
    local self_p_dir 			= self_Point:new(0,0);
    local self_p_cur 			= self_Point:new(0,0);
    local self_p_ringStart 		= self_Point:new(0,0);
    local self_p_centerPoint 	= self_Point:new(0,0);
    local self_p_A 				= self_Point:new(0,0);
    local self_p_B 				= self_Point:new(0,0);
    local self_p_dirA 			= self_Point:new(0,0);
    local self_p_dirB 			= self_Point:new(0,0);
    local self_p_startDirA 		= self_Point:new(0,0);
    local self_p_startDirB 		= self_Point:new(0,0);
    -- vars
    local self_corner 			= 0;
    local self_size_max 		= self_Floor( self_chunkMapWidth /2 ) -1;
    local self_size_cur 		= 0;
    local self_flip				= true;
    local self_steps            = 0;
    local self_size_next        = 0;
    --Clockwise directions
    local self_dircw 			= {	[-1] 	= { [-1] = self_Point:new( 1, 0), [0] = self_Point:new( 0,-1), [1] = self_Point:new( 0,-1), },
        [ 0]	= { [-1] = self_Point:new( 1, 0), [0] = self_Point:new( 0, 0), [1] = self_Point:new(-1, 0), },
        [ 1]	= { [-1] = self_Point:new( 0, 1), [0] = self_Point:new( 0, 1), [1] = self_Point:new(-1, 0), }, };
    --Counter Clockwise directions
    local self_dirccw 			= {	[-1] 	= { [-1] = self_Point:new( 0, 1), [0] = self_Point:new( 0, 1), [1] = self_Point:new( 1, 0), },
        [ 0]	= { [-1] = self_Point:new(-1, 0), [0] = self_Point:new( 0, 0), [1] = self_Point:new( 1, 0), },
        [ 1]	= { [-1] = self_Point:new(-1, 0), [0] = self_Point:new( 0,-1), [1] = self_Point:new( 0,-1), }, };

    function self.SetNextRing( _ringNum )
        self_steps 			= 0;
        self_size_cur 		= _ringNum;
        self_size_next 		= self_size_cur > 0 and 8 * self_size_cur or 1;
        self_p_ringStart 	= self_p_centerPoint + ( self_p_dir * self_Point:new( self_size_cur, self_size_cur ) );
        self_corner 		= (self_size_cur*2)-1;
        if self_p_dir.x == 0 or self_p_dir.y == 0 then self_corner = self_size_cur-1 end --if not a "corner" direction adjust corner count

        self_p_cur.setPoint( 	self_p_ringStart );
        self_p_A.setPoint( 		self_p_ringStart );
        self_p_B.setPoint( 		self_p_ringStart );
        self_p_dirA.setPoint( 	self_p_startDirA );
        self_p_dirB.setPoint( 	self_p_startDirB );
    end

    function self.ResetChunkReader( _start_point, _move_dir )
        self_p_cur.setPoint( _start_point );
        self_p_centerPoint.setPoint( _start_point );
        self_p_dir.setPoint( _move_dir );

        if self_p_dir == self_p_zero then self_p_dir.x = -1 end

        self_flip = true;

        self_p_startDirA.setPoint( self_dircw[  self_p_dir.x][ self_p_dir.y ] ); -- get initial direction for point A (clockwise)
        self_p_startDirB.setPoint( self_dirccw[ self_p_dir.x][ self_p_dir.y ] ); -- get initial direction for point B (counterclockwise)

        -- if the block based on the previous coordinate has completed rings take that count -1.
        if self_size_cur >= 2 then
            self.SetNextRing(self_size_cur-1);
        else
            self.SetNextRing(0);
        end
        --print("$$$$$ NEXTRING: ", self_size_cur)
    end

    function self.GetNextChunk( _curTick )
        local cornerPnt = self_Point:new(0,0);
        while true do
            -- if assigned maximum size reached abort
            if self_size_cur > self_size_max then
                return false;
            end

            local chunkMapPos = self_p_cur - (self_p_centerPoint - self_p_chunkMapOffset);
            local chunk = self_chunkMap:getChunk(chunkMapPos.x, chunkMapPos.y);
            if chunk then
                local sq = chunk:getGridSquare(0, 0, 0);
                if sq then
                    local sqModData = sq:getModData();
                    if sqModData["DebugTestTick"] and sqModData["DebugTestTick"] > _curTick then
                        sqModData["DebugTestTick"] = nil
                    end
                    if ( not sqModData["DebugTestTick"] or sqModData["DebugTestTick"] < _curTick ) then
                        sqModData["DebugTestTick"] = _curTick;
                        return chunk, self_p_cur.copy(), sqModData;
                    end
                else
                    print("No chunkinfo square for: ",chunkMapPos);
                    return false;
                end
            else
                print("Chunk not loaded for: ",chunkMapPos);
                return false;
            end

            self_steps = self_steps + 1;
            local steps_h = self_Floor((self_steps-1)/2);

            if self_size_cur >= 1 then
                local swap, swapdir;
                if self_flip then
                    swap = self_p_A;
                    swapdir = self_p_dirA;
                    cornerPnt.set( self_p_dirA.x, self_p_dirA.y );
                else
                    swap = self_p_B;
                    swapdir = self_p_dirB;
                    cornerPnt.set( self_p_dirB.y ,self_p_dirB.x ); -- x and y inverted to rotate counterclockwise
                end

                if steps_h == self_corner+1 then
                    self_corner = self_corner + (self_size_cur*2);
                end

                swap = swap + swapdir;
                self_p_cur = swap;

                if steps_h == self_corner then
                    local buff = cornerPnt.x;
                    cornerPnt.x = -cornerPnt.y;
                    cornerPnt.y = buff;
                end

                if self_flip then
                    self_p_A = swap;
                    self_p_dirA.set( cornerPnt.x, cornerPnt.y );
                else
                    self_p_B = swap;
                    self_p_dirB.set( cornerPnt.y, cornerPnt.x );
                end

                self_flip = not self_flip and true or false;
            end

            if self_steps == self_size_next then
                self.SetNextRing(self_size_cur + 1);
            end

        end
    end

    return self;
end
