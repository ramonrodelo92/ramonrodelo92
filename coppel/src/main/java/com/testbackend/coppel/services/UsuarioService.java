package com.testbackend.coppel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testbackend.coppel.models.Usuario;
import com.testbackend.coppel.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario getUsuario(String email, String contrasena) {
        return usuarioRepository.findUsuarioByEmailAndContrasena(email, contrasena);
    }
}
