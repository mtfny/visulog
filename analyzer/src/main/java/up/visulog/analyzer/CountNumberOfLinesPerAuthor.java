package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.NumberOfLinesPerAuthor;
import up.visulog.gitrawdata.ProgrammingLanguage;

import java.util.*;

public class CountNumberOfLinesPerAuthor implements AnalyzerPlugin{
    private final Configuration configuration;
    private Result result;

    public CountNumberOfLinesPerAuthor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void run() {
        if(configuration != null)
            result = new Result(NumberOfLinesPerAuthor.parseLogFromCommand());
    }

    @Override
    public Result getResult() {
        //If the analysis hasn't already been run, it is run and only then is the result returned
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private List<NumberOfLinesPerAuthor> NlinesPerAuthor ;

        public Result(List<NumberOfLinesPerAuthor> nlinesPerAuthor) {
            NlinesPerAuthor = nlinesPerAuthor;
        }

        //Method that returns the hashmap that contains the percentage of each programmibng language
        public List<NumberOfLinesPerAuthor> getNlinesPerAuthor() { return NlinesPerAuthor; }

        @Override
        //Method that returns the PLpercentage list in String form
        public String getResultAsString() {
            String res="";
            for(NumberOfLinesPerAuthor n : NlinesPerAuthor){
                res=res+n.toString()+"\n";
            }
            return res;
        }

        @Override
        //Method that returns a String which can then be used to display the PLpercentage list as an html page
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>programming languages percentage: <ul>");
            for (var item : NlinesPerAuthor) {
                html.append("<li>").append(item.getName()).append(": ").append("lines added: ").append(item.getLines_added()).append("lines removed: ").append(item.getLines_removed()).append("total").append(item.getLines_added()-item.getLines_removed()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
}

