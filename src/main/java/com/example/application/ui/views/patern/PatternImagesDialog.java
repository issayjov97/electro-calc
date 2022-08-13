package com.example.application.ui.views.patern;

import com.example.application.persistence.entity.ImageEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.ImageRepository;
import com.example.application.service.PatternService;
import com.example.application.ui.views.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import javassist.bytecode.ByteArray;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class PatternImagesDialog extends Div {
    private final MultiSelectListBox<ImageEntity> listBox = new MultiSelectListBox<>();
    private final Dialog                          dialog;
    private       PatternEntity                   patternEntity;
    private final ImageRepository imageRepository;
    private final PatternService  patternService;

    public PatternImagesDialog(ImageRepository imageRepository, PatternService patternService) {
        this.imageRepository = imageRepository;
        this.patternService = patternService;
        dialog = new Dialog();
        dialog.add(createDialogLayout());
        dialog.setResizable(true);
        dialog.setDraggable(true);
        dialog.add(listBox);
        dialog.add(uploadImageToFile());
    }

    private VerticalLayout createDialogLayout() {
        H2 headline = new H2("Images");
        headline.setClassName("headline");
        VerticalLayout dialogLayout = new VerticalLayout();
        HorizontalLayout header = new HorizontalLayout(headline);
        dialogLayout.add(header);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        listBox.setRenderer(new ComponentRenderer<>(imageEntity -> {
            HorizontalLayout row = new HorizontalLayout();
            row.setWidthFull();
            row.setAlignItems(FlexComponent.Alignment.CENTER);

            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                imageRepository.delete(imageEntity);
                updateList();
            });

            Image imageFromStream = new Image(new StreamResource(imageEntity.getName(),
                    (InputStreamFactory) () -> new ByteArrayInputStream(imageEntity.getData())), "sdfds");
            imageFromStream.setWidth("320px");
            imageFromStream.setHeight("240px");

            Span name = new Span(imageEntity.getName());
            Span profession = new Span(imageEntity.getDescription());
            profession.getStyle()
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "var(--lumo-font-size-s)");

            VerticalLayout column = new VerticalLayout(name, profession, deleteButton);
            column.setPadding(false);
            column.setSpacing(false);

            row.add(imageFromStream, column);
            row.getStyle().set("line-height", "var(--lumo-line-height-m)");
            return row;
        }));

        return dialogLayout;
    }

    public Component uploadImageToFile() {
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload upload = new Upload(memoryBuffer);
        add(upload);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.addSucceededListener(event -> {
            String attachmentName = event.getFileName();
            try {
                final ImageEntity imageEntity = new ImageEntity();
                imageEntity.setName(attachmentName);
                imageEntity.setData(memoryBuffer.getInputStream().readAllBytes());
                imageEntity.getImagePatterns().add(patternEntity);
                imageRepository.saveAndFlush(imageEntity);
                updateList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return upload;
    }

    public void open() {
        dialog.open();
    }

    private void updateList() {
        this.patternEntity = patternService.getPattern(patternEntity.getName());
        listBox.setItems((imageRepository.find(List.of(patternEntity.getId()))));
        listBox.getDataProvider().refreshAll();

    }

    public void close() {
        dialog.close();
    }

    public Dialog getDialog() {
        return dialog;
    }

    public PatternEntity getPatternEntity() {
        return patternEntity;
    }

    public void setPatternEntity(PatternEntity patternEntity) {
        this.patternEntity = patternEntity;
        updateList();
    }
}
