package com.annatala.pixelponies.items.weapon.melee;

import com.annatala.pixelponies.items.Item;

public class WoodenBow extends Bow {

	public WoodenBow() {
		super( 1, 0.8f, 1.5f );
		imageFile = "items/ranged.png";
		image = 0;
	}
	
	@Override
	public Item burn(int cell) {
		return null;
	}
	
	@Override
	public double acuFactor() {
		return 1 + level() * 0.1;
	}
	
	@Override
	public double dmgFactor() {
		return 1 + level() * 0.25;
	}
}
