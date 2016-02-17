package com.aurionpro.product.licensing;

import java.util.Calendar;
import java.util.Date;


public class MainClass {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		boolean okToProvideService = false;
		okToProvideService = LicenseManager.isLicenseValid();
		if (okToProvideService == true){
			System.out.println("License is Valid. Service can be provided.");
		}
		else if (okToProvideService == false)
		{
			System.out.println("License Expired. Service cannot be provided.");
		}
	}

}
