package org.vaadin.example;

import com.infraleap.animatecss.Animated;
import com.vaadin.flow.component.html.Span;

public class AnimatedSpan extends Span implements Animated {

    public AnimatedSpan(String text) {
        super(text);
    }
}
