package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements Observer {

	private model.Board board;
	private Map<model.Square, ImageButton> buttonMap;

	public MainFrame(model.Board board) {
		this.board = board;
		board.addObserver(this);

		initGUI();

		this.setTitle("MineCreeper");
		this.setPreferredSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initGUI()
	{
		int cols = this.board.width;
		int rows = this.board.height;
		
		buttonMap = new HashMap<model.Square, ImageButton>();
		
		JPanel boardPanel = new JPanel(
				new GridLayout(
						rows,
						cols));

		for (int y = 0; y < rows; ++y)
			for (int x = 0; x < cols; ++x)
			{
				ImageButton button = new ImageButton(
						new ImageIcon("creeper.png", "creeper").getImage(), 
						x, y);
				buttonMap.put(board.getSquare(x,y), button); 

				button.setEnabled(true);

				button.addMouseListener(new controller.MineCreeperListener(board));

				boardPanel.add(button);
			}
		
		add(boardPanel);
	}

	@Override
	public void update(Observable o, Object arg) {

		if (!(o == board))
			return;
		if (!(arg instanceof model.Square))
			return;


		model.Square square = (model.Square) arg;
		ImageButton button = buttonMap.get(square);

		button.setDrawImage(square.isFlagged);

		if (!square.isRevealed)
			return;
		
		if (square.isMine)
			button.setBackground(new Color(0xffff0000));
		else
		{
			//button.setEnabled(false);
			button.setBackground(Color.WHITE);
			int n = square.neighboringMines;
			button.setText(n <= 0 ? "" : String.format("%2d", n));
		}

		if (board.gameOver()) {
			if (board.won())
				JOptionPane.showMessageDialog(this,
						"Congratulations! You have won the game.");
			if (board.lost())
				JOptionPane.showMessageDialog(this,
						"Game Over");
			System.exit(0);
		}
	}

}
