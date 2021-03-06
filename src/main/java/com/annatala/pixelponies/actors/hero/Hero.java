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
package com.annatala.pixelponies.actors.hero;

import android.support.annotation.NonNull;

import com.annatala.pixelponies.android.util.Scrambler;
import com.annatala.pixelponies.items.rings.RatKingCrown;
import com.annatala.pixelponies.items.barding.SpiderBarding;
import com.annatala.pixelponies.items.quest.HeartOfDarkness;
import com.annatala.pixelponies.android.EventCollector;
import com.annatala.pixelponies.android.R;
import com.annatala.pixelponies.actors.mobs.guts.SpiritOfPain;
import com.annatala.noosa.Camera;
import com.annatala.noosa.Game;
import com.annatala.noosa.audio.Sample;
import com.annatala.pixelponies.Assets;
import com.annatala.pixelponies.Badges;
import com.annatala.pixelponies.Bones;
import com.annatala.pixelponies.Dungeon;
import com.annatala.pixelponies.GamesInProgress;
import com.annatala.pixelponies.PixelPonies;
import com.annatala.pixelponies.Rankings;
import com.annatala.pixelponies.ResultDescriptions;
import com.annatala.pixelponies.actors.Actor;
import com.annatala.pixelponies.actors.Char;
import com.annatala.pixelponies.actors.Gender;
import com.annatala.pixelponies.actors.blobs.Blob;
import com.annatala.pixelponies.actors.blobs.Web;
import com.annatala.pixelponies.actors.buffs.Barkskin;
import com.annatala.pixelponies.actors.buffs.Bleeding;
import com.annatala.pixelponies.actors.buffs.Blindness;
import com.annatala.pixelponies.actors.buffs.Buff;
import com.annatala.pixelponies.actors.buffs.Burning;
import com.annatala.pixelponies.actors.buffs.Charm;
import com.annatala.pixelponies.actors.buffs.Combo;
import com.annatala.pixelponies.actors.buffs.Cripple;
import com.annatala.pixelponies.actors.buffs.Fury;
import com.annatala.pixelponies.actors.buffs.GasesImmunity;
import com.annatala.pixelponies.actors.buffs.Hunger;
import com.annatala.pixelponies.actors.buffs.Invisibility;
import com.annatala.pixelponies.actors.buffs.Ooze;
import com.annatala.pixelponies.actors.buffs.Paralysis;
import com.annatala.pixelponies.actors.buffs.Poison;
import com.annatala.pixelponies.actors.buffs.Regeneration;
import com.annatala.pixelponies.actors.buffs.Roots;
import com.annatala.pixelponies.actors.buffs.SnipersMark;
import com.annatala.pixelponies.actors.buffs.Vertigo;
import com.annatala.pixelponies.actors.buffs.Weakness;
import com.annatala.pixelponies.actors.hero.HeroAction.Attack;
import com.annatala.pixelponies.actors.mobs.Fraction;
import com.annatala.pixelponies.actors.mobs.Mob;
import com.annatala.pixelponies.actors.mobs.Rat;
import com.annatala.pixelponies.actors.mobs.npcs.NPC;
import com.annatala.pixelponies.effects.CheckedCell;
import com.annatala.pixelponies.effects.Flare;
import com.annatala.pixelponies.effects.Pushing;
import com.annatala.pixelponies.effects.Speck;
import com.annatala.pixelponies.items.utility.Amulet;
import com.annatala.pixelponies.items.utility.Ankh;
import com.annatala.pixelponies.items.utility.DewVial;
import com.annatala.pixelponies.items.utility.Spellbook;
import com.annatala.pixelponies.items.weapon.missiles.CommonArrow;
import com.annatala.pixelponies.plants.Dewdrop;
import com.annatala.pixelponies.items.Heap;
import com.annatala.pixelponies.items.Heap.Type;
import com.annatala.pixelponies.items.Item;
import com.annatala.pixelponies.items.weapon.KindOfWeapon;
import com.annatala.pixelponies.items.barding.Barding;
import com.annatala.pixelponies.items.keys.GoldenKey;
import com.annatala.pixelponies.items.keys.IronKey;
import com.annatala.pixelponies.items.keys.Key;
import com.annatala.pixelponies.items.keys.SkeletonKey;
import com.annatala.pixelponies.items.potions.PotionOfHonesty;
import com.annatala.pixelponies.items.quest.CorpseDust;
import com.annatala.pixelponies.items.rings.RingOfAccuracy;
import com.annatala.pixelponies.items.rings.RingOfDetection;
import com.annatala.pixelponies.items.rings.RingOfElements;
import com.annatala.pixelponies.items.rings.RingOfEvasion;
import com.annatala.pixelponies.items.rings.RingOfHaste;
import com.annatala.pixelponies.items.rings.RingOfShadows;
import com.annatala.pixelponies.items.rings.RingOfStoneWalking;
import com.annatala.pixelponies.items.rings.RingOfThorns;
import com.annatala.pixelponies.items.scrolls.ScrollOfLoyalOath;
import com.annatala.pixelponies.items.scrolls.ScrollOfMagicMapping;
import com.annatala.pixelponies.items.scrolls.ScrollOfUpgrade;
import com.annatala.pixelponies.items.weapon.melee.Bow;
import com.annatala.pixelponies.items.weapon.melee.MeleeWeapon;
import com.annatala.pixelponies.items.weapon.melee.SpecialWeapon;
import com.annatala.pixelponies.items.weapon.missiles.Arrow;
import com.annatala.pixelponies.items.weapon.missiles.MissileWeapon;
import com.annatala.pixelponies.levels.Terrain;
import com.annatala.pixelponies.levels.TerrainFlags;
import com.annatala.pixelponies.levels.features.AlchemyPot;
import com.annatala.pixelponies.levels.features.Chasm;
import com.annatala.pixelponies.mechanics.Ballistica;
import com.annatala.pixelponies.scenes.GameScene;
import com.annatala.pixelponies.scenes.InterlevelScene;
import com.annatala.pixelponies.scenes.SurfaceScene;
import com.annatala.pixelponies.sprites.CharSprite;
import com.annatala.pixelponies.sprites.HeroSpriteDef;
import com.annatala.pixelponies.ui.AttackIndicator;
import com.annatala.pixelponies.ui.BuffIndicator;
import com.annatala.utils.GLog;
import com.annatala.pixelponies.windows.WndMessage;
import com.annatala.pixelponies.windows.WndResurrect;
import com.annatala.pixelponies.windows.WndTradeItem;
import com.annatala.utils.Bundle;
import com.annatala.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class Hero extends Char {

	private static final String TXT_LEAVE = Game.getVar(R.string.Hero_Leave);
	private static final String TXT_COME_TOO_FAR = Game.getVar(R.string.Hero_ComeTooFar);

	private static final String TXT_LEVEL_UP = Game.getVar(R.string.Hero_LevelUp);
	private static final String TXT_NEW_LEVEL = Game.getVar(R.string.Hero_NewLevel);

	public static final String TXT_YOU_NOW_HAVE = Game.getVar(R.string.Hero_YouNowHave);

	private static final String TXT_SOMETHING_ELSE = Game.getVar(R.string.Hero_SomethingElse);
	private static final String TXT_LOCKED_CHEST = Game.getVar(R.string.Hero_LockedChest);
	private static final String TXT_LOCKED_DOOR = Game.getVar(R.string.Hero_LockedDoor);
	private static final String TXT_NOTICED_SMTH = Game.getVar(R.string.Hero_NoticedSmth);

	private static final String TXT_WAIT = Game.getVar(R.string.Hero_Wait);
	private static final String TXT_SEARCH = Game.getVar(R.string.Hero_Search);

    public static final int STARTING_HONESTY = 3;
	public static final int STARTING_LOYALTY = 3;
	public static final int STARTING_LAUGHTER = 3;
	public static final int STARTING_GENEROSITY = 3;
	public static final int STARTING_KINDNESS = 3;
	public static final int STARTING_MAGIC = 3;

	private static final float TIME_TO_REST = 1f;
	private static final float TIME_TO_SEARCH = 2f;

	// This shit literally replaces acres of magic number calculations, and it scales better.
	// Chance = [level]. Search chance = [2 * level + 1]. Rogue bonus = [(int) (1.5 * prev + 0.5)].
	// But we're going to modify it to work with generosity now, and calculate on the fly!
	private static final float[] detectChances = new float[50];
	static {
		for (int i = 0; i < detectChances.length ; i++) {
			detectChances[i] = 1.0F - 1.0F / (0.1F * ((float) i) + 1.0F );
		}
	}

	public HeroClass heroClass = HeroClass.PEGASUS;
	public HeroSubClass subClass = HeroSubClass.NONE;

	int attackSkill = 10;
	int defenseSkill = 5;

	private boolean ready = false;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;

	private Char enemy;

	public Barding.Glyph killerGlyph = null;

	private Item theKey;

	public boolean restoreHealth = false;

	public MissileWeapon rangedWeapon = null;
	public Class<? extends Arrow> lastArrowType = CommonArrow.class;
	public Belongings belongings;

    private int honesty;
	private int loyalty;
	private int laughter;	// Luck and saves (need to add support) (joke books and shopkeeper pranks)
	private int generosity;	// Awareness, but not detect radius (store donations, buy-outs, quest option to forfeit)
	private int kindness;	// Stealth (quests, bosses, level skips?, minus if kill fleeing)
	private int magic;		// Wand power (rare books)

	public boolean hasHadLaughterIncreasedByPie = false;
	public boolean hasHadLaughterIncreasedBySeltzerBottle = false;
	public boolean hasHadLaughterIncreasedByFunnyGlasses = false;
	public boolean hasHadLaughterIncreasedByRubberChicken = false;

	public boolean weakened = false;
    public boolean sugarRush = false;

	private int lvl = Scrambler.scramble(1);
	private int exp = Scrambler.scramble(0);

	public String levelKind;
	public String levelId;

	private ArrayList<Mob> visibleEnemies;
	private Collection<Mob> pets = new ArrayList<>();

	public void addPet(@NonNull Mob pet) {
		pets.add(pet);
	}

	private int difficulty;

	public Hero() {
		name = Game.getVar(R.string.Hero_Name);
		name_objective = Game.getVar(R.string.Hero_Name_Objective);

        setHonesty(STARTING_HONESTY);
        setLoyalty(STARTING_LOYALTY);
		setLaughter(STARTING_LAUGHTER);
		setGenerosity(STARTING_GENEROSITY);
		setKindness(STARTING_KINDNESS);
		setMagic(STARTING_MAGIC);

		belongings = new Belongings(this);

		visibleEnemies = new ArrayList<>();
	}

	public Hero(int difficulty) {
		this();
		setDifficulty(difficulty);

		if (getDifficulty() != 0) {
			hp(ht(20));
		} else {
			hp(ht(30));
		}
		live();
	}

	@Override
	protected void readCharData() {
	}

    public int effectiveHonesty() {
        int effHonesty = Scrambler.descramble(honesty);
        return weakened ? effHonesty - 2 : effHonesty;
    }

	public int effectiveLoyalty() {
		return Scrambler.descramble(loyalty);
	}

    public int effectiveLaughter() {
        int effLaughter = Scrambler.descramble(laughter);
        return sugarRush ? effLaughter + 3 : effLaughter;
	}

	public int effectiveGenerosity() {
		return Scrambler.descramble(generosity);
	}
	public int effectiveKindness() {
		return Scrambler.descramble(kindness);
	}
	public int effectiveMagic() {
		return Scrambler.descramble(magic);
	}

    public void setHonesty(int newHonesty) { honesty = Scrambler.scramble(newHonesty); }
    public void setLoyalty(int newLoyalty) { loyalty = Scrambler.scramble(newLoyalty); }
	public void setLaughter(int newLaughter) { laughter = Scrambler.scramble(newLaughter); }
	public void setGenerosity(int newGenerosity) { generosity = Scrambler.scramble(newGenerosity); }
	public void setKindness(int newKindness) { kindness = Scrambler.scramble(newKindness); }
	public void setMagic(int newMagic) { magic = Scrambler.scramble(newMagic); }

    public int honesty() { return Scrambler.descramble(honesty); }
    public int loyalty() { return Scrambler.descramble(loyalty); }
    public int laughter() { return Scrambler.descramble(laughter); }
    public int generosity() { return Scrambler.descramble(generosity); }
    public int kindness() { return Scrambler.descramble(kindness); }
    public int magic() { return Scrambler.descramble(magic); }

	private static final String ATTACK = "attackSkill";
	private static final String DEFENSE = "defenseSkill";
    private static final String HONESTY = "honesty";
    private static final String LOYALTY = "loyalty";
    private static final String LAUGHTER = "laughter";
    private static final String GENEROSITY = "generosity";
    private static final String KINDNESS = "kindness";
    private static final String MAGIC = "magic";
	private static final String LEVEL = "lvl";
	private static final String EXPERIENCE = "exp";
	private static final String LEVEL_KIND = "levelKind";
	private static final String LEVEL_ID = "levelId";
	private static final String DIFFICULTY = "difficulty";
	private static final String PETS = "pets";

	private void refreshPets() {
		ArrayList<Mob> alivePets = new ArrayList<>();
		if (pets != null) {
			for (Mob pet : pets) {
				if (pet.isAlive() && pet.fraction() == Fraction.HEROES) {
					alivePets.add(pet);
				}
			}
		}
		pets = alivePets;
	}

	public void releasePets() {
		pets = new ArrayList<>();
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		heroClass.storeInBundle(bundle);
		subClass.storeInBundle(bundle);

		bundle.put(ATTACK, attackSkill);
		bundle.put(DEFENSE, defenseSkill);

        bundle.put(HONESTY, honesty());
        bundle.put(LOYALTY, loyalty());
        bundle.put(LAUGHTER, laughter());
        bundle.put(GENEROSITY, generosity());
        bundle.put(KINDNESS, kindness());
        bundle.put(MAGIC, magic());
		bundle.put(LEVEL, lvl());
		bundle.put(EXPERIENCE, getExp());
		bundle.put(LEVEL_KIND, levelKind);
		bundle.put(LEVEL_ID, levelId);
		bundle.put(DIFFICULTY, getDifficulty());

		refreshPets();

		bundle.put(PETS, pets);

		belongings.storeInBundle(bundle);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		heroClass = HeroClass.restoreFromBundle(bundle);
		subClass = HeroSubClass.restoreFromBundle(bundle);

		attackSkill = bundle.getInt(ATTACK);
		defenseSkill = bundle.getInt(DEFENSE);

        setHonesty(bundle.getInt(HONESTY));
        setLoyalty(bundle.getInt(LOYALTY));
        setLaughter(bundle.getInt(LAUGHTER));
        setGenerosity(bundle.getInt(GENEROSITY));
        setKindness(bundle.getInt(KINDNESS));
        setMagic(bundle.getInt(MAGIC));

		lvl(bundle.getInt(LEVEL));
		setExp(bundle.getInt(EXPERIENCE));
		levelKind = bundle.getString(LEVEL_KIND);
		levelId = bundle.optString(LEVEL_ID, "unknown");
		setDifficulty(bundle.optInt(DIFFICULTY, 2));

		Collection<Mob> _pets = bundle.getCollection(PETS, Mob.class);

		if (_pets != null) {
			for (Mob pet : _pets) {
				pets.add(pet);
			}
		}

		belongings.restoreFromBundle(bundle);

		gender = heroClass.gender();
	}

	public static void preview(GamesInProgress.Info info, Bundle bundle) {
		info.level = bundle.getInt(LEVEL);
	}

	public String className() {
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass.toString() : subClass.toString();
	}

	private void live() {
		if(buff(Regeneration.class)==null) {
			Buff.affect(this, Regeneration.class);
		}
		if(buff(Hunger.class)==null) {
			Buff.affect(this, Hunger.class);
		}
	}

	public int tier() {
		return belongings.barding == null ? 0 : belongings.barding.tier;
	}

	public boolean bowEquipped() {
		return belongings.weapon instanceof Bow;
	}

	public boolean shoot(Char enemy, MissileWeapon wep) {

		rangedWeapon = wep;
		boolean result = attack(enemy);
		rangedWeapon = null;

		return result;
	}

	@Override
	public int attackSkill(Char target) {

		int bonus = 0;

		// Mix in loyalty, which is now 1/2 of attack skill's progression.
		float loyaltyPartOfAttack = (effectiveLoyalty() - 3) * 2.5f + 1.0f;
		float attack = (loyaltyPartOfAttack + (float) attackSkill) / 2.0f;

		for (Buff buff : buffs(RingOfAccuracy.Accuracy.class)) {
			bonus += ((RingOfAccuracy.Accuracy) buff).level;
		}
		float accuracy = (bonus == 0) ? 1.0f : (float) Math.pow(1.4, bonus);

		if (rangedWeapon != null && Dungeon.level.distance(getPos(), target.getPos()) == 1) {

			// Zeebees don't suffer as much from making point-blank thrown attacks.
			if (heroClass == HeroClass.ZEBRA) {
				accuracy *= 0.75f;
			} else {
				accuracy *= 0.5f;
			}
		}

		if (getDifficulty() == 0) {
			accuracy *= 1.2;
		}

		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
		if (wep != null) {
			return (int) (attack * accuracy * wep.accuracyFactor(this));
		} else {
			return (int) (attack * accuracy);
		}
	}

	@Override
	public int defenseSkill(Char enemy) {

		int bonus = 0;

		// Mix in kindness, which is now 2/3rds of defense skill's progression.
		float kindnessPartOfDefense = (effectiveKindness() - 3) * 2.5f + 1.0f;
		float defense = (kindnessPartOfDefense * 2.0f + (float) defenseSkill) / 3.0f;

		for (Buff buff : buffs(RingOfEvasion.Evasion.class)) {
			bonus += ((RingOfEvasion.Evasion) buff).level;
		}
		float evasion = bonus == 0 ? 1.0f : (float) Math.pow(1.2, bonus);

		if (paralysed) {
			evasion /= 2.0f;
		}

		// Easy mode is easy.
		if (getDifficulty() == 0) {
			evasion *= 1.2;
		}

		defense *= evasion;

		int aEnc = 0 - effectiveHonesty();
		if (belongings.barding != null) {
			aEnc += belongings.barding.minHonesty;
		}

		// Encumbrance hits you pretty hard, but at least you can absorb the blows now maybe?
		if (aEnc > 0) {
			defense /= Math.pow(1.5, aEnc);

		} else {

			// Nightwings get a special bonus for having excess honesty above armor level.
			if (heroClass == HeroClass.NIGHTWING) {
				defense -= (float) aEnc * evasion;
			}
		}

		return (int) defense;
	}

	@Override
	public int dr() {
		int dr = belongings.barding != null ? Math.max(belongings.barding.resistance, 0) : 0;
		Barkskin barkskin = buff(Barkskin.class);
		if (barkskin != null) {
			dr += barkskin.level();
		}
		return dr;
	}

	private boolean inFury() {
		return (buff(Fury.class) != null) || (buff(CorpseDust.UndeadRageAuraBuff.class) != null);
	}

	@Override
	public int damageRoll() {
		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
		int dmg;
		if (wep != null) {
			dmg = wep.damageRoll(this);
		} else {
			dmg = Random.IntRange(1, effectiveHonesty());
		}
		return inFury() ? (int) (dmg * 1.5f) : dmg;
	}

	@Override
	public float speed() {

		int loyaltyBoost =  effectiveLoyalty() - 3;

		double speed = super.speed();

		int aEnc = belongings.barding != null ? belongings.barding.minHonesty - effectiveHonesty() : 0;
		if (aEnc > 0) {

			// TODO: Fold this into the other calculation at some point.
			speed = speed * Math.pow(1.3, -aEnc);

		} else {

			// Let's say pegasi always move more quickly when unencumbered.
			if (heroClass == HeroClass.PEGASUS) {
				loyaltyBoost += 4;
			}

			// TODO: Cannibalize later for THUNDERBOLT
			// return getHeroSprite().sprint(subClass == HeroSubClass.FREERUNNER && !isStarving()) ? 1.6f * speed : speed;

		}

		// Movement is 95% at Y:2, 128% at Y:10, 155% at Y:12, 200% at crazy Y:18 (+4 Y for pega).
		speed = speed * Math.pow(1.05, loyaltyBoost);
		return (float) speed;
	}

	public float attackDelay() {
		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
		if (wep != null) {
			return wep.speedFactor(this);

		} else {
			return 1f;
		}
	}

	@Override
	public void spend(float time) {
		int hasteLevel = this.effectiveLaughter() - 3;

		for (Buff buff : buffs(RingOfHaste.Haste.class)) {
			hasteLevel += ((RingOfHaste.Haste) buff).level * 2;
		}

		// This makes laughter points 1/2 of a Haste step, which is pretty strong,
		// without changing the underlying mechanics of Haste's buffing.
		// Will need to pump up the baseSpeeds of mobs on lower levels to compensate.
		super.spend(hasteLevel == 0 ? time : (float) (time * Math.pow(1.05, -hasteLevel)));
	}

	public void spendAndNext(float time) {
		busy();
		spend(time);
		next();
	}

	@Override
	public boolean act() {

		super.act();

		if (paralysed) {

			curAction = null;

			spendAndNext(TICK);
			return false;
		}

		checkVisibleMobs();
		AttackIndicator.updateState();

		if (curAction == null) {
			if (restoreHealth) {
				if (isStarving() || hp() >= ht()) {
					restoreHealth = false;
				} else {
					spend(TIME_TO_REST);
					next();
					return false;
				}
			}

			if (PixelPonies.realtime()) {
				if (!ready) {
					ready();
				}
				spend(TICK);
				next();
			} else {
				ready();
			}
			return false;

		} else {

			restoreHealth = false;

			ready = false;

			if (curAction instanceof HeroAction.Move) {

				return actMove((HeroAction.Move) curAction);

			} else if (curAction instanceof HeroAction.Interact) {

				return actInteract((HeroAction.Interact) curAction);

			} else if (curAction instanceof HeroAction.Buy) {

				return actBuy((HeroAction.Buy) curAction);

			} else if (curAction instanceof HeroAction.PickUp) {

				return actPickUp((HeroAction.PickUp) curAction);

			} else if (curAction instanceof HeroAction.OpenChest) {

				return actOpenChest((HeroAction.OpenChest) curAction);

			} else if (curAction instanceof HeroAction.Unlock) {

				return actUnlock((HeroAction.Unlock) curAction);

			} else if (curAction instanceof HeroAction.Descend) {

				return actDescend((HeroAction.Descend) curAction);

			} else if (curAction instanceof HeroAction.Ascend) {

				return actAscend((HeroAction.Ascend) curAction);

			} else if (curAction instanceof HeroAction.Attack) {

				return actAttack((HeroAction.Attack) curAction);

			} else if (curAction instanceof HeroAction.Cook) {

				return actCook((HeroAction.Cook) curAction);

			}
		}
		return false;
	}

	public void busy() {
		ready = false;
	}

	private void ready() {
		getSprite().idle();

		curAction = null;
		ready = true;

		GameScene.ready();
	}

	public void interrupt() {
		if (curAction != null && curAction.dst != getPos()) {
			lastAction = curAction;
		}
		curAction = null;
	}

	public void resume() {
		curAction = lastAction;
		lastAction = null;
		act();
	}

	private boolean actMove(HeroAction.Move action) {
		if (getCloser(action.dst)) {

			return true;

		} else {
			if (Dungeon.level.map[getPos()] == Terrain.SIGN) {
				GameScene.show(new WndMessage(Dungeon.tip()));
			}
			ready();
			return false;
		}
	}

	private boolean actInteract(HeroAction.Interact action) {

		Mob npc = action.npc;

		if (Dungeon.level.adjacent(getPos(), npc.getPos())) {

			ready();
			getSprite().turnTo(getPos(), npc.getPos());
			if (!npc.interact(this)) {
				actAttack(new HeroAction.Attack(npc));
			}
			return false;

		} else {

			if (Dungeon.level.fieldOfView[npc.getPos()] && getCloser(npc.getPos())) {
				return true;
			} else {
				ready();
				return false;
			}
		}
	}

	private boolean actBuy(HeroAction.Buy action) {
		int dst = action.dst;
		if (getPos() == dst || Dungeon.level.adjacent(getPos(), dst)) {

			ready();

			Heap heap = Dungeon.level.getHeap(dst);
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
				GameScene.show(new WndTradeItem(heap, true));
			}

			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actCook(HeroAction.Cook action) {
		int dst = action.dst;
		if (Dungeon.visible[dst]) {

			ready();
			AlchemyPot.operate(this, dst);
			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actPickUp(HeroAction.PickUp action) {
		int dst = action.dst;
		if (getPos() == dst) {

			Heap heap = Dungeon.level.getHeap(getPos());
			if (heap != null) {
				Item item = heap.pickUp();
				item = item.pick(this, getPos());
				if (item != null) {
					if (item.doPickUp(this)) {

						itemPickedUp(item);

						if (!heap.isEmpty()) {
							GLog.i(TXT_SOMETHING_ELSE);
						}
						curAction = null;
					} else {
						Dungeon.level.drop(item, getPos()).sprite.drop();
						ready();
					}
				} else {
					ready();
				}
			} else {
				ready();
			}

			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	public void itemPickedUp(Item item) {
		if (item instanceof Dewdrop) {
			return;
		}

		if ((item instanceof ScrollOfUpgrade && ((ScrollOfUpgrade) item).isKnown())
				|| (item instanceof PotionOfHonesty && ((PotionOfHonesty) item).isKnown())
				|| (item instanceof ScrollOfLoyalOath && ((ScrollOfLoyalOath) item).isKnown())
				|| (item instanceof Spellbook)) {
			GLog.p(TXT_YOU_NOW_HAVE, item.name());
		} else {
			GLog.i(TXT_YOU_NOW_HAVE, item.name());
		}
	}

	private boolean actOpenChest(HeroAction.OpenChest action) {
		int dst = action.dst;
		if (Dungeon.level.adjacent(getPos(), dst) || getPos() == dst) {

			Heap heap = Dungeon.level.getHeap(dst);
			if (heap != null && (heap.type == Type.CHEST || heap.type == Type.TOMB || heap.type == Type.SKELETON
					|| heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST || heap.type == Type.MIMIC)) {

				theKey = null;

				if (heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST) {

					theKey = belongings.getKey(GoldenKey.class, Dungeon.depth);

					if (theKey == null) {
						GLog.w(TXT_LOCKED_CHEST);
						ready();
						return false;
					}
				}

				switch (heap.type) {
					case TOMB:
						Sample.INSTANCE.play(Assets.SND_TOMB);
						Camera.main.shake(1, 0.5f);
						break;
					case SKELETON:
						break;
					default:
						Sample.INSTANCE.play(Assets.SND_UNLOCK);
				}

				spend(Key.TIME_TO_UNLOCK);
				getSprite().operate(dst);

			} else {
				ready();
			}

			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actUnlock(HeroAction.Unlock action) {
		int doorCell = action.dst;

		if (Dungeon.level.adjacent(getPos(), doorCell)) {
			theKey = null;
			int door = Dungeon.level.map[doorCell];

			if (door == Terrain.LOCKED_DOOR) {
				theKey = belongings.getKey(IronKey.class, Dungeon.depth);
			} else if (door == Terrain.LOCKED_EXIT) {
				theKey = belongings.getKey(SkeletonKey.class, Dungeon.depth);
			}

			if (theKey != null) {
				spend(Key.TIME_TO_UNLOCK);
				getSprite().operate(doorCell);
				Sample.INSTANCE.play(Assets.SND_UNLOCK);
			} else {
				GLog.w(TXT_LOCKED_DOOR);
				ready();
			}

			return false;

		} else if (getCloser(doorCell)) {
			return true;
		} else {
			ready();
			return false;
		}
	}

	private boolean actDescend(HeroAction.Descend action) {

		refreshPets();

		int stairs = action.dst;
		if (getPos() == stairs && Dungeon.level.isExit(getPos())) {

			Dungeon.level.onHeroDescend(getPos());

			clearActions();

			Hunger hunger = buff(Hunger.class);
			if (hunger != null && !hunger.isStarving()) {
				hunger.satisfy(-Hunger.STARVING / 10);
			}

			InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
			Game.switchScene(InterlevelScene.class);

			return false;

		} else if (getCloser(stairs)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAscend(HeroAction.Ascend action) {
		refreshPets();

		int stairs = action.dst;
		if (getPos() == stairs && getPos() == Dungeon.level.entrance) {

			if (Dungeon.depth == 0) {
				GameScene.show(new WndMessage(TXT_COME_TOO_FAR));
				ready();
			} else if (Dungeon.depth == 1) {

				if (belongings.getItem(Amulet.class) == null) {
					GameScene.show(new WndMessage(TXT_LEAVE));
					ready();
				} else {
					Dungeon.win(ResultDescriptions.WIN, Rankings.gameOver.WIN_HAPPY);

					Dungeon.gameOver();

					Game.switchScene(SurfaceScene.class);
				}

			} else {

				clearActions();

				Hunger hunger = buff(Hunger.class);
				if (hunger != null && !hunger.isStarving()) {
					hunger.satisfy(-Hunger.STARVING / 10);
				}

				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}

			return false;

		} else if (getCloser(stairs)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean getCloserToEnemy() {
		if (Dungeon.level.fieldOfView[enemy.getPos()] && getCloser(enemy.getPos())) {
			return true;
		} else {
			ready();
			return false;
		}
	}

	private boolean actMeleeAttack() {

		if (Dungeon.level.adjacent(getPos(), enemy.getPos())) {
			spend(attackDelay());
			getSprite().attack(enemy.getPos());

			return false;
		}
		return getCloserToEnemy();
	}

	private boolean actBowAttack() {

		Bow bow = (Bow) belongings.weapon;
		Class<? extends Arrow> selectedArrowType = bow.arrowType();
		Arrow arrow;

		// No arrow type selected: try using last arrow type
		if (selectedArrowType.equals(Arrow.class)) {
			arrow = belongings.getItem(lastArrowType);

		// Specific arrow type was selected
		} else {
			arrow = belongings.getItem(selectedArrowType);
		}

		// All out of those arrows: try to grab first arrow type in class order
		if (arrow == null) {
			arrow = belongings.getItem(Arrow.class);
		}

		// Totally out of arrows, or can't shoot because we're flanked
		if (arrow == null || (this.isFlanked() && !(heroClass == HeroClass.NIGHTWING))) {
			return actMeleeAttack();

		// Time to use that arrow, and update lastArrowType
		} else {
			lastArrowType = arrow.getClass();
			arrow.cast(this, enemy.getPos());
			ready();
			return false;
		}


	}

	private boolean actAttack(HeroAction.Attack action) {

		enemy = action.target;

		if (enemy.isAlive() && !pacified) {
			if (belongings.weapon instanceof SpecialWeapon) {
				return actSpecialAttack(action);
			}

			if (bowEquipped()) {
				return actBowAttack();
			} else {
				return actMeleeAttack();
			}

		}

		return getCloserToEnemy();
	}

	private boolean applySpecialTo(SpecialWeapon weapon, Char enemy) {
		spend(attackDelay());
		getSprite().attack(enemy.getPos());
		weapon.applySpecial(this, enemy);
		return false;
	}

	private boolean actSpecialAttack(Attack action) {
		SpecialWeapon weapon = (SpecialWeapon) belongings.weapon;

		if (weapon.getRange() == 1) {
			if (Dungeon.level.adjacent(getPos(), enemy.getPos())) {
				return applySpecialTo(weapon, enemy);
			}
			return getCloserToEnemy();
		} else {

			Ballistica.cast(getPos(), action.target.getPos(), false, true);

			for (int i = 1; i <= Math.min(Ballistica.distance, weapon.getRange()); i++) {
				Char chr = Actor.findChar(Ballistica.trace[i]);
				if (chr != null) {
					return applySpecialTo(weapon, chr);
				}
			}

			return getCloserToEnemy();
		}
	}

	public void rest(boolean tillHealthy) {
		spendAndNext(TIME_TO_REST);
		if (!tillHealthy) {
			getSprite().showStatus(CharSprite.DEFAULT, TXT_WAIT);
		}
		restoreHealth = tillHealthy;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
		if (wep != null) {

			wep.proc(this, enemy, damage);

			switch (subClass) {
				case ROYAL_GUARD:
					if (wep instanceof MeleeWeapon) {
						damage += Buff.affect(this, Combo.class).hit(enemy, damage);
					}
					break;

				case ASSASSIN:
					if (rangedWeapon != null) {
						Buff.prolong(enemy, SnipersMark.class, attackDelay() * 1.1f);
					}
					break;
				default:
			}
		}


		return damage;
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		damage = super.defenseProc(enemy, damage);

		RingOfThorns.Thorns thorns = buff(RingOfThorns.Thorns.class);
		if (thorns != null) {
			int dmg = Random.IntRange(0, damage);
			if (dmg > 0) {
				enemy.damage(dmg, thorns);
			}
		}

		if (buff(HeartOfDarkness.HeartOfDarknessBuff.class) != null) {
			int spiritPos = Dungeon.level.getEmptyCellNextTo(getPos());

			if (Dungeon.level.cellValid(spiritPos)) {
				SpiritOfPain spirit = new SpiritOfPain();
				spirit.setPos(spiritPos);
				Dungeon.level.spawnMob(spirit, 0);
				Actor.addDelayed(new Pushing(spirit, getPos(), spirit.getPos()), -1);
				Mob.makePet(spirit, this);
			}
		}

		if (belongings.barding != null) {
			damage = belongings.barding.proc(enemy, this, damage);
		}

		return damage;
	}

	@Override
	public void damage(int dmg, Object src) {
		restoreHealth = false;
		super.damage(dmg, src);

		checkIfFurious();
		interrupt();

		if (belongings.barding instanceof SpiderBarding)
		{
			//Barding proc
			if (Random.Int(100) < 50){
				GameScene.add( Blob.seed( getPos(), Random.Int( 5, 7 ), Web.class ) );
			}
		}


	}

	public void checkIfFurious() {
		if (subClass == HeroSubClass.NOCTURNE && 0 < hp() && hp() <= ht() * Fury.LEVEL) {
			if (buff(Fury.class) == null) {
				Buff.affect(this, Fury.class);
				ready();
			}
		}
	}

	private void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<>();

		boolean newMob = false;

		for (Mob m : Dungeon.level.mobs) {
			if (Dungeon.level.fieldOfView[m.getPos()] && m.hostile && !m.isPet()) {
				visible.add(m);
				if (!visibleEnemies.contains(m)) {
					newMob = true;
				}
			}
		}

		if (newMob) {
			interrupt();
			restoreHealth = false;
		}

		visibleEnemies = visible;
	}

	public Mob getNearestEnemy() {

		Mob nearest = null;
		int dist = Integer.MAX_VALUE;
		for (Mob mob : visibleEnemies) {
			int mobDist = Dungeon.level.distance(getPos(), mob.getPos());
			if (mobDist < dist) {
				dist = mobDist;
				nearest = mob;
			}
		}
		return nearest;
	}

	public int visibleEnemies() {
		return visibleEnemies.size();
	}

	public Mob visibleEnemy(int index) {
		return visibleEnemies.get(index % visibleEnemies.size());
	}

	private boolean getCloser(final int target) {

		if (rooted) {
			return false;
		}

		int step = -1;

		Buff wallWalkerBuff = buff(RingOfStoneWalking.StoneWalking.class);

		if (Dungeon.level.adjacent(getPos(), target)) {

			if (Actor.findChar(target) == null) {
				if (Dungeon.level.pit[target] && !flying && !Chasm.jumpConfirmed) {
					Chasm.heroJump(this);
					interrupt();
					return false;
				}
				if (wallWalkerBuff == null && (Dungeon.level.passable[target] || Dungeon.level.avoid[target])) {
					step = target;
				}
				if (wallWalkerBuff != null && Dungeon.level.solid[target]) {
					step = target;
				}
			}

		} else {

			int len = Dungeon.level.getLength();
			boolean[] p = wallWalkerBuff != null ? Dungeon.level.solid : Dungeon.level.passable;
			boolean[] v = Dungeon.level.visited;
			boolean[] m = Dungeon.level.mapped;
			boolean[] passable = new boolean[len];
			for (int i = 0; i < len; i++) {
				passable[i] = p[i] && (v[i] || m[i]);
			}

			step = Dungeon.findPath(this, getPos(), target, passable, Dungeon.level.fieldOfView);
		}

		if (step != -1) {

			int oldPos = getPos();

			move(step);
			getSprite().move(oldPos, getPos());

			if (wallWalkerBuff != null) {
				int dmg = hp() / 2 > 2 ? hp() / 2 : 2;
				damage(dmg, wallWalkerBuff);
			}

			spend(1 / speed());

			return true;

		} else {
			return false;
		}
	}

	public boolean handle(int cell) {

		if (!Dungeon.level.cellValid(cell)) {
			return false;
		}

		Char ch;
		Heap heap;

		if (Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != getPos()) {

			curAction = new HeroAction.Cook(cell);

		} else if (Dungeon.level.fieldOfView[cell] && (ch = Actor.findChar(cell)) instanceof Mob) {

			Mob mob = (Mob) ch;

			if (ch instanceof NPC && ((NPC) ch).friendly()) {
				curAction = new HeroAction.Interact(mob);
			} else if (mob.isPet()) {
				curAction = new HeroAction.Interact(mob);
			} else {
				curAction = new HeroAction.Attack(ch);
			}

		} else if ((heap = Dungeon.level.getHeap(cell)) != null) {

			switch (heap.type) {
				case HEAP:
					curAction = new HeroAction.PickUp(cell);
					break;
				case FOR_SALE:
					curAction = heap.size() == 1 && heap.peek().price() > 0 ? new HeroAction.Buy(cell)
							: new HeroAction.PickUp(cell);
					break;
				default:
					curAction = new HeroAction.OpenChest(cell);
			}

		} else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {

			curAction = new HeroAction.Unlock(cell);

		} else if (Dungeon.level.isExit(cell)) {

			curAction = new HeroAction.Descend(cell);

		} else if (cell == Dungeon.level.entrance) {

			curAction = new HeroAction.Ascend(cell);

		} else {

			curAction = new HeroAction.Move(cell);
			lastAction = null;

		}

		return act();
	}

	public void earnExp(int exp) {

		this.setExp(this.getExp() + exp);

		boolean levelUp = false;
		while (this.getExp() >= maxExp()) {
			this.setExp(this.getExp() - maxExp());
			lvl(lvl() + 1);

			ht(ht() + 5);
			hp(hp() + 5);
			attackSkill++;
			defenseSkill++;

			levelUp = true;
		}

		if (levelUp) {

			GLog.p(TXT_NEW_LEVEL, lvl());
			getSprite().showStatus(CharSprite.POSITIVE, TXT_LEVEL_UP);
			Sample.INSTANCE.play(Assets.SND_LEVELUP);

			Badges.validateLevelReached();
		}

		if (subClass == HeroSubClass.VAMPONY) {

			int value = Math.min(ht() - hp(), 1 + (Dungeon.depth - 1) / 5);
			if (value > 0) {
				hp(hp() + value);
				getSprite().emitter().burst(Speck.factory(Speck.HEALING), 1);
			}

			buff(Hunger.class).satisfy(10);
		}
	}

	public int maxExp() {
		if (getDifficulty() != 0) {
			return 5 + lvl() * 5;
		} else {
			return 5 + lvl() * 4;
		}
	}

	public boolean isStarving() {
		return buff(Hunger.class).isStarving();
	}

	@Override
	public void updateSpriteState(){
		super.updateSpriteState();
	}

	@Override
	public void add(Buff buff) {
		super.add(buff);

		if(!GameScene.isSceneReady()) {
			return;
		}

		if (buff instanceof Burning) {
			GLog.w(Game.getVar(R.string.Hero_StaBurning));
			interrupt();
		} else if (buff instanceof Paralysis) {
			GLog.w(Game.getVar(R.string.Hero_StaParalysis));
			interrupt();
		} else if (buff instanceof Poison) {
			GLog.w(Game.getVar(R.string.Hero_StaPoison));
			interrupt();
		} else if (buff instanceof Ooze) {
			GLog.w(Game.getVar(R.string.Hero_StaOoze));
		} else if (buff instanceof Roots) {
			GLog.w(Game.getVar(R.string.Hero_StaRoots));
		} else if (buff instanceof Weakness) {
			GLog.w(Game.getVar(R.string.Hero_StaWeakness));
		} else if (buff instanceof Blindness) {
			GLog.w(Game.getVar(R.string.Hero_StaBlindness));
		} else if (buff instanceof Fury) {
			GLog.w(Game.getVar(R.string.Hero_StaFury));
			getSprite().showStatus(CharSprite.POSITIVE, Game.getVar(R.string.Hero_StaFurious));
		} else if (buff instanceof Charm) {
			GLog.w(Game.getVar(R.string.Hero_StaCharm));
		} else if (buff instanceof Cripple) {
			GLog.w(Game.getVar(R.string.Hero_StaCripple));
		} else if (buff instanceof Bleeding) {
			GLog.w(Game.getVar(R.string.Hero_StaBleeding));
		} else if (buff instanceof Vertigo) {
			GLog.w(Game.getVar(R.string.Hero_StaVertigo));
			interrupt();
		}

		BuffIndicator.refreshHero();
	}

	@Override
	public void remove(Buff buff) {
		super.remove(buff);

		BuffIndicator.refreshHero();
	}

	@Override
	public int stealth() {
		int stealth = super.stealth();
		for (Buff buff : buffs(RingOfShadows.Shadows.class)) {
			stealth += ((RingOfShadows.Shadows) buff).level;
		}

		int kindnessVal = effectiveKindness() - 2;

		stealth += (kindnessVal / 3);

		// Not sure this residual calculation is worth the overhead...
		if (Random.Int(3) < kindnessVal % 3) stealth++;

		return stealth;
	}

	@Override
	public void die(Object cause) {
		clearActions();

		DewVial.autoDrink(this);
		if (isAlive()) {
			new Flare(8, 32).color(0xFFFF66, true).show(getSprite(), 2f);
			return;
		}

		Actor.fixTime();
		super.die(cause);

		Ankh ankh = belongings.getItem(Ankh.class);
		if (ankh == null) {
			reallyDie(cause);
		} else {
			Dungeon.deleteGame(false);
			while(belongings.removeItem(ankh));
			GameScene.show(new WndResurrect(ankh, cause));
		}
	}

	public void clearActions() {
		curAction  = null;
		lastAction = null;
	}

	public static void reallyDie(Object cause) {

		int length = Dungeon.level.getLength();
		int[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Dungeon.level.discoverable;

		for (int i = 0; i < length; i++) {
			int terr = map[i];
			if (discoverable[i]) {
				visited[i] = true;
				if ((TerrainFlags.flags[terr] & TerrainFlags.SECRET) != 0) {
					Dungeon.level.set(i, Terrain.discover(terr));
					GameScene.updateMap(i);
				}
			}
		}

		Bones.leave();

		Dungeon.observe();

		Dungeon.hero.belongings.identify();

		GameScene.gameOver();

		if (cause instanceof Hero.Doom) {
			((Hero.Doom) cause).onDeath();
		}

		Dungeon.gameOver();
	}

	@Override
	public void move(int step) {
		super.move(step);

		if (!flying) {

			if (Dungeon.level.water[getPos()]) {
				Sample.INSTANCE.play(Assets.SND_WATER, 1, 1, Random.Float(0.8f, 1.25f));
			} else {
				Sample.INSTANCE.play(Assets.SND_STEP);
			}
			Dungeon.level.pressHero(getPos(), this);
		}
	}

	@Override
	public void onMotionComplete() {
		Dungeon.observe();
		search(false);

		super.onMotionComplete();
	}

	@Override
	public void onAttackComplete() {

		if (enemy instanceof Rat && buff(RatKingCrown.RatKingAuraBuff.class) != null) {
			Rat rat = (Rat) enemy;
			Mob.makePet(rat, this);
		} else {
			AttackIndicator.target(enemy);
			attack(enemy);
		}
		curAction = null;

		Invisibility.dispel(this);

		super.onAttackComplete();
	}

	@Override
	public void onOperateComplete() {

		if (curAction instanceof HeroAction.Unlock) {

			if (theKey != null) {
				theKey.detach(belongings.backpack);
				theKey = null;
			}

			int doorCell = ((HeroAction.Unlock) curAction).dst;
			int door = Dungeon.level.map[doorCell];

			switch (door) {
				case Terrain.LOCKED_DOOR:
					Dungeon.level.set(doorCell,Terrain.DOOR);
					break;
				case Terrain.LOCKED_EXIT:
					Dungeon.level.set(doorCell,Terrain.UNLOCKED_EXIT);
					break;
				default:
					EventCollector.logException(new Exception("trying to unlock tile:"+door));
			}
			GameScene.updateMap(doorCell);

		} else if (curAction instanceof HeroAction.OpenChest) {

			if (theKey != null) {
				theKey.detach(belongings.backpack);
				theKey = null;
			}

			Heap heap = Dungeon.level.getHeap(((HeroAction.OpenChest) curAction).dst);
			if (heap != null) {
				if (heap.type == Type.SKELETON) {
					Sample.INSTANCE.play(Assets.SND_BONES);
				}
				heap.open(this);
			}
		}
		curAction = null;

		super.onOperateComplete();
	}

	public boolean search(boolean intentional) {

		boolean found = false;

		int positive = 0;
		int negative = 0;
		for (Buff buff : buffs(RingOfDetection.Detection.class)) {
			int bonus = ((RingOfDetection.Detection) buff).level;
			if (bonus > positive) {
				positive = bonus;
			} else if (bonus < 0) {
				negative += bonus;
			}
		}
		int distance = 1 + positive + negative;


		// This is the correct code for the original approach, translated into new, not-shit system.
//		int awarenessIndex = Math.max(lvl(), 9);
//		if (intentional) {
//			awarenessIndex = awarenessIndex * 2 + 1;
//		}
//		if (heroClass == HeroClass.PEGASUS) {
//			awarenessIndex = (int) ((float) awarenessIndex * 1.5F + 0.5F);
//		}

		// This is the experimental code that uses generosity instead of only relying on character level.
		int awarenessIndex;

		// Generosity should now count for about 2/3rds of the detection effect.
		if (effectiveGenerosity() >= 2) {
			awarenessIndex = Math.min(0, effectiveGenerosity() - 2);

		// If your generosity is abominably low, penalize it off-chart by adding distance.
		} else {
			awarenessIndex = 0;
			distance += 2 - effectiveGenerosity();
		}

		// Character level counts for the other 1/3rd.
		awarenessIndex += lvl() / 6;

		// Trying to search is much easier, naturally.
		if (intentional) {
			awarenessIndex = awarenessIndex * 2 + 1;
		}

		float detectChance;

		// Now that I'm pretty sure this works somewhat correctly, adding a safeguard so we can delay testing.
		if (awarenessIndex < detectChances.length) {
			detectChance = detectChances[awarenessIndex];

		// We're hushing an error for now: this should not be possible with current char level and generosity limits. But for the future, this scales.
		} else {
			awarenessIndex -= detectChances.length;
			detectChance = detectChances[detectChances.length - 1] + ((float)awarenessIndex * 0.0035F);
		}

		if (distance <= 0) {
			detectChance /= 2.0F - (float) distance;
			distance = 1;
		}

		int cx = getPos() % Dungeon.level.getWidth();
		int cy = getPos() / Dungeon.level.getWidth();
		int ax = cx - distance;
		if (ax < 0) {
			ax = 0;
		}
		int bx = cx + distance;
		if (bx >= Dungeon.level.getWidth()) {
			bx = Dungeon.level.getWidth() - 1;
		}
		int ay = cy - distance;
		if (ay < 0) {
			ay = 0;
		}
		int by = cy + distance;
		if (by >= Dungeon.level.getHeight()) {
			by = Dungeon.level.getHeight() - 1;
		}

		for (int y = ay; y <= by; y++) {
			for (int x = ax, p = ax + y * Dungeon.level.getWidth(); x <= bx; x++, p++) {

				if (Dungeon.visible[p]) {

					if (intentional) {
						getSprite().getParent().addToBack(new CheckedCell(p));
					}

					if (Dungeon.level.secret[p] && (intentional || Random.Float() < detectChance)) {

						int oldValue = Dungeon.level.map[p];

						GameScene.discoverTile(p, oldValue);

						Dungeon.level.set(p, Terrain.discover(oldValue));

						GameScene.updateMap(p);

						ScrollOfMagicMapping.discover(p);

						found = true;
					}
				}
			}
		}

		if (intentional) {
			getSprite().showStatus(CharSprite.DEFAULT, TXT_SEARCH);
			getSprite().operate(getPos());
			if (found) {
				spendAndNext(Random.Float() < detectChance ? TIME_TO_SEARCH : TIME_TO_SEARCH * 2);
			} else {
				spendAndNext(TIME_TO_SEARCH);
			}

		}

		if (found) {
			GLog.w(TXT_NOTICED_SMTH);
			Sample.INSTANCE.play(Assets.SND_SECRET);
			interrupt();
		}

		return found;
	}

	public void resurrect(int resetLevel) {

		hp(ht());
		Dungeon.gold(0);
		setExp(0);

		belongings.resurrect(resetLevel);

		live();
	}

	@Override
	public Set<Class<?>> resistances() {
		RingOfElements.Resistance r = buff(RingOfElements.Resistance.class);
		return r == null ? super.resistances() : r.resistances();
	}

	@Override
	public Set<Class<?>> immunities() {
		GasesImmunity buff = buff(GasesImmunity.class);
		return buff == null ? super.immunities() : GasesImmunity.IMMUNITIES;
	}

	public HeroSpriteDef getHeroSprite() {
		return (HeroSpriteDef) getSprite();
	}

	public int lvl() {
		return Scrambler.descramble(lvl);
	}

	private void lvl(int lvl) {
		this.lvl = Scrambler.scramble(lvl);
	}

	public int getExp() {
		return Scrambler.descramble(exp);
	}

	public void setExp(int exp) {
		this.exp = Scrambler.scramble(exp);
	}

	public interface Doom {
		void onDeath();
	}

	public void updateLook() {
		getHeroSprite().heroUpdated(this);
		ready();
	}

	public void collect(Item item) {
		if (!item.collect(this)) {
			if (Dungeon.level != null && getPos() != 0) {
				Dungeon.level.drop(item, getPos()).sprite.drop();
			}
		}
	}

	public boolean isReady() {
		return isAlive() && ready;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void spawnPets() {
		refreshPets();

		for (Mob pet : pets) {
			int cell = Dungeon.level.getEmptyCellNextTo(getPos());
			if(cell == -1){
				cell = getPos();
			}
			pet.setPos(cell);

			pet.state = pet.WANDERING;
			Dungeon.level.spawnMob(pet);
		}
	}

	private void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
		Dungeon.setDifficulty(difficulty);
	}

	public boolean isFlanked() {
		for (Mob mob : Dungeon.level.mobs) {

			// TODO: I'm pretty sure this field falsely reports 'true' for pets.
			if (mob.hostile && mob.distance(this) == 1) {
				return true;
			}
		}
		return false;
	}

}
