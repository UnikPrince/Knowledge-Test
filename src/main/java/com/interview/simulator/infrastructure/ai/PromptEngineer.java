package com.interview.simulator.infrastructure.ai;

import com.interview.simulator.domain.entities.DocumentChunk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Prompt engineering service.
 * Constructs AI prompts with RAG context for interview questions and evaluations.
 */
@Service
@Slf4j
public class PromptEngineer {

    @Autowired
    private RagRetriever ragRetriever;

    private static final String SYSTEM_PROMPT = """
You are an expert technical interviewer for software engineering positions.
Your role is to:
1. Ask one clear, focused question at a time
2. Evaluate student answers based on technical accuracy, clarity, and depth
3. Ask follow-up questions to probe deeper understanding
4. Adapt difficulty based on student responses
5. Maintain a professional yet friendly tone
6. Focus on understanding over memorization
""";

    /**
     * Generate initial interview question prompt.
     *
     * @param topic interview topic
     * @return prompt for generating initial question
     */
    public String generateInitialQuestionPrompt(String topic) {
        log.debug("Generating initial question prompt for topic: {}", topic);
        
        List<DocumentChunk> context = ragRetriever.retrieveContext(topic);
        String contextStr = ragRetriever.buildContextString(context);
        
        return String.format("""
%s

%s

Generate ONE clear, focused technical interview question about: %s

The question should:
- Test conceptual understanding
- Be appropriate for intermediate level
- Be answerable in 2-3 minutes
- Focus on practical application

Provide only the question, no explanations.
        """, SYSTEM_PROMPT, contextStr, topic);
    }

    /**
     * Generate follow-up question prompt based on student answer.
     *
     * @param previousQuestion the original question
     * @param studentAnswer student's answer
     * @return prompt for generating follow-up question
     */
    public String generateFollowUpPrompt(String previousQuestion, String studentAnswer) {
        log.debug("Generating follow-up prompt");
        
        return String.format("""
%s

Original Question: %s
Student Answer: %s

Generate ONE focused follow-up question that:
- Probes deeper into the student's understanding
- Addresses gaps or ambiguities in their answer
- Tests related concepts

Provide only the question, no explanations.
        """, SYSTEM_PROMPT, previousQuestion, studentAnswer);
    }

    /**
     * Generate evaluation prompt for student answer.
     *
     * @param question the interview question
     * @param studentAnswer student's answer
     * @return prompt for evaluating answer
     */
    public String generateEvaluationPrompt(String question, String studentAnswer) {
        log.debug("Generating evaluation prompt");
        
        List<DocumentChunk> context = ragRetriever.retrieveContext(question);
        String contextStr = ragRetriever.buildContextString(context);
        
        return String.format("""
%s

%s

Question: %s
Student Answer: %s

Provide a technical evaluation with:
1. Accuracy (0-100): How technically correct is the answer?
2. Clarity (0-100): How well is the answer explained?
3. Completeness (0-100): Does it cover all important aspects?
4. Feedback: Specific, actionable feedback for improvement

Format your response as:
Accuracy: [score]
Clarity: [score]
Completeness: [score]
Feedback: [detailed feedback]
        """, SYSTEM_PROMPT, contextStr, question, studentAnswer);
    }

    /**
     * Generate performance report prompt.
     *
     * @param interviewTranscript full interview transcript
     * @return prompt for generating performance report
     */
    public String generateReportPrompt(String interviewTranscript) {
        log.debug("Generating performance report prompt");
        
        return String.format("""
As an expert technical interviewer, analyze this interview transcript and provide:

1. Overall Assessment (0-100)
2. Technical Knowledge (0-100)
3. Communication Skills (0-100)
4. Problem-Solving Ability (0-100)
5. Key Strengths (2-3 points)
6. Areas for Improvement (2-3 points)
7. Recommended Study Topics
8. Interview Readiness Level (1-5 scale)

Interview Transcript:
%s

Provide detailed, actionable feedback.
        """, interviewTranscript);
    }
}
