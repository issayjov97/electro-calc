package com.example.application.ui.views.patern.events;

import com.example.application.persistence.entity.PatternEntity;
import com.example.application.ui.views.patern.PatternForm;

public class DeleteEvent extends PatternFormEvent {
        public DeleteEvent(PatternForm source, PatternEntity item) {
            super(source, item);
        }
    }