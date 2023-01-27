package com.small.library.json;

import java.io.File;
import java.io.PrintStream;

import org.apache.commons.collections4.CollectionUtils;
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

		int i = 0;
		for (var clazz : conf.classes)
		{
			i++;
			if (null == clazz.name)
			{
				log.warn("Item '{}' is missing the name property in '{}'.", i, file.getAbsolutePath());
				continue;	// Skip this file.
			}
			if (null == clazz.caption)
			{
				log.warn("Item '{}' is missing the caption property in '{}'.", i, file.getAbsolutePath());
				continue;	// Skip this file.
			}
			if (CollectionUtils.isEmpty(clazz.fields))
			{
				log.warn("Item '{}' has not specified any fields in '{}'.", i, file.getAbsolutePath());
				continue;	// Skip this file.
			}

			try (var out = new PrintStream(new File(output, clazz.name + ".java")))
			{
				new JSONValue(conf, clazz, out).run();
				out.flush();
			}

			if (clazz.generateFilter)
			{
				try (var out = new PrintStream(new File(output, JSONFilter.getClassName(clazz.name) + ".java")))
				{
					new JSONFilter(conf, clazz, out).run();
					out.flush();
				}
			}

			if (clazz.generateDao)
			{
				try (var out = new PrintStream(new File(output, JSONDao.getClassName(clazz.name) + ".java")))
				{
					new JSONDao(conf, clazz, out).run();
					out.flush();
				}
			}

			if (clazz.generateResource)
			{
				try (var out = new PrintStream(new File(output, JSONResource.getClassName(clazz.name) + ".java")))
				{
					new JSONResource(conf, clazz, out).run();
					out.flush();
				}
			}
		}
	}
}
