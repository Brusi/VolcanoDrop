package com.retrom.volcano.game.conf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.utils.WeightedRandom;
import com.retrom.volcano.game.objects.Collectable.Type;

public class CoinChancesConfiguration {
	private static List<WeightedRandom<Type>> levelCoinChances;
	
	private static WeightedRandom<Type> bronzeCoins;
	private static WeightedRandom<Type> silverCoins;
	private static WeightedRandom<Type> goldCoins;
	private static WeightedRandom<Type> diamonds;
	private static WeightedRandom<Type> rings;

	private static WeightedRandom.Builder<Type> builder() {
		return new WeightedRandom.Builder<Type>();
	}

	public static void init() {
		initSpecificTypes();
		
		levelCoinChances = new ArrayList<WeightedRandom<Type>>(
				Collections.<WeightedRandom<Type>> nCopies(60, null));
		
		Collections.nCopies(60, null);
		
		levelCoinChances.set(0, builder()
				.add(1, Type.COIN_1_1)
				.build());
		
		levelCoinChances.set(1, builder()
				.add(1, Type.COIN_1_1)
				.build());
		
		levelCoinChances.set(2, builder()
				.add(0.35f, Type.COIN_1_1)
				.add(0.65f, Type.COIN_2_1)
				.build());
		
		levelCoinChances.set(3, builder()
				.add(0.20f, Type.COIN_1_1)
				.add(0.45f, Type.COIN_2_1)
				.add(0.25f, Type.COIN_3_1)
				.add(0.10f, Type.COIN_4_1)
				.build());
		
		levelCoinChances.set(4, builder()
				.add(0.10f, Type.COIN_1_1)
				.add(0.25f, Type.COIN_2_1)
				.add(0.50f, Type.COIN_3_1)
				.add(0.15f, Type.COIN_4_1)
				.build());
		
		levelCoinChances.set(5, builder()
				.add(0.10f, Type.COIN_2_1)
				.add(0.60f, Type.COIN_3_1)
				.add(0.05f, Type.COIN_4_1)
				.add(0.05f, Type.COIN_5_4)
				.add(0.20f, Type.COIN_5_1)
				.build());
		
		levelCoinChances.set(6, builder()
				.add(0.60f, Type.COIN_3_1)
				.add(0.10f, Type.COIN_4_1)
				.add(0.10f, Type.COIN_5_4)
				.add(0.20f, Type.COIN_5_1)
				.build());
		
		levelCoinChances.set(7, builder()
				.add(0.50f, Type.COIN_3_1)
				.add(0.15f, Type.COIN_4_1)
				.add(0.15f, Type.COIN_5_4)
				.add(0.20f, Type.COIN_5_1)
				.build());
	}

	private static void initSpecificTypes() {
		bronzeCoins = builder()
				.add(0.5f, Type.COIN_1_1)
				.add(0.5f, Type.COIN_1_2).build();
		
		silverCoins = builder()
				.add(0.35f, Type.COIN_2_1)
				.add(0.35f, Type.COIN_2_2)
				.add(0.3f, Type.COIN_2_3).build();
		
		goldCoins = builder()
				.add(0.35f, Type.COIN_3_1)
				.add(0.35f, Type.COIN_3_2)
				.add(0.3f, Type.COIN_3_3).build();
		
		diamonds = builder()
				.add(1/3f, Type.COIN_4_1)
				.add(1/3f, Type.COIN_4_2)
				.add(1/3f, Type.COIN_4_3).build();
		
		rings = builder()
				.add(1/3f, Type.COIN_5_1)
				.add(1/3f, Type.COIN_5_2)
				.add(1/3f, Type.COIN_5_3).build();
	}
	
	public static Type getNextCoin(int level) {
		WeightedRandom<Collectable.Type> wr =
				CoinChancesConfiguration.levelCoinChances.get(level);
		if (wr != null) {
			Type t = wr.getNext();
			switch (t) {
			case COIN_1_1:
				return bronzeCoins.getNext();
			case COIN_2_1:
				return silverCoins.getNext();
			case COIN_3_1:
				return goldCoins.getNext();
			case COIN_4_1:
				return diamonds.getNext();
			case COIN_5_1:
				return rings.getNext();
			default:
				return t;
			}
		} else {
			return Type.COIN_1_1;
		}
	}

	// private static float[] = {0.1f};
}
