package org.example.springaicustom.services;

import org.example.springaicustom.model.Answer;
import org.example.springaicustom.model.Question;

public interface OpenAIService {
    String getAnswer(String question);
    Answer getAnswer(Question question);
}
