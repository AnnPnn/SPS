package granch.sps.pars;


import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class DistanceMessage  {
    public ConcurrentHashMap<String, MeasuredDistance> map = new ConcurrentHashMap<>();
    public static NavigableMap<String, ConcurrentHashMap <String, MeasuredDistance>> Distances= new TreeMap<>();
}
