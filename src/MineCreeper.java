

import view.MainFrame;
import model.Board;

public class MineCreeper {

	public static void main(String[] args) {
		final Board board = new Board(32, 18, 20, 5);

		MainFrame m = new MainFrame(board);
		m.setVisible(true);
	}

}
