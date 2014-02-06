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
package com.buzzcoders.yasw.map.ui;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

import com.buzzcoders.yasw.map.MapActivator;
import com.buzzcoders.yasw.map.MapWidgetConstants;
import com.buzzcoders.yasw.map.browserfunctions.AddNewMarker;
import com.buzzcoders.yasw.map.browserfunctions.ClearMarkersList;
import com.buzzcoders.yasw.map.browserfunctions.RemoveMarker;
import com.buzzcoders.yasw.map.browserfunctions.TestJavaCallSupport;
import com.buzzcoders.yasw.map.browserfunctions.UpdateMapCenter;
import com.buzzcoders.yasw.map.browserfunctions.UpdateMarkerPosition;
import com.buzzcoders.yasw.map.browserfunctions.UpdateZoomLevel;
import com.buzzcoders.yasw.map.core.LatLng;
import com.buzzcoders.yasw.map.core.MapType;
import com.buzzcoders.yasw.map.core.Marker;
import com.buzzcoders.yasw.map.support.GoogleMapSupport;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class implements the support for the Google Map component. The panel
 * that is shown contains:
 * <ul>
 * <li>a browser with the Google Maps component loaded</li>
 * <li>an additional panel with controls that allows the interaction with the
 * Google Maps in the browser</li>
 * </ul>
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 */
public class GMapsDetailsPanel implements GoogleMapSupport {

	// Fields
	private java.util.List<Marker> markers;
	
	// SWT widgets
	private Browser mapControl;
	private List markersList;
	private Text addressLocation;
	private Button goToBtn;
	private Scale mapZoom;
	private Label zoomLevelLbl;
	private Label mapCenterCoordinatesLbl;

