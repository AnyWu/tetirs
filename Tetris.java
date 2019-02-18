package tetris;

import game.Tetromino;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import cell.Cell;

public class Tetris extends JPanel{
  /*當前方塊*/	
	private Tetromino tetromino;
	private Tetromino nextOne;
	public static final int ROWS = 18;
	public static final int COLS = 12;
     /*墻*/	
	private Cell[][] wall = new Cell[ROWS][COLS];
	private int lines;
	private int scores;
	public static final int CELL_SIZE = 26;
	private boolean pause;
	private boolean gameOver;
	private Timer timer;
	private static Image background;
	public static Image I;
	public static Image J;
	public static Image L;
	public static Image S;
	public static Image Z;
	public static Image O;
	public static Image T;
	static{
	try{	
		background = ImageIO.read(Tetris.class.getResource("tetris.png"));
		T = ImageIO.read(Tetris.class.getResource("T.png"));
		J= ImageIO.read(Tetris.class.getResource("J.png"));
		L = ImageIO.read(Tetris.class.getResource("L.png"));
		S = ImageIO.read(Tetris.class.getResource("S.png"));
		Z = ImageIO.read(Tetris.class.getResource("Z.png"));
		O = ImageIO.read(Tetris.class.getResource("O.png"));
		T = ImageIO.read(Tetris.class.getResource("T.png"));
		
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	//鍵盤綁定
	public void action(){
		startAction();
		repaint();
		KeyAdapter l = new KeyAdapter() {
			public void KeyPressed(KeyEvent e){
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_Q){
					System.exit(0);//退出
				}
				if(gameOver){
					if(key == KeyEvent.VK_S){
						startAction();
					}
					return;
				}
				if(pause){
					if(key == KeyEvent.VK_C){
						continueAction();
					}
					return;
				}
				
				switch(key){
				                case KeyEvent.VK_RIGHT: moveRightAction(); break;
				                case KeyEvent.VK_LEFT: moveLeftAction(); break;
				                case KeyEvent.VK_DOWN: softDropAction() ; break;
				                case KeyEvent.VK_UP: rotateRightAction() ; break;
				                 case KeyEvent.VK_Z: rotateLeftAction() ; break;
				                 case KeyEvent.VK_SPACE: hardDropAction() ; break;
				                 case KeyEvent.VK_P: pauseAction() ; break;
				                 }
				repaint();
			}
		
		};
		this.requestFocus();
		this.addKeyListener(l);
	}
	//繪圖
	public void paint(Graphics g){
		g.drawImage(background, 0, 0, null);
		g.translate(15, 15);
		paintTetromino(g);
		paintWall(g);
		paintNextOne(g);
		paintScore(g);
	}
	public static final int FONT_COLOR = 0x667799;
	public static final int FONT_SIZE = 0x20;
	
	private void paintScore(Graphics g) {
		Font f = getFont();
		Font font = new Font(f.getName(),Font.BOLD,FONT_SIZE);
		int x = 290;
		int y = 162;
		g.setColor(new Color(FONT_COLOR));
		g.setFont(font);
		String str = "SCORES:"+this.scores;
		g.drawString(str, x, y);
		y+=56;
		str = "LINES:"+this.lines;
		g.drawString(str, x, y);
		y+=56;
		str="[P]Pause";
		if(pause){str = "[C]Continue";}
		if(gameOver){
			str = "[S]Start!";
		}
		g.drawString(str, x, y);
	}
	private void paintNextOne(Graphics g) {
		Cell[] cells = nextOne.getCells();
		for(Cell c:cells){
			int x = (c.getCol()+10)*CELL_SIZE-1;
			int y = (c.getRow()+1)*CELL_SIZE-1;
			g.drawImage(c.getImage(), x, y, null);
		}
	}
	private void paintWall(Graphics g) {
		for(int row = 0; row<wall.length;row++){
			Cell[] line = wall[row];
			for(int col = 0;col<line.length;col++){
				Cell cell = line[col];
				int x = col*CELL_SIZE;
				int y = row*CELL_SIZE;
				if(cell==null){
					
				}else{
					g.drawImage(cell.getImage(), x-1, y-1, null);
				}
			}
		}
		
	}
	private void paintTetromino(Graphics g) {
		Cell[] cells = tetromino.getCells();
		for(Cell c:cells){
			int x = (c.getCol()+1)*CELL_SIZE-1;
			int y = (c.getRow()+1)*CELL_SIZE-1;
			g.drawImage(c.getImage(), x, y, null);
		}		
	}
	
	private void softDropAction() {
		if(tetrominoCanDrop()){
			tetromino.softDrop();
		}else{
			tetrominoLandToWall();
			destroyLines();
			checkGameOver();
			tetromino = nextOne;
			nextOne = Tetromino.randomTetromino();
		}
	}
	private void hardDropAction() {
		while(tetrominoCanDrop()){
			tetromino.softDrop();
		}
		tetrominoLandToWall();
		destroyLines();
		checkGameOver();
		tetromino = nextOne;
		nextOne = Tetromino.randomTetromino();	
	}
	private void destroyLines() {
		int lines = 0;
		for(int row= 0; row < wall.length;row++){
			if(fullCells(row)){
			deleteRow(row);
			lines++;
			}
		}
		this.lines += lines;
		this.scores += SCORE_TABLE[lines];
	}
	private static final int[] SCORE_TABLE={0,1,10,30,200};
	private void deleteRow(int row) {
		for(int i=row;i>=1;i--){
			System.arraycopy(wall[i-1], 0, wall[i], 0, COLS);
		}
		Arrays.fill(wall[0], null);
	}
	private boolean fullCells(int row) {
		Cell[] line = wall[row];
		for(Cell l : line){
			if(l == null){
				return false;
			}
		}
		return true;
	}
	private void checkGameOver() {
		if(wall[0][4] == null){
			return;
		}
		gameOver = true;
		timer.cancel();
		repaint();		
	}
	
	private void tetrominoLandToWall() {
		Cell[] cells = tetromino.getCells();
		for(Cell c : cells){
			int row = c.getRow();
			int col = c.getCol();
		    wall[row][col] = c;	
		}	
	}
	
	private boolean tetrominoCanDrop() {
		Cell[] cells = tetromino.getCells();
		for(Cell c : cells){
			int row = c.getRow();
			int col = c.getCol();
			if(row == ROWS-1) {return false;}
			else if(wall[row+1][col] != null){
				return false;
			}
		}
		return true;
	}
	
	private void rotateLeftAction() {
		tetromino.rotateLeft();
		if(outOfBound() || coincide()){
			tetromino.rotateRight();
		}	
	}

	private void rotateRightAction() {
		tetromino.rotateRight();
		if(outOfBound() || coincide()){
			tetromino.rotateLeft();
		}
	}
	private void moveRightAction(){
		tetromino.moveRight();
		if(outOfBound() || coincide()){
			tetromino.moveLeft();
		}
	}
	private void moveLeftAction(){
		tetromino.moveLeft();
		if(outOfBound() || coincide()){
			tetromino.moveRight();
		}
	}

	private boolean coincide() {
		Cell[] cells = tetromino.getCells();
		for(Cell c : cells){
			int row = c.getRow();
			int col = c.getCol();
			if(row<0 || row>=ROWS || col<0 || col>=COLS || wall[row][col]!=null) 
			{return true;}
		}
		return false;
	}
	private boolean outOfBound() {
		Cell[] cells = tetromino.getCells();
		for(Cell c : cells){
			int col = c.getCol();
			if(col<0 || col>=COLS){
				return true;
			}
		}
		return false;
	}
	
	private void startAction() {
		clearWall();
		tetromino = Tetromino.randomTetromino();
		nextOne = Tetromino.randomTetromino();
		lines = 0; scores = 0;
		pause = false; gameOver = false;
		timer = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				softDropAction();
				repaint();
			}
		} ;
		timer.schedule(task,700, 700);		
	}
	private void clearWall() {
		for(int r=0;r<ROWS;r++){
			Arrays.fill(wall[r], null);
		}	
	}
	private void pauseAction() {
		timer.cancel();
		pause = true;
		repaint();
	}
	private void continueAction() {
		timer = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				softDropAction();
				repaint();
			}
		} ;
		timer.schedule(task, 700, 700);
		pause = false;
		repaint();
	}
}
