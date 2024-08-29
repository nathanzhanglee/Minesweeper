public class Space {

    private boolean containsBomb;
    private boolean flagged;
    private boolean revealed;
    private boolean isDetonator;

    public Space() {
        this.containsBomb = false;
        this.flagged = false;
        this.revealed = false;
        this.isDetonator = false;
    }

    public Space(boolean bool1, boolean bool2, boolean bool3, boolean bool4) {
        this.containsBomb = bool1;
        this.flagged = bool2;
        this.revealed = bool3;
        this.isDetonator = bool4;
    }

    public boolean getBombStatus() {
        return containsBomb;
    }

    public boolean getFlagStatus() {
        return flagged;
    }

    public boolean getRevealedStatus() {
        return revealed;
    }

    public boolean getDetonatorStatus() {
        return isDetonator;
    }

    public void placeBomb() {
        this.containsBomb = true;
    }

    public void flag() {
        this.flagged = !this.flagged;
    }

    public void reveal() {
        this.revealed = true;
    }

    public void setDetonator() {
        this.isDetonator = true;
    }

    public boolean saveGameHelper(boolean bool1, boolean bool2, boolean bool3, boolean bool4) {
        return this.containsBomb == bool1 && this.flagged == bool2 && this.revealed == bool3
                && this.isDetonator == bool4;
    }
}