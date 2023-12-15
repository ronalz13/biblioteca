/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.controller;

import com.egg.biblioteca.entities.Usuario;
import com.egg.biblioteca.exceptions.MiException;
import com.egg.biblioteca.services.UsuarioService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ronald
 */
@Controller
@RequestMapping("/")
public class portalController {
    
    @Autowired
    private UsuarioService usuarioServ;

    @GetMapping("/")
    public String index() {

        return "index_index.html";
    }

    @GetMapping("/registrar")
    public String registrar(){
        
        return "registro.html";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre,@RequestParam String email,@RequestParam String password,
            @RequestParam String rePassword, ModelMap modelo, MultipartFile archivo){
        
        try {
            usuarioServ.registrar(archivo, nombre, email, password, rePassword);
            
            modelo.put("exito", "Usuario registrado con Exito! :D");
            return "index_index.html";
            
        } catch (MiException ex) {
            
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            
            return "registro.html";
        }
        
        
    }
    
     @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo){
        
         if (error != null) {
             modelo.put("error", "usuario o contraseña invalidos");
         }
        
        return "login.html";
    }
         
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session){
         
        Usuario logueado = (Usuario) session.getAttribute("usuariosesion");
       
        
        if(logueado.getRol().toString().equals("ADMIN")){
            
            return "redirect:/admin/dashboard";
        }
        
        return"index_user.html";
    }
           
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuariosesion");
        
        modelo.put("usuario", usuario);
        
        return "usuario_modificar.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre,@RequestParam String email,
    @RequestParam String password, @RequestParam String rePassword, ModelMap modelo){
    
        try {
            usuarioServ.actualizar(archivo, id, nombre, email, password, rePassword);
            modelo.put("exito", "Usuario actualizado con exito");
            
            return "redirect:/inicio";
        } catch (MiException ex) {
           
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            
            return"ususario_modificar.html";
        }
    }
    }