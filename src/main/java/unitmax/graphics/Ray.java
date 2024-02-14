package unitmax.graphics;

import unitmax.math.Vec3;

public class Ray {

    private Vec3 origin;

    private Vec3 direction;

    public Vec3 getOrigin() {
        return origin;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public Ray(Vec3 origin, Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    Vec3 at(double t) {
        // origin + t * direction;
        return origin.add(direction.multScalar(t));
    }
}
