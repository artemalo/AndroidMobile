package com.example.walkofinterest.models.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkofinterest.R;
import com.example.walkofinterest.interfaces.CallBackIndexRoute;

import java.util.ArrayList;

public class RouteInfoRVAdapter extends RecyclerView.Adapter<RouteInfoRVAdapter.MyViewHolder> {
    CallBackIndexRoute callBackIndexRoute;
    Context context;
    ArrayList<RouteInfoModel> routeInfoModels;

    public RouteInfoRVAdapter(Context context, ArrayList<RouteInfoModel> routeInfoModels) {
        this.context = context;
        this.routeInfoModels = routeInfoModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_view_route_info, parent, false);

        return new RouteInfoRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.index.setText(routeInfoModels.get(position).getIndex());
        holder.time.setText(routeInfoModels.get(position).getTime());
        holder.countSteps.setText(routeInfoModels.get(position).getCountSteps());
        holder.constraintLayout.setBackground(routeInfoModels.get(position).getColor());

        holder.indexShowRoute.setOnClickListener(v-> {
            Log.d("Click", "indexShowRoute: " + routeInfoModels.get(position).getIndex());
            callBackIndexRoute.ClickIndex(routeInfoModels.get(position).getIndexInt());
        });

        holder.view.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return routeInfoModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView index;
        TextView time;
        TextView countSteps;
        ConstraintLayout constraintLayout;
        ConstraintLayout indexShowRoute;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;

            index = itemView.findViewById(R.id.indexRouteInfo);
            time = itemView.findViewById(R.id.timeRouteInfo);
            countSteps = itemView.findViewById(R.id.countStepsRouteInfo);
            constraintLayout = itemView.findViewById(R.id.route);
            indexShowRoute = itemView.findViewById(R.id.indexShowRoute);
        }
    }

    public void setCallBackIndexRoute(CallBackIndexRoute callBackIndexRoute) {
        this.callBackIndexRoute = callBackIndexRoute;
    }
}
