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
package com.buzzcoders.swt.widgets.gmap;

import java.io.IOException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MapActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.buzzcoders.swt.widgets.gmap"; //$NON-NLS-1$

	// The shared instance
	private static MapActivator plugin;
	
	/**
	 * The constructor
	 */
	public MapActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static MapActivator getDefault() {
		return plugin;
	}
	
	/**
	 * Logs an error message and an optional exception for the current bundle.
	 * 
	 * @param message
	 *            a human-readable message
	 * @param exception
	 *            a low-level exception, or <code>null</code> if not applicable
	 */
	public static void logError(String message, Throwable exception) {
		Platform.getLog(getDefault().getBundle()).log(
				new Status(IStatus.ERROR, PLUGIN_ID, message, exception));
	}

	/**
	 * Gets the full path name for a resource located inside the current bundle.
	 * 
	 * @param path
	 *            the path of the internal resource
	 * @return the string corresponding to the full path
	 * @throws IOException
	 *             if a problem occurs during conversion
	 */
	public static String getFileLocation(String path) {
		Assert.isNotNull(path);
		Bundle bundle = getDefault().getBundle();
		String fullPath = null;
		try {
			if (bundle != null) {
				fullPath = FileLocator.toFileURL(bundle.getEntry(path)).getPath();
			}
		}
		catch (IOException ex) {
			logError("A problem occurred when trying to open the map.html file", ex);
		}
		return fullPath;
	}
	
}
