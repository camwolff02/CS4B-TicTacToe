package messages;

import router.Message;

public class MakeMoveResponse extends Message {
    private int cells;

    public MakeMoveResponse(String id, int cells) {
      super(id);  
      this.cells = cells;
    }

    public int getCells() {
        return cells;
    }
}