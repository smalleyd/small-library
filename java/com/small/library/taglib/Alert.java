package com.small.library.taglib;

/********************************************************************************
*
*	Bean class that represents the components of an alert, which are ...
*
*	<UL>
*		<LI>Message</LI>
*		<LI>OK action</LI>
*		<LI>Cancel action</LI>
*	</UL>
*
*	The OK action and the Cancel action should reference javascript functions
*	in the body of the HTML. Either action can be <CODE>NULL</CODE>.
*
*	@author David Small
*	@version 1.1.0.0
*	@date 6/5/2003
*
**********************************************************************************/

public class Alert
{
	/**************************************************************************
	*
	*	Constructors
	*
	**************************************************************************/

	/** Constructs an empty object. */
	public Alert() { this(null); }

	/** Constructs a fully populated object.
		@param message The alert message.
	*/
	public Alert(String message) { this(message, null); }

	/** Constructs a fully populated object.
		@param message The alert message.
		@param okAction The alert OK action.
	*/
	public Alert(String message, String okAction) { this(message, okAction, null); }

	/** Constructs a fully populated object.
		@param message The alert message.
		@param okAction The alert OK action.
		@param cancelAction The alert Cancel action.
	*/
	public Alert(String message, String okAction, String cancelAction)
	{
		this.message = message;
		this.okAction = okAction;
		this.cancelAction = cancelAction;
	}

	/**************************************************************************
	*
	*	Accessor methods
	*
	**************************************************************************/

	/** Accessor method - gets the message. */
	public String getMessage() { return message; }

	/** Accessor method - gets the OK action. */
	public String getOkAction() { return okAction; }

	/** Accessor method - gets the Cancel action. */
	public String getCancelAction() { return cancelAction; }

	/**************************************************************************
	*
	*	Mutator methods
	*
	**************************************************************************/

	/** Mutator method - sets the message. */
	public void setMessage(String newValue) { message = newValue; }

	/** Mutator method - sets the OK action. */
	public void setOkAction(String newValue) { okAction = newValue; }

	/** Mutator method - sets the Cancel action. */
	public void setCancelAction(String newValue) { cancelAction = newValue; }

	/**************************************************************************
	*
	*	Member variables
	*
	**************************************************************************/

	/** Member variable - contains the message. */
	public String message;

	/** Member variable - contains the OK action. */
	public String okAction;

	/** Member variable - contains the Cancel action. */
	public String cancelAction;
}

