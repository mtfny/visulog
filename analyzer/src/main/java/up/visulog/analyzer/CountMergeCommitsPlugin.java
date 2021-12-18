package up.visulog.analyzer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

public class CountMergeCommitsPlugin extends DateAnalyzerPlugin{
	private final Configuration configuration;
    private Result result;
    
    public CountMergeCommitsPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }
    
    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        if(gitLog == null)
    		return result;
        
        for (var commit : gitLog) {
        	if(commit.mergedFrom!=null) {
        		result.NumberOfMergeCommits++;
        	}
        }
        return result;
    }
    
    @Override
    public void run() {
    	List<String> command = new LinkedList<String>();
    	command.add("log");
    	if(configuration != null) {
			Map<String, String> settings = configuration.getPluginConfigs().get("CountMergeCommits").getSettings();
			if(settings.containsKey(dateDebutOption) || settings.containsKey(dateFinOption))
				command = dateAnalysis(command, settings);
			
			result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), command));
		}
    }

    @Override
    public Result getResult() {
    	//If the analysis hasn't already been run, it is run and only then is the result returned
        if (result == null) run();
        return result;
    }
    
    static class Result implements AnalyzerPlugin.Result {
        private int NumberOfMergeCommits=0;

        //Method that returns number of merge commits.
        int getNumberOfMergeCommits() {
            return NumberOfMergeCommits;
        }

        @Override
        //Method that returns the numberOfMergeCommits in String form
        public String getResultAsString() {
            return Integer.toString(NumberOfMergeCommits);
        }

        @Override
      //Method that returns a String which can then be used with an html page
        public String getResultAsHtmlDiv() {
        	StringBuilder html = new StringBuilder("<div class=\"module\" hidden>mergeCounter</div><div id=\"data-merge-number\" hidden>");
            html.append("<div data-merge-number=").append(NumberOfMergeCommits).append("></div>");
            html.append("</div>");
            return html.toString();
        }
    }

}
