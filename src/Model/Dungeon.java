package Model;


import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import Model.Tile.TileType;

public class Dungeon {
    public final int MAX_SIZE = 20;


    private Point topLeft;
    private Point bottomRight;

    private Map<Point, Tile> tileGrid;
    private Map<Point, ComputerAgent> agentGrid;
    private Point playerPosition;
    private Player player;
    //private Map<Point, Pickups> pickGrid;

    public Dungeon(int size) throws IllegalArgumentException{
    	playerPosition = null;
    	agentGrid = new HashMap<Point, ComputerAgent>();
    	
        if (size > MAX_SIZE || size < 1) {
            throw new IllegalArgumentException("Dungeon constructor size param 1-20. Received " + size);
        }

        this.tileGrid = initTileGrid(size);

        this.topLeft = new Point(0, 0);
        this.bottomRight = new Point(size+1, size+1);
    }
   

    public Map<Point, Tile> getTileGrid() {
        return tileGrid;
    }


    public Point getTopLeft() {
        return topLeft;
    }


    public Point getBottomRight() {
        return bottomRight;
    }


    /**
     * Generate a HashMap of Tiles, keyed by Point location
     *
     * The Map is a (size) by (size) grid of no tiles, ringed by
     * a double-wall of INVINCIBLE_TILES.
     *
     * @param (size > 0 && size <= MAX_SIZE)
     * @return A default empty dungeon
     */

    private HashMap<Point, Tile> initTileGrid(int size) {

        if (size < 1) {
            // TODO: refine this error checking
            System.err.println("tried to make a dungeon grid size less than 1");
            System.exit(1);
        }

        HashMap<Point, Tile> ret = new HashMap<Point, Tile>();
        int[] edges = {-1, 0, size+1, size+2};
        // Set the dungeons walls on left/right sides
        for(int i = -1; i <= size + 2; i++) {
            for (int j : edges) {
                ret.put(new Point(i, j), new Tile(Tile.TileType.INVINCIBLE_WALL));
            }
        }

        // Set the dungeons walls on remaining top/bottom sides
        for (int i : edges) {
            for (int j = 1; j <= size; j++) {
                ret.put(new Point(i, j), new Tile(Tile.TileType.INVINCIBLE_WALL));
            }
        }
        //Set rest of tiles to default tiles that allow for free movement
        for(int i = 1; i <= size; i++) {
        	for(int j = 1; j <= size; j++) {
        		ret.put(new Point(i,j), new Tile(Tile.TileType.DEFAULT));
        	}
        }

        return ret;
    }

    /**
     * Default tile generator.
     *
     * Makes a Tile Grid of MAX_SIZE
     * @return A default empty dungeon size MAX_SIZE
     */
 
    private HashMap<Point, Tile> initTileGrid() {
        return initTileGrid(this.MAX_SIZE);
    }

    /**
     * Exposes the type of tile at a location
     * @param location
     * @return Tile.TileType
     */

    public Tile.TileType pointTileType(Point location) {
        Tile local = tileGrid.get(location);
        if (local == null) {
            return null;
        }

        return local.getType();
    }

    /**
     * Adds a tile to a location
     * @param tileType The Tile type that is to be placed
     * @param myPoint The location to place the Tile
     * @return true if Tile placed
     * @throws IllegalArgumentException when myPoint outside topLeft and bottomRight defined boundaries
     */
    public boolean placeTile(Tile.TileType tileType, Point myPoint) throws IllegalArgumentException {

        // Cannot place invincible wall
        if(tileType == Tile.TileType.INVINCIBLE_WALL) {
            return false;
        }

        int aX = myPoint.x;
        int aY = myPoint.y;

        int top = topLeft.y;
        int left = topLeft.x;
        int bot = bottomRight.y;
        int right = bottomRight.x;

        if (aX < left || aX > right ||
            aY > bot || aY < top) {
            throw new IllegalArgumentException("Placement out of bounds");
        }
        //If no tile
        if (tileGrid.get(myPoint) == null) {
            tileGrid.put(myPoint, new Tile(tileType));
            return true;
        }
        //If tile already exists, simply switch type!
        if(tileGrid.get(myPoint) != null) {
        	tileGrid.get(myPoint).setType(tileType);
        	return true;
        }

        return false;
    }

