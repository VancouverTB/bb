package scripts.rs07.fisher.methods;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;

import scripts.rs07.api.VInventory;
import scripts.rs07.api.utls.Tools;
import scripts.rs07.fisher.config.Data;
import scripts.rs07.fisher.types.Equipment;
import scripts.rs07.fisher.types.State;

public class Rasolo {
	
	private static final RSArea RASOLO_AREA = new RSArea(new RSTile[] { 
			new RSTile(2530, 3432, 0),
			new RSTile(2537, 3430, 0),
			new RSTile(2542, 3425, 0),
			new RSTile(2537, 3420, 0),
			new RSTile(2530, 3420, 0),
			new RSTile(2529, 3427, 0)	
	});
	
	private static RSNPC getRasolo() {
		RSNPC[] npcs = NPCs.find("Rasolo");
		return (npcs.length > 0) ? npcs[0] : null;
	}
	
	private static boolean isTradeScreenUp() {
		return Interfaces.get(300, 75) != null;
	}
	
	private static boolean isRasoloValid() {
		return getRasolo() != null;
	}
	
	private static boolean tradeRasolo() {
		RSNPC npc = getRasolo();
		if(npc != null) {
			if(npc.isOnScreen()) {
				if(DynamicClicking.clickRSNPC(npc, "Trade")) {
					return Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {
							return isTradeScreenUp();
						}
						
					}, General.random(2500, 3500));
				}
			} else if(org.tribot.api2007.Walking.blindWalkTo(npc)) {
				General.sleep(150, 250);
			}
		}

		return isTradeScreenUp();
	}
	
	private static void sellItems() {
		RSItem[] items = Inventory.filterDuplicates(Inventory.find(Inventory.generateFilterGroup(new Filter<RSItem>() {

			@Override
			public boolean accept(RSItem o) {
				return o.getDefinition() != null && !Tools.stringInArray(o.getDefinition().getName(), org.tribot.api2007.Equipment.getEquipmentNames(Data.spot.getEquipment()));
			}
			
		})));
		
		if(items.length > 0) {
			for(RSItem item : items) {
				if(item.getID() == 995) {
					continue;
				}
				
				RSItem[] tItems = Inventory.find(item.getID());
				if(tItems.length > 0) {
					int i = Inventory.getCount(tItems[0].getID());
					
					long t = System.currentTimeMillis();
					while(Inventory.getCount(item.getID()) > 0) {
						if(Timing.timeFromMark(t) > General.random(4500, 5000)) {
							break;
						}
						
						if(i <= 0) {
							break;
						}
						
						try { 
							if(VInventory.clickItem(tItems[tItems.length-1], "Sell 10")) {
								i -= 10;
								General.sleep(50, 75);
							}
						} catch(NullPointerException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	public static void doTrading() {
		int i = 0;
		while(!isRasoloValid()) {
			if(org.tribot.api2007.Walking.blindWalkTo(RASOLO_AREA.getRandomTile())) {
				long t = System.currentTimeMillis();
				while(Player.isMoving()) {
					if(Timing.timeFromMark(t) > General.random(1250, 1500)) {
						break;
					}
					
					General.sleep(50, 75);
				}
			}
			
			if(i >= 15) {
				General.println("Failed to find Rasolo after moving around 15 times, stopping.");
				State.setState(State.STOP);
				break;
			}

			i++;
		}
		
		if(!State.getState().equals(State.STOP)) {
			if(tradeRasolo()) {
				sellItems();
			}
		}
	}
	
}