	/**
	 * Creates a new panel containing the controls to work with a 
	 * Google Maps component presented inside a browser instance.
	 * 
	 * @param parent a composite control which will be the parent of the new instance (cannot be null)
	 * @param style the style of widget to construct
	 */
	public GMapsDetailsPanel(Composite parent, int style) {
	    SashForm sash = new SashForm(parent, style | SWT.HORIZONTAL);
	    
	    try {
	    	mapControl = new Browser(sash, SWT.NONE);
	    	mapControl.addControlListener(new ControlListener() {
	 
	          public void controlResized(ControlEvent e) {
	        	  mapControl.execute("document.getElementById('map_canvas').style.width= "+ (mapControl.getSize().x - 20) + ";");
	        	  mapControl.execute("document.getElementById('map_canvas').style.height= "+ (mapControl.getSize().y - 20) + ";");
	          }
	 
	          public void controlMoved(ControlEvent e) {
	          }
	    });
	    } catch (SWTError e) {
	        MapActivator.logError("Could not instantiate the browser widget: ", e);
	        return;
	    }
		
		Composite containerCmp = new Composite(sash, SWT.BORDER);
		containerCmp.setLayout(new GridLayout(1,true));
		
		Label mapCenterLbl = new Label(containerCmp,SWT.NONE);
		mapCenterLbl.setText("Map Center:");
		
		mapCenterCoordinatesLbl = new Label(containerCmp,SWT.NONE);
		mapCenterCoordinatesLbl.setText("<?,?>");
		mapCenterCoordinatesLbl.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
		
		Label addressLocationLbl = new Label(containerCmp, SWT.NONE);
		addressLocationLbl.setText("Look for an ddress:");
		
	    Group searchGrp = new Group(containerCmp,SWT.BORDER);
	    searchGrp.setLayout(new GridLayout(2,false));
	    searchGrp.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
	    
	    addressLocation = new Text(searchGrp, SWT.BORDER);
	    addressLocation.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,2,1));
	    
	    goToBtn=new Button(searchGrp, SWT.PUSH);
	    goToBtn.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,true,false,2,1));
	    goToBtn.setText("Find");
	    goToBtn.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		try {
	    			String addressUrlEncoded = URLEncoder.encode(addressLocation.getText(), "UTF-8");
	    			String locationFindURL = "http://maps.google.com/maps/api/geocode/json?sensor=false&address="+addressUrlEncoded;
	    			HttpClient client = new HttpClient();
	    			GetMethod locateAddressGET = new GetMethod(locationFindURL);
	    			int httpRetCode = client.executeMethod(locateAddressGET);
	    			if(httpRetCode == HttpStatus.SC_OK){
		    			String responseBodyAsString = locateAddressGET.getResponseBodyAsString();
						ObjectMapper mapper = new ObjectMapper();
						mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
						mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
						mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
						JsonNode jsonRoot = mapper.readTree(responseBodyAsString);
						JsonNode location = jsonRoot.path("results").get(0).path("geometry").path("location");
						JsonNode lat = location.get("lat");
						JsonNode lng = location.get("lng");
						GMapsDetailsPanel.this.mapControl.evaluate("myMap.panTo(new google.maps.LatLng("+lat.asText()+","+lng.asText()+"));");
						GMapsDetailsPanel.this.mapControl.evaluate("myMap.addMarker(new google.maps.LatLng("+lat.asText()+","+lng.asText()+"));");
	    			}
	    			locateAddressGET.releaseConnection();
	    			client.getState().clear();
				} catch (Exception ex) {
					ex.printStackTrace();
				}    		
	    	}
		});
	    
	    zoomLevelLbl = new Label(containerCmp, SWT.NONE);
	    zoomLevelLbl.setText("Zoom");
	    zoomLevelLbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	    
	    mapZoom = new Scale(containerCmp, SWT.NONE);
	    mapZoom.setMinimum(0);
	    mapZoom.setMaximum(19);
	    mapZoom.setIncrement(1);
	    mapZoom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	    mapZoom.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		GMapsDetailsPanel.this.mapControl.evaluate("JAVA_TO_JAVASCRIPT_CALLED=true");
	    		GMapsDetailsPanel.this.mapControl.evaluate("myMap.setZoom("+mapZoom.getSelection()+");");
	    		zoomLevelLbl.setText("Zoom: " + mapZoom.getSelection());
	    	}
		});

	    Label markersLbl = new Label(containerCmp,SWT.NONE);
	    markersLbl.setText("Markers");
	    markersLbl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	    
	    markersList = new List(containerCmp, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	    markersList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    Button b = new Button(containerCmp, SWT.PUSH);
	    b.setLayoutData(new GridData(SWT.RIGHT,SWT.FILL,false,false));
	    b.setText("Delete markers");
	    b.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	GMapsDetailsPanel.this.mapControl.evaluate("myMap.clearAllMarkers();");
	        }
	    });
	    markersList.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		int markerIdx = markersList.getSelectionIndex();
	    		GMapsDetailsPanel.this.mapControl.evaluate("myMap.bounceMarker("+markerIdx+");");
	    	}
		});
	    markersList.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if(e.keyCode == SWT.DEL) {
	    			int markerIdx = markersList.getSelectionIndex();
					RemoveMarker.removeMarker(markerIdx, GMapsDetailsPanel.this);
					GMapsDetailsPanel.this.mapControl.evaluate("JAVA_TO_JAVASCRIPT_CALLED=true");
					GMapsDetailsPanel.this.mapControl.evaluate("myMap.removeMarkerByIndex("+markerIdx+");");
	    		}
	    	}
		});

	    sash.setWeights(new int[] {4,1});

	    // Add the functions for Java <-> Javascript communication to the browser instance
	    new AddNewMarker(mapControl, MapWidgetConstants.BROWSER_FUNCTION_ADD_MARKER, this);
	    new ClearMarkersList(mapControl, MapWidgetConstants.BROWSER_FUNCTION_CLEAR_MARKERS, this);
	    new RemoveMarker(mapControl, MapWidgetConstants.BROWSER_FUNCTION_REMOVE_MARKER, this);
	    new UpdateMarkerPosition(mapControl, MapWidgetConstants.BROWSER_FUCTION_UPDATE_MARKER_POSITION, this);
	    new UpdateZoomLevel(mapControl, MapWidgetConstants.BROWSER_FUNCTION_UPDATE_ZOOM_LEVEL, this);
	    new UpdateMapCenter(mapControl, MapWidgetConstants.BROWSER_FUNCTION_UPDATE_MAP_CENTER, this);
	    new TestJavaCallSupport(mapControl, MapWidgetConstants.BROWSER_FUNCTION_TEST_JAVACALL_SUPPORT, this);
	    
	    mapControl.setUrl(MapActivator.getFileLocation("mapfiles/gmaps_library/map.html"));
	}

	@Override
	public LatLng getMapCenter() {
		return new LatLng(
				(Double) GMapsDetailsPanel.this.mapControl.evaluate("return myMap.getCenter().lat()"), 
				(Double) GMapsDetailsPanel.this.mapControl.evaluate("return myMap.getCenter().lng()"));
	}

	@Override
	public void setMapCenter(LatLng position) {
		mapCenterCoordinatesLbl.setText("<"+position.getLat()+" , " + position.getLng() + ">");
	}

	@Override
	public int getZoomLevel() {
		return mapZoom.getSelection();
	}

	@Override
	public void setZoomLevel(int newZoomLevel) {
		mapZoom.setSelection(newZoomLevel);
		zoomLevelLbl.setText("Zoom: " + newZoomLevel);
	}

	@Override
	public void addNewMarker(Marker newMarker) {
		getMarkers().add(newMarker);
		LatLng position = newMarker.getPosition();
		markersList.add(position.getLat() + " : " + position.getLng());
	}

	@Override
	public void removeMarker(Marker oldMarker) {
		int mIdx = getMarkers().indexOf(oldMarker);
		if(mIdx>0){
			getMarkers().remove(mIdx);
			markersList.remove(mIdx);
		}
		else {
			// FIXME do nothing or raise error (at least log)?!
		}
	}

	@Override
	public void clearMarkers() {
		getMarkers().clear();
		markersList.removeAll();
	}

	@Override
	public java.util.List<Marker> getMarkers() {
		if(markers==null){
			markers = new ArrayList<Marker>();
		}
		return markers;
	}

	@Override
	public void updateMarkerPosition(int markerIdx, LatLng newPosition) {
		Marker marker = getMarkers().get(markerIdx);
		marker.setPosition(newPosition);
		markersList.setItem(markerIdx, newPosition.getLat() + " : " + newPosition.getLng());
	}

	@Override
	public void highlightMarker(int markerIdx) {
		markersList.setSelection(markerIdx);
	}

	@Override
	public void removeMarker(int markerIndex) {
		getMarkers().remove(markerIndex);
		markersList.remove(markerIndex);
	}

	@Override
	public int getMarkersNum() {
		return getMarkers().size();
	}

	@Override
	public Browser getBrowserControl() {
		return mapControl;
	}

	@Override
	public void setMapType(MapType mapType) {
		GMapsDetailsPanel.this.mapControl.evaluate("myMap.setMapType("+mapType.getGoogleConstant()+");");
	}

	@Override
	public MapType getMapType() {
		return MapType.fromStringID(
				(String) GMapsDetailsPanel.this.mapControl.evaluate("return myMap.getMapType();"));
	}

}
