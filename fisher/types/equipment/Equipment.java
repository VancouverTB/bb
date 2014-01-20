package scripts.rs07.fisher.types.equipment;

import java.util.ArrayList;

public enum Equipment {

	REG_LURE(null, null),
	REG_BAIT(null, null);
	
	private Tool tool;
	private Bait[] bait;
	
	private Equipment(Tool tool, Bait... bait) {
		this.tool = tool;
		this.bait = bait;
	}
	
	public Tool getTool() {
		return tool;
	}
	
	public Bait[] getBait() {
		return bait;
	}
	
	public static boolean isMissingEquipment(Equipment equipment) {
		if(equipment.getTool() != null) {
			if(equipment.getTool().getCount() == 0) {
				return true;
			}
		}
		
		if(equipment.getBait() != null) {
			for(Bait bait : equipment.getBait()) {
				if(bait.getCount() > 0) {
					return false;
				}
			}
		}

		return true;
	}
	
	public static String[] getEquipmentNames(Equipment equipment) {
		ArrayList<String> list = new ArrayList<String>(20);
		
		if(equipment.getTool() != null) {
			list.add(equipment.getTool().getName());
		}
		
		if(equipment.getBait() != null) {
			for(Bait bait : equipment.getBait()) {
				list.add(bait.getName());
			}
		}
		
		return list.toArray(new String[list.size()]);
	}
	
	public static Tool getMissingTool() {
		return null;
	}
	
}
