package com.contactManagement.api;

import com.contactManagement.model.Contact;
import com.contactManagement.payload.ContactDTO;
import com.contactManagement.payload.ContactResponse;
import org.springframework.stereotype.Service;

@Service
public class ApiServiceImpl implements ApiService{
    @Override
    public ContactDTO addContact(Contact contact) {
        return null;
    }

    @Override
    public ContactResponse getContacts() {
        return null;
    }

    @Override
    public ContactDTO deleteContact(Long contactId) {
        return null;
    }
}
