package Adapters;

import android.app.Service;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.R;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Pravilo;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.retrofit.RetrofitServis;
import com.example.redditcloneandroid.retrofit.PraviloServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PravilaAd extends RecyclerView.Adapter<PravilaAd.MyViewHolder>{

    private List<Pravilo> praviloList;
    private Context context;
    private int moderator;

    public PravilaAd(List<Pravilo> praviloList, Context context, int moderator){
        this.praviloList = praviloList;
        this.context = context;
        this.moderator = moderator;
    }

    @NonNull
    @Override
    public PravilaAd.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pravila_prrozor, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PravilaAd.MyViewHolder holder, int position) {
        RetrofitServis retrofitServis = new RetrofitServis();
        PraviloServis praviloServis = retrofitServis.getRetrofit().create(PraviloServis.class);
        Pravilo pravilo = praviloList.get(position);

        holder.ruleDescription.setText(pravilo.getoPravilu());

        holder.moreAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.azuriranje_prozor, null);

                int width = 320;
                int height = 220;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                AppCompatButton change = popupView.findViewById(R.id.changeData);
                AppCompatButton delete = popupView.findViewById(R.id.deleteData);

                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        popupWindow.dismiss();
                        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                        View popupView = inflater.inflate(R.layout.izmena_naslova_posta_prozor, null);

                        int width = 600;
                        int height = 500;
                        boolean focusable = true;
                        final PopupWindow popupWindow1 = new PopupWindow(popupView, width, height, focusable);
                        popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);

                        EditText newRuleDescription = popupView.findViewById(R.id.newTitleForPost);
                        newRuleDescription.setHint("New rule description");
                        AppCompatButton submit = popupView.findViewById(R.id.btnSubmitChangeTitle);
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String newDescription = newRuleDescription.getText().toString();
                                pravilo.setoPravilu(newDescription);
                                Call<Pravilo> updateRule = praviloServis.updateRule(pravilo.getIdPra(), pravilo);
                                updateRule.enqueue(new Callback<Pravilo>() {
                                    @Override
                                    public void onResponse(Call<Pravilo> call, Response<Pravilo> response) {
                                        Toast.makeText(view.getContext(), "Successful change rule!", Toast.LENGTH_SHORT).show();
                                        popupWindow1.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<Pravilo> call, Throwable t) {
                                        Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
                                    }
                                });

                            }
                        });

                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        retrofit2.Call<ResponseBody> deleteRule = praviloServis.deleteRule(pravilo.getIdPra());
                        deleteRule.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(view.getContext(), "Successful delete rule!", Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
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
        return praviloList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ruleDescription;
        ImageButton moreAction;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ruleDescription = itemView.findViewById(R.id.rulesTxt);
            moreAction = itemView.findViewById(R.id.moreActionIcon);

            Korisnik korisnik = JWTUtils.getCurrentUser();

            if(!(korisnik != null && korisnik.getIdKor() == moderator)){
                moreAction.setVisibility(View.GONE);
            }

        }
    }
}
