package rest_client;

import static org.junit.Assert.assertEquals;

public class testRestClientClass {

	@org.junit.Test
	public void test() {
		assertEquals("", restClientClass.makeUniversalRequest("https://wrong.host.badssl.com", "GET", null, null, null,
				null, null));

	}

}
