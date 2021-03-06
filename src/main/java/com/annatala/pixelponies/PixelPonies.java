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
package com.annatala.pixelponies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.annatala.pixelponies.android.util.Flavours;
import com.annatala.pixelponies.android.util.ModdingMode;
import com.annatala.pixelponies.android.util.Util;
import com.annatala.pixelponies.android.EventCollector;
import com.annatala.pixelponies.android.google.Iap;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Music;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.items.ItemSpritesDescription;
import com.annatala.pixelponies.items.weapon.melee.Claymore;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.scenes.InterlevelScene;
import com.annatala.pixelponies.scenes.PixelScene;
import com.annatala.pixelponies.scenes.TitleScene;
import com.annatala.pixelponies.scenes.WelcomeScene;
import com.annatala.utils.GLog;
import com.annatala.utils.Utils;

import java.util.Locale;

import javax.microedition.khronos.opengles.GL10;

public class PixelPonies extends Game {

	public PixelPonies() {
		super(TitleScene.class);
		
		// Not 100% sure how necessary this thing is, nor how to remove it.
		com.annatala.utils.Bundle.addAlias(
				com.annatala.pixelponies.items.food.Ration.class,
				"com.annatala.pixelponies.items.food.Food");

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		EventCollector.init(this);

		// I don't think I ever want to make this an rT game.
		PixelPonies.realtime(false);
		
		ModdingMode.selectMod(PixelPonies.activeMod());
		PixelPonies.activeMod(ModdingMode.activeMod());

		Iap.initIap(this);
		
		if(!Utils.canUseClassicFont(uiLanguage())) {
			PixelPonies.classicFont(false);
		}
		
		ModdingMode.setClassicTextRenderingMode(PixelPonies.classicFont());

		EventCollector.logEvent("font", String.valueOf(PixelPonies.classicFont()));

		useLocale(uiLanguage());
		ItemSpritesDescription.readItemsDesc();

		updateImmersiveMode();

		DisplayMetrics metrics = new DisplayMetrics();
		instance().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		boolean landscape = metrics.widthPixels > metrics.heightPixels;

		if (Preferences.INSTANCE.getBoolean(Preferences.KEY_LANDSCAPE, false) != landscape) {
			landscape(!landscape);
		}

		Music.INSTANCE.enable(music());
		Sample.INSTANCE.enable(soundFx());
		
		if (PixelPonies.version() != Game.versionCode) {
			switchScene(WelcomeScene.class);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		super.onWindowFocusChanged(hasFocus);

		if (hasFocus) {
			updateImmersiveMode();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		GLog.i("onActivityResult(" + requestCode + "," + resultCode + "," + data);

		if(!Iap.onActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public static void switchNoFade(Class<? extends PixelScene> c) {
		PixelScene.noFade = true;
		switchScene(c);
	}

	// TODO: Figure out why Iap.isReady() returns false on my tablet.
	public static boolean canDonate() {
		return Flavours.haveDonations() && Iap.isReady();
	}
	
	/*
	 * ---> Preferences
	 */

	public static void landscape(boolean value) {
		Game.instance()
				.setRequestedOrientation(value ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
						: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Preferences.INSTANCE.put(Preferences.KEY_LANDSCAPE, value);
	}

	public static boolean landscape() {
		return width() > height();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);

		if (needSceneRestart && !(scene instanceof InterlevelScene)) {
			requestedReset = true;
			needSceneRestart = false;
		}
	}

	@SuppressLint("NewApi")
	public static void updateImmersiveMode() {
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			if (instance() != null) {
				instance().getWindow()
						.getDecorView()
						.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
										| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
										| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
										| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
										| View.SYSTEM_UI_FLAG_FULLSCREEN
										| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
			}
		}
	}

	public static void zoom(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_ZOOM, value);
	}

	public static int zoom() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_ZOOM, 0);
	}

	public static void music(boolean value) {
		Music.INSTANCE.enable(value);
		Preferences.INSTANCE.put(Preferences.KEY_MUSIC, value);
	}

