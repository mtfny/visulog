package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        if(gitLog == null)
    		return result;
        
        for (var commit : gitLog) {
            var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0);
            result.commitsPerAuthor.put(commit.author, nb + 1);
        }
        return result;
    }

    @Override
    public void run() {
    	if(configuration != null)
    		result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }

    @Override
    public Result getResult() {
    	//If the analysis hasn't already been run, it is run and only then is the result returned
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>();

        //Method that returns the hashmap that contains the number of commits associated with each author 
        Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }

        @Override
        //Method that returns the commitsPerAuthor list in String form
        public String getResultAsString() {
            return commitsPerAuthor.toString();
        }

        @Override
        //Method that returns a String which can then be used to display the commitsPerAuthor list as an html page
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div class=\"module\" hidden>contributorActivity</div><div id=\"data-contributor-activity\" hidden>");
            for (var item : commitsPerAuthor.entrySet()) {
                html.append("<div data-contributor-name=\"").append(item.getKey()).append("\">").append(item.getValue()).append("</div>");
            }
            html.append("</div>");
            return html.toString();
        }
    }
}
