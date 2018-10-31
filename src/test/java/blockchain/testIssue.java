package blockchain;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.tencent.trustsql.sdk.exception.TrustSDKException;

public class testIssue {

	@Test
	public void test() throws UnsupportedEncodingException, TrustSDKException, Exception {
	
		Timestamp stamp = new Timestamp(1540345353000L);
		Date date = new Date(stamp.getTime());
		System.out.println(date);
		SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd'   'HH:mm:ss");
		System.out.println(simpleFormatter.format(date));

	}

}
