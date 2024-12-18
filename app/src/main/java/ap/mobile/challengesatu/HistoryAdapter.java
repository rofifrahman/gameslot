package ap.mobile.challengesatu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter {

  private Context context;
  private List<History> histories;
  private DatabaseReference appDb;

  public HistoryAdapter(Context context, List<History> histories) {
    this.context = context;
    this.histories = histories;
  }

  public void setAppDb(DatabaseReference appDb) {
    this.appDb = appDb;
  }

  public class VH extends RecyclerView.ViewHolder {
    private ImageView slot1;
    private ImageView slot2;
    private ImageView slot3;
    private TextView tvStatus;
    private History history;

    public VH(@NonNull View itemView) {
      super(itemView);
      this.slot1 = itemView.findViewById(R.id.ivSlotHis1);
      this.slot2 = itemView.findViewById(R.id.ivSlotHis2);
      this.slot3 = itemView.findViewById(R.id.ivSlotHis3);
      this.tvStatus = itemView.findViewById(R.id.tvStatus);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Toast.makeText(context, "Deleted: " + (history.getId()), Toast.LENGTH_SHORT).show();
          appDb.child(history.getId()).removeValue();
        }
      });
    }

    private void bind(History h) {
      this.slot1.setImageResource(HistoryAdapter.getDrawable(h.getSlot1()));
      this.slot2.setImageResource(HistoryAdapter.getDrawable(h.getSlot2()));
      this.slot3.setImageResource(HistoryAdapter.getDrawable(h.getSlot3()));
      if (h.isWinStatus()) {
        this.tvStatus.setText("Win");
      } else {
        this.tvStatus.setText("Lose");
      }
      this.history = h;
    }
  }

  public static int getDrawable(int slot) {
    switch (slot) {
      case 0:
        return R.drawable.slot_1;
      case 1:
        return R.drawable.slot_2;
      case 2:
        return R.drawable.slot_3;
      case 3:
        return R.drawable.slot_4;
      case 4:
        return R.drawable.slot_5;
      case 5:
        return R.drawable.slot_6;
      case 6:
        return R.drawable.slot_7;
      case 7:
        return R.drawable.slot_8;
      case 8:
        return R.drawable.slot_9;
    }
    return 0;
  }


  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
    VH vh = new VH(view);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    VH vh = (VH) holder;
    vh.bind(histories.get(position));
  }

  @Override
  public int getItemCount() {
    return histories.size();
  }
}
