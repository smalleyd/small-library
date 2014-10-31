package com.small.library.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/***************************************************************************************
*
*	Extends the swing class JFrame to support more features.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 3/4/2000
*
***************************************************************************************/

public abstract class XFrame extends JFrame
{
	/******************************************************************************
	*
	* Constructors/Destructor
	*
	******************************************************************************/

	/******************************************************************************
	*
	* Main functionality
	*
	******************************************************************************/

	/** Centers the window frame on the screen.
		@param bSetSize If "bSetSize" is true the window frame's size will be
			set to half of the screen's size. Otherwise, the window frame
			maintains it's current size.
	*/
	public void Center(boolean bSetSize)
	{
		/* If the window height/width has not been set, make it half the size
		   of the screen. Do this first, because the remainder of the function
		   will need to know the size. */
		if (bSetSize) setSize();

		/* Get the dimensions of the screen and window. */
		Dimension pScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension pWindow = getSize();

		/* Center the window on the screen. */
		setLocation((pScreen.width - pWindow.width) / 2,
			(pScreen.height - pWindow.height) / 2);
	}

	/** Centers the window frame on the screen.
		@param nWidth The width in pixels to set the window frame.
		@param nHeight The height in pixels to set the window frame.
	*/
	public void Center(int nWidth, int nHeight) { setSize(nWidth, nHeight); Center(false); }

	/** Centers the window frame on the screen.
		@param pSize The width and height in pixels to set the window
			frame size.
	*/
	public void Center(Dimension pSize) { setSize(pSize); Center(false); }

	/** Accepts the name of an image file and makes the icon for the frame.
		@param strFile The name of the icon file.
	*/
	public void setIconImage(String strFile)
	{
		ImageIcon pImage = new ImageIcon(strFile);
		setIconImage(pImage.getImage());
	}

	/** Sets the size of the window frame to half the size of the screen. */
	public void setSize()
	{
		Dimension pScreen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(pScreen.width / 2, pScreen.height / 2);
	}
}
