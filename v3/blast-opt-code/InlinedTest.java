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
//		f(5);
		int i = 5;
		int sum = 1;
		while (i >= 0) {
			sum+= i;
			i--;
		}
	}
	
	public int f(int arg) {
		if (arg == 0) {
			return 1;
		} else {
			return f(arg - 1);
		}
	}

}