package scripts.rs07.fisher.types;

import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public enum Location {

	LUMBRIDGE(
			new RSArea(new RSTile(3207, 3220, 2), new RSTile(3210, 3218, 2)), // Bank
			new RSArea(new RSTile(3240, 3258), new RSTile(3243, 3239)), // Home
			new Spot[] { Spot.LURE_BAIT_BAIT, Spot.LURE_BAIT_LURE }),
			
	DRAYNOR(
			new RSArea(new RSTile(3092, 3245), new RSTile(3094, 3242)), // Bank
			new RSArea(new RSTile(3084, 3234), new RSTile(3089, 3225)), // Home
			new Spot[] { Spot.NET_BAIT_NET, Spot.NET_BAIT_BAIT }),
	
	BARB_VILLAGE(
			new RSArea(new RSTile(3091, 3494), new RSTile(3094, 3493)), // Bank
			new RSArea(new RSTile(3103, 3437), new RSTile(3112, 3445)), // Home
			new Spot[] { Spot.LURE_BAIT_BAIT, Spot.LURE_BAIT_LURE}),
	
	CATHERBY(
			new RSArea(new RSTile(2807, 3441), new RSTile(2811, 3439)), // Bank
			new RSArea(new RSTile(2835, 3435), new RSTile(2849, 3429)),  // Home
			new Spot[] { 
				Spot.NET_BAIT_NET, Spot.NET_BAIT_BAIT, 
				Spot.NET_HARPOON_NET, Spot.NET_HARPOON_HARPOON, 
				Spot.CAGE_HARPOON_CAGE, Spot.CAGE_HARPOON_HARPOON
		}),
	
	GUILD(
			new RSArea(new RSTile(2586, 3422), new RSTile(2588, 3418)), // Bank
			new RSArea(new RSTile(2596, 3430), new RSTile(2604, 3420)), // Home
			new Spot[] { 
				Spot.CAGE_HARPOON_CAGE, Spot.CAGE_HARPOON_HARPOON,
				Spot.NET_HARPOON_HARPOON, Spot.NET_HARPOON_NET
		}),
	
	COLONY(
			new RSArea(new RSTile(0, 0), new RSTile(0, 0)), // Bank 
			new RSArea(new RSTile(0, 0), new RSTile(0, 0)), // Home
			new Spot[] { null, null }),
	
	SHILO(
			new RSArea(new RSTile(0, 0), new RSTile(0, 0)), // Bank
			new RSArea(new RSTile(0, 0), new RSTile(0, 0)), // Home
			new Spot[] { null, null }),
	
	GROTTO(
			null, // We don't use a bank for Grotto
			new RSArea(new RSTile(2498, 3513), new RSTile(2507, 3491)), // Home
			new Spot[] { null, null }),
	
	BAX_FALLS(
			null, // We don't use a bank area for Bax. Falls
			new RSArea(new RSTile[] { // Home
					new RSTile(2527, 3414, 0),
					new RSTile(2534, 3411, 0),
					new RSTile(2540, 3407, 0),
					new RSTile(2533, 3408, 0),
					new RSTile(2528, 3413, 0)
					}),
			new Spot[] { Spot.LURE_BAIT_BAIT, Spot.LURE_BAIT_LURE }),
	
	WILDERNESS(
			new RSArea(new RSTile(0, 0), new RSTile(0, 0)), // Bank
			new RSArea(new RSTile(0, 0), new RSTile(0, 0)), // Home
			new Spot[] { null, null });
	
	private RSArea bankArea;
	private RSArea homeArea;
	private Spot[] spots;
	
	private Location(RSArea bankArea, RSArea homeArea, Spot[] spots) {
		this.bankArea = bankArea;
		this.homeArea = homeArea;
		this.spots = spots;
	}
	
	public RSArea getHomeArea() {
		return homeArea;
	}
	
	public RSArea getBankArea() {
		return bankArea;
	}
	
	public Spot[] getSpots() {
		return spots;
	}
	
	public boolean isAtHome() {
		return homeArea.contains(Player.getPosition());
	}
	
	public boolean isAtBank() {
		return bankArea.contains(Player.getPosition());
	}
	
}
