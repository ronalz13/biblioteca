/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.controller;

import com.egg.biblioteca.entities.Usuario;
import com.egg.biblioteca.services.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ronald
 */

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UsuarioService usuarioServ;
    
    @GetMapping("/dashboard")
    public String panelAdministrador(){
        
        return "index_admin.html";
        
    }
    
    @GetMapping("/usuarios")
    public String listaUsuarios(ModelMap modelo){
       List <Usuario> usuarios = usuarioServ.listarUsuarios();
       modelo.put("usuarios", usuarios);
 
        return "usuarios_list.html";
    }
    
    
    @GetMapping("/rol/{id}")
    public String cambiarROL(@PathVariable String id){
        
        usuarioServ.cambiarRol(id);
        
     return  "redirect:/admin/usuarios";  
    }
    
}
