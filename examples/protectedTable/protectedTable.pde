/*
 * Not an example, but a test to make sure table data is protected.
 * 
 * Every frame, a new Csound table is generated, overwriting the existing
 * table. Each value is replaced with a sine + noise value, and then read
 * from the table and used for drawing an animation.
 *
 * Some indicators that the test fails at times, where it reads a value
 * before it has been properly set.
 *
 * !Example by Jacob Joaquin
 */

import csoundo.*;

Csoundo cs;

void setup() {
    size(740, 480);
    frameRate(30);
    smooth();
    noFill();    
    ellipseMode(RADIUS);
        
    cs = new Csoundo(this, "data/synth.csd");
    cs.run();
}

void draw() {
    background(0);
    
    float size = pow(2, 13);
    cs.event("f 20 0 " + size + " 10 0");
    int length = cs.tableLength(20);
    float phase = (float) (frameCount % 120) / 120 * TWO_PI;

    for (int i = 0; i < length; i++) {
        float r = random(255.0);
        stroke(r, r, 255);
        
        // Intentionally read backwards
        cs.tableSet(20, length - i - 1, sin(2 * PI * (float) i *
                   (random(0.2) + 2.0) / (float) length  + phase));

        float v = cs.tableGet(20, length - i - 1);
        point((float) i / (float) length * (float) width, height / 2.0 + height / 2.0 * v);
    }
}
