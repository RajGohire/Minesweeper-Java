import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class BoardSquareButton extends JButton
{
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	boolean mine = false;			//Whether the button is a mine
	int neighbourMines = 0;			//Whether the button has been investigated (green with adjacent numbers/grey with "?")
	boolean investigated = false;	//Whether the button has been right-clicked (red)
	int potential = 0;
	int x, y;
	
	public BoardSquareButton(int width, int height, Color initialCol, int X, int Y)
	{
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(initialCol);
		x = X;
		y = Y;
	}

	public void initialise()
	{
		this.setText("?");
		this.setFont(new Font("", Font.BOLD, 30));
		this.setBackground(Color.gray);
		investigated = false;
		potential = 0;
		neighbourMines = 0;
		mine = false;
		setEnabled(true);
	}
	
	public int getNeighbours()
	{
		return neighbourMines;
	}
	
	public void setNeighbours(int number)
	{
		neighbourMines += number;
	}

	public boolean isMine()
	{
		return mine;
	}

	public void setMine()
	{
		mine = true;
	}

	public boolean isInvestigated()
	{
		return investigated;
	}
	
	public int flagged()
	{
		if( !investigated )
		{
			switch( potential )
			{
				// Square is flagged
				case 0:
					potential = 1;
					setEnabled( false );
					this.setText("?");
					this.setBackground(Color.red);
					return -1;

				// Clearing the flag
				case 1:
					potential = 0;
					setEnabled(true);
					this.setText("?");
					this.setBackground(Color.gray);
					return 0;

				default:
					break;
			}
		}
		return 0;
	}

	public int isPotential()
	{
		return potential;
	}
	
	public int getXpos()
	{ 
		return x;
	}

	public int getYpos()
	{
		return y;
	}

	public void click()
	{
		this.setBackground(Color.green);
		
		if( potential == 1)
		{
			this.setBackground(Color.red);
		}
		else
		{
			investigated = true;

			if( neighbourMines > 0 )
				this.setText(Integer.toString(getNeighbours()));
		}
	}
}