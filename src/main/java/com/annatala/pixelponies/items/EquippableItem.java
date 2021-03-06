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
package com.annatala.pixelponies.items;

import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.effects.particles.ShadowParticle;
import com.annatala.utils.GLog;

public abstract class EquippableItem extends Item {

	public static final String AC_EQUIP		= Game.getVar(R.string.EquipableItem_ACEquip);
	public static final String AC_UNEQUIP	= Game.getVar(R.string.EquipableItem_ACUnequip);
	public static final String AC_EQUIP_HORN = Game.getVar(R.string.EquipableItem_ACEquipHorn);
	public static final String AC_EQUIP_MANE = Game.getVar(R.string.EquipableItem_ACEquipMane);
	public static final String AC_EQUIP_TAIL = Game.getVar(R.string.EquipableItem_ACEquipTail);

	private static final String TXT_UNEQUIP_CURSED	= Game.getVar(R.string.EquipableItem_Unequip);

	protected int	minAttribute = 3;

	public int minAttribute() {
		return minAttribute;
	}

	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_EQUIP_HORN )) {
			doEquipHorn(hero);
		} else if (action.equals ( AC_EQUIP_MANE )) {
			doEquipMane(hero);
		} else if (action.equals( AC_EQUIP_TAIL ) || action.equals( AC_EQUIP )) {
			doEquip( hero );
		} else if (action.equals( AC_UNEQUIP )) {
			doUnequip( hero, true );
		} else {
			super.execute( hero, action );
		}
	}
	
	@Override
	public void doDrop( Hero hero ) {
		if (!isEquipped( hero ) || doUnequip( hero, false, false )) {
			super.doDrop( hero );
		}
	}
	
	@Override
	public void cast( final Hero user, int dst ) {

		if (isEquipped( user )) {
			if (quantity() == 1 && !this.doUnequip( user, false, false )) {
				return;
			}
		}
		
		super.cast( user, dst );
	}
	
	protected static void equipCursed( Hero hero ) {
		hero.getSprite().emitter().burst( ShadowParticle.CURSE, 6 );
		Sample.INSTANCE.play( Assets.SND_CURSED );
	}
	
	protected float time2equip( Hero hero ) {
		return 1;
	}
	
	public abstract boolean doEquip( Hero hero );

	public boolean doEquipMane( Hero hero ) { return false; }

	public boolean doEquipHorn( Hero hero ) { return false; }
	
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		
		if (cursed) {
			GLog.w( TXT_UNEQUIP_CURSED, name() );
			return false;
		}
		
		if (single) {
			hero.spendAndNext( time2equip( hero ) );
		} else {
			hero.spend( time2equip( hero ) );
		}
		
		if (collect && !collect( hero.belongings.backpack )) {
			Dungeon.level.drop( this, hero.getPos() );
		}
				
		return true;
	}
	
	public boolean doUnequip( Hero hero, boolean collect ) {
		return doUnequip( hero, collect, true );
	}
}
