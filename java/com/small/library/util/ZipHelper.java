package com.small.library.util;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/***************************************************************************************
*
*	Helper class for interaction with Zip files. This will include compression 
*	and uncompression of Zip Files.
* 
*	@author Xpedior\Narendra Mehta
*	@version 1.1.0.0
*	@date 11/16/2000
*
***************************************************************************************/

public class ZipHelper
{
	/*******************************************************************************
	*
	*	Main functionality
	*
	*******************************************************************************/
	
	/** Uncompresses a Zip file.
	 	@param pFileIn File Object to be Unzipped. 
	 	@param pFileOut File Object that specifies the Output path for the unZipped files.
	*/
	public static void decompressAll(File pFileIn, File pFileOut)
		throws ZipException, IOException
	{
		ZipFile pZipFile = new ZipFile(pFileIn);
		ArrayList pFileList = new ArrayList();

		for(Enumeration pZipFiles = pZipFile.entries(); pZipFiles.hasMoreElements();)
		{
			ZipEntry pZipEntry = (ZipEntry) pZipFiles.nextElement();				
			
			//If it is a Directory, create a Directory.
			if (pZipEntry.isDirectory())
			{
				File pFile = new File(pFileOut, pZipEntry.getName());
				pFile.mkdir();
			}

			//Else If it is a File, Add it to an ArrayList for further use.
			else
				pFileList.add(pZipEntry);
		}

		//Start going through Arraylist of Files.
		for (int i = 0; i < pFileList.size(); i++)
		{
			ZipEntry pZipEntry = (ZipEntry) pFileList.get(i);
			
			// Create a new file for each Zipentry using the Path suppplied in pFileout.
			File pFile = new File(pFileOut, pZipEntry.getName());

			int nByte = 0;
			InputStream pIn = pZipFile.getInputStream(pZipEntry);
			BufferedOutputStream pOut = new BufferedOutputStream(new FileOutputStream(pFile));

			while(-1 < (nByte = pIn.read()))
				pOut.write(nByte);

			pIn.close();

			pOut.flush();
			pOut.close();				
		}

		pZipFile.close();		
	}

	/*******************************************************************************
	*
	*	Driver methods
	*
	*******************************************************************************/

	public static void main(String arg[])
	{
		try {ZipHelper.decompressAll(new File(arg[0]), new File(arg[1]));}
	  	catch(Exception pEx){System.out.println("Exceptionnnn occured:" + pEx);}
	}
}
