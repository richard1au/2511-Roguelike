package Model.Tile;

import Model.Paintable;
import javafx.scene.image.Image;

public abstract class Tile extends Paintable {

	public boolean isReachable(EntityType type) {
		return true;
	}
	
	public abstract Type getType();
	
	public boolean isType(Type tiletype) {
		return getType().equals(tiletype);
	}
	
}