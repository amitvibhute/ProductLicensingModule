package com.aurionpro.product.licensing;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateNotYetValidException;

public class LicenseManager {
	
private static String pathToCAChain = null;
private static String pathToCustomerCert = null;
private static boolean isLicenseValid = false;

private static String certValidationResult = null;
private static final String expired = "EXPIRED";
private static final String invalid = "INVALID";
private static final String ok = "OK";

private static String systemDateStatus = null;
private static final String clean = "CLEAN";
private static final String altered = "ALTERED";

private static String CUST_CERT = "CUSTOMER_CERTIFICATE";
private static String CA_CERT = "CA_CERTIFICATE";

	public static String validateCertificate() throws IOException
	{
		
		pathToCAChain = FileHandlingUtil.readPropertyFromFile(CA_CERT);
		pathToCustomerCert = FileHandlingUtil.readPropertyFromFile(CUST_CERT);
		ByteArrayInputStream certFile = FileHandlingUtil.readCertificate(pathToCustomerCert);
	  	// get X509 certificate factory
		try 
		{
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			// certificate factory can now create the certificate 
			X509Certificate clientCert = (X509Certificate) certFactory.generateCertificate(certFile);
			certFile.reset();
			certFactory = CertificateFactory.getInstance("X.509");
			certFile = FileHandlingUtil.readCertificate(pathToCAChain);
			X509Certificate chainCACert = (X509Certificate) certFactory.generateCertificate(certFile);
			PublicKey key = chainCACert.getPublicKey();
			clientCert.verify(key);
			System.out.println("Client Certificate Verified Successfully");
			clientCert.checkValidity();
			System.out.println("Client Certificate is VALID");
			certValidationResult = ok;
			isLicenseValid = true;
		}catch (InvalidKeyException | SignatureException  e) 
		{
			System.out.println("Client Certificate Could Not be verified Successfully");
			e.printStackTrace();
			certValidationResult = invalid;
		}catch (CertificateExpiredException |CertificateNotYetValidException e)
		{
			System.out.println("Client Certificate is either EXPIRED or NOT YET VALID");
			certValidationResult = expired;
			isLicenseValid = false;
		}catch (IOException | NoSuchProviderException | NoSuchAlgorithmException | 
				CertificateException e)
		{
			System.out.println("Exception Occurred while instantiating the Digital Certificate." + e.getMessage());
			e.printStackTrace();
		}
		finally 
		{
			certFile.close();
		}
		return certValidationResult;
		
	 }
	
	public static boolean isLicenseValid(){
		if (getSystemDateStatus().equalsIgnoreCase("CLEAN"))
		{
			try 
			{
				String licenseValidationStatus = validateCertificate();
				if (licenseValidationStatus.equals(ok))
				{
					isLicenseValid = true;
				}else if (licenseValidationStatus.equals(expired))
				{
					isLicenseValid = false;
					System.out.println("License is Expired.");
				}else if (licenseValidationStatus.equals(invalid))
				{
					isLicenseValid = false;
					System.out.println("License is either Invalid or Corrupted.");
				}
					
			} catch (IOException e) 
			{
				System.out.println("Exception while validating the License : " + e.getMessage());
				e.printStackTrace();
			}
			
		}
		return isLicenseValid;
	}
	
	public static String getSystemDateStatus (){
		/*
		 * This method will invoke the OS level utility that we are going to develop in order to keep track of the System Date.
		 * The utility will return ERROR code if the System date has been shifted back after the product installation.
		 * Currently assuming that the System Date will be clean.
		 */
		systemDateStatus = clean;
		return systemDateStatus;
	}

}
