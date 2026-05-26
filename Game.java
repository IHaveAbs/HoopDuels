public class Game {
    private boolean userTurn;
    private boolean animationRunning;
    private boolean isovertime;
    private boolean gameOver;
    private String currentMessage;

    public Game() {
        userTurn = true;
        animationRunning = false;
        isovertime = false;
        gameOver = false;
        currentMessage = "";
    }
    public boolean isUserTurn() {
        return userTurn;
    }
    public boolean isAnimationRunning(){
        return animationRunning;
    }
    public boolean isisovertime() {
        return isovertime;
    }
        public boolean isGameOver() {
        return gameOver;
    }

    public String getCurrentMessage() {
        return currentMessage;
    }

    public void setUserTurn(boolean u) {
        userTurn = u;
    }
    public void setAnimationRunning(boolean animationRunning) {
        this.animationRunning = animationRunning;
    }

    public void setisovertime(boolean isovertime) {
        this.isovertime = isovertime;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setCurrentMessage(String currentMessage) {
        this.currentMessage = currentMessage;
    }
}

    
    

