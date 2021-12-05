package up.visulog.analyzer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import up.visulog.analyzer.CountCommitsPerDayPlugin.DateObj;
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
    	List<String> command = new LinkedList<String>();
    	command.add("log");
    	if(configuration != null) {
			Map<String, String> settings = configuration.getPluginConfigs().get("CountMergeCommits").getSettings();
			if(settings.containsKey(dateDebutOption) || settings.containsKey(dateFinOption))
				command = dateAnalysis(command, settings);
			
			result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), command));
		}
    }
    
    public List<String> dateAnalysis(List<String> command, Map<String, String> settings){
    	command = addDateOption(command, settings, dateDebutOption, "--since=");
    	command = addDateOption(command, settings, dateFinOption, "--until=");
		
		return command;
    }
    
    public List<String> addDateOption(List<String> command, Map<String, String> settings, String parameterName, String gitOptionName){
		//On verifie qu'on a bien des clés parameterName
			if (settings.containsKey(parameterName)) {
				String debut = settings.get(parameterName);
				
				//On créé des tableux de notre string de date.
				String[] date = debut.split("/");

				//On verifie que l'on un tableau de dates de taille 3 chacuns pour le JJ/MM/AAAA.
				if (date.length == 3) {
					//On regarde si chaque partie de notre string est une date valide.
					try{
						int day = Integer.parseInt(date[0]);
						int mois = Integer.parseInt(date[1]);
						int annee = Integer.parseInt(date[2]);

						DateObj dateObj = new DateObj(day, mois, annee);

						//Regarde si les jours sont bien entre 1 et 31, les mois entre 1 et 12 etc. avec l'aide du constructeur de
						//la date
						if (dateObj.getDay() != 0 && dateObj.getIntMonth() != 0 && dateObj.getYear() != 0) {

							//Alors on passe nos parametres à la fonction. Gitlog prend le format AAAA/MM/JJ
							String formatGL1 = date[2] + "-" + date[1] + "-" + date[0];
							command.add(gitOptionName + formatGL1);
						}
					}
					catch (NumberFormatException ex){
						ex.printStackTrace();
					}
				}
			}
			
			return command;
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
