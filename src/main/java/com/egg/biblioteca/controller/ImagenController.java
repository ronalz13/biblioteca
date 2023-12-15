/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.controller;

import com.egg.biblioteca.entities.Usuario;
import com.egg.biblioteca.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ronald
 */
@Controller
@RequestMapping("/imagen")
public class ImagenController {
    
    @Autowired
    private UsuarioService usuarioServ;
    
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUser(@PathVariable String id){
        
        Usuario user = usuarioServ.getOne(id);
        
        byte[] imagen = user.getImg().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        
       return new ResponseEntity<>(imagen, headers, HttpStatus.OK); 
        
    }
    
    }
