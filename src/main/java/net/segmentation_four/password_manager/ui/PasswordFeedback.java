package net.segmentation_four.password_manager.ui;

/**
 * Interface for handling password feedback
 */
public interface PasswordFeedback {
    // Public methods

    /**
     * Updates something with a feedback
     * @param text The password feedback
     */
    void update(String text);
}