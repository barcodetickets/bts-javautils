package net.sourceforge.barcodetickets.api;

import java.net.URLEncoder;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

/**
 * Produces BTS API request signatures according to the specification as a
 * reference implementation for API clients built in Java.
 * 
 * @license Apache License 2.0
 * @author Frederick Ding
 * @version $Id$
 */
public class Signature {
	protected String apiKey;

	public Signature() {

	}

	public Signature(String apiKey) {
		setApiKey(apiKey);
	}

	public void setApiKey(String apiKey) {
		if (apiKey.length() != 0 && apiKey != null)
			this.apiKey = apiKey;
	}

	public String generate(String httpVerb, String hostname, String uri,
			TreeMap<String, String> params) throws Exception {
		StringBuffer message = new StringBuffer();
		if (!httpVerb.equals("GET") && !httpVerb.equals("POST")
				&& !httpVerb.equals("PUT") && !httpVerb.equals("HEAD"))
			throw new Exception("Invalid HTTP verb");
		if (uri.length() == 0 || uri.charAt(0) != '/')
			throw new Exception("Invalid URI");

		// concatenate them onto the StringBuffer
		message.append(httpVerb);
		message.append(' ');
		message.append(hostname);
		message.append(uri);
		message.append('\n');

		// process parameters
		// we use TreeMap because it naturally sorts items by keys
		StringBuffer parameters = new StringBuffer();
		Set<Entry<String, String>> _params = params.entrySet();
		for (Entry<String, String> parameter : _params) {
			parameters.append(parameter.getKey());
			parameters.append('=');
			parameters.append(URLEncoder.encode(parameter.getValue(), "UTF-8"));
			parameters.append('&');
		}
		parameters.deleteCharAt(parameters.length() - 1);

		// add the parameters to the StringBuffer
		message.append(parameters);

		Mac hmac = Mac.getInstance("HmacSHA1");
		SecretKeySpec secret = new SecretKeySpec(apiKey.getBytes(), "HmacSHA1");
		hmac.init(secret);
		byte[] digest = hmac.doFinal(message.toString().getBytes());

		return new BASE64Encoder().encode(digest);
	}
}
