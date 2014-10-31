package com.small.library.http;

import java.net.URL;

public class UrlTest
{
	public static void main(String[] strArgs)
	{
		try
		{
			URL pUrl = new URL("http://www.netaddress.com/dave/mypage.html?ID=3");

			System.out.println("Authority: " + pUrl.getAuthority());
			System.out.println("Protocol: " + pUrl.getProtocol());
			System.out.println("Host: " + pUrl.getHost());
			System.out.println("Port: " + pUrl.getPort());
			System.out.println("UserInfo: " + pUrl.getUserInfo());
			System.out.println("Path: " + pUrl.getPath());
			System.out.println("File: " + pUrl.getFile());
			System.out.println("Query: " + pUrl.getQuery());
			System.out.println("Ref: " + pUrl.getRef());
			System.out.println("toString: " + pUrl.toString());
		}

		catch (Exception pEx) { pEx.printStackTrace(); }
	}
}
