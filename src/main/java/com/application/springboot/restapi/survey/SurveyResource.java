package com.application.springboot.restapi.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class SurveyResource {

    //@Autowired
    private SurveyService surveyService;

    public SurveyResource(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    // /surveys
    // /surveys/Survey1
    // These are the good URIs.

    @RequestMapping("/surveys")
    public List<Survey> retrieveAllSurveys() {
        return surveyService.retrieveAllSurveys();
    }

    @RequestMapping("/surveys/{surveyId}")
    public Survey retrieveSurveyById(@PathVariable String surveyId) {
        Survey survey = surveyService.retrieveSurveyById(surveyId);
        if(survey == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return survey;
    }
    @RequestMapping("/surveys/{surveyId}/questions")
    public List<Question> retrieveAllQuestionsBySurveyId(@PathVariable String surveyId){
        List<Question> questionList = surveyService.retrieveAllQuestionsBySurveyId(surveyId);
        if(questionList == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return questionList;
    }

    @RequestMapping("/surveys/{surveyId}/questions/{questionId}")
    public Question retrieveSpecificQuestionBySurveyIdAndQuestionId(@PathVariable String surveyId, @PathVariable String questionId){
        Question question = surveyService.retrieveSpecificQuestionBySurveyIdAndQuestionId(surveyId, questionId);
        if(question == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return question;
    }
}
