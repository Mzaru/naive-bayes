import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Observation {
    private List<String> atts;
    private String answer;
    private String decision;

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Observation(List<String> atts) {
        this.atts = atts;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getAtts() {
        return atts;
    }

    public static List<Observation> readObservations(String file) {
        List<Observation> observations = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                observations.add(parseObservation(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return observations;
    }

    public static Observation parseObservation(String line) {
        List<String> atts = new ArrayList<>();
        String[] parse = line.split(",");
        for (int i = 0; i < parse.length - 1; i++) {
            atts.add(parse[i]);
        }
        Observation o = new Observation(atts);
        o.setAnswer(parse[parse.length - 1]);
        return o;
    }

    public static void printResults(List<Observation> list) {
        double correct = 0;
        for(Observation o : list) {
            System.out.println(o);
            if (o.getAnswer().equals(o.getDecision())) {
                ++correct;
            }
        }
        double acc = correct / list.size();
        System.out.println("Accuracy = " + acc * 100 + "%");
    }

    @Override
    public String toString() {
        return String.format("Attribs: %s;  Ans: %s; Dec: %s;", atts.toString(), answer, decision);
    }

}
