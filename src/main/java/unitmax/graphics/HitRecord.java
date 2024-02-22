package unitmax.graphics;

import unitmax.math.Vec3;

public class HitRecord {

    private Vec3 point;
    
    private Vec3 normal;
    
    private double t;
    
    /**
     * Whether or not ray is "inside" the geometry or not
     * true  => outside
     * false => inside
     */
    private boolean frontFace = false;

    public HitRecord(Vec3 point, Vec3 normal, double t) {
        this.point = point;
        this.normal = normal;
        this.t = t;
    }

    public Vec3 getPoint() {
        return point;
    }

    public Vec3 getNormal() {
        return normal;
    }

    public double getT() {
        return t;
    }

    public boolean isFrontFace() {
        return frontFace;
    }

    public void setFaceNormal(Ray r, Vec3 outwardNormal) {
        frontFace = r.getDirection().dotProduct(outwardNormal) < 0;
        normal = frontFace ? outwardNormal : outwardNormal.negative();
    }

}