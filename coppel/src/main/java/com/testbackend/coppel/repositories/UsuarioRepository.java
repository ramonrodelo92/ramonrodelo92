package com.testbackend.coppel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.testbackend.coppel.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    public abstract Usuario findUsuarioByEmailAndContrasena(String email, String contrasena);

    public Optional<Usuario> findOneByEmail(String email);
}
