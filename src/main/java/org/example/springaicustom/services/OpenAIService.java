package org.example.springaicustom.services;

import org.example.springaicustom.model.Answer;
import org.example.springaicustom.model.GetDeveloperRequest;
import org.example.springaicustom.model.GetResponse;
import org.example.springaicustom.model.Question;

public interface OpenAIService {
    GetResponse getJsonAnswer(GetDeveloperRequest topic);
    Answer getAnswer(Question question);
    Answer getDeveloperAnswer(GetDeveloperRequest topic);
    Answer getFormatedDeveloperAnswer(GetDeveloperRequest topic);
}
