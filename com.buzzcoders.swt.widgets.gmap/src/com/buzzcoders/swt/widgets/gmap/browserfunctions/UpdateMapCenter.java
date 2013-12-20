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
package com.buzzcoders.swt.widgets.gmap.browserfunctions;

import org.eclipse.swt.browser.Browser;

import com.buzzcoders.swt.widgets.gmap.core.LatLng;
import com.buzzcoders.swt.widgets.gmap.support.GMapUtils;
import com.buzzcoders.swt.widgets.gmap.support.GoogleMapSupport;

public class UpdateMapCenter extends GMapEnabledFunction {

	public UpdateMapCenter(Browser browser, String name,
			GoogleMapSupport mapSupport) {
		super(browser, name, mapSupport);
	}

	@Override
	public Object function(Object[] arguments) {
		LatLng newPosition = GMapUtils.getPosition(arguments);
		getMapSupport().setMapCenter(newPosition);
		return null;
	}
}
