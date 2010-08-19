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

/**
 * Experimental class for thread locking/memory protection.
 */
public class Mutex {
    // FIXME: My tests have confirmed that I have no idea what I'm doing
    //        here.

    SWIGTYPE_p_void m;                // Mutex
    private int locks = 0;            // Number of active locks
    private boolean useLocks = true;  // Use locks?
    
    public Mutex() {
        m = csnd.csoundCreateMutex(1);        
    }

    public int activeThreads() {
        return locks;
    }

    public void cleanup() {
        if (!useLocks) return;
        while (locks > 0) {
            unlock();
        }
    }

    public void lock() {
        if (!useLocks) return;
        csnd.csoundLockMutex(m);
        locks++;
    }
    
    public void unlock() {
        if (!useLocks) return;
        if (locks > 0) {
            locks--;
            csnd.csoundUnlockMutex(m);
        } else {
            System.out.println("Warning: Mutex trying to unlock imaginary threads.");
        }
    }
}




