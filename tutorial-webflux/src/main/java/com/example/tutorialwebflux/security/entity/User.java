package com.example.tutorialwebflux.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    private int id;
    private String username;
    private String email;
    private String password;
    private String roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(roles.split(", ")).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /*
     La variable roles es una cadena que contiene roles separados por comas
     Por ejemplo, "ROLE_USER, ROLE_ADMIN"
     Se divide la cadena en roles individuales y se mapean a objetos SimpleGrantedAuthority
     Los objetos SimpleGrantedAuthority representan roles en Spring Security

     ---

    roles.split(", "): Divide la cadena roles en una matriz de cadenas utilizando la coma y el espacio como delimitadores.
    Esto es útil si la cadena de roles está en el formato "ROLE_USER, ROLE_ADMIN".

    Stream.of(...) : Convierte la matriz resultante en un flujo (stream) de cadenas.

    map(SimpleGrantedAuthority::new): Por cada cadena en el flujo, crea un objeto SimpleGrantedAuthority.
    SimpleGrantedAuthority es una implementación de la interfaz GrantedAuthority que Spring Security utiliza
    para representar roles.

    collect(Collectors.toList()): Recolecta los objetos SimpleGrantedAuthority en una lista.
    La lista resultante se utiliza como la colección de autoridades (roles) para el usuario.


     */

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
