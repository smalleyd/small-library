package com.small.library.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

import com.small.library.servlet.Model;

/**********************************************************************
*
*	Tag class that handles the creation of an <CODE>IMG</CODE> tag
*	on a PDD page. Performs necessary source directory substitution
*	based on personalization.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/20/2002
*
***********************************************************************/

public class ImageTag extends PageElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*******************************************************************
	*
	*	Constants
	*
	*******************************************************************/

	/** Constant - tag name. */
	public static final String TAG_NAME = "img";

	/** Constant - property name for the alternate caption attribute. */
	public static final String PROPERTY_ALTERNATE_CAPTION = "title";

	/*******************************************************************
	*
	*	PddTagSupport methods
	*
	*******************************************************************/

	/** PddTagSupport method - gets the tag name. */
	public String getTagName() { return TAG_NAME; }

	/*******************************************************************
	*
	*	TagSupport methods
	*
	*******************************************************************/

	/** TagSupport method - handles the start of the tag. */
	public int doStartTag()
		throws JspTagException
	{
		JspWriter out = pageContext.getOut();

		try
		{
			startTag();

			String source = getImagePath(getContextRoot(),
				getModel(),
				src);

			outputProperty(PROPERTY_SOURCE, source);
			outputProperty(PROPERTY_HEIGHT, height);
			outputProperty(PROPERTY_WIDTH, width);
			outputProperty(PROPERTY_BORDER, border);
			outputProperty(PROPERTY_ALTERNATE_CAPTION, alt);

			out.print(">");
		}

		catch (IOException ex) { throw new JspTagException(ex.getMessage()); }

		return SKIP_BODY;
	}

	/*******************************************************************
	*
	*	Accessor methods
	*
	*******************************************************************/
	
	/** Accessor method - gets the source of the image. */
	public String getSrc() { return src; }
	
	/** Accessor method - gets the height of the image. */
	public int getHeight() { return height; }
	
	/** Accessor method - gets the width of the image. */
	public int getWidth() { return width; }
	
	/** Accessor method - gets the border width of the image. */
	public int getBorder() { return border; }

	/** Accessor method - gets the alternate caption of the image. */
	public String getAlt() { return alt; }

	/*******************************************************************
	*
	*	Mutator methods
	*
	*******************************************************************/

	/** Mutator method - sets the source of the image. */
	public void setSrc(String newValue) { src = newValue; }

	/** Mutator method - sets the height of the image. */
	public void setHeight(int newValue) { height = newValue; }

	/** Mutator method - sets the width of the image. */
	public void setWidth(int newValue) { width = newValue; }

	/** Mutator method - sets the border width of the image. */
	public void setBorder(int newValue) { border = newValue; }

	/** Mutator method - sets the alternate caption of the image. */
	public void setAlt(String newValue) { alt = newValue; }

	/*******************************************************************
	*
	*	Helper methods
	*
	*******************************************************************/

	/** Helper method - gets the image source from the context root,
	    the page object, and the image name.
	    	@param contextRoot Application virtual directory.
	    	@param model Current requests <I>Model</I>.
	    	@param imageName File name of the image without directory
	    		information.
	*/
	public static String getImagePath(String contextRoot,
		Model model, String imageName)
	{
			// TODO: implement correctly.
			return contextRoot + "/images/" + imageName;
	}

	/*******************************************************************
	*
	*	Member variables
	*
	*******************************************************************/

	/** Member variable - contains the source of the image. */
	private String src = null;

	/** Member variable - contains the height of the image. */
	private int height = NULL_VALUE_INT;

	/** Member variable - contains the width of the image. */
	private int width = NULL_VALUE_INT;

	/** Member variable - contains the border width of the image. */
	private int border = NULL_VALUE_INT;

	/** Member variable - contains the alternate caption of the
	    image.
	*/
	private String alt = null;
}
