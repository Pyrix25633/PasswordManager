package net.segmentation_four.password_manager.ui;

/**
 * Interface that handles password feedback
 */
public interface PasswordFeedback {
    // Public methods

    /**
     * Updates something with a feedback
     * @param text The password feedback
     */
    void update(String text);
}