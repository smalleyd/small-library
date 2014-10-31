package com.small.library.html;

import java.io.IOException;
import java.io.Writer;

/***************************************************************************************
*
*	Class that represents HTML form elements. A form element can also contain
*	other HTML elements.
*
*	@author i-Deal\David Small
*	@version 2.0.0.0
*	@date 2/15/2000
*
***************************************************************************************/

public class Form extends TagElement
{
	/******************************************************************************
	*
	*	Constants
	*
	******************************************************************************/

	/** Constant - element's tag name. */
	public static final String TAG = "FORM";

	/** Constant - general use name prefix. */
	public static final String PREFIX = "frm";

	/** Constant - form method "GET". */
	public static final String METHOD_GET = "GET";

	/** Constant - form method "POST". */
	public static final String METHOD_POST = "POST";

	/** Constant - form method "PUT". */
	public static final String METHOD_PUT = "PUT";

	/** Constant - form method "DELETE". */
	public static final String METHOD_DELETE = "DELETE";

	/** Constant - plain text encode type. */
	public static final String ENCTYPE_PLAIN = "text/plain";

	/** Constant - multiple part file up encode type. */
	public static final String ENCTYPE_MULTIPART = "multipart/form-data";

	/** Constant - file up encode type. */
	public static final String ENCTYPE_FILE_UP = ENCTYPE_MULTIPART;

	/** Constant - default encode type. */
	public static final String ENCTYPE_DEFAULT = ENCTYPE_PLAIN;

	/** Constant - attribute name of the Action attribute. */
	public static final String ATTRIBUTE_ACTION = "ACTION";

	/** Constant - attribute name of the Method attribute. */
	public static final String ATTRIBUTE_METHOD = "METHOD";

	/** Constant - attribute name of the onSubmit attribute. */
	public static final String ATTRIBUTE_ONSUBMIT = "onSubmit";

	/** Constant - attribute name of the EncType attribute. */
	public static final String ATTRIBUTE_ENCTYPE = "ENCTYPE";

	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an object with a Name.
		@param strName Name of the form element.
		@param pChildElement HTML <I>Element</I> object that is contained
			within the form element.
	*/
	public Form(String strName) { this(strName, null, null, null, null); }

	/** Constructor - constructs an object with a Name, and an Action attribute.
		@param strName Name of the form element.
		@param strAction Action attribute of the form element.
		@param pChildElement HTML <I>Element</I> object that is contained
			within the form element.
	*/
	public Form(String strName, String strAction, Element pChildElement)
	{ this(strName, strAction, null, pChildElement); }

	/** Constructor - constructs an object with a Name, an Action attribute,
	    and a Method attribute.
		@param strName Name of the form element.
		@param strAction Action attribute of the form element.
		@param strMethod Method attribute of the form element.
		@param pChildElement HTML <I>Element</I> object that is contained
			within the form element.
	*/
	public Form(String strName, String strAction, String strMethod,
		Element pChildElement)
	{ this(strName, strAction, strMethod, null, pChildElement); }

	/** Constructor - constructs an object with a Name, an Action attribute,
	    a Method attribute, and an onSubmit attribute.
		@param strName Name of the form element.
		@param strAction Action attribute of the form element.
		@param strMethod Method attribute of the form element.
		@param strOnSubmit onSubmit attribute of the form element.
		@param pChildElement HTML <I>Element</I> object that is contained
			within the form element.
	*/
	public Form(String strName, String strAction, String strMethod,
		String strOnSubmit, Element pChildElement)
	{ this(strName, strAction, strMethod, strOnSubmit, null, pChildElement); }

	/** Constructor - constructs an object with a Name, an Action attribute,
	    a Method attribute, an onSubmit attribute, and an EncType attribute.
		@param strName Name of the form element.
		@param strAction Action attribute of the form element.
		@param strMethod Method attribute of the form element.
		@param strOnSubmit onSubmit attribute of the form element.
		@param strEncType EncType attribute of the form element.
		@param pChildElement HTML <I>Element</I> object that is contained
			within the form element.
	*/
	public Form(String strName, String strAction, String strMethod,
		String strOnSubmit, String strEncType, Element pChildElement)
	{
		super(PREFIX + strName);

		m_strAction = strAction;
		m_strMethod = strMethod;
		m_strOnSubmit = strOnSubmit;
		m_strEncType = strEncType;
		m_ChildElement = pChildElement;
	}

