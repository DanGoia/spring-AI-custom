package org.example.springaicustom.services;

import org.example.springaicustom.model.Answer;
import org.example.springaicustom.model.Question;

public interface OpenAIExpertService {
    Answer getAnswer(Question question);
}
