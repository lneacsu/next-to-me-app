package com.neal.android.upitplatforma;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
/**
 * Created by Loredana on 06.01.2019.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private List<Place> placeList;
    private static Context mContext;
    private static String type;
    private static final SparseBooleanArray array = new SparseBooleanArray();

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public TextView nume, adress, longitudine, place_id, url, nota_place, phone, la, lo;
        public CheckBox vizitat;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            nume = (TextView) view.findViewById(R.id.nume);
            adress = (TextView) view.findViewById(R.id.latitudine);
            longitudine = (TextView) view.findViewById(R.id.longitudine);
            vizitat = (CheckBox) view.findViewById(R.id.vizitat);
            place_id = (TextView) view.findViewById(R.id.place_id);
            url = (TextView) view.findViewById(R.id.url);
            nota_place = (TextView) view.findViewById(R.id.rating);
            phone = (TextView) view.findViewById(R.id.phone);
            la = (TextView) view.findViewById(R.id.lat);
            lo = (TextView) view.findViewById(R.id.longit);
        }


        @Override
        public void onClick(View view) {
            String locatie = (String) nume.getText();
            String adresa = (String) adress.getText();
            String distanta = (String) longitudine.getText();
            String id = (String) place_id.getText();
            String website = (String) url.getText();
            String nota = (String) nota_place.getText();
            String tel = (String) phone.getText();
            String lat = (String) la.getText();
            String lon = (String) lo.getText();
            Intent intent = new Intent(view.getContext(), Detalii.class);
            intent.putExtra("locatie", locatie);
            intent.putExtra("adresa", adresa);
            intent.putExtra("distanta", distanta);
            intent.putExtra("place_id", id);
            intent.putExtra("website", website);
            intent.putExtra("nota", nota);
            intent.putExtra("tel", tel);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            intent.putExtra("tipLoc", type);
            view.getContext().startActivity(intent);

        }
    }

    public PlacesAdapter(List<Place> placesList, Context mContext, String tipLocatie) {
        this.placeList = placesList;
        this.mContext = mContext;
        this.type = tipLocatie;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final int pos = position;
        final ViewHolder holderview = holder;
        Place place = placeList.get(position);
        holder.nume.setText(place.getName());
        holder.adress.setText(place.getAddress());
        holder.longitudine.setText((Math.round(place.getDistance() * 100.0) / 100.0) + "km");
        holder.vizitat.setChecked(placeList.get(position).isSelected());
        holder.vizitat.setTag(placeList.get(position));
        holder.place_id.setText(place.getPlace_id());
        holder.nota_place.setText(place.getRating());
        holder.la.setText(place.getmLatitudine().toString());
        holder.lo.setText(place.getmLongitudine().toString());


        holder.vizitat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View view = v;
                CheckBox cb = (CheckBox) v;
                Place place = (Place) cb.getTag();
                place.setSelected(cb.isChecked());
                placeList.get(pos).setSelected(cb.isChecked());

                if (cb.isChecked()) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent(view.getContext(), Activity_register.class);
                                    intent.putExtra("nume", holderview.nume.getText().toString());
                                    intent.putExtra("adresa", holderview.adress.getText().toString());
                                    intent.putExtra("pozitieItem", position);
                                    view.getContext().startActivity(intent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;

                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Bun Venit!");
                    builder.setMessage("Adaugati locatie in jurnal de calatorie?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

}

