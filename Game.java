public class Game {
    private boolean userTurn;
    private boolean animationRunning;
    private boolean overtime;
    private boolean gameOver;
    private String currentMessage;

    public Game() {
        reset();
    }
    public boolean isUserTurn() {
        return userTurn;
    }
    public boolean isAnimationRunning(){
        return animationRunning;
    }
    public boolean isOverTime() {
        return overtime;
    }
        public boolean isGameOver() {
        return gameOver;
    }

    public String getCurrentMessage() {
        return currentMessage;
    }

    public void setUserTurn(boolean userTurn) {
        this.userTurn = userTurn;
    }

    public void setAnimationRunning(boolean animationRunning) {
        this.animationRunning = animationRunning;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setCurrentMessage(String currentMessage) {
        this.currentMessage = currentMessage;
    }

    public void reset() {
        userTurn = true;
        animationRunning = false;
        overtime = false;
        gameOver = false;
        currentMessage = "";
    }
}

    
    

