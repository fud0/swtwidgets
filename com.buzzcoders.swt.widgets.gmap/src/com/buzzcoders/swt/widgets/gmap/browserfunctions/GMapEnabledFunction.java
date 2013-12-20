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
import org.eclipse.swt.browser.BrowserFunction;

import com.buzzcoders.swt.widgets.gmap.support.GoogleMapSupport;

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
