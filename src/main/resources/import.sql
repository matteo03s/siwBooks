-- Iniziamo popolando la tabella Ingrediente
--INSERT INTO ingrediente (nome, prezzo, url_image) VALUES ('Pomodoro', 0.50, 'images/pomodoro.png');
--INSERT INTO ingrediente (nome, prezzo, url_image) VALUES ('Mozzarella', 1.00, 'images/mozzarella.png');
--INSERT INTO ingrediente (nome, prezzo, url_image) VALUES ('Prosciutto', 1.50, 'images/prosciutto.png');
--INSERT INTO ingrediente (nome, prezzo, url_image) VALUES ('Funghi', 0.75, 'images/funghi.png');
--INSERT INTO ingrediente (nome, prezzo, url_image) VALUES ('Olive', 0.30, 'images/olive.png');
-- Successivamente, popoliamo la tabella Pizza
-- Assumiamo che i nuovi ID delle pizze siano generati automaticamente, quindi non li includiamo nella query
-- Per associare le pizze agli ingredienti, dovrai fare riferimento agli ID degli ingredienti (ad esempio, ingredienti_id)


--Inserisci l'admin username=gestore pw=pwadmin
INSERT INTO credenziali (username, password, ruolo) VALUES ('gestore', '$2a$10$JOX7nLjPEbvs/iUIp9D0b.S7MLOfKtsn1yzUr2ZsRgwN1d60v7SCa', 'ADMIN');
--Inserisci un utente base username=utente pw=utente
INSERT INTO credenziali (username, password, ruolo) VALUES ('utente', '$2a$10$/1qzoQAwjUf/GA2Tgwz/m.SuaueQODPzaSWjsfnX8O9Ty5KAZ.HLK', 'UTENTE');


-- Inserimento in Utente
INSERT INTO utente (nome, cognome, email, numero_telefonico) VALUES ('Mario', 'Rossi', 'mario.rossi@example.com', '1234567890');
INSERT INTO utente (nome, cognome, email, numero_telefonico) VALUES ('Anna', 'Verdi', 'anna.verdi@example.com', '0987654321');
INSERT INTO utente (nome, cognome, email, numero_telefonico) VALUES ('Giulia', 'Bianchi', 'giulia.bianchi@example.com', '1122334455');

-- Inserimento in Credenziali, con riferimento a Utente
--aaa
INSERT INTO credenziali (username, password, ruolo, utente_id) VALUES ('mario_rossi', '$2a$10$9z6bRpThZOwGn0s4ALmaLOo3HxnePIedMS1pmw1bcqe.Lf5ZNrhhy', 'UTENTE', 1);
--aaa
INSERT INTO credenziali (username, password, ruolo, utente_id) VALUES ('anna_verdi', '$2a$10$9z6bRpThZOwGn0s4ALmaLOo3HxnePIedMS1pmw1bcqe.Lf5ZNrhhy', 'UTENTE', 2);
INSERT INTO credenziali (username, password, ruolo, utente_id) VALUES ('admin_giulia', 'adminpass789', 'ADMIN', 3);

-- Inserimento in Autore
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazione) VALUES ('Alessandro', 'Manzoni', '1785-03-07', '1873-05-22', 'Italia');
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazione) VALUES ('Jane', 'Austen', '1775-12-16', '1817-07-18', 'Inghilterra');
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazione) VALUES ('Gabriel', 'García Márquez', '1927-03-06', '2014-04-17', 'Colombia');
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazione) VALUES ('Virginia', 'Woolf', '1882-01-25', '1941-03-28', 'Inghilterra');
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazione) VALUES ('Italo', 'Calvino', '1923-10-15', '1985-09-19', 'Italia');
INSERT INTO autore (nome, cognome, data_nascita, data_morte, nazione) VALUES ('Haruki', 'Murakami', '1949-01-12', NULL, 'Giappone');

