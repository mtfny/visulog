package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.ProgrammingLanguage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountPLPecrcenrage implements AnalyzerPlugin{
    private final Configuration configuration;
    private Result result;

    public CountPLPecrcenrage(Configuration configuration) {
        this.configuration = configuration;
    }

    static Result processPLpercentage(List<ProgrammingLanguage> Planguages) {
        var result = new Result();
        int total_size=0;
        for (var PL : Planguages) {
            total_size+= PL.size;
        }
        for (var PL : Planguages) {
            var nb = result.PLpercentage.getOrDefault(PL.name, 0.0);
            double c= nb + PL.size;
            double percentage=PercentageComparedTo(total_size,c);
            result.PLpercentage.put(PL.name, nb + PL.size);
        }
        return result;
    }


    @Override
    public void run() {
        result = processPLpercentage(ProgrammingLanguage.parsePLpercentageCommand());
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    private static double PercentageComparedTo(double total,double x){ double res= (x*100)/total;return res; }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Double> PLpercentage = new HashMap<>();

        //Method that returns the hashmap that contains the percentage of each programmibng language
        public Map<String, Double> getPLpercentage() { return PLpercentage; }

        @Override
        //Method that returns the PLpercentage list in String form
        public String getResultAsString() {
            return PLpercentage.toString();
        }

        @Override
        //Method that returns a String which can then be used to display the PLpercentage list as an html page
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>programming languages percentage: <ul>");
            for (var item : PLpercentage.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }

}