	public static boolean music() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_MUSIC, true);
	}

	public static void soundFx(boolean value) {
		Sample.INSTANCE.enable(value);
		Preferences.INSTANCE.put(Preferences.KEY_SOUND_FX, value);
	}

	public static boolean soundFx() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_SOUND_FX, true);
	}

	public static void brightness(boolean value) {
		Preferences.INSTANCE.put(Preferences.KEY_BRIGHTNESS, value);
		if (scene() instanceof GameScene) {
			((GameScene) scene()).brightness(value);
		}
	}

	public static boolean brightness() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_BRIGHTNESS,
				false);
	}

	private static void donated(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_DONATED, value);
	}

	public static int donated() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_DONATED, 0);
	}

	public static void lastClass(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_LAST_CLASS, value);
	}

	public static int lastClass() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_LAST_CLASS, 0);
	}

	public static void challenges(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_CHALLENGES, value);
	}

	public static int challenges() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_CHALLENGES, 0);
	}

	public static void intro(boolean value) {
		Preferences.INSTANCE.put(Preferences.KEY_INTRO, value);
	}

	public static boolean intro() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_INTRO, true);
	}

	public static String uiLanguage() {
		String deviceLocale = Locale.getDefault().getLanguage();
		GLog.i("Device locale: %s", deviceLocale);
		return Preferences.INSTANCE.getString(Preferences.KEY_LOCALE,
				deviceLocale);
	}

	public static void uiLanguage(String lang) {
		Preferences.INSTANCE.put(Preferences.KEY_LOCALE, lang);

		instance().doRestart();
	}

	public static void secondQuickslot(boolean checked) {
		Preferences.INSTANCE.put(Preferences.KEY_SECOND_QUICKSLOT, checked);
		if (scene() instanceof GameScene) {
			((GameScene) scene()).updateToolbar();
		}
	}

	public static boolean secondQuickslot() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_SECOND_QUICKSLOT, false);
	}

	public static void thirdQuickslot(boolean checked) {
		Preferences.INSTANCE.put(Preferences.KEY_THIRD_QUICKSLOT, checked);
		if (scene() instanceof GameScene) {
			((GameScene) scene()).updateToolbar();
		}
	}
	
	public static boolean thirdQuickslot() {
		return Preferences.INSTANCE.getBoolean(Preferences.KEY_THIRD_QUICKSLOT, false);
	}
	
	public static void version( int value)  {
        Preferences.INSTANCE.put( Preferences.KEY_VERSION, value );
    }

    public static int version() {
        return Preferences.INSTANCE.getInt( Preferences.KEY_VERSION, 0 );
    }
	
	public static void fontScale(int value) {
		Preferences.INSTANCE.put(Preferences.KEY_FONT_SCALE, value);
	}

	public static int fontScale() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_FONT_SCALE, 0);
	}
	
	public static boolean classicFont() {
		boolean val = Preferences.INSTANCE.getBoolean(Preferences.KEY_CLASSIC_FONT, true);
		ModdingMode.setClassicTextRenderingMode(val);
		return val;
	}

	public static void classicFont(boolean value) {
		ModdingMode.setClassicTextRenderingMode(value);
		Preferences.INSTANCE.put(Preferences.KEY_CLASSIC_FONT, value);
	}

	public static void activeMod(String mod) {
		Preferences.INSTANCE.put(Preferences.KEY_ACTIVE_MOD, mod);
		ModdingMode.selectMod(PixelPonies.activeMod());
		Util.storeEventInAcra("RPD_active_mod", mod);
	}
	
	public static String activeMod() {
		return Preferences.INSTANCE.getString(Preferences.KEY_ACTIVE_MOD, ModdingMode.REMIXED);
	}

	private static Boolean realtimeCached = null;
	public static boolean realtime() {
		if(realtimeCached == null) {
			realtimeCached = Preferences.INSTANCE.getBoolean(Preferences.KEY_REALTIME, false);
		}
		return realtimeCached;
	}

	public static void realtime(boolean value) {
		realtimeCached = value;
		Preferences.INSTANCE.put(Preferences.KEY_REALTIME, value);
	}
	
	/*
	 * <--- Preferences
	 */

	/*
	 * <---Purchases
	 */
	static public void setDonationLevel(int level) {
		
		if (level < donated()) {
			return;
		}
		
		if (donated() == 0 && level != 0) {
			executeInGlThread(new Runnable() {
				
				@Override
				public void run() {
					Sample.INSTANCE.play(Assets.SND_GOLD);
					Badges.validateSupporter();
				}
			});
		}
		donated(level);
	}

	public static void setDifficulty(int _difficulty) {
		difficulty = _difficulty;
	}
}