package com.retrom.volcano.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.utils.WeightedRandom;
import com.retrom.volcano.game.objects.Collectable.Type;
import com.retrom.volcano.game.objects.Collectable.BaseType;

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
				.add(1, Type.BRONZE_1)
				.build());
		
		levelCoinChances.set(1, builder()
				.add(1, Type.BRONZE_1)
				.build());
		
		levelCoinChances.set(2, builder()
				.add(0.35f, Type.BRONZE_1)
				.add(0.65f, Type.SILVER_1)
				.build());
		
		levelCoinChances.set(3, builder()
				.add(0.20f, Type.BRONZE_1)
				.add(0.45f, Type.SILVER_1)
				.add(0.25f, Type.GOLD_1)
				.add(0.10f, Type.RING_GREEN)
				.build());
		
		levelCoinChances.set(4, builder()
				.add(0.10f, Type.BRONZE_1)
				.add(0.25f, Type.SILVER_1)
				.add(0.50f, Type.GOLD_1)
				.add(0.15f, Type.RING_GREEN)
				.build());
		
		levelCoinChances.set(5, builder()
				.add(0.10f, Type.SILVER_1)
				.add(0.60f, Type.GOLD_1)
				.add(0.05f, Type.RING_GREEN)
				.add(0.05f, Type.DIAMOND_GREEN)
				.add(0.20f, Type.TOKEN)
				.build());
		
		levelCoinChances.set(6, builder()
				.add(0.60f, Type.GOLD_1)
				.add(0.10f, Type.RING_GREEN)
				.add(0.10f, Type.DIAMOND_GREEN)
				.add(0.20f, Type.TOKEN)
				.build());
		
		levelCoinChances.set(7, builder()
				.add(0.50f, Type.GOLD_1)
				.add(0.15f, Type.RING_GREEN)
				.add(0.15f, Type.DIAMOND_GREEN)
				.add(0.20f, Type.TOKEN)
				.build());
	}

	private static void initSpecificTypes() {
		bronzeCoins = builder()
				.add(0.5f, Type.BRONZE_1)
				.add(0.5f, Type.BRONZE_2).build();
		
		silverCoins = builder()
				.add(0.35f, Type.SILVER_1)
				.add(0.35f, Type.SILVER_2)
				.add(0.3f, Type.SILVER_MASK).build();
		
		goldCoins = builder()
				.add(0.35f, Type.GOLD_1)
				.add(0.35f, Type.GOLD_2)
				.add(0.3f, Type.GOLD_MASK).build();
		
		diamonds = builder()
				.add(1/3f, Type.RING_GREEN)
				.add(1/3f, Type.RING_PURPLE)
				.add(1/3f, Type.RING_BLUE).build();
		
		rings = builder()
				.add(1/3f, Type.DIAMOND_GREEN)
				.add(1/3f, Type.DIANOMD_BLUE)
				.add(1/3f, Type.DIAMOND_PURPLE).build();
	}
	
//	public static Type getNextCoin(int level) {
//		WeightedRandom<Collectable.Type> wr =
//				CoinChancesConfiguration.levelCoinChances.get(level);
//		return getNextCoin(wr);
//	}

	public static Type CoinFromBase(BaseType t) {
		if (t == null) return null;
		switch (t) {
		case BRONZE:
			return bronzeCoins.getNext();
		case SILVER:
			return silverCoins.getNext();
		case GOLD:
			return goldCoins.getNext();
		case DIAMOND:
			return diamonds.getNext();
		case RING:
			return rings.getNext();
		case TOKEN:
			return Collectable.Type.TOKEN;
		case NONE:
			return null;
		default:
			break;
		}
		// Should never happen.
		return null;
	}

	// private static float[] = {0.1f};
}
