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
package com.annatala.pixelponies.windows;

import com.annatala.pixelponies.android.util.GuiProperties;
import com.annatala.noosa.Image;
import com.annatala.noosa.Text;
import com.annatala.noosa.ui.Component;
import com.annatala.pixelponies.scenes.PixelScene;
import com.annatala.pixelponies.ui.Window;

public class WndTitledMessage extends Window {

	private static final int WIDTH	= 120;
	private static final int GAP	= 2;

	public WndTitledMessage( Image icon, String title, String message ) {
		
		this( new IconTitle( icon, title ), message );
	}
	
	public WndTitledMessage( Component titlebar, String message ) {
		
		super();
		
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		Highlighter hl = new Highlighter( message );

		Text normal = PixelScene.createMultiline(hl.text, GuiProperties.regularFontSize());
		if (hl.isHighlighted()) {
			normal.mask = hl.inverted();
		}
		
		normal.maxWidth(WIDTH);
		normal.measure();
		normal.x = titlebar.left();
		normal.y = titlebar.bottom() + GAP;
		add(normal);

		if (hl.isHighlighted()) {

			Text highlighted = PixelScene.createMultiline(hl.text, GuiProperties.regularFontSize());
			highlighted.mask = hl.mask;
			highlighted.maxWidth(normal.getMaxWidth());
			highlighted.measure();
			highlighted.x = normal.x;
			highlighted.y = normal.y;
			add(highlighted);
			
			highlighted.hardlight(TITLE_COLOR);
		}
		
		resize( WIDTH, (int)(normal.y + normal.height()) );
	}
}
