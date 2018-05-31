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
		f(4);
	}
	
	public int f(int arg) {
		if (arg == 0) {
			return 1;
		} else {
			return f(arg - 1);
		}
	}

	public int g(int arg) {
		if (arg == 0) {
			return 1;
		} else {
			return f(arg - 1);
		}
	}
	
	public int h(int arg) {
		if (arg == 0) {
			return 1;
		} else {
			return f(arg - 1);
		}
	}


}