package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.R;

public class OdgNaPostAd extends RecyclerView.Adapter<OdgNaPostAd.MyViewHolder> {

    String data1[], data2[], data3[], data4[];
    Context context;

    public OdgNaPostAd(Context ct, String usernamePost[], String communityPost[], String titlePost[], String textPost[]){
        this.context = ct;
        this.data1 = usernamePost;
        this.data2 = communityPost;
        this.data3 = titlePost;
        this.data4 = textPost;
    }

    @NonNull
    @Override
    public OdgNaPostAd.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.priajvljeni_post_prozor, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OdgNaPostAd.MyViewHolder holder, int position) {
        holder.myText1.setText(data1[position]);
        holder.myText2.setText(data2[position]);
        holder.myText3.setText(data3[position]);
        holder.myText4.setText(data4[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2, myText3, myText4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.username);
            myText2 = itemView.findViewById(R.id.zajednica);
            myText3 = itemView.findViewById(R.id.postTitle);
            myText4 = itemView.findViewById(R.id.postText);
        }
    }
}
