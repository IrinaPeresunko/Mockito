package examples;

import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import junit.framework.TestCase;

public class TestExampleVerify extends TestCase {
	@Test
	public void testMethodCallCheckSampleForVerify(){
		InterfaceForTest mockedObject = mock(InterfaceForTest.class);
		mockedObject.setStringValue("value1");
		mockedObject.actionMethod();
		verify(mockedObject).setStringValue("value1");
		verify(mockedObject).actionMethod();
	}
	@Test
	public void testMethodCallCheckSampleForAction(){
		InterfaceForTest mockedObject = mock(InterfaceForTest.class);
		when(mockedObject.getStringValue()).thenReturn("expectedValue");
		when(mockedObject.processMethod("input1")).thenReturn("value1");
		when(mockedObject.processMethod("input3")).thenThrow(new RuntimeException());
		
		assertEquals("value1",mockedObject.processMethod("input1"));
		try{
			mockedObject.processMethod("input3");
		}catch(RuntimeException e){
			System.out.println("generated runtime exception");
		}
		assertNull(mockedObject.processMethod("not mocked value"));
		assertEquals("get String Value","expectedValue",mockedObject.getStringValue());	
	}
	public void testMethodCallCheckSampleForStubbing(){
		InterfaceForTest mockedObject = mock(InterfaceForTest.class);
		when(mockedObject.processMethod(anyString())).thenAnswer(new Answer<String>(){
			public String answer(InvocationOnMock invocation){
				Object[] args = invocation.getArguments();
				return "called with arguments "+args[0];
			}
		});
		String returnValue = mockedObject.processMethod(String.valueOf(System.currentTimeMillis()));
		System.out.println(returnValue);
	}
	public void testMethodCallCheckSampleForMatcher(){
		InterfaceForTest mockedObject = mock(InterfaceForTest.class);
		Matcher<String> matcher = new ArgumentMatcher<String>(){
		@Override
			public boolean matches(Object argument){
				return argument.equals("expectedValue");
				//return argument.equals("EXPECTED_VALUE");
			}
		};
		when(mockedObject.processMethod(argThat(matcher))).thenReturn("returnValue");
		assertEquals("returnValue",mockedObject.processMethod("expectedValue"));
		assertNull(mockedObject.processMethod("not mocked value"));
	}
}
