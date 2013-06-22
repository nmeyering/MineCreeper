package model;

import java.util.Observable;

public class Square extends Observable{
	public final int x;
	public final int y;
	public boolean isMine;
	public boolean isFlagged;
	public boolean isRevealed;
	public int neighboringMines;
	
	/**
	 * @param mine indicates whether this square has a mine on it
	 * @param flag indicates whether this square is protected (flagged)
	 */
	public Square(boolean mine, boolean flag, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.isMine = mine;
		this.isFlagged = flag;
		this.isRevealed = false;
		this.neighboringMines = 0;
	}

	public Square(boolean mine, int x, int y)
	{
		this(mine, false, x, y);
	}
	
	public boolean reveal()
	{
		if (!this.isRevealed)
		{
			this.isRevealed = true;
			return true;
		}
		return false;
	}
}
