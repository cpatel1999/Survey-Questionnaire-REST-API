package com.application.springboot.restapi.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public Question retrieveSpecificQuestionBySurveyIdAndQuestionId(@PathVariable String surveyId,
                                                                    @PathVariable String questionId){
        Question question = surveyService.retrieveSpecificQuestionBySurveyIdAndQuestionId(surveyId, questionId);
        if(question == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return question;
    }

    @RequestMapping(value = "/surveys/{surveyId}/questions", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewSurveyQuestionBySurveyId(@PathVariable String surveyId,
                                                                 @RequestBody Question question) {
        String questionId = surveyService.addNewSurveyQuestionBySurveyId(surveyId, question);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().
                path("/{questionId}").buildAndExpand(questionId).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/surveys/{surveyId}/questions/{questionId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteSpecificQuestionBySurveyIdAndQuestionId(@PathVariable String surveyId,
                                                                                @PathVariable String questionId){
        surveyService.deleteSpecificQuestionBySurveyIdAndQuestionId(surveyId, questionId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/surveys/{surveyId}/questions/{questionId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateSpecificQuestionBySurveyIdAndQuestionId(@PathVariable String surveyId,
                                                                                @PathVariable String questionId,
                                                                                @RequestBody Question question){
        surveyService.updateSpecificQuestionBySurveyIdAndQuestionId(surveyId, questionId, question);
        return ResponseEntity.noContent().build();
    }
}