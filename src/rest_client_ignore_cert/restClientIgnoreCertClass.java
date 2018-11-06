package rest_client_ignore_cert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;

public class restClientIgnoreCertClass {

	private static final Logger LOGGER = Logger.getLogger(restClientIgnoreCertClass.class.getName());

	private restClientIgnoreCertClass() {
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

	private static void disableSslVerification() {
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[0];
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
					// Do nothing
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
					// Do nothing
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (KeyManagementException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

	static {
		disableSslVerification();
	}
}