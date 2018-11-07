package file.works;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

public class FileWork {

	private static final Logger LOGGER = Logger.getLogger(FileWork.class.getName());

	private FileWork() {
		super();
	}

	public static boolean downloadFromUrlToFile(String url, String location) {

		boolean ret = false;
		try {
			URL website = new URL(url);
			InputStream in = website.openStream();
			Path path = Paths.get(location);
			Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
			try {
				in.close();
			} catch (Exception ei) {
			}
			ret = true;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return ret;
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
		} catch (

		NoSuchAlgorithmException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (KeyManagementException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

	static {
		disableSslVerification();
	}
}