package com.small.library.json;

import java.io.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/** Generates a Java value object from a JSON document.
 * 
 * @author smalleyd
 * @version 2.0
 * @since 9/19/2017
 *
 */

public abstract class JSONBase implements Runnable
{
	private static final Logger log = LoggerFactory.getLogger(JSONBase.class);

	public static final char CHAR_QUOTE = '`';
	public static final int NUM_OF_TESTS = 10;

	public static final String QUOTE_CHARACTER = ", quoteCharacter='" + CHAR_QUOTE + "'";

	protected final JSONConfig conf;
	protected final JSONClass clazz;
	protected final PrintStream out;
	protected final String domainPackage;
	protected final String appPackage;

	public JSONBase(final JSONConfig conf, final JSONClass clazz, final PrintStream out)
	{
		this.conf = conf;
		this.clazz = clazz;
		this.out = out;

		var i = conf.packageName.indexOf('.');
		if (0 < i)
		{
			var ii = conf.packageName.indexOf('.', i + 1);
			if (0 < ii)
			{
				domainPackage = conf.packageName.substring(0, ii);
				var iii = conf.packageName.indexOf('.', ii + 1);
				if (0 < iii) appPackage = conf.packageName.substring(0, iii);
				else appPackage = conf.packageName.substring(0, ii);
			}
			else appPackage = domainPackage = conf.packageName.substring(0, i);
		}
		else appPackage = domainPackage = "";
	}

	/** Application entry point. The first two arguments are required.
	 * 
	 * @param args[0] the configuration file name (JSON). Required.
	 * @param args[1] the output directory. Required.
	 * @param args[2] comma separated list of classes to generate to limit output. Optional.
	 * @throws Exception
	 */
	public static void main(final String... args) throws Exception
	{
		if (2 > args.length)
			throw new IllegalArgumentException("Please provide at least the configuration file(s) and output directory.");

		// Get references to the configuration file(s).
		var file = new File(args[0]);
		if (!file.isFile())
			throw new IllegalArgumentException("'" + args[0] + "' is not a file.");

		// Get a reference to the output directory.
		var output = new File(args[1]);
		if (!output.isDirectory())
			throw new IllegalArgumentException("'" + args[1] + "' is not a directory.");

		// Load each configuration file and create the output.
		var mapper = new ObjectMapper();
		var conf = mapper.readValue(file, JSONConfig.class);
		if (null == conf.packageName)
		{
			throw new IllegalArgumentException("Configuration file '" + file.getAbsolutePath() + "' is missing the packageName property.");
		}
		if (CollectionUtils.isEmpty(conf.classes))
		{
			throw new IllegalArgumentException("Configuration file '" + file.getAbsolutePath() + "' has not defined any classes.");
		}

		var fileName = file.getAbsolutePath();
		var classNames = (3 <= args.length) ? StringUtils.trimToNull(args[2]) : null;
		if (null == classNames)
		{
			int i = -1;
			for (var clazz : conf.classes)
			{
				generate(fileName, conf, clazz, output, ++i);
			}
		}
		else
		{
			int i = -1;
			for (var className : classNames.split(","))
			{
				i++;	// Increment even if the class is not in the file.
				var clazz = conf.clazz(className);
				if (null == clazz)
					log.warn("Configuration file '{}' does not contain the class with name '{}'.", file.getAbsolutePath(), className);
				else
					generate(fileName, conf, clazz, output, i);
			}
		}
	}

	static void generate(final String fileName, final JSONConfig conf, final JSONClass clazz, final File dir, final int i)
		throws IOException
	{
		var lowerCase = clazz.name.toLowerCase();

		if (null == clazz.name)
		{
			log.warn("Item '{}' is missing the name property in '{}'.", i, fileName);
			return;	// Skip this file.
		}
		if (null == clazz.caption)
		{
			log.warn("Item '{}' is missing the caption property in '{}'.", i, fileName);
			return;	// Skip this file.
		}
		if (CollectionUtils.isEmpty(clazz.fields))
		{
			log.warn("Item '{}' has not specified any fields in '{}'.", i, fileName);
			return;	// Skip this file.
		}

		try (var out = new PrintStream(new File(dir, clazz.name + ".java")))
		{
			new JSONValue(conf, clazz, out).run();
			out.flush();
		}

		if (clazz.generateFilter)
		{
			try (var out = new PrintStream(new File(dir, JSONFilter.getClassName(clazz.name) + ".java")))
			{
				new JSONFilter(conf, clazz, out).run();
				out.flush();
			}
		}

		if (clazz.generateElastic)
		{
			try (var out = new PrintStream(new File(dir, JSONElastic.getClassName(clazz.name) + ".java")))
			{
				new JSONElastic(conf, clazz, out).run();
				out.flush();
			}
			try (var out = new PrintStream(new File(dir, JSONElasticTest.getClassName(clazz.name) + ".java")))
			{
				new JSONElasticTest(conf, clazz, out).run();
				out.flush();
			}
			try (var out = new PrintStream(new File(dir, lowerCase + ".json")))
			{
				new JSONElasticMapping(conf, clazz, out).run();
				out.flush();
			}
		}

		if (clazz.generateResource)
		{
			try (var out = new PrintStream(new File(dir, JSONResource.getClassName(clazz.name) + ".java")))
			{
				new JSONResource(conf, clazz, out).run();
				out.flush();
			}

			try (var out = new PrintStream(new File(dir, JSONResourceTest.getClassName(clazz.name) + ".java")))
			{
				new JSONResourceTest(conf, clazz, out).run();
				out.flush();
			}
		}

		if (clazz.generateResource || clazz.generateElastic)
		{
			final JSONIndexTest indexTest;

			try (var out = new PrintStream(new File(dir, lowerCase + "-index.json")))
			{
				(indexTest = new JSONIndexTest(conf, clazz, out, 1)).run();
				out.flush();
			}
			try (var out = new PrintStream(new File(dir, lowerCase + "-invalid.json")))
			{
				new JSONInvalidTest(conf, clazz, out, indexTest.inputs.get(0), indexTest.sampleData.get(0)).run();
				out.flush();
			}
			try (var out = new PrintStream(new File(dir, lowerCase + "-update.json")))
			{
				new JSONIndexTest(conf, clazz, out, 7).run();
				out.flush();
			}
			try (var out = new PrintStream(new File(dir, lowerCase + "-search.json")))
			{
				new JSONSearchTest(conf, clazz, out, indexTest.sampleData).run();
				out.flush();
			}
		}
	}
}
