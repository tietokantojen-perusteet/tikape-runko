package tikape.runko.tmp;


import java.util.ArrayList;
import java.util.List;


public class Resepti_TMP {

	private String nimi;
	private final List<RaakaAine_TMP> raakaAineet;
	private String ohje;
        public Integer id;

	public Resepti_TMP() {
		this.raakaAineet = new ArrayList();
		this.nimi = "";
		this.ohje = "";
                this.id = -1;
	}
	
	public void lisaaRaakaAine(RaakaAine_TMP rkaine) {
		this.raakaAineet.add(rkaine);
	}

	public List<RaakaAine_TMP> getRaakaAineet() {
		return raakaAineet;
	}

	public String getOhje() {
		return ohje;
	}

	public void setOhje(String ohje) {
		this.ohje = ohje;
	}

	public String getNimi() {
		return nimi;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}
        
        public void setId(int id){
            this.id = id;
        }
	
	@Override
	public String toString() {
		String str = this.getNimi() + "\n";
		for (RaakaAine_TMP rkaine : this.raakaAineet) {
			str += rkaine.toString() + "\n";
		}
		
		return str += this.getOhje();
	}
	
	public void tyhjenna() {
		this.nimi = "";
		this.ohje = "";
		this.raakaAineet.clear();
	}
}
