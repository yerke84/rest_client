package rest_client_ignore_cert;

import static org.junit.Assert.assertEquals;

public class testRestClientIgnoreCertClass {

	@org.junit.Test
	public void test() {
		assertEquals("", restClientIgnoreCertClass.makeUniversalRequest("https://wrong.host.badssl.com", "GET", null,
				null, null, null, null));

	}

}
