import csoundo.*;

Csoundo cs;

void setup() {
    size(720, 480);
    frameRate(30);
    background(0);
        
    cs = new Csoundo(this, "data/synth.csd");
    cs.run();
    
    cs.tableSet(1, 2048, -1.0);
}

void draw() {
    if (frameCount % 9 == 0) {
        cs.event("i 2 0 0.3 0.5 " + (random(440.0) + 440));
    }
    
    println(cs.tableLength(1));
    
    if (frameCount % 5 == 0) {
        cs.setChn("amp", random(1.0));
    }
}
