import csoundo.*;

Csoundo cs;

void setup() {
    size(740, 480);
    frameRate(30);
    smooth();
    
    cs = new Csoundo(this, sketchPath("data/synth.csd"));
    cs.run();
}

void draw() {
    dingOneHertz();
}


void dingOneHertz() {
   if (frameCount % frameRate == 0) {
        cs.event("i 2 0 1 0.4 440");
    }
}