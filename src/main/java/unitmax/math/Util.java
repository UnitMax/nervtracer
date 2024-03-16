package unitmax.math;

import java.util.concurrent.ThreadLocalRandom;

public class Util {
    
    public static double degToRad(double degrees) {
        return degrees * Math.PI / 180.0;
    }

    /**
     * 
     * @return Returns random double between [0,1)
     */
    public static double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    /**
     * 
     * @param min minimum value (inclusive)
     * @param max maximum value (exclusive)
     * @return Returns random double between [min, max)
     */
    public static double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

}