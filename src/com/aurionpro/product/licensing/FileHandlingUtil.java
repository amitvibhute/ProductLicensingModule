package com.aurionpro.product.licensing;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileHandlingUtil {

	private static String CUST_CERT = "CUSTOMER_CERTIFICATE";
	private static String CA_CERT = "CA_CERTIFICATE";
	private static final String pathToLicenseProperties = "resources\\lic.properties";
	private static Properties props = new Properties();
	
	public static ByteArrayInputStream readCertificate (String keyFile) throws IOException {
		FileInputStream fis = null;
		ByteArrayInputStream bais = null;
		try 
		{
		  fis = new FileInputStream(keyFile);
		  byte value[] = new byte[fis.available()];
		  fis.read(value);
		  bais = new ByteArrayInputStream(value);
		}catch (Exception e)
		{
			 System.out.println(e);
		}finally 
		{
			fis.close();
		}
		return bais;
	}
	
	public static String readPropertyFromFile (String property) {
		System.out.println("Reading License properties");
		if (null == props || props.isEmpty())
		{
			loadProperties();
		}
		return props.getProperty(property);
	}
	
	public static void loadProperties (){
		System.out.println("Loading properties File");
		try 
		{
			InputStream ip = new FileInputStream(pathToLicenseProperties);
			props.load(ip);
			System.out.println("Properties File loaded. " + props.keys());
			ip.close();
		}catch (FileNotFoundException e)
		{
			System.out.println("File Not Found");
		}catch (IOException e) 
		{
			System.out.println("File could not be loaded because of following Exception - " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
