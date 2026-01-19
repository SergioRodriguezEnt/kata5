package software.ulpgc.kata5.architecture.model;

public record Movie(String title, int year, int duration) {
    public static Movie fromTsp(String tsp) {
        return fromSplitTsp(tsp.split("\t"));
    }

    private static Movie fromSplitTsp(String[] split) {
        return new Movie(split[2], toInt(split[5]), toInt(split[7]));
    }

    private static int toInt(String s) {
        if (s.equals("\\N")) return -1;
        return Integer.parseInt(s);
    }
}
