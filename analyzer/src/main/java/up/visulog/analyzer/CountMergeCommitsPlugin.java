package up.visulog.analyzer;

import java.util.List;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

public class CountMergeCommitsPlugin implements AnalyzerPlugin{
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
            return "<div>Number Of Merge commits : "+getResultAsString()+" </div>";
        }
    }

}
