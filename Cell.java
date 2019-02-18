package cell;

import java.awt.Image;

public class Cell {
	private int row;
	private int col;
	private Image image;

	public Cell(int i, int j, Image l) {
	  super();
		this.row = i;
		this.col = j;
		this.image = l;
	}

	public int getCol() {
		// TODO Auto-generated method stub
		return col;
	}

	public int getRow() {
		// TODO Auto-generated method stub
		return row;
	}

	public Image getImage() {
		// TODO Auto-generated method stub
		return image;
	}
	public void moveRight(){
		col++;
	}
	public void moveLeft(){
		col--;
	}
	public void moveDown(){
		row++;
	}

	public void setRow(int i) {
		this.row = i;
		
	}

	public void setCol(int i) {
		this.col = i;	
	}

}
