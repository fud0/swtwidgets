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
package com.buzzcoders.yasw.widgets.map.support;

import java.util.List;

import org.eclipse.swt.browser.Browser;

import com.buzzcoders.yasw.widgets.map.core.LatLng;
import com.buzzcoders.yasw.widgets.map.core.MapType;
import com.buzzcoders.yasw.widgets.map.core.Marker;

/**
 * Basic implementation of the support to the map component (Javascript side).
 * For most use case there will be no need for further customization.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class BaseJSMapSupport implements JSMapSupport {
	
	private Browser browser;
	private String mapId;
	
	public BaseJSMapSupport(Browser browser){
		this(browser,"myMap");
	}
	
	public BaseJSMapSupport(Browser browser, String mapId) {
		this.browser = browser;
		this.mapId = mapId;
	}

	@Override
	public LatLng getMapCenter() {
		return new LatLng(
				(Double) getBrowserControl().evaluate("return "+mapId+".getCenter().lat();"), 
				(Double) getBrowserControl().evaluate("return "+mapId+".getCenter().lng();"));
	}

	@Override
	public void setMapCenter(LatLng position) {
		getBrowserControl().evaluate(mapId+".setCenter("+position.getLat()+","+position.getLng()+");");
	}

	@Override
	public int getZoomLevel() {
		return ((Double) 
				getBrowserControl().evaluate("return "+mapId+".getZoom();")).intValue();
	}

	@Override
	public void setZoomLevel(int newZoomLevel) {
		getBrowserControl().evaluate(mapId+".setZoom("+newZoomLevel+");");
	}

	@Override
	public void addNewMarker(Marker newMarker) {
		// FIXME - we currently miss to create a marker options array.
		String markerPosition = "new google.maps.LatLng("
				+ newMarker.getPosition().getLat() + ","
				+ newMarker.getPosition().getLng() + ")";
		getBrowserControl().evaluate(mapId+".addMarker("+markerPosition+");");
	}

	@Override
	public void removeMarker(Marker oldMarker) {
		// TODO - A check on position could be fine?!
		throw new UnsupportedOperationException("This method is not supported yet.");
	}

	@Override
	public void removeMarker(int markerIndex) {
		getBrowserControl().evaluate(mapId+".removeMarkerByIndex("+markerIndex+")");
	}

	@Override
	public void clearMarkers() {
		getBrowserControl().evaluate(mapId+".clearAllMarkers();");
	}

	@Override
	public List<Marker> getMarkers() {
		// TODO - we could get back an XML representation of the markers as string
		//		  and later convert them.
		throw new UnsupportedOperationException("This method is not supported yet.");
	}

	@Override
	public int getMarkersNum() {
		return ((Double) getBrowserControl().evaluate(
				"return " + mapId + ".mapMarkers.length;")).intValue();
	}

	@Override
	public void updateMarkerPosition(int markerIdx, LatLng newPosition) {
		String newPositionStr = "new google.maps.LatLng("
				+ newPosition.getLat() + "," + newPosition.getLng() + ")";
		getBrowserControl().evaluate(mapId+".mapMarkers["+markerIdx+"].setPosition("+newPositionStr+");");
	}

	@Override
	public void highlightMarker(int markerIdx) {
		getBrowserControl().evaluate(mapId+".bounceMarker("+markerIdx+");");
	}

	@Override
	public Browser getBrowserControl() {
		return this.browser;
	}

	@Override
	public void setMapType(MapType mapType) {
		getBrowserControl().evaluate(mapId+".setMapType("+mapType.getGoogleConstant()+");");
	}

	@Override
	public MapType getMapType() {
		return MapType.fromStringID(
				(String) getBrowserControl().evaluate("return "+mapId+".getMapType();"));
	}

	@Override
	public Object evaluateJavascript(String snippet) {
		return getBrowserControl().evaluate(snippet);
	}

}
