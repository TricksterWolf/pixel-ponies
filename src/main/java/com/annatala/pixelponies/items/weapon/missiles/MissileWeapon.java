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
package com.annatala.pixelponies.items.weapon.missiles;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroClass;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.weapon.Weapon;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.utils.Utils;
import com.annatala.pixelponies.windows.WndOptions;

import java.util.ArrayList;

public class MissileWeapon extends Weapon {

	private static final String TXT_MISSILES = Game.getVar(R.string.MissileWeapon_Missiles);
	private static final String TXT_YES      = Game.getVar(R.string.MissileWeapon_Yes);
	private static final String TXT_NO       = Game.getVar(R.string.MissileWeapon_No);
	private static final String TXT_R_U_SURE = Game.getVar(R.string.MissileWeapon_Sure);
	
	{
		stackable = true;
		levelKnown = true;
		defaultAction = AC_THROW;
	}

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.remove( AC_EQUIP );
		actions.remove( AC_UNEQUIP );
		return actions;
	}

	@Override
	protected void onThrow( int cell ) {
		Char enemy = Actor.findChar( cell );
		if (enemy == null || enemy == getCurUser()) {
			super.onThrow( cell );
		} else {
			if (!getCurUser().shoot( enemy, this )) {
				miss( cell );
			}
		}
	}
	
	protected void miss( int cell ) {

		if(this instanceof Arrow) {
			Arrow arrow = (Arrow) this;
			if(arrow.firedFrom != null ) {
				arrow.firedFrom.onMiss();
			}
		}
		
		super.onThrow( cell );
	}
	
	@Override
	public void proc(Char attacker, Char defender, int damage) {

		super.proc(attacker, defender, damage);
		
		if(this instanceof Arrow) {
			Arrow arrow = (Arrow) this;
			if(arrow.firedFrom != null && arrow.firedFrom.isEnchanted()) {
				arrow.firedFrom.getEnchantment().proc( arrow.firedFrom, attacker, defender, damage );
			}
		}

		Hero hero = (Hero) attacker;
		if (hero.rangedWeapon == null && stackable) {
			if (this != hero.belongings.weapon) {
				if (quantity() == 1) {
					doUnequip(hero, false, false);
				} else {
					detach(null);
				}
			}
		}
	}
	
	@Override
	public boolean doEquip( final Hero hero ) {
		GameScene.show( 
			new WndOptions( TXT_MISSILES, TXT_R_U_SURE, TXT_YES, TXT_NO ) {
				@Override
				protected void onSelect(int index) {
					if (index == 0) {
						MissileWeapon.super.doEquip( hero );
					}
				}
			}
		);
		
		return false;
	}
	
	@Override
	public Item random() {
		return this;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {
		
		StringBuilder info = new StringBuilder( desc() );
		
		info.append(Utils.format(Game.getVar(R.string.MissileWeapon_AverageDam), MIN + (MAX - MIN) / 2));

		if (Dungeon.hero.belongings.backpack.items.contains( this ) && !(this instanceof Arrow)) {

			int effectiveHonesty = Dungeon.hero.effectiveHonesty();

			// Report zeebee bonuses, to be more transparent.
			if (Dungeon.hero.heroClass == HeroClass.ZEBRA) {
				if (minAttribute > effectiveHonesty + 2) {
					info.append(" ");
					info.append(Utils.format(Game.getVar(R.string.MissileWeapon_InadequateHonestyZebra), name));
				} else if (minAttribute > effectiveHonesty) {
					info.append(" ");
					info.append(Utils.format(Game.getVar(R.string.MissileWeapon_BareHonestyZebra), name));
				} else if (minAttribute < effectiveHonesty) {
					info.append(" ");
					info.append(Utils.format(Game.getVar(R.string.MissileWeapon_ExcessHonestyZebra), name));
				}
			} else if (minAttribute > effectiveHonesty){
				info.append(" ");
				info.append(Utils.format(Game.getVar(R.string.MissileWeapon_InadequateHonesty), name));
			}

		}

		// This should no longer be possible.
//		if (isEquipped( Dungeon.hero )) {
//			info.append(Utils.format(Game.getVar(R.string.MissileWeapon_HoldReady), name));
//		}
		
		return info.toString();
	}
	
	@Override
	public boolean isFliesStraight() {
		return true;
	}
}
