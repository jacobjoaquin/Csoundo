/*
 * Not an example, but a test to make sure table data is protected.
 * 
 * Every frame, a new Csound table is generated, overwriting the existing
 * table. Each value is replaced with a sine + noise value, and then read
 * from the table and used for drawing an animation.
 *
 * Some indicators that the test fails at times, where it reads a value
 * before it has been properly set. Watch the zero line. Pay attention
 * or you'll miss it.
 *
 * !Example by Jacob Joaquin
 */

import csoundo.*;

Csoundo cs;

void setup() {
    size(740, 480);
    frameRate(30);
    smooth();

    ellipseMode(RADIUS);
    background(0);    
    cs = new Csoundo(this, "protectedTable.csd");
    cs.run();
}

void draw() {
    noStroke();
    fill(0, 8);
    rect(0, 0, width, height);
        
    float size = pow(2, 12);
    cs.event("f 20 0 " + size + " 10 0");
    int length = cs.tableLength(20);
    float phase = (float) (frameCount % 60) / 60 * TWO_PI;

    stroke(255, 255);
    beginShape();
    for (int i = 0; i < length; i++) {

        // Intentionally read backwards
        cs.tableSet(20, length - i - 1, sin(2 * PI * (float) i *
                   (random(0.0001) + 2.0) / (float) length  + phase));

        float v = cs.tableGet(20, length - i - 1);
        vertex((float) i / (float) length * (float) width, height / 2.0 + height / 2.0 * v);
    }
    
    endShape();
}
