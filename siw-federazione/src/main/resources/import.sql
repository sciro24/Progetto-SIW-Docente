-- Inserimento Utente
insert into utente(id, cognome, data_nascita, email, nome)  values (nextval('utente_seq'), 'Scirocco','2005-08-26' , 'diego.scirocco@gmail.com', 'Diego');
insert into utente(id, cognome, data_nascita, email, nome)  values (nextval('utente_seq'), 'Marconi', '2007-07-26' , 'marco.marconi@gmail.com' , 'Marco');
insert into utente(id, cognome, data_nascita, email, nome)  values (nextval('utente_seq'), 'Andreoni','2006-06-26' , 'andriu@hotmail.com', 'Andrea');
insert into utente(id, cognome, data_nascita, email, nome)  values (nextval('utente_seq'), 'Scirocco','2001-05-26' , 'lorenzo.scirocco@gmail.com', 'Lorenzo');
insert into utente(id, cognome, data_nascita, email, nome)  values (nextval('utente_seq'), 'Bianchi','2000-04-26' , 'bianchi@gmail.com', 'Fede');
insert into utente(id, cognome, data_nascita, email, nome)  values (nextval('utente_seq'), 'Neri','2004-03-26' , 'neri@gmail.com', 'Fra');


-- Inserimento Credenziali
insert into credenziali(id, password, role, username, utente_id) values (nextval('credenziali_seq'), '$2a$10$p6sFaE0TOXvkmrOUXjdFXuiCq7HkALiEwi1KVhhmqyYZvefF3lE7K', 'AMMINISTRATORE', 'Diego', (select id from utente where email = 'diego.scirocco@gmail.com'));
insert into credenziali(id, password, role, username, utente_id) values (nextval('credenziali_seq'), '$2a$10$36fHPrcZ5kjTLgxtbB8FdeL5ETeDyCBDSvIKbI7n6AJbqs/c4flmS', 'PRESIDENTE', 'Marco', (select id from utente where email = 'marco.marconi@gmail.com'));
insert into credenziali(id, password, role, username, utente_id) values (nextval('credenziali_seq'), '$2a$10$S6HueP.58CzXZhQtat/sGOHgpoRSDpYQ835AA3Gq1Pu2dOijVLzCS', 'PRESIDENTE', 'Andrea', (select id from utente where email = 'andriu@hotmail.com')); 
insert into credenziali(id, password, role, username, utente_id) values (nextval('credenziali_seq'), '$2a$10$KTHFybov.hr94njClsS7qu/V21ngPGM9nCJlBbi6cOAOP7urmTQMe', 'PRESIDENTE', 'Lorenzo', (select id from utente where email = 'lorenzo.scirocco@gmail.com'));
insert into credenziali(id, password, role, username, utente_id) values (nextval('credenziali_seq'), '$2a$10$qE/4gbq.UiOrY8shvwk/VOO/3d3KJ6F7qmmyhVKJjZGjgk2mV5SFm', 'DEFAULT', 'Fede', (select id from utente where email = 'bianchi@gmail.com'));
insert into credenziali(id, password, role, username, utente_id) values (nextval('credenziali_seq'), '$2a$10$vInxSibcoAYUgjt6BFjluOLRCqL8/QJdwLpHQc6LA3xwQq8lprpCm', 'DEFAULT', 'Fra', (select id from utente where email = 'neri@gmail.com')); 


-- Inserimento Presidente
insert into presidente(id, codice_fiscale, luogo_nascita, utente_id) values (nextval('presidente_seq'), 'MRCH501 ' , 'Roma', (select id from utente where email = 'marco.marconi@gmail.com'));
insert into presidente(id, codice_fiscale, luogo_nascita, utente_id) values (nextval('presidente_seq'), 'NDRH501 ' , 'Roma', (select id from utente where email = 'andriu@hotmail.com'));
insert into presidente(id, codice_fiscale, luogo_nascita, utente_id) values (nextval('presidente_seq'), 'LRNZH501 ' , 'Roma', (select id from utente where email = 'lorenzo.scirocco@gmail.com'));


