import org.json.JSONObject;

public class User {
    private String id;
    private String password;
    private String roles;
    private String fullName;

    public User(JSONObject userJson) {
        this.id = userJson.getString("id");
        this.password = userJson.getString("password");
        this.roles = userJson.getString("roles");
        this.fullName = userJson.optString("fullName");
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getRoles() {
        return roles;
    }

    public String getFullName() {
        return fullName;
    }
}
