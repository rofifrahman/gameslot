package ap.mobile.challengesatu;

public class History {
  private String id;
  private int slot1;
  private int slot2;
  private int slot3;
  private boolean winStatus;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getSlot1() {
    return slot1;
  }

  public void setSlot1(int slot1) {
    this.slot1 = slot1;
  }

  public int getSlot2() {
    return slot2;
  }

  public void setSlot2(int slot2) {
    this.slot2 = slot2;
  }

  public int getSlot3() {
    return slot3;
  }

  public void setSlot3(int slot3) {
    this.slot3 = slot3;
  }

  public boolean isWinStatus() {
    return winStatus;
  }

  public void setWinStatus(boolean winStatus) {
    this.winStatus = winStatus;
  }
}
