package com.contactManagement.api;

import com.contactManagement.model.Contact;
import com.contactManagement.payload.ContactDTO;
import com.contactManagement.payload.ContactResponse;

public interface ApiService {
    ContactDTO addContact(Contact contact);

    ContactResponse getContacts();

    ContactDTO deleteContact(Long contactId);
}
