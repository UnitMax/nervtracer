package unitmax.graphics;

import unitmax.math.Vec3;

public record HitRecord(Vec3 point, Vec3 normal, double t) {}