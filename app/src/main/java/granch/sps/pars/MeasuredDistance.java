package granch.sps.pars;

public class MeasuredDistance {
    public final short decawave_m;
    public final float distance;
    private final int packageNumber;
    private final byte protocolVersion;
    public long time;

    public MeasuredDistance(short decawave_m, long time, int packageNumber, byte protocolVersion) {
        this.decawave_m = decawave_m;
        this.distance = (float) (-1.31579E-10F * Math.pow(decawave_m, 3) + 8.47401e-07F * Math.pow(decawave_m, 2) + 0.003236103F * decawave_m + 0.273049717F);
        this.time = time;
        this.packageNumber = packageNumber;
        this.protocolVersion = protocolVersion;
    }

    @Override
    public String toString() {
        return "MeasuredDistance {" +
                "decawave_m=" + decawave_m +
                ", distance=" + distance +
                '}';
    }
}