    /**
     * Inserts a new ComputerAgent object into the agentGrid
     * @param a agent to be placed
     * @param agentPoint Location to be placed
     * @TODO Ensure that bad placedment attempt throuws Exception
     * @TODO Ensure that agent can be placed.
     */
    public void placeComputerAgent(ComputerAgent a, Point agentPoint) {
    	agentGrid.put(agentPoint, a);
    	a.setPos(agentPoint);
    }
    /**
     * Inserts a new Player object into the dungeon
     * @param p Player to be placed
     * @param playerStart Location to be placed
     * @TODO Ensure that bad placement attempt throuws Exception
     * @TODO Ensure that Player can be placed.
     */
    public void placePlayer(Player p, Point playerStart) {
    	playerPosition = playerStart;
    	player = p;
    }
    public Player getPlayer() {
    	return this.player;
    }

    /**
     * Utilises entrySet iterator
     * Iterates over agentGrid to move agents
     * Grabs new position
     * Deletes old entry in agent hashmap
     * Enters new entry
     */
    public void updateAgents() {
    	for(Map.Entry<Point,ComputerAgent> entry : agentGrid.entrySet()) {
    		Point updatePos = entry.getValue().move(this);
    		agentGrid.remove(entry.getKey());
    		if(!entry.getValue().deathStatus()) { //If agent still has health after its turn
    			//TODO: use collision checker to simply remove dead things! alot easier
    			agentGrid.put(updatePos, entry.getValue()); //Give new position, otherwise removed forever
    		}
    	}
    }
    public void updatePlayer(char key) {
    	int x = (int) this.playerPosition.getX();
    	int y = (int) this.playerPosition.getY();
    	switch (key) {
    		case 'a':
    			Point left = new Point(x-1, y);
    			if (isValidMove(left)) {
    				this.playerPosition = left;
    				this.player.setDirection("left");
    			}
    			break;
    		case 's':
    			Point down = new Point(x, y-1);
    			if (isValidMove(down)) {
    				this.playerPosition = down;
    				this.player.setDirection("down");
    			}
    			break;
    		case 'd':
    			Point right = new Point(x+1, y);
    			if (isValidMove(right)) {
    				this.playerPosition = right;
    				this.player.setDirection("right");
    			}
    			break;
    		case 'w':
    			Point up = new Point(x, y+1);
    			if (isValidMove(up)) {
    				this.playerPosition = up;
    				this.player.setDirection("up");
    			}
    			break;
    	}
    	//TODO: some collision check function?		
    }

    public Point getPlayerPos() {
    	return this.playerPosition;
    }
    
    
    /**
     * Checks if tile to be moved on is valid to move on.
     * @param check
     * @return
     */
    public boolean isValidMove(Point check) {
    	//Checks cases for types of tiles that can't be moved on
    	if (check == null) return false;
    	Tile tileA = tileGrid.get(check);
    	if (tileA != null) {
    		TileType type = tileA.getType();
    		switch (type) {
    		case INVINCIBLE_WALL:
    			return false;
    		case CLOSED_DOOR:
    			return false;
    		case PIT:
    			return false;
    		//TODO: make it so players will go in pit valid movement, but enemies wont? how to implement reuse
    		case DESTRUCTABLE_WALL:
    			return false;
    		}
    	}
    	return true;
    }
    /**
     * Typically called after isValidMove(Point) to further verify for
     * agents, so agents do not overlap
     * Player object do not call this, as they can fight agents, agents can't
     * fight other agents
     * @param check
     * @return
     */
    public boolean isAgentExist(Point check) {
    	//If agent already on that spot
    	if(agentGrid.containsKey(check)) {
    		return true;
    	}
    	return false;
    }
}

