/**
 * you can put a one sentence description of your library here.
 *
 * ##copyright##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author		##author##
 * @modified	##date##
 * @version		##version##
 */

package csoundo;

import processing.core.*;
import csnd.*;

/**
 * This is a template class and can be used to start a new processing library or tool.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own lobrary or tool naming convention.
 *  
 * 
 * (the tag @example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 *
 */



public class Csoundo {
	PApplet myParent;
	public final static String VERSION = "##version##";

	public Csound csound;
	private String csd;
	public boolean isRunning = false;

	public CsoundPerformanceThread cpThread;

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	public Csoundo(PApplet theParent) {
		myParent = theParent;
		welcome();
		myParent.registerDispose(this);
		myParent.registerPre(this);
		myParent.registerPost(this);
	}

	public Csoundo(PApplet theParent, String f) {
		myParent = theParent;
		welcome();

		csd = f;
		myParent.registerDispose(this);
		myParent.registerPost(this);
	}

	public void dispose() {
		System.out.println("Csound dispose");
		//cpThread.Stop();
		csound.Stop();
		csound.Cleanup();
		csound.Reset();
	}

	public void pre() {
//		cpThread.FlushMessageQueue();
//		csnd.csoundCreateThreadLock();
		
	}

	public void post() {
//		csnd.csoundDestroyThreadLock(arg0);
//		cpThread.FlushMessageQueue();
	}

	private void welcome() {
		System.out.println("##name## ##version## by ##author##");

	}


	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	public void run() {
		csoundPerformanceThread();		
	}

	private void csoundPerformanceThread() {
		if (csound == null) {
			csnd.csoundInitialize(null, null,
					csnd.CSOUNDINIT_NO_SIGNAL_HANDLER);
			csound = new Csound(); 

			int compile = csound.Compile(csd);

			if (compile == 0) {
				isRunning = true;
				cpThread = new CsoundPerformanceThread(csound);
				cpThread.Play();
			}
		}
	}

	public void event(String s) {
		if (isRunning) {
			cpThread.InputMessage(s);
			cpThread.FlushMessageQueue();

		}
	}

	public float getChn(String chn) {
		if (isRunning) {
			return csound.GetChannel(chn);
		}

		return 0;
	}

	public void setChn(String chn, float value) {
		if (isRunning) {
			csound.SetChannel(chn, value);
		}
	}

	public float tableGet(int t, int i) {
		if (isRunning) {
			return csound.TableGet(t, i);
		}

		return 0;    
	}

	public int tableLength(int t) {
		if (isRunning) {
			return csound.TableLength(t);
		}

		return 0;
	}


	public void tableSet(int t, int i, float v) {
		if (isRunning) {
			csound.TableSet(t, i, v);
		}
	}

	public float sr() {
		if (isRunning) {
			return csound.GetSr();
		}

		return 0;
	}

	public float kr() {
		if (isRunning) {
			return csound.GetKr();
		}

		return 0;
	}

	public float ksmps() {
		if (isRunning) {
			return csound.GetKsmps();
		}

		return 0;
	}

	public float nchnls() {
		if (isRunning) {
			return csound.GetNchnls();
		}

		return 0;
	}
	
}












