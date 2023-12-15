/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.services;

import com.egg.biblioteca.entities.Autor;
import com.egg.biblioteca.exceptions.MiException;

import com.egg.biblioteca.repository.AutorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ronald
 */
@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepo;

    @Transactional
    public void crearAutor(String nombre) throws MiException {

        validar(nombre);

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autorRepo.save(autor);

    }

    public List<Autor> listarLibros() {

        List<Autor> autores = new ArrayList();
        autores = autorRepo.findAll();

        return autores;

    }
    
    public Autor getOne(String id){
        
       Autor a1 =  autorRepo.getOne(id);
       return a1;
    }

    public void modificarAutor(String idAutor, String nombre) throws MiException {

        validar(nombre);

        Optional<Autor> respuestaAutor = autorRepo.findById(idAutor);

        if (respuestaAutor.isPresent()) {

            Autor autor = respuestaAutor.get();
            autor.setNombre(nombre);

            autorRepo.save(autor);
        }

    }

    private void validar(String nombre) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre de Autor no puede ser nulo ni estar vacio");
        }

    }
}
