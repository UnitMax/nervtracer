package unitmax.math;

public class Vec3 {

    private double e[] = new double[3];

    public Vec3() {
        // Arrays in Java are 0-initialized
    }

    public Vec3(double e0, double e1, double e2) {
        this.e[0] = e0;
        this.e[1] = e1;
        this.e[2] = e2;
    }

    public double x() {
        return e[0];
    }

    public double y() {
        return e[1];
    }

    public double z() {
        return e[2];
    }

    /**
     * Negates the vector
     * 
     * @return the vector multiplied by -1
     */
    public Vec3 negative() {
        return new Vec3(-e[0], -e[1], -e[2]);
    }

    /**
     * Adds another vector to the vector
     * 
     * @param v other vector
     * @return a new vector containing the values of this vector + v
     */
    public Vec3 add(Vec3 v) {
        return new Vec3(this.x() + v.x(), this.y() + v.y(), this.z() + v.z());
    }

    /**
     * Multiplies this vector with a scalar
     * 
     * @param s scalar
     * @return a new vector containing the values of this vector multiplied by s
     */
    public Vec3 mult(double s) {
        return new Vec3(this.x() * s, this.y() * s, this.z() * s);
    }

    /**
     * Divides this vector by a scalar
     * 
     * @param s scalar
     * @return a new vector containing the values of this vector divided by s
     */
    public Vec3 div(double s) {
        return new Vec3(this.x() / s, this.y() / s, this.z() / s);
    }

    /**
     * Magnitude of the vector
     * @return magnitude of the vector (sum of the squares of each component)
     */
    public double lengthSquared() {
        return e[0]*e[0] + e[1]*e[1] + e[2]*e[2];
    }

    /**
     * Length of the vector
     * @return length of the vector (square root of {@link #lengthSquared()})
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }
}
