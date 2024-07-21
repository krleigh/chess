package service.requestresult;


import model.GameData;

public class GameListResult  {
    private GameData[] games;

    public GameListResult(GameData[] games) {
        this.games = games;
    }

    public GameData[] getGames() {
        return this.games;
    }
}
