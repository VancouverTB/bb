package scripts.rs07.fisher.types;

import java.util.Arrays;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSNPC;

import scripts.rs07.api.Walking;
import scripts.rs07.fisher.config.Data;
import scripts.rs07.fisher.types.equipment.Equipment;

public enum Spot {

	LURE_BAIT_LURE("Lure", new String[] { "Lure", "Bait" }, 
			null),
			
	LURE_BAIT_BAIT("Bait", new String[] { "Lure", "Bait" }, 
			null),
			
	NET_BAIT_NET("Net", new String[] { "Net", "Bait" },
			null),
			
	NET_BAIT_BAIT("Bait", new String[] { "Net", "Bait" },
			null),
			
	CAGE_HARPOON_CAGE("Cage", new String[] { "Cage", "Harpoon" },
			null),
			
	CAGE_HARPOON_HARPOON("Harpoon", new String[] { "Cage", "Harpoon" },
			null),
	
	NET_HARPOON_NET("Net", new String[] { "Net", "Harpoon" },
			null),
		
	NET_HARPOON_HARPOON("Harpoon", new String[] { "Net", "Harpoon" },
			null);
	
	private String option;
	private String[] spotOptions;
	private Equipment equipment;
	
	private Spot(String option, String[] spotOptions, Equipment equipment) {
		this.option = option;
		this.spotOptions = spotOptions;
		this.equipment = equipment;
	}

	public Equipment getEquipment() {
		return equipment;
	}
	
	public static boolean spotIsValid(RSNPC npc) {
		RSModel model = npc.getModel();		
		try { 
			return model != null && model.getPoints() != null && model.getPoints().length == 36 && 
					npc.getName().equals("Fishing spot") && 
					Arrays.equals(npc.getActions(), Data.spot.spotOptions);
		} catch(NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private RSNPC getFishingSpot() {
		RSNPC[] npcs = NPCs.findNearest(NPCs.generateFilterGroup(new Filter<RSNPC>() {

			@Override
			public boolean accept(RSNPC o) {
				return o != null && spotIsValid(o);
			}
			
		}));
		
		return (npcs.length > 0) ? npcs[0] : null;
	}
	
	public void fish() {
		RSNPC npc = null;
		// Fail-safe to prevent getting stuck when the npc returns null
		int i = 0;
		while(i != 5) {
			npc = getFishingSpot();
			if(npc != null) {
				break;
			}
			
			General.println("No spots in proximity, moving around the fishing area.");
			if(Walking.walkTo(Data.location.getHomeArea().getRandomTile())) {
				General.sleep(500, 750);
			}

			i++;
		}
		
		if(npc != null) {
			if(npc.isOnScreen()) {
				if(DynamicClicking.clickRSNPC(npc, option)) {
					if(!Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {
							return Player.getAnimation() != -1;
						}
						
					}, General.random(2500, 3500))) {
						General.println("Timed-out waiting to start fishing.");
					}
					
					if(Player.getAnimation() != -1) {
						Data.currentSpot = npc;
					}
				}
			} else if(Walking.walkTo(npc)) {
				General.sleep(500, 750);
			}
		} else {
			General.println("No fish spots were found at all in the home area, stopping.");
			State.setState(State.STOP);
		}		
	}
	
}
