package phonebooks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phonebooks.entities.PhoneBookEntry;
import phonebooks.services.PhoneBookEntryService;

import java.net.URI;
import java.util.List;

@RestController
public class PhoneBookEntryController {
    private PhoneBookEntryService entryService;

    private final String PHONE_BOOK_ENTRY_URI = "/phone-book-entry/";

    @Autowired
    public PhoneBookEntryController(PhoneBookEntryService entryService) {
        this.entryService = entryService;
    }

    @PostMapping("/user/{id}/contacts")
    public ResponseEntity addEntryForUser(
            @PathVariable("id") Long id,
            @RequestBody PhoneBookEntry entry
    ) {
        entry = entryService.create(id, entry);
        if (entry != null) {
            return ResponseEntity.created(URI.create(PHONE_BOOK_ENTRY_URI + entry.getId())).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(PHONE_BOOK_ENTRY_URI + "/{id}")
    public ResponseEntity<PhoneBookEntry> getEntryById(@PathVariable("id") Long id) {
        PhoneBookEntry entry = entryService.getById(id);
        if (entry != null) {
            return ResponseEntity.ok(entry);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(PHONE_BOOK_ENTRY_URI)
    public List<PhoneBookEntry> getAllEntries() {
        return entryService.getAll();
    }

    @PutMapping(PHONE_BOOK_ENTRY_URI + "/{id}")
    public ResponseEntity deleteEntry(
            @PathVariable("id") Long id,
            @RequestBody PhoneBookEntry entry
    ) {
        entry = entryService.update(id, entry);
        if (entry != null) return ResponseEntity.noContent().build();
        else return ResponseEntity.notFound().build();
    }

    @DeleteMapping(PHONE_BOOK_ENTRY_URI + "/{id}")
    public ResponseEntity deleteEntry(@PathVariable("id") Long id) {
        if (entryService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
