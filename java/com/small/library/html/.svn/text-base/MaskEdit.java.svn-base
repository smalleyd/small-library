package com.small.library.html;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

/***************************************************************************************
*
*	Class that represents a series of the HTML textbox elements grouped together
*	to accept masked input. Examples are phone numbers (###-###-####) and
*	zip codes (#####-9999).
*
*	<BR><BR>
*
*	Mask Codes:
*	<UL>
*		<LI># - required numeric value.</LI>
*		<LI>9 - optional numeric value.</LI>
*		<LI>@ - required alpha-numeric value.</LI>
*		<LI>A - optional alpha-numeric value.</LI>
*	</UL>
*
*	All other characters are displayed "as is" in their supplied position.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public class MaskEdit extends FormElementGroup
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Required numeric mask character. */
	public static final char MASK_NUMERIC_REQUIRED = '#';

	/** Optional numeric mask character. */
	public static final char MASK_NUMERIC_OPTIONAL = '9';

	/** Required alpha-numeric mask character. */
	public static final char MASK_ALPHA_REQUIRED = '@';

	/** Optional alpha-numeric mask character. */
	public static final char MASK_ALPHA_OPTIONAL = 'A';

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the input element.
		@param strMask Mask representing the group of input elements.
	*/
	public MaskEdit(String strName, String strMask) { this(strName, null, null, null, strMask); }

	/** Constructor - constructs an object with a Name, a Default Value attribute.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param strMask Mask representing the group of input elements.
	*/
	public MaskEdit(String strName, String strDefaultValue, String strMask)
	{ this(strName, strDefaultValue, null, null, strMask); }

	/** Constructor - constructs an object with a Name, a Default Value attribute,
	    a Cascading Stylesheet class name, and a Cascading Stylesheet style string.
		@param strName Name of the input element.
		@param strDefaultValue Default Value attribute of the input element.
		@param strCSSClass Cascading Stylesheet class name.
		@param strCSSStyle Cascading Stylesheet style string.
		@param strMask Mask representing the group of input elements.
	*/
	public MaskEdit(String strName, String strDefaultValue,
		String strCSSClass, String strCSSStyle, String strMask)
	{
		// Pass on supplied values to parent.
		super(strName, null, strCSSClass, strCSSStyle);

		// First build an ArrayList of textboxes. Later move to array for speed.
		ArrayList m_Elements = new ArrayList();

		int nSize = 0;
		char[] cChars = strMask.toCharArray();
		String strValue = strDefaultValue;
		String strNonMaskElement = null;
		boolean bPrevMaskChar = false;	// always initialize to false

		// Parse the mask and create the textboxes.
		for (int i = 0; i < cChars.length; i++)
		{
			// Get the current character.
			char cChar = cChars[i];
			boolean bMaskChar = isMaskChar(cChar);

			if (bMaskChar)
			{
				// If the previous character was NOT a mask character
				// and non-mask characters were found add to list
				// of elements.
				if (!bPrevMaskChar && (null != strNonMaskElement))
				{
					m_Elements.add(strNonMaskElement);
					strNonMaskElement = null;
				}

				nSize++;
			}

			else
			{
				String strNonMaskChar = (new Character(cChar)).toString();

				if (bPrevMaskChar)
				{
					// create a textbox with the appropriate portion of the default
					// value.
					// Always use this member variable, so that at least
					// one reference is maintained for getting values.
					m_Textbox = new Textbox(strName, substring(strValue, 0, nSize),
						strCSSClass, strCSSStyle, nSize, nSize, null);

					m_Elements.add(m_Textbox);

					// shorten the default value to the unused remainder.
					strValue = substring(strValue, nSize);

					// Reset mask size.
					nSize = 0;

					// Start the non-mask element string.
					strNonMaskElement = strNonMaskChar;
				}

				// otherwise, append to the non-mask element string.
				else
					strNonMaskElement+= strNonMaskChar;
			}

			// Set at the end the previous mask character indicator.
			bPrevMaskChar = bMaskChar;
    		}
	}

	/******************************************************************************
	*
	*	Required methods: Input
	*
	******************************************************************************/

	/** Action method - creates all HTML elements required to represent the
	    mask.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter) throws IOException
	{
		create(pWriter, getDefaultValue());
	}

	/** Action method - creates all HTML elements required to represent the
	    mask.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param strValue Value attribute of the mask edit field.
	*/
	public void create(Writer pWriter, String strValue) throws IOException
	{
		// Loop through the list of elements and write their display.

		int nSize = m_Elements.size();
		String strInput = strValue;

		for (int i = 0; i < nSize; i++)
		{
			Object pElement = m_Elements.get(i);

			if (pElement instanceof String)
				write(pWriter, (String) pElement);
			else
			{
				Textbox pTextbox = (Textbox) pElement;
				pTextbox.create(pWriter, substring(strInput, 0, pTextbox.getSize()));
				strInput = substring(strInput, pTextbox.getSize());
			}
		}
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - gets the mask representing the group of input elements. */
	public String getMask() { return m_strMask; }

	/** Accessor method - gets a concatenated <I>String</I> value of all textboxes
	    from the HTTP request.
	*/
	public String getString() 
	{
		ArrayList pList = getStrings();

		if ((null == pList) || (0 == pList.size()))
			return null;

		String strReturn = (String) pList.get(0);

		for (int i = 1; i < pList.size(); i++)
			strReturn+= (String) pList.get(i);

		return strReturn;
	}

	/** Accessor method - gets an <I>ArrayList</I> of <I>String</I> values
	    from the HTTP request.
	*/
	public ArrayList getStrings()
	{ return m_Textbox.getStrings(); }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - sets the mask representing the group of input elements. */
	public void setMask(String strNewValue) { m_strMask = strNewValue; }

	/******************************************************************************
	*
	*	Helper methods - mask validity
	*
	******************************************************************************/

	/** Helper method - Determines the validity of the content of the MaskEdit control. */
	public boolean isValid()
	{ return isValueValid(m_strMask, getString()); }

	/** Helper method - Determines the validity a supplied <I>String</I> value
	    compared to the object's mask.
		@param strValue The string value to be compared against the mask
			for validity.
	*/
	public boolean isValueValid(String strValue) { return isValueValid(m_strMask, strValue); }

	/** Helper method - Determines the validity a supplied <I>String</I> value
	    compared to a specified mask.
		@param strMask Data-entry mask value.
		@param strValue The string value to be compared against the mask
			for validity.
	*/
	public static boolean isValueValid(String strMask, String strValue)
	{
		// Local variables.
		int nValCounter;
		char cMask;
		char cValue;
		int nLenValue;

		// Get the value in the textbox(es).
		nLenValue = strValue.length();

		// Are the supplied value valid?
		if ((0 == strMask.length()) || (0 == nLenValue))
			return true;

		// Initiative value character pointer.
		nValCounter = 0;

		// Loop through the mask.
		for (int i = 0; i < strMask.length(); i++)
		{
			// Get the individual mask character.
			cMask = strMask.charAt(i);

			// Is this a mask character?
			if (isMaskChar(cMask))
			{
				// If so, are there any characters left to validate?
				if (nValCounter < nLenValue)
				{
					// Get the next value to validate.
					cValue = strValue.charAt(nValCounter);

					// Is the current mask character a numeric mask?
					if (isNumericMask(cMask))
					{
						if (!Character.isDigit(cValue))
							return false;
					}

					// If not a numeric mask, must be an alpha mask.
					else if (!Character.isLetterOrDigit(cValue))
                        			return false;
				}

				// We've reached the end of "strValue". Is the value
				// required though.
				else if (isRequiredMask(cMask))
					return false;
                
				// If not required, but end has been reached, exit.
				else
					break;

				nValCounter++;
			}
		}

		// Indicates a successfully formatted value.
		return true;
	}

	/******************************************************************************
	*
	*	Helper methods - formatting
	*
	******************************************************************************/

	/** Helper method - Formats the value based on the object's mask.
		@param strValue Value to be formatted based on the mask.
	*/
	public String format(String strValue) { return format(m_strMask, strValue); }

	/** Helper method - Formats the value based on the mask.
		@param strMask Mask to be employed for formatting.
		@param strValue Value to be formatted based on the mask.
	*/
	public static String format(String strMask, String strValue)
	{
		// Local variables.
		char cMask;
		int nSize = 0;

		// Valid value?
		if ((null == strMask) || (null == strValue))
        		return strValue;

		// Possible return value.
		String strReturn = "";

		for (int i = 0; i < strMask.length(); i++)
		{

			if (strValue.length() <= nSize)
				return strReturn;

			cMask = strMask.charAt(i);
			if (isMaskChar(cMask))
			{
				strReturn+=(strValue.substring(nSize, nSize + 1));
				
				nSize++;
				
			}
		        else
				strReturn+=(String.valueOf(cMask));
		}

		return strReturn;
	}

	/******************************************************************************
	*
	*	Helper methods - character determination
	*
	******************************************************************************/

	/** Helper method - Does the character supplied indicate a numeric entry position.
		@param cChar The character to check.
	*/
	public static boolean isNumericMask(char cChar)
	{
		return (((MASK_NUMERIC_REQUIRED == cChar) ||
			(MASK_NUMERIC_OPTIONAL == cChar)) ? true : false);
	}

	/** Helper method - Does the character supplied indicate an alpha-numeric entry position.
		@param cChar The character to check.
	*/
	public static boolean isAlphaMask(char cChar)
	{
		return (((MASK_ALPHA_REQUIRED == cChar) ||
			(MASK_ALPHA_OPTIONAL == cChar)) ? true : false);
	}

	/** Helper method - Does the character supplied indicate a required entry position.
		@param cChar The character to check.
	*/
	public static boolean isRequiredMask(char cChar)
	{
		return (((MASK_NUMERIC_REQUIRED == cChar) ||
			(MASK_ALPHA_REQUIRED == cChar)) ? true : false);
	}

	/** Helper method - Does the character supplied indicate an optional entry position.
		@param cChar The character to check.
	*/
	public static boolean isOptionalMask(char cChar)
	{
		return (((MASK_NUMERIC_OPTIONAL == cChar) ||
			(MASK_ALPHA_OPTIONAL == cChar)) ? true : false);
	}

	/** Helper method - Does the character supplied indicate an entry position.
		@param cChar The character to check.
	*/
	public static boolean isMaskChar(char cChar)
	{
		return (isNumericMask(cChar) || isAlphaMask(cChar));
	}

	/******************************************************************************
	*
	*	Helper methods - <I>String</I> handling.
	*
	******************************************************************************/

	/** Helper method - Assists with the handling of string "substringing".
	    Catches any exceptions and returns an empty <I>String</I> on error.
		@param strValue The string to be substringed.
		@param nBegin The first character in the new substring (zero based).
		@param nEnd The last character in the new substring (1 based).
	*/
	private static String substring(String strValue, int nBegin, int nEnd)
	{
		if (null == strValue)
			return null;

		try { return strValue.substring(nBegin, nEnd); }
		catch (Exception ex) { return ""; }
	}

	/** Helper method - Assists with the handling of string "substringing".
	    Catches any exceptions and returns an empty <I>String</I> on error.
		@param strValue The string to be substringed.
		@param nBegin The first character in the new substring (zero based).
	*/
	private static String substring(String strValue, int nBegin)
	{
		if (null == strValue)
			return null;

		try { return strValue.substring(nBegin); }
		catch (Exception ex) { return ""; }
	}

	/******************************************************************************
	*
	*	Member variables
	*
	******************************************************************************/

	/** Member variable - contains the <I>ArrayList</I> of elements used to display
	    the mask edit.
	*/
	private ArrayList m_Elements = null;

	/** Member variable - contains one of the references to a textbox element contained
	    in the element <I>ArrayList</I>. Used to get values.
	*/
	private Textbox m_Textbox = null;

	/** Member variable - contains the mask representing the group of input elements. */
	private String m_strMask = null;
}
