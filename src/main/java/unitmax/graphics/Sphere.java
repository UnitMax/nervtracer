package unitmax.graphics;

import java.util.Optional;

import unitmax.math.Vec3;

public class Sphere implements Hittable {

    private Vec3 center;

    private double radius;

    public Sphere(Vec3 center, double radius) {
        this.center = center;
        this.radius = radius;
    }

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

    @Override
    public Optional<HitRecord> hit(Ray r, double rayTmin, double rayTmax) {
        Vec3 originCenterVector = r.getOrigin().sub(center);
        var a = r.getDirection().lengthSquared();
        var halfB = originCenterVector.dotProduct(r.getDirection());
        var c = originCenterVector.lengthSquared() - radius * radius;
        var discriminant = halfB * halfB - a * c;
        var squaredDiscriminant = Math.sqrt(discriminant);
        if (discriminant < 0) {
            return Optional.empty();
        }

        // nearest root within acceptable range
        var root = (-halfB - squaredDiscriminant) / a;
        if (root <= rayTmin || rayTmax <= root) {
            root = (-halfB + squaredDiscriminant) / a;
            if (root <= rayTmin || rayTmax <= root) {
                // both outside of range, no solution
                return Optional.empty();
            }
        }

        return Optional.of(new HitRecord(r.at(root), r.at(root).sub(center).divScalar(radius), root));
    }

}
