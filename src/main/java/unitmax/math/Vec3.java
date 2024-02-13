package unitmax.math;

import java.util.Arrays;

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
     * Subtracts another vector from the vector
     * 
     * @param v other vector
     * @return a new vector containing the values of this vector - v
     */
    public Vec3 sub(Vec3 v) {
        return new Vec3(this.x() - v.x(), this.y() - v.y(), this.z() - v.z());
    }

    /**
     * Multiplies another vector with the vector
     * 
     * @param v other vector
     * @return a new vector containing the values of this vector * v
     */
    public Vec3 mult(Vec3 v) {
        return new Vec3(this.x() * v.x(), this.y() * v.y(), this.z() * v.z());
    }

    /**
     * Multiplies this vector with a scalar
     * 
     * @param s scalar
     * @return a new vector containing the values of this vector multiplied by s
     */
    public Vec3 multScalar(double s) {
        return new Vec3(this.x() * s, this.y() * s, this.z() * s);
    }

    /**
     * Divides this vector by a scalar
     * 
     * @param s scalar
     * @return a new vector containing the values of this vector divided by s
     */
    public Vec3 divScalar(double s) {
        return new Vec3(this.x() / s, this.y() / s, this.z() / s);
    }

    /**
     * Calculates the dot product of this vector with another vector
     * 
     * @param v other vector
     * @return the dot product of this vector and v
     */
    public double dotProduct(Vec3 v) {
        return this.x() * v.x() + this.y() * v.y() + this.z() * v.z();
    }

    /**
     * Calculates the cross product of this vector with another vector
     * 
     * @param v other vector
     * @return the cross product of this vector and v
     */
    public Vec3 crossProduct(Vec3 v) {
        return new Vec3(this.y() * v.z() - this.z() * v.y(), this.z() * v.x() - this.x() * v.z(),
                this.x() * v.y() - this.y() - v.x());
    }

    /**
     * Calculates the unit vector of this vector
     * 
     * @return this vector's unit vector
     */
    public Vec3 unitVector() {
        return this.divScalar(this.length());
    }

    /**
     * Magnitude of the vector
     * 
     * @return magnitude of the vector (sum of the squares of each component)
     */
    public double lengthSquared() {
        return e[0] * e[0] + e[1] * e[1] + e[2] * e[2];
    }

    /**
     * Length of the vector
     * 
     * @return length of the vector (square root of {@link #lengthSquared()})
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override
    public String toString() {
        return "Vec3 [e=" + Arrays.toString(e) + "]";
    }

}
