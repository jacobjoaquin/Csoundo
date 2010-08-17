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

import java.io.*;
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
	public boolean isRunning = false;

	private CppSound csound;
	public CsoundFile csoundFile;
	public CsoundPerformanceThread perfThread;
	
	public SWIGTYPE_p_CSOUND_ csound_p;
	SWIGTYPE_p_void mutex;
	
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

		csound = new CppSound();
		csd = f;

		myParent.registerDispose(this);
		myParent.registerPost(this);
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

	private void cppSoundPerf() {
		// TODO: Make sure csound isn't already running
		if (true) {
			csnd.csoundInitialize(null, null,
					csnd.CSOUNDINIT_NO_SIGNAL_HANDLER);
			
			csound = new CppSound();
			csound_p = csound.getCsound();
			
			csoundFile = csound.getCsoundFile();
			csoundFile.setCSD(fileToString(csd));
			
			// TODO: csd should get unique names, in case of multiple
			//       instances.
			String tempCSD = "temp.csd";
			csoundFile.setCommand("-d -odac " + myParent.dataPath("") +
					              tempCSD);
			
			csoundFile.exportForPerformance();
			int compile = csound.compile();
			System.out.println("compile status: " + compile);
			
			if (compile == 0) {
				isRunning = true;
				perfThread = new CsoundPerformanceThread(csound_p);
				perfThread.Play();
				mutex = csnd.csoundCreateMutex(1);
			}
		}
	}
	
	/**
	 * Reads a csd file into a string
	 */
	private String fileToString(String path) {
		FileInputStream fin;
		StringBuilder sb = new StringBuilder();
		
		try {
		    fin = new FileInputStream(csd);
		    BufferedReader d = new BufferedReader(new InputStreamReader(fin));
		    String line;
		    while ((line = d.readLine()) != null) {
		    		sb.append(line + "\n");
		    }
		    
		    fin.close();		
		} catch (IOException e) {
			System.err.println ("Unable to read CSD file.");
		}
		
		return sb.toString();
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
		cppSoundPerf();
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









