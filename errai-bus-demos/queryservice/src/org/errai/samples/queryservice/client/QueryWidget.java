package org.errai.samples.queryservice.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import org.jboss.errai.bus.client.*;

public class QueryWidget extends Composite {
    @UiHandler("sendQuery")
    void doSubmit(ClickEvent event) {
        /**
         * Define a message to be sent.
         */
        CommandMessage msg = CommandMessage.create()
                .toSubject("QueryService")
                .set("QueryString", queryBox.getText());
     
        /**
         * Define a MessageCallback to handle the response.
         */
        MessageCallback responseHandler = new MessageCallback() {
            public void callback(CommandMessage message) {
                /**
                 * Extract the results String[] from the incoming message.
                 */
                String[] resultsString = message.get(String[].class, "QueryResponse");

                /**
                 * If there's no results, display a message.
                 */
                if (resultsString == null) {
                    resultsString = new String[]{"No results."};
                }

                /**
                 * Build an HTML unordered list based on the results.
                 */
                StringBuffer buf = new StringBuffer("<ul>");
                for (String result : resultsString) {
                    buf.append("<li>").append(result).append("</li>");
                }
                results.setHTML(buf.append("</ul>").toString());
            }
        };

        /**
         * Initiate the conversation.
         */
        bus.conversationWith(msg, responseHandler);
    }

    /**
     * Do boilerplate for UIBinder
     */
    @UiTemplate("QueryWidget.ui.xml")
    interface Binder extends UiBinder<Panel, QueryWidget> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    {
        initWidget(binder.createAndBindUi(this));
    }

    @UiField
    TextBox queryBox;

    @UiField
    HTML results;

    private MessageBus bus = ErraiBus.get();
}



