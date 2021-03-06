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
package com.annatala.pixelponies.scenes;

import com.annatala.pixelponies.android.util.GuiProperties;
import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.noosa.Image;
import com.annatala.noosa.Text;
import com.annatala.noosa.audio.Music;
import com.annatala.noosa.audio.Sample;
import com.annatala.noosa.ui.Button;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.effects.BannerSprites;
import com.annatala.pixelponies.effects.Fireball;
import com.annatala.pixelponies.ui.Archs;
import com.annatala.pixelponies.ui.DonateButton;
import com.annatala.pixelponies.ui.ExitButton;
import com.annatala.pixelponies.ui.PrefsButton;
import com.annatala.pixelponies.ui.PremiumPrefsButton;

public class TitleScene extends PixelScene {

	private static final String TXT_PLAY = Game.getVar(R.string.TitleScene_Play);
	private static final String TXT_HIGHSCORES = Game.getVar(R.string.TitleScene_Highscores);
	private static final String TXT_BADGES = Game.getVar(R.string.TitleScene_Badges);
	private static final String TXT_NEWS = Game.getVar(R.string.TitleScene_News);
	private static final String TXT_ABOUT = Game.getVar(R.string.TitleScene_About);

	Text            pleaseSupport;
	DonateButton    btnDonate;
	
	@Override
	public void create() {
		super.create();

		Music.INSTANCE.play(Assets.THEME, true);
		Music.INSTANCE.volume(1f);

		uiCamera.setVisible(false);

		int w = Camera.main.width;
		int h = Camera.main.height;

		float height = 180;

		Image title = BannerSprites.get(BannerSprites.Type.PIXEL_DUNGEON);
		add(title);

		title.x = (w - title.width()) / 2;
		title.y = (title.height() * 0.3f) / 2;

		placeTorch(title.x + 12, title.y + 20);
		placeTorch(title.x + title.width - 12, title.y + 20);

		DashboardItem btnNews = new DashboardItem(TXT_NEWS, 4) {
			@Override
			protected void onClick() {
				PixelPonies.switchNoFade(WelcomeScene.class);
			}
		};
		btnNews.setPos(w / 2 - btnNews.width(), (h + height) / 2 - DashboardItem.SIZE);
		add(btnNews);

		DashboardItem btnAbout = new DashboardItem(TXT_ABOUT, 1) {
			@Override
			protected void onClick() {
				PixelPonies.switchNoFade(AboutScene.class);
			}
		};
		btnAbout.setPos(w / 2, (h + height) / 2 - DashboardItem.SIZE);
		add(btnAbout);

		DashboardItem btnHighscores = new DashboardItem(TXT_HIGHSCORES, 2) {
			@Override
			protected void onClick() {
				PixelPonies.switchNoFade(RankingsScene.class);
			}
		};
		btnHighscores.setPos(w / 2 - (btnHighscores.width()/2), btnAbout.top() - DashboardItem.SIZE);
		add(btnHighscores);

		DashboardItem btnPlay = new DashboardItem(TXT_PLAY, 0) {
			@Override
			protected void onClick() {
				PixelPonies.switchNoFade(StartScene.class);
			}
		};
		btnPlay.setPos(w / 2 - btnPlay.width() - (btnHighscores.width()/2), btnHighscores.top());
		add(btnPlay);

		DashboardItem btnBadges = new DashboardItem(TXT_BADGES, 3) {
			@Override
			protected void onClick() {
				PixelPonies.switchNoFade(BadgesScene.class);
			}
		};
		btnBadges.setPos(w / 2 + (btnHighscores.width()/2), (h + height) / 2 - DashboardItem.SIZE);
		add(btnBadges);

		btnDonate = new DonateButton();

		pleaseSupport = PixelScene.createText(GuiProperties.titleFontSize());
		pleaseSupport.text(btnDonate.getText());
		pleaseSupport.measure();
		pleaseSupport.setPos((w - pleaseSupport.width()) / 2,
				h - pleaseSupport.height() * 2);

		btnDonate.setPos((w - btnDonate.width()) / 2, pleaseSupport.y
				- btnDonate.height());

		float dashBaseline = btnDonate.top() - DashboardItem.SIZE;

		if (PixelPonies.landscape()) {
			btnHighscores.setPos(w / 2 - btnHighscores.width() - (btnBadges.width()/2), dashBaseline);
			btnBadges.setPos(btnHighscores.right(), dashBaseline);
			btnPlay.setPos(btnHighscores.left() - btnPlay.width(), dashBaseline);
			btnNews.setPos(btnBadges.right(), dashBaseline);
			btnAbout.setPos(btnNews.right(), dashBaseline);
		} else {
			btnNews.setPos(w / 2 - btnNews.width(), dashBaseline);
			btnAbout.setPos(btnNews.right(), dashBaseline);
			btnPlay.setPos(w / 2 - btnPlay.width() -  (btnNews.width()/2), btnAbout.top() - DashboardItem.SIZE);
			btnHighscores.setPos(btnPlay.right(), btnPlay.top());
			btnBadges.setPos(btnHighscores.right(), btnPlay.top());
		}

		Archs archs = new Archs();
		archs.setSize(w, h);
		addToBack(archs);

		Text version = Text.createBasicText("v " + Game.version, font1x);
		version.measure();
		version.hardlight(0x888888);
		version.setPos(w - version.width(), h - version.height());
		add(version);

		float freeInternalStorage = Game.getAvailableInternalMemorySize();

		if (freeInternalStorage < 2) {
			Text lowInteralStorageWarning = PixelScene
					.createMultiline(GuiProperties.regularFontSize());
			lowInteralStorageWarning.text(Game
					.getVar(R.string.TitleScene_InternalStorageLow));
			lowInteralStorageWarning.measure();
			lowInteralStorageWarning.setPos(0,
					h - lowInteralStorageWarning.height());
			lowInteralStorageWarning.hardlight(0.95f, 0.1f, 0.1f);
			add(lowInteralStorageWarning);
		}

		PrefsButton btnPrefs = new PrefsButton();
		btnPrefs.setPos(0, 0);
		add(btnPrefs);

		if (PixelPonies.donated() > 0) {
			PremiumPrefsButton btnPPrefs = new PremiumPrefsButton();
			btnPPrefs.setPos(btnPrefs.right() + 2, 0);
			add(btnPPrefs);
		}

		ExitButton btnExit = new ExitButton();
		btnExit.setPos(w - btnExit.width(), 0);
		add(btnExit);

		fadeIn();
	}

