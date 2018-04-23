import org.junit.*;
import static org.junit.Assert.*;

/*
 * Do not rename the class!
 * Do not rename the method!
 * Do not add any further test!
 */
public class InlinedTest {
	
	@Test
	public void test() {
		int a = 12;
		if (a < 200) {
			a = 234;
		}
		int b = a;
	}
	
}