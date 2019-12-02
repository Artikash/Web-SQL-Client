// TODO: figure out how to actually test my controller

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class BasicTest {
	@Test
	public void LanguageTest() {
		assertEquals("Five", "Fi" + "ve");
	}
}
