public class GameState {
    private boolean userTurn;
    private boolean animationRunning;
    private boolean overtime;
    private boolean gameOver;
    private String currentMessage;

    public GameState() {
        userTurn = true;
        animationRunning = false;
        overtime = false;
        gameOver = false;
        currentMessage = "";
    }

    public boolean isUserTurn() {
        return userTurn;
    }
    public boolean isAnimationRunning() {
        return animationRunning;
    }
    public boolean isOvertime() {
        return overtime;
    }
    public boolean isGameOver() {
        return gameOver;
    }
    public String getCurrentMessage() {
        return currentMessage;
    }
    public void setUserTurn(boolean b){
        userTurn = b;
    }
    public void setAnimationRunning(boolean b) {
        animationRunning = b;
    }
    public void setOvertime(boolean b){
        overtime = b;
    }
    public void setGameOver(boolean b){
        gameOver = b;
    }
    public void setCurrentMessage(String msg){
        currentMessage = msg;
    }
    public void reset() {
        userTurn = true;
        animationRunning = false;
        overtime = false;
        gameOver = false;
        currentMessage = "";
    }
}

