package up.visulog.analyzer;

import java.util.*;

import up.visulog.analyzer.CountCommitsPerDayPlugin.DateObj;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.NumberOfLinesPerAuthor;



public class CountNumberOfLinesPerAuthorPlugin implements AnalyzerPlugin{
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