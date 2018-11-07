package file.works;

import static org.junit.Assert.assertEquals;

public class testFile {

	@org.junit.Test

	// https://github.com/yerke84/rest_client/blob/master/testFile/%D0%9D%D1%83%D1%80%D0%BB%D1%8B%20%D0%B6%D0%B5%D1%80.xlsx?raw=true
	public void test() {
		String url = "https://github.com/yerke84/rest_client/blob/master/testFile/%D0%9D%D1%83%D1%80%D0%BB%D1%8B%20%D0%B6%D0%B5%D1%80.xlsx?raw=true";
		assertEquals("", FileWork.downloadFromUrlToFile(url, "/tmp/01.xlsx"));

	}

}
