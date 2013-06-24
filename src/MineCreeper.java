

import view.MainFrame;
import model.Board;

public class MineCreeper {

	public static void main(String[] args) {

		int width = 0;
		int height = 0;
		int bombs = 0;
		int seed = 5;
		
		try
		{
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
			bombs = Integer.parseInt(args[2]);

			if (width < 1)
				throw new IllegalArgumentException("width >= 1 required");
			if (height < 1)
				throw new IllegalArgumentException("height >= 1 required");
			if (bombs > width * height - 1)
				throw new IllegalArgumentException("cannot place more bombs than squares");
			if (bombs < 0)
				throw new IllegalArgumentException("A negative amount of bombs? What does that even mean?");

		}
		catch (NullPointerException e)
		{
			printUsage();
			System.exit(1);
		}
		catch (IllegalArgumentException e)
		{
			System.err.println(e.getMessage());
			printUsage();
			System.exit(1);
		}

		final Board board = new Board(
				width,
				height,
				bombs,
				seed);

		MainFrame m = new MainFrame(board);
	}
	
	public static void printUsage()
	{
		System.out.println(
		"Usage: MineCreeper <width> <height> <bombs>\n" +
		"\twidth: width of the board\n" +
		"\theight: height of the board\n" +
		"\tbombs: number of bombs to place on the board"
		);
	}

}
