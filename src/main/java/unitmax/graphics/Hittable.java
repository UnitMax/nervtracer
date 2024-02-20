package unitmax.graphics;

public interface Hittable {
    public boolean hit(Ray r, double rayTmin, double rayTmax);
}
