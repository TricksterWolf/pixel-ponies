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
package com.annatala.pixelponies.actors.mobs;

import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Cripple;
import com.annatala.pixelponies.actors.buffs.Light;
import com.annatala.pixelponies.actors.buffs.Poison;
import com.annatala.pixelponies.items.food.MysteriousHay;
import com.annatala.pixelponies.items.potions.PotionOfHealing;
import com.annatala.pixelponies.items.weapon.enchantments.Leech;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.pixelponies.sprites.ScorpioSprite;
import com.annatala.utils.Random;

public class Scorpio extends Mob {
	
	public Scorpio() {
		spriteClass = ScorpioSprite.class;
		
		hp(ht(95));
		defenseSkill = 24;
		viewDistance = Light.DISTANCE;
		
		EXP = 14;
		maxLvl = 25;
		
		loot = new PotionOfHealing();
		lootChance = 0.125f;
		
		RESISTANCES.add( Leech.class );
		RESISTANCES.add( Poison.class );
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 20, 32 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 36;
	}
	
	@Override
	public int dr() {
		return 16;
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return !Dungeon.level.adjacent( getPos(), enemy.getPos() ) && Ballistica.cast( getPos(), enemy.getPos(), false, true ) == enemy.getPos();
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (Random.Int( 5 ) < 3 && !Random.luckBonus()) {
			Buff.prolong( enemy, Cripple.class, Cripple.DURATION );
		}
		
		return damage;
	}
	
	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	protected void dropLoot() {

		// Slight increase in drop chance for the lucky.
		boolean bonusLootChance = Random.luckBonus() && Random.luckBonus() && Random.luckBonus();

		if (Random.Int( 9 ) == 0 || bonusLootChance) {
			Dungeon.level.drop( new PotionOfHealing(), getPos() ).sprite.drop();
		} else if (Random.Int( 7 ) == 0 || bonusLootChance) {
			Dungeon.level.drop( new MysteriousHay(), getPos() ).sprite.drop();
		}
	}	
}
