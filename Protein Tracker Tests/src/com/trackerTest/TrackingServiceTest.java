package com.trackerTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.concurrent.Synchroniser;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;

import com.tracker.HistoryItem;
import com.tracker.InvalidGoalException;
import com.tracker.Notifier;
import com.tracker.NotifierStub;
import com.tracker.TrackingService;

public class TrackingServiceTest {
	private TrackingService service;
	@BeforeClass
	public static void beforeClass()
	{
		//System.out.println("Before Class"); 
	}
	
	@AfterClass
	public static void afterClass()
	{
		//System.out.println("After Class");
	}
	@Before
	public void setup()
	{
		//System.out.println("Before"); 
		service = new TrackingService(new NotifierStub());
	}
	
	@Test 
	@Category(GoodTestsCategory.class)
	public void testTotalIsZero()
	{
		assertEquals("Tracking service total wasn't zero", 0, service.getTotal());
	}
	
	@Test
	@Category(GoodTestsCategory.class)
	//@Ignore
	public void testAddProtein()
	{
		service.addProtein(10);
		assertEquals(10, service.getTotal());
		assertThat(service.getTotal(), is(10));
		assertThat(service.getTotal(), allOf(is(10), instanceOf(Integer.class)));
	}
	
	@Test
	@Category(GoodTestsCategory.class)
	public void testRemoveProtein()
	{
		service.removeProtein(10);
		assertEquals(0, service.getTotal());
	}
	
	/* Expected Exceptions */
	@Rule
	public ExpectedException thrown = ExpectedException.none(); 
	@Test /*(expected = InvalidGoalException.class)*/
	public void goalLessThanZero() throws InvalidGoalException
	{
		thrown.expect(InvalidGoalException.class);
		//thrown.expectMessage("Goal was less than zero.");
		thrown.expectMessage(containsString("zero"));
		service.setGoal(-2);
	}
	
	@Rule 
	public Timeout timeout = new Timeout(2000);
	@Test //(timeout = 200)
	public void badTest()
	{
		for (int i = 1; i < 1000000; i++)
			service.addProtein(1);
	}
	
	@Test
	public void goalMetHistoryUpdated() throws InvalidGoalException
	{
	    Mockery context = new Mockery(){{
	        setThreadingPolicy(new Synchroniser());
	    }};
		final Notifier mockNotifier = context.mock(Notifier.class);
		service = new TrackingService(mockNotifier);
		context.checking(new Expectations(){{
			oneOf(mockNotifier).send("Goal Met!");
			will(returnValue(true));
		}});
		
		service.setGoal(5);
		service.addProtein(6);
		
		HistoryItem result = service.getHistory().get(1);
		assertEquals("sent:goal met", result.getOperation());
		
		context.assertIsSatisfied();
	}
	@After
	public void tearDown()
	{
		//System.out.println("After");
	}
	
}
