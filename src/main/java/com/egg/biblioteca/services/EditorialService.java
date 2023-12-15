/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.services;

import com.egg.biblioteca.entities.Autor;
import com.egg.biblioteca.entities.Editorial;
import com.egg.biblioteca.exceptions.MiException;
import com.egg.biblioteca.repository.EditorialRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ronald
 *
 */
@Service
public class EditorialService {

    @Autowired
    private EditorialRepository editorialRepo;

    @Transactional
    public void crearEditorial(String nombre) throws MiException {
        validar(nombre);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);

        editorialRepo.save(editorial);

    }
    
    public Editorial getOne(String id){
       Editorial e1 = editorialRepo.getOne(id);
        
        return e1;
    }

    public List<Editorial> listarLibros() {

        List<Editorial> editoriales = new ArrayList();
        editoriales = editorialRepo.findAll();

        return editoriales;

    }

    public void modificaEditorial(String idEditorial, String nombre) throws MiException {
        
        validar(nombre);

        Optional<Editorial> respuesta = editorialRepo.findById(idEditorial);

        if (respuesta.isPresent()) {

            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);

            editorialRepo.save(editorial);
        }
    }

    private void validar(String nombre) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre de la Editorial no puede ser nulo ni estar vacio");
        }

    }

}
