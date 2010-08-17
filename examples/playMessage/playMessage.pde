import csoundo.*;

Csoundo cs;

void setup() {
    size(1600, 1200);
    background(0);
    noLoop();
        
    cs = new Csoundo(this, "data/message_from_another_planet.csd");
    cs.run();
}

void draw() {

}
