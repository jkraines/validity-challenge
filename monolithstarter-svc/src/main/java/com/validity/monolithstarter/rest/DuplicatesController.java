package com.validity.monolithstarter.rest;

import com.validity.monolithstarter.service.DuplicatesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api")
public class DuplicatesController {

    @Inject
    private DuplicatesService duplicatesService;

    @GetMapping("/duplicates")
    public String getDuplicatesMessage() {
        return duplicatesService.getDuplicatesMessage();
    }
}