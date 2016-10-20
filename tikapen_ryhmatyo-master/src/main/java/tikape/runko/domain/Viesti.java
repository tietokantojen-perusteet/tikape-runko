package tikape.runko.domain;

import java.sql.Timestamp;

public class Viesti {

    private Integer id;
    private String nimi;
    private String text;
    private Timestamp time;
    private Aihe aihe;
    

    public Viesti(Aihe aihe, Integer id, String nimi, String text, Timestamp time) {
        this.aihe = aihe;
        this.id = id;
        this.nimi = nimi;
        this.text = text;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the aihe
     */
    public Aihe getAihe() {
        return aihe;
    }

    /**
     * @param aihe the aihe to set
     */
    public void setAihe(Aihe aihe) {
        this.aihe = aihe;
    }

    /**
     * @return the time
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }

}
