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
 * @author      ##author##
 * @modified    ##date##
 * @version     ##version##
 */

package csoundo;

import csnd.*;

public class Mutex {
    
    SWIGTYPE_p_void m;
    int instances = 0;
    private boolean useThreads = true;
    
    public void cleanup() {
        if (!useThreads) return;
        while (instances > 0) {
            unlock();
        }
    }

    public Mutex() {
        m = csnd.csoundCreateMutex(1);        
    }

    public void lock() {
        if (!useThreads) return;
        csnd.csoundLockMutex(m);
        instances++;
    }
    
    public void unlock() {
        if (!useThreads) return;
        if (instances > 0) {
            instances--;
            csnd.csoundUnlockMutex(m);
        } else {
            System.out.println("Warning: Mutex trying to unlock imaginary threads.");
        }
    }
    
}




