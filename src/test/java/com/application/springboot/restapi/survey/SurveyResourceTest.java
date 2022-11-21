package com.application.springboot.restapi.survey;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = SurveyResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class SurveyResourceTest {

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private MockMvc mockMvc;

    private static String SPECIFIC_QUESTION_URL = "http://localhost:8080/surveys/Survey1/questions/Question1";
    private static String GENERIC_QUESTIONS_URL = "http://localhost:8080/surveys/Survey1/questions";
    private static String SPECIFIC_SURVEY_URI = "http://localhost:8080/surveys/Survey1";

    @Test
    void retrieveSpecificQuestionBySurveyIdAndQuestionId_404Scenario() throws Exception {

        //Preparing the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL)
                .accept(MediaType.APPLICATION_JSON);

        //Performing/Sending the request
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    void retrieveSpecificQuestionBySurveyIdAndQuestionId_basicScenario() throws Exception {

        //Preparing the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL)
                .accept(MediaType.APPLICATION_JSON);

        Question question = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");

        when(surveyService.retrieveSpecificQuestionBySurveyIdAndQuestionId("Survey1", "Question1"))
                .thenReturn(question);

        //Performing/Sending the request
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        String expectedResponse = """
                {
                    "id":"Question1",
                    "description":"Most Popular Cloud Platform Today",
                    "options":
                    [
                        "AWS",
                        "Azure",
                        "Google Cloud",
                        "Oracle Cloud"
                    ],
                    "correctAnswer":"AWS"
                }
                """;

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());
        JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), false);
    }

    @Test
    public void retrieveAllQuestionsBySurveyId_basicScenario() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GENERIC_QUESTIONS_URL)
                .accept(MediaType.APPLICATION_JSON);

        Question question1 = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
        Question question2 = new Question("Question2",
                "Fastest Growing Cloud Platform", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
        Question question3 = new Question("Question3",
                "Most Popular DevOps Tool", Arrays.asList(
                "Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

        List<Question> questions = new ArrayList<>(Arrays.asList(question1,
                question2, question3));

        when(surveyService.retrieveAllQuestionsBySurveyId("Survey1")).thenReturn(questions);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

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

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());
        JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), false);
    }

    @Test
    public void retrieveSurveyById_basicScenario() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(SPECIFIC_SURVEY_URI)
                .accept(MediaType.APPLICATION_JSON);

        Question question1 = new Question("Question1",
                "Most Popular Cloud Platform Today", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
        Question question2 = new Question("Question2",
                "Fastest Growing Cloud Platform", Arrays.asList(
                "AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
        Question question3 = new Question("Question3",
                "Most Popular DevOps Tool", Arrays.asList(
                "Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

        List<Question> questions = new ArrayList<>(Arrays.asList(question1,
                question2, question3));

        Survey survey = new Survey("Survey1", "My Favorite Survey",
                "Description of the Survey", questions);

        when(surveyService.retrieveSurveyById("Survey1")).thenReturn(survey);

        String expectedResponse = """
                {
                    "id": "Survey1"
                }
                """;

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());
        JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), false);
        System.out.println(response.getContentAsString());
    }

    @Test
    public void addNewSurveyQuestionBySurveyId_basicScenario() throws Exception {
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
        when(surveyService.addNewSurveyQuestionBySurveyId(anyString(), any())).thenReturn("SOME_ID");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(GENERIC_QUESTIONS_URL)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(201, response.getStatus());
        String locationHeader = response.getHeader("location");
        assertTrue(locationHeader.contains("surveys/Survey1/questions/SOME_ID"));
    }
}