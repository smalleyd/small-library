package com.small.library.jasper;

import java.io.*;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRLoader;

/***********************************************************************************************
*
*	Command line access to the JasperCompileManager. The first and only argument can
*	be a directory or a Jasper XML file. If the argument is a directory, the application
*	recursively works through the directory and all sub-directories and compiles the
*	files with an XML extension or a JRXML extension.
*
*	@author David Small
*	@version 1.1.0.0
*	@date 7/10/2004
*
**********************************************************************************************/

public class Compiler
{
	/** Constant - XML extension. */
	public static final String EXTENSION_XML = ".xml";

	/** Constant - JRXML extension. */
	public static final String EXTENSION_JRXML = ".jrxml";

	/** Compiles Jasper XML documents.
		@param arg0 name of a a Jasper XML file or a directory containing
			Jasper XML files.
	*/
	public static void main(String[] args)
	{
		try
		{
			if ((0 == args.length) || (null == args[0]) || (0 == args[0].length()))
				throw new Exception("Please provide a Jasper XML file or directory.");

			File file = new File(args[0]);

			if (!file.exists())
				throw new Exception("File, \"" + args[0] + "\" does not exist.");

			if (file.isDirectory())
				compileDir(file);

			else
				compileFile(file);
		}

		catch (Exception ex)
		{
			ex.printStackTrace();
			System.exit(1);
		}
	}

	/** Compiles a directory. */
	private static void compileDir(File dir)
		throws JRException
	{
		System.out.println("DIR - " + dir.getAbsolutePath() + ": compiling contents");

		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++)
		{
			File file = files[i];

			if (file.isDirectory())
				compileDir(file);
			else
			{
				String fileName = file.getName();

				if (fileName.endsWith(EXTENSION_XML) || fileName.endsWith(EXTENSION_JRXML))
					compileFile(file);
			}
		}
	}

	/** Compiles a Jasper XML document. */
	private static void compileFile(File file)
		throws JRException
	{
		System.out.println("FILE - " + file.getName() + ": compiling file");

		String fileName = JasperCompileManager.compileReportToFile(
			file.getAbsolutePath());

		System.out.println("\tOUT - " + fileName);

		try
		{
			JRLoader.loadObject(fileName);
		}

		catch (Exception ex)
		{
			System.out.println("\tERROR ON LOAD - " + ex.getMessage());
		}
	}
}

