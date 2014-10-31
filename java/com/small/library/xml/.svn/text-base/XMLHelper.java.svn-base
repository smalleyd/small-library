package com.small.library.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.*;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.xerces.parsers.DOMParser;

/***********************************************************************************
*
*	Provides general, stateless XML helper methods. All methods are static,
*	as no state is required.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 5/10/2001
*
***********************************************************************************/

public class XMLHelper
{
	/***************************************************************************
	*
	*	Constants
	*
	***************************************************************************/

	/** Constant - boolean <I>String</I> representation of <CODE>true</CODE>. */
	public static final String ATTRIBUTE_VALUE_TRUE_STRING = "True";

	/** Constant - boolean numeric representation of <CODE>true</CODE>. */
	public static final int ATTRIBUTE_VALUE_TRUE_INT = 1;

	/***************************************************************************
	*
	*	Helper methods - node traversal
	*
	***************************************************************************/

	/** Helper method - creates an XML DOM document object from a <I>String</I>.
		@param strDocument XML document.
	*/
	public static Document createDocument(String strDocument)
		throws SAXException, IOException
	{
		DOMParser pParser = new DOMParser();

		// Continue after fatal error. Phoenix does not wrap "ampersands" in "&amp" notation.
		pParser.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);

		InputSource pInputSource = new InputSource(new ByteArrayInputStream(strDocument.getBytes()));

		pParser.parse(pInputSource);

