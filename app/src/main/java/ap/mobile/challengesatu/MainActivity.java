package ap.mobile.challengesatu;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  public static final String FirebaseURL = "https://pamd-3b7db-default-rtdb.asia-southeast1.firebasedatabase.app/";
  private ImageView ivSlot1;
  private ImageView ivSlot2;
  private ImageView ivSlot3;
  private Button btPull;
  private HistoryAdapter historyAdapter;
  private RecyclerView rvHistory;
  private Handler[] handler = new Handler[3];
  private Thread[] acakThread = new Thread[3];
  private int[] imageResource = {
          R.drawable.slot_1, R.drawable.slot_2, R.drawable.slot_3,
          R.drawable.slot_4, R.drawable.slot_5, R.drawable.slot_6,
          R.drawable.slot_7, R.drawable.slot_8, R.drawable.slot_9,
  };
  private int indexGambar1, indexGambar2, indexGambar3;
  private int loopButton = 0;
  private MediaPlayer jackpotSound;
  private MediaPlayer failSound;
  private List<History> dataset = new ArrayList<>();
  private FirebaseDatabase db;
  private DatabaseReference appDB;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    this.ivSlot1 = this.findViewById(R.id.ivSlot1);
    this.ivSlot2 = this.findViewById(R.id.ivSlot2);
    this.ivSlot3 = this.findViewById(R.id.ivSlot3);
    this.btPull = this.findViewById(R.id.btPull);
    this.btPull.setOnClickListener(this);
    this.jackpotSound = MediaPlayer.create(this, R.raw.jackpot);
    this.failSound = MediaPlayer.create(this, R.raw.spongebob_fail);

    buatHandler(0);
    buatHandler(1);
    buatHandler(2);

    this.historyAdapter = new HistoryAdapter(this, dataset);
    this.rvHistory = this.findViewById(R.id.rvHistory);
    this.rvHistory.setLayoutManager(new LinearLayoutManager(this));
    this.rvHistory.setAdapter(this.historyAdapter);

    this.db = FirebaseDatabase.getInstance(FirebaseURL);
    this.appDB = this.db.getReference("");
    this.historyAdapter.setAppDb(this.appDB);

    this.appDB.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        dataset.clear();
        for (DataSnapshot s : snapshot.getChildren()){
          History history = s.getValue(History.class);
          dataset.add(history);
        }
        historyAdapter.notifyDataSetChanged();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.btPull) {
      loopButton++;
      switch (loopButton % 4) {
        case 1:
          if (acakThread[0] == null && acakThread[1] == null && acakThread[2] == null){
            buatThread(0, 500);
            buatThread(1, 1000);
            buatThread(2, 1500);
          }
          else if (!acakThread[0].isAlive() && !acakThread[1].isAlive() && !acakThread[2].isAlive()){
            buatThread(0,500);
            buatThread(1, 1000);
            buatThread(2, 1500);
          }
          acakThread[0].start();
          acakThread[1].start();
          acakThread[2].start();
          break;
        case 2:
          if (acakThread[0].isAlive() && acakThread[1].isAlive() && acakThread[2].isAlive()) {
            acakThread[0].interrupt();
          }
          break;
        case 3:
          if (acakThread[1].isAlive() && acakThread[2].isAlive()) {
            acakThread[1].interrupt();
          }
          break;
        case 0:
          if (acakThread[2].isAlive()) {
            acakThread[2].interrupt();
          }
          if (indexGambar1 == indexGambar2 && indexGambar2 == indexGambar3){
            jackpotSound.start();
            Toast.makeText(MainActivity.this, "Selamat Anda Mendapatkan Jackpot", Toast.LENGTH_SHORT).show();
          } else {
            failSound.start();
            Toast.makeText(MainActivity.this, "Maaf Anda Belum Berhasil", Toast.LENGTH_SHORT).show();
          }
          tambahDataset(indexGambar1, indexGambar2, indexGambar3);
          break;
      }
    }
  }

  private void buatThread(int indexAcakThread, int sleep){
    acakThread[indexAcakThread] = new Thread(new Runnable() {
      @Override
      public void run() {
        int i = 0;
        try {
          while (true) {
            int imageIndex = i % 9;
            Message m = handler[indexAcakThread].obtainMessage();
            Bundle b = new Bundle();
            if (indexAcakThread == 0){
              b.putInt("indexGambar1", imageIndex);
            } else if (indexAcakThread == 1) {
              b.putInt("indexGambar2", imageIndex);
            } else if (indexAcakThread == 2) {
              b.putInt("indexGambar3", imageIndex);
            }
            m.setData(b);
            handler[indexAcakThread].sendMessage(m);
            Thread.sleep(sleep);
            i++;
          }
        } catch (Exception e){}
      }
    });
  }

  private void buatHandler(int indexHandler){
    handler[indexHandler] = new Handler(Looper.getMainLooper()){
      @Override
      public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        Bundle b = msg.getData();
        if (indexHandler == 0){
          indexGambar1 = b.getInt("indexGambar1");
          ivSlot1.setImageResource(imageResource[indexGambar1]);
        } else if (indexHandler == 1) {
          indexGambar2 = b.getInt("indexGambar2");
          ivSlot2.setImageResource(imageResource[indexGambar2]);
        } else if (indexHandler == 2) {
          indexGambar3 = b.getInt("indexGambar3");
          ivSlot3.setImageResource(imageResource[indexGambar3]);
        }
      }
    };
  }

  private void tambahDataset(int indexGambar1, int indexGambar2, int indexGambar3){
    String id = appDB.push().getKey();
    History history = new History();
    history.setId(id);
    history.setSlot1(indexGambar1);
    history.setSlot2(indexGambar2);
    history.setSlot3(indexGambar3);
    history.setWinStatus(indexGambar1 == indexGambar2 && indexGambar2 == indexGambar3);
    appDB.child(id).setValue(history);
  }
}