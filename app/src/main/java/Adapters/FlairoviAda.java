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
import com.example.redditcloneandroid.model.Flair;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.retrofit.FlairServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlairoviAda extends RecyclerView.Adapter<FlairoviAda.MyViewHolder> {

    private List<Flair> flairList;
    private Context context;
    private int moderator;

    public FlairoviAda(List<Flair> flairList, Context context, int moderator){
        this.flairList = flairList;
        this.context = context;
        this.moderator = moderator;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pravila_prrozor, parent, false);


        return new FlairoviAda.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RetrofitServis retrofitServis = new RetrofitServis();
        FlairServis flairServis = retrofitServis.getRetrofit().create(FlairServis.class);
        Flair flair = flairList.get(position);

        holder.flairName.setText(flair.getNaziv());

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

                        EditText newFlairName = popupView.findViewById(R.id.newTitleForPost);
                        newFlairName.setHint("New flair name");
                        AppCompatButton submit = popupView.findViewById(R.id.btnSubmitChangeTitle);
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String newName = newFlairName.getText().toString();

                                flair.setNaziv(newName);
                                Call<Flair> updateFlair = flairServis.updateFlair(flair.getIdFlair(), flair);
                                updateFlair.enqueue(new Callback<Flair>() {
                                    @Override
                                    public void onResponse(Call<Flair> call, Response<Flair> response) {
                                        Toast.makeText(view.getContext(), "Successful change flair!", Toast.LENGTH_SHORT).show();
                                        popupWindow1.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<Flair> call, Throwable t) {
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
                        retrofit2.Call<ResponseBody> deleteFlair = flairServis.deleteFlair(flair.getIdFlair());
                        deleteFlair.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(view.getContext(), "Successful delete flair!", Toast.LENGTH_SHORT).show();
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
        return flairList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView flairName;
        ImageButton moreAction;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            flairName = itemView.findViewById(R.id.rulesTxt);
            moreAction = itemView.findViewById(R.id.moreActionIcon);

            Korisnik korisnik = JWTUtils.getCurrentUser();

            if(!(korisnik != null && korisnik.getIdKor() == moderator)){
                moreAction.setVisibility(View.GONE);
            }

        }
    }

}
