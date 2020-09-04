import javax.swing.JFrame;

public class Main extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static int WIDTH = 5;		//The number of buttons WIDE that your display is.
	final static int HEIGHT = 5;		//The number of buttons HIGH that your display is.
	final static int NUM_MINES = 6;		//The number of MINES to create.
	public BoardSquareButton buttons[][] = new BoardSquareButton[HEIGHT][WIDTH];

	public Main()
	{
		Board frame = new Board();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Minesweeper");
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Main();
	}	
}
