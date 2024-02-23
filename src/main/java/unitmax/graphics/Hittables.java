package unitmax.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Hittables {
    private List<Hittable> objects = new ArrayList<>();

    public void clear() {
        objects.clear();
    }

    public void add(Hittable object) {
        objects.add(object);
    }

    Optional<HitRecord> hit(Ray r, double rayTmin, double rayTmax) {
        double closestSoFar = rayTmax;
        Optional<HitRecord> returnHitRecord = Optional.empty();

        for (Hittable object : objects) {
            var hr = object.hit(r, rayTmin, closestSoFar);
            if (hr.isPresent()) {
                closestSoFar = hr.get().getT();                
                returnHitRecord = hr;
            }
        }

        return returnHitRecord;
    }
}
