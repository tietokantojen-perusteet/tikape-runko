package tikape.runko.tmp;


public class RaakaAine_TMP {

	private String nimi;
	private String maara;

	public RaakaAine_TMP(String nimi, String maara) {
		this.nimi = nimi;
		this.maara = maara;
	}

	public String getNimi() {
		return nimi;
	}

	public String getMaara() {
		return maara;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public void setMaara(String maara) {
		this.maara = maara;
	}
	
	@Override
	public String toString() {
		return this.nimi + ", " + this.maara;
	}
	
	
}
