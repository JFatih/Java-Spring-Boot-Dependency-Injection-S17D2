package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.Experience;
import com.workintech.s17d2.tax.DeveloperTax;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.StandardSocketOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public Map<Integer, Developer> developers;

    private Taxable taxeble;

    @PostConstruct
    public void init(){
        developers = new HashMap<>();
    }

    @Autowired
    public DeveloperController(Taxable taxable){
        this.taxeble = taxable;
    }

    @GetMapping
    public List<Developer> getDevelopers(){
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer getDeveloper(@PathVariable Integer id){
        return developers.get(id);
    }

    @PostMapping
    public ResponseEntity<Developer> addDeveloper(@RequestBody Developer developer) {
        double taxRate = 0.0;
        if (developer.getExperience() == Experience.JUNIOR) {
            taxRate = taxeble.getSimpleTaxRate();
        } else if (developer.getExperience() == Experience.MID) {
            taxRate = taxeble.getMiddleTaxRate();
        } else if (developer.getExperience() == Experience.SENIOR) {
            taxRate = taxeble.getUpperTaxRate();
        }
        developer.setSalary(developer.getSalary() - (developer.getSalary() * taxRate / 100));
        developers.put(developer.getId(), developer);
        return ResponseEntity.status(201).body(developer);
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable Integer id ,@RequestBody Developer developer){
        Developer devOps = developers.get(id);
        devOps.setName(developer.getName());
        devOps.setId(developer.getId());
        devOps.setSalary(developer.getSalary());
        devOps.setExperience(developer.getExperience());
        return devOps;
    }

    @DeleteMapping("/{id}")
    public Developer deleteDeveloper(@PathVariable Integer id){
        Developer deletedDevops = developers.get(id);
        developers.remove(id);
        return deletedDevops;
    }
}
