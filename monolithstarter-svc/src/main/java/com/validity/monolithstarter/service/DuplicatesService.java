package com.validity.monolithstarter.service;

import org.springframework.stereotype.Service;

@Service
public class DuplicatesService {
    public String getDuplicatesMessage() {
        return "Duplicates!";
    }
}