-- Inserimento Squadra
insert into squadra(id, anno_fondazione, indirizzo_sede, nome, presidente_id) values (nextval('squadra_seq'), '1927', 'Via Uffici del Vicario' , 'AS Roma' , 1);
insert into squadra(id, anno_fondazione, indirizzo_sede, nome, presidente_id) values (nextval('squadra_seq'), '1897', 'via della croce', 'Juventus', 2);
insert into squadra(id, anno_fondazione, indirizzo_sede, nome, presidente_id) values (nextval('squadra_seq'), '1908', 'viale della liberazione', 'Inter', NULL);
insert into squadra(id, anno_fondazione, indirizzo_sede, nome, presidente_id) values (nextval('squadra_seq'), '1900', 'corso galfer', 'AC Milan', NULL);
insert into squadra(id, anno_fondazione, indirizzo_sede, nome, presidente_id) values (nextval('squadra_seq'), '1928', 'piazza del duomo', 'Fiorentina', NULL);


-- Inserimento Giocatore
insert into giocatore(id, cognome, data_nascita, luogo_nascita, nome, inizio_tesseramento, fine_tesseramento, squadra_id, ruolo) values (nextval('giocatore_seq') , 'Totti', '2000-02-02', 'Roma', 'Francesco' ,'2023-01-01', '2024-12-31', 1, 'ATT' );
insert into giocatore(id, cognome, data_nascita, luogo_nascita, nome, inizio_tesseramento, fine_tesseramento, squadra_id, ruolo) values (nextval('giocatore_seq'), 'Ronaldo', '1985-02-05', 'Funchal', 'Cristiano', '2023-01-01', '2024-12-31', 1, 'ATT');
insert into giocatore(id, cognome, data_nascita, luogo_nascita, nome, inizio_tesseramento, fine_tesseramento, squadra_id, ruolo) values (nextval('giocatore_seq'), 'Dybala', '1993-11-15', 'Laguna Larga', 'Paulo', '2023-01-01', '2024-12-31', 1, 'ATT');
insert into giocatore(id, cognome, data_nascita, luogo_nascita, nome, inizio_tesseramento, fine_tesseramento, squadra_id, ruolo) values (nextval('giocatore_seq'), 'Barella', '1997-02-07', 'Cagliari', 'Nicolo', '2023-01-01', '2024-12-31', 2, 'CC');
insert into giocatore(id, cognome, data_nascita, luogo_nascita, nome, inizio_tesseramento, fine_tesseramento, squadra_id, ruolo) values (nextval('giocatore_seq'), 'Martinez', '1997-08-22', 'Bahía Blanca', 'Lautaro', '2023-01-01', '2024-12-31', 2, 'ATT');
insert into giocatore(id, cognome, data_nascita, luogo_nascita, nome, inizio_tesseramento, fine_tesseramento, squadra_id, ruolo) values (nextval('giocatore_seq'), 'Milenkovic', '1997-10-12', 'Belgrado', 'Nikola', NULL, NULL, NULL, 'DC');
insert into giocatore(id, cognome, data_nascita, luogo_nascita, nome, inizio_tesseramento, fine_tesseramento, squadra_id, ruolo) values (nextval('giocatore_seq'), 'Zaniolo', '1999-07-02', 'Rome', 'Nicolo', '2023-01-01', '2024-12-31', 3, 'CC');
insert into giocatore(id, cognome, data_nascita, luogo_nascita, nome, inizio_tesseramento, fine_tesseramento, squadra_id, ruolo) values (nextval('giocatore_seq'), 'Pedro', '1987-06-28', 'San Isidro', 'Pedro', '2023-03-01', '2024-12-31', 3, 'ATT');
insert into giocatore(id, cognome, data_nascita, luogo_nascita, nome, inizio_tesseramento, fine_tesseramento, squadra_id, ruolo) values (nextval('giocatore_seq'), 'Lukaku', '1993-06-13', 'Antwerp', 'Romelu', NULL, NULL, NULL, 'ATT');
insert into giocatore(id, cognome, data_nascita, luogo_nascita, nome, inizio_tesseramento, fine_tesseramento, squadra_id, ruolo) values (nextval('giocatore_seq'), 'Chiesa', '1997-10-25', 'Genova', 'Federico', '2023-01-01', '2024-12-31', 4, 'ATT');
insert into giocatore(id, cognome, data_nascita, luogo_nascita, nome, inizio_tesseramento, fine_tesseramento, squadra_id, ruolo) values (nextval('giocatore_seq'), 'Rabiot', '1995-04-03', 'Saint-Maurice', 'Adrien', '2023-01-01', '2024-12-31', 5, 'CC');
