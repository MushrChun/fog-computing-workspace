package au.edu.sydney.uni.fogcomputingclient;

/**
 * Created by cshe6391 on 23/03/18.
 */

public class DetectionFrame {
    public int x;
    public int y;
    public int w;
    public int h;
    public String label;

    @Override
    public String toString() {
        return label + ":" + x + ":" + y + ":" + w + ":" + h;
    }
}
