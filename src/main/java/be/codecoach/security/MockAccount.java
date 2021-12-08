package be.codecoach.security;

import be.codecoach.security.authentication.user.Authority;
import be.codecoach.security.authentication.user.api.Account;
import com.google.common.collect.Lists;

import java.util.List;

public class MockAccount implements Account {
    @Override
    public Long getId() {
        return 1L;
    }

    @Override
    public String getEmail() {
        return "test@test.com";
    }

    @Override
    public String getPassword() {
        return "$2a$04$CASajUUVJpKk6w3XaxU2Jevpp02QmdX4FhqaNu4Z9/Yqai/3qe8WO";
    } //codecoach

    @Override
    public void setPassword(String encode) {

    }

    @Override
    public List<Authority> getAuthorities() {
        return Lists.newArrayList(Authority.ADMIN);
    }

    @Override
    public boolean isAccountEnabled() {
        return true;
    }

    @Override
    public void enableAccount() {

    }
}