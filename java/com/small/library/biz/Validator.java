package com.small.library.biz;

/**
 * 
 * Validate e-mail addresses.
 * 
 * @author David Small
 * @version 1.0
 * @since 6/12/2010
 *
 */

public class Validator
{
	/** Validate the value.
	 * @param value E-mail address to validate
	 * @return NULL if not errors found.
	 **/
	public static String validateEmailAddress(String value)
	{
		if ((null == value) || (5 > value.length()))
			return "it does not exist or is too short";

		int size = value.length();
		int atIndex = -1;
		for (int i = 0; i < size; i++)
		{
			char c = value.charAt(i);
			if (c == ' ') return "it cannot contain a space";
			if (c == '\'') return "it cannot contain a single quote";
			if (c == '"') return "it cannot contain a double quote";
			if (c == ';') return "it cannot contain a semi-colon";
			if (c == ':') return "it cannot contain a colon";
			if (c == '^') return "it cannot contain a ^";
			if (c == ',') return "it cannot contain a comma";

			if ((0 > atIndex) && (c == '@'))
				atIndex = i;
		}

		if ('.' == value.charAt(0))
			return "it cannot begin with a period";

		if (0 > atIndex)
			return "it is missing the @ modifier";
		else if (0 == atIndex)
			return "it is missing the name preceding the @ modifier";
		else if (0 < value.indexOf('@', atIndex + 1))
			return "it cannot contain multiple @ symbols";
		else if ('.' == value.charAt(atIndex - 1))
			return "it's name cannot end with a period";

		if (0 <= value.indexOf("..", atIndex + 1))
			return "it cannot contain a \"..\"";

		int spotIndex = value.indexOf('.', atIndex + 1);
		if (0 > spotIndex)
			return "it is missing the \".\" modifier after the @ modifier";
		else if (spotIndex == (atIndex + 1))
			return "it is missing the domain name between the @ and the \".\" modifier";
		else if (spotIndex == (value.length() - 1))
			return "it is missing the domain type following the \".\" modifier";

		return null;
	}
}
