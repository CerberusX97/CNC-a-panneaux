/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.equipe58.domaine;

/**
 *
 * @author ADMO-PC
 */
import java.util.List;
import java.util.stream.Collectors;

public class CncDTO {
    private PanneauDTO panneau;
    private List<OutilDTO> outilListe;
    private OutilDTO outilActuel;

    public CncDTO(PanneauDTO panneau, List<OutilDTO> outilListe, OutilDTO outilActuel) {
        this.panneau = panneau;
        this.outilListe = outilListe;
        this.outilActuel = outilActuel;
    }

    public PanneauDTO getPanneau() {
        return panneau;
    }

    public List<OutilDTO> getOutilListe() {
        return outilListe;
    }

    public OutilDTO getOutilActuel() {
        return outilActuel;
    }
    
    public static CncDTO fromCnc(Cnc cnc) {
        PanneauDTO panneauDto = PanneauDTO.fromPanneau(cnc.getPanneau());
        List<OutilDTO> outilsDto = cnc.getOutils().stream()
            .map(OutilDTO::fromOutil)
            .collect(Collectors.toList());
        OutilDTO outilActuelDto = cnc.getOutilActuel() != null ? OutilDTO.fromOutil(cnc.getOutilActuel()) : null;

        return new CncDTO(panneauDto, outilsDto, outilActuelDto);
    }
}
