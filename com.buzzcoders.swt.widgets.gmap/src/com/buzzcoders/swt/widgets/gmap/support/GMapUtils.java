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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.buzzcoders.swt.widgets.gmap.core.Animation;
import com.buzzcoders.swt.widgets.gmap.core.LatLng;
import com.buzzcoders.swt.widgets.gmap.core.MarkerOptions;
import com.buzzcoders.swt.widgets.gmap.ui.GMapsDetailsPanel;

public class GMapUtils {
	
	public static final int OPTIONS_LAT_INDEX = 0;
	public static final int OPTIONS_LNG_INDEX = 1;
	public static final int OPTIONS_DRAGGABLE_INDEX = 2;
	public static final int OPTIONS_ANIMATION_INDEX = 3;
	public static final int OPTIONS_VISIBLE_INDEX = 4;
	public static final int OPTIONS_CLICKABLE_INDEX = 5;

	public static MarkerOptions getMarkerOptions(Object[] arguments) {
		MarkerOptions options = new MarkerOptions();
		// LatLng
		options.setPosition(new LatLng(
				(Double) arguments[OPTIONS_LAT_INDEX], 
				(Double) arguments[OPTIONS_LNG_INDEX]));
		// Draggable
		options.setDraggable((Boolean)arguments[OPTIONS_DRAGGABLE_INDEX]);
		// Animation
		options.setAnimation(getMarkerAnimation((Double)arguments[OPTIONS_ANIMATION_INDEX]));
		// Visible
		options.setVisible((Boolean)arguments[OPTIONS_VISIBLE_INDEX]);
		// Clickable
		options.setClickable((Boolean)arguments[OPTIONS_CLICKABLE_INDEX]);
		return options;
	}
	
	public static LatLng getPosition(Object[] arguments) {
		return new LatLng((Double) arguments[0],(Double) arguments[1]);
	}

	public static Animation getMarkerAnimation(Double value) {
		if(value!=null){
			switch (value.intValue()) {
			case 1:
				// google.maps.Animation.BOUNCE	=> 1
				return Animation.BOUNCE;
			case 2:
				// google.maps.Animation.DROP	=> 2
				return Animation.DROP;
			default:
				return null;
			}
		}
		return null;
	}

	/**
	 * Opens in a separate {@link Shell} the panel containing the Google Map
	 * controls.
	 */
	public static void openSample() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new GMapsDetailsPanel(shell, SWT.NONE);
		shell.open();
	}
}
