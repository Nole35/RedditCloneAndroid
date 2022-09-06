package Adapters;

import android.app.Service;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.HomeActivity;
import com.example.redditcloneandroid.R;
import com.example.redditcloneandroid.model.Ban;
import com.example.redditcloneandroid.model.Zajednica;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.retrofit.BanServis;
import com.example.redditcloneandroid.retrofit.ZajednicaServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UkiniZajednicu extends RecyclerView.Adapter<UkiniZajednicu.MyViewHolder> {

    private List<Zajednica> zajednicaList;
    private Context context;

    public UkiniZajednicu(List<Zajednica> zajednicaList, Context context) {

        this.zajednicaList = zajednicaList;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blokiranje_prozor, parent, false);

        return new UkiniZajednicu.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RetrofitServis retrofitServis = new RetrofitServis();
        ZajednicaServis zajednicaServis = retrofitServis.getRetrofit().create(ZajednicaServis.class);
        BanServis banServis = retrofitServis.getRetrofit().create(BanServis.class);


        Zajednica zajednica = zajednicaList.get(position);

        holder.communityName.setText(zajednica.getNaziv());

        holder.blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.izmena_naslova_posta_prozor, null);

                int width = 600;
                int height = 500;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                EditText suspendReasonTxt = popupView.findViewById(R.id.newTitleForPost);
                suspendReasonTxt.setHint("SUSPEND REASON");

                AppCompatButton submitSuspendCommunity = popupView.findViewById(R.id.btnSubmitChangeTitle);
                submitSuspendCommunity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Korisnik korisnik = JWTUtils.getCurrentUser();
                        String suspendReason = suspendReasonTxt.getText().toString();

                        Ban ban = new Ban();
                        ban.setBanKor(korisnik.getIdKor());
                        ban.setZajednica(zajednica.getIdZaj());
                        ban.setRazlog(suspendReason);

                        Call<Ban> bannedCommunity = banServis.createBan(ban);
                        bannedCommunity.enqueue(new Callback<Ban>() {
                            @Override
                            public void onResponse(Call<Ban> call, Response<Ban> response) {
                                Toast.makeText(view.getContext(), "Successful banned community!", Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();

                                Call<ResponseBody> deleteCommunity = zajednicaServis.deleteCommunity(zajednica.getIdZaj());
                                deleteCommunity.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast.makeText(view.getContext(), "Successful delete community!", Toast.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<Ban> call, Throwable t) {
                                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });




                    }
                });

                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return zajednicaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView communityName;
        AppCompatButton blockButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            communityName = itemView.findViewById(R.id.itemNameForBlock);
            blockButton = itemView.findViewById(R.id.buttonBlock);
        }
    }

}
