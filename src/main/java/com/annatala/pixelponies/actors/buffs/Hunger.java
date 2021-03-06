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
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.ResultDescriptions;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroSubClass;
import com.annatala.pixelponies.items.rings.RingOfSatiety;
import com.annatala.pixelponies.ui.BuffIndicator;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

public class Hunger extends Buff implements Hero.Doom {

	private static final float STEP	= 10f;
	
	public static final float HUNGRY	= 260f;
	public static final float STARVING	= 360f;
	
	private static final String[] TXT_HUNGRY	= Game.getVars(R.array.Hunger_Hungry);
	private static final String[] TXT_STARVING	= Game.getVars(R.array.Hunger_Starving);
	
	private static final String TXT_DEATH		= Game.getVar(R.string.Hunger_Death);
	
	private float level;

	private static final String LEVEL	= "level";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		level = bundle.getFloat( LEVEL );
	}
	
	@Override
	public boolean act() {
		if (target.isAlive()) {
			
			Hero hero = (Hero)target;
			
			if (isStarving()) {

				// Make starving a little more likely, but require a luck bonus check.
				if (Random.Float() < 0.45F && (target.hp() > 1 || !target.paralysed) && !Random.luckBonus()) {
					
					GLog.n( TXT_STARVING[hero.gender().ordinal()] );
					
					if(hero.getDifficulty() >= 3) {
						hero.damage(Math.max((hero.ht()-15)/10, 1), this);
					} else {
						hero.damage( 1, this );
					}
					
					hero.interrupt();
				}
				
				if(hero.getDifficulty() >= 3) {
					if(Random.Float() < 0.01) {
						Buff.prolong(hero, Weakness.class, Weakness.duration(hero));
					}
					
					if(Random.Float() < 0.01) {
						Buff.prolong(hero, Vertigo.class, Vertigo.duration(hero));
					}
				}
				
			} else {	
				
				int bonus = 0;
				for (Buff buff : target.buffs( RingOfSatiety.Satiety.class )) {
					bonus += ((RingOfSatiety.Satiety)buff).level;
				}
				
				float delta = STEP - bonus;

				// Add in extra hunger for humorous ponies.
				// This starts to pay back once you hit (Haste*2+L:)10.
				delta += ((float) (hero.effectiveLaughter()-3)) * 0.07F;
				
				if(PixelPonies.realtime()) {
					delta *= 0.3F;
				}
				
				if(hero.getDifficulty() == 0) {
					delta *= 0.8F;
				}
				
				float newLevel = level + delta;
				boolean statusUpdated = false;
				if (newLevel >= STARVING) {
					
					GLog.n( TXT_STARVING[hero.gender().ordinal()] );
					statusUpdated = true;
					
					hero.interrupt();
					
				} else if (newLevel >= HUNGRY && level < HUNGRY) {
					
					GLog.w( TXT_HUNGRY[hero.gender().ordinal()] );
					statusUpdated = true;
					
				}
				level = newLevel;
				
				if (statusUpdated) {
					BuffIndicator.refreshHero();
				}
				
			}
			
			float step = hero.subClass == HeroSubClass.FARMER ? STEP * 1.2f : STEP;
			spend( target.buff( Shadows.class ) == null ? step : step * 1.5f );
			
		} else {
			
			deactivate();
			
		}
		
		return true;
	}
	
	public void satisfy( float energy ) {
		level -= energy;
		if (level < 0) {
			level = 0;
		} else if (level > STARVING) {
			level = STARVING;
		}
		
		BuffIndicator.refreshHero();
	}
	
	public boolean isStarving() {
		return level >= STARVING;
	}
	
	@Override
	public int icon() {
		if (level < HUNGRY) {
			return BuffIndicator.NONE;
		} else if (level < STARVING) {
			return BuffIndicator.HUNGER;
		} else {
			return BuffIndicator.STARVATION;
		}
	}
	
	@Override
	public String toString() {
		if (level < STARVING) {
			return Game.getVar(R.string.Hunger_Info1);
		} else {
			return Game.getVar(R.string.Hunger_Info2);
		}
	}

	@Override
	public boolean attachTo( Char target ) {

		if(target.buff(Hunger.class) != null) {
			return false;
		}

		return super.attachTo(target);
	}

	@Override
	public void onDeath() {
		
		Badges.validateDeathFromHunger();
		
		Dungeon.fail( Utils.format( ResultDescriptions.HUNGER, Dungeon.depth ) );
		GLog.n( TXT_DEATH );
	}
}
