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
package com.annatala.pixelponies.levels.painters;

import com.annatala.pixelponies.items.utility.Codex;
import com.annatala.pixelponies.items.Generator;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.keys.IronKey;
import com.annatala.pixelponies.items.scrolls.BlankScroll;
import com.annatala.pixelponies.items.scrolls.Scroll;
import com.annatala.pixelponies.items.utility.CommonCodex;
import com.annatala.pixelponies.levels.Level;
import com.annatala.pixelponies.levels.Room;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.utils.Point;
import com.annatala.utils.Random;

public class LibraryPainter extends Painter {

	public static void paint(Level level, Room room) {

		fill(level, room, Terrain.WALL);
		fill(level, room, 1, Terrain.EMPTY);

		Room.Door entrance = room.entrance();
		Point a = null;
		Point b = null;

		if (entrance.x == room.left) {
			a = new Point(room.left + 1, entrance.y - 1);
			b = new Point(room.left + 1, entrance.y + 1);
			fill(level, room.right - 1, room.top + 1, 1, room.height() - 1,
					Terrain.BOOKSHELF);
		} else if (entrance.x == room.right) {
			a = new Point(room.right - 1, entrance.y - 1);
			b = new Point(room.right - 1, entrance.y + 1);
			fill(level, room.left + 1, room.top + 1, 1, room.height() - 1,
					Terrain.BOOKSHELF);
		} else if (entrance.y == room.top) {
			a = new Point(entrance.x + 1, room.top + 1);
			b = new Point(entrance.x - 1, room.top + 1);
			fill(level, room.left + 1, room.bottom - 1, room.width() - 1, 1,
					Terrain.BOOKSHELF);
		} else if (entrance.y == room.bottom) {
			a = new Point(entrance.x + 1, room.bottom - 1);
			b = new Point(entrance.x - 1, room.bottom - 1);
			fill(level, room.left + 1, room.top + 1, room.width() - 1, 1,
					Terrain.BOOKSHELF);
		}
		if (a != null && level.map[a.x + a.y * level.getWidth()] == Terrain.EMPTY) {
			set(level, a, Terrain.STATUE);
		}
		if (b != null && level.map[b.x + b.y * level.getWidth()] == Terrain.EMPTY) {
			set(level, b, Terrain.STATUE);
		}

		int n = Random.IntRange(2, 3);
		if (Random.luckBonus()) n++;

		for (int i = 0; i < n; i++) {
			int pos;
			do {
				pos = room.random(level);
			} while (level.map[pos] != Terrain.EMPTY
					|| level.getHeap(pos) != null);
			level.drop(prize(level), pos);

			int luckTest = 4;
			if (Random.luckBonus()) luckTest--;

			if (Random.Int(luckTest) == 0) {
				level.drop(Generator.random(Generator.Category.CODEX), pos);
			} else {
				if (Random.Int(luckTest) == 0) {
					level.drop(new BlankScroll(), pos);
				}
			}
		}
		if (Random.Int(3) == 0 && Random.luckBonus()) {
			level.drop(Generator.random(Generator.Category.CODEX), room.random(level));
		}

		entrance.set(Room.Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey());
	}

	private static Item prize(Level level) {

		Item prize = level.itemToSpanAsPrize();
		if (prize instanceof Scroll) {
			return prize;
		} else if (prize != null) {
			level.addItemToSpawn(prize);
		}

		return Generator.random(Generator.Category.SCROLL);
	}
}
