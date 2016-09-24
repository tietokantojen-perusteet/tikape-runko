package tikape.runko.domain;

public class User {

    private int id;
    private String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * Palauttaa käyttäjän id:n
     *
     * @return integer
     */
    public int getId() {
        return id;
    }

    /**
     * Palauttaa käyttäjätunnuksen
     *
     * @return String
     */
    public String getUsername() {
        return username;
    }

}
