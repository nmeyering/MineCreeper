package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import view.ImageButton;

public class MineCreeperListener extends MouseAdapter {
	private model.Board board;
	
	public MineCreeperListener(model.Board board)
	{
		this.board = board;
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		// FIXME is this the correct way to handle this?
		ImageButton source = ((ImageButton) e.getSource());
		

		if (e.getButton() == MouseEvent.BUTTON1) {
			board.reveal(source.x(), source.y());
		}
		else {
			board.toggleFlag(source.x(), source.y());
		}

	}
}
