package com.application.springboot.restapi.survey;

import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {

    private static List<Survey> surveys = new ArrayList<>();

    static {
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

        surveys.add(survey);

    }

    public List<Survey> retrieveAllSurveys() {
        return surveys;
    }

    public Survey retrieveSurveyById(String id) {
        Optional<Survey> optionalSurvey = surveys.stream().filter(survey -> survey.getId().equalsIgnoreCase(id)).findFirst();
        if (optionalSurvey.isEmpty()) {
            return null;
        }
        return optionalSurvey.get();
    }

    public List<Question> retrieveAllQuestionsBySurveyId(String surveyId) {
        Survey survey = retrieveSurveyById(surveyId);
        if (survey == null) {
            return null;
        }
        return survey.getQuestions();
    }

    public Question retrieveSpecificQuestionBySurveyIdAndQuestionId(String surveyId, String questionId) {
        List<Question> surveyQuestions = retrieveAllQuestionsBySurveyId(surveyId);
        if (surveyQuestions == null) {
            return null;
        }
        Optional<Question> opetionalQuestion = surveyQuestions.stream().filter(question -> question.getId().equalsIgnoreCase(questionId)).findFirst();
        if (opetionalQuestion.isEmpty()) {
            return null;
        }
        return opetionalQuestion.get();
    }

    public String addNewSurveyQuestionBySurveyId(String surveyId, Question question) {
        List<Question> questions = retrieveAllQuestionsBySurveyId(surveyId);
        question.setId(generateRandomId());
        questions.add(question);
        return question.getId();
    }

    private static String generateRandomId() {
        SecureRandom secureRandom = new SecureRandom();
        String randomId = new BigInteger(32, secureRandom).toString();
        return randomId;
    }

    public String deleteSpecificQuestionBySurveyIdAndQuestionId(String surveyId, String questionId) {
        List<Question> surveyQuestions = retrieveAllQuestionsBySurveyId(surveyId);
        if (surveyQuestions == null) {
            return null;
        }
        boolean removed = surveyQuestions.removeIf(question -> question.getId().equalsIgnoreCase(questionId));
        if (!removed) {
            return null;
        }
        return questionId;
    }

    public void updateSpecificQuestionBySurveyIdAndQuestionId(String surveyId, String questionId, Question question) {
        List<Question> surveyQuestions = retrieveAllQuestionsBySurveyId(surveyId);
        surveyQuestions.removeIf(q -> q.getId().equalsIgnoreCase(questionId));
        surveyQuestions.add(question);
    }
}
