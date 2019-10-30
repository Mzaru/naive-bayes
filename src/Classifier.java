import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Classifier {
    private List<Observation> train;
    private List<String> decisions;

    public Classifier(List<Observation> train) {
        this.train = train;
        decisions = train.stream().map(observation -> observation.getAnswer()).distinct().collect(Collectors.toList());
    }

    public void classify(Observation o) {
        List<Double> probs = new ArrayList<>();
        for (String decision : decisions) {
            double denominator = train.stream().filter(observation -> observation.getAnswer().equals(decision)).count();
            double genProb = denominator / train.stream().count();
            double condProbs = 1;
            for (int i = 0; i < o.getAtts().size(); i++) {
                final int ind = i;
//                double prob = train.stream().filter(observation -> observation.getAtts().get(ind).equals(o.getAtts().get(ind))).count();
                double prob = train.stream().filter(observation -> observation.getAnswer().equals(decision)).filter(observation -> observation.getAtts().get(ind).equals(o.getAtts().get(ind))).count();
                prob += 1;
                prob /= (denominator + train.stream().map(observation -> observation.getAtts().get(ind)).distinct().count());
                condProbs *= prob;
            }
            double prob = genProb * condProbs;
            probs.add(prob);
        }
        int dec = getMaxIndex(probs);
        o.setDecision(decisions.get(dec));
    }

    public static int getMaxIndex(List<Double> list) {
        double max = list.get(0);
        int maxInd = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > max) {
                max = list.get(i);
                maxInd = i;
            }
        }
        return maxInd;
    }

    public Observation input() {
        int n = train.get(0).getAtts().size();
        Scanner in = new Scanner(System.in);
        System.out.printf("Enter the data (%d times)%n", n);
        List<String> data = new ArrayList<>();
        String line;
        for (int i = 0; i < n; i++) {
            line = in.nextLine();
            data.add(line);
        }
        return new Observation(data);
    }

    public static void main(String[] args) {
        Classifier c = new Classifier(Observation.readObservations("train"));
        List<Observation> o = Observation.readObservations("test");
        o.forEach(observation -> c.classify(observation));
        Observation.printResults(o);
        while(true) {
            Observation obs = c.input();
            c.classify(obs);
            System.out.println(obs);
        }
    }

}
