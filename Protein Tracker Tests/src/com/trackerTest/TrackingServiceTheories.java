package com.trackerTest;

import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.junit.Assume;

import com.tracker.NotifierStub;
import com.tracker.TrackingService;

import org.junit.experimental.theories.*;

@RunWith(Theories.class)
public class TrackingServiceTheories {
	@DataPoints
	public static int[] data(){
		return new int[]{
				1, 5, 10, 15, 22, -4, -22
		};
	}
	
	@Theory
	public void postiveInpuGivesPositiveTotal(int value)
	{
		TrackingService service = new TrackingService(new NotifierStub());
		service.addProtein(value);
		// Ignores a negative number
		Assume.assumeTrue(value > 0);
		assertTrue(service.getTotal() > 0);
	}
	
}
