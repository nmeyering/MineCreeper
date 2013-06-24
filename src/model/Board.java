package model;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
import java.util.Random;

public class Board extends Observable{
	private Square[][] squares;
	public final int width;
	public final int height;
	public final int mines;
	private int unrevealed;
	private boolean lost;

	public Board(int width, int height, int mines, int seed)
	{
		assert(width > 0);
		assert(height > 0);
		assert(mines > 0);

		this.width = width;
		this.height = height;
		if (mines > width * height)
			mines = width * height;
		this.mines = mines;
		
		this.unrevealed = width * height;
		this.lost = false;
		
		this.squares = new Square[width][height];

		for (int y = 0; y < height; ++y)
			for (int x = 0; x < width; ++x)
				this.squares[x][y] = new Square(false, x, y);
		
		Random random = new Random(seed);
		
		for (int i = 0; i < mines; ++i)
		{
			int x;
			int y;
			
			do
			{
				x = random.nextInt(width);
				y = random.nextInt(height);
			}
			while(this.squares[x][y].isMine);
			
			squares[x][y].isMine = true;
			for (int u = x - 1; u <= x + 1; ++u)
				for (int v = y - 1; v <= y + 1; ++v)
				{
					if (!withinBounds(u, v, width, height))
						continue;
					squares[u][v].neighboringMines++;
						
				}
		}

	}

	private static boolean withinBounds(int i, int j, int w, int h) {
		return i >= 0 && i < w && j >=0 && j < h;
	}

	public Board(int width, int height)
	{
		this(width, height, 10, 0);
	}
	
	public void floodFill(int xPos, int yPos)
	{
		Square square = squares[xPos][yPos];

		Queue<Square> q = new LinkedList<>();
		
		q.add(square);

		while(!q.isEmpty())
		{
			square = q.poll();

			if (!square.reveal())
				continue;
			
			unrevealed--;
			setChanged();
			notifyObservers(square);

			if (square.neighboringMines == 0)
			{
				for (int y = square.y - 1; y <= square.y + 1; ++y)
					for (int x = square.x - 1; x <= square.x + 1; ++x)
						if (withinBounds(x, y, width, height))
							q.add(squares[x][y]);
			}
		}
	}
	
	/**
	 * 
	 * @param x x coordinate of the square to reveal
	 * @param y y coordinate of the square to reveal
	 * @return true, if the square was successfully revealed
	 */
	public void reveal(int x, int y)
	{
		Square square = squares[x][y];

		if (!withinBounds(x, y, width, height))
			throw new IllegalArgumentException();
		
		if (!square.reveal())
			return;

		if (square.isMine)
			this.lost = true;

		unrevealed--;
		setChanged();
		notifyObservers(square);
		
		//FIXME hack so the floodFill works for the tile it starts on

		if (square.neighboringMines == 0)
		{
			unrevealed++;
			square.isRevealed = false;
			floodFill(x, y);
		}
		
	}
	
	/**
	 * inverts the square's "flagged"-state
	 * @param x x coordinate of the square
	 * @param y y coordinate of the square
	 */
	public void toggleFlag(int x, int y)
	{
		Square square = squares[x][y];
		square.isFlagged = !square.isFlagged;
		setChanged();
		notifyObservers(square);
	}
	
	/**
	 * when there are as many squares left unrevealed as there are mines,
	 * the game is won.
	 * @return whether the game is won
	 */
	public boolean won()
	{
		return unrevealed == mines;
	}
	
	public boolean lost()
	{
		return lost;
	}
	
	public boolean gameOver()
	{
		return lost() || won();
	}
	
	public int unrevealed()
	{
		return unrevealed;
	}
	
	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder(
				(this.width + 1) *
				this.height);
		
		for (int y = 0; y < height; ++y)
		{
			for (int x = 0; x < width; ++x)
			{
				Square square = this.squares[x][y];
				if (square.isFlagged)
					b.append('!');
				else if (!square.isRevealed)
					b.append(square.isMine ? '*' : '·');
				else if (square.isMine)
					b.append('*');
				else
				{
					int m = square.neighboringMines;

					b.append(m > 0 ? m : " ");
				}
			}
			b.append('\n');
		}
		
		return b.toString();
	}

	public Square getSquare(int x, int y) {
		if (!withinBounds(x, y, this.width, this.height))
			throw new IllegalArgumentException("Requested Square is out of bounds.");
		return squares[x][y];
	}
}