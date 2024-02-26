package unitmax.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import unitmax.math.Interval;

public class Hittables implements Hittable {
    private List<Hittable> objects = new ArrayList<>();

    public void clear() {
        objects.clear();
    }

    public void add(Hittable object) {
        objects.add(object);
    }

    @Override
    public Optional<HitRecord> hit(Ray r, Interval rayT) {
        double closestSoFar = rayT.getMax();
        Optional<HitRecord> returnHitRecord = Optional.empty();

        for (Hittable object : objects) {
            var hr = object.hit(r, new Interval(rayT.getMin(), closestSoFar));
            if (hr.isPresent()) {
                closestSoFar = hr.get().getT();                
                returnHitRecord = hr;
            }
        }

        return returnHitRecord;
    }
}
