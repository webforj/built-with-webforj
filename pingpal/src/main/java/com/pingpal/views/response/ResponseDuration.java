package com.pingpal.views.response;

import java.time.Duration;

import com.pingpal.helpers.DurationFormatter;
import com.webforj.component.html.elements.Div;
import com.webforj.component.text.Label;

public class ResponseDuration extends Div {

    private Label time;

    public ResponseDuration() {
        addClassName("response-duration");

        time = new Label("-").addClassName("response-duration-label");
        add(time);
    }

    public void setData(Duration duration) {
        String prettyTime = DurationFormatter.format(duration);
        time.setText(prettyTime);
    }

    public void clear() {
        time.setText("-");
    }
    
}
