package com.small.library.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

/*********************************************************************
*
*	Class that represents the Anchor (A) tag. Useful for personalizing
*	image mouse overs.
*
*	@author David Small
*	@version 2.0.0.0
*	@date 11/22/2002
*
*********************************************************************/

public class AnchorTag extends PageElement
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/*****************************************************************
	*
	*	Constants
	*
	*****************************************************************/

	/** Constant - tag name. */
	public static final String TAG_NAME = "a";

	/** Constant - end tag. */
	public static final String END_TAG = "</" + TAG_NAME + ">";

	/** Constant - property name for the onMouseOver attribute. */
	public static final String PROPERTY_ON_MOUSE_OVER = "onmouseover";

	/** Constant - property name for the onMouseOut attribute. */
	public static final String PROPERTY_ON_MOUSE_OUT = "onmouseout";

	/*******************************************************************
	*
	*	PddTagSupport methods
	*
	*******************************************************************/

	/** PddTagSupport method - gets the tag name. */
	public String getTagName() { return TAG_NAME; }

	/*****************************************************************
	*
	*	TagSupport methods
	*
	*****************************************************************/

	/** TagSupport method - handles the start of the tag. */
	public int doStartTag()
		throws JspTagException
	{
		// Validate the tag.
		validate();

		// Get the writer.
		JspWriter out = pageContext.getOut();

		try
		{
			startTag();

			outputProperty(PROPERTY_HREF, href);
			outputProperty(PROPERTY_ON_CLICK, onClick);

			// If the image properties exist, supply
			// specific functionality.
			if ((null != imageOverSrc) && (null != imageName))
			{
				String imageOverSrcPath = ImageTag.getImagePath(getContextRoot(),
					getModel(), imageOverSrc);

				outputProperty(PROPERTY_ON_MOUSE_OVER, "MM_swapImage('" + imageName + "','','" + imageOverSrcPath + "',1);");
				outputProperty(PROPERTY_ON_MOUSE_OUT, "MM_swapImgRestore();");
			}
			
			else
			{
				outputProperty(PROPERTY_ON_MOUSE_OVER, onMouseOver);
				outputProperty(PROPERTY_ON_MOUSE_OUT, onMouseOut);
			}

			out.print(">");
		}

		catch (IOException ex) { throw new JspTagException(ex.getMessage()); }

		return EVAL_BODY_INCLUDE;
	}

	/** TagSupport method - handles the end tag. */
	public int doEndTag()
		throws JspTagException
	{
		try { pageContext.getOut().print(END_TAG); }
		catch (IOException ex) { throw new JspTagException(ex.getMessage()); }

		return EVAL_PAGE;
	}

	/*****************************************************************
	*
	*	Helper methods
	*
	*****************************************************************/

	/** Helper method - validates the attributes. */
	protected void validate()
		throws JspTagException
	{
		if ((null == name) && (null == href) && (null == onClick))
			throw new JspTagException("Anchor Tag Exception: Please provide " +
				"either a name, an HREF, or an onClick event.");
	}

	/*****************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************/

	/** Accessor method - gets the HREF of the anchor. */
	public String getHref() { return href; }

	/** Accessor method - gets the click envet of the anchor. */
	public String getOnClick() { return onClick; }

	/** Accessor method - gets the mouse over event of the anchor. */
	public String getOnMouseOver() { return onMouseOver; }

	/** Accessor method - gets the mouse out event of the anchor. */
	public String getOnMouseOut() { return onMouseOut; }

	/** Accessor method - gets the image over source of the anchor. */
	public String getImageOverSrc() { return imageOverSrc; }

	/** Accessor method - gets the image name of the anchor. */
	public String getImageName() { return imageName; }

	/*****************************************************************
	*
	*	Mutator methods
	*
	*****************************************************************/

	/** Mutator method - sets the HREF of the anchor. */
	public void setHref(String newValue) { href = newValue; }

	/** Mutator method - sets the click event of the anchor. */
	public void setOnClick(String newValue) { onClick = newValue; }

	/** Mutator method - sets the mouse over event of the anchor. */
	public void setOnMouseOver(String newValue) { onMouseOver = newValue; }

	/** Mutator method - sets the mouse out event of the anchor. */
	public void setOnMouseOut(String newValue) { onMouseOut = newValue; }

	/** Mutator method - sets the image over source of the anchor. */
	public void setImageOverSrc(String newValue) { imageOverSrc = newValue; }

	/** Mutator method - sets the image name of the anchor. */
	public void setImageName(String newValue) { imageName = newValue; }

	/*****************************************************************
	*
	*	Member variables
	*
	*****************************************************************/

	/** Member variable - contains the HREF of the anchor. */
	private String href = null;

	/** Member variable - contains the click event of the anchor. */
	private String onClick = null;

	/** Member variable - contains the mouse over event of the anchor. */
	private String onMouseOver = null;

	/** Member variable - contains the mouse out event of the anchor. */
	private String onMouseOut = null;

	/** Member variable - contains the image over source of the anchor. */
	private String imageOverSrc = null;

	/** Member variable - contains the image name of the anchor. */
	private String imageName = null;
}
