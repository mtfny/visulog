package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import java.util.Calendar;
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
        LocalDate first = LocalDate.of(dateFirst.year, dateFirst.month+1, dateFirst.day);
        LocalDate last = LocalDate.of(dateLast.year, dateLast.month+1, dateLast.day+1);
        
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
    	if(configuration != null) {
			Map<String, String> settings = configuration.getPluginConfigs().get("CountCommitsPerDay").getSettings();
			if (settings.isEmpty()) {
				result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
			}
			else {
				//On verifie qu'on a bien des clés "-Debut" et "-Fin".
				if (settings.containsKey("-Debut") && settings.containsKey("-Fin")) {
					String debut = settings.get("-Debut");
					String fin = settings.get("-Fin");
					
					//On créé des tableux de nos strings de date.
					String[] datedebut = debut.split("/");
					String[] datefin = fin.split("/");

					//On verifie que l'on un tableau de dates de taille 3 chacuns pour le JJ/MM/AAAA.
					if (datedebut.length == 3 && datefin.length == 3) {
						//On regarde si chaque partie de notre string est une date valide.
						try{
							int daydebut = Integer.parseInt(datedebut[0]);
							int moisdebut = Integer.parseInt(datedebut[1]);
							int anneedebut = Integer.parseInt(datedebut[2]);
							
							int dayfin = Integer.parseInt(datefin[0]);
							int moisfin = Integer.parseInt(datefin[1]);
							int anneefin = Integer.parseInt(datefin[2]);

							DateObj dateObjDebut = new DateObj(daydebut, moisdebut, anneedebut);
							DateObj dateObjFin = new DateObj(dayfin, moisfin, anneefin);

							//Regarde si les jours sont bien entre 1 et 31, les mois entre 1 et 12 etc. avec l'aide du constructeur de
							//la date
							if (dateObjDebut.getDay() != 0 && dateObjDebut.getIntMonth() != 0 && dateObjDebut.getYear() != 0
							&& dateObjFin.getDay() != 0 && dateObjFin.getIntMonth() != 0 && dateObjFin.getYear() != 0) {
								//Si la date de début est inferieure à la date de fin.

								if (dateObjDebut.compareTo(dateObjFin) == -1) {
									//Alors on passe nos parametres à la fonction. Gitlog prend le format AAAA/MM/JJ
									String formatGL1 = datedebut[2] + "-" + datedebut[1] + "-" + datedebut[0];
									String formatGL2 = datefin[2] + "-" + datefin[1] + "-" + datefin[0];
									System.out.println(formatGL1);
									System.out.println(formatGL2);
							        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), formatGL1, formatGL2));
								}
							}
						}
						catch (NumberFormatException ex){
							ex.printStackTrace();
						}
					}
				}
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
            int firstDay = commitsPerDate.isEmpty() ? -1 : commitsPerDate.firstKey().weekDay;
            html.append("</div><div class=\"days\" data-day-start=\"" + firstDay + "\">");
            
            for(var item : commitsPerDate.entrySet()) {
            	html.append("<div class=\"case\" data-commit-number=\"").append(item.getValue()).append("\" data-date=\"").append(item.getKey().getDay() + " " + item.getKey().getMonth()).append("\"></div>");
            }
            
            html.append("</div></div></div>");
            return html.toString();
        }
    }
    
    private static class DateObj implements Comparable<DateObj>{
    	private final int day;
    	private final int weekDay;
    	private final int month;
    	private final int year;
    	private final String[] weekDays = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
		private final String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre","Novembre", "Décembre"};

    	public DateObj(int day, int weekDay, int month, int year) {
    		this.day = day;
    		this.year = year;
    		
    		
    		//On applique -1 car ce sont des index de tableau
    		//Si la valeur du jour/mois n'est pas cohérent (ex : mois = 14) on met 0 par défaut
    		this.weekDay = weekDay-1 < 0 || weekDay-1 >= weekDays.length ? 0 : weekDay-1;
    		this.month = month-1 < 0 || month-1 >= months.length ? 0 : month-1;
    	}

		public DateObj(int day, int month, int year) {
			if (day >= 1 && day <= 31) this.day = day;
			else this.day = 0;

			if (year >= 1990) this.year = year;
			else this.year = 0;

			if (month >= 1 && month <= 12) this.month = month;
			else this.month = 0;
			this.weekDay = 0;
		}
    	
    	public int getDay() { return day; }
    	public String getWeekDay() { return weekDays[weekDay]; }
		public int getIntMonth() { return month;}
    	public String getMonth() { return months[month]; }
    	public int getYear() { return year; }
    	public String monthAndYear() { return months[month].substring(0,3) + " " + String.valueOf(year);}
    	
    	public String toString() {
    		return weekDays[weekDay] + " " + String.valueOf(day) + " " + months[month] + " " + String.valueOf(year);
    	}
    	
    	@Override
    	public int compareTo(DateObj o) {
    		if(this.year == o.year) {
    			
    			if(this.month == o.month) {
    				
    				if(this.day == o.day)
    					return 0;
    				if(this.day < o.day)
    					return -1;
    				else
    					return 1;
    			}
    			
    			if(this.month < o.month)
    				return -1;
    			else
    				return 1;
    		}
    		
    		if(this.year < o.year)
    			return -1;
    		else
    			return 1;
    	}
    	
    	public boolean equals(Object o) {
    		if(o == null) return false;
    		if(o == this) return true;
    		if(!(o instanceof DateObj)) return false;
    		DateObj d = (DateObj)o;
    		if(d.day == this.day && d.weekDay == this.weekDay && d.month == this.month && d.year == this.year)
    			return true;
    		return false;
    	}
    	
    	public int hashCode() {
    		int result = 17;
            result = 31 * result + day;
            result = 31 * result + weekDay;
            result = 31 * result + month;
            result = 31 * result + year;
            return result;
    	}
    }
}
