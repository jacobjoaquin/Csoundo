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

	private boolean useThreads = true;
	private String csd;
	private String path;
	
	public boolean isRunning = false;

	private Engine engine;
	private CppSound csound;
	private CsoundPerformanceThread perfThread;	
	private SWIGTYPE_p_CSOUND_ csound_p;
	private SWIGTYPE_p_void mutex;
	
	/**
	 * The Csoundo onstructor, usually called in the setup() method in your
	 * sketch to initialize and start the library.
	 * 
	 * @param theParent The PApplet. Usually pass 'this'
	 * @param f The Csound file to run. Requires full absolute path.
	 */
	public Csoundo(PApplet theParent, String _csd) {
		myParent = theParent;
		welcome();
		myParent.registerDispose(this);
		myParent.registerPost(this);

//		csound = new CppSound();
		csd = myParent.sketchPath(_csd);
		path = myParent.sketchPath("");
		engine = new Engine(csd, path); 
	}

	public void dispose() {
		System.out.println("Csound dispose");
		// TODO: Shut down Csound properly.
	}

	public void pre() { }

	public void post() { }

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

	/**
	 * Creates a Csound score event.
	 * 
	 * @param s The score event. ie "i 1 0 1 0.707 440"
	 */
	public void event(String s) {
		if (isRunning) {
			
			if (useThreads) {
				csnd.csoundLockMutex(mutex);
				perfThread.InputMessage(s);
//				perfThread.FlushMessageQueue();
				csnd.csoundUnlockMutex(mutex);
			} else {
				perfThread.InputMessage(s);
			}

		}
	}

	/**
	 * Returns the value of the specified chn bus.
	 * 
	 * @return chn bus value
	 */
	public float getChn(String chn) {
		if (isRunning) {
		    return 0;
			//return csound.GetChannel(chn);
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
		engine.start();
		perfThread = engine.perfThread;
		csound = engine.csound;
		csound_p = engine.csound_p;
		mutex = engine.mutex;
		isRunning = engine.isRunning;
	}

	/**
	 * Sets the value of the specified chn bus.
	 */
	public void setChn(String chn, float value) {
		if (isRunning) {
            CsoundMYFLTArray myflt = new CsoundMYFLTArray();
            int check = csnd.csoundGetChannelPtr(csound_p, myflt.GetPtr(), chn,
                                                 csnd.CSOUND_CONTROL_CHANNEL |
                                                 csnd.CSOUND_INPUT_CHANNEL);

            if (check == 0) {
                myflt.SetValue(0, value);
            }
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
			if (useThreads) {
				csnd.csoundLockMutex(mutex);
				float value = csnd.csoundTableGet(csound_p, t, i);
				csnd.csoundUnlockMutex(mutex);
				return value;
			} else {
				return csnd.csoundTableGet(csound_p, t, i);
			}
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
			if (useThreads) {
				csnd.csoundLockMutex(mutex);
				int value = csnd.csoundTableLength(csound_p, t);
				csnd.csoundUnlockMutex(mutex);
				return value;
			} else {
				return csnd.csoundTableLength(csound_p, t);
			}
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
			if (useThreads) {
				csnd.csoundLockMutex(mutex);
				csnd.csoundTableSet(csound_p, t, i, v);
				csnd.csoundUnlockMutex(mutex);
			} else {
				csnd.csoundTableSet(csound_p, t, i, v);
			}
		}
	}
}









