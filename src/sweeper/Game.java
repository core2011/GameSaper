package sweeper;

public class Game {

    private Bomb bomb;
    private Flag flag;
    private GamaState state;

    public GamaState getState() {
        return state;
    }

    public Game(int cols, int rews, int bombs) {
        Ranges.setSize(new Coord(cols, rews));
        bomb = new Bomb(bombs);
        flag = new Flag();
    }

    public void start() {
        bomb.start();
        flag.start();
        state = GamaState.PLAYED;
    }

    public Box getBox(Coord coord) {
        if (flag.get(coord) == Box.OPENED) {
            return bomb.get(coord);
        } else {
            return flag.get(coord);
        }
    }

    public void pressLeftButton(Coord coord) {
        if (gameOver())return;
        openBox(coord);
        checkWinner();
    }

    private void checkWinner(){
        if (state==GamaState.PLAYED){
            if (flag.getCountOfClosedBoxes() == bomb.getTotalBombs()){
                state=GamaState.WINNER;
            }
        }
    }

    private void openBox(Coord coord){
        switch (flag.get(coord)){
            case OPENED:setOpenedToCloseBoxesAroundNumbers(coord);return;
            case FLAGED:return;
            case CLOSED:
                switch (bomb.get(coord)){
                    case ZERO:openBoxAround(coord);return;
                    case BOMB: openBombs(coord); return;
                    default: flag.setOpenedToBox(coord);return;
                }
        }
    }

    void setOpenedToCloseBoxesAroundNumbers (Coord coord){
        if (bomb.get(coord)!=Box.BOMB){
            if (flag.getCountOfFlagedBoxesAround(coord)== bomb.get(coord).getNumber()){
                for (Coord around : Ranges.getCoordsAround(coord)){
                    if (flag.get(around)==Box.CLOSED){
                        openBox(around);
                    }
                }
            }
        }
    }

    private void openBombs(Coord bombed) {
        state = GamaState.BOMBED;
        flag.setBombedToBox(bombed);
        for (Coord coord : Ranges.getAllCoords()){
            if (bomb.get(coord)==Box.BOMB){
                flag.setOpenedToClosedBombBox(coord);
            } else {
                flag.setNoBombToFlagSafeBox(coord);
            }
        }
    }

    private void openBoxAround(Coord coord) {
        flag.setOpenedToBox(coord);
        for (Coord around : Ranges.getCoordsAround(coord)){
            openBox(around);
        }
    }

    public void pressRightButton(Coord coord) {
        if (gameOver())return;
        flag.toggleFlageToBox(coord);
    }

    private boolean gameOver() {
        if (state==GamaState.PLAYED) {
            return false;
        }
        start();
        return true;

    }
}
