package de.hsos.gateway;

import de.hsos.control.KundenVerwalter;
import de.hsos.entity.Adresse;
import de.hsos.entity.Kunde;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class KundenRepository implements KundenVerwalter {

    private static final Logger LOG = Logger.getLogger(KundenRepository.class);

    private final String instanzId = UUID.randomUUID().toString();
    @PostConstruct
    void init() {
        System.out.println("🛠 KundenRepository initialisiert mit ID: " + instanzId);
    }

    @Inject
    EntityManager em;

    @Override
    @Transactional
    public long neuenKundenAnlegen(String vorname, String nachname, String strasse, String plz, String ort, Integer hausnummer) {
        Adresse adresse = null;
        if (strasse != null && plz != null && ort != null && hausnummer != null) {
            adresse = new Adresse(strasse, plz, ort, hausnummer);
        }
        Kunde kunde = new Kunde(vorname, nachname, adresse);
        em.persist(kunde);
        return kunde.getId();
    }


    @Override
    @Transactional
    public boolean kundeEntfernen(String id) {
        try {
            long kundenId = Long.parseLong(id);
            Kunde kunde = em.find(Kunde.class, kundenId);
            if (kunde != null) {
                em.remove(kunde);
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    @Override
    public Optional<Kunde> findeKundeMitId(long id) {
        return Optional.ofNullable(em.find(Kunde.class, id));
    }

    @Override
    public Collection<Kunde> findeAlleKunden() {
        return em.createQuery("SELECT k FROM Kunde k", Kunde.class).getResultList();
    }

    @Override
    @Transactional
    public boolean adresseSetzen(long kundenId, Adresse adresse) {
        Kunde kunde = em.find(Kunde.class, kundenId);
        if (kunde == null) return false;
        kunde.setAdresse(adresse);
        em.merge(kunde);
        return true;
    }

}
