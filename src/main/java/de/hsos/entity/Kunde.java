package de.hsos.entity;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Vetoed;
import jakarta.persistence.*;

@Entity
@Vetoed
public class Kunde {

    @PostConstruct
    void init() {
        System.out.println("🧩 Neue Dependent-Instanz Kunde erzeugt: " + id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vorname;
    private String nachname;

    @OneToOne(cascade = CascadeType.ALL)
    private Adresse adresse;

    public Kunde() {
    }

    public Kunde(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public Kunde(String vorname, String nachname, Adresse adresse) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.adresse = adresse;
    }

    public Long getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Adresse getAdresse() {
        return adresse;
    }
}
