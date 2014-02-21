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
package com.buzzcoders.yasw.widgets.map;

/**
 * Shared constants for the plugin.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public interface MapWidgetConstants {
	
	// Names for the different functions that invoked from HTML/Javascript will callback Java code 
	public static final String BROWSER_FUNCTION_ADD_MARKER = "javaCall_AddMarker";
	public static final String BROWSER_FUNCTION_REMOVE_MARKER = "javaCall_DelMarker";
	public static final String BROWSER_FUNCTION_CLEAR_MARKERS = "javaCall_ClearMarkers";
	public static final String BROWSER_FUCTION_UPDATE_MARKER_POSITION = "javaCall_UpdateMarkerPosition";
	public static final String BROWSER_FUNCTION_UPDATE_ZOOM_LEVEL = "javaCall_UpdateZoomLevel";
	public static final String BROWSER_FUNCTION_UPDATE_MAP_CENTER = "javaCall_UpdateMapCenter";
	public static final String BROWSER_FUNCTION_TEST_JAVACALL_SUPPORT = "javaCall_TestJavaCallSupport";
	public static final String BROWSER_FUNCTION_UPDATE_MAP_TYPE = "javaCall_UpdateMapType";
	
}
