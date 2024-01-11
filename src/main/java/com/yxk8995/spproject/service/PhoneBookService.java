package com.yxk8995.spproject.service;

import com.google.gson.JsonArray;
import com.yxk8995.spproject.dao.PhoneBookDao;
import com.yxk8995.spproject.model.PhoneBook;
import jakarta.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class PhoneBookService {
	@Autowired
    PhoneBookDao phoneBookDao;
    @Value("${baseUrl}")
	private String baseUrl;
    private Pattern phoneNumberPattern = null;
	private Pattern namePattern = null;
    private static Logger LOG = null;

    @PostConstruct
    public void initialize() {
        try {
            String phoneNumberRegex = "^((\\+[1-9]{1,2}\\s?)|(\\d{1,3}\\s?))?(\\d)?[\\s.-]?\\(?[1-9][0-9]?[0-9]\\)?[\\s.-]?\\d{3}[\\s.-]\\d{4}$"
                    + "|^\\d{5}[\\s.]\\d{5}$"
                    + "|^\\d{3}-\\d{4}$"
                    + "|^\\d{5}$"
                    + "|^\\+?\\d{2}\\s\\d{2}\\s\\d{2}\\s\\d{2}\\s?(\\d{2})?\\s?$";
            phoneNumberPattern = Pattern.compile(phoneNumberRegex);
            String nameRegex = "^(\\s?[a-zA-Z]+[-,']?\\s?[a-zA-Z].?){1,3}$";
            namePattern = Pattern.compile(nameRegex);

            LOG = Logger.getLogger(PhoneBookService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getResponse(String status, String heading, String message){
        Map<String, String> response = new HashMap<>();

        response.put("Status", status);
        response.put("Response", heading);
        response.put("Message", message);
        return response;
    }

    public ResponseEntity<?> addPhoneBookEntry(PhoneBook phoneBook) {
        if(phoneNumberPattern.matcher(phoneBook.getPhoneNumber()).matches() && namePattern.matcher(phoneBook.getName()).matches()) {
            try {
                JSONObject jsonObjectResponse = phoneBookDao.handleAddRequest("" + baseUrl + "/insert.php", phoneBook.getName(), phoneBook.getPhoneNumber());
                for (String key : jsonObjectResponse.keySet()) {
                    if (key.contains("Success")) {
                        LOG.info(String.format("%s, Add Phone Book Entry, name: %s, number: %s", jsonObjectResponse.getString(key), phoneBook.getName(), phoneBook.getPhoneNumber()));
                        return new ResponseEntity<>(getResponse(Integer.toString(HttpStatus.OK.value()), key, jsonObjectResponse.getString(key)), HttpStatus.OK);
                    } else if (key.contains("Error")) {
                        LOG.debug(String.format("%s, Add Phone Book Entry, name: %s, number: %s", jsonObjectResponse.getString(key), phoneBook.getName(), phoneBook.getPhoneNumber()));
                        return new ResponseEntity<>(getResponse(Integer.toString(HttpStatus.BAD_REQUEST.value()), key, jsonObjectResponse.getString(key)), HttpStatus.BAD_REQUEST);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                LOG.debug(String.format("%s, Add Phone Book Entry, name: %s, number: %s", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), phoneBook.getName(), phoneBook.getPhoneNumber()));
                return new ResponseEntity<>(getResponse(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "Exception", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            LOG.debug(String.format("Invalid Input, Add Phone Book Entry, name: %s, number: %s", phoneBook.getName(), phoneBook.getPhoneNumber()));
            return new ResponseEntity<>(getResponse(Integer.toString(HttpStatus.BAD_REQUEST.value()), "Error", "Invalid Input"), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public JsonArray viewPhoneBook() {
        try {
            JsonArray jsonArray = phoneBookDao.handleViewRequest("" + baseUrl + "/view.php");
            LOG.info("Phone Book listed successfully");
            return jsonArray;
        }catch (Exception e){
            e.printStackTrace();
            LOG.debug("Error while listing records");
        }
        return null;
    }

    public ResponseEntity<?> deletePhoneBookEntryByName(String name) {
        if(namePattern.matcher(name).matches()) {
            try {
                JSONObject jsonObjectResponse = phoneBookDao.handleDeleteByNameRequest(baseUrl + "/deleteByName.php", name);
                for (String key : jsonObjectResponse.keySet()) {
                    if (key.contains("Success")) {
                        LOG.info(String.format("%s, Deleting entry by name %s", jsonObjectResponse.getString(key), name));
                        return new ResponseEntity<>(getResponse(Integer.toString(HttpStatus.OK.value()), key, jsonObjectResponse.getString(key)), HttpStatus.OK);
                    } else if (key.contains("Error")) {
                        if(jsonObjectResponse.getString(key).equals("404")){
                            LOG.debug(String.format("%s, Deleting entry by name %s", HttpStatus.NOT_FOUND.getReasonPhrase(), name));
                            return new ResponseEntity<>(getResponse(jsonObjectResponse.getString(key), key, HttpStatus.NOT_FOUND.getReasonPhrase()), HttpStatus.NOT_FOUND);
                        }else{
                            LOG.debug(String.format("%s, Deleting entry by name %s", jsonObjectResponse.getString(key), name));
                            return new ResponseEntity<>(getResponse("400", key, jsonObjectResponse.getString(key)), HttpStatus.BAD_REQUEST);
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                LOG.debug(String.format("%s, Deleting entry by name %s", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), name));
                return new ResponseEntity<>(getResponse(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "Exception", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            LOG.debug(String.format("Invalid Input, Deleting entry by name %s", name));
            return new ResponseEntity<>(getResponse(Integer.toString(HttpStatus.BAD_REQUEST.value()), "Error", "Invalid Input"), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public ResponseEntity<?> deletePhoneBookEntryByNumber(String phoneNumber) {
        if(phoneNumberPattern.matcher(phoneNumber).matches()) {
            try {
                JSONObject jsonObjectResponse  = phoneBookDao.handleDeleteByNumberRequest(baseUrl + "/deleteByNumber.php", phoneNumber);
                for (String key : jsonObjectResponse.keySet()) {
                    if (key.contains("Success")) {
                        LOG.info(String.format("%s, Deleting entry by number %s", jsonObjectResponse.getString(key), phoneNumber));
                        return new ResponseEntity<>(getResponse(Integer.toString(HttpStatus.OK.value()), key, jsonObjectResponse.getString(key)), HttpStatus.OK);
                    } else if (key.contains("Error")) {
                        if(jsonObjectResponse.getString(key).equals("404")){
                            LOG.debug(String.format("%s, Deleting entry by number %s", HttpStatus.NOT_FOUND.getReasonPhrase(), phoneNumber));
                            return new ResponseEntity<>(getResponse(jsonObjectResponse.getString(key), key, HttpStatus.NOT_FOUND.getReasonPhrase()), HttpStatus.NOT_FOUND);
                        }else{
                            LOG.debug(String.format("%s, Deleting entry by number %s", jsonObjectResponse.getString(key), phoneNumber));
                            return new ResponseEntity<>(getResponse(Integer.toString(HttpStatus.BAD_REQUEST.value()), key, jsonObjectResponse.getString(key)), HttpStatus.BAD_REQUEST);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                LOG.debug(String.format("%s, Deleting entry by number %s", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), phoneNumber));
                return new ResponseEntity<>(getResponse(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), "Exception", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            LOG.debug(String.format("Invalid Input, Deleting entry by number %s", phoneNumber));
            return new ResponseEntity<>(getResponse(Integer.toString(HttpStatus.BAD_REQUEST.value()), "Error", "Invalid Input"), HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}