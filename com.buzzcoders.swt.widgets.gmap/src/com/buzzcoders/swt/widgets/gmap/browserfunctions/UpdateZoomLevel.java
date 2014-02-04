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

import com.buzzcoders.swt.widgets.gmap.support.GoogleMapSupport;

/**
 * Browser function invoked when the zoom level is updated.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class UpdateZoomLevel extends GMapEnabledFunction {

	public UpdateZoomLevel(Browser browser, String name,
			GoogleMapSupport mapSupport) {
		super(browser, name, mapSupport);
	}

	@Override
	public Object function(Object[] arguments) {
		int newZoomLevel = ((Double)arguments[0]).intValue();
		getMapSupport().setZoomLevel(newZoomLevel);		
		return null;
	}
}
