package unitmax.graphics;

import java.util.Optional;

import unitmax.math.Vec3;

public class Sphere {

    public static Optional<Double> hit(Vec3 center, double radius, Ray r) {
        Vec3 originCenterVector = r.getOrigin().sub(center);
        var a = r.getDirection().lengthSquared();
        var halfB = originCenterVector.dotProduct(r.getDirection());
        var c = originCenterVector.lengthSquared() - radius * radius;
        var discriminant = halfB * halfB - a * c;
        if (discriminant < 0) {
            return Optional.empty();
        }
        return Optional.of((-halfB - Math.sqrt(discriminant)) / a);
    }

}
