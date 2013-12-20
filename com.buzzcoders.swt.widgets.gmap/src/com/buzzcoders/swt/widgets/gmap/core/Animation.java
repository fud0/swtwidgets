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
package com.buzzcoders.swt.widgets.gmap.core;

/**
 * Animations that can be played on a marker.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @see Marker
 * @see {@link https://developers.google.com/maps/documentation/javascript/reference#MarkerOptions}
 *
 */
public enum Animation {
	BOUNCE,	// Marker bounces until animation is stopped.
	DROP	// Marker falls from the top of the map ending with a small bounce.
}
