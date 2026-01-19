package software.ulpgc.kata5.architecture.viewmodel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Histogram implements Iterable<Integer> {
    private final Map<Integer, Integer> map;
    private final Map<String, String> labels;

    public Histogram(Map<String, String> labels) {
        this.labels = labels;
        this.map = new HashMap<>();
    }

    public void put(int bin) {
        map.put(bin, count(bin)+1);
    }

    public int count(int bin) {
        return map.getOrDefault(bin, 0);
    }

    public String title() {
        return labels.get("title");
    }

    public String x() {
        return labels.get("x");
    }

    public String y() {
        return labels.get("y");
    }

    public String legend() {
        return labels.get("legend");
    }

    @Override
    public Iterator<Integer> iterator() {
        return map.keySet().iterator();
    }
}
