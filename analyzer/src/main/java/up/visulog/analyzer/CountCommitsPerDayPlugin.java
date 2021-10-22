package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountCommitsPerDayPlugin implements AnalyzerPlugin {
	private final Configuration configuration;
    private Result result;

    public CountCommitsPerDayPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        Result result = new Result();
        for (var commit : gitLog) {
            Integer nb = result.commitsPerDate.getOrDefault(commit.date, 0);
            result.commitsPerDate.put(commit.date.toString(), nb + 1);
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }

    @Override
    public Result getResult() {
    	//If the analysis hasn't already been run, it is run and only then is the result returned
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commitsPerDate = new HashMap<>();

        //Method that returns the hashmap that contains the number of commits associated with each author 
        Map<String, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }

        @Override
        //Method that returns the commitsPerAuthor list in String form
        public String getResultAsString() {
            return commitsPerDate.toString();
        }

        @Override
        //Method that returns a String which can then be used to display the commitsPerAuthor list as an html page
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per date: <ul>");
            for (var item : commitsPerDate.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
}
