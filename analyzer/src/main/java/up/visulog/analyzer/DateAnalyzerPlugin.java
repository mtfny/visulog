package up.visulog.analyzer;

import java.util.List;
import java.util.Map;

public interface DateAnalyzerPlugin  {
	public static final String dateDebutOption = "-Debut";
	public static final String dateFinOption = "-Fin";
	
	/*
	 * Interface implemented by plugins that may take starting and/or ending dates into account during the analysis, if at least one such date is specified by the user
	 */
	
	public default List<String> dateAnalysis(List<String> command, Map<String, String> settings){
    	command = addDateOption(command, settings, dateDebutOption, "--since=");
    	command = addDateOption(command, settings, dateFinOption, "--until=");
		
		return command;
    }
    
    public default List<String> addDateOption(List<String> command, Map<String, String> settings, String parameterName, String gitOptionName){
    	//On verifie qu'on a bien des clés parameterName
    	if (settings.containsKey(parameterName)) {
    		String debut = settings.get(parameterName);
    		
    		//On créé des tableaux de notre string de date.
    		String[] date = debut.split("/");
    		
    		//On verifie que l'on un tableau de dates de taille 3 chacuns pour le JJ/MM/AAAA.
    		if (date.length == 3) {
    			//On regarde si chaque partie de notre string est une date valide.
    			try{
    				int day = Integer.parseInt(date[0]);
    				int mois = Integer.parseInt(date[1]);
    				int annee = Integer.parseInt(date[2]);
    				
    				DateObj dateObj = new DateObj(day, mois, annee);
    				
    				//Regarde si les jours sont bien entre 1 et 31, les mois entre 1 et 12 etc. avec l'aide du constructeur de la date
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
}
