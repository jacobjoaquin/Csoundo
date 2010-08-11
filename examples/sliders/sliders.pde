import controlP5.*;
import csoundo.*;

ControlP5 controlP5;
Csoundo cs;

float mod = 1;
float index = 1;

void setup() {
    size(180, 180);
    frameRate(30);
    smooth();

    controlP5 = new ControlP5(this);
    cs = new Csoundo(this, sketchPath("data/synth.csd"));
    cs.run();
    cs.setChn("slider_1", mod);
    cs.setChn("slider_2", index);
        
    controlP5.addSlider("mod", 1, 4, mod, 30, 40, 40, 100);
    controlP5.addSlider("index", 0, 3, index, 110, 40, 40, 100);
}

void draw() {
    background(0);

    cs.setChn("slider_1", mod);
    cs.setChn("slider_2", index);
}
