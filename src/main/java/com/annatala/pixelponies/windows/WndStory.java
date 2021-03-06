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
import com.annatala.noosa.Game;
import com.annatala.noosa.Text;
import com.annatala.noosa.ui.Component;
import com.annatala.pixelponies.Chrome;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.scenes.PixelScene;
import com.annatala.pixelponies.ui.ScrollPane;
import com.annatala.pixelponies.ui.Window;
import com.annatala.utils.SparseArray;

public class WndStory extends Window {

	private static final int WIDTH  = 120;
	private static final int HEIGHT = 120;
	
	private static final int MARGIN = 6;
	
	private static final float bgR	= 0.77f;
	private static final float bgG	= 0.73f;
	private static final float bgB	= 0.62f;

	public static final int ID_START 		= -1;
	public static final int ID_SEWERS		= 0;
	public static final int ID_PRISON		= 1;
	public static final int ID_CAVES		= 2;
	public static final int ID_METROPOLIS	= 3;
	public static final int ID_HALLS		= 4;
	public static final int ID_SPIDERS		= 5;
	public static final int ID_GUTS         = 6;
	
	private static final SparseArray<String> CHAPTERS = new SparseArray<>();
	
	static {
		CHAPTERS.put(ID_START,		Game.getVar(R.string.WndStory_Start));
		CHAPTERS.put(ID_SEWERS,     Game.getVar(R.string.WndStory_Sewers));
		CHAPTERS.put(ID_PRISON,     Game.getVar(R.string.WndStory_Prision));
		CHAPTERS.put(ID_CAVES,      Game.getVar(R.string.WndStory_Caves));
		CHAPTERS.put(ID_METROPOLIS, Game.getVar(R.string.WndStory_Metropolis));
		CHAPTERS.put(ID_HALLS,      Game.getVar(R.string.WndStory_Halls));
		CHAPTERS.put(ID_SPIDERS,    Game.getVar(R.string.WndStory_Spiders));
		CHAPTERS.put(ID_GUTS,       Game.getVar(R.string.WndStory_Guts));
	}

	public WndStory( String text ) {
		super( 0, 0, Chrome.get( Chrome.Type.SCROLL ) );

		Text tf = PixelScene.createMultiline(text, GuiProperties.regularFontSize());
		tf.maxWidth(WIDTH - MARGIN * 2);
		tf.measure();
		tf.ra = bgR;
		tf.ga = bgG;
		tf.ba = bgB;
		tf.rm = -bgR;
		tf.gm = -bgG;
		tf.bm = -bgB;
		tf.x = MARGIN;
		
		int h = (int) Math.min(HEIGHT - MARGIN, tf.height());
		int w = (int)(tf.width() + MARGIN * 2);
		
		resize( w, h );
		
		Component content = new Component();
		
		content.add(tf);
		
		content.setSize(tf.width(), tf.height());
		
		ScrollPane list = new ScrollPane(content);
		add(list);

		list.setRect(0, 0, w, h);
	}
	
	@Override
	public void update() {
		super.update();
		
	}
	
	public static void showCustomStory( String text ) {
		if (text != null) {
			WndStory wnd = new WndStory( text );
			
			Game.scene().add( wnd );
		}
	}
	
	public static void showChapter( int id ) {
		
		if (Dungeon.chapters.contains( id )) {
			return;
		}
		
		String text = CHAPTERS.get( id );
		if (text != null) {
			WndStory wnd = new WndStory( text );
			
			Game.scene().add( wnd );
			
			Dungeon.chapters.add( id );
		}
	}
}
