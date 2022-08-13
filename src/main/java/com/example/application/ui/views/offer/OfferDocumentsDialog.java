package com.example.application.ui.views.offer;

import com.example.application.persistence.entity.FileEntity;
import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.repository.FileRepository;
import com.example.application.ui.views.DocumentsDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class OfferDocumentsDialog extends DocumentsDialog<OfferEntity> {

    public OfferDocumentsDialog(FileRepository fileRepository) {
        super(fileRepository);
        dialog.add(createDialogLayout());
        dialog.add(uploadFile());
    }

    @Override
    protected VerticalLayout createDialogLayout() {
        H2 headline = new H2("Images");
        headline.setClassName("headline");
        VerticalLayout dialogLayout = new VerticalLayout();
        HorizontalLayout header = new HorizontalLayout(headline);
        dialogLayout.add(header);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        filesGrid.setColumns("name");
        filesGrid.addColumn(new ComponentRenderer<>(fileEntity -> {
            Anchor open = new Anchor(new StreamResource(fileEntity.getName(),
                    (InputStreamFactory) () -> new ByteArrayInputStream(fileEntity.getData())), "Open");
            open.getElement().addEventListener("click", event ->
                    UI.getCurrent().getPage().open(open.getHref(), "_blank")
            );
            open.addClassName("anchor-link");
            Anchor download = new Anchor(new StreamResource(fileEntity.getName(),
                    (InputStreamFactory) () -> new ByteArrayInputStream(fileEntity.getData())), "Download");
            download.getElement().setAttribute("download", true);
            download.addClassName("anchor-link");

            final Span deleteSpan = new Span("Delete");
            deleteSpan.addClickListener(e -> {
                getEntity().getOfferFiles().remove(fileEntity);
                fileRepository.delete(fileEntity);
                updateList();
            });
            return new HorizontalLayout(open, download, deleteSpan);
        })).setHeader("Actions");
        return dialogLayout;
    }

    @Override
    protected void configureGrid() {
        filesGrid.setColumns("name");
        filesGrid.addColumn(new ComponentRenderer<>(fileEntity -> {
            Anchor open = new Anchor(new StreamResource(fileEntity.getName(),
                    (InputStreamFactory) () -> new ByteArrayInputStream(fileEntity.getData())), "Open");
            open.getElement().addEventListener("click", event ->
                    UI.getCurrent().getPage().open(open.getHref(), "_blank")
            );
            open.addClassName("anchor-link");
            Anchor download = new Anchor(new StreamResource(fileEntity.getName(),
                    (InputStreamFactory) () -> new ByteArrayInputStream(fileEntity.getData())), "Download");
            download.getElement().setAttribute("download", true);
            download.addClassName("anchor-link");

            final Span deleteSpan = new Span("Delete");
            deleteSpan.addClickListener(e -> {
                getEntity().removeFileEntity(fileEntity);
                fileRepository.delete(fileEntity);
                updateList();
            });
            return new HorizontalLayout(open, download, deleteSpan);
        })).setHeader("Actions");
    }

    @Override
    protected Component uploadFile() {
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload upload = new Upload(memoryBuffer);
        upload.addSucceededListener(event -> {
            String attachmentName = event.getFileName();
            try {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setName(attachmentName);
                fileEntity.setData(IOUtils.toByteArray(memoryBuffer.getInputStream()));
                fileEntity.setType(memoryBuffer.getFileData().getMimeType());
                fileEntity.setOfferEntity(getEntity());
                fileRepository.saveAndFlush(fileEntity);
                updateList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return upload;
    }

    @Override
    protected void updateList() {
        filesGrid.setItems(fileRepository.findByOfferEntityId(getEntity().getId()));
    }
}
