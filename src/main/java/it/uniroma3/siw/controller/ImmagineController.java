package it.uniroma3.siw.controller;


import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Immagine;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.ImmagineService;
import it.uniroma3.siw.service.LibroService;


@Controller
public class ImmagineController {

    @Autowired
    private ImmagineService immagineService;

    @Autowired
    private LibroService libroService;
    
    @Autowired
    private AutoreService autoreService;

    @GetMapping("/libro/{libroId}/uploadImmagine")
    public String showUploadForm(@PathVariable("libroId") Long libroId, Model model) {
        Libro libro = libroService.getLibroById(libroId);
        if (libro == null) {
            return "redirect:/libri";
        }
        model.addAttribute("libro", libro);
        return "uploadImmagine";
    }

    @PostMapping("/libro/{libroId}/uploadImmagine")
    public String uploadImmagine(@PathVariable("libroId") Long libroId,
                                 @RequestParam("file") MultipartFile file,
                                 Model model) {
        try {
            Libro libro = libroService.getLibroById(libroId);
            if (libro == null) {
                return "redirect:/libri";
            }
            immagineService.saveImmagine(file);
            return "redirect:/libro/" + libroId;
        } catch (IOException e) {
            model.addAttribute("error", "Errore nel caricamento dell'immagine.");
            model.addAttribute("libro", libroService.getLibroById(libroId));
            return "uploadImmagine";
        }
    }

    // Serve image data
    @GetMapping("/immagine/{id}")
    public ResponseEntity<byte[]> getImmagine(@PathVariable("id") Long id) {
        Immagine immagine = immagineService.getImmagineById(id);
        if (immagine == null || immagine.getDati() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(immagine.getTipoContenuto()))
                .body(immagine.getDati());
    }

    
/*****************************************************************************
******************************SEZIONE AUTORE**********************************
*****************************************************************************/

    @GetMapping("/admin/modificaImmagineAutore/{autoreId}")
    public String getCaricamentoImmagineAutore(@PathVariable("autoreId") Long autoreId, Model model) {

		Autore autore = autoreService.getAutoreById(autoreId);
        if (autore == null) {
            return "redirect:/admin/modificaAutori";
        }
        model.addAttribute("autore", autore);
        return "admin/autore/caricaImmagine.html";
    }

    @PostMapping("/admin/modificaImmagineAutore/{autoreId}")
    public String caricaImmagineAutore(@PathVariable("autoreId") Long autoreId,
                                 @RequestParam("file") MultipartFile file,
                                 Model model) {
        try {
           Autore autore = autoreService.getAutoreById(autoreId);
            if (autore == null) {
                return "redirect:/admin/modificaAutori";
            }
            immagineService.saveImmagineAutore(file, autore);
            return "redirect:/admin/modificaAutore/" + autoreId;
        } catch (IOException e) {
            model.addAttribute("error", "Errore nel caricamento dell'immagine.");
            model.addAttribute("autore", autoreService.getAutoreById(autoreId));
            return "admin/autore/caricaImmagine.html";
        }
    }
    
    @GetMapping("/admin/cancellaImmagineAutore/{autoreId}/{immagineId}")
    public String cancellaImmagineAutore (@PathVariable("autoreId") Long autoreId, @PathVariable("immagineId") Long immagineId, Model model) {
    	Autore autore = this.autoreService.getAutoreById(autoreId);
    	autore.setImmagine(null);
    	this.immagineService.removeImmagine(immagineId);
		return "redirect:/admin/modificaImmagineAutore/" + autoreId;
	}
    
    
/*****************************************************************************
******************************SEZIONE LIBRO***********************************
*****************************************************************************/    
    @Transactional
    @GetMapping("/admin/modificaImmagineLibro/{libroId}")
    public String getCaricamentoImmagineLibro(@PathVariable("libroId") Long libroId, Model model) {

		Libro libro = libroService.getLibroById(libroId);
        if (libro == null) {
            return "redirect:/admin/modificaLibri";
        }
        model.addAttribute("libro", libro);
        return "admin/libro/caricaImmagine.html";
    }

    @PostMapping("/admin/modificaImmagineLibro/{libroId}")
    public String caricaImmagineLibro(@PathVariable("libroId") Long libroId,
                                 @RequestParam("file") MultipartFile file,
                                 Model model) {
        try {
           Libro libro = libroService.getLibroById(libroId);
            if (libro == null) {
                return "redirect:/admin/modificaLibri";
            }
            immagineService.saveImmagineLibro(file, libro);
            return "redirect:/admin/modificaLibro/" + libroId;
        } catch (IOException e) {
            model.addAttribute("error", "Errore nel caricamento dell'immagine.");
            model.addAttribute("libro", libroService.getLibroById(libroId));
            return "admin/libro/caricaImmagine.html";
        }
    }
    
    @GetMapping("/admin/cancellaImmagineLibro/{libroId}/{immagineId}")
    public String cancellaImmagineLibro (@PathVariable("libroId") Long libroId, @PathVariable("immagineId") Long immagineId, Model model) {
    	Libro libro = this.libroService.getLibroById(libroId);
    	Immagine img = this.immagineService.getImmagineById(immagineId);
    	libro.getImmagini().remove(img);
    	this.libroService.saveLibro(libro);
    	this.immagineService.removeImmagine(immagineId);
		return "redirect:/admin/modificaImmagineLibro/" + libroId;
	}

}