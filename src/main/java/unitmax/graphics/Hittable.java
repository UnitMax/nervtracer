package unitmax.graphics;

public abstract class Hittable {
    public abstract boolean hit(Ray r, double rayTmin, double rayTmax);
}
