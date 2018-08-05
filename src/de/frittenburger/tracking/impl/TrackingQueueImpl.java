package de.frittenburger.tracking.impl;
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

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.io.interfaces.PersistenceService;
import de.frittenburger.tracking.bo.TrackingPoint;
import de.frittenburger.tracking.interfaces.TrackingPointCalculator;
import de.frittenburger.tracking.interfaces.TrackingQueue;

public class TrackingQueueImpl extends Thread implements TrackingQueue {

	private final Logger logger = Logger.getLogger(this.getClass());

	private List<TrackingPoint> queue = new ArrayList<TrackingPoint>();

	private String path = null;

	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");

	private final TrackingPointCalculator trackingPointCalculator;

	private final PersistenceService persistenceService;

	public TrackingQueueImpl(TrackingPointCalculator trackingPointCalculator, PersistenceService persistenceService) {
		this.trackingPointCalculator = trackingPointCalculator;
		this.persistenceService = persistenceService;
	}

	@Override
	public void init(String path, boolean reinitDatabase) throws IOException, SQLException {

		this.path = path;

		if (!reinitDatabase)
			return;

		// read files in Tracking Directory
		String files[] = persistenceService.listFiles(path, "tracking_.*[.]txt$");

		Arrays.sort(files);

		ObjectMapper mapper = new ObjectMapper();

		for (int f = files.length - 1; f >= 0; f--) {

			List<String> lines = persistenceService.readLines(path + "/" + files[f]);

			for (int ix = lines.size() - 1; ix >= 0; ix--) {
				String line = lines.get(ix);

				TrackingPoint p = mapper.readValue(line, TrackingPoint.class);
				trackingPointCalculator.calculate(p);

			}
		}

		trackingPointCalculator.aggregate();

	}

	private void persist(TrackingPoint trackingPoint) throws IOException {

		String filename = path + "/tracking_" + simpleDateFormat.format(trackingPoint.getDate()) + ".txt";
		ObjectMapper mapper = new ObjectMapper();
		persistenceService.appendLine(filename, mapper.writeValueAsString(trackingPoint));

	}

	@Override
	public void run() {
		
		logger.debug("Start TrackingQueueImpl");
		
		try {
			while (true) {
				int cnt = 0;
				synchronized (queue) {
					cnt = queue.size();
				}

				if (cnt <= 0) {
					Thread.sleep(1000);
					continue;
				}

				for (int i = 0; i < cnt; i++) {
					TrackingPoint trackingPoint = null;
					synchronized (queue) {
						trackingPoint = queue.remove(0);
					}

					try {
						persist(trackingPoint);
						trackingPointCalculator.calculate(trackingPoint);
					} catch (IOException e) {
						logger.error(e);
					} catch (SQLException e) {
						logger.error(e);
					}

				}

				try {
					trackingPointCalculator.aggregate();
				} catch (SQLException e) {
					logger.error(e);
				} catch (IOException e) {
					logger.error(e);
				}
			}
		} catch (InterruptedException e) {
			logger.error(e);
		}
		logger.warn("Stop TrackingQueueImpl (should never happens)");

	}

	@Override
	public void enqueue(TrackingPoint trackingPoint) {

		synchronized (queue) {
			queue.add(trackingPoint);
		}

	}

}
