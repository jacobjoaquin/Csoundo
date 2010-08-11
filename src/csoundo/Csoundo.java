/**
 * A Csound interface library for Processing.
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
 * @example chnInOut
 */


public class Csoundo {
	PApplet myParent;
	public final static String VERSION = "##version##";

	public Csound csound;
	private String csd;
	public boolean isRunning = false;

	public CsoundPerformanceThread perfThread;

	/**
	 * The Csoundo onstructor, usually called in the setup() method in your
	 * sketch to initialize and start the library.
	 * 
	 * @param theParent
	 */
	public Csoundo(PApplet theParent) {
		myParent = theParent;
		welcome();
		myParent.registerDispose(this);
		myParent.registerPre(this);
		myParent.registerPost(this);
	}

	/**
	 * The Csoundo onstructor, usually called in the setup() method in your
	 * sketch to initialize and start the library.
	 * 
	 * @param theParent The PApplet. Usually pass 'this'
	 * @param f The Csound file to run. Requires full absolute path.
	 */
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

	private void csoundPerformanceThread() {
		if (csound == null) {
			csnd.csoundInitialize(null, null,
					csnd.CSOUNDINIT_NO_SIGNAL_HANDLER);
			csound = new Csound(); 

			int compile = csound.Compile(csd);

			if (compile == 0) {
				isRunning = true;
				perfThread = new CsoundPerformanceThread(csound);
				perfThread.Play();
			}
		}
	}

	/**
	 * Creates a Csound score event.
	 * 
	 * @param s The score event. ie "i 1 0 1 0.707 440"
	 */
	public void event(String s) {
		if (isRunning) {
			perfThread.InputMessage(s);
			perfThread.FlushMessageQueue();

		}
	}

	/**
	 * Returns the value of the specified chn bus.
	 * 
	 * @return chn bus value
	 */
	public float getChn(String chn) {
		if (isRunning) {
			return csound.GetChannel(chn);
		}

		return 0;
	}

	/**
	 * Return the control rate.
	 * 
	 * @return krate
	 */
	public float kr() {
		if (isRunning) {
			return csound.GetKr();
		}

		return 0;
	}

	/**
	 * Return the ksmps, samples per k-block
	 * 
	 * @return ksmps
	 */
	public float ksmps() {
		if (isRunning) {
			return csound.GetKsmps();
		}

		return 0;
	}

	/**
	 * Return the number or audio output channels.
	 * 
	 * @return number of output channels
	 */
	public float nchnls() {
		if (isRunning) {
			return csound.GetNchnls();
		}

		return 0;
	}

	/**
	 * starts the Csound engine.
	 */
	public void run() {
		csoundPerformanceThread();		
	}

	/**
	 * Sets the value of the specified chn bus.
	 */
	public void setChn(String chn, float value) {
		if (isRunning) {
			csound.SetChannel(chn, value);
		}
	}

	/**
	 * Return the samplerate.
	 * 
	 * @return samplerate
	 */
	public float sr() {
		if (isRunning) {
			return csound.GetSr();
		}

		return 0;
	}

	/**
	 * Return a value from a Csound table.
	 * 
	 * @param t Table number
	 * @param i Index
	 * @return Csound table value
	 */
	public float tableGet(int t, int i) {
		if (isRunning) {
			return csound.TableGet(t, i);
		}

		return 0;    
	}

	/**
	 * Return the length of a Csound table.
	 * 
	 * @param t Table number
	 * @return Csound table length
	 */
	public int tableLength(int t) {
		if (isRunning) {
			return csound.TableLength(t);
		}

		return 0;
	}

	/**
	 * Sets the value of a Csound table at a specif index.
	 * 
	 * @param t Table number
	 * @param i Index
	 * @param v Value
	 */
	public void tableSet(int t, int i, float v) {
		if (isRunning) {
			csound.TableSet(t, i, v);
		}
	}
}












