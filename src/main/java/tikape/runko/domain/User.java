package tikape.runko.domain;

public class User {

    private int id;
    private String username;
    private String password;
    private String salt;
    private int userLevel = -1;

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

    public String getPasswordHash() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public User setPasswordHash(String hash) {
        password = hash;
        return this;
    }

    public User setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public User setUserLevel(int level) {
        userLevel = level;
        return this;
    }

    @Override
    public String toString() {
        return id + " : " + username; //To change body of generated methods, choose Tools | Templates.
    }

}
