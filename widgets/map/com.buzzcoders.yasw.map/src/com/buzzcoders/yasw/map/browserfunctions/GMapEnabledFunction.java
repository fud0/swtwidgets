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
import org.eclipse.swt.browser.BrowserFunction;

import com.buzzcoders.yasw.map.support.GoogleMapSupport;

/**
 * A browser function that maintains a reference to the google map support component.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public abstract class GMapEnabledFunction extends BrowserFunction {

	private GoogleMapSupport mapSupport;
	
	public GMapEnabledFunction(Browser browser, String name, GoogleMapSupport mapSupport) {
		super(browser, name);
		this.mapSupport = mapSupport;
	}
	
	public GoogleMapSupport getMapSupport() {
		return mapSupport;
	}
}
