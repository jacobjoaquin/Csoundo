import csoundo.*;

Csoundo cs;

void setup() {
    size(720, 480);
    smooth();
    noLoop();
    
    cs = new Csoundo(this, sketchPath("data/synth.csd"));
    cs.run();
}

void draw() {
    background(255);

    displayTable(1, 20, height * 0.25, width - 40, height * 0.20);
    displayDiscreteTable(2, 20, height * 0.75, width - 40, height * 0.20);
}

void randomFillTable(int t) {
    int s = cs.tableLength(t);
    
    for (int i = 0; i < s; i++) {
        cs.tableSet(t, i, random(2.0) - 1);
    }
}

void createRandomTable(int t, int s) {
    cs.event("f " + t + " 0 " + s + " 10 0");
    
    for (int i = 0; i < s; i++) {
        cs.tableSet(t, i, i * 2.0 / (float) s - 1.0);
    }
}

void displayDiscreteTable(int t, float x, float y, float w, float h) {
    int length = cs.tableLength(t);        

    stroke(0);
    strokeWeight(1);
    line(x, y, x + w, y);
    
    for (int i = 0; i < length; i++) {
        float xpos = x + (float) i / (float) (length - 1) * w;
        float ypos = y + (cs.tableGet(t, i) * -1) * h;
        line(xpos, y, xpos, ypos);
    }

    for (int i = 0; i < length; i++) {
        float xpos = x + (float) i / (float) (length - 1) * w;
        float ypos = y + (cs.tableGet(t, i) * -1) * h;        
        fill(0);
        noStroke();
        ellipseMode(RADIUS);
        ellipse(xpos, ypos, 3, 3);
    }
}

void displayTable(int t, float x, float y, float w, float h) {
    int length = cs.tableLength(t);    

    stroke(0);
    strokeWeight(1);
    noFill();
    line(x, y, x + w, y);

    beginShape();    
    for (int i = 0; i < length; i++) {
        float v = cs.tableGet(t, i);
        float xpos = x + (float) i / (float) (length - 1) * w;
        float ypos = y + (v * -1) * h;
        
        vertex(xpos, ypos);
    }    
    endShape();
}
