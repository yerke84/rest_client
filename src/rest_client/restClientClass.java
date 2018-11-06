package rest_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

public class restClientClass {

	private static final Logger LOGGER = Logger.getLogger(restClientClass.class.getName());

	private restClientClass() {
		super();
	}

	public static String makeUniversalRequest(String url, String method, String consumes, String accept,
			String authHeaderName, String authHeaderValue, String body) {

		URL restServiceURL = null;
		HttpURLConnection httpConnection = null;
		BufferedReader responseBuffer = null;
		String output;
		JSONObject ret = null;
		try {
			restServiceURL = new URL(url);
			httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod(method);
			if (authHeaderName != null && authHeaderValue != null)
				httpConnection.setRequestProperty(authHeaderName, authHeaderValue);
			if (consumes != null)
				httpConnection.setRequestProperty("Content-Type", consumes);
			if (accept != null)
				httpConnection.setRequestProperty("ACCEPT", accept);
			byte[] postDataBytes = null;
			if (body != null) {
				postDataBytes = body.getBytes("UTF-8");
				httpConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			}
			httpConnection.setDoOutput(true);
			if (postDataBytes != null)
				httpConnection.getOutputStream().write(postDataBytes);
			responseBuffer = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			while ((output = responseBuffer.readLine()) != null) {
				sb.append(output);
			}
			ret = new JSONObject(sb.toString());
			responseBuffer.close();
			httpConnection.disconnect();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			ret = new JSONObject();
			ret.put("RETCODE", -1);
			ret.put("RETTEXT", e.getMessage());
		}
		return ret.toString();
	}
}
