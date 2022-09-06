package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.ZajednicaA;
import com.example.redditcloneandroid.R;
import com.example.redditcloneandroid.model.Zajednica;

import java.util.List;

public class ZajedniceAd extends RecyclerView.Adapter<ZajedniceAd.MyViewHolder>{

    private List<Zajednica> zajednicaList;
    private Context context;

    public ZajedniceAd(List<Zajednica> zajednicaList, Context context) {

        this.zajednicaList = zajednicaList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zajednica_prozor, parent, false);

        TextView communityTxtBtn = view.findViewById(R.id.communityName);
        communityTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String communityName = communityTxtBtn.getText().toString();

                Intent intent= new Intent(context, ZajednicaA.class);
                intent.putExtra("communityName",communityName);
                context.startActivity(intent);
            }
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZajedniceAd.MyViewHolder holder, int position) {

        Zajednica zajednica = zajednicaList.get(position);

        String communityId = zajednica.getNaziv();

        holder.communityName.setText(communityId);

    }

    @Override
    public int getItemCount() {
        return zajednicaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView communityName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            communityName = itemView.findViewById(R.id.communityName);
        }
    }
}
