package com.yxk8995.spproject.controller;

import com.yxk8995.spproject.model.PhoneBook;
import com.yxk8995.spproject.service.PhoneBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/PhoneBook")
public class CallAPIController {
    @Autowired
    private PhoneBookService phoneBookService;

    @GetMapping("/list")
    public String getPhoneBookList() {
        return phoneBookService.viewPhoneBook().toString();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPhoneBookEntry(@RequestBody PhoneBook phoneBook) {
        return phoneBookService.addPhoneBookEntry(phoneBook);
    }

    @PutMapping("/deleteByName")
    public ResponseEntity<?> deletePhoneBookEntryByName(@RequestParam String name){
        return phoneBookService.deletePhoneBookEntryByName(name);
    }

    @PutMapping("/deleteByNumber")
    public ResponseEntity<?> deletePhoneBookEntryByNumber(@RequestParam String number){
        return phoneBookService.deletePhoneBookEntryByNumber(number);
    }
}
