package org.softuni.javafundamentals.game.utils;

/**
 * Utility class
 *
 */
public class GameUtils {
	
	/**
	 * Enum for the direction
	 *
	 */
	public enum Direction {
		UP,	DOWN, LEFT,	RIGHT
	}
	
	/**
	 * Defines the size of one block
	 */
	public static final int BLOCK_SIZE = 30;
	/**
	 * Defines the width
	 */
	public static final int WIDTH = 30 * BLOCK_SIZE;
	/**
	 * Defines the height
	 */
	public static final int HEIGHT = 20 * BLOCK_SIZE;
	/**
	 * Defines the score step
	 */
	public static final int SCORE_STEP = 10;
}
