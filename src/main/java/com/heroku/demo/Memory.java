package com.heroku.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String Price;




    public long getId() {
        return id;
    }	
    public void setId(long id) {
	   this.id = id;
   }
   
    public void setName(String name) {
	   this.name = name;
   }
    public String getName() {
        return name;
    }

   
    public void setPrice(String surname) { this.Price = surname; }
    public String getPrice() {
        return Price;
    }

 }
    
    
