package com.library.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class MessagesController {
    @GetMapping("/messages")
    public ResponseEntity<List<String>> messages (){
        return ResponseEntity.ok(Arrays.asList("first","second"));
    }
    //    ResponseEntity.ok() este folosit pentru a construi un răspuns HTTP cu
    //    statusul 200 OK și corpul răspunsului conținând lista de mesaje.
    //Arrays.asList("first", "second") creează o listă cu două elemente: "first" și "second",
    // care vor fi trimise înapoi clientului.
}
