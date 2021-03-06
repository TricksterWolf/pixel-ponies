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

import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.Gender;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Charm;
import com.annatala.pixelponies.actors.buffs.Light;
import com.annatala.pixelponies.actors.buffs.Sleep;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.quest.DriedRose;
import com.annatala.pixelponies.items.scrolls.ScrollOfLullaby;
import com.annatala.pixelponies.items.wands.WandOfBlink;
import com.annatala.pixelponies.items.weapon.enchantments.Leech;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.pixelponies.sprites.SuccubusSprite;
import com.annatala.utils.Random;

public class Succubus extends Mob {

	private static final int BLINK_DELAY = 5;

	private int delay = 0;

	public Succubus() {
		gender = Gender.FEMININE;

		spriteClass = SuccubusSprite.class;

		hp(ht(80));
		defenseSkill = 25;
		viewDistance = Light.DISTANCE;

		EXP = 12;
		maxLvl = 25;

		loot = new ScrollOfLullaby();
		lootChance = 0.05f;

		RESISTANCES.add(Leech.class);
		IMMUNITIES.add(Sleep.class);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(15, 25);
	}

	@Override
	public int attackProc(Char enemy, int damage) {

		if (Random.Int(2) == 0 && !Random.luckBonus()) {
			float duration = Charm.durationFactor(enemy) * Random.IntRange(2, 5);

			if (enemy.buff(DriedRose.OneWayCursedLoveBuff.class) != null) {
				duration *= 2;
			}
			Char target = enemy;

			if (enemy.buff(DriedRose.OneWayLoveBuff.class) != null) {
				target = this;
			}

			Buff.affect(target, Charm.class, duration);
			enemy.getSprite().centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);

			Sample.INSTANCE.play(Assets.SND_CHARMS);
		}

		return damage;
	}

	@Override
	protected boolean getCloser(int target) {
		if (Dungeon.level.fieldOfView[target] && Dungeon.level.distance(getPos(), target) > 2 && delay <= 0) {

			blink(target);
			spend(-1 / speed());
			return true;

		} else {

			delay--;
			return super.getCloser(target);

		}
	}

	private void blink(int target) {

		int cell = Ballistica.cast(getPos(), target, true, true);

		if (Actor.findChar(cell) != null && Ballistica.distance > 1) {
			cell = Ballistica.trace[Ballistica.distance - 2];
		}

		WandOfBlink.appear(this, cell);

		delay = BLINK_DELAY;
	}

	@Override
	public int attackSkill(Char target) {
		return 40;
	}

	@Override
	public int dr() {
		return 10;
	}
}
