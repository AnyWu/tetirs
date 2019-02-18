package tetris;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Tetris tetris = new Tetris();
		frame.add(tetris);
		frame.setSize(535,550);
		frame.setUndecorated(false);
		frame.setTitle("Teris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		tetris.action();
	}
}
