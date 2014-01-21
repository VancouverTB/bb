package scripts.rs07.api;

import java.awt.Rectangle;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;

public class VBanking {
	
	private static boolean itemOnScreen(RSItem item) {
		return Interfaces.get(12, 6).getAbsoluteBounds().contains(Interfaces.get(12, 6).getChild(item.getIndex()).getAbsolutePosition());
	}
	
	private static void scroll(boolean down) {
		RSInterfaceComponent button = (down) ? Interfaces.get(12, 7).getChild(5) : Interfaces.get(12, 7).getChild(4);
		if(button != null) {
			Rectangle r = button.getAbsoluteBounds();
			Mouse.moveBox(r.x + 5, r.y + 5, r.x + r.width - 5, r.y + r.height - 5);
			Mouse.sendPress(Mouse.getPos(), 1);
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < General.random(1250, 1500)) {
				General.sleep(75, 100);
			}
			Mouse.sendRelease(Mouse.getPos(), 1);
		}
	}
	
	public static boolean scrollToItem(final RSItem item) {
		// true = down, false = up
		final boolean direction = (Interfaces.get(12, 6).getChild(item.getIndex()).getAbsolutePosition().y > 270) ? true : false;
		return Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {
				scroll(direction);
				General.sleep(75, 100);
				return itemOnScreen(item);
			}
			
		}, General.random(12500, 15000));
	}
	
	public static boolean withdrawItem(int amount, RSItem item) {
		if(!itemOnScreen(item)) {
			if(!scrollToItem(item)) {
				General.println("Timed-out trying to find " + item.name);
				return false;
			}
		}
		
		return Banking.withdrawItem(item, amount);
	}
	
	public static boolean openBankBooth(String name, String option) {
		if(!Banking.isBankScreenOpen() && !Banking.isPinScreenOpen()) {
			RSObject[] objects = Objects.findNearest(10, name);
			if(objects.length > 0) {
				if(DynamicClicking.clickRSObject(objects[0], option)) {
					return Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {
							return Banking.isBankScreenOpen() || Banking.isPinScreenOpen();
						}
						
					}, General.random(2500, 3500));
				}
			}
		}
		
		return Banking.isBankScreenOpen() || Banking.isPinScreenOpen();
	}
	
	public static boolean openBankNPC(String name, String option) {
		if(!Banking.isBankScreenOpen() && !Banking.isPinScreenOpen()) {
			RSNPC[] npcs = NPCs.findNearest(name);
			if(npcs.length > 0) {
				if(DynamicClicking.clickRSNPC(npcs[0], option)) {
					return Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {
							return Banking.isBankScreenOpen() || Banking.isPinScreenOpen();
						}
						
					}, General.random(2500, 3500));
				}
			}
		}
		
		return Banking.isBankScreenOpen() || Banking.isPinScreenOpen();
	}
	
	public static boolean openDepositBox() {
		return false;
	}
	
}
