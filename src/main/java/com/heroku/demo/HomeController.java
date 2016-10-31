package com.heroku.demo;

import javax.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@Controller
@RequestMapping("/")
public class HomeController {

    private MemoryRepository repository;

    @Autowired
    public HomeController(MemoryRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(ModelMap model) {
        List<Memory> memories = repository.findAll();
        model.addAttribute("memories", memories);
        model.addAttribute("insertMemory", new Memory());
        return "home";
    }

    @RequestMapping(value = "/memory/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Memory> getRecord(@PathVariable("id") long id) {
        Memory memory = repository.findOne(id);
        if (memory == null) {
            return new ResponseEntity<Memory>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Memory>(memory, HttpStatus.OK);
    }


    //---------------------Retrieve List of Adoptions---------------------------------------------------
    @RequestMapping(value = "/memories/",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Memory>> getOneRecord()
    {
        List<Memory> memory = repository.findAll();
        if(memory.isEmpty())
        {
            return new ResponseEntity<List<Memory>>(HttpStatus.NO_CONTENT);//OR HttpStatus.Not_Found
        }

        return new ResponseEntity<List<Memory>>(memory,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String insertData(ModelMap model, 
                             @ModelAttribute("insertRecordMemory") @Valid Memory memory,
                             BindingResult result) {
        if (!result.hasErrors()) {
            repository.save(memory);
        }
        return home(model);
    }
    
 @RequestMapping(value = "/memory/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createRecord(@RequestBody Memory memory, UriComponentsBuilder ucBuilder)
    {
        repository.save(memory);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/memory/{id}").buildAndExpand(memory.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    
    
    @RequestMapping(value = "/memory/delete/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Memory> deleteRecord(@PathVariable("id")long id)
    {
        System.out.println("Fetching & Deleting Adoption with id" + id);
        Memory memory = repository.findOne(id);
        if(memory == null)
        {
            System.out.println("Unable to delete. Adoption with id " + id + " not found");//comment
            return new ResponseEntity<Memory>(HttpStatus.NOT_FOUND);

        }

            repository.delete(memory);
            return new ResponseEntity<Memory>(HttpStatus.NO_CONTENT);
        }
      
}
       
