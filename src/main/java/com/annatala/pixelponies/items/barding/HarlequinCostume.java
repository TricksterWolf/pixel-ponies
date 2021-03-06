package com.annatala.pixelponies.items.barding;

import com.annatala.pixelponies.android.R;
import com.annatala.noosa.Game;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.SugarRush;
import com.annatala.pixelponies.actors.hero.Hero;
import com.annatala.pixelponies.actors.hero.HeroSubClass;
import com.annatala.utils.GLog;

public class HarlequinCostume extends ClassBarding {

	private static final String TXT_NOT_BARD = Game.getVar(R.string.HarlequinCostume_NotBard);
	private static final String AC_SPECIAL = Game.getVar(R.string.HarlequinCostume_ACSpecial);

	public HarlequinCostume() {
		image = 7;
		hasHelmet = false;
	}	
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public void doSpecial() {

        Buff.affect( getCurUser(), SugarRush.class, 10 );

	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.subClass == HeroSubClass.BARD) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_BARD );
			return false;
		}
	}
	
	@Override
	public String desc() {
		return Game.getVar(R.string.HarlequinCostume_Desc);
	}
}