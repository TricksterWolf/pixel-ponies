/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.annatala.pixelponies.actors.buffs;

import com.annatala.noosa.Game;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.items.rings.RingOfElements.Resistance;
import com.annatala.pixelponies.ui.BuffIndicator;

public class Weakness extends FlavourBuff {

	private static final float DURATION = 40f;

	@Override
	public int icon() {
		return BuffIndicator.WEAKNESS;
	}
	
	@Override
	public String toString() {
		return Game.getVar(R.string.Weakness_Info);
	}
	
	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			Hero hero = (Hero)target;
			hero.weakened = true;
			hero.belongings.discharge();
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		((Hero)target).weakened = false;
	}
	
	public static float duration( Char ch ) {
		Resistance r = ch.buff( Resistance.class );
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}
}