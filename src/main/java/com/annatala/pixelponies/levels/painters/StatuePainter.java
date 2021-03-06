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

import com.annatala.pixelponies.actors.mobs.common.ArmoredStatue;
import com.annatala.pixelponies.actors.mobs.common.GoldenStatue;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.actors.mobs.WeaponStatue;
import com.annatala.pixelponies.items.keys.IronKey;
import com.annatala.pixelponies.levels.Level;
import com.annatala.pixelponies.levels.Room;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.utils.Point;
import com.annatala.utils.Random;

public class StatuePainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );

		Point c = room.center();
		int cx = c.x;
		int cy = c.y;
		
		Room.Door door = room.entrance();
		
		door.set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );

		boolean flipSprite = false;
		
		if (door.x == room.left) {
			
			fill( level, room.right - 1, room.top + 1, 1, room.height() - 1 , Terrain.STATUE );
			cx = room.right - 2;
			flipSprite = true;
			
		} else if (door.x == room.right) {
			
			fill( level, room.left + 1, room.top + 1, 1, room.height() - 1 , Terrain.STATUE );
			cx = room.left + 2;
			flipSprite = false;
			
		} else if (door.y == room.top) {
			
			fill( level, room.left + 1, room.bottom - 1, room.width() - 1, 1 , Terrain.STATUE );
			cy = room.bottom - 2;
			flipSprite = (door.x <= cx);

		} else if (door.y == room.bottom) {
			
			fill( level, room.left + 1, room.top + 1, room.width() - 1, 1 , Terrain.STATUE );
			cy = room.top + 2;
			flipSprite = (door.x < cx);
		}

		Mob statue;
		//WeaponStatue type proc
		if (Random.Int(10) > 5){
			if (Random.Int(10) == 1){
				statue = new GoldenStatue(flipSprite);
			}
			else{
				statue = new WeaponStatue(flipSprite);
			}
		}
		else{
			statue = new ArmoredStatue(flipSprite);
		}

		statue.setPos(cx + cy * level.getWidth());
		level.mobs.add( statue );
		Actor.occupyCell( statue );
	}
}
