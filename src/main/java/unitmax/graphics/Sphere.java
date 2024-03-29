package unitmax.graphics;

import java.util.Optional;

import unitmax.math.Interval;
import unitmax.math.Vec3;

public class Sphere implements Hittable {

    private Vec3 center;

    private double radius;

    public Sphere(Vec3 center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    private static Optional<Double> hit(Vec3 center, double radius, Ray r) {
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
    public Optional<HitRecord> hit(Ray ray, Interval rayT) {
        Vec3 originCenterVector = ray.getOrigin().sub(center);
        var a = ray.getDirection().lengthSquared();
        var halfB = originCenterVector.dotProduct(ray.getDirection());
        var c = originCenterVector.lengthSquared() - radius * radius;
        var discriminant = halfB * halfB - a * c;
        var squaredDiscriminant = Math.sqrt(discriminant);
        if (discriminant < 0) {
            return Optional.empty();
        }

        // nearest root within acceptable range
        var root = (-halfB - squaredDiscriminant) / a;
        if (root <= rayT.getMin() || rayT.getMax() <= root) {
            root = (-halfB + squaredDiscriminant) / a;
            if (root <= rayT.getMax() || rayT.getMax() <= root) {
                // both outside of range, no solution
                return Optional.empty();
            }
        }

        var hitRecord = new HitRecord(ray.at(root), ray.at(root).sub(center).divScalar(radius), root);
        var outwardNormal = hitRecord.getPoint().sub(center).divScalar(radius); // normalize because setFaceNormal expects normalized vector
        hitRecord.setFaceNormal(ray, outwardNormal);

        return Optional.of(hitRecord);
    }

}