-- Inserimento in Libro
INSERT INTO libro (titolo, anno, trama) VALUES ('I Promessi Sposi', 1840, 'La storia di Renzo e Lucia, ambientata in Lombardia durante il dominio spagnolo.');
INSERT INTO libro (titolo, anno, trama) VALUES ('Orgoglio e Pregiudizio', 1813, 'Una storia d''amore e di conflitti sociali tra Elizabeth Bennet e Mr. Darcy.');
INSERT INTO libro (titolo, anno, trama) VALUES ('Cent''anni di solitudine', 1967, 'La saga della famiglia Buendía in un villaggio immaginario, Macondo.');
INSERT INTO libro (titolo, anno, trama) VALUES ('Mrs Dalloway', 1925, 'Una giornata nella vita di Clarissa Dalloway, tra introspezione e vita londinese.');
INSERT INTO libro (titolo, anno, trama) VALUES ('Il barone rampante', 1957, 'La storia di Cosimo, che vive sugli alberi per ribellarsi alla società.');
INSERT INTO libro (titolo, anno, trama) VALUES ('Norwegian Wood', 1987, 'Un romanzo di formazione che esplora amore e perdita nella Tokyo degli anni 60');


-- Inserimento nella tabella di relazione many-to-many tra Libro e Autore (libro_autori)
INSERT INTO autore_libri (libri_id, autori_id) VALUES (1, 1); -- I Promessi Sposi di Alessandro Manzoni
INSERT INTO autore_libri (libri_id, autori_id) VALUES (2, 2); -- Orgoglio e Pregiudizio di Jane Austen
INSERT INTO autore_libri (libri_id, autori_id) VALUES (3, 3); -- Cent'anni di solitudine di Gabriel García Márquez
INSERT INTO autore_libri (libri_id, autori_id) VALUES (4, 4); -- Mrs Dalloway di Virginia Woolf
INSERT INTO autore_libri (libri_id, autori_id) VALUES (5, 5); -- Il barone rampante di Italo Calvino
INSERT INTO autore_libri (libri_id, autori_id) VALUES (6, 6); -- Norwegian Wood di Haruki Murakami
INSERT INTO autore_libri (libri_id, autori_id) VALUES (1, 5); -- I Promessi Sposi di Alessandro Manzoni e Italo Calvino (esempio ipotetico)

-- Inserimento in Immagine, con riferimento a Libro e Autore
INSERT INTO immagine (nome_file, tipo_contenuto, dati) VALUES ('copertina_promessi_sposi.jpg', 'image/jpeg'); -- Immagine per il libro
INSERT INTO immagine (nome_file, tipo_contenuto, dati) VALUES ('copertina_orgoglio_pregiudizio.jpg', 'image/jpeg');
INSERT INTO immagine (nome_file, tipo_contenuto, dati) VALUES ('ritratto_manzoni.jpg', 'image/jpeg', 0x00); -- Immagine per l'autore
INSERT INTO immagine (nome_file, tipo_contenuto, dati) VALUES ('ritratto_austen.jpg', 'image/jpeg', 0x00);

-- Inserimento in Recensione, con riferimento a Libro e Utente
INSERT INTO recensione (titolo, voto, testo, data, libro_id, utente_id) VALUES ('Capolavoro italiano', 5, 'Un romanzo storico avvincente e ben scritto.', '2025-06-01', 1, 1);
INSERT INTO recensione (titolo, voto, testo, data, libro_id, utente_id) VALUES ('Romantico e profondo', 4, 'Una storia d''amore che cattura.', '2025-06-02', 2, 2);
INSERT INTO recensione (titolo, voto, testo, data, libro_id, utente_id) VALUES ('Magico e complesso', 5, 'Un viaggio epico attraverso generazioni.', '2025-06-03', 3, 1);
INSERT INTO recensione (titolo, voto, testo, data, libro_id, utente_id) VALUES ('Un flusso di coscienza straordinario', 5, 'Un capolavoro modernista che cattura l''anima.', '2025-06-04', 4, 1);
INSERT INTO recensione (titolo, voto, testo, data, libro_id, utente_id) VALUES ('Avventura e fantasia', 4, 'Un racconto originale e pieno di significato.', '2025-06-05', 5, 2);
INSERT INTO recensione (titolo, voto, testo, data, libro_id, utente_id) VALUES ('Emozionante e nostalgico', 5, 'Un libro che tocca il cuore con la sua semplicità.', '2025-06-06', 6, 1);

--inserisci i libri
INSERT INTO libro (titolo, anno, trama) VALUES ('1984', 1949, 'Dolce al cucchiaio a base di panna, zucchero e vaniglia, servito con frutti di bosco.');
INSERT INTO libro (titolo, anno, trama) VALUES ('Frutta Fresca', 1990, 'Mix di frutta fresca di stagione, leggera e dissetante.');
INSERT INTO libro (titolo, anno, trama) VALUES ('Crostata di Marmellata', 2000, 'Pasta frolla ripiena di marmellata di frutta, croccante e gustosa.');

