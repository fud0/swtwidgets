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
package com.buzzcoders.yasw.map.browserfunctions;

import org.eclipse.swt.browser.Browser;

import com.buzzcoders.yasw.map.support.GoogleMapSupport;

/**
 * Browser function invoked when the list of markers is supposed to be deleted.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class ClearMarkersList extends GMapEnabledFunction {
	
	public ClearMarkersList(Browser browser, String name,
			GoogleMapSupport mapSupport) {
		super(browser, name, mapSupport);
	}

	@Override
	public Object function(Object[] arguments) {
		getMapSupport().clearMarkers();
		return null;
	}
}
