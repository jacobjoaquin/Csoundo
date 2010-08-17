import csoundo.*;

Csoundo cs;

void setup() {
    size(1600, 1200);
    frameRate(30);
    smooth();
    background(0);
//    noLoop();
        
    cs = new Csoundo(this, "data/synth.csd");
    cs.run();
    
}

void draw() {
    fill(0, 16);
    rect(0, 0, width, height);    
    ellipseMode(RADIUS);
    noStroke();
    
//    float r = random(255.0);
//    fill(r, r, 196);

    println("before");
    float size = pow(2, 11);
    println(size);
    cs.event("f 20 0 " + size + " 10 0");
    println("after");
    int length = cs.tableLength(20);
    float phase = (float) (frameCount % 120) / 120 * TWO_PI;
    for (int i = 0; i < length; i++) {
        float r = random(255.0);
        fill(r, r, 255, 196);
        cs.tableSet(20, length - i - 1, sin(2 * PI * (float) i *
                                 (random(0.2) + 1.0) / (float) length  + phase));
        float v = cs.tableGet(20, length - i - 1);

        float r1 = random(2.0);
        float r2 = random(2.0);
        
        ellipse((float) i / (float) length * (float) width,
                height / 2.0 + height / 2.0 * v, r1, r1);
    }
//    float v = cs.tableGet(20, (length - 1));
//    print("done " + v);
}
