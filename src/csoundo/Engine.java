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
import csnd.*;

/**
 * This class is for getting Csound up and running.
 */
public class Engine {
	private String csd;
	private String path;
	public String options = "-g -odac";
    private String tempCSD = "__CSOUNDO__.csd";
	
	public boolean isRunning = false;

	public Csound csound;
	public CsoundFile csoundFile;
	public CsoundPerformanceThread perfThread;
	
	public SWIGTYPE_p_void v;
	public SWIGTYPE_p_CSOUND_ csound_p;
	public Mutex mutex;
	
	/**
	 * The Engine constructor, usually called in the setup() method in your
	 * sketch to initialize and start the library.
	 * 
	 * @param _csd The name of the csd file
	 * @param _path The path to the csd
	 */
	public Engine(String _csd, String _path) {
	    csound = new Csound();
		csd = _csd;
		path = _path;
	}
	
	/**
	 * Setup the Csound engine and play it.
	 */
    private void csoundPerf() {
        // TODO: Make sure csound isn't already running
        if (true) {
            csnd.csoundInitialize(null, null,
                    csnd.CSOUNDINIT_NO_SIGNAL_HANDLER);
            
            csound = new Csound();                        
            csound_p = csound.GetCsound();
            csoundFile = new CsoundFile();
            csoundFile.setCSD(fileToString(csd));
            
            // TODO: csd should get unique names, in case of multiple
            //       instances.
            csoundFile.setCommand(options + " " + path + tempCSD);            
            csoundFile.exportForPerformance();

            int compile = csound.Compile(path + tempCSD);
            System.out.println("compile status: " + compile);
            
            if (compile == 0) {
                isRunning = true;
                perfThread = new CsoundPerformanceThread(csound_p);
                perfThread.Play();
                //mutex = new Mutex();
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
	 * Start Csound.
	 */
	public void start() {
        csoundPerf();
	}
}
