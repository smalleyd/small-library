package com.small.library.util;

import sun.net.ftp.*;
import java.net.*;
import java.io.*;

/****************************************************************************************
*
*	Helper class for interaction with an FTP server. This would include downloads
*	and uploads.
*
*	@author Xpedior\Narendra Mehta
*	@version 1.1.0.0
*	@date 11/16/2000
*
****************************************************************************************/

public class FtpHelper
{
	/*******************************************************************************
	*
	*	Main functionality
	*
	*******************************************************************************/

	/** Downloads file form a specified FTP location.
	 	@param pURL a URL Object specifying the FTP Location. 
	 	@param pFile a File Object.
	*/
	public static void download(URL pUrl, File pFile)
		throws MalformedURLException, IOException
	{ download(pUrl.openStream(), pFile); }
	
	/** Downloads file form a specified FTP location.
	 	@param strURL String Object specifying the FTP Location.
	 	@param strResource String Object specifying the File Path at the FTP server.
	 	@param strUserName UserName String specifying the UserName.
		@param strPaasword UserName String specifying the Password.
	 	@param pFile a File Object.
	*/
	public static void download(String strUrl, String strResource,
		String strUserName, String strPassword, File pFile)
		throws IOException
	{
		FtpClient pFtpClient = new FtpClient();
		
		pFtpClient.openServer(strUrl);
		
		pFtpClient.login(strUserName, strPassword);
		pFtpClient.binary();
		
		download(pFtpClient.get(strResource), pFile);
	}

	/** Downloads a file from an InputStream. */
	private static void download(InputStream pIn, File pFile)
		throws IOException
	{
		int nByte = 0;
		FileOutputStream pOut = new FileOutputStream(pFile);
		
		while (-1 < (nByte = pIn.read()))
			pOut.write(nByte);
		
		pOut.flush();
		pOut.close();
	}

	/*******************************************************************************
	*
	*	Driver methods
	*
	*******************************************************************************/
	
	public static void main(String arg[])
	{
		try
		{
			File f = null;
			
			//for download with two params
			if (arg[0].equals("" + 1))
			{	
				URL pUrl = new URL(arg[1]);
				f = new File(arg[2]);
				FtpHelper.download(pUrl, f);
			}
			//for download with five params
			else if (arg[0].equals("" + 2))
			{
				String strUrl = arg[1];
				String strResource = arg[2];
				String strUser = arg[3];
				String strPwd = arg[4];
				f = new File(arg[5]);
				
				FtpHelper.download(strUrl, strResource, strUser, strPwd, f);
			}		
			
		}
		catch(MalformedURLException e)
		{ System.out.println("MalExceptionnnn occured:" + e);}
		catch(IOException e)
		{ System.out.println("Exceptionnnn occured:" + e);}
	}
}
