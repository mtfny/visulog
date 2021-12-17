package up.visulog.analyzer;

public class DateObj implements Comparable<DateObj>{
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
    	public int getWeekDayInt() { return weekDay; }
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