/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.services;

import com.egg.biblioteca.entities.Autor;
import com.egg.biblioteca.entities.Editorial;
import com.egg.biblioteca.entities.Libro;
import com.egg.biblioteca.exceptions.MiException;
import com.egg.biblioteca.repository.AutorRepository;
import com.egg.biblioteca.repository.EditorialRepository;
import com.egg.biblioteca.repository.LibroRepository;
import java.util.ArrayList;
import java.util.Date;
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

public class LibroService {

    @Autowired
    private LibroRepository libroRepo;
    @Autowired
    private EditorialRepository editorialRepo;
    @Autowired
    private AutorRepository autorRepo;

    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {

        validar(isbn, titulo, ejemplares, idAutor, idEditorial);

        Autor autor = autorRepo.findById(idAutor).get();
        Editorial editorial = editorialRepo.findById(idEditorial).get();

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);

        libro.setAutor(autor);
        libro.setEditorial(editorial);

        libro.setAlta(new Date());

        libroRepo.save(libro);

    }

    public List<Libro> listarLibros() {

        List<Libro> libros = new ArrayList();
        libros = libroRepo.findAll();

        return libros;

    }

      public Libro getOne(Long isbn){
        
       Libro l1 =  libroRepo.getOne(isbn);
       return l1;
    }
    
    public void modificarLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {

        validar(isbn, titulo, ejemplares, idAutor, idEditorial);

        Optional<Libro> respuestaLibro = libroRepo.findById(isbn);
        Optional<Autor> respuestaAutor = autorRepo.findById(idAutor);
        Optional<Editorial> respuestaEditorial = editorialRepo.findById(idEditorial);

        Autor autor = new Autor();
        Editorial editorial = new Editorial();

        if (respuestaAutor.isPresent()) {

            autor = respuestaAutor.get();
        }
        if (respuestaEditorial.isPresent()) {
            editorial = respuestaEditorial.get();
        }
        if (respuestaLibro.isPresent()) {
            Libro libro = respuestaLibro.get();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libro.setEjemplares(ejemplares);

            libroRepo.save(libro);
        }
    }

    
    private void validar(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {

        if (isbn == null) {
            throw new MiException("el isbn no puede ser nulo");
        }
        if (titulo.isEmpty() || titulo == null) {
            throw new MiException("el titulo no puede ser nulo ni estar vacio");
        }
        if (idAutor.isEmpty() || idAutor == null) {
            throw new MiException("el id del Autor no puede ser nulo ni estar vacio");
        }
        if (idEditorial.isEmpty() || idEditorial == null) {
            throw new MiException("el id de la Editorial no puede ser nulo ni estar vacio");
        }
        if (ejemplares == null) {
            throw new MiException("el numero de ejemplares no puede nulo");
        }

    }

}
