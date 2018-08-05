package de.frittenburger.tracking;
/*
 *  Copyright notice
 *
 *  (c) 2016 Dirk Friedenberger <projekte@frittenburger.de>
 *
 *  All rights reserved
 *
 *  This script is part of the JHttpSwitch project. The JHttpSwitch is
 *  free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  The GNU General Public License can be found at
 *  http://www.gnu.org/copyleft/gpl.html.
 *
 *  This script is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  This copyright notice MUST APPEAR in all copies of the script!
 */
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.io.interfaces.PersistenceService;
import de.frittenburger.tracking.bo.TrackingPoint;
import de.frittenburger.tracking.impl.TrackingQueueImpl;
import de.frittenburger.tracking.interfaces.TrackingPointCalculator;
import de.frittenburger.tracking.interfaces.TrackingQueue;

public class TestTrackingQueueImpl {


	@Test
	public void testInit() throws IOException, SQLException {
		
		String path = "path";
		
		
		TrackingPointCalculator trackingPointCalculator = mock(TrackingPointCalculator.class);
		PersistenceService persistenceService = mock(PersistenceService.class);

		when(persistenceService.listFiles(eq(path), any(String.class))).thenReturn(new String[]{ "file"});
		
		when(persistenceService.readLines(any(String.class))).thenReturn(Arrays.asList(new String[]{new ObjectMapper().writeValueAsString(new TrackingPoint())}));
		
		TrackingQueue queue = new TrackingQueueImpl(trackingPointCalculator,persistenceService);
		
		queue.init(path,true);
		
		verify(trackingPointCalculator).calculate(any(TrackingPoint.class));
		verify(trackingPointCalculator).aggregate();

		
	}

	@Test
	public void testEnqueue() throws IOException, SQLException, InterruptedException {
		
		String path = "path";
		
		TrackingPoint trackingPoint = new TrackingPoint();
		trackingPoint.setDate(new Date());
		
		TrackingPointCalculator trackingPointCalculator = mock(TrackingPointCalculator.class);
		PersistenceService persistenceService = mock(PersistenceService.class);

		
		TrackingQueue queue = new TrackingQueueImpl(trackingPointCalculator,persistenceService);
		
		queue.init(path,false);
		
		((TrackingQueueImpl)queue).start();
		queue.enqueue(trackingPoint);

		Thread.sleep(2000); //wait - let start Thread
		((TrackingQueueImpl)queue).interrupt();
		((TrackingQueueImpl)queue).join();
		
		verify(trackingPointCalculator).calculate(trackingPoint);
		verify(trackingPointCalculator).aggregate();
		verify(persistenceService).appendLine(any(String.class), any(String.class));
		
		

	}
}