	/******************************************************************************
	*
	*	Required methods: Element
	*
	******************************************************************************/

	/** Action method - creates the HTML form element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void create(Writer pWriter)
		throws IOException
	{
		create(pWriter, m_ChildElement);
	}

	/** Action method - creates the HTML form element.
		@param pWriter <I>Writer</I> object used to output HTML.
		@param pChildElement HTML <I>Element</I> object that is contained
			within the form element.
	*/
	public void create(Writer pWriter, Element pChildElement)
		throws IOException
	{
		open(pWriter);

		if (null != pChildElement)
			pChildElement.create(pWriter);

		close(pWriter);
	}

	/******************************************************************************
	*
	*	Action methods
	*
	******************************************************************************/

	/** Action method - opens the form element.
	*/
	public void open() throws IOException
	{
		open(getWriter());
	}

	/** Action method - opens the form element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void open(Writer pWriter) throws IOException
	{
		openTag(pWriter, TAG);

		writeAttribute(pWriter, ATTRIBUTE_ACTION, getAction());
		writeAttribute(pWriter, ATTRIBUTE_METHOD, getMethod());
		writeAttribute(pWriter, ATTRIBUTE_ONSUBMIT, getOnSubmit());
		writeAttribute(pWriter, ATTRIBUTE_ENCTYPE, getEncType());

		closeTag(pWriter);
		writeNewLine(pWriter);
	}

	/** Action method - closes the form element.
	*/
	public void close() throws IOException
	{
		close(getWriter());
	}

	/** Action method - closes the form element.
		@param pWriter <I>Writer</I> object used to output HTML.
	*/
	public void close(Writer pWriter) throws IOException
	{ writeTagClosing(pWriter, TAG); writeNewLine(pWriter); }

	/**********************************************************************************
	*
	*	Accessor methods
	*
	**********************************************************************************/

	/** Accessor method - gets the Name of the form element. */
	public String getName() { return m_strName; }

	/** Accessor method - gets the Action attribute of the form element. */
	public String getAction() { return m_strAction; }

	/** Accessor method - gets the Method attribute of the form element. */
	public String getMethod() { return m_strMethod; }

	/** Accessor method - gets the onSubmit attribute of the form element. */
	public String getOnSubmit() { return m_strOnSubmit; }

	/** Accessor method - gets the EncType attribute of the form element. */
	public String getEncType() { return m_strEncType; }

	/** Accessor method - gets the HTML <I>Element</I> object that is contained
	    within the form element.
	*/
	public Element getChildElement() { return m_ChildElement; }

	/**********************************************************************************
	*
	*	Mutator methods
	*
	**********************************************************************************/

	/** Mutator method - sets the Name of the form element. */
	public void setName(String strNewValue) { m_strName = strNewValue; }

	/** Mutator method - sets the Action attribute of the form element. */
	public void setAction(String strNewValue) { m_strAction = strNewValue; }

	/** Mutator method - sets the Method attribute of the form element. */
	public void setMethod(String strNewValue) { m_strMethod = strNewValue; }

	/** Mutator method - sets the onSubmit attribute of the form element. */
	public void setOnSubmit(String strNewValue) { m_strOnSubmit = strNewValue; }

	/** Mutator method - sets the EncType attribute of the form element. */
	public void setEncType(String strNewValue) { m_strEncType = strNewValue; }

	/** Mutator method - sets the HTML <I>Element</I> object that is contained
	    within the form element.
	*/
	public void setChildElement(Element pNewValue) { m_ChildElement = pNewValue; }

	/**********************************************************************************
	*
	*	Member variables
	*
	**********************************************************************************/

	/** Member variable - contains the Name of the form element. */
	private String m_strName = null;

	/** Member variable - contains the Action attribute of the form element. */
	private String m_strAction = null;

	/** Member variable - contains the Method attribute of the form element. */
	private String m_strMethod = null;

	/** Member variable - contains the onSubmit attribute of the form element. */
	private String m_strOnSubmit = null;

	/** Member variable - contains the EncType attribute of the form element. */
	private String m_strEncType = null;

	/** Member variable - a reference to an HTML <I>Element</I> object that is
	    contained within the form element.
	*/
	private Element m_ChildElement = null;
}
