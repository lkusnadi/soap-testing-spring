package au.com.carsguide.endpoint.soap;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import au.com.carsguide.xmlns.schemas.vehiclelistings.ListingNotFoundFault;
import au.com.carsguide.xmlns.schemas.vehiclelistings.ListingServiceFault;
import au.com.carsguide.xmlns.schemas.vehiclelistings.ListingStatusResult;
import au.com.carsguide.xmlns.schemas.vehiclelistings.PrivatePartyListingWebService;

public class PPLSoapSpringClient {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
		
		PrivatePartyListingWebService client = (PrivatePartyListingWebService) context.getBean("privateListingClient");
		
		try {
			ListingStatusResult result = client.getListingStatus("CG_Private_163516");
			System.out.println("Listing status: " + result.getListingStatus().value());
		} catch (ListingServiceFault | ListingNotFoundFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
