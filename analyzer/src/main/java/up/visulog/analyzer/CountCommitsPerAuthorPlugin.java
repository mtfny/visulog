package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.*;

public class CountCommitsPerAuthorPlugin implements DateAnalyzerPlugin {
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
    	List<String> command = new LinkedList<>();
    	command.add("log");
    	
    	if(configuration != null) {
			Map<String, String> settings = configuration.getPluginConfigs().get("CountCommitsPerAuthor").getSettings();
			if(settings.containsKey(dateDebutOption) || settings.containsKey(dateFinOption))
				command = dateAnalysis(command, settings);
			result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), command));
            if(settings.containsKey(sortNumerically) || settings.containsKey(sortAlphabetically)){
                if(settings.containsKey(sortAlphabetically)) result.sortValues();
                if (settings.containsKey(sortNumerically))result.sortKeys();
                if(settings.containsKey(reverse))result.reverse();
            }
		}
    }

    @Override
    public Result getResult() {
    	//If the analysis hasn't already been run, it is run and only then is the result returned
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private  LinkedHashMap<String, Integer> commitsPerAuthor = new LinkedHashMap<>();

        private void sortValues() {
            LinkedList<Map.Entry<String,Integer>> list = new LinkedList<>(commitsPerAuthor.entrySet());
            list.sort( (o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));

            //copying the sorted list in HashMap to preserve the iteration order
            LinkedHashMap<String,Integer> sortedHashMap = new LinkedHashMap<>();
            Iterator<Map.Entry<String,Integer>> it = list.iterator();

            while (it.hasNext()) {
                Map.Entry<String,Integer> entry = it.next();
                sortedHashMap.put(entry.getKey(),entry.getValue());
            }
            commitsPerAuthor = sortedHashMap;
        }
        private void sortKeys() {
            LinkedList<Map.Entry<String,Integer>> list = new LinkedList<>(commitsPerAuthor.entrySet());
            list.sort( (o1, o2) -> (o1.getKey()).compareToIgnoreCase(o2.getKey()));

            //copying the sorted list in HashMap to preserve the iteration order
            LinkedHashMap<String,Integer> sortedHashMap = new LinkedHashMap<>();
            Iterator<Map.Entry<String,Integer>> it = list.iterator();
            while (it.hasNext()) {
                Map.Entry<String,Integer> entry = it.next();
                sortedHashMap.put(entry.getKey(),entry.getValue());
            }
            commitsPerAuthor = sortedHashMap;
        }
        private void reverse() {
            LinkedList<Map.Entry<String,Integer>> list = new LinkedList<>(commitsPerAuthor.entrySet());
            Collections.reverse(list);

            //copying the sorted list in HashMap to preserve the iteration order
            LinkedHashMap<String,Integer> sortedHashMap = new LinkedHashMap<>();
            Iterator<Map.Entry<String,Integer>> it = list.iterator();
            while (it.hasNext()) {
                Map.Entry<String,Integer> entry = it.next();
                sortedHashMap.put(entry.getKey(),entry.getValue());
            }
            commitsPerAuthor = sortedHashMap;
        }


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
