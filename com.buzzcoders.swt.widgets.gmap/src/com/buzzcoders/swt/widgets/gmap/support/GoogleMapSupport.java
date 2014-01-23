/*****************************************************************************************
 * Copyright (C) 2013 Massimo Rabbi. All rights reserved.
 * http://www.buzzcoders.com
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Massimo Rabbi <mrabbi@users.sourceforge.net> - initial API and implementation
 *****************************************************************************************/
package com.buzzcoders.swt.widgets.gmap.support;

import java.util.List;

import org.eclipse.swt.browser.Browser;

import com.buzzcoders.swt.widgets.gmap.core.LatLng;
import com.buzzcoders.swt.widgets.gmap.core.MapType;
import com.buzzcoders.swt.widgets.gmap.core.Marker;

/**
 * Clients that want to interact with the Google Map, should implement this
 * interface in order to support the allowed operations for bidirectional
 * Java/Javascript communication.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 */
public interface GoogleMapSupport {

	LatLng getMapCenter();

	void setMapCenter(LatLng position);

	int getZoomLevel();

	void setZoomLevel(int newZoomLevel);
	
	void addNewMarker(Marker newMarker);
	
	void removeMarker(Marker oldMarker);
	
	void removeMarker(int markerIndex);
	
	void clearMarkers();
	
	List<Marker> getMarkers();
	
	int getMarkersNum();
	
	void updateMarkerPosition(int markerIdx, LatLng newPosition);
	
	void highlightMarker(int markerIdx);

	Browser getBrowserControl();
	
	void setMapType(MapType mapType);
	
	MapType getMapType();

	// TODO - Add more???
}
