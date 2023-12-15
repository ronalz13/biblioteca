/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.controller;

import com.egg.biblioteca.entities.Autor;
import com.egg.biblioteca.exceptions.MiException;
import com.egg.biblioteca.services.AutorService;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ronald
 */
@Controller
@RequestMapping("/autor")//locahost:8080/autor

public class autorController {
   
    @Autowired
    private AutorService autorServ;
    
    @GetMapping("/registrar")//localhost:8080/autor/registrar
    public String registrar(){
        
        return "autor_form";
    }
    
    @PostMapping("/registro")//llama a al mismo url que registrar pero realiza el post
    public String registro(@RequestParam String nombre, ModelMap modelo){
        try {
            // llamamos al metodo de servicio que se encarga de llamar a la persistencia
            autorServ.crearAutor(nombre);
            
           modelo.put("exito", "el autor se guardo exitosamente");
           
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "autor_form";
        }
        
        
        return "index_admin";
        
    }
    
    @GetMapping("/lista")//llama al url donde vamos a renderizar la tabla
    public String listar(ModelMap modelo){
        List<Autor> autores = autorServ.listarLibros();
        
        modelo.put("autores", autores);
        
        return "autores_lista";
    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
         
        modelo.put("autor", autorServ.getOne(id));
        
        return "autor_modifcar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id,String nombre, ModelMap modelo){
        
        try {
            autorServ.modificarAutor(id, nombre);
            modelo.put("exito", "el autor se modifico con exito");
           return "redirect:../lista";
           
        } catch (MiException ex) {
            
            modelo.put("error", ex.getMessage());
            
           return "autor_modifcar.html" ;
        }
        
        
    }
}
