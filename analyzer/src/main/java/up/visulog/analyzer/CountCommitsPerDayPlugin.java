package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import java.util.Calendar;
import java.util.LinkedList;
import java.time.LocalDate;

import java.util.TreeMap;
import java.util.List;
import java.util.Map;

public class CountCommitsPerDayPlugin implements AnalyzerPlugin {
	private final Configuration configuration;
    private Result result;
    private static final int maxCommit = 10;

    public CountCommitsPerDayPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    //Returns a result object that contains the number of commits per day
    static Result processLog(List<Commit> gitLog) {   	
    	Result result = new Result();
    	if(gitLog == null || gitLog.isEmpty())
    		return result;
    	
    	Calendar cal = Calendar.getInstance();


		
    	//Two DateObj variables are created in order to retrieve the dates of both the earliest and latest commits contained in the gitLog list.
    	DateObj dateFirst = null;
    	DateObj dateLast = null;
    	
        for (var commit : gitLog) {
        	if(commit.date != null) {
        		cal.setTime(commit.date);
        		DateObj date = null;
        		//Since the value DAY_OF_WEEK attribute of Calendar objects start with Sundays, an if/else statement for the case in which the day of the week is a Sunday is needed
        		int dayOfWeek = cal.get(cal.DAY_OF_WEEK);
        		if(dayOfWeek == 1) {
        			dayOfWeek = 7;
        		}else {
        			dayOfWeek--;
        		}
        		date = new DateObj(cal.get(cal.DAY_OF_MONTH), dayOfWeek, cal.get(cal.MONTH)+1, cal.get(cal.YEAR));
        		Integer nb = result.commitsPerDate.getOrDefault(date, 0);
        		result.commitsPerDate.put(date, nb + 1);
        		
        		if(dateFirst == null || date.compareTo(dateFirst) == -1) dateFirst = date;
        		if(dateLast == null || date.compareTo(dateLast) == 1) dateLast = date;
        	}
        }
        
        //The dates of the first and last commits of the gitlog list are retrieved.
        LocalDate first = LocalDate.of(dateFirst.getYear(), dateFirst.getIntMonth()+1, dateFirst.getDay());
        LocalDate last = LocalDate.of(dateLast.getYear(), dateLast.getIntMonth()+1, dateLast.getDay()+1);
        
        //We iterate between the dates of the earliest and latest commits of the gitLog list. If a date in that interval of time is not a key in the result.commitsPerDate TreeMap (if no commits were published that day), then it is added as a key to the TreeMap with an associated value of 0 (so as to indicate that no commits were published that day).
        for (LocalDate date = first; date.isBefore(last); date = date.plusDays(1)) {
        	DateObj dateIter = new DateObj(date.getDayOfMonth(), date.getDayOfWeek().ordinal()+1, date.getMonthValue(), date.getYear());
        	if(! result.commitsPerDate.containsKey(dateIter)) {
        		result.commitsPerDate.put(dateIter, 0);
        	}
        }
        return result;
    }

    @Override
    public void run() {
    	List<String> command = new LinkedList<String>();
    	command.add("log");
    	
    	if(configuration != null) {
			Map<String, String> settings = configuration.getPluginConfigs().get("CountCommitsPerDay").getSettings();
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
    	//On utilise une treeMap car les éléments sont triés par clé (donc par date)
        private final TreeMap<DateObj, Integer> commitsPerDate = new TreeMap<>();

        //Method that returns the hashmap that contains the number of commits associated with each day
        Map<DateObj, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }

        @Override
        //Method that returns the commitsPerDay list in String form
        public String getResultAsString() {
            return commitsPerDate.toString();
        }

        @Override
        //Method that returns a String which can then be used to display the commitsPerAuthor list as an html page
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div class=\"module\" hidden>commitActivity</div><div id=\"grid-container\"><div id=\"day-list\"><div id=\"month-space\"></div><div id=\"days-list-name\"><p>Lun</p><p>Mar</p><p>Mer</p><p>Jeu</p><p>Ven</p><p>Sam</p><p>Dim</p></div></div><div id =\"stats-grid\"><p id=\"max-commit\" hidden>" + maxCommit + "</p><div class=\"months\">");
            String lastShortDate ="";
            for (var item : commitsPerDate.entrySet()) {
            	String shortDate = item.getKey().monthAndYear();
            	
            	if(!shortDate.equals(lastShortDate)) {
                    html.append("<p>").append(shortDate).append("</p>");
                    lastShortDate = shortDate;
            	}
            }
            
            //Le numéro du premier jour trouvé est utile pour l'affichage en Javascript
            int firstDay = commitsPerDate.isEmpty() ? -1 : commitsPerDate.firstKey().getWeekDayInt();
            html.append("</div><div class=\"days\" data-day-start=\"" + firstDay + "\">");
            
            for(var item : commitsPerDate.entrySet()) {
            	html.append("<div class=\"case\" data-commit-number=\"").append(item.getValue()).append("\" data-date=\"").append(item.getKey().getDay() + " " + item.getKey().getMonth()).append("\"></div>");
            }
            
            html.append("</div></div></div>");
            return html.toString();
        }
    }
    
    
}
