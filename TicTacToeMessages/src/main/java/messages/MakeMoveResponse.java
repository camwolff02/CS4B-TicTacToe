package messages;

import router.Message;

public class MakeMoveResponse extends Message {
    private String[][] cells;

    public MakeMoveResponse(String id, String[][] cells) {
      super(id);  
      this.cells = cells;
    }

    public String[][] getCells() {
        return cells;
    }
}