package br.jus.tjes.integracao.drive.security;

public class CustomAuthToken {
    private String token;

    public CustomAuthToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
