import csoundo.*;

Csoundo cs;

void setup() {
    size(720, 480);
    frameRate(30);
    smooth();
        
    cs = new Csoundo(this, "data/synth.csd");
    cs.run();
    println("perf status:  " + cs.getPerfStatus());
    cs.tableSet(1, 2048, -1.0);
}

void draw() {
    background(0);
    noStroke();
    fill(random(255));
    ellipseMode(RADIUS);
    ellipse(width / 2, height / 2, 50, 50);
    
    cs.kLock();
    if (frameCount % 30 == 0) {
        cs.event("i 2 0   0.3 0.5 440");
        cs.event("i 2 0.1 0.3 0.5 880");
    }
    cs.kUnlock();
    
}
