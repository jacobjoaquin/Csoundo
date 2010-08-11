import csoundo.*;

Csoundo cs;
int cnt=0;
float x,y,amp;

void setup() {
    background(0);
    size(740, 480);
    frameRate(10);
    smooth();
    cs = new Csoundo(this, sketchPath("data/test.csd"));
    cs.run();
}

void draw() {
    x = random(740);
    y = random(480);
    amp = random(15000);

    cs.setChn("pitch", y);
    cs.setChn("pan", x/740);
    cs.setChn("amp", amp);
    
    fill(255, (x/740) * 255, 0, (y/480) * 255);
    ellipse(x,y, 20*(amp/2500), 20*(amp/2500)); 
    fill(0, 0, 0, 10);
    rect(0, 0, 740, 480);
}
