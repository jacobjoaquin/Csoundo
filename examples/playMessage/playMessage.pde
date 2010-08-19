/*
 * Demonstrates running a Csound composition.
 * 
 * Message From Another Planet by Jacob Joaquin
 */

import csoundo.*;

Csoundo cs;

void setup() {
    size(740, 480);
    background(0);
    noLoop();
        
    cs = new Csoundo(this, "data/message_from_another_planet.csd");
    cs.run();
}

void draw() { }
