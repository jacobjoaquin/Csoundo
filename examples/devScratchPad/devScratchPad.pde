import csoundo.*;

Csoundo cs;

void setup() {
    size(740, 480);
    frameRate(30);
    smooth();
    noLoop();
        
    cs = new Csoundo(this, sketchPath("data/synth.csd"));
    cs.run();
    
}

void draw() {
    background(0);
    
    ellipseMode(RADIUS);
    noStroke();
    fill(255, 180);

    println("before");
    float size = pow(2, 10);
    println(size);
    cs.event("f 20 0 " + size + " 10 0");
    println("after");
    int length = cs.tableLength(20);
    for (int i = 0; i < length; i++) {
        cs.tableSet(20, length - i - 1, sin(2 * PI * (float) i * 8.0 / (float) length));
        float v = cs.tableGet(20, length - i - 1);

        ellipse((float) i / (float) length * (float) width,
                height / 2.0 + height / 2.0 * v, 1, 1);
    }
}
