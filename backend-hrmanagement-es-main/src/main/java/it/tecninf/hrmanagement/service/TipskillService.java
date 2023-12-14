package it.tecninf.hrmanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tecninf.hrmanagement.model.Competenze;
import it.tecninf.hrmanagement.model.Curriculum;
import it.tecninf.hrmanagement.model.Dipendente;
import it.tecninf.hrmanagement.model.Tipskill;
import it.tecninf.hrmanagement.repository.CompetenzeRepository;
import it.tecninf.hrmanagement.repository.CurriculumRepository;
import it.tecninf.hrmanagement.repository.DipendenteRepository;
import it.tecninf.hrmanagement.repository.TipskillRepository;

@Service
public class TipskillService {
	
	@Autowired
	private TipskillRepository tipskillRepository;
	@Autowired
	private CompetenzeRepository competenzeRepository;
	@Autowired
	private CurriculumRepository curriculumRepository;
	@Autowired
	private DipendenteRepository dipendenteRepository;
	@Autowired
	private DipendenteService dipendeteService;
	
	public List<Tipskill> listaSkill() {
		return (List<Tipskill>) tipskillRepository.listaSkill();
	}
	
	public void cancellaSkill(int id_tip_skill) {
		tipskillRepository.cancellaSkill(id_tip_skill);
	}
	
	public void aggiungiSkill(Tipskill tipskill) {
		tipskillRepository.save(tipskill);
	}
	
	
	
	
	//------------esecizio 3------------esecizio 5------------
	public String esercizio_3_addSkillsFromIDCV(int id_curriculum,Set<Tipskill> skills)
	{	
		boolean flag=false;
		for(Tipskill skill:skills)
		{
			if(skill!=null)
			{
				flag=true;
				break;
			}
		}
		if(!curriculumRepository.existsById(id_curriculum))
		{
			return "\nID missing\n";
		}
		if(!flag)
		{
	        return "\nSkills missing\n";
	    }
		Curriculum curriculum=curriculumRepository.findById(id_curriculum).get();
	    Dipendente dipendente=curriculum.getDipendente();//non mi serve controllare se è nullo perchè inserisco il cv in base ad un dipendente esistente
		for(Tipskill skill:skills)
	    {
			/*if(dipendente.getSkills().contains(skill)) //non entra mai qui dentro, perchè? perche il set<dipendente> dipednenti è diverso?
			{
				return "\nSkill already present\n";
			}*/
			for(Tipskill t:dipendente.getSkills())
			{
				//if(skill.equals(t)) //neanche questo funziona
				//if(skill.getIdTipskill()==(t.getIdTipskill())
				if(skill.getTipologiaSkill().equalsIgnoreCase(t.getTipologiaSkill()))
				{
					return "\nSkill already present\n";
				}
			}
			Competenze comodo = new Competenze();
	        comodo.setIdTipskill(skill.getIdTipskill());
	        comodo.setIdDipendente(dipendente.getIdDipendente());
	        comodo.setIdCurriculum(id_curriculum);
	        competenzeRepository.save(comodo);
	    }
	    return "\nSkills updated\n";
	}
	
	public void aggiungiSkillToCV(int idCurriculum,Set<Integer> tpskill) throws Exception {
	    Curriculum c = curriculumRepository.findById(idCurriculum).orElse(null);

	    if (c != null) {
	        Dipendente d = c.getDipendente();
	        Set<Tipskill> skill = d.getSkills();
	      
	      
	        
	        if (d != null) {
	            for (Integer s : tpskill) {
	                Tipskill t = tipskillRepository.findById(s).orElse(null);

	                if (t != null) {
	                    if (skill.contains(t)) {
	                        throw new IOException("Questa skill: " + t.getTipologiaSkill() + " è già presente");
	                    } else {
	                    	 List<Competenze> listcompente = (List<Competenze>) competenzeRepository.findAll();
	              	       int index= listcompente.get(listcompente.size()-1).getIdCompetenze();
	                        Competenze competenza = new Competenze();
	                        competenza.setIdCompetenze(index+1);
	                        competenza.setIdTipskill(t.getIdTipskill());
	                        competenza.setIdDipendente(d.getIdDipendente());
	                        competenza.setIdCurriculum(idCurriculum);
	                        System.out.println(competenza);
	                        competenzeRepository.save(competenza);
	                    }
	                } else {
	                    throw new IOException("TipSkill non trovato per l'id " + s);
	                }
	            }
	        } else {
	            throw new IOException("Nessun dipendente trovato per il curriculum con ID " + idCurriculum);
	        }
	    } else {
	        throw new IOException("Nessun curriculum trovato per l'ID " + idCurriculum);
	    }	
	}    
}
