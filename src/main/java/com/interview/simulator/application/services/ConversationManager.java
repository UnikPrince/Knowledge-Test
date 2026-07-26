package com.interview.simulator.application.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Conversation manager for maintaining interview context and state.
 * Tracks conversation history and manages session state.
 */
@Service
@Slf4j
public class ConversationManager {

    private final Map<Long, ConversationContext> activeConversations = new HashMap<>();

    /**
     * Initialize a new conversation session.
     *
     * @param sessionId interview session ID
     */
    public void initializeSession(Long sessionId) {
        log.debug("Initializing conversation for session: {}", sessionId);
        ConversationContext context = new ConversationContext(sessionId);
        activeConversations.put(sessionId, context);
    }

    /**
     * Add message to conversation history.
     *
     * @param sessionId session ID
     * @param role role (user/assistant)
     * @param content message content
     */
    public void addMessage(Long sessionId, String role, String content) {
        ConversationContext context = activeConversations.get(sessionId);
        if (context != null) {
            context.addMessage(role, content);
        }
    }

    /**
     * Get conversation history for a session.
     *
     * @param sessionId session ID
     * @return list of messages
     */
    public List<Map<String, String>> getConversationHistory(Long sessionId) {
        ConversationContext context = activeConversations.get(sessionId);
        if (context != null) {
            return context.getMessages();
        }
        return new ArrayList<>();
    }

    /**
     * Close conversation session.
     *
     * @param sessionId session ID
     */
    public void closeSession(Long sessionId) {
        log.debug("Closing conversation for session: {}", sessionId);
        activeConversations.remove(sessionId);
    }

    /**
     * Inner class for conversation context.
     */
    @Slf4j
    private static class ConversationContext {
        private final Long sessionId;
        private final List<Map<String, String>> messages;

        public ConversationContext(Long sessionId) {
            this.sessionId = sessionId;
            this.messages = new ArrayList<>();
        }

        public void addMessage(String role, String content) {
            Map<String, String> message = new HashMap<>();
            message.put("role", role);
            message.put("content", content);
            messages.add(message);
        }

        public List<Map<String, String>> getMessages() {
            return new ArrayList<>(messages);
        }
    }
}
