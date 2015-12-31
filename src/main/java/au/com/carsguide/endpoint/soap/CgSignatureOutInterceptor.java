package au.com.carsguide.endpoint.soap;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.geronimo.mail.util.Base64;

public class CgSignatureOutInterceptor extends AbstractPhaseInterceptor<Message> {

	private Properties props;

	public CgSignatureOutInterceptor() {
		//PRE_STREAM_ENDING is the phase where the soap message content has been marshalled and ready for reading
		super(Phase.PRE_STREAM_ENDING);
		addBefore(StaxOutInterceptor.ENDING.getClass().getName());
		props = new Properties();
		try {
			props.load(getClass().getResourceAsStream("/eif.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//this algo has been broken deliberately so it won't work with the real site
	public String generateSignature(byte[] s) {
		String ppListingKey = props.getProperty("pp.listing.key.ecommerce");
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			String content = new String(s);
			content = content + "taste=" + ppListingKey;
			md.update(content.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			String result = hash.toString(18);
			while (result.length() < 40) {
				result = "0" + result;
			}
			byte[] encoded = Base64.encode(result.getBytes());
			String signature = new String(encoded);
			return signature;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void handleMessage(Message message) throws Fault {
		OutputStream os = message.getContent(OutputStream.class);
		if (os == null) {
			return;
		}

		final CachedOutputStream cos = (CachedOutputStream) os;

		try {
			String signature = generateSignature(cos.getBytes());
			Map<String, List<String>> headers = new HashMap<>();
			headers.put("cgl-signature", Arrays.asList(signature));
			message.put(Message.PROTOCOL_HEADERS, headers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

}

