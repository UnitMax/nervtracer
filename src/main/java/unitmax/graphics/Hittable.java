package unitmax.graphics;

import java.util.Optional;

import unitmax.math.Interval;

public interface Hittable {
    public Optional<HitRecord> hit(Ray r, Interval rayT);
}
