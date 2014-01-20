package scripts.rs07.fisher.types;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;

import scripts.rs07.fisher.config.Data;

public enum State {

	STOP, WAIT, CAN_FISH, CAN_DROP, CAN_BANK, CAN_SELL, NEED_GO_HOME, 
	NEED_LOOT_EQUIPMENT;
	
	private static State state;
	
	public static State getState() {
		return state;
	}
	
	public static void setState(State s) {
		state = s;
	}
	
	public static State getCurrentState() {
		if(Player.isMoving() || !Login.getLoginState().equals(Login.STATE.INGAME)) {
			return WAIT;
		}
		
		if(Inventory.isFull()) {
			if(Data.location.equals(Location.BAX_FALLS) && !Data.isBanking) {
				return CAN_SELL;
			}
			
			if(Data.isBanking) {
				return CAN_BANK;
			} else {
				return CAN_DROP;
			}
		}
		
		if(Equipment.getMissingEquipment(Data.spot.getEquipment()) != null) {
			Equipment equipment = Equipment.getMissingEquipment(Data.spot.getEquipment());
			if(equipment.isOnGround()) {
				return NEED_LOOT_EQUIPMENT;
			} else if(Data.isBanking) {
				General.println("Missing " + equipment.getName() + ", banking for another.");
				return CAN_BANK;
			} else {
				General.println("Missing " + equipment.getName() + ", stopping.");
				return STOP;
			}
		}
		
		if(!Data.location.isAtHome()) {
			return NEED_GO_HOME;
		}
		
		if(Player.getAnimation() == -1 || Data.currentSpot == null || !Spot.spotIsValid(Data.currentSpot)) {
			return CAN_FISH;
		}
		
		return WAIT;
	}
	
}