		return pParser.getDocument();
	}

	/** Helper method - gets the local name of the node without the namespace prefix.
	    The method checks to be sure that the DOM 2 interface is implemented otherwise
	    an exception is thrown when the method is called.
		@param pNode A <I>org.w3c.dom.Node</I> object.
		@return the local name.
	*/
	public static String getLocalName(Node pNode)
	{
		try { return pNode.getLocalName(); }
		catch (NoSuchMethodError pEx) { return pNode.getNodeName(); }
	}

	/** Helper method - finds the node with the specified element name. The returned node can be
	    the passed in node itself or one of its child nodes. The first node found is the
	    one returned.
		@param pNode A <I>org.w3c.dom.Node</I> object being searched.
		@param strElementName Name of the element being searched for.
		@return <I>org.w3c.dom.Node</I> object.
	*/
	public static Node findNode(Node pNode, String strElementName)
	{
		// Check the parent, first.
		if (pNode.getNodeName().equals(strElementName))
			return pNode;

		NodeList pNodeList = null;

		if (pNode instanceof Element)
			pNodeList = ((Element) pNode).getElementsByTagName(strElementName);

		else if (pNode instanceof Document)
			pNodeList = ((Document) pNode).getElementsByTagName(strElementName);

		if ((null == pNodeList) || (0 == pNodeList.getLength()))
			return null;

		return pNodeList.item(0);
	}

	/** Helper method - gets the text value of an element node. The text value
	    is stored in the child nodes.
		@param pNode A org.w3c.dom.Node object.
		@return Element's text.
	*/
	public static String getNodeText(Node pNode)
	{
		NodeList pChildren = pNode.getChildNodes();
		int nLength = pChildren.getLength();

		if (0 == nLength)
			return null;

		String strReturn = "";

		for (int i = 0; i < nLength; i++)
		{
			Node pChild = pChildren.item(i);

			if (pChild instanceof CharacterData)
				strReturn+= ((CharacterData) pChild).getData();
		}

		if (0 == strReturn.length())
			return null;

		return strReturn;
	}

	/** Helper method - finds the first matching child node and returns the element's text
	    data. The method performs the equivalent of calling <CODE>findNode</CODE> and
	    <CODE>getNodeText</CODE>. If the node is not found, the method returns
	    <CODE>null</CODE>.
		@param pNode A <I>org.w3c.dom.Node</I> object being searched.
		@param strElementName Name of the element being searched for.
		@return The text data of the found node. If the node is not found, the method
			returns <CODE>null</CODE>.
	*/
	public static String findNodeText(Node pNode, String strElementName)
	{
		Node pNodeSearch = findNode(pNode, strElementName);

		if (null == pNodeSearch)
			return  null;

		return getNodeText(pNodeSearch);
	}

	/** Helper method - Converts an XML <I>Element</I> to a <I>String</I> object.
		@param pElement An <I>org.w3c.dom.Element</I> object to be serialized to a <I>String</I>.
		@return <I>String</I> representation of the <I>Element</I>.
	*/
	public static String convertElementToString(Element pElement)
	{
		// Local variables.
		int nCount = 0;

		// Start the element.
		String strName = pElement.getNodeName();
		String strReturn = "<" + strName;

		// Get the list of attributes.
		NamedNodeMap pNodeMap = pElement.getAttributes();
		nCount = pNodeMap.getLength();

		// Build the attributes.
		for (int i = 0; i < nCount; i++)
		{
			Attr pAttr = (Attr) pNodeMap.item(i);
			strReturn+= " " + pAttr.getNodeName() + "=\"" + pAttr.getNodeValue() + "\"";
		}

		// Get the list of children.
		NodeList pChildren = pElement.getChildNodes();
		nCount = pChildren.getLength();

		// If no children exist, return a single node.
		if (0 == nCount)
			return strReturn + " />";

		// Close node.
		strReturn+= ">";

		// Build out the children nodes.
		for (int i = 0; i < nCount; i++)
		{
			Node pChild = pChildren.item(i);

			if (pChild instanceof CharacterData)
				strReturn+= ((CharacterData) pChild).getData();
			else if (pChild instanceof Element)
				strReturn+= convertElementToString((Element) pChild);
		}

		return strReturn + "</" + strName + ">";
	}

	/** Helper method - Converts an XML <I>Element</I> to a <I>String</I> object, but
	    excludes the top level (wrapper) Element from the returned <I>String</I>.
		@param pElement An <I>org.w3c.dom.Element</I> object who's children are
			to be serialized to a <I>String</I>.
		@return <I>String</I> representation of the <I>Element</I>'s children.
	*/
	public static String convertElementChildrenToString(Element pElement)
	{
		String strReturn = "";
		NodeList pChildren = pElement.getChildNodes();
		int nCount = pChildren.getLength();

		for (int i = 0; i < nCount; i++)
		{
			Node pChild = pChildren.item(i);

			if (pChild instanceof Element)
				strReturn+= convertElementToString((Element) pChild);
		}

		return strReturn;
	}

	/** Helper method - Determines if a <I>Node</I> has any non-<I>CharacterData</I> nodes.
	    This call is stricter than the <CODE>Node.hasChildNodes</CODE> call.
	*/
	public static boolean hasNonCharacterChildren(Element pElement)
	{
		// Do the quick test.
		if (!pElement.hasChildNodes())
			return false;

		NodeList pNodeList = pElement.getChildNodes();
		int nLength = pNodeList.getLength();

		// Find a non-character data node.
		for (int i = 0; i < nLength; i++)
			if (!(pNodeList.item(i) instanceof CharacterData))
				return true;

		return false;
	}

	/***************************************************************************
	*
	*	Helper methods - attribute retrieval
	*
	***************************************************************************/

	/** Helper methods - gets the <I>Attr</I> <I>Node</I> object from a
	    <I>NamedNodeMap</I> of attributes.
		@param pNodeMap The <I>NamedNodeMap</I>.
		@param strName Name of the attribute.
		@return An <I>Attr</I> object.
	*/
	public static Attr getAttribute(NamedNodeMap pNodeMap, String strName)
		throws ClassCastException
	{
		Node pReturn = pNodeMap.getNamedItem(strName);

		if ((null == pReturn) || (pReturn instanceof Attr))
			return (Attr) pReturn;

		throw new ClassCastException("The node retrieved from the NamedNodeMap is not an Attr object.");
	}

	/** Helper method - gets a named attribute's value as a <I>String</I>.
		@param pNodeMap The <I>NamedNodeMap</I>.
		@param strName Name of the attribute.
		@return Value of the attribute.
	*/
	public static String getAttributeString(NamedNodeMap pNodeMap, String strName)
		throws ClassCastException
	{
		Attr pAttr = getAttribute(pNodeMap, strName);

		if (null == pAttr)
			return null;

		return pAttr.getValue();
	}

	/** Helper method - gets a named attribute's value as an <I>int</I>.
		@param pNodeMap The <I>NamedNodeMap</I>.
		@param strName Name of the attribute.
		@return Value of the attribute.
	*/
	public static int getAttributeInt(NamedNodeMap pNodeMap, String strName)
		throws ClassCastException, NumberFormatException
	{
		String strValue = getAttributeString(pNodeMap, strName);

		if (null == strValue)
			return Integer.MIN_VALUE;

		return Integer.parseInt(strValue);
	}

	/** Helper method - gets a named attribute's value as a <I>long</I>.
		@param pNodeMap The <I>NamedNodeMap</I>.
		@param strName Name of the attribute.
		@return Value of the attribute.
	*/
	public static long getAttributeLong(NamedNodeMap pNodeMap, String strName)
		throws ClassCastException, NumberFormatException
	{
		String strValue = getAttributeString(pNodeMap, strName);

		if (null == strValue)
			return Long.MIN_VALUE;

		return Long.parseLong(strValue);
	}

	/** Helper method - gets a named attribute's value as a <I>short</I>.
		@param pNodeMap The <I>NamedNodeMap</I>.
		@param strName Name of the attribute.
		@return Value of the attribute.
	*/
	public static short getAttributeShort(NamedNodeMap pNodeMap, String strName)
		throws ClassCastException, NumberFormatException
	{
		int nReturn = getAttributeInt(pNodeMap, strName);

		if (Integer.MIN_VALUE == nReturn)
			return Short.MIN_VALUE;

		return (short) nReturn;
	}

	/** Helper method - gets a named attribute's value as a <I>Byte</I>.
		@param pNodeMap The <I>NamedNodeMap</I>.
		@param strName Name of the attribute.
		@return Value of the attribute.
	*/
	public static byte getAttributeByte(NamedNodeMap pNodeMap, String strName)
		throws ClassCastException, NumberFormatException
	{
		int nReturn = getAttributeInt(pNodeMap, strName);

		if (Integer.MIN_VALUE == nReturn)
			return Byte.MIN_VALUE;

		return (byte) nReturn;
	}


	/** Helper method - gets a named attribute's value as a <I>boolean</I>.
		@param pNodeMap The <I>NamedNodeMap</I>.
		@param strName Name of the attribute.
		@return Value of the attribute.
	*/
	public static boolean getAttributeBoolean(NamedNodeMap pNodeMap, String strName)
		throws ClassCastException, NumberFormatException
	{ return ATTRIBUTE_VALUE_TRUE_STRING.equals(getAttributeString(pNodeMap, strName)); }

	/** Helper method - gets a named attribute's value as a <I>float</I>.
		@param pNodeMap The <I>NamedNodeMap</I>.
		@param strName Name of the attribute.
		@return Value of the attribute.
	*/
	public static float getAttributeFloat(NamedNodeMap pNodeMap, String strName)
		throws ClassCastException, NumberFormatException
	{
		double dblReturn = getAttributeDouble(pNodeMap, strName);

		if (Double.NaN == dblReturn)
			return Float.NaN;

		return (float) dblReturn;
	}

	/** Helper method - gets a named attribute's value as a <I>double</I>.
		@param pNodeMap The <I>NamedNodeMap</I>.
		@param strName Name of the attribute.
		@return Value of the attribute.
	*/
	public static double getAttributeDouble(NamedNodeMap pNodeMap, String strName)
		throws ClassCastException, NumberFormatException
	{
		String strValue = getAttributeString(pNodeMap, strName);

		if (null == strValue)
			return Double.NaN;

		return Double.parseDouble(strValue);
	}

	/** Helper method - gets a named attribute's value as a <I>Date</I> object.
		@param pNodeMap The <I>NamedNodeMap</I>.
		@param strName Name of the attribute.
		@return Value of the attribute.
	*/
	public static Date getAttributeDate(NamedNodeMap pNodeMap, String strName)
		throws ClassCastException, NumberFormatException, IllegalArgumentException
	{ return getDateFromXMLString(getAttributeString(pNodeMap, strName)); }

	/***************************************************************************
	*
	*	Helper methods - element data retrieval
	*
	***************************************************************************/

	/** Helper method - gets a named element's value as a <I>String</I>.
		@param pElement The parent <I>Element</I>.
		@param strName Name of the element.
		@return Value of the element.
	*/
	public static String getElementString(Element pElement, String strName)
		throws ClassCastException
	{
		Node pNodeValue = findNode(pElement, strName);

		if (null == pNodeValue)
			return null;

		return getNodeText(pNodeValue);
	}

	/** Helper method - gets a named element's value as an <I>int</I>.
		@param pElement The parent <I>Element</I>.
		@param strName Name of the element.
		@return Value of the element.
	*/
	public static int getElementInt(Element pElement, String strName)
		throws ClassCastException, NumberFormatException
	{
		String strValue = getElementString(pElement, strName);

		if (null == strValue)
			return Integer.MIN_VALUE;

		return Integer.parseInt(strValue);
	}

	/** Helper method - gets a named element's value as a <I>long</I>.
		@param pElement The parent <I>Element</I>.
		@param strName Name of the element.
		@return Value of the element.
	*/
	public static long getElementLong(Element pElement, String strName)
		throws ClassCastException, NumberFormatException
	{
		String strValue = getElementString(pElement, strName);

		if (null == strValue)
			return Long.MIN_VALUE;

		return Long.parseLong(strValue);
	}

	/** Helper method - gets a named element's value as a <I>short</I>.
		@param pElement The parent <I>Element</I>.
		@param strName Name of the element.
		@return Value of the element.
	*/
	public static short getElementShort(Element pElement, String strName)
		throws ClassCastException, NumberFormatException
	{
		int nReturn = getElementInt(pElement, strName);

		if (Integer.MIN_VALUE == nReturn)
			return Short.MIN_VALUE;

		return (short) nReturn;
	}


	/** Helper method - gets a named element's value as a <I>Byte</I>.
		@param pElement The parent <I>Element</I>.
		@param strName Name of the element.
		@return Value of the element.
	*/
	public static byte getElementByte(Element pElement, String strName)
		throws ClassCastException, NumberFormatException
	{
		int nReturn = getElementInt(pElement, strName);

		if (Integer.MIN_VALUE == nReturn)
			return Byte.MIN_VALUE;

		return (byte) nReturn;
	}

	/** Helper method - gets a named element's value as a <I>boolean</I>.
		@param pElement The parent <I>Element</I>.
		@param strName Name of the element.
		@return Value of the element.
	*/
	public static boolean getElementBoolean(Element pElement, String strName)
		throws ClassCastException, NumberFormatException
	{ return (ATTRIBUTE_VALUE_TRUE_INT == getElementInt(pElement, strName)) ? true : false; }

	/** Helper method - gets a named element's value as a <I>float</I>.
		@param pElement The parent <I>Element</I>.
		@param strName Name of the element.
		@return Value of the element.
	*/
	public static float getElementFloat(Element pElement, String strName)
		throws ClassCastException, NumberFormatException
	{
		double dblReturn = getElementDouble(pElement, strName);

		if (Double.NaN == dblReturn)
			return Float.NaN;

		return (float) dblReturn;
	}


	/** Helper method - gets a named element's value as a <I>double</I>.
		@param pElement The parent <I>Element</I>.
		@param strName Name of the element.
		@return Value of the element.
	*/
	public static double getElementDouble(Element pElement, String strName)
		throws ClassCastException, NumberFormatException
	{
		String strValue = getElementString(pElement, strName);

		if (null == strValue)
			return Double.NaN;

		return Double.parseDouble(strValue);
	}

	/** Helper method - gets a named element's value as a <I>Date</I> object.
		@param pElement The parent <I>Element</I>.
		@param strName Name of the element.
		@return Value of the element.
	*/
	public static Date getElementDate(Element pElement, String strName)
		throws ClassCastException, NumberFormatException, IllegalArgumentException
	{ return getDateFromXMLString(getElementString(pElement, strName)); }

	/** Helper method - gets a named element's values as an array of <I>String</I>
	    objects.
		@param pElement The parent <I>Element</I>.
		@param strName Name of the element.
		@return Array of <I>String</I> objects representing the values
			of the child elements.
	*/
	public static String[] getElementStrings(Element pElement, String strName)
	{
		NodeList pNodeList = pElement.getElementsByTagName(strName);

		if (null == pNodeList)
			return null;

		int nLength = pNodeList.getLength();

		if (0 == nLength)
			return null;

		String[] strReturn = new String[nLength];

		for (int i = 0; i < nLength; i++)
			strReturn[i] = getNodeText(pNodeList.item(i));

		return strReturn;
	}

	/***************************************************************************
	*
	*	Helper methods - conversion
	*
	***************************************************************************/

	/** Helper method - creates a <I>Date</I> object from the date string
	    found in Phoenix XML documents.
		@param strXMLDate Date <I>String</I> from an XML document that
			follows the format "YYYY-MM-DDTHH:NN:SS.00000".
		@return <I>Date</I> object.
	*/
	public static Date getDateFromXMLString(String strXMLDate)
		throws IllegalArgumentException
	{
		if (null == strXMLDate)
			return null;

		int nLength = strXMLDate.length();

		if (10 > nLength)
			throw new IllegalArgumentException("XML date value (" + strXMLDate + ") is not of the expected format (yyyy-mm-ddThh:nn:ss).");

		// Create date object from timestamp string.
		try
		{
			Calendar pCalendar = Calendar.getInstance();

			pCalendar.clear();

			int nHour = 0;
			int nMinute = 0;
			int nSecond = 0;

			if (13 <= nLength)
				nHour = Integer.parseInt(strXMLDate.substring(11, 13));

			if (16 <= nLength)
				nMinute = Integer.parseInt(strXMLDate.substring(14, 16));

			if (19 <= nLength)
				nSecond = Integer.parseInt(strXMLDate.substring(17, 19));

			pCalendar.set(Integer.parseInt(strXMLDate.substring(0, 4)),
				Integer.parseInt(strXMLDate.substring(5, 7)) - 1,
				Integer.parseInt(strXMLDate.substring(8, 10)),
				nHour, nMinute, nSecond);

			return pCalendar.getTime();
		}

		catch (NumberFormatException pEx)
		{ throw new IllegalArgumentException("XML date value (" + strXMLDate + ") is not of the expected format (yyyy-mm-ddThh:nn:ss)."); }
	}

	/** Helper method - creates a Phoenix date <I>String</I> from a Date object.
		@param dteValue <I>Date</I> object to be converted.
		@return A <I>String</I> that follows the format "YYYY-MM-DDTHH:NN:SS.00000".
	*/
	public static String getXMLStringFromDate(Date dteValue)
	{
		if (null == dteValue)
			return null;

		Calendar pCalendar = Calendar.getInstance();

		pCalendar.setTime(dteValue);

		// Return the String value.
		// Special treatment for the month because the current implementation
		// values January as 0. Code below should work if that ever changes.
		return pCalendar.get(Calendar.YEAR) + "-" +
			padNumber(pCalendar.get(Calendar.MONTH) + 1 - Calendar.JANUARY) + "-" +
			padNumber(pCalendar.get(Calendar.DAY_OF_MONTH)) + "T" +
			padNumber(pCalendar.get(Calendar.HOUR_OF_DAY)) + ":" +
			padNumber(pCalendar.get(Calendar.MINUTE)) + ":" +
			padNumber(pCalendar.get(Calendar.SECOND));
	}

	/** Helper method - Pads single digit values with a leading zero. */
	private static String padNumber(int nValue)
	{
		if (10 <= nValue)
			return "" + nValue;

		return "0" + nValue;
	}

	/***************************************************************************
	*
	*	Driver method
	*
	***************************************************************************/

	/** Display a list of the features/properties recognized. */
	public static void main(String[] strArgs)
	{
		try
		{
			DOMParser pParser = new DOMParser();

			// Handle a file request.
			String strFile = null;

			if (0 < strArgs.length)
			{
				strFile = "file://" + strArgs[0];
				pParser.parse(strFile);
				// Document pDocTest = pParser.getDocument();
			}

			/** NOT SUPPORTED *******

			// Show list of features.
			String[] strFeatures = pParser.getFeaturesRecognized();

			System.out.println("Features:");
			for (int i = 0; i < strFeatures.length; i++)
				System.out.println("\t" + strFeatures[i]);

			// Show list of properties.
			String[] strProperties = pParser.getPropertiesRecognized();

			System.out.println();
			System.out.println("Properties:");
			for (int i = 0; i < strProperties.length; i++)
				System.out.println("\t" + strProperties[i]);

			**/

			// Parse document with ampersand.
			/** Document pDocument = createDocument("<?xml version='1.0' ?>" +
				"<tests>" +
				"<test attribute='i-Deal & Phoenix' />" +
				"<tests>");
			*/
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
