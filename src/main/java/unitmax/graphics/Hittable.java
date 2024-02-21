package unitmax.graphics;

import java.util.Optional;

public interface Hittable {
    public Optional<HitRecord> hit(Ray r, double rayTmin, double rayTmax);
}
