package unitmax.graphics;

import unitmax.math.Vec3;

public class Sphere {

    public static boolean hit(Vec3 center, double radius, Ray r) {
        Vec3 originCenterVector = r.getOrigin().sub(center);
        var a = r.getDirection().dotProduct(r.getDirection());
        var b = 2.0 * originCenterVector.dotProduct(r.getDirection());
        var c = originCenterVector.dotProduct(originCenterVector) - radius * radius;
        var discriminant = b * b - 4 * a * c;
        return discriminant >= 0;
    }

}
