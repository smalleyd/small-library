package com.small.library.servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

/***********************************************************************************
*
*	This servlet class contains helper methods for loading non-standard Posts.
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 7/18/2001
*
***********************************************************************************/

public abstract class HttpServletText extends HttpServlet
{
	/** Helper method - retrieves all data from the InputStream as text data.
		@param pRequest The <I>HttpRequest</I> object for the current request.
	*/
	protected String getTextData(HttpServletRequest pRequest)
		throws ServletException, IOException
	{
		int nContentLength = pRequest.getContentLength();

		// Is there any data?
		if (0 > nContentLength)
			return null;

		int nBuffer = 1024;
		int nRead = 0;
		char[] pBuffer = new char[nBuffer];
		StringBuffer strReturn = new StringBuffer(nContentLength);
		InputStreamReader pIn = new InputStreamReader(pRequest.getInputStream(), "ISO-8859-1");

		while (0 < (nRead = pIn.read(pBuffer)))
			strReturn.append(pBuffer, 0, nRead);

		return strReturn.toString();
	}
}
