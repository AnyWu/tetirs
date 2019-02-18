package game;

import java.util.Random;

import cell.Cell;

public class Tetromino {
	protected Cell[] cells = new Cell[4];
	protected State[] states;
	private int index = 10000;
	
	public static Tetromino randomTetromino() {
		Random r= new Random();
		int type = r.nextInt(7);
		switch(type){
		case 0: return new T();
		case 1: return new I();
		case 2: return new J();
		case 3: return new L();
		case 4: return new O();
		case 5: return new S();
		case 6: return new Z();
		}
		return null;
	}

	public Cell[] getCells() {
		// TODO Auto-generated method stub
		return cells;
	}

	public void softDrop() {
		for(Cell c:cells){
			c.moveDown();
		}
	}

	public void moveRight() {
		for(Cell c:cells){
			c.moveRight();
		}	
	}

	public void moveLeft() {
		for(Cell c:cells){
			c.moveLeft();
		}
	}

	public void rotateRight() {
		index++;
		State s = states[index%states.length];
		Cell o = cells[0];
		cells[1].setRow(o.getRow()+s.row1);
		cells[1].setCol(o.getCol()+s.col1);
		cells[2].setRow(o.getRow()+s.row2);
		cells[2].setCol(o.getCol()+s.col2);
		cells[3].setRow(o.getRow()+s.row3);
		cells[3].setCol(o.getCol()+s.col3);
	}

	public void rotateLeft() {
		index--;
		State s = states[index%states.length];
		Cell o = cells[0];
		cells[1].setRow(o.getRow()+s.row1);
		cells[1].setCol(o.getCol()+s.col1);
		cells[2].setRow(o.getRow()+s.row2);
		cells[2].setCol(o.getCol()+s.col2);
		cells[3].setRow(o.getRow()+s.row3);
		cells[3].setCol(o.getCol()+s.col3);
	}
	
	//ƒ⁄≤øÓê£¨”õ‰õ–˝ﬁD†ÓëB
protected class State {
		int row0;
		int col0;
		int row1;
		int col1;
		int row2;
		int col2;
		int row3;
		int col3;
		public State(int row0, int col0, int row1, int col1, int row2, int col2,
				int row3, int col3) {
			super();
			this.row0 = row0;
			this.col0 = col0;
			this.row1 = row1;
			this.col1 = col1;
			this.row2 = row2;
			this.col2 = col2;
			this.row3 = row3;
			this.col3 = col3;
		}
	}
}
