package scripts.rs07.fisher.types.equipment;

import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;

public enum Tool {

	SMALL_NET("Small fishing net", false),
	BIG_NET("Big fishing net", false),
	REG_ROD("Fishing rod", false),
	FLY_ROD("Fly fishing rod", false),
	BARB_ROD("Barbarian fishing rod", false),
	HARPOON("Harpoon", false),
	BARB_HARPOON("Barb-tail harpoon", true),
	LOBSTER_POT("Lobster pot", false);
	
	private String name;
	private boolean equippable;
	
	private Tool(String name, boolean equippable) {
		this.name = name;
		this.equippable = equippable;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCount() {
		if(equippable) {
			return Equipment.getCount(name);
		}
		
		return Inventory.getCount(new String[] { name });
	}
	
	public boolean isEquippable() {
		return equippable;
	}
	
}
