package com.contactManagement.api;

import com.contactManagement.model.Contact;
import com.contactManagement.payload.ContactDTO;
import com.contactManagement.payload.ContactResponse;
import com.contactManagement.repository.ContactRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ContactDTO addContact(Contact contact) {
        Contact savedContact = contactRepository.findByContactName(contact.getContactName());
        if (savedContact != null)
            throw new RuntimeException("Contact Already present");
        Contact contactSaved = contactRepository.save(contact);
        return modelMapper.map(contactSaved,ContactDTO.class);
    }

    @Override
    public ContactResponse getContacts() {
        List<Contact> allPresent = contactRepository.findAll();
        if (allPresent.isEmpty())
            throw new RuntimeException("The list is blank");

        List<ContactDTO> contactDTOs = allPresent.stream()
                .map(contact -> modelMapper.map(contact, ContactDTO.class))
                .toList();

        ContactResponse contactResponse = new ContactResponse();
        contactResponse.setContactContent(contactDTOs);
        return contactResponse;
    }

    @Override
    public ContactDTO deleteContact(Long contactId) {
        Contact contactToDelete = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact not found to delete"));
        contactRepository.delete(contactToDelete);
        return modelMapper.map(contactToDelete,ContactDTO.class);
    }
}
