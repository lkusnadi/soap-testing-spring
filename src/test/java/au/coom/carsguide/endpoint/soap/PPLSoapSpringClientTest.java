package au.coom.carsguide.endpoint.soap;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;

import org.hamcrest.text.IsEqualIgnoringCase;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import au.com.carsguide.xmlns.schemas.vehiclelistings.GetListing;
import au.com.carsguide.xmlns.schemas.vehiclelistings.GetListingResponse;
import au.com.carsguide.xmlns.schemas.vehiclelistings.ListingNotFoundFault;
import au.com.carsguide.xmlns.schemas.vehiclelistings.ListingServiceFault;
import au.com.carsguide.xmlns.schemas.vehiclelistings.ListingStatusResult;
import au.com.carsguide.xmlns.schemas.vehiclelistings.PrivatePartyListingWebService;
import au.com.carsguide.xmlns.schemas.vehiclelistings.VehicleListing;

public class PPLSoapSpringClientTest {

	private static PrivatePartyListingWebService client;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/application-context.xml");
		client = (PrivatePartyListingWebService) context.getBean("privateListingClient");
	}

	@Test
	public void getListingStatusTest() {
		try {
			ListingStatusResult result = client.getListingStatus("CG_Private_163516");
			assertThat("listing status is active", result.getListingStatus().value(), IsEqualIgnoringCase.equalToIgnoringCase("active"));
		} catch (ListingServiceFault | ListingNotFoundFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getListingTest() {
		GetListing parameters = new GetListing();
		parameters.setListingExternalReferenceId("CG_Private_163516");
		try {
			GetListingResponse result = client.getListing(parameters);
			VehicleListing vl = result.getVehicleListing();
			assertThat("listing id is 3787863", vl.getListingID(), is(3787863));
			assertThat("listing date is 2015-12-16+11:00", vl.getListDate().toString(), is("2015-12-16+11:00"));
			assertThat("listing postcode is 2323", vl.getPostCode(), is("2323"));
			assertThat("listing price is big decimal", vl.getPrice().getListPrice(), instanceOf(BigDecimal.class));
		} catch (ListingServiceFault | ListingNotFoundFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
