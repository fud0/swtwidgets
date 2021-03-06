/*******************************************************************************
 * Copyright (c) 2014 Massimo Rabbi.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Massimo Rabbi <mrabbi@users.sourceforge.net> - initial API and implementation
 ******************************************************************************/
package com.buzzcoders.yasw.widgets.map.browserfunctions;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.browser.Browser;

import com.buzzcoders.yasw.widgets.map.support.JavaMapSupport;
import com.buzzcoders.yasw.widgets.map.support.MapSupport;

/**
 * Browser function invoked when a marker is removed from the map.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class RemoveMarker extends GMapEnabledFunction {
	
    public RemoveMarker(Browser browser, String name,
    		JavaMapSupport mapSupport) {
		super(browser, name, mapSupport);
	}

	@Override
    public Object function (Object[] arguments) {
        int markerIdx = ((Double) arguments[0]).intValue();
        removeMarker(markerIdx, getMapSupport());
        return null;
    }
    
    public static void removeMarker(int markerIndex, MapSupport mapSupport) {
    	Assert.isNotNull(mapSupport);
    	Assert.isTrue(markerIndex>=0 && markerIndex<mapSupport.getMarkersNum());
    	mapSupport.removeMarker(markerIndex);
    }
}
