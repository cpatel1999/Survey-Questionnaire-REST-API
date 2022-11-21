package com.application.springboot.restapi.survey;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Used to listen at random port.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {

    private static String SPECIFIC_QUESTION_URL = "/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTIONS_URL = "/surveys/Survey1/questions";

    //Authorization
    //Basic Yzpw

    //Used so that application directly talks with https://localhost:RANDOM_PORT
    @Autowired
    private TestRestTemplate template;


    String str = """
            {
                "id": "Question1",
                "description": "Most Popular Cloud Platform Today",
                "options": [
                    "AWS",
                    "Azure",
                    "Google Cloud",
                    "Oracle Cloud"
                ],
                "correctAnswer": "AWS"
            }
            """;

    @Test
    void retrieveSpecificQuestionBySurveyIdAndQuestionId_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = template.getForEntity(GENERIC_QUESTIONS_URL, String.class);
        String expectedResponse = """
                [
                     {
                         "id": "Question1"
                     },
                     {
                         "id": "Question2"
                     },
                     {
                         "id": "Question3"
                     }
                ]
                """;

        // Status of the response is 200.
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        // Content-Type:"application/json"
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    void retrieveAllQuestionsBySurveyId_basicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_QUESTION_URL, String.class);
        String expectedResponse = """
                {
                    "id":"Question1",
                    "description":"Most Popular Cloud Platform Today",
                    "correctAnswer":"AWS"
                }
                """;

        // Status of the response is 200.
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        // Content-Type:"application/json"
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }



    @Test
    public void addNewSurveyQuestionBySurveyId_basicScenario() {

        String requestBody = """
            {
                "description": "Your Favourite Languages",
                "options": [
                    "Java",
                    "Python",
                    "Javascript",
                    "Haskell"
                ],
                "correctAnswer": "Java"
            }
            """;

            //    http://localhost:8080/surveys/Survey1/questions
            //    POST
            //    201
            //    Location:	http://localhost:8080/surveys/Survey1/questions/1997597783

        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders ();

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = template.exchange(GENERIC_QUESTIONS_URL, HttpMethod.POST, httpEntity, String.class);

        // Status of the response is 200.
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        String locationHeader = responseEntity.getHeaders().get("location").get(0);
        assertTrue(locationHeader.contains("/surveys/Survey1/questions/"));

        //DELETE
        //location header

        template.delete(locationHeader);
    }

    private HttpHeaders createHttpContentTypeAndAuthorizationHeaders () {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Basic " + performBasicAuthEncoding("c", "p"));
        return headers;
    }

    public String performBasicAuthEncoding(String user, String password) {
        String combined = user + ":" + password;

        //Base64 Encoding => Bytes
        byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());

        //Bytes => String
        return new String(encodedBytes);

    }
}
