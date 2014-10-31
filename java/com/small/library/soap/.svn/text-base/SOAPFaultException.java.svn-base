package com.small.library.soap;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.small.library.xml.XMLHelper;

/***********************************************************************************
*
*	Thrown by a SOAP requester (caller) when a SOAP fault is returned in
*	response.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 5/10/2001
*
***********************************************************************************/

public class SOAPFaultException extends Exception
{
	/** Constant - Local node name for SOAP Fault Code. */
	public static final String NODE_NAME_CODE = "faultcode";

	/** Constant - Local node name for SOAP Fault String. */
	public static final String NODE_NAME_STRING = "faultstring";

	/** Constant - Local node name for the SOAP Fault Actor. */
	public static final String NODE_NAME_ACTOR = "faultactor";

	/** Constant - Local node name for the SOAP Fault Detail. */
	public static final String NODE_NAME_DETAIL = "detail"; // NOT "faultdetail"

	/** Constructor - accepts a SOAP Fault element node to create the fault.
		@param pNodeFault An element node that contains the SOAP fault information.
	*/
	public SOAPFaultException(Node pNode)
	{
		NodeList pNodeList = pNode.getChildNodes();
		int nLength = pNodeList.getLength();

		if (0 == nLength)
			return;

		// Build a map of the fault information.
		Map pMap = new HashMap(4);

		for (int i = 0; i < nLength; i++)
		{
			Node pChild = pNodeList.item(i);
			String strValue = XMLHelper.getNodeText(pChild);

			// In case the node is a wraps more messages in more nodes.
			// Mainly used for the "detail" node.
			if (null == strValue)
				strValue = XMLHelper.convertElementChildrenToString((Element) pChild);

			// Incase using old version of DOM that doesn't support "getLocalName".
			// Using "getLocalName", because need only local part to map correctly below.
			pMap.put(XMLHelper.getLocalName(pChild), strValue);
		}

		// Put the fault values into variables.
		m_strCode = (String) pMap.get(NODE_NAME_CODE);
		m_strString = (String) pMap.get(NODE_NAME_STRING);
		m_strActor = (String) pMap.get(NODE_NAME_ACTOR);
		m_strDetail = (String) pMap.get(NODE_NAME_DETAIL);

		// Build the full message.
		m_strMessage = "SOAP Fault [Code: " + m_strCode +
			", String: " + m_strString +
			", Actor: " + m_strActor +
			", Detail: " + m_strDetail + "]";
	}

	/** Accessor method - gets the fault code. */
	public String getCode() { return m_strCode; }

	/** Accessor method - gets the fault string. */
	public String getString() { return m_strString; }

	/** Accessor method - gets the fault actor. */
	public String getActor() { return m_strActor; }

	/** Accessor method - gets the fault details. */
	public String getDetail() { return m_strDetail; }

	/** Accessor method - gets the fault full message. */
	public String getMessage() { return m_strMessage; }

	/** Member variable - contains the fault code. */
	private String m_strCode = null;

	/** Member variable - contains the fault string. */
	private String m_strString = null;

	/** Member variable - contains the fault actor. */
	private String m_strActor = null;

	/** Member variable - contains the fault details. */
	private String m_strDetail = null;

	/** Member variable - contains the fault full message. */
	private String m_strMessage = null;
}
