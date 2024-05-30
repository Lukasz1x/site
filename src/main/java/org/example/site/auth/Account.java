package org.example.site.auth;

public record Account(String username, int id)
{
    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", id=" + id +
                '}';
    }
}
