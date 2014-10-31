package com.small.library.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.small.library.servlet.ControllerServlet;

/*********************************************************************************
*
*	Tag library class that represents an alert on the page. Be sure to put
*	the scripts needed to be called by the OK action and/or Cancel action
*	ahead of this tag to ensure that the functions can be found. Also, do
*	not include any terminating semi-colons. The final semi-colon will be
*	provided.
*
* 	<br /><br />
* 	
*	After the alert has been rendered, the <I>Alert</I> object is removed
*	from the session.
*
*	@author David Small
*	@version 1.1.0.0
*	@date 6/5/2003
*
*********************************************************************************/

public class AlertTag extends TagSupport
{
	/** Constant - serial version UID. */
	public static final long serialVersionUID = 1L;

	/** TagSupport method - handles the start of the tag. */
	public int doStartTag()
		throws JspException
	{
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = request.getSession(true);
		Alert alert = (Alert) session.getAttribute(ControllerServlet.SESSION_ATTR_NAME_ALERT);

		if (null == alert)
			return SKIP_BODY;

		String message = alert.getMessage();

		if (null == message)
			return SKIP_BODY;

		try
		{
			JspWriter out = pageContext.getOut();

			out.println("<script language=\"text/javascript\">");
			out.println("<!--");

			String okAction = alert.getOkAction();

			if (null == okAction)
			{
				out.print("\twindow.alert('");
				out.print(message);
				out.println("');");
			}

			else
			{
				out.print("\tif (window.confirm('");
				out.print(message);
				out.println("'))");

				out.print("\t\t");
				out.print(okAction);
				out.println(";");

				String cancelAction = alert.getCancelAction();

				if (null != cancelAction)
				{
					out.println("\telse");
					out.print("\t\t");
					out.print(cancelAction);
					out.println(";");
				}
			}

			out.println("// -->");
			out.println("</script>");

			// Remove the Alert.
			session.removeAttribute(ControllerServlet.SESSION_ATTR_NAME_ALERT);
		}

		catch (IOException ex) { throw new JspException(ex); }

		return SKIP_BODY;
	}
}

