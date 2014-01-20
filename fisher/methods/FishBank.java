package scripts.rs07.fisher.methods;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSTile;

import scripts.rs07.api.VBanking;
import scripts.rs07.api.Walking;
import scripts.rs07.fisher.config.Data;
import scripts.rs07.fisher.types.Location;
import scripts.rs07.fisher.types.State;

public class FishBank {
	
	private static boolean needEquipment() {
		return Equipment.getMissingEquipment(Data.spot.getEquipment()) != null;
	}
	
	private static boolean withdrawEquipment() {
		Equipment equipment = Equipment.getMissingEquipment(Data.spot.getEquipment());
		if(equipment != null) {
			RSItem[] items = Banking.find(equipment.getName());
			if(items.length > 0) {
				int amount = 1;
				if(equipment.getName().equals("Feathers") || equipment.getName().equals("Fishing bait")) {
					amount = 0;
				}
				
				return VBanking.withdrawItem(amount, items[0]);
			} else {
				General.println("No more " + equipment.getName() + " in our bank, stopping.");
			}
		}
		
		return false;
	}
	
	public static void doBanking() {
		General.println(Data.location.isAtBank());
		
		if(Data.location.isAtBank()) {
			if(VBanking.openBankNPC("Banker", "Bank Bank")) {
				if(needEquipment()) {
					if(!withdrawEquipment()) {
						State.setState(State.STOP);
						return;
					}
				}
				
				Banking.depositAllExcept(Equipment.getEquipmentNames(Data.spot.getEquipment()));
				int i = General.random(1, 7);
				if(i == 1) {
					if(Banking.close()) {
						General.sleep(50, 75);
					}
				}
			}
		} else {
			RSTile tile = Data.location.getBankArea().getRandomTile();
			if(Data.location.equals(Location.GUILD)) {
				if(org.tribot.api2007.Walking.blindWalkTo(tile)) {
					General.sleep(500, 750);
				}
			} else {
				if(Walking.walkTo(tile)) {
					General.sleep(500, 750);
				}
			}
		}
	}
	
}
