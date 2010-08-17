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

public class Engine {
	private String csd;
	private String path;
	
	public boolean isRunning = false;

	public CppSound csound;
	public CsoundFile csoundFile;
	public CsoundPerformanceThread perfThread;
	
	public SWIGTYPE_p_CSOUND_ csound_p;
	SWIGTYPE_p_void mutex;
	
	/**
	 * The Engine constructor, usually called in the setup() method in your
	 * sketch to initialize and start the library.
	 * 
	 * @param theParent The PApplet. Usually pass 'this'
	 * @param f The Csound file to run. Requires full absolute path.
	 */
	public Engine(String _csd, String _path) {
		csound = new CppSound();
		csd = _csd;
		path = _path;
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
			csoundFile.setCommand("-d -odac " + path + tempCSD);
			
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

	public void start() {
		cppSoundPerf();
	}
}









