package com.small.library.util.ant;

import java.io.*;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.small.library.util.JavaScriptCompressor;

/** Ant task that compresses a set of JavaScript files. Accepts an IN directory
 * and an OUT directory. Processes the IN directory recursively.
 * 
 * @author David Small
 * @version 1.0
 * @since 5/9/2007
 *
 */

public class JavaScriptCompressorTaskDef extends Task
{
	/** Default. */
	public JavaScriptCompressorTaskDef() {}

	/** Performs the ANT task. */
	public void execute() throws BuildException
	{
		processDir(srcDir, destDir);
	}

	/** Processes a directory recursively. */
	private void processDir(File dir, File dest) throws BuildException
	{
		String path = dir.getName();

		// Don't handle CVS directory or hidden directories.
		if (path.endsWith("CVS") || path.startsWith("."))
			return;

		// Create the respective destination directory.
		File destDir = null;
		try
		{
			destDir = new File(dest, path);
			destDir.mkdirs();
		}

		catch (Exception ex) { throw new BuildException(ex); }
		
		File[] files = dir.listFiles();
		for (File file : files)
		{
			if (file.isDirectory())
				processDir(file, destDir);
			else
				processFile(file, destDir);
		}
	}

	/** Processes a file. */
	private void processFile(File file, File destDir) throws BuildException
	{
		String path = file.getName();

		// Only handle files that end in "js".
		if (!(path.endsWith(".js") || path.endsWith(".JS")))
			return;

		try
		{
			File destFile = new File(destDir, path);
			compressor.run(new FileInputStream(file), new FileOutputStream(destFile, false));

			// Run compressor first.
			if (isVerbose)
			{
				long srcSize = file.length();
				long destSize = destFile.length();
				totalSrcSize+= srcSize;
				totalDestSize+= destSize;
				float compressionPct = ((float) srcSize - destSize) / (float) srcSize;
				System.out.println("Old File: " + file.getPath() +
					" (" + srcSize + " bytes) - New File: " + destFile.getPath() +
					" (" + destSize + " bytes) - Compression Rate: " + (compressionPct * 100) + "%.");
			}
		}

		catch (Exception ex) { throw new BuildException(ex); }
	}

	/** Sets the Source directory. */
	public void setSrcDir(File newValue) { srcDir = newValue; }

	/** Sets the destination directory. */
	public void setDestDir(File newValue) { destDir = newValue; }

	/** Set the verbose output. */
	public void setVerbose(boolean newValue) { isVerbose = newValue; }

	/** The JavaScript compressor. */
	private JavaScriptCompressor compressor = new JavaScriptCompressor();

	/** Directory that contains the non-compressed script files. */
	private File srcDir = null;

	/** Directory that will contain the generated compressed version of the script files. */
	private File destDir = null;

	/** Is verbose. */
	private boolean isVerbose = false;

	/** Total source file size. */
	private long totalSrcSize = 0L;

	/** Total destination file size. */
	private long totalDestSize = 0L;

	/** Driver method. */
	public static void main(String args[])
	{
		try
		{
			JavaScriptCompressorTaskDef task = new JavaScriptCompressorTaskDef();
			task.setSrcDir(new File(args[0]));
			task.setDestDir(new File(args[1]));
			task.setVerbose(true);

			task.execute();

			// Calculate the total savings.
			float compressionPct = ((float) task.totalSrcSize - task.totalDestSize) / (float) task.totalSrcSize;

			System.out.println();
			System.out.println("Total Compression Rate: " + (compressionPct * 100) + "%");

			System.exit(0);
		}

		catch (Exception ex) { ex.printStackTrace(); System.exit(1); }
	}
}
