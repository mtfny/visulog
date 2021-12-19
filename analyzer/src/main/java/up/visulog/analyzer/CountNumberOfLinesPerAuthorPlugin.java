package up.visulog.analyzer;

import java.util.*;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.NumberOfLinesPerAuthor;

public class CountNumberOfLinesPerAuthorPlugin implements DateAnalyzerPlugin{
    private final Configuration configuration;
    private Result result;

    public CountNumberOfLinesPerAuthorPlugin(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void run() {
    	List<String> command = new LinkedList<String>();
    	command.add("log");
    	
    	if(configuration != null) {
			Map<String, String> settings = configuration.getPluginConfigs().get("CountNumberOfLinesPerAuthor").getSettings();
			if(settings.containsKey(dateDebutOption) || settings.containsKey(dateFinOption))
				command = dateAnalysis(command, settings);
			
			result = new Result(NumberOfLinesPerAuthor.parseLogFromCommand(configuration.getGitPath(), command));
		}
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
            StringBuilder html = new StringBuilder("<div class=\"module\" hidden>linesPerContributor</div><div id=\"data-contributor-lines\" hidden>");
            
            for (var item : NlinesPerAuthor) {
                html.append("<div data-contributor=\"").append(item.getName()).append("\">").append(item.getLines_added() + item.getLines_removed()).append("</div>");
            }
            html.append("</div>");
            return html.toString();
        }
    }
}