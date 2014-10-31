package com.small.library.rest;

import static java.lang.reflect.Modifier.*;

import java.lang.reflect.*;
import java.util.Date;

import javax.xml.parsers.*;

import org.w3c.dom.*;

/** Generates XML based on a supplied object. Uses reflection to output the 
 * public, non-static member variables. The builder returns the Document object
 * instead of outputting the text in case the caller wants to respond to an error
 * with HTTP response error code.
 * 
 * @author David Small
 * @version 1.1
 * @since 1/23/2009
 *
 */

@SuppressWarnings("unchecked")
public class XmlBuilder
{
	/** Transforms the specified object to a DOM Document. */
	public Document create(Object value)
	{
		// Create the document. Need to build a DOM object first as nodes and sub-elements can be added
		// simultaneously for the same object without worrying about the order of outputting. For example,
		// if a subelement (new object or long string) is added before the object is done writing it's
		// attributes, the current element would have to close.
		DocumentBuilder builder = null;
		try { builder = DocumentBuilderFactory.newInstance().newDocumentBuilder(); }
		catch (ParserConfigurationException ex) { throw new RuntimeException(ex); }
		Document doc = builder.newDocument();
		doc.setXmlStandalone(true);
		doc.setStrictErrorChecking(false);

		// Create the base element.
		doc.appendChild(createElement(doc, value));

		return doc;
	}

	/** Creates simple and complex elements. */
	public Element createElement(Document doc, Object value)
	{
		Class<? extends Object> clazz = value.getClass();
		Element node = doc.createElement(fixName(clazz));

		// If the value is a simple object (String, Number, ...) just set the element text and leave.
		if ((value instanceof Number) || (value instanceof Boolean))
		{
			node.setTextContent(value.toString());
			return node;
		}

		else if (value instanceof String)
		{
			node.appendChild(doc.createCDATASection(value.toString()));
			return node;
		}

		else if (value instanceof Date)
		{
			node.setTextContent(value.toString());
			return node;
		}

		// Otherwise ...
		// Get all the fields, including from the super classes.
		Field[] fields = clazz.getFields();

		// Add an attribute or element for each field with a public, non-static, non-null value.
		for (Field field : fields)
		{
			int modifiers = field.getModifiers();

			// Only public-member variables are to be output.
			if (!isPublic(modifiers) || isStatic(modifiers))
				continue;

			// Create a JavaScript array for Java arrays and lists.
			addNode(doc, node, field, value);
		}

		return node;
	}

	/** Output a single node - can possibly be a new element. */
	public void addNode(Document doc, Element parent, Field field, Object value)
	{
		Object item = null;
		try { item = field.get(value); }
		catch (IllegalAccessException ex) { /** Just move to next field. */ return; }

		// Keep NULL as undefined.
		if (null == item)
			return;

		String name = field.getName();

		if (item instanceof Iterable)
		{
			Iterable<? extends Object> iterable = (Iterable<? extends Object>) item;
			for (Object it : iterable)
				parent.appendChild(createElement(doc, it));
		}
		else if (item.getClass().isArray())
		{
			Object[] iterable = (Object[]) item;
			for (Object it : iterable)
				parent.appendChild(createElement(doc, it));
		}

		else if (isLongText(field))
			parent.appendChild(createElement(doc, item));

		else if ((item instanceof Number) || (item instanceof Boolean) || (item instanceof String))
			parent.setAttribute(name, item.toString());

		// Keep date, separate as will have to come up with different formatter.
		else if (item instanceof Date)
			parent.setAttribute(name, item.toString());

		// Leave open the possibility to traverse the object graph and serialize.
		else
			parent.appendChild(createElement(doc, item));
	}

	/** Indicates whether a field has a LongText annotation. If so, create a separate element with a CDATA section. */
	protected boolean isLongText(Field field)
	{
		return (null != field.getAnnotation(LongText.class));
	}

	/** Converts a class name to an XML element name. It removed the package name and lower cases the first letter. */
	protected String fixName(Class<? extends Object> clazz)
	{
		String name = clazz.getSimpleName();

		if (1 == name.length())
			return name.toLowerCase();

		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}
}
