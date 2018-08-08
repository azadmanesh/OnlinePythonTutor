import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.lang.reflect.*;

/*
 * Do not rename the class!
 * Do not rename the method!
 * Do not add any further test!
 */
public class InlinedTest {
	
	@Test
	public void test() {
		InlinedTest i = new InlinedTest();
		i.f();
	}
	
	public void f() {
		try {
			this.getClass().getField("name");
		} catch (NoSuchFieldException | SecurityException e) {
			int a = 12;
			int b = 14;
			System.out.println(a + b);
		}
    }
}
//		int[][] a = {{1,2,3},{4,5,6}};
//		int[][] b = {{7,8,9,10},{11,12,13,14},{15,16,17,18}};
//
//		int[][] c = multiply(a, b);
//		assert c[1][3] == 218;
//	} 
//
//	static int[][] multiply(int[][] a, int[][] b){
//		int[][] c = new int[a.length][b[0].length];
//		
//		for (int i = 0; i < a.length; i++){
//			for (int j = 0; j < b[i].length; j++){
//				int temp = 0;
//				for (int k = 0; k < a.length; k++){
//					temp += a[i][k] * b[k][j];
//				}
//				c[i][j] = temp; 
//			}
//		}
//
//		return c;
//	}
//}



















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