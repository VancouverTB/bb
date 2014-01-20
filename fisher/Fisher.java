package scripts.rs07.fisher;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import scripts.rs07.api.VInventory;
import scripts.rs07.api.Walking;
import scripts.rs07.api.security.Blacklist;
import scripts.rs07.api.security.Whitelist;
import scripts.rs07.fisher.config.Data;
import scripts.rs07.fisher.methods.FishBank;
import scripts.rs07.fisher.methods.Rasolo;
import scripts.rs07.fisher.types.Equipment;
import scripts.rs07.fisher.types.Location;
import scripts.rs07.fisher.types.Spot;
import scripts.rs07.fisher.types.State;

@ScriptManifest(
		authors = { "Vancouver" }, 
		category = "Fishing", 
		name = "Vancouver's Fisher",
		gameMode = 1)

public class Fisher extends Script {

	@Override
	public void run() {
		onStart();
		
		if(!Data.isBlacklisted) {
			State.setState(State.WAIT);
			while(!State.getState().equals(State.STOP)) {
				Mouse.setSpeed(General.random(115, 125));
				
				switch(State.getState()) {
				case CAN_FISH : Data.spot.fish(); break;
				case CAN_DROP : VInventory.dropInventory(Equipment.getEquipmentNames(Data.spot.getEquipment()), Data.mouseKeys); break;
				case CAN_BANK : FishBank.doBanking(); break;
				case CAN_SELL : Rasolo.doTrading(); break;
				case NEED_LOOT_EQUIPMENT : Equipment.getMissingEquipment(Data.spot.getEquipment()).lootGroundItem(); break;
				case NEED_GO_HOME : 
					if(Data.location.equals(Location.GUILD) || Data.location.equals(Location.BAX_FALLS)) {
						if(org.tribot.api2007.Walking.blindWalkTo(Data.location.getHomeArea().getRandomTile())) {
							General.sleep(500, 750);
						}
					} else {
						if(Walking.walkTo(Data.location.getHomeArea().getRandomTile())) {
							General.sleep(500, 750);
						}
					}
					
					break;
				default : break;
				}
				
				if(!State.getState().equals(State.STOP)) {
					State.setState(State.getCurrentState());
					General.sleep(100, 150);
				}
			}
		}
	}
	
	private void onStart() {
		if(Whitelist.isWhitelisted()) {
			Whitelist.displayAcceptedMessage();
			Data.isPremium = true;
		} else if(Blacklist.isBlacklisted()) {
			Blacklist.displayBannedMessage();
			Data.isBlacklisted = true;
		} else {
			Data.isPremium = true;
		}
		
		Data.isBanking = false;
		Data.location = Location.LUMBRIDGE;
		Data.spot = Spot.LURE_BAIT_LURE;
		Data.mouseKeys = false;
	}

}
