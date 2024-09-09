package pl.dreamcode.errornotifier.users;

public interface PasswordMatchesForm {
    public String getPassword();

    public void setPassword(String password);

    public String getMatchingPassword();

    public void setMatchingPassword(String matchingPassword);
}
