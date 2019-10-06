import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class BasicTest {
	@Test
	public void LanguageTest() {
		assertEquals("Five", "Fi" + "ve");
	}

	@Test
	public void ClassTest() {
		assertEquals(Application.Add(3, 4), 7);
	}
}
