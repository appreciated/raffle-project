package org.vaadin.example;

import com.infraleap.animatecss.Animated;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import elemental.json.impl.JreJsonObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route("")
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@Push
@JavaScript("https://cdn.jsdelivr.net/npm/party-js@1.0.4/party.min.js")
public class RaffleView extends HorizontalLayout {
    private final Button start;
    private final RaffleService service;
    private final Button stop;
    VerticalLayout winners = new VerticalLayout();
    List<String> currentWinners = new ArrayList<>();
    FlexLayout participants = new FlexLayout();
    HashMap<String, AnimatedSpan> components = new HashMap<>();

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public RaffleView(@Autowired RaffleService service) {
        this.service = service;
        setSizeFull();
        getStyle()
                .set("padding", "10px");
        participants.getStyle()
                .set("padding", "10px")
                .set("overflow", "auto");
        participants.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        add(winners);
        add(participants);
        service.getParticipants().forEach(s -> {
            AnimatedSpan span = new AnimatedSpan(s);
            span.setHeight("30px");
            span.getStyle()
                    .set("padding", "10px")
                    .set("border-radius", "10px")
                    .set("background", "white")
                    .set("font-size", "16px")
                    .set("box-shadow", "0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24)")
                    .set("margin-right", "5px")
                    .set("line-height", "30px");
            participants.add(span);
            components.put(s, span);
        });
        IntStream.rangeClosed(1, service.getNumberOfWinners()).forEach(value -> {
            AnimatedSpan span = new AnimatedSpan(value + ". ");
            span.setHeight("56px");
            span.getStyle()
                    .set("line-height", "56px")
                    .set("margin-top", "0px");
            winners.add(span);
        });
        start = new Button("Start");
        stop = new Button("Stop");
        winners.add(start);
        winners.add(stop);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI ui = attachEvent.getUI();
        start.addClickListener(buttonClickEvent -> new Thread(() -> {
            currentWinners.clear();
            List<String> drawnWinners = service.getWinners().collect(Collectors.toList());
            for (String winner : drawnWinners) {
                ui.access(() -> components.entrySet()
                        .stream()
                        .filter(entry -> !currentWinners.contains(entry.getKey()))
                        .map(Map.Entry::getValue)
                        .forEach(entry -> {
                            entry.animate(Animated.Animation.FADE_IN);
                            entry.getStyle()
                                    .set("position", "relative")
                                    .set("background", "white")
                                    .set("font-size", "16px")
                                    .set("transition", "none")
                                    .set("left", "unset")
                                    .set("top", "unset");
                        }));
                raffleUser(winner, ui);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start());
        stop.addClickListener(buttonClickEvent -> new Thread(() -> ui.access(() ->
                components.values()
                        .forEach(entry -> {
                            entry.animate(Animated.Animation.FADE_IN);
                            entry.getStyle()
                                    .set("position", "relative")
                                    .set("background", "white")
                                    .set("transition", "none")
                                    .set("font-size", "16px")
                                    .set("left", "unset")
                                    .set("top", "unset");
                        })
        )).start());
    }

    private void raffleUser(String currentWinner, UI ui) {

        currentWinners.add(currentWinner);
        List<Map.Entry<String, AnimatedSpan>> loosers = components.entrySet()
                .stream()
                .filter(entry -> !currentWinners.contains(entry.getKey()))
                .collect(Collectors.toList());

        for (Map.Entry<String, AnimatedSpan> looser : loosers) {
            ui.access(() -> looser.getValue().animate(Animated.Animation.FADE_OUT));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ui.access(() -> {
            AnimatedSpan winnerComponent = components.get(currentWinner);
            PendingJavaScriptResult result = winnerComponent.getElement().executeJs("return this.getBoundingClientRect()");
            result.then(jsonValue -> {
                JreJsonObject object = (JreJsonObject) jsonValue;
                ui.access(() -> {
                    winnerComponent.getStyle()
                            .set("position", "absolute")
                            .set("background", "#ffea98")
                            .set("font-size", "16px")
                            .set("transition", "all 1s")
                            .set("left", object.getNumber("x") + "px")
                            .set("top", object.getNumber("y") + "px");
                    ui.getPage().executeJs("party.element($0)",winnerComponent.getElement());
                });
                int top = (currentWinners.size() - 1) * 56 + 12;

                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ui.access(() -> {
                        winnerComponent.getStyle()
                                .set("left", "45px")
                                .set("font-size", "20px")
                                .set("top", top + "px");

                    });

                }).start();
            });
        });
    }
}
