package scripts.rs07.fisher.types.equipment;

import org.tribot.api2007.Inventory;

public enum Bait {

	FEATHERS(""),
	FISHING_BAIT(""),
	CUTLINGS(""),
	ROE(""),
	CAVIAR("");
	
	private String name;
	
	private Bait(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCount() {
		return Inventory.getCount(new String[] { name });
	}
	
}
