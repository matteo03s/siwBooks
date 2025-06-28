package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Immagine;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.ImmagineRepository;
import it.uniroma3.siw.repository.LibroRepository;


@Service
public class ImmagineService {

    @Autowired
    private ImmagineRepository immagineRepository;
    @Autowired
    private LibroRepository libroRepository;

    @Transactional
    public Immagine saveImmagine(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Immagine immagine = new Immagine();
            immagine.setNomeFile(file.getOriginalFilename());
            immagine.setTipoContenuto(file.getContentType());
            immagine.setDati(file.getBytes());
            return immagineRepository.save(immagine);
        }
        return null;
    }

   
    @Transactional
    public Immagine saveImmagineAutore(MultipartFile file, Autore autore) throws IOException {
        if (file != null && !file.isEmpty()) {
            Immagine immagine = new Immagine();
            immagine.setNomeFile(file.getOriginalFilename());
            immagine.setTipoContenuto(file.getContentType());
            immagine.setDati(file.getBytes());
            
            autore.setImmagine(immagine);
            return immagineRepository.save(immagine);
        }
        return null;
    }
    

    public Immagine saveImmagineLibro(MultipartFile file, Libro libro) throws IOException {
        if (file != null && !file.isEmpty()) {
            Immagine immagine = new Immagine();
            immagine.setNomeFile(file.getOriginalFilename());
            immagine.setTipoContenuto(file.getContentType());
            immagine.setDati(file.getBytes());
            
            libro.getImmagini().add(immagine);
            
            libroRepository.save(libro);
//            return immagineRepository.save(immagine);
            return immagine;
        }
        return null;
    }

//    public Immagine getById(Long id) {
//        return immagineRepository.findById(id).orElse(null);
//    }
    @Transactional(readOnly = true)
    public Immagine getImmagineById(Long id) {
        return immagineRepository.findById(id).orElse(null);
    }

    public void removeImmagine(Long id) {
        immagineRepository.deleteById(id);
    }
    
    
}