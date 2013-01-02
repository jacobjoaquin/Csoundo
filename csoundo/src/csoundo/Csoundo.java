/**
 * A Csound interface library for Processing.
 *
 * (c) 2010
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
 * @author		Jacob Joaquin http://csoundblog.com
 * @upgraded            02/11/2010 by Conor Robotham
 * @version		0.2.1
 */

package csoundo;

import csnd.*;
import processing.core.*;
import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Csoundo extends Thread{
	PApplet myParent;
	public final static String VERSION = "0.2.1";
        
        private String csd, path;
	public boolean isRunning = false;

	private Engine engine;
        private Csound csound;
	private CsoundPerformanceThread perfThread;	
	private Mutex mutex;
	
	/**
	 * The Csoundo constructor, usually called in the setup() method in your
	 * sketch. Only supports 1 csd file for now, regardless of the number
	 * of instances of Csoundo you are running. Your Csound csd file must
	 * reside in the /data folder of your sketch and must be named after
         * the sketch for Android Mode(caps-sensitive). A temporary CSD file 
         * called __CSOUNDO__.csd is created in your /data folder for Java Mode.
         * Set Android Mode csound options in your .csd file. Use cs.setOptions
         * to set Java Mode csound options.
	 * 
         * Android Mode
	 * @param theParent The PApplet. Usually pass 'this'
	 * @param context Must be super.getApplicationContext.
         * 
         * Java Mode
         * @param theParent The PApplet. Usually pass 'this'
	 * @param _csd The Csound file to run e.g. "myfile.csd"
	 */
  
        //Android Mode Constructor
        public Csoundo(PApplet theParent, Context context) {

            myParent = theParent;
            welcome();
            myParent.registerDispose(this);
            myParent.registerPost(this);
            engine = new Engine(createTempFile(getResourceFileAsString(0x7f040000, context), context).getAbsolutePath());
            
        }
        //Java Mode Constructor
        public Csoundo(PApplet theParent, String _csd) {

            myParent = theParent;
            welcome();
            myParent.registerDispose(this);
            myParent.registerPost(this);

            path = myParent.dataPath("");
            csd  = myParent.dataPath(_csd);
            engine = new Engine(csd, path);
        
        }
        //Method for Android, locates .csd file in .apk and returns it as a string.
        protected String getResourceFileAsString(int resId, Context context) {
           StringBuilder str = new StringBuilder();

           InputStream is = context.getResources().openRawResource(resId);
           BufferedReader r = new BufferedReader(new InputStreamReader(is));
           String line;

           try {
             while ((line = r.readLine()) != null) {
               str.append(line).append("\n");
             }
           } catch (IOException ios) {

           }

           return str.toString();
         }
         //Method for Android, writes .csd string to a temorary file for use with java(and Processing)
         protected File createTempFile(String csd, Context context) {
           File f = null;

           try {
             f = File.createTempFile("temp", ".csd", context.getCacheDir());
             FileOutputStream fos = new FileOutputStream(f);
             fos.write(csd.getBytes());
             fos.close();
           } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
           }

           return f;
         }

	public void dispose() {
            System.out.println("Csound dispose");
            // NOTE: 
	    perfThread.Stop();
	    System.out.println("perfThread waiting");
	    perfThread.Join();
	    System.out.println("perfThread finished");
	    csound.Cleanup();
	    csound.delete();
	    System.out.println("Csound dispose complete");		
	}

	public void pre() { }

	public void post() {
//	    mutex.cleanup();  // Closes any mutex locks left open from lock()
	}

	private void welcome() {
		System.out.println("csoundo 0.2.1 by Jacob Joaquin http://csoundblog.com Upgraded by Conor Robotham");
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
//        if (!isRunning) return;
        // TODO: Is mutex needed if csoundPerformanceThread.InputMessage()
        //       is already thread safe? Does it use mutex?
//        mutex.lock();
        perfThread.InputMessage(s);
//        mutex.unlock();
	}

    /**
     * Returns the 0dbfs value, set in the orchestra header.
     * 
     * @return 0dbfs
     */
    public float get0dBFS() {
//        if (!isRunning) return 0;
        return (float) csound.Get0dBFS();
    }

    /**
	 * Returns the value of the specified chn bus.
	 * 
	 * @return chn bus value
	 */
	public float getChn(String chn) {
//        if (!isRunning) return 0;
//        mutex.lock();
        float value = (float) csound.GetChannel(chn);
//        mutex.unlock();
        return value;
	}

    /**
     * Returns the command-line options string.
     * 
     * @return command-line options
     */
	public String getOptions() {
	    return engine.options;
	}
	
    /**
     * Returns the status of csoundPerformanceThread.
     * 
     * NOTE: This is for Csoundo development and is likely to disappear
     * or be renamed.
     * 
     * @return status
     */
	public int getPerfStatus() {
	    // TODO: Make ENUMS, not magic numbers
//        if (!isRunning) return -99999;
        return perfThread.GetStatus();
	}
	
	/**
	 * Return the control rate.
	 * 
	 * @return krate
	 */
	public float kr() {
//        if (!isRunning) return 0;
        return (float) csound.GetKr();
	}

	/**
	 * Return the ksmps, samples per k-block
	 * 
	 * @return ksmps
	 */
	public float ksmps() {
	    // TODO: Throw exceptions, not magic numbers.
//        if (!isRunning) return 0;
        return csound.GetKsmps();
	}

    /**
     * Lock mutex. Experimental.
     */
    public void lock() {
        mutex.lock();
    }
    
	/**
	 * Return the number or audio output channels.
	 * 
	 * @return number of output channels
	 */
	public float nchnls() {
//        if (!isRunning) return 0;
        return csound.GetNchnls();
	}

	/**
	 * starts the Csound engine.
	 */
	public void run() {
	    super.run();
	    System.out.println(getName());
		engine.start();
		perfThread = engine.perfThread;
		while(perfThread.GetStatus() != 0) {
		    System.out.println("Waiting for csoundPerformanceThread");
		}
              
                csound =engine.csound;
		mutex = engine.mutex;
		isRunning = engine.isRunning;
	}

    /**
     * Sets the value of the specified chn bus.
     */
    public void setChn(String chn, float value) {
//        if (!isRunning) return;
//        mutex.lock();
        csound.SetChannel(chn, value);
//        mutex.unlock();
    }

    /**
     * Sets the string of the specified chn bus.
     */
    public void setChn(String chn, String sValue) {
        // TODO: Untested. Need to figure out how to get a string.
//        if (!isRunning) return;
//        mutex.lock();
        csound.SetChannel(chn, sValue);
//        mutex.unlock();
    }

    /* TODO: Try to get this working, even if unnecessary.
    public void setChnTest(String chn, float value) {
        if (isRunning) {
            CsoundMYFLTArray myflt = new CsoundMYFLTArray(4);
            int check = csnd.csoundGetChannelPtr(csound_p, myflt.GetPtr(), chn,
                                                 csnd.CSOUND_CONTROL_CHANNEL |
                                                 csnd.CSOUND_INPUT_CHANNEL);

            if (check == 0) {
                myflt.SetValue(0, value);
            }
        }
    }
     */
    
    /**
     * Overwrites the Csound options.
     * 
     * Default options are '-g -odac' where -g sets graphics to display
     * as ascii characters in the console output and -odac means to send
     * audio to the default digital-audio-converter.
     * 
     * @param Command-line string
     */
    public void setOptions(String s) {
        engine.options = s;
    }
    
	/**
	 * Return the samplerate.
	 * 
	 * @return samplerate
	 */
	public float sr() {
//        if (!isRunning) return 0;
        return (float) csound.GetSr();
	}

	/**
	 * Return a value from a Csound table.
	 * 
	 * @param t Table number
	 * @param i Index
	 * @return Csound table value
	 */
	public float tableGet(int t, int i) {
	    // TODO: If sets are locked, do gets need to be?
//	    if (!isRunning) return 0;
//	    mutex.lock();
	    float value = (float) csound.TableGet(t, i);
//	    mutex.unlock();
	    return value;
	}

	/**
	 * Return the length of a Csound table.
	 * 
	 * @param t Table number
	 * @return Csound table length
	 */
    public int tableLength(int t) {
//        if (!isRunning) return 0;
//        mutex.lock();
        int value = csound.TableLength(t);
//        mutex.unlock();
        return value;
    }

    /**
	 * Sets the value of a Csound table at a specif index.
	 * 
	 * @param t Table number
	 * @param i Index
	 * @param v Value
	 */
    public void tableSet(int t, int i, float v) {
//        if (!isRunning) return;
//        mutex.lock();
        csound.TableSet(t, i, v);
        //perfThread.SetProcessCallback(0, csndConstants.CSOUND_CONTROL_CHANNEL_LIN);
        // csoundTableSet
//        mutex.unlock();
    }

    /**
     * Unlock mutex. Experimental.
     */
    public void unlock() {
        mutex.unlock();
    }
}
