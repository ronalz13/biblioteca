/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.controller;

import com.egg.biblioteca.entities.Autor;
import com.egg.biblioteca.entities.Editorial;
import com.egg.biblioteca.entities.Libro;
import com.egg.biblioteca.exceptions.MiException;
import com.egg.biblioteca.services.AutorService;
import com.egg.biblioteca.services.EditorialService;
import com.egg.biblioteca.services.LibroService;
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
 *
 * @author ronald
 */
@Controller
@RequestMapping("/libro")

public class LibroController {

    @Autowired
    private LibroService libroServ;
    @Autowired
    private AutorService autorServ;
    @Autowired
    private EditorialService editorialServ;

    @GetMapping("/registrar")
    public String libro(ModelMap modelo) {
        List<Autor> autores = autorServ.listarLibros();
        List<Editorial> editoriales = editorialServ.listarLibros();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_form";

    }

    @PostMapping("/registro")
    public String cargarLibro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
            @RequestParam(required = false) Integer ejemplares, @RequestParam String idAutor,
            @RequestParam String idEditorial, ModelMap modelo) {

        try {
            libroServ.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            modelo.put("exito", "el libro fue cargado con exito!!");

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            List<Autor> autores = autorServ.listarLibros();
            List<Editorial> editoriales = editorialServ.listarLibros();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);

            return "libro_form";
        }
        return "index";
    }

    @GetMapping("/lista")
    public String listado(ModelMap modelo){
        List<Libro> libros = libroServ.listarLibros();
        
        modelo.put("libros", libros);
        
        return "libros_lista.html";
    }
    
    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, ModelMap modelo){
        
       modelo.put("libro", libroServ.getOne(isbn));
        List<Autor> autores = autorServ.listarLibros();
        List<Editorial> editoriales = editorialServ.listarLibros();
        
        modelo.put("editoriales", editoriales);
        modelo.put("autores", autores);
       return "libro_modificar.html";
    }

     @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn,String titulo, Integer ejemplares, String idAutor, String idEditorial, ModelMap modelo){
        
        try {
            libroServ.modificarLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            
           return "redirect:../lista";
        } catch (MiException ex) {
            
            modelo.put("error", ex.getMessage());
            
           return "libro_modifcar.html" ;
        }
        
        
    }
}

