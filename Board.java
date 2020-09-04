import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JFrame implements MouseListener
{
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	int i, j;
	public BoardSquareButton buttons[][] = new BoardSquareButton[Main.HEIGHT][Main.WIDTH];
	public boolean started = false;
	public boolean finished = false;
	public int remMines = Main.NUM_MINES;
	public int remSquares = Main.HEIGHT * Main.WIDTH - remMines;
	public JPanel board;

	Board()
	{
		getContentPane().setLayout( new BorderLayout() );
		
		initialise();
	}
	
	public void initialise()
	{
		board = new JPanel( new GridLayout( Main.HEIGHT, Main.WIDTH, 1, 1 ) );
		
		for (i=0; i<Main.HEIGHT; i++)
		{
			for (j=0; j<Main.WIDTH; j++)
			{
				buttons[i][j] = new BoardSquareButton(60, 60, Color.gray, i, j );
				buttons[i][j].addMouseListener( this );
				buttons[i][j].setText("?");
				buttons[i][j].setFont(new Font("", Font.BOLD, 30));
				buttons[i][j].setBackground(Color.gray);
				board.add( buttons[i][j] );
			}		
		}

		getContentPane().add( board, BorderLayout.CENTER );
	}

	public void initialiseAll()
	{
		board.removeAll();
		board.setLayout( new GridLayout( Main.HEIGHT, Main.WIDTH, 1, 1 ) );

		for (i=0; i<Main.HEIGHT; i++)
		{
			for (j=0; j<Main.WIDTH; j++)
			{
				buttons[i][j] = new BoardSquareButton(60, 60, Color.gray, i, j );
				buttons[i][j].addMouseListener( this );
				board.add( buttons[i][j] );
				//buttons[i][j].setBackground(Color.gray);
				buttons[i][j].initialise();
			}
		}
		
		started = false;
		finished = false;
		remMines = Main.NUM_MINES;
		remSquares = Main.HEIGHT * Main.WIDTH - remMines;
	}

	public void createMines()
	{	
		Random randInt = new Random();
		int randCol, randRow;

		// Alter these two parameters if error (Debug)
		for (i=0; i<Main.NUM_MINES; i++)
		{
			randCol = randInt.nextInt(Main.HEIGHT);
			randRow = randInt.nextInt(Main.WIDTH);
			
			// Check to not put more than one mine on a square
			if(!buttons[randCol][randRow].isMine())
			{
				buttons[randCol][randRow].setMine();
			}
			else
			{
				i--;
			}
		}

		// Marking the neighbouring mines of each square
		for (i=0; i<Main.HEIGHT; i++)
		{
			for (j=0; j<Main.WIDTH; j++)
			{
				if(buttons[i][j].isMine() )
				{
					//Upper left
					if (i-1 >= 0 && j-1 >= 0)
					{
						buttons[i-1][j-1].setNeighbours(1);
					}
					
					//Upper middle
					if (i-1 >= 0 && j >= 0)
					{
						buttons[i-1][j].setNeighbours(1);
					}
					
					//Upper right
					if (i-1 >= 0 && j+1 < Main.WIDTH)
					{
						buttons[i-1][j+1].setNeighbours(1);
					}
					
					//Middle left
					if (i >= 0 && j-1 >= 0)
					{
						buttons[i][j-1].setNeighbours(1);
					}
					
					//Middle right
					if (i >= 0 && j+1 < Main.WIDTH)
					{
						buttons[i][j+1].setNeighbours(1);
					}
					
					//Bottom left
					if (i+1 < Main.HEIGHT && j-1 >= 0)
					{
						buttons[i+1][j-1].setNeighbours(1);
					}
					
					//Bottom middle
					if (i+1 < Main.HEIGHT && j >= 0)
					{
						buttons[i+1][j].setNeighbours(1);
					}
					
					//Bottom right
					if (i+1 < Main.HEIGHT && j+1 < Main.WIDTH)
					{
						buttons[i+1][j+1].setNeighbours(1);
					}
				}
			}
		}
  	}
	
	//Shows empty button
	public void showEmpty( BoardSquareButton empty )
	{
		int posX = empty.getXpos();
		int posY = empty.getYpos();

		checkEmpty(posX, posY);
		return;
	}

	//Checks if button is empty
	public void checkEmpty(int x, int y)
	{
		if( x < Main.HEIGHT && y < Main.WIDTH && x >= 0 && y >= 0 && !buttons[x][y].isInvestigated() )
		{
			buttons[x][y].click();
			remSquares--;

			if(buttons[x][y].getNeighbours() == 0)
			{
				buttons[x][y].setText("0");
				checkEmpty(x-1, y-1);
				checkEmpty(x, 	y-1);
				checkEmpty(x+1, y-1);
				checkEmpty(x-1, y);
				checkEmpty(x+1, y);
				checkEmpty(x-1, y+1);
				checkEmpty(x, 	y+1);
				checkEmpty(x+1, y+1);
			}
		}
	}
	
	public void hasWon(boolean won)
	{
		finished = true;

		// Show all mines and Disable all buttons
		for (int i=0; i<Main.HEIGHT; i++)
		{
			for (int j=0; j<Main.WIDTH; j++)
			{
				buttons[i][j].setText(Integer.toString(buttons[i][j].getNeighbours()));
				buttons[i][j].setEnabled(false);
				
				// If Lost
				if(!won)
				{					
					// Show wrong flagged
					if( buttons[i][j].isPotential() == 1 )
					{
						buttons[i][j].setText(Integer.toString(buttons[i][j].getNeighbours()));
					}

					//Show mines
					if(buttons[i][j].isMine())
					{
						buttons[i][j].setText("X");
						buttons[i][j].setBackground(Color.yellow);
						
						if (buttons[i][j].isPotential() == 1)
						{
							buttons[i][j].setBackground(Color.red);
						}
					}
				}

				//If Won
				if(won)
				{
					// Show mines
					if(buttons[i][j].isMine() || buttons[i][j].isPotential() == 1)
					{
						buttons[i][j].setText("X");
						buttons[i][j].setBackground(Color.red);
					}
				}
			}
		}
		
		if (!won)
		{
			JOptionPane.showMessageDialog(board, "You lost!");
			initialiseAll();
			return;
		}
		
		if (won)
		{
			JOptionPane.showMessageDialog(board, "You won!");
			initialiseAll();
			return;
		}
	}
	
	

	public void mousePressed(MouseEvent e)
	{}

	public void mouseReleased(MouseEvent e)
	{}

	public void mouseEntered(MouseEvent e)
	{}

	public void mouseExited(MouseEvent e)
	{}

	public void mouseClicked(MouseEvent e)
	{
		int button = e.getButton();

		BoardSquareButton square = (BoardSquareButton)e.getSource();

		// If left button clicked
		if( button == 1 && !finished )
		{
			// If clicked button is mine
			if(square.isMine() && square.isPotential() != 1)
			{
				hasWon(false);				
				return;
			}
			
			if(!started)
			{
				createMines();
				started = true;
			}

			if(!square.isInvestigated() && square.isPotential() != 1)
			{			
				if(square.getNeighbours() == 0)
				{
					showEmpty(square);
				}
				
				else
				{
					remSquares--;
				}
			}
			
			// Telling button that it's clicked
			square.click();
			
		}
		// If right button clicked
		else if( button == 3 )
		{
			remMines += square.flagged();
		}

		// If no empty squares left, game won
		if( remSquares == 0 )
		{
			finished = true;
			hasWon(true);
		}
	}
}