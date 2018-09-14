package Model;
import java.util.ArrayList;

public class Player {

	private boolean isDead;
	private PlayerInventory inventory;
	private Item heldItem;
	private ArrayList<Potion> status;
	private String direction;
	
	public Player() {
		this.inventory = new PlayerInventory();
		this.isDead = false;
		this.heldItem = null;
		this.status = new ArrayList<Potion>();
		this.direction = null;
	}
	//How do we make item disappear? are we allowed to pass dungeon in to make it disappear!
	//Coupled with a move method?
	public void pickup(Item i) {
		inventory.storeItem(i);
	}
	public void selectItem(int index) {
		this.heldItem = inventory.getItem(index);
	}
	
	//Maybe pass in player itself as an argument to use, who is using it and where they are using it
	//Consider usage for:
	//Potion - Require player 
	//MeleeWeapons - Should be fine, when we move and have equipped sword (how do we know eqipped?)
	//RangedWeapons - Should be fine as above
	//Bomb - Fine, dungeon square works
	//Key - Find, move onto door, opens door no problem
	public void useItem(Dungeon map) {
		heldItem.use(map);
	}
	public boolean deathStatus() {
		return this.isDead;
	}
	public void addStatus(Potion p) {
		for (Potion a: this.status) {
			if (a.equals(p)) {
				this.status.remove(a);
				this.status.add(p);
				return;
			}
		}
		this.status.add(p);
	}
	public String getDirection() {
		return this.direction;
	}
	public boolean isInvinc() {
		for (Potion p: this.status) {
			if (p.isInvinc()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isHover() {
		for (Potion p: this.status) {
			if (p.isHover()) {
				return true;
			}
		}
		return false;
	}
	public ArrayList<Potion> getStatus() {
		return this.status;
	}
	public PlayerInventory getInventory() {
		return this.inventory;
	}
}

	




	



