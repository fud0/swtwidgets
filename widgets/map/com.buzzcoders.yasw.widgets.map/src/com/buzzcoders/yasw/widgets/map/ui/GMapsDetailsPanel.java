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
package com.buzzcoders.yasw.widgets.map.ui;

import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.ui.PlatformUI;

import com.buzzcoders.yasw.widgets.map.MapActivator;
import com.buzzcoders.yasw.widgets.map.MapWidgetConstants;
import com.buzzcoders.yasw.widgets.map.browserfunctions.AddNewMarker;
import com.buzzcoders.yasw.widgets.map.browserfunctions.ClearMarkersList;
import com.buzzcoders.yasw.widgets.map.browserfunctions.RemoveMarker;
import com.buzzcoders.yasw.widgets.map.browserfunctions.TestJavaCallSupport;
import com.buzzcoders.yasw.widgets.map.browserfunctions.UpdateMapCenter;
import com.buzzcoders.yasw.widgets.map.browserfunctions.UpdateMarkerPosition;
import com.buzzcoders.yasw.widgets.map.browserfunctions.UpdateZoomLevel;
import com.buzzcoders.yasw.widgets.map.core.LatLng;
import com.buzzcoders.yasw.widgets.map.core.Marker;
import com.buzzcoders.yasw.widgets.map.support.BaseJSMapSupport;
import com.buzzcoders.yasw.widgets.map.support.BaseJavaMapSupport;
import com.buzzcoders.yasw.widgets.map.support.GMapUtils;
import com.buzzcoders.yasw.widgets.map.support.JSMapSupport;
import com.buzzcoders.yasw.widgets.map.support.JavaMapSupport;

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
public class GMapsDetailsPanel {

	// Fields
	private JavaMapSupport javaMapSupport;
	private JSMapSupport jsMapSupport;
	
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
		addressLocationLbl.setText("Look for an address:");
		
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
	    		LatLng coords = GMapUtils.getAddressCoordinates(addressLocation.getText());
	    		if(coords!=null) {
					jsMapSupport.evaluateJavascript("myMap.panTo(new google.maps.LatLng("+coords.getLat()+","+coords.getLng()+"));");
					jsMapSupport.evaluateJavascript("myMap.addMarker(new google.maps.LatLng("+coords.getLat()+","+coords.getLng()+"));");
	    		}
	    		else {
	    			MessageDialog.openError(
	    					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
	    					"Geo-location error", "Unable to locate the specified address. Please verify it is correct.");
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
	    		jsMapSupport.evaluateJavascript("JAVA_TO_JAVASCRIPT_CALLED=true");
	    		jsMapSupport.setZoomLevel(mapZoom.getSelection());
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
	        	jsMapSupport.clearMarkers();
	        }
	    });
	    markersList.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		int markerIdx = markersList.getSelectionIndex();
	    		jsMapSupport.highlightMarker(markerIdx);
	    	}
		});
	    markersList.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if(e.keyCode == SWT.DEL) {
	    			int markerIdx = markersList.getSelectionIndex();
					RemoveMarker.removeMarker(markerIdx, javaMapSupport);
					jsMapSupport.evaluateJavascript("JAVA_TO_JAVASCRIPT_CALLED=true");
					jsMapSupport.removeMarker(markerIdx);
	    		}
	    	}
		});

	    sash.setWeights(new int[] {4,1});
	    
	    jsMapSupport = new BaseJSMapSupport(mapControl);
	    javaMapSupport = new DetailsPanelMapSupport(mapControl);

	    // Add the functions for Java <-> Javascript communication to the browser instance
	    new AddNewMarker(mapControl, MapWidgetConstants.BROWSER_FUNCTION_ADD_MARKER, javaMapSupport);
	    new ClearMarkersList(mapControl, MapWidgetConstants.BROWSER_FUNCTION_CLEAR_MARKERS, javaMapSupport);
	    new RemoveMarker(mapControl, MapWidgetConstants.BROWSER_FUNCTION_REMOVE_MARKER, javaMapSupport);
	    new UpdateMarkerPosition(mapControl, MapWidgetConstants.BROWSER_FUCTION_UPDATE_MARKER_POSITION, javaMapSupport);
	    new UpdateZoomLevel(mapControl, MapWidgetConstants.BROWSER_FUNCTION_UPDATE_ZOOM_LEVEL, javaMapSupport);
	    new UpdateMapCenter(mapControl, MapWidgetConstants.BROWSER_FUNCTION_UPDATE_MAP_CENTER, javaMapSupport);
	    new TestJavaCallSupport(mapControl, MapWidgetConstants.BROWSER_FUNCTION_TEST_JAVACALL_SUPPORT, javaMapSupport);
	    
	    mapControl.setUrl(MapActivator.getFileLocation("mapfiles/gmaps_library/map.html"));
	}

	class DetailsPanelMapSupport extends BaseJavaMapSupport{

		DetailsPanelMapSupport(Browser browser) {
			super(browser);
		}
		
		@Override
		public void removeMarker(int markerIndex) {
			super.removeMarker(markerIndex);
			markersList.remove(markerIndex);
		}
		
		@Override
		public void highlightMarker(int markerIdx) {
			super.highlightMarker(markerIdx);
			markersList.setSelection(markerIdx);
		}
		
		@Override
		public void updateMarkerPosition(int markerIdx, LatLng newPosition) {
			super.updateMarkerPosition(markerIdx, newPosition);
			markersList.setItem(markerIdx, newPosition.getLat() + " : " + newPosition.getLng());
		}
		
		@Override
		public void clearMarkers() {
			super.clearMarkers();
			markersList.removeAll();
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
		public void addNewMarker(Marker newMarker) {
			super.addNewMarker(newMarker);
			LatLng position = newMarker.getPosition();
			markersList.add(position.getLat() + " : " + position.getLng());
		}
		
		@Override
		public void setZoomLevel(int newZoomLevel) {
			super.setZoomLevel(newZoomLevel);
			mapZoom.setSelection(newZoomLevel);
			zoomLevelLbl.setText("Zoom: " + newZoomLevel);
		}
		
		@Override
		public int getZoomLevel() {
			return mapZoom.getSelection();
		}
		
		@Override
		public void setMapCenter(LatLng position) {
			super.setMapCenter(position);
			mapCenterCoordinatesLbl.setText("<"+position.getLat()+" , " + position.getLng() + ">");
		}
	}
}
