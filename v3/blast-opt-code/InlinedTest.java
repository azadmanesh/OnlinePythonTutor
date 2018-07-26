import org.junit.*;
import static org.junit.Assert.*;

import java.util.HashMap;

/*
 * Do not rename the class!
 * Do not rename the method!
 * Do not add any further test!
 */
public class InlinedTest {
	
	@Test
	public void test() {
		int[][] a = {{1,2,3},{4,5,6}};
		int[][] b = {{7,8,9,10},{11,12,13,14},{15,16,17,18}};

		multiply(a, b);

	} 

	static int[][] multiply(int[][] a, int[][] b){
		int[][] c = new int[a.length][b[0].length];
		int lll = 10;
		int val = Integer.MAX_VALUE + lll;
		for (int i = 0; i < a.length; i++){
			for (int j = 0; j < b[i].length; j++){
				int temp = 0;
				for (int k = 0; k < a.length; k++){
					temp += a[i][k] * b[k][j];
				}
				c[i][j] = temp; 
			}
		}

		return c;
	}
}


//return rhs().stream().filter(e-> memLocMap(e).getCategory() == LocationCategory.ARRAY_ELEMENT).collect(Collectors.toList());

//return ops()
//.stream()
//.filter(e -> e.getEventClassCode() == EventConstants.BINARY_EVENT &&
//        isInt(lhs(e)) && intValue(lhs(e)) < 0 &&
//rhs(e)
//                .stream()
//				.filter(ee -> isInt(ee) && intValue(ee) > 0)
//				.collect(Collectors.toList())
//				.size() == rhs(e).size()
//				)
//.collect(Collectors.toList()); 