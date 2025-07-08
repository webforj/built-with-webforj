package com.pingpal.views.layout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.pingpal.models.RequestModel;
import com.pingpal.services.RequestService;
import com.webforj.component.Composite;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.Icon;
import com.webforj.component.icons.IconButton;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.optiondialog.InputDialog;
import com.webforj.router.Router;
import com.webforj.router.history.Location;

public class RequestsManager extends Composite<FlexLayout> {

    private FlexLayout self = getBoundComponent();
    private FlexLayout requestsContainer;
    private HashMap<String, FlexLayout> requests = new HashMap<String, FlexLayout>();
    private FlexLayout selectedRequest;
    private InputDialog newDialog, editDialog;
    private RequestService requestService;

    public RequestsManager() {
        self.addClassName("requests-manager");
        self.setDirection(FlexDirection.COLUMN);
        self.setAlignment(FlexAlignment.START);

        if (requestService == null) requestService = new RequestService();

        H3 heading = new H3("Requests").addClassName("requests-manager-heading");
        self.add(heading);

        Icon icon = TablerIcon.create("plus");
        IconButton button = new IconButton(icon);
        button.addClassName("requests-manager-new-button");
        button.onClick(e -> {
            if (newDialog == null) {
                newDialog = new InputDialog("What is the name of your new request?", "New request", InputDialog.InputType.TEXT);
                newDialog.setFirstButtonText("Save");
                newDialog.setSecondButtonText("Cancel");
                newDialog.setFirstButtonTheme(ButtonTheme.PRIMARY);
            }

            String requestName = newDialog.show();
            if (requestName != null && !requestName.isEmpty()) {
                RequestModel model = RequestModel.create(requestName);
                model = requestService.add(model);
                buildRequest(model);
            }
        });
        self.add(button);

        requestsContainer = new FlexLayout();
        requestsContainer.setWidth("100%");
        requestsContainer.setHeight("100%");
        requestsContainer.setDirection(FlexDirection.COLUMN);
        requestsContainer.setSpacing("10px");
        self.add(requestsContainer);

        List<RequestModel> requests = requestService.get();
        if (requests != null) {
            for (RequestModel request : requests) {
                buildRequest(request);
            }
        }
    }

    private void buildRequest(RequestModel request) {
        FlexLayout requestContainer = new FlexLayout().addClassName("request-container");
        requestContainer.setUserData("UUID", request.getId());
        requestContainer.setJustifyContent(FlexJustifyContent.BETWEEN);
        requestContainer.setAlignment(FlexAlignment.CENTER);
        requestContainer.setSpacing("10px");

        requestsContainer.add(requestContainer);
        requests.put(request.getId(), requestContainer);

        Div left = new Div().setWidth("100%").addClassName("request-container-left");
        left.onClick(e -> {
            setActive(requestContainer);
            Router.getCurrent().navigate(new Location("/requests/" + request.getId()));
        });

        FlexLayout right = new FlexLayout().setSpacing("10px").addClassName("request-container-right");
        requestContainer.add(left, right);

        Paragraph title = new Paragraph(request.getName());
        left.add(title);

        Icon icon = TablerIcon.create("trash");
        IconButton removeButton = new IconButton(icon);
        removeButton.onClick(e -> {
            requests.remove(request.getId());
            requestContainer.destroy();
            requestService.delete(request.getId());
        });

        icon = TablerIcon.create("edit");
        IconButton editButton = new IconButton(icon);
        editButton.onClick(e -> {
            if (editDialog == null) {
                editDialog = new InputDialog("", "Change request name", InputDialog.InputType.TEXT);
                editDialog.setFirstButtonText("Save");
                editDialog.setSecondButtonText("Cancel");
                editDialog.setFirstButtonTheme(ButtonTheme.PRIMARY);
            }

            editDialog.setMessage("What is the new name for request '" + request.getName().trim() + "'?");

            String requestName = editDialog.show();
            if (requestName != null && !requestName.isEmpty()) {
                request.setName(requestName);
                title.setText(requestName);
                requestService.update(request);
            }
        });

        right.add(editButton, removeButton);
    }

    private void setActive(FlexLayout activeRequest) {
        selectedRequest = activeRequest;
        
        Iterator<FlexLayout> it = requests.values().iterator();
        while (it.hasNext()) {
            FlexLayout request = it.next();
            request.removeClassName("active");
        }

        selectedRequest.addClassName("active");
    }
    
}
