/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca.services;

import com.egg.biblioteca.entities.Imagen;
import com.egg.biblioteca.entities.Usuario;
import com.egg.biblioteca.enumeraciones.Rol;
import com.egg.biblioteca.exceptions.MiException;
import com.egg.biblioteca.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;


/**
 *
 * @author ronald
 */

@Service
public class UsuarioService implements UserDetailsService{
    
    @Autowired
    private UsuarioRepository usuarioRepo;
    
    @Autowired
    private ImagenService imgServ;
    
    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String email, String password, String rePassword) throws MiException {
    
        validar(nombre, email, password, rePassword);
    
        Usuario user = new Usuario();
        
        user.setNombre(nombre);
        user.setEmail(email);
        user.setPassword( new BCryptPasswordEncoder().encode(password));
        
        user.setRol(Rol.USER);
        
        Imagen imagen = imgServ.guardar(archivo);
        
        user.setImg(imagen);
        
        
        usuarioRepo.save(user);
        
    }
    
    @Transactional
    public void actualizar(MultipartFile archivo, String idUsuario, String nombre, String email, String password, String rePassword) throws MiException {

        validar(nombre, email, password, rePassword);

        Optional<Usuario> respuesta = usuarioRepo.findById(idUsuario);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);

            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            usuario.setRol(Rol.USER);
           
            String idImagen = null;
            
            if(usuario.getImg() != null){
                
                idImagen = usuario.getImg().getId();
            }
            
            Imagen imagen = imgServ.actualizar(archivo, idImagen);
            
            usuario.setImg(imagen);
            
            usuarioRepo.save(usuario);
        }

    }
    
     public Usuario getOne(String id){
        
       Usuario user =  usuarioRepo.getOne(id);
       return user;
    }
    
    
  private void validar(String nombre, String email, String password, String rePassword) throws MiException{
        
        if (email.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo ni estar vacio");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo ni estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("el Password no puede ser nulo ni estar vacio");
        }else{
            if(!password.equals(rePassword)){
            throw new MiException("los Password no coinciden");
        }
        }
        
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       
        Usuario usuario = usuarioRepo.buscarPorEmail(email);
        
        if(usuario != null){
            List <GrantedAuthority> permisos = new ArrayList();
  
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+usuario.getRol().toString());
             permisos.add(p);
             
            ServletRequestAttributes attr= (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            
            HttpSession session = attr.getRequest().getSession(true);
             session.setAttribute("usuariosesion", usuario);
          return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        }else {
            return null;
        }
    }
  
  
    public List<Usuario> listarUsuarios(){
    List <Usuario> usuarios = new ArrayList();
    usuarios = usuarioRepo.findAll();
     return usuarios;
}
    
    public void cambiarRol(String id){
        
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        
        if (respuesta != null) {
            Usuario user = respuesta.get();
            if(user.getRol().equals(Rol.USER)){
                user.setRol(Rol.ADMIN);
            }else if(user.getRol().equals(Rol.ADMIN)){
                user.setRol(Rol.USER);
            }
            usuarioRepo.save(user);
        }
        
        
    }
}