	private double time = 0;
	private boolean donationAdded = false;
	@Override
	public void update() {
		super.update();
		time += Game.elapsed;
		
		if(!donationAdded) {
			if (PixelPonies.canDonate()) {
				add(pleaseSupport);
				add(btnDonate);
			}
			donationAdded = true;
		} else {
			float cl = (float) Math.sin(time) * 0.5f + 0.5f;
			pleaseSupport.hardlight(cl, cl, cl);
		}

	}

	private void placeTorch(float x, float y) {
		Fireball fb = new Fireball();
		fb.setPos(x, y);
		add(fb);
	}

	private static class DashboardItem extends Button {

		public static final float SIZE = 48;

		private static final int IMAGE_SIZE = 32;

		private Image image;
		private Text label;

		public DashboardItem(String text, int index) {
			super();

			image.frame(image.texture.uvRect(index * IMAGE_SIZE, 0, (index + 1)
					* IMAGE_SIZE, IMAGE_SIZE));
			this.label.text(text);
			this.label.measure();

			setSize(SIZE, SIZE);
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			image = new Image(Assets.DASHBOARD);
			add(image);

			label = createText(GuiProperties.titleFontSize());
			add(label);
		}

		@Override
		protected void layout() {
			super.layout();

			image.x = align(x + (width - image.width()) / 2);
			image.y = align(y);

			label.x = align(x + (width - label.width()) / 2);
			label.y = align(image.y + image.height() + 2);
		}

		@Override
		protected void onTouchDown() {
			image.brightness(1.5f);
			Sample.INSTANCE.play(Assets.SND_CLICK, 1, 1, 0.8f);
		}

		@Override
		protected void onTouchUp() {
			image.resetColor();
		}
	}
}
