package com.contactManagement.api;

import com.contactManagement.jwt.JwtUtils;
import com.contactManagement.jwt.LoginRequest;
import com.contactManagement.jwt.LoginResponse;
import com.contactManagement.model.Contact;
import com.contactManagement.payload.ContactDTO;
import com.contactManagement.payload.ContactResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ApiService apiService;

    @PostMapping("/SignInToken")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        }catch(AuthenticationException e){
            Map<String,Object> map = new HashMap<>();
            map.put("message: ", "Bad Credential");
            map.put("status:", false);

            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());
        LoginResponse response = new LoginResponse(jwtToken,userDetails.getUsername(),roles);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/contacts")
    public ResponseEntity<ContactDTO> addContact(@RequestBody Contact contact){
        ContactDTO addContact = apiService.addContact(contact);
        return new ResponseEntity<>(addContact,HttpStatus.CREATED);
    }

    @GetMapping("/contacts")
    public ResponseEntity<ContactResponse> getContacts(){
        ContactResponse contactList = apiService.getContacts();
        return new ResponseEntity<>(contactList,HttpStatus.FOUND);
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<ContactDTO> deleteContact(@PathVariable Long contactId){
        ContactDTO deletedContact = apiService.deleteContact(contactId);
        return new ResponseEntity<>(deletedContact,HttpStatus.ACCEPTED);
    }

    @GetMapping("/contacts/public/info")
    public ResponseEntity<ContactResponse> publicInfo(){
        ContactResponse contactList = apiService.getContacts();
        return new ResponseEntity<>(contactList,HttpStatus.FOUND);
    }
}
