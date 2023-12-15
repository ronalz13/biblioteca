
package com.egg.biblioteca.controller;

import com.egg.biblioteca.entities.Editorial;
import com.egg.biblioteca.exceptions.MiException;
import com.egg.biblioteca.services.EditorialService;
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
@RequestMapping("/editorial")
public class EditorialController {
    
    @Autowired
    private EditorialService editorialServ;
    
    @GetMapping("/registrar")
    public String edtitorial(){
        
        return "editorial_form";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo){
        
        try {
            editorialServ.crearEditorial(nombre);
            
            modelo.put("exito", "La editorial fue cargada con exito");
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
                 return"editorial_form";
        }
        
        return"index_admin.html";
    }
        
    @GetMapping("/lista")
    public String listado(ModelMap modelo){
        List<Editorial> editoriales = editorialServ.listarLibros();
        
        modelo.put("editoriales", editoriales);
        
        return "editoriales_list.html";
       
    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
       
        modelo.put("editorial",editorialServ.getOne(id) );
        
        return "editorial_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap modelo){
        
        try {
            editorialServ.modificaEditorial(id, nombre);
            
            return "redirect:../lista";
            
        } catch (MiException ex) {
            
            modelo.put("error", ex.getMessage());
            
            
            return "editorial_modificar.html";
        }
        
        
    }
   
}
