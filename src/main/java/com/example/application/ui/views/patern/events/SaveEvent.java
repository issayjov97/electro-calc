package com.example.application.ui.views.patern.events;

import com.example.application.persistence.entity.PatternEntity;
import com.example.application.ui.views.patern.PatternForm;

public class SaveEvent extends PatternFormEvent {
    public SaveEvent(PatternForm source, PatternEntity item) {
        super(source, item);
    }
}
